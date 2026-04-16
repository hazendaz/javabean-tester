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
 * The Class IdentityEqualsStringBean.
 *
 * <p>
 * A bean whose {@code equals} uses reference equality ({@code ==}) for its {@code String} field. This causes
 * {@code EqualsVerifier} to fail (it creates non-interned strings), which exercises the {@code AssertionError} catch
 * block in {@code JavaBeanTesterWorker.processEqualsVerifierSymmetricTest()} (L440-441).
 *
 * <p>
 * The worker's own checks still pass because {@link com.codebox.bean.ValueBuilder} always returns the same interned
 * {@code "TEST_VALUE"} literal, so two loaded instances share the same {@code String} reference.
 */
public class IdentityEqualsStringBean {

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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdentityEqualsStringBean)) {
            return false;
        }
        // Intentionally uses == (reference equality) instead of Objects.equals to trigger EqualsVerifier failure
        return this.value == ((IdentityEqualsStringBean) o).value; // NOSONAR
    }

    @Override
    public int hashCode() {
        // Return a non-zero prime when null so the ByteBuddy extension (which multiplies by 31) gets a
        // different hashCode than the base class, ensuring assertNotEquals(ext, y) passes in the worker tests.
        return this.value == null ? 31 : this.value.hashCode();
    }

}
