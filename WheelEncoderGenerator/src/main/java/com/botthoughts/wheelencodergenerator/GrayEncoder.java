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
public class GrayEncoder extends BinaryEncoder {

    /**
     * Return list of ordered Track objects for this encoder. Track information
     * is computed on-demand
     * @param id Inside diameter
     * @param od Outside diameter
     * @param resolution
     * @param index ignored because it doesn't make sense for this type of encoder
     * @return list of tracks
     */
    @Override
    public List<EncoderTrack> getTracks(double id, double od, int resolution, boolean index) {
        double tw = (od - id)/resolution; // track width
        double angle = 0;
        double start;
        int res = 0;

        tracks = new ArrayList<>();

        id = od - tw;
        for (int b = resolution-1; b >= 0; b--) {
            if (b > 0) {
                res = 1 << b;
                angle = 360.0/res;
                start = angle/2;
            } else {
                // for the final track, we keep the previous
                // resolution and angle, and simply start it
                // 90 degrees offset from the previous track
                start = 0;
            }

            tracks.add(new EncoderTrack(od, id, start, angle, res));
            od -= tw;
            id -= tw;            
        }
        
        return tracks;
    }
}
