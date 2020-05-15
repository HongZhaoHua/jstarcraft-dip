package com.github.kilianB.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

/**
 * High performant access of RGB/YCrCb/HSV Data.
 * 
 * <p>
 * RGB methods run faster by a magnitude (factor 10) compared to the original
 * java getRGB method.
 * 
 * <p>
 * Currently only ARGB and RGB image types are supported.
 * 
 * @author Kilian
 * @since 1.3.0
 */
public class BytePixel extends AbstractPixel {

	/** Full alpha constant */
	private static final int ALPHA_MASK = 255 << 24;

	/** Offset used in case alpha is present */
	private final int alphaOffset;
	/** Bytes used to represent a single pixel */
	private final int bytesPerColor;

	/** Raw data */
	private final byte[] imageData;

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
	public BytePixel(BufferedImage bImage) {
		super(bImage.getWidth(), bImage.getHeight(), bImage.getColorModel().hasAlpha());
		imageData = ((DataBufferByte) bImage.getRaster().getDataBuffer()).getData();

		if (bImage.getColorModel().hasAlpha()) {
			alphaOffset = 1;
			bytesPerColor = 4;
		} else {
			alphaOffset = 0;
			bytesPerColor = 3;
		}
	}

	@Override
	public int getRgbScalar(int index) {
		return (hasTransparency() ? (imageData[index++] & 0xFF) << 24 : ALPHA_MASK) | ((imageData[index++] & 0xFF)) | ((imageData[index++] & 0xFF) << 8) | ((imageData[index++] & 0xFF) << 16);
	}

	/**
	 * Returns the rgb values of the entire image in an 2 d array in the default RGB
	 * color model(TYPE_INT_ARGB). There are only 8-bits of precision for each color
	 * component in the returned data when using this method. An
	 * ArrayOutOfBoundsException may be thrown if the coordinates are not in bounds.
	 * 
	 * @return a 2d integer array containing the argb values of the image
	 * @since 1.3.0
	 */
	@Override
	public int[][] getRgbMatrix() {
		int[][] rgb = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i++) {
			// We could use the getRGB(x,y) method. but lets inline some calls
			int argb;
			argb = hasTransparency() ? (imageData[i++] & 0xFF) << 24 : ALPHA_MASK;
			// Red
			argb |= (imageData[i++] & 0xFF) | (imageData[i++] & 0xFF) << 8 | (imageData[i] & 0xFF) << 16;

			rgb[x][y] = argb;
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return rgb;
	}

	/**
	 * Get the alpha component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the alpha values or null if alpha is not supported
	 * @since 1.3.0
	 */
	@Override
	public int[][] getTransparencyMatrix() {
		if (!hasTransparency())
			return null;
		int[][] alpha = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor) {
			alpha[x][y] = (imageData[i] & 0xFF);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return alpha;
	}

	@Override
	public int getTransparencyScalar(int index) {
		if (!hasTransparency())
			return -1;
		return imageData[index] & 0xFF;
	}

	@Override
	public void setTransparencyScalar(int index, int transparency) {
		if (!hasTransparency())
			return;
		imageData[index] = (byte) (transparency);
	}

	/**
	 * Set new alpha values for the entire picture
	 * 
	 * @param transparencies red values in range [0-255]
	 * @since 1.4.5
	 */
	@Override
	public void setTransparencyMatrix(int[][] transparencies) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setTransparencyScalar(x, y, transparencies[x][y]);
			}
		}
	}

	@Override
	public int getRedScalar(int index) {
		return imageData[index + alphaOffset + 2] & 0xFF;
	}

	@Override
	public void setRedScalar(int index, int newRed) {
		imageData[index + alphaOffset + 2] = (byte) (newRed);
	}

	/**
	 * Set new red values for the entire picture
	 * 
	 * @param newRed red values in range [0-255]
	 * @since 1.4.5
	 */
	@Override
	public void setRedMatrix(int[][] newRed) {
		// TODO inline method call?
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setRedScalar(x, y, newRed[x][y]);
			}
		}
	}

	@Override
	public int getGreenScalar(int index) {
		return imageData[index + alphaOffset + 1] & 0xFF;
	}

	@Override
	public void setGreenScalar(int index, int newGreen) {
		imageData[index + alphaOffset + 1] = (byte) (newGreen);
	}

	/**
	 * Set new green values for the entire picture
	 * 
	 * @param newGreen red values in range [0-255]
	 * @since 1.4.5
	 */
	@Override
	public void setGreenMatrix(int[][] newGreen) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setGreenScalar(x, y, newGreen[x][y]);
			}
		}
	}

	/**
	 * Get the green component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the green values
	 * @since 1.3.0
	 */
	@Override
	public int[][] getGreenMatrix() {
		int[][] green = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor) {
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
		return imageData[index + alphaOffset] & 0xFF;
	}

	@Override
	public void setBlueScalar(int index, int newBlue) {
		imageData[index + alphaOffset] = (byte) (newBlue);
	}

	/**
	 * Get the blue component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the blue values
	 * @since 1.3.0
	 */
	@Override
	public int[][] getBlueMatrix() {
		int[][] blue = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor) {
			blue[x][y] = getBlueScalar(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return blue;
	}

	/**
	 * Set new blue values for the entire picture
	 * 
	 * @param newBlue red values in range [0-255]
	 * @since 1.4.5
	 */
	@Override
	public void setBlueMatrix(int[][] newBlue) {
		// TODO inline method call?
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setBlueScalar(x, y, newBlue[x][y]);
			}
		}
	}

	// Grayscale

	@Override
	public int[][] getGrayscaleMatrix() {
		int[][] gray = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor) {
			gray[x][y] = getGrayscaleScalar(x, y);
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
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setGrayscaleScalar(x, y, newGrayValue[x][y]);
			}
		}
	}

	// YCrCb

	/**
	 * Return the Y(Luma) component of the YCbCr color model fof the entire image
	 * mapped to a 2d array representing the x and y coordinates of the pixel.
	 * 
	 * @return the luma component in range [0-255]
	 * @since 1.3.1
	 */
	@Override
	public int[][] getLumaMatrix() {
		int luma[][] = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor) {

			luma[x][y] = getLumaScalar(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return luma;
	}

	@Override
	public int[] getLumaVector() {
		int luma[] = new int[width * height];
		for (int i = 0, j = 0; i < imageData.length; i += bytesPerColor, j++) {
			luma[j] = getLumaScalar(i);
		}
		return luma;
	}

	public int getIndex(int x, int y) {
		return (y * bytesPerColor * width) + (x * bytesPerColor);
	}

	@Override
	public int[][] getRedMatrix() {
		int[][] red = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor) {
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
	public int[] getRedVector() {
		int[] red = new int[width * height];
		int j = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor, j++) {
			red[j] = getRedScalar(i);
		}
		return red;
	}

	@Override
	public int[] getGreenVector() {
		int[] green = new int[width * height];
		int j = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor, j++) {
			green[j] = getGreenScalar(i);
		}
		return green;
	}

	@Override
	public int[] getBlueVector() {
		int[] blue = new int[width * height];
		int j = 0;
		for (int i = 0; i < imageData.length; i += bytesPerColor, j++) {
			blue[j] = getBlueScalar(i);
		}
		return blue;
	}

}