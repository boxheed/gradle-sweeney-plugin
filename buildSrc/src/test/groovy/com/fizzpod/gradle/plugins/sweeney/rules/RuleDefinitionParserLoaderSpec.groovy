/* (C) 2024 */
/* SPDX-License-Identifier: Apache-2.0 */
package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class RuleDefinitionParserLoaderSpec extends ProjectSpec {
	
	def 'rule definition parser loader should load parsers from service loader'() {
		when:
		def loader = new RuleDefinitionParserLoader()
		
		then:
		def parsers = loader.all()
		parsers != null
		parsers.size() == 2
	}
	
}
