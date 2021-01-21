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

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SingleSelectionModel;

/**
 *
 * @author mes
 */
public class PrintController implements Initializable {

    @FXML
    ListView listView;
    
    public void show() {
    }

    private void generatePrinterList() {
        Printer dp = Printer.getDefaultPrinter();
        
        listView.getItems().add(dp);
        listView.getSelectionModel().selectFirst();
        for (Printer p: Printer.getAllPrinters()) {
            if (!dp.equals(p)) {
                listView.getItems().add(p);
            }
        }

        listView.getItems().addAll(Printer.getAllPrinters());
        Printer.defaultPrinterProperty();
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generatePrinterList();
    }
    
}