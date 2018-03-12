package com.fizzpod.gradle.plugins.sweeney.rules

public class RuleMessageFormatter {

    private String msg = "";

    public RuleMessageFormatter() {

    }

    public RuleMessageFormatter(String msg) {
        this.msg = msg;
    }

    public String format(RuleDefinition ruleDefinition) {
        return this.format(ruleDefinition, [:])
    }

    public String format(RuleDefinition ruleDefinition, Map extras) {
        def binding = resolveBinding(ruleDefinition);
        binding.putAll(extras);
        binding.put("ruleDefinition", ruleDefinition)
        def message = resolveMessage(binding);
        def engine = new groovy.text.SimpleTemplateEngine()
		def template = engine.createTemplate(message).make(binding)
        return template.toString()
    }

    private String resolveMessage(def binding) {
        if(binding.containsKey(Rule.MSG_ATTRIBUTE)) {
            return binding.msg;
        }
        return msg;
    }

    private Map resolveBinding(RuleDefinition ruleDefinition) {
		def m = [:]
		ruleDefinition.getDefinition().each{ k, v -> 
			m.put(k, v.call());
		}
		return m;
    }

}