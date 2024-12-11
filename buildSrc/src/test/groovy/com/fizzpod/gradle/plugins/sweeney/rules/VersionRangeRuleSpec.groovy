/* (C) 2024 */
/* SPDX-License-Identifier: Apache-2.0 */
package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class VersionRangeRuleSpec extends ProjectSpec {

	VersionRangeRule rule = new VersionRangeRule()

	def 'versions equal to a specific lower version'() {
		setup:
			def stringDefinition = "range:[2.0,):2.0" 
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			notThrown(AssertionError)
			accept == true
	}
	
	def 'versions greater than a specific lower version'() {
		setup:
			def stringDefinition = "range:[2.0,):3.0"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			notThrown(AssertionError)
			accept == true
	}
	
	
	def 'versions not equal to a specific lower version'() {
		setup:
			def stringDefinition = "range:]2.0,):2.0"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			thrown(AssertionError)
			accept == true
	}
	
	def 'versions less than a specific lower version'() {
		setup:
			def stringDefinition = "range:[2.0,):1.8"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			thrown(AssertionError)
			accept == true
	}
	
	def 'versions equal to a specific upper version'() {
		setup:
			def stringDefinition = "range:(,2.0]:2.0"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			notThrown(AssertionError)
			accept == true
	}
	
	def 'versions less than a specific upper version'() {
		setup:
			def stringDefinition = "range:(,2.0]:1.8"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			notThrown(AssertionError)
			accept == true
	}
	
	
	def 'versions not equal to a specific upper version'() {
		setup:
			def stringDefinition = "range:(,2.0[:2.0"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			thrown(AssertionError)
			accept == true
	}
	
	def 'versions greater than a specific upper version'() {
		setup:
			def stringDefinition = "range:(,2.0]:2.0.1"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			thrown(AssertionError)
			accept == true
	}
	
	def 'versions between version range'() {
		setup:
			def stringDefinition = "range:[2.0,3.0]:2.5"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			notThrown(AssertionError)
			accept == true
	}
	
	def 'versions at lower version range'() {
		setup:
			def stringDefinition = "range:[2.0,3.0]:2."
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			notThrown(AssertionError)
			accept == true
	}
	
	def 'versions at upper version range'() {
		setup:
			def stringDefinition = "range:[2.0,3]:3.0"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			notThrown(AssertionError)
			accept == true
	}
	
	def 'versions at lower version range excluded'() {
		setup:
			def stringDefinition = "range:]2.0,3.0]:2."
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			thrown(AssertionError)
			accept == true
	}
	
	def 'versions at upper version range excluded'() {
		setup:
			def stringDefinition = "range:[2.0,3[:3.0"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			thrown(AssertionError)
			accept == true
	}
	
	def 'versions at below version range'() {
		setup:
			def stringDefinition = "range:[2.0,3.0]:1"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			thrown(AssertionError)
			accept == true
	}
	
	def 'versions above version range excluded'() {
		setup:
			def stringDefinition = "range:[2.0,3]:4.0"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			thrown(AssertionError)
			accept == true
	}
	
	def 'minor versions .10 should be higher than .1'() {
		setup:
			def stringDefinition = "range:(,2.10[:2.1"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project)
		then:
			notThrown(AssertionError)
			accept == true
	}
}
