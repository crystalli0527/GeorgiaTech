# **Requirements Document -- Student Average Sentence Length Tool**

Author: Team 52

##1 User Requirements


**Problem Statement:** The Word Count (WC) software tool is a command line program providing the average word count, per sentence, within a submitted essay. Lauren's students will use WC and compare its output to Lauren's recommended average for proper sentence length. The project goal is the WC program will improve students' writing style by allowing them to objectively determine if sentence word count is excessive.

**Requirements Scope:** Team 52 will gather the requirements, analyse, design, develop, implement, test and integrate the program according to the guidelines stated within this requirements document. Completed program will be delivered to Lauren via compiled Java file with user manual.

**Intended Users:** The intended users of WC will Lauren's students, with varying degrees of computer experience and technical proficiency.

**Expected use:** The Students will run WC via console command line. The program will require input files with optional word length and/or sentence delimiter parameters. WC will analyze submitted essays containing varying number of sentences, with varying sentence length. Successful processing will result in average sentence length displayed on user's console. 

###1.1 Software Interfaces

WC is an executable command line Java program and shall require JDK 1.7 for development.


###1.2 User Interfaces

Students will use command line to interact with WC program. To run the software students need to type the software name in a terminal and specify a path to a file that needs to be analyzed by the tool as well as optional arguments to indicate minimum word length (-l) and sentence delimiters (-d). The default values for minimum word length and sentence delimiters will be used if option arguments are omitted. Appropriate error messages will be displayed in case of misconfiguration or incorrect input.

###1.2.1 User Interaction Examples:

The following use cases display valid user input to execute WC on the command line:<br />
 - Use Case - Default settings, no user provided flags:<br />
java -cp 'path to the bin directory' edu.gatech.seclass.project1.WC C:/user/essays/myessay.doc<br />
 - Use Case - With word length configured to value of 6:<br />
java -cp 'path to the bin directory' edu.gatech.seclass.project1.WC -1 6 C:/user/essays/myessay.doc<br />
 - Use Case - With sentence delimiter configured to }, /, and | characters:<br />
java -cp 'path to the bin directory' edu.gatech.seclass.project1.WC C:/user/essays/myessay.doc -d }/|<br />
 - Use Case - With sentence delimiter configured to }, /, and | characters, and word length configured to value of 6:<br />
java -cp 'path to the bin directory' edu.gatech.seclass.project1.WC -l 6 C:/user/essays/myessay.doc -d }/|<br />

###1.3 User Characteristics


####1.3.1 User Type: Students

####1.3.2 User Attributes:

- College level
- Number of Students
	- 45 per unit or section
	- 6 sections per semester
- Computer skills:
	- From no technical experience
	- Up to high technical proficiency
- Familiarity with PCs
	- Some who do not program
	- Some who do program


##2 System Requirements


###2.1 Functional Requirements

- Command Line 
	- After program is compiled, it should run via the following command line:<br />
	  java -cp <path to the bin directory> edu.gatech.seclass.project1.WC <arguments to WC>
	- The "-d" flag denotes a user provided, delimiter parameter.
	- The delimiter parameter should accept multiple delimiters and does not require separator.
	- There are no invalid delimiter characters.
	- The "-l" flag denotes a user specified, word length parameter (int value).
	- Word length is inclusive, such as when user enters 5, then all words with 5 or more characters will be counted as a word.
	- User provided flag parameters shall override defaults.
	- If flag parameters are not provided by user, then defaults are used.
	- The path+filename string is a required parameter.
	- The program shall accept an absolute or relative path within the path+filename parameter
	- Parameters shall be accepted in any order.
	- Example of parameter usage with absolute path parameter:<br />
	  java -cp <path to the bin directory> edu.gatech.seclass.project1.WC C:/user/essays/myessay.doc -d $/| -l 5
	- Parameters can only be inputted once, per program execution.
- Input File
	- File with any extension shall be accepted as input file (including .txt, .doc, gif, jpeg, etc).
	- Any language shall be considered acceptable input.
	- All content within input file shall be processed.
	- Program shall only process one input file per execution.
- Word
	- Program shall default word length to four or more consecutive ASCII characters.
	- No maximum character limit for words.
	- Program shall consider space, tab, newline, and sentence delimiters as word delimiters.
	- Delimiters shall not be counted as part of words or sentences, or included in word/sentence calculations.
- Sentence
	- Program shall consider words within delimiters, default and/or user provided, as a sentence.
	- Sentence delimiters used in any grammatical condition shall designate a sentence, such as: “Mr.”
	- Line breaks and newlines, unless user specified on command line, shall not count as sentence delimiters.
	- Default sentence delimiter characters shall be:  ; . ? ! : 
	- Sentences of with zero words shall not count as a sentence.
	- File with at least one delimiter present and one or more words shall be counted as a sentence.
	- Program shall not require grammar rules for sentence processing.
	- If no sentence delimiters are found in file,  but contains 4 or more words, then should be counted as one sentence.
	- Delimiters shall not be counted as part of words or sentences, or included in word/sentence calculations.
- Output
	- Upon successful processing, program shall output to console average sentence length for file.
	- Average sentence length shall be calculated by the sum of all sentence word counts then divided by number of 			  sentences within input file.
	- Output will be double/real number rounded to two decimal points. 
	- If input file sentences all contain zero words, then program shall output 0.00 for average.
	- Program shall use Java standard output command to console.
- Error Handling
	- Program shall output all error messages to console.
	- Program shall use Java standard error output command to console.	
	- Program shall output error message if parameter flags possess no corresponding values.
	- Program shall output error message for invalid flags.
	- Program shall output error message if file cannot be found.
	- Program shall output error message if file cannot be read.
	- Program shall output error message for caught exceptions.
	- Error message shall be formatted as following: “ERROR: Error message example”
- MISC
	- Program performance:  5000 char per second processing speed
	- User manual shall be provided.
	- User manual shall not instruct installation.
	- Program shall be developed with JDK 1.7 or above..

###2.2 Non-Functional Requirements

1. Tested on a machine with a Vanilla installation of Java 1.7.
2. Executable from command line; should be an application, with a main method and executable from the command line using the java command.
4. If any other libraries are included, the entire source code should be included.
5. Able to implement file size limit, but not required. 
6. All error message should be user-friendly output to console



