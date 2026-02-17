/* (C) 2024 */
/* SPDX-License-Identifier: Apache-2.0 */
package com.fizzpod.gradle.plugins.sweeney.rules

public class RuleDefinitionParserLoader {

	private static final DEFAULT_PARSERS = [
		new StringRuleDefinitionParser(),
		new MapRuleDefinitionParser()
	]

	private static ServiceLoader<RuleDefinitionParser> ruleDefinitionParserServiceLoader = ServiceLoader
			.load(RuleDefinitionParser.class)

	private static final List<RuleDefinitionParser> CACHED_PARSERS = (ruleDefinitionParserServiceLoader.asList() + DEFAULT_PARSERS).asImmutable()

	def all () {
		return CACHED_PARSERS
	}

}
