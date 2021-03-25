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

import com.botthoughts.util.ResizeableCanvas;
import com.botthoughts.wheelencodergenerator.model.EncoderModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * EncoderView renders an encoder on a canvas based on encoder properties
 *
 * @author mes
 */
public final class EncoderView extends ResizeableCanvas {

  public static int PADDING = 2; // in pixels, each side
  public static int FIT_TO_CANVAS = 0;
  private GraphicsContext gc;
  private Color backgroundColor; // background
  private Color foregroundColor; // foreground
  private double scale;
  private boolean fit; // fit render to canvas?
  private EncoderProperties ep;
  private boolean redraw;

  /**
   * Create a new object to render encoder
   *
   * @param scale scaling factor for drawing; 0 means fit to canvas
   * @param ep
   * @param width
   * @param height
   */
  public EncoderView(double scale, EncoderProperties ep, Double width, Double height) {
    this.setScale(scale);
    this.gc = this.getGraphicsContext2D();
    this.setEffect(null);
    this.ep = ep;
    this.setWidth(width);
    this.setHeight(height);
    this.redraw = true;
    this.widthProperty().addListener(e -> this.redraw = true);
    this.heightProperty().addListener(e -> this.redraw = true);
  }

  // TODO: documentation
  public EncoderView(EncoderProperties ep, double width, double height) {
    this(0, ep, width, height);
  }
  
  
  /**
   * Set the scaling factor for drawing
   *
   * @param scale scaling factor for drawing; 0 means fit to canvas
   */
  public void setScale(double scale) {
    if (scale == FIT_TO_CANVAS) {
      this.scale = 1.0;
      this.fit = true;
    } else {
      this.scale = scale;
      this.fit = false;
    }
  }

  public double getScale() {
    return this.scale;
  }

  private void adjustScaleToFit() {
    double od = ep.outerDiameterProperty().get();
    double width;
    double height;
    
    width = this.getWidth() - 2*PADDING;
    height = this.getHeight() - 2*PADDING;
    this.scale = Math.min(width / od, height / od);
  }

  /**
   * Draws a single track with specified number of stripes at specified start angle. This method
   * can draw encoder tracks for absolute or standard/incremental encoders, or can an index track
   * with a single stripe. Or whatever else you want.
   *
   * @param x1 represents the left of the bounding rectangle for the track circle
   * @param y1 represents the upper limit of the bounding rectangle for the track circle
   * @param t is the EncoderTrack which contains all info to draw the track
   */
  private void drawTrack(double x1, double y1, EncoderTrack t) {
    double od = t.outerDiameter * scale;
    double id = t.innerDiameter * scale;
    
    gc.setFill(backgroundColor);
    gc.fillOval(x1, y1, od, od);
    
    // Draw the stripes for the track
    gc.setFill(foregroundColor);
    for (int s = 0; s < t.stripeCount; s += 2) {
      gc.fillArc(x1, y1, od, od,
          t.startAngle + s * t.stripeAngle, 
          t.stripeAngle, ArcType.ROUND);
    }

    // Draw outside stroke
    gc.setStroke(foregroundColor);
    gc.strokeOval(x1, y1, od, od);

    // Draw inner circle
    double trackWidth = (t.outerDiameter - t.innerDiameter) * scale / 2;
    gc.setFill(backgroundColor);
    gc.fillOval(x1 + trackWidth, y1 + trackWidth, id, id);
    gc.strokeOval(x1 + trackWidth, y1 + trackWidth, id, id);
  }

  /**
   * Render the encoder on our canvas
   */
  @Override
  public void draw() {
    EncoderModel enc = ep.getEncoder();

//    System.out.println("render()");

    if (!ep.isValid()) {
      return;
    }

    // Set foreground and background colors
    if (ep.invertedProperty().get()) {
      foregroundColor = Color.WHITE;
      backgroundColor = Color.BLACK;
    } else {
      foregroundColor = Color.BLACK;
      backgroundColor = Color.WHITE;
    }

    if (this.fit) {
      this.adjustScaleToFit();
    }

    if (redraw) {
      gc.clearRect(0, 0, getWidth(), getHeight());
    }
    
    double centerX = getWidth()/2.0;//ep.outerDiameterProperty().get() * scale / 2.0;
    double centerY = getHeight()/2.0;//centerX;
    System.out.println("canvas: x="+getWidth()+"y="+getHeight()+" cX="+centerX+" cY="+centerY);

    if (enc != null) {
      ep.getTracks().forEach(t -> {
        double od = t.outerDiameter * scale;
        double x1 = centerX - od / 2;
        double y1 = centerY - od / 2;
        this.drawTrack(x1, y1, t);
      });
    }

    // Draw center circle and crosshairs
    gc.setFill(Color.DARKGRAY);
    double cd = ep.centerDiameterProperty().get() * scale;
    double x = centerX - cd / 2;
    double y = centerY - cd / 2;
    gc.fillOval(x, y, cd, cd);
    gc.strokeOval(x, y, cd, cd);
    gc.setStroke(Color.BLACK);
    gc.strokeLine(centerX, y, centerX, y + cd); // draw vertical line
    gc.strokeLine(x, centerY, x + cd, centerY); // draw the horizontal line

  }


}
