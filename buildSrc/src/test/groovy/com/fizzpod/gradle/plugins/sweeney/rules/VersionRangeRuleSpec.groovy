package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class VersionRangeRuleSpec extends ProjectSpec {

	VersionRangeRule rule = new VersionRangeRule();

	def 'versions equal to a specific lower version'() {
		setup:
			def stringDefinition = "range:[2.0,):2.0" 
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition)
			rule.validate(project, definition);
		then:
			notThrown(AssertionError)
			accept == true;
	}
	
	def 'versions greater than a specific lower version'() {
		setup:
			def stringDefinition = "range:[2.0,):3.0"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition)
			rule.validate(project, definition);
		then:
			notThrown(AssertionError)
			accept == true;
	}
	
	
	def 'versions not equal to a specific lower version'() {
		setup:
			def stringDefinition = "range:]2.0,):2.0"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition)
			rule.validate(project, definition);
		then:
			thrown(AssertionError)
			accept == true;
	}
	
	def 'versions less than a specific lower version'() {
		setup:
			def stringDefinition = "range:[2.0,):1.8"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition)
			rule.validate(project, definition);
		then:
			thrown(AssertionError)
			accept == true;
	}
	
	def 'versions equal to a specific upper version'() {
		setup:
			def stringDefinition = "range:(,2.0]:2.0"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition)
			rule.validate(project, definition);
		then:
			notThrown(AssertionError)
			accept == true;
	}
	
	def 'versions less than a specific upper version'() {
		setup:
			def stringDefinition = "range:(,2.0]:1.8"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition)
			rule.validate(project, definition);
		then:
			notThrown(AssertionError)
			accept == true;
	}
	
	
	def 'versions not equal to a specific upper version'() {
		setup:
			def stringDefinition = "range:(,2.0[:2.0"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition)
			rule.validate(project, definition);
		then:
			thrown(AssertionError)
			accept == true;
	}
	
	def 'versions greater than a specific upper version'() {
		setup:
			def stringDefinition = "range:(,2.0]:2.0.1"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition)
			rule.validate(project, definition);
		then:
			thrown(AssertionError)
			accept == true;
	}
	
	
	
}
