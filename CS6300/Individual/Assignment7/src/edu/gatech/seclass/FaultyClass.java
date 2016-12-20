package edu.gatech.seclass;

import java.nio.file.Path;

/**
 * @author rskelton7@gatech.edu
 * Robert Skelton
 */

public class FaultyClass {

	//contains a division by zero fault such that 
	//(1) it is possible to create a test suite that achieves 100% statement coverage 
	//and does not reveal the fault, (2) it is possible to create a test suite that 
	//achieves 100% branch coverage and does not reveal the fault, and 
	//(3) every test suite that achieves 100% path coverage reveals the fault.
	public static double method1(int a) {
		double tmp = 0;
		if (a > 0) {
			tmp = 1 / a;
		}
		else if (a < 0) {
			tmp = -1 / a;
		}
		return tmp;
	}
	
	//contains a division by zero fault such that
	//(1) it is possible to create a test suite that achieves 100% path coverage 
	//and does not reveal the fault, (2) it is possible to create a test suite 
	//that achieves 100% statement coverage and does not reveal the fault, and 
	//(3) it is possible to create a test suite that achieves 100% branch coverage, 
	//achieves 100% statement coverage, does not achieve 100% path coverage, and reveals the fault.
	public void method2() {
		/*This is not possible
		This is because if tests cover 100% branch and statement coverage, this encompasses 
		100% Path coverage, then there is no way for requirement (3) to fulfill the 100% path coverage.*/
		
	}

	//contains a divide by zero fault such that it is 
	//not possible to create a test suite that reveals the fault.
	public static double method3(int a) {
		int a_in = a;
		int tmp1 = 14;
		int tmp2 = 18;
		if (tmp1 > tmp2) {
			System.out.println("Should never get here");
			return a_in / 0;
		} else {
			System.out.println("it got here");
			return a_in;
		}
	
	}

	//contains a division by zero fault such that (1) it is possible to 
	//create a test suite that achieves 100% branch coverage and does not 
	//reveal the fault, and (2) every test suite that achieves 100% statement coverage reveals the fault.
	public String method4(int a) {
		String tmp1 = "";
		if (10/a > 5) {
			tmp1 = "10/a is greater than 5\n";
		}
		if (10/a < 10) { 
			tmp1 += "10/a is less than 10";
			return tmp1;
		}
		String tmp2 = "10/a is less than 5 or greater than 10";
		return tmp2;
	}
}
