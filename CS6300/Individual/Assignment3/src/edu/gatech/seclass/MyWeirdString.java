package edu.gatech.seclass;

/**
 * MyWeirdString, based off the interface provided by Rufus of Georgia Tech
 * 
 * @author rskelton7 (Robert Skelton / rjs@gatech.edu)
 * @date   1.27.2016
 */

public class MyWeirdString implements MyWeirdStringInterface {
		
	// weirdString is a String
	public String weirdString;
	
	/**
	 * Sets the value of the current string.
	 * 
	 * @param string
	 *            The value to be set
	 */ 
	public void setWeirdString(String string) {
		if (string == null) {
			weirdString = "";
		}
		weirdString = string;
	}

	/**
	 * Returns the current string
	 * 
	 * @return Current string
	 */
	public String getWeirdString() {
		return weirdString;
	}

	/**
	 * Returns a string that consists of all and only the characters
	 * in even positions (i.e., second, fourth, sixth, and so on) in
	 * the current string, in the same order and with the same case as
	 * in the current string. The first character in the string is
	 * considered to be in Position 1.
	 * 
	 * @return String made of characters in even positions in the
	 * current string
	 */
	public String getEvenCharacters() {
		 String[] evenCharArray = getWeirdString().split("");
		 String evenChars = "";
		 for(int i = 0; i < evenCharArray.length; i = i + 2) {
	            evenChars += evenCharArray[i];
	        }
		 return evenChars;	
	}

	/**
	 * Returns a string that consists of all and only the characters
	 * in odd positions (i.e., first, third, fifth, and so on) in the 
	 * current string, in the same order and with the same case as in 
	 * the current string. The first character in the string is
	 * considered to be in Position 1.
	 * 
	 * @return String made of characters in odd positions in the
	 * current string
	 */
	public String getOddCharacters() {
		 String[] oddCharArray = getWeirdString().split("");
		 String oddChars = "";
		 for(int i = 1; i < oddCharArray.length; i = i + 2) {
	            oddChars += oddCharArray[i];
	        }
		 return oddChars;
	}

	/**
	 * Returns the number of digits in the current string
	 * 
	 * @return Number of digits in the current string
	 */
	public int countDigits() {
		int numDigits = 0;
		for (int i = 0, length = getWeirdString().length(); i < length; i++) {
			if (Character.isDigit(getWeirdString().charAt(i))) {
				numDigits++;
			}
		}
		return numDigits;
	}
	
	/**
	 * Replace the _individual_ digits in the current string, between
	 * startPosition and endPosition (included), with the corresponding
	 * Roman numeral symbol(s). The first character in the string is
	 * considered to be in Position 1. Digits are converted individually,
	 * even if contiguous, and digit "0" is not converted (e.g., 460 is
	 * converted to IVVI0). In case you are not familiar with Roman
	 * numerals, see https://en.wikipedia.org/wiki/Roman_numerals
	 * 
	 * @param startPosition
	 *            Position of the first character to consider
	 * @param endPosition
	 *            Position of the last character to consider
	 * @return
	 * @throws MyIndexOutOfBoundsException
	 *            If either "startPosition" or "endPosition" are out of
	 *            bounds (i.e., either less than 1 or greater then the
	 *            length of the string)
	 * @throws IllegalArgumentException
	 *            If "startPosition" > "endPosition" (but both are
	 *            within bounds)
	 */
	public void convertDigitsToRomanNumeralsInSubstring(int startPosition, int endPosition)
			throws MyIndexOutOfBoundsException, IllegalArgumentException {
		
		// Throw all the exceptions
		int length = getWeirdString().length();
		if ((startPosition < 0) || (endPosition > length + 1)) {
			throw new MyIndexOutOfBoundsException();
		} else if (startPosition > endPosition) {
			throw new IllegalArgumentException();
		}
		
		String[] weirdStringArray = getWeirdString().split("");
		
		// Start StringBuilder with all values before start Position
		StringBuilder finalString = new StringBuilder();
		
		// builds finalString BEFORE Roman Numeral Translation kicks in
		for (int i = 0; i < startPosition; i++) {
			finalString.append(weirdStringArray[i]);
		}
		
		// cycle from startPosition to endPosition
		// j is set as the value of the array at the current index
		for (int i = startPosition; i < endPosition; i++) {
			String j = weirdStringArray[i];
			switch (j) {
			case "1":
				finalString.append("I");
				break;
			case "2":
				finalString.append("II");
				break;
			case "3":
				finalString.append("III");
				break;
			case "4":
				finalString.append("IV");
				break;
			case "5":
				finalString.append("V");
				break;
			case "6":
				finalString.append("VI");
				break;
			case "7":
				finalString.append("VII");
				break;
			case "8":
				finalString.append("VIII");
				break;
			case "9":
				finalString.append("IX");
				break;
			default:
				finalString.append(j);
				break;
			}			
		}
		
		// Add the rest of the string AFTER endPosition to the StringBuilder
		for (int i = endPosition; i < length + 1; i++) {
			finalString.append(weirdStringArray[i]);
		}
		
		// Finally, set the final String to be weirdString
		setWeirdString(finalString.toString());
	}
}
