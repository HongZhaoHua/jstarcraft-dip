package com.github.kilianB.graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.github.kilianB.MathUtil;

/**
 * @author Kilian
 *
 */
public class FastPixelInt extends FastPixelImpl {
	
	/** Full alpha constant */
	private static final int FULL_ALPHA = 255 << 24;

	/** True if the underlying image has an alpha component */
	private boolean alpha;

	/** Raw data */
	private int[] imageData;

	private int redMask;
	private int greenMask;
	private int blueMask;
	private int alphaMask;

	private int alphaOffset;
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
	public FastPixelInt(BufferedImage bImage) {

		super(bImage.getWidth(),bImage.getHeight());
		
		imageData = ((DataBufferInt) bImage.getRaster().getDataBuffer()).getData();

		switch (bImage.getType()) {

		case BufferedImage.TYPE_INT_ARGB:
			redMask 	= 0x00ff0000;
			greenMask 	= 0x0000ff00;
			blueMask 	= 0x000000ff;
			alphaMask 	= 0xff000000;
			alpha = true;
			break;
		case BufferedImage.TYPE_INT_RGB:
			redMask 	= 0x00ff0000;
			greenMask 	= 0x0000ff00;
			blueMask 	= 0x000000ff;
			alphaMask 	= 0x00000000;
			break;
		case BufferedImage.TYPE_INT_BGR:
			redMask 	= 0x000000ff;
			greenMask 	= 0x0000ff00;
			blueMask 	= 0x00ff0000;
			alphaMask 	= 0x00000000;
			break;
		}

		redOffset = MathUtil.getLowerShiftBitMask(redMask);
		greenOffset = MathUtil.getLowerShiftBitMask(greenMask);
		blueOffset = MathUtil.getLowerShiftBitMask(blueMask);
		alphaOffset = MathUtil.getLowerShiftBitMask(alphaMask);

	}
	
	@Override
	public int getRGB(int index) {
		return (alpha ? (getAlpha(index) << 24) : FULL_ALPHA) | (getRed(index) << 16)
			| (getGreen(index) << 8) | (getBlue(index));
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
	public int getRGB(int x, int y) {
		return getRGB(getOffset(x,y));
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
	public int[][] getRGB() {
		int[][] rgb = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i++) {
			rgb[x][y] = getRGB(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return rgb;
	}

	public int getAlpha(int index) {
		if (!alpha)
			return -1;
		return (imageData[index] & alphaMask) >>> alphaOffset;
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
	public int getAlpha(int x, int y) {
		return getAlpha(getOffset(x,y));
	}

	/**
	 * Get the alpha component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the alpha values or null if alpha is not supported
	 * @since 1.3.0
	 */
	@Override
	public int[][] getAlpha() {
		if (!alpha)
			return null;
		int[][] alpha = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i++) {
			alpha[x][y] = getAlpha(i);
			x++;
			if (x >= width) {
				x = 0;
				y++;
			}
		}
		return alpha;
	}
	


	@Override
	public void setAlpha(int index, int newAlpha) {
		if (!alpha)
			return;
		imageData[index] |= (newAlpha << alphaOffset);
	}
	
	/**
	 * Set the alpha value of the specified pixel. This method is a NOP if alpha is
	 * not supported.
	 * 
	 * @param x        The x coordinate of the images' pixel
	 * @param y        The y coordinate of the images' pixel
	 * @param newAlpha the new alpha value in range [0-255]
	 * @since 1.3.0
	 */
	@Override
	public void setAlpha(int x, int y, int newAlpha) {
		setAlpha(getOffset(x,y), newAlpha);
	}

	/**
	 * Set new alpha values for the entire picture
	 * 
	 * @param newAlpha red values in range [0-255]
	 * @since 1.4.5
	 */
	@Override
	public void setAlpha(int[][] newAlpha) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setAlpha(x, y, newAlpha[x][y]);
			}
		}
	}

	@Override
	public int getRed(int index) {
		return (imageData[index] & redMask) >>> redOffset;
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
	public int getRed(int x, int y) {
		return getRed(getOffset(x, y));
	}

	/**
	 * Get the red component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the red values
	 * @since 1.3.0
	 */
	@Override
	public int[][] getRed() {
		int[][] red = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i++) {
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
		//Clear red part first	
		imageData[index] = (imageData[index] & (~redMask)) | (newRed << redOffset);
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
	public void setRed(int x, int y, int newRed) {
		setRed(getOffset(x,y),newRed);
	}

	/**
	 * Set new red values for the entire picture
	 * 
	 * @param newRed red values in range [0-255]
	 * @since 1.4.5
	 */
	@Override
	public void setRed(int[][] newRed) {
		// TODO inline method call?
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setRed(x, y, newRed[x][y]);
			}
		}
	}

	@Override
	public int getGreen(int index) {
		return (imageData[index] & greenMask) >>> greenOffset;
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
	public int getGreen(int x, int y) {
		return getGreen(getOffset(x, y));
	}

	@Override
	public void setGreen(int index, int newGreen) {
		//Clear green part first	
		imageData[index] = (imageData[index] & (~greenMask)) | (newGreen << greenOffset);
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
	public void setGreen(int x, int y, int newGreen) {
		setGreen(getOffset(x,y),newGreen);
	}

	/**
	 * Set new green values for the entire picture
	 * 
	 * @param newGreen red values in range [0-255]
	 * @since 1.4.5
	 */
	@Override
	public void setGreen(int[][] newGreen) {
		// TODO inline method call?
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setGreen(x, y, newGreen[x][y]);
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
	public int[][] getGreen() {
		int[][] green = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i++) {
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
		return (imageData[index] & blueMask) >>> blueOffset;
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
	public int getBlue(int x, int y) {
		return getBlue(getOffset(x, y));
	}

	@Override
	public void setBlue(int index, int newBlue) {
		imageData[index] = (imageData[index] & (~blueMask)) | (newBlue << blueOffset);
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
	public void setBlue(int x, int y, int newBlue) {
		setBlue(getOffset(x,y),newBlue);
	}

	/**
	 * Get the blue component of the entire image mapped to a 2d array representing
	 * the x and y coordinates of the pixel.
	 * 
	 * @return the blue values
	 * @since 1.3.0
	 */
	@Override
	public int[][] getBlue() {
		int[][] blue = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i++) {
			blue[x][y] = getBlue(i);
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
	public void setBlue(int[][] newBlue) {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setBlue(x, y, newBlue[x][y]);
			}
		}
	}
	
	// grayscale
	

	@Override
	public int[][] getAverageGrayscale() {
		int[][] gray = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i++) {
			gray[x][y] = getAverageGrayscale(x,y);
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
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				setAverageGrayscale(x, y, newGrayValue[x][y]);
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
	public int[][] getLuma() {
		int luma[][] = new int[width][height];
		int x = 0;
		int y = 0;
		for (int i = 0; i < imageData.length; i++) {
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
		int luma[] = new int[width * height];
		for (int i = 0; i < imageData.length; i++) {
			luma[i] = getLuma(i);
		}
		return luma;
	}


	public int getOffset(int x, int y) {
		return (y * width) + x;
	}

	@Override
	public boolean hasAlpha() {
		return alpha;
	}

	
}
