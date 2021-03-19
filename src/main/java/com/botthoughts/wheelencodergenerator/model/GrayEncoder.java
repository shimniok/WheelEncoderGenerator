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
package com.botthoughts.wheelencodergenerator.model;

import com.botthoughts.wheelencodergenerator.EncoderTrack;
import java.util.ArrayList;
import java.util.List;

/**
 * Gray-code encoder model is a type of binary encoder (See BinaryEncoder) encoder identical in
 * every respect except the bit pattern between any two positions differs only by a single bit
 * (e.g, 00, 01, 11, 10).
 * @author mes
 */
public class GrayEncoder extends BinaryEncoder {

  /**
   * Return list of ordered Track objects for this encoder. Track information is computed on-demand
   *
   * @param id Inside diameter
   * @param od Outside diameter
   * @param resolution
   * @param index ignored because it doesn't make sense for this type of encoder
   * @return list of tracks
   */
  @Override
  public List<EncoderTrack> getTracks(double id, double od, int resolution, boolean index,
      boolean clockwise) {
    double tw = (od - id) / resolution; // track width
    double angle = 0;
    double start;
    int res = 0;

    tracks = new ArrayList<>();

    id = od - tw;
    for (int b = resolution - 1; b >= 0; b--) {
      if (b == 0) {
        /**
         * for the final, innermost track, we still have 2 stripes, black and white, covering
         * the same angle of 180°, but rotated ±90 degrees from the previous track (-90 for
         * clockwise, +90 for counter-clockwise)
         */
        if (clockwise) {
          start = 180;
        } else {
          start = 0;
        }
      } else {
        /**
         * for all the other tracks, the number of black and white stripes is 2^b with the first
         * stripe centered on 0 degrees
         */
        res = 1 << b;
        angle = 360.0 / res;
        start = angle / 2;
      }

      tracks.add(new EncoderTrack(od, id, start, angle, res));
      od -= tw;
      id -= tw;
    }

    return tracks;
  }
}
