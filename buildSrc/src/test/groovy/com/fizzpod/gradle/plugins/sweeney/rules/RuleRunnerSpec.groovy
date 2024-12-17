/* (C) 2024 */
/* SPDX-License-Identifier: Apache-2.0 */
package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class RuleRunnerSpec extends ProjectSpec {

	def processor = new RuleDefinitionProcessor()

	def 'the rule should pass'() {
		when:
			def definitions = [processor.process("equal:abc:abc", project)]
			new RuleRunner(RunMode.ENFORCE).runRules(definitions, project)
		then:
			notThrown(AssertionError)
	}
	
	def 'expect and value are different should fail'() {
		when:
			def definitions = [processor.process("equal:abc:xyz", project)]
			new RuleRunner(RunMode.ENFORCE).runRules(definitions, project)
		then:
			thrown(AssertionError)
	}
	
	def 'unknown definition should throw an exception'() {
		when:
			def definitions = [processor.process("banana:tree:house", project)]
			new RuleRunner(RunMode.ENFORCE).runRules(definitions, project)
		then:
			thrown(IllegalArgumentException)

	}
	
	def 'bad definition should throw an illegal argument exception'() {
		when:
			def definitions = [[definition:123, rule:null]]
			new RuleRunner(RunMode.ENFORCE).applyRules(definitions, project)
		then:
			thrown(IllegalArgumentException)
	}
}
