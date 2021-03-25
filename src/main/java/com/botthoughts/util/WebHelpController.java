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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

/**
 * FXML Controller class to display web-based Help documentation in a separate window. The URL
 * for the help website is specified by the property help.url in /web.properties file. The
 * injectables include WebView helpView and AnchorPane helpWindow.
 *
 * @author mes
 */
public class WebHelpController implements Initializable {

  @FXML WebView helpView;
  @FXML AnchorPane helpWindow;
  
  /**
   * Initializes the controller class.
   * @param url
   * @param rb
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    Properties props = new Properties();
    
    try {
      InputStream stream = getClass().getResourceAsStream("/help.properties");
      props.load(stream);
      String helpUrl = props.getProperty("help.url");
      helpView.getEngine().load(helpUrl);
    } catch (IOException ex) {
      System.out.println("HelpController: error reading properties file: "+ex);
    }
  }

}
