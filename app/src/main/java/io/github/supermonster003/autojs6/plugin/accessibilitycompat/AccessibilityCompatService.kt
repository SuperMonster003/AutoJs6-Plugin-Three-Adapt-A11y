package io.github.supermonster003.autojs6.plugin.accessibilitycompat

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

/**
 * Privacy-minimal compatibility trigger.
 *
 * The callback deliberately ignores every event. AutoJs6 continues to obtain and act on
 * nodes through its own accessibility service; this service neither reads nor proxies them.
 */
open class AccessibilityCompatService : AccessibilityService() {
    final override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit

    final override fun onInterrupt() = Unit
}
