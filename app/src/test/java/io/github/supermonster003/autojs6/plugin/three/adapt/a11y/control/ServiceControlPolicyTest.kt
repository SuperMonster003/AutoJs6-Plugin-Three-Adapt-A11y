package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control

import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control.ServiceControlPolicy.Decision
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.ServicePolicy
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

    @Test
    fun aListedServiceTheSystemDoesNotRunIsRepairedOnlyWhenThePolicyWantsIt() {
        assertEquals(Decision.REPAIR, ServiceControlPolicy.decide(ServicePolicy.FOLLOW_HOST, hostServiceEnabled = true, serviceEnabled = true, serviceRunning = false))
        assertEquals(Decision.REPAIR, ServiceControlPolicy.decide(ServicePolicy.ENABLED, hostServiceEnabled = false, serviceEnabled = true, serviceRunning = false))
        assertEquals(Decision.DISABLE, ServiceControlPolicy.decide(ServicePolicy.FOLLOW_HOST, hostServiceEnabled = false, serviceEnabled = true, serviceRunning = false))
        assertEquals(Decision.DISABLE, ServiceControlPolicy.decide(ServicePolicy.DISABLED, hostServiceEnabled = true, serviceEnabled = true, serviceRunning = false))
        assertEquals(Decision.NONE, ServiceControlPolicy.decide(ServicePolicy.ENABLED, hostServiceEnabled = true, serviceEnabled = true, serviceRunning = true))
        assertEquals(Decision.ENABLE, ServiceControlPolicy.decide(ServicePolicy.ENABLED, hostServiceEnabled = true, serviceEnabled = false, serviceRunning = false))
    }

    @Test
    fun onlyAFailedRepairAsksTheUserToRestartTheService() {
        assertTrue(SyncOutcome(Decision.REPAIR, succeeded = false).requiresManualRestart)
        assertTrue(SyncOutcome(Decision.REPAIR, succeeded = false).requiresManualAction)
        assertFalse(SyncOutcome(Decision.REPAIR, succeeded = true, via = "secure-settings").requiresManualRestart)
        assertFalse(SyncOutcome(Decision.ENABLE, succeeded = false).requiresManualRestart)
        assertFalse(SyncOutcome.NO_CHANGE.requiresManualRestart)
    }

    @Test
    fun bindingGraceStopsAsSoonAsTheServiceRunsAndGivesUpAfterTheGrace() {
        var now = 0L
        val sleeps = mutableListOf<Long>()
        val sleep: (Long) -> Unit = { sleeps += it; now += it }
        var polls = 0
        assertTrue(BindingGrace.await({ ++polls > 3 }, graceMillis = 3_000, pollMillis = 250, clock = { now }, sleep = sleep))
        assertEquals(listOf(250L, 250L, 250L), sleeps)

        sleeps.clear()
        now = 0
        assertFalse(BindingGrace.await({ false }, graceMillis = 1_000, pollMillis = 250, clock = { now }, sleep = sleep))
        assertEquals(1_000L, now)
        assertEquals(4, sleeps.size)

        sleeps.clear()
        assertTrue(BindingGrace.await({ true }, clock = { now }, sleep = sleep))
        assertTrue(sleeps.isEmpty())
    }

    @Test
    fun repairsAreSpacedByTheCooldownAndAReadingFromBeforeABootDoesNotBlock() {
        assertTrue(RepairThrottle.allowed(lastRepairAt = null, now = 5_000))
        assertFalse(RepairThrottle.allowed(lastRepairAt = 10_000, now = 10_000 + RepairThrottle.COOLDOWN_MILLIS - 1))
        assertTrue(RepairThrottle.allowed(lastRepairAt = 10_000, now = 10_000 + RepairThrottle.COOLDOWN_MILLIS))
        assertTrue(RepairThrottle.allowed(lastRepairAt = 900_000, now = 4_000))
    }
}
