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

import java.util.function.UnaryOperator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.converter.IntegerStringConverter;

/**
 * A flexible, dynamically adjustable SpinnerValueFactory for the resolution spinner the provides
 * properties for up and down increment functions and for min and max values.
 * @author mes
 */
public class ResolutionValueFactory extends SpinnerValueFactory<Integer> {

  private final SimpleObjectProperty<UnaryOperator<Integer>> up;
  private final SimpleObjectProperty<UnaryOperator<Integer>> down;

  /**
   * Constructor for ResolutionValueFactory which provides properties for min, max, increment,
   * and value.
   * @param converter is the IntegerConverter used to convert between String and Integer
   * @param up
   * @param down
   */
  public ResolutionValueFactory(IntegerStringConverter converter, 
      SimpleObjectProperty<UnaryOperator<Integer>> up, 
      SimpleObjectProperty<UnaryOperator<Integer>> down) {
    super();
    this.setConverter(converter);
    this.up = up;
    this.down = down;
  }

  /**
   * Decrement the value by specified number of steps using increment property and bounding the
   * result to the min property.
   * @param steps 
   */
  @Override
  public void decrement(int steps) {
    int v = this.valueProperty().get();
    while (steps-- > 0) {
      this.valueProperty().set(this.down.get().apply(v));
    }
  }

  // TODO: sync min/max in documentation
  
  /**
   * Increment the value by specified number of steps using increment property and bounding the
   * result to the max property.
   * @param steps 
   */
  @Override
  public void increment(int steps) {
    int v = this.valueProperty().get();
    while (steps-- > 0) {
      this.valueProperty().set(this.up.get().apply(v));
    }
  }

}
