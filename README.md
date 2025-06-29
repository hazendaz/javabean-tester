# javabean-tester

[![Java CI](https://github.com/hazendaz/javabean-tester/actions/workflows/Java%20CI/badge.svg)](https://github.com/hazendaz/javabean-tester/actions/workflows/Java%20CI)
[![Coverage Status](https://coveralls.io/repos/github/hazendaz/javabean-tester/badge.svg?branch=master)](https://coveralls.io/github/hazendaz/javabean-tester?branch=master)
[![Renovate enabled](https://img.shields.io/badge/renovate-enabled-brightgreen.svg)](https://renovatebot.com/)
[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.github.hazendaz/javabean-tester/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.hazendaz/javabean-tester)
[![Project Stats](https://www.openhub.net/p/javabean-tester/widgets/project_thin_badge.gif)](https://www.openhub.net/p/javabean-tester)
[![Apache 2](http://img.shields.io/badge/license-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

## Latest Release

You can download source binaries from our [releases page](https://github.com/hazendaz/javabean-tester/releases) and release binaries from [Maven central](https://search.maven.org/search?q=g:com.github.hazendaz%20AND%20a:javabean-tester)

Alternatively you can pull it using Maven:

```xml
<dependency>
  <groupId>com.github.hazendaz</groupId>
  <artifactId>javabean-tester</artifactId>
  <version>2.9.0</version>
  <scope>test</scope>
</dependency>
```

Or using Gradle:

```groovy
testImplementation 'com.github.hazendaz:javabean-tester:2.9.0'
```

Information for other build frameworks can be found [here](http://hazendaz.github.io/javabean-tester/dependency-info.html).

Requires java 17+

## Sites

* [site-page](http://hazendaz.github.io/javabean-tester/)
* [sonarqube](https://sonarqube.com/dashboard/index?id=com.github.hazendaz:javabean-tester)

Javabean Tester is a reflection based library for testing java beans.  Effectively test constructors, clear, getters/setters, hashcode, toString, equals, and serializable are correct.

## Left Codebox

Original maintainer [codebox](https://github.com/codebox) only intended for this project to be a simple class that users would make additional
changes to.  Over time, this copy has evolved into a full project and now is the primary project going forwards.  Issues has been opened up
for use and pull requests are gladly appreciated.  Original maintainer has now made his version gradle enabled build but it still lacks additional features found here.

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
