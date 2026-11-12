package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control

/**
 * One way of enabling or disabling the compatibility service in system settings without user
 * interaction. Strategies are tried in preference order; the system settings page is not a strategy
 * because it cannot report success.
 */
internal interface ServiceToggleStrategy {
    /** Stable name for logs and messages: "root", "secure-settings" or "shizuku". */
    val name: String

    fun isAvailable(): Boolean

    /** Enables the service and reports whether the setting lists it afterwards (read back). */
    fun enable(): Boolean

    /** Disables the service and reports whether the setting no longer lists it. */
    fun disable(): Boolean
}

/**
 * Enables or disables [serviceId] by rewriting the enabled-services list through [store]. Only the
 * list is written: since Android 7.0 the framework derives `accessibility_enabled` from the bound
 * services itself.
 */
internal class SettingsStoreStrategy(
    override val name: String,
    private val serviceId: String,
    private val store: EnabledServicesStore,
    private val available: () -> Boolean,
    private val readBack: () -> String? = store::read,
    private val log: (String) -> Unit = {},
    private val relistGapMillis: Long = RELIST_GAP_MILLIS,
    private val sleep: (Long) -> Unit = Thread::sleep,
) : ServiceToggleStrategy {
    override fun isAvailable(): Boolean = runCatching(available)
        .onFailure { log("$name: availability check failed: $it") }
        .getOrDefault(false)

    override fun enable(): Boolean = runCatching {
        val current = store.read()
        // A listed but unbound service is only bound again when the system sees it leave and re-enter.
        // Two writes a few milliseconds apart reach the system as one change (the service stays
        // crashed), so the second one waits.
        if (EnabledAccessibilityServices.contains(current, serviceId)) {
            store.write(EnabledAccessibilityServices.detach(current, serviceId))
            sleep(relistGapMillis)
        }
        store.write(EnabledAccessibilityServices.attach(current, serviceId))
        EnabledAccessibilityServices.contains(readBack(), serviceId)
    }.onFailure { log("$name: unable to enable $serviceId: $it") }.getOrDefault(false)

    override fun disable(): Boolean = runCatching {
        store.write(EnabledAccessibilityServices.detach(store.read(), serviceId))
        !EnabledAccessibilityServices.contains(readBack(), serviceId)
    }.onFailure { log("$name: unable to disable $serviceId: $it") }.getOrDefault(false)

    companion object {
        /** Measured on ColorOS 15: 3 ms apart fails every time, 300 ms and 1 s always work. */
        const val RELIST_GAP_MILLIS = 1_000L
    }
}
