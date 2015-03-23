package com.fizzpod.gradle.plugins.sweeney.rules

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SimpleRule implements Rule {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRule); 
	
	public static final String SIMPLE_TYPE_VALUE = "simple"

	@Override
	public String getType() {
		return SIMPLE_TYPE_VALUE;
	}

	@Override
	public boolean accept(RuleDefinition ruleDefinition) {
		if(ruleDefinition.hasAttribute(TYPE_ATTRIBUTE) && SIMPLE_TYPE_VALUE.equals(ruleDefinition.getAttribute(TYPE_ATTRIBUTE).call())) {
			return true;
		} 
		return false;
	}

	@Override
	public void validate(def scope, RuleDefinition ruleDefinition) {
		def value = ruleDefinition.getAttribute("value").call(); 
		def expect = ruleDefinition.getAttribute("expect").call();
		LOGGER.info("Checking value {} with expected {}", value, expect);
		assert value.equals(expect), 'Validation failed for rule definition' + ruleDefinition
	}

	@Override
	public String toString() {
		return getType();
	}
	

}
