package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class StringRuleDefinitionParserSpec extends ProjectSpec {

	def 'rule definition from two part string'() {
		when:
			def str = "abc:xyz"
			def definition = new StringRuleDefinitionParser().parse(str)
		then:
			definition != null
			definition.getAttribute("type") instanceof Closure
			definition.getAttribute("type").call().equals("abc")
			definition.getAttribute("expect") instanceof Closure
			definition.getAttribute("expect").call().equals("xyz")
	}
	
	def 'rule definition has too many parts'() {
		when:
			def str = "abc:def:ghi:jkl"
			def definition = new StringRuleDefinitionParser().parse(str)
		then:
			thrown(AssertionError)
	}
	
}
