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
import com.codebox.enums.CanSerialize;
import com.codebox.enums.LoadData;
import com.codebox.enums.LoadType;
import com.codebox.enums.SkipStrictSerialize;
import com.codebox.instance.ClassInstance;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

import net.sf.cglib.beans.BeanCopier;

import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class JavaBeanTesterWorker.
 *
 * @param <T>
 *            the generic type
 * @param <E>
 *            the element type
 */
@Data
class JavaBeanTesterWorker<T, E> {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JavaBeanTesterWorker.class);

    /** The serializable. */
    private CanSerialize checkSerializable;

    /** The skip strict serialize. */
    private SkipStrictSerialize skipStrictSerializable;

    /** The load data. */
    private LoadData loadData;

    /** The check equals. */
    private CanEquals checkEquals;

    /** The clazz. */
    private Class<T> clazz;

    /** The extension. */
    private Class<E> extension;

    /** The skip these. */
    private Set<String> skipThese = new HashSet<>();

    /**
     * Instantiates a new java bean tester worker.
     *
     * @param newClazz
     *            the clazz
     */
    JavaBeanTesterWorker(final Class<T> newClazz) {
        this.clazz = newClazz;
    }

    /**
     * Instantiates a new java bean tester worker.
     *
     * @param newClazz
     *            the clazz
     * @param newExtension
     *            the extension
     */
    JavaBeanTesterWorker(final Class<T> newClazz, final Class<E> newExtension) {
        this.clazz = newClazz;
        this.extension = newExtension;
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
     */
    public static <L> JavaBeanTesterWorker<L, Object> load(final Class<L> clazz, final L instance,
            final LoadData loadData, final String... skipThese) {
        final JavaBeanTesterWorker<L, Object> worker = new JavaBeanTesterWorker<>(clazz);

        worker.setLoadData(loadData);
        if (skipThese != null) {
            worker.setSkipThese(new HashSet<>(Arrays.asList(skipThese)));
        }
        worker.getterSetterTests(instance);

        return worker;
    }

    /**
     * Tests the get/set/equals/hashCode/toString methods and constructors of the specified class.
     */
    public void test() {
        this.getterSetterTests(new ClassInstance<T>().newInstance(this.clazz));
        this.constructorsTest();
        this.checkSerializableTest();
        if (this.checkEquals == CanEquals.ON) {
            this.equalsHashCodeToStringSymmetricTest();
        }
    }

    /**
     * Getter Setter Tests.
     *
     * @param instance
     *            the instance of class under test.
     * @return the ter setter tests
     */
    void getterSetterTests(final T instance) {
        PropertyDescriptor[] props;
        try {
            props = Introspector.getBeanInfo(this.clazz).getPropertyDescriptors();
        } catch (final IntrospectionException e) {
            Assertions.fail(String.format("An exception was thrown while testing class '%s': '%s'",
                    this.clazz.getName(), e.toString()));
            return;
        }
        nextProp: for (final PropertyDescriptor prop : props) {
            // Check the list of properties that we don't want to test
            for (final String skipThis : this.skipThese) {
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
                        final Object value = this.buildValue(returnType, LoadType.STANDARD_DATA);

                        // Call the set method, then check the same value comes back out of the get method
                        setter.invoke(instance, value);

                        final Object expectedValue = value;
                        final Object actualValue = getter.invoke(instance);

                        Assertions.assertEquals(expectedValue, actualValue,
                                String.format("Failed while testing property '%s'", prop.getName()));

                    } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException
                            | SecurityException e) {
                        Assertions.fail(String.format(
                                "An exception was thrown while testing the property (getter/setter) '%s': '%s'",
                                prop.getName(), e.toString()));
                    }
                }
            }
        }
    }

    /**
     * Constructors test.
     */
    void constructorsTest() {
        for (final Constructor<?> constructor : this.clazz.getConstructors()) {
            final Class<?>[] types = constructor.getParameterTypes();

            final Object[] values = new Object[constructor.getParameterTypes().length];

            for (int i = 0; i < values.length; i++) {
                values[i] = this.buildValue(types[i], LoadType.STANDARD_DATA);
            }

            try {
                constructor.newInstance(values);
            } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                Assertions.fail(String.format("An exception was thrown while testing the constructor(s) '%s': '%s'",
                        constructor.getName(), e.toString()));
            }

            // TODO: Add checking of new object properties
        }
    }

    /**
     * Check Serializable test.
     */
    void checkSerializableTest() {
        final T object = new ClassInstance<T>().newInstance(this.clazz);
        if (this.implementsSerializable(object)) {
            final T newObject = this.canSerialize(object);
            if (this.skipStrictSerializable != SkipStrictSerialize.ON) {
                Assertions.assertEquals(object, newObject);
            } else {
                Assertions.assertNotEquals(object, newObject);
            }
            return;
        }
        if (this.checkSerializable == CanSerialize.ON) {
            Assertions.fail(String.format("Class is not serializable '%s'", object.getClass().getName()));
        }
    }

    /**
     * Implements serializable.
     *
     * @param object
     *            the object
     * @return true, if successful
     */
    boolean implementsSerializable(final T object) {
        return object instanceof Serializable || object instanceof Externalizable;
    }

    /**
     * Can serialize.
     *
     * @param object
     *            the object
     * @return object read after serialization
     */
    <T> T canSerialize(final T object) {
        // Serialize data
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            new ObjectOutputStream(baos).writeObject(object);
        } catch (final IOException e) {
            Assertions.fail(String.format("An exception was thrown while serializing the class '%s': '%s',",
                    object.getClass().getName(), e.toString()));
            return null;
        }

        // Deserialize Data
        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        try {
            return (T) new ObjectInputStream(bais).readObject();
        } catch (final ClassNotFoundException | IOException e) {
            Assertions.fail(String.format("An exception was thrown while deserializing the class '%s': '%s',",
                    object.getClass().getName(), e.toString()));
        }
        return null;
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
     */
    private <R> Object buildValue(final Class<R> returnType, final LoadType loadType) {
        final ValueBuilder valueBuilder = new ValueBuilder();
        valueBuilder.setLoadData(this.loadData);
        return valueBuilder.buildValue(returnType, loadType);
    }

    /**
     * Tests the equals/hashCode/toString methods of the specified class.
     */
    public void equalsHashCodeToStringSymmetricTest() {
        // Create Instances
        final T x = new ClassInstance<T>().newInstance(this.clazz);
        final T y = new ClassInstance<T>().newInstance(this.clazz);

        // TODO Internalize extension will require canEquals, equals, hashcode, and toString overrides.
        /*
         * try { this.extension = (Class<E>) new ExtensionBuilder<T>().generate(this.clazz); } catch (NotFoundException
         * e) { Assert.fail(e.getMessage()); } catch (CannotCompileException e) { Assert.fail(e.getMessage()); }
         */
        final E ext = new ClassInstance<E>().newInstance(this.extension);

        // Test Empty Equals, HashCode, and ToString
        Assertions.assertEquals(x, y);
        Assertions.assertEquals(x.hashCode(), y.hashCode());
        Assertions.assertEquals(x.toString(), y.toString());

        // Test Extension Empty Equals, HashCode, and ToString
        Assertions.assertNotEquals(ext, y);
        Assertions.assertNotEquals(ext.hashCode(), y.hashCode());

        // Test Empty One Sided Tests
        Assertions.assertNotEquals(x, null);
        Assertions.assertEquals(x, x);

        // Test Extension Empty One Sided Tests
        Assertions.assertNotEquals(ext, null);
        Assertions.assertEquals(ext, ext);

        // Populate Side X
        JavaBeanTesterWorker.load(this.clazz, x, this.loadData);

        // Populate Extension Side E
        JavaBeanTesterWorker.load(this.extension, ext, this.loadData);

        // ReTest Equals (flip)
        Assertions.assertNotEquals(y, x);

        // ReTest Extension Equals (flip)
        Assertions.assertNotEquals(y, ext);

        // Populate Size Y
        JavaBeanTesterWorker.load(this.clazz, y, this.loadData);

        // ReTest Equals and HashCode
        if (this.loadData == LoadData.ON) {
            Assertions.assertEquals(x, y);
            Assertions.assertEquals(x.hashCode(), y.hashCode());
        } else {
            Assertions.assertNotEquals(x, y);
            Assertions.assertNotEquals(x.hashCode(), y.hashCode());
        }

        // ReTest Extension Equals and HashCode
        Assertions.assertNotEquals(ext, y);
        Assertions.assertNotEquals(ext.hashCode(), y.hashCode());
        Assertions.assertNotEquals(ext.toString(), y.toString());

        // Create Immutable Instance
        try {
            final BeanCopier clazzBeanCopier = BeanCopier.create(this.clazz, this.clazz, false);
            final T e = new ClassInstance<T>().newInstance(this.clazz);
            clazzBeanCopier.copy(x, e, null);
            Assertions.assertEquals(e, x);
        } catch (final Exception e) {
            JavaBeanTesterWorker.LOGGER.trace("Do nothing class is not mutable", e.toString());
        }

        // Create Extension Immutable Instance
        try {
            final BeanCopier extensionBeanCopier = BeanCopier.create(this.extension, this.extension, false);
            final E e2 = new ClassInstance<E>().newInstance(this.extension);
            extensionBeanCopier.copy(ext, e2, null);
            Assertions.assertEquals(e2, ext);
        } catch (final Exception e) {
            JavaBeanTesterWorker.LOGGER.trace("Do nothing class is not mutable", e.toString());
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
     */
    void equalsTests(final T instance, final T expected) {

        // Perform hashCode test dependent on data coming in
        // Assert.assertEquals(expected.hashCode(), instance.hashCode());
        if (expected.hashCode() == instance.hashCode()) {
            Assertions.assertEquals(expected.hashCode(), instance.hashCode());
        } else {
            Assertions.assertNotEquals(expected.hashCode(), instance.hashCode());
        }

        final ValueBuilder valueBuilder = new ValueBuilder();
        valueBuilder.setLoadData(this.loadData);

        PropertyDescriptor[] props;
        try {
            props = Introspector.getBeanInfo(instance.getClass()).getPropertyDescriptors();
        } catch (final IntrospectionException e) {
            Assertions.fail(String.format("An exception occurred during introspection of '%s': '%s'",
                    instance.getClass().getName(), e.toString()));
            return;
        }
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
                            Assertions.assertEquals(expected, instance);
                        } else {
                            Assertions.assertNotEquals(expected, instance);
                        }

                        // Build a value of the correct type to be passed to the set method using null test
                        value = valueBuilder.buildValue(returnType, LoadType.NULL_DATA);

                        // Call the set method, then check the same value comes back out of the get method
                        setter.invoke(instance, value);

                        // Check equals depending on data
                        if (instance.equals(expected)) {
                            Assertions.assertEquals(expected, instance);
                        } else {
                            Assertions.assertNotEquals(expected, instance);
                        }

                        // Reset to original value
                        setter.invoke(instance, original);

                    } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException
                            | SecurityException e) {
                        Assertions.fail(
                                String.format("An exception was thrown while testing the property (equals) '%s': '%s'",
                                        prop.getName(), e.toString()));
                    }
                }
            }
        }
    }

}
