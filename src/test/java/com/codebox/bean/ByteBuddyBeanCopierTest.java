/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
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
