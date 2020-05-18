package com.jstarcraft.dip.color;

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
public class DefaultPixel extends AbstractPixel {

	/** Full alpha constant */

	private static final int MASK = 0xFFFFFFFF;

	private static final int TRANSPARENCY_GET_MASK = 255 << 24;
	private static final int TRANSPARENCY_SET_MASK = MASK ^ (TRANSPARENCY_GET_MASK);
	private static final int RED_GET_MASK = 255 << 16;
	private static final int RED_SET_MASK = MASK ^ (RED_GET_MASK);
	private static final int GREEN_GET_MASK = 255 << 8;
	private static final int GREEN_SET_MASK = MASK ^ (GREEN_GET_MASK);
	private static final int BLUE_GET_MASK = 255 << 0;
	private static final int BLUE_SET_MASK = MASK ^ (BLUE_GET_MASK);

	/** Raw data */
	private final int[] pixelData;

	private BufferedImage pixelImage;

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
	public DefaultPixel(BufferedImage bImage) {
		super(bImage.getWidth(), bImage.getHeight(), bImage.getColorModel().hasAlpha());

		pixelData = bImage.getRGB(0, 0, width, height, null, 0, width);
	}

	@Override
	public int getRgbScalar(int index) {
		return pixelData[index];
	}

	@Override
	public int[][] getRgbMatrix() {
		int[][] rgb = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < pixelData.length; i++) {
			rgb[x][y] = getRgbScalar(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return rgb;
	}

	@Override
	public int getTransparencyScalar(int index) {
		if (!hasTransparency()) {
			return -1;
		} else {
			return (pixelData[index] & TRANSPARENCY_GET_MASK) >>> 24;
		}
	}

	@Override
	public int[][] getTransparencyMatrix() {
		if (!hasTransparency())
			return null;
		int[][] alpha = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < pixelData.length; i++) {
			alpha[x][y] = getTransparencyScalar(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return alpha;
	}

	@Override
	public void setTransparencyScalar(int index, int transparency) {
		int newRGB = getRgbScalar(index) & TRANSPARENCY_SET_MASK | (transparency << 24);
		pixelData[index] = newRGB;
		pixelImage.setRGB(getX(index), getY(index), newRGB);
	}

	@Override
	public void setTransparencyMatrix(int[][] transparencies) {
		for (int x = 0; x < transparencies.length; x++) {
			for (int y = 0; y < transparencies[x].length; y++) {
				int index = getIndex(x, y);
				int newRGB = getRgbScalar(index) & TRANSPARENCY_SET_MASK | (transparencies[x][y] << 24);
				pixelData[index] = newRGB;

				// setAlpha(getOffset(x,y),newAlpha[x][y]);
			}
		}
		pixelImage.setRGB(0, 0, width, height, pixelData, 0, width);
	}

	@Override
	public int getRedScalar(int index) {
		return (pixelData[index] & RED_GET_MASK) >>> 16;
	}

	@Override
	public int[][] getRedMatrix() {
		int[][] red = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < pixelData.length; i++) {
			red[x][y] = getRedScalar(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return red;
	}

	@Override
	public void setRedScalar(int index, int newRed) {
		int newRGB = getRgbScalar(index) & RED_SET_MASK | (newRed << 16);
		pixelData[index] = newRGB;
		pixelImage.setRGB(getX(index), getY(index), newRGB);
	}

	@Override
	public void setRedMatrix(int[][] newRed) {
		for (int x = 0; x < newRed.length; x++) {
			for (int y = 0; y < newRed[x].length; y++) {
				// setRed(getOffset(x,y),newRed[x][y]);
				int index = getIndex(x, y);
				int newRGB = getRgbScalar(index) & RED_SET_MASK | (newRed[x][y] << 16);
				pixelData[index] = newRGB;
			}
		}
		pixelImage.setRGB(0, 0, width, height, pixelData, 0, width);
	}

	@Override
	public int getGreenScalar(int index) {
		return (pixelData[index] & GREEN_GET_MASK) >>> 8;
	}

	@Override
	public void setGreenScalar(int index, int newGreen) {
		int newRGB = getRgbScalar(index) & GREEN_SET_MASK | (newGreen << 8);
		pixelData[index] = newRGB;
		pixelImage.setRGB(getX(index), getY(index), newRGB);
	}

	@Override
	public void setGreenMatrix(int[][] newGreen) {
		for (int x = 0; x < newGreen.length; x++) {
			for (int y = 0; y < newGreen[x].length; y++) {
				// setGreen(getOffset(x,y),newRed[x][y]);
				int index = getIndex(x, y);
				int newRGB = getRgbScalar(index) & GREEN_SET_MASK | (newGreen[x][y] << 8);
				pixelData[index] = newRGB;
			}
		}
		pixelImage.setRGB(0, 0, width, height, pixelData, 0, width);
	}

	@Override
	public int[][] getGreenMatrix() {
		int[][] green = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < pixelData.length; i++) {
			green[x][y] = getGreenScalar(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return green;
	}

	@Override
	public int getBlueScalar(int index) {
		return pixelData[index] & BLUE_GET_MASK;
	}

	@Override
	public void setBlueScalar(int index, int newBlue) {
		int newRGB = getRgbScalar(index) & BLUE_SET_MASK | (newBlue);
		pixelData[index] = newRGB;
		pixelImage.setRGB(getX(index), getY(index), newRGB);
	}

	@Override
	public int[][] getBlueMatrix() {
		int[][] blue = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < pixelData.length; i++) {
			blue[x][y] = getBlueScalar(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return blue;
	}

	@Override
	public void setBlueMatrix(int[][] newBlue) {
		for (int x = 0; x < newBlue.length; x++) {
			for (int y = 0; y < newBlue[x].length; y++) {
				int index = getIndex(x, y);
				int newRGB = getRgbScalar(index) & BLUE_SET_MASK | (newBlue[x][y]);
				pixelData[index] = newRGB;
			}
		}
		pixelImage.setRGB(0, 0, width, height, pixelData, 0, width);
	}

	@Override
	public int[][] getGrayscaleMatrix() {
		int[][] gray = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < pixelData.length; i++) {
			gray[x][y] = getGrayscaleScalar(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return gray;
	}

	@Override
	public void setGrayscaleMatrix(int[][] newGrayValue) {
		for (int x = 0; x < newGrayValue.length; x++) {
			for (int y = 0; y < newGrayValue[x].length; y++) {
				this.setGrayscaleScalar(x, y, newGrayValue[x][y]);
			}
		}
		pixelImage.setRGB(0, 0, width, height, pixelData, 0, width);
	}

	@Override
	public int[][] getLuminanceMatrix() {
		int[][] luma = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < pixelData.length; i++) {
			luma[x][y] = getLuminanceScalar(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return luma;
	}

	@Override
	public int[] getLuminanceVector() {
		int[] luma = new int[width * height];
		for (int i = 0; i < pixelData.length; i++) {
			luma[i] = getLuminanceScalar(i);
		}
		return luma;
	}

	private int getX(int index) {
		return index % width;
	}

	private int getY(int index) {
		return index / width;
	}

}
