package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui

import android.app.Activity
import android.app.AlertDialog
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
 * screens are built from. The app deliberately stays on framework widgets only.
 */
internal abstract class ThemedActivity : Activity() {
    lateinit var palette: Palette
        private set

    val settingsStore: CompatSettingsStore by lazy { CompatSettingsStore(this) }

    private var appliedAppearance: Int? = null
    private var recreateRequested = false

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppConfiguration.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        palette = Palette.resolve(this)
        appliedAppearance = AppConfiguration.appearanceSignature(this)
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
        if (recreateRequested) return
        if (AppConfiguration.appearanceSignature(this, appliedAppearance) != appliedAppearance) {
            recreateRequested = true
            recreate()
        }
    }

    /* Screen scaffolding. */

    /** Root with system-bar insets applied, an optional [toolbar] and a scrolling [content]. */
    fun screen(toolbar: View?, content: LinearLayout.() -> Unit): View {
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
                            setPaddingRelative(dp(SCREEN_MARGIN), dp(4), dp(SCREEN_MARGIN), dp(28))
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
        setTypeface(typeface)
        this.gravity = gravity
        setLineSpacing(0f, 1.18f)
    }

    fun sectionTitle(title: CharSequence): TextView =
        text(title, 13.5f, palette.accent, Typeface.DEFAULT_BOLD).apply {
            setPaddingRelative(dp(4), dp(20), dp(4), dp(8))
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
            minimumHeight = dp(60)
            setPaddingRelative(dp(18), dp(12), dp(14), dp(12))
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
                    setImageDrawable(icon(R.drawable.ic_chevron_right, palette.secondaryText))
                    alpha = 0.7f
                },
                lp(dp(22), dp(22)).apply { marginStart = dp(8) },
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
        val switch = Switch(this).apply {
            isChecked = checked
            isClickable = false
            isFocusable = false
            tint(this)
        }
        return row(title, summary, iconRes, trailing = switch) {
            switch.isChecked = !switch.isChecked
            onToggle(switch.isChecked)
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
        val radio = RadioButton(this).apply {
            isChecked = checked
            isClickable = false
            isFocusable = false
            buttonTintList = controlTint()
        }
        val shell = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            minimumHeight = dp(48)
            setPaddingRelative(dp(18), dp(6), dp(24), dp(6))
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
        addView(text(title, 15.5f, palette.primaryText).apply { textAlignment = View.TEXT_ALIGNMENT_VIEW_START })
        if (!summary.isNullOrEmpty()) {
            addView(text(summary, 12.5f, palette.secondaryText).apply {
                textAlignment = View.TEXT_ALIGNMENT_VIEW_START
                setPaddingRelative(0, dp(2), 0, 0)
            })
        }
    }

    /** The summary of a [row] built with one, so that screens can refresh it in place. */
    fun rowSummary(row: LinearLayout): TextView? =
        (0 until row.childCount).map(row::getChildAt).filterIsInstance<LinearLayout>().firstOrNull()?.getChildAt(1) as? TextView

    private fun rowIcon(@DrawableRes iconRes: Int, color: Int): ImageView = ImageView(this).apply {
        setImageDrawable(icon(iconRes, color))
        layoutParams = lp(dp(22), dp(22)).apply { marginEnd = dp(16) }
    }

    /* Buttons. */

    fun primaryButton(label: CharSequence, onClick: () -> Unit): Button = Button(this).apply {
        text = label
        isAllCaps = false
        textSize = 15f
        setTextColor(palette.onAccent)
        backgroundTintList = ColorStateList.valueOf(palette.accent)
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
     * spacing match the rest of the app. Choosing an option dismisses the dialog; re-choosing the
     * current one only dismisses it.
     */
    fun singleChoiceDialog(
        title: CharSequence,
        choices: List<Choice>,
        checkedIndex: Int,
        onSelect: (Int) -> Unit,
    ) {
        var dialog: AlertDialog? = null
        val list = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPaddingRelative(0, dp(4), 0, dp(8))
            choices.forEachIndexed { index, choice ->
                val (row, _) = radioRow(choice.title, choice.summary, index == checkedIndex, choice.swatch?.let { colorSwatch(it) }) {
                    dialog?.dismiss()
                    if (index != checkedIndex) onSelect(index)
                }
                addView(row)
            }
        }
        dialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setView(ScrollView(this).apply { addView(list) })
            .setNegativeButton(android.R.string.cancel, null)
            .create()
        showDialog(dialog)
    }

    /** A filled circle for theme colors, used as a row trailing view. */
    fun colorSwatch(color: Int, sizeDp: Int = 22): View = View(this).apply {
        background = roundedBackground(color, sizeDp / 2f, palette.outline)
        layoutParams = lp(dp(sizeDp), dp(sizeDp))
    }

    fun showDialog(dialog: AlertDialog) {
        applyLayoutDirection(dialog.window)
        dialog.show()
        tintDialog(dialog)
    }

    fun tintDialog(dialog: AlertDialog) {
        listOf(AlertDialog.BUTTON_POSITIVE, AlertDialog.BUTTON_NEGATIVE, AlertDialog.BUTTON_NEUTRAL).forEach { which ->
            dialog.getButton(which)?.setTextColor(palette.accent)
        }
    }

    fun toast(message: CharSequence, long: Boolean = false) {
        Toast.makeText(this, message, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
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
        return getDrawable(value.resourceId)
    }

    fun tint(switch: Switch) {
        switch.thumbTintList = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_checked), intArrayOf()),
            intArrayOf(ColorPolicy.withAlpha(palette.secondaryText, 0x55), palette.accent, if (palette.isDark) palette.secondaryText else Color.WHITE),
        )
        switch.trackTintList = ColorStateList(
            arrayOf(intArrayOf(-android.R.attr.state_enabled), intArrayOf(android.R.attr.state_checked), intArrayOf()),
            intArrayOf(ColorPolicy.withAlpha(palette.secondaryText, 0x24), ColorPolicy.withAlpha(palette.accent, 0x66), ColorPolicy.withAlpha(palette.secondaryText, 0x66)),
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
        window.navigationBarColor = palette.background
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
