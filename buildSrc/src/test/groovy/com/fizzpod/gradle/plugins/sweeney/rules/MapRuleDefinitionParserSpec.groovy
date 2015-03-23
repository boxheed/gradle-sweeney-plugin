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
	
}
