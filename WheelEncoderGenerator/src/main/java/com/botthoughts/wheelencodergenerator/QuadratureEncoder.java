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
public class QuadratureEncoder extends EncoderTracks {
    
    @Override
    public boolean validResolution(int resolution) {
        return resolution > 1;
    }

    /**
     * Return list of ordered Track objects for this encoder.
     * Track information is computed on-demand
     * @return list of tracks
     */
    @Override
    public List<EncoderTrack> getTracks() {
        tracks = new ArrayList<>();
        double angle = 360.0/ep.getResolution().getValue();

        // outer track
        tracks.add(new EncoderTrack(ep.getOuterDiameter().getValue(),
                ep.getInnerDiameter().getValue(), 0, angle, ep.getResolution().getValue()));
        
        // inner track
        tracks.add(new EncoderTrack(ep.getOuterDiameter().getValue(),
                ep.getInnerDiameter().getValue(), angle/2, angle, ep.getResolution().getValue()));
        
        // index track
        if (this.ep.getIndexTrack().getValue()) {
            tracks.add(new EncoderTrack(ep.getOuterDiameter().getValue(),
                ep.getInnerDiameter().getValue(), angle/2, angle, ep.getResolution().getValue()));
        }
        
        return tracks;
    }

    QuadratureEncoder(EncoderProperties ep) {
        super(ep);
    }

}
