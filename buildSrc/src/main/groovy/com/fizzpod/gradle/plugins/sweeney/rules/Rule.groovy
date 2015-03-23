package com.fizzpod.gradle.plugins.sweeney.rules

interface Rule {
	
	String getName()
	
	boolean accept()
	
	boolean validate()
	
}
