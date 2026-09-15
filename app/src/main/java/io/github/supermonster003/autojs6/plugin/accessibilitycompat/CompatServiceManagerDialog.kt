package io.github.supermonster003.autojs6.plugin.accessibilitycompat

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings.Secure
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.TextView
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.control.CompatServiceController
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.control.ControlAvailability
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.control.HostServiceState
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.control.ServiceControlPolicy
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.control.ServiceState
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.control.SyncOutcome
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.CompatSettings
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.settings.ServicePolicy
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.shizuku.ShizukuShell
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.shizuku.ShizukuState
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.sync.CompatServiceSync
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.ui.ThemedActivity
import java.util.concurrent.Executors

/**
 * Manager of the compatibility service, opened from the status card and from the settings screen.
 * Three sections: live state rows (each copyable), the control policy (applied as soon as an option
 * is chosen), and the unattended methods the user allows. Mirrors the AutoJs6 accessibility service
 * manager in shape and wording.
 */
internal class CompatServiceManagerDialog private constructor(
    private val activity: ThemedActivity,
    private val onServiceChanged: () -> Unit,
) {
    private class StateRow(val view: View, val value: TextView)

    private val controller = CompatServiceController(activity)
    private val handler = Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()
    private val palette = activity.palette
    private var settings: CompatSettings = activity.settingsStore.load()
    private var busy = false

    private lateinit var compatRow: StateRow
    private lateinit var hostRow: StateRow
    private lateinit var rootRow: StateRow
    private lateinit var secureRow: StateRow
    private lateinit var shizukuRow: StateRow
    private val policyRadios = LinkedHashMap<ServicePolicy, RadioButton>()
    private var availability: ControlAvailability? = null

    private val settingsObserver = object : ContentObserver(handler) {
        override fun onChange(selfChange: Boolean) = refresh()
    }

    private val dialog: AlertDialog

    init {
        val content = LinearLayout(activity).apply {
            orientation = LinearLayout.VERTICAL
            setPaddingRelative(0, activity.dp(4), 0, activity.dp(8))
            addView(sectionTitle(R.string.manager_section_state))
            compatRow = addStateRow(this, R.string.state_compat_service)
            hostRow = addStateRow(this, R.string.state_host_service)
            rootRow = addStateRow(this, R.string.state_root)
            secureRow = addStateRow(this, R.string.state_secure_settings) { showSecureSettingsHelp() }
            shizukuRow = addStateRow(this, R.string.state_shizuku) { onShizukuRowClicked() }

            addView(sectionTitle(R.string.manager_section_control))
            ServicePolicy.entries.forEach { policy ->
                val (row, radio) = activity.radioRow(
                    activity.getString(policy.labelRes()),
                    activity.getString(policy.summaryRes()),
                    settings.servicePolicy == policy,
                ) { selectPolicy(policy) }
                policyRadios[policy] = radio
                addView(row)
            }

            addView(sectionTitle(R.string.manager_section_automation))
            addView(activity.switchRow(activity.getString(R.string.auto_root_title), activity.getString(R.string.auto_root_summary), settings.enableWithRoot) { checked ->
                update { it.copy(enableWithRoot = checked) }
            }.aligned())
            addView(activity.switchRow(activity.getString(R.string.auto_secure_settings_title), activity.getString(R.string.auto_secure_settings_summary), settings.enableWithSecureSettings) { checked ->
                update { it.copy(enableWithSecureSettings = checked) }
            }.aligned())
            addView(activity.switchRow(activity.getString(R.string.auto_shizuku_title), activity.getString(R.string.auto_shizuku_summary), settings.enableWithShizuku) { checked ->
                update { it.copy(enableWithShizuku = checked) }
            }.aligned())
        }
        dialog = AlertDialog.Builder(activity)
            .setTitle(R.string.manager_title)
            .setView(ScrollView(activity).apply { addView(content) })
            .setPositiveButton(R.string.action_system_settings) { _, _ -> AccessibilitySettingsLauncher.open(activity) }
            .setNeutralButton(R.string.action_copy, null)
            .setNegativeButton(R.string.action_close, null)
            .setOnDismissListener { onDismissed() }
            .create()
    }

    fun dismiss() = dialog.dismiss()

    private fun show(): CompatServiceManagerDialog {
        activity.showDialog(dialog)
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL)?.setOnClickListener { copyReport() }
        runCatching {
            activity.contentResolver.registerContentObserver(
                Secure.getUriFor(Secure.ENABLED_ACCESSIBILITY_SERVICES),
                false,
                settingsObserver,
            )
        }
        refresh()
        return this
    }

    private fun onDismissed() {
        runCatching { activity.contentResolver.unregisterContentObserver(settingsObserver) }
        handler.removeCallbacksAndMessages(null)
        executor.shutdown()
        onServiceChanged()
    }

    /* Building. */

    private fun sectionTitle(titleRes: Int): View = activity.sectionTitle(activity.getString(titleRes)).apply {
        setPaddingRelative(activity.dp(24), activity.dp(14), activity.dp(24), activity.dp(4))
    }

    /** Dialog rows share the 24dp inset of the title instead of the card inset of the screens. */
    private fun LinearLayout.aligned(): LinearLayout = apply {
        setPaddingRelative(activity.dp(24), paddingTop, activity.dp(20), paddingBottom)
    }

    private fun addStateRow(container: LinearLayout, labelRes: Int, onClick: (() -> Unit)? = null): StateRow {
        val row = activity.valueRow(activity.getString(labelRes), "") {
            onClick?.invoke() ?: copyRow(labelRes)
        }
        row.setPaddingRelative(activity.dp(24), activity.dp(6), activity.dp(24), activity.dp(6))
        container.addView(row)
        return StateRow(row, row.getChildAt(1) as TextView)
    }

    /* State. */

    private fun refresh() {
        if (executor.isShutdown) return
        executor.execute {
            val serviceState = runCatching { controller.serviceState() }.getOrDefault(ServiceState.DISABLED)
            val hostState = runCatching { controller.hostServiceState() }.getOrDefault(HostServiceState.NOT_INSTALLED)
            val availability = runCatching { controller.availability() }.getOrNull()
            handler.post {
                if (!dialog.isShowing) return@post
                this.availability = availability
                setValue(compatRow, when (serviceState) {
                    ServiceState.ENABLED -> R.string.value_enabled
                    ServiceState.ENABLED_NOT_BOUND -> R.string.value_enabled_not_bound
                    ServiceState.DISABLED -> R.string.value_disabled
                }, if (serviceState == ServiceState.ENABLED) palette.positive else if (serviceState == ServiceState.DISABLED) palette.secondaryText else palette.warning)
                setValue(hostRow, when (hostState) {
                    HostServiceState.ENABLED -> R.string.value_enabled
                    HostServiceState.DISABLED -> R.string.value_disabled
                    HostServiceState.NOT_INSTALLED -> R.string.value_not_installed
                }, if (hostState == HostServiceState.ENABLED) palette.positive else palette.secondaryText)
                if (availability != null) {
                    setValue(rootRow, if (availability.rootPresent) R.string.value_available else R.string.value_unavailable, if (availability.rootPresent) palette.positive else palette.secondaryText)
                    setValue(secureRow, if (availability.secureSettingsGranted) R.string.value_granted else R.string.value_not_granted, if (availability.secureSettingsGranted) palette.positive else palette.secondaryText)
                    setValue(shizukuRow, when (availability.shizuku) {
                        ShizukuState.NOT_INSTALLED -> R.string.value_not_installed
                        ShizukuState.NOT_RUNNING -> R.string.value_not_running
                        ShizukuState.PERMISSION_DENIED -> R.string.value_not_granted
                        ShizukuState.AVAILABLE -> R.string.value_available
                    }, if (availability.shizuku == ShizukuState.AVAILABLE) palette.positive else palette.secondaryText)
                }
            }
        }
    }

    private fun setValue(row: StateRow, valueRes: Int, color: Int) {
        row.value.text = activity.getString(valueRes)
        row.value.setTextColor(color)
    }

    private fun scheduleRefreshes() {
        listOf(400L, 1_500L, 3_000L).forEach { delay -> handler.postDelayed({ refresh() }, delay) }
    }

    /* Control. */

    private fun selectPolicy(policy: ServicePolicy) {
        policyRadios.forEach { (entry, radio) -> radio.isChecked = entry == policy }
        update { it.copy(servicePolicy = policy) }
        applyPolicy()
    }

    private fun update(transform: (CompatSettings) -> CompatSettings) {
        settings = activity.settingsStore.update(transform)
        CompatServiceSync.ensureWatcher(activity)
    }

    private fun applyPolicy() {
        if (busy || executor.isShutdown) return
        busy = true
        executor.execute {
            val outcome = runCatching { controller.sync() }.getOrElse { SyncOutcome(ServiceControlPolicy.Decision.NONE, succeeded = false) }
            handler.post {
                busy = false
                if (!dialog.isShowing) return@post
                activity.toast(outcomeMessage(outcome), long = outcome.requiresManualAction)
                if (outcome.requiresManualAction) AccessibilitySettingsLauncher.open(activity)
                refresh()
                scheduleRefreshes()
                onServiceChanged()
            }
        }
    }

    private fun outcomeMessage(outcome: SyncOutcome): String = when {
        outcome.decision == ServiceControlPolicy.Decision.NONE -> activity.getString(R.string.sync_no_change)
        outcome.succeeded && outcome.decision == ServiceControlPolicy.Decision.ENABLE ->
            activity.getString(R.string.sync_enabled_via, viaLabel(outcome.via))
        outcome.succeeded -> activity.getString(R.string.sync_disabled_via, viaLabel(outcome.via))
        else -> activity.getString(R.string.sync_manual_required)
    }

    private fun viaLabel(via: String?): String = activity.getString(
        when (via) {
            CompatServiceController.VIA_ROOT -> R.string.via_root
            CompatServiceController.VIA_SECURE_SETTINGS -> R.string.via_secure_settings
            CompatServiceController.VIA_SHIZUKU -> R.string.via_shizuku
            else -> R.string.via_self
        },
    )

    /* Helpers for the elevated methods. */

    private fun onShizukuRowClicked() {
        when (availability?.shizuku) {
            ShizukuState.PERMISSION_DENIED -> {
                val requested = ShizukuShell.requestPermission { granted ->
                    handler.post {
                        if (!dialog.isShowing) return@post
                        activity.toast(activity.getString(if (granted) R.string.shizuku_permission_granted else R.string.shizuku_permission_denied))
                        refresh()
                    }
                }
                if (!requested) activity.toast(activity.getString(R.string.shizuku_not_running_hint), long = true)
            }
            ShizukuState.NOT_RUNNING, ShizukuState.NOT_INSTALLED -> {
                val launch = ShizukuShell.managerLaunchIntent(activity)
                if (launch == null || !AccessibilitySettingsLauncher.startFirstAvailable(activity, listOf(launch))) {
                    activity.toast(activity.getString(R.string.shizuku_not_running_hint), long = true)
                }
            }
            else -> copyRow(R.string.state_shizuku)
        }
    }

    private fun showSecureSettingsHelp() {
        val command = CompatServiceController.secureSettingsGrantCommand(activity)
        val helpDialog = AlertDialog.Builder(activity)
            .setTitle(R.string.secure_settings_grant_title)
            .setMessage(activity.getString(R.string.secure_settings_grant_body, command))
            .setPositiveButton(R.string.action_copy) { _, _ -> copyToClipboard(command) }
            .setNegativeButton(R.string.action_close, null)
            .create()
        activity.showDialog(helpDialog)
    }

    private fun copyRow(labelRes: Int) {
        val row = when (labelRes) {
            R.string.state_compat_service -> compatRow
            R.string.state_host_service -> hostRow
            R.string.state_root -> rootRow
            R.string.state_secure_settings -> secureRow
            else -> shizukuRow
        }
        copyToClipboard("${activity.getString(labelRes)}: ${row.value.text}")
    }

    private fun copyReport() {
        val lines = listOf(
            R.string.state_compat_service to compatRow,
            R.string.state_host_service to hostRow,
            R.string.state_root to rootRow,
            R.string.state_secure_settings to secureRow,
            R.string.state_shizuku to shizukuRow,
        ).map { (labelRes, row) -> "${activity.getString(labelRes)}: ${row.value.text}" } +
            "${activity.getString(R.string.manager_section_control)}: ${activity.getString(settings.servicePolicy.labelRes())}" +
            "${activity.getString(R.string.state_service_component)}: ${controller.serviceId}"
        copyToClipboard(lines.joinToString("\n"))
    }

    private fun copyToClipboard(text: String) {
        val clipboard = activity.getSystemService(ClipboardManager::class.java) ?: return
        clipboard.setPrimaryClip(ClipData.newPlainText(activity.getString(R.string.manager_title), text))
        activity.toast(activity.getString(R.string.copied_to_clipboard))
    }

    companion object {
        fun show(activity: ThemedActivity, onServiceChanged: () -> Unit = {}): CompatServiceManagerDialog =
            CompatServiceManagerDialog(activity, onServiceChanged).show()
    }
}

private fun ServicePolicy.summaryRes(): Int = when (this) {
    ServicePolicy.FOLLOW_HOST -> R.string.policy_follow_host_summary
    ServicePolicy.ENABLED -> R.string.policy_enabled_summary
    ServicePolicy.DISABLED -> R.string.policy_disabled_summary
}
