# Bundled plugin API

`common-plugin-api.aar` is copied from the AutoJs6 common plugin API used by the official plugin family.

- SHA-256: `81B4F2EB09757C494047B8C142BF48AEC122A83B9E3F388962928BF6A4984130`
- Built from: `plugin-api/common-plugin-api` of the AutoJs6 repository (`:plugin-api:common-plugin-api:assembleRelease`), compiled against Android API 37
- Purpose: expose standard `PluginInfo` metadata through `org.autojs.plugin.INFO`, read the official host settings snapshot (`AutoJs6HostSettingsContract`) for the "follow AutoJs6" appearance options, and share the accessibility companion contract (`AutoJs6AccessibilityCompanionContract`) that names the `SERVICE_POLICY` capability and the host service state broadcast
- License and source: [SuperMonster003/AutoJs6](https://github.com/SuperMonster003/AutoJs6), Mozilla Public License 2.0

Because the AAR metadata requires `compileSdk` 37 or later, `COMPILE_SDK_VERSION` in `version.properties` must stay at 37 or above when this file is updated.
