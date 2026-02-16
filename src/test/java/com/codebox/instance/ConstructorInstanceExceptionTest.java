/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.instance;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class ConstructorInstanceExceptionTest.
 */
class ConstructorInstanceExceptionTest {

    /**
     * The Class AbstractClass triggers InstantiationException (abstract class).
     */
    abstract static class AbstractClass {

        /**
         * Instantiates a new abstract class.
         */
        public AbstractClass() {
        }
    }

    /**
     * The Class PrivateConstructorClass triggers IllegalAccessException (private constructor, but may not throw in
     * modern JVMs).
     */
    static class PrivateConstructorClass {

        /**
         * Instantiates a new private constructor class.
         */
        private PrivateConstructorClass() {
        }
    }

    /**
     * The Class ThrowsOnConstruct triggers InvocationTargetException (constructor throws).
     */
    static class ThrowsOnConstruct {

        /**
         * Instantiates a new throws on construct.
         */
        public ThrowsOnConstruct() {
            throw new RuntimeException("fail");
        }
    }

    /**
     * Test new instance instantiation exception.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void testNewInstanceInstantiationException() throws Exception {
        Constructor<?> ctor = AbstractClass.class.getDeclaredConstructor();
        Assertions.assertThrows(org.opentest4j.AssertionFailedError.class, () -> ConstructorInstance.newInstance(ctor));
    }

    /**
     * Test new instance invocation target exception.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void testNewInstanceInvocationTargetException() throws Exception {
        Constructor<?> ctor = ThrowsOnConstruct.class.getDeclaredConstructor();
        Assertions.assertThrows(org.opentest4j.AssertionFailedError.class, () -> ConstructorInstance.newInstance(ctor));
    }

}
