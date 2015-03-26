[![Build Status](https://api.shippable.com/projects/550fe54f5ab6cc1352a7dd4c/badge?branchName=master)](https://app.shippable.com/projects/550fe54f5ab6cc1352a7dd4c/builds/latest)
# Gradle Sweeney Plugin
A Gradle plugin with the capability to enforce rules against the current project/environment like the Maven enforcer plugin.

**Sweeney**: *London slang for the Flying Squad, a branch of the Metropolitan Police Service that investigates serious robberies and organised crime.*

# Usage
Import the sweeney gradle plugin
```
buildscript {
	repositories {
		jcenter()
	}
	dependencies {
	    // sweeney dependency
		classpath 'com.fizzpod:gradle-sweeney-plugin:0.1+'
    }
}

apply plugin: 'com.fizzpod.sweeney'

sweeney {
    //sweeney supports two types of rules, enforced rules which will cause a build failure, 
    //and caution rules which are warnings
    
    //enforces that the value '123' is equal to the expected value of '123'
    enforce type: 'equal', expect: '123', value:'456'
    //will emit a cautionary warning of the value of 'def' does not equal 'abc'
    caution type: 'equal', expect: 'abc', value:'def'
}
```

# Rule definitions
Sweeney has support for a number of ways to define a rule definition (the definition is the configuration block for a rule as defined in the sweeney block). 
The built in rules have different requirements for the definition, but at the most basic they are in three parts: *type,expected value,actual value*
## Type
This defines which rule is targeted for the definition.
## Expect
This defines the expected value for the rule to test against.
## Value
The value to be tested.
## Definition specs
Sweeney has two configuration mechanisms for defining the rules, either via a map or a string, with some useful flexibility. At the end of the day the definition will end up in a map of String keys to Closure values.
### Map based definition using closures
This configuration most closely resembles the internal representation of the rule definitions and offers the greatest flexibility.
```
sweeney {
    enforce type: {'equal'}, expect: {'abc'}, value: {'def}
}
```
### Map based definition using Strings
To shorten the definition down it is possible to just define each part as a string, the definition parser will convert the strings into closures.
```
sweeney {
    enforce type: 'equal', expect: 'abc', value: 'def'
}
```

### Map based definition using mixture
It s also possible to mix the previous two definitions in order to give greater flexibility
```
sweeney {
    enforce type: 'equal', expect: 'abc', value: {'def'}
}
```
### String based definition
A shorthand string based parser is also available to construct rules. The string is constructed as `type:expect:value` where 
```
sweeney {
    enforce 'equal:abc:def'
}
```

# Built in rules
Sweeney has the following built in rules::
## Equality
Tests for equality between two values, e.g. to test the 'def' is equal to the expected value of 'abc' it can be defined in the following ways:
```
sweeney {
    //map definition
    enforce type: 'equal', expect: 'abc', value: {'def'}
    //as a string
    enforce 'equal:abc:def'
}
```
Testing the JDK version is equal to 1.7:
```
sweeney {
    enforce type: "equal", expect: "1.7", value: {System.getProperty('java.version').substring(0,3)}
}
```

## Pattern
Test using a Java regular expression as the `expect` value. Requires a type of `pattern`
```
sweeney {
    //map definition
    enforce type: 'pattern', expect: 'abc', value: {'def'}
    //string definition
    enforce 'pattern:abc:def'
}
```
Testing the JDK version is equal to 1.7:
```
sweeney {
    enforce type: "pattern", expect: "1\\.7.*", value: {System.getProperty('java.version')}
}
```
## VersionRange
This compares the value as if it was a version number with the definition of the expected version number. This supports the same syntax as the Ivy Version Range matcher see: [Ivy version matchers](http://ant.apache.org/ivy/history/trunk/settings/version-matchers.html)

```
sweeney {
    //map definition
    enforce type: 'range', expect: '[1.0,)', value: {'2.0'}
    //string definition
    enforce 'range:[1.0,):2.0'
}
```
Testing the JDK version is version 1.7 or above:
```
sweeney {
    enforce type: "range", expect: "[1.7,)", value: {System.getProperty('java.version')}
}
```

## JDK Version
This is a shorthand to test the jdk version, and is a form of specialisation of the VersionRange as such it does not require the 'value' to be defined and can take on a shorter syntax
```
sweeney {
    // tests whether the jdk is version 1.7 or above
    enforce type: 'jdk', expect: '[1.7,)'
    // string version
    enforce 'jdk:[1.7,)'
}
```

## Gradle Version
Similar to the JDK version but testing the Gradle version instead
```
sweeney {
    // tests whether gradle is 2.0 or above
    enforce type: 'gradle', expect: '[2.0,)'
    // string version
    enforce 'gradle:[2.0,)'
}
```

# Rule scopes
The plugin listens for the afterEvaluate on the project for running the defined rules, it is envisaged that additional hooks into the different lifecycle phases will be added at a later date.

# Extending with your own rules
The customisation uses the Java Service Loader for finding and loading implementations of `com.fizzpod.gradle.plugins.sweeney.rules.Rule` interface. In the META-INF/services folder your library create a file called `com.fizzpod.gradle.plugins.sweeney.rules.Rule` and in it specify an implementation class one per line. The contents of the built in rules is:
```
com.fizzpod.gradle.plugins.sweeney.rules.VersionRangeRule
com.fizzpod.gradle.plugins.sweeney.rules.JdkVersionRule
com.fizzpod.gradle.plugins.sweeney.rules.GradleVersionRule
com.fizzpod.gradle.plugins.sweeney.rules.PatternRule
com.fizzpod.gradle.plugins.sweeney.rules.EqualRule
```
You then only need to include your library on the build script classpath and your implementations will be picked up.
