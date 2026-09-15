# Compatibility service control

This note explains how the companion keeps its accessibility service aligned with the policy the
user selected, which privileged paths it may use, and what it never does. It is the reference for
the `WRITE_SECURE_SETTINGS` and Shizuku permissions declared in the manifest.

## Policy

`CompatSettings.servicePolicy` holds one of three values, mirrored by
`AutoJs6AccessibilityCompanionContract.SERVICE_POLICY_*` in the shared plugin API:

| Policy | Stored name | Contract value | Meaning |
| --- | --- | --- | --- |
| Follow AutoJs6 (default) | `FOLLOW_HOST` | `follow-host` | The compatibility service is enabled exactly while any accessibility service of `org.autojs.autojs6` is enabled |
| Enabled | `ENABLED` | `enabled` | Keep the compatibility service enabled |
| Disabled | `DISABLED` | `disabled` | Keep the compatibility service disabled |

`ServiceControlPolicy.decide(policy, hostServiceEnabled, serviceEnabled)` is the only place that turns
the policy and the observed state into an `ENABLE`, `DISABLE`, or `NONE` decision. It is pure and
unit tested.

The selected policy is also published through the plugin-info capabilities as
`org.autojs.plugin.accessibility.compat.SERVICE_POLICY` (contract version 3), so that a host which
implements the companion contract can perform the same attach/detach from its side.

## Triggers

`CompatServiceSync.request()` runs `CompatServiceController.sync()` on one background thread. It is
invoked by:

- `HostServiceStateReceiver`: the host broadcast
  `org.autojs.plugin.action.ACCESSIBILITY_SERVICE_STATE_CHANGED`, protected by
  `org.autojs.permission.PLUGIN`, sent by AutoJs6 when its own service binds or unbinds.
- `SecureSettingsWatchJob`: a JobScheduler content-triggered job on
  `Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES`, scheduled only while the policy is
  `FOLLOW_HOST` and re-armed after every run (content-triggered jobs are one-shot and cannot be
  persisted; the wake activity and the receiver re-arm it after a reboot).
- `AccessibilityCompatService`: while bound, a `ContentObserver` on the same setting and the
  `onServiceConnected` callback.
- `WakeActivity` (`org.autojs.plugin.action.WAKE`) and `MainActivity.onResume`.

Passive triggers never open UI. The manager dialog calls `sync()` itself so that it can show the
outcome as a toast and fall back to the system accessibility settings when no method is available.

## Methods

`CompatServiceController` changes only the `enabled_accessibility_services` secure setting. It never
writes `accessibility_enabled` or any other setting. The strategies are tried in this order, each
skipped when its switch in the manager is off or the capability is missing:

1. Root: `su -c settings put secure enabled_accessibility_services '<value>'`.
2. Secure settings: `Settings.Secure.putString` with `WRITE_SECURE_SETTINGS`, which the user grants
   with `adb shell pm grant io.github.supermonster003.autojs6.plugin.accessibilitycompat android.permission.WRITE_SECURE_SETTINGS`
   (the manager shows and copies this command).
3. Shizuku: a user service (`ShizukuShellService`) that runs `settings get|put` with the shell
   identity, bound through `Shizuku.bindUserService` after the user granted the Shizuku permission.

Every write goes through `EnabledAccessibilityServices.attach/detach`, which removes every service of
this package before appending the component (the detach-then-attach cycle also revives a service that
is listed but not running), and `SettingsStoreStrategy` reads the setting back before reporting
success.

Disabling prefers `AccessibilityService.disableSelf()` when the service is bound because that needs
no privilege; the strategies cover a listed but unbound service.

## What stays unchanged

- The accessibility callback remains a no-op; nothing in this feature reads events or nodes.
- No networking, analytics, storage, overlay, microphone, or camera permission is added.
- The AutoJs6 service is never enabled or disabled by the companion; following is one-directional.
- Without root, `WRITE_SECURE_SETTINGS`, or Shizuku the companion behaves as before: the user
  toggles the service in system settings.

## Host side

AutoJs6 discovers companions through `org.autojs.plugin.INFO` services carrying the category
`org.autojs.plugin.category.ACCESSIBILITY_COMPANION`, reads `SERVICE_POLICY` and `SERVICE_COMPONENT`
from the capabilities of official-signed plugins, attaches or detaches `follow-host` companions with
its own strategies when its service binds or unbinds, and broadcasts the state change to each
companion package. Older hosts that lack this logic still work: the companion follows through the
settings watcher and the wake activity.
