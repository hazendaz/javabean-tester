javabean-tester
===============
[![Build Status](https://travis-ci.org/hazendaz/javabean-tester.svg?branch=master)](https://travis-ci.org/hazendaz/javabean-tester)
[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.github.hazendaz/javabean-tester/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.hazendaz/javabean-tester)
[![Apache 2](http://img.shields.io/badge/license-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

[site-page](http://hazendaz.github.io/javabean-tester/)

Javabean Tester is a reflection based library for testing java beans.  Effectively test getters/setters, hashcode, toString, equals are correct.

## Left Codebox

Original maintainer [codebox](https://github.com/codebox) only intended for this project to be a simple class that users would make additional
changes to.  Over time, this copy has evolved into a full project and now is the primary project going forwards.  Issues has been opened up
for use and pull requests are gladly appreciated.

## Documentation Status

Actively accepting pull requests for documentation of this library.  Focus will only be on new builder pattern.  Vast majority of examples exist in the test package.

## Example Usage
```java
JavaBeanTester.builder(Test.class, Extension.class).checkEquals().checkSerializable().loadData().skip("FieldToSkip", "AnotherFieldToSkip").test();
```

Check Equals will perform equality checks.  This applies when hashcode, toString, and equals/canEqual are setup.

Check Serializable will perform a serialization check.  This ensures that just because a class is marked as serializable that it really is serializable including children.

Load Data will load underlying data as best possible for basic java types.

Skip will skip all included elements from underlying getter/setter checks.