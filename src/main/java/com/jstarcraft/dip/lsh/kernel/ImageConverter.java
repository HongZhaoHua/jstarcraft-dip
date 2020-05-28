package com.jstarcraft.dip.lsh.kernel;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * A filter modifies the content of an image while leaving the source image
 * intact.
 * 
 * @author Kilian
 * @since 2.0.0
 */
/**
 * 图像转换器
 * 
 * @author Birdy
 *
 */
public interface ImageConverter {

    /**
     * Apply the filter to the input image and return an altered copy
     * 
     * @param image the input image to apply the filter on
     * @return the altered image
     */
    public BufferedImage convert(BufferedImage image);

}
