package com.fizzpod.gradle.plugins.sweeney.rules

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class EqualRule extends AbstractRule implements Rule {

	private static final Logger LOGGER = LoggerFactory.getLogger(EqualRule); 
	
	public static final String SIMPLE_TYPE_VALUE = "equal"

	@Override
	public String getType() {
		return SIMPLE_TYPE_VALUE;
	}

	@Override
	public boolean accept(RuleDefinition ruleDefinition, def scope) {
		if(ruleDefinition.hasAttribute(TYPE_ATTRIBUTE) && SIMPLE_TYPE_VALUE.equals(ruleDefinition.getAttribute(TYPE_ATTRIBUTE).call())) {
			return true;
		} 
		return false;
	}

	@Override
	public void validate(RuleDefinition ruleDefinition, def scope) {
		def value = ruleDefinition.getAttribute("value").call(); 
		def expect = ruleDefinition.getAttribute("expect").call();
		LOGGER.info("Checking value {} with expected {}", value, expect);
		assert value.equals(expect), Rule.DEFAULT_MESSAGE.format(ruleDefinition);
	}

	@Override
	public String toString() {
		return getType();
	}
	

}
