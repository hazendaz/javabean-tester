# javabean-tester

[![Java CI](https://github.com/hazendaz/javabean-tester/actions/workflows/ci.yaml/badge.svg)](https://github.com/hazendaz/javabean-tester/actions/workflows/ci.yaml)
[![Coverage Status](https://coveralls.io/repos/github/hazendaz/javabean-tester/badge.svg?branch=master)](https://coveralls.io/github/hazendaz/javabean-tester?branch=master)
[![Renovate enabled](https://img.shields.io/badge/renovate-enabled-brightgreen.svg)](https://renovatebot.com/)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.hazendaz/javabean-tester)](https://maven-badges.herokuapp.com/maven-central/com.github.hazendaz/javabean-tester)
[![Project Stats](https://www.openhub.net/p/javabean-tester/widgets/project_thin_badge.gif)](https://www.openhub.net/p/javabean-tester)
[![Apache 2](http://img.shields.io/badge/license-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

## Latest Release

You can download source binaries from our [releases page](https://github.com/hazendaz/javabean-tester/releases) and release binaries from [Maven central](https://search.maven.org/search?q=g:com.github.hazendaz%20AND%20a:javabean-tester)

Alternatively you can pull it using Maven:

```xml
<dependency>
  <groupId>com.github.hazendaz</groupId>
  <artifactId>javabean-tester</artifactId>
  <version>2.12.1</version>
  <scope>test</scope>
</dependency>
```

Or using Gradle:

```groovy
testImplementation 'com.github.hazendaz:javabean-tester:2.12.1'
```

Information for other build frameworks can be found [here](http://hazendaz.github.io/javabean-tester/dependency-info.html).

Requires java 17+

## Sites

* [site-page](http://hazendaz.github.io/javabean-tester/)
* [sonarqube](https://sonarqube.com/dashboard/index?id=com.github.hazendaz:javabean-tester)

Javabean Tester is a reflection based library for testing java beans.  Effectively test constructors, clear, getters/setters, hashcode, toString, equals, and serializable are correct.

## Left Codebox and fork network

Portions of the initial baseline (getter/setter test) were contributed by Rob Dawson ([codebox](https://github.com/codebox)).  
This project has since evolved into a full-featured version and is the primary maintained fork.  
Pull requests and issues are welcome; the original CodeBox version exists separately and lacks the additional features here.

See historical discussion here:
- [Pull Request #2](https://github.com/codebox/javabean-tester/pull/2)
- [Issue #3](https://github.com/codebox/javabean-tester/issues/3)

## Builder API documentation

The fluent builder is the supported entry point for configuring tests.

* [Builder quick start and migration guide](docs/builder-pattern.md)
* [Additional project notes](docs/README.md)

## Builder quick start

Run the standard bean checks:

```java
JavaBeanTester.builder(Test.class).test();
```

Add equals, hashCode, toString, and canEqual verification:

```java
JavaBeanTester.builder(Test.class).checkEquals().loadData().test();
```

Test two existing instances:

```java
JavaBeanTester.builder(Test.class).loadData().testEquals(instance1, instance2);
```

Skip selected bean properties:

```java
JavaBeanTester.builder(Test.class).checkEquals().loadData().skip("fieldToSkip").test();
```

Use an explicit extension type when you do not want the generated one:

```java
JavaBeanTester.builder(Test.class, Extension.class).checkEquals().test();
```

Test a private constructor:

```java
JavaBeanTester.builder(Test.class).testPrivateConstructor();
```

### Builder options

* `checkEquals()` enables equals/hashCode/toString/canEqual verification.
* `checkSerializable()` fails fast when the class under test is not serializable and validates serializable round-tripping.
* `loadData()` asks the tester to populate nested values when sample data is needed.
* `skipStrictSerializable()` relaxes the equality assertion performed after serialization for complex object graphs.
* `skip("propertyName")` excludes specific bean properties from getter/setter checks.
