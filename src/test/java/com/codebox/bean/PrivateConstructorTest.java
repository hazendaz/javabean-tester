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
 * The Class PrivateConstructorTest.
 */
class PrivateConstructorTest {

    /**
     * Private constructor.
     */
    @Test
    void privateConstructor() {
        JavaBeanTester.builder(PrivateConstructor.class).testPrivateConstructor();
    }

}
