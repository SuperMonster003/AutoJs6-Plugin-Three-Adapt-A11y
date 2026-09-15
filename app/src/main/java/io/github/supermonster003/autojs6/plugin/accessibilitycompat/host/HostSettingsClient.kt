package io.github.supermonster003.autojs6.plugin.accessibilitycompat.host

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.HostDarkModePolicy
import org.autojs.plugin.common.api.AutoJs6HostSettingsContract as Contract

internal enum class HostAvailability {
    AVAILABLE,
    NOT_INSTALLED,
    DISABLED,
    CONTRACT_UNAVAILABLE,
}

internal data class HostSettingsSnapshot(
    val hostVersionCode: Long,
    val hostVersionName: String,
    val themeColorPrimary: Int,
    val themeColorAccent: Int,
    val darkModePolicy: HostDarkModePolicy,
    val darkModeActive: Boolean,
    val languageTag: String,
    val resolvedLanguageTag: String,
)

internal data class HostSettingsResult(
    val availability: HostAvailability,
    val snapshot: HostSettingsSnapshot? = null,
) {
    init {
        require((availability == HostAvailability.AVAILABLE) == (snapshot != null))
    }

    val selectable: Boolean
        get() = availability == HostAvailability.AVAILABLE

    val installed: Boolean
        get() = availability != HostAvailability.NOT_INSTALLED
}

/**
 * Client for the read-only appearance snapshot AutoJs6 exposes to official plugins. Results are
 * cached briefly because several steps of one screen creation query it on the main thread.
 */
internal object HostSettingsClient {
    private val settingsUri: Uri = Uri.parse(Contract.CONTENT_URI)
    private const val CACHE_TTL_MILLIS = 1_500L

    private var cached: HostSettingsResult? = null
    private var cachedAt = 0L

    @Synchronized
    fun query(context: Context): HostSettingsResult {
        val now = SystemClock.elapsedRealtime()
        cached?.takeIf { now - cachedAt < CACHE_TTL_MILLIS }?.let { return it }
        return queryUncached(context).also {
            cached = it
            cachedAt = now
        }
    }

    @Synchronized
    fun invalidate() {
        cached = null
    }

    fun inspectHostPackage(context: Context): HostAvailability {
        val packageManager = context.packageManager
        val applicationInfo = runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getApplicationInfo(
                    Contract.HOST_PACKAGE_NAME,
                    PackageManager.ApplicationInfoFlags.of(PackageManager.MATCH_DISABLED_COMPONENTS.toLong()),
                )
            } else {
                @Suppress("DEPRECATION")
                packageManager.getApplicationInfo(Contract.HOST_PACKAGE_NAME, PackageManager.MATCH_DISABLED_COMPONENTS)
            }
        }.getOrNull() ?: return HostAvailability.NOT_INSTALLED
        val enabled = when (packageManager.getApplicationEnabledSetting(Contract.HOST_PACKAGE_NAME)) {
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER,
            -> false
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED -> true
            else -> applicationInfo.enabled
        }
        return if (enabled) HostAvailability.AVAILABLE else HostAvailability.DISABLED
    }

    private fun queryUncached(context: Context): HostSettingsResult {
        val installed = inspectHostPackage(context)
        if (installed != HostAvailability.AVAILABLE) return HostSettingsResult(installed)
        val bundle = runCatching {
            context.contentResolver.call(settingsUri, Contract.METHOD_GET_SETTINGS, null, null)
        }.getOrNull() ?: return HostSettingsResult(HostAvailability.CONTRACT_UNAVAILABLE)
        val snapshot = runCatching {
            require(bundle.getInt(Contract.KEY_PROTOCOL_VERSION, 0) == Contract.PROTOCOL_VERSION)
            require(bundle.getString(Contract.KEY_HOST_PACKAGE_NAME) == Contract.HOST_PACKAGE_NAME)
            require(bundle.containsKey(Contract.KEY_THEME_COLOR_PRIMARY))
            require(bundle.containsKey(Contract.KEY_THEME_COLOR_ACCENT))
            HostSettingsSnapshot(
                hostVersionCode = bundle.getLong(Contract.KEY_HOST_VERSION_CODE, 0L),
                hostVersionName = bundle.getString(Contract.KEY_HOST_VERSION_NAME).orEmpty(),
                themeColorPrimary = bundle.getInt(Contract.KEY_THEME_COLOR_PRIMARY),
                themeColorAccent = bundle.getInt(Contract.KEY_THEME_COLOR_ACCENT),
                darkModePolicy = HostDarkModePolicy.valueOf(bundle.getString(Contract.KEY_DARK_MODE_POLICY).orEmpty()),
                darkModeActive = bundle.getBoolean(Contract.KEY_DARK_MODE_ACTIVE),
                languageTag = bundle.getString(Contract.KEY_LANGUAGE_TAG).orEmpty(),
                resolvedLanguageTag = bundle.getString(Contract.KEY_RESOLVED_LANGUAGE_TAG).orEmpty(),
            )
        }.getOrNull() ?: return HostSettingsResult(HostAvailability.CONTRACT_UNAVAILABLE)
        return HostSettingsResult(HostAvailability.AVAILABLE, snapshot)
    }
}
