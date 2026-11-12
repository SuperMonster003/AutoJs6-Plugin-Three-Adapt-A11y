package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsStoreStrategyTest {
    private val service = "io.github.supermonster003.autojs6.plugin.three.adapt.a11y/.ThreeAdaptA11yService"
    private val host = "org.autojs.autojs6/org.autojs.autojs.core.accessibility.AccessibilityServiceUsher"

    private class RecordingStore(var value: String?, private val events: MutableList<String>) : EnabledServicesStore {
        override fun read(): String? = value

        override fun write(value: String) {
            events += "write:$value"
            this.value = value
        }
    }

    private fun strategy(store: RecordingStore, events: MutableList<String>) = SettingsStoreStrategy(
        name = "secure-settings",
        serviceId = service,
        store = store,
        available = { true },
        sleep = { events += "sleep:$it" },
    )

    @Test
    fun aListedServiceLeavesTheListAndComesBackAfterTheGap() {
        val events = mutableListOf<String>()
        val store = RecordingStore("$host:$service", events)
        assertTrue(strategy(store, events).enable())
        assertEquals(listOf("write:$host", "sleep:${SettingsStoreStrategy.RELIST_GAP_MILLIS}", "write:$host:$service"), events)
    }

    @Test
    fun anUnlistedServiceIsAddedWithASingleWriteAndNoWait() {
        val events = mutableListOf<String>()
        val store = RecordingStore(host, events)
        assertTrue(strategy(store, events).enable())
        assertEquals(listOf("write:$host:$service"), events)
    }

    @Test
    fun aWriteThatDoesNotStickIsReportedAsAFailure() {
        val events = mutableListOf<String>()
        val store = object : EnabledServicesStore {
            override fun read(): String? = host

            override fun write(value: String) {
                events += "write:$value"
            }
        }
        assertFalse(SettingsStoreStrategy("shizuku", service, store, available = { true }, sleep = {}).enable())
    }
}
