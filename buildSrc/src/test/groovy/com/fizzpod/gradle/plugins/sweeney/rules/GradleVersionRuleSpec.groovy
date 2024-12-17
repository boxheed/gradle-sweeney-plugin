/* (C) 2024 */
/* SPDX-License-Identifier: Apache-2.0 */
package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class GradleVersionRuleSpec extends ProjectSpec {

	GradleVersionRule gradleVersionRule = new GradleVersionRule()

	def 'gradle rule should allow gradle versions greater than 2.0'() {
		setup:
			def stringDefinition = "gradle:[2.0,)"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = gradleVersionRule.accept(definition, project)
			def runNow = gradleVersionRule.isRunNow(definition)
			gradleVersionRule.validate(definition, project)
		then:
			notThrown(AssertionError)
			accept == true
			runNow == true
	}

	def 'gradle rule should not allow gradle versions less than and equal to 20.8.1'() {
		setup:
			def stringDefinition = "gradle:]20.8.1,)"
			def definition = new StringRuleDefinitionParser().parse(stringDefinition)
		when:
			def accept = gradleVersionRule.accept(definition, project)
			gradleVersionRule.validate(definition, project)
		then:
			thrown(AssertionError)
			accept == true
	}

}
