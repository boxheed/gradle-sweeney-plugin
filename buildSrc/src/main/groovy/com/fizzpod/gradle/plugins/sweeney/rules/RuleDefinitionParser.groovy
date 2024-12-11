/* (C) 2024 */
/* SPDX-License-Identifier: Apache-2.0 */
package com.fizzpod.gradle.plugins.sweeney.rules

public interface RuleDefinitionParser {

	def RuleDefinition parse(def definition)
	
}
