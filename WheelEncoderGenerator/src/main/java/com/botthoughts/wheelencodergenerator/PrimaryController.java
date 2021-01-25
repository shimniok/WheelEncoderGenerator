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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

public class PrimaryController implements Initializable {

    EncoderProperties ep;
    BasicEncoder basicEncoder;
    QuadratureEncoder quadratureEncoder;
    BinaryEncoder binaryEncoder;
    GrayEncoder grayEncoder;
    EncoderInterface currentEncoder;
    EncoderRenderer renderer;
    EncoderRenderer preview;
    EncoderRenderer printview;
    
//    private Color bg; // background
//    private Color fg; // foreground

    @FXML
    ComboBox typeUI;
    @FXML
    Spinner resolutionUI;
    @FXML
    TextField outerUI;
    @FXML
    TextField innerUI;
    @FXML
    TextField centerUI;
    @FXML
    ComboBox unitsUI;
    @FXML
    ToggleGroup directionUI;
    @FXML
    ToggleButton cwUI;
    @FXML
    ToggleButton ccwUI;
    @FXML
    ToggleButton invertedUI;
    @FXML
    ToggleButton indexUI;
    @FXML
    AnchorPane canvasContainer;

    @FXML
    public void saveFileAs() {
    }

    @FXML
    public void saveFile() {
    }

    @FXML
    public void openFile() {
    }

    @FXML
    public void newFile() {
    }

    @FXML
    public void print(Event e) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("print.fxml"));
        Parent parent;
        Scene scene;
        Stage stage;

        System.out.println("print()");

        try {
            parent = fxmlLoader.load();
            PrintController pc = fxmlLoader.getController();
            //pc.setEncoderProperties(ep);
            stage = new Stage();
            scene = new Scene(parent);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException ex) {
            System.out.println("IOException in print(): "+ex);
            //ex.printStackTrace(); // TODO: error handling
        }

    }

    @FXML
    public void export() {

    }

    @FXML
    void onOpenDialog(ActionEvent event) throws IOException {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        basicEncoder = new BasicEncoder();
        binaryEncoder = new BinaryEncoder();
        quadratureEncoder = new QuadratureEncoder();
        grayEncoder = new GrayEncoder();
        ep = new EncoderProperties();
        renderer = new EncoderRenderer(ep);
        renderer.setWidth(500);
        renderer.setHeight(500);
        canvasContainer.getChildren().add(renderer);

        // TODO - convert type to property on eProperties
        typeUI.getItems().setAll(ep.getTypeOptions());
        typeUI.valueProperty().bindBidirectional(ep.getType());
//        typeUI.getSelectionModel().select(0);
        typeUI.valueProperty().addListener(renderer); // TODO - rendering breaks switching type

        resolutionUI.setValueFactory(new ResolutionValueFactory(ep.getEncoder(), ep.getResolution().get()));
        resolutionUI.getValueFactory().valueProperty().bindBidirectional(ep.getResolution());
        resolutionUI.getValueFactory().valueProperty().addListener(renderer);
        ep.getType().addListener((observable, oldvalue, newvalue) -> {
            ResolutionValueFactory vf = (ResolutionValueFactory) resolutionUI.getValueFactory();
            vf.setEncoder(ep.getEncoder());
            // TODO - fix invalid values upon type change
        });

        outerUI.textProperty().bindBidirectional((Property) ep.getOuterDiameter(),
                new DoubleStringConverter());
        outerUI.textProperty().addListener(renderer);

        innerUI.textProperty().bindBidirectional((Property) ep.getInnerDiameter(),
                new DoubleStringConverter());
        innerUI.textProperty().addListener(renderer);

        centerUI.textProperty().bindBidirectional((Property) ep.getCenterDiameter(),
                new DoubleStringConverter());
        centerUI.textProperty().addListener(renderer);

        unitsUI.getItems().addAll(ep.getUnitOptions());
        unitsUI.valueProperty().bindBidirectional(ep.getUnits());
        unitsUI.valueProperty().addListener(renderer);

        invertedUI.selectedProperty().bindBidirectional(ep.getInverted());
        invertedUI.selectedProperty().addListener(renderer);

        indexUI.selectedProperty().bindBidirectional(ep.getIndexTrack());
        indexUI.selectedProperty().addListener(renderer);

        // TODO - direction
        // directionUI
        cwUI.selectedProperty().bindBidirectional(ep.getDirection());
        cwUI.selectedProperty().addListener((observable, oldvalue, newvalue) -> {
            System.out.println("cwUI, newvalue: " + newvalue);
            ep.setDirection(cwUI.selectedProperty());
            ccwUI.selectedProperty().set(oldvalue); // make sure the other toggle toggles
        });

        renderer.drawEncoder();

        // TODO print
        // TODO input verification for all fields
        // TODO file save
        // TODO file save as
        // TODO file open
        // TODO file new
        // TODO file export
    }

    private Parent loadFXML(String print) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
