package com.jstarcraft.dip.color;

/**
 * @author Kilian
 *
 */
public abstract class AbstractPixel implements ColorPixel {

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
	public int getIndex(int x, int y) {
		return (y * width) + x;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean hasTransparency() {
		return transparency;
	}

}