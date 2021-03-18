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
package com.botthoughts.wheelencodergenerator;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.converter.DoubleStringConverter;

/**
 * Convert between String and Double
 * @author mes
 */
public class DoubleConverter extends DoubleStringConverter {
  private SimpleIntegerProperty decimals = new SimpleIntegerProperty();
  
  /**
   * Constructor for DoubleConverter
   * @param decimals is the number of decimal places to use for formatting
   */
  public DoubleConverter(int decimals) {
    if (decimals < 1) decimals = 1;
    this.decimals.set(decimals);
  }

  /**
   * Default constructor, sets number of decimal places to a default value.
   */
  DoubleConverter() {
    this(2);
  }

  /**
   * Converts the Double to String with the number of decimal places given by decimals property.
   * @param d double to convert to string
   * @return formatted String (e.g. 123.456, 123.45, etc.)
   */
  @Override
  public String toString(Double d) {
    String format = String.format("%%.%df", decimals.get());
    return String.format(format, d);
  }

  /**
   * Return decimals property that sets the number of decimal places to use in formatting
   * @return decimals property
   */
  public SimpleIntegerProperty decimalsProperty() {
    return decimals;
  }

}
