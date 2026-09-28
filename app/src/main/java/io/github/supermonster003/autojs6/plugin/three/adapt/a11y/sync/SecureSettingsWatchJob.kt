package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.sync

import android.app.job.JobParameters
import android.app.job.JobService

/**
 * Fired by the system when the enabled accessibility services change while the policy follows
 * AutoJs6. Content-triggered jobs are one-shot, so the sync runner re-schedules it afterwards.
 */
class SecureSettingsWatchJob : JobService() {
    override fun onStartJob(params: JobParameters): Boolean {
        CompatServiceSync.request(this, "settings-watcher") { jobFinished(params, false) }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean = false
}
