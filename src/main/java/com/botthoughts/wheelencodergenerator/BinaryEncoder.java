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
package com.botthoughts.wheelencodergenerator; // TODO: move to encoder package

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mes
 */
public class BinaryEncoder extends BasicEncoder {
  
  // TODO: add comments/documentation
  public BinaryEncoder() {
    this.RESOLUTION_MIN = 1;
    this.RESOLUTION_MAX = 11;
    this.INCREMENT = 1;
    this.INDEXABLE = false;
    this.DIRECTIONAL = true;
  }

  /**
   * Return list of ordered Track objects for this encoder. Tracks are computed on-demand based on
   * current properties
   *
   * @param id Inside diameter
   * @param od Outside diameter
   * @param resolution is the number of "bits" or separate tracks
   * @param index is ignored for binary
   * @param clockwise is true if encoder designed for clockwise rotation
   * @return list of tracks
   */
  @Override
  public List<EncoderTrack> getTracks(double id, double od, int resolution, boolean index,
      boolean clockwise) {
    tracks = new ArrayList<>();

    double trackWidth = (od - id) / resolution;

    id = od - trackWidth;
    for (int b = resolution; b > 0; b--) {
      int res = 1 << b;
      double angle = 360.0 / res;
      
      if (clockwise) angle = -angle;

      tracks.add(new EncoderTrack(od, id, 0, angle, res));
      od -= trackWidth;
      id -= trackWidth;
    }

    return tracks;
  }

}
