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
import javafx.scene.control.TextFormatter;

/**
 * Filter text input to ensure a valid Double is entered
 * @author mes
 */
public class DoubleStringFilter implements UnaryOperator<TextFormatter.Change> {

  static final String REGEX = "([0-9]*\\.?[0-9]*)";

  /**
   * Filters prior to applying changes to text field
   * @param change contains the original and changed text information
   * @return change with filters applied
   */
  @Override
  public TextFormatter.Change apply(TextFormatter.Change change) {
    String newText = change.getControlNewText();
    int start = change.getRangeStart();
    int end = change.getRangeEnd();
    int caret = change.getCaretPosition();
    
    if (change.isAdded()) {
      String addedText = change.getText();

      if (!newText.matches(REGEX)) {
        change.setText("");
        change.setRange(start, start);
        change.setCaretPosition(caret - addedText.length());
        change.setAnchor(caret - addedText.length());
      }

      // Special case to allow .N without breaking parsing)
      if (change.getControlNewText().equals(".")) {
        change.setText("0.");
        change.setCaretPosition(caret + 1);
        change.setAnchor(caret + 1);
      }

    } else if (change.isDeleted()) {
      if (!newText.matches(REGEX)) {
        change.setRange(start, start);
      }
    } else if (change.isReplaced()) {
      // This never seems to get called in normal operation ??
      System.out.println("replaced");
    }

    return change;
  }
  
}
