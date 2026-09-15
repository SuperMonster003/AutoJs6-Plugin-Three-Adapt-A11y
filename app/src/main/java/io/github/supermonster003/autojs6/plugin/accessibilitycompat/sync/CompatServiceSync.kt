package io.github.supermonster003.autojs6.plugin.accessibilitycompat.sync

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Secure
import android.util.Log
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.control.CompatServiceController
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.control.SyncOutcome
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.CompatSettingsStore
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.ServicePolicy
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
            onDone?.let { callback -> mainHandler.post { callback(outcome) } }
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
