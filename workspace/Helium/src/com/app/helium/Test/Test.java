package com.app.helium.Test;

import java.util.ArrayList;

public class Test {

	protected ArrayList<ITestable> testable_units_register = new  ArrayList<ITestable>();
	
	public Test() {
		//NR: Register all Unit Tests here
		this.testable_units_register.add(new ContactTest());   // Contacts test
		this.testable_units_register.add( new SyncManagerTest());  // SyncManager test
	}

	public void run() {
		for(ITestable testable_units_register : this.testable_units_register) {
			testable_units_register.runTest();
		}
		
	}
}
