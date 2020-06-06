package com.jstarcraft.dip.color;

import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 颜色像素
 * 
 * @author Birdy
 *
 */
public interface ColorPixel {

    /**
     * Map the x and y values to the underlying one dimensional data array
     * 
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the corresponding 1d array index
     */
    int getIndex(int x, int y);

    int getWidth();

    int getHeight();

    /**
     * Check if an image supports alpha values
     * 
     * @return true if the image has an alpha channel. false otherwise
     */
    boolean hasTransparency();

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
    default int[][] getRgbMatrix() {
        int width = getWidth();
        int height = getHeight();
        int[][] matrix = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                matrix[x][y] = getRgbScalar(x, y);
            }
        }
        return matrix;
    }

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
    default int[][] getTransparencyMatrix() {
        int width = getWidth();
        int height = getHeight();
        int[][] matrix = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                matrix[x][y] = getTransparencyScalar(x, y);
            }
        }
        return matrix;
    }

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
    default void setTransparencyMatrix(int[][] matrix) {
        int width = getWidth();
        int height = getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                setTransparencyScalar(x, y, matrix[x][y]);
            }
        }
    }

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
    default int[][] getRedMatrix() {
        int width = getWidth();
        int height = getHeight();
        int[][] matrix = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                matrix[x][y] = getRedScalar(x, y);
            }
        }
        return matrix;
    }

    /**
     * Get the red component of the entire image mapped to a 1d array
     * 
     * @return the red values
     * @since 1.5.5
     */
    default int[] getRedVector() {
        int width = getWidth();
        int height = getHeight();
        int size = width * height;
        int[] vector = new int[size];
        for (int index = 0; index < size; index++) {
            vector[index] = getRedScalar(index);
        }
        return vector;
    }

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
    default void setRedMatrix(int[][] matrix) {
        int width = getWidth();
        int height = getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                setRedScalar(x, y, matrix[x][y]);
            }
        }
    }

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
    default int[][] getGreenMatrix() {
        int width = getWidth();
        int height = getHeight();
        int[][] matrix = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                matrix[x][y] = getGreenScalar(x, y);
            }
        }
        return matrix;
    }

    /**
     * Get the green component of the entire image mapped to a 1d array
     * 
     * @return the green values
     * @since 1.5.5
     */
    default int[] getGreenVector() {
        int width = getWidth();
        int height = getHeight();
        int size = width * height;
        int[] vector = new int[size];
        for (int index = 0; index < size; index++) {
            vector[index] = getGreenScalar(index);
        }
        return vector;
    }

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
    default void setGreenMatrix(int[][] matrix) {
        int width = getWidth();
        int height = getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                setGreenScalar(x, y, matrix[x][y]);
            }
        }
    }

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
    default int[][] getBlueMatrix() {
        int width = getWidth();
        int height = getHeight();
        int[][] matrix = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                matrix[x][y] = getBlueScalar(x, y);
            }
        }
        return matrix;
    }

    /**
     * Get the blue component of the entire image mapped to a 1d array
     * 
     * @return the red values
     * @since 1.5.5
     */
    default int[] getBlueVector() {
        int width = getWidth();
        int height = getHeight();
        int size = width * height;
        int[] vector = new int[size];
        for (int index = 0; index < size; index++) {
            vector[index] = getBlueScalar(index);
        }
        return vector;
    }

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
    default void setBlueMatrix(int[][] matrix) {
        int width = getWidth();
        int height = getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                setBlueScalar(x, y, matrix[x][y]);
            }
        }
    }

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
    default int[][] getGrayscaleMatrix() {
        int width = getWidth();
        int height = getHeight();
        int[][] matrix = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                matrix[x][y] = getGrayscaleScalar(x, y);
            }
        }
        return matrix;
    }

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
    default void setGrayscaleMatrix(int[][] matrix) {
        int width = getWidth();
        int height = getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                setGrayscaleScalar(x, y, matrix[x][y]);
            }
        }
    }

    // YCrCb
    /**
     * Return the Y(Luma) component of the YCbCr color model for the specified
     * offset.
     * 
     * @param index of the underlying array
     * @return the luma component in range [0-255]
     * @since 1.3.0
     */
    default int getLuminanceScalar(int index) {
        int scalar = (int) ((getRedScalar(index)) * ColorUtility.LUMA_RED + (getGreenScalar(index)) * ColorUtility.LUMA_GREEN + (getBlueScalar(index)) * ColorUtility.LUMA_BLUE);
        return scalar > 255 ? 255 : scalar;
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
    default int getLuminanceScalar(int x, int y) {
        return getLuminanceScalar(getIndex(x, y));
    }

    /**
     * Return the Y(Luma) component of the YCbCr color model for the entire image
     * mapped to a 2d array representing the x and y coordinates of the pixel.
     * 
     * @return the luma component in range [0-255]
     * @since 1.3.1
     */
    default int[][] getLuminanceMatrix() {
        int width = getWidth();
        int height = getHeight();
        int[][] matrix = new int[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                matrix[x][y] = getLuminanceScalar(x, y);
            }
        }
        return matrix;
    }

    /**
     * Return the Y(Luma) component of the YCbCr color model fof the entire image
     * mapped to a 1d array
     * 
     * @return the luma component in range [0-255]
     */
    default int[] getLuminanceVector() {
        int width = getWidth();
        int height = getHeight();
        int size = width * height;
        int[] vector = new int[size];
        for (int index = 0; index < size; index++) {
            vector[index] = getLuminanceScalar(index);
        }
        return vector;
    }

    default int getCrScalar(int index) {
        int cr = (int) (getRedScalar(index) * ColorUtility.CR_RED + getGreenScalar(index) * ColorUtility.CR_GREEN + getBlueScalar(index) * ColorUtility.CR_BLUE);
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
        int cb = (int) (getRedScalar(index) * ColorUtility.CB_RED + getGreenScalar(index) * ColorUtility.CB_GREEN + getBlueScalar(index) * ColorUtility.CB_BLUE);
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
        int maximum = Math.max(blue, Math.max(green, red));
        int minimum = Math.min(blue, Math.min(green, red));

        if (maximum == minimum) {
            return 0;
        }

        float range = maximum - minimum;
        float hue;
        if (red == maximum) {
            hue = 60 * ((green - blue) / range);
        } else if (green == maximum) {
            hue = 60 * (2 + (blue - red) / range);
        } else {
            hue = 60 * (4 + (red - green) / range);
        }

        int scalar = Math.round(hue);
        if (scalar < 0) {
            scalar += 360;
        }
        return scalar;
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
    default float getSaturationScalar(int index) {
        int blue = getBlueScalar(index);
        int green = getGreenScalar(index);
        int red = getRedScalar(index);
        int maximum = Math.max(blue, Math.max(green, red));
        if (maximum == 0) {
            return 0;
        }
        int minimum = Math.min(blue, Math.min(green, red));
        return ((maximum - minimum) / (float) maximum);
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
    default float getSaturationScalar(int x, int y) {
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
     * 将图像转换为像素
     * 
     * @param image
     * @return
     */
    // TODO 考虑迁移到ColorUtilty或者ImageUtility
    public static ColorPixel convert(BufferedImage image) {
        switch (image.getType()) {
        case BufferedImage.TYPE_3BYTE_BGR:
        case BufferedImage.TYPE_4BYTE_ABGR:
            return new BytePixel(image);
        case BufferedImage.TYPE_INT_BGR:
        case BufferedImage.TYPE_INT_ARGB:
        case BufferedImage.TYPE_INT_RGB:
            return new IntegerPixel(image);
        default:
            return new DefaultPixel(image);
        }
    }

}