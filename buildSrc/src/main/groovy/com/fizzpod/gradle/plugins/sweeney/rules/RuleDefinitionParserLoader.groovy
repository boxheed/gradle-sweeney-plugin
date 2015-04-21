package com.fizzpod.gradle.plugins.sweeney.rules;

public class RuleDefinitionParserLoader {

	private static final DEFAULT_PARSERS = [
		new StringRuleDefinitionParser(),
		new MapRuleDefinitionParser()
	]

	private static ServiceLoader<RuleDefinitionParser> ruleDefinitionParserServiceLoader = ServiceLoader
			.load(RuleDefinitionParser.class);

	def all () {
		return ruleDefinitionParserServiceLoader.asList() + DEFAULT_PARSERS;
	}
	
}
