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

import javafx.scene.control.TextFormatter;

/**
 *
 * @author mes
 */
public class IntegerFormatter extends TextFormatter {
  private final ResolutionStringFilter filter;
  private final IntegerConverter converter;
  
  /**
   * Create new IntegerFormatter
   * @param converter converts a String to a Integer
   * @param v is the default value
   * @param filter filters input into a text field
   */
  public IntegerFormatter(IntegerConverter converter, Integer v, ResolutionStringFilter filter) {
    super(converter, v, filter);
    this.converter = converter;
    this.filter = filter;
  }

  /**
   * Returns the converter associated with this formatter
   * @return converter
   */
  public IntegerConverter getConverter() {
    return converter;
  }
  
  /**
   * Returns the filter associated with this formatter as a ResolutionStringFilter
   * @return filter
   */
  public ResolutionStringFilter getTextFilter() {
    return filter;
  }

}
