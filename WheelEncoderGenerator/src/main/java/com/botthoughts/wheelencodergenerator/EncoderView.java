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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * EncoderView renders an encoder on a canvas based on encoder properties
 * @author mes
 */
public class EncoderView implements ChangeListener {

    private Canvas canvas;
    private GraphicsContext gc;
    private Color bg; // background
    private Color fg; // foreground
    private double scale;

    /**
     * Create renderer node 
     */
    EncoderView(Canvas c) {
        this.canvas = c;
        this.gc = c.getGraphicsContext2D();
    }
    
    /**
     * Create renderer node with specified width and height
     * @param width width of node
     * @param height height of node
     */
//    EncoderView(double width, double height) {
//        this.setWidth(width);
//        this.setHeight(height);
//    }

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
        gc.setFill(bg);
        gc.fillOval(x, y, outer, outer);
        // Draw the stripes for the track
        gc.setFill(fg);
        for (int s=0; s < stripeCount; s += 2) {
            gc.fillArc(x, y, outer, outer, startAngle+s*sweepAngle, 
                    sweepAngle, ArcType.ROUND);
        }
        // Draw outside stroke
        gc.setStroke(fg);
        gc.strokeOval(x, y, outer, outer);
        // Draw inner filled circle
        double trackWidth = (outer - inner)/2;
        gc.setFill(bg);
        gc.fillOval(x + trackWidth, y + trackWidth, inner, inner);
        gc.strokeOval(x + trackWidth, y + trackWidth, inner, inner);
    }
    
    
    private void drawTrack(double x1, double y1, EncoderTrack t) {
        double od = t.outerDiameter*scale;
        double id = t.innerDiameter*scale;
        
        // Draw circle with white background
        gc.setFill(bg);
        gc.fillOval(x1, y1, od, od);
        // Draw the stripes for the track
        gc.setFill(fg);
        for (int s=0; s < t.stripeCount; s += 2) {
            gc.fillArc(x1, y1, 
                    od, od, 
                    t.startAngle+s*t.stripeAngle, 
                    t.stripeAngle, ArcType.ROUND);
        }
        // Draw outside stroke
        gc.setStroke(fg);
        gc.strokeOval(x1, y1, od, od);
        // Draw inner filled circle
        double trackWidth = (t.outerDiameter - t.innerDiameter)*scale/2;
        gc.setFill(bg);
        gc.fillOval(x1 + trackWidth, y1 + trackWidth, id, id);
        gc.strokeOval(x1 + trackWidth, y1 + trackWidth, id, id);
    }
    
    
    public void setScale(double scale) {
        this.scale = scale;
    }

    public void render() {
        EncoderProperties ep = EncoderProperties.getInstance();
        EncoderModel enc = ep.getEncoder();
        double widthPx = gc.getCanvas().getWidth();
        double heightPx = gc.getCanvas().getHeight();
        double centerX = widthPx/2;
        double centerY = heightPx/2;
        
        if (ep.getInverted().get()) {
            fg = Color.WHITE;
            bg = Color.BLACK;
        } else {
            fg = Color.BLACK;
            bg = Color.WHITE;
        }
        
        if (enc != null) {
            ep.getTracks().forEach(t -> {
                double od = t.outerDiameter*scale;
                double x1 = centerX - od/2;
                double y1 = centerY - od/2;
//                System.out.println("o=" + t.outerDiameter + " i=" + t.innerDiameter
//                        + " a=" + t.stripeAngle + " c=" + t.stripeCount);
                this.drawTrack(x1, y1, t);
            });
        }
        
        // Draw center circle diameter
//        gc.setFill(Color.DARKGRAY);
//        double x = centerX - ep.getCenterDiameter().get()/2;
//        double y = centerY - ep.getCenterDiameter().get()/2;
//        gc.fillOval(x, y, ep.getCenterDiameter().get(), ep.getCenterDiameter().get());
//        gc.strokeOval(x, y, ep.getCenterDiameter().get(), ep.getCenterDiameter().get());
        
        // Draw crosshairs
//        double x1 = x + ep.getCenterDiameter().get()/2.0;
//        double y1 = y;
//        double x2 = x1;
//        double y2 = y1 + ep.getCenterDiameter().get();
//        gc.setStroke(Color.BLACK);
//        gc.strokeLine(x1, y1, x2, y2); // draw vertical line
//        gc.strokeLine(y1, x1, y2, x2); // draw the horizontal line
    }
    
    
//    public void render() {
//        // Encoder measurements
//        Double id; // inside diameter
//        Double od; // outside diameter
//        Double cd; // center diameter
//        Boolean cw; // clockwise direction?
//        Integer res; // resolution
//        Boolean index; // index track present?
//        Boolean inverted; // invert image?
//
//        EncoderProperties ep = EncoderProperties.getInstance();
//       
//        id = ep.getInnerDiameter().getValue();
//        od = ep.getOuterDiameter().getValue();
//        cd = ep.getCenterDiameter().getValue();
//        cw = ep.getDirection().getValue();
//        res = ep.getResolution().getValue();
//        index = ep.getIndexTrack().getValue();
//        inverted = ep.getInverted().getValue();
//        
//        // Real Pixels & Scaling
//        //double padding = 10.0; // TODO - configurable?
//        double centerX = canvas.getWidth() / 2;
//        double centerY = canvas.getHeight() / 2;
//        double canvasPx = Math.min(canvas.getWidth(), canvas.getHeight()); // canvas min width
//        double max = Math.max(od, Math.max(id, cd)); // get max dimension (in case of bad input)
//        double scale = (canvasPx)/max; // scaling factor: px / units
//        double cdPx = cd * scale; // scaled center diameter
//        
//        // TODO - real lines separating each track
//        
//        EncoderModel enc = ep.getEncoder();
//        
//        if (id >= od || cd >= id) return;
//        
//        if (inverted) {
//            fg = Color.WHITE;
//            bg = Color.BLACK;
//        } else {
//            fg = Color.BLACK;
//            bg = Color.WHITE;
//        }
//        
//        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
//
//        if (enc != null) {
//            enc.getTracks(id, od, res, index).forEach(t -> {
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
//            });
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
//        // Draw scale markings       
////        x1 = padding;
////        y1 = padding;
////        gc.strokeRect(x1, y1, scale, scale);
////        gc.strokeText("1x1"+ep.getUnits().get(), x1+scale*1.5, y1+scale/2);
//    }

    @Override
    public void changed(ObservableValue ov, Object t, Object t1) {
        System.out.println("EncoderView changed()");
        //this.render();
    }
    
}
