import java.util.Properties
import java.util.zip.CRC32

plugins {
    id("org.autojs.build.utils")
    id("org.autojs.build.versions")
    id("org.autojs.build.signs")
    id("org.autojs.build.jvm-convention")
    id("com.android.application")
}

val globalApplicationId = "io.github.supermonster003.autojs6.plugin.accessibilitycompat"
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

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resValue("string", "app_name", "AutoJs6 Accessibility Compat")
        resValue("string", "plugin_author", "SuperMonster003")
        resValue("string", "plugin_engine", "accessibility")
        resValue("string", "plugin_id", "accessibility-compat")
        resValue("string", "plugin_variant", "wechat")
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
        resValues = true
    }
}

dependencies {
    implementation(files("$rootDir/libs/common-plugin-api.aar"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.test.runner)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.register("appendDigestToReleasedFiles") {
    group = "build"
    description = "Builds the signed release APK and copies it with a CRC32 suffix."
    dependsOn("assembleRelease")

    doLast {
        require(isSignsValid) { "Release signing configuration is missing or incomplete." }
        val outputDirectory = layout.buildDirectory.dir("outputs/apk/release").get().asFile
        val sourceApks = outputDirectory.listFiles { file ->
            file.isFile && file.extension == "apk" && !file.name.contains("unsigned")
        }.orEmpty().toList()
        require(sourceApks.size == 1) { "Expected exactly one signed release APK, found ${sourceApks.map { it.name }}" }

        val releaseDirectory = rootProject.layout.projectDirectory.dir("releases").asFile
        releaseDirectory.mkdirs()
        releaseDirectory.listFiles()?.filter { it.extension == "apk" }?.forEach { it.delete() }

        val source = sourceApks.single()
        val crc32 = CRC32().apply { source.inputStream().use { input -> input.copyTo(outputStream()) } }.value
        val digest = crc32.toString(16).uppercase().padStart(8, '0')
        source.copyTo(
            releaseDirectory.resolve("autojs6-plugin-accessibility-compat-v${android.defaultConfig.versionName}-$digest.apk"),
            overwrite = true,
        )
    }
}

private fun CRC32.outputStream() = object : java.io.OutputStream() {
    override fun write(value: Int) = update(value)
    override fun write(bytes: ByteArray, offset: Int, length: Int) = update(bytes, offset, length)
}
