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
import java.util.Set;
import javafx.beans.property.Property;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
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
    Canvas canvas;
    
    private GraphicsContext gc;
    
    private void drawTrack(double offset, double diameter, int resolution) {
        if (resolution >= 2) {
            double angle = 360.0 / resolution;
            gc.fillOval(offset, offset, diameter, diameter);
            for (int s=0; s < resolution; s++) {
                if (s % 2 == 0) {
                    gc.setFill(Color.WHITE);
                } else {
                    gc.setFill(Color.BLACK);
                }
                gc.fillArc(offset, offset, diameter, diameter, s*angle, angle, ArcType.ROUND);
            }
        }        
    }
    
    
    public void drawEncoder() {

        // Encoder measurements
        Integer id = encoder.getInnerDiameter().getValue();
        Integer od = encoder.getOuterDiameter().getValue();
        Integer cd = encoder.getCenterDiameter().getValue();
        
        // Real Pixels
        double padding = 10.0;
        double maxWidth = Math.min(canvas.getWidth(), canvas.getHeight());
        double outerDiameter = maxWidth - 2 * padding;
        double scale = outerDiameter / od; // scaling factor in pixes per encoder-unit-of-measure
        double innerDiameter = id * scale;
        double centerDiameter = cd * scale;
        double offset = padding; // initial circle offset is just padding

        // Track info
        int resolution = encoder.getIncrementalResolution().getValue();
        double stripeAngle = 360.0 / resolution;
        
        System.out.println("outerDiameter=" + outerDiameter);
        System.out.println("innerDiameter=" + innerDiameter);
        
        // Draw outer diameter circle
        gc.setStroke(Color.BLACK);
        gc.strokeOval(offset, padding, outerDiameter, outerDiameter);
        
        // Draw outer track
        this.drawTrack(offset, outerDiameter, resolution);
        
        /*
        // Draw inner diameter circle
        offset = padding + (outerDiameter - innerDiameter) / 2.0;
        gc.setFill(Color.WHITE);
        gc.fillOval(offset, offset, innerDiameter, innerDiameter);
        gc.strokeOval(offset, offset, innerDiameter, innerDiameter);
        
        // Draw center circle diameter
        offset = padding + (outerDiameter - centerDiameter) / 2.0;
        gc.setFill(Color.WHITE);
        gc.fillOval(offset, offset, centerDiameter, centerDiameter);
        gc.strokeOval(offset, offset, centerDiameter, centerDiameter);
        
        // Draw crosshairs
        double x1 = offset + centerDiameter/2.0;
        double y1 = offset;
        double x2 = x1;
        double y2 = y1 + centerDiameter;
        gc.setStroke(Color.DARKGREY);
        gc.strokeLine(x1, y1, x2, y2); // draw vertical line
        gc.strokeLine(y1, x1, y2, x2); // draw the horizontal line
        */
    }
    
    @FXML
    private void handleUnits(ActionEvent e) {
        int i = units.getSelectionModel().getSelectedIndex();
        String selected = encoder.getUnitOptions().get(i);
    }

    @FXML
    private void handleType(Event e) {
        if (absolute.isSelected()) {
            encoder.getType().set("absolute");
        } else if (incremental.isSelected()) {
            encoder.getType().set("incremental");
        }
    }
    
    @FXML
    private void handleCoding(Event e) {
        int i = coding.getSelectionModel().getSelectedIndex();
        String selected = encoder.getCodingOptions().get(i);
    }
 
    class Base2SpinnerValueFactory extends SpinnerValueFactory {
        private int compute(int n) {
            return (int) Math.pow(2, n);
        }

        private void set(int value) {
            if (this.min <= value && value <= this.max) {
                this.value = value;
                this.valueProperty().set(this.compute(this.value));
            }         
        }
         
        @Override
        public void increment(int steps) {
            set(this.value + steps);
        }
        
        @Override
        public void decrement(int steps) {
            set(this.value - steps);
        }
        
        private int value;
        private int max;
        private int min;

        public Base2SpinnerValueFactory(int min, int max, int initial) {
            if (this.min >= 0) this.min = min; // negative values don't make sense
            if (this.max >= 0) this.max = max;// negative values don't make sense
            this.set(initial);
        }
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
            units.selectionModelProperty().setValue(newValue);
        });
        units.getSelectionModel().selectFirst();

        /* absolute encoder */
        encoder.getCoding().addListener((observable, oldValue, newValue) -> {
            coding.selectionModelProperty().setValue(newValue);
        });
        coding.getSelectionModel().selectFirst();
        absResolution.setValueFactory(new Base2SpinnerValueFactory(1, 16, 4));
        
        /* incremental encoder */
        quadrature.selectedProperty().bindBidirectional((Property) encoder.getQuadratureTrack());
        index.selectedProperty().bindBidirectional((Property) encoder.getIndexTrack());
        incResolution.setValueFactory(new IntegerSpinnerValueFactory(2, 2048, 16, 1));
        
        this.gc = canvas.getGraphicsContext2D();
        this.drawEncoder();
        
        // TODO draw circle
        // TODO print
        // TODO input verification for all fields
        // TODO file save
        // TODO file save as
        // TODO file open
        // TODO file new
        // TODO file export
        // TODO toolbar icons
    }    
     
}
