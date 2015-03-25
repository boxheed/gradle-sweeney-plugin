package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class RuleRunnerSpec extends ProjectSpec {

	def 'the rule should pass'() {
		when:
			def definition = ["equal:abc:abc"]
			new RuleRunner(RunMode.ENFORCE).applyRules(project, definition)
		then:
			notThrown(AssertionError)
	}
	
	def 'expect and value are different should fail'() {
		when:
			def definition = ["equal:abc:xyz"]
			new RuleRunner(RunMode.ENFORCE).applyRules(project, definition)
		then:
			thrown(AssertionError)
	}
}
