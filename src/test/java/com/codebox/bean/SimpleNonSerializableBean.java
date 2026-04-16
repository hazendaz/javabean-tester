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
 * The Class SimpleNonSerializableBean.
 *
 * <p>
 * A minimal bean with one getter/setter and NO {@link java.io.Serializable} implementation. Used to exercise the
 * {@code checkSerializable == ON} failure path in {@code JavaBeanTesterWorker.checkSerializableTest()} (L338-339).
 */
public class SimpleNonSerializableBean {

    /** The name. */
    private String name;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the name
     */
    public void setName(final String name) {
        this.name = name;
    }

}
