package io.github.supermonster003.autojs6.plugin.accessibilitycompat

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AccessibilityServiceStatusTest {

    @Test
    fun exactServiceIdIsRecognizedAmongUnrelatedServices() {
        val expected = AccessibilityCompatContract.expectedServiceId()
        val enabled = listOf(
            "example.reader/example.reader.ReaderService",
            expected,
            "example.automation/example.automation.AutomationService",
        )

        assertTrue(AccessibilityServiceStatus.isExpectedServiceEnabled(enabled))
    }

    @Test
    fun emptyAndNearMatchServiceIdsAreRejected() {
        val serviceClass = AccessibilityCompatContract.COMPATIBILITY_SERVICE_CLASS
        val expected = AccessibilityCompatContract.expectedServiceId()
        val nearMatches = listOf(
            "",
            " ${expected}",
            "${expected} ",
            expected.uppercase(),
            "other.application/$serviceClass",
            "${AccessibilityCompatContract.APPLICATION_ID}/.$serviceClass",
            "${AccessibilityCompatContract.APPLICATION_ID}/SelectToSpeakService",
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
        val installedApplicationId = "io.github.supermonster003.autojs6.plugin.accessibilitycompat.debug"
        val installedServiceId = AccessibilityCompatContract.expectedServiceId(installedApplicationId)

        assertTrue(
            AccessibilityServiceStatus.isExpectedServiceEnabled(
                listOf(installedServiceId),
                installedApplicationId,
            ),
        )
        assertFalse(AccessibilityServiceStatus.isExpectedServiceEnabled(listOf(installedServiceId)))
    }
}
