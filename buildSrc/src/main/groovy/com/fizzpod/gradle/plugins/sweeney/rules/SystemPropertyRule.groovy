package com.fizzpod.gradle.plugins.sweeney.rules

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SystemPropertyRule implements Rule {

	private static final Logger LOGGER = LoggerFactory.getLogger(SystemPropertyRule); 
	
	public static final String SYSTEM_PROPERTY_TYPE_VALUE = "sys"

	@Override
	public String getType() {
		return SYSTEM_PROPERTY_TYPE_VALUE;
	}

	@Override
	public boolean accept(RuleDefinition ruleDefinition, def scope) {
		if(ruleDefinition.hasAttribute(TYPE_ATTRIBUTE) && SYSTEM_PROPERTY_TYPE_VALUE.equals(ruleDefinition.getAttribute(TYPE_ATTRIBUTE).call())) {
			return true;
		} 
		return false;
	}

	@Override
	public void validate(RuleDefinition ruleDefinition, def scope) {
		String systemPropertyKey = String.valueOf(ruleDefinition.getAttribute(VALUE_ATTRIBUTE).call()); 
		String value = System.getProperty(systemPropertyKey);
		def expect = ruleDefinition.getAttribute("expect").call();
		LOGGER.info("Checking system property {} has value {}", systemPropertyKey, expect);
		assert expect.equals(value), 'Validation failed for rule definition' + ruleDefinition
	}

	@Override
	public String toString() {
		return getType();
	}
	

}
