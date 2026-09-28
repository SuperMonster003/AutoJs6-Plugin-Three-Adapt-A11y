package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import android.content.Context
import android.os.Build
import android.os.Bundle
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.CompatSettingsStore
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.ServicePolicy
import org.autojs.plugin.common.api.PluginCapabilityKeys
import org.autojs.plugin.common.api.PluginInfo

internal const val PLUGIN_INSTRUCTION_REFERENCE = "@raw/plugin_instruction"

internal data class PluginRuntimeFields(
    val name: String,
    val description: String,
    val author: String,
    val id: String,
    val engine: String,
    val variant: String,
    val versionName: String,
    val versionCode: Long,
    val versionDate: String,
    val requiredHostVersion: Int = ThreeAdaptA11yContract.REQUIRED_HOST_VERSION,
    val contractVersion: Int = ThreeAdaptA11yContract.CONTRACT_VERSION,
    val targetPackages: List<String> = ThreeAdaptA11yContract.SUPPORTED_PACKAGES,
    val servicePolicy: ServicePolicy = ServicePolicy.FOLLOW_HOST,
)

internal fun Context.pluginInfo(name: String, description: String): PluginInfo {
    val appContext = applicationContext
    val packageInfo = appContext.packageManager.getPackageInfo(appContext.packageName, 0)
    val fields = PluginRuntimeFields(
        name = name,
        description = description,
        author = appContext.getString(R.string.plugin_author),
        id = appContext.getString(R.string.plugin_id),
        engine = appContext.getString(R.string.plugin_engine),
        variant = appContext.getString(R.string.plugin_variant),
        versionName = packageInfo.versionName ?: "",
        versionCode = packageInfo.versionCodeCompat(),
        versionDate = appContext.getString(R.string.plugin_version_date),
        servicePolicy = CompatSettingsStore(appContext).load().servicePolicy,
    )
    return PluginInfo().apply {
        this.name = fields.name
        this.description = fields.description
        instruction = appContext.pluginInstructionReference()
        author = fields.author
        id = fields.id
        engine = fields.engine
        variant = fields.variant
        versionName = fields.versionName
        versionCode = fields.versionCode
        versionDate = fields.versionDate
        supportedAbis = emptyArray()
        capabilities = capabilitiesOf(fields, appContext.packageName)
    }
}

/** Capability bundle of the plugin info; kept separate so that unit tests can cover the mapping. */
internal fun capabilitiesOf(fields: PluginRuntimeFields, applicationId: String): Bundle = Bundle().apply {
    putInt(PluginCapabilityKeys.REQUIRES_HOST_VERSION, fields.requiredHostVersion)
    putInt(ThreeAdaptA11yContract.CAPABILITY_CONTRACT_VERSION, fields.contractVersion)
    putString(ThreeAdaptA11yContract.CAPABILITY_MODE, ThreeAdaptA11yContract.MODE_GLOBAL_EXPOSURE_TRIGGER)
    putString(
        ThreeAdaptA11yContract.CAPABILITY_SERVICE_COMPONENT,
        ThreeAdaptA11yContract.expectedServiceId(applicationId),
    )
    putStringArrayList(
        ThreeAdaptA11yContract.CAPABILITY_TARGET_PACKAGES,
        ArrayList(fields.targetPackages),
    )
    putString(ThreeAdaptA11yContract.CAPABILITY_SERVICE_POLICY, fields.servicePolicy.contractValue)
}

private fun Context.pluginInstructionReference(): String =
    "@raw/${resources.getResourceEntryName(R.raw.plugin_instruction)}"

private fun android.content.pm.PackageInfo.versionCodeCompat(): Long {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) return longVersionCode
    @Suppress("DEPRECATION")
    return versionCode.toLong()
}
