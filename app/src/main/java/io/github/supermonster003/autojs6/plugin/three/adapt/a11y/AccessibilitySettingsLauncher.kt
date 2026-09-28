package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast

/** Opens the Android page of the compatibility service, falling back to the accessibility list. */
internal object AccessibilitySettingsLauncher {
    private const val ACTION_ACCESSIBILITY_DETAILS_SETTINGS = "android.settings.ACCESSIBILITY_DETAILS_SETTINGS"
    private const val EXTRA_COMPONENT_NAME = "android.intent.extra.COMPONENT_NAME"

    fun open(context: Context): Boolean {
        val component = ComponentName(context.packageName, ThreeAdaptA11yContract.COMPATIBILITY_SERVICE_CLASS)
        val intents = listOf(
            Intent(ACTION_ACCESSIBILITY_DETAILS_SETTINGS).putExtra(EXTRA_COMPONENT_NAME, component.flattenToString()),
            Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS),
        )
        val opened = startFirstAvailable(context, intents)
        if (!opened) Toast.makeText(context, R.string.settings_unavailable, Toast.LENGTH_LONG).show()
        return opened
    }

    fun startFirstAvailable(context: Context, intents: Iterable<Intent>): Boolean {
        for (intent in intents) {
            try {
                context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return true
            } catch (_: ActivityNotFoundException) {
                // Try the next, more general entry point.
            } catch (_: SecurityException) {
                // Some OEMs expose the action but block third-party callers.
            } catch (_: RuntimeException) {
                // A broken OEM settings activity must not crash this companion.
            }
        }
        return false
    }
}
