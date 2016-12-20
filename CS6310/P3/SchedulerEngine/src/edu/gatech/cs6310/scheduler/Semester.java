package edu.gatech.cs6310.scheduler;


/**
 * Enumeration of Semesters 
 */
public enum Semester {
	
	FALL(0),
	SPRING(1),
	SUMMER(2);
	
	private final int semester;
	
	Semester(int semester) {
		this.semester = semester;
	}
	public int getSemesterId() {
		return this.semester;
	}
	
	public static Semester getSemester(int semester) {
		Semester[] values = Semester.values();
		for (int i = 0; i < values.length; i++) {
			if (values[i].getSemesterId() == semester) {
				return values[i];
			}
		}
		// TODO: This is bad practice, need error handling to catch invalid semester id
		return FALL;
	}
}
