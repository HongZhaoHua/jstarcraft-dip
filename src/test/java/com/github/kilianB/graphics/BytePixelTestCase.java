package com.github.kilianB.graphics;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

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
		assertEquals(BytePixel.class, Pixel.create(lena).getClass());
		assertEquals(BytePixel.class, Pixel.create(bw).getClass());
		assertEquals(BytePixel.class, Pixel.create(red).getClass());
		assertEquals(BytePixel.class, Pixel.create(green).getClass());
		assertEquals(BytePixel.class, Pixel.create(blue).getClass());
		assertEquals(BytePixel.class, Pixel.create(brown).getClass());
		assertEquals(BytePixel.class, Pixel.create(brownOpacity).getClass());
	}

	@Test
	void hasAlphaFalse() {
		assertFalse(Pixel.create(brown).hasTransparency());
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
				assertEquals(lena.getRGB(x, y), fp.getRGB(x, y));
			}
		}
	}

	@Test
	void getRGBArray() {
		Pixel fp = Pixel.create(lena);
		int[][] rgb = fp.getRGB();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getRGB(x, y), rgb[x][y]);
			}
		}
	}

	@Test
	void red() {
		Pixel fp = Pixel.create(lena);
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(lena.getRGB(x, y));
				assertEquals(comp[1], fp.getRed(x, y));
			}
		}
	}

	@Test
	void redArray() {
		Pixel fp = Pixel.create(lena);
		int[][] red = fp.getRed();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getRed(x, y), red[x][y]);
			}
		}
	}

	@Test
	void setRed() {
		BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		Pixel fp = Pixel.create(bi);
		fp.setRed(0, 0, 255);

		assertAll(() -> {
			assertEquals(255, fp.getRed(0, 0));
		}, () -> {
			assertEquals(0, fp.getGreen(0, 0));
		}, () -> {
			assertEquals(0, fp.getBlue(0, 0));
		});
	}

	@Test
	void bulkSetRed() {
		int w = 10;
		int h = 10;
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
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
		fp.setRed(values);
		assertArrayEquals(values, fp.getRed());
	}

	@Test
	void green() {
		Pixel fp = Pixel.create(lena);
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(lena.getRGB(x, y));
				assertEquals(comp[2], fp.getGreen(x, y));
			}
		}
	}

	@Test
	void greenArray() {
		Pixel fp = Pixel.create(lena);
		int[][] green = fp.getGreen();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getGreen(x, y), green[x][y]);
			}
		}
	}

	@Test
	void setGreen() {
		BufferedImage bi = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		Pixel fp = Pixel.create(bi);
		fp.setGreen(0, 0, 255);

		assertAll(() -> {
			assertEquals(0, fp.getRed(0, 0));
		}, () -> {
			assertEquals(255, fp.getGreen(0, 0));
		}, () -> {
			assertEquals(0, fp.getBlue(0, 0));
		});
	}

	@Test
	void bulkSetGreen() {
		int w = 10;
		int h = 10;
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
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
		fp.setGreen(values);
		assertArrayEquals(values, fp.getGreen());
	}

	@Test
	void blue() {
		Pixel fp = Pixel.create(lena);
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(lena.getRGB(x, y));
				assertEquals(comp[3], fp.getBlue(x, y));
			}
		}
	}

	@Test
	void blueArray() {
		Pixel fp = Pixel.create(lena);
		int[][] blue = fp.getBlue();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getBlue(x, y), blue[x][y]);
			}
		}
	}

	@Test
	void blueArray1D() {
		Pixel fp = Pixel.create(lena);
		int[] blue = fp.getBlue1D();
		int[][] blue2D = fp.getBlue();
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
		Pixel fp = Pixel.create(bi);
		fp.setBlue(0, 0, 255);

		assertAll(() -> {
			assertEquals(0, fp.getRed(0, 0));
		}, () -> {
			assertEquals(0, fp.getGreen(0, 0));
		}, () -> {
			assertEquals(255, fp.getBlue(0, 0));
		});
	}

	@Test
	void bulkSetBlue() {
		int w = 10;
		int h = 10;
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);
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
		fp.setBlue(values);
		assertArrayEquals(values, fp.getBlue());
	}

	@Test
	void getRGBOpaque() {
		Pixel fp = Pixel.create(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				assertEquals(brownOpacity.getRGB(x, y), fp.getRGB(x, y));
			}
		}
	}

	@Test
	void alphaOpaque() {
		Pixel fp = Pixel.create(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(brownOpacity.getRGB(x, y));
				assertEquals(comp[0], fp.getTransparency(x, y));
			}
		}
	}

	@Test
	void redOpaque() {
		Pixel fp = Pixel.create(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(brownOpacity.getRGB(x, y));
				assertEquals(comp[1], fp.getRed(x, y));
			}
		}
	}

	@Test
	void greenOpaque() {
		Pixel fp = Pixel.create(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(brownOpacity.getRGB(x, y));
				assertEquals(comp[2], fp.getGreen(x, y));
			}
		}
	}

	@Test
	void blueOpaque() {
		Pixel fp = Pixel.create(brownOpacity);
		for (int x = 0; x < brownOpacity.getWidth(); x++) {
			for (int y = 0; y < brownOpacity.getHeight(); y++) {
				int[] comp = ColorUtil.argbToComponents(brownOpacity.getRGB(x, y));
				assertEquals(comp[3], fp.getBlue(x, y));
			}
		}
	}

	@Test
	void bulkSetRGBA() {
		int w = 1000;
		int h = 1000;
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
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
		fp.setRed(values);
		fp.setBlue(values);
		fp.setGreen(values);
		fp.setTransparencies(values);

		assertAll(() -> {
			assertArrayEquals(values, fp.getRed());
		}, () -> {
			assertArrayEquals(values, fp.getGreen());
		}, () -> {
			assertArrayEquals(values, fp.getBlue());
		}, () -> {
			assertArrayEquals(values, fp.getTransparencies());
		});
	}

	@Test
	void rgbArray() {
		int arg[] = lena.getRGB(0, 0, lena.getWidth(), lena.getHeight(), null, 0, lena.getWidth());
		Pixel fp = Pixel.create(lena);
		int[][] argFp = fp.getRGB();
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
					assertEquals(0, fp.getLuma(x, y));
				} else {
					// White
					assertEquals(255, fp.getLuma(x, y));
				}
			}
		}
	}

	@Test
	void lumArray() {
		Pixel fp = Pixel.create(lena);
		int[][] lumArr = fp.getLuma();
		for (int x = 0; x < lena.getWidth(); x++) {
			for (int y = 0; y < lena.getHeight(); y++) {
				assertEquals(fp.getLuma(x, y), lumArr[x][y]);
			}
		}
	}

	@Test
	void lumInRange() {
		Pixel fp = Pixel.create(lena);

		for (int x = 0; x < bw.getWidth(); x++) {
			for (int y = 0; y < bw.getHeight(); y++) {

				int lum = fp.getLuma(x, y);
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
				assertEquals(0, fp.getHue(x, y));
			}
		}
	}

	@Test
	void hueRed() {
		Pixel fp = Pixel.create(red);

		for (int x = 0; x < red.getWidth(); x++) {
			for (int y = 0; y < red.getHeight(); y++) {
				assertEquals(0, fp.getHue(x, y));
			}
		}
	}

	@Test
	void hueGreen() {
		Pixel fp = Pixel.create(green);
		for (int x = 0; x < green.getWidth(); x++) {
			for (int y = 0; y < green.getHeight(); y++) {
				assertEquals(120, fp.getHue(x, y));
			}
		}
	}

	@Test
	void hueBlue() {
		Pixel fp = Pixel.create(blue);
		for (int x = 0; x < blue.getWidth(); x++) {
			for (int y = 0; y < blue.getHeight(); y++) {
				assertEquals(240, fp.getHue(x, y));
			}
		}
	}

	@Test
	void hueBrown() {
		Pixel fp = Pixel.create(brown);
		for (int x = 0; x < brown.getWidth(); x++) {
			for (int y = 0; y < brown.getHeight(); y++) {
				assertEquals(20, fp.getHue(x, y));
			}
		}
	}

	@Test
	void satBlackWhite() {
		Pixel fp = Pixel.create(bw);
		// Invalid hue value. Defined as 0
		for (int x = 0; x < bw.getWidth(); x++) {
			for (int y = 0; y < bw.getHeight(); y++) {
				assertEquals(0, fp.getSat(x, y));
			}
		}
	}

	@Test
	void satRed() {
		Pixel fp = Pixel.create(red);

		for (int x = 0; x < red.getWidth(); x++) {
			for (int y = 0; y < red.getHeight(); y++) {
				assertEquals(1, fp.getSat(x, y));
			}
		}
	}

	@Test
	void satGreen() {
		Pixel fp = Pixel.create(green);
		for (int x = 0; x < green.getWidth(); x++) {
			for (int y = 0; y < green.getHeight(); y++) {
				assertEquals(1, fp.getSat(x, y));
			}
		}
	}

	@Test
	void satBlue() {
		Pixel fp = Pixel.create(blue);
		for (int x = 0; x < blue.getWidth(); x++) {
			for (int y = 0; y < blue.getHeight(); y++) {
				assertEquals(1, fp.getSat(x, y));
			}
		}
	}

	@Test
	void satBrown() {
		Pixel fp = Pixel.create(brown);
		for (int x = 0; x < brown.getWidth(); x++) {
			for (int y = 0; y < brown.getHeight(); y++) {
				assertEquals(0.75, fp.getSat(x, y));
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
					assertEquals(0, fp.getVal(x, y));
				} else {
					// White
					assertEquals(255, fp.getVal(x, y));
				}

			}
		}
	}

	@Test
	void valRed() {
		Pixel fp = Pixel.create(red);

		for (int x = 0; x < red.getWidth(); x++) {
			for (int y = 0; y < red.getHeight(); y++) {
				assertEquals(255, fp.getVal(x, y));
			}
		}
	}

	@Test
	void valGreen() {
		Pixel fp = Pixel.create(green);
		for (int x = 0; x < green.getWidth(); x++) {
			for (int y = 0; y < green.getHeight(); y++) {
				assertEquals(255, fp.getVal(x, y));
			}
		}
	}

	@Test
	void valBlue() {
		Pixel fp = Pixel.create(blue);
		for (int x = 0; x < blue.getWidth(); x++) {
			for (int y = 0; y < blue.getHeight(); y++) {
				assertEquals(255, fp.getVal(x, y));
			}
		}
	}

	@Test
	void valBrown() {
		Pixel fp = Pixel.create(brown);
		for (int x = 0; x < brown.getWidth(); x++) {
			for (int y = 0; y < brown.getHeight(); y++) {
				assertEquals(92, fp.getVal(x, y));
			}
		}
	}

}