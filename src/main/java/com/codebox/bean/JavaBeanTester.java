/*
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright 2012-2021 Hazendaz.
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

/**
 * This helper class can be used to unit test the get/set/equals/canEqual/toString/hashCode methods of JavaBean-style
 * Value Objects.
 *
 * @author rob.dawson
 * @author jeremy.landis
 */
public enum JavaBeanTester {

    // Private Usage
    ;

    /**
     * Configure JavaBeanTester using Fluent API.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return A builder implementing the fluent API to configure JavaBeanTester
     */
    public static <T> JavaBeanTesterBuilder<T, ?> builder(final Class<T> clazz) {
        // TODO 1/12/2019 JWL For now push in Object.class as we eventually will internalize setup
        return new JavaBeanTesterBuilder<>(clazz, Object.class);
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
    public static <T, E> JavaBeanTesterBuilder<T, E> builder(final Class<T> clazz, final Class<E> extension) {
        return new JavaBeanTesterBuilder<>(clazz, extension);
    }
}
