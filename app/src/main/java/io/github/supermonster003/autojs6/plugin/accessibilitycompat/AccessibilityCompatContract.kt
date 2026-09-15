package io.github.supermonster003.autojs6.plugin.accessibilitycompat

import org.autojs.plugin.common.api.AutoJs6AccessibilityCompanionContract as Companion

object AccessibilityCompatContract {
    const val APPLICATION_ID = "io.github.supermonster003.autojs6.plugin.accessibilitycompat"
    const val PLUGIN_ID = "accessibility-compat"
    const val PLUGIN_ENGINE = "accessibility"
    const val PLUGIN_VARIANT = "service-identity"
    const val PROJECT_URL = "https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat"
    val SUPPORTED_PACKAGES: List<String> = listOf("com.tencent.mm")

    const val PLUGIN_PERMISSION = Companion.PLUGIN_PERMISSION
    const val INFO_ACTION = "org.autojs.plugin.INFO"
    const val INFO_CATEGORY = PLUGIN_ID
    const val COMPANION_INFO_CATEGORY = Companion.INFO_CATEGORY
    const val WAKE_ACTION = "org.autojs.plugin.action.WAKE"
    const val COMPATIBILITY_SERVICE_CLASS =
        "com.google.android.accessibility.selecttospeak.SelectToSpeakService"

    const val HOST_PACKAGE_NAME = Companion.HOST_PACKAGE_NAME
    const val HOST_SERVICE_STATE_ACTION = Companion.ACTION_HOST_SERVICE_STATE_CHANGED
    const val HOST_SERVICE_STATE_EXTRA_ENABLED = Companion.EXTRA_HOST_SERVICE_ENABLED

    /** Bumped when a capability key is added; see docs/development/service-control.md. */
    const val CONTRACT_VERSION = 3
    const val REQUIRED_HOST_VERSION = 3923
    const val CAPABILITY_CONTRACT_VERSION = Companion.CAPABILITY_CONTRACT_VERSION
    const val CAPABILITY_MODE = Companion.CAPABILITY_MODE
    const val CAPABILITY_SERVICE_COMPONENT = Companion.CAPABILITY_SERVICE_COMPONENT
    const val CAPABILITY_TARGET_PACKAGES = Companion.CAPABILITY_TARGET_PACKAGES
    const val CAPABILITY_SERVICE_POLICY = Companion.CAPABILITY_SERVICE_POLICY
    const val MODE_GLOBAL_EXPOSURE_TRIGGER = "global-exposure-trigger"

    fun expectedServiceId(applicationId: String = APPLICATION_ID): String =
        "$applicationId/$COMPATIBILITY_SERVICE_CLASS"
}
