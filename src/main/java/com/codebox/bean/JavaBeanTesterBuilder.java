/**
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright (c) 2012 - 2017 Hazendaz.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of The Apache Software License,
 * Version 2.0 which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Contributors:
 *     CodeBox (Rob Dawson).
 *     Hazendaz (Jeremy Landis).
 */
package com.codebox.bean;

import com.codebox.enums.CanEquals;
import com.codebox.enums.LoadData;
import com.codebox.instance.ConstructorInstance;
import com.codebox.enums.CanSerialize;

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
    private JavaBeanTesterWorker<T, E> worker;

    /**
     * Instantiates a new java bean tester builder.
     *
     * @param clazz
     *            the clazz
     */
    JavaBeanTesterBuilder(final Class<T> clazz) {
        this.worker = new JavaBeanTesterWorker<T, E>(clazz);
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
        this.worker = new JavaBeanTesterWorker<T, E>(clazz, extension);
    }

    /**
     * Check equals.
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> checkEquals() {
        return checkEquals(true);
    }

    /**
     * Check equals.
     *
     * @param value
     *            the value
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> checkEquals(final boolean value) {
        this.worker.setCheckEquals(value ? CanEquals.ON : CanEquals.OFF);
        return this;
    }

    /**
     * Load data.
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> loadData() {
        return loadData(true);
    }

    /**
     * Load data.
     *
     * @param value
     *            the value
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> loadData(final boolean value) {
        this.worker.setLoadData(value ? LoadData.ON : LoadData.OFF);
        return this;
    }

    /**
     * Check Serializable.
     *
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> checkSerializable() {
        return checkSerializable(true);
    }

    /**
     * Check Serializable.
     *
     * @param value
     *            the value
     * @return the java bean tester builder
     */
    public JavaBeanTesterBuilder<T, E> checkSerializable(final boolean value) {
        this.worker.setCheckSerializable(value ? CanSerialize.ON : CanSerialize.OFF);
        return this;
    }

    /**
     * Skip.
     *
     * @param propertyNames
     *            the property names
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
