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
 * Quadrature encoder model consisting of two tracks of stripes, offset to detect direction of
 * rotation, and an optional index track.
 * @author mes
 */
public class QuadratureEncoder extends BasicEncoder {

  /**
   * Create a new quadrature encoder model
   */
  public QuadratureEncoder() {
    super();
    this.DIRECTIONAL = true;
  }
  
  /**
   * Return list of ordered Track objects for this encoder.Track information is computed on-demand
   *
   * @param id inside diameter of track
   * @param od outside diameter of track
   * @param resolution number of black stripes on track
   * @param index include index track?
   * @return list of tracks
   */
  @Override
  public List<EncoderTrack> getTracks(double id, double od, int resolution, boolean index,
      boolean clockwise) {
    int tc;    // track count
    double tw; // track width
    int r = resolution * 2;
    double angle = 360.0 / r;

    tracks = new ArrayList<>();

    if (index) {
      tc = 3;
    } else {
      tc = 2;
    }
    tw = (od - id) / tc;

    if (clockwise) angle = -angle;
    
    // outer track
    tracks.add(new EncoderTrack(od, od - tw, angle / 2, angle, r));

    // inner track
    od -= tw;
    tracks.add(new EncoderTrack(od, od - tw, 0, angle, r));

    // index track
    if (index) {
      od -= tw;
      tracks.add(new EncoderTrack(od, od - tw, 0, angle, 1));
    }

    return tracks;
  }

}
