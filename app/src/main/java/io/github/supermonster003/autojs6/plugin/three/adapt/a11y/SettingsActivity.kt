package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.annotation.StringRes
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control.CompatServiceController
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control.ServiceState
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.host.HostSettingsClient
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.host.HostSettingsResult
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.AppDarkMode
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.AppLanguage
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.AppThemeColor
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.CompatSettings
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.HostDarkModePolicy
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.ServicePolicy
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.SettingsPolicy
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui.ColorPolicy
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui.ThemedActivity
import java.util.Locale

internal class SettingsActivity : ThemedActivity() {
    private lateinit var settings: CompatSettings
    private lateinit var hostResult: HostSettingsResult
    private var managerDialog: CompatServiceManagerDialog? = null
    private var advancedProtectionRow: LinearLayout? = null
    private var launcherIconRow: LinearLayout? = null
    internal var launcherIconDialog: android.app.AlertDialog? = null; private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = settingsStore.load()
        hostResult = HostSettingsClient.query(this)
        setContentView(screen(toolbar(getString(R.string.settings_title), showBack = true)) { buildContent() })
    }

    override fun onDestroy() {
        launcherIconDialog?.dismiss()
        managerDialog?.dismiss()
        managerDialog = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        advancedProtectionRow?.let { row ->
            rowSummary(row)?.text = AdvancedProtectionNotice.summary(this)
        }
    }

    private fun LinearLayout.buildContent() {
        addView(sectionTitle(getString(R.string.settings_section_appearance)))
        addView(card {
            addView(row(getString(R.string.settings_language), languageSummary(), R.drawable.ic_language) { showLanguageDialog() })
            addView(divider(56))
            addView(row(getString(R.string.settings_dark_mode), darkModeSummary(), R.drawable.ic_dark_mode) { showDarkModeDialog() })
            addView(divider(56))
            addView(row(getString(R.string.settings_theme_color), themeSummary(), R.drawable.ic_palette, trailing = colorSwatch(palette.accent, 24)) { showThemeColorDialog() })
            addView(divider(56))
            launcherIconRow = row(getString(R.string.launcher_icon_title),
                getString(launcherIconLabels[LauncherIcons.current(this@SettingsActivity).ordinal]), R.drawable.ic_palette) { showLauncherIconDialog() }
                .apply { tag = "launcher-icon" }
            addView(launcherIconRow)
        })

        addView(sectionTitle(getString(R.string.settings_section_service)))
        addView(card {
            advancedProtectionRow = row(getString(R.string.a11y_advanced_protection_title), AdvancedProtectionNotice.summary(this@SettingsActivity), R.drawable.ic_shield) {
                AccessibilitySettingsLauncher.open(this@SettingsActivity)
            }
            addView(advancedProtectionRow)
            addView(divider(56))
            lateinit var managerRow: LinearLayout
            managerRow = row(getString(R.string.settings_service_manager), serviceSummary(), R.drawable.ic_accessibility) {
                managerDialog = CompatServiceManagerDialog.show(this@SettingsActivity) {
                    settings = settingsStore.load()
                    rowSummary(managerRow)?.text = serviceSummary()
                }
            }
            addView(managerRow)
            addView(divider(56))
            addView(row(getString(R.string.settings_system_accessibility), getString(R.string.settings_system_accessibility_summary), R.drawable.ic_tune) {
                AccessibilitySettingsLauncher.open(this@SettingsActivity)
            })
        })

        addView(sectionTitle(getString(R.string.settings_section_information)))
        addView(card {
            addView(documentRow(R.string.mechanism_title, R.drawable.ic_extension, DocumentActivity.Document.MECHANISM))
            addView(divider(56))
            addView(documentRow(R.string.privacy_title, R.drawable.ic_shield, DocumentActivity.Document.PRIVACY))
            addView(divider(56))
            addView(documentRow(R.string.limitations_title, R.drawable.ic_block, DocumentActivity.Document.LIMITATIONS))
            addView(divider(56))
            addView(row(getString(R.string.release_history), getString(R.string.settings_release_history_summary), R.drawable.ic_history) {
                startActivity(DocumentActivity.intent(this@SettingsActivity, DocumentActivity.Document.RELEASE_HISTORY))
            })
            addView(divider(56))
            addView(row(getString(R.string.settings_about), getString(R.string.settings_about_summary), R.drawable.ic_info) {
                startActivity(Intent(this@SettingsActivity, AboutActivity::class.java))
            })
        })
    }

    private fun documentRow(@StringRes titleRes: Int, iconRes: Int, document: DocumentActivity.Document) =
        row(getString(titleRes), null, iconRes) { startActivity(DocumentActivity.intent(this, document)) }

    /* Summaries. */

    private fun languageSummary(): String = when (settings.language) {
        AppLanguage.FOLLOW_AUTOJS6 -> followSummary(resolvedHostLanguageLabel())
        else -> getString(settings.language.labelRes())
    }

    private fun darkModeSummary(): String = when (settings.darkMode) {
        AppDarkMode.FOLLOW_AUTOJS6 -> followSummary(getString(hostResult.snapshot?.darkModePolicy?.labelRes() ?: R.string.settings_follow_system))
        else -> getString(settings.darkMode.labelRes())
    }

    private fun themeSummary(): String = when (settings.themeColor) {
        AppThemeColor.FOLLOW_AUTOJS6 -> followSummary(ColorPolicy.hex(followedThemeSeed()))
        else -> "${getString(settings.themeColor.labelRes())} (${ColorPolicy.hex(requireNotNull(settings.themeColor.seed))})"
    }

    private fun serviceSummary(): String {
        val policy = getString(settings.servicePolicy.labelRes())
        val state = when (CompatServiceController(this).serviceState()) {
            ServiceState.ENABLED -> R.string.value_enabled
            ServiceState.ENABLED_NOT_BOUND -> R.string.value_enabled_not_bound
            ServiceState.DISABLED -> R.string.value_disabled
        }
        return "$policy (${getString(state)})"
    }

    private fun followSummary(resolved: String): String =
        if (hostResult.selectable) getString(R.string.settings_follow_autojs6_summary, resolved)
        else getString(R.string.settings_follow_autojs6_unavailable, resolved)

    private fun followedThemeSeed(): Int =
        SettingsPolicy.resolveThemeSeed(AppThemeColor.FOLLOW_AUTOJS6, hostResult.snapshot?.themeColorPrimary)

    private fun resolvedHostLanguageLabel(): String {
        val tag = hostResult.snapshot?.resolvedLanguageTag?.takeIf(String::isNotBlank)
            ?: return getString(R.string.settings_follow_system)
        SettingsPolicy.languageForTag(tag)?.let { return getString(it.labelRes()) }
        return Locale.forLanguageTag(tag).getDisplayName(resources.configuration.locales[0]).ifBlank { tag }
    }

    /* Dialogs. */

    private fun showLauncherIconDialog() {
        launcherIconDialog = singleChoiceDialog(getString(R.string.launcher_icon_title), LauncherIconMode.entries.map { mode ->
            Choice(getString(launcherIconLabels[mode.ordinal]), when (mode) {
                LauncherIconMode.AUTO -> getString(R.string.launcher_icon_auto_note)
                LauncherIconMode.TRANSPARENT -> getString(R.string.launcher_icon_transparent_note)
                else -> null
            })
        }, LauncherIcons.current(this).ordinal) { index ->
            val result = runCatching { LauncherIcons.select(this, LauncherIconMode.entries[index]) }
            toast(getString(if (result.isSuccess) R.string.launcher_icon_applied_note else R.string.launcher_icon_failed), long = true)
            launcherIconRow?.let { rowSummary(it)?.text = getString(launcherIconLabels[LauncherIcons.current(this).ordinal]) }
        }
    }

    internal val launcherIconLabels = listOf(R.string.launcher_icon_light, R.string.launcher_icon_dark,
        R.string.launcher_icon_auto, R.string.launcher_icon_transparent)

    private fun showLanguageDialog() {
        val values = AppLanguage.entries
        singleChoiceDialog(
            title = getString(R.string.settings_language),
            choices = values.map { value ->
                val label = getString(value.labelRes())
                if (value == AppLanguage.FOLLOW_AUTOJS6) {
                    Choice(label, followChoiceSummary(resolvedHostLanguageLabel(), getString(R.string.settings_follow_system)))
                } else {
                    Choice(label)
                }
            },
            checkedIndex = values.indexOf(settings.language),
        ) { index -> save(settings.copy(language = values[index])) }
    }

    private fun showDarkModeDialog() {
        val values = AppDarkMode.entries
        singleChoiceDialog(
            title = getString(R.string.settings_dark_mode),
            choices = values.map { value ->
                val label = getString(value.labelRes())
                if (value == AppDarkMode.FOLLOW_AUTOJS6) {
                    val resolved = getString(hostResult.snapshot?.darkModePolicy?.labelRes() ?: R.string.settings_follow_system)
                    Choice(label, followChoiceSummary(resolved, getString(R.string.settings_follow_system)))
                } else {
                    Choice(label)
                }
            },
            checkedIndex = values.indexOf(settings.darkMode),
        ) { index -> save(settings.copy(darkMode = values[index])) }
    }

    private fun showThemeColorDialog() {
        val values = AppThemeColor.entries
        singleChoiceDialog(
            title = getString(R.string.settings_theme_color),
            choices = values.map { value ->
                val label = getString(value.labelRes())
                if (value == AppThemeColor.FOLLOW_AUTOJS6) {
                    val seed = followedThemeSeed()
                    Choice(label, followChoiceSummary(ColorPolicy.hex(seed), ColorPolicy.hex(SettingsPolicy.AUTOJS6_DEFAULT_THEME_COLOR)), seed)
                } else {
                    val seed = requireNotNull(value.seed)
                    Choice(label, ColorPolicy.hex(seed), seed)
                }
            },
            checkedIndex = values.indexOf(settings.themeColor),
        ) { index -> save(settings.copy(themeColor = values[index])) }
    }

    /** The follow choice shows what it resolves to, or the fallback used while the host is absent. */
    private fun followChoiceSummary(resolved: String, fallback: String): String =
        if (hostResult.selectable) resolved else getString(R.string.settings_follow_autojs6_unavailable, fallback)

    private fun save(updated: CompatSettings) {
        settingsStore.save(updated)
        settings = updated
        HostSettingsClient.invalidate()
        toast(getString(R.string.settings_saved))
        recreate()
    }
}

@StringRes
internal fun AppLanguage.labelRes(): Int = when (this) {
    AppLanguage.FOLLOW_AUTOJS6 -> R.string.settings_follow_autojs6
    AppLanguage.FOLLOW_SYSTEM -> R.string.settings_follow_system
    AppLanguage.ZH_HANS -> R.string.language_name_zh_hans
    AppLanguage.ZH_HANT_HK -> R.string.language_name_zh_hant_hk
    AppLanguage.ZH_HANT_TW -> R.string.language_name_zh_hant_tw
    AppLanguage.EN -> R.string.language_name_en
    AppLanguage.FR -> R.string.language_name_fr
    AppLanguage.ES -> R.string.language_name_es
    AppLanguage.JA -> R.string.language_name_ja
    AppLanguage.KO -> R.string.language_name_ko
    AppLanguage.RU -> R.string.language_name_ru
    AppLanguage.AR -> R.string.language_name_ar
}

@StringRes
internal fun AppDarkMode.labelRes(): Int = when (this) {
    AppDarkMode.FOLLOW_AUTOJS6 -> R.string.settings_follow_autojs6
    AppDarkMode.FOLLOW_SYSTEM -> R.string.settings_follow_system
    AppDarkMode.LIGHT -> R.string.settings_light
    AppDarkMode.DARK -> R.string.settings_dark
}

@StringRes
internal fun HostDarkModePolicy.labelRes(): Int = when (this) {
    HostDarkModePolicy.FOLLOW_SYSTEM -> R.string.settings_follow_system
    HostDarkModePolicy.LIGHT -> R.string.settings_light
    HostDarkModePolicy.DARK -> R.string.settings_dark
}

@StringRes
internal fun AppThemeColor.labelRes(): Int = when (this) {
    AppThemeColor.FOLLOW_AUTOJS6 -> R.string.settings_follow_autojs6
    AppThemeColor.DEFAULT -> R.string.theme_color_default
    AppThemeColor.TEAL -> R.string.theme_color_teal
    AppThemeColor.GREEN -> R.string.theme_color_green
    AppThemeColor.ORANGE -> R.string.theme_color_orange
    AppThemeColor.PURPLE -> R.string.theme_color_purple
    AppThemeColor.RED -> R.string.theme_color_red
}

@StringRes
internal fun ServicePolicy.labelRes(): Int = when (this) {
    ServicePolicy.FOLLOW_HOST -> R.string.policy_follow_host
    ServicePolicy.ENABLED -> R.string.policy_enabled
    ServicePolicy.DISABLED -> R.string.policy_disabled
}
