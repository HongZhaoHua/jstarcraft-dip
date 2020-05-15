package com.github.kilianB.graphics;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Kilian
 *
 */
class ImageUtilTest {

	// No alpha image
	private static BufferedImage lena;

	//
	private static BufferedImage bw;

	private static BufferedImage catCustom;
	
	@BeforeAll
	static void loadImage() {
		try {
			lena = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("Lena.png"));
			bw = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("BlackWhite.png"));
			catCustom = ImageIO.read(ImageUtilTest.class.getClassLoader().getResourceAsStream("catMono.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	void getScaledInstanceSize() {
		BufferedImage scaled = ImageUtil.getScaledInstance(lena, 10, 10);
		assertEquals(10, scaled.getWidth());
		assertEquals(10, scaled.getHeight());
	}
	
	@Test
	void getScaledInstanceSizeBW() {
		BufferedImage scaled = ImageUtil.getScaledInstance(bw, 10, 10);
		assertEquals(10, scaled.getWidth());
		assertEquals(10, scaled.getHeight());
	}
	
	@Test
	void getScaledInstanceCustomImage() {
		BufferedImage scaled = ImageUtil.getScaledInstance(catCustom, 10, 10);
		assertEquals(10, scaled.getWidth());
		assertEquals(10, scaled.getHeight());
	}

}
