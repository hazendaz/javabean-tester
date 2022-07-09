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

import lombok.Data;

/**
 * The Class Task.
 */
@Data
public class Task {

    /** The task id. */
    private int taskId;

    /** The weight. */
    private int weight;

    /**
     * Instantiates a new task.
     *
     * @param taskId
     *            the task id
     * @param weight
     *            the weight
     */
    public Task(final int taskId, final int weight) {
        this.taskId = taskId;
        this.weight = weight;
    }

}
