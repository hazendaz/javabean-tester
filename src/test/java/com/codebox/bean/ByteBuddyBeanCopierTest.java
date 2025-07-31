/*
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright 2012-2025 Hazendaz.
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
package com.codebox.bean;

import lombok.Data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The Class ByteBuddyBeanCopierTest.
 */
class ByteBuddyBeanCopierTest {

    /**
     * The Class SourceBean.
     */
    @Data
    static class SourceBean {
        /** The name. */
        private String name;

        /** The age. */
        private int age;

        /** The active. */
        private boolean active;

        /** The enabled. */
        private Boolean enabled;
    }

    /**
     * The Class TargetBean.
     */
    @Data
    static class TargetBean {
        /** The name. */
        private String name;

        /** The age. */
        private int age;

        /** The active. */
        private boolean active;

        /** The enabled. */
        private Boolean enabled;
    }

    /**
     * Test copy null source or target.
     */
    @Test
    void testCopyNullSourceOrTarget() {
        var target = new TargetBean();
        Assertions.assertDoesNotThrow(() -> ByteBuddyBeanCopier.copy(null, target, null));
        Assertions.assertDoesNotThrow(() -> ByteBuddyBeanCopier.copy(new SourceBean(), null, null));
    }

}
