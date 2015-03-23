package com.fizzpod.gradle.plugins.sweeney;

import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class SweeneyPluginExtension {

	private static final Logger LOGGER = LoggerFactory.getLogger(SweeneyPluginExtension.class);
	
	def enforceableRules = [];
	
	def cautionaryRules = [];
	
	void enforce(def args) {
		LOGGER.info("enforce: " + args)
		enforceableRules << args;
	}
	
	void caution(def args) {
		LOGGER.info("caution: " + args)
		cautionaryRules << args
	}
	
}
