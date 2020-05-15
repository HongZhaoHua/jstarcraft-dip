package com.github.kilianB;

/**
 * @author Kilian
 */
public class StringUtil {

	/**
	 * Calculate the number of characters needed to present the integer part of a
	 * number +1 if the number is negative.
	 * 
	 * <pre>
	 * 1234     -{@literal>} 4
	 * -1234    -{@literal>} 5
	 * 12345    -{@literal>} 5
	 * 12345.12 -{@literal>} 5
	 * </pre>
	 * 
	 * @param n A number
	 * @return the character count of the integer part of the number
	 * @since 1.0.0
	 */
	public static int charsNeeded(Number n) {
		double numberAsDouble = n.doubleValue();
		int negative = (numberAsDouble < 0 ? 1 : 0);
		if (MathUtil.isDoubleEquals(numberAsDouble, 0d, 1e-5)) {
			return 1 + negative;
		}
		return (int) Math.floor(Math.log10(Math.abs(numberAsDouble)) + 1) + negative;
	}

	/**
	 * If the text can not be centered due to (length - textLength)%2 != 0 the
	 * string will be center left alligned with 1 padding string appended at the end
	 * of the string for consistent length.
	 * 
	 * @param text    the text to center
	 * @param padding the padding symbol inserted left and right of the text (1
	 *                character long)
	 * @param length  the final length of the entire string
	 * @return the centered text as string.
	 * @since 1.0.0
	 */
	public static String centerText(String text, String padding, int length) {
		int textLength = text.length();
		// int paddingWidth = padding.length();

		int leftPad = (length - textLength) / 2;
		int rightPad = leftPad;

		String paddingStrLeft;
		String paddingStrRight = multiplyChar(padding, rightPad);

		if ((leftPad + rightPad) != (length - textLength)) {
			paddingStrLeft = paddingStrRight;
			paddingStrRight += padding;
		} else {
			paddingStrLeft = paddingStrRight;
		}

		StringBuilder sb = new StringBuilder(length);
		sb.append(paddingStrLeft);
		sb.append(text);
		sb.append(paddingStrRight);
		return sb.toString();
	}

	/**
	 * Construct a string with multiple chars concatenated x times
	 * 
	 * @param Char  the char of the final string
	 * @param count the number of the chars appended after one another.
	 * @return The concat string.
	 * @since 1.0.0
	 */
	public static String multiplyChar(String Char, int count) {
		if (Char == null || Char.isEmpty())
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append(Char);
		}
		return sb.toString();
	}

	/**
	 * Append the given char to the start of the string until the string has reached
	 * the given size
	 * 
	 * @param Char          The char or string to append
	 * @param desiredLength The desired length of the final string
	 * @param content       the original string
	 * @return the string with the desired length and concatenated char
	 * @since 1.4.0
	 */
	public static String fillStringBeginning(String Char, int desiredLength, String content) {
		return multiplyChar(Char, desiredLength - content.length()) + content;
	}

	/**
	 * Append the given char to the end of the string until the string has reached
	 * the given size
	 * 
	 * @param Char          The char or string to append
	 * @param desiredLength The desired length of the final string
	 * @param content       the original string
	 * @return the string with the desired length and concatenated char
	 * @since 1.0.0
	 */
	public static String fillString(String Char, int desiredLength, String content) {
		return content + multiplyChar(Char, desiredLength - content.length());
	}

}
