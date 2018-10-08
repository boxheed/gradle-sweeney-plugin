package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class JdkVersionRuleSpec extends ProjectSpec {

	JdkVersionRule jdkVersionRule = new JdkVersionRule();

	def 'jdk rule should not allow jdks less than 1.7'() {
		setup:
			def stringDefinition = "jdk:[1.7,)"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = jdkVersionRule.accept(definition, project)
			jdkVersionRule.validate(definition, project);
		then:
			notThrown(AssertionError)
			accept == true;
	}

	def 'jdk rule should not allow jdks less than and equal to 1.10'() {
		setup:
			def stringDefinition = "jdk:]1.10,)"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = jdkVersionRule.accept(definition, project)
			jdkVersionRule.validate(definition, project);
		then:
			thrown(AssertionError)
			accept == true;
	}

}
