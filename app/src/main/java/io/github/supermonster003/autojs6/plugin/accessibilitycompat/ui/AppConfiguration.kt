package io.github.supermonster003.autojs6.plugin.accessibilitycompat.ui

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.host.HostAvailability
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.host.HostSettingsClient
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.host.HostSettingsResult
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.CompatSettings
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.CompatSettingsStore
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.SettingsPolicy
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
        val dark = SettingsPolicy.resolveDark(resolved.settings.darkMode, snapshot?.darkModePolicy, systemDark)
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
     * transient failure to reach the host (an update in progress, say) keep the previous value so
     * that an open dialog is not thrown away by a needless recreate.
     */
    fun appearanceSignature(context: Context, previous: Int? = null): Int {
        val settings = CompatSettingsStore(context).load()
        var signature = listOf(settings.language, settings.darkMode, settings.themeColor).hashCode()
        if (settings.followsHostAppearance) {
            val host = HostSettingsClient.query(context)
            if (host.availability == HostAvailability.CONTRACT_UNAVAILABLE && previous != null) return previous
            signature = 31 * signature + listOf(
                host.availability,
                host.snapshot?.themeColorPrimary,
                host.snapshot?.darkModePolicy,
                host.snapshot?.resolvedLanguageTag,
            ).hashCode()
        }
        return signature
    }
}
