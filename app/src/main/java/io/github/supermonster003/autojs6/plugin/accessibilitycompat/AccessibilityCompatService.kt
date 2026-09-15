package io.github.supermonster003.autojs6.plugin.accessibilitycompat

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Secure
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.sync.CompatServiceSync

/**
 * Privacy-minimal compatibility trigger.
 *
 * The callback deliberately ignores every event. AutoJs6 continues to obtain and act on
 * nodes through its own accessibility service; this service neither reads nor proxies them.
 *
 * While bound, the instance watches the enabled accessibility services so that it can disable
 * itself, without any privilege, when the selected policy no longer wants it running.
 */
open class AccessibilityCompatService : AccessibilityService() {
    private val handler = Handler(Looper.getMainLooper())
    private val settingsObserver = object : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean) {
            CompatServiceSync.request(this@AccessibilityCompatService, "service-observer")
        }
    }

    final override fun onAccessibilityEvent(event: AccessibilityEvent?) = Unit

    final override fun onInterrupt() = Unit

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        runCatching {
            contentResolver.registerContentObserver(
                Secure.getUriFor(Secure.ENABLED_ACCESSIBILITY_SERVICES),
                false,
                settingsObserver,
            )
        }.onFailure { Log.w(TAG, "Unable to observe the enabled accessibility services", it) }
        CompatServiceSync.request(this, "service-connected")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        dispose()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        dispose()
        super.onDestroy()
    }

    private fun dispose() {
        runCatching { contentResolver.unregisterContentObserver(settingsObserver) }
        if (instance === this) instance = null
    }

    companion object {
        private const val TAG = "AccessibilityCompatService"

        @Volatile
        private var instance: AccessibilityCompatService? = null

        val isBound: Boolean
            get() = instance != null

        /** Asks the bound instance to disable itself; returns whether the request was issued. */
        fun disableSelfIfBound(): Boolean {
            val service = instance ?: return false
            return runCatching {
                service.disableSelf()
                true
            }.onFailure { Log.w(TAG, "Unable to disable the compatibility service", it) }.getOrDefault(false)
        }
    }
}
