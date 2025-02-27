/* (C) 2024 */
/* SPDX-License-Identifier: Apache-2.0 */
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

	def 'rule definition from three part string'() {
	when:
		def str = "abc:xyz:hij"
		def definition = new StringRuleDefinitionParser().parse(str)
	then:
		definition != null
		definition.getAttribute("type") instanceof Closure
		definition.getAttribute("type").call().equals("abc")
		definition.getAttribute("expect") instanceof Closure
		definition.getAttribute("expect").call().equals("xyz")
		definition.getAttribute("value") instanceof Closure
		definition.getAttribute("value").call().equals("hij")
	}

	def 'rule definition from four part string'() {
	when:
		def str = "abc:xyz:hij:lmn"
		def definition = new StringRuleDefinitionParser().parse(str)
	then:
		definition != null
		definition.getAttribute("type") instanceof Closure
		definition.getAttribute("type").call().equals("abc")
		definition.getAttribute("expect") instanceof Closure
		definition.getAttribute("expect").call().equals("xyz")
		definition.getAttribute("value") instanceof Closure
		definition.getAttribute("value").call().equals("hij")
		definition.getAttribute("msg") instanceof Closure
		definition.getAttribute("msg").call().equals("lmn")
	}
	
	def 'rule definition has too many parts'() {
		when:
			def str = "abc:def:ghi:jkl:bbb:lll"
			def definition = new StringRuleDefinitionParser().parse(str)
		then:
			thrown(AssertionError)
	}

	def 'when is now'() {
		when:
			def str = "abc:def:ghi:jkl:now"
			def definition = new StringRuleDefinitionParser().parse(str)
		then:
			definition.getAttribute("when").call().equals("now")
	}

	def 'when is not now'() {
		when:
			def str = "abc:def:ghi:jkl"
			def definition = new StringRuleDefinitionParser().parse(str)
		then:
			definition.hasAttribute("when") == false
	}
	
}
