/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.instance;

import java.lang.reflect.Constructor;
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
 * The Class ConstructorInstanceTest.
 */
// TODO 2025-06-29 JWL Class is not mockable
@Disabled
@ExtendWith(MockitoExtension.class)
class ConstructorInstanceTest {

    /** The constructor instance. */
    @InjectMocks
    ConstructorInstance constructorInstance;

    @Mock
    Constructor<?> mockConstructor;

    /**
     * New instance instantiation exception.
     *
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     */
    @Test
    void newInstanceInstantiationException()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Mockito.when(mockConstructor.newInstance()).thenThrow(new InstantiationException());

        Assertions.assertThrows(InstantiationException.class, () -> ConstructorInstance.newInstance(mockConstructor));
    }

    /**
     * New instance illegal access exception.
     *
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     */
    @Test
    void newInstanceIllegalAccessException()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Mockito.when(mockConstructor.newInstance()).thenThrow(new IllegalAccessException());

        Assertions.assertThrows(IllegalAccessException.class, () -> ConstructorInstance.newInstance(mockConstructor));
    }

    /**
     * New instance invocation target exception.
     *
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     */
    @Test
    void newInstanceInvocationTargetException()
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Mockito.when(mockConstructor.newInstance()).thenThrow(new InvocationTargetException(new Exception()));

        Assertions.assertThrows(InvocationTargetException.class,
                () -> ConstructorInstance.newInstance(mockConstructor));
    }

}
