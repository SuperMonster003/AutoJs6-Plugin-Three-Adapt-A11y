package io.github.supermonster003.autojs6.plugin.accessibilitycompat

object AccessibilityCompatContract {
    const val APPLICATION_ID = "io.github.supermonster003.autojs6.plugin.accessibilitycompat"
    const val PLUGIN_ID = "accessibility-compat"
    const val PLUGIN_ENGINE = "accessibility"
    const val PLUGIN_VARIANT = "wechat"
    const val TARGET_PACKAGE = "com.tencent.mm"

    const val PLUGIN_PERMISSION = "org.autojs.permission.PLUGIN"
    const val INFO_ACTION = "org.autojs.plugin.INFO"
    const val INFO_CATEGORY = PLUGIN_ID
    const val WAKE_ACTION = "org.autojs.plugin.action.WAKE"
    const val COMPATIBILITY_SERVICE_CLASS =
        "com.google.android.accessibility.selecttospeak.SelectToSpeakService"

    const val CONTRACT_VERSION = 1
    const val REQUIRED_HOST_VERSION = 3923
    const val CAPABILITY_CONTRACT_VERSION =
        "org.autojs.plugin.accessibility.compat.CONTRACT_VERSION"
    const val CAPABILITY_MODE = "org.autojs.plugin.accessibility.compat.MODE"
    const val CAPABILITY_SERVICE_COMPONENT =
        "org.autojs.plugin.accessibility.compat.SERVICE_COMPONENT"
    const val CAPABILITY_TARGET_PACKAGES =
        "org.autojs.plugin.accessibility.compat.TARGET_PACKAGES"
    const val MODE_GLOBAL_EXPOSURE_TRIGGER = "global-exposure-trigger"

    fun expectedServiceId(applicationId: String = APPLICATION_ID): String =
        "$applicationId/$COMPATIBILITY_SERVICE_CLASS"
}
