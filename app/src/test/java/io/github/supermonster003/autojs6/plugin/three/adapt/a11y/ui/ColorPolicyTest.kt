package io.github.supermonster003.autojs6.plugin.three.adapt.a11y.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ColorPolicyTest {
    private val white = ColorPolicy.OPAQUE_WHITE
    private val black = ColorPolicy.OPAQUE_BLACK
    private val nightBackground = 0xFF10141B.toInt()
    private val dayBackground = 0xFFF6F8FC.toInt()

    @Test
    fun luminanceAndContrastFollowWcag() {
        assertEquals(1.0, ColorPolicy.luminance(white), 1e-9)
        assertEquals(0.0, ColorPolicy.luminance(black), 1e-9)
        assertEquals(21.0, ColorPolicy.contrastRatio(black, white), 1e-9)
        assertEquals(ColorPolicy.contrastRatio(white, black), ColorPolicy.contrastRatio(black, white), 1e-9)
    }

    @Test
    fun onColorPicksTheMoreReadableExtreme() {
        assertEquals(white, ColorPolicy.onColor(0xFF1F68AC.toInt()))
        assertEquals(black, ColorPolicy.onColor(0xFFFFDEAD.toInt()))
    }

    @Test
    fun readableKeepsSeedsThatAlreadyContrastAndFixesTheOthers() {
        val blue = 0xFF1F68AC.toInt()
        assertEquals(blue, ColorPolicy.readable(blue, dayBackground))
        assertEquals(blue, ColorPolicy.readable(0x001F68AC, dayBackground))

        val navajo = 0xFFFFDEAD.toInt()
        val onDay = ColorPolicy.readable(navajo, dayBackground)
        assertTrue(ColorPolicy.contrastRatio(onDay, dayBackground) >= ColorPolicy.MINIMUM_TEXT_CONTRAST)
        assertTrue("stays a warm tone: ${ColorPolicy.hex(onDay)}", (onDay shr 16 and 0xFF) >= (onDay and 0xFF))

        val onNight = ColorPolicy.readable(blue, nightBackground)
        assertTrue(ColorPolicy.contrastRatio(onNight, nightBackground) >= ColorPolicy.MINIMUM_TEXT_CONTRAST)
        assertTrue("lighter than the seed", ColorPolicy.luminance(onNight) > ColorPolicy.luminance(blue))
        assertEquals(navajo, ColorPolicy.readable(navajo, nightBackground))
    }

    @Test
    fun fillContrastRequirementIsLooser() {
        val blue = 0xFF1F68AC.toInt()
        val fill = ColorPolicy.readable(blue, nightBackground, ColorPolicy.MINIMUM_FILL_CONTRAST)
        assertTrue(ColorPolicy.contrastRatio(fill, nightBackground) >= ColorPolicy.MINIMUM_FILL_CONTRAST)
        assertTrue(ColorPolicy.luminance(fill) <= ColorPolicy.luminance(ColorPolicy.readable(blue, nightBackground)))
    }

    @Test
    fun blendAlphaAndHexAreExact() {
        assertEquals(0xFF808080.toInt(), ColorPolicy.blend(black, white, 128.0 / 255.0))
        assertEquals(black, ColorPolicy.blend(black, white, -1.0))
        assertEquals(white, ColorPolicy.blend(black, white, 2.0))
        assertEquals(0x80123456.toInt(), ColorPolicy.withAlpha(0xFF123456.toInt(), 0x80))
        assertEquals(0xFF123456.toInt(), ColorPolicy.withAlpha(0x123456, 999))
        assertEquals("#123456", ColorPolicy.hex(0xFF123456.toInt()))
        assertEquals("#000000", ColorPolicy.hex(black))
    }
}
