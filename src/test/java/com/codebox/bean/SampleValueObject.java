/*
 * SPDX-License-Identifier: Apache-2.0
 * See LICENSE file for details.
 *
 * Copyright 2012-2026 hazendaz
 *
 * Portions of initial baseline code (getter/setter test) by Rob Dawson (CodeBox)
 */
package com.codebox.bean;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

/**
 * The Class SampleValueObject.
 *
 * <p>
 * From Lombok docs: https://projectlombok.org/features/Value
 *
 * <p>
 * <code>@Value</code> is the immutable variant of <code>@Data</code>; all fields are made and final by default, and
 * setters are not generated. The class itself is also made final by default, because immutability is not something that
 * can be forced onto a subclass.
 */
@Value
public class SampleValueObject {

    /** The empty bean. */
    EmptyBean emptyBean;

    /** The sample depth bean. */
    SampleDepthBean sampleDepthBean;

    /** The list. */
    List<String> list;

    /** The map. */
    Map<String, String> map;

    /** The concurrent map. */
    ConcurrentMap<String, String> concurrentMap;

    /** The set. */
    Set<String> set;

    /** The string. */
    String string;

    /** The string array. */
    String[] stringArray;

    /** The boolean wrapper. */
    Boolean booleanWrapper;

    /** The int wrapper. */
    Integer intWrapper;

    /** The long wrapper. */
    Long longWrapper;

    /** The double wrapper. */
    Double doubleWrapper;

    /** The float wrapper. */
    Float floatWrapper;

    /** The character wrapper. */
    Character characterWrapper;

    /** The byte wrapper. */
    Byte byteWrapper;

    /** The byte array. */
    Byte[] byteArray;

    /** The boolean primitive. */
    boolean booleanPrimitive;

    /** The int primitive. */
    int intPrimitive;

    /** The long primitive. */
    long longPrimitive;

    /** The double primitive. */
    double doublePrimitive;

    /** The float primitive. */
    float floatPrimitive;

    /** The char primitive. */
    char charPrimitive;

    /** The byte primitive. */
    byte bytePrimitive;

    /** The big decimal. */
    BigDecimal bigDecimal;

    /** The uuid. */
    UUID uuid;

    /** The instant. */
    Instant instant;

    /** The date. */
    Date date;

    /** The Local Date. */
    LocalDate localDate;

    /** The Local Date Time. */
    LocalDateTime localDateTime;

    /** The Local Time. */
    LocalTime localTime;

    /** The Offset Date Time. */
    OffsetDateTime offsetDateTime;

    /** The Zoned Date Time. */
    ZonedDateTime zonedDateTime;

    /** The Boolean wrapper with is/setter style (non lombok - java metro style). */
    @Getter(AccessLevel.NONE)
    Boolean booleanWrapperIsSetter;
}
