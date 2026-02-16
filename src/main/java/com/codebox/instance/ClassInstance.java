/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.instance;

import com.codebox.bean.ValueBuilder;
import com.codebox.enums.LoadData;
import com.codebox.enums.LoadType;
import com.codebox.util.LombokBuilderUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;

/**
 * The Class Instance.
 *
 * @param <T>
 *            the element type
 */
public class ClassInstance<T> {

    /**
     * New instance. This will get the first available constructor to run the test on. This allows for instances where
     * there is intentionally not a no-arg constructor.
     *
     * @param clazz
     *            the class
     *
     * @return the t
     */
    @SuppressWarnings("unchecked")
    public final T newInstance(final Class<T> clazz) {
        // If class is null, return null
        if (clazz == null) {
            return null;
        }

        // Check if class has Lombok @Builder pattern (static builder() method)
        Method builderMethod = LombokBuilderUtil.getLombokBuilderMethod(clazz);
        if (builderMethod != null) {
            try {
                return this.createInstanceWithBuilder(clazz, builderMethod);
            } catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                Assertions.fail(String.format(
                        "An exception was thrown while creating instance using Lombok @Builder for class '%s': '%s'",
                        clazz.getName(), e.toString()));
            }
        }

        // Try no-arg constructor first
        for (final Constructor<?> constructor : clazz.getConstructors()) {
            // Skip deprecated constructors
            if (constructor.isAnnotationPresent(Deprecated.class)) {
                continue;
            }

            // Find available no-arg constructor and return it
            if (constructor.getParameterCount() == 0) {
                try {
                    return (T) constructor.newInstance();
                } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    Assertions.fail(String.format(
                            "An exception was thrown while testing class no-arg constructor (new instance) '%s': '%s'",
                            constructor.getName(), e.toString()));
                }
            }
        }

        // Try any constructor
        for (final Constructor<?> constructor : clazz.getConstructors()) {

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
                return (T) constructor.newInstance(values);
            } catch (final InstantiationException | IllegalAccessException | InvocationTargetException e) {
                Assertions.fail(String.format(
                        "An exception was thrown while testing the class (new instance) '%s' with '%s': '%s'",
                        constructor.getName(), Arrays.toString(values), e.toString()));
            }
        }
        return null;
    }

    /**
     * Create an instance using Lombok's builder pattern.
     *
     * @param clazz
     *            the class to instantiate
     * @param builderMethod
     *            the builder method
     * @return the instance created via builder
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws IllegalArgumentException
     *             the illegal argument exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws NoSuchMethodException
     *             the no such method exception
     * @throws SecurityException
     *             the security exception
     */
    @SuppressWarnings("unchecked")
    private T createInstanceWithBuilder(final Class<T> clazz, final Method builderMethod) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        // Invoke the builder() method to get a builder instance
        final T builder = (T) builderMethod.invoke(null);
        // Find the build() method on the builder
        Method buildMethod = builder.getClass().getMethod("build");
        // Invoke build() to create the instance
        return (T) buildMethod.invoke(builder);
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
    public <R> Object buildValue(final Class<R> returnType, final LoadType loadType) {
        final ValueBuilder valueBuilder = new ValueBuilder();
        valueBuilder.setLoadData(LoadData.ON);
        return valueBuilder.buildValue(returnType, loadType);
    }

}
