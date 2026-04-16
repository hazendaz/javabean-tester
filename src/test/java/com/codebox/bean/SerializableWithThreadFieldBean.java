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

/**
 * The Class SerializableWithThreadFieldBean.
 *
 * <p>
 * A {@link Serializable} bean that holds a non-serializable {@link Thread} field initialised in the constructor.
 * Serializing an instance always throws {@link java.io.NotSerializableException} (a subtype of
 * {@link java.io.IOException}), which exercises the IOException catch block in
 * {@code JavaBeanTesterWorker.canSerialize()}.
 */
public class SerializableWithThreadFieldBean implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The thread - NOT serializable; always non-null. */
    @SuppressWarnings("java:S1948")
    private Thread thread = Thread.currentThread();

    /**
     * Gets the thread.
     *
     * @return the thread
     */
    public Thread getThread() {
        return this.thread;
    }

    /**
     * Sets the thread.
     *
     * @param thread
     *            the thread
     */
    public void setThread(final Thread thread) {
        this.thread = thread;
    }

}
