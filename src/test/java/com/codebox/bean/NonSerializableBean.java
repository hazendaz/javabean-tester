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

import lombok.Data;

/**
 * The Class NonSerializableBean.
 */
@Data
public class NonSerializableBean implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The empty bean which is not serializable. */
    private final EmptyBean emptyBean;

    /**
     * Make bean non serializable.
     */
    public NonSerializableBean() {
        this.emptyBean = new EmptyBean();
    }

}
