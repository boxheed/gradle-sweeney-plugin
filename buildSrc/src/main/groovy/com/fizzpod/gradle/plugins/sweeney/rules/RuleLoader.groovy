package com.fizzpod.gradle.plugins.sweeney.rules;


public class RuleLoader {

	private static ServiceLoader<Rule> ruleServiceLoader = ServiceLoader
			.load(Rule.class);

	def all () {
		return ruleServiceLoader.asList();
	}
			
}
