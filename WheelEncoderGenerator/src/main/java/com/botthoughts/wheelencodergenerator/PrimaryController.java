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
import java.util.List;
import java.util.ResourceBundle;
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
    
    BasicEncoder encoder;
    
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

    /**
     * Draws a single track with specified number of stripes at specified start angle.
     * This method can draw encoder tracks for absolute or standard/incremental encoders,
     * or can an index track with a single stripe. Or whatever else you want.
     * 
     * @param x represents the left of the bounding rectangle for the track circle
     * @param y represents the upper limit of the bounding rectangle for the track circle
     * @param outer represents the width/height of the outside track circle
     * @param inner represents the width/height of the inside of track circle
     * @param stripeCount number of stripes to draw     
     * @param startAngle is the angle to start drawing the first stripe in degrees
     * @param sweepAngle is the angular width of each stripe
     */
    private void drawTrack(double x, double y, double outer, 
            double inner, int stripeCount, double startAngle, 
            double sweepAngle) {
        // Draw circle with white background
        gc.setFill(Color.WHITE);
        gc.fillOval(x, y, outer, outer);
        // Draw the stripes for the track
        gc.setFill(Color.BLACK);
        for (int s=0; s < stripeCount; s += 2) {
            gc.fillArc(x, y, outer, outer, startAngle+s*sweepAngle, 
                    sweepAngle, ArcType.ROUND);
        }
        // Draw outside stroke
        gc.setStroke(Color.BLACK);
        gc.strokeOval(x, y, outer, outer);
        // Draw inner filled circle
        double trackWidth = (outer - inner)/2;
        gc.setFill(Color.WHITE);
        gc.fillOval(x + trackWidth, y + trackWidth, inner, inner);
        gc.strokeOval(x + trackWidth, y + trackWidth, inner, inner);
        
    }

    
    public void drawEncoder() {
        // Encoder measurements
        Double id = encoder.getInnerDiameter().getValue();
        Double od = encoder.getOuterDiameter().getValue();
        Double cd = encoder.getCenterDiameter().getValue();
        
        // Real Pixels & Scaling
        double padding = 10.0; // TODO - configurable?
        double centerX = canvas.getWidth() / 2;
        double centerY = canvas.getHeight() / 2;
        double outerDiameterPx = Math.min(canvas.getWidth(), canvas.getHeight()) - 2*padding;
        double scale = outerDiameterPx / od; // scaling factor: px / units
        double innerDiameterPx = id * scale;
        double centerDiameterPx = cd * scale;
        
        // TODO - real lines separating each track
        
        // INCREMENTAL

        for (EncoderTrack t : encoder.getTracks()) {
            double outerPx = t.outerDiameter * scale;
            double innerPx = t.innerDiameter * scale;            
            double offsetX = centerX - outerPx/2;
            double offsetY = centerY - outerPx/2;
            
            System.out.println("o=" + t.outerDiameter + " i=" + t.innerDiameter
                    + " a=" + t.stripeAngle + " c=" + t.stripeCount);
            
            this.drawTrack(offsetX, offsetY, outerPx, innerPx, 
                    t.stripeCount, t.startAngle, t.stripeAngle);
        }
        
        // Draw center circle diameter
        gc.setFill(Color.WHITE);
        double x = centerX - centerDiameterPx/2;
        double y = centerY - centerDiameterPx/2;
        gc.fillOval(x, y, centerDiameterPx, centerDiameterPx);
        gc.strokeOval(x, y, centerDiameterPx, centerDiameterPx);
        
        // Draw crosshairs
        double x1 = x + centerDiameterPx/2.0;
        double y1 = y;
        double x2 = x1;
        double y2 = y1 + centerDiameterPx;
        gc.setStroke(Color.DARKGREY);
        gc.strokeLine(x1, y1, x2, y2); // draw vertical line
        gc.strokeLine(y1, x1, y2, x2); // draw the horizontal line

    }
    
    @FXML
    private void handleUnits(ActionEvent e) {
        //int i = units.getSelectionModel().getSelectedIndex();
        //String selected = encoder.getUnitOptions().get(i);
    }

    @FXML
    private void handleType(Event e) {
        /*
        if (absolute.isSelected()) {
            encoder.getType().set("absolute");
        } else if (incremental.isSelected()) {
            encoder.getType().set("incremental");
        }
        */
    }
    
    @FXML
    private void handleCoding(Event e) {
        //int i = coding.getSelectionModel().getSelectedIndex();
        //String selected = encoder.getCodingOptions().get(i);
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
        
        //encoder = new BasicEncoder();
        //encoder = new BinaryEncoder();
        encoder = new GrayEncoder();

        /*
        outer.textProperty().bindBidirectional((Property) encoder.getOuterDiameter(), new IntegerStringConverter());
        inner.textProperty().bindBidirectional((Property) encoder.getInnerDiameter(), new IntegerStringConverter());
        center.textProperty().bindBidirectional((Property) encoder.getCenterDiameter(), new IntegerStringConverter());
        inverted.selectedProperty().bindBidirectional((Property) encoder.getInverted());
        units.getItems().addAll(encoder.getUnitOptions());
        //coding.getItems().addAll(encoder.getCodingOptions());

        encoder.getUnits().addListener((observable, oldValue, newValue) -> {
            units.selectionModelProperty().setValue(newValue);
        });
        units.getSelectionModel().selectFirst();
        */
        
        /* absolute encoder */
        /*
        encoder.getCoding().addListener((observable, oldValue, newValue) -> {
            coding.selectionModelProperty().setValue(newValue);
        });
        coding.getSelectionModel().selectFirst();
        */
        //absResolution.setValueFactory(new Base2SpinnerValueFactory(1, 16, 4));
        
        /* incremental encoder */
        //quadrature.selectedProperty().bindBidirectional((Property) encoder.getQuadratureTrack());
        //index.selectedProperty().bindBidirectional((Property) encoder.getIndexTrack());
        //incResolution.setValueFactory(new IntegerSpinnerValueFactory(2, 2048, 16, 1));
        
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
