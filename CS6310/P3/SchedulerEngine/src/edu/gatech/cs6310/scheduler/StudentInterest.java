package edu.gatech.cs6310.scheduler;

import java.util.Date;

public class StudentInterest {
	
	public Title title;
	public boolean addedBySystem;
	public Date completedOn;
	
	
	/**
	 * StudentInterest Constructor
	 */
	public StudentInterest(Title title) {
		this.title = title;
		this.addedBySystem = false;
		this.completedOn = null;
	}
	public StudentInterest(Title title, boolean systemAdd) {
		this.title = title;
		this.addedBySystem = systemAdd;
		this.completedOn = null;
	}
	
	public void completedOn(Date date) {
		this.completedOn = date;
	}
}
