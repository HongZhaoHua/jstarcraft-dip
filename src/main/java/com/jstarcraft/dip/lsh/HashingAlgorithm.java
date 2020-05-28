package com.jstarcraft.dip.lsh;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.github.kilianB.Require;
import com.jstarcraft.dip.color.ColorPixel;
import com.jstarcraft.dip.color.ImageUtility;
import com.jstarcraft.dip.hash.Hash;
import com.jstarcraft.dip.lsh.kernel.ImageConverter;

/**
 * Base class for hashing algorithms returning perceptual hashes for supplied
 * images reducing the number of bits needed to represent said image.
 * 
 * <p>
 * Opposed to cryptographic hashes, hashes computed by these classes are
 * entirely predictable. Similarity metrics applied to these hashes shall
 * produce a higher score for closely related images.
 * 
 * <p>
 * If implementing impose a limitation on the lower bounds on the dimension of
 * hashable images the method {@link #getKeyResolution()} has to be overridden.
 * 
 * <p>
 * Unless otherwise noted hashing algorithms are thread safe.
 * 
 * @author Kilian
 * @since 1.0.0
 */

public abstract class HashingAlgorithm {

    // maybe move to bitsets//Mutable inetegers? not efficient for small keys?
    protected List<ImageConverter> converters = new ArrayList<>();

    /**
     * The target bit resolution supplied during algorithm creation. This number
     * represents the number of bits the final hash SHOULD have, but does not
     * necessarily reflect it's actual length.
     * <p>
     * Therefore, it is not advised to use this value during computation of the hash
     * unless you made sure that the value actually reflects
     * 
     */
    protected final int bitResolution;

    /** The actual bit resolution of produced hashes */
    protected int keyResolution = -1;

    /**
     * The algorithm id of this hashing algorithm. The algorithm id specifies a
     * unique identifier which allows to check if two distinct hashes are created by
     * the same hashing algorithm and therefore are comparable. Even algorithms with
     * the same
     */
    private int algorithmId;

    /**
     * After a hash was created or the id was calculated the object may not be
     * altered anymore.
     */
    protected boolean immutableState = false;

    private static final String LOCKED_MODIFICATION_EXCEPTION = "Hashing algorithms may only be " + "modified as long as no hash has been generated or hashcode has been used by this object. This limitation is " + "imposed to ensure that each hash is associated with the correct algorithm id which " + "might change if the internal state of the algorithm is altered. Be aware" + " that method like getKeyResolution() already perform a hashing operation " + "and therefore invalidate further modification requests";

    /**
     * The height and width of the scaled instance used to compute the hash
     */
    protected int height, width;

    /**
     * Promises a key with approximately bit resolution. Due to geometric
     * requirements the key might be marginally larger or smaller than specified.
     * Hashing algorithms shall try to at least provide the number of bits specified
     * 
     * @param bitResolution The bit count of the final hash
     */
    public HashingAlgorithm(int bitResolution) {
        this.bitResolution = Require.positiveValue(bitResolution, "The bit resolution for hashing algorithms has to be positive");
    }

    /**
     * Calculate hashes for the given images. Invoking the hash function on the same
     * image has to return the same hash value. A comparison of the hashes relates
     * to the similarity of the images. The lower the value the more similar the
     * images are. Equal images will produce a similarity of 0.
     * 
     * @param images whose hash will be calculated
     * @return The hash representing the image
     * @see Hash
     * @since 3.0.0
     */
    public Hash[] hash(BufferedImage... images) {
        Hash[] hashs = new Hash[images.length];
        for (int index = 0; index < images.length; index++) {
            hashs[index] = this.hash(images[index]);
        }
        return hashs;
    }

    /**
     * Calculate hashes for the given images. Invoking the hash function on the same
     * image has to return the same hash value. A comparison of the hashes relates
     * to the similarity of the images. The lower the value the more similar the
     * images are. Equal images will produce a similarity of 0.
     * 
     * @param files pointing to the images
     * @return The hash representing the images
     * @throws IOException if an error occurs during loading the image
     * @see Hash
     * @since 3.0.0
     */
    public Hash[] hash(File... files) throws IOException {
        Hash[] hashs = new Hash[files.length];
        for (int index = 0; index < files.length; index++) {
            hashs[index] = this.hash(files[index]);
        }
        return hashs;
    }

    /**
     * Calculate a hash for the given image. Invoking the hash function on the same
     * image has to return the same hash value. A comparison of the hashes relates
     * to the similarity of the images. The lower the value the more similar the
     * images are. Equal images will produce a similarity of 0.
     * 
     * @param image Image whose hash will be calculated
     * @return The hash representing the image
     * @see Hash
     */
    public Hash hash(BufferedImage image) {
        // If we have kernels defined alter red green and blue values accordingly
        if (!converters.isEmpty()) {
            for (ImageConverter kernel : converters) {
                image = kernel.convert(image);
            }
        }
        immutableState = true;
        BigInteger bits;
        ColorPixel pixel = ColorPixel.create(ImageUtility.getScaledInstance(image, width, height));
        if (keyResolution < 0) {
            HashBuilder builder = new HashBuilder(this.bitResolution);
            bits = hash(pixel, builder);
            keyResolution = builder.length;
        } else {
            bits = hash(pixel, new HashBuilder(getKeyResolution()));
        }
        return new Hash(bits, getKeyResolution(), algorithmId());
    }

    /**
     * Calculate a hash for the given image. Invoking the hash function on the same
     * image has to return the same hash value. A comparison of the hashes relates
     * to the similarity of the images. The lower the value the more similar the
     * images are. Equal images will produce a similarity of 0.
     * 
     * @param file The file pointing to the image
     * @return The hash representing the image
     * @throws IOException if an error occurs during loading the image
     * @see Hash
     */
    public Hash hash(File file) throws IOException {
        immutableState = true;
        return hash(ImageIO.read(file));
    }

    /**
     * Calculate a hash for the given image. Invoking the hash function on the same
     * image has to return the same hash value. A comparison of the hashes relates
     * to the similarity of the images. The lower the value the more similar the
     * images are. Equal images will produce a similarity of 0.
     * 
     * <p>
     * This method is intended to be overwritten by implementations and takes a
     * baseHash argument to allow concatenating multiple hashes as well to be able
     * to compute the effective hash length in {@link #getKeyResolution()}.
     * Preceding 0's are omitted in big integer objects, while the usual hamming
     * distance can be calculated due to xoring without issue the normalized
     * distance requires the potential length of the key to be known.
     * 
     * @param image   Image whose hash will be calculated
     * @param builder a hash builder used to construct the hash
     * @return the hash encoded as a big integer
     */
    protected abstract BigInteger hash(ColorPixel pixel, HashBuilder builder);

    /**
     * A unique id identifying the settings and algorithms used to generate the
     * output result. The id shall stay consistent throughout restarts of the jvm.
     * 
     * <p>
     * Even if different bitResolutions are used in the constructor
     * {@link #HashingAlgorithm(int)} the algorithId <b>MUST</b> return the same id
     * for two instances if the returned hashes for the same input will always be
     * equal. Therefore instead of checking against the bitResolution the actual
     * resolution as returned by {@link #getKeyResolution()} should be used.
     * 
     * @return the algorithm id identifying this hashing algorithm
     */
    public final int algorithmId() {
        if (algorithmId == 0) {
            algorithmId = 31 * precomputeAlgoId();
            // Make sure the algo id doesn't collide with version 2.0.0 id's
            algorithmId = 31 * algorithmId + 5 + converters.hashCode();
            immutableState = true;
        }
        return algorithmId;
    }

    /**
     * A unique id identifying the settings and algorithms used to generate the
     * output result. The id shall stay consistent throughout restarts of the jvm.
     * This method shall contain a hash code for the object which
     * 
     * <ul>
     * <li>Stays consistent throughout restart of the jvm</li>
     * <li>Value does not change after the constructor finished</li>
     * <li>Must return the same value if two instances compute the same hashes for
     * identical input</li>
     * </ul>
     * 
     * <p>
     * Even if different bitResolutions are used in the constructor
     * {@link #HashingAlgorithm(int)} the algorithId <b>MUST</b> return the same id
     * for two instances if the returned hashes for the same input will always be
     * equal. Therefore instead of checking against the bitResolution the actual
     * resolution as returned by {@link #getKeyResolution()} should be used.
     * 
     * This method algorithm id's as information available to the child class and
     * will be extended the hashcode of the kernels.
     * 
     * @return the preliminary algorithm id identifying this hashing algorithm
     */
    protected abstract int precomputeAlgoId();

    /**
     * Get the actual bit key resolution of all hashes computed by this algorithm.
     * 
     * <p>
     * Be aware that this value may differ from:
     * 
     * <ul>
     * <li>the supplied bit resolution during algorithm creation due to geometric
     * constraints of the hashing algorithm.</li>
     * <li>the returned hash's {@link java.math.BigInteger#bitCount()} value due to
     * preceding 0 bits being truncated in the big integer</li>
     * </ul>
     * 
     * @return the actual bit resolution of the hash.
     */
    public int getKeyResolution() {
        // If they key resolution is not know compute a sample hash and cache it's
        // return value
        if (keyResolution < 0) {
            BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
            ColorPixel pixel = ColorPixel.create(ImageUtility.getScaledInstance(image, width, height));
            HashBuilder builder = new HashBuilder(this.bitResolution);
            this.hash(pixel, builder);
            keyResolution = builder.length;
        }
        return keyResolution;
    }

    /**
     * Add a {@link com.jstarcraft.dip.lsh.kernel.ImageConverter Filter} to this
     * hashing algorithm which will be used to alter the image before the hashing
     * operation is applied. Kernels are invoked in the order they are added and are
     * performed individually on all 3 RGB channels.
     * 
     * <p>
     * Be aware that filters can only be added or removed until the first hash is
     * computed. This limitation is enforced due to modified Kernels changing the
     * hashcode of the object which might be used in hash collections leading to the
     * object not being found after said operation.
     * 
     * @param converter The filter to add.
     * @throws NullPointerException  if filter is null
     * @throws IllegalStateException if a hash was already created and the object is
     *                               considered immutable.
     * @since 2.0.0
     */
    public void addConverter(ImageConverter converter) {
        Objects.requireNonNull(converter);
        if (immutableState) {
            throw new IllegalStateException(LOCKED_MODIFICATION_EXCEPTION);
        }
        this.converters.add(converter);
    }

    /**
     * Remove the first occurance of a
     * {@link com.jstarcraft.dip.lsh.kernel.ImageConverter Filter} from this hashing
     * algorithm.
     * 
     * <p>
     * Be aware that filters can only be added or removed until the first hash is
     * computed. This limitation is enforced due to modified Kernels changing the
     * hashcode of the object which might be used in hash collections leading to the
     * object not being found after said operation.
     * 
     * @param converter The filters to remove.
     * @return true if the kernel was removed. False otherwise
     * @throws IllegalStateException if a hash was already created and the object is
     *                               considered immutable.
     * @since 2.0.0
     */
    public boolean removeConverter(ImageConverter converter) {
        if (immutableState) {
            throw new IllegalStateException(LOCKED_MODIFICATION_EXCEPTION);
        }
        return this.converters.remove(converter);
    }

    /**
     * Wraps the values supplied in the argument hash into a hash object as it would
     * be produced by this algorithm.
     * <p>
     * Some algorithms may choose to return an extended hash class to overwrite
     * certain behavior, in particular the
     * {@link com.jstarcraft.dip.hash.Hash#toImage(int)} is likely to differ.
     * 
     * <p>
     * If the algorithm does not utilize a special hash sub class this method
     * returns the supplied argument.
     * 
     * @param original the hash to transform
     * @return a hash as it would be created by this algorithm.
     * @since 3.0.0
     */
    public Hash createAlgorithmSpecificHash(Hash original) {
        return original;
    }

    public int getBitResolution() {
        return bitResolution;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [" + bitResolution + "]";
    }

    @Override
    public int hashCode() {
        return algorithmId();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null)
            return false;
        if (getClass() != object.getClass())
            return false;
        HashingAlgorithm that = (HashingAlgorithm) object;
        if (this.algorithmId() != that.algorithmId())
            return false;
        return true;
    }

}