To compile:
	create a directory for class files (out/ in this case)
	javac -d out/ src/edu/gatech/seclass/project1/*.java src/org/apache/commons/cli/*.java

To run:
	java -cp out/ edu.gatech.seclass.project1.Wc test.txt

	if you cd to out/, you can omit -cp out/ and run it with:
	java edu.gatech.seclass.project1.Wc test.txt


Alternatively, .classpath and .project files are provided under Project1/ for easy Eclipse project imports.
The test txt files for the test cases are located in the same directory with CoreTest.java so the project's directory needs to be changed to Project1/test/edu/gatech/seclass/project1

To do so:
	run configurations -> arguments -> working directory -> workspace... -> select Project1/test/edu/gatech/seclass/project1

To display the man page:
	cd into the directory where manual.md is: cd 6300Spring16Team52/Project1
	man ./manual.md
	q exits the man page viewer
	
