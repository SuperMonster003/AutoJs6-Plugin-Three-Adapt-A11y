package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui

import android.content.Context
import android.content.res.Configuration
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.R
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ThemeAccentRoles
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.host.HostSettingsClient
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.CompatSettingsStore
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.SettingsPolicy

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
    val primary: Int,
    val onPrimary: Int,
    val accent: Int,
    val onAccent: Int,
    val accentSurface: Int,
    val positive: Int,
    val warning: Int,
) {
    companion object {
        fun resolve(context: Context): Palette {
            val settings = CompatSettingsStore(context).load()
            val host = if (settings.followsHostAppearance) HostSettingsClient.query(context).snapshot else null
            val seed = SettingsPolicy.resolveThemeSeed(settings.themeColor, host?.themeColorPrimary, settings.customThemeColor)
            val accentSeed = if (settings.themeColor == io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.AppThemeColor.FOLLOW_AUTOJS6)
                host?.themeColorAccent ?: seed else seed
            return resolve(context, seed, accentSeed)
        }

        fun resolve(context: Context, seed: Int, accentSeed: Int = seed): Palette {
            val isDark = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                Configuration.UI_MODE_NIGHT_YES
            val background = context.getColor(R.color.theme_background)
            val primaryText = context.getColor(R.color.text_primary)
            val roles = ThemeAccentRoles.fromSeed(seed, isDark)
            val accentRoles = ThemeAccentRoles.fromSeed(accentSeed, isDark)
            var accent = ColorPolicy.readable(accentRoles.primary, background)
            repeat(8) {
                for (reference in listOf(background, context.getColor(R.color.surface), context.getColor(R.color.surface_variant),
                    ColorPolicy.blend(background, accent, if (isDark) 0.16 else 0.10))) {
                    accent = ColorPolicy.readable(accent, reference)
                }
            }
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
                primary = roles.primary,
                onPrimary = roles.onPrimary,
                accent = accent,
                onAccent = accentRoles.onPrimary,
                accentSurface = ColorPolicy.blend(background, accent, if (isDark) 0.16 else 0.10),
                positive = context.getColor(R.color.state_positive),
                warning = context.getColor(R.color.state_warning),
            )
        }
    }
}
