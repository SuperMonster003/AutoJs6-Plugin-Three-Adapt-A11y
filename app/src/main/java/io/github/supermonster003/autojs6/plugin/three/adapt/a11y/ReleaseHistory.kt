package io.github.supermonster003.autojs6.plugin.three.adapt.a11y

import java.io.IOException

internal fun releaseHistoryCandidates(language: String, country: String, script: String): List<String> {
    val code = when {
        language == "zh" && country == "HK" -> "zh-Hant-HK"
        language == "zh" && (country == "TW" || script == "Hant") -> "zh-Hant-TW"
        language == "zh" -> "zh-Hans"
        language in setOf("en", "ar", "es", "fr", "ja", "ko", "ru") -> language
        else -> "en"
    }
    return listOf("doc/CHANGELOG-$code.md", "doc/CHANGELOG-en.md").distinct()
}

internal fun loadReleaseHistory(candidates: List<String>, loader: (String) -> String): String? {
    for (path in candidates) {
        try { loader(path).takeIf { it.isNotBlank() }?.let { return it } } catch (_: IOException) { }
    }
    return null
}

/** One display block of the bundled changelog Markdown; kept Android-free for unit tests. */
internal sealed class ReleaseHistoryBlock {
    data class Title(val text: String) : ReleaseHistoryBlock()
    data class Version(val text: String) : ReleaseHistoryBlock()
    data class Category(val text: String) : ReleaseHistoryBlock()
    data class Bullet(val text: String) : ReleaseHistoryBlock()
    data class Paragraph(val text: String) : ReleaseHistoryBlock()
}

/**
 * Minimal reader for the generated changelog files: headings, bullets and paragraphs. Inline code
 * markers and Markdown links are reduced to their visible text; comments and rules are dropped.
 */
internal object ReleaseHistoryFormatter {
    private val link = Regex("""\[([^\]]+)]\(([^)]+)\)""")
    private val htmlComment = Regex("""<!--.*?-->""", RegexOption.DOT_MATCHES_ALL)

    fun parse(markdown: String): List<ReleaseHistoryBlock> {
        val blocks = mutableListOf<ReleaseHistoryBlock>()
        val paragraph = StringBuilder()
        fun flushParagraph() {
            if (paragraph.isNotEmpty()) {
                blocks += ReleaseHistoryBlock.Paragraph(paragraph.toString())
                paragraph.setLength(0)
            }
        }
        htmlComment.replace(markdown, "").lines().forEach { rawLine ->
            val line = rawLine.trimEnd()
            val trimmed = line.trim()
            when {
                trimmed.isEmpty() || trimmed.all { it == '-' || it == '*' || it == '_' } && trimmed.length >= 3 -> flushParagraph()
                trimmed.startsWith("#") -> {
                    flushParagraph()
                    val level = trimmed.takeWhile { it == '#' }.length
                    val text = inline(trimmed.drop(level).trim())
                    blocks += when {
                        level <= 1 -> ReleaseHistoryBlock.Title(text)
                        level == 2 -> ReleaseHistoryBlock.Version(text)
                        else -> ReleaseHistoryBlock.Category(text)
                    }
                }
                trimmed.startsWith("- ") || trimmed.startsWith("* ") -> {
                    flushParagraph()
                    blocks += ReleaseHistoryBlock.Bullet(inline(trimmed.drop(2).trim()))
                }
                trimmed.startsWith(">") -> {
                    flushParagraph()
                    blocks += ReleaseHistoryBlock.Paragraph(inline(trimmed.drop(1).trim()))
                }
                else -> {
                    if (paragraph.isNotEmpty()) paragraph.append(' ')
                    paragraph.append(inline(trimmed))
                }
            }
        }
        flushParagraph()
        return blocks
    }

    fun inline(text: String): String = link.replace(text) { it.groupValues[1] }
        .replace("`", "")
        .replace("**", "")
}
