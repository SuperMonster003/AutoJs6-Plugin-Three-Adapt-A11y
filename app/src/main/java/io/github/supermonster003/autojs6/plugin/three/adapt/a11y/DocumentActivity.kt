package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.widget.LinearLayout
import androidx.annotation.StringRes
import io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui.ThemedActivity

/** Read-only pages reachable from the settings screen: the three notices and the release history. */
internal class DocumentActivity : ThemedActivity() {
    enum class Document(@StringRes val titleRes: Int, @StringRes val bodyRes: Int?) {
        MECHANISM(R.string.mechanism_title, R.string.mechanism_body),
        PRIVACY(R.string.privacy_title, R.string.privacy_body),
        LIMITATIONS(R.string.limitations_title, R.string.limitations_body),
        RELEASE_HISTORY(R.string.release_history, null),
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
                        if (document == Document.RELEASE_HISTORY) addReleaseHistory() else addNotice(document)
                    },
                    lp(MATCH_PARENT, WRAP_CONTENT).margins(top = 12),
                )
            },
        )
    }

    private fun LinearLayout.addNotice(document: Document) {
        val body = getString(requireNotNull(document.bodyRes))
        addView(text(body, 15f, palette.primaryText).apply { setTextIsSelectable(true) })
    }

    private fun LinearLayout.addReleaseHistory() {
        val locale = resources.configuration.locales[0]
        val markdown = loadReleaseHistory(releaseHistoryCandidates(locale.language, locale.country, locale.script)) { path ->
            assets.open(path).bufferedReader(Charsets.UTF_8).use { it.readText() }
        }
        if (markdown == null) {
            addView(text(getString(R.string.release_history_unavailable), 15f, palette.secondaryText))
            return
        }
        ReleaseHistoryFormatter.parse(markdown).forEach { block ->
            val view = when (block) {
                is ReleaseHistoryBlock.Title -> text(block.text, 20f, palette.primaryText, Typeface.DEFAULT_BOLD)
                is ReleaseHistoryBlock.Version -> text(block.text, 17f, palette.accent, Typeface.DEFAULT_BOLD).apply {
                    setPaddingRelative(0, dp(18), 0, dp(2))
                }
                is ReleaseHistoryBlock.Category -> text(block.text, 14.5f, palette.primaryText, Typeface.DEFAULT_BOLD).apply {
                    setPaddingRelative(0, dp(10), 0, dp(2))
                }
                is ReleaseHistoryBlock.Bullet -> LinearLayout(this@DocumentActivity).apply {
                    orientation = LinearLayout.HORIZONTAL
                    addView(text("•", 14.5f, palette.secondaryText), lp(dp(16), WRAP_CONTENT))
                    addView(text(block.text, 14.5f, palette.primaryText).apply { setTextIsSelectable(true) }, lp(0, WRAP_CONTENT, weight = 1f))
                    setPaddingRelative(0, dp(3), 0, dp(3))
                }
                is ReleaseHistoryBlock.Paragraph -> text(block.text, 14f, palette.secondaryText).apply {
                    setPaddingRelative(0, dp(4), 0, dp(4))
                }
            }
            addView(view, lp(MATCH_PARENT, WRAP_CONTENT))
        }
    }

    companion object {
        private const val EXTRA_DOCUMENT = "document"

        fun intent(context: Context, document: Document): Intent =
            Intent(context, DocumentActivity::class.java).putExtra(EXTRA_DOCUMENT, document.name)
    }
}
