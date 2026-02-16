/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.codebox.bean.JavaBeanTester;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

/**
 * The Class LombokBuilderUtilTest.
 */
class LombokBuilderUtilTest {

    /**
     * The Class NoBuilder.
     */
    static class NoBuilder {
    }

    /**
     * The Class WithBuilder.
     */
    static class WithBuilder {

        /**
         * Builder.
         *
         * @return the with builder
         */
        public static WithBuilder builder() {
            return new WithBuilder();
        }
    }

    /**
     * The Class WithNonStaticBuilder.
     */
    static class WithNonStaticBuilder {
        public WithNonStaticBuilder builder() {
            return new WithNonStaticBuilder();
        }
    }

    /**
     * The Class WithStaticBuilderWithParams.
     */
    static class WithStaticBuilderWithParams {
        public static WithStaticBuilderWithParams builder(String arg) {
            return new WithStaticBuilderWithParams();
        }
    }

    /**
     * Test get lombok builder method returns null when no builder.
     */
    @Test
    void testGetLombokBuilderMethodReturnsNullWhenNoBuilder() {
        assertNull(LombokBuilderUtil.getLombokBuilderMethod(NoBuilder.class));
    }

    /**
     * Test get lombok builder method returns method when present.
     */
    @Test
    void testGetLombokBuilderMethodReturnsMethodWhenPresent() {
        Method method = LombokBuilderUtil.getLombokBuilderMethod(WithBuilder.class);
        assertNotNull(method);
        assertEquals("builder", method.getName());
        assertTrue(Modifier.isStatic(method.getModifiers()));
        assertEquals(0, method.getParameterCount());
    }

    /**
     * Test get lombok builder method returns null when builder is not static.
     */
    @Test
    void testGetLombokBuilderMethodReturnsNullWhenBuilderNotStatic() throws Exception {
        Method method = WithNonStaticBuilder.class.getMethod("builder");
        assertFalse(Modifier.isStatic(method.getModifiers()));
        assertEquals(0, method.getParameterCount());
        assertNull(LombokBuilderUtil.getLombokBuilderMethod(WithNonStaticBuilder.class));
    }

    /**
     * Test get lombok builder method returns null when builder has parameters.
     */
    @Test
    void testGetLombokBuilderMethodReturnsNullWhenBuilderHasParams() {
        assertNull(LombokBuilderUtil.getLombokBuilderMethod(WithStaticBuilderWithParams.class));
    }

    /**
     * Test private constructor.
     *
     * @throws Exception
     *             the exception
     */
    @Test
    void testPrivateConstructor() throws Exception {
        JavaBeanTester.builder(LombokBuilderUtil.class).testPrivateConstructor();
    }

}
