package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class RuleLoaderSpec extends ProjectSpec {
	
	def 'rule loader should load rules from service loader'() {
		when:
		def loader = new RuleLoader();
		
		then:
		def parsers = loader.all();
		parsers != null
		parsers.size() == 7
	}
	
}
