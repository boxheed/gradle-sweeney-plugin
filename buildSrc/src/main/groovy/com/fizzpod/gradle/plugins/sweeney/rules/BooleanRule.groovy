package com.fizzpod.gradle.plugins.sweeney.rules

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class BooleanRule implements Rule {

	private static final Logger LOGGER = LoggerFactory.getLogger(BooleanRule); 
	
	public static final String BOOLEAN_TYPE_VALUE = "bool"

	@Override
	public String getType() {
		return BOOLEAN_TYPE_VALUE;
	}

	@Override
	public boolean accept(RuleDefinition ruleDefinition, def scope) {
		if(ruleDefinition.hasAttribute(TYPE_ATTRIBUTE) && BOOLEAN_TYPE_VALUE.equals(ruleDefinition.getAttribute(TYPE_ATTRIBUTE).call())) {
			return true;
		} 
		return false;
	}

	@Override
	public void validate(RuleDefinition ruleDefinition, def scope) {
		boolean value = Boolean.valueOf(ruleDefinition.getAttribute(VALUE_ATTRIBUTE).call()); 
	
		LOGGER.info("Checking value {} is true", value);
		assert value, 'Validation failed for rule definition' + ruleDefinition
	}

	@Override
	public String toString() {
		return getType();
	}
	

}
