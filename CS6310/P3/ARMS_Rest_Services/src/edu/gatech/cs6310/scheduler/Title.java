package edu.gatech.cs6310.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.gatech.cs6310.services.RequestService;

public class Title {
	private static Logger log = Logger.getLogger(RequestService.class.getName());
	
	public int courseId;
	public String courseName;
	public List<Title> preReqs;
	public List<Course> courses;
	public int maxClassSize;
	public boolean isOfferedSpring;
	public boolean isOfferedFall;
	public boolean isOfferedSummer;

	/**
	 * Title Constructor
	 */
	public Title(int courseId, String courseName, int maxClassSize) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.maxClassSize = maxClassSize;
		this.preReqs = new ArrayList<Title>();
		this.courses = new ArrayList<Course>();
		this.isOfferedFall = false;
		this.isOfferedSpring = false;
		this.isOfferedSummer = false;
	}
	
	public void addPreReq(Title title) {
		if (title != null && title.courseId != this.courseId) {
			log.info("Added PreReqId: " + title.courseId + "; Name:" + title.courseName);
			this.preReqs.add(title);	
		}
		else if (title == null) {
			log.warning("Failed to add null title");
		}
		else {
			log.warning("Failed to add title prepreq with same courseID");
		}
	}
	
	public void isOfferedDuring(Semester sem) {
		if (sem == Semester.FALL) {
			this.isOfferedFall = true;
		}
		else if (sem == Semester.SPRING) {
			this.isOfferedSpring = true;
		}
		else {
			this.isOfferedSummer = true;
		}
	}
	
	public static List<Integer> getAncestorPreReqs(Title title) {
		List<Integer> out = new ArrayList<Integer>();
		List<Title> root = title.preReqs;
		if (root.size() < 1) {
			out.add(title.courseId);
			return out;
		}

		for (Title t : root) {
			out.addAll(Title.getAncestorPreReqs(t));
			out.add(t.courseId);
		}
		return out;
	}
}
