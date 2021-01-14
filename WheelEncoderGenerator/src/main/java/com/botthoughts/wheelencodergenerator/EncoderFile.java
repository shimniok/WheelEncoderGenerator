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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mes
 */
public class EncoderFile {
    
    Properties p;
    
    EncoderFile() {
    }

    /* storeProperties()
     *
     * The properties object represents what is stored on disk. It's only called from
     * the routine used to save the object's data to disk.
     */
    private void storeProperties(OutputStream out, EncoderProperties ep) {
        
        p.setProperty("encoder.type", ep.getType().get());
        p.setProperty("encoder.resolution", ep.getResolution().toString());
        p.setProperty("encoder.centerDiameter", ep.getCenterDiameter().toString());
        p.setProperty("encoder.innerDiameter", ep.getInnerDiameter().toString());
        p.setProperty("encoder.outerDiameter", ep.getOuterDiameter().toString());
        p.setProperty("encoder.indexTrack", ep.getIndexTrack().toString());
        p.setProperty("encoder.inverted", ep.getInverted().toString());
        p.setProperty("encoder.clockwise", ep.getDirection().toString());
        p.setProperty("encoder.units", ep.getUnits().get());
        try {
            p.store(out, "WheelEncoder Generator v2 format");
        } catch (IOException ex) {
            ex.printStackTrace(); // TODO - handle gracefully
        }
    }

    /* loadProperties()
     *
     * The properties object represents what is stored on disk. Load the properties
     * file into the properties object, then immediately set the object's attributes
     * to the corresponding properties.
     */
    private void loadProperties(InputStream in, EncoderProperties ep) {
        ep.setType(new SimpleStringProperty(p.getProperty("encoder.type")));
        ep.setResolution(new SimpleIntegerProperty(Integer.parseInt(p.getProperty("encoder.resolution"))));
        ep.setCenterDiameter(new SimpleDoubleProperty(Double.parseDouble(p.getProperty("encoder.centerDiameter"))));
        ep.setInnerDiameter(new SimpleDoubleProperty(Double.parseDouble(p.getProperty("encoder.innerDiameter"))));
        ep.setOuterDiameter(new SimpleDoubleProperty(Double.parseDouble(p.getProperty("encoder.outerDiameter"))));
        ep.setIndexTrack(new SimpleBooleanProperty(Boolean.parseBoolean(p.getProperty("encoder.indexTrack"))));
        ep.setInverted(new SimpleBooleanProperty(Boolean.parseBoolean(p.getProperty("encoder.inverted"))));
        ep.setDirection(new SimpleBooleanProperty(Boolean.parseBoolean(p.getProperty("encoder.inverted"))));
        try {
            p.load(in);
        } catch (IOException ex) {
            ex.printStackTrace(); // TODO - handle gracefully
        }
    }
    
}
