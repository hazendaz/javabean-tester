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

@Data
@Builder
public class SampleLombokBuilder {
    String wheels;
    String color;
    String model;
    String make;
}
