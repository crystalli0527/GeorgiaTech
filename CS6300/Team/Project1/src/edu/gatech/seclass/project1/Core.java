package edu.gatech.seclass.project1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Core {

	private static final String DELIM_REPLACEMENT = "SNTNCDLMTR"; //this is what we're replacing delimiters with to count number of sentences
	private static final String TRIM_PUNCTUATION_DELIMS = ".?!:;,"; //holds delimiters to be trimmed so that "one," isn't considered as a 4 letter word
	
	//error messages for arg parser
	private static final String ERR_MSG_SPECIFY_DELIMS = "Please specify the delimiters";
	private static final String ERR_MSG_WORD_LEN_MUST_BE_INT = "Word length limit must be an integer > 0";
	private static final String ERR_MSG_SPECIFY_WORD_LEN_LIMIT = "Please specify the word length limit";
	private static final String ERR_MSG_COULD_NOT_FIND_FILE_NAME_ARG = "Couldn't find the file name argument";

	
	
	/*
	 * Replaces all delimiters in a string with delimReplacement constant
	 * and trims punctuation delimiters
	 */
	private static String replaceDelimiters(String inputString, String delimiters){
		String result = inputString;
		
		for (int i=0; i<delimiters.length(); i++){
			result = result.replace("" + delimiters.charAt(i), " "+DELIM_REPLACEMENT+" ");
		}
		
		for (int i=0; i<TRIM_PUNCTUATION_DELIMS.length(); i++){
			result = result.replace(TRIM_PUNCTUATION_DELIMS.charAt(i), ' ');
		}
		
		return result;
	}
	
	/*
	 * Computes the average sentence length
	 */
	public static String computeAvgSentenceLength(String fileName, int wordLengthLowLimit, String delimiters) throws FileNotFoundException, IOException{
		double result = 0.0;
		
		ArrayList<String> wordArrList = new ArrayList <String> (); //stores words on a line
		BufferedReader fileIn;

		fileIn = new BufferedReader(new FileReader(fileName));    
		String readLine = fileIn.readLine();

		while (readLine != null) {
			//process current line
			String processedLine = replaceDelimiters(readLine, delimiters);
		    	
			//tokenize a string into words and put them into array list
			StringTokenizer st = new StringTokenizer(processedLine);
			while (st.hasMoreTokens()) {
				wordArrList.add(st.nextToken()); //add words to the array list
			}
		    	
			readLine = fileIn.readLine(); // read next line
		}
		fileIn.close();
				
		//count words and sentences
		int sentenceCount = 0;
		int wordCount = 0;
		int wordsInCurrentSentence = 0; //counts words in current sentence to keep track of sentences with 0 words
		for (int i=0; i<wordArrList.size(); i++){
			if (wordArrList.get(i).equals(DELIM_REPLACEMENT)){
				if (wordsInCurrentSentence > 0){
					sentenceCount++;
				}
				wordsInCurrentSentence = 0; //reset
			}
			else{
				if (wordArrList.get(i).length() >= wordLengthLowLimit){
					wordCount++;
					wordsInCurrentSentence++;
				}
			}
		}
		
		//System.out.println("words (>="+wordLengthLowLimit+" chars): "+wordCount);
		//System.out.println("Sentences: "+sentenceCount);
		
		if (sentenceCount > 0){
			result = (double)wordCount / (double)sentenceCount;
		}
		else{
			if (wordCount >= 4){
				result = wordCount; // no delims but 4 or more words = 1 sentence 
			}
			else{
				result = 0.0; //no sentences
			}
		}
		
		DecimalFormat df = new DecimalFormat("#0.00");
		df.setRoundingMode(RoundingMode.HALF_UP);
		
		return df.format(result);
	}
	
	
	/*
	 * Parses and validates args
	 * returns String array
	 * 0: fileName
	 * 1: word length limit
	 * 2: delimiters
	 */
	public static String[] parseArgs(String[] args) throws ParseException{
		String[] parsedArgs = new String[3]; //what we're returning
		
		Options options = new Options();

		// add options
		options.addOption("d", true, "Delimiter specification");
		options.addOption("l", true, "Minimum word length specification");
		
		CommandLineParser parser = new DefaultParser();
		
		CommandLine cmd = parser.parse( options, args);
		
		if(cmd.hasOption("d")) {
			String delimStringD = cmd.getOptionValue("d");
			if (delimStringD != null && !delimStringD.equalsIgnoreCase("")){
				parsedArgs[2] = delimStringD;
			}
			else{
				throw new IllegalArgumentException(ERR_MSG_SPECIFY_DELIMS);
			}
		}
		else {
			parsedArgs[2] = ";.?!:"; //use default delimiters
		}
		
		if(cmd.hasOption("l")) {
			String delimStringL = cmd.getOptionValue("l");
			if (delimStringL != null && !delimStringL.equalsIgnoreCase("")){
					
				try{
					int tmp = Integer.parseInt(delimStringL);
					if (tmp < 1){
						throw new IllegalArgumentException(ERR_MSG_WORD_LEN_MUST_BE_INT);
					}
				}
				catch (NumberFormatException e){
					throw new IllegalArgumentException(ERR_MSG_WORD_LEN_MUST_BE_INT);
				}
					
				parsedArgs[1] = delimStringL;
			}
			else{
				throw new IllegalArgumentException(ERR_MSG_SPECIFY_WORD_LEN_LIMIT);
			}
		}
		else {
			parsedArgs[1] = "4"; //default wordLengthLowLimit
		}
			
			
		String[] leftoverArgs = cmd.getArgs(); //this is where the fileName should be after the option args are parsed
		if (leftoverArgs != null && leftoverArgs.length == 1){
			parsedArgs[0] = leftoverArgs[0]; // fileName
		}
		else{
			throw new IllegalArgumentException(ERR_MSG_COULD_NOT_FIND_FILE_NAME_ARG);
		}

		
		return parsedArgs;
	}
	
}
