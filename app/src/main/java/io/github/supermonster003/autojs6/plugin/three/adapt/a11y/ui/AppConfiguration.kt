package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.host.HostAvailability
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.host.HostSettingsClient
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.host.HostSettingsResult
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.CompatSettings
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.CompatSettingsStore
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.SettingsPolicy
import java.util.Locale

internal data class ResolvedAppearance(
    val settings: CompatSettings,
    val host: HostSettingsResult?,
)

internal object AppConfiguration {
    fun resolve(context: Context): ResolvedAppearance {
        val settings = CompatSettingsStore(context).load()
        // The stored "follow AutoJs6" intent survives an absent host: resolution falls back at
        // runtime, so installing AutoJs6 later resumes following without rewriting the preference.
        val host = if (settings.followsHostAppearance) HostSettingsClient.query(context) else null
        return ResolvedAppearance(settings, host)
    }

    /** Wraps [base] so that its resources honour the language and dark-mode settings. */
    fun wrap(base: Context): Context {
        val resolved = resolve(base)
        val snapshot = resolved.host?.snapshot
        val configuration = Configuration(base.resources.configuration)
        val systemDark = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        val dark = SettingsPolicy.resolveDark(resolved.settings.darkMode, snapshot?.darkModePolicy, systemDark, snapshot?.darkModeActive)
        configuration.uiMode = configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK.inv() or
            if (dark) Configuration.UI_MODE_NIGHT_YES else Configuration.UI_MODE_NIGHT_NO
        SettingsPolicy.resolveLanguageTag(resolved.settings.language, snapshot?.resolvedLanguageTag)
            ?.let(Locale::forLanguageTag)
            ?.takeIf { it.language.isNotEmpty() }
            ?.let { locale ->
                configuration.setLocales(LocaleList(locale))
                configuration.setLayoutDirection(locale)
            }
        return base.createConfigurationContext(configuration)
    }

    /**
     * Changes only when something a visible screen depends on changes: the appearance settings and,
     * while following AutoJs6, the host fields they resolve against. Service-control settings and a
     * service controls are excluded. An unavailable snapshot resolves a real fallback; the Activity
     * defers recreation while the user is interacting or has an unconfirmed dialog.
     */
    fun appearanceSignature(context: Context): Int {
        val resolved = resolve(context)
        val settings = resolved.settings
        val host = resolved.host?.snapshot
        val system = context.resources.configuration
        val language = SettingsPolicy.resolveLanguageTag(settings.language, host?.resolvedLanguageTag) ?: system.locales[0].toLanguageTag()
        val dark = SettingsPolicy.resolveDark(settings.darkMode, host?.darkModePolicy,
            system.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES, host?.darkModeActive)
        val seed = SettingsPolicy.resolveThemeSeed(settings.themeColor, host?.themeColorPrimary, settings.customThemeColor)
        val accent = if (settings.themeColor == io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.AppThemeColor.FOLLOW_AUTOJS6)
            host?.themeColorAccent ?: seed else seed
        return listOf(language, dark, seed, accent, resolved.host?.selectable).hashCode()
    }
}
