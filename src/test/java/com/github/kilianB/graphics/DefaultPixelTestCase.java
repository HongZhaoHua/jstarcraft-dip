package com.github.kilianB.graphics;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.github.kilianB.ArrayUtil;
import com.github.kilianB.graphics.ImageUtil.BImageType;

@Nested
class DefaultPixelTestCase {

	// No alpha image
	private static BufferedImage lena;

	// R(255)G(0)B(0) H(0)S(100)V(100) //Luminosity
	private static BufferedImage red;
	// R(0)G(255)B(0) H(120)S(100)V(100) //Luminosity
	private static BufferedImage green;
	// R(0)G(0)B(255) H(240)S(100)V(100) //Luminosity
	private static BufferedImage blue;
	// R(92)G(46)B(23) ~ H(20)S(75)V(36) //Luminosity
	private static BufferedImage brown;
	// R(92)G(46)B(23) ~ H(20)S(75)V(36) //Luminosity
	private static BufferedImage brownOpacity;

	private static BufferedImage cat;

	//
	private static BufferedImage bw;

	@BeforeAll
	static void loadImage() {
		try {
			lena = ImageIO.read(DefaultPixelTestCase.class.getClassLoader().getResourceAsStream("Lena.png"));
			lena = ImageUtil.toNewType(lena, BImageType.TYPE_USHORT_GRAY);

			bw = ImageIO.read(DefaultPixelTestCase.class.getClassLoader().getResourceAsStream("BlackWhite.png"));
			bw = ImageUtil.toNewType(bw, BImageType.TYPE_USHORT_GRAY);

			red = ImageIO.read(DefaultPixelTestCase.class.getClassLoader().getResourceAsStream("red.png"));
			red = ImageUtil.toNewType(red, BImageType.TYPE_4BYTE_ABGR_PRE);

			green = ImageIO.read(DefaultPixelTestCase.class.getClassLoader().getResourceAsStream("green.png"));
			green = ImageUtil.toNewType(green, BImageType.TYPE_4BYTE_ABGR_PRE);

			blue = ImageIO.read(DefaultPixelTestCase.class.getClassLoader().getResourceAsStream("blue.png"));
			blue = ImageUtil.toNewType(blue, BImageType.TYPE_4BYTE_ABGR_PRE);

			brown = ImageIO.read(DefaultPixelTestCase.class.getClassLoader().getResourceAsStream("brown.png"));
			brown = ImageUtil.toNewType(brown, BImageType.TYPE_4BYTE_ABGR_PRE);

			brownOpacity = ImageIO.read(DefaultPixelTestCase.class.getClassLoader().getResourceAsStream("brownOpacity.png"));
			brownOpacity = ImageUtil.toNewType(brownOpacity, BImageType.TYPE_INT_ARGB_PRE);

			// Type custom
			cat = ImageIO.read(DefaultPixelTestCase.class.getClassLoader().getResourceAsStream("catMono.png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void factoryCorrectClass() {
		assertEquals(DefaultPixel.class, Pixel.create(lena).getClass());
		assertEquals(DefaultPixel.class, Pixel.create(bw).getClass());
		assertEquals(DefaultPixel.class, Pixel.create(red).getClass());
		assertEquals(DefaultPixel.class, Pixel.create(green).getClass());
		assertEquals(DefaultPixel.class, Pixel.create(blue).getClass());
		assertEquals(DefaultPixel.class, Pixel.create(brown).getClass());
		assertEquals(DefaultPixel.class, Pixel.create(brownOpacity).getClass());
		assertEquals(DefaultPixel.class, Pixel.create(cat).getClass());
	}

	@Nested
	class TypeCustom {

		@Test
		void hasAlphaTrue() {
			assertTrue(Pixel.create(cat).hasTransparency());
		}

		@Test
		void getRGB() {
			Pixel fp = Pixel.create(cat);
			for (int x = 0; x < cat.getWidth(); x++) {
				for (int y = 0; y < cat.getHeight(); y++) {
					assertEquals(cat.getRGB(x, y), fp.getRgbScalar(x, y));
				}
			}
		}

		@Test
		void getRGBArray() {
			Pixel fp = Pixel.create(cat);
			int[][] rgb = fp.getRgbMatrix();
			for (int x = 0; x < cat.getWidth(); x++) {
				for (int y = 0; y < cat.getHeight(); y++) {
					assertEquals(fp.getRgbScalar(x, y), rgb[x][y]);
				}
			}
		}

	}

	@Test
	void hasAlphaFalse() {
		assertFalse(Pixel.create(lena).hasTransparency());
	}

	@Test
	void hasAlphaTrue() {
		assertTrue(Pixel.create(brownOpacity).hasTransparency());
	}

	@Test
	void getRGB() {
		Pixel fp = Pixel.create(lena);
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(lena.getRGB(x, y), fp.getRgbScalar(x, y));
			}
		}
	}

	@Test
	void getRGBArray() {
		Pixel fp = Pixel.create(lena);
		int[][] rgb = fp.getRgbMatrix();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getRgbScalar(x, y), rgb[x][y]);
			}
		}
	}

	@Test
	void red() {
		Pixel fp = Pixel.create(lena);
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(lena.getRGB(x, y));
				assertEquals(comp[1], fp.getRedScalar(x, y));
			}
		}
	}

	@Test
	void redArray() {
		Pixel fp = Pixel.create(lena);
		int[][] red = fp.getRedMatrix();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getRedScalar(x, y), red[x][y]);
			}
		}
	}

	@Test
	void setRed() {
		BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		Pixel fp = Pixel.create(bi);
		fp.setRedScalar(0, 0, 255);

		assertAll(() -> {
			assertEquals(255, fp.getRedScalar(0, 0));
		}, () -> {
			assertEquals(0, fp.getGreenScalar(0, 0));
		}, () -> {
			assertEquals(0, fp.getBlueScalar(0, 0));
		});
	}

	@Test
	void bulkSetRed() {
		int w = 10;
		int h = 10;
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Pixel fp = Pixel.create(bi);
		int[][] values = new int[w][h];
		double len = 255 / (double) w;
		double lenFa = len / h;
		for (int i = 0; i < w; i++) {
			int temp = i;
			ArrayUtil.fillArray(values[i], (index) -> {
				return (int) (len * temp + index * lenFa);
			});
		}
		fp.setRedMatrix(values);
		assertArrayEquals(values, fp.getRedMatrix());
	}

	@Test
	void green() {
		Pixel fp = Pixel.create(lena);
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(lena.getRGB(x, y));
				assertEquals(comp[2], fp.getGreenScalar(x, y));
			}
		}
	}

	@Test
	void greenArray() {
		Pixel fp = Pixel.create(lena);
		int[][] green = fp.getGreenMatrix();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getGreenScalar(x, y), green[x][y]);
			}
		}
	}

	@Test
	void setGreen() {
		BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		Pixel fp = Pixel.create(bi);
		fp.setGreenScalar(0, 0, 255);

		assertAll(() -> {
			assertEquals(0, fp.getRedScalar(0, 0));
		}, () -> {
			assertEquals(255, fp.getGreenScalar(0, 0));
		}, () -> {
			assertEquals(0, fp.getBlueScalar(0, 0));
		});
	}

	@Test
	void bulkSetGreen() {
		int w = 10;
		int h = 10;
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Pixel fp = Pixel.create(bi);
		int[][] values = new int[w][h];
		double len = 255 / (double) w;
		double lenFa = len / h;
		for (int i = 0; i < w; i++) {
			int temp = i;
			ArrayUtil.fillArray(values[i], (index) -> {
				return (int) (len * temp + index * lenFa);
			});
		}
		fp.setGreenMatrix(values);
		assertArrayEquals(values, fp.getGreenMatrix());
	}

	@Test
	void blue() {
		Pixel fp = Pixel.create(lena);
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(lena.getRGB(x, y));
				assertEquals(comp[3], fp.getBlueScalar(x, y));
			}
		}
	}

	@Test
	void blueArray() {
		Pixel fp = Pixel.create(lena);
		int[][] blue = fp.getBlueMatrix();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getBlueScalar(x, y), blue[x][y]);
			}
		}
	}

	@Test
	void setBlue() {
		BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		Pixel fp = Pixel.create(bi);
		fp.setBlueScalar(0, 0, 255);

		assertAll(() -> {
			assertEquals(0, fp.getRedScalar(0, 0));
		}, () -> {
			assertEquals(0, fp.getGreenScalar(0, 0));
		}, () -> {
			assertEquals(255, fp.getBlueScalar(0, 0));
		});
	}

	@Test
	void bulkSetBlue() {
		int w = 10;
		int h = 10;
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Pixel fp = Pixel.create(bi);
		int[][] values = new int[w][h];
		double len = 255 / (double) w;
		double lenFa = len / h;
		for (int i = 0; i < w; i++) {
			int temp = i;
			ArrayUtil.fillArray(values[i], (index) -> {
				return (int) (len * temp + index * lenFa);
			});
		}
		fp.setBlueMatrix(values);
		assertArrayEquals(values, fp.getBlueMatrix());
	}

	@Test
	void getRGBOpaque() {
		Pixel fp = Pixel.create(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				assertEquals(brownOpacity.getRGB(x, y), fp.getRgbScalar(x, y));
			}
		}
	}

	@Test
	void alphaOpaque() {
		Pixel fp = Pixel.create(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(brownOpacity.getRGB(x, y));
				assertEquals(comp[0], fp.getTransparencyScalar(x, y));
			}
		}
	}

	@Test
	void redOpaque() {
		Pixel fp = Pixel.create(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(brownOpacity.getRGB(x, y));
				assertEquals(comp[1], fp.getRedScalar(x, y));
			}
		}
	}

	@Test
	void greenOpaque() {
		Pixel fp = Pixel.create(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(brownOpacity.getRGB(x, y));
				assertEquals(comp[2], fp.getGreenScalar(x, y));
			}
		}
	}

	@Test
	void blueOpaque() {
		Pixel fp = Pixel.create(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(brownOpacity.getRGB(x, y));
				assertEquals(comp[3], fp.getBlueScalar(x, y));
			}
		}
	}

	@Test
	void bulkSetRGBA() {
		int w = 1000;
		int h = 1000;
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Pixel fp = Pixel.create(bi);
		int[][] values = new int[w][h];
		double len = 255 / (double) w;
		double lenFa = len / h;
		for (int i = 0; i < w; i++) {
			int temp = i;
			ArrayUtil.fillArray(values[i], (index) -> {
				return (int) (len * temp + index * lenFa);
			});
		}
		fp.setRedMatrix(values);
		fp.setBlueMatrix(values);
		fp.setGreenMatrix(values);
		fp.setTransparencyMatrix(values);

		assertAll(() -> {
			assertArrayEquals(values, fp.getRedMatrix());
		}, () -> {
			assertArrayEquals(values, fp.getGreenMatrix());
		}, () -> {
			assertArrayEquals(values, fp.getBlueMatrix());
		}, () -> {
			assertArrayEquals(values, fp.getTransparencyMatrix());
		});
	}

	@Test
	void rgbArray() {
		int arg[] = lena.getRGB(0, 0, lena.getWidth(), lena.getHeight(), null, 0, lena.getWidth());
		Pixel fp = Pixel.create(lena);
		int[][] argFp = fp.getRgbMatrix();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(arg[ArrayUtil.twoDimtoOneDim(y, x, lena.getWidth())], argFp[x][y]);
			}
		}
	}

	@Test
	void lum() {
		Pixel fp = Pixel.create(bw);

		for (int x = 0; x < bw.getWidth(); x++) {
			for (int y = 0; y < bw.getHeight(); y++) {

				if (y < 2) {
					// Black
					assertEquals(0, fp.getLumaScalar(x, y));
				} else {
					// White
					assertEquals(255, fp.getLumaScalar(x, y));
				}
			}
		}
	}

	@Test
	void lumArray() {
		Pixel fp = Pixel.create(lena);
		int[][] lumArr = fp.getLumaMatrix();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getLumaScalar(x, y), lumArr[x][y]);
			}
		}
	}

	@Test
	void lumInRange() {
		Pixel fp = Pixel.create(lena);

		for (int x = 0; x < bw.getWidth(); x++) {
			for (int y = 0; y < bw.getHeight(); y++) {

				int lum = fp.getLumaScalar(x, y);
				if (lum < 0 || lum > 255)
					fail("Luminosity ouside range");
			}
		}
	}

	@Test
	void hueBlackWhite() {
		Pixel fp = Pixel.create(bw);
		// Invalid hue value. Defined as 0
		for (int x = 0; x < bw.getWidth(); x++) {
			for (int y = 0; y < bw.getHeight(); y++) {
				assertEquals(0, fp.getHueScalar(x, y));
			}
		}
	}

	@Test
	void hueRed() {
		Pixel fp = Pixel.create(red);

		for (int x = 0; x < red.getWidth(); x++) {
			for (int y = 0; y < red.getHeight(); y++) {
				assertEquals(0, fp.getHueScalar(x, y));
			}
		}
	}

	@Test
	void hueGreen() {
		Pixel fp = Pixel.create(green);
		for (int x = 0; x < green.getWidth(); x++) {
			for (int y = 0; y < green.getHeight(); y++) {
				assertEquals(120, fp.getHueScalar(x, y));
			}
		}
	}

	@Test
	void hueBlue() {
		Pixel fp = Pixel.create(blue);
		for (int x = 0; x < blue.getWidth(); x++) {
			for (int y = 0; y < blue.getHeight(); y++) {
				assertEquals(240, fp.getHueScalar(x, y));
			}
		}
	}

	@Test
	void hueBrown() {
		Pixel fp = Pixel.create(brown);
		for (int x = 0; x < brown.getWidth(); x++) {
			for (int y = 0; y < brown.getHeight(); y++) {
				assertEquals(20, fp.getHueScalar(x, y));
			}
		}
	}

	@Test
	void satBlackWhite() {
		Pixel fp = Pixel.create(bw);
		// Invalid hue value. Defined as 0
		for (int x = 0; x < bw.getWidth(); x++) {
			for (int y = 0; y < bw.getHeight(); y++) {
				assertEquals(0, fp.getSaturationScalar(x, y));
			}
		}
	}

	@Test
	void satRed() {
		Pixel fp = Pixel.create(red);

		for (int x = 0; x < red.getWidth(); x++) {
			for (int y = 0; y < red.getHeight(); y++) {
				assertEquals(1, fp.getSaturationScalar(x, y));
			}
		}
	}

	@Test
	void satGreen() {
		Pixel fp = Pixel.create(green);
		for (int x = 0; x < green.getWidth(); x++) {
			for (int y = 0; y < green.getHeight(); y++) {
				assertEquals(1, fp.getSaturationScalar(x, y));
			}
		}
	}

	@Test
	void satBlue() {
		Pixel fp = Pixel.create(blue);
		for (int x = 0; x < blue.getWidth(); x++) {
			for (int y = 0; y < blue.getHeight(); y++) {
				assertEquals(1, fp.getSaturationScalar(x, y));
			}
		}
	}

	@Test
	void satBrown() {
		Pixel fp = Pixel.create(brown);
		for (int x = 0; x < brown.getWidth(); x++) {
			for (int y = 0; y < brown.getHeight(); y++) {
				assertEquals(0.75, fp.getSaturationScalar(x, y));
			}
		}
	}

	@Test
	void valBlackWhite() {
		Pixel fp = Pixel.create(bw);
		for (int x = 0; x < bw.getWidth(); x++) {
			for (int y = 0; y < bw.getHeight(); y++) {
				if (y < 2) {
					// Black
					assertEquals(0, fp.getBrightnessScalar(x, y));
				} else {
					// White
					assertEquals(255, fp.getBrightnessScalar(x, y));
				}

			}
		}
	}

	@Test
	void valRed() {
		Pixel fp = Pixel.create(red);

		for (int x = 0; x < red.getWidth(); x++) {
			for (int y = 0; y < red.getHeight(); y++) {
				assertEquals(255, fp.getBrightnessScalar(x, y));
			}
		}
	}

	@Test
	void valGreen() {
		Pixel fp = Pixel.create(green);
		for (int x = 0; x < green.getWidth(); x++) {
			for (int y = 0; y < green.getHeight(); y++) {
				assertEquals(255, fp.getBrightnessScalar(x, y));
			}
		}
	}

	@Test
	void valBlue() {
		Pixel fp = Pixel.create(blue);
		for (int x = 0; x < blue.getWidth(); x++) {
			for (int y = 0; y < blue.getHeight(); y++) {
				assertEquals(255, fp.getBrightnessScalar(x, y));
			}
		}
	}

	@Test
	void valBrown() {
		Pixel fp = Pixel.create(brown);
		for (int x = 0; x < brown.getWidth(); x++) {
			for (int y = 0; y < brown.getHeight(); y++) {
				assertEquals(92, fp.getBrightnessScalar(x, y));
			}
		}
	}

}