/*
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright 2012-2024 Hazendaz.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of The Apache Software License,
 * Version 2.0 which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Contributors:
 *     CodeBox (Rob Dawson).
 *     Hazendaz (Jeremy Landis).
 */
package com.codebox.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Instantiates a new sample extension bean.
 */
@Data
@EqualsAndHashCode(callSuper = false)
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
