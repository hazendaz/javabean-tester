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
package com.codebox.instance;

import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * The Class ClassInstanceTest.
 */
// TODO 2025-06-29 JWL Class is not mockable
@Disabled
@ExtendWith(MockitoExtension.class)
class ClassInstanceTest {

    /** The class instance. */
    @InjectMocks
    ClassInstance<Object> classInstance;

    /** The mock clazz. */
    @Mock
    Class<Object> mockClazz;

    /**
     * New instance instantiation exception.
     *
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws NoSuchMethodException
     *             the no such method exception
     * @throws InvocationTargetException
     *             the invocation target exception
     */
    @Test
    void newInstanceInstantiationException()
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Mockito.when(mockClazz.getDeclaredConstructor()).thenReturn(Object.class.getDeclaredConstructor());
        Mockito.when(mockClazz.getDeclaredConstructor().newInstance()).thenThrow(new InstantiationException());

        Assertions.assertThrows(InstantiationException.class, () -> this.classInstance.newInstance(this.mockClazz));
    }

    /**
     * New instance illegal access exception.
     *
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws NoSuchMethodException
     *             the no such method exception
     * @throws InvocationTargetException
     *             the invocation target exception
     */
    @Test
    void newInstanceIllegalAccessException()
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Mockito.when(mockClazz.getDeclaredConstructor()).thenReturn(Object.class.getDeclaredConstructor());
        Mockito.when(mockClazz.getDeclaredConstructor().newInstance()).thenThrow(new IllegalAccessException());

        Assertions.assertThrows(InstantiationException.class, () -> this.classInstance.newInstance(this.mockClazz));
    }

}
