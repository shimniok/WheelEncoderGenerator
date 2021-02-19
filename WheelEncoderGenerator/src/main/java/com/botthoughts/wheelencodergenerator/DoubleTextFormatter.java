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

import java.util.Formatter;
import java.util.Locale;
import java.util.function.UnaryOperator;
import javafx.scene.control.TextFormatter;

/**
 *
 * @author mes
 */
public class DoubleTextFormatter {

  private UnaryOperator<TextFormatter.Change> filter;

  /**
   * Create a new DoubleTextFormatter object
   */
  public DoubleTextFormatter() {
    this.filter = (change) -> {
      String doubleRegex = "([0-9]*\\.?[0-9]*)";
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

        // Special case to allow .N without breaking parsing)
        if (change.getControlNewText().equals(".")) {
          change.setText("0.");
          change.setCaretPosition(caret + 1);
          change.setAnchor(caret + 1);
        }

      } else if (change.isDeleted()) {
        if (!newText.matches(doubleRegex)) {
          change.setRange(start, start);
        }
      } else if (change.isReplaced()) {
        // This never seems to get called in normal operation ??
        System.out.println("replaced");
      }

      return change;
    };
  }

  /**
   * Return a new DoubleTextFormatter as a TextFormatter object, factory style.
   *
   * @return new TextFormatter
   */
  public TextFormatter get() {
    return new TextFormatter(filter);
  }

}
