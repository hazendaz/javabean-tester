/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.builders;

import com.codebox.bean.SampleBean;

import javassist.CannotCompileException;
import javassist.NotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Class ExtensionBuilderTest.
 */
class ExtensionBuilderTest {

    /** The class. */
    private Class<SampleBean> clazz;

    /** The extension. */
    private Class<SampleBean> extension;

    /**
     * Inits the.
     */
    @BeforeEach
    void init() {
        this.clazz = SampleBean.class;
        this.extension = SampleBean.class;
    }

    /**
     * Extension builder.
     *
     * @throws NotFoundException
     *             the not found exception
     * @throws CannotCompileException
     *             the cannot compile exception
     */
    @SuppressWarnings("unchecked")
    @Test
    void extensionBuilder() throws NotFoundException, CannotCompileException {
        this.extension = (Class<SampleBean>) new ExtensionBuilder<SampleBean>().generate(this.clazz);
        Assertions.assertNotEquals(this.clazz, this.extension);
    }

    /**
     * Test generate covers already created extension (Class.forName branch).
     */
    @Test
    void testGenerateCoversAlreadyCreatedExtension() throws Exception {
        ExtensionBuilder<SampleBean> builder = new ExtensionBuilder<>();
        // First call: creates the extension
        Class<?> extClass1 = builder.generate(SampleBean.class);
        Assertions.assertNotNull(extClass1);
        Assertions.assertTrue(extClass1.getName().endsWith("SampleBeanExtension"));

        // Second call: should hit the Class.forName branch
        Class<?> extClass2 = builder.generate(SampleBean.class);
        Assertions.assertNotNull(extClass2);
        Assertions.assertSame(extClass1, extClass2);
    }

}
