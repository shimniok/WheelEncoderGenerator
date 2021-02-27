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
    double id = 0.0;
    double od = 0.0;
    int resolution = 0;
    boolean index = false;
    boolean clockwise = false;
    BasicEncoder instance = new BasicEncoder();
    List<EncoderTrack> tracks = instance.getTracks(id, od, resolution, index, clockwise);
    assertEquals(tracks.size(), 1);
    // TODO review the generated test code and remove the default call to fail.
  }

  /**
   * Test of validResolution method, of class BasicEncoder.
   */
  @Test
  public void testValidResolution() {
    System.out.println("validResolution");
    int resolution = 0;
    BasicEncoder instance = new BasicEncoder();
    boolean result;
    result = instance.validResolution(-1);
    assertEquals(result, false);
    result = instance.validResolution(0);
    assertEquals(result, false);
    result = instance.validResolution(1);
    assertEquals(result, true);
    result = instance.validResolution(BasicEncoder.RESOLUTION_MAX);
    assertEquals(result, true);
    result = instance.validResolution(BasicEncoder.RESOLUTION_MIN);
    assertEquals(result, true);
    result = instance.validResolution(2);
    assertEquals(result, true);
  }

  /**
   * Test of fixResolution method, of class BasicEncoder.
   */
  @Test
  public void testFixResolution() {
    System.out.println("fixResolution");
    int resolution = 0;
    BasicEncoder instance = new BasicEncoder();
    int result;
    result = instance.fixResolution(-1);
    assertEquals(result, BasicEncoder.RESOLUTION_MIN);
    result = instance.fixResolution(0);
    assertEquals(result, BasicEncoder.RESOLUTION_MIN);
    result = instance.fixResolution(BasicEncoder.RESOLUTION_MIN-1);
    assertEquals(result, BasicEncoder.RESOLUTION_MIN);
    result = instance.fixResolution(BasicEncoder.RESOLUTION_MIN-2);
    assertEquals(result, BasicEncoder.RESOLUTION_MIN);
    result = instance.fixResolution(BasicEncoder.RESOLUTION_MAX);
    assertEquals(result, BasicEncoder.RESOLUTION_MAX);
    result = instance.fixResolution(BasicEncoder.RESOLUTION_MAX+1);
    assertEquals(result, BasicEncoder.RESOLUTION_MAX);
    result = instance.fixResolution(BasicEncoder.RESOLUTION_MAX+2);
    assertEquals(result, BasicEncoder.RESOLUTION_MAX);
  }

  /**
   * Test of getMinResolution method, of class BasicEncoder.
   */
  @Test
  public void testGetMinResolution() {
    System.out.println("getMinResolution");
    BasicEncoder instance = new BasicEncoder();
    int expResult = BasicEncoder.RESOLUTION_MIN;
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
    int expResult = BasicEncoder.RESOLUTION_MAX;
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
    int expResult = 1;
    int result = instance.getResolutionIncrement();
    assertEquals(expResult, result);
  }
  
}
