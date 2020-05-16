package com.github.kilianB.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.github.kilianB.MathUtil;

/**
 * @author Kilian
 *
 */
public class IntegerPixel extends AbstractPixel {

	/** Full alpha constant */
	private static final int TRANSPARENCY_MASK = 255 << 24;

	/** Raw data */
	private int[] pixelData;

	private int transparencyMask;
	private int redMask;
	private int greenMask;
	private int blueMask;

	private int transparencyOffset;
	private int redOffset;
	private int blueOffset;
	private int greenOffset;

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
	public IntegerPixel(BufferedImage bImage) {
		super(bImage.getWidth(), bImage.getHeight(), bImage.getColorModel().hasAlpha());

		pixelData = ((DataBufferInt) bImage.getRaster().getDataBuffer()).getData();

		switch (bImage.getType()) {

		case BufferedImage.TYPE_INT_ARGB:
			redMask = 0x00ff0000;
			greenMask = 0x0000ff00;
			blueMask = 0x000000ff;
			transparencyMask = 0xff000000;
			break;
		case BufferedImage.TYPE_INT_RGB:
			redMask = 0x00ff0000;
			greenMask = 0x0000ff00;
			blueMask = 0x000000ff;
			transparencyMask = 0x00000000;
			break;
		case BufferedImage.TYPE_INT_BGR:
			redMask = 0x000000ff;
			greenMask = 0x0000ff00;
			blueMask = 0x00ff0000;
			transparencyMask = 0x00000000;
			break;
		}

		redOffset = MathUtil.getLowerShiftBitMask(redMask);
		greenOffset = MathUtil.getLowerShiftBitMask(greenMask);
		blueOffset = MathUtil.getLowerShiftBitMask(blueMask);
		transparencyOffset = MathUtil.getLowerShiftBitMask(transparencyMask);

	}

	@Override
	public int getRgbScalar(int index) {
		return (hasTransparency() ? (getTransparencyScalar(index) << 24) : TRANSPARENCY_MASK) | (getRedScalar(index) << 16) | (getGreenScalar(index) << 8) | (getBlueScalar(index));
	}

	/**
	 * Returns an integer pixel in the default RGB color model(TYPE_INT_ARGB). There
	 * are only 8-bits of precision for each color component in the returned data
	 * when using this method. An ArrayOutOfBoundsException may be thrown if the
	 * coordinates are not in bounds.
	 * 
	 * @param x the X coordinate of the pixel from which to get the pixel in the
	 *          default RGB color model
	 * @param y the Y coordinate of the pixel from which to get the pixel in the
	 *          default RGB color model
	 * @return an integer pixel in the default RGB color model and default sRGB
	 *         colorspace.
	 * @since 1.3.0
	 */
	@Override
	public int getRgbScalar(int x, int y) {
		return getRgbScalar(getIndex(x, y));
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

	public int getTransparencyScalar(int index) {
		if (!hasTransparency())
			return -1;
		return (pixelData[index] & transparencyMask) >>> transparencyOffset;
	}

	/**
	 * Get the alpha value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the alpha value in range [0-255] or -1 if alpha is not supported
	 * @since 1.3.0
	 */
	@Override
	public int getTransparencyScalar(int x, int y) {
		return getTransparencyScalar(getIndex(x, y));
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
		if (!this.hasTransparency())
			return;
		pixelData[index] |= (transparency << transparencyOffset);
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
		return (pixelData[index] & redMask) >>> redOffset;
	}

	/**
	 * Get the red value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the red value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public int getRedScalar(int x, int y) {
		return getRedScalar(getIndex(x, y));
	}

	/**
	 * Get the red component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the red values
	 * @since 1.3.0
	 */
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
		// Clear red part first
		pixelData[index] = (pixelData[index] & (~redMask)) | (newRed << redOffset);
	}

	/**
	 * Set the red value of the specified pixel
	 * 
	 * @param x      The x coordinate of the images' pixel
	 * @param y      The y coordinate of the images' pixel
	 * @param newRed the new red value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public void setRedScalar(int x, int y, int newRed) {
		setRedScalar(getIndex(x, y), newRed);
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
		return (pixelData[index] & greenMask) >>> greenOffset;
	}

	/**
	 * Get the green value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the green value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public int getGreenScalar(int x, int y) {
		return getGreenScalar(getIndex(x, y));
	}

	@Override
	public void setGreenScalar(int index, int newGreen) {
		// Clear green part first
		pixelData[index] = (pixelData[index] & (~greenMask)) | (newGreen << greenOffset);
	}

	/**
	 * Set the green value of the specified pixel
	 * 
	 * @param x        The x coordinate of the images' pixel
	 * @param y        The y coordinate of the images' pixel
	 * @param newGreen the new green value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public void setGreenScalar(int x, int y, int newGreen) {
		setGreenScalar(getIndex(x, y), newGreen);
	}

	/**
	 * Set new green values for the entire picture
	 * 
	 * @param newGreen red values in range [0-255]
	 * @since 1.4.5
	 */
	@Override
	public void setGreenMatrix(int[][] newGreen) {
		// TODO inline method call?
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
		return (pixelData[index] & blueMask) >>> blueOffset;
	}

	/**
	 * Get the blue value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the blue value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public int getBlueScalar(int x, int y) {
		return getBlueScalar(getIndex(x, y));
	}

	@Override
	public void setBlueScalar(int index, int newBlue) {
		pixelData[index] = (pixelData[index] & (~blueMask)) | (newBlue << blueOffset);
	}

	/**
	 * Set the blue value of the specified pixel
	 * 
	 * @param x       The x coordinate of the images' pixel
	 * @param y       The y coordinate of the images' pixel
	 * @param newBlue the new blue value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public void setBlueScalar(int x, int y, int newBlue) {
		setBlueScalar(getIndex(x, y), newBlue);
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

	/**
	 * Set new blue values for the entire picture
	 * 
	 * @param newBlue red values in range [0-255]
	 * @since 1.4.5
	 */
	@Override
	public void setBlueMatrix(int[][] newBlue) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setBlueScalar(x, y, newBlue[x][y]);
			}
		}
	}

	// grayscale

	@Override
	public int[][] getGrayscaleMatrix() {
		int[][] gray = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < pixelData.length; i++) {
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
		for (int i = 0; i < pixelData.length; i++) {
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
		for (int i = 0; i < pixelData.length; i++) {
			luma[i] = getLumaScalar(i);
		}
		return luma;
	}

}
