package com.fizzpod.gradle.plugins.sweeney;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.fizzpod.gradle.plugins.sweeney.rules.RuleRunner
import com.fizzpod.gradle.plugins.sweeney.rules.RunMode
import com.fizzpod.gradle.plugins.sweeney.rules.RuleDefinitionProcessor

public class SweeneyPluginExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(SweeneyPluginExtension.class);
	
	def RuleDefinitionProcessor processor = new RuleDefinitionProcessor()

	def defaultScope;
	
	def enforceableRules = [];

	def cautionaryRules = [];

	SweeneyPluginExtension(def defaultScope) {
		this.defaultScope = defaultScope;
	}
	
	void enforce(def args) {
		LOGGER.info("enforce: {}", args)
		def rule = processor.process(args, defaultScope)
		enforceableRules << rule
		if(rule.rule.isRunNow(rule.definition)) {
			RuleRunner runner = new RuleRunner(RunMode.ENFORCE);
			runner.applyRules([args], defaultScope)
		}
	}
	
	void caution(def args) {
		LOGGER.info("caution: " + args)
		def rule = processor.process(args, defaultScope)
		cautionaryRules << rule
	}
	
	void validate() {
		this.validate(null);
	}
	
	void validate(def scope) {
		if(scope == null) {
			scope = defaultScope;
		}
		runEnforcementRules(scope)
		runCautionRules(scope)
	}

	void runEnforcementRules(def scope) {
		RuleRunner runner = new RuleRunner(RunMode.ENFORCE);
		runner.runRules(enforceableRules, scope)
	}
	
	void runCautionRules(def scope) {
		RuleRunner runner = new RuleRunner(RunMode.CAUTION);
		runner.runRules(cautionaryRules, scope)
	}
	
	
}
