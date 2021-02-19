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
public interface EncoderModel {

  /**
   * Return minimum resolution for the encoder.
   *
   * @return min resolution
   */
  abstract public int getMinResolution();

  /**
   * Return maximum resolution for the encoder.
   *
   * @return max resolution
   */
  abstract public int getMaxResolution();

  /**
   * Return the amount by which resolution can increment
   *
   * @return resolution increment value
   */
  abstract public int getResolutionIncrement();

  /**
   * Return list of ordered Track objects for this encoder.Track information is computed on-demand.
   *
   * @param id Inside diameter
   * @param od Outside diameter
   * @param resolution Resolution of encoder
   * @param index include index track
   * @param clockwise true if encoder is designed to rotate clockwise
   * @return list of tracks
   */
  abstract public List<EncoderTrack> getTracks(double id, double od, int resolution, boolean index,
      boolean clockwise);

  /**
   * Determine if provided resolution is valid for encoder type
   *
   * @param resolution
   * @return true if resolution is valid, false otherwise
   */
  abstract public boolean validResolution(int resolution);

  /**
   * If resolution is not valid, set it to the nearest valid value.
   * @param resolution is the encoder resolution
   * @return resolution, if valid, or the nearest valid value
   */
  abstract public int fixResolution(int resolution);

}
