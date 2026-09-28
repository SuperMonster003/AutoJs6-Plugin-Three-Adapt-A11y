package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.view.accessibility.AccessibilityManager

object AccessibilityServiceStatus {
    fun isEnabled(context: Context): Boolean {
        val manager = context.getSystemService(AccessibilityManager::class.java) ?: return false
        return try {
            isExpectedServiceEnabled(
                manager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
                    .mapNotNull { it.id },
                context.packageName,
            )
        } catch (_: RuntimeException) {
            false
        }
    }

    fun isExpectedServiceEnabled(
        enabledServiceIds: Iterable<String>,
        applicationId: String = ThreeAdaptA11yContract.APPLICATION_ID,
    ): Boolean {
        val expected = ThreeAdaptA11yContract.expectedServiceId(applicationId)
        return enabledServiceIds.any { it == expected }
    }
}
