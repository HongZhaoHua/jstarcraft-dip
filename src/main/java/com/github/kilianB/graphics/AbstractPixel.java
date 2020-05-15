package com.github.kilianB.graphics;

import com.github.kilianB.ArrayUtil;

/**
 * @author Kilian
 *
 */
public abstract class AbstractPixel implements Pixel {

	/** Width of the image */
	protected final int width;
	/** Height of the image */
	protected final int height;

	/** True if the underlying image has an alpha component */
	private final boolean transparency;

	public AbstractPixel(int width, int height, boolean transparency) {
		this.width = width;
		this.height = height;
		this.transparency = transparency;
	}

	@Override
	public int[] getRedVector() {
		int[] red = new int[width * height];
		ArrayUtil.fillArray(red, i -> {
			return getRedScalar(i);
		});
		return red;
	}

	@Override
	public int[] getBlueVector() {
		int[] blue = new int[width * height];
		ArrayUtil.fillArray(blue, i -> {
			return getBlueScalar(i);
		});
		return blue;
	}

	@Override
	public int[] getGreenVector() {
		int[] green = new int[width * height];
		ArrayUtil.fillArray(green, i -> {
			return getGreenScalar(i);
		});
		return green;
	}

	@Override
	public boolean hasTransparency() {
		return transparency;
	}

}