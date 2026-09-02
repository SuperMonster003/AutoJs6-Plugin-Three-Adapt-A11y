package io.github.supermonster003.autojs6.plugin.accessibilitycompat

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
        assertEquals("wechat", AccessibilityCompatContract.PLUGIN_VARIANT)
        assertEquals("com.tencent.mm", AccessibilityCompatContract.TARGET_PACKAGE)
        assertEquals("org.autojs.permission.PLUGIN", AccessibilityCompatContract.PLUGIN_PERMISSION)
        assertEquals("org.autojs.plugin.INFO", AccessibilityCompatContract.INFO_ACTION)
        assertEquals(AccessibilityCompatContract.PLUGIN_ID, AccessibilityCompatContract.INFO_CATEGORY)
        assertEquals("org.autojs.plugin.action.WAKE", AccessibilityCompatContract.WAKE_ACTION)
        assertEquals(1, AccessibilityCompatContract.CONTRACT_VERSION)
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
    }
}
