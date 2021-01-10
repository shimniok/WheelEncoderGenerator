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
    
    private static int diameter = 500;
    
        /* getOffset()
     *
     * Returns the offset in degrees of the current track
     */
    private double getTrackOffset(int whichTrack)
    {
        double offset = 0.0;

        // In all cases, offset is zero, except if the encoder is:
        // 1) of type absolute and encoding is Gray Code, in which case, offset
        // depends on which track we're talking about among other things
        // 2) the current track is the quadrature track
        // TODO: clockwise vs counter clockwise
        /*
        if (type == WheelEncoder.ABSOLUTE && numbering == WheelEncoder.GRAY) {
            if (whichTrack == resolution-1)
                offset = 0; //-getDegree(whichTrack, 0); // CCW: 0, CW: -getDegree(whichTrack, 0)
            else
                offset = -getDegree(whichTrack, 0)/2; // CW: - CCW: +
        } else if (type == WheelEncoder.STANDARD) {
            if (whichTrack == getQuadratureTrack()) {
                offset = getDegree(whichTrack, 0)/2;
            }
        }
        */
        //Debug.println("track="+whichTrack);
        //Debug.println("offset="+offset);
        return offset;
    }
    
    public void drawEncoder(GraphicsContext gc) {
        double maxDiameter = Math.min(canvas.getWidth(), canvas.getHeight());
        double centerX = canvas.getWidth() / 2.0;
        double centerY = canvas.getHeight() / 2.0;
        
        Integer id = encoder.getInnerDiameter().getValue();
        Integer od = encoder.getOuterDiameter().getValue();
        Integer cd = encoder.getCenterDiameter().getValue();
        
        double ioRatio = (double) id / (double) od; // ratio of inner to outer diameter
        double innerDiam = this.diameter * (double) id / (double) od;
        double trackWidth = (this.diameter - innerDiam) / 2.0;
        double ctrRatio = (double) cd / (double) od;
        double ctrDiam = this.diameter * ctrRatio;
        double ctrWidth = (this.diameter - ctrDiam) / 2.0;
        double degree = 0;
        double offset = 0;
        int maxTrack = 0;
        Color color;

        // Background
        /*
        if (background != null) {
            g2D.setColor(background);
            g2D.fillRect(0, 0, (int) Math.round(width), (int) Math.round(height));
        }*/

        maxTrack = encoder.getTrackCount();

        /*
        for (int track = 0; track < maxTrack; track++) {
            offset = e.getOffset(track);
            double dA = innerDiam + (maxTrack-track) * (diameter - innerDiam - 1) / maxTrack;
            double xA = x + track * trackWidth / maxTrack;
            double yA = y + track * trackWidth / maxTrack;

            if (e.isInverted())
                color = Color.white;
            else
                color = Color.black;
            int stripe = 0;
            for (double i=offset; i < (360.0+offset); i += degree) {
                degree = e.getDegree(track, stripe++);
                g2D.setColor( color );
                g2D.fill( new Arc2D.Double(xA, yA, dA, dA, i, degree, Arc2D.PIE) );
                if (color == Color.white)
                    color = Color.black;
                else
                    color = Color.white;
            }
            g2D.setColor(Color.black);
            g2D.drawOval((int) Math.round(xA), (int) Math.round(yA), (int) Math.round(dA), (int) Math.round(dA));
        }
        */
        
        // Draw inner circle
        gc.strokeOval(0, 0, maxDiameter, maxDiameter);
        
        /*
        gc.setFill(Color.WHITE);
        gc.fillOval((int) Math.round(x+trackWidth), (int) Math.round(y+trackWidth), (int) Math.round(innerDiam), (int) Math.round(innerDiam));
        gc.setFill(Color.BLACK);
        gc.drawOval((int) Math.round(x+trackWidth), (int) Math.round(y+trackWidth), (int) Math.round(innerDiam), (int) Math.round(innerDiam));
        // Draw center circle
        gc.drawOval((int) Math.round(x+ctrWidth), (int) Math.round(y+ctrWidth), (int) Math.round(ctrDiam), (int) Math.round(ctrDiam));
        // Draw crosshairs
        gc.drawLine((int) Math.round(x+trackWidth), (int) Math.round(y+diameter/2), (int) Math.round(x+(diameter+innerDiam)/2), (int) Math.round(y+diameter/2));
        gc.drawLine((int) Math.round(x+diameter/2), (int) Math.round(y+trackWidth), (int) Math.round(x+diameter/2), (int) Math.round(y+(diameter+innerDiam)/2));        
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
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        this.drawEncoder(gc);
        
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
