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
package com.botthoughts.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
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
public class BoundedIntegerConverterTest {
  
  public final int min = 1;
  public final int max = 10;
  public BoundedIntegerConverter instance;
  
  public BoundedIntegerConverterTest() {
    instance = new BoundedIntegerConverter(min, max);
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
   * Test of minProperty method, of class BoundedIntegerConverter.
   */
  @Test
  public void testMinProperty() {
    System.out.println("minProperty");
    assertEquals(min, instance.minProperty().get());
  }

  /**
   * Test of maxProperty method, of class BoundedIntegerConverter.
   */
  @Test
  public void testMaxProperty() {
    System.out.println("maxProperty");
    assertEquals(max, instance.maxProperty().get());
  }

  /**
   * Test of toString method, of class BoundedIntegerConverter.
   */
  @Test
  public void testToString() {
    System.out.println("toString");
    
    assertEquals(Integer.toString(min), instance.toString(min));
    assertEquals(Integer.toString(min), instance.toString(min-1));
    assertEquals(Integer.toString(max), instance.toString(max));
    assertEquals(Integer.toString(max), instance.toString(max+1));
  }

  /**
   * Test of fromString method, of class BoundedIntegerConverter.
   */
  @Test
  public void testFromString() {
    System.out.println("fromString");

    assertEquals(min, instance.fromString(Integer.toString(min)));
    assertEquals(min, instance.fromString(Integer.toString(min-1)));
    assertEquals(max, instance.fromString(Integer.toString(max)));
    assertEquals(max, instance.fromString(Integer.toString(max+1)));
    // Test parsing
    try {
      assertEquals(min, instance.fromString("a"));
      fail("did not throw exception");
    } catch (Exception e) {
      System.out.println("caught exception "+e.getMessage());
    }
  }
  
}
