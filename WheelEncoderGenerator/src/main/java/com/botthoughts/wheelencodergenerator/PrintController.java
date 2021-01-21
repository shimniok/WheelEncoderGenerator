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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Printer;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author mes
 */
public class PrintController implements Initializable {

    @FXML
    AnchorPane previewUI;
    @FXML
    ComboBox destinationUI;
    @FXML
    ComboBox sizeUI;
    @FXML
    ComboBox layoutUI;
    @FXML
    Button cancelUI;
    @FXML
    Button printUI;

    private List<PageSize> sizes;
    
    private class PageSize {
        private String name;
        private double length;
        private double width;
        
        public String toString() {
            return name;
        }
        
        public PageSize(String name, double length, double width) {
            this.name = name;
            this.length = length;
            this.width = width;
        }
        
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getLength() {
            return length;
        }

        public void setLength(double length) {
            this.length = length;
        }

        public double getWidth() {
            return width;
        }

        public void setWidth(double width) {
            this.width = width;
        }
    }
   
    private void generatePageSizes() {
            //  TODO: add more sizes        
//        Legal (US)	215.9 x 355.6	8.5 x 14
//        Ledger (US)	279.4 x 431.8	11 X 17
//        A5	5-7/8 x 8-1/4	148 x 210
//        A6	4-1/8 x 5-7/8	105 x 148
//        A7	2-15/16 x 4-1/8	74 x 105
        sizeUI.getItems().addAll(
                new PageSize("Letter (US)", 279.4, 215.9),
                new PageSize("A4", 297, 210)
        );
        sizeUI.getSelectionModel().selectFirst();
    }
    

    public void doPrint() {
        System.out.println("doPrint()");
//        double pr = 600; // dpi
//        double w = 8.5; // inches
//        
//        EncoderRenderer er = new EncoderRenderer(ep);
//        
//        er.setWidth(w*pr);
//        er.setHeight(w*pr);
//
//        er.drawEncoder();
//        
//        PrinterJob job = PrinterJob.createPrinterJob();
//        if (job != null) {
//            boolean success = job.printPage(er);
//            if (success) {
//                job.endJob();
//            }
//        }
    }
    
    public void doCancel() {
        System.out.println("doCancel()");
    }
    
    private void generatePrinterList() {
        Printer dp = Printer.getDefaultPrinter();

        destinationUI.getItems().add(dp);
        destinationUI.getSelectionModel().selectFirst();
        for (Printer p: Printer.getAllPrinters()) {
            if (!dp.equals(p)) {
                destinationUI.getItems().add(p);
            }
        }
    }

    private void generateLayoutList() {
        layoutUI.getItems().addAll(Arrays.asList("Portrait", "Landscape"));
        layoutUI.getSelectionModel().selectFirst();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generatePrinterList();
        generateLayoutList();
        generatePageSizes();
    }

}