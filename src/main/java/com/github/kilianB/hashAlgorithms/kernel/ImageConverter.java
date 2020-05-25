package com.github.kilianB.hashAlgorithms.kernel;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * A filter modifies the content of an image while leaving the source image
 * intact.
 * 
 * @author Kilian
 * @since 2.0.0
 */
public interface ImageConverter {

	/**
	 * Apply the filter to the input image and return an altered copy
	 * 
	 * @param bi the input image to apply the filter on
	 * @return the altered image
	 */
	public BufferedImage convert(BufferedImage bi);

}
