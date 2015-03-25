package com.fizzpod.gradle.plugins.sweeney.rules

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class JdkVersionRule implements Rule {

	private static final Logger LOGGER = LoggerFactory.getLogger(JdkVersionRule); 
	
	public static final String JDK_TYPE_VALUE = "jdk"

	private VersionRangeRule versionRangeRule = new VersionRangeRule();
	
	@Override
	public String getType() {
		return JDK_TYPE_VALUE;
	}

	@Override
	public boolean accept(RuleDefinition ruleDefinition) {
		if(ruleDefinition.hasAttribute(TYPE_ATTRIBUTE) && JDK_TYPE_VALUE.equals(ruleDefinition.getAttribute(TYPE_ATTRIBUTE).call())) {
			ruleDefinition = convertForVersionRangeRule(ruleDefinition);
			return versionRangeRule.accept(ruleDefinition);
		} 
		return false;
	}
	
	private RuleDefinition convertForVersionRangeRule(RuleDefinition ruleDefinition) {
		Map<String, Closure> newRuleDefinition = new HashMap<String, Closure>(ruleDefinition.getDefinition());
		newRuleDefinition.put(TYPE_ATTRIBUTE, {VersionRangeRule.VERSION_RANGE_TYPE_VALUE})
		newRuleDefinition.put(VALUE_ATTRIBUTE, {System.getProperty('java.version')})
		newRuleDefinition.put("decription", {"JDK version rule"})
		return new RuleDefinition(newRuleDefinition);
	}

	@Override
	public void validate(def scope, RuleDefinition ruleDefinition) {
		versionRangeRule.validate(scope, convertForVersionRangeRule(ruleDefinition))
	}

	@Override
	public String toString() {
		return getType();
	}
	

}
