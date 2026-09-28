package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/** Pure color arithmetic (WCAG contrast) used to derive a readable palette from any theme seed. */
internal object ColorPolicy {
    const val OPAQUE_BLACK = -0x1000000
    const val OPAQUE_WHITE = -0x1
    const val MINIMUM_TEXT_CONTRAST = 4.5
    const val MINIMUM_FILL_CONTRAST = 3.0

    fun luminance(color: Int): Double {
        fun channel(shift: Int): Double {
            val value = (color shr shift and 0xFF) / 255.0
            return if (value <= 0.04045) value / 12.92 else ((value + 0.055) / 1.055).pow(2.4)
        }
        return 0.2126 * channel(16) + 0.7152 * channel(8) + 0.0722 * channel(0)
    }

    fun contrastRatio(first: Int, second: Int): Double {
        val lighter = max(luminance(first), luminance(second))
        val darker = min(luminance(first), luminance(second))
        return (lighter + 0.05) / (darker + 0.05)
    }

    /** Black or white, whichever reads better on [background]. */
    fun onColor(background: Int): Int =
        if (contrastRatio(OPAQUE_BLACK, background) >= contrastRatio(OPAQUE_WHITE, background)) OPAQUE_BLACK else OPAQUE_WHITE

    /**
     * Keeps the hue of [seed] while moving it toward black or white only as far as needed to reach
     * [minimumContrast] against [background]; the direction changing the luminance least wins.
     */
    fun readable(seed: Int, background: Int, minimumContrast: Double = MINIMUM_TEXT_CONTRAST): Int {
        val opaque = seed or -0x1000000
        if (contrastRatio(opaque, background) >= minimumContrast) return opaque

        fun adjustedToward(target: Int): Int? {
            if (contrastRatio(target, background) < minimumContrast) return null
            var low = 0.0
            var high = 1.0
            repeat(18) {
                val middle = (low + high) / 2.0
                if (contrastRatio(blend(opaque, target, middle), background) >= minimumContrast) high = middle else low = middle
            }
            return blend(opaque, target, high)
        }

        val sourceLuminance = luminance(opaque)
        return listOfNotNull(adjustedToward(OPAQUE_BLACK), adjustedToward(OPAQUE_WHITE))
            .minByOrNull { abs(luminance(it) - sourceLuminance) }
            ?: onColor(background)
    }

    fun withAlpha(color: Int, alpha: Int): Int = color and 0xFFFFFF or (alpha.coerceIn(0, 255) shl 24)

    fun blend(first: Int, second: Int, ratio: Double): Int {
        val clamped = ratio.coerceIn(0.0, 1.0)
        fun channel(shift: Int): Int {
            val start = first shr shift and 0xFF
            val end = second shr shift and 0xFF
            return (start + (end - start) * clamped).toInt().coerceIn(0, 255)
        }
        return -0x1000000 or (channel(16) shl 16) or (channel(8) shl 8) or channel(0)
    }

    fun hex(color: Int): String = "#%06X".format(color and 0xFFFFFF)
}
