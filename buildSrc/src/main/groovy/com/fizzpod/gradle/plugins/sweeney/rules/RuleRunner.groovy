package com.fizzpod.gradle.plugins.sweeney.rules;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class RuleRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuleRunner);

	private RuleDefinitionProcessor processor = new RuleDefinitionProcessor()

	private RuleLoader ruleLoader = new RuleLoader();

	private RuleDefinitionParserLoader ruleDefinitionParserLoader = new RuleDefinitionParserLoader();

	private RunMode runMode = RunMode.ENFORCE;

	public RuleRunner(RunMode mode) {
		runMode = mode;
	}

	@Deprecated
	public void applyRules(def ruleSpecifications, def scope) {
		ruleSpecifications.each { spec ->
			def definition = processor.process(spec, scope)
			run(definition.definition, definition.rule, scope)
		}
	}

	public void runRules(def ruleDefinitions, def scope) {
		ruleDefinitions.each { ruleDefinition ->
			run(ruleDefinition.definition, ruleDefinition.rule, scope);
		}
	}

	public void run(def definition, def rule, def scope) {
		try {
			LOGGER.debug("Applying rule: {}", definition)
			rule.validate(definition, scope)
		} catch(AssertionError e) {
			if(RunMode.ENFORCE == runMode) {
				throw e;
			} else {
				LOGGER.warn("Warning: {}", e.getMessage());
			}
		}
	}
}
