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
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
    TabPane type;

    @FXML
    Tab absolute;
    @FXML
    Spinner absResolution;
    @FXML
    ComboBox coding;

    @FXML
    Tab incremental;
    @FXML
    Spinner incResolution;
    @FXML
    CheckBox index;
    @FXML
    CheckBox quadrature;

    
    @FXML
    private void handleUnits(ActionEvent e) {
        int i = units.getSelectionModel().getSelectedIndex();
        String selected = encoder.getUnitOptions().get(i);
        System.out.println("selected " + selected + " " + i);
        //         System.out.println("selected " + selected);
    }

    @FXML
    private void handleType(Event e) {
        System.out.print("selected ");
        if (absolute.isSelected()) {
            
            System.out.println("absolute");
            encoder.getType().set("absolute");
            
        } else if (incremental.isSelected()) {
            
            System.out.println("incremental");
            encoder.getType().set("incremental");
            
        }
    }
    
    @FXML
    private void handleCoding(Event e) {
        int i = coding.getSelectionModel().getSelectedIndex();
        String selected = encoder.getCodingOptions().get(i);
        System.out.println("coding " + selected + " " + i);    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        
        encoder = new Encoder();
        
        outer.textProperty().bindBidirectional((Property) encoder.getOuterDiameter(), new IntegerStringConverter());
        inner.textProperty().bindBidirectional((Property) encoder.getInnerDiameter(), new IntegerStringConverter());
        center.textProperty().bindBidirectional((Property) encoder.getCenterDiameter(), new IntegerStringConverter());
        inverted.selectedProperty().bindBidirectional((Property) encoder.getInverted());
        units.getItems().addAll(encoder.getUnitOptions());
        coding.getItems().addAll(encoder.getCodingOptions());
        
        encoder.getUnits().addListener((observable, oldValue, newValue) -> {
            System.out.println("model change:" + newValue);
            units.selectionModelProperty().setValue(newValue);
        });
        units.getSelectionModel().selectFirst();

        encoder.getCoding().addListener((observable, oldValue, newValue) -> {
            System.out.println("model change:" + newValue);
            coding.selectionModelProperty().setValue(newValue);
        });
        coding.getSelectionModel().selectFirst();
        
        quadrature.selectedProperty().bindBidirectional((Property) encoder.getQuadratureTrack());
        index.selectedProperty().bindBidirectional((Property) encoder.getIndexTrack());
        
        // TODO - add TextFormatters and Input verifiers
    }    
     
}
