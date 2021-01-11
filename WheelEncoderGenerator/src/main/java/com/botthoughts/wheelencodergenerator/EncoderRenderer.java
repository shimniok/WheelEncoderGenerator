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

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 *
 * @author mes
 */
public class EncoderRenderer {
    private GraphicsContext gc;
    
    /**
     * Draws a single track with specified number of stripes at specified start angle.
     * This method can draw encoder tracks for absolute or standard/incremental encoders,
     * or can an index track with a single stripe. Or whatever else you want.
     * 
     * @param offset represents the upper left of the bounding rectangle for the track circle
     * @param diameter represents the width/height of the track circle
     * @param stripeCount number of black stripes to draw     
     * @param startAngle is the angle to start drawing the first stripe in degrees
     * @param sweepAngle is the angular width of each stripe
     */
    private void drawTrack(double offset, double diameter, int stripeCount, double startAngle, double sweepAngle) {
        // Draw circle with white background
        gc.setFill(Color.WHITE);
        gc.fillOval(offset, offset, diameter, diameter);
        // Draw the stripes for the track
        gc.setFill(Color.BLACK);
        for (int s=0; s < stripeCount; s += 2) {
            gc.fillArc(offset, offset, diameter, diameter, startAngle+s*sweepAngle, sweepAngle, ArcType.ROUND);
        }
        // Draw outside stroke
        gc.setStroke(Color.BLACK);
        gc.strokeOval(offset, offset, diameter, diameter);
        // Draw inner filled circle
        
    }
    
    private void drawBinaryEncoder(double offset, double outerDiameter, double innerDiameter, int bits) {
        // TODO - reverse bit pattern
        double tw = 0.5 * (outerDiameter - innerDiameter) / (bits-1);
        double o = offset;
        double d = outerDiameter;
        int res;
        double sweep; 
        double start = 90;
        for (int t = bits-1; t > 0; t--) {
            res = 1<<t;
            sweep = 360.0/res; // TODO - reverse rotation + is ccw, - is cw
            this.drawTrack(o, d, res, start, sweep);
            o += tw;
            d -= 2*tw;
        }
    }
    
    
    private void drawGrayEncoder(double offset, double outerDiameter, double innerDiameter, int bits) {
        // TODO - reverse gray
        double tw = 0.5 * (outerDiameter - innerDiameter) / bits;
        double o = offset;
        double d = outerDiameter;
        int res;
        double sweep;
        double start;
        for (int t = bits-1; t >= 0; t--) {
            if (t == 0) {
                res = 2;
                start = 90;
                sweep = 180;
            } else {
                res = 1<<t;
                sweep = 360.0/res;
                start = 90 - sweep/2;
            }
            this.drawTrack(o, d, res, start, sweep);
            o += tw;
            d -= 2*tw;
        }
    }
    
        
    private void drawIncrementalEncoder(double offset, double outerDiameter, double innerDiameter, 
        int resolution, boolean index, boolean quadrature) {
        int trackCount = 1;
        if (index) trackCount++;
        if (quadrature) trackCount++;
        double trackWidth = 0.5 * (outerDiameter - innerDiameter) / trackCount;
        double stripeAngle = 360.0 / resolution;

        this.drawTrack(offset, outerDiameter, resolution, 90, stripeAngle);
        if (quadrature) this.drawTrack(offset + trackWidth, outerDiameter - 2*trackWidth, resolution, 90 - stripeAngle/2.0, stripeAngle);
        if (index) this.drawTrack(offset + trackWidth*2, outerDiameter - 4*trackWidth, 1, 90, stripeAngle);
    }
    
    public void drawEncoder() {
    
        /*
        if (type.equals(Encoder.INCREMENTAL)) {
            drawIncrementalEncoder(padding, outerDiameter, innerDiameter, resolution, 
                encoder.getIndexTrack().getValue(), encoder.getQuadratureTrack().getValue());
        } else if (type.equals(Encoder.GRAY)) {
            drawGrayEncoder(padding, outerDiameter, innerDiameter, resolution);
        } else if (type.equals(Encoder.GRAY)) {
            drawBinaryEncoder(padding, outerDiameter, innerDiameter, resolution);
        }
        */
        
    }
    
}
