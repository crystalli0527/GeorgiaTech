package edu.gatech.cs6310.scheduler;

import java.util.ArrayList;
import java.util.List;

public class Course {
	
	public Semester semesterStart;
	public int yearStart;
	public List<Student> students;

	/**
	 * Course Constructor
	 */
	public Course(Semester start, int year) {
		this.semesterStart = start;
		this.yearStart = year;
		this.students = new ArrayList<Student>();
	}
	
	public void registerStudent(Student student) {
		this.students.add(student);
	}
	
	public int getTotalStudents() {
		return this.students.size();
	}
}
