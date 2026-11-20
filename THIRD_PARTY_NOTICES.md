# Third-party notices

This project uses the following libraries and resources under their upstream licenses.

## Material 3 standalone interface

- Material Components for Android 1.13.0, `com.google.android.material:material`, Apache License 2.0. Source and release: https://github.com/material-components/material-components-android/releases/tag/1.13.0
- Material AAR SHA-256: `6d5e1cbb67c05bcdcbbf84005787dafd354a155f59a4d3800fd45fa7eb3689f5`.
- AndroidX AppCompat 1.7.1, `androidx.appcompat:appcompat`, Apache License 2.0. Source and release: https://developer.android.com/jetpack/androidx/releases/appcompat#1.7.1
- AppCompat AAR SHA-256: `2ad334a323b28046e89b738c77d184cb3dcca32a551ab048851b2fda23a3ba26`.
- AndroidX transitive libraries retain their Apache License 2.0 notices. AndroidX source: https://android.googlesource.com/platform/frameworks/support
- The common settings line icons derive from Google Material Symbols, Apache License 2.0. Source: https://github.com/google/material-design-icons

The AAR hashes above were verified against the exact Gradle cache artifacts used by the build.

## Release history page

- Jetpack Compose BOM 2026.09.00, `androidx.compose:compose-bom` with `androidx.compose.runtime:runtime`, `androidx.compose.ui:ui`, `androidx.compose.foundation:foundation`, `androidx.compose.material3:material3`, `androidx.compose.animation:animation` and `androidx.activity:activity-compose`, Apache License 2.0. Source: https://github.com/androidx/androidx
- `ui-graphics` brings `androidx.graphics:graphics-path`, whose `libandroidx.graphics.path.so` (arm64-v8a, armeabi-v7a, x86_64, x86) is the only native library in the APK; it is stored uncompressed and 16 KB aligned.
- Test-only dependencies (JUnit 4, AndroidX Test, Espresso core, Compose UI test with `ui-test-manifest` in debug builds only, Apache License 2.0 or EPL 1.0) are not shipped in the release APK.

## Existing optional Shizuku service control

- Shizuku API / provider 13.1.5, Apache License 2.0. Source: https://github.com/RikkaApps/Shizuku-API
- AndroidX annotations, Apache License 2.0. Source: https://android.googlesource.com/platform/frameworks/support

Apache License 2.0: https://www.apache.org/licenses/LICENSE-2.0
