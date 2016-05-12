package com.fizzpod.gradle.plugins.sweeney;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.fizzpod.gradle.plugins.sweeney.rules.RuleRunner
import com.fizzpod.gradle.plugins.sweeney.rules.RunMode

public class SweeneyPluginExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(SweeneyPluginExtension.class);
	
	def defaultScope;
	
	def enforceableRules = [];
	
	def cautionaryRules = [];
	
	SweeneyPluginExtension(def defaultScope) {
		this.defaultScope = defaultScope;
	}
	
	void enforce(def args) {
		LOGGER.info("enforce: " + args)
		enforceableRules << args;
	}
	
	void caution(def args) {
		LOGGER.info("caution: " + args)
		cautionaryRules << args
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
		
		runner.applyRules(enforceableRules, scope)
	}
	
	void runCautionRules(def scope) {
		
		RuleRunner runner = new RuleRunner(RunMode.CAUTION);
		
		runner.applyRules(cautionaryRules, scope)
		
	}
	
	
}
