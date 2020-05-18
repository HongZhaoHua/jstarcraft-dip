package com.github.kilianB.hashAlgorithms;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.util.Objects;

import com.github.kilianB.hash.Hash;
import com.jstarcraft.dip.color.ColorPixel;

/**
 * Calculates a hash based on gradient tracking. This hash is cheap to compute
 * and provides a high degree of accuracy. Robust to a huge range of color
 * transformation
 * 
 * @author Kilian
 * @since 1.0.0
 */
public class DifferenceHash extends HashingAlgorithm {

	/**
	 * Algorithm precision used during calculation.
	 * 
	 * <p>
	 * <b>implnote:</b> Be aware that changing the enum names will alter the
	 * algorithm id rendering generated keys unusable
	 * 
	 * @author Kilian
	 *
	 */
	public enum Precision {
		/** left to right gradient */
		Horizontal,
		/** top to bottom gradient */
		Vertical,
		/** diagonally gradient */
		Diagonal
	}

	/**
	 * Precision used to calculate the hash
	 */
	private final Precision precision;

	/**
	 * 
	 * Create a difference hasher with the given settings. The bit resolution always
	 * corresponds to the simple precision value and will increase accordingly
	 * depending on the precision chosen.
	 * 
	 * <p>
	 * Tests have shown that a 64 bit simple precision hash usually performs better
	 * than a 32 bit double precision hash.
	 * 
	 * @param bitResolution The bit resolution specifies the final length of the
	 *                      generated hash. A higher resolution will increase
	 *                      computation time and space requirement while being able
	 *                      to track finer detail in the image. <b>Be aware that a
	 *                      high resolution is not always desired.</b> The bit
	 *                      resolution is only an <b>approximation</b> of the final
	 *                      hash length.
	 * @param precision     Algorithm precision. Allowed Values:
	 *                      <dl>
	 *                      <dt>Simple:</dt>
	 *                      <dd>Calculates top - bottom gradient</dd>
	 *                      <dt>Double:</dt>
	 *                      <dd>Additionally computes left - right gradient (doubles
	 *                      key length)</dd>
	 *                      <dt>Tripple:</dt>
	 *                      <dd>Additionally computes diagonal gradient (triples key
	 *                      length)</dd>
	 *                      </dl>
	 */
	public DifferenceHash(int bitResolution, Precision precision) {
		super(bitResolution);

		computeDimensions(bitResolution);

		this.precision = precision;
	}

	@Override
	protected BigInteger hash(ColorPixel pixel, HashBuilder hash) {
		// Use data buffer for faster access

		int[][] lum = pixel.getLuminanceMatrix();

		// Calculate the left to right gradient
		switch (precision) {
		case Horizontal: {
			for (int x = 1; x < width; x++) {
				for (int y = 0; y < height; y++) {
					if (lum[x][y] < lum[x - 1][y]) {
						hash.prependOne();
					} else {
						hash.prependZero();
					}
				}
			}
			break;
		}
		case Vertical: {
			// Top to bottom gradient
			// We need a padding row at the top now.
			// Caution width and height are swapped
			for (int x = 0; x < width; x++) {
				for (int y = 1; y < height; y++) {
					if (lum[x][y] < lum[x][y - 1]) {
						hash.prependOne();
					} else {
						hash.prependZero();
					}
				}
			}
			break;
		}
		case Diagonal: {
			// Diagonally hash
			for (int x = 1; x < width; x++) {
				for (int y = 1; y < height; y++) {
					if (lum[x][y] < lum[x - 1][y - 1]) {
						hash.prependOne();
					} else {
						hash.prependZero();
					}
				}
			}
			break;
		}
		}
		return hash.toBigInteger();
	}

	/**
	 * Compute the dimension for the resize operation. We want to get to close to a
	 * quadratic images as possible to counteract scaling bias.
	 * 
	 * @param bitResolution the desired resolution
	 */
	private void computeDimensions(int bitResolution) {
		int dimension = (int) Math.round(Math.sqrt(bitResolution + 1));

		// width //height
		int normalBound = (dimension - 1) * (dimension);
		int higherBound = (dimension - 1) * (dimension + 1);

		this.width = dimension;
		this.height = dimension;

		if (higherBound < bitResolution) {
			this.width++;
			this.height++;
		} else {
			if (normalBound < bitResolution || (normalBound - bitResolution) > (higherBound - bitResolution)) {
				this.height++;
			}
		}

	}

	@Override
	protected int precomputeAlgoId() {
		// + 1 to ensure id is incompatible to earlier version
		return Objects.hash(getClass().getName(), height, width, this.precision.name()) * 31 + 1;
	}

	/*
	 * Difference hash requires a little bit different handling when converting the
	 * hash to an image.
	 */
	@Override
	public Hash hash(BufferedImage image) {
		return new DHash(super.hash(image), this.precision, width, height);
	}

	@Override
	public Hash createAlgorithmSpecificHash(Hash original) {
		return new DHash(original, this.precision, width, height);
	}

	/**
	 * An extended hash class allowing dhashes to be visually represented.
	 * 
	 * @author Kilian
	 * @since 3.0.0
	 */
	public static class DHash extends Hash {

		private Precision precision;
		private int width;
		private int height;

		public DHash(Hash h, Precision precision, int width, int height) {
			super(h.getHashValue(), h.getBitResolution(), h.getAlgorithmId());
			this.precision = precision;
			this.width = width;
			this.height = height;
		}

		public BufferedImage toImage(int blockSize) {

			Color[] colorArr = new Color[] { Color.WHITE, Color.BLACK };
			int[] colorIndex = new int[hashLength];

			for (int i = 0; i < hashLength; i++) {
				colorIndex[i] = hashValue.testBit(i) ? 1 : 0;
			}
			return toImage(colorIndex, colorArr, blockSize);
		}

		public BufferedImage toImage(int[] bitColorIndex, Color[] colors, int blockSize) {
			BufferedImage bi = new BufferedImage(blockSize * width, blockSize * height, BufferedImage.TYPE_3BYTE_BGR);
			ColorPixel fp = ColorPixel.create(bi);

			switch (precision) {
			case Horizontal: {
				drawDoublePrecision(fp, width, 1, height, 0, blockSize, bitColorIndex, colors);
				break;
			}
			case Vertical: {
				drawDoublePrecision(fp, width, 0, height, 1, blockSize, bitColorIndex, colors);
				break;
			}
			case Diagonal: {
				drawDoublePrecision(fp, width, 1, height, 1, blockSize, bitColorIndex, colors);
				break;
			}
			}
			return bi;
		}

		private void drawDoublePrecision(ColorPixel writer, int width, int wOffset, int height, int hOffset, int blockSize, int[] bitColorIndex, Color[] colors) {
			int i = 0;
			for (int w = 0; w < (width - wOffset) * blockSize; w = w + blockSize) {
				for (int h = 0; h < (height - hOffset) * blockSize; h = h + blockSize) {
					Color c = colors[bitColorIndex[i++]];
					int red = c.getRed();
					int green = c.getGreen();
					int blue = c.getBlue();

					for (int m = 0; m < blockSize; m++) {
						for (int n = 0; n < blockSize; n++) {
							int x = w + m;
							int y = h + n;
							// bi.setRGB(y, x, bit ? black : white);
							writer.setRedScalar(x, y, red);
							writer.setGreenScalar(x, y, green);
							writer.setBlueScalar(x, y, blue);
						}
					}
				}
			}
		}
	}

	public Precision getPrecision() {
		return precision;
	}
}
