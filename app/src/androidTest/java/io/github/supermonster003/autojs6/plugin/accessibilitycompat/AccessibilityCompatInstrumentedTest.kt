package io.github.supermonster003.autojs6.plugin.accessibilitycompat

import android.Manifest
import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.IBinder
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.autojs.plugin.common.api.IPluginInfoProvider
import org.autojs.plugin.common.api.PluginCapabilityKeys
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Locale
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class AccessibilityCompatInstrumentedTest {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val context = instrumentation.targetContext
    private var connection: ServiceConnection? = null

    @After
    fun unbindService() {
        connection?.let { boundConnection ->
            runCatching { context.unbindService(boundConnection) }
        }
        connection = null
    }

    @Test
    fun manifestDeclaresExactAccessibilityServiceBoundaryAndMetadata() {
        val packageInfo = context.packageManager.packageInfo()
        val services = packageInfo.services.orEmpty().associateBy { it.name }
        val service = services.getValue(AccessibilityCompatContract.COMPATIBILITY_SERVICE_CLASS)

        assertEquals(context.packageName, service.packageName)
        assertTrue(service.exported)
        assertEquals(Manifest.permission.BIND_ACCESSIBILITY_SERVICE, service.permission)
        assertEquals(
            R.xml.accessibility_service_config,
            service.metaData?.getInt(AccessibilityService.SERVICE_META_DATA),
        )

        val intent = Intent(AccessibilityService.SERVICE_INTERFACE).setPackage(context.packageName)
        val matches = context.packageManager.queryIntentServicesCompat(intent)
            .filter { it.serviceInfo.name == AccessibilityCompatContract.COMPATIBILITY_SERVICE_CLASS }
        assertEquals(1, matches.size)
    }

    @Test
    fun installedAccessibilityProfileRetrievesOnlySupportedAppWindowsWithMinimalFlags() {
        val manager = requireNotNull(context.getSystemService(AccessibilityManager::class.java))
        val expectedId = AccessibilityCompatContract.expectedServiceId(context.packageName)
        val profile = manager.installedAccessibilityServiceList.single { it.id == expectedId }

        assertEquals(
            AccessibilityCompatContract.COMPATIBILITY_SERVICE_CLASS,
            profile.resolveInfo.serviceInfo.name,
        )
        assertArrayEquals(AccessibilityCompatContract.SUPPORTED_PACKAGES.toTypedArray(), profile.packageNames)
        assertEquals(AccessibilityServiceInfo.FEEDBACK_GENERIC, profile.feedbackType)
        assertEquals(
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED or
                AccessibilityEvent.TYPE_WINDOWS_CHANGED,
            profile.eventTypes,
        )
        assertEquals(MainActivity::class.java.name, profile.settingsActivityName)
        assertTrue(
            profile.capabilities and AccessibilityServiceInfo.CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT != 0,
        )
        val prohibitedCapabilities = AccessibilityServiceInfo.CAPABILITY_CAN_CONTROL_MAGNIFICATION or
            AccessibilityServiceInfo.CAPABILITY_CAN_PERFORM_GESTURES or
            AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_FILTER_KEY_EVENTS or
            AccessibilityServiceInfo.CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION or
            AccessibilityServiceInfo.CAPABILITY_CAN_TAKE_SCREENSHOT
        assertEquals(0, profile.capabilities and prohibitedCapabilities)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            assertFalse(profile.isAccessibilityTool)
        }

        val requiredFlags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
            AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
            AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        assertEquals(requiredFlags, profile.flags and requiredFlags)
        assertEquals(0, profile.flags and AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS)
        assertEquals(0, profile.flags and AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE)
        assertEquals(
            context.getString(R.string.accessibility_service_name),
            profile.resolveInfo.loadLabel(context.packageManager).toString(),
        )
        assertEquals(
            context.getString(R.string.accessibility_service_description),
            profile.loadDescription(context.packageManager),
        )
    }

    @Test
    fun visibleIdentityRemainsAppAgnosticAcrossSupportedLocales() {
        val identityStrings = intArrayOf(
            R.string.app_name,
            R.string.accessibility_service_name,
            R.string.accessibility_service_description,
            R.string.plugin_description,
            R.string.screen_intro,
            R.string.button_open_supported_app,
            R.string.supported_app_unavailable,
            R.string.supported_apps_title,
            R.string.mechanism_body,
            R.string.limitations_body,
            R.string.privacy_body,
        )
        val forbiddenTargetTerms = listOf("wechat", "微信", "com.tencent.mm")
        val languageTags = listOf(
            "ar",
            "en",
            "es",
            "fr",
            "ja",
            "ko",
            "ru",
            "zh-CN",
            "zh-HK",
            "zh-TW",
        )

        languageTags.forEach { languageTag ->
            val configuration = Configuration(context.resources.configuration).apply {
                setLocale(Locale.forLanguageTag(languageTag))
            }
            val localizedContext = context.createConfigurationContext(configuration)
            identityStrings.forEach { resourceId ->
                val value = localizedContext.getString(resourceId).lowercase(Locale.ROOT)
                assertTrue(
                    "Target-specific visible identity in $languageTag: $value",
                    forbiddenTargetTerms.none(value::contains),
                )
            }
        }
    }

    @Test
    fun serviceStatusMatchesTheSystemEnabledServiceList() {
        val manager = requireNotNull(context.getSystemService(AccessibilityManager::class.java))
        val enabledIds = manager
            .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            .mapNotNull { it.id }
        val expected = AccessibilityCompatContract.expectedServiceId(context.packageName) in enabledIds

        assertEquals(expected, AccessibilityServiceStatus.isEnabled(context))
    }

    @Test
    fun packageRequestsOnlyPluginPermissionAndNoSensitiveCapabilities() {
        val packageInfo = context.packageManager.packageInfo()
        val requested = packageInfo.requestedPermissions.orEmpty().toSet()
        val prohibited = setOf(
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.SYSTEM_ALERT_WINDOW,
            "android.permission.MANAGE_EXTERNAL_STORAGE",
            "android.permission.READ_MEDIA_AUDIO",
            "android.permission.READ_MEDIA_IMAGES",
            "android.permission.READ_MEDIA_VIDEO",
            "android.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION",
        )

        assertEquals(setOf(AccessibilityCompatContract.PLUGIN_PERMISSION), requested)
        assertTrue("Sensitive permissions declared: ${requested.intersect(prohibited)}", requested.none { it in prohibited })
        assertEquals(
            0,
            requireNotNull(packageInfo.applicationInfo).flags and ApplicationInfo.FLAG_ALLOW_BACKUP,
        )
    }

    @Test
    fun infoServiceDiscoveryAndBinderPayloadMatchInstalledPackage() {
        val discoveryIntent = Intent(AccessibilityCompatContract.INFO_ACTION)
            .addCategory(AccessibilityCompatContract.INFO_CATEGORY)
            .setPackage(context.packageName)
        val matches = context.packageManager.queryIntentServicesCompat(discoveryIntent)
        assertEquals(1, matches.size)
        val discovered = matches.single().serviceInfo
        assertEquals(AccessibilityCompatInfoService::class.java.name, discovered.name)
        assertEquals(AccessibilityCompatContract.PLUGIN_PERMISSION, discovered.permission)
        assertTrue(discovered.exported)

        val (rawBinder, provider) = bindInfoService()
        assertEquals(IPluginInfoProvider.DESCRIPTOR, rawBinder.interfaceDescriptor)
        val info = provider.info
        val installed = context.packageManager.packageInfo()

        assertEquals(context.getString(R.string.app_name), info.name)
        assertEquals(context.getString(R.string.plugin_description), info.description)
        assertEquals(PLUGIN_INSTRUCTION_REFERENCE, info.instruction)
        assertEquals(context.getString(R.string.plugin_author), info.author)
        assertEquals(AccessibilityCompatContract.PLUGIN_ID, info.id)
        assertEquals(AccessibilityCompatContract.PLUGIN_ENGINE, info.engine)
        assertEquals(AccessibilityCompatContract.PLUGIN_VARIANT, info.variant)
        assertEquals(installed.versionName, info.versionName)
        assertEquals(installed.versionCodeCompat(), info.versionCode)
        assertEquals(context.getString(R.string.plugin_version_date), info.versionDate)
        assertTrue(info.collaborators.orEmpty().isEmpty())
        assertTrue(info.supportedAbis.orEmpty().isEmpty())

        assertNotNull(info.capabilities)
        val capabilities = requireNotNull(info.capabilities)
        assertEquals(
            AccessibilityCompatContract.REQUIRED_HOST_VERSION,
            capabilities.getInt(PluginCapabilityKeys.REQUIRES_HOST_VERSION),
        )
        assertEquals(
            AccessibilityCompatContract.CONTRACT_VERSION,
            capabilities.getInt(AccessibilityCompatContract.CAPABILITY_CONTRACT_VERSION),
        )
        assertEquals(
            AccessibilityCompatContract.MODE_GLOBAL_EXPOSURE_TRIGGER,
            capabilities.getString(AccessibilityCompatContract.CAPABILITY_MODE),
        )
        assertEquals(
            AccessibilityCompatContract.expectedServiceId(context.packageName),
            capabilities.getString(AccessibilityCompatContract.CAPABILITY_SERVICE_COMPONENT),
        )
        assertEquals(
            AccessibilityCompatContract.SUPPORTED_PACKAGES,
            capabilities.getStringArrayList(AccessibilityCompatContract.CAPABILITY_TARGET_PACKAGES),
        )
    }

    @Test
    fun wakeMetadataAndComponentRemainProtectedByPluginPermission() {
        val packageInfo = context.packageManager.packageInfo()
        val activities = packageInfo.activities.orEmpty().associateBy { it.name }
        val wake = activities.getValue(WakeActivity::class.java.name)

        assertTrue(wake.exported)
        assertEquals(AccessibilityCompatContract.PLUGIN_PERMISSION, wake.permission)
        assertEquals(
            ".WakeActivity",
            packageInfo.applicationInfo?.metaData?.getString("org.autojs.plugin.WAKE_ACTIVITY"),
        )

        val wakeIntent = Intent(AccessibilityCompatContract.WAKE_ACTION)
            .addCategory(Intent.CATEGORY_DEFAULT)
            .setPackage(context.packageName)
        assertTrue(
            context.packageManager.queryIntentActivitiesCompat(wakeIntent)
                .any { it.activityInfo.name == WakeActivity::class.java.name },
        )
    }

    private fun bindInfoService(): Pair<IBinder, IPluginInfoProvider> {
        val latch = CountDownLatch(1)
        var binder: IBinder? = null
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                binder = service
                latch.countDown()
            }

            override fun onServiceDisconnected(name: ComponentName) = Unit
        }
        connection = serviceConnection
        assertTrue(
            context.bindService(
                Intent(context, AccessibilityCompatInfoService::class.java),
                serviceConnection,
                Context.BIND_AUTO_CREATE,
            ),
        )
        assertTrue("Timed out binding INFO service", latch.await(5, TimeUnit.SECONDS))
        val rawBinder = requireNotNull(binder)
        return rawBinder to IPluginInfoProvider.Stub.asInterface(rawBinder)
    }

    private fun PackageManager.packageInfo(): PackageInfo {
        val flags = PackageManager.GET_ACTIVITIES or PackageManager.GET_SERVICES or
            PackageManager.GET_PERMISSIONS or PackageManager.GET_META_DATA
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        } else {
            @Suppress("DEPRECATION")
            getPackageInfo(context.packageName, flags)
        }
    }

    private fun PackageManager.queryIntentServicesCompat(intent: Intent) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            queryIntentServices(intent, PackageManager.ResolveInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            queryIntentServices(intent, 0)
        }

    private fun PackageManager.queryIntentActivitiesCompat(intent: Intent) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            queryIntentActivities(intent, 0)
        }

    private fun PackageInfo.versionCodeCompat(): Long {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) return longVersionCode
        @Suppress("DEPRECATION")
        return versionCode.toLong()
    }
}
