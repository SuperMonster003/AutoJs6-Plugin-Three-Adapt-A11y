package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.autojs.plugin.common.api.IPluginInfoProvider

class ThreeAdaptA11yInfoService : Service() {
    private val binder = object : IPluginInfoProvider.Stub() {
        override fun getInfo() = pluginInfo(
            name = getString(R.string.app_name),
            description = getString(R.string.plugin_description),
        ).apply { supportedAbis = emptyArray() }
    }

    override fun onBind(intent: Intent?): IBinder = binder
}
