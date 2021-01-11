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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 *
 * @author mes
 */
public class EncoderController implements Initializable {
    
    @FXML
    Canvas c;
    
    private GraphicsContext gc;
    
    private void drawStripe(Stripe stripe, double offset, double diameter) {
        gc.setFill(Color.BLACK);
        gc.fillArc(offset, offset, diameter, diameter, stripe.startAngle, stripe.sweepAngle, ArcType.ROUND);
    }
        
    private void drawTrack(Track track, double offset, double diameter) {
        gc.fillOval(offset, offset, diameter, diameter);
        for (int s=0; s < track.stripeCount; s++) {
            this.drawStripe(track.stripe, track.innerDiameter, track.outerDiameter);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {   
    }
    
}
