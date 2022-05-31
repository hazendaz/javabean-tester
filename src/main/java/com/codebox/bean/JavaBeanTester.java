/*
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright 2012-2022 Hazendaz.
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

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.EqualsMethod;
import net.bytebuddy.implementation.HashCodeMethod;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.ToStringMethod;
import net.bytebuddy.matcher.ElementMatchers;

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
     *
     * @return A builder implementing the fluent API to configure JavaBeanTester
     */
    public static <T> JavaBeanTesterBuilder<T, ? extends T> builder(final Class<T> clazz) {
        try {
            Class<? extends T> loaded = new ByteBuddy().with(new NamingStrategy.AbstractBase() {
                @Override
                protected String name(TypeDescription superClass) {
                    return "com.codebox.bean.Extended" + superClass.getSimpleName();
                }
            }).subclass(clazz).method(ElementMatchers.any()).intercept(SuperMethodCall.INSTANCE)
                    .method(ElementMatchers.named("equals")).intercept(EqualsMethod.requiringSuperClassEquality())
                    .method(ElementMatchers.named("hashCode")).intercept(HashCodeMethod.usingSuperClassOffset())
                    .method(ElementMatchers.named("toString")).intercept(ToStringMethod.prefixedBySimpleClassName())
                    .defineField("extension", String.class, Visibility.PACKAGE_PRIVATE).make()
                    .load(clazz.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();
            // EqualsMethod
            return builder(clazz, loaded);
        } catch (Exception e) {
            return builder(clazz, clazz);
        }
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
     *
     * @return A builder implementing the fluent API to configure JavaBeanTester
     */
    public static <T, E> JavaBeanTesterBuilder<T, E> builder(final Class<T> clazz, final Class<E> extension) {
        return new JavaBeanTesterBuilder<>(clazz, extension);
    }
}
