package edu.gatech.seclass.project1;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.cli.ParseException;

public class Wc {
	
	public static void main(String[] args) {
		
		/* 
		 * to make merges easier:
		 * parsedArgs[0]: fileName
		 * parsedArgs[1]: word length limit
		 * parsedArgs[2]: delimiters
		 */
		try{
			String[] parsedArgs = Core.parseArgs(args);
			
			String fileName = parsedArgs[0];
			int wordLengthLowLimit = Integer.parseInt(parsedArgs[1]);
			String delimiters = parsedArgs[2];
			
			try{
				String avgSentenceLength = Core.computeAvgSentenceLength(fileName, wordLengthLowLimit, delimiters);
				System.out.println("Average sentence length: " + avgSentenceLength);
			}
			catch (FileNotFoundException e){
				System.out.println("Error: Could not open " + parsedArgs[0] + ": file not found");
				System.exit(1);
			}
			catch (IOException e){
				System.out.println("Error: " + e.getMessage());
				System.exit(1);
			}
		}
		catch (ParseException e){
			System.out.println("Error: "" + e.getMessage());
			System.exit(1);
		}
		catch (IllegalArgumentException e){ //these are thrown in the parseArgs method
			System.out.println("Error: " + e.getMessage());
			System.exit(1);
		}
		
		
		
	}
}
