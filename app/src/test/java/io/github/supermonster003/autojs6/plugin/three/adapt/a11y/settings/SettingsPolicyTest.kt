package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsPolicyTest {

    @Test
    fun defaultsFollowAutoJs6AndAllowEveryUnattendedMethod() {
        val settings = CompatSettings()
        assertEquals(AppLanguage.FOLLOW_AUTOJS6, settings.language)
        assertEquals(AppDarkMode.FOLLOW_AUTOJS6, settings.darkMode)
        assertEquals(AppThemeColor.FOLLOW_AUTOJS6, settings.themeColor)
        assertEquals(ServicePolicy.FOLLOW_HOST, settings.servicePolicy)
        assertTrue(settings.enableWithRoot && settings.enableWithSecureSettings && settings.enableWithShizuku)
        assertTrue(settings.followsHostAppearance)
        assertFalse(
            settings.copy(language = AppLanguage.EN, darkMode = AppDarkMode.DARK, themeColor = AppThemeColor.RED).followsHostAppearance,
        )
    }

    @Test
    fun storedEnumFallsBackOnUnknownValues() {
        assertEquals(AppDarkMode.DARK, SettingsPolicy.storedEnum("DARK", AppDarkMode.FOLLOW_AUTOJS6))
        assertEquals(AppDarkMode.FOLLOW_AUTOJS6, SettingsPolicy.storedEnum("dark", AppDarkMode.FOLLOW_AUTOJS6))
        assertEquals(AppDarkMode.FOLLOW_AUTOJS6, SettingsPolicy.storedEnum(null, AppDarkMode.FOLLOW_AUTOJS6))
    }

    @Test
    fun themeSeedFollowsTheHostAndIsAlwaysOpaque() {
        assertEquals(0xFF123456.toInt(), SettingsPolicy.resolveThemeSeed(AppThemeColor.FOLLOW_AUTOJS6, 0x00123456))
        assertEquals(0xFFFFDEAD.toInt(), SettingsPolicy.resolveThemeSeed(AppThemeColor.FOLLOW_AUTOJS6, null))
        assertEquals(0xFF1F68AC.toInt(), SettingsPolicy.resolveThemeSeed(AppThemeColor.DEFAULT, 0x00123456))
        AppThemeColor.entries.filter { it != AppThemeColor.FOLLOW_AUTOJS6 }.forEach { color ->
            assertEquals(color.name, 0xFF, SettingsPolicy.resolveThemeSeed(color, null) ushr 24)
        }
    }

    @Test
    fun darkModeResolutionPrefersTheExplicitChoiceThenTheHostThenTheSystem() {
        assertTrue(SettingsPolicy.resolveDark(AppDarkMode.DARK, HostDarkModePolicy.LIGHT, systemDark = false))
        assertFalse(SettingsPolicy.resolveDark(AppDarkMode.LIGHT, HostDarkModePolicy.DARK, systemDark = true))
        assertTrue(SettingsPolicy.resolveDark(AppDarkMode.FOLLOW_SYSTEM, HostDarkModePolicy.LIGHT, systemDark = true))
        assertTrue(SettingsPolicy.resolveDark(AppDarkMode.FOLLOW_AUTOJS6, HostDarkModePolicy.DARK, systemDark = false))
        assertFalse(SettingsPolicy.resolveDark(AppDarkMode.FOLLOW_AUTOJS6, HostDarkModePolicy.LIGHT, systemDark = true))
        assertTrue(SettingsPolicy.resolveDark(AppDarkMode.FOLLOW_AUTOJS6, HostDarkModePolicy.FOLLOW_SYSTEM, systemDark = true))
        assertFalse(SettingsPolicy.resolveDark(AppDarkMode.FOLLOW_AUTOJS6, null, systemDark = false))
    }

    @Test
    fun languageTagResolutionUsesTheHostOnlyWhenFollowingIt() {
        assertEquals("fr", SettingsPolicy.resolveLanguageTag(AppLanguage.FOLLOW_AUTOJS6, " fr "))
        assertNull(SettingsPolicy.resolveLanguageTag(AppLanguage.FOLLOW_AUTOJS6, " "))
        assertNull(SettingsPolicy.resolveLanguageTag(AppLanguage.FOLLOW_SYSTEM, "fr"))
        assertEquals("zh-Hant-HK", SettingsPolicy.resolveLanguageTag(AppLanguage.ZH_HANT_HK, "fr"))
    }

    @Test
    fun languageForTagMapsChineseVariantsAndRejectsUnsupportedLanguages() {
        assertEquals(AppLanguage.ZH_HANS, SettingsPolicy.languageForTag("zh-CN"))
        assertEquals(AppLanguage.ZH_HANS, SettingsPolicy.languageForTag("zh-Hans-SG"))
        assertEquals(AppLanguage.ZH_HANT_HK, SettingsPolicy.languageForTag("zh-HK"))
        assertEquals(AppLanguage.ZH_HANT_HK, SettingsPolicy.languageForTag("zh-Hant-MO"))
        assertEquals(AppLanguage.ZH_HANT_TW, SettingsPolicy.languageForTag("zh-TW"))
        assertEquals(AppLanguage.ZH_HANT_TW, SettingsPolicy.languageForTag("zh-Hant"))
        assertEquals(AppLanguage.EN, SettingsPolicy.languageForTag("en-US"))
        assertEquals(AppLanguage.AR, SettingsPolicy.languageForTag("ar-EG"))
        assertNull(SettingsPolicy.languageForTag("de-DE"))
        assertNull(SettingsPolicy.languageForTag(""))
        assertNull(SettingsPolicy.languageForTag(null))
    }

    @Test
    fun servicePolicyRoundTripsThroughTheContractValues() {
        ServicePolicy.entries.forEach { policy ->
            assertEquals(policy, SettingsPolicy.servicePolicyForContractValue(policy.contractValue))
        }
        assertNull(SettingsPolicy.servicePolicyForContractValue("FOLLOW_HOST"))
        assertNull(SettingsPolicy.servicePolicyForContractValue(null))
    }
    @org.junit.Test fun followingHostUsesItsResolvedNightValueWhileLocalChoicesStayIndependent() {
        org.junit.Assert.assertTrue(SettingsPolicy.resolveDark(AppDarkMode.FOLLOW_AUTOJS6, HostDarkModePolicy.FOLLOW_SYSTEM, false, true))
        org.junit.Assert.assertFalse(SettingsPolicy.resolveDark(AppDarkMode.LIGHT, HostDarkModePolicy.DARK, true, true))
        org.junit.Assert.assertFalse(SettingsPolicy.resolveDark(AppDarkMode.FOLLOW_SYSTEM, HostDarkModePolicy.DARK, false, true))
    }

}
