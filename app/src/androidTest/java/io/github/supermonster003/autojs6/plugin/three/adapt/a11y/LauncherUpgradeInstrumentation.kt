package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import android.app.Instrumentation
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import org.json.JSONObject

/** Framework-only AVD fixture: also runs against a pre-Material target APK with no AndroidX Trace. */
class LauncherUpgradeInstrumentation : Instrumentation() {
    private lateinit var args: Bundle
    override fun onCreate(arguments: Bundle?) { super.onCreate(arguments); args = arguments ?: Bundle(); start() }
    override fun onStart() {
        val result = Bundle()
        try {
            check(Build.HARDWARE in listOf("ranchu", "goldfish")) { "AVD-only upgrade fixture" }
            val requested = requireNotNull(args.getString("prepareLauncherState"))
            require(requested in listOf("defaults", "explicit-dark", "mixed-dark"))
            val app = targetContext
            val pm = app.packageManager
            val pkg = pm.getPackageInfo(app.packageName, 0)
            val build = if (Build.VERSION.SDK_INT >= 28) pkg.longVersionCode else pkg.versionCode.toLong()
            args.getString("expectedLauncherBuild")?.let { check(build == it.toLong()) { "Unexpected old build $build" } }
            val names = listOf("AdaptiveLightIconAlias", "AdaptiveDarkIconAlias", "AdaptiveAutoIconAlias", "TransparentIconAlias")
            val states = names.associateWith {
                when { requested == "defaults" -> PackageManager.COMPONENT_ENABLED_STATE_DEFAULT
                    it == "AdaptiveDarkIconAlias" -> PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                    requested == "explicit-dark" -> PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    else -> PackageManager.COMPONENT_ENABLED_STATE_DEFAULT }
            }
            fun component(name: String) = ComponentName(app.packageName, "${app.packageName}.launcher.$name")
            for (name in names.sortedBy { if (it == "AdaptiveDarkIconAlias") 0 else 1 }) {
                pm.setComponentEnabledSetting(component(name), states.getValue(name), PackageManager.DONT_KILL_APP)
            }
            for ((name, state) in states) check(state == pm.getComponentEnabledSetting(component(name))) { "State mismatch for $name" }
            val entries = pm.queryIntentActivities(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setPackage(app.packageName), 0)
            check(entries.size == 1 && entries.single().activityInfo.name == component("AdaptiveDarkIconAlias").className)
            result.putString("launcherPrepared", JSONObject().apply {
                put("package", app.packageName); put("build", build); put("requested", requested); put("states", JSONObject(states))
            }.toString())
            finish(-1, result)
        } catch (failure: Throwable) {
            result.putString("launcherPreparationError", failure.javaClass.simpleName + ": " + failure.message)
            finish(0, result)
        }
    }
}
