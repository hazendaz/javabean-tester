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
 * Fluent configuration object returned by {@link JavaBeanTester#builder(Class)}.
 *
 * <p>
 * Configure optional checks by chaining methods such as {@link #checkEquals()}, {@link #checkSerializable()},
 * {@link #loadData()}, or {@link #skip(String...)}, then finish with a terminal method such as {@link #test()},
 * {@link #testInstance(Object)}, {@link #testEquals(Object, Object)}, {@link #testObjectMethods()}, or
 * {@link #testPrivateConstructor()}.
 *
 * @param <T>
 *            the type under test
 * @param <E>
 *            the extension type used for equality-related checks
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
     * Enables {@code clear()} verification.
     *
     * @return this builder
     */
    public JavaBeanTesterBuilder<T, E> checkClear() {
        return this.checkClear(true);
    }

    /**
     * Toggles {@code clear()} verification.
     *
     * @param value
     *            {@code true} to enable the check, {@code false} to disable it
     *
     * @return this builder
     */
    public JavaBeanTesterBuilder<T, E> checkClear(final boolean value) {
        this.worker.setCheckClear(value ? CheckClear.ON : CheckClear.OFF);
        return this;
    }

    /**
     * Enables public-constructor verification.
     *
     * @return this builder
     */
    public JavaBeanTesterBuilder<T, E> checkConstructor() {
        return this.checkConstructor(true);
    }

    /**
     * Toggles public-constructor verification.
     *
     * @param value
     *            {@code true} to enable the check, {@code false} to disable it
     *
     * @return this builder
     */
    public JavaBeanTesterBuilder<T, E> checkConstructor(final boolean value) {
        this.worker.setCheckConstructor(value ? CheckConstructor.ON : CheckConstructor.OFF);
        return this;
    }

    /**
     * Enables {@code equals}, {@code hashCode}, {@code toString}, and {@code canEqual} verification.
     *
     * @return this builder
     */
    public JavaBeanTesterBuilder<T, E> checkEquals() {
        return this.checkEquals(true);
    }

    /**
     * Toggles {@code equals}, {@code hashCode}, {@code toString}, and {@code canEqual} verification.
     *
     * @param value
     *            {@code true} to enable the check, {@code false} to disable it
     *
     * @return this builder
     */
    public JavaBeanTesterBuilder<T, E> checkEquals(final boolean value) {
        this.worker.setCheckEquals(value ? CheckEquals.ON : CheckEquals.OFF);
        return this;
    }

    /**
     * Requires the type under test to be serializable and validates a serialization round-trip.
     *
     * @return this builder
     */
    public JavaBeanTesterBuilder<T, E> checkSerializable() {
        return this.checkSerializable(true);
    }

    /**
     * Toggles strict serializable validation.
     *
     * @param value
     *            {@code true} to require serialization support, {@code false} to skip the strict requirement
     *
     * @return this builder
     */
    public JavaBeanTesterBuilder<T, E> checkSerializable(final boolean value) {
        this.worker.setCheckSerializable(value ? CheckSerialize.ON : CheckSerialize.OFF);
        return this;
    }

    /**
     * Enables recursive sample-data generation for supported property types.
     *
     * @return this builder
     */
    public JavaBeanTesterBuilder<T, E> loadData() {
        return this.loadData(true);
    }

    /**
     * Toggles recursive sample-data generation for supported property types.
     *
     * @param value
     *            {@code true} to enable generated data, {@code false} to disable it
     *
     * @return this builder
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
     * Skips the supplied bean properties during getter/setter validation.
     *
     * @param propertyNames
     *            the bean property names to exclude
     *
     * @return this builder
     */
    public JavaBeanTesterBuilder<T, E> skip(final String... propertyNames) {
        if (propertyNames != null) {
            this.worker.getSkipThese().addAll(Arrays.asList(propertyNames));
        }
        return this;
    }

    /**
     * Runs the configured bean validation flow.
     */
    public void test() {
        this.worker.test();
    }

    /**
     * Verifies that a private constructor can be exercised.
     */
    public void testPrivateConstructor() {
        ConstructorInstance.inaccessible(this.worker.getClazz());
    }

    /**
     * Runs only the object-method checks for the configured class.
     */
    public void testObjectMethods() {
        this.worker.equalsHashCodeToStringSymmetricTest();
    }

    /**
     * Runs getter/setter checks against the supplied instance.
     *
     * @param instance
     *            the instance to populate and verify
     */
    public void testInstance(final T instance) {
        this.worker.getterSetterTests(instance);
    }

    /**
     * Compares two prepared instances using the configured equality options.
     *
     * @param instance
     *            the instance under test
     * @param expected
     *            the expected comparison instance
     */
    public void testEquals(final T instance, final T expected) {
        this.worker.equalsTests(instance, expected);
    }

}
