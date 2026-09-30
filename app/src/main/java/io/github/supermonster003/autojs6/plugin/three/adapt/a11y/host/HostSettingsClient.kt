package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.host

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.HostDarkModePolicy
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
 * read on a worker and published only by a live owner. Main-thread consumers read the snapshot.
 */
internal object HostSettingsClient {
    private val settingsUri: Uri = Uri.parse(Contract.CONTENT_URI)
    @Volatile private var cached = HostSettingsResult(HostAvailability.CONTRACT_UNAVAILABLE)
    private val worker = java.util.concurrent.Executors.newSingleThreadExecutor()
    private val main = android.os.Handler(android.os.Looper.getMainLooper())

    fun query(context: Context): HostSettingsResult =
        if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) cached else runCatching { queryUncached(context.applicationContext) }.getOrDefault(HostSettingsResult(HostAvailability.CONTRACT_UNAVAILABLE))

    fun refresh(context: Context, result: (HostSettingsResult) -> Unit) {
        val app = context.applicationContext
        worker.execute { val next = runCatching { queryUncached(app) }.getOrDefault(HostSettingsResult(HostAvailability.CONTRACT_UNAVAILABLE)); main.post { result(next) } }
    }

    fun publish(result: HostSettingsResult) { cached = result }
    fun invalidate() { cached = HostSettingsResult(HostAvailability.CONTRACT_UNAVAILABLE) }

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
            require(!bundle.hasFileDescriptors())
            @Suppress("DEPRECATION")
            val typesValid = bundle.get(Contract.KEY_DARK_MODE_ACTIVE) is Boolean && bundle.get(Contract.KEY_THEME_COLOR_PRIMARY) is Int && bundle.get(Contract.KEY_THEME_COLOR_ACCENT) is Int
            require(typesValid)
            val resolvedTag = requireNotNull(bundle.getString(Contract.KEY_RESOLVED_LANGUAGE_TAG))
            require(resolvedTag.length in 2..80 && resolvedTag.matches(Regex("[A-Za-z0-9]+(?:-[A-Za-z0-9]+)*")) && java.util.Locale.forLanguageTag(resolvedTag).language.isNotBlank())
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
