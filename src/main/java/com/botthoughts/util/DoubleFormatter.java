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
import javafx.scene.control.TextFormatter;

/**
 * TextFormatter for Double consisting of a FixedFormatDoubleConverter and UnsignedDoubleTextFilter
 * @author mes
 */
public class DoubleFormatter extends TextFormatter {
  private FixedFormatDoubleConverter converter;
  private UnsignedDoubleTextFilter filter;
  
  /**
   * Create new DoubleFormatter
   * @param converter converts a String to a Double
   * @param v is the default value
   * @param filter filters input into a text field
   */
  public DoubleFormatter(FixedFormatDoubleConverter converter, Double v, UnsignedDoubleTextFilter filter) {
    super(converter, v, filter);
    this.converter = converter;
    this.filter = filter;
  }

  /**
   * Alternative constructor where decimal format and default value are specified
   * @param decimals is the number of decimal places to use for formatting
   * @param v is the default value
   */
  public DoubleFormatter(int decimals, Double v) {
    this(new FixedFormatDoubleConverter(), v, new UnsignedDoubleTextFilter());
    this.converter.decimalsProperty().set(decimals);
  }
  
  /**
   * Alternative constructor where decimal format is specified and default value is 0
   * @param decimals is the number of decimal places to use for formatting
   */
  public DoubleFormatter(int decimals) {
    this(decimals, 0.0);
  }
     
  /**
   * Default constructor where default decimal format of 2 is used and default value is 0
   */
  public DoubleFormatter() {
    this(2, 0.0);
  }
  
  /**
   * Return the converter property specifying the number of decimal places to use for formatting
   * @return decimals property for the converter
   */
  public SimpleIntegerProperty decimalsProperty() {
    return this.converter.decimalsProperty();
  }
  
  /**
   * Sets the converter property specifying the number of decimal places to use for formatting
   * @param decimals number of decimal places to use for formatting
   */
  public void setDecimals(int decimals) {
    this.converter.decimalsProperty().set(decimals);
  }
  
  /**
   * Returns the converter associated with this formatter
   * @return converter
   */
  public FixedFormatDoubleConverter getConverter() {
    return converter;
  }
  
  /**
   * Returns the filter associated with this formatter as a UnsignedDoubleTextFilter
   * @return filter
   */
  public UnsignedDoubleTextFilter getTextFilter() {
    return filter;
  }
  
}
