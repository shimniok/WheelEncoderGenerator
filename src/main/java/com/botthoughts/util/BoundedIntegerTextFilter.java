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

import java.util.function.UnaryOperator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.TextFormatter;

/**
 * String filter for encoder resolution text entry (via text field/editor)
 * @author mes
 */
public class BoundedIntegerTextFilter implements UnaryOperator<TextFormatter.Change> {
  SimpleIntegerProperty min;
  SimpleIntegerProperty max;

  /**
   * Create new ResolutionStringFilter with specified min and max properties to accommodate 
   * dynamically updating the min/max values. NOTE: be sure when changing these values of these
   * properties to also change the value in the field to fall inside the bounds.
   * @param min minimum value property
   * @param max maximum value property
   */
  public BoundedIntegerTextFilter(SimpleIntegerProperty min, SimpleIntegerProperty max) {
    this.min = min;
    this.max = max;
  }

  /**
   * Returns minimum value property
   * @return min value property
   */
  public SimpleIntegerProperty minProperty() {
    return min;
  }

  /**
   * Returns maximum value property
   * @return min value property
   */
  public SimpleIntegerProperty maxProperty() {
    return max;
  }
  
  /**
   * Implements the text filter, rejecting any illegal (non-numeric) characters, as well as any
   * change that would result in a value outside of the min and max properties
   * @param change specifies the change being made
   * @return the change, after filtering of input/changes is complete
   */
  @Override
  public TextFormatter.Change apply(TextFormatter.Change change) {
    String doubleRegex = "[0-9]*";
    String newText = change.getControlNewText();
    int start = change.getRangeStart();
    int end = change.getRangeEnd();
    int caret = change.getCaretPosition();

    if (change.isAdded()) {
      String addedText = change.getText();

      if (!newText.matches(doubleRegex)) {
        change.setText("");
        change.setRange(start, start);
        change.setCaretPosition(caret - addedText.length());
        change.setAnchor(caret - addedText.length());
      }

    } else if (change.isDeleted()) {
      if (!newText.matches(doubleRegex)) {
        change.setRange(start, start);
      }
    } else if (change.isReplaced()) {
      // This never seems to get called in normal operation ??
      System.out.println("replaced");
    }
    
    int r = Integer.parseInt(newText);
    if (r < min.get() || r > max.get()) {
      change.setText("");
      change.setRange(start, start);
    }
    
    return change;
  }
}
