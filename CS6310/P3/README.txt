#README.txt

Provided as required by Extra/Clarifying Instructions for VM Submission/Upload on Project 3
More information on how to setup the environment and all connectors is provided at the bottom of the Design Document

##Locations of Source Code:

Front end code (HTML webpages and Javascript) are in P3/Frontend

Backend, Gurobi interacting code is in P3/SchedulerEngine/src
The README.md has more information about these Java files. 
The main Java files you want to look
at are under P3/SchedulerEngine/src/edu/gatech/cs6310/scheduler, 

##Locations of database schemas:

Database (MySQL) files are located in P3/ARMS_Rest_Services
Project3-ARMS.sql creates the schema for ARMS_DB
Project3-Data.sql loads initial state data into database

##Locations of extra data files:

mysql-connector-java-5.1.38-bin.jar, which is the Java Database Connectivity (JDBC) driver for MySQL
ARMS_Rest_Services.war, which is the Tomcat WAR file

How to setup the ARMS Environment

#Deployment Steps

##Import MySQL Database

We designed our system to contain the database schema on the MySQL database instance of our virtual machine image.  
To load our database schema, please use the PHPMyAdmin console and import the following files in order

Project3-ARMS.sql - Creates schema for ARMS_DB
Project3-Data.sql - Loads initial state data into database

Create a user in the PHPMyAdmin console named "admin" with password of "password".  
Ensure that user has all privileges for all ARMS_DB tables.

##Tomcat Engine

In order to host our application tier, we chose the Apache Tomcat engine.  
This application server should be loaded onto the virtual machine image with the following steps.

On the virtual machine image, create the directory ~/opt/tomcat
Owner should be ubuntu, if not, chown that directory
Download Tomcat version 7.070 from
https://tomcat.apache.org/download=70.cgi
Select the binary distribution for CORE tar.gz
Copy that file into ~/opt/tomcat
Run tar zxvf apache.tomcat-7.0.70.tar.gz -C ~/opt/tomcat

Edit ~/.bashrc and add the following lines to the end of the file
export CATALINA_HOME=/home/ubuntu/opt/tomcat/apache-tomcat-7.0.70
export JAVA_HOME=/usr/lib/jvm/java-8-oracle/jre

Edit the file ~/opt/tomcat/apache-tomcat-7.0.70/conf/tomcat-users.xml
Add the line 
<user username=”admin” password=”password” roles=”manager-gui, admin-gui”/>
Note: The double quotes need to be typed in manually. 
If copied and pasted from this document, they will not be interpreted correctly
Save the file

Copy the file mysql-connector-java-5.1.38-bin.jar into the directory ~/opt/tomcat/apache-tomcat-7.0.70/lib.  
This is the JDBC driver for MySQL.

Copy the file gurobi.jar into the directory ~/opt/tomcat/apache-tomcat-7.0.70/lib.  
This is the Gurobi Library necessary for the SchedulerEngine.

Start up Tomcat by running
~/opt/tomcat/apache-tomcat-7.0.70/bin/startup.sh
Ensure that the server starts by watching the output of the command on the console

Open the browser on your virtual machine to http://localhost:8080/
The admin console will appear on your browser if all is well
Select the Manager App button on the web page
uid=admin password=password

Scroll down to WAR file deploy and browse to the file
ARMS_Rest_Services.war which we included in our release
Select Deploy button

At this point, the RESTful services will work.  You can test by calling the following URL.
http://localhost:8080/ARMS_Rest_Services/api/CourseService/fetchCourses

A list of all of the courses loaded in the database should be displayed on the screen.
Front End User Interface

We have submitted our User Interface pages in our source tree under the Frontend directory.  
To install this site, please copy the files in this directory to $TOMCAT_ROOT/webapps under 
a new directory entitled ARMS.  The URL for the web site with then be:

http://localhost:8080:/ARMS

JSON Examples for Testing RESTful Service

Using the Firefox browser, browse to the RESTClient plugin and install that plugin on your browser.  
This tool provides a way to create various JSON requests and save them.  
Open this tool and select Favorite Requests / Import Favorite Request.  
Select the file JSON Requests.json for a few example JSON requests.



