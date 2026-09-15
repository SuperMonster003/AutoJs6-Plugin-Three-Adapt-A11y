package io.github.supermonster003.autojs6.plugin.accessibilitycompat

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.widget.LinearLayout
import androidx.annotation.StringRes
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.control.CompatServiceController
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.control.ServiceState
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.host.HostSettingsClient
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.host.HostSettingsResult
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.AppDarkMode
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.AppLanguage
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.AppThemeColor
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.CompatSettings
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.HostDarkModePolicy
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.ServicePolicy
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.SettingsPolicy
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.ui.ColorPolicy
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.ui.ThemedActivity
import java.util.Locale

internal class SettingsActivity : ThemedActivity() {
    private lateinit var settings: CompatSettings
    private lateinit var hostResult: HostSettingsResult
    private var managerDialog: CompatServiceManagerDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settings = settingsStore.load()
        hostResult = HostSettingsClient.query(this)
        setContentView(screen(toolbar(getString(R.string.settings_title), showBack = true)) { buildContent() })
    }

    override fun onDestroy() {
        managerDialog?.dismiss()
        managerDialog = null
        super.onDestroy()
    }

    private fun LinearLayout.buildContent() {
        addView(sectionTitle(getString(R.string.settings_section_appearance)))
        addView(card {
            addView(row(getString(R.string.settings_language), languageSummary(), R.drawable.ic_language) { showLanguageDialog() })
            addView(divider(56))
            addView(row(getString(R.string.settings_dark_mode), darkModeSummary(), R.drawable.ic_dark_mode) { showDarkModeDialog() })
            addView(divider(56))
            addView(row(getString(R.string.settings_theme_color), themeSummary(), R.drawable.ic_palette, trailing = colorSwatch()) { showThemeColorDialog() })
        })

        addView(sectionTitle(getString(R.string.settings_section_service)))
        addView(card {
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
            addView(row(getString(R.string.settings_about), aboutSummary(), R.drawable.ic_info) { openProjectPage() })
        })
    }

    private fun documentRow(@StringRes titleRes: Int, iconRes: Int, document: DocumentActivity.Document) =
        row(getString(titleRes), null, iconRes) { startActivity(DocumentActivity.intent(this, document)) }

    private fun colorSwatch() = android.view.View(this).apply {
        background = roundedBackground(palette.accent, 12f, palette.outline)
        layoutParams = lp(dp(24), dp(24))
    }

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

    private fun aboutSummary(): String = getString(
        R.string.settings_about_summary,
        runCatching { packageManager.getPackageInfo(packageName, 0).versionName }.getOrNull().orEmpty(),
        getString(R.string.plugin_author),
    )

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

    private fun showLanguageDialog() {
        val values = AppLanguage.entries
        singleChoiceDialog(
            title = getString(R.string.settings_language),
            labels = values.map { value ->
                val label = getString(value.labelRes())
                if (value == AppLanguage.FOLLOW_AUTOJS6) followChoiceLabel(label, getString(R.string.settings_follow_system)) else label
            },
            checkedIndex = values.indexOf(settings.language),
        ) { index -> save(settings.copy(language = values[index])) }
    }

    private fun showDarkModeDialog() {
        val values = AppDarkMode.entries
        singleChoiceDialog(
            title = getString(R.string.settings_dark_mode),
            labels = values.map { value ->
                val label = getString(value.labelRes())
                if (value == AppDarkMode.FOLLOW_AUTOJS6) followChoiceLabel(label, getString(R.string.settings_follow_system)) else label
            },
            checkedIndex = values.indexOf(settings.darkMode),
        ) { index -> save(settings.copy(darkMode = values[index])) }
    }

    private fun showThemeColorDialog() {
        val values = AppThemeColor.entries
        singleChoiceDialog(
            title = getString(R.string.settings_theme_color),
            labels = values.map { value ->
                val label = getString(value.labelRes())
                when (value) {
                    AppThemeColor.FOLLOW_AUTOJS6 -> followChoiceLabel("$label (${ColorPolicy.hex(followedThemeSeed())})", ColorPolicy.hex(SettingsPolicy.AUTOJS6_DEFAULT_THEME_COLOR))
                    else -> "$label (${ColorPolicy.hex(requireNotNull(value.seed))})"
                }
            },
            checkedIndex = values.indexOf(settings.themeColor),
        ) { index -> save(settings.copy(themeColor = values[index])) }
    }

    /** The follow choice stays selectable while the host is absent; a hint explains the fallback. */
    private fun followChoiceLabel(title: String, fallback: String): CharSequence {
        if (hostResult.selectable) return title
        val hint = getString(R.string.settings_follow_autojs6_unavailable, fallback)
        return SpannableString("$title\n$hint").apply {
            val start = title.length + 1
            setSpan(ForegroundColorSpan(palette.secondaryText), start, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            setSpan(RelativeSizeSpan(0.82f), start, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun save(updated: CompatSettings) {
        settingsStore.save(updated)
        settings = updated
        HostSettingsClient.invalidate()
        toast(getString(R.string.settings_saved))
        recreate()
    }

    private fun openProjectPage() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(AccessibilityCompatContract.PROJECT_URL)))
        } catch (_: ActivityNotFoundException) {
            toast(getString(R.string.project_page_unavailable))
        }
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
