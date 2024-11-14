[![CircleCI](https://circleci.com/gh/boxheed/gradle-sweeney-plugin/tree/master.svg?style=shield)](https://circleci.com/gh/boxheed/gradle-sweeney-plugin/tree/master)

# Gradle Sweeney Plugin
A Gradle plugin with the capability to enforce rules against the current project/environment like the Maven enforcer plugin.

**Sweeney**: *London slang for the Flying Squad, a branch of the Metropolitan Police Service that investigates serious robberies and organised crime.*

# Usage
Import the sweeney gradle plugin
```
buildscript {
  repositories {
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }
  }
  dependencies {
    classpath("com.fizzpod:gradle-sweeney-plugin:5.0.4")
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
## Message (msg)
An optional message that will be output when the assertion fails, uses a groovy template for fomatting
## When
This controls when to run the rule. For most rules it waits until the project has been initialised before running the rule. This can be modified by setting the `when` to the value `now` at which point it evaluates the rul immediately. However the JDK and Gradle version rules change that behaviour and by default are evaluated immediately which can be changed by setting the `when` to any other value e.g. `later`.
## Definition specs
Sweeney has two configuration mechanisms for defining the rules, either via a map or a string, with some useful flexibility. At the end of the day the definition will end up in a map of String keys to Closure values.
### Map based definition using closures
This configuration most closely resembles the internal representation of the rule definitions and offers the greatest flexibility.
```
sweeney {
    enforce type: {'equal'}, expect: {'abc'}, value: {'def'}, msg: {'Assertion of rule $type failed, expected $expect but got $value', when: {'now'}}
}
```
### Map based definition using Strings
To shorten the definition down it is possible to just define each part as a string, the definition parser will convert the strings into closures.
```
sweeney {
    enforce type: 'equal', expect: 'abc', value: 'def', msg: 'Assertion of rule $type failed, expected $expect but got $value', when: 'now'
}
```

### Map based definition using mixture
It is also possible to mix the previous two definitions in order to give greater flexibility
```
sweeney {
    enforce type: 'equal', expect: 'abc', value: {'def'}, msg: {'Assertion of rule $type failed, expected $expect but got $value', when: {'now'}
}
```
### String based definition
A shorthand string based parser is also available to construct rules. The string is constructed as `type:expect:value` where 
```
sweeney {
    enforce 'equal:abc:def:Assertion of rule $type failed, expected $expect but got $value:now'
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
Testing the JDK version is equal to 17:
```
sweeney {
    enforce type: "equal", expect: "17", value: {System.getProperty('java.version').substring(0,2)}
}
```

## Boolean
Tests that the result of the value is true, this is a specialisation of the equality rule, and provides a slightly shorter syntax

```
sweeney {
    //map definition
    enforce type: 'bool', value: {true}
    //as a string, note the double colon (::) this is needed as the string parser expects the expected value to be present.
    enforce 'bool::true'
}
```

Testing the JDK version is equal to 1.7:
```
sweeney {
    enforce type: "bool", value: {System.getProperty('java.version').substring(0,3) == '1.7'}
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

## System Property
This rule tests for the existence and value of a specified system property. The key for the system property is specified in the value position and the expected value is the value of the key.
```
sweeney {
    // tests whether the jdk is version 1.7.0_79
    enforce type: 'sys', expect: '1.7.0_79', value: 'java.version'
    // string version
    enforce 'sys:1.7.0_79:java.version'
}
```

If you just wish to test for the existence of a system property leave the expected value as undefined as in the following example.
```
sweeney {
    // tests whether the java.version system property is defined.
    enforce type: 'sys', value: 'java.version'
    // string version, note the double colon '::' this is required for the parser.
    enforce 'sys::java.version'
}
```

## JDK Version
This is a shorthand to test the jdk version, and is a form of specialisation of the VersionRange as such it does not require the 'value' to be defined and can take on a shorter syntax. By default this rule is evaluated immediately on parsing of the definition.
```
sweeney {
    // tests whether the jdk is version 1.7 or above
    enforce type: 'jdk', expect: '[1.7,)'
    // string version
    enforce 'jdk:[1.7,)'
}
```

## Gradle Version
Similar to the JDK version but testing the Gradle version instead. By default this rule is evaluated immediately on parsing of the definition.
```
sweeney {
    // tests whether gradle is 2.0 or above
    enforce type: 'gradle', expect: '[2.0,)'
    // string version
    enforce 'gradle:[2.0,)'
}
```

# Failing fast
If for example you want your some or all of your rules to run immediately you can provide a call to `validate`
after the definition of the rules, for example to test the Java and Gradle versions you may run define the following rules and then invoke the validate method. Note that now the gradle and JDK version rules by default are evaluated immediately.

```
sweeney {
    // tests whether gradle is 2.0 or above
    enforce 'gradle:[2.0,)'
    // tests whether the jdk is version 1.7 or above
    enforce 'jdk:[1.7,)'
    //run the rules 
    validate()
    //this rule is run in the normal phase
    enforce 'pattern:abc:def'
}

Note that the rules after the validate will run in the normal phase. The call to `validate` does not mark the rules as already run so will be run a second time in the normal phase of the gradle lifecycle.

# Rule evaluation phases
It's possible to invoke rules in different phases of the gradle build. 
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

Note that as of version `6.0.0` the `Rule` interface has a breaking change on it, any custome rules will need to implement the new method or extend the AbstractRule which provides a default implementation.