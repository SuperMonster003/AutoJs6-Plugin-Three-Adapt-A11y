plugins {
    id("com.android.application") version System.getProperty("gradle.agp.version") apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

tasks {
    register<Delete>("clean").configure {
        delete(rootProject.layout.buildDirectory)
    }
}
