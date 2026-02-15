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
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * The Class NonDeserializableBean.
 */
@Data
public class NonDeserializableBean implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The array which can sometimes not be serializable. */
    private final List<Object> list;

    /**
     * Make bean non deserializable.
     */
    public NonDeserializableBean() {
        this.list = new ArrayList<>();
    }

}
