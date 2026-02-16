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
 * The Class ClassInstanceBuilderExceptionTest.
 */
class ClassInstanceBuilderExceptionTest {

    /**
     * The Class NoBuildMethod for NoSuchMethodException: builder returns object with no build() method.
     */
    static class NoBuildMethod {

        /**
         * Builder.
         *
         * @return the object
         */
        public static Object builder() {
            return new Object();
        }
    }

    /**
     * The Class BuilderThrows for InvocationTargetException: builder returns object with build() that throws.
     */
    static class BuilderThrows {

        /**
         * Builder.
         *
         * @return the builder throws builder
         */
        public static BuilderThrowsBuilder builder() {
            return new BuilderThrowsBuilder();
        }

        /**
         * The Class BuilderThrowsBuilder.
         */
        static class BuilderThrowsBuilder {

            /**
             * Builds the.
             *
             * @return the builder throws
             * @throws Exception
             *             the exception
             */
            public BuilderThrows build() throws Exception {
                throw new Exception("fail");
            }
        }
    }

    /**
     * Test create instance with builder no such method exception.
     */
    @Test
    void testCreateInstanceWithBuilderNoSuchMethodException() {
        ClassInstance<NoBuildMethod> classInstance = new ClassInstance<>();
        Assertions.assertThrows(org.opentest4j.AssertionFailedError.class,
                () -> classInstance.newInstance(NoBuildMethod.class));
    }

    /**
     * Test create instance with builder invocation target exception.
     */
    @Test
    void testCreateInstanceWithBuilderInvocationTargetException() {
        ClassInstance<BuilderThrows> classInstance = new ClassInstance<>();
        Assertions.assertThrows(org.opentest4j.AssertionFailedError.class,
                () -> classInstance.newInstance(BuilderThrows.class));
    }

}
