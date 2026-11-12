package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control

import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.ServicePolicy

/** Pure decision logic for keeping the compatibility service aligned with the selected policy. */
internal object ServiceControlPolicy {
    enum class Decision {
        NONE,
        ENABLE,

        /**
         * Listed but not run by the system: after its process is killed the system records the
         * service as crashed and binds it again only when it leaves the list and comes back.
         */
        REPAIR,
        DISABLE,
    }

    fun desiredEnabled(policy: ServicePolicy, hostServiceEnabled: Boolean): Boolean = when (policy) {
        ServicePolicy.FOLLOW_HOST -> hostServiceEnabled
        ServicePolicy.ENABLED -> true
        ServicePolicy.DISABLED -> false
    }

    /**
     * [serviceEnabled] is the system list, [serviceRunning] whether the system has the service
     * bound. Callers pass a running state read after the binding grace (see [BindingGrace]), since
     * a freshly listed service is unbound for a moment.
     */
    fun decide(
        policy: ServicePolicy,
        hostServiceEnabled: Boolean,
        serviceEnabled: Boolean,
        serviceRunning: Boolean = serviceEnabled,
    ): Decision {
        val desired = desiredEnabled(policy, hostServiceEnabled)
        return when {
            desired && serviceEnabled && !serviceRunning -> Decision.REPAIR
            desired == serviceEnabled -> Decision.NONE
            desired -> Decision.ENABLE
            else -> Decision.DISABLE
        }
    }
}

/**
 * Waits out the moment between a service being listed (or this process starting) and the system
 * binding it, so that only a service that stays unbound is repaired.
 */
internal object BindingGrace {
    const val GRACE_MILLIS = 3_000L
    const val POLL_MILLIS = 250L

    /** Polls [isRunning] until it holds or [graceMillis] passed; returns the last answer. */
    fun await(
        isRunning: () -> Boolean,
        graceMillis: Long = GRACE_MILLIS,
        pollMillis: Long = POLL_MILLIS,
        clock: () -> Long = { System.nanoTime() / 1_000_000 },
        sleep: (Long) -> Unit = Thread::sleep,
    ): Boolean {
        val deadline = clock() + graceMillis
        while (!isRunning()) {
            if (clock() >= deadline) return false
            sleep(pollMillis)
        }
        return true
    }
}

/**
 * Keeps a service that the system never binds from being rewritten again and again: every repair
 * changes the setting, which wakes the settings watcher, which would repair once more.
 */
internal object RepairThrottle {
    const val COOLDOWN_MILLIS = 60_000L

    /** [lastRepairAt] and [now] come from the elapsed-realtime clock, which restarts at boot. */
    fun allowed(lastRepairAt: Long?, now: Long): Boolean =
        lastRepairAt == null || now < lastRepairAt || now - lastRepairAt >= COOLDOWN_MILLIS
}

/** Result of one synchronization pass, kept Android-free so that messages are composed by the UI. */
internal data class SyncOutcome(
    val decision: ServiceControlPolicy.Decision,
    val succeeded: Boolean,
    /** Which strategy performed the change: "root", "secure-settings", "shizuku" or "self". */
    val via: String? = null,
) {
    val requiresManualAction: Boolean
        get() = decision != ServiceControlPolicy.Decision.NONE && !succeeded

    /** The service is listed but not running and could not be restarted without the user. */
    val requiresManualRestart: Boolean
        get() = decision == ServiceControlPolicy.Decision.REPAIR && !succeeded

    companion object {
        val NO_CHANGE = SyncOutcome(ServiceControlPolicy.Decision.NONE, succeeded = true)
    }
}
