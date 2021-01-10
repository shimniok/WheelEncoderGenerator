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

    public static String ABSOLUTE = "absolute";
    public static String INCREMENTAL = "incremental";
    public static String MM = "mm";
    public static String INCH = "inch";
    public static String GRAY = "gray";
    public static String BINARY = "binary";

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
        
        if (this.type.getValue().equals(ABSOLUTE)) {
            result = this.absoluteResolution.getValue();
        } else if (this.type.getValue().equals(INCREMENTAL)) {
            if (this.quadratureTrack.getValue()) result += 1;
            if (this.indexTrack.getValue()) result += 1;
        }
        return result;
    }
   

     /**
     * Returns track number of quadrature track, if enabled.
     * 
     * @return track number of quadrature track or -1 if no index track
     */
    private int quadratureTrackNumber() {
        if (this.getIndexTrack().getValue()) {
            return 2;
        } else {
            return -1;
        }
    }
    
    
    /**
     * Returns track number of index track, if enabled.
     * 
     * @return track number of index track or -1 if no index track
     */
    private int indexTrackNumber() {
        if (this.getIndexTrack().getValue()) {
            return this.quadratureTrackNumber() + 1;
        } else {
            return -1;
        }
    }
    
    
    /* getStripes()
     * 
     * Returns the number of black stripes for a given track
     */
    public int getStripeCount(int whichTrack)
    {
        int stripes = 0;

        if (this.type.getValue().equals(INCREMENTAL) && whichTrack == this.indexTrackNumber())
            stripes = 2;
        else {
            stripes = (int) Math.ceil( 360.0 / Encoder.this.getStripeAngle() );
        }
        // TODO -- do gray vs. binary vs. incremental
    
        return stripes;
    }

    /* getOffset()
     *
     * Returns the offset in degrees of the current track
     */
    public double getAngleOffset(int whichTrack)
    {
        double offset = 0.0;

        // In all cases, offset is zero, except if the encoder is:
        // 1) of type absolute and encoding is Gray Code, in which case, offset
        // depends on which track we're talking about among other things
        // 2) the current track is the quadrature track
        // TODO: clockwise vs counter clockwise
        if (this.type.equals(ABSOLUTE) && this.coding.equals(GRAY)) {
            if (whichTrack == this.absoluteResolution.getValue()-1)
                offset = 0; //-getDegree(whichTrack, 0); // CCW: 0, CW: -getDegree(whichTrack, 0)
            else
                offset = -getStripeAngle(whichTrack, 0)/2; // CW: - CCW: +
        } else if (this.type.equals(INCREMENTAL)) {
            if (whichTrack == quadratureTrackNumber()) {
                offset = getStripeAngle(whichTrack, 0)/2;
            }
            else if (whichTrack == indexTrackNumber()) {
                offset = -getStripeAngle(whichTrack, 0);
            }
        }
        //Debug.println("track="+whichTrack);
        //Debug.println("offset="+offset);
        return offset;
    }
    
    /**
     * Get the angle of a single stripe
     * 
     * @return angle of a single stripe
     */

    private double getStripeAngle()
    {
        double d=0.0;
        if (this.type.getValue().equals(INCREMENTAL)) {
            // The standard encoder has one track and
            // the resolution specifies the number of stripes
            // directly
            d = 360.0 / this.incrementalResolution.getValue(); 
        }
        //Debug.println("degree="+d);
        return d;
    }

    /**
     * Get the angle of specified stripe on specified track
     * 
     * @param whichTrack
     * @param whichStripe
     * @return 
     */
    public double getStripeAngle(int whichTrack, int whichStripe)
    {
        double d=0.0;
        int theTrack = whichTrack;

        if (this.type.getValue().equals(ABSOLUTE)) {
            // With an absolute encoder (gray or binary), the resolution
            // defines the total number of tracks. The resolution for a given
            // track is dependent on the track number.  A 3 track absolute
            // encoder has 2^1 stripes on the inner track, 2^2 on the middle
            // track and 2^3 on the outer track
            // In Gray coding, the innermost track starts the same as binary,
            // but the next track out is a duplicate (only one block of black)
            // and it is offset degree/2 == 90*; the rest of the tracks are
            // same as binary (starting with 2 black stripes), but are offset
            // by degree/2 from the previous track.
            //Debug.println("whichTrack=" + Integer.toString(whichTrack));
            if (this.coding.equals(GRAY) && (this.absoluteResolution.getValue() - theTrack) > 1) {
                //Debug.println("incrementing theTrack");
                theTrack++;
            }
            d = 360.0 / Math.pow(2, resolution - theTrack); // TODO - getTrackResolution(whichTrack)
        }
        else if (this.type.getValue().equals(INCREMENTAL)) {
            // Index track has only two stripes, one small black stripe
            // and one giant white stripe that covers the rest of the track
            if (whichTrack == indexTrackNumber() && whichStripe == 0) {
                d = 360 - Encoder.this.getStripeAngle();
            } else {
                d = Encoder.this.getStripeAngle();
            }
        }
        return d;
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

    // TODO - track width property, should innerdiameter be calculated?
    
    /* Generic type */
    public Encoder() {
        unitOptions = Arrays.asList(this.MM, this.INCH);
        typeOptions = Arrays.asList(this.ABSOLUTE, this.INCREMENTAL);
        codingOptions = Arrays.asList(this.GRAY, this.BINARY);
        
        outerDiameter = new SimpleIntegerProperty(50);
        innerDiameter = new SimpleIntegerProperty(30);
        centerDiameter = new SimpleIntegerProperty(5);
        inverted = new SimpleBooleanProperty(false);
        units = new SimpleStringProperty(unitOptions.get(0));

        type = new SimpleStringProperty(typeOptions.get(0));
        
        // Incremental
        incrementalResolution = new SimpleIntegerProperty(16);
        quadratureTrack = new SimpleBooleanProperty(true);
        indexTrack = new SimpleBooleanProperty(true);
        
        // Absolute
        absoluteResolution = new SimpleIntegerProperty(4);
        coding = new SimpleStringProperty(codingOptions.get(0));
               
    }
    
}
