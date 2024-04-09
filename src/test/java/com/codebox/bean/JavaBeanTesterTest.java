/*
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright 2012-2023 Hazendaz.
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
     * Load_full bean.
     */
    @Test
    void load_fullBean() {
        JavaBeanTester.builder(SampleBean.class).loadData().testInstance(this.sampleBean);
        Assertions.assertNotNull(this.sampleBean.getDoubleWrapper());
    }

    /**
     * Load_full bean equals.
     */
    @Test
    void load_fullBeanEquals() {
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
     * Load_full bean equals short.
     */
    @Test
    void load_fullBeanEqualsShort() {
        JavaBeanTester.builder(SampleBean.class).loadData().testInstance(this.sampleBean);
        JavaBeanTester.builder(SampleBean.class).loadData().testInstance(this.expectedBean);
        JavaBeanTester.builder(SampleBean.class).loadData().testEquals(this.sampleBean, this.expectedBean);
    }

    /**
     * Load_full bean equals skip underlying.
     */
    @Test
    void load_fullBeanEqualsSkipUnderlying() {
        JavaBeanTester.builder(SampleBean.class).testInstance(this.sampleBean);
        JavaBeanTester.builder(SampleBean.class).testInstance(this.expectedBean);
        JavaBeanTester.builder(SampleBean.class).testEquals(this.sampleBean, this.expectedBean);
    }

    /**
     * Load_full bean skip underlying data.
     */
    @Test
    void load_fullBeanSkipUnderlyingData() {
        JavaBeanTester.builder(SampleBean.class).testInstance(this.sampleBean);
        Assertions.assertNotNull(this.sampleBean.getDoubleWrapper());
    }

    /**
     * Load_partial bean equals.
     */
    @Test
    void load_partialBeanEquals() {
        JavaBeanTester.builder(SampleBean.class).loadData().testInstance(this.sampleBean);
        JavaBeanTester.builder(SampleBean.class).loadData().testInstance(this.expectedBean);

        this.sampleBean.setSampleDepthBean(new SampleDepthBean());
        this.expectedBean.setSampleDepthBean(new SampleDepthBean());
        JavaBeanTester.builder(SampleDepthBean.class).loadData().testInstance(this.sampleBean.getSampleDepthBean());
        JavaBeanTester.builder(SampleDepthBean.class).loadData().testInstance(this.expectedBean.getSampleDepthBean());

        JavaBeanTester.builder(SampleBean.class).loadData().testEquals(this.sampleBean, this.expectedBean);
    }

    /**
     * Load_skip bean properties.
     */
    @Test
    void load_skipBeanProperties() {
        JavaBeanTester.builder(SampleBean.class).loadData().skip("string").testInstance(this.sampleBean);
        Assertions.assertNotNull(this.sampleBean.getDoubleWrapper());
        Assertions.assertNull(this.sampleBean.getString());
    }

    /**
     * Test_full bean.
     */
    @Test
    void test_fullBean() {
        JavaBeanTester.builder(SampleBean.class).checkEquals().loadData().test();
    }

    /**
     * Test_full bean.
     */
    @Test
    void test_fullBeanWithExtension() {
        JavaBeanTester.builder(SampleBean.class, SampleExtensionBean.class).checkEquals().loadData().test();
    }

    /**
     * Test_full bean null ext.
     */
    @Test
    void test_fullBeanNullExt() {
        JavaBeanTester.builder(SampleBean.class).checkEquals().loadData().test();
        JavaBeanTester.builder(SampleValueObject.class).checkEquals().loadData().test();
    }

    /**
     * Test_full bean skip underlying data.
     */
    @Test
    void test_fullBeanSkipUnderlyingData() {
        JavaBeanTester.builder(SampleBean.class).checkEquals().test();
    }

    /**
     * Test_full bean skip underlying data.
     */
    @Test
    void test_fullBeanSkipUnderlyingDataWithExtension() {
        JavaBeanTester.builder(SampleBean.class, SampleExtensionBean.class).checkEquals().test();
    }

    /**
     * Test_skip bean properties.
     */
    @Test
    void test_skipBeanProperties() {
        JavaBeanTester.builder(SampleBean.class).checkEquals().loadData().skip("string").test();
    }

    /**
     * Test_skip bean properties.
     */
    @Test
    void test_skipBeanPropertiesWithExtension() {
        JavaBeanTester.builder(SampleBean.class, SampleExtensionBean.class).checkEquals().loadData().skip("string")
                .test();
    }

    /**
     * Test_skip bean properties null just ignores the skipping.
     */
    @Test
    void test_skipBeanPropertiesNull() {
        JavaBeanTester.builder(SampleBean.class).checkEquals().loadData().skip((String[]) null).test();
    }

    /**
     * Test_skip bean properties null just ignores the skipping.
     */
    @Test
    void test_skipBeanPropertiesNullWithExtension() {
        JavaBeanTester.builder(SampleBean.class, SampleExtensionBean.class).checkEquals().loadData()
                .skip((String[]) null).test();
    }

    /**
     * Test_skip can equals.
     */
    @Test
    void test_skipCanEquals() {
        JavaBeanTester.builder(SampleBean.class).loadData().test();
    }

    /**
     * Test_skip can equals.
     */
    @Test
    void test_skipCanEqualsWithExtension() {
        JavaBeanTester.builder(SampleBean.class, SampleExtensionBean.class).loadData().test();
    }

    /**
     * Test_skip all as false.
     */
    @Test
    void test_skipCanEqualsFalse() {
        JavaBeanTester.builder(SampleBean.class).checkEquals(false).checkSerializable(false).loadData(false).test();
    }

    /**
     * Test_skip all as false.
     */
    @Test
    void test_skipCanEqualsFalseWithExtension() {
        JavaBeanTester.builder(SampleBean.class, SampleExtensionBean.class).checkEquals(false).checkSerializable(false)
                .loadData(false).test();
    }

    /**
     * Test_serializable.
     */
    @Test
    void test_serializable() {
        JavaBeanTester.builder(SerializableBean.class).checkSerializable().test();
    }

    /**
     * Test_non serializable.
     */
    @Test
    void test_nonSerializableInternallyFails() {
        JavaBeanTester.builder(NonSerializableBean.class).checkSerializable().skipStrictSerializable().test();
    }

    /**
     * Test_non deserializable.
     *
     * @throws Exception
     *             generic exception.
     */
    @Test
    void test_nonSerializable() throws Exception {
        final NonDeserializableBean bean = new NonDeserializableBean();
        bean.getList().add(new Object());

        Assertions.assertThrows(NotSerializableException.class, () -> {
            JavaBeanTesterTest.serialize(bean);
        });
    }

    /**
     * Test_clear.
     */
    @Test
    void test_clear() {
        JavaBeanTester.builder(SerializableBean.class).checkClear().test();
    }

    /**
     * Test_clear false.
     */
    @Test
    void test_clearFalse() {
        JavaBeanTester.builder(SerializableBean.class).checkClear(false).test();
    }

    /**
     * Test_constructor.
     */
    @Test
    void test_constructor() {
        JavaBeanTester.builder(SerializableBean.class).checkConstructor().test();
    }

    /**
     * Test_constructor false.
     */
    @Test
    void test_constructorFalse() {
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
     * Test temporary single mode.
     */
    // TODO 1/12/2019 JWL Temporary until we start using internalized extension logic
    @Test
    void test_temporarySingleMode() {
        final JavaBeanTesterBuilder<String, Object> builder = new JavaBeanTesterBuilder<>(String.class);
        final JavaBeanTesterWorker<String, Object> worker = Whitebox.getInternalState(builder, "worker");
        Assertions.assertEquals(String.class, worker.getClazz());
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
