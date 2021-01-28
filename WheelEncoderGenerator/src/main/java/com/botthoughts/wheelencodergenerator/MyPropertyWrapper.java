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

import java.lang.*;
import java.util.*;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.*;

/**
 * MyPropertyWrapper provides to a subclass the ability to easily manage a 
 * fixed set of properties and attach ChangeListeners to monitor either 
 * individual properties or to the entire collection of properties.
 * @author mes
 */
abstract public class MyPropertyWrapper implements ObservableValue {

    private List<ChangeListener> cls;
    private Map<String, Property> properties;
    private MapProperty<String, Property> plist;

    /**
     * Subclass is expected to implement a default constructor calling
     * super() with a list of permitted keys.
     * @param keys is a list of strings that become keys
     */
    protected MyPropertyWrapper(String ... keys) {
        cls = new ArrayList<>();
        properties = new HashMap();
        for (String k: keys) {
            properties.put(k, null);
        }
    }
    private MyPropertyWrapper() {
        throw new UnsupportedOperationException("Constructor must be supplied with list of allowable keys");
    }
    

    /**
     * Associates the specified value with the specified key, but only if the
     * key was provided in the constructor as an allowable key
     * @param key
     * @param value 
     */
    public void put(String key, Property value) {
        Property previous = properties.replace(key, value);
        if (previous == null) {
            throw new UnsupportedOperationException("Illegal key");
        }
        
        this.callChangeListeners(previous, value);
    }

    public Property get(String key) {
        return properties.get(key);
    }
    
    
    private void callChangeListeners(Property previous, Property current) {
        cls.forEach(cl -> {
            cl.changed(this, previous, current);
        });
    }
    
    private void callInvalidationListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void addListener(ChangeListener cl) {
        cls.add(cl);
    }

    @Override
    public void removeListener(ChangeListener cl) {
        cls.remove(cl);
    }

    @Override
    public Object getValue() {
        return properties;
    }

    @Override
    public void addListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeListener(InvalidationListener il) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
