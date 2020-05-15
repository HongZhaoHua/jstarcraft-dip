package com.github.kilianB.graphics;

import com.github.kilianB.ArrayUtil;

/**
 * @author Kilian
 *
 */
public abstract class FastPixelImpl  implements FastPixel{

	/** Width of the image */
	protected final int width;
	/** Height of the image */
	protected final int height;

	public FastPixelImpl(int width,int height) {
		this.width = width;
		this.height = height;
	}
	
	@Override
	public int[] getRed1D() {
		int[] red = new int[width*height];
		ArrayUtil.fillArray(red,i -> {return getRed(i);});
		return red;
	}

	@Override
	public int[] getBlue1D() {
		int[] blue = new int[width*height];
		ArrayUtil.fillArray(blue,i -> {return getBlue(i);});
		return blue;
	}
	
	@Override
	public int[] getGreen1D() {
		int[] green = new int[width*height];
		ArrayUtil.fillArray(green,i -> {return getGreen(i);});
		return green;
	}

	
}