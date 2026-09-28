package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings

import android.content.Context
import android.content.SharedPreferences
import org.autojs.plugin.common.api.AutoJs6AccessibilityCompanionContract
import java.util.Locale

internal enum class AppLanguage(val languageTag: String?) {
    FOLLOW_AUTOJS6(null),
    FOLLOW_SYSTEM(null),
    ZH_HANS("zh-Hans"),
    ZH_HANT_HK("zh-Hant-HK"),
    ZH_HANT_TW("zh-Hant-TW"),
    EN("en"),
    FR("fr"),
    ES("es"),
    JA("ja"),
    KO("ko"),
    RU("ru"),
    AR("ar"),
}

internal enum class AppDarkMode {
    FOLLOW_AUTOJS6,
    FOLLOW_SYSTEM,
    LIGHT,
    DARK,
}

/** Seed colors are opaque ARGB values; [FOLLOW_AUTOJS6] resolves against the host snapshot. */
internal enum class AppThemeColor(val seed: Int?) {
    FOLLOW_AUTOJS6(null),
    DEFAULT(-0xe09754), // #1F68AC
    TEAL(-0xff7685), // #00897B
    GREEN(-0xd182ce), // #2E7D32
    ORANGE(-0x109400), // #EF6C00
    PURPLE(-0x95b34f), // #6A4CB1
    RED(-0x39d7d8), // #C62828
}

/** How the compatibility service should be kept; mirrors the companion contract values. */
internal enum class ServicePolicy(val contractValue: String) {
    FOLLOW_HOST(AutoJs6AccessibilityCompanionContract.SERVICE_POLICY_FOLLOW_HOST),
    ENABLED(AutoJs6AccessibilityCompanionContract.SERVICE_POLICY_ENABLED),
    DISABLED(AutoJs6AccessibilityCompanionContract.SERVICE_POLICY_DISABLED),
}

internal data class CompatSettings(
    val language: AppLanguage = AppLanguage.FOLLOW_AUTOJS6,
    val darkMode: AppDarkMode = AppDarkMode.FOLLOW_AUTOJS6,
    val themeColor: AppThemeColor = AppThemeColor.FOLLOW_AUTOJS6,
    val servicePolicy: ServicePolicy = ServicePolicy.FOLLOW_HOST,
    val enableWithRoot: Boolean = true,
    val enableWithSecureSettings: Boolean = true,
    val enableWithShizuku: Boolean = true,
) {
    val followsHostAppearance: Boolean
        get() = language == AppLanguage.FOLLOW_AUTOJS6 ||
            darkMode == AppDarkMode.FOLLOW_AUTOJS6 ||
            themeColor == AppThemeColor.FOLLOW_AUTOJS6
}

internal object SettingsPolicy {
    /** AutoJs6's documented default theme seed (#FFDEAD), used when the host cannot be read. */
    const val AUTOJS6_DEFAULT_THEME_COLOR = -8_531

    inline fun <reified T : Enum<T>> storedEnum(value: String?, fallback: T): T =
        value?.let { stored -> enumValues<T>().firstOrNull { it.name == stored } } ?: fallback

    fun normalizeOpaque(color: Int): Int = color or -0x1000000

    fun resolveThemeSeed(themeColor: AppThemeColor, hostThemeColor: Int?): Int = when (themeColor) {
        AppThemeColor.FOLLOW_AUTOJS6 -> normalizeOpaque(hostThemeColor ?: AUTOJS6_DEFAULT_THEME_COLOR)
        else -> normalizeOpaque(requireNotNull(themeColor.seed))
    }

    fun resolveDark(mode: AppDarkMode, hostPolicy: HostDarkModePolicy?, systemDark: Boolean): Boolean = when (mode) {
        AppDarkMode.FOLLOW_AUTOJS6 -> when (hostPolicy) {
            HostDarkModePolicy.LIGHT -> false
            HostDarkModePolicy.DARK -> true
            HostDarkModePolicy.FOLLOW_SYSTEM, null -> systemDark
        }
        AppDarkMode.FOLLOW_SYSTEM -> systemDark
        AppDarkMode.LIGHT -> false
        AppDarkMode.DARK -> true
    }

    fun resolveLanguageTag(language: AppLanguage, hostResolvedLanguageTag: String?): String? = when (language) {
        AppLanguage.FOLLOW_AUTOJS6 -> hostResolvedLanguageTag?.trim()?.takeIf(String::isNotEmpty)
        AppLanguage.FOLLOW_SYSTEM -> null
        else -> language.languageTag
    }

    /** Maps any language tag onto the closest supported [AppLanguage], or null when unsupported. */
    fun languageForTag(languageTag: String?): AppLanguage? {
        val locale = languageTag?.trim()?.takeIf(String::isNotEmpty)?.let(Locale::forLanguageTag)
            ?.takeIf { it.language.isNotEmpty() } ?: return null
        return when (locale.language.lowercase(Locale.ROOT)) {
            "zh" -> when {
                locale.script.equals("Hans", ignoreCase = true) -> AppLanguage.ZH_HANS
                locale.country.equals("HK", ignoreCase = true) ||
                    locale.country.equals("MO", ignoreCase = true) -> AppLanguage.ZH_HANT_HK
                locale.script.equals("Hant", ignoreCase = true) ||
                    locale.country.equals("TW", ignoreCase = true) -> AppLanguage.ZH_HANT_TW
                else -> AppLanguage.ZH_HANS
            }
            "en" -> AppLanguage.EN
            "fr" -> AppLanguage.FR
            "es" -> AppLanguage.ES
            "ja" -> AppLanguage.JA
            "ko" -> AppLanguage.KO
            "ru" -> AppLanguage.RU
            "ar" -> AppLanguage.AR
            else -> null
        }
    }

    fun servicePolicyForContractValue(value: String?): ServicePolicy? =
        ServicePolicy.entries.firstOrNull { it.contractValue == value }
}

internal enum class HostDarkModePolicy {
    FOLLOW_SYSTEM,
    LIGHT,
    DARK,
}

internal class CompatSettingsStore(context: Context) {
    private val preferences: SharedPreferences = context.applicationContext.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE,
    )

    fun load(): CompatSettings = CompatSettings(
        language = SettingsPolicy.storedEnum(preferences.getString(KEY_LANGUAGE, null), AppLanguage.FOLLOW_AUTOJS6),
        darkMode = SettingsPolicy.storedEnum(preferences.getString(KEY_DARK_MODE, null), AppDarkMode.FOLLOW_AUTOJS6),
        themeColor = SettingsPolicy.storedEnum(preferences.getString(KEY_THEME_COLOR, null), AppThemeColor.FOLLOW_AUTOJS6),
        servicePolicy = SettingsPolicy.storedEnum(preferences.getString(KEY_SERVICE_POLICY, null), ServicePolicy.FOLLOW_HOST),
        enableWithRoot = preferences.getBoolean(KEY_ENABLE_WITH_ROOT, true),
        enableWithSecureSettings = preferences.getBoolean(KEY_ENABLE_WITH_SECURE_SETTINGS, true),
        enableWithShizuku = preferences.getBoolean(KEY_ENABLE_WITH_SHIZUKU, true),
    )

    fun save(settings: CompatSettings) {
        preferences.edit()
            .putString(KEY_LANGUAGE, settings.language.name)
            .putString(KEY_DARK_MODE, settings.darkMode.name)
            .putString(KEY_THEME_COLOR, settings.themeColor.name)
            .putString(KEY_SERVICE_POLICY, settings.servicePolicy.name)
            .putBoolean(KEY_ENABLE_WITH_ROOT, settings.enableWithRoot)
            .putBoolean(KEY_ENABLE_WITH_SECURE_SETTINGS, settings.enableWithSecureSettings)
            .putBoolean(KEY_ENABLE_WITH_SHIZUKU, settings.enableWithShizuku)
            .putLong(KEY_REVISION, revision() + 1L)
            .apply()
    }

    fun update(transform: (CompatSettings) -> CompatSettings): CompatSettings =
        transform(load()).also(::save)

    /** Incremented on every save so that visible screens can recreate themselves when needed. */
    fun revision(): Long = preferences.getLong(KEY_REVISION, 0L)

    private companion object {
        const val PREFERENCES_NAME = "compat-settings"
        const val KEY_LANGUAGE = "language"
        const val KEY_DARK_MODE = "dark-mode"
        const val KEY_THEME_COLOR = "theme-color"
        const val KEY_SERVICE_POLICY = "service-policy"
        const val KEY_ENABLE_WITH_ROOT = "enable-with-root"
        const val KEY_ENABLE_WITH_SECURE_SETTINGS = "enable-with-secure-settings"
        const val KEY_ENABLE_WITH_SHIZUKU = "enable-with-shizuku"
        const val KEY_REVISION = "revision"
    }
}
