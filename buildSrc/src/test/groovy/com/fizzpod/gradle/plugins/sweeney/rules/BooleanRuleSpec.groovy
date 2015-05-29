package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class BooleanRuleSpec extends ProjectSpec {

	BooleanRule rule = new BooleanRule();

	def 'true is true'() {
		setup:
			def stringDefinition = "bool::true"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project);
		then:
			notThrown(AssertionError)
			accept == true;
	}
	
	def 'false is not true'() {
		setup:
			def stringDefinition = "bool::false"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project);
		then:
			thrown(AssertionError)
			accept == true;
	}
	
		def 'banana is not true'() {
		setup:
			def stringDefinition = "bool::banana"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = rule.accept(definition, project)
			rule.validate(definition, project);
		then:
			thrown(AssertionError)
			accept == true;
	}
	
}
