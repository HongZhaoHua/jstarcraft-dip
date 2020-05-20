package com.jstarcraft.dip.color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javafx.scene.paint.Color;

class ColorUtilTest {

	@Nested
	class AwtToFX {
		@Test
		void black() {
			assertEquals(Color.BLACK, ColorUtility.awtToFxColor(java.awt.Color.BLACK));
		}

		@Test
		void white() {
			assertEquals(Color.WHITE, ColorUtility.awtToFxColor(java.awt.Color.WHITE));
		}

		@Test
		void gray() {
			assertEquals(Color.GRAY, ColorUtility.awtToFxColor(java.awt.Color.GRAY));
		}

		@Test
		void test() {
			Color fxColor = Color.web("#961e1e");

			java.awt.Color awtColor = java.awt.Color.decode("#961e1e");

			Color genfxColor = ColorUtility.awtToFxColor(awtColor);
			assertEquals(fxColor, genfxColor);
		}

		@Test
		@Disabled
		void alpha() {
			// Color.web(colorString, opacity)
			// java.awt.Color.decode(nm) what is the awt equivalent?
			// DON'T compare float and doubles!
			assertEquals(new Color(0.5d, 0.5d, 0.5d, 0.3d), ColorUtility.awtToFxColor(new java.awt.Color(0.5f, 0.5f, 0.5f, 0.3f)));
		}

	}

	@Nested
	class FXToAwt {
		@Test
		void black() {
			assertEquals(java.awt.Color.BLACK, ColorUtility.fxToAwtColor(Color.BLACK));
		}

		@Test
		void white() {
			assertEquals(java.awt.Color.WHITE, ColorUtility.fxToAwtColor(Color.WHITE));
		}

		@Test
		void gray() {
			assertEquals(java.awt.Color.GRAY, ColorUtility.fxToAwtColor(Color.GRAY));
		}

		@Test
		void test() {
			Color fxColor = Color.web("#961e1e");

			java.awt.Color awtColor = java.awt.Color.decode("#961e1e");

			java.awt.Color genAwtColor = ColorUtility.fxToAwtColor(fxColor);
			assertEquals(awtColor, genAwtColor);
		}

		@Test
		@Disabled
		void alpha() {
			// Color.web(colorString, opacity)
			// java.awt.Color.decode(nm) what is the awt equivalent?

			// DON'T compare float and doubles!
			assertEquals(new Color(0.5d, 0.5d, 0.5d, 0.3d), ColorUtility.awtToFxColor(new java.awt.Color(0.5f, 0.5f, 0.5f, 0.3f)));
		}
	}

	@Nested
	class ColorDistance {

		@Test
		void symmetry() {
			Color c1 = Color.GREENYELLOW;
			Color c2 = Color.DARKORANGE;
			assertEquals(ColorUtility.distance(c1, c2), ColorUtility.distance(c2, c1));
		}

		@Test
		void identity() {
			Color c1 = Color.DARKORANGE;
			Color c2 = Color.DARKORANGE;
			assertEquals(0, ColorUtility.distance(c2, c1));
		}

		@Test
		void distance() {
			Color c1 = Color.WHITE;
			Color c2 = Color.DARKORANGE;
			Color c3 = Color.ORANGE;
			assertTrue(ColorUtility.distance(c1, c2) > ColorUtility.distance(c2, c3));
		}
	}

	@Nested
	class ContrastColor {
		@Test
		void white() {
			assertEquals(Color.BLACK, ColorUtility.getContrastColor(Color.WHITE));
		}

		@Test
		void black() {
			assertEquals(Color.WHITE, ColorUtility.getContrastColor(Color.BLACK));
		}

		@Test
		void yellow() {
			assertEquals(Color.BLACK, ColorUtility.getContrastColor(Color.YELLOW));
		}

		@Test
		void blue() {
			assertEquals(Color.WHITE, ColorUtility.getContrastColor(Color.BLUE));
		}
	}
}