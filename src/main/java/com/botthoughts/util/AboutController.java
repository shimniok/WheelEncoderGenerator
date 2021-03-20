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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author mes
 */
public class AboutController implements Initializable {

  @FXML AnchorPane aboutWindow;
  @FXML Label aboutName;
  @FXML Label aboutDescription;
  @FXML Label aboutVersion;
  @FXML Label aboutBuild;
  @FXML Label aboutAuthor;

  private AppInfo appInfo;

  @Override
  public void initialize(URL url, ResourceBundle rb) {

    AppInfo info;
    
    // Attempt to load app info
    try {
      info = AppInfo.get();
      // Put app info into labels
      aboutName.setText(info.getName());
      aboutDescription.setText(info.getDescription());
      aboutVersion.setText("Version "+info.getVersion());
      aboutBuild.setText(info.getPlatform() + " " + info.getBuildDate());
      aboutAuthor.setText("by\n"+info.getAuthor());
    } catch (IOException ex) {
      System.out.println("AppInfo: " + ex);
    }
    
  }

}
