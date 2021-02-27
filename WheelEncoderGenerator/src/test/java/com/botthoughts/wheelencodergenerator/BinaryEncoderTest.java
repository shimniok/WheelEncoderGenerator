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
public class BinaryEncoderTest {
  
  public BinaryEncoderTest() {
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
   * Test of validResolution method, of class BinaryEncoder.
   */
  @Test
  public void testValidResolution() {
    System.out.println("validResolution");
    int resolution = 0;
    BinaryEncoder instance = new BinaryEncoder();
    boolean result;

    result = instance.validResolution(-1);
    assertEquals(result, false);
    result = instance.validResolution(0);
    assertEquals(result, false);
    
    result = instance.validResolution(instance.RESOLUTION_MIN-1);
    assertEquals(result, false);
    result = instance.validResolution(instance.RESOLUTION_MIN-2);
    assertEquals(result, false);

    result = instance.validResolution(instance.RESOLUTION_MAX+1);
    assertEquals(result, false);
    result = instance.validResolution(instance.RESOLUTION_MAX+1);
    assertEquals(result, false);
    
    for (int i=instance.RESOLUTION_MIN; i <= instance.RESOLUTION_MAX; i++) {
      result = instance.validResolution(i);
      assertEquals(result, true);     
    }
  }

  /**
   * Test of getTracks method, of class BinaryEncoder.
   */
  @Test
  public void testGetTracks() {
    System.out.println("getTracks");
    double id = 45.0;
    double od = 22.0;
    int resolution = 5;
    boolean index = false;
    boolean clockwise = false;
    BinaryEncoder instance = new BinaryEncoder();
    List<EncoderTrack> result = instance.getTracks(id, od, resolution, index, clockwise);
    assertEquals(result.size(), resolution);
    // TODO additional tests
  }

  /**
   * Test of getMinResolution method, of class BinaryEncoder.
   */
  @Test
  public void testGetMinResolution() {
    System.out.println("getMinResolution");
    BinaryEncoder instance = new BinaryEncoder();
    int expResult = instance.RESOLUTION_MIN;
    int result = instance.getMinResolution();
    assertEquals(expResult, result);
  }

  /**
   * Test of getMaxResolution method, of class BinaryEncoder.
   */
  @Test
  public void testGetMaxResolution() {
    System.out.println("getMaxResolution");
    BinaryEncoder instance = new BinaryEncoder();
    int expResult = instance.RESOLUTION_MAX;
    int result = instance.getMaxResolution();
    assertEquals(expResult, result);
  }

  /**
   * Test of getResolutionIncrement method, of class BinaryEncoder.
   */
  @Test
  public void testGetResolutionIncrement() {
    System.out.println("getResolutionIncrement");
    BinaryEncoder instance = new BinaryEncoder();
    int expResult = instance.INCREMENT;
    int result = instance.getResolutionIncrement();
    assertEquals(expResult, result);
    assertEquals(1, result);
  }
  
}
