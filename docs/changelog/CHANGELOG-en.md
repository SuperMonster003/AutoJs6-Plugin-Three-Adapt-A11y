<!-- This file is generated. Edit .changelog/lang_*.json and rerun .python/generate_markdown.py. -->

# Accessibility Compat Release History

> Page language: English

## v1.3.0 - 2026/09/15

### Feature

- Add a settings screen with language, dark mode, and theme color options that follow AutoJs6 by default, plus mechanism, privacy, limitations, release history, and about entries
- Add a compatibility service manager with a three-way control policy (Follow AutoJs6, Enabled, Disabled) that can change the service through root, WRITE_SECURE_SETTINGS, or Shizuku, with live state rows and a copyable report
- Follow the AutoJs6 accessibility service automatically through a host state broadcast, a content-triggered job on the enabled services list, and checks when the service connects or the app opens
- Publish the selected service policy through the plugin-info capabilities and the shared accessibility companion contract (contract version 3)

### Improvement

- Redesign the main screen: the status card opens the manager, supported apps carry launch buttons, and the former open, accessibility settings, and refresh buttons move into the settings screen
- Compile against Android API 37 to match the updated common plugin API

### Dependency

- Add Shizuku API 13.1.5 and AndroidX Annotation 1.10.0
- Update the bundled common plugin API to include the accessibility companion contract

## v1.2.0 - 2026/09/13

### Feature

- Local release history is available from the interface, with localized text and an English fallback

### Improvement

- Release packages are checked for a complete signing configuration, exact APK contents and reproducible documentation

## v1.1.0 - 2026/09/12

### Improvement

- Generalize the app, accessibility service, plugin metadata, embedded instructions, and README wording around application-neutral support profiles while keeping WeChat (`com.tencent.mm`) as the only currently verified profile
- Replace the target-specific plugin variant with `service-identity`, publish supported packages as a collection, and list or open installed supported apps through generic UI actions
- Refresh adaptive launcher icons with light and dark variants and a monochrome layer for themed icons
- Generalize the A-B-A protocol, research entry point, and privacy-safe metrics tool, including an explicit `targetPackage` input for future support profiles
- Remove obsolete minimum Android Studio and IntelliJ IDEA version properties now that IDE and toolchain compatibility is selected centrally
- Build verification rejects accidental native dependencies and produces a JSON report

### Dependency

- Upgrade `io.github.supermonster003.autojs6-platform-versions` from 1.7.0 to 1.7.3 so JDK 25 and 26 builds automatically align the selected KGP on the root buildscript classpath

## v1.0.0 - 2026/09/02

### Hint

- This is an experimental compatibility approach. Results depend on the WeChat version, page, and remote configuration, and a release cannot promise success for every device or account
- The service cannot create semantic nodes that a WeChat Mini Program, XWeb, or Canvas never exposed, and it does not bypass login, risk controls, or platform anti-abuse systems

### Feature

- Provide a standalone no-op accessibility companion APK scoped to `com.tencent.mm`, with a truthful and distinct application ID, icon, labels, description, and signature
- Register a compatibility service implementation class name supported by public experiments while AutoJs6 continues to read and operate nodes through its own service and the companion callbacks read no user content
- Report the compatibility mode, target package, service component, and minimum host build through the AutoJs6 plugin-info interface, with a user-controlled screen for enabling and disabling the service

### Improvement

- Document [AutoJs6#289](https://github.com/SuperMonster003/AutoJs6/issues/289), [#382](https://github.com/SuperMonster003/AutoJs6/issues/382), [#432](https://github.com/SuperMonster003/AutoJs6/issues/432), [#463](https://github.com/SuperMonster003/AutoJs6/issues/463), [#520](https://github.com/SuperMonster003/AutoJs6/issues/520), [#521](https://github.com/SuperMonster003/AutoJs6/issues/521), GKD `47267c7`, and sanitized static evidence from WeChat 8.0.72
- Provide an A-B-A device protocol that stores no private node text or screenshots and uses repeated statistics plus reversibility to distinguish compatibility effects from loading and cache accidents
- Add README and changelog copy sources in 10 languages, a reproducible Markdown generator, a read-only consistency check, and GitHub Actions gates
- Record a 7-sample A-B-A run made through AutoJs6 itself on QV710AF65F, where node count rose stably from 1 to 244 and returned to 1 after compatibility was disabled, with exact system accessibility-setting restoration verified
