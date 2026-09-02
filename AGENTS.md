# Workspace rules

This repository contains the `Accessibility Compat` Android companion for AutoJs6.

- Preserve user changes and inspect `git status --short` before and after edits.
- Use the online `io.github.supermonster003.autojs6-platform-versions` plugin. Do not use `mavenLocal()` or sibling-project build dependencies.
- Keep the application identity honest. The application ID is `io.github.supermonster003.autojs6.plugin.accessibilitycompat`; do not imitate another application's package, label, icon, or signature.
- The allow-listed accessibility class name is an explicitly documented compatibility shim. Its visible service label and description must remain truthful.
- The accessibility callback is privacy-minimal: do not read `event.source`, `event.text`, `rootInActiveWindow`, screenshots, or user content; do not add networking, analytics, storage, overlay, microphone, or camera permissions.
- Keep the accessibility service scoped to `com.tencent.mm` unless a separately documented experiment proves a broader scope is necessary.
- Keep user-visible strings synchronized across the default English resources and the supported `en`, `ar`, `es`, `fr`, `ja`, `ko`, `ru`, `zh`, `zh-rHK`, and `zh-rTW` resources. Use ASCII punctuation in resource strings.
- Update all localized changelog JSON files for feature, fix, improvement, or dependency changes, then regenerate Markdown artifacts.
- Before delivery, run the Markdown check, JVM tests, debug APK and androidTest assembly, lint, and relevant device tests. State anything not run.
- Do not commit `sign.properties`, keystores, build outputs, caches, device dumps, or extracted third-party APKs.
- Use Conventional Commits. Before each commit, set `VERSION_BUILD` to the resulting reachable commit count. Finish with a clean worktree.
