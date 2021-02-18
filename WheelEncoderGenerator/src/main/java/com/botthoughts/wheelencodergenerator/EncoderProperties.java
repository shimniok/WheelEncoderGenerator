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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import javafx.beans.InvalidationListener;
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
public final class EncoderProperties implements ObservableValue {

  public static final String UNITS_MM = "mm";
  public static final String UNITS_INCH = "inch";
  public static final Boolean DIRECTION_CLOCKWISE = true;
  public static final Boolean DIRECTION_COUNTERCLOCKWISE = false;
  public static final String TYPE_QUADRATURE = "Quadrature";
  public static final String TYPE_SIMPLE = "Simple";
  public static final String TYPE_BINARY = "Binary";
  public static final String TYPE_GRAY = "Gray";

  protected SimpleStringProperty type;
  protected SimpleStringProperty units; // see this.unitOptions
  protected SimpleDoubleProperty outerDiameter;
  protected SimpleDoubleProperty innerDiameter;
  protected SimpleDoubleProperty centerDiameter;
  protected SimpleIntegerProperty resolution;
  protected SimpleBooleanProperty inverted;
  protected SimpleBooleanProperty indexTrack;
  protected SimpleBooleanProperty clockwise; // see this.CLOCKWISE
  protected SimpleBooleanProperty indexable; // true if index track possible
  protected SimpleBooleanProperty directional; // true if encoder is directional
  protected EncoderModel encoder;
  
  /**
   * A hash map relating encoder type to EncoderModels
   */
  protected LinkedHashMap<String, EncoderModel> encoderMap = new LinkedHashMap();

  /**
   * List of options for Units
   */
  protected static List<String> unitOptions = Arrays.asList(EncoderProperties.UNITS_MM, EncoderProperties.UNITS_INCH);

  private List<ChangeListener> changeListeners;
  private List<InvalidationListener> invalidationListeners;

  ChangeListener changed = (obs, ov, nv) -> {
    System.out.println("EncoderProperties changed()");
    changeListeners.forEach((ChangeListener cl) -> {
      cl.changed(obs, ov, nv);
    });
  };
  
  public EncoderProperties() {
    this.changeListeners = new ArrayList();
    this.invalidationListeners = new ArrayList();

    this.outerDiameter = new SimpleDoubleProperty(100);
    this.outerDiameter.addListener(changed);

    this.innerDiameter = new SimpleDoubleProperty(10);
    this.innerDiameter.addListener(changed);

    this.centerDiameter = new SimpleDoubleProperty(5);
    this.centerDiameter.addListener(changed);

    this.inverted = new SimpleBooleanProperty(false);
    this.inverted.addListener(changed);

    this.units = new SimpleStringProperty(unitOptions.get(0));
    this.units.addListener(changed);

    this.resolution = new SimpleIntegerProperty(10);
    this.resolution.addListener(changed);

    this.clockwise = new SimpleBooleanProperty(DIRECTION_CLOCKWISE);
    this.clockwise.addListener(changed);

    this.indexTrack = new SimpleBooleanProperty(false);
    this.indexTrack.addListener(changed);

    this.indexable = new SimpleBooleanProperty(true);
    this.directional = new SimpleBooleanProperty(true);

    this.encoderMap.put("Quadrature", new QuadratureEncoder());
    this.encoderMap.put("Simple", new BasicEncoder());
    this.encoderMap.put("Binary", new BinaryEncoder());
    this.encoderMap.put("Gray", new GrayEncoder());

    this.type = new SimpleStringProperty(this.getTypeOptions().get(0));
    this.type.addListener(changed);

    // Listener to set indexable and directional properties based on encoder type
    this.type.addListener((obs, ov, nv) -> {
      switch (nv) {
        case TYPE_QUADRATURE:
          this.indexable.set(true);
          this.directional.set(true);
          break;
        case TYPE_SIMPLE:
          this.indexable.set(true);
          this.directional.set(false);
          break;
        case TYPE_BINARY:
        case TYPE_GRAY:
          this.indexable.set(false);
          this.directional.set(true);
          break;
      }
    });
  }

  public List<String> getTypeOptions() {
    return new ArrayList<>(this.encoderMap.keySet());
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

  public SimpleBooleanProperty getIndexable() {
    return this.indexable;
  }

  public SimpleBooleanProperty getDirectional() {
    return this.directional;
  }
  
  /**
   * Get the encoder associated with these properties
   *
   * @return the encoder associated with these properties
   */
  public EncoderModel getEncoder() {
    return (EncoderModel) this.encoderMap.get(this.type.get());
  }

  public List<EncoderTrack> getTracks() {
    return getEncoder().getTracks(this.getInnerDiameter().get(),
        this.getOuterDiameter().get(), this.getResolution().get(),
        this.getIndexTrack().get(), this.getDirection().get());
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

}
