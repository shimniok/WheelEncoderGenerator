/*
 * Copyright 2021 Michael Shimniok.
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Michael Shimniok
 */
public class UnitConverterTest {
  
  public UnitConverterTest() {
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
   * Test of toInch method, of class UnitConverter.
   */
  @Test
  public void testToInch() {
    System.out.println("toInch");
    Double mmValue = 25.4;
    Double expResult = 1.0;
    Double result = UnitConverter.toInch(mmValue);
    assertEquals(expResult, result);
  }

  /**
   * Test of toMillimeter method, of class UnitConverter.
   */
  @Test
  public void testToMillimeter() {
    System.out.println("toMillimeter");
    Double inchValue = 1.0;
    Double expResult = 25.4;
    Double result = UnitConverter.toMillimeter(inchValue);
    assertEquals(expResult, result);
  }
  
}
