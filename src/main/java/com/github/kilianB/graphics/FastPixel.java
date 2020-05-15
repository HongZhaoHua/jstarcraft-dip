package com.github.kilianB.graphics;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

/**
 * Utility class to access pixel data in a fraction of the time required by the
 * native JDK methods.
 * 
 * <p>
 * Additionally support different color spaces and bulk operations
 * 
 * @author Kilian
 *
 */
public interface FastPixel {

	static final Logger LOGGER = Logger.getLogger(FastPixel.class.getSimpleName());

	/**
	 * Return a fast pixel instance mapped to the buffered image type
	 * 
	 * @param bufferedImage the buffered image to create a fast pixel instance for
	 * @return an instantiated FastPixelObject
	 */
	public static FastPixel create(BufferedImage bufferedImage) {

		switch (bufferedImage.getType()) {

		case BufferedImage.TYPE_3BYTE_BGR:
		case BufferedImage.TYPE_4BYTE_ABGR:
			return new FastPixelByte(bufferedImage);
		case BufferedImage.TYPE_INT_BGR:
		case BufferedImage.TYPE_INT_ARGB:
		case BufferedImage.TYPE_INT_RGB:
			return new FastPixelInt(bufferedImage);
		default:
			LOGGER.info("No fast implementation available for " + bufferedImage.getType()
					+ ". Fallback to slow default variant.");
			return new FastPixelSlowDefault(bufferedImage);
//			throw new UnsupportedOperationException(
//					"The image type is currently not supported: " + bufferedImage.getType());
//		
		}
	}

	int getRGB(int index);

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
	default int getRGB(int x, int y) {
		return getRGB(getOffset(x, y));
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
	int[][] getRGB();

	/**
	 * Get the alpha value of the specified pixel
	 * 
	 * @param index the offset in the underlying array
	 * @return the alpha value in range [0-255] or -1 if alpha is not supported
	 * @since 1.5.0
	 */
	int getAlpha(int index);

	/**
	 * Get the alpha value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the alpha value in range [0-255] or -1 if alpha is not supported
	 * @since 1.3.0
	 */
	default int getAlpha(int x, int y) {
		return getAlpha(getOffset(x, y));
	}

	/**
	 * Get the alpha component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the alpha values or null if alpha is not supported
	 * @since 1.3.0
	 */
	int[][] getAlpha();

	/**
	 * Set the alpha value of the specified pixel. This method is a NOP if alpha is
	 * not supported.
	 * 
	 * @param x        The x coordinate of the images' pixel
	 * @param y        The y coordinate of the images' pixel
	 * @param newAlpha the new alpha value in range [0-255]
	 * @since 1.3.0
	 */
	default void setAlpha(int x, int y, int newAlpha) {
		setAlpha(getOffset(x, y), newAlpha);
	}

	/**
	 * Set the alpha value. This method is a NOP if alpha is not supported.
	 * 
	 * @param index    the offset of the underlying array
	 * @param newAlpha the new alpha value in range [0-255]
	 * @since 1.5.0
	 */
	void setAlpha(int index, int newAlpha);

	/**
	 * Set new alpha values for the entire picture
	 * 
	 * @param newAlpha red values in range [0-255]
	 * @since 1.4.5
	 */
	void setAlpha(int[][] newAlpha);
	
	
	/**
	 * Get the red value at the specified offset
	 * 
	 * @param index offset of ther underlying array
	 * @return the red value in range [0-255]
	 * @since 1.5.0
	 */
	int getRed(int index);

	/**
	 * Get the red value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the red value in range [0-255]
	 * @since 1.3.0
	 */
	default int getRed(int x, int y) {
		return getRed(getOffset(x, y));
	}

	/**
	 * Get the red component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the red values
	 * @since 1.3.0
	 */
	int[][] getRed();

	/**
	 * Get the red component of the entire image mapped to a 1d array 
	 * 
	 * @return the red values
	 * @since 1.5.5
	 */
	int[] getRed1D();
	
	/**
	 * Set the red value of the specified pixel
	 * 
	 * @param x      The x coordinate of the images' pixel
	 * @param y      The y coordinate of the images' pixel
	 * @param newRed the new red value in range [0-255]
	 * @since 1.3.0
	 */
	default void setRed(int x, int y, int newRed) {
		setRed(getOffset(x, y), newRed);
	}

	/**
	 * Set the red value at the specified offset
	 * 
	 * @param index  the offset of the underlying array
	 * @param newRed the new red value in range [0-255]
	 * @since 1.5.0
	 */
	void setRed(int index, int newRed);

	/**
	 * Set new red values for the entire picture
	 * 
	 * @param newRed red values in range [0-255]
	 * @since 1.4.5
	 */
	void setRed(int[][] newRed);

	/**
	 * Get the green value of the specified offset
	 * 
	 * @param index the offset of the underlying array
	 * @return the green value in range [0-255]
	 * @since 1.5.0
	 */
	int getGreen(int index);

	/**
	 * Get the green value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the green value in range [0-255]
	 * @since 1.3.0
	 */
	default int getGreen(int x, int y) {
		return getGreen(getOffset(x, y));
	}

	/**
	 * Set the green value of the specified pixel
	 * 
	 * @param x        The x coordinate of the images' pixel
	 * @param y        The y coordinate of the images' pixel
	 * @param newGreen the new green value in range [0-255]
	 * @since 1.3.0
	 */
	default void setGreen(int x, int y, int newGreen) {
		setGreen(getOffset(x, y), newGreen);
	}

	/**
	 * Set the green value at the specified offset
	 * 
	 * @param index    the offset of the underlying array
	 * @param newGreen the new green value in range [0-255]
	 * @since 1.5.0
	 */
	void setGreen(int index, int newGreen);

	/**
	 * Set new green values for the entire picture
	 * 
	 * @param newGreen red values in range [0-255]
	 * @since 1.4.5
	 */
	void setGreen(int[][] newGreen);

	/**
	 * Get the green component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the green values
	 * @since 1.3.0
	 */
	int[][] getGreen();

	/**
	 * Get the green component of the entire image mapped to a 1d array 
	 * 
	 * @return the green values
	 * @since 1.5.5
	 */
	int[] getGreen1D();
	
	/**
	 * Get the blue value of the specified offset
	 * 
	 * @param index the offset of the underlying array
	 * @return the green value in range [0-255]
	 * @since 1.5.0
	 */
	int getBlue(int index);

	/**
	 * Get the blue value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the blue value in range [0-255]
	 * @since 1.3.0
	 */
	default int getBlue(int x, int y) {
		return getBlue(getOffset(x, y));
	}

	/**
	 * Set the blue value of the specified pixel
	 * 
	 * @param x       The x coordinate of the images' pixel
	 * @param y       The y coordinate of the images' pixel
	 * @param newBlue the new blue value in range [0-255]
	 * @since 1.3.0
	 */
	default void setBlue(int x, int y, int newBlue) {
		setBlue(getOffset(x, y), newBlue);
	}

	void setBlue(int index, int newBlue);

	/**
	 * Get the blue component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the image.
	 * 
	 * @return the blue values
	 * @since 1.3.0
	 */
	int[][] getBlue();

	/**
	 * Get the blue component of the entire image mapped to a 1d array 
	 * 
	 * @return the red values
	 * @since 1.5.5
	 */
	int[] getBlue1D();
	
	/**
	 * Set new blue values for the entire picture
	 * 
	 * @param newBlue red values in range [0-255]
	 * @since 1.4.5
	 */
	void setBlue(int[][] newBlue);

	// Derivatives

	/**
	 * Get the average grayscale at the specified offset
	 * 
	 * <p>
	 * Average grayscale: (R+G+B)/3
	 * 
	 * @param index offset of der underlying array
	 * @return the grayscale values in range [0-255]
	 * @since 1.5.0
	 */

	default int getAverageGrayscale(int index) {
		return (getRed(index) + getGreen(index) + getBlue(index)) / 3;
	}

	/**
	 * Get the average grayscale of the specified pixel
	 * 
	 * <p>
	 * Average grayscale: (R+G+B)/3
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the grayscale values in range [0-255]
	 * @since 1.5.0
	 */
	default int getAverageGrayscale(int x, int y) {
		return getAverageGrayscale(getOffset(x, y));
	}

	/**
	 * Get the average grayscale of the entire image mapped to a 2d array
	 * representing the x and y coordinates of the pixel.
	 * 
	 * <p>
	 * Average grayscale: (R+G+B)/3
	 * 
	 * <p>
	 * Average grayscale: (R+G+B)/3
	 * 
	 * @return the grayscale values in range [0 - 255]
	 * @since 1.5.0
	 */
	int[][] getAverageGrayscale();

	/**
	 * Set the gray values at the specified offset
	 * 
	 * <p>
	 * Average grayscale: (R+G+B)/3
	 * 
	 * <p>
	 * It is up to the inheriting class to decide how the gray value is reflected at
	 * the value level. If the image is still in rgb or argb mode the value of each
	 * individual channel will be set to the gray value
	 * 
	 * @param index        offset of der underlaying array
	 * @param newGrayValue to set the pixels to range [0 - 255]
	 * @since 1.5.0
	 */
	default void setAverageGrayscale(int index, int newGrayValue) {
		setGreen(index, newGrayValue);
		setRed(index, newGrayValue);
		setBlue(index, newGrayValue);
	}

	/**
	 * Set the gray values of the specified pixel image.
	 * 
	 * <p>
	 * Average grayscale: (R+G+B)/3
	 * 
	 * <p>
	 * It is up to the inheriting class to decide how the gray value is reflected at
	 * the value level. If the image is still in rgb or argb mode the value of each
	 * individual channel will be set to the gray value
	 * 
	 * @param x            The x coordinate of the images' pixel
	 * @param y            The y coordinate of the images' pixel
	 * @param newGrayValue to set the pixels to range [0 - 255]
	 * @since 1.5.0
	 */
	default void setAverageGrayscale(int x, int y, int newGrayValue) {
		setAverageGrayscale(getOffset(x, y), newGrayValue);
	}

	/**
	 * Set the gray values of the entire image.
	 * 
	 * <p>
	 * Average grayscale: (R+G+B)/3
	 * 
	 * <p>
	 * It is up to the inheriting class to decide how the gray value is reflected at
	 * the value level. If the image is still in rgb or argb mode the value of each
	 * individual channel will be set to the gray value
	 * 
	 * @param newGrayValue to set the pixels to range [0 - 255]
	 * @since 1.5.0
	 */
	void setAverageGrayscale(int[][] newGrayValue);

	// YCrCb
	/**
	 * Return the Y(Luma) component of the YCbCr color model for the specified
	 * offset.
	 * 
	 * @param index of the underlying array
	 * @return the luma component in range [0-255]
	 * @since 1.3.0
	 */
	default int getLuma(int index) {
		int lum = (int) ((getRed(index)) * ColorUtil.LUMA_RED + (getGreen(index)) * ColorUtil.LUMA_GREEN
				+ (getBlue(index)) * ColorUtil.LUMA_BLUE);
		return lum > 255 ? 255 : lum;
	}

	/**
	 * Return the Y(Luma) component of the YCbCr color model for the specified
	 * pixel.
	 * 
	 * @param x the x coordinate of the image
	 * @param y the y coordinate of the image
	 * @return the luma component in range [0-255]
	 * @since 1.3.0
	 */
	default int getLuma(int x, int y) {
		return getLuma(getOffset(x, y));
	}

	/**
	 * Return the Y(Luma) component of the YCbCr color model for the entire image
	 * mapped to a 2d array representing the x and y coordinates of the pixel.
	 * 
	 * @return the luma component in range [0-255]
	 * @since 1.3.1
	 */
	int[][] getLuma();

	/**
	 * Return the Y(Luma) component of the YCbCr color model fof the entire image
	 * mapped to a 1d array
	 * 
	 * @return the luma component in range [0-255]
	 */
	int[] getLuma1D();

	default int getCr(int index) {
		int cr = (int) (getRed(index) * ColorUtil.CR_RED + getGreen(index) * ColorUtil.CR_GREEN
				+ getBlue(index) * ColorUtil.CR_BLUE);
		return cr > 255 ? 255 : cr;
	}

	/**
	 * Return the Cr(red-difference) component of the YCbCr color model for the
	 * specified pixel.
	 * 
	 * @param x the x coordinate of the image
	 * @param y the y coordinate of the image
	 * @return the cr component in range [0-255]
	 * @since 1.3.0
	 */
	default int getCr(int x, int y) {
		return getCr(getOffset(x, y));
	}

	/**
	 * Return the Cb(blue-difference) component of the YCbCr color model for the
	 * specified offset.
	 * 
	 * @param index offset of the underlying array
	 * @return the cb component in range [0-255]
	 * @since 1.5.0
	 */
	default int getCb(int index) {
		int cb = (int) (getRed(index) * ColorUtil.CB_RED + getGreen(index) * ColorUtil.CB_GREEN
				+ getBlue(index) * ColorUtil.CB_BLUE);
		return cb > 255 ? 255 : cb;
	}

	/**
	 * Return the Cb(blue-difference) component of the YCbCr color model for the
	 * specified pixel.
	 * 
	 * @param x the x coordinate of the image
	 * @param y the y coordinate of the image
	 * @return the cb component in range [0-255]
	 * @since 1.3.0
	 */
	default int getCb(int x, int y) {
		return getCb(getOffset(x, y));
	}

	/**
	 * Return the hue component (angle) of the HSV color model for the specified
	 * offset
	 * 
	 * @param index offset of der underlying array
	 * @return the hue component in range [0-360]. As defined the hue is 0 for
	 *         undefined colors (e.g. white or black)
	 * @since 1.5.0
	 */
	default int getHue(int index) {

		int blue = getBlue(index);
		int green = getGreen(index);
		int red = getRed(index);

		int min = Math.min(blue, Math.min(green, red));
		int max = Math.max(blue, Math.max(green, red));

		if (max == min)
			return 0;

		double range = max - min;

		double h;
		if (red == max) {
			h = 60 * ((green - blue) / range);
		} else if (green == max) {
			h = 60 * (2 + (blue - red) / range);
		} else {
			h = 60 * (4 + (red - green) / range);
		}

		int hue = (int) Math.round(h);

		if (hue < 0)
			hue += 360;

		return hue;
	}

	/**
	 * Return the hue component (angle) of the HSV color model for the specified
	 * pixel
	 * 
	 * @param x the x coordinate of the image
	 * @param y the y coordinate of the image
	 * @return the hue component in range [0-360]. As defined the hue is 0 for
	 *         undefined colors (e.g. white or black)
	 * @since 1.3.0
	 */
	default int getHue(int x, int y) {
		return getHue(getOffset(x, y));
	}

	/**
	 * Return the saturation component of the HSV color model for the specified
	 * offset
	 * 
	 * <p>
	 * Note: Opposed to all other values for the hsb model saturation is returned as
	 * double in the range of [0-1] instead of [0-255] to allow for a higher
	 * accuracy.
	 * 
	 * @param index the offset of the underlying array
	 * @return the sat component in range [0-1]. As defined the sat is 0 for
	 *         undefined colors (i.e. black)
	 * @since 1.5.0
	 */
	default double getSat(int index) {
		int blue = getBlue(index);
		int green = getGreen(index);
		int red = getRed(index);
		int max = Math.max(blue, Math.max(green, red));
		if (max == 0) {
			return 0;
		}
		int min = Math.min(blue, Math.min(green, red));
		return ((max - min) / (double) max);
	}

	/**
	 * Return the saturation component of the HSV color model for the specified
	 * pixel
	 * 
	 * <p>
	 * Note: Opposed to all other values for the hsb model saturation is returned as
	 * double in the range of [0-1] instead of [0-255] to allow for a higher
	 * accuracy.
	 * 
	 * @param x the x coordinate of the image
	 * @param y the y coordinate of the image
	 * @return the sat component in range [0-1]. As defined the sat is 0 for
	 *         undefined colors (i.e. black)
	 * @since 1.3.0
	 */
	default double getSat(int x, int y) {
		return getSat(getOffset(x, y));
	}

	/**
	 * Return the value component of the HSV color model for the specified offset
	 * 
	 * @param index offset of the udnerlying array
	 * @return the value component in range [0-255].
	 * @since 1.5.0
	 */
	default int getVal(int index) {
		int blue = getBlue(index);
		int green = getGreen(index);
		int red = getRed(index);
		int max = Math.max(blue, Math.max(green, red));
		return max;
	}

	/**
	 * Return the value component of the HSV color model for the specified pixel
	 * 
	 * @param x the x coordinate of the image
	 * @param y the y coordinate of the image
	 * @return the value component in range [0-255].
	 */
	default int getVal(int x, int y) {
		return getVal(getOffset(x, y));
	}

	/**
	 * Check if an image supports alpha values
	 * 
	 * @return true if the image has an alpha channel. false otherwise
	 */
	boolean hasAlpha();

	/**
	 * Map the x and y values to the underlying one dimensional data array
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the corresponding 1d array index
	 */
	int getOffset(int x, int y);

}