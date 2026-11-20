package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import android.os.Bundle
import androidx.activity.compose.setContent
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui.ReleaseHistoryColors
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui.ReleaseHistoryScreen
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui.ReleaseHistoryTheme
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui.ThemedActivity

/**
 * Renders the bundled changelog for the current locale (English when that one is missing) as one group per released
 * version, under the title "Release history" with the plugin name as the subtitle
 * (AUTOJS6_PLUGIN_RELEASE_HISTORY_AGENTS.md); reached from the settings page. Not exported and takes no data. The
 * language, night mode and colors are those of the settings page ([ThemedActivity]), which recreates the page when
 * they change.
 */
internal class ReleaseHistoryActivity : ThemedActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val candidates = ReleaseHistoryAssetPolicy.candidatesFor(resources.configuration.locales[0])
        val entries = loadReleaseHistory(candidates) { path ->
            assets.open(path).use { stream -> stream.readBytes().toString(Charsets.UTF_8) }
        }
        val colors = ReleaseHistoryColors(
            dark = palette.isDark,
            primary = palette.accent,
            primaryContainer = palette.accentContainer,
            // The settings page draws its top bar on the page background; this page does the same.
            background = palette.background,
            surface = palette.background,
            onSurface = palette.primaryText,
            onSurfaceVariant = palette.secondaryText,
            outlineVariant = palette.divider,
        )
        setContent {
            ReleaseHistoryTheme(colors) {
                ReleaseHistoryScreen(entries = entries, onBack = ::finish)
            }
        }
    }
}
