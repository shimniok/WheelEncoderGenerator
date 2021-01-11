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

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author mes
 */
public class GrayEncoder extends BinaryEncoder {

    /**
     * Return list of ordered Track objects for this encoder.
     * Track information is computed on-demand
     * @return list of tracks
     */
    @Override
    public List<EncoderTrack> getTracks() {
        tracks = new ArrayList<>();

        double outer = outerDiameter.getValue();
        double inner = innerDiameter.getValue();
        double trackWidth = (outer - inner)/getTrackCount();
        double angle = 0;
        double start = 0;
        int res = 0;
        

        inner = outer - trackWidth;
        for (int b = resolution.getValue()-1; b >= 0; b--) {
            if (b > 0) {
                res = 1<<b;
                angle = 360.0/res;
                start = angle/2;
            } else {
                start = 0;
            }

            tracks.add(new EncoderTrack(outer, inner, start, angle, res));
            outer -= trackWidth;
            inner -= trackWidth;            
        }
        
        return tracks;
    }
    
    public GrayEncoder() {
        this.resolution = new SimpleIntegerProperty(5);
    }
    
}
