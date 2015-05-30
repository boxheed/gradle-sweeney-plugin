package com.fizzpod.gradle.plugins.sweeney.rules;


public class RuleLoader {

	private static final DEFAULT_RULES = [
		new VersionRangeRule(),
		new JdkVersionRule(),
		new GradleVersionRule(),
		new PatternRule(),
		new EqualRule(),
		new BooleanRule(),
		new SystemPropertyRule()
	]

	private static ServiceLoader<Rule> ruleServiceLoader = ServiceLoader
			.load(Rule.class);

	def all () {
		return ruleServiceLoader.asList() + DEFAULT_RULES;
	}
			
}
