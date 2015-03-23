package com.fizzpod.gradle.plugins.sweeney.rules;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class RuleRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuleRunner);

	private RuleLoader ruleLoader = new RuleLoader();

	private RuleDefinitionParserLoader ruleDefinitionParserLoader = new RuleDefinitionParserLoader();

	private RunMode runMode = RunMode.ENFORCE;

	public RuleRunner(RunMode mode) {
		runMode = mode;
	}


	public void applyRules(def scope, def ruleDefinitions) {

		ruleDefinitions.each { ruleDefinition ->
			LOGGER.debug("Applying rule: {}", ruleDefinition);
			convertAndApplyRule(scope, ruleDefinition);
		}
	}

	void convertAndApplyRule(def scope, def ruleDefinition) {
		def convertedRuleDefinition = null;
		new RuleDefinitionParserLoader().all().each { it ->
			LOGGER.debug("Using parser: {}", it)
			if(convertedRuleDefinition == null) {
				convertedRuleDefinition = it.parse(ruleDefinition);
			}
		}
		if(convertedRuleDefinition != null) {
			applyRule(scope, convertedRuleDefinition);
		} else {
			LOGGER.error("Unable to parse rule: {}", ruleDefinition);
			throw new IllegalArgumentException("Unable to parse rule: " + ruleDefinition);
		}
	}

	void applyRule(def scope, def ruleDefinition) {
		ruleLoader.all().each { it ->
			LOGGER.info("Checking whether rule {} accepts definition {}", it.getType(), ruleDefinition);
			if(it.accept(ruleDefinition)) {
				LOGGER.info("Testing definition {} with rule {}", ruleDefinition, it.getType())
				try {
					it.validate(scope, ruleDefinition);
				} catch(AssertionError e) {
					if(RunMode.ENFORCE == runMode) {
						throw e;
					} else {
						LOGGER.warn("Warning: {}", e.getMessage());
					}
				}
			} else {
				LOGGER.debug("Rule {} rejected definition {}", it.getType(), ruleDefinition);
			}
		}
	}
}
