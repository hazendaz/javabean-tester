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
import lombok.Data;
import lombok.Getter;

/**
 * Instantiates a new sample depth bean.
 */
@Data
public class SampleDepthBean {

    /** The list. */
    private List<String> list;

    /** The map. */
    private Map<String, String> map;

    /** The concurrent map. */
    private ConcurrentMap<String, String> concurrentMap;

    /** The set. */
    private Set<String> set;

    /** The string. */
    private String string;

    /** The string array. */
    private String[] stringArray;

    /** The boolean wrapper. */
    private Boolean booleanWrapper;

    /** The int wrapper. */
    private Integer intWrapper;

    /** The long wrapper. */
    private Long longWrapper;

    /** The double wrapper. */
    private Double doubleWrapper;

    /** The float wrapper. */
    private Float floatWrapper;

    /** The character wrapper. */
    private Character characterWrapper;

    /** The byte wrapper. */
    private Byte byteWrapper;

    /** The byte array. */
    private Byte[] byteArray;

    /** The boolean primitive. */
    private boolean booleanPrimitive;

    /** The int primitive. */
    private int intPrimitive;

    /** The long primitive. */
    private long longPrimitive;

    /** The double primitive. */
    private double doublePrimitive;

    /** The float primitive. */
    private float floatPrimitive;

    /** The char primitive. */
    private char charPrimitive;

    /** The byte primitive. */
    private byte bytePrimitive;

    /** The big decimal. */
    private BigDecimal bigDecimal;

    /** The uuid. */
    private UUID uuid;

    /** The instant. */
    private Instant instant;

    /** The date. */
    private Date date;

    /** The Local Date. */
    private LocalDate localDate;

    /** The Local Date Time. */
    private LocalDateTime localDateTime;

    /** The Local Time. */
    private LocalTime localTime;

    /** The Offset Date Time. */
    private OffsetDateTime offsetDateTime;

    /** The Zoned Date Time. */
    private ZonedDateTime zonedDateTime;

    /** The Boolean wrapper with is/setter style (non lombok - java metro style). */
    @Getter(AccessLevel.NONE)
    private Boolean booleanWrapperIsSetter;

    /** SampleBean nesting would not cause stack overflow as no no-arg constructor. */
    private SampleBean sampleBean;

    /** SampleDepthBean nesting would cause stack overflow. Fixed by not deeply testing in value builder. */
    private SampleDepthBean sampleDepthBean;

}
