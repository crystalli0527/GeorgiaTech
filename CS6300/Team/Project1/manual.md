.\" Manpage for cw
.\" Contact robertjskelton@gmail.com to correct errors or typos.
.TH man 8 "12 Feb 2016" "1.0" "countword man page"
.SH NAME
cw \- (countword) count the average number of characters per word in a sentence
.SH SYNOPSIS
.B cw
[\fB\-d\fR \fIdelimiters\fR]
[\fB\-l\fR \fIminimum_word_length\fR]
.IR file
.SH DESCRIPTION
cw is a Java program to determine the average number of characters per word in a sentence. 
.br
This is used to improve the essay-writing skills of Lauren's students.
.SH OPTIONS
\-d Specify alternative delimiters. Defaults are "!?.:;" 
.br
\-l Specify minimum word length to consider in the average. Default is 4.

.SH EXAMPLES
cw -d "!?.:" -l 5 file
.br
cw file -d !
.br
cw -l 20 -d "!" file
.SH AUTHORS
Nathan Morcos (nathanmorcos@gmail.com)
.br
Alexander Trusov (alxtrsvm@gmail.com)
.br
Matthew Hernandez (matt.hernandez82@gmail.com)
.br
Robert Skelton (robertjskelton@gmail.com)

.SH TO COMPILE
create a directory for class files (out/ in this case)
.br
javac -d out/ src/edu/gatech/seclass/project1/*.java src/org/apache/commons/cli/*.java

.SH TO RUN
java -cp out/ edu.gatech.seclass.project1.Wc test.txt
.br
If you cd to out/, you can omit -cp out/ and run it with:
.br
java edu.gatech.seclass.project1.Wc test.txt


.SH MISC
Alternatively, .classpath and .project files are provided under Project1/ for easy Eclipse project imports. The test txt files for the test cases are located in the same directory with CoreTest.java so the project's directory needs to be changed to Project1/test/edu/gatech/seclass/project1

.br
To do so:
run configurations -> arguments -> working directory -> workspace... -> select Project1/test/edu/gatech/seclass/project1
