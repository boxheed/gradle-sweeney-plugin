package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class RuleRunnerSpec extends ProjectSpec {

	def 'the rule should pass'() {
		when:
			def definitions = ["equal:abc:abc"]
			new RuleRunner(RunMode.ENFORCE).applyRules(definitions, project)
		then:
			notThrown(AssertionError)
	}
	
	def 'expect and value are different should fail'() {
		when:
			def definitions = ["equal:abc:xyz"]
			new RuleRunner(RunMode.ENFORCE).applyRules(definitions, project)
		then:
			thrown(AssertionError)
	}
}
