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
	
	def 'unknown definition should throw an exception'() {
		when:
			def definitions = ["banana:tree:house"]
			new RuleRunner(RunMode.ENFORCE).applyRules(definitions, project)
		then:
			thrown(IllegalArgumentException)

	}
	
	def 'bad definition should throw an illegal argument exception'() {
		when:
			def definitions = [123]
			new RuleRunner(RunMode.ENFORCE).applyRules(definitions, project)
		then:
			thrown(IllegalArgumentException)
	}
}
