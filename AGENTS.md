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
- Screens are built from framework widgets through `ThemedActivity`; do not add AppCompat, Material, or other UI libraries. Appearance options must keep a "Follow AutoJs6" choice that degrades to system or built-in values when AutoJs6 is absent.
- Keep the accessibility service scoped to the explicitly documented supported-package profiles. Add a package only after a separately documented experiment proves the same compatibility mechanism.
- Keep user-visible strings synchronized across the default English resources and the supported `en`, `ar`, `es`, `fr`, `ja`, `ko`, `ru`, `zh`, `zh-rHK`, and `zh-rTW` resources. Use ASCII punctuation in resource strings.
- Update all localized changelog JSON files for feature, fix, improvement, or dependency changes, then regenerate Markdown artifacts.
- Before delivery, run the Markdown check, JVM tests, debug APK and androidTest assembly, lint, and relevant device tests. State anything not run.
- Do not commit `sign.properties`, keystores, build outputs, caches, device dumps, or extracted third-party APKs.
- Use Conventional Commits. Before each commit, set `VERSION_BUILD` to the resulting reachable commit count. Finish with a clean worktree.

## Launcher icon

- Sources are the maintainer's `.python/icons/three-adapt-ic-launcher-light.png` (used in light mode, dark `#282828` glyph) and `.python/icons/three-adapt-ic-launcher-dark.png` (used in night mode, light `#FDFDFD` glyph): 2048 x 2048 RGBA, transparent background, identical alpha channels, glyph bounding box 1424 x 1454 (slightly taller than square). The naming follows `AUTOJS6_PLUGIN_BLACK_N_WHITE_ADAPTIVE_ICON_AGENTS.md`: "light" and "dark" name the mode that uses the file, not the glyph color.
- `app/src/main/res/mipmap/` and `mipmap-night/` each hold four 432 x 432 PNGs composed deterministically by `py .python/generate_launcher_icons.py`: `ic_launcher.png` (glyph at 66% of the canvas, transparent background), `ic_launcher_round.png` (the same glyph on a disc filled with `ic_launcher_background`), `ic_launcher_foreground.png` (glyph at 42%, so that the glyph corners stay inside the 66 dp adaptive safe circle: 0.5 x 45.4 dp x sqrt(1 + (1454 / 1424)^2) = 32.4 dp < 33 dp; the generic 44% would exceed it), and `ic_launcher_monochrome.png` (black silhouette derived from the glyph alpha). Change the icon by replacing the sources or the script and regenerating; never edit the PNGs by hand.
- `values/ic_launcher_background.xml` is `#D8D8D8` and `values-night/ic_launcher_background.xml` is `#272727`; the script draws the round disc with the same values, so the adaptive background and the round icon always match.
- `mipmap-anydpi-v26/` and `mipmap-night-anydpi-v26/` hold identical `ic_launcher.xml` and `ic_launcher_round.xml` that reference the mipmap layers directly (no inset drawables or fractions); the night copies keep night mode on the adaptive path instead of the legacy night PNG. The Manifest declares both `android:icon` and `android:roundIcon`. Lint reports `IconDuplicatesConfig` for the two byte-identical monochrome layers; that warning is accepted.
- README headers use `<picture>` with `mipmap-night/ic_launcher.png` for the dark color scheme and `mipmap/ic_launcher.png` as the default image; the transparent legacy icons exist for that purpose and for API 24 / 25 launchers.

## Shared repository standard (2026-09-13)

Read [the complete repository standard](docs/development/repository-standard.md) before changing this repository. It is part of this repository guidance. Existing product-specific constraints above remain in force.

This APK contains ABI-independent managed code; native alignment verification rejects native dependencies. No ABI splits are appropriate. Release collection is `:app:appendDigestToReleasedFiles` and verifies the exact signed APK set. Do not claim physical ColorOS activation, projection consent or host output publication was tested unless it was actually exercised.

Run `.python/check_markdown.bat`, `py -3 -m unittest discover -s .python/tests`, and the Gradle Wrapper with `--max-workers=2`. Platform acceptance: `--no-daemon -Djava.vendor="Eclipse Adoptium" -Djava.vendor.version=Temurin-21.0.12.1+1 :app:assembleDebug :app:testDebugUnitTest`. Disable version auto-increment while checking a prepared commit. Before every commit set VERSION_BUILD to `git rev-list --count HEAD` plus one.
