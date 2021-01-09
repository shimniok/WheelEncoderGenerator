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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mes
 */
final public class Encoder {

    public SimpleStringProperty getType() {
        return type;
    }

    public void setType(SimpleStringProperty type) {
        this.type = type;
    }

    public SimpleIntegerProperty getResolution() {
        return resolution;
    }

    public void setResolution(SimpleIntegerProperty resolution) {
        this.resolution = resolution;
    }

    public SimpleIntegerProperty getOuterDiameter() {
        return outerDiameter;
    }

    public void setOuterDiameter(SimpleIntegerProperty outerDiameter) {
        this.outerDiameter = outerDiameter;
    }

    public SimpleIntegerProperty getInnerDiameter() {
        return innerDiameter;
    }

    public void setInnerDiameter(SimpleIntegerProperty innerDiameter) {
        this.innerDiameter = innerDiameter;
    }

    public SimpleIntegerProperty getCenterDiameter() {
        return centerDiameter;
    }

    public void setCenterDiameter(SimpleIntegerProperty centerDiameter) {
        this.centerDiameter = centerDiameter;
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
   
    private SimpleStringProperty type;
    private SimpleIntegerProperty resolution;
    private SimpleIntegerProperty outerDiameter;
    private SimpleIntegerProperty innerDiameter;
    private SimpleIntegerProperty centerDiameter;
    private SimpleBooleanProperty inverted;
    private SimpleStringProperty units;
    private List<String> unitOptions;
    private List<String> typeOptions;

    /* incremental encoder properties */
    private SimpleBooleanProperty quadratureTrack;
    private SimpleBooleanProperty indexTrack;   
    
    /* absolute encoder properties */
    private SimpleStringProperty coding;

    /* Generic type */
    public Encoder() {
        
        unitOptions = Arrays.asList("mm", "in");
        typeOptions = Arrays.asList("absolute", "incremental");
        
        outerDiameter = new SimpleIntegerProperty(50);
        innerDiameter = new SimpleIntegerProperty(20);
        centerDiameter = new SimpleIntegerProperty(5);
        inverted = new SimpleBooleanProperty(false);
        type = new SimpleStringProperty(typeOptions.get(0));
        units = new SimpleStringProperty(typeOptions.get(0));
        
    }
    
}
