# AutoJs6 WeChat Accessibility Compat

## Purpose

This is an optional experimental companion. It may help AutoJs6 retrieve more normal accessibility nodes from affected WeChat versions. Automation is still performed by the AutoJs6 accessibility service.

## Enable the service

1. Install AutoJs6 and this companion.
2. Manually enable both the AutoJs6 service and AutoJs6 WeChat Accessibility Compat in Android accessibility settings.
3. Fully exit and reopen WeChat, then inspect the same static page several times.
4. Disable this service in system settings when compatibility is not needed.

Android does not let an ordinary app silently enable an accessibility service. This companion does not attempt to bypass that restriction.

## Experimental compatibility mechanism

Some WeChat versions may decide whether to expose normal nodes based on the implementation class name of an enabled service. This companion contains the following experimentally verified compatibility class name.

`com.google.android.accessibility.selecttospeak.SelectToSpeakService`

This companion is not Google Select to Speak, is not affiliated with Google, and does not copy a Google application ID, signature, name, or icon. The class name is used only as a disclosed compatibility identifier and may stop working after a WeChat update.

## Privacy

- The service callback is a no-op and ignores every accessibility event.
- The companion does not read event sources, event text, root nodes, screenshots, or user content.
- The companion does not store, share, or upload data and has no network access.
- The service is configured only for the WeChat package `com.tencent.mm`.

AutoJs6 continues to read and act on nodes through its own accessibility service. This companion does not proxy a node tree or perform automation actions.

## Limitations and risks

Compatibility depends on the WeChat version, device, and system implementation. Success is not guaranteed. This companion cannot create semantic nodes that a page does not expose. Controls in Mini Programs, XWeb, Canvas, and custom-drawn interfaces may remain unavailable.

WeChat may change its detection behavior and make this companion ineffective. Automation may also be subject to WeChat rules or account risk controls. Evaluate the risk on your own device and account.
