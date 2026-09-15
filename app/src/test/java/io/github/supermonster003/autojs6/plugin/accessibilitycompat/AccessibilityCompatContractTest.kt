package io.github.supermonster003.autojs6.plugin.accessibilitycompat

import org.autojs.plugin.common.api.AutoJs6AccessibilityCompanionContract
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AccessibilityCompatContractTest {

    @Test
    fun stableIdentityAndDiscoveryValuesMatchPublishedContract() {
        assertEquals(
            "io.github.supermonster003.autojs6.plugin.accessibilitycompat",
            AccessibilityCompatContract.APPLICATION_ID,
        )
        assertEquals("accessibility-compat", AccessibilityCompatContract.PLUGIN_ID)
        assertEquals("accessibility", AccessibilityCompatContract.PLUGIN_ENGINE)
        assertEquals("service-identity", AccessibilityCompatContract.PLUGIN_VARIANT)
        assertEquals(listOf("com.tencent.mm"), AccessibilityCompatContract.SUPPORTED_PACKAGES)
        assertEquals("org.autojs.permission.PLUGIN", AccessibilityCompatContract.PLUGIN_PERMISSION)
        assertEquals("org.autojs.plugin.INFO", AccessibilityCompatContract.INFO_ACTION)
        assertEquals(AccessibilityCompatContract.PLUGIN_ID, AccessibilityCompatContract.INFO_CATEGORY)
        assertEquals(
            "org.autojs.plugin.category.ACCESSIBILITY_COMPANION",
            AccessibilityCompatContract.COMPANION_INFO_CATEGORY,
        )
        assertEquals("org.autojs.plugin.action.WAKE", AccessibilityCompatContract.WAKE_ACTION)
        assertEquals(
            "https://github.com/SuperMonster003/AutoJs6-Plugin-Accessibility-Compat",
            AccessibilityCompatContract.PROJECT_URL,
        )
        assertEquals(3, AccessibilityCompatContract.CONTRACT_VERSION)
        assertEquals(3923, AccessibilityCompatContract.REQUIRED_HOST_VERSION)
    }

    @Test
    fun compatibilityComponentUsesTruthfulApplicationIdAndRequiredServiceClass() {
        assertEquals(
            "com.google.android.accessibility.selecttospeak.SelectToSpeakService",
            AccessibilityCompatContract.COMPATIBILITY_SERVICE_CLASS,
        )
        assertEquals(
            "io.github.supermonster003.autojs6.plugin.accessibilitycompat/" +
                "com.google.android.accessibility.selecttospeak.SelectToSpeakService",
            AccessibilityCompatContract.expectedServiceId(),
        )
        assertEquals(
            "example.application/com.google.android.accessibility.selecttospeak.SelectToSpeakService",
            AccessibilityCompatContract.expectedServiceId("example.application"),
        )
    }

    @Test
    fun capabilityKeysAreStableAndDoNotCollide() {
        val keys = listOf(
            AccessibilityCompatContract.CAPABILITY_CONTRACT_VERSION,
            AccessibilityCompatContract.CAPABILITY_MODE,
            AccessibilityCompatContract.CAPABILITY_SERVICE_COMPONENT,
            AccessibilityCompatContract.CAPABILITY_TARGET_PACKAGES,
            AccessibilityCompatContract.CAPABILITY_SERVICE_POLICY,
        )

        assertEquals(keys.size, keys.toSet().size)
        keys.forEach { key ->
            assertTrue(key.startsWith("org.autojs.plugin.accessibility.compat."))
        }
        assertNotEquals(
            AccessibilityCompatContract.CAPABILITY_MODE,
            AccessibilityCompatContract.MODE_GLOBAL_EXPOSURE_TRIGGER,
        )
        assertEquals("global-exposure-trigger", AccessibilityCompatContract.MODE_GLOBAL_EXPOSURE_TRIGGER)
        assertEquals(
            "org.autojs.plugin.accessibility.compat.SERVICE_POLICY",
            AccessibilityCompatContract.CAPABILITY_SERVICE_POLICY,
        )
    }

    @Test
    fun hostFacingValuesComeFromTheSharedCompanionContract() {
        assertEquals(AutoJs6AccessibilityCompanionContract.HOST_PACKAGE_NAME, AccessibilityCompatContract.HOST_PACKAGE_NAME)
        assertEquals("org.autojs.autojs6", AccessibilityCompatContract.HOST_PACKAGE_NAME)
        assertEquals(
            AutoJs6AccessibilityCompanionContract.ACTION_HOST_SERVICE_STATE_CHANGED,
            AccessibilityCompatContract.HOST_SERVICE_STATE_ACTION,
        )
        assertEquals(
            "org.autojs.plugin.action.ACCESSIBILITY_SERVICE_STATE_CHANGED",
            AccessibilityCompatContract.HOST_SERVICE_STATE_ACTION,
        )
        assertEquals("hostServiceEnabled", AccessibilityCompatContract.HOST_SERVICE_STATE_EXTRA_ENABLED)
        assertEquals(
            "org.autojs.autojs6/org.autojs.autojs.core.accessibility.AccessibilityServiceUsher",
            AutoJs6AccessibilityCompanionContract.HOST_SERVICE_COMPONENT,
        )
        assertEquals(
            listOf("follow-host", "enabled", "disabled"),
            listOf(
                AutoJs6AccessibilityCompanionContract.SERVICE_POLICY_FOLLOW_HOST,
                AutoJs6AccessibilityCompanionContract.SERVICE_POLICY_ENABLED,
                AutoJs6AccessibilityCompanionContract.SERVICE_POLICY_DISABLED,
            ),
        )
    }
}
