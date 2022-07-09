/*
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright 2012-2022 Hazendaz.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of The Apache Software License,
 * Version 2.0 which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Contributors:
 *     CodeBox (Rob Dawson).
 *     Hazendaz (Jeremy Landis).
 */
package com.aws.machine;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Instantiates a new machine.
 */
@Data
public class Machine {

    /** The machine id. */
    private int machineId;

    /** The capacity. */
    private int capacity;

    /** The tasks. */
    private List<Integer> tasks;

    /** The last used. */
    private long lastUsed;

    /**
     * Instantiates a new machine.
     */
    public Machine() {
        // Initialize tasks to empty
        tasks = new ArrayList<>();
    }

}
