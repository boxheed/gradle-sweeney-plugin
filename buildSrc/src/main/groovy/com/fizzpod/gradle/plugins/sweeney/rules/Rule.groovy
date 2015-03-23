package com.fizzpod.gradle.plugins.sweeney.rules

interface Rule {
	
	String getType();
	
	boolean accept(RuleDefinition ruleDefinition)
	
	boolean validate(def scope, RuleDefinition ruleDefinition)
	
}
