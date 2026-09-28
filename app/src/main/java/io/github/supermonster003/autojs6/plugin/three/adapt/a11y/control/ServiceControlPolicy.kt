package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control

import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.ServicePolicy

/** Pure decision logic for keeping the compatibility service aligned with the selected policy. */
internal object ServiceControlPolicy {
    enum class Decision {
        NONE,
        ENABLE,
        DISABLE,
    }

    fun desiredEnabled(policy: ServicePolicy, hostServiceEnabled: Boolean): Boolean = when (policy) {
        ServicePolicy.FOLLOW_HOST -> hostServiceEnabled
        ServicePolicy.ENABLED -> true
        ServicePolicy.DISABLED -> false
    }

    fun decide(policy: ServicePolicy, hostServiceEnabled: Boolean, serviceEnabled: Boolean): Decision {
        val desired = desiredEnabled(policy, hostServiceEnabled)
        return when {
            desired == serviceEnabled -> Decision.NONE
            desired -> Decision.ENABLE
            else -> Decision.DISABLE
        }
    }
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

    companion object {
        val NO_CHANGE = SyncOutcome(ServiceControlPolicy.Decision.NONE, succeeded = true)
    }
}
