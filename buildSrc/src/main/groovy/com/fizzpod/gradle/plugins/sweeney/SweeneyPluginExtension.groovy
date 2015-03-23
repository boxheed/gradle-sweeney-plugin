package com.fizzpod.gradle.plugins.sweeney;

public class SweeneyPluginExtension {

	void enforce(def args) {
		println("enforce: " + args)
	}
	
	void caution(def args) {
		println("caution: " + args)
	}
	
}
