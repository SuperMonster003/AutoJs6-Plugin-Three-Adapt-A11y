<!-- This file is generated. Edit .changelog/lang_*.json and rerun .python/generate_markdown.py. -->

# Accessibility Compat Release History

> Page language: English

## v1.1.0 - 2026/09/03

### Improvement

- Generalize the app, accessibility service, plugin metadata, embedded instructions, and README wording around application-neutral support profiles while keeping WeChat (`com.tencent.mm`) as the only currently verified profile
- Replace the target-specific plugin variant with `service-identity`, publish supported packages as a collection, and list or open installed supported apps through generic UI actions
- Generalize the A-B-A protocol, research entry point, and privacy-safe metrics tool, including an explicit `targetPackage` input for future support profiles
- Remove obsolete minimum Android Studio and IntelliJ IDEA version properties now that IDE and toolchain compatibility is selected centrally

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
