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
public class QuadratureEncoderTest {
  
  public QuadratureEncoderTest() {
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
   * Test of getTracks method, of class QuadratureEncoder.
   */
  @Test
  public void testGetTracks() {
    System.out.println("getTracks");
    double id = 11.0;
    double od = 77.0;
    int resolution = 20;
    boolean index = true;
    boolean clockwise = false;
    QuadratureEncoder instance = new QuadratureEncoder();
    List<EncoderTrack> result = instance.getTracks(id, od, resolution, index, clockwise);
    assertEquals(result.size(), 3);
    EncoderTrack t0 = result.get(0);
    EncoderTrack t1 = result.get(1);
    assertNotEquals(t0.startAngle, t1.startAngle);
    assertEquals(t0.stripeCount, t1.stripeCount);
    assertEquals(t0.stripeAngle, t1.stripeAngle);
    assertEquals(t0.outerDiameter, od);
    EncoderTrack t2 = result.get(2);
    assertEquals(t2.startAngle, t0.startAngle);
    assertEquals(t2.stripeCount, 1);
    assertEquals(t2.innerDiameter, id);
  }
  
}
