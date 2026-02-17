/* (C) 2024-2026 */
/* SPDX-License-Identifier: Apache-2.0 */
package com.fizzpod.gradle.plugins.sweeney.rules

public class RuleDefinitionParserLoader {

	private static final DEFAULT_PARSERS = [
		new StringRuleDefinitionParser(),
		new MapRuleDefinitionParser()
	]

	private static ServiceLoader<RuleDefinitionParser> ruleDefinitionParserServiceLoader = ServiceLoader
			.load(RuleDefinitionParser.class)

	private static volatile List<RuleDefinitionParser> CACHED_PARSERS

	def all () {
		if (CACHED_PARSERS == null) {
			synchronized(RuleDefinitionParserLoader.class) {
				if (CACHED_PARSERS == null) {
					CACHED_PARSERS = (ruleDefinitionParserServiceLoader.asList() + DEFAULT_PARSERS).asImmutable()
				}
			}
		}
		return CACHED_PARSERS
	}

}
