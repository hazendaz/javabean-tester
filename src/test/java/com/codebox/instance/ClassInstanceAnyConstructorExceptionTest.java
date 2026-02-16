/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.instance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class ClassInstanceAnyConstructorExceptionTest.
 */
class ClassInstanceAnyConstructorExceptionTest {

    /**
     * The Class ThrowsOnConstruct triggers InvocationTargetException.
     */
    static class ThrowsOnConstruct {

        /**
         * Instantiates a new throws on construct.
         *
         * @param s
         *            the s
         */
        public ThrowsOnConstruct(String s) {
            throw new RuntimeException("fail");
        }
    }

    /**
     * The Class AbstractWithArgs triggers InstantiationException.
     */
    abstract static class AbstractWithArgs {

        /**
         * Instantiates a new abstract with args.
         *
         * @param s
         *            the s
         */
        public AbstractWithArgs(String s) {
        }
    }

    /**
     * The Class PrivateConstructor triggers IllegalAccessException.
     */
    static class PrivateConstructor {

        /**
         * Instantiates a new private constructor.
         *
         * @param s
         *            the s
         */
        private PrivateConstructor(String s) {
        }
    }

    /**
     * Test any constructor invocation target exception.
     */
    @Test
    void testAnyConstructorInvocationTargetException() {
        ClassInstance<ThrowsOnConstruct> classInstance = new ClassInstance<>();
        Assertions.assertThrows(org.opentest4j.AssertionFailedError.class,
                () -> classInstance.newInstance(ThrowsOnConstruct.class));
    }

    /**
     * Test any constructor instantiation exception.
     */
    @Test
    void testAnyConstructorInstantiationException() {
        ClassInstance<AbstractWithArgs> classInstance = new ClassInstance<>();
        Assertions.assertThrows(org.opentest4j.AssertionFailedError.class,
                () -> classInstance.newInstance(AbstractWithArgs.class));
    }

}
