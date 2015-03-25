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


	public void applyRules(def ruleDefinitions, def scope) {

		ruleDefinitions.each { ruleDefinition ->
			LOGGER.debug("Applying rule: {}", ruleDefinition);
			convertAndApplyRule(ruleDefinition, scope);
		}
	}

	private void convertAndApplyRule(def ruleDefinition, def scope) {
		def convertedRuleDefinition = null;
		new RuleDefinitionParserLoader().all().each { it ->
			LOGGER.debug("Using parser: {}", it)
			if(convertedRuleDefinition == null) {
				convertedRuleDefinition = it.parse(ruleDefinition);
			}
		}
		if(convertedRuleDefinition != null) {
			applyRule(convertedRuleDefinition, scope);
		} else {
			LOGGER.error("Unable to parse rule: {}", ruleDefinition);
			throw new IllegalArgumentException("Unable to parse rule: " + ruleDefinition);
		}
	}

	private void applyRule(def ruleDefinition, def scope) {
		boolean accepted = false;
		ruleLoader.all().each { it ->
			LOGGER.info("Checking whether rule {} accepts definition {}", it.getType(), ruleDefinition);
			if(it.accept(ruleDefinition, scope)) {
				accepted = true;
				LOGGER.info("Testing definition {} with rule {}", ruleDefinition, it.getType())
				try {
					it.validate(ruleDefinition, scope);
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
		if(!accepted) {
			throw new IllegalArgumentException("Rule definition does not match any rule: " + ruleDefinition);
		}
	}
}
