package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class EqualRuleSpec extends ProjectSpec {

	EqualRule simpleRule = new EqualRule();

	def 'simple rule abc is equal to abc'() {
		setup:
			def stringDefinition = "equal:abc:abc"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = simpleRule.accept(definition, project)
			simpleRule.validate(definition, project);
		then:
			notThrown(AssertionError)
			accept == true;
			simpleRule.isRunNow(definition) == false
	}
	
	def 'simple rule abc is not equal to def'() {
		setup:
			def stringDefinition = "equal:abc:def"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = simpleRule.accept(definition, project)
			simpleRule.validate(definition, project);
		then:
			thrown(AssertionError)
			accept == true;
	}

	def 'simple rule run now'() {
		setup:
			def stringDefinition = "equal:abc:def::now"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def runNow = simpleRule.isRunNow(definition)
		then:
			runNow == true;
	}

	def 'simple rule not run now'() {
		setup:
			def stringDefinition = "equal:abc:def::later"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def runNow = simpleRule.isRunNow(definition)
		then:
			runNow == false;
	}
	
}
