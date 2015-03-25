package com.fizzpod.gradle.plugins.sweeney.rules

import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class GradleVersionRule implements Rule {

	private static final Logger LOGGER = LoggerFactory.getLogger(GradleVersionRule); 
	
	public static final String GRADLE_TYPE_VALUE = "gradle"

	private VersionRangeRule versionRangeRule = new VersionRangeRule();
	
	@Override
	public String getType() {
		return GRADLE_TYPE_VALUE;
	}

	@Override
	public boolean accept(RuleDefinition ruleDefinition) {
		if(ruleDefinition.hasAttribute(TYPE_ATTRIBUTE) && GRADLE_TYPE_VALUE.equals(ruleDefinition.getAttribute(TYPE_ATTRIBUTE).call())) {
			ruleDefinition = convertForVersionRangeRule(ruleDefinition, "placeholder");
			return versionRangeRule.accept(ruleDefinition);
		} 
		return false;
	}
	
	private RuleDefinition convertForVersionRangeRule(RuleDefinition ruleDefinition, String value) {
		Map<String, Closure> newRuleDefinition = new HashMap<String, Closure>(ruleDefinition.getDefinition());
		newRuleDefinition.put(TYPE_ATTRIBUTE, {VersionRangeRule.VERSION_RANGE_TYPE_VALUE})
		newRuleDefinition.put(VALUE_ATTRIBUTE, {value})
		newRuleDefinition.put("decription", {"Gradle version rule"})
		return new RuleDefinition(newRuleDefinition);
	}

	@Override
	public void validate(def scope, RuleDefinition ruleDefinition) {
		if(scope instanceof Project) {
			def version = ((Project)scope).gradle.gradleVersion;
			ruleDefinition = convertForVersionRangeRule(ruleDefinition, version)
			versionRangeRule.validate(scope, ruleDefinition)
		}
	}

	@Override
	public String toString() {
		return getType();
	}
	

}
