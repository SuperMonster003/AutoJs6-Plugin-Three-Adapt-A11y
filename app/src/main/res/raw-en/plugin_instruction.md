# AutoJs6 Accessibility Compat

## Purpose

This is an optional experimental companion. It may help AutoJs6 retrieve more normal accessibility nodes from apps covered by published support profiles. Automation is still performed by the AutoJs6 accessibility service.

## Enable the service

1. Install AutoJs6 and this companion.
2. Open the companion and tap the status card to open the compatibility service manager. The default policy, Follow AutoJs6, keeps the compatibility service enabled whenever the AutoJs6 accessibility service is enabled.
3. Without root access, WRITE_SECURE_SETTINGS, or Shizuku, manually enable both the AutoJs6 service and AutoJs6 Accessibility Compat in Android accessibility settings. Once one of them is granted, the companion applies the selected policy itself.
4. Fully exit and reopen a target app from the support list, then inspect the same static page several times.
5. Select the Disabled policy in the manager, or disable this service in system settings, when compatibility is not needed.

Android does not let an ordinary app silently enable an accessibility service. This companion changes the service automatically only after you explicitly grant root access, WRITE_SECURE_SETTINGS, or Shizuku, and it modifies only the list of enabled accessibility services.

## Experimental compatibility mechanism

Some affected apps may decide whether to expose normal nodes based on the implementation class name of an enabled service. This companion contains the following experimentally verified compatibility class name required by its current support profiles.

`com.google.android.accessibility.selecttospeak.SelectToSpeakService`

This companion is not Google Select to Speak, is not affiliated with Google, and does not copy a Google application ID, signature, name, or icon. The class name is used only as a disclosed compatibility identifier and may stop working after a target app update.

## Current support profiles

- WeChat (`com.tencent.mm`) is the only profile verified in this release.
- A profile applies only to documented app versions and pages. Inclusion does not guarantee every environment.

## Privacy

- The service callback is a no-op and ignores every accessibility event.
- The companion does not read event sources, event text, root nodes, screenshots, or user content.
- The companion does not store, share, or upload data and has no network access.
- The service receives events only from package IDs in the published support profiles.

AutoJs6 continues to read and act on nodes through its own accessibility service. This companion does not proxy a node tree or perform automation actions.

## Limitations and risks

Compatibility depends on the target app version, page, device, and system implementation. Success is not guaranteed. This companion cannot create semantic nodes that a page does not expose. Controls in WebView, Canvas, and custom-drawn interfaces may remain unavailable.

A target app may change its detection behavior and make this companion ineffective. Automation may also be subject to the target platform rules or account risk controls. Evaluate the risk on your own device and account.
