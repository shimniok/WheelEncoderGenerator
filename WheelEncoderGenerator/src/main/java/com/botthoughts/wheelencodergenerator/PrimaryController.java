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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.util.converter.DoubleStringConverter;


public class PrimaryController implements Initializable {
    
    EncoderProperties ep;
    BasicEncoder basicEncoder;
    QuadratureEncoder quadratureEncoder;
    BinaryEncoder binaryEncoder;
    GrayEncoder grayEncoder;
    EncoderInterface currentEncoder;
    EncoderRenderer renderer;
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
    
//    private GraphicsContext gc; 

//    /**
//     * Draws a single track with specified number of stripes at specified start angle.
//     * This method can draw encoder tracks for absolute or standard/incremental encoders,
//     * or can an index track with a single stripe. Or whatever else you want.
//     * 
//     * @param x represents the left of the bounding rectangle for the track circle
//     * @param y represents the upper limit of the bounding rectangle for the track circle
//     * @param outer represents the width/height of the outside track circle
//     * @param inner represents the width/height of the inside of track circle
//     * @param stripeCount number of stripes to draw     
//     * @param startAngle is the angle to start drawing the first stripe in degrees
//     * @param sweepAngle is the angular width of each stripe
//     */
//    private void drawTrack(double x, double y, double outer, 
//            double inner, int stripeCount, double startAngle, 
//            double sweepAngle) {
//        // Draw circle with white background
//        gc.setFill(bg);
//        gc.fillOval(x, y, outer, outer);
//        // Draw the stripes for the track
//        gc.setFill(fg);
//        for (int s=0; s < stripeCount; s += 2) {
//            gc.fillArc(x, y, outer, outer, startAngle+s*sweepAngle, 
//                    sweepAngle, ArcType.ROUND);
//        }
//        // Draw outside stroke
//        gc.setStroke(fg);
//        gc.strokeOval(x, y, outer, outer);
//        // Draw inner filled circle
//        double trackWidth = (outer - inner)/2;
//        gc.setFill(bg);
//        gc.fillOval(x + trackWidth, y + trackWidth, inner, inner);
//        gc.strokeOval(x + trackWidth, y + trackWidth, inner, inner);
//    }
//
//    
//    public void drawEncoder() {
//        // Encoder measurements
//        Double id = ep.getInnerDiameter().getValue();
//        Double od = ep.getOuterDiameter().getValue();
//        Double cd = ep.getCenterDiameter().getValue();
//        Boolean index = ep.getIndexTrack().getValue();
//        Integer res = ep.getResolution().getValue();
//        Boolean inverted = ep.getInverted().getValue();
//        
//        // Real Pixels & Scaling
//        double padding = 10.0; // TODO - configurable?
//        double centerX = canvas.getWidth() / 2;
//        double centerY = canvas.getHeight() / 2;
//        double canvasPx = Math.min(canvas.getWidth(), canvas.getHeight()); // canvas min width
//        double scale = (canvasPx-2*padding)/ od; // scaling factor: px / units
//        double cdPx = cd * scale; // scaled center diameter
//        
//        // TODO - real lines separating each track
//        
//        EncoderInterface enc = ep.getEncoder();
//        
//        if (inverted) {
//            fg = Color.WHITE;
//            bg = Color.BLACK;
//        } else {
//            fg = Color.BLACK;
//            bg = Color.WHITE;
//        }
//        
//        if (enc != null) {
//            for (EncoderTrack t : enc.getTracks(id, od, res, index)) {
//                double outerPx = t.outerDiameter * scale;
//                double innerPx = t.innerDiameter * scale;            
//                double offsetX = centerX - outerPx/2;
//                double offsetY = centerY - outerPx/2;
//
//                System.out.println("o=" + t.outerDiameter + " i=" + t.innerDiameter
//                        + " a=" + t.stripeAngle + " c=" + t.stripeCount + " index=" + 
//                        index.toString() + " inverted=" + inverted.toString());
//
//                this.drawTrack(offsetX, offsetY, outerPx, innerPx, 
//                        t.stripeCount, t.startAngle, t.stripeAngle);
//            }
//        }
//
//        // Draw center circle diameter
//        gc.setFill(Color.DARKGRAY);
//        double x = centerX - cdPx/2;
//        double y = centerY - cdPx/2;
//        gc.fillOval(x, y, cdPx, cdPx);
//        gc.strokeOval(x, y, cdPx, cdPx);
//        
//        // Draw crosshairs
//        double x1 = x + cdPx/2.0;
//        double y1 = y;
//        double x2 = x1;
//        double y2 = y1 + cdPx;
//        gc.setStroke(Color.BLACK);
//        gc.strokeLine(x1, y1, x2, y2); // draw vertical line
//        gc.strokeLine(y1, x1, y2, x2); // draw the horizontal line
//
//    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
        
        basicEncoder = new BasicEncoder();
        binaryEncoder = new BinaryEncoder();
        quadratureEncoder = new QuadratureEncoder();
        grayEncoder = new GrayEncoder();
        ep = new EncoderProperties(quadratureEncoder);
        renderer = new EncoderRenderer(ep);
        renderer.setWidth(500);
        renderer.setHeight(500);

        canvasContainer.getChildren().add(renderer);
//        
//        ChangeListener cl = (observable, oldValue, newValue) -> {
//            renderer.drawEncoder();
//        };
        
        // TODO - convert type to property on eProperties
        typeUI.getItems().setAll(ep.getTypeOptions());
        typeUI.valueProperty().bindBidirectional(ep.getType());
        typeUI.getSelectionModel().select(0);
        typeUI.valueProperty().addListener(renderer);
        
        // TODO - prevent odd numbers
        resolutionUI.setValueFactory(new IntegerSpinnerValueFactory(
            ep.getEncoder().getMinResolution(),
            ep.getEncoder().getMaxResolution(),
            ep.getResolution().getValue(),
            ep.getEncoder().getResolutionIncrement()
        ));
        resolutionUI.getValueFactory().valueProperty().bindBidirectional(ep.getResolution());
        resolutionUI.getValueFactory().valueProperty().addListener(renderer);

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
//            ep.setDirection(cwUI.selectedProperty());
            ccwUI.selectedProperty().set(oldvalue); // make sure the other toggle toggles
        });
//        ep.getDirection().addListener((observable, oldvalue, newvalue) -> {
//            System.out.println("direction, newvalue: " + newvalue);
//            cwUI.selectedProperty().set(newvalue);
//        });
//        cwUI.selectedProperty().set(ep.getDirection().get());

//        directionUI..selectedToggleProperty().addListener(cl);
        
//        .addListener((obs, wasSelected, isNowSelected) -> {
//            
//        });

//        this.gc = canvas.getGraphicsContext2D();

        renderer.drawEncoder();
        
        // TODO print
        // TODO input verification for all fields
        // TODO file save
        // TODO file save as
        // TODO file open
        // TODO file new
        // TODO file export
    }    
     
}
