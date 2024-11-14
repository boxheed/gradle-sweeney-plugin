package com.fizzpod.gradle.plugins.sweeney.rules

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PatternRule extends AbstractRule implements Rule {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatternRule); 
	
	public static final String PATTERN_TYPE_VALUE = "pattern"

	@Override
	public String getType() {
		return PATTERN_TYPE_VALUE;
	}

	@Override
	public boolean accept(RuleDefinition ruleDefinition, def scope) {
		if(ruleDefinition.hasAttribute(TYPE_ATTRIBUTE) && PATTERN_TYPE_VALUE.equals(ruleDefinition.getAttribute(TYPE_ATTRIBUTE).call())) {
			return true;
		} 
		return false;
	}

	@Override
	public void validate(RuleDefinition ruleDefinition, def scope) {
		String value = String.valueOf(ruleDefinition.getAttribute("value").call()); 
		String expect = ruleDefinition.getAttribute("expect").call();
		
		LOGGER.info("Checking value {} with expected {}", value, expect);
		assert value.matches(expect), Rule.DEFAULT_MESSAGE.format(ruleDefinition);
	}

	@Override
	public String toString() {
		return getType();
	}
	

}
