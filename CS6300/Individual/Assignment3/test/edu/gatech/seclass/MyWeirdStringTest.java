package edu.gatech.seclass;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MyWeirdStringTest {

	private MyWeirdStringInterface myweirdstring;

	@Before
	public void setUp() throws Exception {
		myweirdstring = new MyWeirdString();
	}

	@After
	public void tearDown() throws Exception {
		myweirdstring = null;
	}
	
	// Provided Test
	@Test
	public void testCountDigits1() {
		myweirdstring.setWeirdString("I'd better put s0me d1gits in this 5tr1n9, right?");
		assertEquals(5, myweirdstring.countDigits());
	}

	// Try another string with more digits
	@Test
	public void testCountDigits2() {
		myweirdstring.setWeirdString("12349/129abs!!!");
		assertEquals(8, myweirdstring.countDigits());
	}

	// Try a string with no numbers at all
	@Test
	public void testCountDigits3() {
		myweirdstring.setWeirdString("Robert is a cool guy. ");
		assertEquals(0, myweirdstring.countDigits());
	}

	// Try a blank string
	@Test
	public void testCountDigits4() {
		myweirdstring.setWeirdString("");
		assertEquals(0, myweirdstring.countDigits());
	}

	// Provided Test
	@Test
	public void testGetEvenCharacters1() {
		myweirdstring.setWeirdString("I'd better put s0me d1gits in this 5tr1n9, right?");
		assertEquals("' etrptsm 1isi hs5rn,rgt", myweirdstring.getEvenCharacters());
	}

	// Verify an empty String returns nothing
	@Test
	public void testGetEvenCharacters2() {
		myweirdstring.setWeirdString("");
		assertEquals("", myweirdstring.getEvenCharacters());
	}

	// Verify a string of length one also returns nothing, since the first index is 1, which is odd
	@Test
	public void testGetEvenCharacters3() {
		myweirdstring.setWeirdString("a");
		assertEquals("", myweirdstring.getEvenCharacters());
	}

	// Exactly one even character
	@Test
	public void testGetEvenCharacters4() {
		myweirdstring.setWeirdString("abc");
		assertEquals("b", myweirdstring.getEvenCharacters());
	}
	
	// Try a null string
	@Test (expected = NullPointerException.class)
	public void testGetEvenCharacters5() {
		myweirdstring.setWeirdString(null);
		assertEquals("0", myweirdstring.getOddCharacters());
	}

	// Provided Test
	@Test
	public void testGetOddCharacters1() {
		myweirdstring.setWeirdString("I'd better put s0me d1gits in this 5tr1n9, right?");
		assertEquals("Idbte u 0edgt nti t19 ih?", myweirdstring.getOddCharacters());
	}

	// Verify a string of length one returns that one index only
	@Test
	public void testGetOddCharacters2() {
		myweirdstring.setWeirdString("b");
		assertEquals("b", myweirdstring.getOddCharacters());
	}

	// Verify an empty String returns nothing
	@Test
	public void testGetOddCharacters3() {
		myweirdstring.setWeirdString("");
		assertEquals("", myweirdstring.getOddCharacters());
	}

	// Return one odd character, but the string is length 2
	@Test
	public void testGetOddCharacters4() {
		myweirdstring.setWeirdString("b!");
		assertEquals("b", myweirdstring.getOddCharacters());
	}
	
	// Try a null string
	@Test (expected = NullPointerException.class)
	public void testGetOddCharacters5() {
		myweirdstring.setWeirdString(null);
		assertEquals("0", myweirdstring.getOddCharacters());
	}

	// Provided test
	@Test
	public void testConvertDigitsToRomanNumeralsInSubstring1() {
		myweirdstring.setWeirdString("I'd better put s0me d1gits in this 5tr1n9, right?");
		myweirdstring.convertDigitsToRomanNumeralsInSubstring(40, 45);
		assertEquals("I'd better put s0me d1gits in this 5tr1nIX, right?", myweirdstring.getWeirdString());
	}

	// Verify that IllegalArgumentException is thrown correctly , when endPosition > startPosition
	@Test (expected = IllegalArgumentException.class)
	public void testConvertDigitsToRomanNumeralsInSubstring2() {
		myweirdstring.setWeirdString("I'd better put s0me d1gits in this 5tr1n9, right?");
		myweirdstring.convertDigitsToRomanNumeralsInSubstring(38, 2);
		assertEquals("I'd better put s0me d1gits in this 5tr1nIX, right?", myweirdstring.getWeirdString());
	}

	// Check if endPosition of way too high throws MyIndexOutOfBoundsException
	@Test (expected = MyIndexOutOfBoundsException.class)
	public void testConvertDigitsToRomanNumeralsInSubstring3() {
		myweirdstring.setWeirdString("I'd better put s0me d1gits in this 5tr1n9, right?");
		myweirdstring.convertDigitsToRomanNumeralsInSubstring(38, 200);
		assertEquals("I'd better put s0me d1gits in this 5tr1nIX, right?", myweirdstring.getWeirdString());
	}
	
	// Check if startPosition of -1 throws MyIndexOutOfBoundsException
	@Test (expected = MyIndexOutOfBoundsException.class)
	public void testConvertDigitsToRomanNumeralsInSubstring4() {
		myweirdstring.setWeirdString("I'd better put s0me d1gits in this 5tr1n9, right?");
		myweirdstring.convertDigitsToRomanNumeralsInSubstring(-1, 45);
		assertEquals("I'd better put s0me d1gits in this 5tr1nIX, right?", myweirdstring.getWeirdString());
	}

	// Test when there is no digits to convert 
	@Test
	public void testConvertDigitsToRomanNumeralsInSubstring5() {
		myweirdstring.setWeirdString("Test String yo!!!");
		myweirdstring.convertDigitsToRomanNumeralsInSubstring(3, 6);
		assertEquals("Test String yo!!!", myweirdstring.getWeirdString());
	}

	// Simple one just to make sure the switch will work
	@Test
	public void testConvertDigitsToRomanNumeralsInSubstring6() {
		myweirdstring.setWeirdString("1Test String yo!!!");
		myweirdstring.convertDigitsToRomanNumeralsInSubstring(1, 6);
		assertEquals("ITest String yo!!!", myweirdstring.getWeirdString());
	}
	
	// More in depth test, should get all numbers
	@Test
	public void testConvertDigitsToRomanNumeralsInSubstring7() {
		myweirdstring.setWeirdString("Th1s is T3st f0r some R0m4n Num3r4l77711123456789");
		myweirdstring.convertDigitsToRomanNumeralsInSubstring(1, 50);
		assertEquals("ThIs is TIIIst f0r some R0mIVn NumIIIrIVlVIIVIIVIIIIIIIIIIIVVVIVIIVIIIIX", myweirdstring.getWeirdString());
	}
	
	// Test for a blank string
	@Test
	public void testConvertDigitsToRomanNumeralsInSubstring8() {
		myweirdstring.setWeirdString("");
		myweirdstring.convertDigitsToRomanNumeralsInSubstring(0, 0);
		assertEquals("", myweirdstring.getWeirdString());
	}
	
	// startPosition == endPosition
	@Test
	public void testConvertDigitsToRomanNumeralsInSubstring9() {
		myweirdstring.setWeirdString("1Test String yo!!!");
		myweirdstring.convertDigitsToRomanNumeralsInSubstring(3, 3);
		assertEquals("1Test String yo!!!", myweirdstring.getWeirdString());
	}
}
