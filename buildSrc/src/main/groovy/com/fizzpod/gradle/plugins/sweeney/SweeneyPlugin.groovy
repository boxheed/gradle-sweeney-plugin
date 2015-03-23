package com.fizzpod.gradle.plugins.sweeney;

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.fizzpod.gradle.plugins.sweeney.rules.RuleDefinitionParserLoader
import com.fizzpod.gradle.plugins.sweeney.rules.RuleLoader

public class SweeneyPlugin implements Plugin<Project> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SweeneyPlugin.class);

	private RuleLoader ruleLoader = new RuleLoader();
	
	private RuleDefinitionParserLoader ruleDefinitionParserLoader = new RuleDefinitionParserLoader();
	
	void apply(Project project) {
		project.extensions.create("sweeney", SweeneyPluginExtension);
		
		project.afterEvaluate { proj -> 
			LOGGER.info("Running rules after evaluation for project: {}", proj)
			runEnforcementRules(proj);
			runCautionRules(proj);
		}
		
	}
	
	void runEnforcementRules(Project project) {
		SweeneyPluginExtension theSweeney = project.getExtensions().findByType(SweeneyPluginExtension)
		LOGGER.info("Applying enforce rules: {}", theSweeney.enforceableRules);
		applyRules(project, theSweeney.enforceableRules, true)
	}
	
	void runCautionRules(Project project) {
		SweeneyPluginExtension theSweeney = project.getExtensions().findByType(SweeneyPluginExtension)
		LOGGER.info("Applying caution rules: {}", theSweeney.cautionaryRules);
		applyRules(project, theSweeney.cautionaryRules, false)
	}
	
	void applyRules(def scope, def ruleDefinitions, def enforce) {
		
		ruleDefinitions.each { ruleDefinition ->
			LOGGER.debug("Applying rule: {}", ruleDefinition);
			convertAndApplyRule(scope, ruleDefinition, enforce);
		}
	}
	
	void convertAndApplyRule(def scope, def ruleDefinition, def enforce) {
		def convertedRuleDefinition = null;
		ruleDefinitionParserLoader.all().each { it -> 
			 convertedRuleDefinition = it.parse(ruleDefinition);
		}
		if(convertedRuleDefinition != null) {
			applyRule(scope, convertedRuleDefinition, enforce);
		} else {
			LOGGER.error("Unable to parse rule: {}", ruleDefinition);
			throw new IllegalArgumentException("Unable to parse rule: " + ruleDefinition);
		}
	}
	
	void applyRule(def scope, def ruleDefinition, def enforce) {
		ruleLoader.all().each { it ->
			LOGGER.debug("Checking whether rule {} accepts definition {}", it.getType(), ruleDefinition);
			if(it.accept(ruleDefinition)) {
				LOGGER.debug("Testing definition {} with rule {}", ruleDefinition, it.getType())
				try {
					it.validate(scope, ruleDefinition);
				} catch(AssertionError e) {
					if(enforce) {
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