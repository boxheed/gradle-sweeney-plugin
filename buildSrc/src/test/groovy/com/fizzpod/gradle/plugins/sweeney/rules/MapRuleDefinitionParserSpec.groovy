/* (C) 2024 */
/* SPDX-License-Identifier: Apache-2.0 */
package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class MapRuleDefinitionParserSpec extends ProjectSpec {

	def 'rule definition from map of strings'() {
		when:
			def map = ["abc":"xyz"]
			def definition = new MapRuleDefinitionParser().parse(map)
		then:
			definition != null
			definition.getAttribute("abc") instanceof Closure
			definition.getAttribute("abc").call().equals("xyz")
	}

	def 'when is now'() {
		when:
			def map = ["when":"now"]
			def definition = new MapRuleDefinitionParser().parse(map)
		then:
			definition != null
			definition.getAttribute("when") instanceof Closure
			definition.getAttribute("when").call().equals("now")
	}

	def 'when is not now'() {
		when:
			def map = ["when":"abc"]
			def definition = new MapRuleDefinitionParser().parse(map)
		then:
			definition != null
			definition.getAttribute("when") instanceof Closure
			!definition.getAttribute("when").call().equals("now")
	}
	
}
