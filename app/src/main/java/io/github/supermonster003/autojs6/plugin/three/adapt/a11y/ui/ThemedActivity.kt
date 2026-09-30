package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.content.res.Configuration
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.radiobutton.MaterialRadioButton
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.R
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.settings.CompatSettingsStore

/**
 * Base of every screen: applies the language, dark-mode and theme-color settings (following AutoJs6
 * when selected), recreates itself when they change, and offers the small programmatic view kit the
 * screens are built from. Interactive controls use Material 3 with an explicit runtime palette.
 */
internal abstract class ThemedActivity : AppCompatActivity() {
    lateinit var palette: Palette
        private set

    val settingsStore: CompatSettingsStore by lazy { CompatSettingsStore(this) }

    private var appliedAppearance: Int? = null
    private var recreateRequested = false
    private lateinit var systemContext: Context
    private var appearanceGeneration = 0
    private var interacted = false
    protected open fun hasUnconfirmedDialog(): Boolean = false
    override fun onUserInteraction() { interacted = true; super.onUserInteraction() }

    override fun attachBaseContext(newBase: Context) {
        systemContext = newBase
        val configured = AppConfiguration.wrap(newBase)
        // Keep AppCompat dialogs on the same explicit app night mode as the wrapped resources.
        delegate.localNightMode = if (configured.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES)
            AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        super.attachBaseContext(configured)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        io.github.supermonster003.autojs6.plugin.three.adapt.a11y.LauncherIcons.normalizeAsync(this)
        actionBar?.hide()
        palette = Palette.resolve(this)
        appliedAppearance = AppConfiguration.appearanceSignature(systemContext)
        configureWindow()
        applyLayoutDirection(window)
    }

    /**
     * The window root inherits its direction from the device configuration, not from the locale
     * override applied in [attachBaseContext]; pinning it before the root is attached is what makes
     * an Arabic UI actually mirror.
     */
    private fun applyLayoutDirection(window: android.view.Window?) {
        window?.decorView?.layoutDirection = resources.configuration.layoutDirection
    }

    /** Screens recreate themselves when the language, dark mode or theme color they show changed. */
    override fun onResume() {
        super.onResume()
        interacted = false
        if (!recreateRequested && !hasUnconfirmedDialog() && AppConfiguration.appearanceSignature(systemContext) != appliedAppearance) {
            recreateRequested = true
            recreate()
            return
        }
        val expected = ++appearanceGeneration
        io.github.supermonster003.autojs6.plugin.three.adapt.a11y.host.HostSettingsClient.refresh(applicationContext) { next ->
            if (expected != appearanceGeneration || isFinishing || isDestroyed) return@refresh
            io.github.supermonster003.autojs6.plugin.three.adapt.a11y.host.HostSettingsClient.publish(next)
            if (!interacted && !hasUnconfirmedDialog() && !recreateRequested && AppConfiguration.appearanceSignature(systemContext) != appliedAppearance) {
                recreateRequested = true
                recreate()
            }
        }
    }

    override fun onPause() { appearanceGeneration++; super.onPause() }

    /* Screen scaffolding. */

    /** Root with system-bar insets applied, an optional [toolbar] and a scrolling [content]. */
    fun screen(toolbar: View?, pageInsetDp: Int = SCREEN_MARGIN, content: LinearLayout.() -> Unit): View {
        val column = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            toolbar?.let { addView(it, lp(MATCH_PARENT, WRAP_CONTENT)) }
            addView(
                ScrollView(this@ThemedActivity).apply {
                    isFillViewport = true
                    isVerticalScrollBarEnabled = false
                    addView(
                        LinearLayout(context).apply {
                            orientation = LinearLayout.VERTICAL
                            setPaddingRelative(dp(pageInsetDp), dp(4), dp(pageInsetDp), dp(28))
                            content()
                        },
                        ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT),
                    )
                },
                lp(MATCH_PARENT, 0, weight = 1f),
            )
        }
        return FrameLayout(this).apply {
            setBackgroundColor(palette.background)
            addView(column, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
            applySystemBarInsets(this)
        }
    }

    fun toolbar(title: CharSequence, showBack: Boolean, actions: List<View> = emptyList()): LinearLayout =
        LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            minimumHeight = dp(56)
            setPaddingRelative(dp(if (showBack) 8 else SCREEN_MARGIN), 0, dp(8), 0)
            if (showBack) {
                addView(iconButton(R.drawable.ic_arrow_back, getString(R.string.navigation_back)) { finish() })
            }
            addView(
                text(title, 19f, palette.primaryText, Typeface.DEFAULT_BOLD).apply {
                    maxLines = 1
                    setPaddingRelative(dp(if (showBack) 8 else 0), 0, dp(8), 0)
                },
                lp(0, WRAP_CONTENT, weight = 1f),
            )
            actions.forEach { addView(it) }
        }

    /* Text and containers. */

    fun text(
        text: CharSequence,
        sizeSp: Float,
        color: Int,
        typeface: Typeface = Typeface.DEFAULT,
        gravity: Int = Gravity.START,
    ): TextView = TextView(this).apply {
        this.text = text
        textSize = sizeSp
        setTextColor(color)
        setLinkTextColor(palette.accent)
        highlightColor = ColorPolicy.withAlpha(palette.accent, 0x55)
        if (Build.VERSION.SDK_INT >= 29) {
            textSelectHandle?.mutate()?.apply { setTint(palette.accent) }?.let(::setTextSelectHandle)
            textSelectHandleLeft?.mutate()?.apply { setTint(palette.accent) }?.let(::setTextSelectHandleLeft)
            textSelectHandleRight?.mutate()?.apply { setTint(palette.accent) }?.let(::setTextSelectHandleRight)
        }
        setTypeface(typeface)
        this.gravity = gravity
        setLineSpacing(0f, 1.18f)
    }

    fun sectionTitle(title: CharSequence): TextView =
        text(title, 14f, palette.secondaryText, Typeface.create("sans-serif-medium", Typeface.NORMAL)).apply {
            setPaddingRelative(dp(24), dp(24), dp(24), dp(8))
            letterSpacing = 0.02f
        }

    fun card(content: LinearLayout.() -> Unit): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        background = roundedBackground(palette.surface, CARD_RADIUS, palette.outline)
        clipToOutline = true
        content()
    }

    fun divider(insetStartDp: Int = 20): View = View(this).apply {
        setBackgroundColor(palette.divider)
        layoutParams = lp(MATCH_PARENT, dp(1)).apply { marginStart = dp(insetStartDp) }
    }

    /* Rows. */

    fun row(
        title: CharSequence,
        summary: CharSequence? = null,
        @DrawableRes iconRes: Int? = null,
        trailing: View? = null,
        onClick: (() -> Unit)? = null,
    ): LinearLayout {
        val shell = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            minimumHeight = dp(if (summary.isNullOrEmpty()) 56 else 72)
            setPaddingRelative(dp(24), dp(12), dp(24), dp(12))
        }
        iconRes?.let { shell.addView(rowIcon(it, palette.secondaryText)) }
        shell.addView(textColumn(title, summary), lp(0, WRAP_CONTENT, weight = 1f))
        if (trailing != null) {
            // Keep a fixed size the caller assigned; a bare View would otherwise fill the row.
            val params = trailing.layoutParams as? LinearLayout.LayoutParams ?: lp(WRAP_CONTENT, WRAP_CONTENT)
            shell.addView(trailing, params.apply { marginStart = dp(12) })
        } else if (onClick != null) {
            shell.addView(
                ImageView(this).apply {
                    setImageDrawable(icon(R.drawable.ic_settings_chevron, palette.secondaryText))
                    alpha = 0.7f
                },
                lp(dp(24), dp(24)).apply { marginStart = dp(16) },
            )
        }
        if (onClick != null) {
            shell.isClickable = true
            shell.isFocusable = true
            shell.background = rippleBackground()
            shell.setOnClickListener { onClick() }
        }
        return shell
    }

    fun switchRow(
        title: CharSequence,
        summary: CharSequence?,
        checked: Boolean,
        @DrawableRes iconRes: Int? = null,
        onToggle: (Boolean) -> Unit,
    ): LinearLayout {
        val switch = MaterialSwitch(this).apply {
            isChecked = checked
            isClickable = false
            isFocusable = false
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
            tint(this)
        }
        return row(title, summary, iconRes, trailing = switch) {
            switch.isChecked = !switch.isChecked
            onToggle(switch.isChecked)
        }.apply {
            accessibilityDelegate = object : View.AccessibilityDelegate() {
                override fun onInitializeAccessibilityNodeInfo(host: View, info: android.view.accessibility.AccessibilityNodeInfo) {
                    super.onInitializeAccessibilityNodeInfo(host, info)
                    info.className = Switch::class.java.name
                    info.isCheckable = true
                    info.isChecked = switch.isChecked
                    info.text = listOfNotNull(title, rowSummary(this@apply)?.text).joinToString(", ")
                }
            }
        }
    }

    /**
     * A dialog option. The 32dp radio drawable insets its 20dp circle by 6dp, so 18dp of start
     * padding puts the circle on the 24dp inset shared by dialog titles, state rows and switches.
     */
    fun radioRow(
        title: CharSequence,
        summary: CharSequence?,
        checked: Boolean,
        trailing: View? = null,
        onSelect: () -> Unit,
    ): Pair<LinearLayout, RadioButton> {
        val radio = MaterialRadioButton(this).apply {
            isChecked = checked
            isClickable = false
            isFocusable = false
            buttonTintList = controlTint()
        }
        val shell = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            minimumHeight = dp(if (summary.isNullOrEmpty()) 56 else 72)
            setPaddingRelative(dp(24), dp(8), dp(24), dp(8))
            addView(radio, lp(WRAP_CONTENT, WRAP_CONTENT).apply { marginEnd = dp(10) })
            addView(textColumn(title, summary), lp(0, WRAP_CONTENT, weight = 1f))
            if (trailing != null) {
                val params = trailing.layoutParams as? LinearLayout.LayoutParams ?: lp(WRAP_CONTENT, WRAP_CONTENT)
                addView(trailing, params.apply { marginStart = dp(12) })
            }
            isClickable = true
            isFocusable = true
            background = rippleBackground()
            setOnClickListener { onSelect() }
        }
        return shell to radio
    }

    fun valueRow(label: CharSequence, value: CharSequence, valueColor: Int = palette.primaryText, onClick: (() -> Unit)? = null): LinearLayout =
        LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            minimumHeight = dp(40)
            setPaddingRelative(dp(18), dp(6), dp(18), dp(6))
            addView(text(label, 13.5f, palette.secondaryText), lp(0, WRAP_CONTENT, weight = 1f))
            addView(
                text(value, 13.5f, valueColor, Typeface.DEFAULT_BOLD, Gravity.END),
                lp(0, WRAP_CONTENT, weight = 1.2f).apply { marginStart = dp(12) },
            )
            if (onClick != null) {
                isClickable = true
                isFocusable = true
                background = rippleBackground()
                setOnClickListener { onClick() }
            }
        }

    /**
     * Title over optional summary. Both align to the view start rather than to the text
     * direction, so a right-to-left label (an Arabic language name) stays next to its icon
     * or radio button in a left-to-right layout, and the reverse.
     */
    private fun textColumn(title: CharSequence, summary: CharSequence?): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        addView(text(title, 16f, palette.primaryText).apply { textAlignment = View.TEXT_ALIGNMENT_VIEW_START })
        if (!summary.isNullOrEmpty()) {
            addView(text(summary, 14f, palette.secondaryText).apply {
                textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                setPaddingRelative(0, dp(4), 0, 0)
            })
        }
    }

    /** The summary of a [row] built with one, so that screens can refresh it in place. */
    fun rowSummary(row: LinearLayout): TextView? =
        (0 until row.childCount).map(row::getChildAt).filterIsInstance<LinearLayout>().firstOrNull()?.getChildAt(1) as? TextView

    private fun rowIcon(@DrawableRes iconRes: Int, color: Int): ImageView = ImageView(this).apply {
        setImageDrawable(icon(iconRes, color))
        importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        layoutParams = lp(dp(24), dp(24)).apply { marginEnd = dp(16) }
    }

    /* Buttons. */

    fun primaryButton(label: CharSequence, onClick: () -> Unit): Button = Button(this).apply {
        text = label
        isAllCaps = false
        textSize = 15f
        setTextColor(palette.onPrimary)
        backgroundTintList = ColorStateList.valueOf(palette.primary)
        minHeight = dp(50)
        setOnClickListener { onClick() }
    }

    fun tonalButton(label: CharSequence, onClick: () -> Unit): Button = Button(this).apply {
        text = label
        isAllCaps = false
        textSize = 15f
        setTextColor(palette.accent)
        backgroundTintList = ColorStateList.valueOf(palette.accentSurface)
        minHeight = dp(50)
        setOnClickListener { onClick() }
    }

    fun iconButton(@DrawableRes iconRes: Int, contentDescription: CharSequence, color: Int = palette.primaryText, onClick: () -> Unit): ImageButton =
        ImageButton(this).apply {
            setImageDrawable(icon(iconRes, color))
            this.contentDescription = contentDescription
            background = rippleBackground(borderless = true)
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            layoutParams = lp(dp(44), dp(44))
            setOnClickListener { onClick() }
        }

    /* Dialogs. */

    /** One option of [singleChoiceDialog]; [swatch] is a color shown at the end of the row. */
    class Choice(val title: CharSequence, val summary: CharSequence? = null, val swatch: Int? = null)

    /**
     * Options are [radioRow]s instead of the platform list items, so that text sizes, insets and
     * spacing match the rest of the app. Item taps only update a draft; OK commits it, and all
     * other dismissal paths discard it.
     */
    fun singleChoiceDialog(
        title: CharSequence,
        choices: List<Choice>,
        checkedIndex: Int,
        onSelect: (Int) -> Unit,
    ): AlertDialog {
        var draft = checkedIndex
        val radios = mutableListOf<RadioButton>()
        val list = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPaddingRelative(0, dp(4), 0, dp(8))
            choices.forEachIndexed { index, choice ->
                val (row, radio) = radioRow(choice.title, choice.summary, index == checkedIndex, choice.swatch?.let { colorSwatch(it) }) {
                    draft = index
                    radios.forEachIndexed { position, button -> button.isChecked = position == draft }
                }
                radios += radio
                addView(row)
            }
        }
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setView(ScrollView(this).apply { addView(list) })
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(android.R.string.ok) { _, _ -> onSelect(draft) }
            .create()
        showDialog(dialog)
        return dialog
    }

    /** A filled circle for theme colors, used as a row trailing view. */
    fun colorSwatch(color: Int, sizeDp: Int = 22): View = View(this).apply {
        background = roundedBackground(color, sizeDp / 2f, palette.outline)
        layoutParams = lp(dp(sizeDp), dp(sizeDp))
    }

    fun showDialog(dialog: AlertDialog) {
        applyLayoutDirection(dialog.window)
        dialog.show()
        dialog.window?.setBackgroundDrawable(roundedBackground(palette.surface, 24f))
        val width = minOf(dp(560), resources.displayMetrics.widthPixels - dp(48))
        dialog.window?.setLayout(width, WRAP_CONTENT)
        dialog.window?.decorView?.post {
            val maximum = (resources.displayMetrics.heightPixels * 0.85f).toInt()
            if ((dialog.window?.decorView?.height ?: 0) > maximum) dialog.window?.setLayout(width, maximum)
        }
        dialog.findViewById<TextView>(androidx.appcompat.R.id.alertTitle)?.apply { textSize = 20f; setTextColor(palette.primaryText) }
        tintDialog(dialog)
    }

    fun tintDialog(dialog: AlertDialog) {
        listOf(AlertDialog.BUTTON_POSITIVE, AlertDialog.BUTTON_NEGATIVE, AlertDialog.BUTTON_NEUTRAL).forEach { which ->
            dialog.getButton(which)?.apply {
                isAllCaps = false
                setTextColor(ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf()),
                    intArrayOf(ColorPolicy.withAlpha(palette.secondaryText, 0x66), palette.accent)))
                if (this is com.google.android.material.button.MaterialButton)
                    rippleColor = ColorStateList.valueOf(ColorPolicy.withAlpha(palette.accent, 0x2E))
            }
        }
    }

    fun toast(message: CharSequence, long: Boolean = false) {
        Toast.makeText(applicationContext, message, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
    }

    /* Drawables and tints. */

    fun icon(@DrawableRes iconRes: Int, color: Int): Drawable? = getDrawable(iconRes)?.mutate()?.apply { setTint(color) }

    fun roundedBackground(color: Int, radiusDp: Float, strokeColor: Int? = null): GradientDrawable =
        GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(color)
            cornerRadius = dp(radiusDp).toFloat()
            strokeColor?.let { setStroke(dp(1), it) }
        }

    fun rippleBackground(borderless: Boolean = false): Drawable? {
        val value = TypedValue()
        val attr = if (borderless) android.R.attr.selectableItemBackgroundBorderless else android.R.attr.selectableItemBackground
        theme.resolveAttribute(attr, value, true)
        return getDrawable(value.resourceId)?.mutate()?.also {
            (it as? android.graphics.drawable.RippleDrawable)?.setColor(ColorStateList.valueOf(ColorPolicy.withAlpha(palette.accent, 0x2E)))
        }
    }

    fun tint(switch: MaterialSwitch) {
        switch.thumbTintList = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_checked), intArrayOf()),
            intArrayOf(ColorPolicy.withAlpha(palette.secondaryText, 0x55), palette.onPrimary, palette.secondaryText),
        )
        switch.trackTintList = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_checked), intArrayOf()),
            intArrayOf(ColorPolicy.withAlpha(palette.secondaryText, 0x24), palette.primary, palette.surfaceVariant),
        )
    }

    fun controlTint(): ColorStateList = ColorStateList(
        arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_checked), intArrayOf()),
        intArrayOf(ColorPolicy.withAlpha(palette.secondaryText, 0x66), palette.accent, palette.secondaryText),
    )

    /* Metrics. */

    fun dp(value: Int): Int = (value * resources.displayMetrics.density + 0.5f).toInt()

    fun dp(value: Float): Int = (value * resources.displayMetrics.density + 0.5f).toInt()

    fun lp(width: Int, height: Int, weight: Float = 0f): LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(width, height, weight)

    fun LinearLayout.LayoutParams.margins(top: Int = 0, bottom: Int = 0, start: Int = 0, end: Int = 0): LinearLayout.LayoutParams =
        apply {
            topMargin = dp(top)
            bottomMargin = dp(bottom)
            marginStart = dp(start)
            marginEnd = dp(end)
        }

    /* Window. */

    @Suppress("DEPRECATION")
    private fun configureWindow() {
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = if (Build.VERSION.SDK_INT >= 26) palette.background else 0xff121212.toInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        val lightBackground = ColorPolicy.luminance(palette.background) >= 0.179
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val mask = WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            window.insetsController?.setSystemBarsAppearance(if (lightBackground) mask else 0, mask)
        } else {
            var visibility = window.decorView.systemUiVisibility
            visibility = if (lightBackground) visibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else visibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                visibility = if (lightBackground) visibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR else visibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
            window.decorView.systemUiVisibility = visibility
        }
    }

    @Suppress("DEPRECATION")
    private fun applySystemBarInsets(root: View) {
        root.setOnApplyWindowInsetsListener { view, insets ->
            val top: Int
            val bottom: Int
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val bars = insets.getInsets(WindowInsets.Type.systemBars() or WindowInsets.Type.displayCutout())
                top = bars.top
                bottom = bars.bottom
            } else {
                top = insets.systemWindowInsetTop
                bottom = insets.systemWindowInsetBottom
            }
            view.setPadding(0, top, 0, bottom)
            insets
        }
        root.requestApplyInsets()
    }

    companion object {
        const val SCREEN_MARGIN = 18
        const val CARD_RADIUS = 20f
        const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
        const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT
    }
}
