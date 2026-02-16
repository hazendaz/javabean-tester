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
 * The Class ClassInstanceRealTest.
 */
class ClassInstanceRealTest {

    /**
     * The Class AbstractClass.
     */
    abstract static class AbstractClass {

        /**
         * Instantiates a new abstract class.
         */
        public AbstractClass() {
        }
    }

    /**
     * New instance throws assertion failed error on instantiation exception.
     */
    @Test
    void newInstanceThrowsAssertionFailedErrorOnInstantiationException() {
        ClassInstance<AbstractClass> classInstance = new ClassInstance<>();
        Assertions.assertThrows(org.opentest4j.AssertionFailedError.class,
                () -> classInstance.newInstance(AbstractClass.class));
    }

}
