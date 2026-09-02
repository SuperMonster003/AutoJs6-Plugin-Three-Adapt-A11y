package io.github.supermonster003.autojs6.plugin.accessibilitycompat

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PluginRuntimeInfoTest {

    @Test
    fun runtimeFieldsDefaultToCurrentContractAndSingleTargetPackage() {
        val fields = PluginRuntimeFields(
            name = "Accessibility Compat",
            description = "Compatibility trigger",
            author = "Author",
            id = AccessibilityCompatContract.PLUGIN_ID,
            engine = AccessibilityCompatContract.PLUGIN_ENGINE,
            variant = AccessibilityCompatContract.PLUGIN_VARIANT,
            versionName = "1.0.0",
            versionCode = 1L,
            versionDate = "Sep 2, 2026",
        )

        assertEquals(AccessibilityCompatContract.REQUIRED_HOST_VERSION, fields.requiredHostVersion)
        assertEquals(AccessibilityCompatContract.CONTRACT_VERSION, fields.contractVersion)
        assertEquals(listOf(AccessibilityCompatContract.TARGET_PACKAGE), fields.targetPackages)
        assertEquals("accessibility-compat", fields.id)
        assertEquals("accessibility", fields.engine)
        assertEquals("wechat", fields.variant)
    }

    @Test
    fun runtimeFieldsPreserveExplicitCapabilityOverrides() {
        val fields = PluginRuntimeFields(
            name = "Name",
            description = "Description",
            author = "Author",
            id = "id",
            engine = "engine",
            variant = "variant",
            versionName = "2.0.0",
            versionCode = 42L,
            versionDate = "Date",
            requiredHostVersion = 99,
            contractVersion = 7,
            targetPackages = listOf("one.package", "two.package"),
        )

        assertEquals(99, fields.requiredHostVersion)
        assertEquals(7, fields.contractVersion)
        assertEquals(listOf("one.package", "two.package"), fields.targetPackages)
    }

    @Test
    fun instructionReferenceIsAnEmbeddedRawResource() {
        assertEquals("@raw/plugin_instruction", PLUGIN_INSTRUCTION_REFERENCE)
        assertTrue(PLUGIN_INSTRUCTION_REFERENCE.startsWith("@raw/"))
    }
}
