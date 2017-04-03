package com.example.service;

public class Properties {
	
	public static final String portName;
	public static final String portName2;
	
	static { 
		portName = System.getProperty("portName");
		portName2 = System.getProperty("portName2");

	}

}
