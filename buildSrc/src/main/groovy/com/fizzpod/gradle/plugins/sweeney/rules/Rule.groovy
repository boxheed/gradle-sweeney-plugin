package com.fizzpod.gradle.plugins.sweeney.rules

interface Rule {
	
	public static final String TYPE_ATTRIBUTE = "type"
	
	String getType();
	
	boolean accept(RuleDefinition ruleDefinition)
	
	void validate(def scope, RuleDefinition ruleDefinition)
	
}
