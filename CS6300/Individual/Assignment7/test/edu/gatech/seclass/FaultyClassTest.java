package edu.gatech.seclass;

import edu.gatech.seclass.FaultyClass;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author rskelton7@gatech.edu
 * Robert Skelton
 */
public class FaultyClassTest extends FaultyClass {

	// 100% Statement Coverage
	@SuppressWarnings("deprecation")
	@Test
	public void method1SC() {
	    assertEquals(1.0, method1(1), .01);
	    assertEquals(1.0, method1(-1), .01);
	}
	
	// 100% Branch Coverage
	@Test
	public void method1BC() {
	    assertEquals(1.0, method1(1), .01);
	    assertEquals(1.0, method1(-1), .01);
	}
	
	// Fails on 100% Path Coverage
	@Test
	public void method1PC() {
	    assertEquals(1.0, method1(1), .01);
	    assertEquals(1.0, method1(-1), .01);	
	    
	 // divide by zero here, which would be the 3rd and final path
	 // really should be undefined, not infinity 
	    assertEquals(Double.POSITIVE_INFINITY, method1(0), .01); 
	}
	
	@Test
	public void method2PC() {
		// Not needed
	}

	@Test
	public void method2SC() {
		// Not needed
	}
	
	@Test
	public void method2BC() {
		// Not needed
	}
	
	@Test
	public void method4BC() {
	    assertEquals("10/a is less than 5 or greater than 10", method4(1));	
	    assertEquals("10/a is less than 10", method4(2));	
	}
	
	@Test
	public void method4SC() {
		assertEquals("10/a is less than 5 or greater than 10", method4(1));	
	    assertEquals("10/a is less than 10", method4(2));
	    assertEquals("Will error out because divided by 0!", method4(0));
	}

}
