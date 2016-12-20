/**
 * 
 */
package edu.gatech.cs6310.scheduler;

import java.util.HashMap;
import java.util.logging.Logger;

import edu.gatech.cs6310.services.RequestService;

public class Catalog {
	private static Logger log = Logger.getLogger(RequestService.class.getName());
	
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
			log.info("Added TitleId: " + title.courseId + "; Name: " + title.courseName); 
		}
		else {
			log.warning("Title " + title.courseName + " already exists");
			System.out.println("Title " + title.courseName + " already exists");
		}
	}
}
