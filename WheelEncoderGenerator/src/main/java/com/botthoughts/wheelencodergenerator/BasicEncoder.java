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
import java.util.List;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mes
 */
public class BasicEncoder implements EncoderInterface {
    
    /**
     * Gets the outer diameter of the encoder disc
     * @return outer diameter
     */
    @Override    
    final public SimpleDoubleProperty getOuterDiameter() {
        return outerDiameter;
    }

    /**
     * Sets the outer diameter of the encoder disc
     * @param outerDiameter 
     */
    @Override    
    final public void setOuterDiameter(SimpleDoubleProperty outerDiameter) {
        if (outerDiameter.getValue() >= 0) this.outerDiameter = outerDiameter;
    }

    /**
     * Gets the inner diameter of the encoder disc
     * @return inner diameter
     */
    @Override    
    final public SimpleDoubleProperty getInnerDiameter() {
        return innerDiameter;
    }

    /**
     * Sets the inner diameter of the encoder disc
     * @param innerDiameter 
     */   
    @Override
    final public void setInnerDiameter(SimpleDoubleProperty innerDiameter) {
        if (innerDiameter.getValue() >= 0) this.innerDiameter = innerDiameter;
    }

    
    @Override
    final public SimpleDoubleProperty getCenterDiameter() {
        return centerDiameter;
    }

    @Override
    final public void setCenterDiameter(SimpleDoubleProperty centerDiameter) {
        if (centerDiameter.getValue() >= 0) this.centerDiameter = centerDiameter;
    }

    /**
     * Gets the units being used; see getUnitOptions()
     *
     * @return units
     */
    @Override
    final public SimpleStringProperty getUnits() {
        return units;
    }

    /**
     * Sets the units being used; see getUnitOptions()
     * 
     * @param units 
     */
    @Override
    final public void setUnits(SimpleStringProperty units) {
        // TODO - check validity
        this.units = units;
    }

    /**
     * Determine if specified resolution is valid for this encoder
     * 
     * @param resolution
     * @return true if valid, false otherwise
     */
    @Override
    public boolean validResolution(int resolution) {
        return resolution >= 0;
    }

    /**
     * Gets the resolution of the encoder
     * @return resolution
     */
    @Override 
    final public SimpleIntegerProperty getResolution() {
        return this.resolution;
    }
    
    /**
     * Sets the resolution of the encoder
     * @param resolution of the encoder
     */
    @Override
    final public void setResolution(SimpleIntegerProperty resolution) {
        int r = resolution.getValue();
        
        if (validResolution(r)) {
            this.resolution.set(r);
        }
    }
    
    /**
     * Return list of options for valid unit setting
     * @return list of options
     */
    @Override
    final public List<String> getUnitOptions() {
        return unitOptions;
    }

    /**
     * Return whether encoder pattern is inverted or not.
     * @return true if inverted, false if not
     */
    @Override
    final public SimpleBooleanProperty getInverted() {
        return this.inverted;
    }
    
    /**
     * Set the encoder pattern to be inverted, or not
     * @param inverted
     */
    @Override
    final public void setInverted(SimpleBooleanProperty inverted) {
        this.inverted = inverted;
    }
    
    @Override
    final public SimpleIntegerProperty getDirection() {
        // check validity
        return this.direction;
    }

    @Override
    final public void setDirection(SimpleIntegerProperty direction) {
        this.direction = direction;
    }

    /**
     * Return list of ordered Track objects for this encoder.
     * Track information is computed on-demand
     * @return list of tracks
     */
    public List<EncoderTrack> getTracks() {

        tracks = new ArrayList<>();
        
        EncoderTrack outer = new EncoderTrack(outerDiameter.getValue(),
                innerDiameter.getValue(), 0, 360.0/resolution.getValue(), 
                resolution.getValue());
        tracks.add(outer);
        
        return tracks;
    }
    
    protected SimpleDoubleProperty outerDiameter;
    protected SimpleDoubleProperty innerDiameter;
    protected SimpleDoubleProperty centerDiameter;
    protected SimpleBooleanProperty inverted;
    protected final List<String> unitOptions;
    protected SimpleStringProperty units;
    protected SimpleIntegerProperty resolution; // number of bits
    protected SimpleIntegerProperty direction;
    protected List<EncoderTrack> tracks;

    public BasicEncoder() {
        this.outerDiameter = new SimpleDoubleProperty(50);
        this.innerDiameter = new SimpleDoubleProperty(30);
        this.centerDiameter = new SimpleDoubleProperty(10);
        this.inverted = new SimpleBooleanProperty(false);
        this.unitOptions = Arrays.asList(BasicEncoder.MM, BasicEncoder.INCH);
        this.units = new SimpleStringProperty(unitOptions.get(0));
        this.resolution = new SimpleIntegerProperty(16);
        this.direction = new SimpleIntegerProperty(BasicEncoder.CLOCKWISE);
    }

}
