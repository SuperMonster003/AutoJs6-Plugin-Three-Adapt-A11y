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

`ServiceControlPolicy.decide(policy, hostServiceEnabled, serviceEnabled, serviceRunning)` is the only
place that turns the policy and the observed state into an `ENABLE`, `DISABLE`, `REPAIR`, or `NONE`
decision. It is pure and unit tested.

`REPAIR` means the service should be on, is listed in `enabled_accessibility_services`, but is not
bound. After its process is killed (not force-stopped) the system may record the service as crashed
and leave it listed without binding it again until it leaves the list and comes back. Because a
freshly listed service is unbound for a moment, `CompatServiceController.sync()` waits up to 3 s
(`BindingGrace`, polling every 250 ms) and decides again before repairing. A repair is not repeated
within 60 s (`RepairThrottle`, stored in the `compat_service_repair` preferences and cleared when
the service connects), so a service the system refuses to bind for another reason is left to the
user instead of being rewritten by every settings change the repair itself causes.

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
- `ThreeAdaptA11yService`: while bound, a `ContentObserver` on the same setting and the
  `onServiceConnected` callback.
- `WakeActivity` (`org.autojs.plugin.action.WAKE`) and `MainActivity.onResume`.
- `ShizukuShell.runOnceWhenBinderReceived` (trigger `shizuku-binder`): when a pass found no usable
  method while Shizuku is allowed in the manager but its binder has not arrived yet (a process just
  started by the host broadcast or the watcher job receives it a moment later), one more pass runs
  when the binder arrives. Shizuku that is not started never delivers one, and a pass that fails with
  Shizuku reachable does not register again.

Passive triggers never open UI. The manager dialog calls `sync()` itself so that it can show the
outcome as a toast and fall back to the system accessibility settings when no method is available.

## Methods

`CompatServiceController` changes only the `enabled_accessibility_services` secure setting. It never
writes `accessibility_enabled` or any other setting. The strategies are tried in this order, each
skipped when its switch in the manager is off or the capability is missing:

1. Root: `su -c settings put secure enabled_accessibility_services '<value>'`.
2. Secure settings: `Settings.Secure.putString` with `WRITE_SECURE_SETTINGS`, which the user grants
   with `adb shell pm grant io.github.supermonster003.autojs6.plugin.three.adapt.a11y android.permission.WRITE_SECURE_SETTINGS`
   (the manager shows and copies this command).
3. Shizuku: a user service (`ShizukuShellService`) that runs `settings get|put` with the shell
   identity, bound through `Shizuku.bindUserService` after the user granted the Shizuku permission.

Every write goes through `EnabledAccessibilityServices.attach/detach`, which removes every service of
this package before appending the component (the detach-then-attach cycle also revives a service that
is listed but not running), and `SettingsStoreStrategy` reads the setting back before reporting
success. When the component is already listed, the strategy writes the list without it, waits 1 s
(`SettingsStoreStrategy.RELIST_GAP_MILLIS`), and writes it back: two `Settings.Secure.putString`
calls a few milliseconds apart reach the accessibility manager as one change, so the service stayed
crashed (measured on ColorOS 15: 3 ms apart failed every time, 300 ms and 1 s always worked).

When `REPAIR` finds no method, `MainActivity` shows a card that asks the user to turn the service
off and on again in the system accessibility settings, with a button that opens them; the card
disappears once the service runs again.

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

From 6.8.1 the host also watches `enabled_accessibility_services` (and, on Android 13 and later,
the set of bound services) while its process lives. When a `follow-host` companion was force-stopped
(removed from the list and its package stopped) or killed (listed but not bound) while the host
service is on, the host writes it back or repairs it with its own strategies, and broadcasts the
state change so that the companion can do it itself when the host has no method.
