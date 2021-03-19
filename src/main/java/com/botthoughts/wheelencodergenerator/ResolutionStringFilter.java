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
import javafx.scene.control.TextFormatter;

// TODO: documentation

/**
 *
 * @author mes
 */
public class ResolutionStringFilter implements UnaryOperator<TextFormatter.Change> {
  SimpleIntegerProperty min;
  SimpleIntegerProperty max;

  public ResolutionStringFilter(SimpleIntegerProperty min, SimpleIntegerProperty max) {
    this.min = min;
    this.max = max;
  }

  public SimpleIntegerProperty minProperty() {
    return min;
  }

  public SimpleIntegerProperty maxProperty() {
    return max;
  }
  
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
    
    System.out.println("apply() newText="+change.getControlNewText());
    int r = Integer.parseInt(newText);
    if (r < min.get() || r > max.get()) {
      change.setText("");
      change.setRange(start, start);
    }
    
    return change;
  }
}
