/* (C) 2024-2026 */
/* SPDX-License-Identifier: Apache-2.0 */
package com.fizzpod.gradle.plugins.sweeney.rules

import nebula.test.ProjectSpec

class VersionRangeConstantsSpec extends ProjectSpec {

	VersionRangeRule rule = new VersionRangeRule()

    def 'verify standard maven ranges'() {
        setup:
            def definitions = [
                "[1.0,2.0]",
                "(1.0,2.0)",
                "[1.0,2.0)",
                "(1.0,2.0]"
            ]
        expect:
            definitions.each { range ->
                def definition = new StringRuleDefinitionParser().parse("range:$range:1.5")
                assert rule.accept(definition, project)
            }
    }

    def 'verify ivy specific brackets'() {
        setup:
            def definitions = [
                "[1.0,2.0[", // inclusive, exclusive
                "]1.0,2.0]", // exclusive, inclusive
                "]1.0,2.0["  // exclusive, exclusive
            ]
        expect:
            definitions.each { range ->
                def definition = new StringRuleDefinitionParser().parse("range:$range:1.5")
                assert rule.accept(definition, project)
            }
    }

    def 'verify infinite ranges'() {
        setup:
             def definitions = [
                "[1.0,)", // >= 1.0
                "(,2.0]"  // <= 2.0
            ]
        expect:
            definitions.each { range ->
                def definition = new StringRuleDefinitionParser().parse("range:$range:1.5")
                assert rule.accept(definition, project)
            }
    }

    def 'verify invalid infinite ranges with brackets'() {
        setup:
            def definitions = [
                "[1.0,[", // >= 1.0 using exclusive bracket?
                "] ,2.0]"  // <= 2.0 using exclusive bracket?
            ]
        expect:
            definitions.each { range ->
                try {
                    def definition = new StringRuleDefinitionParser().parse("range:$range:1.5")
                    rule.accept(definition, project)
                    // If accept returns true (which it shouldn't for invalid format), we should fail
                    // But here we expect rule.validate or rule.accept might fail with assertion error
                    // Actually accept() checks expectPatternSupported() which asserts.
                    assert false, "Range $range should have failed validation"
                } catch (AssertionError e) {
                    assert e.message.contains("Supported patterns for range matching")
                }
            }
    }
}
