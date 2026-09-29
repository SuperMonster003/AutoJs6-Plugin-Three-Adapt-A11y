<!-- This file is generated. Edit .readme/lang_*.json and rerun .python/generate_markdown.py. -->
<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-three-adapt-a11y-ic-launcher" border="0" width="128" />
    </picture>
  </p>
  <h1>3-Adapt A11y</h1>
  <p>A privacy-minimal accessibility compatibility trigger for supported apps</p>
  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/issues"><img alt="GitHub issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y?color=534BAE&label=License"/></a>
  </p>
</div>

> Page language: English

### Languages

[简体中文](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hans.md) | [香港繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hant-HK.md) | [台灣繁體](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-zh-Hant-TW.md) | [English](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-en.md) | [Français](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-fr.md) | [Español](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-es.md) | [日本語](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ja.md) | [한국어](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ko.md) | [Русский](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ru.md) | [العربية](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/.readme/README-ar.md)

### Project Status

3-Adapt A11y is a standalone experimental companion APK. It attempts to help apps covered by published support profiles expose their control trees to the AutoJs6 accessibility service. It is not a general automation engine and never replaces AutoJs6 as the reader or operator of controls.

> Compatibility depends on each target app version, page, device, and remote configuration. The project cannot promise success in every environment. Complete A-B-A validation before relying on it in a script.

### No-op Companion Architecture

The project isolates application compatibility profiles in a separate APK and leaves the name and general behavior of the AutoJs6 core service unchanged:

- The AutoJs6 accessibility service remains the only component that reads, queries, and acts on nodes.
- The companion registers a service implementation class name supported by public experiments, while its application ID, icon, labels, description, and signature truthfully identify 3-Adapt A11y.
- The compatibility service callbacks are no-op. They never read `event.source`, `event.text`, `rootInActiveWindow`, screenshots, or page content.
- This release is not a node proxy or Binder bridge. It only attempts to trigger the conditional node-exposure behavior documented by its support profiles.

### Installation and Use

1. Confirm that the device runs Android 7.0 (API 24) or later and that the AutoJs6 internal build is at least 3923.
2. Install the APK only from this project's [Releases](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/releases) or a trusted AutoJs6 plugin entry.
3. Open 3-Adapt A11y, verify its real name and purpose, then tap the status card to open the compatibility service manager. The default policy, Follow AutoJs6, keeps the service enabled only while the AutoJs6 accessibility service is enabled.
4. Without root access, WRITE_SECURE_SETTINGS, or Shizuku, enable the accessibility service listed under 3-Adapt A11y manually in Android settings; Android's risk warning is expected. With one of them granted on purpose, the app applies the selected policy itself and touches no other setting.
5. Keep the AutoJs6 accessibility service enabled as well, reopen a target page listed in a support profile, and inspect nodes from the AutoJs6 layout inspector or a script.

Installing the APK alone has no effect. Select the Disabled policy or disable the compatibility service in Android settings when it is not needed, or uninstall the app when finished.

### Settings and Service Control

The settings screen groups generic options that can follow AutoJs6, and the compatibility service manager decides when the service runs:

- Language, dark mode, and theme color default to Follow AutoJs6 and are read from the AutoJs6 settings contract; without AutoJs6 they fall back to the system or built-in values.
- The control policy is a three-way choice: Follow AutoJs6 (default), Enabled, or Disabled. Follow AutoJs6 keeps the compatibility service in the same state as the AutoJs6 accessibility service and re-checks it when AutoJs6 reports a change, when the system list of enabled services changes, and when the app opens.
- Automatic changes use root, WRITE_SECURE_SETTINGS (granted with `adb shell pm grant`), or Shizuku, and each method can be switched off. Without any of them the app only opens the system accessibility settings.
- The manager shows the live state of both services and of each method, copies a diagnostic report, and opens the Android settings page of the service.
- Launcher icon can be set to adaptive light, adaptive dark (default), adaptive automatic or transparent background. Automatic mode tries to follow the system theme, but launchers may cache a single color scheme; transparent icons may receive a launcher background or mask. Switching preserves the running app and may take a few seconds to appear.

### A-B-A Device Validation

Do not treat one successful dump as proof. Run an A-B-A comparison on the same static page before attributing a change to this service:

- A: Keep AutoJs6 enabled and the compatibility service disabled, then collect at least 5 sanitized samples.
- B: Enable only the compatibility service, return to the same page, and collect at least 5 more samples.
- A2: Disable the compatibility service again and repeat collection. The change should reverse, ruling out loading and cache accidents.
- Record node counts, non-empty text/desc/resource-id counts, clickable counts, and a structure hash. Never retain chat text, contact names, or screenshots.

[Read the complete A-B-A validation protocol](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/aba-device-validation.md)

### Verified Device Result

On 2026-09-02, a reversible A-B-A run completed on the authorized Sony XQ-AT72 with Android 12/API 31, WeChat 8.0.72 code 3085, and AutoJs6 6.8.0 code 5277. The same 6 pre-existing accessibility services remained unchanged. Each phase force-stopped and relaunched WeChat LauncherUI, waited more than 5 seconds, then took 7 samples through AutoJs6 itself:

- A with compatibility off: nodes 1, text 0, desc 0, id 0, clickable 0, with the same structure hash in all 7 samples.
- B with compatibility on and both target services bound: nodes 244, text 31, desc 13, id 157, clickable 38, with the same structure hash in all 7 samples.
- A2 with compatibility off again: exactly returned to nodes 1 and 0 for every other metric, with the same structure hash in all 7 samples. System accessibility settings were restored exactly afterward.

This verifies the compatibility trigger for that device, WeChat build, and LauncherUI page combination. It cannot be generalized to other versions, accounts, devices, Mini Programs, XWeb, or Canvas pages.

[Read the complete sanitized QV710AF65F report](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/device-QV710AF65F.md)

### Known Limitations

- Current support profiles contain only WeChat (`com.tencent.mm`). This is an explicit support set, not a claim of universal compatibility.
- WebView, mini-app, Canvas, and custom-drawn controls may have no native semantic nodes. The plugin cannot invent missing `text` or `content-desc`.
- Even when a root returns, some pages may expose empty attributes, stale nodes, random trees, or bounds only.
- A target app update or remote configuration change can invalidate a profile at any time. Downgrades, OCR, and coordinate fallbacks carry their own safety and stability costs.
- The plugin only addresses observability. It does not bypass login, risk controls, captchas, permissions, account restrictions, or platform anti-abuse systems.
- Android 17 Advanced Protection can restrict accessibility services that are not accessibility tools, including this compatibility service. The global mode alone does not establish whether this service is blocked. Check the actual service state and Android accessibility settings. Granting secure settings access does not bypass system restrictions.

### Privacy Boundary

- Compatibility callbacks do not read event sources, event text, the active-window root, or screen images.
- The app does not upload, persist, or log target-app page content and contains no networking or analytics feature.
- The app requests no storage, overlay, camera, microphone, or media permission. It declares WRITE_SECURE_SETTINGS and the Shizuku permission only for optional service control, and both stay inactive until you grant them.
- The plugin-info Binder reports only version, identity, and capability metadata. It never transfers a node tree.
- The compatibility service changes only according to the control policy you selected, through root, secure settings, or Shizuku when you allowed them. The app never modifies any other system setting and never enables the AutoJs6 service.

### Ethics and Compliance

Accessibility compatibility should only help users automate interfaces they are authorized to operate. The user remains responsible for script behavior and account consequences.

- Use it only on your own device, account, and workflows for which you have explicit authorization.
- Do not use it for harassment, spam, non-consensual data collection, surveillance, or bypassing security controls.
- Follow applicable law, target-platform rules, and organizational policy, with human confirmation and stop conditions for UI changes and mistakes.
- Share only aggregate diagnostics and sanitized structures. Never publish chats, contacts, tokens, APKs, or raw dex files.

### Compatibility Information

The service component contains a compatibility class name used in public experiments. It is not Google Select to Speak, provides no text-to-speech feature, and does not imitate a Google app or signature. The real identity remains visible through this project's application ID, labels, icon, information page, and signature.

```text
application id: io.github.supermonster003.autojs6.plugin.three.adapt.a11y
accessibility service: io.github.supermonster003.autojs6.plugin.three.adapt.a11y/com.google.android.accessibility.selecttospeak.SelectToSpeakService
supported packages: com.tencent.mm
minimum Android: Android 7.0 (API 24)
minimum AutoJs6 build: 3923
```

### FAQ

#### Does this project impersonate a Google app?

No. Only the accessibility service implementation class name is used for the compatibility experiment. The package, app and service labels, icon, documentation, and signature remain truthful, and the project explicitly says it is not Google Select to Speak.

#### Why did nothing change after installation?

Without root access, WRITE_SECURE_SETTINGS, or Shizuku, Android does not let an app enable its own accessibility service, so enable the compatibility service in system settings and also enable AutoJs6. With one of them available, the compatibility service manager applies the selected policy for you. If nothing changes, use the A-B-A protocol to determine whether the current target-app version and page are supported.

#### Why are text nodes still missing on a supported page?

A WebView, mini-app, Canvas, or custom-rendered page may have no corresponding Android semantic nodes. The service can only try to restore a conditionally hidden tree. It cannot generate information that the page never exposes.

#### Does the plugin guarantee account safety?

No. It does not bypass target-platform risk controls and cannot promise that any automation behavior is safe. Use low-risk, auditable scripts with human confirmation and follow platform rules yourself.

### Research Basis

The research note covers AutoJs6 #289, #382, #432, #463, #520, and #521, GKD `47267c7`, and sanitized static evidence from WeChat 8.0.72 on the test device. It separates public facts, reproducible experiments, and inference instead of presenting WeChat internals as a public guarantee.

[Read the accessibility service identity compatibility research note](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/research/accessibility-service-identity-compat.md)

### Release History

#### v1.4.0 - 2026/09/29

##### Hint

- The application ID changed from `io.github.supermonster003.autojs6.plugin.accessibilitycompat` to `io.github.supermonster003.autojs6.plugin.three.adapt.a11y`, so Android treats this release as a new app: uninstall Accessibility Compat 1.3.1 or earlier first, then enable the 3-Adapt A11y accessibility service again

##### Feature

- Launcher icon can be set to adaptive light, adaptive dark (default), adaptive automatic or transparent background. Automatic mode tries to follow the system theme, but launchers may cache a single color scheme; transparent icons may receive a launcher background or mask. Switching preserves the running app and may take a few seconds to appear.

##### Improvement

- Rename the plugin from Accessibility Compat to 3-Adapt A11y across the application title, accessibility service label, plugin ID `three-adapt-a11y`, package and component names, release artifacts, documentation, and the GitHub repository

[Read the complete release history](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/changelog/CHANGELOG-en.md)

### Build and Documentation Checks

Regular users should install a prebuilt APK from Releases. Developers can use the repository Gradle Wrapper to build and verify the project:

```powershell
.\gradlew.bat :app:testDebugUnitTest :app:assembleDebug :app:assembleDebugAndroidTest :app:lintDebug
py .python\generate_markdown.py --check
```

README and changelog files are generated from JSON copy sources. After changing `.readme/lang_*.json`, `.changelog/lang_*.json`, or a template, run:

```powershell
py .python\generate_markdown.py
py .python\generate_markdown.py --check
```

### License

Project code is licensed under the [Mozilla Public License 2.0](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/LICENSE). WeChat, Google, and Select to Speak names belong to their respective owners. This project is not affiliated with or endorsed by those companies.

### Links

- [AutoJs6](https://github.com/SuperMonster003/AutoJs6)
- [Read the accessibility service identity compatibility research note](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/research/accessibility-service-identity-compat.md)
- [Read the complete A-B-A validation protocol](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/aba-device-validation.md)
- [Read the complete sanitized QV710AF65F report](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/testing/device-QV710AF65F.md)


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y/blob/master/docs/16kb.md)
