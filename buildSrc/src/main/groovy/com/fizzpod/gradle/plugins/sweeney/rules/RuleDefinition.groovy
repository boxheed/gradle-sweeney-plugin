package com.fizzpod.gradle.plugins.sweeney.rules

class RuleDefinition {

	private Map<String, Closure> definition;
	
	RuleDefinition(Map<String, Closure> definition) {
		this.definition = definition;
	}
	
	public Map<String, Closure> getDefinition() {
		return Collections.unmodifiableMap(definition);
	}
	
	public Closure getAttribute(String attribute) {
		return definition.get(attribute);
	}
	
	public boolean hasAttribute(String attribute) {
		return this.definition.containsKey(attribute);
	}
	
	public String toString() {
		def m = [:]
		definition.each{ k, v -> 
			m.put(k, v.call());
		}
		return m.toString();
	}
}
