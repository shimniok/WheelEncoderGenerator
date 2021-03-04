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

import javafx.scene.control.SpinnerValueFactory;
import javafx.util.converter.IntegerStringConverter;

/**
 *
 * @author mes
 */
public class ResolutionValueFactory extends SpinnerValueFactory<Integer> {

    private EncoderProperties ep;
    
    public ResolutionValueFactory(EncoderProperties ep) {
        super();
        this.ep = ep;
        setConverter(new IntegerStringConverter());
//        this.valueProperty().addListener((observable, oldvalue, newvalue) -> {
//            if (!this.e.validResolution(newvalue)) {
//              // TODO: find a way to fix the resolution if it's invalid Issue #12
//            }
//        });
        this.setValue(ep.getResolution().get());
    }

//    public void setEncoder(EncoderProperties ep) {
//        this.ep = ep;
//    }
    
    private int bump(int steps, int increment) {
        int v;
        
        v = valueProperty().get();

        while (steps-- > 0 && ep.validResolution(v + increment)) {
            v += increment;
        }    
        
        return v;
    }
    
    @Override
    public void decrement(int i) {
        valueProperty().set(bump(i, -ep.getResolutionIncrement()));
    }

    @Override
    public void increment(int i) {
        valueProperty().set(bump(i, ep.getResolutionIncrement()));
    }
    
}


//    public SpinnerValueFactory getResolutionValueFactory() {
//        return new IntegerSpinnerValueFactory(
//            encoder.getMinResolution(),
//            encoder.getMaxResolution()
//        );
//            
//            @Override
//            public void increment(int i) {
//                while (i-- > 0) {
//                    valueProperty().set(valueProperty().get() + 1);
//                }
//            }
//            
//            @Override
//            public void decrement(int i) {
//                while (i-- > 0) {
//                    valueProperty().set(valueProperty().get() - 1);
//                }
//            }
//
//        };
//    }    

//    @Override
//    public void addListener(ChangeListener cl) {
//        outerDiameter.addListener(cl);
//        innerDiameter.addListener(cl);
//        centerDiameter.addListener(cl);
//        resolution.addListener(cl);
//        inverted.addListener(cl);
//        indexTrack.addListener(cl);
//        direction.addListener(cl);
//        
//    }
//
//    @Override
//    public void removeListener(InvalidationListener il) {
//        outerDiameter.removeListener(il);
//        innerDiameter.removeListener(il);
//        centerDiameter.removeListener(il);
//        resolution.removeListener(il);
//        inverted.removeListener(il);
//        indexTrack.removeListener(il);
//        direction.removeListener(il);
//    }