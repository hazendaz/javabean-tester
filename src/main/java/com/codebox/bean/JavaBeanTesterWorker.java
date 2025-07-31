/*
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright 2012-2025 Hazendaz.
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

import com.codebox.enums.CheckClear;
import com.codebox.enums.CheckConstructor;
import com.codebox.enums.CheckEquals;
import com.codebox.enums.CheckSerialize;
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
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

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

    /** The Constant logger. */
    private static final Logger logger = LoggerFactory.getLogger(JavaBeanTesterWorker.class);

    /** The check clear. */
    private CheckClear checkClear;

    /** The check constructor. */
    private CheckConstructor checkConstructor;

    /** The check equals. */
    private CheckEquals checkEquals;

    /** The check serializable. */
    private CheckSerialize checkSerializable;

    /** The load data. */
    private LoadData loadData;

    /** The clazz. */
    private final Class<T> clazz;

    /** The extension. */
    private Class<E> extension;

    /** The skip strict serialize. */
    private SkipStrictSerialize skipStrictSerializable;

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
     *
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
     * Tests the clear, get, set, equals, hashCode, toString, serializable, and constructor(s) methods of the specified
     * class.
     */
    public void test() {

        // Test Getter/Setter
        this.getterSetterTests(new ClassInstance<T>().newInstance(this.clazz));

        // Test Clear
        if (this.checkClear != CheckClear.OFF) {
            this.clearTest();
        }

        // Test constructor
        if (this.checkConstructor != CheckConstructor.OFF) {
            this.constructorsTest();
        }

        // Test Serializable (internally uses on/off/strict checks)
        this.checkSerializableTest();

        // Test Equals
        if (this.checkEquals == CheckEquals.ON) {
            this.equalsHashCodeToStringSymmetricTest();
        }

    }

    /**
     * Getter Setter Tests.
     *
     * @param instance
     *            the instance of class under test.
     *
     * @return the ter setter tests
     */
    void getterSetterTests(final T instance) {
        final PropertyDescriptor[] props = this.getProps(this.clazz);
        for (final PropertyDescriptor prop : props) {
            Method getter = prop.getReadMethod();
            final Method setter = prop.getWriteMethod();

            // Java Metro Bug Patch (Boolean Wrapper usage of 'is' possible
            if (getter == null && setter != null) {
                final String isBooleanWrapper = "is" + setter.getName().substring(3);
                try {
                    getter = this.clazz.getMethod(isBooleanWrapper);
                } catch (NoSuchMethodException | SecurityException e) {
                    // Do nothing
                }
            }

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

                        // Build an instance of the bean that we are testing (each property test gets a new instance)
                        final T bean = new ClassInstance<T>().newInstance(this.clazz);

                        // Call the set method, then check the same value comes back out of the get method
                        setter.invoke(bean, value);

                        // Use data set on instance
                        setter.invoke(instance, value);

                        final Object expectedValue = value;
                        Object actualValue = getter.invoke(bean);

                        // java.util.Date normalization patch
                        //
                        // Date is zero based so it adds 1 through normalization. Since we always pass '1' here, it is
                        // the same as stating February. Thus we roll over the month quite often into March towards
                        // end of the month resulting in '1' != '2' situation. The reason we pass '1' is that we are
                        // testing the content of the object and have no idea it is a date to start with. It is simply
                        // that it sees getters/setters and tries to load them appropriately. The underlying problem
                        // with that is that the Date object performs normalization to avoid dates like 2-30 that do
                        // not exist and is not a typical getter/setter use-case. It is also deprecated but we don't
                        // want to simply skip all deprecated items as we intend to test as much as possible.
                        //
                        if (this.clazz == Date.class && prop.getName().equals("month")
                                && expectedValue.equals(Integer.valueOf("1"))
                                && actualValue.equals(Integer.valueOf("2"))) {
                            actualValue = Integer.valueOf("1");
                        }

                        Assertions.assertEquals(expectedValue, actualValue,
                                String.format("Failed while testing property '%s' of class '%s'", prop.getName(),
                                        this.clazz.getName()));

                    } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException
                            | SecurityException e) {
                        Assertions.fail(String.format(
                                "An exception was thrown while testing class '%s' with the property (getter/setter) '%s': '%s'",
                                this.clazz.getName(), prop.getName(), e.toString()));
                    }
                }
            }
        }
    }

    /**
     * Clear test.
     */
    void clearTest() {
        final Method[] methods = this.clazz.getDeclaredMethods();
        for (final Method method : methods) {
            if (method.getName().equals("clear")) {
                final T newClass = new ClassInstance<T>().newInstance(this.clazz);
                final T expectedClass = new ClassInstance<T>().newInstance(this.clazz);
                try {
                    // Perform any Post Construction on object without parameters
                    List<Annotation> annotations = null;
                    for (final Method mt : methods) {
                        annotations = Arrays.asList(mt.getAnnotations());
                        for (final Annotation annotation : annotations) {
                            // XXX On purpose logic change to support both javax and jakarta namespace for annotations
                            if ("PostConstruct".equals(annotation.annotationType().getSimpleName())
                                    && mt.getParameterTypes().length == 0) {
                                // Invoke method newClass
                                mt.invoke(newClass);
                                // Invoke method expectedClass
                                mt.invoke(expectedClass);
                            }
                        }
                    }
                    // Invoke clear only on newClass
                    newClass.getClass().getMethod("clear").invoke(newClass);
                    Assertions.assertEquals(expectedClass, newClass,
                            String.format("Clear method does not match new object '%s'", this.clazz));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
                        | NoSuchMethodException | SecurityException e) {
                    Assertions.fail(String.format("An exception was thrown while testing the Clear method '%s' : '%s'",
                            this.clazz.getName(), e.toString()));
                }
            }
        }
    }

    /**
     * Constructors test.
     */
    void constructorsTest() {
        for (final Constructor<?> constructor : this.clazz.getConstructors()) {

            // Skip deprecated constructors
            if (constructor.isAnnotationPresent(Deprecated.class)) {
                continue;
            }

            final Class<?>[] types = constructor.getParameterTypes();

            final Object[] values = new Object[constructor.getParameterTypes().length];

            // Load Data
            for (int i = 0; i < values.length; i++) {
                values[i] = this.buildValue(types[i], LoadType.STANDARD_DATA);
            }

            try {
                constructor.newInstance(values);
            } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                Assertions.fail(
                        String.format("An exception was thrown while testing the constructor(s) '%s' with '%s': '%s'",
                                constructor.getName(), Arrays.toString(values), e.toString()));
            }

            // TODO 1/12/2019 JWL Add checking of new object properties
        }
    }

    /**
     * Check Serializable test.
     */
    void checkSerializableTest() {
        final T object = new ClassInstance<T>().newInstance(this.clazz);
        if (this.implementsSerializable(object)) {
            final T newObject = this.canSerialize(object);
            // Toggle to throw or not throw error with only one way working
            if (this.skipStrictSerializable != SkipStrictSerialize.ON) {
                Assertions.assertEquals(object, newObject);
            } else {
                Assertions.assertNotEquals(object, newObject);
            }
            return;
        }

        // Only throw error when specifically checking on serialization
        if (this.checkSerializable == CheckSerialize.ON) {
            Assertions.fail(String.format("Class is not serializable '%s'", object.getClass().getName()));
        }
    }

    /**
     * Implements serializable.
     *
     * @param object
     *            the object
     *
     * @return true, if successful
     */
    boolean implementsSerializable(final T object) {
        return object instanceof Externalizable || object instanceof Serializable;
    }

    /**
     * Can serialize.
     *
     * @param object
     *            the object
     *
     * @return object read after serialization
     */
    @SuppressWarnings("unchecked")
    T canSerialize(final T object) {
        byte[] byteArray = null;

        // Serialize data
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            byteArray = baos.toByteArray();
        } catch (final IOException e) {
            Assertions.fail(String.format("An exception was thrown while serializing the class '%s': '%s',",
                    object.getClass().getName(), e.toString()));
            return null;
        }

        // Deserialize Data
        try (ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
                ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
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
     *
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
        // Run Equals Verifier
        try {
            EqualsVerifier.simple().forClass(this.clazz).suppress(Warning.BIGDECIMAL_EQUALITY).verify();
        } catch (AssertionError e) {
            JavaBeanTesterWorker.logger.warn("EqualsVerifier attempt failed: {}", e.getMessage());
        }

        // Create Instances
        final T x = new ClassInstance<T>().newInstance(this.clazz);
        final T y = new ClassInstance<T>().newInstance(this.clazz);

        Assertions.assertNotNull(x,
                String.format("Create new instance of class '%s' resulted in null", this.clazz.getName()));
        Assertions.assertNotNull(y,
                String.format("Create new instance of class '%s' resulted in null", this.clazz.getName()));

        // TODO 1/12/2019 JWL Internalize extension will require canEquals, equals, hashcode, and toString overrides.
        /*
         * try { this.extension = (Class<E>) new ExtensionBuilder<T>().generate(this.clazz); } catch (NotFoundException
         * e) { Assert.fail(e.getMessage()); } catch (CannotCompileException e) { Assert.fail(e.getMessage()); }
         */
        final E ext = new ClassInstance<E>().newInstance(this.extension);

        Assertions.assertNotNull(ext,
                String.format("Create new instance of extension %s resulted in null", this.extension.getName()));

        // Test Equals, HashCode, and ToString on Empty Objects
        Assertions.assertEquals(x, y,
                String.format(".equals() should be consistent for two empty objects of type %s", this.clazz.getName()));
        Assertions.assertEquals(x.hashCode(), y.hashCode(), String
                .format(".hashCode() should be consistent for two empty objects of type %s", this.clazz.getName()));
        Assertions.assertEquals(x.toString(), y.toString(), String
                .format(".toString() should be consistent for two empty objects of type %s", this.clazz.getName()));

        // Test Extension Equals, HashCode, and ToString on Empty Objects
        Assertions.assertNotEquals(ext, y,
                String.format(".equals() should not be equal for extension of type %s and empty object of type %s",
                        this.extension.getName(), this.clazz.getName()));
        Assertions.assertNotEquals(ext.hashCode(), y.hashCode(),
                String.format(".hashCode() should not be equal for extension of type %s and empty object of type %s",
                        this.extension.getName(), this.clazz.getName()));
        Assertions.assertNotEquals(ext.toString(), y.toString(),
                String.format(".toString() should not be equal for extension of type %s and empty object of type %s",
                        this.extension.getName(), this.clazz.getName()));

        // Test One Sided Tests on Empty Objects
        Assertions.assertNotEquals(x, null,
                String.format("An empty object of type %s should not be equal to null", this.clazz.getName()));
        Assertions.assertEquals(x, x,
                String.format("An empty object of type %s should be equal to itself", this.clazz.getName()));

        // Test Extension One Sided Tests on Empty Objects
        Assertions.assertNotEquals(ext, null,
                String.format("An empty extension of type %s should not be equal to null", this.clazz.getName()));
        Assertions.assertEquals(ext, ext,
                String.format("An empty extension of type %s should be equal to itself", this.extension.getName()));

        // If the class has setters, the previous tests would have been against empty classes
        // If so, load the classes and re-test
        if (this.classHasSetters(this.clazz)) {
            // Populate Side X
            JavaBeanTesterWorker.load(this.clazz, x, this.loadData);

            // Populate Extension Side Ext
            JavaBeanTesterWorker.load(this.extension, ext, this.loadData);

            // ReTest Equals (flip)
            Assertions.assertNotEquals(y, x,
                    String.format(".equals() should not be consistent for one empty and one loaded object of type %s",
                            this.clazz.getName()));

            // ReTest Extension Equals (flip)
            Assertions.assertNotEquals(y, ext,
                    String.format(".equals() should not be equal for extension of type %s and empty object of type %s",
                            this.extension.getName(), this.clazz.getName()));

            // Populate Size Y
            JavaBeanTesterWorker.load(this.clazz, y, this.loadData);

            // ReTest Equals and HashCode
            if (this.loadData == LoadData.ON) {
                Assertions.assertEquals(x, y,
                        String.format(".equals() should be equal for two instances of type %s with loaded data",
                                this.clazz.getName()));
                Assertions.assertEquals(x.hashCode(), y.hashCode(),
                        String.format(".hashCode() should be equal for two instances of type %s with loaded data",
                                this.clazz.getName()));
            } else {
                Assertions.assertNotEquals(x, y);
                Assertions.assertNotEquals(x.hashCode(), y.hashCode());
            }

            // ReTest Extension Equals, HashCode, and ToString
            Assertions.assertNotEquals(ext, y,
                    String.format(".equals() should not be equal for extension of type %s and empty object of type %s",
                            this.extension.getName(), this.clazz.getName()));
            Assertions.assertNotEquals(ext.hashCode(), y.hashCode(),
                    String.format(
                            ".hashCode() should not be equal for extension of type %s and empty object of type %s",
                            this.extension.getName(), this.clazz.getName()));
            Assertions.assertNotEquals(ext.toString(), y.toString(),
                    String.format(
                            ".toString() should not be equal for extension of type %s and empty object of type %s",
                            this.extension.getName(), this.clazz.getName()));
        }

        // Create Immutable Instance
        try {
            final T e = new ClassInstance<T>().newInstance(this.clazz);
            ByteBuddyBeanCopier.copy(x, e, (value, targetType) -> {
                if (targetType == boolean.class) {
                    return value == null ? Boolean.FALSE : value;
                }
                return value;
            });
            Assertions.assertEquals(e, x);
        } catch (final Exception e) {
            JavaBeanTesterWorker.logger.trace("Do nothing class is not mutable", e);
        }

        // If class is final, use Object.class for comparison needs
        if (Modifier.isFinal(clazz.getModifiers())) {
            JavaBeanTesterWorker.logger.trace("Final object does not go through final equals check");
            return;
        }

        // Create Extension Immutable Instance
        try {
            final E e = new ClassInstance<E>().newInstance(this.extension);
            ByteBuddyBeanCopier.copy(ext, e, (value, targetType) -> {
                if (targetType == boolean.class) {
                    return value == null ? Boolean.FALSE : value;
                }
                return value;
            });
            Assertions.assertEquals(e, ext);
        } catch (final Exception e) {
            JavaBeanTesterWorker.logger.trace("Do nothing class is not mutable", e);
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

        final PropertyDescriptor[] props = this.getProps(instance.getClass());
        for (final PropertyDescriptor prop : props) {
            Method getter = prop.getReadMethod();
            final Method setter = prop.getWriteMethod();

            // Java Metro Bug Patch (Boolean Wrapper usage of 'is' possible
            if (getter == null && setter != null) {
                final String isBooleanWrapper = "is" + setter.getName().substring(3);
                try {
                    getter = this.clazz.getMethod(isBooleanWrapper);
                } catch (NoSuchMethodException | SecurityException e) {
                    // Do nothing
                }
            }

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

    /**
     * Class has setters.
     *
     * @param clazz
     *            the clazz
     *
     * @return true, if successful
     */
    private boolean classHasSetters(final Class<T> clazz) {
        return Arrays.stream(this.getProps(clazz))
                .anyMatch(propertyDescriptor -> propertyDescriptor.getWriteMethod() != null);
    }

    /**
     * Gets the props.
     *
     * @param clazz
     *            the clazz
     *
     * @return the props
     */
    private PropertyDescriptor[] getProps(final Class<?> clazz) {
        try {
            final List<PropertyDescriptor> usedProps = new ArrayList<>(
                    Introspector.getBeanInfo(clazz).getPropertyDescriptors().length);
            final List<PropertyDescriptor> props = Arrays
                    .asList(Introspector.getBeanInfo(clazz).getPropertyDescriptors());
            for (final PropertyDescriptor prop : props) {
                // Check the list of properties that we don't want to test
                if (this.skipThese.contains(prop.getName())) {
                    continue;
                }
                usedProps.add(prop);
            }
            return usedProps.toArray(new PropertyDescriptor[0]);
        } catch (final IntrospectionException e) {
            Assertions.fail(String.format("An exception was thrown while testing class '%s': '%s'",
                    this.clazz.getName(), e.toString()));
            return new PropertyDescriptor[0];
        }
    }

}
