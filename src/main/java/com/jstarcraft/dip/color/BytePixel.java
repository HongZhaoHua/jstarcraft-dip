package com.jstarcraft.dip.color;

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
	private static final int TRANSPARENCY_MASK = 255 << 24;

	/** Offset used in case alpha is present */
	private final int transparencyOffset;
	/** Bytes used to represent a single pixel */
	private final int rgbOffset;

	/** Raw data */
	private final byte[] pixelData;

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
		pixelData = ((DataBufferByte) bImage.getRaster().getDataBuffer()).getData();

		if (bImage.getColorModel().hasAlpha()) {
			transparencyOffset = 1;
			rgbOffset = 4;
		} else {
			transparencyOffset = 0;
			rgbOffset = 3;
		}
	}

	@Override
	public int getRgbScalar(int index) {
		index = index * rgbOffset;
		return (hasTransparency() ? (pixelData[index++] & 0xFF) << 24 : TRANSPARENCY_MASK) | ((pixelData[index++] & 0xFF)) | ((pixelData[index++] & 0xFF) << 8) | ((pixelData[index++] & 0xFF) << 16);
	}

	@Override
	public int getTransparencyScalar(int index) {
		if (!hasTransparency())
			return -1;
		index = index * rgbOffset;
		return pixelData[index] & 0xFF;
	}

	@Override
	public void setTransparencyScalar(int index, int transparency) {
		if (!hasTransparency())
			return;
		index = index * rgbOffset;
		pixelData[index] = (byte) (transparency);
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
		index = index * rgbOffset;
		return pixelData[index + transparencyOffset + 2] & 0xFF;
	}

	@Override
	public void setRedScalar(int index, int scalar) {
		index = index * rgbOffset;
		pixelData[index + transparencyOffset + 2] = (byte) (scalar);
	}

	@Override
	public int getGreenScalar(int index) {
		index = index * rgbOffset;
		return pixelData[index + transparencyOffset + 1] & 0xFF;
	}

	@Override
	public void setGreenScalar(int index, int scalar) {
		index = index * rgbOffset;
		pixelData[index + transparencyOffset + 1] = (byte) (scalar);
	}

	@Override
	public int getBlueScalar(int index) {
		index = index * rgbOffset;
		return pixelData[index + transparencyOffset] & 0xFF;
	}

	@Override
	public void setBlueScalar(int index, int scalar) {
		index = index * rgbOffset;
		pixelData[index + transparencyOffset] = (byte) (scalar);
	}

}