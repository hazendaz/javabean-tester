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
}
