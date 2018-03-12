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
		String expect = ruleDefinition.getAttribute(EXPECT_ATTRIBUTE)?.call();
		String systemPropertyKey = String.valueOf(ruleDefinition.getAttribute(VALUE_ATTRIBUTE).call()); 
		LOGGER.info("Checking system property {} has value {}", systemPropertyKey, expect);
		if(expect?.trim()) {
			String value = System.getProperty(systemPropertyKey);
			assert expect.equals(value), Rule.DEFAULT_MESSAGE.format(ruleDefinition);
		} else {
			assert System.getProperties().containsKey(systemPropertyKey), Rule.DEFAULT_MESSAGE.format(ruleDefinition);
		}
				
	}

	@Override
	public String toString() {
		return getType();
	}
	

}
