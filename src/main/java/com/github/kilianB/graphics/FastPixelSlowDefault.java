package com.github.kilianB.graphics;

import java.awt.image.BufferedImage;

/**
 * A fallback instance of fast pixel using a cached getRGB instance instead of
 * the underlying buffer due to the fact that no other implementation currently
 * is available. This allows to support all image formats but individual
 * implementations should be replaced by faster alternatives in the future.
 * 
 * @author Kilian
 * @since 1.5.2
 */
public class FastPixelSlowDefault extends AbstractPixel {

	/** Full alpha constant */

	private static final int FULL = 0xFFFFFFFF;

	private static final int ALPHA_MASK = 255 << 24;
	private static final int ALPHA_MASK_INVERSE = FULL ^ (ALPHA_MASK);
	private static final int RED_MASK = 255 << 16;
	private static final int RED_MASK_INVERSE = FULL ^ (RED_MASK);
	private static final int GREEN_MASK = 255 << 8;
	private static final int GREEN_MASK_INVERSE = FULL ^ (GREEN_MASK);
	private static final int BLUE_MASK = 255 << 0;
	private static final int BLUE_MASK_INVERSE = FULL ^ (BLUE_MASK);

	/** True if the underlying image has an alpha component */
	private final boolean transparency;

	/** Raw data */
	private final int[] rgbImageData;

	private BufferedImage bImage;

	/**
	 * Constructs a fast pixel object with the underlying buffered image.
	 * 
	 * <p>
	 * Note that calling this method may cause this DataBufferobject to be
	 * incompatible with performance optimizations used by some implementations
	 * (such as caching an associated image in video memory).
	 * 
	 * @param bImage The buffered image to extract data from
	 * @since 1.3.0
	 */
	public FastPixelSlowDefault(BufferedImage bImage) {

		super(bImage.getWidth(), bImage.getHeight());
		transparency = bImage.getColorModel().hasAlpha();

		rgbImageData = bImage.getRGB(0, 0, width, height, null, 0, width);

	}

	@Override
	public int getRGB(int index) {
		return rgbImageData[index];
	}

	@Override
	public int[][] getRGB() {
		int[][] rgb = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < rgbImageData.length; i++) {
			rgb[x][y] = getRGB(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return rgb;
	}

	@Override
	public int getTransparency(int index) {
		if (!transparency) {
			return -1;
		} else {
			return (rgbImageData[index] & ALPHA_MASK) >>> 24;
		}
	}

	@Override
	public int[][] getTransparencies() {
		if (!transparency)
			return null;
		int[][] alpha = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < rgbImageData.length; i++) {
			alpha[x][y] = getTransparency(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return alpha;
	}

	@Override
	public void setTransparency(int index, int transparency) {
		int newRGB = getRGB(index) & ALPHA_MASK_INVERSE | (transparency << 24);
		rgbImageData[index] = newRGB;
		bImage.setRGB(getX(index), getY(index), newRGB);
	}

	@Override
	public void setTransparencies(int[][] transparencies) {
		for (int x = 0; x < transparencies.length; x++) {
			for (int y = 0; y < transparencies[x].length; y++) {
				int index = getOffset(x, y);
				int newRGB = getRGB(index) & ALPHA_MASK_INVERSE | (transparencies[x][y] << 24);
				rgbImageData[index] = newRGB;

				// setAlpha(getOffset(x,y),newAlpha[x][y]);
			}
		}
		bImage.setRGB(0, 0, width, height, rgbImageData, 0, width);
	}

	@Override
	public int getRed(int index) {
		return (rgbImageData[index] & RED_MASK) >>> 16;
	}

	@Override
	public int[][] getRed() {
		int[][] red = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < rgbImageData.length; i++) {
			red[x][y] = getRed(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return red;
	}

	@Override
	public void setRed(int index, int newRed) {
		int newRGB = getRGB(index) & RED_MASK_INVERSE | (newRed << 16);
		rgbImageData[index] = newRGB;
		bImage.setRGB(getX(index), getY(index), newRGB);
	}

	@Override
	public void setRed(int[][] newRed) {
		for (int x = 0; x < newRed.length; x++) {
			for (int y = 0; y < newRed[x].length; y++) {
				// setRed(getOffset(x,y),newRed[x][y]);
				int index = getOffset(x, y);
				int newRGB = getRGB(index) & RED_MASK_INVERSE | (newRed[x][y] << 16);
				rgbImageData[index] = newRGB;
			}
		}
		bImage.setRGB(0, 0, width, height, rgbImageData, 0, width);
	}

	@Override
	public int getGreen(int index) {
		return (rgbImageData[index] & GREEN_MASK) >>> 8;
	}

	@Override
	public void setGreen(int index, int newGreen) {
		int newRGB = getRGB(index) & GREEN_MASK_INVERSE | (newGreen << 8);
		rgbImageData[index] = newRGB;
		bImage.setRGB(getX(index), getY(index), newRGB);
	}

	@Override
	public void setGreen(int[][] newGreen) {
		for (int x = 0; x < newGreen.length; x++) {
			for (int y = 0; y < newGreen[x].length; y++) {
				// setGreen(getOffset(x,y),newRed[x][y]);
				int index = getOffset(x, y);
				int newRGB = getRGB(index) & GREEN_MASK_INVERSE | (newGreen[x][y] << 8);
				rgbImageData[index] = newRGB;
			}
		}
		bImage.setRGB(0, 0, width, height, rgbImageData, 0, width);
	}

	@Override
	public int[][] getGreen() {
		int[][] green = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < rgbImageData.length; i++) {
			green[x][y] = getGreen(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return green;
	}

	@Override
	public int getBlue(int index) {
		return rgbImageData[index] & BLUE_MASK;
	}

	@Override
	public void setBlue(int index, int newBlue) {
		int newRGB = getRGB(index) & BLUE_MASK_INVERSE | (newBlue);
		rgbImageData[index] = newRGB;
		bImage.setRGB(getX(index), getY(index), newRGB);
	}

	@Override
	public int[][] getBlue() {
		int[][] blue = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < rgbImageData.length; i++) {
			blue[x][y] = getBlue(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return blue;
	}

	@Override
	public void setBlue(int[][] newBlue) {
		for (int x = 0; x < newBlue.length; x++) {
			for (int y = 0; y < newBlue[x].length; y++) {
				int index = getOffset(x, y);
				int newRGB = getRGB(index) & BLUE_MASK_INVERSE | (newBlue[x][y]);
				rgbImageData[index] = newRGB;
			}
		}
		bImage.setRGB(0, 0, width, height, rgbImageData, 0, width);
	}

	@Override
	public int[][] getAverageGrayscale() {
		int[][] gray = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < rgbImageData.length; i++) {
			gray[x][y] = getAverageGrayscale(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return gray;
	}

	@Override
	public void setAverageGrayscale(int[][] newGrayValue) {
		for (int x = 0; x < newGrayValue.length; x++) {
			for (int y = 0; y < newGrayValue[x].length; y++) {
				this.setAverageGrayscale(x, y, newGrayValue[x][y]);
			}
		}
		bImage.setRGB(0, 0, width, height, rgbImageData, 0, width);
	}

	@Override
	public int[][] getLuma() {
		int[][] luma = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < rgbImageData.length; i++) {
			luma[x][y] = getLuma(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return luma;
	}

	@Override
	public int[] getLuma1D() {
		int[] luma = new int[width * height];
		for (int i = 0; i < rgbImageData.length; i++) {
			luma[i] = getLuma(i);
		}
		return luma;
	}

	@Override
	public boolean hasTransparency() {
		return transparency;
	}

	@Override
	public int getOffset(int x, int y) {
		return (y * width) + x;
	}

	private int getX(int index) {
		return index % width;
	}

	private int getY(int index) {
		return index / width;
	}
}
