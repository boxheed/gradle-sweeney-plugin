package com.fizzpod.gradle.plugins.sweeney.rules;

public abstract class AbstractRule implements Rule {

	private boolean defaultRunNow = false;

	
	
	public AbstractRule() {
		this(false);
	}

	public AbstractRule(boolean defaultRunNow) {
		this.defaultRunNow = defaultRunNow;
	}

	public boolean isRunNow(RuleDefinition ruleDefinition) {
		boolean runNow = defaultRunNow;
		if(ruleDefinition.hasAttribute(WHEN_ATTRIBUTE)) {
			runNow = WHEN_NOW_VALUE.equals(ruleDefinition.getAttribute(WHEN_ATTRIBUTE).call());
		}
		return runNow;
	}

	
}
