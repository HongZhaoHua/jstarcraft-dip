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
public interface Pixel {

	static final Logger LOGGER = Logger.getLogger(Pixel.class.getSimpleName());

	/**
	 * Check if an image supports alpha values
	 * 
	 * @return true if the image has an alpha channel. false otherwise
	 */
	boolean hasTransparency();

	/**
	 * Map the x and y values to the underlying one dimensional data array
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return the corresponding 1d array index
	 */
	int getIndex(int x, int y);

	int getRgbScalar(int index);

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
	default int getRgbScalar(int x, int y) {
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
	int[][] getRgbMatrix();

	/**
	 * Get the alpha value of the specified pixel
	 * 
	 * @param index the offset in the underlying array
	 * @return the alpha value in range [0-255] or -1 if alpha is not supported
	 * @since 1.5.0
	 */
	int getTransparencyScalar(int index);

	/**
	 * Get the alpha value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the alpha value in range [0-255] or -1 if alpha is not supported
	 * @since 1.3.0
	 */
	default int getTransparencyScalar(int x, int y) {
		return getTransparencyScalar(getIndex(x, y));
	}

	/**
	 * Get the alpha component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the alpha values or null if alpha is not supported
	 * @since 1.3.0
	 */
	int[][] getTransparencyMatrix();

	/**
	 * Set the alpha value. This method is a NOP if alpha is not supported.
	 * 
	 * @param index  the offset of the underlying array
	 * @param scalar the new alpha value in range [0-255]
	 * @since 1.5.0
	 */
	void setTransparencyScalar(int index, int scalar);

	/**
	 * Set the alpha value of the specified pixel. This method is a NOP if alpha is
	 * not supported.
	 * 
	 * @param x      The x coordinate of the images' pixel
	 * @param y      The y coordinate of the images' pixel
	 * @param scalar the new alpha value in range [0-255]
	 * @since 1.3.0
	 */
	default void setTransparencyScalar(int x, int y, int scalar) {
		setTransparencyScalar(getIndex(x, y), scalar);
	}

	/**
	 * Set new alpha values for the entire picture
	 * 
	 * @param matrix red values in range [0-255]
	 * @since 1.4.5
	 */
	void setTransparencyMatrix(int[][] matrix);

	/**
	 * Get the red value at the specified offset
	 * 
	 * @param index offset of ther underlying array
	 * @return the red value in range [0-255]
	 * @since 1.5.0
	 */
	int getRedScalar(int index);

	/**
	 * Get the red value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the red value in range [0-255]
	 * @since 1.3.0
	 */
	default int getRedScalar(int x, int y) {
		return getRedScalar(getIndex(x, y));
	}

	/**
	 * Get the red component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the red values
	 * @since 1.3.0
	 */
	int[][] getRedMatrix();

	/**
	 * Get the red component of the entire image mapped to a 1d array
	 * 
	 * @return the red values
	 * @since 1.5.5
	 */
	int[] getRedVector();

	/**
	 * Set the red value at the specified offset
	 * 
	 * @param index  the offset of the underlying array
	 * @param scalar the new red value in range [0-255]
	 * @since 1.5.0
	 */
	void setRedScalar(int index, int scalar);

	/**
	 * Set the red value of the specified pixel
	 * 
	 * @param x      The x coordinate of the images' pixel
	 * @param y      The y coordinate of the images' pixel
	 * @param scalar the new red value in range [0-255]
	 * @since 1.3.0
	 */
	default void setRedScalar(int x, int y, int scalar) {
		setRedScalar(getIndex(x, y), scalar);
	}

	/**
	 * Set new red values for the entire picture
	 * 
	 * @param matrix red values in range [0-255]
	 * @since 1.4.5
	 */
	void setRedMatrix(int[][] matrix);

	/**
	 * Get the green value of the specified offset
	 * 
	 * @param index the offset of the underlying array
	 * @return the green value in range [0-255]
	 * @since 1.5.0
	 */
	int getGreenScalar(int index);

	/**
	 * Get the green value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the green value in range [0-255]
	 * @since 1.3.0
	 */
	default int getGreenScalar(int x, int y) {
		return getGreenScalar(getIndex(x, y));
	}

	/**
	 * Get the green component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the green values
	 * @since 1.3.0
	 */
	int[][] getGreenMatrix();

	/**
	 * Set the green value at the specified offset
	 * 
	 * @param index  the offset of the underlying array
	 * @param scalar the new green value in range [0-255]
	 * @since 1.5.0
	 */
	void setGreenScalar(int index, int scalar);

	/**
	 * Set the green value of the specified pixel
	 * 
	 * @param x      The x coordinate of the images' pixel
	 * @param y      The y coordinate of the images' pixel
	 * @param scalar the new green value in range [0-255]
	 * @since 1.3.0
	 */
	default void setGreenScalar(int x, int y, int scalar) {
		setGreenScalar(getIndex(x, y), scalar);
	}

	/**
	 * Set new green values for the entire picture
	 * 
	 * @param matrix red values in range [0-255]
	 * @since 1.4.5
	 */
	void setGreenMatrix(int[][] matrix);

	/**
	 * Get the green component of the entire image mapped to a 1d array
	 * 
	 * @return the green values
	 * @since 1.5.5
	 */
	int[] getGreenVector();

	/**
	 * Get the blue value of the specified offset
	 * 
	 * @param index the offset of the underlying array
	 * @return the green value in range [0-255]
	 * @since 1.5.0
	 */
	int getBlueScalar(int index);

	/**
	 * Get the blue value of the specified pixel
	 * 
	 * @param x The x coordinate of the images' pixel
	 * @param y The y coordinate of the images' pixel
	 * @return the blue value in range [0-255]
	 * @since 1.3.0
	 */
	default int getBlueScalar(int x, int y) {
		return getBlueScalar(getIndex(x, y));
	}

	/**
	 * Get the blue component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the image.
	 * 
	 * @return the blue values
	 * @since 1.3.0
	 */
	int[][] getBlueMatrix();

	/**
	 * Get the blue component of the entire image mapped to a 1d array
	 * 
	 * @return the red values
	 * @since 1.5.5
	 */
	int[] getBlueVector();

	void setBlueScalar(int index, int scalar);

	/**
	 * Set the blue value of the specified pixel
	 * 
	 * @param x      The x coordinate of the images' pixel
	 * @param y      The y coordinate of the images' pixel
	 * @param scalar the new blue value in range [0-255]
	 * @since 1.3.0
	 */
	default void setBlueScalar(int x, int y, int scalar) {
		setBlueScalar(getIndex(x, y), scalar);
	}

	/**
	 * Set new blue values for the entire picture
	 * 
	 * @param matrix red values in range [0-255]
	 * @since 1.4.5
	 */
	void setBlueMatrix(int[][] matrix);

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

	default int getGrayscaleScalar(int index) {
		return (getRedScalar(index) + getGreenScalar(index) + getBlueScalar(index)) / 3;
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
	default int getGrayscaleScalar(int x, int y) {
		return getGrayscaleScalar(getIndex(x, y));
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
	int[][] getGrayscaleMatrix();

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
	 * @param index  offset of der underlaying array
	 * @param scalar to set the pixels to range [0 - 255]
	 * @since 1.5.0
	 */
	default void setGrayscaleScalar(int index, int scalar) {
		setGreenScalar(index, scalar);
		setRedScalar(index, scalar);
		setBlueScalar(index, scalar);
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
	 * @param x      The x coordinate of the images' pixel
	 * @param y      The y coordinate of the images' pixel
	 * @param scalar to set the pixels to range [0 - 255]
	 * @since 1.5.0
	 */
	default void setGrayscaleScalar(int x, int y, int scalar) {
		setGrayscaleScalar(getIndex(x, y), scalar);
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
	 * @param matrix to set the pixels to range [0 - 255]
	 * @since 1.5.0
	 */
	void setGrayscaleMatrix(int[][] matrix);

	// YCrCb
	/**
	 * Return the Y(Luma) component of the YCbCr color model for the specified
	 * offset.
	 * 
	 * @param index of the underlying array
	 * @return the luma component in range [0-255]
	 * @since 1.3.0
	 */
	default int getLumaScalar(int index) {
		int lum = (int) ((getRedScalar(index)) * ColorUtil.LUMA_RED + (getGreenScalar(index)) * ColorUtil.LUMA_GREEN + (getBlueScalar(index)) * ColorUtil.LUMA_BLUE);
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
	default int getLumaScalar(int x, int y) {
		return getLumaScalar(getIndex(x, y));
	}

	/**
	 * Return the Y(Luma) component of the YCbCr color model for the entire image
	 * mapped to a 2d array representing the x and y coordinates of the pixel.
	 * 
	 * @return the luma component in range [0-255]
	 * @since 1.3.1
	 */
	int[][] getLumaMatrix();

	/**
	 * Return the Y(Luma) component of the YCbCr color model fof the entire image
	 * mapped to a 1d array
	 * 
	 * @return the luma component in range [0-255]
	 */
	int[] getLumaVector();

	default int getCrScalar(int index) {
		int cr = (int) (getRedScalar(index) * ColorUtil.CR_RED + getGreenScalar(index) * ColorUtil.CR_GREEN + getBlueScalar(index) * ColorUtil.CR_BLUE);
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
	default int getCrScalar(int x, int y) {
		return getCrScalar(getIndex(x, y));
	}

	/**
	 * Return the Cb(blue-difference) component of the YCbCr color model for the
	 * specified offset.
	 * 
	 * @param index offset of the underlying array
	 * @return the cb component in range [0-255]
	 * @since 1.5.0
	 */
	default int getCbScalar(int index) {
		int cb = (int) (getRedScalar(index) * ColorUtil.CB_RED + getGreenScalar(index) * ColorUtil.CB_GREEN + getBlueScalar(index) * ColorUtil.CB_BLUE);
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
	default int getCbScalar(int x, int y) {
		return getCbScalar(getIndex(x, y));
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
	default int getHueScalar(int index) {

		int blue = getBlueScalar(index);
		int green = getGreenScalar(index);
		int red = getRedScalar(index);

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
	default int getHueScalar(int x, int y) {
		return getHueScalar(getIndex(x, y));
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
	default double getSaturationScalar(int index) {
		int blue = getBlueScalar(index);
		int green = getGreenScalar(index);
		int red = getRedScalar(index);
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
	default double getSaturationScalar(int x, int y) {
		return getSaturationScalar(getIndex(x, y));
	}

	/**
	 * Return the value component of the HSV color model for the specified offset
	 * 
	 * @param index offset of the udnerlying array
	 * @return the value component in range [0-255].
	 * @since 1.5.0
	 */
	default int getBrightnessScalar(int index) {
		int blue = getBlueScalar(index);
		int green = getGreenScalar(index);
		int red = getRedScalar(index);
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
	default int getBrightnessScalar(int x, int y) {
		return getBrightnessScalar(getIndex(x, y));
	}

	/**
	 * Return a fast pixel instance mapped to the buffered image type
	 * 
	 * @param bufferedImage the buffered image to create a fast pixel instance for
	 * @return an instantiated FastPixelObject
	 */
	public static Pixel create(BufferedImage bufferedImage) {

		switch (bufferedImage.getType()) {

		case BufferedImage.TYPE_3BYTE_BGR:
		case BufferedImage.TYPE_4BYTE_ABGR:
			return new BytePixel(bufferedImage);
		case BufferedImage.TYPE_INT_BGR:
		case BufferedImage.TYPE_INT_ARGB:
		case BufferedImage.TYPE_INT_RGB:
			return new IntegerPixel(bufferedImage);
		default:
			LOGGER.info("No fast implementation available for " + bufferedImage.getType() + ". Fallback to slow default variant.");
			return new DefaultPixel(bufferedImage);
//			throw new UnsupportedOperationException(
//					"The image type is currently not supported: " + bufferedImage.getType());
//		
		}
	}

}