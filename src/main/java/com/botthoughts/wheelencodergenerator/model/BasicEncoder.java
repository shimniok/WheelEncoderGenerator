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
import java.util.function.UnaryOperator;

/**
 * BasicEncoder implements the EncoderModel interface and represents a non-directional encoder
 * with a single track to detect motion and an optional index track for an initial position.
 * @author mes
 */
public class BasicEncoder implements EncoderModel {

  protected List<EncoderTrack> tracks; // list of tracks
  protected int RESOLUTION_MIN;
  protected int RESOLUTION_MAX;
  protected int INCREMENT;
  protected boolean INDEXABLE;
  protected boolean DIRECTIONAL;

  /**
   * Create a new BasicEncoder with min resolution of 1 and max resolution of 512 and a
   * resolution increment of 1.
   */
  public BasicEncoder() {
    this.RESOLUTION_MIN = 1;
    this.RESOLUTION_MAX = 512;
    this.INCREMENT = 1;
    this.INDEXABLE = true;
    this.DIRECTIONAL = false;
  }
  
  /**
   * Return list of tracks
   *
   * @param id inside diameter
   * @param od outside diameter
   * @param resolution resolution of encoder
   * @param index is true if an index track is present
   * @param clockwise ignored for a single-track encoder
   * @return list of tracks
   */
  @Override
  public List<EncoderTrack> getTracks(double id, double od, int resolution, boolean index,
      boolean clockwise) {
    tracks = new ArrayList<>();
    int r = resolution * 2;
    int tc;
    double tw;
    double angle = 360.0 / r;

    if (index) {
      tc = 2;
    } else {
      tc = 1;
    }
    tw = (od - id) / tc;

    tracks.add(new EncoderTrack(od, od - tw, 0, angle, r));

    if (index) {
      od -= tw;
      tracks.add(new EncoderTrack(od, od - tw, 0, angle, 1));
    }

    return tracks;
  }

  /**
   * Determine if specified resolution is valid
   *
   * @return true if valid, false otherwise
   */
  @Override
  public boolean validResolution(int resolution) {
    return RESOLUTION_MIN <= resolution && resolution <= RESOLUTION_MAX;
  }

  /**
   * If resolution is not valid, set it to the nearest valid value
   * @param resolution is the encoder resolution
   * @return resolution, if valid, or the nearest valid value  @Override
   */
  @Override
  public int fixResolution(int resolution) {
    int r = resolution;
    if (r < this.getMinResolution()) {
      r = this.getMinResolution();
    }
    if (r > this.getMaxResolution()) {
      r = this.getMaxResolution();
    }
    return r;
  }

  /**
   * Get minimum resolution for this type of encoder
   * @return minimum resolution
   */
  @Override
  public int getMinResolution() {
    return RESOLUTION_MIN;
  }

  /**
   * Get maximum resolution for this type of encoder
   * @return maximum resolution
   */
  @Override
  public int getMaxResolution() {
    return RESOLUTION_MAX;
  }

  /**
   * Return the UnaryOperator for incrementing resolution
   *
   * @return resolution increment value
   */
  @Override
  public UnaryOperator<Integer> getResolutionIncrement() {
    return (x) -> { return fixResolution(x+1); };
  }

  /**
   * Return the UnaryOperator for decrementing resolution
   *
   * @return resolution increment value
   */
  @Override
  public UnaryOperator<Integer> getResolutionDecrement() {
    return (x) -> { return fixResolution(x-1); };
  }

  /**
   * Indicates whether the encoder is indexable or not.
   * @return true only if the encoder supports an index track
   */
  @Override
  public boolean isIndexable() {
    return this.INDEXABLE;
  }

  /**
   * Returns the directionality of the encoder
   * @return true if the encoder is directional, false if non-directional.
   */
  @Override
  public boolean isDirectional() {
    return this.DIRECTIONAL;
  }

}
