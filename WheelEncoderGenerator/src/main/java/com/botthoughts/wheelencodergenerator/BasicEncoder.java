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
public class BasicEncoder extends EncoderTracks {

    @Override
    public List<EncoderTrack> getTracks() {
        tracks = new ArrayList<>();
        
        EncoderTrack outer = new EncoderTrack(
                ep.getOuterDiameter().getValue(),
                ep.getInnerDiameter().getValue(), 
                0, 360.0/this.ep.getResolution().getValue(), 
                ep.getResolution().getValue());
        tracks.add(outer);
        
        return tracks;
    }

    /**
     * Determine if specified resolution is valid
     * @return true if valid, false otherwise
     */
    @Override
    public boolean validResolution(int resolution) {
        return resolution > 1;
    }
    
    public BasicEncoder(EncoderProperties ep) {
        super(ep);
    }
}
