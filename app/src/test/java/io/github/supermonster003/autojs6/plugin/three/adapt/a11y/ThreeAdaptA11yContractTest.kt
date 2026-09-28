package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import org.autojs.plugin.common.api.AutoJs6AccessibilityCompanionContract
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ThreeAdaptA11yContractTest {

    @Test
    fun stableIdentityAndDiscoveryValuesMatchPublishedContract() {
        assertEquals(
            "io.github.supermonster003.autojs6.plugin.three.adapt.a11y",
            ThreeAdaptA11yContract.APPLICATION_ID,
        )
        assertEquals("three-adapt-a11y", ThreeAdaptA11yContract.PLUGIN_ID)
        assertEquals("accessibility", ThreeAdaptA11yContract.PLUGIN_ENGINE)
        assertEquals("service-identity", ThreeAdaptA11yContract.PLUGIN_VARIANT)
        assertEquals(listOf("com.tencent.mm"), ThreeAdaptA11yContract.SUPPORTED_PACKAGES)
        assertEquals("org.autojs.permission.PLUGIN", ThreeAdaptA11yContract.PLUGIN_PERMISSION)
        assertEquals("org.autojs.plugin.INFO", ThreeAdaptA11yContract.INFO_ACTION)
        assertEquals(ThreeAdaptA11yContract.PLUGIN_ID, ThreeAdaptA11yContract.INFO_CATEGORY)
        assertEquals(
            "org.autojs.plugin.category.ACCESSIBILITY_COMPANION",
            ThreeAdaptA11yContract.COMPANION_INFO_CATEGORY,
        )
        assertEquals("org.autojs.plugin.action.WAKE", ThreeAdaptA11yContract.WAKE_ACTION)
        assertEquals(
            "https://github.com/SuperMonster003/AutoJs6-Plugin-Three-Adapt-A11y",
            ThreeAdaptA11yContract.PROJECT_URL,
        )
        assertEquals(3, ThreeAdaptA11yContract.CONTRACT_VERSION)
        assertEquals(3923, ThreeAdaptA11yContract.REQUIRED_HOST_VERSION)
    }

    @Test
    fun compatibilityComponentUsesTruthfulApplicationIdAndRequiredServiceClass() {
        assertEquals(
            "com.google.android.accessibility.selecttospeak.SelectToSpeakService",
            ThreeAdaptA11yContract.COMPATIBILITY_SERVICE_CLASS,
        )
        assertEquals(
            "io.github.supermonster003.autojs6.plugin.three.adapt.a11y/" +
                "com.google.android.accessibility.selecttospeak.SelectToSpeakService",
            ThreeAdaptA11yContract.expectedServiceId(),
        )
        assertEquals(
            "example.application/com.google.android.accessibility.selecttospeak.SelectToSpeakService",
            ThreeAdaptA11yContract.expectedServiceId("example.application"),
        )
    }

    @Test
    fun capabilityKeysAreStableAndDoNotCollide() {
        val keys = listOf(
            ThreeAdaptA11yContract.CAPABILITY_CONTRACT_VERSION,
            ThreeAdaptA11yContract.CAPABILITY_MODE,
            ThreeAdaptA11yContract.CAPABILITY_SERVICE_COMPONENT,
            ThreeAdaptA11yContract.CAPABILITY_TARGET_PACKAGES,
            ThreeAdaptA11yContract.CAPABILITY_SERVICE_POLICY,
        )

        assertEquals(keys.size, keys.toSet().size)
        keys.forEach { key ->
            assertTrue(key.startsWith("org.autojs.plugin.accessibility.compat."))
        }
        assertNotEquals(
            ThreeAdaptA11yContract.CAPABILITY_MODE,
            ThreeAdaptA11yContract.MODE_GLOBAL_EXPOSURE_TRIGGER,
        )
        assertEquals("global-exposure-trigger", ThreeAdaptA11yContract.MODE_GLOBAL_EXPOSURE_TRIGGER)
        assertEquals(
            "org.autojs.plugin.accessibility.compat.SERVICE_POLICY",
            ThreeAdaptA11yContract.CAPABILITY_SERVICE_POLICY,
        )
    }

    @Test
    fun hostFacingValuesComeFromTheSharedCompanionContract() {
        assertEquals(AutoJs6AccessibilityCompanionContract.HOST_PACKAGE_NAME, ThreeAdaptA11yContract.HOST_PACKAGE_NAME)
        assertEquals("org.autojs.autojs6", ThreeAdaptA11yContract.HOST_PACKAGE_NAME)
        assertEquals(
            AutoJs6AccessibilityCompanionContract.ACTION_HOST_SERVICE_STATE_CHANGED,
            ThreeAdaptA11yContract.HOST_SERVICE_STATE_ACTION,
        )
        assertEquals(
            "org.autojs.plugin.action.ACCESSIBILITY_SERVICE_STATE_CHANGED",
            ThreeAdaptA11yContract.HOST_SERVICE_STATE_ACTION,
        )
        assertEquals("hostServiceEnabled", ThreeAdaptA11yContract.HOST_SERVICE_STATE_EXTRA_ENABLED)
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
