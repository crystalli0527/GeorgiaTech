package edu.gatech.cs6310.scheduler;

import java.util.HashMap;

public class Catalog {
	
	public HashMap<String, Title> courseTitles;
	
	/**
	 * Course Constructor
	 */
	public Catalog() {
		this.courseTitles = new HashMap<String, Title>();
	}
	
	public void addCourseTitle(Title title) {

		if (!courseTitles.containsKey(title.courseName)) {
			courseTitles.put(String.valueOf(title.courseId), title);
		}
		else {
			System.out.println("Title " + title.courseName + " already exists");
		}
	}
}
