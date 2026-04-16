/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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

    @Test
    void testCopyPropertiesAndConverter() {
        var source = new SourceBean();
        source.setName("name");
        source.setAge(42);
        source.setActive(true);
        source.setEnabled(Boolean.TRUE);

        var target = new SourceBean();

        ByteBuddyBeanCopier.copy(source, target, (value, targetType) -> Boolean.FALSE);

        Assertions.assertEquals("name", target.getName());
        Assertions.assertEquals(42, target.getAge());
        Assertions.assertFalse(target.isActive());
        Assertions.assertFalse(target.getEnabled());
    }

    @Test
    void testCopySkipsPrimitiveBooleanWhenNoGetter() {
        var source = new PrimitiveSetterOnlySource();
        source.setActive(false);

        var target = new PrimitiveSetterOnlySource();
        target.setActive(true);

        ByteBuddyBeanCopier.copy(source, target, null);

        Assertions.assertTrue(target.active());
    }

    @Test
    void testCopySkipsPrimitiveBooleanWhenFallbackIsMethodIsNotBoolean() {
        var source = new NonBooleanIsMethodSource();
        source.setActive(false);

        var target = new NonBooleanIsMethodSource();
        target.setActive(true);

        ByteBuddyBeanCopier.copy(source, target, null);

        Assertions.assertTrue(target.active());
    }

    @Test
    void testCopyWrapsGetterInvocationFailures() {
        var source = new FailingGetterSource();
        var target = new FailingGetterSource();

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> ByteBuddyBeanCopier.copy(source, target, null));
        Assertions.assertTrue(exception.getMessage().startsWith("Failed to copy property"));
    }

    @Test
    void testUtilityConstructorThrowsAssertionError() throws Exception {
        Constructor<ByteBuddyBeanCopier> constructor = ByteBuddyBeanCopier.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception = Assertions.assertThrows(InvocationTargetException.class,
                constructor::newInstance);
        Assertions.assertInstanceOf(AssertionError.class, exception.getCause());
    }

    static class PrimitiveSetterOnlySource {
        private boolean active;

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean active() {
            return this.active;
        }
    }

    static class NonBooleanIsMethodSource {
        private boolean active;

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean active() {
            return this.active;
        }

        public String isActive() {
            return "not-boolean";
        }
    }

    static class FailingGetterSource {
        public void setName(String name) {
        }

        public String getName() {
            throw new IllegalStateException("boom");
        }
    }

}
