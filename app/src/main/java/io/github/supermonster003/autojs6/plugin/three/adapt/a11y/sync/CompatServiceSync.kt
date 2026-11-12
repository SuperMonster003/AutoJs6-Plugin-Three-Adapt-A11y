package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.sync

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Secure
import android.util.Log
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control.CompatServiceController
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control.SyncOutcome
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.CompatSettingsStore
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.ServicePolicy
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.shizuku.ShizukuShell
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.shizuku.ShizukuState
import java.util.concurrent.Executors

/**
 * Serial background runner shared by every passive trigger (host broadcast, wake, settings watcher,
 * service callbacks). Passive runs never open UI; screens call [CompatServiceController.sync]
 * themselves when they need the outcome for a message.
 */
internal object CompatServiceSync {
    private const val TAG = "CompatServiceSync"
    private const val WATCH_JOB_ID = 0x0A11
    private const val WATCH_UPDATE_DELAY_MILLIS = 300L
    private const val WATCH_MAX_DELAY_MILLIS = 3_000L

    private val executor = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "compat-service-sync").apply { isDaemon = true }
    }
    private val mainHandler = Handler(Looper.getMainLooper())

    fun request(context: Context, trigger: String, onDone: ((SyncOutcome) -> Unit)? = null) {
        val appContext = context.applicationContext
        executor.execute {
            val outcome = runCatching { CompatServiceController(appContext).sync() }
                .onFailure { Log.w(TAG, "Sync triggered by $trigger failed", it) }
                .getOrDefault(SyncOutcome.NO_CHANGE)
            Log.d(TAG, "Sync triggered by $trigger: $outcome")
            runCatching { ensureWatcher(appContext) }
            if (outcome.requiresManualAction) runCatching { retryWhenShizukuArrives(appContext) }
            onDone?.let { callback -> mainHandler.post { callback(outcome) } }
        }
    }

    /**
     * A pass that found no usable method while Shizuku is allowed but not reachable yet runs again
     * once Shizuku delivers its binder: in a process just started by a broadcast or the watcher job,
     * the binder arrives a moment after the first pass (Q108). Shizuku that is not started never
     * delivers one, and a pass that fails with Shizuku reachable does not register again.
     */
    private fun retryWhenShizukuArrives(context: Context) {
        if (!CompatSettingsStore(context).load().enableWithShizuku) return
        if (ShizukuShell.state(context) != ShizukuState.NOT_RUNNING) return
        ShizukuShell.runOnceWhenBinderReceived {
            Log.d(TAG, "Shizuku binder received after a pass without a usable method")
            request(context, "shizuku-binder")
        }
    }

    /**
     * Keeps a content-triggered job alive while the policy follows AutoJs6, so that a change of the
     * enabled accessibility services wakes this process even when nothing else runs.
     */
    fun ensureWatcher(context: Context) {
        val appContext = context.applicationContext
        val scheduler = appContext.getSystemService(JobScheduler::class.java) ?: return
        if (CompatSettingsStore(appContext).load().servicePolicy != ServicePolicy.FOLLOW_HOST) {
            scheduler.cancel(WATCH_JOB_ID)
            return
        }
        val job = JobInfo.Builder(WATCH_JOB_ID, ComponentName(appContext, SecureSettingsWatchJob::class.java))
            .addTriggerContentUri(JobInfo.TriggerContentUri(Secure.getUriFor(Secure.ENABLED_ACCESSIBILITY_SERVICES), 0))
            .setTriggerContentUpdateDelay(WATCH_UPDATE_DELAY_MILLIS)
            .setTriggerContentMaxDelay(WATCH_MAX_DELAY_MILLIS)
            .build()
        runCatching { scheduler.schedule(job) }
            .onFailure { Log.w(TAG, "Unable to schedule the settings watcher", it) }
    }
}
