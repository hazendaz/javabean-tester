/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.builders;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExtensionBuilder dynamically generates a subclass of the given class with additional String properties and their
 * getters/setters.
 * <p>
 * Uses Javassist to create a new class at runtime named <code>clazz.getName() + "Extension"</code>. The generated class
 * will have four String properties: jbExtension1, jbExtension2, jbExtension3, jbExtension4.
 *
 * @param <T>
 *            the type to extend
 */
public class ExtensionBuilder<T> {

    /** The Constant logger. */
    private static final Logger logger = LoggerFactory.getLogger(ExtensionBuilder.class);

    /**
     * Generates a dynamic subclass of the given class with additional String properties and their getters/setters.
     *
     * @param clazz
     *            the class to extend
     * @return the generated class
     * @throws NotFoundException
     *             if the class cannot be found in the class pool
     * @throws CannotCompileException
     *             if the class cannot be compiled
     */
    public Class<?> generate(final Class<T> clazz) throws NotFoundException, CannotCompileException {
        try {
            // If extension already recreated, return it
            return Class.forName(clazz.getName() + "Extension");
        } catch (final ClassNotFoundException e) {
            // No extension exists, so create it
            ExtensionBuilder.logger.trace("No extension exists, so create it", e);
        }

        final ClassPool pool = ClassPool.getDefault();
        final CtClass cc = pool.makeClass(clazz.getName() + "Extension");

        // add super class
        cc.setSuperclass(ExtensionBuilder.resolveCtClass(clazz));

        final Map<String, Class<?>> properties = new HashMap<>();
        properties.put("jbExtension1", String.class);
        properties.put("jbExtension2", String.class);
        properties.put("jbExtension3", String.class);
        properties.put("jbExtension4", String.class);

        for (final Entry<String, Class<?>> entry : properties.entrySet()) {

            // Add field
            cc.addField(new CtField(ExtensionBuilder.resolveCtClass(entry.getValue()), entry.getKey(), cc));

            // Add getter
            cc.addMethod(ExtensionBuilder.generateGetter(cc, entry.getKey(), entry.getValue()));

            // Add setter
            cc.addMethod(ExtensionBuilder.generateSetter(cc, entry.getKey(), entry.getValue()));
        }

        return cc.toClass();
    }

    /**
     * Generates a getter method for the specified field.
     *
     * @param declaringClass
     *            the class to add the method to
     * @param fieldName
     *            the name of the field
     * @param fieldClass
     *            the type of the field
     * @return the generated getter method
     * @throws CannotCompileException
     *             if the method cannot be compiled
     */
    private static CtMethod generateGetter(final CtClass declaringClass, final String fieldName,
            final Class<?> fieldClass) throws CannotCompileException {
        String methodSrc = """
                public %s get%s() {
                    return this.%s;
                }
                """.formatted(fieldClass.getName(), fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1),
                fieldName);
        return CtMethod.make(methodSrc, declaringClass);
    }

    /**
     * Generates a setter method for the specified field.
     *
     * @param declaringClass
     *            the class to add the method to
     * @param fieldName
     *            the name of the field
     * @param fieldClass
     *            the type of the field
     * @return the generated setter method
     * @throws CannotCompileException
     *             if the method cannot be compiled
     */
    private static CtMethod generateSetter(final CtClass declaringClass, final String fieldName,
            final Class<?> fieldClass) throws CannotCompileException {
        String methodSrc = """
                public void set%s(%s %s) {
                    this.%s = %s;
                }
                """.formatted(fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), fieldClass.getName(),
                fieldName, fieldName, fieldName);
        return CtMethod.make(methodSrc, declaringClass);
    }

    /**
     * Resolves a CtClass from a Java Class using the default class pool.
     *
     * @param clazz
     *            the Java class to resolve
     * @return the corresponding CtClass
     * @throws NotFoundException
     *             if the class cannot be found in the class pool
     */
    private static CtClass resolveCtClass(final Class<?> clazz) throws NotFoundException {
        return ClassPool.getDefault().get(clazz.getName());
    }

}
