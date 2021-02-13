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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public final class EncoderProperties implements ObservableValue, ChangeListener {

    // Singleton pattern
    private static EncoderProperties INSTANCE = null;
    
    public static String MM = "mm";
    public static String INCH = "inch";
    public static Boolean CLOCKWISE = true;
    public static Boolean COUNTERCLOCKWISE = false;
    
    /**
     * A hash map relating encoder type to EncoderModels
     */
    protected static Map<String, EncoderModel> encoderMap = Map.ofEntries(
            new AbstractMap.SimpleEntry<String, EncoderModel>("Quadrature", 
                    new QuadratureEncoder()),
            new AbstractMap.SimpleEntry<String, EncoderModel>("Simple", 
                    new BasicEncoder()),
            new AbstractMap.SimpleEntry<String, EncoderModel>("Binary", 
                    new BinaryEncoder()),
            new AbstractMap.SimpleEntry<String, EncoderModel>("Gray", 
                    new GrayEncoder())
        );

    /**
     *
     */
    protected static List<String> unitOptions = Arrays.asList(
            EncoderProperties.MM, EncoderProperties.INCH);

    /**
     * Encoder types
     */
//    public static enum EncoderTypes {
//        QUADRATURE("Quadrature"),
//        SIMPLE("Simple"),
//        BINARY("Binary"),
//        GRAY("Gray");
//        
//        public final String label;
//        
//        private EncoderTypes(String label) {
//            this.label = label;
//        }
//    }
            
    /**
     * A list of strings representing available encoder types
     */
    protected static List<String> typeOptions = Arrays.asList(
            "Quadrature", "Simple", "Binary", "Gray"
    );
            
    protected SimpleStringProperty type;
    protected SimpleStringProperty units; // see this.unitOptions
    protected SimpleDoubleProperty outerDiameter;
    protected SimpleDoubleProperty innerDiameter;
    protected SimpleDoubleProperty centerDiameter;
    protected SimpleIntegerProperty resolution;
    protected SimpleBooleanProperty inverted;
    protected SimpleBooleanProperty indexTrack;
    protected BooleanProperty direction; // see this.CLOCKWISE
    private List<ChangeListener> changeListeners;
    private List<InvalidationListener> invalidationListeners;

//    protected ResolutionValueFactory qvf;
//    protected ResolutionValueFactory svf;
//    protected ResolutionValueFactory bvf;
//    protected ResolutionValueFactory gvf;  
    
    /**
     * Make new encoder properties object; Singleton pattern
     */
    private EncoderProperties() {
//        EncoderProperties.encoderMap.put("Quadrature", new QuadratureEncoder());
//        EncoderProperties.encoderMap.put("Simple", new BasicEncoder());
//        EncoderProperties.encoderMap.put("Binary", new BinaryEncoder());
//        EncoderProperties.encoderMap.put("Gray", new GrayEncoder());
    }
    
    
    /**
     * Returns Singleton instance of EncoderProperites with lazy instantiation
     * and initialization.
     * @return EncoderProperties singleton instance
     */
    public static synchronized EncoderProperties getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EncoderProperties();
                
            INSTANCE.changeListeners = new ArrayList();
            INSTANCE.invalidationListeners = new ArrayList();

//            INSTANCE.qvf = new ResolutionValueFactory((EncoderModel) encoderMap.get("Quadrature"), 4); // TODO initial value
//            INSTANCE.svf = new ResolutionValueFactory((EncoderModel) encoderMap.get("Simple"), 4); // TODO initial value
//            INSTANCE.bvf = new ResolutionValueFactory((EncoderModel) encoderMap.get("Binary"), 2); // TODO initial value
//            INSTANCE.gvf = new ResolutionValueFactory((EncoderModel) encoderMap.get("Gray"), 2); // TODO initial value

            INSTANCE.outerDiameter = new SimpleDoubleProperty(50);
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

            INSTANCE.direction = new SimpleBooleanProperty(CLOCKWISE);
            INSTANCE.direction.addListener(INSTANCE);

            INSTANCE.indexTrack = new SimpleBooleanProperty(false);
            INSTANCE.indexTrack.addListener(INSTANCE);

            // Prime candidate for invalidation listener??
            INSTANCE.type = new SimpleStringProperty(INSTANCE.getTypeOptions().get(0));
            INSTANCE.type.addListener(INSTANCE);        
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

    
    final public SimpleStringProperty getType() {
        return type;
    }
    
    final public void setType(SimpleStringProperty type) {
        if (getTypeOptions().contains(type.getValue())) {
            this.type = type;
            
            System.out.println("type set to "+type.getValue());
        } else {
            System.out.println("typeOptions doesn't contain "+type.getValue());
        }
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
     * Sets the outer diameter of the encoder disc
     * @param outerDiameter 
     */
    final public void setOuterDiameter(SimpleDoubleProperty outerDiameter) {
        System.out.println("setOuterDiameter");
        if (outerDiameter.getValue() >= 0) this.outerDiameter = outerDiameter;
    }

    /**
     * Gets the inner diameter of the encoder disc
     * @return inner diameter
     */
    final public SimpleDoubleProperty getInnerDiameter() {
        return innerDiameter;
    }

    /**
     * Sets the inner diameter of the encoder disc
     * @param innerDiameter 
     */   
    final public void setInnerDiameter(SimpleDoubleProperty innerDiameter) {
        System.out.println("setInnerDiameter");
        if (innerDiameter.getValue() >= 0) this.innerDiameter = innerDiameter;
    }

    /**
     * Gets the center diameter of the encoder disc
     * @return center diameter
     */
    final public SimpleDoubleProperty getCenterDiameter() {
        return centerDiameter;
    }

    /**
     * Sets the center diameter of the encoder disc
     * @param centerDiameter is the center diameter property to set
     */
    final public void setCenterDiameter(SimpleDoubleProperty centerDiameter) {
        System.out.println("setCenterDiameter");
        if (centerDiameter.getValue() >= 0) this.centerDiameter = centerDiameter;
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
     * Sets the units being used; see getUnitOptions()
     * 
     * @param units 
     */
    final public void setUnits(SimpleStringProperty units) {
        System.out.println("setUnits");
        // TODO - check validity
        this.units = units;
    }

    /**
     * Gets the resolution of the encoder
     * @return resolution
     */
    final public SimpleIntegerProperty getResolution() {
        return this.resolution;
    }
    
    /**
     * Sets the resolution of the encoder
     * @param resolution of the encoder
     */
    final public void setResolution(SimpleIntegerProperty resolution) {
        int r = resolution.getValue();

        System.out.println("setResolution");
        
        if (validResolution(r)) {
            this.resolution.set(r);
        }
    }
    
    /**
     * Return whether encoder pattern is inverted or not.
     * @return true if inverted, false if not
     */
    final public SimpleBooleanProperty getInverted() {
        return this.inverted;
    }
    
    /**
     * Set the encoder pattern to be inverted, or not.
     * 
     * @param inverted
     */
    final public void setInverted(SimpleBooleanProperty inverted) {
        System.out.println("setInverted");
        this.inverted = inverted;
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
     * Enable or disable index track.
     * 
     * @param indexTrack true to enable index track
     */
    public void setIndexTrack(SimpleBooleanProperty indexTrack) {
        System.out.println("setIndexTrack");
        this.indexTrack = indexTrack;
    }
    
    /**
     * Set direction of rotation.
     * 
     * @return CLOCKWISE (true), or COUNTERCLOCKWISE(false)
     */
    public BooleanProperty getDirection() {
        // check validity
        return this.direction;
    }

    /**
     * Set direction of rotation.
     * 
     * @param direction is either CLOCKWISE (true), or COUNTERCLOCKWISE(false)
     */
    public void setDirection(BooleanProperty direction) {
        System.out.println("setDirection");
        this.direction = direction;
    }

    /**
     * Get the encoder associated with these properties
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
     * @return true if valid, false if not
     */
    public boolean isValid() {
        return (this.getOuterDiameter().get() > this.getInnerDiameter().get()) &&
                (this.getInnerDiameter().get() >= this.getCenterDiameter().get());
    }
    
    

    /**
     * Return a Properties object representing this object.
     * @return Properties object
     */
    public Properties toProperties() {
        Properties p;
        
        p = new Properties();
        
        p.setProperty("encoder.type", this.getType().get());
        p.setProperty("encoder.resolution", this.getResolution().toString());
        p.setProperty("encoder.centerDiameter", this.getCenterDiameter().toString());
        p.setProperty("encoder.innerDiameter", this.getInnerDiameter().toString());
        p.setProperty("encoder.outerDiameter", this.getOuterDiameter().toString());
        p.setProperty("encoder.indexTrack", this.getIndexTrack().toString());
        p.setProperty("encoder.inverted", this.getInverted().toString());
        p.setProperty("encoder.clockwise", this.getDirection().toString());
        p.setProperty("encoder.units", this.getUnits().get());
        
        return p;
    }
    
    /**
     * Set this object's properties based on Properties object
     * @param p properties object from which to set this object's properties
     */
    public void fromProperties(Properties p) {
        this.setType(new SimpleStringProperty(p.getProperty("encoder.type")));
        this.setResolution(new SimpleIntegerProperty(
                Integer.parseInt(p.getProperty("encoder.resolution"))));
        this.setCenterDiameter(new SimpleDoubleProperty(
                Double.parseDouble(p.getProperty("encoder.centerDiameter"))));
        this.setInnerDiameter(new SimpleDoubleProperty(
                Double.parseDouble(p.getProperty("encoder.innerDiameter"))));
        this.setOuterDiameter(new SimpleDoubleProperty(
                Double.parseDouble(p.getProperty("encoder.outerDiameter"))));
        this.setIndexTrack(new SimpleBooleanProperty(
                Boolean.parseBoolean(p.getProperty("encoder.indexTrack"))));
        this.setInverted(new SimpleBooleanProperty(
                Boolean.parseBoolean(p.getProperty("encoder.inverted"))));
        this.setDirection(new SimpleBooleanProperty(
                Boolean.parseBoolean(p.getProperty("encoder.inverted"))));
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    
//    /**
//     * Return the appropriate spinner value factory based on type
//     * @return value factory or null if unexpected type value
//     */
//    public ResolutionSpinnerValueFactory getValueFactory() {
//        switch (type.get()) {
//            case "Quadrature":
//                return qvf;
//            case "Simple":
//                return svf;
//            case "Binary":
//                return bvf;
//            case "Gray":
//                return gvf;
//            default:
//                System.out.println("Unrecognized type, can't find value factory");
//                return null;
//        }
//    }

}
