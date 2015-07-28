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
import com.codebox.enums.LoadType;
import org.junit.Assert;
import org.mockito.cglib.beans.BeanCopier;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The Class JavaBeanTesterWorker.
 *
 * @param <T>
 *            the generic type
 * @param <E>
 *            the element type
 */
class JavaBeanTesterWorker<T, E> {

    /** The load data. */
    private LoadData    loadData;

    /** The check equals. */
    private CanEquals   checkEquals;

    /** The clazz. */
    private Class<T>    clazz;

    /** The extension. */
    private Class<E>    extension;

    /** The skip these. */
    private Set<String> skipThese = new HashSet<String>();

    /**
     * Instantiates a new java bean tester worker.
     *
     * @param clazz
     *            the clazz
     */
    JavaBeanTesterWorker(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * Instantiates a new java bean tester worker.
     *
     * @param clazz
     *            the clazz
     * @param extension
     *            the extension
     */
    JavaBeanTesterWorker(Class<T> clazz, Class<E> extension) {
        this.clazz = clazz;
        this.extension = extension;
    }

    /**
     * Tests the load methods of the specified class.
     *
     * @param <L>
     *            the type parameter associated with the class under test.
     * @param clazz
     *            the class under test.
     * @param instance
     *            the instance of class under test.
     * @param loadData
     *            load recursively all underlying data objects.
     * @param skipThese
     *            the names of any properties that should not be tested.
     * @return the java bean tester worker
     * @throws IntrospectionException
     *             thrown if the getterSetterTests method throws this exception for the class under test.
     */
    public static <L> JavaBeanTesterWorker<L, Object> load(final Class<L> clazz, final L instance,
            final LoadData loadData, final String... skipThese) throws IntrospectionException {
        JavaBeanTesterWorker<L, Object> worker = new JavaBeanTesterWorker<L, Object>(clazz);

        worker.setLoadData(loadData);
        if (skipThese != null) {
            worker.setSkipThese(new HashSet<String>(Arrays.asList(skipThese)));
        }
        worker.getterSetterTests(instance);

        return worker;
    }

    /**
     * Sets the load data.
     *
     * @param loadData
     *            the new load data
     */
    public void setLoadData(LoadData loadData) {
        this.loadData = loadData;
    }

    /**
     * Gets the load data.
     *
     * @return the load data
     */
    public LoadData getLoadData() {
        return loadData;
    }

    /**
     * Sets the check equals.
     *
     * @param checkEquals
     *            the new check equals
     */
    public void setCheckEquals(CanEquals checkEquals) {
        this.checkEquals = checkEquals;
    }

    /**
     * Gets the check equals.
     *
     * @return the check equals
     */
    public CanEquals getCheckEquals() {
        return checkEquals;
    }

    /**
     * Sets the skip these.
     *
     * @param skipThese
     *            the new skip these
     */
    public void setSkipThese(Set<String> skipThese) {
        this.skipThese = skipThese;
    }

    /**
     * Gets the skip these.
     *
     * @return the skip these
     */
    public Set<String> getSkipThese() {
        return skipThese;
    }

    /**
     * Tests the get/set/equals/hashCode/toString methods and constructors of the specified class.
     *
     * @throws IntrospectionException
     *             thrown if the getterSetterTests or equalsHashCodeToSTringSymmetricTest method throws this exception
     *             for the class under test.
     * @throws InstantiationException
     *             thrown if the getterSetterTests or equalsHashCodeToSTringSymmetricTest method throws this exception
     *             for the class under test.
     * @throws IllegalAccessException
     *             thrown if the getterSetterTests or clazz.newInstance() method throws this exception for the class
     *             under test.
     */
    public void test() throws IntrospectionException, InstantiationException, IllegalAccessException {
        getterSetterTests(clazz.newInstance());
        constructorsTest();
        if (checkEquals == CanEquals.ON) {
            equalsHashCodeToStringSymmetricTest();
        }
    }

    /**
     * Getter Setter Tests.
     *
     * @param instance
     *            the instance of class under test.
     * @throws IntrospectionException
     *             thrown if the Introspector.getBeanInfo() method throws this exception for the class under test.
     */
    void getterSetterTests(final T instance) throws IntrospectionException {
        final PropertyDescriptor[] props = Introspector.getBeanInfo(clazz).getPropertyDescriptors();
        nextProp: for (final PropertyDescriptor prop : props) {
            // Check the list of properties that we don't want to test
            for (final String skipThis : skipThese) {
                if (skipThis.equals(prop.getName())) {
                    continue nextProp;
                }
            }
            final Method getter = prop.getReadMethod();
            final Method setter = prop.getWriteMethod();

            if (getter != null && setter != null) {
                // We have both a get and set method for this property
                final Class<?> returnType = getter.getReturnType();
                final Class<?>[] params = setter.getParameterTypes();

                if (params.length == 1 && params[0] == returnType) {
                    // The set method has 1 argument, which is of the same type as the return type of the get method, so
                    // we can test this property
                    try {
                        // Build a value of the correct type to be passed to the set method
                        final Object value = buildValue(returnType, LoadType.STANDARD_DATA);

                        // Call the set method, then check the same value comes back out of the get method
                        setter.invoke(instance, value);

                        final Object expectedValue = value;
                        final Object actualValue = getter.invoke(instance);

                        Assert.assertEquals(String.format("Failed while testing property %s", prop.getName()),
                                expectedValue, actualValue);

                    } catch (final IllegalAccessException e) {
                        Assert.fail(String.format("An exception was thrown while testing the property %s: %s",
                                prop.getName(), e.toString()));
                    } catch (final IllegalArgumentException e) {
                        Assert.fail(String.format("An exception was thrown while testing the property %s: %s",
                                prop.getName(), e.toString()));
                    } catch (final InstantiationException e) {
                        Assert.fail(String.format("An exception was thrown while testing the property %s: %s",
                                prop.getName(), e.toString()));
                    } catch (final InvocationTargetException e) {
                        Assert.fail(String.format("An exception was thrown while testing the property %s: %s",
                                prop.getName(), e.toString()));
                    } catch (final SecurityException e) {
                        Assert.fail(String.format("An exception was thrown while testing the property %s: %s",
                                prop.getName(), e.toString()));
                    }
                }
            }
        }
    }

    void constructorsTest() {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            Class<?>[] types = constructor.getParameterTypes();

            Object[] values = new Object[constructor.getParameterTypes().length];

            for (int i = 0; i < values.length; i++) {
                try {
                    values[i] = buildValue(types[i], LoadType.STANDARD_DATA);
                } catch (InstantiationException e) {
                    Assert.fail(String.format("An exception was thrown while testing the constructor %s: %s",
                            constructor.getName(), e.toString()));
                } catch (IllegalAccessException e) {
                    Assert.fail(String.format("An exception was thrown while testing the constructor %s: %s",
                            constructor.getName(), e.toString()));
                } catch (InvocationTargetException e) {
                    Assert.fail(String.format("An exception was thrown while testing the constructor %s: %s",
                            constructor.getName(), e.toString()));
                }
            }

            try {
                constructor.newInstance(values);
            } catch (InstantiationException e) {
                Assert.fail(String.format("An exception was thrown while testing the constructor %s: %s",
                        constructor.getName(), e.toString()));
            } catch (IllegalAccessException e) {
                Assert.fail(String.format("An exception was thrown while testing the constructor %s: %s",
                        constructor.getName(), e.toString()));
            } catch (InvocationTargetException e) {
                Assert.fail(String.format("An exception was thrown while testing the constructor %s: %s",
                        constructor.getName(), e.toString()));
            }

            // TODO: Add checking of new object properties
        }
    }

    /**
     * Builds the value.
     *
     * @param <R>
     *            the generic type
     * @param returnType
     *            the return type
     * @param loadType
     *            the load type
     * @return the object
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     */
    private <R> Object buildValue(Class<R> returnType, LoadType loadType) throws InstantiationException,
            IllegalAccessException, InvocationTargetException {
        ValueBuilder valueBuilder = new ValueBuilder();
        valueBuilder.setLoadData(loadData);
        return valueBuilder.buildValue(returnType, loadType);
    }

    /**
     * Tests the equals/hashCode/toString methods of the specified class.
     *
     * @throws IntrospectionException
     *             thrown if the load method throws this exception for the class under test.
     * @throws InstantiationException
     *             thrown if the clazz.newInstance() method throws this exception for the class under test.
     * @throws IllegalAccessException
     *             thrown if the clazz.newIntances() method throws this exception for the class under test.
     */
    public void equalsHashCodeToStringSymmetricTest() throws IntrospectionException, InstantiationException,
            IllegalAccessException {
        // Create Instances
        final T x = clazz.newInstance();
        final T y = clazz.newInstance();
        E ext = null;
        if (extension != null) {
            ext = extension.newInstance();
        }

        // Test Empty Equals, HashCode, and ToString
        Assert.assertEquals(x, y);
        Assert.assertEquals(x.hashCode(), y.hashCode());
        Assert.assertEquals(x.toString(), y.toString());

        // Test Empty Equals, HashCode, and ToString
        if (ext != null) {
            Assert.assertNotEquals(ext, y);
            Assert.assertNotEquals(ext.hashCode(), y.hashCode());
        }

        // Test Empty One Sided Tests
        Assert.assertNotEquals(x, null);
        Assert.assertEquals(x, x);

        // Test Empty One Sided Tests
        if (ext != null) {
            Assert.assertNotEquals(ext, null);
            Assert.assertEquals(ext, ext);
        }

        // Populate Side X
        load(clazz, x, loadData);

        // Populate Side E
        if (ext != null) {
            load(extension, ext, loadData);
        }

        // ReTest Equals (flip)
        Assert.assertNotEquals(y, x);

        // ReTest Equals (flip)
        if (ext != null) {
            Assert.assertNotEquals(y, ext);
        }

        // Populate Size Y
        load(clazz, y, loadData);

        // ReTest Equals and HashCode
        if (loadData == LoadData.ON) {
            Assert.assertEquals(x, y);
            Assert.assertEquals(x.hashCode(), y.hashCode());
        } else {
            Assert.assertNotEquals(x, y);
            Assert.assertNotEquals(x.hashCode(), y.hashCode());
        }

        // ReTest Equals and HashCode
        if (ext != null) {
            Assert.assertNotEquals(ext, y);
            Assert.assertNotEquals(ext.hashCode(), y.hashCode());
            Assert.assertNotEquals(ext.toString(), y.toString());
        }

        // Create Immutable Instance
        try {
            BeanCopier clazzBeanCopier = BeanCopier.create(clazz, clazz, false);
            final T e = clazz.newInstance();
            clazzBeanCopier.copy(x, e, null);
            Assert.assertEquals(e, x);

            if (extension != null) {
                BeanCopier extensionBeanCopier = BeanCopier.create(extension, extension, false);
                final E e2 = extension.newInstance();
                extensionBeanCopier.copy(ext, e2, null);
                Assert.assertEquals(e2, ext);
            }
        } catch (final Exception e) {
            // Do nothing class is not mutable
        }
    }

    /**
     * Equals Tests will traverse one object changing values until all have been tested against another object. This is
     * done to effectively test all paths through equals.
     *
     * @param instance
     *            the class instance under test.
     * @param expected
     *            the instance expected for tests.
     * @throws IntrospectionException
     *             thrown if the Introspector.getBeanInfo() method throws this exception for the class under test.
     */
    void equalsTests(final T instance, final T expected) throws IntrospectionException {

        // Perform hashCode test dependent on data coming in
        // Assert.assertEquals(expected.hashCode(), instance.hashCode());
        if (expected.hashCode() == instance.hashCode()) {
            Assert.assertEquals(expected.hashCode(), instance.hashCode());
        } else {
            Assert.assertNotEquals(expected.hashCode(), instance.hashCode());
        }

        ValueBuilder valueBuilder = new ValueBuilder();
        valueBuilder.setLoadData(loadData);

        final PropertyDescriptor[] props = Introspector.getBeanInfo(instance.getClass()).getPropertyDescriptors();
        for (final PropertyDescriptor prop : props) {
            final Method getter = prop.getReadMethod();
            final Method setter = prop.getWriteMethod();

            if (getter != null && setter != null) {
                // We have both a get and set method for this property
                final Class<?> returnType = getter.getReturnType();
                final Class<?>[] params = setter.getParameterTypes();

                if (params.length == 1 && params[0] == returnType) {
                    // The set method has 1 argument, which is of the same type as the return type of the get method, so
                    // we can test this property
                    try {
                        // Save original value
                        final Object original = getter.invoke(instance);

                        // Build a value of the correct type to be passed to the set method using alternate test
                        Object value = valueBuilder.buildValue(returnType, LoadType.ALTERNATE_DATA);

                        // Call the set method, then check the same value comes back out of the get method
                        setter.invoke(instance, value);

                        // Check equals depending on data
                        if (instance.equals(expected)) {
                            Assert.assertEquals(expected, instance);
                        } else {
                            Assert.assertNotEquals(expected, instance);
                        }

                        // Build a value of the correct type to be passed to the set method using null test
                        value = valueBuilder.buildValue(returnType, LoadType.NULL_DATA);

                        // Call the set method, then check the same value comes back out of the get method
                        setter.invoke(instance, value);

                        // Check equals depending on data
                        if (instance.equals(expected)) {
                            Assert.assertEquals(expected, instance);
                        } else {
                            Assert.assertNotEquals(expected, instance);
                        }

                        // Reset to original value
                        setter.invoke(instance, original);

                    } catch (final IllegalAccessException e) {
                        Assert.fail(String.format("An exception was thrown while testing the property %s: %s",
                                prop.getName(), e.toString()));
                    } catch (final IllegalArgumentException e) {
                        Assert.fail(String.format("An exception was thrown while testing the property %s: %s",
                                prop.getName(), e.toString()));
                    } catch (final InstantiationException e) {
                        Assert.fail(String.format("An exception was thrown while testing the property %s: %s",
                                prop.getName(), e.toString()));
                    } catch (final InvocationTargetException e) {
                        Assert.fail(String.format("An exception was thrown while testing the property %s: %s",
                                prop.getName(), e.toString()));
                    } catch (final SecurityException e) {
                        Assert.fail(String.format("An exception was thrown while testing the property %s: %s",
                                prop.getName(), e.toString()));
                    }
                }
            }
        }
    }

}
