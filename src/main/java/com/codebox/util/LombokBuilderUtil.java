/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * The Class LombokBuilderUtil.
 */
public class LombokBuilderUtil {

    /**
     * Get the Lombok builder() method if present, else null.
     *
     * @param clazz
     *            the clazz
     * @return the lombok builder method
     */
    public static Method getLombokBuilderMethod(final Class<?> clazz) {
        try {
            final Method builderMethod = clazz.getMethod("builder");
            if (Modifier.isStatic(builderMethod.getModifiers()) && builderMethod.getParameterCount() == 0) {
                return builderMethod;
            }
        } catch (final NoSuchMethodException e) {
            // ignore
        }
        return null;
    }

}
