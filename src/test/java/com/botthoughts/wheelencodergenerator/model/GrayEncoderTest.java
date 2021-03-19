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
import com.botthoughts.wheelencodergenerator.model.GrayEncoder;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author mes
 */
public class GrayEncoderTest {
  
  private static final double TOL = 0.00001;

  public GrayEncoderTest() {
  }
  
  @BeforeAll
  public static void setUpClass() {
  }
  
  @AfterAll
  public static void tearDownClass() {
  }
  
  @BeforeEach
  public void setUp() {
  }
  
  @AfterEach
  public void tearDown() {
  }

  /**
   * Test of getTracks method, of class GrayEncoder.
   */
  @Test
  public void testGetTracks() {
    System.out.println("getTracks");
    double id = 22.0;
    double od = 44.0;
    int resolution = 3;
    boolean index = false;
    boolean clockwise = false;
    GrayEncoder instance = new GrayEncoder();
    List<EncoderTrack> result = instance.getTracks(id, od, resolution, index, clockwise);
    assertEquals(resolution, result.size(), "resolution");

    EncoderTrack t0 = result.get(0);
    assertEquals(od, t0.outerDiameter, TOL, "outer track (t0) track outerDiameter");
    assertEquals(1<<(resolution-1), t0.stripeCount, "t0 stripe count");

    EncoderTrack t1 = result.get(resolution-1);
    assertEquals(1<<(resolution-2), t1.stripeCount, "t1 stripe count");

    EncoderTrack t2 = result.get(resolution-1);
    assertEquals(id, t2.innerDiameter, TOL, "inner track (t2) innerDiameter");
    assertEquals(1<<(resolution-2), t2.stripeCount, "t2 stripe count");
  }
  
}
