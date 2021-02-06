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

import javafx.print.Paper;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Render a preview of a JavaFX Paper to scale
 * @author mes
 */
public class PaperView {

    private final Canvas canvas;
    private final GraphicsContext gc;
    private double scale;
    private double maxWidthPixels;
    private double maxHeightPixels;

    public PaperView(Canvas c, double maxWidth, double maxHeight) {
        this.canvas = c;
        this.gc = canvas.getGraphicsContext2D();
        this.scale = 1.0;
        this.maxWidthPixels = maxWidth;
        this.maxHeightPixels = maxHeight;
    }

    public double getMaxWidth() {
        return maxWidthPixels;
    }

    public void setMaxWidth(double maxWidth) {
        this.maxWidthPixels = maxWidth;
    }

    public double getMaxHeight() {
        return maxHeightPixels;
    }

    public void setMaxHeight(double maxHeight) {
        this.maxHeightPixels = maxHeight;
    }

    public double getScalePixelsToPoints() {
        return this.scale;
    }
    
    public void render(Paper paper) {
        double w;
        double h;
        double pageWidthPoints;
        double pageHeightPoints;
        double scaleX;
        double scaleY;

        // Determine page dimensions
        pageWidthPoints = paper.getWidth();
        pageHeightPoints = paper.getHeight();
        System.out.println("pw="+pageWidthPoints+" ph="+pageHeightPoints);

        // Determine scaling to fit page preview into available space
        scaleX = maxWidthPixels / pageWidthPoints;
        scaleY = maxHeightPixels / pageHeightPoints;
        scale = Math.min(scaleX, scaleY);
        System.out.println("scale="+scale+" scaleX="+scaleX+" scaleY="+scaleY);

        w = scale * pageWidthPoints;
        h = scale * pageHeightPoints;
        System.out.println("w="+w+" h="+h);

        canvas.setWidth(w);
        canvas.setHeight(h);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
