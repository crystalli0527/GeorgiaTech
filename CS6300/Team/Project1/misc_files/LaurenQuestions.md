# **Questions for Lauren:** #

##I. Agreed to Questions ##

Add questions here we've all agreed to ask.

##II. Possible Questions ##
1. Input
	- Arguments (./tool file.txt -t 4 -d ".!?") vs input propmts when arguents are specified:
		$ ./tool file.txt -t -d
		$ please specify maximum word lenght: 4
		$ please specify delimiters: .?!
2. Additional features
	- Registration
	- Other features etc.
3. What error codes?
	- What should produce an error code?
	- What text should show with each error code?
	- Should we check the correctness of the delimiter input string to restrict users using only special characters and not letters (throw an error if user inputs ".!?z" saying that z is a letter and thus is not a correct delimiter)
4. Input format
	- You mentioned raw text file, is this only .txt files?
	- Other file extensions to account for
5. Text formatting
	- Is text formatted as ASCII/Unicode/UTF-8?
	- I don't know much about the differences, or if it would have to be handled differently in our code, but it might be worth asking
6. How many inputs to take at once?
	- Just one text file?
	- Multiple text files?
	- A zip file or a folder?
 
