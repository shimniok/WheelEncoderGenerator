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

import java.util.List;

/**
 * Functionality required of--and unique to--each encoder type.
 * 
 * @author mes
 */
public interface EncoderInterface {

    /**
     * Return minimum resolution for the encoder.
     * @return min resolution
     */
    abstract public int getMinResolution();
    
    /**
     * Return maximum resolution for the encoder.
     * @return max resolution
     */
    abstract public int getMaxResolution();

    /**
     * Return the amount by which resolution can increment
     * @return resolution increment value
     */
    abstract public int getResolutionIncrement();
   
    /**
     * Return list of ordered Track objects for this encoder. Track information
     * is computed on-demand.
     * @param id Inside diameter
     * @param od Outside diameter
     * @param resolution Resolution of encoder
     * @param index include index track
     * @return list of tracks
     */
    abstract public List<EncoderTrack> getTracks(double id, double od, int resolution, boolean index);

    /**
     * Determine if provided resolution is valid for encoder type
     * 
     * @param resolution
     * @return true if resolution is valid, false otherwise
     */
    abstract public boolean validResolution(int resolution);

    /**
     * Attempt to increment the supplied resolution by one step.
     * If the result is valid (see validResolution()) it its returned,
     * otherwise the original resolution is returned
     * 
     * @param resolution 
     * @return incremented resolution if valid, else original resolution
     */
    //abstract public Integer incrementResolution(int resolution);
    
    /**
     * Attempt to decrement the supplied resolution by one step.
     * If the result is valid (see validResolution()) it its returned,
     * otherwise the original resolution is returned
     * 
     * @return decremented resolution if valid, else original resolution
     * @param resolution 
     */
    //abstract public Integer decrementResolution(int resolution);
    
}
