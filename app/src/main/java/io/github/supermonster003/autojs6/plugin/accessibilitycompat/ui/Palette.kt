package io.github.supermonster003.autojs6.plugin.accessibilitycompat.ui

import android.content.Context
import android.content.res.Configuration
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.R
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.host.HostSettingsClient
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.CompatSettingsStore
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.SettingsPolicy

/** Resolved colors of one screen; neutrals come from resources, the accent from the theme seed. */
internal data class Palette(
    val isDark: Boolean,
    val background: Int,
    val surface: Int,
    val surfaceVariant: Int,
    val outline: Int,
    val divider: Int,
    val primaryText: Int,
    val secondaryText: Int,
    val seed: Int,
    val accent: Int,
    val onAccent: Int,
    val accentSurface: Int,
    val positive: Int,
    val warning: Int,
) {
    companion object {
        fun resolve(context: Context): Palette {
            val settings = CompatSettingsStore(context).load()
            val hostColor = if (settings.followsHostAppearance) {
                HostSettingsClient.query(context).snapshot?.themeColorPrimary
            } else {
                null
            }
            return resolve(context, SettingsPolicy.resolveThemeSeed(settings.themeColor, hostColor))
        }

        fun resolve(context: Context, seed: Int): Palette {
            val isDark = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                Configuration.UI_MODE_NIGHT_YES
            val background = context.getColor(R.color.theme_background)
            val primaryText = context.getColor(R.color.text_primary)
            val accent = ColorPolicy.readable(seed, background)
            return Palette(
                isDark = isDark,
                background = background,
                surface = context.getColor(R.color.surface),
                surfaceVariant = context.getColor(R.color.surface_variant),
                outline = context.getColor(R.color.outline),
                divider = context.getColor(R.color.divider),
                primaryText = primaryText,
                secondaryText = context.getColor(R.color.text_secondary),
                seed = seed,
                accent = accent,
                onAccent = ColorPolicy.onColor(accent),
                accentSurface = ColorPolicy.blend(background, accent, if (isDark) 0.16 else 0.10),
                positive = context.getColor(R.color.state_positive),
                warning = context.getColor(R.color.state_warning),
            )
        }
    }
}
