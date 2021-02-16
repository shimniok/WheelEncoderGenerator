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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 *
 * @author mes
 */
public final class EncoderProperties implements ObservableValue, ChangeListener {

  public static String MM = "mm";
  public static String INCH = "inch";
  public static Boolean CLOCKWISE = true;
  public static Boolean COUNTERCLOCKWISE = false;

  protected SimpleStringProperty type;
  protected SimpleStringProperty units; // see this.unitOptions
  protected SimpleDoubleProperty outerDiameter;
  protected SimpleDoubleProperty innerDiameter;
  protected SimpleDoubleProperty centerDiameter;
  protected SimpleIntegerProperty resolution;
  protected SimpleBooleanProperty inverted;
  protected SimpleBooleanProperty indexTrack;
  protected SimpleBooleanProperty clockwise; // see this.CLOCKWISE

  /**
   * A hash map relating encoder type to EncoderModels
   */
  protected static Map<String, EncoderModel> encoderMap = Map.ofEntries(
      new AbstractMap.SimpleEntry<String, EncoderModel>("Quadrature", new QuadratureEncoder()),
      new AbstractMap.SimpleEntry<String, EncoderModel>("Simple", new BasicEncoder()),
      new AbstractMap.SimpleEntry<String, EncoderModel>("Binary", new BinaryEncoder()),
      new AbstractMap.SimpleEntry<String, EncoderModel>("Gray", new GrayEncoder())
  );

  /**
   * List of options for Units
   */
  protected static List<String> unitOptions = Arrays.asList(
      EncoderProperties.MM, EncoderProperties.INCH);

  /**
   * A list of strings representing available encoder types
   */
  protected static List<String> typeOptions = Arrays.asList(
      "Quadrature", "Simple", "Binary", "Gray"
  );

  private List<ChangeListener> changeListeners;
  private List<InvalidationListener> invalidationListeners;

  // Singleton pattern
  private static EncoderProperties INSTANCE = null;

  /**
   * Make new encoder properties object; Singleton pattern
   */
  private EncoderProperties() {
  }

  /**
   * Returns Singleton instance of EncoderProperites with lazy instantiation and
   * initialization.
   *
   * @return EncoderProperties singleton instance
   */
  public static synchronized EncoderProperties getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new EncoderProperties();

      INSTANCE.changeListeners = new ArrayList();
      INSTANCE.invalidationListeners = new ArrayList();

      INSTANCE.outerDiameter = new SimpleDoubleProperty(100);
      INSTANCE.outerDiameter.addListener(INSTANCE);

      INSTANCE.innerDiameter = new SimpleDoubleProperty(10);
      INSTANCE.innerDiameter.addListener(INSTANCE);

      INSTANCE.centerDiameter = new SimpleDoubleProperty(5);
      INSTANCE.centerDiameter.addListener(INSTANCE);

      INSTANCE.inverted = new SimpleBooleanProperty(false);
      INSTANCE.inverted.addListener(INSTANCE);

      INSTANCE.units = new SimpleStringProperty(unitOptions.get(0));
      INSTANCE.units.addListener(INSTANCE);

      INSTANCE.resolution = new SimpleIntegerProperty(10);
      INSTANCE.resolution.addListener(INSTANCE);

      INSTANCE.clockwise = new SimpleBooleanProperty(CLOCKWISE);
      INSTANCE.clockwise.addListener(INSTANCE);

      INSTANCE.indexTrack = new SimpleBooleanProperty(false);
      INSTANCE.indexTrack.addListener(INSTANCE);

      // Prime candidate for invalidation listener??
      INSTANCE.type = new SimpleStringProperty(INSTANCE.getTypeOptions().get(0));
      INSTANCE.type.addListener(INSTANCE);
      
//      Persistence p = new Persistence();
//      p.register("encoder.type", INSTANCE.type);
//      p.register("encoder.outsideDiameter", INSTANCE.outerDiameter);
//      p.register("encoder.resolution", INSTANCE.resolution);
//      p.register("encoder.centerDiameter", INSTANCE.centerDiameter);
//      p.register("encoder.innerDiameter", INSTANCE.innerDiameter);
//      p.register("encoder.indexTrack", INSTANCE.indexTrack);
//      p.register("encoder.inverted", INSTANCE.inverted);
//      p.register("encoder.clockwise", INSTANCE.clockwise);
//      p.register("encoder.units", INSTANCE.units);
      
    }
    return INSTANCE;
  }

  public List<String> getTypeOptions() {
    return new ArrayList<>(EncoderProperties.encoderMap.keySet());
  }

  /**
   * Determine if specified resolution is valid for this encoder
   *
   * @param resolution
   * @return true if valid, false otherwise
   */
  final public boolean validResolution(int resolution) {
    return getEncoder().validResolution(resolution);
  }

  /**
   * Get the type of encoder, represented as a string; see getTypeOptions()
   *
   * @return type of encoder
   */
  final public SimpleStringProperty getType() {
    return type;
  }

  /**
   * Return list of options for valid unit setting
   *
   * @return list of options
   */
  final public List<String> getUnitOptions() {
    return unitOptions;
  }

  /**
   * Gets the outer diameter of the encoder disc
   *
   * @return outer diameter
   */
  final public SimpleDoubleProperty getOuterDiameter() {
    return outerDiameter;
  }

  /**
   * Gets the inner diameter of the encoder disc
   *
   * @return inner diameter
   */
  final public SimpleDoubleProperty getInnerDiameter() {
    return innerDiameter;
  }

  /**
   * Gets the center diameter of the encoder disc
   *
   * @return center diameter
   */
  final public SimpleDoubleProperty getCenterDiameter() {
    return centerDiameter;
  }

  /**
   * Gets the units being used; see getUnitOptions()
   *
   * @return units
   */
  final public SimpleStringProperty getUnits() {
    return units;
  }

  /**
   * Gets the resolution of the encoder
   *
   * @return resolution
   */
  final public SimpleIntegerProperty getResolution() {
    return this.resolution;
  }

  /**
   * Return whether encoder pattern is inverted or not.
   *
   * @return true if inverted, false if not
   */
  final public SimpleBooleanProperty getInverted() {
    return this.inverted;
  }

  /**
   * Return whether an index track is enabled.
   *
   * @return true if index track enabled, false otherwise
   */
  public SimpleBooleanProperty getIndexTrack() {
    return this.indexTrack;
  }

  /**
   * Get direction of rotation of the encoder.
   *
   * @return CLOCKWISE (true), or COUNTERCLOCKWISE(false)
   */
  public BooleanProperty getDirection() {
    // check validity
    return this.clockwise;
  }

  /**
   * Get the encoder associated with these properties
   *
   * @return the encoder associated with these properties
   */
  public EncoderModel getEncoder() {
    return (EncoderModel) EncoderProperties.encoderMap.get(this.type.get());
  }

  public List<EncoderTrack> getTracks() {
    return getEncoder().getTracks(this.getInnerDiameter().get(),
        this.getOuterDiameter().get(), this.getResolution().get(),
        this.getIndexTrack().get());
  }

  /**
   * Determine if model is valid
   *
   * @return true if valid, false if not
   */
  public boolean isValid() {
    return (this.getOuterDiameter().get() > this.getInnerDiameter().get())
        && (this.getInnerDiameter().get() >= this.getCenterDiameter().get());
  }

  /**
   * Return a Properties object representing this object.
   *
   * @return Properties object
   */
  public Properties toProperties() {
    Properties p;

    p = new Properties();

    p.setProperty("encoder.type", this.getType().get());
    p.setProperty("encoder.resolution", Integer.toString(this.getResolution().get()));
    p.setProperty("encoder.outerDiameter", Double.toString(this.getOuterDiameter().get()));
    p.setProperty("encoder.innerDiameter", Double.toString(this.getInnerDiameter().get()));
    p.setProperty("encoder.centerDiameter", Double.toString(this.getCenterDiameter().get()));
    p.setProperty("encoder.indexTrack", Boolean.toString(this.getIndexTrack().get()));
    p.setProperty("encoder.inverted", Boolean.toString(this.getInverted().get()));
    p.setProperty("encoder.clockwise", Boolean.toString(this.getDirection().get()));
    p.setProperty("encoder.units", this.getUnits().get());
    
    return p;
  }

  /**
   * Set this object's properties based on Properties object
   *
   * @param p properties object from which to set this object's properties
   */
  public void fromProperties(Properties p) {
    this.getType().set(p.getProperty("encoder.type"));
    this.getResolution().set(Integer.parseInt(p.getProperty("encoder.resolution")));
    this.getOuterDiameter().set(Double.parseDouble(p.getProperty("encoder.outerDiameter")));
    this.getInnerDiameter().set(Double.parseDouble(p.getProperty("encoder.innerDiameter")));
    this.getCenterDiameter().set(Double.parseDouble(p.getProperty("encoder.centerDiameter")));
    this.getIndexTrack().set(Boolean.parseBoolean(p.getProperty("encoder.indexTrack")));
    this.getInverted().set(Boolean.parseBoolean(p.getProperty("encoder.inverted")));
    this.getDirection().set(Boolean.parseBoolean(p.getProperty("encoder.clockwise")));
    this.getUnits().set(p.getProperty("encoder.units"));
  }

  @Override
  public void addListener(ChangeListener cl) {
    changeListeners.add(cl);
  }

  @Override
  public void removeListener(ChangeListener cl) {
    changeListeners.remove(cl);
  }

  @Override
  public Object getValue() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void addListener(InvalidationListener il) {
    invalidationListeners.add(il);
  }

  @Override
  public void removeListener(InvalidationListener il) {
    invalidationListeners.remove(il);
  }

  @Override
  public void changed(ObservableValue obsV, Object oldV, Object newV) {
    System.out.println("EncoderProperties changed()");
    changeListeners.forEach((ChangeListener cl) -> {
      cl.changed(obsV, oldV, newV);
    });
  }

}
