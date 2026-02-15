/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.bean;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * The Class ByteBuddyBeanCopier.
 */
public final class ByteBuddyBeanCopier {

    /**
     * Instantiates a new byte buddy bean copier.
     */
    private ByteBuddyBeanCopier() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Copy.
     *
     * @param source
     *            the source
     * @param target
     *            the target
     * @param converter
     *            the converter
     */
    public static void copy(Object source, Object target, BooleanConverter converter) {
        if (source == null || target == null) {
            return;
        }

        Class<?> clazz = source.getClass();
        Map<String, Method> getters = new HashMap<>();
        Map<String, Method> setters = new HashMap<>();

        for (Method method : clazz.getMethods()) {
            if (ByteBuddyBeanCopier.isGetter(method)) {
                String property = ByteBuddyBeanCopier.decapitalize(ByteBuddyBeanCopier.extractPropertyName(method));
                getters.put(property, method);
            } else if (ByteBuddyBeanCopier.isSetter(method)) {
                String property = ByteBuddyBeanCopier.decapitalize(method.getName().substring(3));
                setters.put(property, method);
            }
        }

        for (Map.Entry<String, Method> entry : setters.entrySet()) {
            String property = entry.getKey();
            Method setter = entry.getValue();
            Method getter = getters.get(property);

            try {
                Object value = null;
                if (getter != null) {
                    value = getter.invoke(source);
                } else if (isBooleanType(setter.getParameterTypes()[0])) {
                    // Try 'is' method for Boolean if getter is missing
                    String isMethodName = "is" + ByteBuddyBeanCopier.capitalize(property);
                    try {
                        Method isMethod = clazz.getMethod(isMethodName);
                        if (isBooleanType(isMethod.getReturnType())) {
                            value = isMethod.invoke(source);
                        }
                    } catch (NoSuchMethodException ignored) {
                        // No 'is' method, skip
                    }
                }
                if (converter != null && isBooleanType(setter.getParameterTypes()[0])) {
                    value = converter.convert(value, setter.getParameterTypes()[0]);
                }
                if (value != null || !setter.getParameterTypes()[0].isPrimitive()) {
                    setter.invoke(target, value);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to copy property: " + property, e);
            }
        }
    }

    /**
     * Checks if is getter.
     *
     * @param method
     *            the method
     * @return true, if is getter
     */
    private static boolean isGetter(Method method) {
        if (method.getParameterCount() != 0) {
            return false;
        }
        String name = method.getName();
        return name.startsWith("get") && !method.getReturnType().equals(void.class) && name.length() > 3
                || name.startsWith("is") && isBooleanType(method.getReturnType()) && name.length() > 2;
    }

    /**
     * Checks if is setter.
     *
     * @param method
     *            the method
     * @return true, if is setter
     */
    private static boolean isSetter(Method method) {
        return method.getName().startsWith("set") && method.getParameterCount() == 1
                && method.getReturnType().equals(void.class);
    }

    /**
     * Checks if is boolean type.
     *
     * @param type
     *            the type
     * @return true, if is boolean type
     */
    private static boolean isBooleanType(Class<?> type) {
        return type == boolean.class || type == Boolean.class;
    }

    /**
     * Extract property name.
     *
     * @param getter
     *            the getter
     * @return the string
     */
    private static String extractPropertyName(Method getter) {
        String name = getter.getName();
        if (name.startsWith("get")) {
            return name.substring(3);
        }
        if (name.startsWith("is")) {
            return name.substring(2);
        }
        throw new IllegalArgumentException("Not a getter method: " + name);
    }

    /**
     * Decapitalize.
     *
     * @param name
     *            the name
     * @return the string
     */
    private static String decapitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * Capitalize.
     *
     * @param name
     *            the name
     * @return the string
     */
    private static String capitalize(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * The Interface BooleanConverter.
     */
    public interface BooleanConverter {

        /**
         * Convert.
         *
         * @param value
         *            the value
         * @param targetType
         *            the target type
         * @return the object
         */
        Object convert(Object value, Class<?> targetType);
    }

}
