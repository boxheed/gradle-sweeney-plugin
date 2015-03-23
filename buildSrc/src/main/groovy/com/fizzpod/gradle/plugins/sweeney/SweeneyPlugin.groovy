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
		applyRules(project, theSweeney.enforceableRules)
	}
	
	void runCautionRules(Project project) {
		SweeneyPluginExtension theSweeney = project.getExtensions().findByType(SweeneyPluginExtension)
		LOGGER.info("Applying caution rules: {}", theSweeney.cautionaryRules);
		applyRules(project, theSweeney.cautionaryRules)
	}
	
	void applyRules(def scope, def ruleDefinitions) {
		
		ruleDefinitions.each { ruleDefinition ->
			LOGGER.debug("Applying rule: {}", ruleDefinition);
			convertAndApplyRule(scope, ruleDefinition);
		}
	}
	
	void convertAndApplyRule(def scope, def ruleDefinition) {
		def convertedRuleDefinition = null;
		ruleDefinitionParserLoader.all().each { it -> 
			 convertedRuleDefinition = it.parse(ruleDefinition);
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
				LOGGER.info("Testing defintion {} with rule {}", ruleDefinition, it.getType())
				boolean valid = it.validate(scope, ruleDefinition);
				if(!valid) {
					throw new IllegalArgumentException(ruleDefinition.toString() + " is not valid");
				}
			} else {
				LOGGER.info("Rule {} rejected definition {}", it.getType(), ruleDefinition);
			}
		}
	}
	
}