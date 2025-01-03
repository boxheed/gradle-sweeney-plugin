/* (C) 2024 */
/* SPDX-License-Identifier: Apache-2.0 */
package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class SystemPropertyRuleSpec extends ProjectSpec {

	SystemPropertyRule rule = new SystemPropertyRule()

	def 'property abc has value def'() {
		setup:
			System.setProperty("abc", "def")
			def stringDefinition = "sys:def:abc"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			notThrown(AssertionError)
			accept == true
	}
	
	def 'property abc does not have value def'() {
		setup:
			System.setProperty("abc", "xyz")
			def stringDefinition = "sys:def:abc"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			thrown(AssertionError)
			accept == true
	}
	
	def 'property hij does not have a value'() {
		setup:
			def stringDefinition = "sys:def:hij"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			thrown(AssertionError)
			accept == true
	}
	
	def 'property exists'() {
		setup:
			def stringDefinition = "sys::java.version"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			notThrown(AssertionError)
			accept == true
	}
	
	def 'property does not exist'() {
		setup:
			def stringDefinition = "sys::sys.prop.not.defined"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			thrown(AssertionError)
			accept == true
	}
}
