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

/**
 *
 * @author mes
 */
public class EncoderTrack {
    public double outerDiameter;
    public double innerDiameter;
    public double startAngle;
    public double stripeAngle;
    public int stripeCount;

    /**
     * 
     * @param outerDiameter outer diameter (all units managed externally)
     * @param innerDiameter inner diameter
     * @param startAngle starting angle for first stripe in degrees
     * @param stripeSweepAngle angle of each white and black stripe
     * @param stripeCount number of black stripes
     */
    public EncoderTrack(double outerDiameter, double innerDiameter, double startAngle, 
            double stripeSweepAngle, int stripeCount) {
        this.outerDiameter = outerDiameter;
        this.innerDiameter = innerDiameter;
        this.startAngle = startAngle;
        this.stripeAngle = stripeSweepAngle;
        this.stripeCount = stripeCount;
    }

}
