# Workspace rules

This repository contains the `3-Adapt A11y` Android companion for AutoJs6. It was renamed from `Accessibility Compat` on 2026-09-29; the shared rename procedure for the Three series lives in `D:/idea-projects/AUTOJS6_PLUGIN_THREE_SERIES_RENAME_AGENTS.md`.

## Identity

The following values MUST stay identical across Gradle, the Manifest, `ThreeAdaptA11yContract`, resources, documentation, tests, the AutoJs6 host catalog, and the official plugin index. Change every location together and rerun `ThreeAdaptA11yContractTest`, `PluginRuntimeInfoTest`, and the instrumentation suite.

| Item | Value |
|---|---|
| Repository and directory name | `AutoJs6-Plugin-Three-Adapt-A11y` |
| `rootProject.name` | `autojs6-plugin-three-adapt-a11y` |
| Application title (`app_name` resValue, English, not translated) and accessibility service label | `3-Adapt A11y` |
| `applicationId` / namespace / Kotlin package | `io.github.supermonster003.autojs6.plugin.three.adapt.a11y` |
| Plugin ID / engine / variant | `three-adapt-a11y` / `accessibility` / `service-identity` |
| INFO service | `ThreeAdaptA11yInfoService`, action `org.autojs.plugin.INFO`, categories `three-adapt-a11y` and `org.autojs.plugin.category.ACCESSIBILITY_COMPANION` |
| Accessibility service | `com.google.android.accessibility.selecttospeak.SelectToSpeakService` extending `ThreeAdaptA11yService`; the class name is the documented compatibility shim and MUST NOT be renamed |
| Theme | `Theme.ThreeAdaptA11y` |
| Release artifact | `autojs6-plugin-three-adapt-a11y-v{VERSION_NAME}-{CRC32}.apk` |
| Plugin instruction heading (all locales) | `# AutoJs6 3-Adapt A11y` |
| Host and index references | AutoJs6 `PluginInstallWizardCatalog` entry `official("three.adapt.a11y")`, the Advanced Protection row in AutoJs6 `PluginSettingsFragment`, and `official-repositories.json` in AutoJs6-Official-Plugins-Index |

Names that describe the mechanism rather than the product (`CompatServiceController`, `CompatServiceSync`, `CompatSettings`, `CompatServiceManagerDialog`, the "compatibility service" strings, `tools/autojs6-compat-metrics.js`, `docs/research/accessibility-service-identity-compat.md`) are deliberately kept; they are truthful descriptions of the accessibility compatibility shim, not identity.

## Rules

- Preserve user changes and inspect `git status --short` before and after edits.
- Use the online `io.github.supermonster003.autojs6-platform-versions` plugin. Do not use `mavenLocal()` or sibling-project build dependencies.
- Keep the application identity honest. The application ID is `io.github.supermonster003.autojs6.plugin.three.adapt.a11y`; do not imitate another application's package, label, icon, or signature.
- The allow-listed accessibility class name is an explicitly documented compatibility shim. Its visible service label and description must remain truthful.
- The accessibility callback is privacy-minimal: do not read `event.source`, `event.text`, `rootInActiveWindow`, screenshots, or user content; do not add networking, analytics, storage, overlay, microphone, or camera permissions.
- `WRITE_SECURE_SETTINGS` and the Shizuku permission exist only for the optional service control described in `docs/development/service-control.md`. That code may change the `enabled_accessibility_services` secure setting for this package's own service and nothing else; keep the three methods individually switchable, keep "Follow AutoJs6" the default policy, and never touch the AutoJs6 service or other settings.
- Screens use `ThemedActivity` and Material 3 controls/dialogs with explicit runtime palette styling. The maintainer's 2026-09-29 unified settings requirement supersedes the former framework-only restriction. Appearance options must keep a "Follow AutoJs6" choice that degrades to system or built-in values when AutoJs6 is absent.
- Keep the accessibility service scoped to the explicitly documented supported-package profiles. Add a package only after a separately documented experiment proves the same compatibility mechanism.
- Keep user-visible strings synchronized across the default English resources and the supported `en`, `ar`, `es`, `fr`, `ja`, `ko`, `ru`, `zh`, `zh-rHK`, and `zh-rTW` resources. Use ASCII punctuation in resource strings.
- Update all localized changelog JSON files for feature, fix, improvement, or dependency changes, then regenerate Markdown artifacts.
- Before delivery, run the Markdown check, JVM tests, debug APK and androidTest assembly, lint, and relevant device tests. State anything not run.
- Do not commit `sign.properties`, keystores, build outputs, caches, device dumps, or extracted third-party APKs.
- Use Conventional Commits. Before each commit, set `VERSION_BUILD` to the resulting reachable commit count. Finish with a clean worktree.

## Launcher icon

- The source is `.python/icons/three-adapt-ic-launcher-light.png` (2048 x 2048, maintainer-provided artwork; the original dark source is retained). Use its alpha as the common shape for all variants; normalize glyph colors to `#272727` for light application themes and `#D8D8D8` for dark themes. Preserve the source artwork and regenerate, never hand-edit output PNGs.
- `py .python/generate_launcher_icons.py` deterministically generates seven 432 x 432 PNGs, four adaptive XMLs, two legacy bitmap XMLs, and two background color XMLs. `--check` is read-only and rejects stale outputs and obsolete colliding resources. UI glyph width is 66%, adaptive glyph width is 0.42; the script verifies the 66 dp safe circle using the actual source aspect ratio.
- `mipmap/ic_launcher.png` and `mipmap-night/ic_launcher.png` are transparent UI/README assets on every Android version. Never add adaptive XML with this resource name. In-app references use `R.mipmap.ic_launcher`; the host plugin center resolves this resource under its own UI configuration.
- The Manifest's `icon` and `roundIcon` both use `@mipmap/ic_launcher_system`. It is an adaptive icon on API 26+ with `ic_launcher_system_foreground` and a single black `ic_launcher_monochrome`, or a filled circular PNG on older APIs. The default dark resource always uses glyph `#D8D8D8` on background `#212121`. Explicit light uses `ic_launcher_system_light` with glyph `#272727` on `#FAFAFA`. Automatic mode uses a real `ic_launcher_system_auto` resource, with default dark and notnight light bitmap XML wrappers and matching anydpi-v26 / notnight-anydpi-v26 adaptive XML. Never use a values resource alias in a Manifest icon: PackageManager eagerly resolves it and freezes the install-time theme. Tests must check the parsed ActivityInfo icon IDs as well as direct resource rendering. Application UI assets still follow the app theme. System themed icons may be recolored by the launcher.
- Do not put a filled background in the adaptive foreground, use inset drawables, disable the stable real Activity, or clear launcher data. Resource-only `LauncherIconResourceTest` checks bitmap transparency, exact glyph colors, adaptive/legacy selection, dark default fallback, and Manifest wiring without changing user preferences. Actual launcher rendering still requires device inspection.
- Settings offer adaptive light, adaptive dark, adaptive automatic (default), and transparent background. Four stable `.launcher.{AdaptiveLight,AdaptiveDark,AdaptiveAuto,Transparent}IconAlias` components point to the unchanged real Activity; only one is enabled after switching. PackageManager persists the choice. Use `DONT_KILL_APP`, enable the target before migrating mutable shortcut ownership and disabling old aliases, and roll back on failure. Automatic and transparent modes explain launcher caching, theme and masking limitations. Transparent mode uses the original `ic_launcher` resource.
- The workspace reference `AUTOJS6_PLUGIN_BLACK_N_WHITE_ADAPTIVE_ICON_AGENTS.md` documents the current scheme and the platform limitations; the former shared `ic_launcher` adaptive/transparent naming and backgrounds are obsolete.

## Shared repository standard (2026-09-13)

Read [the complete repository standard](docs/development/repository-standard.md) before changing this repository. It is part of this repository guidance. Existing product-specific constraints above remain in force.

This APK contains ABI-independent managed code; native alignment verification rejects native dependencies. No ABI splits are appropriate. Release collection is `:app:appendDigestToReleasedFiles` and verifies the exact signed APK set. Do not claim physical ColorOS activation, projection consent or host output publication was tested unless it was actually exercised.

Run `.python/check_markdown.bat`, `py -3 -m unittest discover -s .python/tests`, and the Gradle Wrapper with `--max-workers=2`. Platform acceptance: `--no-daemon -Djava.vendor="Eclipse Adoptium" -Djava.vendor.version=Temurin-21.0.12.1+1 :app:assembleDebug :app:testDebugUnitTest`. Disable version auto-increment while checking a prepared commit. Before every commit set VERSION_BUILD to `git rev-list --count HEAD` plus one.

## Unified standalone settings (2026-09-29)

The maintainer confirmed `D:/idea-projects/AUTOJS6_PLUGIN_STANDALONE_SETTINGS_AGENTS.md` for all standalone plugin settings. Follow that shared specification for appearance row order, flat groups, 16sp titles / 14sp summaries, 24dp padding, common icons, centered 24dp dialogs with fixed Cancel/OK actions, the shared palette and local-only HEX/RGB preview. Language, night mode and theme default to following AutoJs6; absent-host theme falls back to #FFDEAD. Neutral surfaces must not be tinted by the selected seed. Launcher default is AUTO, not DARK; preserve explicit PackageManager selections during normalization and repair mixed states via the update receiver / Activity startup without a duplicate mode preference. Cancel/back/outside dismissal never saves a draft.
