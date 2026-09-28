package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import android.app.Activity
import android.os.Bundle
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.sync.CompatServiceSync

/**
 * Activation entry point started by AutoJs6. Besides leaving the stopped state, the plugin uses the
 * wake-up to re-apply the selected service policy in the background.
 */
class WakeActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CompatServiceSync.request(applicationContext, "wake")
        finish()
    }
}
