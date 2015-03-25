package com.fizzpod.gradle.plugins.sweeney.rules

interface Rule {
	
	public static final String TYPE_ATTRIBUTE = "type"
	public static final String EXPECT_ATTRIBUTE = "expect"
	public static final String VALUE_ATTRIBUTE = "value"
	
	String getType();
	
	boolean accept(RuleDefinition ruleDefinition, def scope)
	
	void validate(RuleDefinition ruleDefinition, def scope)
	
}
