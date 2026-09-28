package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.sync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ThreeAdaptA11yContract

/**
 * Receives the permission-protected broadcast AutoJs6 sends after its own accessibility service was
 * bound or unbound, and re-applies the selected policy. Only the action is trusted; the extras are
 * informational because the controller reads the real system state itself.
 */
class HostServiceStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ThreeAdaptA11yContract.HOST_SERVICE_STATE_ACTION) return
        val pendingResult = goAsync()
        CompatServiceSync.request(context, "host-broadcast") { pendingResult.finish() }
    }
}
