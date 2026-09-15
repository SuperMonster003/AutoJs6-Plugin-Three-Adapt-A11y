package io.github.supermonster003.autojs6.plugin.accessibilitycompat

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.ui.ThemedActivity

/**
 * About screen reached from the settings: the application identity with its version, package and
 * license, then the developer, project page and feedback links. Values are copyable, links open in
 * the browser.
 */
internal class AboutActivity : ThemedActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(screen(toolbar(getString(R.string.settings_about), showBack = true)) { buildContent() })
    }

    private fun LinearLayout.buildContent() {
        addView(header(), lp(MATCH_PARENT, WRAP_CONTENT))

        addView(sectionTitle(getString(R.string.about_section_app)))
        addView(card {
            val version = versionText()
            addView(copyRow(R.string.about_version, version, R.drawable.ic_info))
            addView(divider(56))
            addView(copyRow(R.string.about_package_name, packageName, R.drawable.ic_apps))
            addView(divider(56))
            addView(linkRow(R.string.about_license, getString(R.string.about_license_summary), R.drawable.ic_description, LICENSE_URL))
        })

        addView(sectionTitle(getString(R.string.about_section_developer)))
        addView(card {
            addView(linkRow(R.string.plugin_author, getString(R.string.about_developer_summary), R.drawable.ic_person, DEVELOPER_URL))
            addView(divider(56))
            addView(linkRow(R.string.about_project_page, AccessibilityCompatContract.PROJECT_URL, R.drawable.ic_code, AccessibilityCompatContract.PROJECT_URL))
            addView(divider(56))
            addView(linkRow(R.string.about_feedback, getString(R.string.about_feedback_summary), R.drawable.ic_bug_report, ISSUES_URL))
        })
    }

    private fun header(): View = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        setPaddingRelative(dp(12), dp(16), dp(12), dp(4))
        addView(
            ImageView(context).apply {
                setImageResource(R.mipmap.ic_launcher)
                contentDescription = getString(R.string.app_name)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
            },
            lp(dp(84), dp(84)).margins(bottom = 14),
        )
        addView(text(getString(R.string.app_name), 23f, palette.primaryText, Typeface.DEFAULT_BOLD, Gravity.CENTER))
        addView(
            text(getString(R.string.plugin_description), 14.5f, palette.secondaryText, gravity = Gravity.CENTER),
            lp(MATCH_PARENT, WRAP_CONTENT).margins(top = 8),
        )
    }

    /** A row whose value is copied on tap; the trailing icon says so instead of the in-app chevron. */
    private fun copyRow(@StringRes titleRes: Int, value: String, @DrawableRes iconRes: Int): LinearLayout =
        row(getString(titleRes), value, iconRes, trailingIcon(R.drawable.ic_content_copy)) { copy(getString(titleRes), value) }

    /** A row that leaves the app; the trailing icon says so instead of the in-app chevron. */
    private fun linkRow(@StringRes titleRes: Int, summary: CharSequence, @DrawableRes iconRes: Int, url: String): LinearLayout =
        row(getString(titleRes), summary, iconRes, trailingIcon(R.drawable.ic_open_in_new)) { openUrl(url) }

    private fun trailingIcon(@DrawableRes iconRes: Int): ImageView = ImageView(this).apply {
        setImageDrawable(icon(iconRes, palette.secondaryText))
        alpha = 0.7f
        layoutParams = lp(dp(20), dp(20))
    }

    private fun openUrl(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (_: ActivityNotFoundException) {
            toast(getString(R.string.link_unavailable))
        }
    }

    private fun copy(label: CharSequence, value: CharSequence) {
        val clipboard = getSystemService(ClipboardManager::class.java) ?: return
        clipboard.setPrimaryClip(ClipData.newPlainText(label, value))
        toast(getString(R.string.copied_to_clipboard))
    }

    private fun versionText(): String {
        val info = packageInfo() ?: return ""
        val code = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            info.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            info.versionCode.toLong()
        }
        return getString(R.string.about_version_value, info.versionName.orEmpty(), code)
    }

    private fun packageInfo(): PackageInfo? = runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, 0)
        }
    }.getOrNull()

    private companion object {
        const val DEVELOPER_URL = "https://github.com/SuperMonster003"
        const val ISSUES_URL = "${AccessibilityCompatContract.PROJECT_URL}/issues"
        const val LICENSE_URL = "${AccessibilityCompatContract.PROJECT_URL}/blob/master/LICENSE"
    }
}
