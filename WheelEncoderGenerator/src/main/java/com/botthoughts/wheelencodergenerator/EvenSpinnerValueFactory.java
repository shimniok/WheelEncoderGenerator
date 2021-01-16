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
public class EvenSpinnerValueFactory extends SpinnerValueFactory<Integer> {

    private int min;
    private int max;
    
    public EvenSpinnerValueFactory(int min, int max, int initialValue) {
        super();
        if (min < max) {
            this.min = min;
            this.max = max;
        }
        setConverter(new IntegerStringConverter());
        this.valueProperty().addListener((observable, oldvalue, newvalue) -> {
            if (!validValue(newvalue)) {
                if (validValue(newvalue+1)) {
                    valueProperty().set(newvalue+1);
                } else if (validValue(newvalue-1)) {
                    valueProperty().set(newvalue-1);
                }
            }
        });
        this.setValue(initialValue);
    }
            
    /**
     * Determine if specified value is even and between min and max
     * @param value to test
     * @return true if valid, false otherwise
     */
    public final boolean validValue(int value) {
        return value % 2 == 0 && min <= value && value <= max;
    }
    
    @Override
    public void decrement(int i) {
        int v;
        
        v = valueProperty().get();
        while (i-- > 0 && validValue(v-2)) {
            v -= 2;
        }    
        valueProperty().set(v);
    }

    @Override
    public void increment(int i) {
        int v;
        
        v = valueProperty().get();
        while (i-- > 0 && validValue(v+2)) {
            v += 2;
        }    
        valueProperty().set(v);
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