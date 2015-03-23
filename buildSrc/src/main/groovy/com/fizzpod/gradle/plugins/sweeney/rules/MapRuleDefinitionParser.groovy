package com.fizzpod.gradle.plugins.sweeney.rules

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MapRuleDefinitionParser implements RuleDefinitionParser {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapRuleDefinitionParser);

	def RuleDefinition parse(def definition) {
		RuleDefinition ruleDefinition = null;
		if(definition instanceof Map) {
			LOGGER.info("Parsing definition: {}", definition)
			ruleDefinition = new RuleDefinition(rebuildDefinition(definition));
		}
		return ruleDefinition;
	}

	def rebuildDefinition(Map definition) {
		def newDefinition = [:];
		definition.each{ k, v ->
			def value = v;
			if(!( v instanceof Closure)) {
				value = {v}
			}
			newDefinition.put(k, value);
		}
		return newDefinition;
	}
}
