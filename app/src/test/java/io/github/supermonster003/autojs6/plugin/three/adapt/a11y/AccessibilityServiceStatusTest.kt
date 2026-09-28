package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AccessibilityServiceStatusTest {

    @Test
    fun exactServiceIdIsRecognizedAmongUnrelatedServices() {
        val expected = ThreeAdaptA11yContract.expectedServiceId()
        val enabled = listOf(
            "example.reader/example.reader.ReaderService",
            expected,
            "example.automation/example.automation.AutomationService",
        )

        assertTrue(AccessibilityServiceStatus.isExpectedServiceEnabled(enabled))
    }

    @Test
    fun emptyAndNearMatchServiceIdsAreRejected() {
        val serviceClass = ThreeAdaptA11yContract.COMPATIBILITY_SERVICE_CLASS
        val expected = ThreeAdaptA11yContract.expectedServiceId()
        val nearMatches = listOf(
            "",
            " ${expected}",
            "${expected} ",
            expected.uppercase(),
            "other.application/$serviceClass",
            "${ThreeAdaptA11yContract.APPLICATION_ID}/.$serviceClass",
            "${ThreeAdaptA11yContract.APPLICATION_ID}/SelectToSpeakService",
            serviceClass,
        )

        assertFalse(AccessibilityServiceStatus.isExpectedServiceEnabled(emptyList()))
        nearMatches.forEach { serviceId ->
            assertFalse(
                "A non-exact component must not be treated as enabled: $serviceId",
                AccessibilityServiceStatus.isExpectedServiceEnabled(listOf(serviceId)),
            )
        }
    }

    @Test
    fun callerSuppliedApplicationIdIsUsedForInstalledVariant() {
        val installedApplicationId = "io.github.supermonster003.autojs6.plugin.three.adapt.a11y.debug"
        val installedServiceId = ThreeAdaptA11yContract.expectedServiceId(installedApplicationId)

        assertTrue(
            AccessibilityServiceStatus.isExpectedServiceEnabled(
                listOf(installedServiceId),
                installedApplicationId,
            ),
        )
        assertFalse(AccessibilityServiceStatus.isExpectedServiceEnabled(listOf(installedServiceId)))
    }
}
