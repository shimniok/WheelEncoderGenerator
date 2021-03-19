/*
 * Copyright 2021 mes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.botthoughts.util;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.converter.IntegerStringConverter;

/**
 * Converts between String and Integer, with optional minimum/maximum value fixing
 * @author mes
 */
public class BoundedIntegerConverter extends IntegerStringConverter {
  private SimpleIntegerProperty min;
  private SimpleIntegerProperty max;
  
  /**
   * Default Constructor
   */
  private BoundedIntegerConverter() {} 
  
  /**
   * Create new IntegerConverter that will keep value bound to minimum..maximum, inclusive
   * @param min minimum bound for the conversion
   * @param max maximum bound for the conversion
   */
  public BoundedIntegerConverter(int min, int max) {
    super();
    this.min = new SimpleIntegerProperty(min);
    this.max = new SimpleIntegerProperty(max);
  }

  /**
   * Return the minimum value property for the converter
   * @return minimum
   */
  public SimpleIntegerProperty minProperty() {
    return min;
  }

  /**
   * Return the maximum value property for the converter
   * @return maximum property
   */
  public SimpleIntegerProperty maxProperty() {
    return max;
  }
  
  /**
   * Converts the Integer to String with the number of decimal places given by decimals property
   * and bounded to minimum and maximum properties.
   *
   * @param value double to convert to string; value is set to min or max if out of bounds
   * @return formatted String (e.g. 123.456, 123.45, etc.)
   */
  @Override
  public String toString(Integer value) {
    System.out.print("IntegerConverter: toString() BEFORE d="+value);
    value = fix(value);
    System.out.println(" AFTER d="+value);
    return super.toString(value);
  }
  
  /**
   * Converts the String to Integer bounded to minimum and maximum properties.
   *
   * @param s is the String to be converted to Double
   * @return Double
   */
  @Override
  public Integer fromString(String s) {
    Integer value = Integer.parseInt(s);
    System.out.print("IntegerConverter: fromString() BEFORE d="+value);
    value = fix(value);
    System.out.println(" AFTER d="+value);
    return value;
  }
  
  private Integer fix(Integer value) {
    if (value < min.get()) value = min.get();
    if (value > max.get()) value = max.get();
    return value;
  }
  
}
