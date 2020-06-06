package com.jstarcraft.dip.color;

/**
 * 抽象像素
 * 
 * @author Birdy
 *
 */
public abstract class AbstractPixel implements ColorPixel {

    /** 图像宽度 */
    protected final int width;

    /** 图像高度 */
    protected final int height;

    /** 是否支持透明度 */
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