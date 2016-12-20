# Gurobi Scheduler

In the ./src/edu/gatech/cs6310/scheduler directory, there will be 2 primary classes responsible for instantiating all other classes.

These are: Scheduler.java and SchedulerEngine.java

## Scheduler.java

This class contains a main method that is used mainly for testing the engine by passing it CSV files from the file system.
It will perform the same logic done by the SchedulerEngine.java class, however the files will first be converted by submethods
into JSON objects.

## SchedulerEngine.java

This class has a public constructor that takes in 1 argument, that is the single JSON object that contains all the
required information originally read from the CSV files. This class should be instantiated whenever there is a need
to pass in a JSON object to the engine.

The method SchedulerEngine.run() will begin the logic that the Scheduler.java method would also perform. It will return a generated schedule in JSON with the following structure:
```json
{ 
	 "studentResults" : [
	   {
	      "studentId" : "12345",
	      "courses" : [
	         {
	            "courseId" : "1",
	            "semester" : "1"
	         }
	      ]
	   }
	 ]
}
```

## JSON structure

In order to correctly execute the SchedulerEngine.run() method, the JSON argument must be in the following structure.

```json
{
	"courseTitles" : [
	   {
	      "courseId" : "1",
	      "courseName" : "class",
	      "maxSize" : "3",
	      "isOfferedSpring" : true,
	      "isOfferedSummer" : false,
	      "isOfferedFall" : true
	   },
	   {
	      "courseId" : "2",
	      "courseName" : "class",
	      "maxSize" : "5",
	      "isOfferedSpring" : true,
	      "isOfferedSummer" : false,
	      "isOfferedFall" : true
	   }
	],
	"studentInterests" : [
	   {
	      "studentId" : "1",
	      "yearsEnrolled" : "3",
	      "courses" : [
	   	     {"id":"1"},{"id":"2"},{"id":"3"}
	   	  ]
	   }
	],
	"titlePreReqs" : [
   	   {
      	  "preReq" : "1",
      	  "forTitleId" : "2"
   	   }
   	],
   	"policies" :
		   {
		      "maxCoursesPerSem" : "3"
		   }
}
```
