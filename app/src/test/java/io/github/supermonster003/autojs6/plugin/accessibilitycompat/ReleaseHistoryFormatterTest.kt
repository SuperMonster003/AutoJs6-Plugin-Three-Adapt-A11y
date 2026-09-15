package io.github.supermonster003.autojs6.plugin.accessibilitycompat

import io.github.supermonster003.autojs6.plugin.accessibilitycompat.ReleaseHistoryBlock.Bullet
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.ReleaseHistoryBlock.Category
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.ReleaseHistoryBlock.Paragraph
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.ReleaseHistoryBlock.Title
import io.github.supermonster003.autojs6.plugin.accessibilitycompat.ReleaseHistoryBlock.Version
import org.junit.Assert.assertEquals
import org.junit.Test

class ReleaseHistoryFormatterTest {

    @Test
    fun headingsBulletsAndParagraphsBecomeBlocks() {
        val markdown = """
            <!-- generated -->
            # Release history

            ## v1.3.0 (2026/09/15)

            ### Features
            - Settings screen with [Follow AutoJs6](https://example.test) options
            * `Shizuku` support
            ***
            > Older releases follow.
            Line one
            line two
        """.trimIndent()

        assertEquals(
            listOf(
                Title("Release history"),
                Version("v1.3.0 (2026/09/15)"),
                Category("Features"),
                Bullet("Settings screen with Follow AutoJs6 options"),
                Bullet("Shizuku support"),
                Paragraph("Older releases follow."),
                Paragraph("Line one line two"),
            ),
            ReleaseHistoryFormatter.parse(markdown),
        )
    }

    @Test
    fun inlineMarkupIsReducedToVisibleText() {
        assertEquals(
            "Follow AutoJs6 and code",
            ReleaseHistoryFormatter.inline("**Follow** [AutoJs6](https://example.test) and `code`"),
        )
        assertEquals("plain", ReleaseHistoryFormatter.inline("plain"))
        assertEquals(emptyList<ReleaseHistoryBlock>(), ReleaseHistoryFormatter.parse("\n\n---\n"))
    }
}
