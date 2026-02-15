/*
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright 2012-2026 Hazendaz.
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.powermock.reflect.Whitebox;

/**
 * The Class JavaBeanTesterTest.
 */
class JavaBeanTesterTest {

    /** The sample bean. */
    private SampleBean sampleBean;

    /** The expected bean. */
    private SampleBean expectedBean;

    /**
     * Inits the.
     */
    @BeforeEach
    void init() {
        this.sampleBean = new SampleBean(null);
        this.expectedBean = new SampleBean(null);
    }

    /**
     * Load full bean.
     */
    @Test
    void loadFullBean() {
        JavaBeanTester.builder(SampleBean.class).loadData().testInstance(this.sampleBean);
        Assertions.assertNotNull(this.sampleBean.getDoubleWrapper());
    }

    /**
     * Load full bean equals.
     */
    @Test
    void loadFullBeanEquals() {
        JavaBeanTester.builder(SampleBean.class).loadData().testInstance(this.sampleBean);
        JavaBeanTester.builder(SampleBean.class).loadData().testInstance(this.expectedBean);

        this.sampleBean.setSampleDepthBean(new SampleDepthBean());
        this.expectedBean.setSampleDepthBean(new SampleDepthBean());
        this.sampleBean.setEmptyBean(new EmptyBean());
        this.expectedBean.setEmptyBean(new EmptyBean());
        JavaBeanTester.builder(SampleDepthBean.class).loadData().testInstance(this.sampleBean.getSampleDepthBean());
        JavaBeanTester.builder(SampleDepthBean.class).loadData().testInstance(this.expectedBean.getSampleDepthBean());

        JavaBeanTester.builder(SampleBean.class).loadData().testEquals(this.sampleBean, this.expectedBean);
    }

    /**
     * Load full bean equals short.
     */
    @Test
    void loadFullBeanEqualsShort() {
        JavaBeanTester.builder(SampleBean.class).loadData().testInstance(this.sampleBean);
        JavaBeanTester.builder(SampleBean.class).loadData().testInstance(this.expectedBean);
        JavaBeanTester.builder(SampleBean.class).loadData().testEquals(this.sampleBean, this.expectedBean);
    }

    /**
     * Load full bean equals skip underlying.
     */
    @Test
    void loadFullBeanEqualsSkipUnderlying() {
        JavaBeanTester.builder(SampleBean.class).testInstance(this.sampleBean);
        JavaBeanTester.builder(SampleBean.class).testInstance(this.expectedBean);
        JavaBeanTester.builder(SampleBean.class).testEquals(this.sampleBean, this.expectedBean);
    }

    /**
     * Load_full bean skip underlying data.
     */
    @Test
    void loadFullBeanSkipUnderlyingData() {
        JavaBeanTester.builder(SampleBean.class).testInstance(this.sampleBean);
        Assertions.assertNotNull(this.sampleBean.getDoubleWrapper());
    }

    /**
     * Load partial bean equals.
     */
    @Test
    void loadPartialBeanEquals() {
        JavaBeanTester.builder(SampleBean.class).loadData().testInstance(this.sampleBean);
        JavaBeanTester.builder(SampleBean.class).loadData().testInstance(this.expectedBean);

        this.sampleBean.setSampleDepthBean(new SampleDepthBean());
        this.expectedBean.setSampleDepthBean(new SampleDepthBean());
        JavaBeanTester.builder(SampleDepthBean.class).loadData().testInstance(this.sampleBean.getSampleDepthBean());
        JavaBeanTester.builder(SampleDepthBean.class).loadData().testInstance(this.expectedBean.getSampleDepthBean());

        JavaBeanTester.builder(SampleBean.class).loadData().testEquals(this.sampleBean, this.expectedBean);
    }

    /**
     * Load skip bean properties.
     */
    @Test
    void loadSkipBeanProperties() {
        JavaBeanTester.builder(SampleBean.class).loadData().skip("string").testInstance(this.sampleBean);
        Assertions.assertNotNull(this.sampleBean.getDoubleWrapper());
        Assertions.assertNull(this.sampleBean.getString());
    }

    /**
     * Test full bean.
     */
    @Test
    void testFullBean() {
        JavaBeanTester.builder(SampleBean.class).checkEquals().loadData().test();
    }

    /**
     * Test full bean.
     */
    @Test
    void testFullBeanWithExtension() {
        JavaBeanTester.builder(SampleBean.class, SampleExtensionBean.class).checkEquals().loadData().test();
    }

    /**
     * Test full bean null ext.
     */
    @Test
    void testFullBeanNullExt() {
        JavaBeanTester.builder(SampleBean.class).checkEquals().loadData().test();
        JavaBeanTester.builder(SampleValueObject.class).checkEquals().loadData().test();
    }

    /**
     * Test full bean skip underlying data.
     */
    @Test
    void testFullBeanSkipUnderlyingData() {
        JavaBeanTester.builder(SampleBean.class).checkEquals().test();
    }

    /**
     * Test full bean skip underlying data.
     */
    @Test
    void testFullBeanSkipUnderlyingDataWithExtension() {
        JavaBeanTester.builder(SampleBean.class, SampleExtensionBean.class).checkEquals().test();
    }

    /**
     * Test skip bean properties.
     */
    @Test
    void testSkipBeanProperties() {
        JavaBeanTester.builder(SampleBean.class).checkEquals().loadData().skip("string").test();
    }

    /**
     * Test skip bean properties.
     */
    @Test
    void testSkipBeanPropertiesWithExtension() {
        JavaBeanTester.builder(SampleBean.class, SampleExtensionBean.class).checkEquals().loadData().skip("string")
                .test();
    }

    /**
     * Test skip bean properties null just ignores the skipping.
     */
    @Test
    void testSkipBeanPropertiesNull() {
        JavaBeanTester.builder(SampleBean.class).checkEquals().loadData().skip((String[]) null).test();
    }

    /**
     * Test skip bean properties null just ignores the skipping.
     */
    @Test
    void testSkipBeanPropertiesNullWithExtension() {
        JavaBeanTester.builder(SampleBean.class, SampleExtensionBean.class).checkEquals().loadData()
                .skip((String[]) null).test();
    }

    /**
     * Test skip can equals.
     */
    @Test
    void testSkipCanEquals() {
        JavaBeanTester.builder(SampleBean.class).loadData().test();
    }

    /**
     * Test skip can equals.
     */
    @Test
    void testSkipCanEqualsWithExtension() {
        JavaBeanTester.builder(SampleBean.class, SampleExtensionBean.class).loadData().test();
    }

    /**
     * TestSkip all as false.
     */
    @Test
    void testSkipCanEqualsFalse() {
        JavaBeanTester.builder(SampleBean.class).checkEquals(false).checkSerializable(false).loadData(false).test();
    }

    /**
     * Test skip all as false.
     */
    @Test
    void testSkipCanEqualsFalseWithExtension() {
        JavaBeanTester.builder(SampleBean.class, SampleExtensionBean.class).checkEquals(false).checkSerializable(false)
                .loadData(false).test();
    }

    /**
     * Test serializable.
     */
    @Test
    void testSerializable() {
        JavaBeanTester.builder(SerializableBean.class).checkSerializable().test();
    }

    /**
     * Test non serializable.
     */
    @Test
    void testNonSerializableInternallyFails() {
        JavaBeanTester.builder(NonSerializableBean.class).checkSerializable().skipStrictSerializable().test();
    }

    /**
     * Test non deserializable.
     *
     * @throws Exception
     *             generic exception.
     */
    @Test
    void testNonSerializable() throws Exception {
        final NonDeserializableBean bean = new NonDeserializableBean();
        bean.getList().add(new Object());

        Assertions.assertThrows(NotSerializableException.class, () -> {
            JavaBeanTesterTest.serialize(bean);
        });
    }

    /**
     * Test clear.
     */
    @Test
    void testClear() {
        JavaBeanTester.builder(SerializableBean.class).checkClear().test();
    }

    /**
     * Test clear false.
     */
    @Test
    void testClearFalse() {
        JavaBeanTester.builder(SerializableBean.class).checkClear(false).test();
    }

    /**
     * Test constructor.
     */
    @Test
    void testConstructor() {
        JavaBeanTester.builder(SerializableBean.class).checkConstructor().test();
    }

    /**
     * Test constructor false.
     */
    @Test
    void testConstructorFalse() {
        JavaBeanTester.builder(SerializableBean.class).checkConstructor(false).test();
    }

    /**
     * Test multi constructor.
     */
    @Test
    void testMultiConstructor() {
        JavaBeanTester.builder(MultiConstructorBean.class).test();
    }

    /**
     * Test multi constructor null extension.
     */
    @Test
    void testMultiConstructorNullExtension() {
        JavaBeanTester.builder(MultiConstructorBean.class, null).checkEquals().test();
    }

    /**
     * Test temporary single mode.
     */
    // TODO 1/12/2019 JWL Temporary until we start using internalized extension logic
    @Test
    void testTemporarySingleMode() {
        final JavaBeanTesterBuilder<String, Object> builder = new JavaBeanTesterBuilder<>(String.class);
        final JavaBeanTesterWorker<String, Object> worker = Whitebox.getInternalState(builder, "worker");
        Assertions.assertEquals(String.class, worker.getClazz());
    }

    /**
     * Test sample lombok builder.
     */
    @Test
    void testSampleLombokBuilder() {
        JavaBeanTester.builder(SampleLombokBuilder.class, null).checkEquals().checkConstructor().checkClear().loadData()
                .test();
    }

    /**
     * Serialize.
     *
     * @param <T>
     *            the generic type
     * @param object
     *            the object
     *
     * @return the t
     *
     * @throws Exception
     *             the exception
     */
    @SuppressWarnings("unchecked")
    private static <T> T serialize(final T object) throws Exception {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(object);

        final ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return (T) new ObjectInputStream(bais).readObject();
    }

}
