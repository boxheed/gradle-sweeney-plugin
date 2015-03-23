package com.fizzpod.gradle.plugins.sweeney.rules

public interface RuleDefinitionParser {

	def RuleDefinition parse(def definition);
	
}
