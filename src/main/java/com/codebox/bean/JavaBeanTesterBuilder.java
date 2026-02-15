/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.bean;

import com.codebox.enums.CheckClear;
import com.codebox.enums.CheckConstructor;
import com.codebox.enums.CheckEquals;
import com.codebox.enums.CheckSerialize;
import com.codebox.enums.LoadData;
import com.codebox.enums.SkipStrictSerialize;
import com.codebox.instance.ConstructorInstance;

import java.util.Arrays;

/**
 * The Class JavaBeanTesterBuilder.
 *
 * @param <T>
 *            the generic type
 * @param <E>
 *            the element type
 */
public class JavaBeanTesterBuilder<T, E> {

    /** The worker. */
    private final JavaBeanTesterWorker<T, E> worker;

    /**
     * Instantiates a new java bean tester builder.
     *
     * @param clazz
     *            the clazz
     */
    JavaBeanTesterBuilder(final Class<T> clazz) {
        this.worker = new JavaBeanTesterWorker<>(clazz);
    }

    /**
     * Instantiates a new java bean tester builder.
     *
     * @param clazz
     *            the clazz
     * @param extension
     *            the extension
     */
    JavaBeanTesterBuilder(final Class<T> clazz, final Class<E> extension) {
        this.worker = new JavaBeanTesterWorker<>(clazz, extension);
    }

    /**
     * Check Clear.
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> checkClear() {
        return this.checkClear(true);
    }

    /**
     * Check Clear.
     *
     * @param value
     *            the value
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> checkClear(final boolean value) {
        this.worker.setCheckClear(value ? CheckClear.ON : CheckClear.OFF);
        return this;
    }

    /**
     * Check Constructor.
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> checkConstructor() {
        return this.checkConstructor(true);
    }

    /**
     * Check Constructor.
     *
     * @param value
     *            the value
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> checkConstructor(final boolean value) {
        this.worker.setCheckConstructor(value ? CheckConstructor.ON : CheckConstructor.OFF);
        return this;
    }

    /**
     * Check equals.
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> checkEquals() {
        return this.checkEquals(true);
    }

    /**
     * Check equals.
     *
     * @param value
     *            the value
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> checkEquals(final boolean value) {
        this.worker.setCheckEquals(value ? CheckEquals.ON : CheckEquals.OFF);
        return this;
    }

    /**
     * Check Serializable.
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> checkSerializable() {
        return this.checkSerializable(true);
    }

    /**
     * Check Serializable.
     *
     * @param value
     *            the value
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> checkSerializable(final boolean value) {
        this.worker.setCheckSerializable(value ? CheckSerialize.ON : CheckSerialize.OFF);
        return this;
    }

    /**
     * Load data.
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> loadData() {
        return this.loadData(true);
    }

    /**
     * Load data.
     *
     * @param value
     *            the value
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> loadData(final boolean value) {
        this.worker.setLoadData(value ? LoadData.ON : LoadData.OFF);
        return this;
    }

    /**
     * Skip Strict Serializable is intended to relax strict check on serializable objects. For complex objects, strict
     * checking will result in issues with equals check. Testing has shown this to be generally not a normal use case of
     * javabean tester as it is normally used with POJOs only. In such a case, caller will get an error and if there is
     * not actually a code problem they should turn this skip on.
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> skipStrictSerializable() {
        this.worker.setSkipStrictSerializable(SkipStrictSerialize.ON);
        return this;
    }

    /**
     * Skip.
     *
     * @param propertyNames
     *            the property names
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> skip(final String... propertyNames) {
        if (propertyNames != null) {
            this.worker.getSkipThese().addAll(Arrays.asList(propertyNames));
        }
        return this;
    }

    /**
     * Test.
     */
    public void test() {
        this.worker.test();
    }

    /**
     * Private Constructor Test.
     */
    public void testPrivateConstructor() {
        ConstructorInstance.inaccessible(this.worker.getClazz());
    }

    /**
     * Tests the equals/hashCode/toString methods of the specified class.
     */
    public void testObjectMethods() {
        this.worker.equalsHashCodeToStringSymmetricTest();
    }

    /**
     * Getter Setter Tests.
     *
     * @param instance
     *            the instance of class under test.
     */
    public void testInstance(final T instance) {
        this.worker.getterSetterTests(instance);
    }

    /**
     * Test equals.
     *
     * @param instance
     *            the instance
     * @param expected
     *            the expected
     */
    public void testEquals(final T instance, final T expected) {
        this.worker.equalsTests(instance, expected);
    }

}
