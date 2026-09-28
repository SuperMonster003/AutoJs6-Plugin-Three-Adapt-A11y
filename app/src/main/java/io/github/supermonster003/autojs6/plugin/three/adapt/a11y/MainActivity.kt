package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control.CompatServiceController
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control.HostServiceState
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.control.ServiceState
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.sync.CompatServiceSync
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui.ThemedActivity

internal class MainActivity : ThemedActivity() {
    private lateinit var statusValue: TextView
    private lateinit var statusDetail: TextView
    private lateinit var statusIndicator: View
    private var managerDialog: CompatServiceManagerDialog? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(screen(null) { buildContent() })
    }

    override fun onResume() {
        super.onResume()
        updateStatus()
        CompatServiceSync.request(this, "main-resume") { updateStatus() }
        // The service binds a moment after the system lists it; pick that up without a manual refresh.
        listOf(1_000L, 3_000L).forEach { delay -> handler.postDelayed(::updateStatus, delay) }
    }

    override fun onPause() {
        handler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun onDestroy() {
        managerDialog?.dismiss()
        managerDialog = null
        super.onDestroy()
    }

    private fun LinearLayout.buildContent() {
        addView(header(), lp(MATCH_PARENT, WRAP_CONTENT))
        addView(statusCard(), lp(MATCH_PARENT, WRAP_CONTENT).margins(top = 22))
        addView(sectionTitle(getString(R.string.supported_apps_title)))
        addView(supportedAppsCard(), lp(MATCH_PARENT, WRAP_CONTENT))
        addView(
            text(getString(R.string.version_format, versionName()), 12.5f, palette.secondaryText, gravity = Gravity.CENTER),
            lp(MATCH_PARENT, WRAP_CONTENT).margins(top = 26),
        )
    }

    private fun header(): View = FrameLayout(this).apply {
        addView(
            LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL
                setPaddingRelative(0, dp(20), 0, 0)
                addView(
                    ImageView(context).apply {
                        setImageResource(R.mipmap.ic_launcher)
                        contentDescription = getString(R.string.app_name)
                        scaleType = ImageView.ScaleType.CENTER_INSIDE
                    },
                    lp(dp(84), dp(84)).margins(bottom = 14),
                )
                addView(
                    text(getString(R.string.app_name), 23f, palette.primaryText, Typeface.DEFAULT_BOLD, Gravity.CENTER),
                    lp(MATCH_PARENT, WRAP_CONTENT).margins(bottom = 8, start = 40, end = 40),
                )
                addView(
                    text(getString(R.string.screen_intro), 14.5f, palette.secondaryText, gravity = Gravity.CENTER),
                    lp(MATCH_PARENT, WRAP_CONTENT).margins(start = 12, end = 12),
                )
            },
            FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT),
        )
        addView(
            iconButton(R.drawable.ic_settings, getString(R.string.settings_title), palette.secondaryText) {
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            },
            FrameLayout.LayoutParams(dp(44), dp(44), Gravity.TOP or Gravity.END),
        )
    }

    private fun statusCard(): View {
        statusIndicator = View(this)
        statusValue = text("", 20f, palette.primaryText, Typeface.DEFAULT_BOLD)
        statusDetail = text("", 13f, palette.secondaryText).apply { setPaddingRelative(0, dp(3), 0, 0) }
        return card {
            addView(
                LinearLayout(context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
                    setPaddingRelative(dp(20), dp(18), dp(14), dp(18))
                    addView(statusIndicator, lp(dp(14), dp(14)).margins(end = 16))
                    addView(
                        LinearLayout(context).apply {
                            orientation = LinearLayout.VERTICAL
                            addView(text(getString(R.string.status_card_title), 13f, palette.secondaryText))
                            addView(statusValue)
                            addView(statusDetail)
                        },
                        lp(0, WRAP_CONTENT, weight = 1f),
                    )
                    addView(
                        ImageView(context).apply {
                            setImageDrawable(icon(R.drawable.ic_chevron_right, palette.secondaryText))
                            alpha = 0.7f
                        },
                        lp(dp(24), dp(24)).margins(start = 8),
                    )
                    isClickable = true
                    isFocusable = true
                    background = rippleBackground()
                    setOnClickListener { managerDialog = CompatServiceManagerDialog.show(this@MainActivity) { updateStatus() } }
                },
            )
        }
    }

    private fun supportedAppsCard(): View = card {
        ThreeAdaptA11yContract.SUPPORTED_PACKAGES.forEachIndexed { index, targetPackage ->
            if (index > 0) addView(divider(72))
            addView(supportedAppRow(targetPackage))
        }
    }

    private fun supportedAppRow(targetPackage: String): View {
        val launchIntent = packageManager.getLaunchIntentForPackage(targetPackage)
        val label = applicationLabel(targetPackage)
        val trailing = if (launchIntent != null) {
            iconButton(R.drawable.ic_open_in_new, getString(R.string.action_open), palette.accent) { launch(launchIntent) }
        } else {
            text(getString(R.string.value_not_installed), 12.5f, palette.secondaryText)
        }
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            minimumHeight = dp(68)
            setPaddingRelative(dp(18), dp(12), dp(10), dp(12))
            addView(
                ImageView(context).apply {
                    setImageDrawable(applicationIcon(targetPackage) ?: icon(R.drawable.ic_apps, palette.secondaryText))
                    scaleType = ImageView.ScaleType.FIT_CENTER
                },
                lp(dp(40), dp(40)).margins(end = 14),
            )
            addView(
                LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    addView(text(label ?: targetPackage, 15.5f, palette.primaryText))
                    if (label != null) {
                        addView(text(targetPackage, 12.5f, palette.secondaryText).apply { setPaddingRelative(0, dp(2), 0, 0) })
                    }
                },
                lp(0, WRAP_CONTENT, weight = 1f),
            )
            addView(trailing, lp(WRAP_CONTENT, WRAP_CONTENT).margins(start = 8, end = if (launchIntent != null) 0 else 8))
        }
    }

    private fun launch(intent: Intent) {
        if (!AccessibilitySettingsLauncher.startFirstAvailable(this, listOf(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)))) {
            toast(getString(R.string.app_launch_failed), long = true)
        }
    }

    private fun updateStatus() {
        if (!::statusValue.isInitialized) return
        val controller = CompatServiceController(this)
        val state = controller.serviceState()
        val (valueRes, color) = when (state) {
            ServiceState.ENABLED -> R.string.value_enabled to palette.positive
            ServiceState.ENABLED_NOT_BOUND -> R.string.value_enabled_not_bound to palette.warning
            ServiceState.DISABLED -> R.string.value_disabled to palette.secondaryText
        }
        statusValue.text = getString(valueRes)
        statusIndicator.background = roundedBackground(color, 7f)
        val hostState = when (controller.hostServiceState()) {
            HostServiceState.ENABLED -> R.string.value_enabled
            HostServiceState.DISABLED -> R.string.value_disabled
            HostServiceState.NOT_INSTALLED -> R.string.value_not_installed
        }
        statusDetail.text = getString(
            R.string.status_detail_format,
            getString(settingsStore.load().servicePolicy.labelRes()),
            getString(R.string.host_service_state_format, getString(hostState)),
        )
    }

    private fun applicationLabel(targetPackage: String): String? = runCatching {
        packageManager.getApplicationLabel(applicationInfo(targetPackage)).toString().takeIf { it.isNotBlank() }
    }.getOrNull()

    private fun applicationIcon(targetPackage: String): Drawable? = runCatching {
        packageManager.getApplicationIcon(applicationInfo(targetPackage))
    }.getOrNull()

    private fun applicationInfo(targetPackage: String) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getApplicationInfo(targetPackage, PackageManager.ApplicationInfoFlags.of(0))
    } else {
        @Suppress("DEPRECATION")
        packageManager.getApplicationInfo(targetPackage, 0)
    }

    private fun versionName(): String = runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0)
        }.versionName.orEmpty()
    }.getOrDefault("")
}
