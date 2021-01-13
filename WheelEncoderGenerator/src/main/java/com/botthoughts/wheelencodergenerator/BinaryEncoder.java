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
public class BinaryEncoder extends EncoderTracks {

    /**
     * Check validity of resolution, which for this type is # bits.
     * @param resolution
     * @return true if valid resolution.
     */
    @Override
    public boolean validResolution(int resolution) {
        return resolution >= 1; // minimum resolution is 1 bit
    }
    
    /**
     * Return list of ordered Track objects for this encoder.
     * Track information is computed on-demand
     * @return list of tracks
     */
    @Override
    public List<EncoderTrack> getTracks() {
        tracks = new ArrayList<>();

        double outer = ep.getOuterDiameter().getValue();
        double inner = ep.getInnerDiameter().getValue();
        
        double trackWidth = (outer - inner)/ep.getResolution().getValue();

        inner = outer - trackWidth;
        for (int b = ep.getResolution().getValue(); b > 0; b--) {
            int res = 1<<b;
            double angle = 360.0/res;

            tracks.add(new EncoderTrack(outer, inner, 0, angle, res));
            outer -= trackWidth;
            inner -= trackWidth;
        }
        
        return tracks;
    }
    
    public BinaryEncoder(EncoderProperties ep) {
        super(ep);
    }
}
