package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EnabledAccessibilityServicesTest {
    private val compat = "io.example.compat/com.example.CompatService"
    private val host = "org.autojs.autojs6/org.autojs.autojs.core.accessibility.AccessibilityServiceUsher"
    private val other = "example.reader/example.reader.ReaderService"

    @Test
    fun parseIgnoresBlankEntriesNewlinesAndShellNull() {
        assertEquals(emptyList<String>(), EnabledAccessibilityServices.parse(null))
        assertEquals(emptyList<String>(), EnabledAccessibilityServices.parse(""))
        assertEquals(emptyList<String>(), EnabledAccessibilityServices.parse("null\n"))
        assertEquals(listOf(host, other), EnabledAccessibilityServices.parse(" $host :: $other\n:"))
    }

    @Test
    fun containsRequiresExactComponentButPackageLookupMatchesAnyService() {
        val raw = "$host:$other"
        assertTrue(EnabledAccessibilityServices.contains(raw, host))
        assertFalse(EnabledAccessibilityServices.contains(raw, host.uppercase()))
        assertFalse(EnabledAccessibilityServices.contains(raw, "org.autojs.autojs6/.Other"))
        assertTrue(EnabledAccessibilityServices.containsPackage(raw, "org.autojs.autojs6"))
        assertFalse(EnabledAccessibilityServices.containsPackage(raw, "org.autojs"))
        assertFalse(EnabledAccessibilityServices.containsPackage(null, "org.autojs.autojs6"))
    }

    @Test
    fun attachAppendsOnceAtTheEndAndDetachRemovesEveryServiceOfThePackage() {
        val duplicated = "$compat:$host:io.example.compat/com.example.Second"
        assertEquals("$host:$compat", EnabledAccessibilityServices.attach(duplicated, compat))
        assertEquals(host, EnabledAccessibilityServices.detach(duplicated, compat))
        assertEquals(compat, EnabledAccessibilityServices.attach(null, compat))
        assertEquals("", EnabledAccessibilityServices.detach(compat, compat))
        assertEquals("$host:$other", EnabledAccessibilityServices.detach("$host:$other", compat))
    }

    @Test
    fun packageOfStopsAtTheComponentSeparator() {
        assertEquals("io.example.compat", EnabledAccessibilityServices.packageOf(compat))
        assertEquals("bare", EnabledAccessibilityServices.packageOf("bare"))
    }
}
