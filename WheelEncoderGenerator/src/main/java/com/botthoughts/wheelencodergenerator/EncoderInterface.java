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

import java.util.List;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author mes
 */
public interface EncoderInterface extends java.io.Serializable {

    public static String GRAY = "gray"; // a type of absolute
    public static String BINARY = "binary"; // a type of absolute
    public static String INCREMENTAL = "incremental";
    public static String MM = "mm";
    public static String INCH = "inch";
    public static Integer CLOCKWISE = 0;
    public static Integer COUNTERCLOCKWISE = 1;

    /**
     * Verifies the supplied resolution is valid
     * @param resolution
     * @return validity of resolution
     */
    public boolean validResolution(int resolution);
            
    /**
     * Gets the resolution of the encoder
     * @return resolution
     */
    public SimpleIntegerProperty getResolution();

    /**
     * Sets the resolution of the encoder
     * @param resolution of the encoder
     */
    public void setResolution(SimpleIntegerProperty resolution);

    /**
     * Gets the outer diameter of the encoder disc
     * @return outer diameter
     */
    public SimpleDoubleProperty getOuterDiameter();

    /**
     * Sets the outer diameter of the encoder disc
     * @param outerDiameter 
     */
    public void setOuterDiameter(SimpleDoubleProperty outerDiameter);

    /**
     * Gets the inner diameter of the encoder disc
     * @return inner diameter
     */
    public SimpleDoubleProperty getInnerDiameter();

    /**
     * Sets the inner diameter of the encoder disc
     * @param innerDiameter 
     */
    public void setInnerDiameter(SimpleDoubleProperty innerDiameter);

    /**
     * Gets the center hole diameter of the encoder disc
     * @return inner diameter
     */
    public SimpleDoubleProperty getCenterDiameter();

    /**
     * Sets the center hole diameter of the encoder disc
     * @param centerDiameter 
     */
    public void setCenterDiameter(SimpleDoubleProperty centerDiameter);

    /**
     * Gets the units being used; see getUnitOptions()
     *
     * @return units
     */    
    public SimpleStringProperty getUnits();
        
    /**
     * Sets the units being used; see getUnitOptions()
     * 
     * @param units 
     */
    public void setUnits(SimpleStringProperty units);
    
    /**
     * Return list of options for valid unit setting
     * @return list of options
     */
    public List<String> getUnitOptions();

    /**
     * Get the intended direction of rotation for the encoder
     * @return the direction
     */
    public SimpleIntegerProperty getDirection();
    
    /**
     * Set the intended direction of rotation for the encoder
     * @param direction
     */
    public void setDirection(SimpleIntegerProperty direction);
    
    /**
     * Return whether encoder pattern is inverted or not.
     * @return true if inverted, false if not
     */
    public SimpleBooleanProperty getInverted();
    
    /**
     * Set the encoder pattern to be inverted, or not
     * @param inverted
     */
    public void setInverted(SimpleBooleanProperty inverted);
    
}
