package com.github.kilianB;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Crypto utilities used to encrypt and decrypt data.
 * <p>
 * NO WARRANTY or liability that the functions are used correctly. If you work
 * with highly sensitive data look for someone who actually knows their way
 * around cryptographic code.
 * 
 * @author Kilian
 *
 */
public class CryptoUtil {

	/**
	 * Derives an AES key from a given password useful for further encryption usage.
	 * Key derivation is needed due to AES requiring an x bit key from a short
	 * password sequence
	 * 
	 * @see <a href=
	 *      "https://stackoverflow.com/questions/992019/java-256-bit-aes-password-based-encryption/992413">
	 *      Based on
	 *      https://stackoverflow.com/questions/992019/java-256-bit-aes-password-based-encryption/992413
	 *      </a>
	 * 
	 * @param password  User defined password
	 * @param salt      Salt added to the password
	 * @param keyLength bit key length. Has to be a 2 complement 64 128 256 525 ...
	 * @return A secret key used for encryption and decryption operations
	 * @throws InvalidKeySpecException if the given key specificationis
	 *                                 inappropriate for this secret-key
	 * @since 1.0.0
	 */
	public static SecretKey deriveKey(char[] password, byte[] salt, int keyLength) throws InvalidKeySpecException {
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(password, salt, 65536, keyLength);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
			return secret;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} // Does not happen
		return null;
	}

	/**
	 * Derives a 128 bit AES key from a given password useful for further encryption
	 * usage.
	 * 
	 * @see <a href=
	 *      "https://stackoverflow.com/questions/992019/java-256-bit-aes-password-based-encryption/992413">
	 *      Based on
	 *      https://stackoverflow.com/questions/992019/java-256-bit-aes-password-based-encryption/992413
	 *      </a>
	 * 
	 * @param password The password used to create the key
	 * @return A secret key used for encryption and decryption operations
	 * @since 1.0.0
	 */
	public static SecretKey deriveKey(char[] password) {
		try {
			return deriveKey(password, "FixedSalt".getBytes(), 128);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final int IV_LENGTH = 16;

	/**
	 * Encrypt a given string with AES/CBC/PKCS5PADDING cipher. The returned string
	 * is base64 encoded. The first 16 bytes are holds the init vector enabling
	 * persistent storage and reconstruction at a later stage.
	 * 
	 * 
	 * 
	 * 
	 * @param key  they key used to encrypt the data
	 * @param text the data t encrypt
	 * @return the encrypted text with the init vector encoded as base64 string
	 * @throws Exception if an exception occurs
	 * @see <a href=
	 *      "https://stackoverflow.com/questions/15554296/simple-java-aes-encrypt-decrypt-example">
	 *      Partially based on:
	 *      https://stackoverflow.com/questions/15554296/simple-java-aes-encrypt-decrypt-example
	 *      </a>
	 * @since 1.0.0
	 */
	public static String encryptAES(SecretKey key, String text) throws Exception {

		byte[] initVector = new byte[IV_LENGTH];

		new SecureRandom().nextBytes(initVector);

		IvParameterSpec iv = new IvParameterSpec(initVector);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);

		byte[] encrypted = cipher.doFinal(text.getBytes());

		byte[] finalWithIV = new byte[encrypted.length + initVector.length];
		System.arraycopy(initVector, 0, finalWithIV, 0, initVector.length);

		System.arraycopy(encrypted, 0, finalWithIV, initVector.length, encrypted.length);

		return new String(Base64.getEncoder().encode(finalWithIV));
	}

	/**
	 * 
	 * @param key       used to decrypt the data. A key with the same settings
	 *                  during encryption should be used to retrieve useable results
	 * @param encrypted the base64 representation of the encrypted text as returned
	 *                  by {@link #encryptAES(SecretKey, String)}
	 * @return the decrypted text
	 * @throws Exception if an Exception occurs
	 * @since 1.0.0
	 */
	public static String decrypt(SecretKey key, String encrypted) throws Exception {

		byte[] inputWithIV = Base64.getDecoder().decode(encrypted);

		IvParameterSpec iv = new IvParameterSpec(inputWithIV, 0, IV_LENGTH);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, key, iv);

		byte[] original = cipher.doFinal(inputWithIV, IV_LENGTH, inputWithIV.length - IV_LENGTH);

		return new String(original);

	}

}