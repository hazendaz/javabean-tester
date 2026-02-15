/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
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
    public static String returnString() {
        return "private class";
    }
}
