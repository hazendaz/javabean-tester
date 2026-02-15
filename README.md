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
  <version>2.11.2</version>
  <scope>test</scope>
</dependency>
```

Or using Gradle:

```groovy
testImplementation 'com.github.hazendaz:javabean-tester:2.11.2'
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

## Documentation Status

Actively accepting pull requests for documentation of this library.  Focus will only be on new builder pattern.  Vast majority of examples exist in the test package.

## Example Usage

```java
JavaBeanTester.builder(Test.class, Extension.class).checkEquals().checkSerializable()
        .loadData().skipStrictSerializable().skip("FieldToSkip", "AnotherFieldToSkip").test();
```

```java
JavaBeanTester.builder(Test.class).loadData().testEquals(instance1, instance2);
```

```java
JavaBeanTester.builder(Test.class).testPrivateConstructor();
```

Check Equals will perform equality checks.  This applies when hashcode, toString, and equals/canEqual are setup.

Check Serializable will perform a serialization check.  This ensures that just because a class is marked as serializable that it really is serializable including children.

Load Data will load underlying data as best possible for basic java types.

Skip Strict Serializable will not perform a serialization check as this is only valid for POJOs currently.

Skip will skip all included elements from underlying getter/setter checks.
