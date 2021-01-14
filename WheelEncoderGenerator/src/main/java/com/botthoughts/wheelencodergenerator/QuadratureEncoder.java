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
public class QuadratureEncoder extends BasicEncoder {
    /**
     * Return list of ordered Track objects for this encoder.Track information is computed on-demand
     * @param index include index track?
     * @return list of tracks
     */
    @Override
    public List<EncoderTrack> getTracks(double id, double od, int resolution, boolean index) {
        int tc;    // track count
        double tw; // track width
        double angle = 360.0/resolution;
        
        List<EncoderTrack> tracks = new ArrayList<>();

        if (index) {
            tc = 3;
        } else {
            tc = 2;
        }
        tw = (od - id)/tc;
        
        // outer track
        tracks.add(new EncoderTrack(od, od-tw, 0, angle, resolution));
       
        // inner track
        od -= tw;
        tracks.add(new EncoderTrack(od, od-tw, angle/2, angle, resolution));
        
        // index track
        if (index) {
            od -= tw;
            tracks.add(new EncoderTrack(od, od-tw, angle/2, angle, 1));
        }
        
        return tracks;
    }

}
