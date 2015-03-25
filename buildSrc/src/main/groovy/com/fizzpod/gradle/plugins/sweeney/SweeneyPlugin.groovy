package com.fizzpod.gradle.plugins.sweeney;

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.fizzpod.gradle.plugins.sweeney.rules.RuleDefinitionParserLoader
import com.fizzpod.gradle.plugins.sweeney.rules.RuleLoader
import com.fizzpod.gradle.plugins.sweeney.rules.RuleRunner
import com.fizzpod.gradle.plugins.sweeney.rules.RunMode

public class SweeneyPlugin implements Plugin<Project> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SweeneyPlugin.class);

	private RuleLoader ruleLoader = new RuleLoader();
	
	private RuleDefinitionParserLoader ruleDefinitionParserLoader = new RuleDefinitionParserLoader();
	
	void apply(Project project) {
		
		project.extensions.create("sweeney", SweeneyPluginExtension);
		
		project.afterEvaluate { proj -> 
			LOGGER.info("Running rules after evaluation for project: {}", proj)
			runEnforcementRules(proj, proj);
			runCautionRules(proj, proj);
		}
		
	}
	
	void runEnforcementRules(def project, def scope) {
		SweeneyPluginExtension theSweeney = project.getExtensions().findByType(SweeneyPluginExtension)
		
		RuleRunner runner = new RuleRunner(RunMode.ENFORCE);
		
		runner.applyRules(theSweeney.enforceableRules, scope)
	}
	
	void runCautionRules(def project, def scope) {
		SweeneyPluginExtension theSweeney = project.getExtensions().findByType(SweeneyPluginExtension)
		
		RuleRunner runner = new RuleRunner(RunMode.CAUTION);
		
		runner.applyRules(theSweeney.cautionaryRules, scope)
		
	}
	
	
}