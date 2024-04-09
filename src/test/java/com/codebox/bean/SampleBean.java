/*
 * JavaBean Tester (https://github.com/hazendaz/javabean-tester)
 *
 * Copyright 2012-2024 Hazendaz.
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

import jakarta.annotation.PostConstruct;

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
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import org.slf4j.Logger;

/**
 * Instantiates a new sample bean.
 */
@Data
public class SampleBean {

    /** The logger. */
    private Logger logger;

    /** The empty bean. */
    private EmptyBean emptyBean;

    /** The sample depth bean. */
    private SampleDepthBean sampleDepthBean;

    /** The list. */
    private List<String> list;

    /** The map. */
    private Map<String, String> map;

    /** The concurrent map. */
    private ConcurrentMap<String, String> concurrentMap;

    /** The tree set. */
    private TreeSet<String> treeSet;

    /** The string. */
    private final String string;

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

    /**
     * Instantiates a new sample bean. Causes JVM to not create a default no-arg constructor.
     *
     * @param newString
     *            the new string
     */
    public SampleBean(final String newString) {
        this.string = newString;
    }

    /**
     * Clear.
     */
    public void clear() {
        // Do nothing so this is the as class setup
    }

    /**
     * Checks if is boolean wrapper is setter.
     *
     * @return the boolean
     */
    public Boolean isBooleanWrapperIsSetter() {
        return this.booleanWrapperIsSetter;
    }

    /**
     * Sample post construct.
     */
    @PostConstruct
    public void init() {
        // Do nothing support to invoke code in clearTest()
    }
}
