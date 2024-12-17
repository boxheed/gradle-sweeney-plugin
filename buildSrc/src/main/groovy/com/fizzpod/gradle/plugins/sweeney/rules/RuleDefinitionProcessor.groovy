/* (C) 2024 */
/* SPDX-License-Identifier: Apache-2.0 */
package com.fizzpod.gradle.plugins.sweeney.rules

import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class RuleDefinitionProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuleRunner)

	private RuleLoader ruleLoader = new RuleLoader()

	private RuleDefinitionParserLoader ruleDefinitionParserLoader = new RuleDefinitionParserLoader()

	def process(def spec, def scope) {
		def definition = convertToDefinition(spec)
		def rule = matchDefinitionToRule(definition, scope)
		return [definition: definition, rule: rule]
	}

	private RuleDefinition convertToDefinition(def spec) {
		def convertedRuleDefinition = null
		LOGGER.debug("Using rules: {}", ruleDefinitionParserLoader.all())
		ruleDefinitionParserLoader.all().each { it ->
			LOGGER.info("Using parser: {}", it)
			if(convertedRuleDefinition == null) {
				convertedRuleDefinition = it.parse(spec)
			}
		}
		return convertedRuleDefinition
	}

	private Rule matchDefinitionToRule(def definition, def scope) {
		def rule = null
		ruleLoader.all().each { it ->
			LOGGER.info("Checking whether rule {} accepts definition {}", it.getType(), definition)
			if(it.accept(definition, scope)) {
				rule = it
			}
		}
		if(rule == null) {
			throw new IllegalArgumentException("Rule definition does not match any rule: " + definition)
		}
		return rule
	}
}
