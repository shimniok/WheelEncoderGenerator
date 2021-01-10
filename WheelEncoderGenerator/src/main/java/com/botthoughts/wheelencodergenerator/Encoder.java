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

import java.util.Arrays;
import java.util.List;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mes
 */
final public class Encoder implements java.io.Serializable {

    private static String ABSOLUTE = "absolute";
    private static String INCREMENTAL = "incremental";
    private static String MM = "mm";
    private static String INCH = "inch";
    private static String GRAY = "gray";
    private static String BINARY = "binary";

    public boolean checkDiameters() {
        Integer i = this.innerDiameter.getValue();
        Integer o = this.outerDiameter.getValue();
        Integer c = this.centerDiameter.getValue();
        
        if (c > i || c > o || i > o) {
            return false;
        }
        
        return true;
    }
    
    public SimpleStringProperty getType() {
        return type;
    }

    public void setType(SimpleStringProperty type) {
        if (this.type.equals(this.ABSOLUTE) || this.type.equals(this.INCREMENTAL)) {
            this.type = type;
        }
    }

    public SimpleIntegerProperty getAbsoluteResolution() {
        return absoluteResolution;
    }

    public void setAbsoluteResolution(SimpleIntegerProperty absoluteResolution) {
        if (absoluteResolution.getValue() >= 0) this.absoluteResolution = absoluteResolution;
    }

    public SimpleIntegerProperty getIncrementalResolution() {
        return incrementalResolution;
    }

    public void setIncrementalResolution(SimpleIntegerProperty incrementalResolution) {
        if (incrementalResolution.getValue() >= 2) this.incrementalResolution = incrementalResolution;
    }
   
    public SimpleIntegerProperty getOuterDiameter() {
        return outerDiameter;
    }

    public void setOuterDiameter(SimpleIntegerProperty outerDiameter) {
        if (outerDiameter.getValue() >= 0) this.outerDiameter = outerDiameter;
    }

    public SimpleIntegerProperty getInnerDiameter() {
        return innerDiameter;
    }

    public void setInnerDiameter(SimpleIntegerProperty innerDiameter) {
        if (innerDiameter.getValue() >= 0) this.innerDiameter = innerDiameter;
    }

    public SimpleIntegerProperty getCenterDiameter() {
        return centerDiameter;
    }

    public void setCenterDiameter(SimpleIntegerProperty centerDiameter) {
        if (centerDiameter.getValue() >= 0) this.centerDiameter = centerDiameter;
    }

    public SimpleBooleanProperty getInverted() {
        return inverted;
    }

    public void setInverted(SimpleBooleanProperty inverted) {
        this.inverted = inverted;
    }

    public SimpleStringProperty getUnits() {
        return units;
    }

    public void setUnits(SimpleStringProperty units) {
        this.units = units;
    }

    public SimpleBooleanProperty getQuadratureTrack() {
        return quadratureTrack;
    }

    public void setQuadratureTrack(SimpleBooleanProperty quadratureTrack) {
        this.quadratureTrack = quadratureTrack;
    }

    public SimpleBooleanProperty getIndexTrack() {
        return indexTrack;
    }

    public void setIndexTrack(SimpleBooleanProperty indexTrack) {
        this.indexTrack = indexTrack;
    }

    public SimpleStringProperty getCoding() {
        return coding;
    }

    public void setCoding(SimpleStringProperty coding) {
        this.coding = coding;
    }
       
    public List<String> getTypeOptions() {
        return typeOptions;
    }

    public List<String> getUnitOptions() {
        return unitOptions;
    }
    
    public List<String> getCodingOptions() {
        return codingOptions;
    }
    
    /**
     * Returns the number of tracks in the encoder, based on its type and various options.
     * This method is helpful for rendering the encoder. Absolute encoders have a track count
     * of base-2 log of its resolution. For example, an encoder that can track 16 positions
     * is a 4-bit encoder (2^4 = 16), thus 4 tracks. An incremental encoder will have 1 track
     * at a minimum, and may have 1 index track and 1 quadrature track for a maximum of 3.
     * 
     * @return number of tracks in the encoder as Integer
     */
    public int getTrackCount() {
        Integer result=1;
        
        if (this.type.equals(this.ABSOLUTE)) {
            result = this.absoluteResolution.getValue();
        } else if (this.type.equals(this.INCREMENTAL)) {
            if (this.quadratureTrack.getValue()) result += 1;
            if (this.indexTrack.getValue()) result += 1;
        }
        return result;
    }

    private SimpleStringProperty type;
    private SimpleIntegerProperty resolution; // TODO use multiple resolutions?
    private SimpleIntegerProperty absoluteResolution; // TODO use multiple resolutions?
    private SimpleIntegerProperty incrementalResolution; // TODO use multiple resolutions?
    private SimpleIntegerProperty outerDiameter;
    private SimpleIntegerProperty innerDiameter;
    private SimpleIntegerProperty centerDiameter;
    private SimpleBooleanProperty inverted;
    private SimpleStringProperty units;
    private final List<String> unitOptions;
    private final List<String> typeOptions;

    /* incremental encoder properties */
    private SimpleBooleanProperty quadratureTrack;
    private SimpleBooleanProperty indexTrack;   
    
    /* absolute encoder properties */
    private final List<String> codingOptions;
    private SimpleStringProperty coding;

    /* Generic type */
    public Encoder() {
        unitOptions = Arrays.asList(this.MM, this.INCH);
        typeOptions = Arrays.asList(this.ABSOLUTE, this.INCREMENTAL);
        codingOptions = Arrays.asList(this.GRAY, this.BINARY);
        
        outerDiameter = new SimpleIntegerProperty(50);
        innerDiameter = new SimpleIntegerProperty(20);
        centerDiameter = new SimpleIntegerProperty(5);
        inverted = new SimpleBooleanProperty(false);
        units = new SimpleStringProperty(unitOptions.get(0));

        type = new SimpleStringProperty(typeOptions.get(0));
        
        // Incremental
        quadratureTrack = new SimpleBooleanProperty(true);
        indexTrack = new SimpleBooleanProperty(true);
        
        // Absolute
        coding = new SimpleStringProperty(codingOptions.get(0));
               
    }
    
}
