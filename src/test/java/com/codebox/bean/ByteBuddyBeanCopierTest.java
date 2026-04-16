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

    /**
     * Test copy with acronym (double-uppercase) getter covers the decapitalize "all-caps" branch (L157-158).
     */
    @Test
    void testCopyWithAcronymGetter() {
        var source = new AcronymBean();
        source.setURL("https://example.com");

        var target = new AcronymBean();
        ByteBuddyBeanCopier.copy(source, target, null);

        Assertions.assertEquals("https://example.com", target.getURL());
    }

    /**
     * Test that a setter named "set" (empty property name) does not throw and exercises the decapitalize/capitalize
     * empty-string branches (L154-155, L171-172).
     */
    @Test
    void testCopyWithEmptyPropertyName() {
        var source = new EmptyPropertyNameBean();
        var target = new EmptyPropertyNameBean();
        Assertions.assertDoesNotThrow(() -> ByteBuddyBeanCopier.copy(source, target, null));
    }

    /**
     * Test that a void-returning "getter" (not recognised by isGetter) causes the copier to treat the property as
     * getter-less and exercises the non-boolean setter without getter branch (L65 false, L101 void-getter branch).
     */
    @Test
    void testCopyWithVoidReturningGetterMethod() {
        var source = new VoidGetterBean();
        var target = new VoidGetterBean();
        // Should copy silently: void "getter" not recognised, String setter copies null
        Assertions.assertDoesNotThrow(() -> ByteBuddyBeanCopier.copy(source, target, null));
    }

    /**
     * Test copy with single-char property name exercises the decapitalize length-1 branch (L157 false branch when
     * name.length() == 1) and the getXxx/setXxx method named "get" exactly exercises the length > 3 false branch of
     * isGetter (L101).
     */
    @Test
    void testCopyWithSingleCharAndExactGetProperty() {
        var source = new SingleCharPropertyBean();
        source.setX("hello");

        var target = new SingleCharPropertyBean();
        ByteBuddyBeanCopier.copy(source, target, null);

        Assertions.assertEquals("hello", target.getX());
    }

    /**
     * Test copy with a fluent (non-void returning) setter exercises the isSetter false branch for non-void return type
     * (L113 and L114 non-void branch).
     */
    @Test
    void testCopyWithFluentSetter() {
        var source = new FluentSetterBean();
        source.setName("test");

        var target = new FluentSetterBean();
        ByteBuddyBeanCopier.copy(source, target, null);

        // Fluent setter is NOT recognised by isSetter() → name is not copied
        Assertions.assertNull(target.getName());
    }

    static class AcronymBean {
        /** The url. */
        private String url;

        /**
         * Gets the URL.
         *
         * @return the URL
         */
        public String getURL() {
            return this.url;
        }

        /**
         * Sets the URL.
         *
         * @param url
         *            the URL
         */
        public void setURL(final String url) {
            this.url = url;
        }
    }

    /** Bean whose only setter is named "set" producing an empty property name. */
    static class EmptyPropertyNameBean {
        /** The val. */
        @SuppressWarnings({ "unused", "java:S100" })
        private boolean val;

        /**
         * Sets the (empty-property-name setter).
         *
         * @param val
         *            the val
         */
        @SuppressWarnings("java:S100")
        public void set(final boolean val) { // NOSONAR intentional empty-name property for edge-case test
            this.val = val;
        }
    }

    /** Bean with a void "getter" – not recognised by isGetter, exercises L101 branch and L65 false branch. */
    static class VoidGetterBean {
        /** The name. */
        private String name;

        /**
         * Gets the name (void – not a valid getter).
         */
        @SuppressWarnings("java:S4144")
        public void getName() { // NOSONAR intentional void "getter" for branch-coverage test
            // void return — not recognised as a getter by ByteBuddyBeanCopier.isGetter()
        }

        /**
         * Sets the name.
         *
         * @param name
         *            the name
         */
        public void setName(final String name) {
            this.name = name;
        }
    }

    /**
     * Bean with a single-char property 'x' and an extra 'get()' method (length 3, not > 3) to exercise the isGetter
     * length-check false branch (L101 A+B+C=F path).
     */
    static class SingleCharPropertyBean {
        /** The x. */
        private String x;

        /**
         * Gets the x.
         *
         * @return the x
         */
        public String getX() {
            return this.x;
        }

        /**
         * Sets the x.
         *
         * @param x
         *            the x
         */
        public void setX(final String x) {
            this.x = x;
        }

        /**
         * A method named "get" exactly (length 3, not > 3) – exercises the isGetter length > 3 false branch (L101).
         *
         * @return null
         */
        @SuppressWarnings("java:S100")
        public String get() { // NOSONAR intentional method named "get" for branch-coverage of isGetter
            return null;
        }
    }

    /** Bean with a fluent (non-void) setter – exercises the isSetter non-void-return false branch (L113-114). */
    static class FluentSetterBean {
        /** The name. */
        private String name;

        /**
         * Gets the name.
         *
         * @return the name
         */
        public String getName() {
            return this.name;
        }

        /**
         * Sets the name (fluent – returns {@code this}; NOT recognised as a setter by isSetter).
         *
         * @param name
         *            the name
         * @return this
         */
        public FluentSetterBean setName(final String name) {
            this.name = name;
            return this;
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
