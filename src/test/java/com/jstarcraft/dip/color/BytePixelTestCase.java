package com.jstarcraft.dip.color;

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

@Nested
class BytePixelTestCase {

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

	//
	static BufferedImage bw;

	@BeforeAll
	static void loadImage() {
		try {
			lena = ImageIO.read(BytePixelTestCase.class.getClassLoader().getResourceAsStream("Lena.png"));
			bw = ImageIO.read(BytePixelTestCase.class.getClassLoader().getResourceAsStream("BlackWhite.png"));
			red = ImageIO.read(BytePixelTestCase.class.getClassLoader().getResourceAsStream("red.png"));
			green = ImageIO.read(BytePixelTestCase.class.getClassLoader().getResourceAsStream("green.png"));
			blue = ImageIO.read(BytePixelTestCase.class.getClassLoader().getResourceAsStream("blue.png"));
			brown = ImageIO.read(BytePixelTestCase.class.getClassLoader().getResourceAsStream("brown.png"));
			brownOpacity = ImageIO.read(BytePixelTestCase.class.getClassLoader().getResourceAsStream("brownOpacity.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void factoryCorrectClass() {
		assertEquals(BytePixel.class, ColorPixel.convert(lena).getClass());
		assertEquals(BytePixel.class, ColorPixel.convert(bw).getClass());
		assertEquals(BytePixel.class, ColorPixel.convert(red).getClass());
		assertEquals(BytePixel.class, ColorPixel.convert(green).getClass());
		assertEquals(BytePixel.class, ColorPixel.convert(blue).getClass());
		assertEquals(BytePixel.class, ColorPixel.convert(brown).getClass());
		assertEquals(BytePixel.class, ColorPixel.convert(brownOpacity).getClass());
	}

	@Test
	void hasAlphaFalse() {
		assertFalse(ColorPixel.convert(brown).hasTransparency());
	}

	@Test
	void hasAlphaTrue() {
		assertTrue(ColorPixel.convert(brownOpacity).hasTransparency());
	}

	@Test
	void getRGB() {
		ColorPixel fp = ColorPixel.convert(lena);
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(lena.getRGB(x, y), fp.getRgbScalar(x, y));
			}
		}
	}

	@Test
	void getRGBArray() {
		ColorPixel fp = ColorPixel.convert(lena);
		int[][] rgb = fp.getRgbMatrix();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getRgbScalar(x, y), rgb[x][y]);
			}
		}
	}

	@Test
	void red() {
		ColorPixel fp = ColorPixel.convert(lena);
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				int[] comp = ColorUtility.argbToComponents(lena.getRGB(x, y));
				assertEquals(comp[1], fp.getRedScalar(x, y));
			}
		}
	}

	@Test
	void redArray() {
		ColorPixel fp = ColorPixel.convert(lena);
		int[][] red = fp.getRedMatrix();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getRedScalar(x, y), red[x][y]);
			}
		}
	}

	@Test
	void setRed() {
		BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		ColorPixel fp = ColorPixel.convert(bi);
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
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
		ColorPixel fp = ColorPixel.convert(bi);
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
		ColorPixel fp = ColorPixel.convert(lena);
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				int[] comp = ColorUtility.argbToComponents(lena.getRGB(x, y));
				assertEquals(comp[2], fp.getGreenScalar(x, y));
			}
		}
	}

	@Test
	void greenArray() {
		ColorPixel fp = ColorPixel.convert(lena);
		int[][] green = fp.getGreenMatrix();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getGreenScalar(x, y), green[x][y]);
			}
		}
	}

	@Test
	void setGreen() {
		BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		ColorPixel fp = ColorPixel.convert(bi);
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
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
		ColorPixel fp = ColorPixel.convert(bi);
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
		ColorPixel fp = ColorPixel.convert(lena);
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				int[] comp = ColorUtility.argbToComponents(lena.getRGB(x, y));
				assertEquals(comp[3], fp.getBlueScalar(x, y));
			}
		}
	}

	@Test
	void blueArray() {
		ColorPixel fp = ColorPixel.convert(lena);
		int[][] blue = fp.getBlueMatrix();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getBlueScalar(x, y), blue[x][y]);
			}
		}
	}

	@Test
	void blueArray1D() {
		ColorPixel fp = ColorPixel.convert(lena);
		int[] blue = fp.getBlueVector();
		int[][] blue2D = fp.getBlueMatrix();
		int i = 0;

		for (int y = 0; y < lena.getHeight(); y++) {
			for (int x = 0; x < lena.getWidth(); x++) {
				assertEquals(blue[i++], blue2D[x][y]);
			}
		}
	}

	@Test
	void setBlue() {
		BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		ColorPixel fp = ColorPixel.convert(bi);
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
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
		ColorPixel fp = ColorPixel.convert(bi);
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
		ColorPixel fp = ColorPixel.convert(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				assertEquals(brownOpacity.getRGB(x, y), fp.getRgbScalar(x, y));
			}
		}
	}

	@Test
	void alphaOpaque() {
		ColorPixel fp = ColorPixel.convert(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				int[] comp = ColorUtility.argbToComponents(brownOpacity.getRGB(x, y));
				assertEquals(comp[0], fp.getTransparencyScalar(x, y));
			}
		}
	}

	@Test
	void redOpaque() {
		ColorPixel fp = ColorPixel.convert(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				int[] comp = ColorUtility.argbToComponents(brownOpacity.getRGB(x, y));
				assertEquals(comp[1], fp.getRedScalar(x, y));
			}
		}
	}

	@Test
	void greenOpaque() {
		ColorPixel fp = ColorPixel.convert(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				int[] comp = ColorUtility.argbToComponents(brownOpacity.getRGB(x, y));
				assertEquals(comp[2], fp.getGreenScalar(x, y));
			}
		}
	}

	@Test
	void blueOpaque() {
		ColorPixel fp = ColorPixel.convert(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				int[] comp = ColorUtility.argbToComponents(brownOpacity.getRGB(x, y));
				assertEquals(comp[3], fp.getBlueScalar(x, y));
			}
		}
	}

	@Test
	void bulkSetRGBA() {
		int w = 1000;
		int h = 1000;
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		ColorPixel fp = ColorPixel.convert(bi);
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
		ColorPixel fp = ColorPixel.convert(lena);
		int[][] argFp = fp.getRgbMatrix();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(arg[ArrayUtil.twoDimtoOneDim(y, x, lena.getWidth())], argFp[x][y]);
			}
		}
	}

	@Test
	void lum() {
		ColorPixel fp = ColorPixel.convert(bw);

		for (int x = 0; x < bw.getWidth(); x++) {
			for (int y = 0; y < bw.getHeight(); y++) {

				if (y < 2) {
					// Black
					assertEquals(0, fp.getLuminanceScalar(x, y));
				} else {
					// White
					assertEquals(255, fp.getLuminanceScalar(x, y));
				}
			}
		}
	}

	@Test
	void lumArray() {
		ColorPixel fp = ColorPixel.convert(lena);
		int[][] lumArr = fp.getLuminanceMatrix();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getLuminanceScalar(x, y), lumArr[x][y]);
			}
		}
	}

	@Test
	void lumInRange() {
		ColorPixel fp = ColorPixel.convert(lena);

		for (int x = 0; x < bw.getWidth(); x++) {
			for (int y = 0; y < bw.getHeight(); y++) {

				int lum = fp.getLuminanceScalar(x, y);
				if (lum < 0 || lum > 255)
					fail("Luminosity ouside range");
			}
		}
	}

	@Test
	void hueBlackWhite() {
		ColorPixel fp = ColorPixel.convert(bw);
		// Invalid hue value. Defined as 0
		for (int x = 0; x < bw.getWidth(); x++) {
			for (int y = 0; y < bw.getHeight(); y++) {
				assertEquals(0, fp.getHueScalar(x, y));
			}
		}
	}

	@Test
	void hueRed() {
		ColorPixel fp = ColorPixel.convert(red);

		for (int x = 0; x < red.getWidth(); x++) {
			for (int y = 0; y < red.getHeight(); y++) {
				assertEquals(0, fp.getHueScalar(x, y));
			}
		}
	}

	@Test
	void hueGreen() {
		ColorPixel fp = ColorPixel.convert(green);
		for (int x = 0; x < green.getWidth(); x++) {
			for (int y = 0; y < green.getHeight(); y++) {
				assertEquals(120, fp.getHueScalar(x, y));
			}
		}
	}

	@Test
	void hueBlue() {
		ColorPixel fp = ColorPixel.convert(blue);
		for (int x = 0; x < blue.getWidth(); x++) {
			for (int y = 0; y < blue.getHeight(); y++) {
				assertEquals(240, fp.getHueScalar(x, y));
			}
		}
	}

	@Test
	void hueBrown() {
		ColorPixel fp = ColorPixel.convert(brown);
		for (int x = 0; x < brown.getWidth(); x++) {
			for (int y = 0; y < brown.getHeight(); y++) {
				assertEquals(20, fp.getHueScalar(x, y));
			}
		}
	}

	@Test
	void satBlackWhite() {
		ColorPixel fp = ColorPixel.convert(bw);
		// Invalid hue value. Defined as 0
		for (int x = 0; x < bw.getWidth(); x++) {
			for (int y = 0; y < bw.getHeight(); y++) {
				assertEquals(0, fp.getSaturationScalar(x, y));
			}
		}
	}

	@Test
	void satRed() {
		ColorPixel fp = ColorPixel.convert(red);

		for (int x = 0; x < red.getWidth(); x++) {
			for (int y = 0; y < red.getHeight(); y++) {
				assertEquals(1, fp.getSaturationScalar(x, y));
			}
		}
	}

	@Test
	void satGreen() {
		ColorPixel fp = ColorPixel.convert(green);
		for (int x = 0; x < green.getWidth(); x++) {
			for (int y = 0; y < green.getHeight(); y++) {
				assertEquals(1, fp.getSaturationScalar(x, y));
			}
		}
	}

	@Test
	void satBlue() {
		ColorPixel fp = ColorPixel.convert(blue);
		for (int x = 0; x < blue.getWidth(); x++) {
			for (int y = 0; y < blue.getHeight(); y++) {
				assertEquals(1, fp.getSaturationScalar(x, y));
			}
		}
	}

	@Test
	void satBrown() {
		ColorPixel fp = ColorPixel.convert(brown);
		for (int x = 0; x < brown.getWidth(); x++) {
			for (int y = 0; y < brown.getHeight(); y++) {
				assertEquals(0.75, fp.getSaturationScalar(x, y));
			}
		}
	}

	@Test
	void valBlackWhite() {
		ColorPixel fp = ColorPixel.convert(bw);
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
		ColorPixel fp = ColorPixel.convert(red);

		for (int x = 0; x < red.getWidth(); x++) {
			for (int y = 0; y < red.getHeight(); y++) {
				assertEquals(255, fp.getBrightnessScalar(x, y));
			}
		}
	}

	@Test
	void valGreen() {
		ColorPixel fp = ColorPixel.convert(green);
		for (int x = 0; x < green.getWidth(); x++) {
			for (int y = 0; y < green.getHeight(); y++) {
				assertEquals(255, fp.getBrightnessScalar(x, y));
			}
		}
	}

	@Test
	void valBlue() {
		ColorPixel fp = ColorPixel.convert(blue);
		for (int x = 0; x < blue.getWidth(); x++) {
			for (int y = 0; y < blue.getHeight(); y++) {
				assertEquals(255, fp.getBrightnessScalar(x, y));
			}
		}
	}

	@Test
	void valBrown() {
		ColorPixel fp = ColorPixel.convert(brown);
		for (int x = 0; x < brown.getWidth(); x++) {
			for (int y = 0; y < brown.getHeight(); y++) {
				assertEquals(92, fp.getBrightnessScalar(x, y));
			}
		}
	}

}