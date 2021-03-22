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

import com.botthoughts.wheelencodergenerator.model.BasicEncoder;
import com.botthoughts.wheelencodergenerator.model.BinaryEncoder;
import com.botthoughts.wheelencodergenerator.model.QuadratureEncoder;
import com.botthoughts.wheelencodergenerator.model.GrayEncoder;
import com.botthoughts.wheelencodergenerator.model.EncoderModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.function.UnaryOperator;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * ViewModel object containing properties describing an encoder
 * @author mes
 */
public final class EncoderProperties implements ObservableValue {

  /////////////////////////////////////////////////////////////////////////////////////////////////
  // "constants"
  
  public static final class Units {
    public static final String MM = "mm";
    public static final String INCH = "inch";
    // Options for units
    protected static List<String> OPTIONS = Arrays.asList(Units.MM, Units.INCH);
  }
  
  public static final class Directions {
    public static final Boolean CLOCKWISE = true;
    public static final Boolean COUNTERCLOCKWISE = false;
  }
  
  public static final class Type {
    public static final String SIMPLE = "Simple";
    public static final String QUADRATURE = "Quadrature";
    public static final String GRAY = "Gray";
    public static final String BINARY = "Binary";
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  // Primary Properties
  protected SimpleStringProperty type; // see TYPE_*
  protected SimpleStringProperty units; // see MM or INCH
  protected SimpleDoubleProperty outerDiameter; // outer diameter of encoder
  protected SimpleDoubleProperty innerDiameter; // inner diameter of innermost encoder track
  protected SimpleDoubleProperty centerDiameter; // axle/shaft diameter
  protected SimpleIntegerProperty resolution; // encoder resolution
  protected SimpleBooleanProperty inverted; // black on white vs white on black
  protected SimpleBooleanProperty indexed; // true if has index track
  protected SimpleBooleanProperty direction; // CLOCKWISE / COUNTERCLOCKWISE

  /////////////////////////////////////////////////////////////////////////////////////////////////
  // Properties dependent on primary properties
  protected SimpleObjectProperty<UnaryOperator<Integer>> incrementProperty;
  protected SimpleObjectProperty<UnaryOperator<Integer>> decrementProperty;
  protected SimpleIntegerProperty resolutionMin;
  protected SimpleIntegerProperty resolutionMax;
  protected SimpleBooleanProperty directional; // true if encoder is directional
  protected SimpleBooleanProperty indexable; // true if index track possible
  protected SimpleBooleanProperty outerValid; // true if outerDiameter is valid
  protected SimpleBooleanProperty innerValid; // true if innerDiameter is valid
  protected SimpleBooleanProperty centerValid; // true if centerDiameter is valid
  
  // Encoder type to EncoderModel mapping
  protected LinkedHashMap<String, EncoderModel> encoderMap = new LinkedHashMap(); 

  // Observable stuff
  private List<ChangeListener> changeListeners;
  private final List<InvalidationListener> invalidationListeners;
  private ChangeListener changed = (obs, ov, nv) -> {
//    System.out.println("EncoderProperties changed()");
    changeListeners.forEach((ChangeListener cl) -> {
//      System.out.println("notify "+cl.getClass().getName());
      cl.changed(obs, ov, nv);
    });
  };
  
  /////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONSTRUCTORS
  //
  /////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Create new EncoderProperties object which must later be initialized with call to 
   * initialize().
   */
  public EncoderProperties() {
    // Observable
    this.changeListeners = new ArrayList();
    this.invalidationListeners = new ArrayList();

    // Primary properties
    this.type = new SimpleStringProperty();
    this.units = new SimpleStringProperty();
    this.outerDiameter = new SimpleDoubleProperty();
    this.innerDiameter = new SimpleDoubleProperty();
    this.centerDiameter = new SimpleDoubleProperty();
    this.resolution = new SimpleIntegerProperty();
    this.inverted = new SimpleBooleanProperty();
    this.indexed = new SimpleBooleanProperty();
    this.direction = new SimpleBooleanProperty();

    // Dependent properties
    this.resolutionMin = new SimpleIntegerProperty();
    this.resolutionMax = new SimpleIntegerProperty();
    this.indexable = new SimpleBooleanProperty();
    this.directional = new SimpleBooleanProperty();
    this.incrementProperty = new SimpleObjectProperty();
    this.decrementProperty = new SimpleObjectProperty();

    // Map of encoder types to encoder models
    this.encoderMap.put(Type.QUADRATURE, new QuadratureEncoder());
    this.encoderMap.put(Type.SIMPLE, new BasicEncoder());
    this.encoderMap.put(Type.BINARY, new BinaryEncoder());
    this.encoderMap.put(Type.GRAY, new GrayEncoder());

    // Type changeListener to set indexable and directional properties based on encoder type
    this.type.addListener((obs, ov, nv) -> {
      System.out.println("type changed ");
      // Ensure dependent properties are updated
      this.incrementProperty.set(getEncoder().getResolutionIncrement());
      this.decrementProperty.set(getEncoder().getResolutionDecrement());
      this.resolutionMin.set(getEncoder().getMinResolution());
      this.resolutionMax.set(getEncoder().getMaxResolution());
      this.indexable.set(getEncoder().isIndexable());
      this.directional.set(getEncoder().isDirectional());
      
      // Ensure the current resolution is within limits for this encoder type
      if (this.resolution.get() > this.resolutionMax.get()) {
        this.resolution.set(this.resolutionMax.get());
      } else if (this.resolution.get() < this.resolutionMin.get()) {
        this.resolution.set(this.resolutionMin.get());
      }
    });

    // Changes to primary property notifies observers of this object
    this.units.addListener(changed);
    this.outerDiameter.addListener(changed);
    this.innerDiameter.addListener(changed);
    this.centerDiameter.addListener(changed);
    this.resolution.addListener(changed);
    this.inverted.addListener(changed);
    this.indexed.addListener(changed);
    this.direction.addListener(changed);
    this.type.addListener(changed);
    
    // Default values
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
    this.units.set(Units.OPTIONS.get(0));
    this.resolution.set(8);
    this.inverted.set(false);
    this.indexed.set(false);
    this.direction.set(Directions.CLOCKWISE);
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // GETTERS AND SETTERS
  //
  /////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Return list of options for Type widget
   * 
   * @return list of valid Type options as String
   */
  public List<String> getTypeOptions() {
    return new ArrayList<>(this.encoderMap.keySet());
  }

  /**
   * Return list of options for valid unit setting
   *
   * @return list of options
   */
  final public List<String> getUnitOptions() {
    return Units.OPTIONS;
  }

  /**
   * Get the encoder associated with these properties
   * 
   * @return the encoder associated with these properties
   */
  public EncoderModel getEncoder() {
    return (EncoderModel) this.encoderMap.get(this.typeProperty().get());
  }
  
  /**
   * Get the list of EncoderTrack for this encoder, each specifying the information needed to render
   * the encoder.
   *
   * @return list of EncoderTrack objects
   */
  public List<EncoderTrack> getTracks() {
    return getEncoder().getTracks(this.innerDiameterProperty().get(),
        this.outerDiameterProperty().get(),
        this.resolutionProperty().get(),
        this.indexedProperty().get(), this.directionProperty().get()
    );
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // UTILITIES
  //
  /////////////////////////////////////////////////////////////////////////////////////////////////

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
   * Determine if model is valid; use with error checking and notification, and to prevent
   * rendering of encoder while editing values in the UI.
   * @return true if valid, false if not
   */
  public boolean isValid() {
//    System.out.println("isValid()");
    return (this.outerDiameterProperty().get() > this.innerDiameterProperty().get())
        && (this.innerDiameterProperty().get() >= this.centerDiameterProperty().get());
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // PROPERTY GETTERS
  //
  /////////////////////////////////////////////////////////////////////////////////////////////////
  
  /**
   * Return minimum resolution property for the current encoder
   * @return min resolution
   */
  public SimpleIntegerProperty minResolutionProperty() {
    return resolutionMin;
  }

  /**
   * Return maximum resolution property for the current encoder
   * @return max resolution property
   */
  public SimpleIntegerProperty maxResolutionProperty() {
    return resolutionMax;
  }
  
  /**
   * Get the type of encoder, represented as a string; see getTypeOptions()
   * @return type of encoder
   */
  final public SimpleStringProperty typeProperty() {
    return type;
  }

  /**
   * Gets the outer diameter of the encoder disc
   * @return outer diameter
   */
  final public SimpleDoubleProperty outerDiameterProperty() {
    return outerDiameter;
  }

  /**
   * Gets the inner diameter of the encoder disc
   * @return inner diameter
   */
  final public SimpleDoubleProperty innerDiameterProperty() {
    return innerDiameter;
  }

  /**
   * Gets the center diameter of the encoder disc
   * @return center diameter
   */
  final public SimpleDoubleProperty centerDiameterProperty() {
    return centerDiameter;
  }

  /**
   * Gets the units being used; see getUnitOptions()
   * @return units
   */
  final public SimpleStringProperty unitsProperty() {
    return units;
  }

  /**
   * Gets the resolution of the encoder
   * @return resolution
   */
  final public SimpleIntegerProperty resolutionProperty() {
    return this.resolution;
  }

  /**
   * Return whether encoder pattern is inverted or not.
   * @return true if inverted, false if not
   */
  final public SimpleBooleanProperty invertedProperty() {
    return this.inverted;
  }

  /**
   * Return whether an index track is enabled.
   * @return true if index track enabled, false otherwise
   */
  public SimpleBooleanProperty indexedProperty() {
    return this.indexed;
  }

  /**
   * Get direction of rotation of the encoder.
   * @return CLOCKWISE (true), or COUNTERCLOCKWISE(false)
   */
  public BooleanProperty directionProperty() {
    // check validity
    return this.direction;
  }
  
  /**
   * Get indexable property; some encoders don't need/can't have index tracks
   * @return true if encoder is indexable, false otherwise
   */
  public SimpleBooleanProperty indexableProperty() {
    return this.indexable;
  }

  /**
   * Get directional property; basic encoders aren't directional; used to determine
   * if directional widget should be enabled/visible.
   * @return true if directional, false otherwise
   */
  public SimpleBooleanProperty directionalProperty() {
    return this.directional;
  }

  /**
   * Return the property containing the UnaryOperator for incrementing the resolution
   * @return UnaryOperator for resolution increment
   */
  public SimpleObjectProperty<UnaryOperator<Integer>> resolutionIncrementProperty() {
    return incrementProperty;
  }

  /**
   * Return the property containing the UnaryOperator for decrementing the resolution
   * @return UnaryOperator for resolution decrement
   */
  public SimpleObjectProperty<UnaryOperator<Integer>> resolutionDecrementProperty() {
    return decrementProperty;
  }
  
  /////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // CONVERT TO/FROM PROPERTIES
  //
  /////////////////////////////////////////////////////////////////////////////////////////////////
  
  /**
   * Return a Properties object representing this object; used for file save
   * @return Properties object
   */
  public Properties toProperties() {
    Properties p;

    p = new Properties();

    p.setProperty("encoder.type", this.typeProperty().get());
    p.setProperty("encoder.resolution", Integer.toString(this.resolutionProperty().get()));
    p.setProperty("encoder.outerDiameter", Double.toString(this.outerDiameterProperty().get()));
    p.setProperty("encoder.innerDiameter", Double.toString(this.innerDiameterProperty().get()));
    p.setProperty("encoder.centerDiameter", Double.toString(this.centerDiameterProperty().get()));
    p.setProperty("encoder.indexed", Boolean.toString(this.indexedProperty().get()));
    p.setProperty("encoder.inverted", Boolean.toString(this.invertedProperty().get()));
    p.setProperty("encoder.clockwise", Boolean.toString(this.directionProperty().get()));
    p.setProperty("encoder.units", this.unitsProperty().get());
    
    return p;
  }

  /**
   * Set this object's properties based on Properties object; used for file load
   * @param p properties object from which to set this object's properties
   */
  public void fromProperties(Properties p) {
    this.typeProperty().set(p.getProperty("encoder.type"));
    this.resolutionProperty().set(Integer.parseInt(p.getProperty("encoder.resolution")));
    this.outerDiameterProperty().set(Double.parseDouble(p.getProperty("encoder.outerDiameter")));
    this.innerDiameterProperty().set(Double.parseDouble(p.getProperty("encoder.innerDiameter")));
    this.centerDiameterProperty().set(Double.parseDouble(p.getProperty("encoder.centerDiameter")));
    this.indexedProperty().set(Boolean.parseBoolean(p.getProperty("encoder.indexed")));
    this.invertedProperty().set(Boolean.parseBoolean(p.getProperty("encoder.inverted")));
    this.directionProperty().set(Boolean.parseBoolean(p.getProperty("encoder.clockwise")));
    this.unitsProperty().set(p.getProperty("encoder.units"));
  }

  /////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // OBSERVABLE METHODS
  //
  /////////////////////////////////////////////////////////////////////////////////////////////////
  
  /**
   * Add a ChangeListener to be notified if any changes are made to the encoder's properties
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
