/* (C) 2024-2026 */
/* SPDX-License-Identifier: Apache-2.0 */
package com.fizzpod.gradle.plugins.sweeney.rules

import java.util.regex.Matcher
import java.util.regex.Pattern
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Matches version ranges for attributes. The implementation is taken from the
 * Ivy VersionRangeMatcher to provide support for the version range specification
 * as documented in the ivy docs here: 
 * http://ant.apache.org/ivy/history/trunk/settings/version-matchers.html 
 *
 */
class VersionRangeRule extends AbstractRule implements Rule {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VersionRangeRule)

	// todo: check these constants
	private static final String OPEN_INC = "["

	private static final String OPEN_EXC = "]"
	private static final String OPEN_EXC_MAVEN = "("

	private static final String CLOSE_INC = "]"

	private static final String CLOSE_EXC = "["
	private static final String CLOSE_EXC_MAVEN = ")"

	private static final String LOWER_INFINITE = "("

	private static final String UPPER_INFINITE = ")"

	private static final String SEPARATOR = ","

	// following patterns are built upon constants above and should not be modified
	private static final String OPEN_INC_PATTERN = "\\" + OPEN_INC

	private static final String OPEN_EXC_PATTERN = "\\" + OPEN_EXC + "\\" + OPEN_EXC_MAVEN

	private static final String CLOSE_INC_PATTERN = "\\" + CLOSE_INC

	private static final String CLOSE_EXC_PATTERN = "\\" + CLOSE_EXC + "\\" + CLOSE_EXC_MAVEN

	private static final String LI_PATTERN = "\\" + LOWER_INFINITE

	private static final String UI_PATTERN = "\\" + UPPER_INFINITE

	private static final String SEP_PATTERN = "\\s*\\" + SEPARATOR + "\\s*"

	private static final String OPEN_PATTERN = "[" + OPEN_INC_PATTERN + OPEN_EXC_PATTERN + "]"

	private static final String CLOSE_PATTERN = "[" + CLOSE_INC_PATTERN + CLOSE_EXC_PATTERN + "]"

	private static final String ANY_NON_SPECIAL_PATTERN = "[^\\s" + SEPARATOR + OPEN_INC_PATTERN +
	OPEN_EXC_PATTERN + CLOSE_INC_PATTERN + CLOSE_EXC_PATTERN + LI_PATTERN + UI_PATTERN + "]"

	private static final String FINITE_PATTERN = OPEN_PATTERN + "\\s*(" + ANY_NON_SPECIAL_PATTERN +
	"+)" + SEP_PATTERN + "(" + ANY_NON_SPECIAL_PATTERN + "+)\\s*" + CLOSE_PATTERN

	private static final String LOWER_INFINITE_PATTERN = LI_PATTERN + SEP_PATTERN + "(" +
	ANY_NON_SPECIAL_PATTERN + "+)\\s*" + CLOSE_PATTERN

	private static final String UPPER_INFINITE_PATTERN = OPEN_PATTERN + "\\s*(" +
	ANY_NON_SPECIAL_PATTERN + "+)" + SEP_PATTERN + UI_PATTERN

	private static final Pattern FINITE_RANGE = Pattern.compile(FINITE_PATTERN)

	private static final Pattern LOWER_INFINITE_RANGE = Pattern.compile(LOWER_INFINITE_PATTERN)

	private static final Pattern UPPER_INFINITE_RANGE = Pattern.compile(UPPER_INFINITE_PATTERN)

	private static final Pattern ALL_RANGE = Pattern.compile(FINITE_PATTERN + "|"
	+ LOWER_INFINITE_PATTERN + "|" + UPPER_INFINITE_PATTERN)


	private static final Pattern VERSION_EXTRACT_PATTERN = Pattern.compile(".*?([0-9]+(?:\\.[0-9]+)*).*")

	public static final String VERSION_RANGE_TYPE_VALUE = "range"

	@Override
	public String getType() {
		return VERSION_RANGE_TYPE_VALUE
	}

	@Override
	public boolean accept(RuleDefinition ruleDefinition, def scope) {
		if(isRequiredType(ruleDefinition) &&
		hasRequiredAttributes(ruleDefinition) &&
		expectPatternSupported(ruleDefinition)) {
			LOGGER.info("Accepting definition: {}", ruleDefinition)
			return true
		}
		LOGGER.info("Rejecting definition: {}", ruleDefinition)
		return false
	}

	private boolean isRequiredType(RuleDefinition ruleDefinition) {
		return ruleDefinition.hasAttribute(TYPE_ATTRIBUTE) && VERSION_RANGE_TYPE_VALUE.equals(ruleDefinition.getAttribute(TYPE_ATTRIBUTE).call())
	}

	private boolean hasRequiredAttributes(RuleDefinition ruleDefinition) {
		assert (ruleDefinition.hasAttribute(TYPE_ATTRIBUTE) && ruleDefinition.hasAttribute(EXPECT_ATTRIBUTE) && ruleDefinition.hasAttribute(VALUE_ATTRIBUTE)), 'Type range must have expect and value attributes defined.'
		return true
	}

	private boolean expectPatternSupported(RuleDefinition ruleDefinition) {
		String expect = ruleDefinition.getAttribute(EXPECT_ATTRIBUTE).call()
		assert ALL_RANGE.matcher(expect).matches(), 'Supported patterns for range matching are one of ' + [FINITE_PATTERN, LOWER_INFINITE_PATTERN, UPPER_INFINITE_PATTERN]
		return true
	}

	@Override
	public void validate(RuleDefinition ruleDefinition, def scope) {
		LOGGER.info("Validating {}", ruleDefinition)
		RuleMessageFormatter formatter = new RuleMessageFormatter('Version $version is not within specification: $ruleDefinition')
		String expect = getExpect(ruleDefinition)
		Version version = getVersion(ruleDefinition)
		LowerVersion lowerVersion = getLowerVersion(expect)
		UpperVersion upperVersion = getUpperVersion(expect)
		assert (lowerVersion.isBelow(version) && upperVersion.isAbove(version)), formatter.format(ruleDefinition, ["version":version]) 
	}

	private String getExpect(RuleDefinition ruleDefinition) {
		return ruleDefinition.getAttribute(EXPECT_ATTRIBUTE).call()
	}

	private Version getVersion(RuleDefinition ruleDefinition) {
		String value = ruleDefinition.getAttribute(VALUE_ATTRIBUTE).call()
		Matcher matcher = VERSION_EXTRACT_PATTERN.matcher(value)

		assert matcher.matches(), "Value $value does not contain a valid version number"
		value = matcher.group(1)
		return new Version(value)
	}

	private LowerVersion getLowerVersion(String expect) {
		String lower = null
		Matcher m = FINITE_RANGE.matcher(expect)
		if (m.matches()) {
			lower = m.group(1)
		}
		m = UPPER_INFINITE_RANGE.matcher(expect)
		if (m.matches()) {
			lower = m.group(1)
		}
		boolean inclusive = expect.startsWith(OPEN_INC)
		return new LowerVersion(lower, inclusive)

	}

	private UpperVersion getUpperVersion(String expect) {
		String upper = null
		Matcher m = FINITE_RANGE.matcher(expect)
		if (m.matches()) {
			upper = m.group(2)
		}

		m = LOWER_INFINITE_RANGE.matcher(expect)
		if (m.matches()) {
			upper = m.group(1)
		}

		boolean inclusive = expect.endsWith(CLOSE_INC)
		return new UpperVersion(upper, inclusive)
	}
}


	public class LowerVersion {

		private Version lowerVersion
		private boolean inclusive

		public LowerVersion(String lower, boolean inclusive) {
			if(lower != null) {
				this.lowerVersion = new Version(lower)
			}
			this.inclusive = inclusive
		}

		public boolean isBelow(Version version) {
			if(lowerVersion == null) {
				return true
			}
			int comparison = lowerVersion.compareTo(version)
			return inclusive? comparison <=0: comparison < 0
		}
		
		public String toString() {
			return "LowerVersion "+  ['version': lowerVersion, 'inclusive': inclusive]
		}
	}

	public class UpperVersion {

		private Version upperVersion
		private boolean inclusive

		public UpperVersion(String upper, boolean inclusive) {
			if(upper != null) {
				this.upperVersion = new Version(upper)
			}
			this.inclusive = inclusive
		}
		
		public boolean isAbove(Version version) {
			if(upperVersion == null) {
				return true
			}
			int comparison = upperVersion.compareTo(version)
			return inclusive? comparison >=0: comparison > 0
		}
		
		public String toString() {
			return "UpperVersion "+  ['version': upperVersion, 'inclusive': inclusive]
		}
	}

	@CompileStatic
	public class Version implements Comparable<Version> {

		private static final Pattern VERSION_PATTERN = Pattern.compile("[0-9]+(\\.[0-9]+)*")

		private String version

		public final String get() {
			return this.version
		}

		public Version(String version) {
			assert version != null, "Version cannot be null"
			assert VERSION_PATTERN.matcher(version).matches(), "Invalid version format: $version"
			this.version = version
		}

		@Override public int compareTo(Version that) {
			if(that == null)
				return 1
			String v1 = this.get()
			String v2 = that.get()
			int i1 = 0
			int i2 = 0
			int len1 = v1.length()
			int len2 = v2.length()

			while (i1 < len1 || i2 < len2) {
				int val1 = 0
				if (i1 < len1) {
					int start1 = i1
					while (i1 < len1 && v1.charAt(i1) != '.') {
						i1++
					}
					val1 = parsePart(v1, start1, i1)
					if (i1 < len1) i1++
				}

				int val2 = 0
				if (i2 < len2) {
					int start2 = i2
					while (i2 < len2 && v2.charAt(i2) != '.') {
						i2++
					}
					val2 = parsePart(v2, start2, i2)
					if (i2 < len2) i2++
				}

				if (val1 < val2) return -1
				if (val1 > val2) return 1
			}
			return 0
		}

		private int parsePart(String s, int start, int end) {
			int result = 0
			for (int i = start; i < end; i++) {
				char c = s.charAt(i)
				// we assume valid input as per regex validation in constructor
				result = result * 10 + (c - (char)'0')
			}
			return result
		}

		@Override public boolean equals(Object that) {
			if(this == that)
				return true
			if(that == null)
				return false
			if(this.getClass() != that.getClass())
				return false
			return this.compareTo((Version) that) == 0
		}
		
		@Override
		public String toString() {
			return this.version
		}

	}
