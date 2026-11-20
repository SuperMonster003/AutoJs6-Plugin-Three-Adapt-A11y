package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.annotation.StringRes
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui.ThemedActivity

/** Read-only pages reachable from the settings screen: the three notices (the release history has its own page). */
internal class DocumentActivity : ThemedActivity() {
    enum class Document(@StringRes val titleRes: Int, @StringRes val bodyRes: Int) {
        MECHANISM(R.string.mechanism_title, R.string.mechanism_body),
        PRIVACY(R.string.privacy_title, R.string.privacy_body),
        LIMITATIONS(R.string.limitations_title, R.string.limitations_body),
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val document = intent.getStringExtra(EXTRA_DOCUMENT)
            ?.let { name -> Document.entries.firstOrNull { it.name == name } }
            ?: Document.MECHANISM
        setContentView(
            screen(toolbar(getString(document.titleRes), showBack = true)) {
                addView(
                    card {
                        setPaddingRelative(dp(20), dp(18), dp(20), dp(20))
                        addNotice(document)
                    },
                    lp(MATCH_PARENT, WRAP_CONTENT).margins(top = 12),
                )
            },
        )
    }

    private fun LinearLayout.addNotice(document: Document) {
        val body = getString(document.bodyRes)
        addView(text(body, 15f, palette.primaryText).apply { setTextIsSelectable(true) })
    }

    companion object {
        private const val EXTRA_DOCUMENT = "document"

        fun intent(context: Context, document: Document): Intent =
            Intent(context, DocumentActivity::class.java).putExtra(EXTRA_DOCUMENT, document.name)
    }
}
