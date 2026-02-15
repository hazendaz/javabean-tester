/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.bean;

import java.io.Serializable;

import lombok.Data;

/**
 * The Class MultiConstructorBean.
 */
@Data
public class MultiConstructorBean implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The string one. */
    private String stringOne;

    /** The string two. */
    private String stringTwo;

    /**
     * Public Constructor.
     */
    public MultiConstructorBean() {
        // Do nothing
    }

    /**
     * Instantiates a new multi constructor bean. Causes JVM to not create a default no-arg constructor.
     *
     * @param newStringOne
     *            the new string one
     * @param newStringTwo
     *            the new string two
     */
    public MultiConstructorBean(final String newStringOne, final String newStringTwo) {
        stringOne = newStringOne;
        stringTwo = newStringTwo;
    }

}
