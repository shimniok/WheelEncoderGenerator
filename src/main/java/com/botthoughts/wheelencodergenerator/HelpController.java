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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @author mes
 */
public class HelpController implements Initializable {

  @FXML
  WebView helpView;
  @FXML
  AnchorPane helpWindow;
  
  SimpleStringProperty locationProperty;

  public HelpController() {
    this.locationProperty = new SimpleStringProperty();
  }

  public SimpleStringProperty getLocationProperty() {
    return locationProperty;
  }
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    System.out.println("HelpControler: initialize()");
    
    if (helpView == null) {
      System.out.println("helpView is NULL");
    } else {
      if (helpView.getEngine() == null) {
        System.out.println("engine is NULL");
      }
    }
    
    helpView.getEngine().load("https://shimniok.github.io/WheelEncoderGenerator/");
  }

}
