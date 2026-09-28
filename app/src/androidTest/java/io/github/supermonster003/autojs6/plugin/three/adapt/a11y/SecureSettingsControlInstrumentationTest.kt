package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import android.app.UiAutomation
import android.os.SystemClock
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control.CompatServiceController
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.CompatSettingsStore
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.ServicePolicy
import org.junit.Assert.*
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith

/** Device acceptance: grant WRITE_SECURE_SETTINGS to the test installation explicitly. */
@RunWith(AndroidJUnit4::class)
class SecureSettingsControlInstrumentationTest {
    @Test fun secureSettingsTogglesOnlyThisServiceAndWaitsForActualBinding() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val context = instrumentation.targetContext
        assumeTrue("Requires an explicit WRITE_SECURE_SETTINGS grant", CompatServiceController.isSecureSettingsGranted(context))
        instrumentation.getUiAutomation(UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES)
        val store = CompatSettingsStore(context)
        val original = store.load()
        val controller = CompatServiceController(context)
        val wasListed = controller.isServiceListed()
        fun others() = controller.enabledServicesRaw().orEmpty().split(':')
            .filter { it.isNotBlank() && it != controller.serviceId }.toSet()
        val otherServices = others()
        val enabled = original.copy(servicePolicy = ServicePolicy.ENABLED,
            enableWithRoot = false, enableWithSecureSettings = true, enableWithShizuku = false)
        try {
            store.save(enabled.copy(servicePolicy = ServicePolicy.DISABLED))
            controller.disable(store.load())
            await { !controller.isServiceListed() && !ThreeAdaptA11yService.isBound }
            assertEquals(otherServices, others())
            store.save(enabled)
            assertEquals(CompatServiceController.VIA_SECURE_SETTINGS, controller.enable(enabled))
            await { controller.isServiceListed() && ThreeAdaptA11yService.isBound }
            assertEquals(otherServices, others())
            assertTrue(AdvancedProtectionNotice.summary(context).isNotBlank())
            store.save(enabled.copy(servicePolicy = ServicePolicy.DISABLED))
            assertNotNull(controller.disable(store.load()))
            await { !controller.isServiceListed() && !ThreeAdaptA11yService.isBound }
            assertEquals(otherServices, others())
        } finally {
            store.save(enabled.copy(servicePolicy = if (wasListed) ServicePolicy.ENABLED else ServicePolicy.DISABLED))
            if (wasListed) controller.enable(store.load()) else controller.disable(store.load())
            store.save(original)
        }
    }

    private fun await(predicate: () -> Boolean) {
        val deadline = SystemClock.uptimeMillis() + 15_000L
        while (!predicate() && SystemClock.uptimeMillis() < deadline) SystemClock.sleep(100)
        assertTrue("Service list and actual process binding did not converge", predicate())
    }
}
