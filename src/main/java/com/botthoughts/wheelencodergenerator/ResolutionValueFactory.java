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
 *
 * @author mes
 */
public class ResolutionValueFactory extends SpinnerValueFactory<Integer> {

  private final SimpleIntegerProperty min;
  private final SimpleIntegerProperty max;
  private final SimpleObjectProperty<UnaryOperator<Integer>> up;
  private final SimpleObjectProperty<UnaryOperator<Integer>> down;

  /**
   * Constructor for ResolutionValueFactory which provides properties for min, max, increment,
   * and value.
   * @param converter is the IntegerConverter used to convert between String and Integer
   * @param min
   * @param max
   * @param up
   * @param down
   */
  public ResolutionValueFactory(IntegerStringConverter converter, 
      SimpleIntegerProperty min, SimpleIntegerProperty max, // TODO: remove min/max, no longer needed
      SimpleObjectProperty<UnaryOperator<Integer>> up, 
      SimpleObjectProperty<UnaryOperator<Integer>> down) {
    super();
    this.setConverter(converter);
    this.min = min;
    this.max = max;
    this.up = up;
    this.down = down;
  }

  /**
   * Return the property representing the minimum permitted value
   * @return minimum property
   */
  public SimpleIntegerProperty minProperty() {
    return min;
  }
  
  /**
   * Return the property representing the maximum permitted value
   * @return maximum property
   */
  public SimpleIntegerProperty maxProperty() {
    return max;
  }
  
  /**
   * Decrement the value by specified number of steps using increment property and bounding the
   * result to the min property.
   * @param steps 
   */
  @Override
  public void decrement(int steps) {
    int v = this.valueProperty().get();
    System.out.println("RVF: dec() BEFORE value="+valueProperty().get()+" min="+min.get()+" max="+max.get());
    while (steps-- > 0) {
      if (this.down.get().apply(v) >= min.get()) {
        this.valueProperty().set(this.down.get().apply(v));
      }
    }
    System.out.println("AFTER value="+valueProperty().get());
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
    System.out.println("RVF: inc() BEFORE value="+valueProperty().get()+" min="+min.get()+" max="+max.get());
    while (steps-- > 0) {
      if (this.up.get().apply(v) <= max.get()) {
        this.valueProperty().set(this.up.get().apply(v));
      }
    }
    System.out.println("AFTER value="+valueProperty().get());
  }

}
