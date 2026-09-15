package io.github.supermonster003.autojs6.plugin.accessibilitycompat

import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.ServicePolicy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PluginRuntimeInfoTest {

    @Test
    fun runtimeFieldsDefaultToCurrentContractAndSupportedPackages() {
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
        assertEquals(AccessibilityCompatContract.SUPPORTED_PACKAGES, fields.targetPackages)
        assertEquals(ServicePolicy.FOLLOW_HOST, fields.servicePolicy)
        assertEquals("follow-host", fields.servicePolicy.contractValue)
        assertEquals("accessibility-compat", fields.id)
        assertEquals("accessibility", fields.engine)
        assertEquals("service-identity", fields.variant)
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
            servicePolicy = ServicePolicy.DISABLED,
        )

        assertEquals(99, fields.requiredHostVersion)
        assertEquals(7, fields.contractVersion)
        assertEquals(listOf("one.package", "two.package"), fields.targetPackages)
        assertEquals(ServicePolicy.DISABLED, fields.servicePolicy)
    }

    @Test
    fun instructionReferenceIsAnEmbeddedRawResource() {
        assertEquals("@raw/plugin_instruction", PLUGIN_INSTRUCTION_REFERENCE)
        assertTrue(PLUGIN_INSTRUCTION_REFERENCE.startsWith("@raw/"))
    }
}
