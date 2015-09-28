/**
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright (c) 2012 - 2015 Hazendaz.
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

/**
 * The Class PrivateConstructor.
 */
public final class PrivateConstructor {

    /**
     * Instantiates a new private constructor.
     */
    private PrivateConstructor() {
        // prevent instantiation
    }

    /**
     * Return string.
     *
     * @return the string
     */
    public static final String returnString() {
        return "private class";
    }
}
