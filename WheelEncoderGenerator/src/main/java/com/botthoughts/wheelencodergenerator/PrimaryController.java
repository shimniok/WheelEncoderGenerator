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
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.converter.IntegerStringConverter;


public class PrimaryController implements Initializable {
    
    Encoder encoder;
    
    @FXML
    TextField outer;
    @FXML
    TextField inner;
    @FXML
    TextField center;
    @FXML
    CheckBox inverted;
    @FXML
    ComboBox units;

    
    @FXML
    private void handleUnits(ActionEvent e) {
        String x = units.getSelectionModel().getSelectedItem().toString();
        System.out.println("selected "+x);
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        
        encoder = new Encoder();
        
        outer.textProperty().bindBidirectional((Property) encoder.getOuterDiameter(), new IntegerStringConverter());
        inner.textProperty().bindBidirectional((Property) encoder.getInnerDiameter(), new IntegerStringConverter());
        center.textProperty().bindBidirectional((Property) encoder.getCenterDiameter(), new IntegerStringConverter());
        inverted.selectedProperty().bindBidirectional((Property) encoder.getInverted());
        units.getItems().addAll(encoder.getUnitOptions());
        
        encoder.getUnits().addListener((observable, oldValue, newValue) -> {
            System.out.println("model change:" + newValue.getClass().toString());
            units.selectionModelProperty().setValue(newValue);
        });
        units.getSelectionModel().selectFirst();

        // TODO - add TextFormatters and Input verifiers
        //invert.setTextFormatter(new TextFormatter<>());
    }    
     
}
