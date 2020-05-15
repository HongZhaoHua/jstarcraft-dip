package com.github.kilianB.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.util.HashMap;

import javax.imageio.ImageTypeSpecifier;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 * @author Kilian
 *
 */
public class ImageUtil {

	/**
	 * Resize the buffered image to an arbitrary dimension
	 * 
	 * <p>
	 * If a high quality interpolated thumbnail is required
	 * {@link #createThumbnail(BufferedImage, int, int)} may be more suited.
	 * 
	 * 
	 * @param source the source image
	 * @param width  the new width
	 * @param height the new height
	 * @return the resized image
	 * @since 1.0.0
	 * @since 1.4.2 fixed not using awt rescale
	 * @since 1.5.3 fixed using ImageTypeSpecifier to create compatible images for custom type
	 */
	public static BufferedImage getScaledInstance(BufferedImage source, int width, int height) {
		
		BufferedImage target = ImageTypeSpecifier.createFromRenderedImage(source).createBufferedImage(width,height);
		
		Graphics g = target.getGraphics();
		g.drawImage(source, 0, 0, width, height, null);
		g.dispose();
		return target;
	}

	/**
	 * <p>
	 * Returns a thumbnail of a source image.
	 * </p>
	 * 
	 * TODO LGPL LICENSE!!
	 * 
	 * @param image     the source image
	 * @param newWidth  the width of the thumbnail
	 * @param newHeight the height of the thumbnail
	 * @return a new compatible <code>BufferedImage</code> containing a thumbnail of
	 *         <code>image</code>
	 * @throws IllegalArgumentException if <code>newWidth</code> is larger than the
	 *                                  width of <code>image</code> or if
	 *                                  <code>newHeight</code> is larger than the
	 *                                  height of
	 *                                  <code>image or if one the dimensions is not &gt; 0</code> @
	 *                                  since 1.5.0
	 */
	public static BufferedImage createThumbnail(BufferedImage image, int newWidth, int newHeight) {
		int width = image.getWidth();
		int height = image.getHeight();

		boolean isTranslucent = true;// image.getTransparency() != Transparency.OPAQUE;

		if (newWidth >= width || newHeight >= height) {
			throw new IllegalArgumentException(
					"newWidth and newHeight cannot" + " be greater than the image" + " dimensions");
		} else if (newWidth <= 0 || newHeight <= 0) {
			throw new IllegalArgumentException("newWidth and newHeight must" + " be greater than 0");
		}

		BufferedImage thumb = image;
		BufferedImage temp = null;

		Graphics2D g2 = null;

		try {
			int previousWidth = width;
			int previousHeight = height;

			do {
				if (width > newWidth) {
					width /= 2;
					if (width < newWidth) {
						width = newWidth;
					}
				}

				if (height > newHeight) {
					height /= 2;
					if (height < newHeight) {
						height = newHeight;
					}
				}

				if (temp == null || isTranslucent) {
					if (g2 != null) {
						// do not need to wrap with finally
						// outer finally block will ensure
						// that resources are properly reclaimed
						g2.dispose();
					}
					temp = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
					g2 = temp.createGraphics();
					g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				}
				g2.drawImage(thumb, 0, 0, width, height, 0, 0, previousWidth, previousHeight, null);

				previousWidth = width;
				previousHeight = height;

				thumb = temp;
			} while (width != newWidth || height != newHeight);
		} finally {
			if (g2 != null) {
				g2.dispose();
			}
		}

		if (width != thumb.getWidth() || height != thumb.getHeight()) {
			temp = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			g2 = temp.createGraphics();

			try {
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				g2.drawImage(thumb, 0, 0, width, height, 0, 0, width, height, null);
			} finally {
				g2.dispose();
			}

			thumb = temp;
		}

		return thumb;
	}

	/**
	 * Convert the image to an image of the new type
	 * 
	 * @param original the original image to convert
	 * @param newType  the new type
	 * @return a copy of the original image with new pixel type
	 */
	public static BufferedImage toNewType(BufferedImage original, int newType) {
		BufferedImage bi = new BufferedImage(original.getWidth(), original.getHeight(), newType);
		Graphics g = bi.getGraphics();
		g.drawImage(original, 0, 0, null);
		g.dispose();
		return bi;
	}

	/**
	 * Convert the image to an image of the new type
	 * 
	 * @param original the original image to convert
	 * @param newType  the new type
	 * @return a copy of the original image with new pixel type
	 */
	public static BufferedImage toNewType(BufferedImage original, BImageType newType) {
		return toNewType(original, newType.type);
	}

	/**
	 * Calculate the interpolated average color of the image
	 * 
	 * @param image the source image
	 * @return the average color of the image
	 * @since 1.0.0
	 */
	public static Color interpolateColor(Image image) {
		BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
		bImage = ImageUtil.getScaledInstance(bImage, 1, 1);
		int argb = bImage.getRGB(0, 0);
		return ColorUtil.argbToFXColor(argb);
	}

	/**
	 * Return the dominant color of this image. The dominant color is the color that
	 * most often occurred in this image. Be aware that calculating the average
	 * color using this approach is a rather expensive operation.
	 * <p>
	 * 
	 * @param image The source image
	 * @return the dominant color of this image
	 * @since 1.0.0
	 */
	public static Color dominantColor(Image image) {

		HashMap<Integer, Integer> colorCount = new HashMap<>();

		PixelReader pr = image.getPixelReader();

		int[] pixels = new int[(int) (image.getWidth() * image.getHeight())];

		pr.getPixels(0, 0, (int) image.getWidth(), (int) image.getHeight(), PixelFormat.getIntArgbInstance(), pixels, 0,
				(int) image.getWidth());

		for (int argb : pixels) {
			// Do we even need to put it back in? Can't be simply change the Integer object?
			Integer curValue = colorCount.get(argb);
			colorCount.put(argb, curValue == null ? 1 : curValue.intValue() + 1);
		}

		int argb = colorCount.entrySet().stream().max((entry, entry2) -> entry.getValue().compareTo(entry2.getValue()))
				.get().getKey().intValue();

		return ColorUtil.argbToFXColor(argb);
	}

	/**
	 * Calculate the average color of this image. The average color is determined by
	 * summing the squared argb component of each pixel and determining the mean
	 * value of these.
	 * 
	 * @param image The source image
	 * @return The average mean color of this image
	 * @since 1.0.0
	 */
	public static Color meanColor(Image image) {

		PixelReader pr = image.getPixelReader();
		int[] pixels = new int[(int) (image.getWidth() * image.getHeight())];

		pr.getPixels(0, 0, (int) image.getWidth(), (int) image.getHeight(), PixelFormat.getIntArgbInstance(), pixels, 0,
				(int) image.getWidth());

		double meanAlpha = 0;
		double meanRed = 0;
		double meanBlue = 0;
		double meanGreen = 0;

		final int pixelCount = pixels.length;

		for (int argb : pixels) {

			// ARGB values are not linearly scaled. therefore square roots are necessary.
			int[] colorComponents = ColorUtil.argbToComponents(argb);

			meanAlpha += (Math.pow(colorComponents[0], 2) / pixelCount);
			meanRed += (Math.pow(colorComponents[1], 2) / pixelCount);
			meanGreen += (Math.pow(colorComponents[2], 2) / pixelCount);
			meanBlue += (Math.pow(colorComponents[3], 2) / pixelCount);
		}

		int argbMean = ColorUtil.componentsToARGB((int) Math.sqrt(meanAlpha) * 255, (int) Math.sqrt(meanRed),
				(int) Math.sqrt(meanGreen), (int) Math.sqrt(meanBlue));

		return ColorUtil.argbToFXColor(argbMean);
	}

	/**
	 * Buffered image types mapped to enum values for easier handling
	 * 
	 * @author Kilian
	 *
	 */
	public enum BImageType {

		/**
		 * Image type is not recognized so it must be a customized image. This type is
		 * only used as a return value for the getType() method.
		 */
		TYPE_CUSTOM(0),

		/**
		 * Represents an image with 8-bit RGB color components packed into integer
		 * pixels. The image has a {@link DirectColorModel} without alpha. When data
		 * with non-opaque alpha is stored in an image of this type, the color data must
		 * be adjusted to a non-premultiplied form and the alpha discarded, as described
		 * in the {@link java.awt.AlphaComposite} documentation.
		 */
		TYPE_INT_RGB(1),

		/**
		 * Represents an image with 8-bit RGBA color components packed into integer
		 * pixels. The image has a <code>DirectColorModel</code> with alpha. The color
		 * data in this image is considered not to be premultiplied with alpha. When
		 * this type is used as the <code>imageType</code> argument to a
		 * <code>BufferedImage</code> constructor, the created image is consistent with
		 * images created in the JDK1.1 and earlier releases.
		 */
		TYPE_INT_ARGB(2),

		/**
		 * Represents an image with 8-bit RGBA color components packed into integer
		 * pixels. The image has a <code>DirectColorModel</code> with alpha. The color
		 * data in this image is considered to be premultiplied with alpha.
		 */
		TYPE_INT_ARGB_PRE(3),

		/**
		 * Represents an image with 8-bit RGB color components, corresponding to a
		 * Windows- or Solaris- style BGR color model, with the colors Blue, Green, and
		 * Red packed into integer pixels. There is no alpha. The image has a
		 * {@link DirectColorModel}. When data with non-opaque alpha is stored in an
		 * image of this type, the color data must be adjusted to a non-premultiplied
		 * form and the alpha discarded, as described in the
		 * {@link java.awt.AlphaComposite} documentation.
		 */
		TYPE_INT_BGR(4),

		/**
		 * Represents an image with 8-bit RGB color components, corresponding to a
		 * Windows-style BGR color model) with the colors Blue, Green, and Red stored in
		 * 3 bytes. There is no alpha. The image has a <code>ComponentColorModel</code>.
		 * When data with non-opaque alpha is stored in an image of this type, the color
		 * data must be adjusted to a non-premultiplied form and the alpha discarded, as
		 * described in the {@link java.awt.AlphaComposite} documentation.
		 */
		TYPE_3BYTE_BGR(5),

		/**
		 * Represents an image with 8-bit RGBA color components with the colors Blue,
		 * Green, and Red stored in 3 bytes and 1 byte of alpha. The image has a
		 * <code>ComponentColorModel</code> with alpha. The color data in this image is
		 * considered not to be premultiplied with alpha. The byte data is interleaved
		 * in a single byte array in the order A, B, G, R from lower to higher byte
		 * addresses within each pixel.
		 */
		TYPE_4BYTE_ABGR(6),

		/**
		 * Represents an image with 8-bit RGBA color components with the colors Blue,
		 * Green, and Red stored in 3 bytes and 1 byte of alpha. The image has a
		 * <code>ComponentColorModel</code> with alpha. The color data in this image is
		 * considered to be premultiplied with alpha. The byte data is interleaved in a
		 * single byte array in the order A, B, G, R from lower to higher byte addresses
		 * within each pixel.
		 */
		TYPE_4BYTE_ABGR_PRE(7),

		/**
		 * Represents an image with 5-6-5 RGB color components (5-bits red, 6-bits
		 * green, 5-bits blue) with no alpha. This image has a
		 * <code>DirectColorModel</code>. When data with non-opaque alpha is stored in
		 * an image of this type, the color data must be adjusted to a non-premultiplied
		 * form and the alpha discarded, as described in the
		 * {@link java.awt.AlphaComposite} documentation.
		 */
		TYPE_USHORT_565_RGB(8),

		/**
		 * Represents an image with 5-5-5 RGB color components (5-bits red, 5-bits
		 * green, 5-bits blue) with no alpha. This image has a
		 * <code>DirectColorModel</code>. When data with non-opaque alpha is stored in
		 * an image of this type, the color data must be adjusted to a non-premultiplied
		 * form and the alpha discarded, as described in the
		 * {@link java.awt.AlphaComposite} documentation.
		 */
		TYPE_USHORT_555_RGB(9),

		/**
		 * Represents a unsigned byte grayscale image, non-indexed. This image has a
		 * <code>ComponentColorModel</code> with a CS_GRAY {@link ColorSpace}. When data
		 * with non-opaque alpha is stored in an image of this type, the color data must
		 * be adjusted to a non-premultiplied form and the alpha discarded, as described
		 * in the {@link java.awt.AlphaComposite} documentation.
		 */
		TYPE_BYTE_GRAY(10),

		/**
		 * Represents an unsigned short grayscale image, non-indexed). This image has a
		 * <code>ComponentColorModel</code> with a CS_GRAY <code>ColorSpace</code>. When
		 * data with non-opaque alpha is stored in an image of this type, the color data
		 * must be adjusted to a non-premultiplied form and the alpha discarded, as
		 * described in the {@link java.awt.AlphaComposite} documentation.
		 */
		TYPE_USHORT_GRAY(11),

		/**
		 * Represents an opaque byte-packed 1, 2, or 4 bit image. The image has an
		 * {@link IndexColorModel} without alpha. When this type is used as the
		 * <code>imageType</code> argument to the <code>BufferedImage</code> constructor
		 * that takes an <code>imageType</code> argument but no <code>ColorModel</code>
		 * argument, a 1-bit image is created with an <code>IndexColorModel</code> with
		 * two colors in the default sRGB <code>ColorSpace</code>: {0,&nbsp;0,&nbsp;0}
		 * and {255,&nbsp;255,&nbsp;255}.
		 *
		 * <p>
		 * Images with 2 or 4 bits per pixel may be constructed via the
		 * <code>BufferedImage</code> constructor that takes a <code>ColorModel</code>
		 * argument by supplying a <code>ColorModel</code> with an appropriate map size.
		 *
		 * <p>
		 * Images with 8 bits per pixel should use the image types
		 * <code>TYPE_BYTE_INDEXED</code> or <code>TYPE_BYTE_GRAY</code> depending on
		 * their <code>ColorModel</code>.
		 * 
		 * <p>
		 * When color data is stored in an image of this type, the closest color in the
		 * colormap is determined by the <code>IndexColorModel</code> and the resulting
		 * index is stored. Approximation and loss of alpha or color components can
		 * result, depending on the colors in the <code>IndexColorModel</code> colormap.
		 */
		TYPE_BYTE_BINARY(12),

		/**
		 * Represents an indexed byte image. When this type is used as the
		 * <code>imageType</code> argument to the <code>BufferedImage</code> constructor
		 * that takes an <code>imageType</code> argument but no <code>ColorModel</code>
		 * argument, an <code>IndexColorModel</code> is created with a 256-color 6/6/6
		 * color cube palette with the rest of the colors from 216-255 populated by
		 * grayscale values in the default sRGB ColorSpace.
		 *
		 * <p>
		 * When color data is stored in an image of this type, the closest color in the
		 * colormap is determined by the <code>IndexColorModel</code> and the resulting
		 * index is stored. Approximation and loss of alpha or color components can
		 * result, depending on the colors in the <code>IndexColorModel</code> colormap.
		 */
		TYPE_BYTE_INDEXED(13);

		private int type;

		private BImageType(int type) {
			this.type = type;
		}

		public int get() {
			return type;
		}
	}
}
