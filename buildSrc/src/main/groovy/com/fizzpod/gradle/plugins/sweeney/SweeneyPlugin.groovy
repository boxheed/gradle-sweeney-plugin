package com.fizzpod.gradle.plugins.sweeney;

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class SweeneyPlugin implements Plugin<Project> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SweeneyPlugin.class);

	void apply(Project project) {
		project.extensions.create("sweeney", SweeneyPluginExtension);
		
		
		
	}
}