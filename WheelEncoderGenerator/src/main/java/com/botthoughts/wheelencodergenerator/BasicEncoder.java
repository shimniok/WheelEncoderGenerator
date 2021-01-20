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
public class BasicEncoder implements EncoderInterface {

    protected List<EncoderTrack> tracks; // list of tracks
    protected static int RESOLUTION_MIN = 2;
    protected static int RESOLUTION_MAX = 2048;
    protected static int INCREMENT = 2;

    /**
     * Return list of tracks
     * @param id inside diameter
     * @param od outside diameter
     * @param resolution resolution of encoder
     * @param index
     * @return list of tracks
     */
    @Override
    public List<EncoderTrack> getTracks(double id, double od, int resolution, boolean index) {
        tracks = new ArrayList<>();
        
        EncoderTrack outer = new EncoderTrack(od, id, 0, 360.0/resolution, resolution);
//        EncoderTrack outer = new EncoderTrack(
//                ep.getOuterDiameter().getValue(),
//                ep.getInnerDiameter().getValue(), 
//                0, 360.0/ep.getResolution().getValue(), 
//                ep.getResolution().getValue());
        tracks.add(outer);
        
        return tracks;
    }

    /**
     * Determine if specified resolution is valid
     * @return true if valid, false otherwise
     */
    @Override
    public boolean validResolution(int resolution) {
        return RESOLUTION_MIN <= resolution && resolution <= RESOLUTION_MAX;
    }
    
    /**
     * Attempt to increment the supplied resolution by one step.
     * If the result is valid (see validResolution()) it its returned,
     * otherwise the original resolution is returned
     * 
     * @param resolution 
     * @return incremented resolution if valid, else original resolution
     */
//    public int incrementResolution(int resolution) {
//        if (validResolution(resolution+1)) {
//            return resolution+1;
//        }
//        return resolution;
//    }
    
    /**
     * Attempt to decrement the supplied resolution by one step.
     * If the result is valid (see validResolution()) it its returned,
     * otherwise the original resolution is returned
     * 
     * @return decremented resolution if valid, else original resolution
     * @param resolution 
     */
//    public int decrementResolution(int resolution) {
//        if (validResolution(resolution-1)) {
//            return resolution-1;
//        }
//        return resolution;
//    }

    @Override
    public int getMinResolution() {
        return RESOLUTION_MIN;
    }

    @Override
    public int getMaxResolution() {
        return RESOLUTION_MAX;
    }

    @Override
    public int getResolutionIncrement() {
        return INCREMENT;
    }

}
