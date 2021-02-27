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
public class BasicEncoderTest {
  
  public BasicEncoderTest() {
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
   * Test of getTracks method, of class BasicEncoder.
   */
  @Test
  public void testGetTracks() {
    System.out.println("getTracks");
    double id = 11.0;
    double od = 66.0;
    int resolution = 64;
    boolean index = true;
    boolean clockwise = false;
    BasicEncoder instance = new BasicEncoder();
    List<EncoderTrack> tracks = instance.getTracks(id, od, resolution, index, clockwise);
    assertEquals(2, tracks.size(), "track count");
    EncoderTrack t0 = tracks.get(0);
    EncoderTrack t1 = tracks.get(1);
    assertEquals(resolution*2, t0.stripeCount, "t0 resolution");
    assertEquals(t1.startAngle, t0.startAngle, "t0 index startAngle");
    assertEquals(360.0/(2*resolution), t0.stripeAngle, "t0 stripeAngle");
    assertEquals(od, t0.outerDiameter);
    assertEquals(1, t1.stripeCount, "index stripeCount");
    assertEquals(id, t1.innerDiameter, "index innerDiameter");
    
  }

  /**
   * Test of validResolution method, of class BasicEncoder.
   */
  @Test
  public void testValidResolution() {
    System.out.println("validResolution");
    BasicEncoder instance = new BasicEncoder();
    boolean result;
    
    // Test nonsensical values
    result = instance.validResolution(-1);
    assertEquals(false, result);
    result = instance.validResolution(0);
    assertEquals(false, result);

    result = instance.validResolution(instance.RESOLUTION_MIN-1);
    assertEquals(false, result);
    result = instance.validResolution(instance.RESOLUTION_MIN-2);
    assertEquals(false, result);
    
    result = instance.validResolution(instance.RESOLUTION_MAX+1);
    assertEquals(false, result);
    result = instance.validResolution(instance.RESOLUTION_MAX+2);
    assertEquals(false, result);

    for (int i=instance.RESOLUTION_MIN; i <= instance.RESOLUTION_MAX; i++) {
      result = instance.validResolution(i);
      assertEquals(true, result);
    }
  }

  /**
   * Test of fixResolution method, of class BasicEncoder.
   */
  @Test
  public void testFixResolution() {
    System.out.println("fixResolution");
    BasicEncoder instance = new BasicEncoder();
    int result;

    // Test nonsensical values
    result = instance.fixResolution(-1);
    assertEquals(instance.RESOLUTION_MIN, result);
    result = instance.fixResolution(0);
    assertEquals(instance.RESOLUTION_MIN, result);

    // Test too-small values
    result = instance.fixResolution(instance.RESOLUTION_MIN-1);
    assertEquals(instance.RESOLUTION_MIN, result);
    result = instance.fixResolution(instance.RESOLUTION_MIN-2);
    assertEquals(instance.RESOLUTION_MIN, result);

    // Test too-large values
    result = instance.fixResolution(instance.RESOLUTION_MAX+1);
    assertEquals(instance.RESOLUTION_MAX, result);
    result = instance.fixResolution(instance.RESOLUTION_MAX+2);
    assertEquals(instance.RESOLUTION_MAX, result);

    // Test all valid values
    for (int i=instance.RESOLUTION_MIN; i <= instance.RESOLUTION_MAX; i++) {
      result = instance.fixResolution(i);
      assertEquals(i, result);
    }
    
  }

  /**
   * Test of getMinResolution method, of class BasicEncoder.
   */
  @Test
  public void testGetMinResolution() {
    System.out.println("getMinResolution");
    BasicEncoder instance = new BasicEncoder();
    int expResult = instance.RESOLUTION_MIN;
    int result = instance.getMinResolution();
    assertEquals(expResult, result);
  }

  /**
   * Test of getMaxResolution method, of class BasicEncoder.
   */
  @Test
  public void testGetMaxResolution() {
    System.out.println("getMaxResolution");
    BasicEncoder instance = new BasicEncoder();
    int expResult = instance.RESOLUTION_MAX;
    int result = instance.getMaxResolution();
    assertEquals(expResult, result);
  }

  /**
   * Test of getResolutionIncrement method, of class BasicEncoder.
   */
  @Test
  public void testGetResolutionIncrement() {
    System.out.println("getResolutionIncrement");
    BasicEncoder instance = new BasicEncoder();
    int expResult = instance.INCREMENT;
    int result = instance.getResolutionIncrement();
    assertEquals(expResult, result);
    assertEquals(1, result);
  }
  
}
