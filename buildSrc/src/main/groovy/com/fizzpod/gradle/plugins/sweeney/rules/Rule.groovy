package com.fizzpod.gradle.plugins.sweeney.rules

interface Rule {
	
	static final String TYPE_ATTRIBUTE = "type"
	static final String EXPECT_ATTRIBUTE = "expect"
	static final String VALUE_ATTRIBUTE = "value"
	static final String DESCRIPTION_ATTRIBUTE = "description"
	static final String WHEN_ATTRIBUTE = "when"
	static final String MSG_ATTRIBUTE = "msg"
	static final String WHEN_NOW_VALUE = "now"

	static final RuleMessageFormatter DEFAULT_MESSAGE = new RuleMessageFormatter('Validation failed for rule definition: $ruleDefinition');
	
	String getType();
	
	boolean accept(RuleDefinition ruleDefinition, def scope)
	
	void validate(RuleDefinition ruleDefinition, def scope)

	boolean isRunNow(RuleDefinition ruleDefinition)


}
