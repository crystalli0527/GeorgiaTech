package edu.gatech.cs6310.scheduler;

public class Policy {
	
	private int totalSemAvailable;
	private int totalCoursesAvailable;
	private int maxCoursesSem;
	
	/**
	 * Course Constructor
	 */
	public Policy() {
		this.totalSemAvailable = 12;
		this.totalCoursesAvailable = 18;
		this.maxCoursesSem = 2;
	}
	
	public Policy(int tSemAvailable, int maxCoursesSem) {
		this.totalSemAvailable = tSemAvailable;
		this.totalCoursesAvailable = 0;
		this.maxCoursesSem = maxCoursesSem;
	}
	
	public void incrementCoursesAvailable() {
		this.totalCoursesAvailable++;
	}
	
	public int getTotalSemAvailable() {
		return this.totalSemAvailable;
	}
	
	public int getTotalCoursesAvailable() {
		return this.totalCoursesAvailable;
	}
	
	public int getMaxCoursesSem() {
		return this.maxCoursesSem;
	}
	
	public void setTotalSemAvailable(int sem) {
		this.totalSemAvailable = sem;
	}
	
	public void setMaxCoursesSem(int max) {
		this.maxCoursesSem = max;
	}
	
	public void setTotalCoursesAvailable(int courseNums) {
		this.totalCoursesAvailable = courseNums;
	}
}
