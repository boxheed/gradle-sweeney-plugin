/* (C) 2024 */
/* SPDX-License-Identifier: Apache-2.0 */
package com.fizzpod.gradle.plugins.sweeney.rules

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class JdkVersionRule extends AbstractRule implements Rule {

	private static final Logger LOGGER = LoggerFactory.getLogger(JdkVersionRule); 
	
	public static final String JDK_TYPE_VALUE = "jdk"
	private static final String DEFAULT_MESSAGE = 'Java version $version is not within specification: $ruleDefinition'

	private VersionRangeRule versionRangeRule = new VersionRangeRule()

	public JdkVersionRule() {
		super(true)
	}
	
	@Override
	public String getType() {
		return JDK_TYPE_VALUE
	}

	@Override
	public boolean accept(RuleDefinition ruleDefinition, def scope) {
		if(ruleDefinition.hasAttribute(TYPE_ATTRIBUTE) && JDK_TYPE_VALUE.equals(ruleDefinition.getAttribute(TYPE_ATTRIBUTE).call())) {
			ruleDefinition = convertForVersionRangeRule(ruleDefinition)
			return versionRangeRule.accept(ruleDefinition, scope)
		} 
		return false
	}
	
	private RuleDefinition convertForVersionRangeRule(RuleDefinition ruleDefinition) {
		Map<String, Closure> newRuleDefinition = new HashMap<String, Closure>(ruleDefinition.getDefinition())
		if(!newRuleDefinition.containsKey(MSG_ATTRIBUTE)) {
			newRuleDefinition.put(MSG_ATTRIBUTE, {DEFAULT_MESSAGE})
		}
		newRuleDefinition.put(TYPE_ATTRIBUTE, {VersionRangeRule.VERSION_RANGE_TYPE_VALUE})
		newRuleDefinition.put(VALUE_ATTRIBUTE, {System.getProperty('java.version')})
		newRuleDefinition.put(DESCRIPTION_ATTRIBUTE, {"JDK version rule"})
		return new RuleDefinition(newRuleDefinition)
	}

	@Override
	public void validate(RuleDefinition ruleDefinition, def scope) {
		versionRangeRule.validate(convertForVersionRangeRule(ruleDefinition), scope)
	}

	@Override
	public String toString() {
		return getType()
	}
	

}
