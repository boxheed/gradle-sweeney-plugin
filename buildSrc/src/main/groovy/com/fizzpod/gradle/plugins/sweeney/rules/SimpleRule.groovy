package com.fizzpod.gradle.plugins.sweeney.rules

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class SimpleRule implements Rule {

	private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRule); 
	
	public static final String TYPE_SIMPLE = "simple"

	@Override
	public String getType() {
		return TYPE_SIMPLE;
	}

	@Override
	public boolean accept(RuleDefinition ruleDefinition) {
		if(ruleDefinition.hasAttribute("type") && TYPE_SIMPLE.equals(ruleDefinition.getAttribute("type").call())) {
			return true;
		} 
		return false;
	}

	@Override
	public boolean validate(def scope, RuleDefinition ruleDefinition) {
		def value = ruleDefinition.getAttribute("value").call(); 
		def expected = ruleDefinition.getAttribute("expect").call();
		LOGGER.info("Testing {} with {} rule", ruleDefinition, getType());
		return value.equals(expected);
	}

	@Override
	public String toString() {
		return getType();
	}
	

}
