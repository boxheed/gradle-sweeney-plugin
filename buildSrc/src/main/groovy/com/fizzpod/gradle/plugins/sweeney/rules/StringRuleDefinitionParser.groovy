package com.fizzpod.gradle.plugins.sweeney.rules

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class StringRuleDefinitionParser implements RuleDefinitionParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(StringRuleDefinitionParser);

	def RuleDefinition parse(def definition) {
		RuleDefinition ruleDefinition = null;
		if(definition instanceof String) {
			LOGGER.info("Parsing definition: {}", definition)
			ruleDefinition = new RuleDefinition(buildDefinition(definition));
		}
		return ruleDefinition;
	}

	def buildDefinition(String definition) {
		def newDefinition = [:];
		
		String[] parts = definition.split("[:]");
		
		assert parts.length <= 3, 'Definition only supports maximum of 3 parts: ' + parts
		
		parts.eachWithIndex() { obj, index ->
			LOGGER.info("index: {}, obj: {}", index, obj);
			switch(index) {
				case 0: newDefinition.put("type", {obj}); break;
				case 1: newDefinition.put("expect", {obj}); break;
				case 2: newDefinition.put("value", {obj}); break;
			}
		}
		
		return newDefinition;
	}
}
