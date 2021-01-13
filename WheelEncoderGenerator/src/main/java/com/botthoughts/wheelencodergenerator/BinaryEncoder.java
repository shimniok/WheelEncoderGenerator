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

/**
 *
 * @author mes
 */
public class BinaryEncoder extends BasicEncoder {

    protected List<EncoderTrack> tracks; // list of tracks

    private static final int RESOLUTION_MAX = 11; /* 2^11 = 2048 */
    private static final int RESOLUTION_MIN = 2;

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
     * Tracks are computed on-demand based on current properties
     * @param id Inside diameter
     * @param od Outside diameter
     * @return list of tracks
     */
    @Override
    public List<EncoderTrack> getTracks(double id, double od, int resolution, boolean index) {
        tracks = new ArrayList<>();

        double trackWidth = (od - id)/resolution;

        id = od - trackWidth;
        for (int b = resolution; b > 0; b--) {
            int res = 1 << b;
            double angle = 360.0/res;

            tracks.add(new EncoderTrack(od, id, 0, angle, res));
            od -= trackWidth;
            id -= trackWidth;
        }
        
        return tracks;
    }
    
    /**
     * Attempt to increment the supplied resolution by one step.
     * If the result is valid (see validResolution()) it its returned,
     * otherwise the original resolution is returned
     * 
     * @param resolution 
     * @return incremented resolution if valid, else original resolution
     */
    @Override
    public int incrementResolution(int resolution) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Attempt to decrement the supplied resolution by one step.
     * If the result is valid (see validResolution()) it its returned,
     * otherwise the original resolution is returned
     * 
     * @return decremented resolution if valid, else original resolution
     * @param resolution 
     */
    @Override
    public int decrementResolution(int resolution) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
