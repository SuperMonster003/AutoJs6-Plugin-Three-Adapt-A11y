import java.util.Properties
import java.util.zip.CRC32
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

plugins {
    id("io.github.supermonster003.autojs6-native-alignment")
    id("org.autojs.build.utils")
    id("org.autojs.build.versions")
    id("org.autojs.build.signs")
    id("org.autojs.build.jvm-convention")
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

val globalApplicationId = "io.github.supermonster003.autojs6.plugin.three.adapt.a11y"
var isSignsValid = false

android {
    namespace = globalApplicationId
    compileSdk = versions.sdkVersionCompile

    defaultConfig {
        applicationId = globalApplicationId
        minSdk = versions.sdkVersionMin
        targetSdk = versions.sdkVersionTarget
        versionCode = versions.appVersionCode
        versionName = versions.appVersionName

        // The pre-Material APK lacks AndroidX Trace, so upgrade preparation has a framework-only runner.
        testInstrumentationRunner = if (providers.gradleProperty("launcherUpgradeProbe").orNull == "true") {
            "$globalApplicationId.LauncherUpgradeInstrumentation"
        } else {
            "androidx.test.runner.AndroidJUnitRunner"
        }

        resValue("string", "app_name", "3-Adapt A11y")
        resValue("string", "plugin_author", "SuperMonster003")
        resValue("string", "plugin_engine", "accessibility")
        resValue("string", "plugin_id", "three-adapt-a11y")
        resValue("string", "plugin_variant", "service-identity")
        resValue("string", "plugin_version_date", utils.getDateString("MMM d, yyyy", "GMT+08:00"))
    }

    signingConfigs {
        val props = Properties().also { properties ->
            File("${project.rootDir}/sign.properties").takeIf { it.exists() }?.let { file ->
                file.inputStream().use { properties.load(it) }
                isSignsValid = properties.isNotEmpty()
            }
        }
        if (isSignsValid) {
            create("release") {
                storeFile = props["storeFile"]?.let { file(it as String) }
                keyPassword = props["keyPassword"] as String
                keyAlias = props["keyAlias"] as String
                storePassword = props["storePassword"] as String
            }
        }
    }

    buildTypes {
        val releaseSigning = takeIf { isSignsValid }?.let { signingConfigs.getByName("release") }
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            releaseSigning?.let { signingConfig = it }
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            releaseSigning?.let { signingConfig = it }
        }
    }

    buildFeatures {
        aidl = true
        compose = true
        resValues = true
    }
}

dependencies {
    implementation(files("$rootDir/libs/common-plugin-api.aar"))

    implementation(libs.androidx.annotation)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // Optional unattended service control through Shizuku; see docs/development/service-control.md.
    implementation(libs.shizuku.api)
    implementation(libs.shizuku.provider)

    // Jetpack Compose, only for the release history page (AUTOJS6_PLUGIN_RELEASE_HISTORY_AGENTS.md); the BOM is the
    // single version source and also manages activity-compose.
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material3)
    implementation(libs.compose.animation)
    implementation(libs.compose.activity)

    testImplementation(libs.junit)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.test.runner)
    // ui-test brings espresso-core 3.5.0, which fails to reflect InputManager.getInstance on API 36; pin 3.7.0.
    androidTestImplementation(libs.test.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    // No ui-test-manifest: the Compose tests use createEmptyComposeRule and launch the real activities, while
    // ui-test-manifest would merge an exported androidx.activity.ComponentActivity into the debug APK.
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.register("appendDigestToReleasedFiles") {
    group = "build"
    description = "Builds the signed release APK and copies it with a CRC32 suffix."
    dependsOn("assembleRelease", "verifyReleaseNativeLibraries")

    doLast {
        require(isSignsValid) { "Release signing configuration is missing or incomplete." }
        val outputDirectory = layout.buildDirectory.dir("outputs/apk/release").get().asFile
        val sourceApks = outputDirectory.listFiles { file ->
            file.isFile && file.extension == "apk" && !file.name.contains("unsigned")
        }.orEmpty().toList()
        require(sourceApks.size == 1) { "Expected exactly one signed release APK, found ${sourceApks.map { it.name }}" }

        val releaseDirectory = rootProject.layout.projectDirectory.dir("releases").asFile
        releaseDirectory.mkdirs()

        val source = sourceApks.single()
        val crc32 = CRC32().apply { source.inputStream().use { input -> input.copyTo(outputStream()) } }.value
        val digest = crc32.toString(16).uppercase().padStart(8, '0')
        source.copyTo(
            releaseDirectory.resolve("autojs6-plugin-three-adapt-a11y-v${android.defaultConfig.versionName}-$digest.apk"),
            overwrite = true,
        )
    }
}

private fun CRC32.outputStream() = object : java.io.OutputStream() {
    override fun write(value: Int) = update(value)
    override fun write(bytes: ByteArray, offset: Int, length: Int) = update(bytes, offset, length)
}

// Native code inventory: the plugin has no native code of its own, but Compose ui-graphics depends on
// androidx.graphics:graphics-path, whose libandroidx.graphics.path.so is packaged for four ABIs. The APK stays one
// universal package without ABI splits; the shared gate keeps the 16 KB alignment checks and writes the supported ABIs
// to the Manifest, and verifyReleaseNativeLibraries below asserts that exactly these libraries are present and stored
// uncompressed. getInfo() still reports supportedAbis = emptyArray().
val nativeLibraryAbis = listOf("arm64-v8a", "armeabi-v7a", "x86_64", "x86")
val allowedNativeLibraries = nativeLibraryAbis.map { "lib/$it/libandroidx.graphics.path.so" }
nativeAlignment { expectNoNativeLibraries.set(false) }

tasks.register("verifyReleaseNativeLibraries") {
    group = "verification"
    description = "Checks that the release APK carries exactly the Compose graphics-path libraries, stored uncompressed"
    dependsOn("assembleRelease")
    val apkDir = layout.buildDirectory.dir("outputs/apk/release")
    doLast {
        val apks = apkDir.get().asFile.listFiles { file -> file.extension == "apk" }.orEmpty().toList()
        check(apks.size == 1) { "Expected one release APK, found ${apks.map { it.name }}" }
        ZipFile(apks.single()).use { zip ->
            val natives = zip.entries().asSequence().filter { it.name.startsWith("lib/") }.toList()
            check(natives.map { it.name }.sorted() == allowedNativeLibraries.sorted()) {
                "Unexpected native library set: ${natives.map { it.name }.sorted()}"
            }
            check(natives.all { it.method == ZipEntry.STORED }) { "Native libraries must be stored uncompressed" }
        }
    }
}


// Fail before collection when credentials, keystore or the actual APK set are incomplete.
val verifySignedReleaseArtifacts = tasks.register("verifySignedReleaseArtifacts") {
    group = "verification"
    dependsOn("assembleRelease")
    doLast {
        val signing = android.buildTypes.getByName("release").signingConfig
        check(signing != null && signing.storeFile?.isFile == true &&
            !signing.storePassword.isNullOrBlank() && !signing.keyAlias.isNullOrBlank() &&
            !signing.keyPassword.isNullOrBlank()) { "Release signing configuration is missing or incomplete" }
        val directory = layout.buildDirectory.dir("outputs/apk/release").get().asFile
        val apks = directory.listFiles { file -> file.isFile && file.extension == "apk" }.orEmpty()
        check(apks.map { it.name }.toSet() == setOf("app-release.apk")) {
            "Unexpected release APK set: ${apks.map { it.name }.sorted()}"
        }
        val buildTools = androidComponents.sdkComponents.sdkDirectory.get().asFile
            .resolve("build-tools/${android.buildToolsVersion}")
        val signerJar = buildTools.resolve("lib/apksigner.jar")
        check(signerJar.isFile) { "Android SDK apksigner is unavailable" }
        val result = providers.exec {
            commandLine("java", "-jar", signerJar.absolutePath, "verify", apks.single().absolutePath)
            isIgnoreExitValue = true
        }.result.get()
        check(result.exitValue == 0) { "Release APK signature verification failed" }
    }
}
tasks.named("appendDigestToReleasedFiles") { dependsOn(verifySignedReleaseArtifacts) }
tasks.matching { it.name == "prepareReleaseArtifacts" }.configureEach {
    dependsOn(verifySignedReleaseArtifacts)
}
