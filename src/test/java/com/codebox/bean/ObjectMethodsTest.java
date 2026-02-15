/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.bean;

import org.junit.jupiter.api.Test;

/**
 * The Class ObjectMethodsTest.
 */
class ObjectMethodsTest {

    /**
     * Object methods.
     */
    @Test
    void objectMethods() {
        JavaBeanTester.builder(SampleBean.class).testObjectMethods();
    }

}
