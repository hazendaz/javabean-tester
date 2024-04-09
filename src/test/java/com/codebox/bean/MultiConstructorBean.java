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
     * Instantiates a new sample bean. Causes JVM to not create a default no-arg constructor.
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
