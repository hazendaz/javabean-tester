/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.bean;

import lombok.Builder;
import lombok.Data;

/**
 * The Class SampleLombokBuilder.
 */
@Data
@Builder
public class SampleLombokBuilder {

    /** The wheels. */
    String wheels;

    /** The color. */
    String color;

    /** The model. */
    String model;

    /** The make. */
    String make;

}
