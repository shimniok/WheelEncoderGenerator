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

// TODO: convert dimensions when switching units Issue #11

/**
 * ViewModel object containing properties describing an encoder
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

  protected SimpleStringProperty type; // see TYPE_*
  protected SimpleStringProperty units; // see UNITS_MM or UNITS_INCH
  protected SimpleDoubleProperty outerDiameter;
  protected SimpleDoubleProperty innerDiameter;
  protected SimpleDoubleProperty centerDiameter;
  protected SimpleIntegerProperty resolution;
  protected SimpleBooleanProperty inverted; // black on white vs white on black
  protected SimpleBooleanProperty indexed; // true if has index track
  protected SimpleBooleanProperty indexable; // true if index track possible
  protected SimpleBooleanProperty direction; // DIRECTION_CLOCKWISE / DIRECTION_COUNTERCLOCKWISE
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

  /**
   * Generic ChangeListener lambda used to notify all ChangeListener objects; used to notify of
   * changes to any of the properties of this object.
   */
  private ChangeListener changed = (obs, ov, nv) -> {
    System.out.println("EncoderProperties changed()");
    changeListeners.forEach((ChangeListener cl) -> {
      cl.changed(obs, ov, nv);
    });
  };
  
  /**
   * Create new EncoderProperties object which must later be initialized with call to 
   * initialize().
   */
  public EncoderProperties() {
    this.changeListeners = new ArrayList();
    this.invalidationListeners = new ArrayList();

    this.units = new SimpleStringProperty();
    this.units.addListener(changed);

    this.outerDiameter = new SimpleDoubleProperty();
    this.outerDiameter.addListener(changed);

    this.innerDiameter = new SimpleDoubleProperty();
    this.innerDiameter.addListener(changed);

    this.centerDiameter = new SimpleDoubleProperty();
    this.centerDiameter.addListener(changed);

    this.resolution = new SimpleIntegerProperty();
    this.resolution.addListener(changed);

    this.inverted = new SimpleBooleanProperty();
    this.inverted.addListener(changed);

    this.indexed = new SimpleBooleanProperty();
    this.indexed.addListener(changed);
    this.indexable = new SimpleBooleanProperty();

    this.direction = new SimpleBooleanProperty();
    this.direction.addListener(changed);
    this.directional = new SimpleBooleanProperty();

    this.encoderMap.put(TYPE_QUADRATURE, new QuadratureEncoder());
    this.encoderMap.put(TYPE_SIMPLE, new BasicEncoder());
    this.encoderMap.put(TYPE_BINARY, new BinaryEncoder());
    this.encoderMap.put(TYPE_GRAY, new GrayEncoder());

    this.type = new SimpleStringProperty();
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
    
    this.initialize();
  }
  
  /**
   * Initialize all the properties to their defaults; use with "new" operation, rather than
   * generating a new object which would break all bindings.
   */
  public void initialize() {
    this.type.set(this.getTypeOptions().get(0));
    this.outerDiameter.set(100);
    this.innerDiameter.set(50);
    this.centerDiameter.set(5);
    this.units.set(unitOptions.get(0));
    this.resolution.set(8);
    this.inverted.set(false);
    this.indexed.set(false);
    this.direction.set(DIRECTION_CLOCKWISE);
  }

  public List<String> getTypeOptions() {
    return new ArrayList<>(this.encoderMap.keySet());
  }

  /**
   * Determine if specified resolution is valid for this encoder.
   *
   * @param resolution
   * @return true if valid, false otherwise
   */
  final public boolean validResolution(int resolution) {
    return getEncoder().validResolution(resolution);
  }
  
  /**
   * If resolution is not valid, set it to the nearest valid value
   * @param resolution is the encoder resolution
   * @return resolution, if valid, or the nearest valid value
   */
  final public int fixResolution(int resolution) {
    return getEncoder().fixResolution(resolution);
  }

  /**
   * Return the amount by which the resolution is incremented (or decremented); for use with
   * SpinnerValueFactory.
   * @return amount to increment (or decrement) resolution
   */
  int getResolutionIncrement() {
    return getEncoder().getResolutionIncrement();
  }
  
  /**
   * Get the type of encoder, represented as a string; see getTypeOptions()
   * @return type of encoder
   */
  final public SimpleStringProperty getType() {
    return type;
  }

  /**
   * Return list of options for valid unit setting
   * @return list of options
   */
  final public List<String> getUnitOptions() {
    return unitOptions;
  }

  /**
   * Gets the outer diameter of the encoder disc
   * @return outer diameter
   */
  final public SimpleDoubleProperty getOuterDiameter() {
    return outerDiameter;
  }

  /**
   * Gets the inner diameter of the encoder disc
   * @return inner diameter
   */
  final public SimpleDoubleProperty getInnerDiameter() {
    return innerDiameter;
  }

  /**
   * Gets the center diameter of the encoder disc
   * @return center diameter
   */
  final public SimpleDoubleProperty getCenterDiameter() {
    return centerDiameter;
  }

  /**
   * Gets the units being used; see getUnitOptions()
   * @return units
   */
  final public SimpleStringProperty getUnits() {
    return units;
  }

  /**
   * Gets the resolution of the encoder
   * @return resolution
   */
  final public SimpleIntegerProperty getResolution() {
    return this.resolution;
  }

  /**
   * Return whether encoder pattern is inverted or not.
   * @return true if inverted, false if not
   */
  final public SimpleBooleanProperty getInverted() {
    return this.inverted;
  }

  /**
   * Return whether an index track is enabled.
   * @return true if index track enabled, false otherwise
   */
  public SimpleBooleanProperty getIndexed() {
    return this.indexed;
  }

  /**
   * Get direction of rotation of the encoder.
   * @return CLOCKWISE (true), or COUNTERCLOCKWISE(false)
   */
  public BooleanProperty getDirection() {
    // check validity
    return this.direction;
  }
  
  /**
   * Get indexable property; some encoders don't need/can't have index tracks
   * @return true if encoder is indexable, false otherwise
   */
  public SimpleBooleanProperty getIndexable() {
    return this.indexable;
  }

  /**
   * Get directional property; basic encoders aren't directional; used to determine
   * if directional widget should be enabled/visible.
   * @return true if directional, false otherwise
   */
  public SimpleBooleanProperty getDirectional() {
    return this.directional;
  }
  
  /**
   * Get the encoder associated with these properties
   * @return the encoder associated with these properties
   */
  public EncoderModel getEncoder() {
    return (EncoderModel) this.encoderMap.get(this.getType().get());
  }

  /**
   * Get the list of EncoderTrack for this encoder, each specifying the information needed
   * to render the encoder.
   * @return list of EncoderTrack objects
   */
  public List<EncoderTrack> getTracks() {
    return getEncoder().getTracks(
        this.getInnerDiameter().get(),
        this.getOuterDiameter().get(), 
        this.getResolution().get(),
        this.getIndexed().get(), this.getDirection().get()
    );
  }

  /**
   * Determine if model is valid; use with error checking and notification, and to prevent
   * rendering of encoder while editing values in the UI.
   * @return true if valid, false if not
   */
  public boolean isValid() {
    return (this.getOuterDiameter().get() > this.getInnerDiameter().get())
        && (this.getInnerDiameter().get() >= this.getCenterDiameter().get());
  }

  /**
   * Return a Properties object representing this object; used for file save
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
    p.setProperty("encoder.indexed", Boolean.toString(this.getIndexed().get()));
    p.setProperty("encoder.inverted", Boolean.toString(this.getInverted().get()));
    p.setProperty("encoder.clockwise", Boolean.toString(this.getDirection().get()));
    p.setProperty("encoder.units", this.getUnits().get());
    
    return p;
  }

  /**
   * Set this object's properties based on Properties object; used for file load
   * @param p properties object from which to set this object's properties
   */
  public void fromProperties(Properties p) {
    this.getType().set(p.getProperty("encoder.type"));
    this.getResolution().set(Integer.parseInt(p.getProperty("encoder.resolution")));
    this.getOuterDiameter().set(Double.parseDouble(p.getProperty("encoder.outerDiameter")));
    this.getInnerDiameter().set(Double.parseDouble(p.getProperty("encoder.innerDiameter")));
    this.getCenterDiameter().set(Double.parseDouble(p.getProperty("encoder.centerDiameter")));
    this.getIndexed().set(Boolean.parseBoolean(p.getProperty("encoder.indexed")));
    this.getInverted().set(Boolean.parseBoolean(p.getProperty("encoder.inverted")));
    this.getDirection().set(Boolean.parseBoolean(p.getProperty("encoder.clockwise")));
    this.getUnits().set(p.getProperty("encoder.units"));
  }

  /**
   * Ad a ChangeListener to be notified if any changes are made to the encoder's properties
   * @param cl is a ChangeListener callback object to be notified of changes
   */
  @Override
  public void addListener(ChangeListener cl) {
    changeListeners.add(cl);
  }

  /**
   * Remove the specific ChangeListener object from the list of objects to be notified of changes
   * @param cl is the ChangeListener to remove
   */
  @Override
  public void removeListener(ChangeListener cl) {
    changeListeners.remove(cl);
  }

  /**
   * Unsupported; there's not really a "value" for the encoder, per se.
   * @return 
   */
  @Override
  public Object getValue() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Not yet implemented; use addListener(ChangeListener cl)
   * @param il 
   */
  @Override
  public void addListener(InvalidationListener il) {
    //invalidationListeners.add(il);
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Not yet implemented; use removeListener(ChangeListener cl)
   * @param il 
   */
  @Override
  public void removeListener(InvalidationListener il) {
    //invalidationListeners.remove(il);
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
