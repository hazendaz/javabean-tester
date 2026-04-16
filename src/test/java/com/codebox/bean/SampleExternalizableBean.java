/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.bean;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * The Class SampleExternalizableBean.
 *
 * <p>
 * A minimal {@link Externalizable} bean that uses identity equals ({@code Object.equals}). After a
 * serialize/deserialize round-trip the two instances are NOT equal, which exercises the {@code skipStrictSerializable}
 * else-branch in {@code JavaBeanTesterWorker.checkSerializableTest()} as well as the {@code Externalizable} branch in
 * {@code implementsSerializable()}.
 */
public class SampleExternalizableBean implements Externalizable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The value. */
    private String value;

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *            the value
     */
    public void setValue(final String value) {
        this.value = value;
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeObject(this.value);
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        this.value = (String) in.readObject();
    }

}
