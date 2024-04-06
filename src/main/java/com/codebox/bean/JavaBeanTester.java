/*
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright 2012-2024 Hazendaz.
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

import java.lang.reflect.Modifier;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.NamedElement;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.EqualsMethod;
import net.bytebuddy.implementation.HashCodeMethod;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.ToStringMethod;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * This helper class can be used to unit test the get/set/equals/canEqual/toString/hashCode methods of JavaBean-style
 * Value Objects.
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
    public static <T> JavaBeanTesterBuilder<T, ?> builder(final Class<T> clazz) {
        // If class is final, use Object.class for comparison needs
        if (Modifier.isFinal(clazz.getModifiers())) {
            return new JavaBeanTesterBuilder<>(clazz, Object.class);
        }

        // Build extension from class using byte buddy
        Class<? extends T> loaded = new ByteBuddy().with(new NamingStrategy.AbstractBase() {
            @Override
            protected String name(TypeDescription superClass) {
                return clazz.getPackageName() + ".ByteBuddyExt" + superClass.getSimpleName();
            }
        }).subclass(clazz).method(ElementMatchers.isEquals()).intercept(EqualsMethod.requiringSuperClassEquality())
                .method(ElementMatchers.isHashCode()).intercept(HashCodeMethod.usingSuperClassOffset())
                .method(ElementMatchers.isToString()).intercept(ToStringMethod.prefixedBySimpleClassName())
                .method(ElementMatchers.named("canEqual")).intercept(MethodDelegation.to(CanEqualInterceptor.class))
                .defineField("javabeanExtension", String.class, Visibility.PACKAGE_PRIVATE).make()
                .load(clazz.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();

        // Builder with proper extension class
        return builder(clazz, loaded);
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

    /**
     * The Class CanEqualInterceptor.
     */
    public static class CanEqualInterceptor {
        public static boolean canEqual(final Object obj) {
            return obj instanceof NamedElement.WithRuntimeName;
        }

    }
}
