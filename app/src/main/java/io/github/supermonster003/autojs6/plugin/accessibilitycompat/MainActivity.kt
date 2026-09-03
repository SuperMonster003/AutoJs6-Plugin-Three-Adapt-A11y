package io.github.supermonster003.autojs6.plugin.accessibilitycompat

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var statusView: TextView

    private val isDarkTheme: Boolean
        get() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
            Configuration.UI_MODE_NIGHT_YES

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        configureSystemBars()
        setContentView(createContentView())
    }

    override fun onResume() {
        super.onResume()
        if (::statusView.isInitialized) updateServiceStatus()
    }

    private fun createContentView(): View {
        val palette = Palette(isDarkTheme)
        return ScrollView(this).apply {
            isFillViewport = true
            setBackgroundColor(palette.background)
            addView(
                LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER_HORIZONTAL
                    setPadding(dp(24), dp(36), dp(24), dp(28))

                    addView(ImageView(context).apply {
                        setImageResource(R.mipmap.ic_launcher)
                        contentDescription = getString(R.string.app_name)
                        scaleType = ImageView.ScaleType.CENTER_INSIDE
                    }, layoutParams(dp(88), dp(88), bottom = 18))

                    addView(textView(
                        text = getString(R.string.app_name),
                        sizeSp = 26f,
                        color = palette.primaryText,
                        typeface = Typeface.DEFAULT_BOLD,
                        gravity = Gravity.CENTER,
                    ), layoutParams(matchParent(), wrapContent(), bottom = 10))

                    addView(textView(
                        text = getString(R.string.screen_intro),
                        sizeSp = 15f,
                        color = palette.secondaryText,
                        gravity = Gravity.CENTER,
                    ), layoutParams(matchParent(), wrapContent(), bottom = 24))

                    statusView = textView(
                        text = "",
                        sizeSp = 16f,
                        color = Color.WHITE,
                        typeface = Typeface.DEFAULT_BOLD,
                        gravity = Gravity.CENTER,
                    ).apply {
                        setPadding(dp(16), dp(14), dp(16), dp(14))
                    }
                    addView(statusView, layoutParams(matchParent(), wrapContent(), bottom = 14))

                    addView(primaryButton(getString(R.string.button_open_accessibility_settings), palette) {
                        openAccessibilitySettings()
                    }, layoutParams(matchParent(), wrapContent(), bottom = 10))

                    addView(secondaryButton(getString(R.string.button_refresh), palette) {
                        updateServiceStatus()
                    }, layoutParams(matchParent(), wrapContent(), bottom = 10))

                    addView(secondaryButton(getString(R.string.button_open_supported_app), palette) {
                        openSupportedApp()
                    }, layoutParams(matchParent(), wrapContent(), bottom = 28))

                    addSection(
                        title = getString(R.string.supported_apps_title),
                        body = supportedAppsSummary(),
                        palette = palette,
                    )

                    addSection(
                        title = getString(R.string.mechanism_title),
                        body = getString(R.string.mechanism_body),
                        palette = palette,
                    )
                    addSection(
                        title = getString(R.string.privacy_title),
                        body = getString(R.string.privacy_body),
                        palette = palette,
                    )
                    addSection(
                        title = getString(R.string.limitations_title),
                        body = getString(R.string.limitations_body),
                        palette = palette,
                    )

                    addView(textView(
                        text = getString(R.string.version_format, appVersionName()),
                        sizeSp = 13f,
                        color = palette.secondaryText,
                        gravity = Gravity.CENTER,
                    ), layoutParams(matchParent(), wrapContent(), top = 6))
                },
                ViewGroup.LayoutParams(matchParent(), wrapContent()),
            )
        }.also { updateServiceStatus() }
    }

    private fun LinearLayout.addSection(title: String, body: String, palette: Palette) {
        val section = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(16), dp(18), dp(17))
            background = roundedBackground(palette.surface, 18f, palette.outline)
            addView(textView(
                text = title,
                sizeSp = 17f,
                color = palette.primaryText,
                typeface = Typeface.DEFAULT_BOLD,
            ), layoutParams(matchParent(), wrapContent(), bottom = 7))
            addView(textView(
                text = body,
                sizeSp = 14f,
                color = palette.secondaryText,
            ), layoutParams(matchParent(), wrapContent()))
        }
        addView(section, layoutParams(matchParent(), wrapContent(), bottom = 14))
    }

    private fun primaryButton(label: String, palette: Palette, action: () -> Unit): Button =
        Button(this).apply {
            text = label
            isAllCaps = false
            textSize = 15f
            setTextColor(Color.WHITE)
            backgroundTintList = ColorStateList.valueOf(palette.accent)
            setOnClickListener { action() }
            minHeight = dp(52)
        }

    private fun secondaryButton(label: String, palette: Palette, action: () -> Unit): Button =
        Button(this).apply {
            text = label
            isAllCaps = false
            textSize = 15f
            setTextColor(palette.primaryText)
            backgroundTintList = ColorStateList.valueOf(palette.buttonSurface)
            setOnClickListener { action() }
            minHeight = dp(52)
        }

    private fun textView(
        text: String,
        sizeSp: Float,
        color: Int,
        typeface: Typeface = Typeface.DEFAULT,
        gravity: Int = Gravity.START,
    ) = TextView(this).apply {
        this.text = text
        textSize = sizeSp
        setTextColor(color)
        setTypeface(typeface)
        this.gravity = gravity
        setLineSpacing(0f, 1.15f)
    }

    private fun updateServiceStatus() {
        val enabled = AccessibilityServiceStatus.isEnabled(this)
        val state = getString(
            if (enabled) R.string.service_status_enabled else R.string.service_status_disabled,
        )
        statusView.text = getString(R.string.service_status_format, state)
        statusView.background = roundedBackground(
            if (enabled) Color.rgb(15, 140, 118) else Color.rgb(190, 114, 25),
            16f,
        )
    }

    private fun openAccessibilitySettings() {
        val component = ComponentName(
            packageName,
            AccessibilityCompatContract.COMPATIBILITY_SERVICE_CLASS,
        )
        val intents = listOf(
            Intent(ACTION_ACCESSIBILITY_DETAILS_SETTINGS).apply {
                putExtra(EXTRA_COMPONENT_NAME, component.flattenToString())
            },
            Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS),
        )
        if (!startFirstAvailable(intents)) {
            Toast.makeText(this, R.string.settings_unavailable, Toast.LENGTH_LONG).show()
        }
    }

    private fun openSupportedApp() {
        val launchIntent = AccessibilityCompatContract.SUPPORTED_PACKAGES
            .asSequence()
            .mapNotNull(packageManager::getLaunchIntentForPackage)
            .firstOrNull()
            ?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (launchIntent == null || !startFirstAvailable(listOf(launchIntent))) {
            Toast.makeText(this, R.string.supported_app_unavailable, Toast.LENGTH_LONG).show()
        }
    }

    private fun supportedAppsSummary(): String =
        AccessibilityCompatContract.SUPPORTED_PACKAGES.joinToString(separator = "\n") { targetPackage ->
            val label = runCatching {
                val applicationInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    packageManager.getApplicationInfo(
                        targetPackage,
                        PackageManager.ApplicationInfoFlags.of(0),
                    )
                } else {
                    @Suppress("DEPRECATION")
                    packageManager.getApplicationInfo(targetPackage, 0)
                }
                packageManager.getApplicationLabel(applicationInfo).toString()
            }.getOrNull()
            if (label.isNullOrBlank()) targetPackage else "$label ($targetPackage)"
        }

    private fun startFirstAvailable(intents: Iterable<Intent>): Boolean {
        for (intent in intents) {
            try {
                startActivity(intent)
                return true
            } catch (_: ActivityNotFoundException) {
                // Try the next, more general settings entry point.
            } catch (_: SecurityException) {
                // Some OEMs expose the action but block third-party callers.
            } catch (_: RuntimeException) {
                // A broken OEM settings activity must not crash this companion.
            }
        }
        return false
    }

    @Suppress("DEPRECATION")
    private fun appVersionName(): String = runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, android.content.pm.PackageManager.PackageInfoFlags.of(0))
        } else {
            packageManager.getPackageInfo(packageName, 0)
        }.versionName.orEmpty()
    }.getOrDefault("")

    @Suppress("DEPRECATION")
    private fun configureSystemBars() {
        val dark = isDarkTheme
        val palette = Palette(dark)
        window.statusBarColor = palette.background
        window.navigationBarColor = palette.background
        window.decorView.systemUiVisibility = if (dark) 0 else {
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                } else {
                    0
                }
        }
    }

    private fun layoutParams(
        width: Int,
        height: Int,
        top: Int = 0,
        bottom: Int = 0,
    ) = LinearLayout.LayoutParams(width, height).apply {
        topMargin = dp(top)
        bottomMargin = dp(bottom)
    }

    private fun roundedBackground(color: Int, radiusDp: Float, strokeColor: Int? = null) =
        GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(color)
            cornerRadius = dp(radiusDp).toFloat()
            strokeColor?.let { setStroke(dp(1), it) }
        }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density + 0.5f).toInt()

    private fun dp(value: Float): Int = (value * resources.displayMetrics.density + 0.5f).toInt()

    private fun matchParent() = ViewGroup.LayoutParams.MATCH_PARENT

    private fun wrapContent() = ViewGroup.LayoutParams.WRAP_CONTENT

    private data class Palette(val dark: Boolean) {
        val background = if (dark) Color.rgb(16, 20, 27) else Color.rgb(246, 248, 252)
        val surface = if (dark) Color.rgb(29, 35, 45) else Color.WHITE
        val buttonSurface = if (dark) Color.rgb(47, 57, 71) else Color.rgb(225, 231, 241)
        val primaryText = if (dark) Color.rgb(240, 244, 250) else Color.rgb(25, 33, 45)
        val secondaryText = if (dark) Color.rgb(186, 196, 211) else Color.rgb(79, 91, 108)
        val outline = if (dark) Color.rgb(58, 68, 83) else Color.rgb(219, 225, 234)
        val accent = if (dark) Color.rgb(45, 171, 151) else Color.rgb(31, 104, 172)
    }

    private companion object {
        const val ACTION_ACCESSIBILITY_DETAILS_SETTINGS =
            "android.settings.ACCESSIBILITY_DETAILS_SETTINGS"
        const val EXTRA_COMPONENT_NAME = "android.intent.extra.COMPONENT_NAME"
    }
}
