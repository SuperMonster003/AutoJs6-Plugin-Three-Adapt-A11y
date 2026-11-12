package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.SystemClock
import android.provider.Settings.Secure
import android.util.Log
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ThreeAdaptA11yContract
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ThreeAdaptA11yService
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.AccessibilityServiceStatus
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.CompatSettings
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.CompatSettingsStore
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.shizuku.ShizukuShell
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.shizuku.ShizukuState

internal enum class ServiceState {
    DISABLED,
    ENABLED_NOT_BOUND,
    ENABLED,
}

internal enum class HostServiceState {
    NOT_INSTALLED,
    DISABLED,
    ENABLED,
}

internal data class ControlAvailability(
    val rootPresent: Boolean,
    val secureSettingsGranted: Boolean,
    val shizuku: ShizukuState,
)

/**
 * Reads the two axes of the compatibility service (listed in system settings, bound in this process)
 * and changes the first one through the unattended strategies the user allowed. Every method may
 * block on shell or Binder work and must be called off the main thread.
 */
internal class CompatServiceController(context: Context) {
    private val appContext = context.applicationContext
    private val resolver = appContext.contentResolver
    private val store = CompatSettingsStore(appContext)
    val serviceId: String = ThreeAdaptA11yContract.expectedServiceId(appContext.packageName)

    fun enabledServicesRaw(): String? = Secure.getString(resolver, Secure.ENABLED_ACCESSIBILITY_SERVICES)

    fun isServiceListed(): Boolean = EnabledAccessibilityServices.contains(enabledServicesRaw(), serviceId)

    /** The system list of bound services covers the case where another process asks. */
    fun serviceState(): ServiceState = when {
        !isServiceListed() -> ServiceState.DISABLED
        isServiceRunning() -> ServiceState.ENABLED
        else -> ServiceState.ENABLED_NOT_BOUND
    }

    fun isServiceRunning(): Boolean = ThreeAdaptA11yService.isBound || AccessibilityServiceStatus.isEnabled(appContext)

    fun hostServiceState(): HostServiceState = when {
        !isHostInstalled() -> HostServiceState.NOT_INSTALLED
        isHostServiceEnabled() -> HostServiceState.ENABLED
        else -> HostServiceState.DISABLED
    }

    /** Any accessibility service of the AutoJs6 package counts, so a host class rename cannot break following. */
    fun isHostServiceEnabled(): Boolean =
        EnabledAccessibilityServices.containsPackage(enabledServicesRaw(), ThreeAdaptA11yContract.HOST_PACKAGE_NAME)

    fun availability(): ControlAvailability = ControlAvailability(
        rootPresent = RootShell.isSuPresent(),
        secureSettingsGranted = isSecureSettingsGranted(appContext),
        shizuku = ShizukuShell.state(appContext),
    )

    /** Applies the stored policy to the current state, returning what happened. */
    fun sync(): SyncOutcome {
        val settings = store.load()
        var decision = decide(settings, isServiceRunning())
        if (decision == ServiceControlPolicy.Decision.REPAIR) {
            decision = decide(settings, BindingGrace.await(::isServiceRunning))
        }
        return when (decision) {
            ServiceControlPolicy.Decision.NONE -> SyncOutcome.NO_CHANGE
            ServiceControlPolicy.Decision.ENABLE -> {
                val via = enable(settings)
                SyncOutcome(decision, succeeded = via != null, via = via)
            }
            ServiceControlPolicy.Decision.REPAIR -> repair(settings)
            ServiceControlPolicy.Decision.DISABLE -> {
                val via = disable(settings)
                SyncOutcome(decision, succeeded = via != null, via = via)
            }
        }
    }

    private fun decide(settings: CompatSettings, running: Boolean): ServiceControlPolicy.Decision =
        ServiceControlPolicy.decide(settings.servicePolicy, isHostServiceEnabled(), isServiceListed(), running)

    /**
     * Restarts a listed service the system does not run by taking it out of the list and putting it
     * back ([SettingsStoreStrategy.enable] waits between the two writes). Not repeated within
     * [RepairThrottle.COOLDOWN_MILLIS], in case the system refuses to bind it for another reason.
     */
    private fun repair(settings: CompatSettings): SyncOutcome {
        val decision = ServiceControlPolicy.Decision.REPAIR
        val now = SystemClock.elapsedRealtime()
        val last = repairPrefs.getLong(KEY_LAST_REPAIR, -1L).takeIf { it >= 0 }
        if (!RepairThrottle.allowed(last, now)) {
            Log.i(TAG, "Not running after a repair ${now - (last ?: now)} ms ago; leaving it to the user")
            return SyncOutcome(decision, succeeded = false)
        }
        val via = enable(settings)
        if (via != null) repairPrefs.edit().putLong(KEY_LAST_REPAIR, now).apply()
        Log.i(TAG, "Listed but not running; repair via ${via ?: "none (no method available)"}")
        return SyncOutcome(decision, succeeded = via != null, via = via)
    }

    private val repairPrefs get() = appContext.getSharedPreferences(REPAIR_PREFS, Context.MODE_PRIVATE)

    /** Returns the strategy name that enabled the service, or null when none could. */
    fun enable(settings: CompatSettings = store.load()): String? =
        strategies(settings).firstOrNull { it.isAvailable() && it.enable() }?.name

    /**
     * Disables the service; the bound instance disables itself first because that needs no
     * privilege, and the strategies cover a listed but unbound service.
     */
    fun disable(settings: CompatSettings = store.load()): String? {
        if (ThreeAdaptA11yService.disableSelfIfBound() && awaitUnlisted()) return VIA_SELF
        return strategies(settings).firstOrNull { it.isAvailable() && it.disable() }?.name
    }

    private fun awaitUnlisted(): Boolean {
        val deadline = System.currentTimeMillis() + SELF_DISABLE_TIMEOUT_MILLIS
        while (isServiceListed()) {
            if (System.currentTimeMillis() >= deadline) return false
            Thread.sleep(SELF_DISABLE_POLL_MILLIS)
        }
        return true
    }

    private fun strategies(settings: CompatSettings): List<ServiceToggleStrategy> {
        val log: (String) -> Unit = { Log.d(TAG, it) }
        val readBack = ::enabledServicesRaw
        return listOf(
            SettingsStoreStrategy(
                name = VIA_ROOT,
                serviceId = serviceId,
                store = RootShellStore(log),
                available = { settings.enableWithRoot && RootShell.isSuPresent() },
                readBack = readBack,
                log = log,
            ),
            SettingsStoreStrategy(
                name = VIA_SECURE_SETTINGS,
                serviceId = serviceId,
                store = SecureSettingsStore(resolver),
                available = { settings.enableWithSecureSettings && isSecureSettingsGranted(appContext) },
                readBack = readBack,
                log = log,
            ),
            SettingsStoreStrategy(
                name = VIA_SHIZUKU,
                serviceId = serviceId,
                store = ShizukuShellStore(appContext, log),
                available = { settings.enableWithShizuku && ShizukuShell.state(appContext) == ShizukuState.AVAILABLE },
                readBack = readBack,
                log = log,
            ),
        )
    }

    private fun isHostInstalled(): Boolean = runCatching {
        appContext.packageManager.getPackageInfo(ThreeAdaptA11yContract.HOST_PACKAGE_NAME, 0)
        true
    }.getOrDefault(false)

    companion object {
        private const val TAG = "CompatServiceController"
        private const val SELF_DISABLE_TIMEOUT_MILLIS = 1_500L
        private const val SELF_DISABLE_POLL_MILLIS = 50L
        private const val REPAIR_PREFS = "compat_service_repair"
        private const val KEY_LAST_REPAIR = "last_repair_elapsed_realtime"

        const val VIA_ROOT = "root"
        const val VIA_SECURE_SETTINGS = "secure-settings"
        const val VIA_SHIZUKU = "shizuku"
        const val VIA_SELF = "self"

        /** The service is bound again, so the last repair worked and does not hold back the next one. */
        fun onServiceRunning(context: Context) {
            val prefs = context.applicationContext.getSharedPreferences(REPAIR_PREFS, Context.MODE_PRIVATE)
            if (prefs.contains(KEY_LAST_REPAIR)) prefs.edit().remove(KEY_LAST_REPAIR).apply()
        }

        fun isSecureSettingsGranted(context: Context): Boolean =
            context.checkCallingOrSelfPermission(Manifest.permission.WRITE_SECURE_SETTINGS) ==
                PackageManager.PERMISSION_GRANTED

        fun secureSettingsGrantCommand(context: Context): String =
            "adb shell pm grant ${context.packageName} ${Manifest.permission.WRITE_SECURE_SETTINGS}"
    }
}
