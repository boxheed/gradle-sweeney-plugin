package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class EqualRuleSpec extends ProjectSpec {

	EqualRule simpleRule = new EqualRule();

	def 'simple rule abc is equal to abc'() {
		setup:
			def stringDefinition = "equal:abc:abc"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = simpleRule.accept(definition)
			simpleRule.validate(project, definition);
		then:
			notThrown(AssertionError)
			accept == true;
	}
	
	def 'simple rule abc is not equal to def'() {
		setup:
			def stringDefinition = "equal:abc:def"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = simpleRule.accept(definition)
			simpleRule.validate(project, definition);
		then:
			thrown(AssertionError)
			accept == true;
	}
	
}
