package io.github.supermonster003.autojs6.plugin.accessibilitycompat.control

import io.github.supermonster003.autojs6.plugin.accessibilitycompat.control.ServiceControlPolicy.Decision
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.ServicePolicy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ServiceControlPolicyTest {

    @Test
    fun followHostMirrorsTheHostServiceWhileFixedPoliciesIgnoreIt() {
        assertTrue(ServiceControlPolicy.desiredEnabled(ServicePolicy.FOLLOW_HOST, hostServiceEnabled = true))
        assertFalse(ServiceControlPolicy.desiredEnabled(ServicePolicy.FOLLOW_HOST, hostServiceEnabled = false))
        assertTrue(ServiceControlPolicy.desiredEnabled(ServicePolicy.ENABLED, hostServiceEnabled = false))
        assertFalse(ServiceControlPolicy.desiredEnabled(ServicePolicy.DISABLED, hostServiceEnabled = true))
    }

    @Test
    fun decisionsOnlyChangeTheServiceWhenItDisagreesWithThePolicy() {
        assertEquals(Decision.NONE, ServiceControlPolicy.decide(ServicePolicy.FOLLOW_HOST, hostServiceEnabled = true, serviceEnabled = true))
        assertEquals(Decision.ENABLE, ServiceControlPolicy.decide(ServicePolicy.FOLLOW_HOST, hostServiceEnabled = true, serviceEnabled = false))
        assertEquals(Decision.DISABLE, ServiceControlPolicy.decide(ServicePolicy.FOLLOW_HOST, hostServiceEnabled = false, serviceEnabled = true))
        assertEquals(Decision.NONE, ServiceControlPolicy.decide(ServicePolicy.FOLLOW_HOST, hostServiceEnabled = false, serviceEnabled = false))
        assertEquals(Decision.ENABLE, ServiceControlPolicy.decide(ServicePolicy.ENABLED, hostServiceEnabled = false, serviceEnabled = false))
        assertEquals(Decision.DISABLE, ServiceControlPolicy.decide(ServicePolicy.DISABLED, hostServiceEnabled = true, serviceEnabled = true))
    }

    @Test
    fun manualActionIsOnlyRequiredForFailedChanges() {
        assertFalse(SyncOutcome.NO_CHANGE.requiresManualAction)
        assertFalse(SyncOutcome(Decision.ENABLE, succeeded = true, via = "root").requiresManualAction)
        assertTrue(SyncOutcome(Decision.ENABLE, succeeded = false).requiresManualAction)
        assertTrue(SyncOutcome(Decision.DISABLE, succeeded = false).requiresManualAction)
        assertFalse(SyncOutcome(Decision.NONE, succeeded = false).requiresManualAction)
    }
}
