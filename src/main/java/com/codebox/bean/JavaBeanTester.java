/**
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright (c) 2012 - 2015 Hazendaz.
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

import java.beans.IntrospectionException;
import java.util.Arrays;
import java.util.HashSet;

/**
 * This helper class can be used to unit test the get/set/equals/canEqual/toString/hashCode methods of JavaBean-style
 * Value Objects.
 *
 * @author rob.dawson
 * @author jeremy.landis
 */
public final class JavaBeanTester {

    /**
     * JavaBeanTester constructor is private to prevent instantiation of object.
     */
    private JavaBeanTester() {
        // Hide constructor of static class.
    }

    /**
     * Tests the equals/hashCode/toString methods of the specified class.
     *
     * @param <T>
     *            the type parameter associated with the class under test.
     * @param <E>
     *            the type parameter associated with the extension class under test.
     * @param clazz
     *            the class under test.
     * @param extension
     *            extension of class under test.
     * @param loadData
     *            load underlying data with values.
     * @throws IntrospectionException
     *             thrown if the JavaBeanTester.load method throws this exception for the class under test.
     * @throws InstantiationException
     *             thrown if the clazz.newInstance() method throws this exception for the class under test.
     * @throws IllegalAccessException
     *             thrown if the clazz.newIntances() method throws this exception for the class under test.
     * @see JavaBeanTester#builder(Class)
     * @see JavaBeanTesterBuilder#testObjectMethods()
     * @deprecated in favor of builder method.
     */
    @Deprecated
    public static <T, E> void equalsHashCodeToStringSymmetricTest(final Class<T> clazz, final Class<E> extension,
            final LoadData loadData) throws IntrospectionException, InstantiationException, IllegalAccessException {
        JavaBeanTesterWorker<T, E> worker = new JavaBeanTesterWorker<T, E>(clazz, extension);
        worker.setLoadData(loadData);
        worker.equalsHashCodeToStringSymmetricTest();
    }

    /**
     * Equals Tests will traverse one object changing values until all have been tested against another object. This is
     * done to effectively test all paths through equals.
     *
     * @param <T>
     *            the type parameter associated with the class under test.
     * @param instance
     *            the class instance under test.
     * @param expected
     *            the instance expected for tests.
     * @param loadData
     *            load underlying data with values.
     * @throws IntrospectionException
     *             thrown if the Introspector.getBeanInfo() method throws this exception for the class under test.
     * @see JavaBeanTester#builder(Class)
     * @see JavaBeanTesterBuilder#testEquals(Object, Object)
     * @deprecated in favor of builder method.
     */
    @Deprecated
    public static <T> void equalsTests(final T instance, final T expected, final LoadData loadData)
            throws IntrospectionException {

        @SuppressWarnings("unchecked")
        JavaBeanTesterWorker<T, Object> worker = new JavaBeanTesterWorker<T, Object>((Class<T>) instance.getClass(), Object.class);
        worker.setLoadData(loadData);
        worker.equalsTests(instance, expected);
    }

    /**
     * Tests the load methods of the specified class.
     *
     * @param <T>
     *            the type parameter associated with the class under test.
     * @param clazz
     *            the class under test.
     * @param instance
     *            the instance of class under test.
     * @param loadData
     *            load recursively all underlying data objects.
     * @param skipThese
     *            the names of any properties that should not be tested.
     * @throws IntrospectionException
     *             thrown if the JavaBeanTester.getterSetterTests method throws this exception for the class under test.
     * @see JavaBeanTester#builder(Class)
     * @see JavaBeanTesterBuilder#testInstance(Object)
     * @deprecated in favor of builder method.
     */
    @Deprecated
    public static <T> void load(final Class<T> clazz, final T instance, final LoadData loadData,
            final String... skipThese) throws IntrospectionException {
        JavaBeanTesterWorker.load(clazz, instance, loadData, skipThese);
    }

    /**
     * Tests the get/set/equals/hashCode/toString methods of the specified class.
     *
     * @param <T>
     *            the type parameter associated with the class under test.
     * @param <E>
     *            the type parameter associated with the extension class under test.
     * @param clazz
     *            the class under test.
     * @param extension
     *            extension of class under test.
     * @param checkEquals
     *            should equals be checked (use true unless good reason not to).
     * @param loadData
     *            load recursively all underlying data objects.
     * @param skipThese
     *            the names of any properties that should not be tested.
     * @throws IntrospectionException
     *             thrown if the JavaBeanTester.getterSetterTests or JavaBeanTester.equalsHashCodeToSTringSymmetricTest
     *             method throws this exception for the class under test.
     * @throws InstantiationException
     *             thrown if the JavaBeanTester.getterSetterTests or JavaBeanTester.equalsHashCodeToSTringSymmetricTest
     *             method throws this exception for the class under test.
     * @throws IllegalAccessException
     *             thrown if the JavaBeanTester.getterSetterTests or clazz.newInstance() method throws this exception
     *             for the class under test.
     * @see JavaBeanTester#builder(Class)
     * @see JavaBeanTesterBuilder#test()
     * @deprecated in favor of builder method.
     */
    @Deprecated
    public static <T, E> void test(final Class<T> clazz, final Class<E> extension, final CanEquals checkEquals,
            final LoadData loadData, final String... skipThese) throws IntrospectionException, InstantiationException,
            IllegalAccessException {
        JavaBeanTesterWorker<T, E> worker = new JavaBeanTesterWorker<T, E>(clazz, extension);
        worker.setCheckEquals(checkEquals);
        worker.setLoadData(loadData);
        if (skipThese != null) {
            worker.setSkipThese(new HashSet<String>(Arrays.asList(skipThese)));
        }
        worker.test();
    }

    /**
     * Configure JavaBeanTester using Fluent API.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return A builder implementing the fluent API to configure JavaBeanTester
     */
    public static <T> JavaBeanTesterBuilder<T, ?> builder(Class<T> clazz) {
        return new JavaBeanTesterBuilder<T, Object>(clazz, Object.class);
    }

    /**
     * Configure JavaBeanTester using Fluent API.
     *
     * @param <T>
     *            the generic type
     * @param <E>
     *            the element type
     * @param clazz
     *            the clazz
     * @param extension
     *            the extension
     * @return A builder implementing the fluent API to configure JavaBeanTester
     */
    public static <T, E> JavaBeanTesterBuilder<T, E> builder(Class<T> clazz, Class<E> extension) {
        return new JavaBeanTesterBuilder<T, E>(clazz, extension);
    }
}
