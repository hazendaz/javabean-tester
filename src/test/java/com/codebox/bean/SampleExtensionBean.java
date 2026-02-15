/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Instantiates a new sample extension bean.
 */
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class SampleExtensionBean extends SampleBean {

    /** The extension. */
    private String extension;

    /**
     * Instantiates a new sample bean. Causes JVM to not create a default no-arg constructor.
     *
     * @param newString
     *            the new string
     */
    public SampleExtensionBean(final String newString) {
        super(newString);
    }
}
