package com.fizzpod.gradle.plugins.sweeney.rules

interface Rule {
	
	static final String TYPE_ATTRIBUTE = "type"
	static final String EXPECT_ATTRIBUTE = "expect"
	static final String VALUE_ATTRIBUTE = "value"
	static final String DESCRIPTION_ATTRIBUTE = "description"
	
	String getType();
	
	boolean accept(RuleDefinition ruleDefinition, def scope)
	
	void validate(RuleDefinition ruleDefinition, def scope)
	
}
