3-Adapt A11y is an optional compatibility companion for AutoJs6: it enables an accessibility service that handles no events, which helps AutoJs6 retrieve more normal accessibility nodes from apps covered by published support profiles. Automation is still performed by the AutoJs6 accessibility service, and the plugin neither reads screen content nor uses the network.

### Getting started

1. Install AutoJs6 and 3-Adapt A11y, open the plugin, and tap the "Compatibility service" status card to open "Compatibility service manager".
2. The default "Control policy", "Follow AutoJs6", enables the compatibility service whenever the AutoJs6 accessibility service is enabled; choose "Disabled" when it is not needed.
3. Without root access, WRITE_SECURE_SETTINGS or Shizuku, tap "System accessibility settings" in "Settings" and enable both the AutoJs6 service and 3-Adapt A11y by hand.
4. Fully exit and reopen a target app listed under "Currently supported apps", then read the page nodes with AutoJs6.

### Main features

- **Compatibility service manager**: see the current state of the compatibility service and the AutoJs6 service under "State", and choose "Follow AutoJs6", "Enabled" or "Disabled" under "Control policy".
- **Automatic control**: after you grant root access, WRITE_SECURE_SETTINGS or Shizuku, turn on the matching option under "Automatic control" and the plugin enables or disables the service by the selected policy, changing only the list of enabled accessibility services.
- **Stopped service repair**: when the system settings show the service as on but it no longer runs, the plugin turns it off and on again in the same way; when it cannot, the home page shows "Compatibility service is not running" with a "System settings" button.
- **Support profiles**: "Currently supported apps" on the home page lists the verified apps, which is only WeChat in this release; a profile applies only to the documented app versions and pages and does not cover every environment.
- **Privacy**: the service callback does nothing and ignores every accessibility event; the plugin does not read event content, root nodes, screenshots or user content, stores and uploads no data, and has no network access.
- **Appearance and information**: in "Settings", adjust "Language", "Dark mode", "Theme color" and "Launcher icon", and read "Compatibility mechanism", "Privacy", "Limitations" and "Release history".

### Requirements

- Android 7.0 (API 24) or later, and AutoJs6 6.8.0 or later; the AutoJs6 accessibility service reads nodes and performs actions.
- The accessibility service of this plugin is enabled only as a compatibility identifier; it receives events only from apps in the support profiles and does not process them.
- Root access, WRITE_SECURE_SETTINGS and Shizuku are optional and used only to enable or disable the service automatically; WRITE_SECURE_SETTINGS is granted through ADB or a root shell.
- The permission to query Advanced Protection mode checks whether Android 17 "Advanced Protection" restricts this service; the result is shown in "Settings".

### FAQ

**Nodes do not change after the service is enabled**

First make sure both the AutoJs6 service and 3-Adapt A11y are on, then fully exit and reopen the target app. The result depends on the app version, page, device and system and is not guaranteed; controls in WebView, Canvas and custom-drawn interfaces may stay unavailable.

**The service is on in the system settings, but the home page shows "Compatibility service is not running"**

Android does not bring the service back after its process is killed. With root access, WRITE_SECURE_SETTINGS or Shizuku the plugin repairs it automatically; otherwise tap "System settings" and turn 3-Adapt A11y off and on again.

**The service class name matches Google Select to Speak**

Some apps decide whether to expose normal nodes from the class name of an enabled service, and the plugin uses that class name only as a disclosed compatibility identifier. This plugin is not Google Select to Speak, is not affiliated with Google, and uses no Google application ID, signature, name or icon.

Support profiles, the compatibility mechanism and validation records are on the [project page](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y).
