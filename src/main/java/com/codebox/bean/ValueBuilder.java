/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.bean;

import com.codebox.enums.LoadData;
import com.codebox.enums.LoadType;
import com.codebox.instance.ClassInstance;
import com.codebox.instance.ConstructorInstance;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Value Builder Builds values from given type.
 */
@Data
public class ValueBuilder {

    /** The Constant Logger. */
    private static final Logger logger = LoggerFactory.getLogger(ValueBuilder.class);

    /** The load data. */
    private LoadData loadData;

    /**
     * Builds the value.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param loadType
     *            the load type
     *
     * @return the object
     */
    public <T> Object buildValue(final Class<T> clazz, final LoadType loadType) {
        // Next check for a no-arg constructor
        final Constructor<?>[] ctrs = clazz.getConstructors();
        for (final Constructor<?> ctr : ctrs) {
            if (ctr.getParameterTypes().length == 0 && clazz != String.class) {
                if (this.loadData == LoadData.ON && !containsSelf(clazz)) {
                    // Load Underlying Data to deeply test when it doesn't contain self
                    final JavaBeanTesterWorker<T, Object> beanTesterWorker = new JavaBeanTesterWorker<>(clazz);
                    beanTesterWorker.setLoadData(this.loadData);
                    beanTesterWorker.getterSetterTests(new ClassInstance<T>().newInstance(clazz));
                    return null;
                }
                // The class has a no-arg constructor, so just call it (e.g. String, Date, etc.)
                return ConstructorInstance.newInstance(ctr);
            }
        }

        // Specific rules for common classes
        Object returnObject = null;
        switch (loadType) {
            case ALTERNATE_DATA:
                returnObject = ValueBuilder.setAlternateValues(clazz);
                break;
            case NULL_DATA:
                returnObject = ValueBuilder.setNullValues(clazz);
                break;
            case STANDARD_DATA:
            default:
                returnObject = ValueBuilder.setStandardValues(clazz);
                break;
        }

        if (returnObject != null || loadType == LoadType.NULL_DATA) {
            return returnObject;
        }

        if (clazz.isAssignableFrom(BigDecimal.class)) {
            return BigDecimal.ONE;
        }

        if (clazz.isAssignableFrom(UUID.class)) {
            return UUID.fromString("00000000-0000-0000-0000-000123456789");
        }

        if (clazz.isAssignableFrom(Instant.class)) {
            return Instant.ofEpochSecond(1L);
        }

        if (clazz.isAssignableFrom(List.class)) {
            return new ArrayList<>();
        }

        if (clazz.isAssignableFrom(Map.class)) {
            return new HashMap<>();
        }

        if (clazz.isAssignableFrom(ConcurrentMap.class)) {
            return new ConcurrentHashMap<>();
        }

        if (clazz.isAssignableFrom(Set.class)) {
            return new TreeSet<>();
        }

        if (clazz.isAssignableFrom(LocalDate.class)) {
            return LocalDate.now();
        }

        if (clazz.isAssignableFrom(LocalDateTime.class)) {
            return LocalDateTime.of(2000, 10, 1, 0, 0);
        }

        if (clazz.isAssignableFrom(LocalTime.class)) {
            return LocalTime.of(0, 0);
        }

        if (clazz.isAssignableFrom(OffsetDateTime.class)) {
            return OffsetDateTime.of(2000, 10, 1, 0, 0, 0, 0, ZoneOffset.MIN);
        }

        if (clazz.isAssignableFrom(ZonedDateTime.class)) {
            return ZonedDateTime.of(LocalDateTime.of(2020, 11, 16, 10, 26, 00, 01), ZoneId.of("UTC"));
        }

        if (clazz == Logger.class) {
            return LoggerFactory.getLogger(clazz);
        }

        if (clazz.isEnum()) {
            return clazz.getEnumConstants()[0];
        }

        // XXX Add additional rules here

        // XXX Don't fail this...until alternative solution is determined
        ValueBuilder.logger.warn(
                "Unable to build value for class '{}', please raise ticket with JavaBeanTester for desired support.",
                clazz.getName());
        return null;
    }

    /**
     * Set using alternate test values.
     *
     * @param <T>
     *            the type parameter associated with the class under test.
     * @param clazz
     *            the class under test.
     *
     * @return Object the Object to use for test.
     */
    private static <T> Object setAlternateValues(final Class<T> clazz) {
        return ValueBuilder.setValues(clazz, "ALT_VALUE", 1, Boolean.FALSE, Integer.valueOf(2), Long.valueOf(2),
                Double.valueOf(2.0), Float.valueOf(2.0F), Character.valueOf('N'), Byte.valueOf((byte) 2));
    }

    /**
     * Set using null test values.
     *
     * @param <T>
     *            the type parameter associated with the class under test.
     * @param clazz
     *            the class under test.
     *
     * @return Object the Object to use for test.
     */
    private static <T> Object setNullValues(final Class<T> clazz) {
        return ValueBuilder.setValues(clazz, null, 0, null, null, null, null, null, null, null);
    }

    /**
     * Set using standard test values.
     *
     * @param <T>
     *            the type parameter associated with the class under test.
     * @param clazz
     *            the class under test.
     *
     * @return Object the Object to use for test.
     */
    private static <T> Object setStandardValues(final Class<T> clazz) {
        return ValueBuilder.setValues(clazz, "TEST_VALUE", 1, Boolean.TRUE, Integer.valueOf(1), Long.valueOf(1),
                Double.valueOf(1.0), Float.valueOf(1.0F), Character.valueOf('Y'), Byte.valueOf((byte) 1));
    }

    /**
     * Set Values for object.
     *
     * @param <T>
     *            the type parameter associated with the class under test.
     * @param clazz
     *            the class instance under test.
     * @param string
     *            value of string object.
     * @param arrayLength
     *            amount of array objects to create.
     * @param booleanValue
     *            value of boolean object.
     * @param integerValue
     *            value of integer object.
     * @param longValue
     *            value of long object.
     * @param doubleValue
     *            value of double object.
     * @param floatValue
     *            value of float object.
     * @param characterValue
     *            value of character object.
     * @param byteValue
     *            value of character object.
     *
     * @return Object value determined by input class. If not found, returns null.
     */
    private static <T> Object setValues(final Class<T> clazz, final String string, final int arrayLength,
            final Boolean booleanValue, final Integer integerValue, final Long longValue, final Double doubleValue,
            final Float floatValue, final Character characterValue, final Byte byteValue) {
        if (clazz == String.class) {
            return string;
        }
        if (clazz.isArray()) {
            return Array.newInstance(clazz.getComponentType(), arrayLength);
        }
        if (clazz == boolean.class || clazz == Boolean.class) {
            return ValueBuilder.initializeBoolean(clazz, booleanValue);
        }
        if (clazz == int.class || clazz == Integer.class) {
            return ValueBuilder.initializeInteger(clazz, integerValue);
        }
        if (clazz == long.class || clazz == Long.class) {
            return ValueBuilder.initializeLong(clazz, longValue);
        }
        if (clazz == double.class || clazz == Double.class) {
            return ValueBuilder.initializeDouble(clazz, doubleValue);
        }
        if (clazz == float.class || clazz == Float.class) {
            return ValueBuilder.initializeFloat(clazz, floatValue);
        }
        if (clazz == char.class || clazz == Character.class) {
            return ValueBuilder.initializeCharacter(clazz, characterValue);
        }
        if (clazz == byte.class || clazz == Byte.class) {
            return ValueBuilder.initializeByte(clazz, byteValue);
        }
        return null;
    }

    /**
     * Initialize boolean.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param booleanValue
     *            the boolean value
     *
     * @return the object
     */
    private static <T> Object initializeBoolean(final Class<T> clazz, final Boolean booleanValue) {
        if (clazz == boolean.class && booleanValue == null) {
            return Boolean.FALSE;
        }
        return booleanValue;
    }

    /**
     * Initialize integer.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param integerValue
     *            the integer value
     *
     * @return the object
     */
    private static <T> Object initializeInteger(final Class<T> clazz, final Integer integerValue) {
        if (clazz == int.class && integerValue == null) {
            return Integer.valueOf(-1);
        }
        return integerValue;
    }

    /**
     * Initialize long.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param longValue
     *            the long value
     *
     * @return the object
     */
    private static <T> Object initializeLong(final Class<T> clazz, final Long longValue) {
        if (clazz == long.class && longValue == null) {
            return Long.valueOf(-1);
        }
        return longValue;
    }

    /**
     * Initialize double.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param doubleValue
     *            the double value
     *
     * @return the object
     */
    private static <T> Object initializeDouble(final Class<T> clazz, final Double doubleValue) {
        if (clazz == double.class && doubleValue == null) {
            return Double.valueOf(-1.0);
        }
        return doubleValue;
    }

    /**
     * Initialize float.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param floatValue
     *            the float value
     *
     * @return the object
     */
    private static <T> Object initializeFloat(final Class<T> clazz, final Float floatValue) {
        if (clazz == float.class && floatValue == null) {
            return Float.valueOf(-1.0F);
        }
        return floatValue;
    }

    /**
     * Initialize character.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param characterValue
     *            the character value
     *
     * @return the object
     */
    private static <T> Object initializeCharacter(final Class<T> clazz, final Character characterValue) {
        if (clazz == char.class && characterValue == null) {
            return Character.valueOf('\u0000');
        }
        return characterValue;
    }

    /**
     * Initialize byte.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @param byteValue
     *            the byte value
     *
     * @return the object
     */
    private static <T> Object initializeByte(final Class<T> clazz, final Byte byteValue) {
        if (clazz == byte.class && byteValue == null) {
            return Byte.valueOf((byte) -1);
        }
        return byteValue;
    }

    /**
     * Contains self.
     *
     * @param <T>
     *            the generic type
     * @param clazz
     *            the clazz
     * @return true, if successful
     */
    private <T> boolean containsSelf(final Class<T> clazz) {
        final List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        for (Field field : fields) {
            if (field.getType().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

}
