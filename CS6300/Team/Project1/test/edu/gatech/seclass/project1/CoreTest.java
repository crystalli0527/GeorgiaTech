package edu.gatech.seclass.project1;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class CoreTest {

	/*
	 * prototype test
	 */
	@Test
	public void test1() throws FileNotFoundException, IOException {
		assertEquals("2.25", Core.computeAvgSentenceLength("test.txt", 4, ".?!"));
	}
	
	/*
	 * 4 words 2 sentences + empty sentences
	 */
	@Test
	public void test2() throws FileNotFoundException, IOException {
		assertEquals("2.00", Core.computeAvgSentenceLength("4w_2s.txt", 4, ".?!"));
	}
	

	/*
	 * 7 As no delims
	 */
	@Test
	public void test3() throws FileNotFoundException, IOException {
		assertEquals("0.00", Core.computeAvgSentenceLength("7As_noDelims.txt", 4, ".?!"));
	}
	
	/*
	 * 7 As no delims with -l 1
	 */
	@Test
	public void test4() throws FileNotFoundException, IOException {
		assertEquals("7.00", Core.computeAvgSentenceLength("7As_noDelims.txt", 1, ".?!"));
	}
	
	/*
	 * test2, with only !
	 */
	@Test
	public void test5() throws FileNotFoundException, IOException {
		assertEquals("1.50", Core.computeAvgSentenceLength("test2.txt", 5, "!"));
	}
	
	/*
	 * test2, with only . so no delimiter in the file until the end
	 */
	@Test
	public void test6() throws FileNotFoundException, IOException {
		assertEquals("3.00", Core.computeAvgSentenceLength("test2.txt", 5, "."));
	}
	
	/*
	 * test2, with many delims, but high word length so will be 0.00
	 */
	@Test
	public void test7() throws FileNotFoundException, IOException {
		assertEquals("0.00", Core.computeAvgSentenceLength("test2.txt", 7, ".!:,5"));
	}
	
	/*
	 * test2, with letters as delims only
	 */
	@Test
	public void test8() throws FileNotFoundException, IOException {
		assertEquals("3.00", Core.computeAvgSentenceLength("test2.txt", 2, "a"));
	}
	
	/*
	 * parseArg return indexes
	 * 0: fileName
	 * 1: word length limit
	 * 2: delimiters
	 */
	
	/*
	 * testing -d
	 */
	@Test
	public void argParserTest1() throws ParseException {
		String[] args = {"file.txt", "-d", ".?!;:", "-l", "5"};
		assertEquals(".?!;:", Core.parseArgs(args)[2]);
	}
	
	/*
	 * testing -l
	 */
	@Test
	public void argParserTest2() throws ParseException {
		String[] args = {"file.txt", "-d", ".?!;:", "-l", "5"};
		assertEquals("5", Core.parseArgs(args)[1]);
	}
	
	/*
	 * testing fileName
	 */
	@Test
	public void argParserTest3() throws ParseException {
		String[] args = {"file.txt", "-d", ".?!;:", "-l", "5"};
		assertEquals("file.txt", Core.parseArgs(args)[0]);
	}
	
	/*
	 * testing fileName in the middle
	 */
	@Test
	public void argParserTest4() throws ParseException {
		String[] args = {"-d", ".?!;:", "file.txt", "-l", "5"};
		assertEquals("file.txt", Core.parseArgs(args)[0]);
	}
	
	/*
	 * no file name
	 */
	@Test (expected = IllegalArgumentException.class)
	public void argParserTest5() throws ParseException {
		String[] args = {"-d", ".?!;:", "-l", "5"};
		assertEquals("file.txt", Core.parseArgs(args)[0]);
		fail();
	}
	
	/*
	 * 2 args without flags: file.txt and file2.txt
	 */
	@Test (expected = IllegalArgumentException.class)
	public void argParserTest6() throws ParseException {
		String[] args = {"file2.txt", "-l", "5", "file.txt"};
		assertEquals("file.txt", Core.parseArgs(args)[0]);
		fail();
	}
	
	/*
	 * -d flag with no value
	 */
	@Test (expected = MissingArgumentException.class)
	public void argParserTest7() throws ParseException {
		String[] args = {"-d", "-l", "5", "file.txt"};
		assertEquals("file.txt", Core.parseArgs(args)[0]);
		fail();
	}

}
