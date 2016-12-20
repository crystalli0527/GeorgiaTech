package edu.gatech.cs6310.scheduler;

import java.util.ArrayList;
import java.util.List;

public class Student {
	
	public int studentId;
	public List<StudentInterest> interests;
	public int yearsEnrolled;

	/**
	 * Student Constructor
	 */
	public Student(int id, int yearsEnrolled) {
		this.studentId = id;
		this.yearsEnrolled = yearsEnrolled;
		this.interests = new ArrayList<StudentInterest>();
	}
	
	public void addInterest(Title interest) {	
		StudentInterest studentInterest = new StudentInterest(interest);
		this.interests.add(studentInterest);
	}
	
	public void addInterest(Title interest, boolean systemAdded) {	
		StudentInterest studentInterest = new StudentInterest(interest, systemAdded);
		this.interests.add(studentInterest);
	}
	
	public List<Integer> getInterestedTitleIds() {
		List<Integer> ids = new ArrayList<Integer>();
		for (StudentInterest t : interests) {
			ids.add(t.title.courseId);
		}
		return ids;
	}
	
	public Title getTitleById(int id) {
		return interests.get(id).title;
		
	}
}
