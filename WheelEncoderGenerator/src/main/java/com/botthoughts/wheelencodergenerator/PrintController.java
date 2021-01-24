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
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author mes
 */
public class PrintController implements Initializable {

    @FXML
    DialogPane dialogUI;
    @FXML
    AnchorPane previewUI;
    @FXML
    ComboBox destinationUI;
    @FXML
    ComboBox paperUI;
    @FXML
    ComboBox orientationUI;
    @FXML
    Spinner copiesUI;
    @FXML
    ComboBox sourceUI;
    @FXML
    ComboBox collationUI;
    @FXML
    ComboBox qualityUI;
    @FXML
    ComboBox resolutionUI;
    @FXML
    Button cancelUI;
    @FXML
    Button printUI;

    private Printer printer;
    private PrinterJob job;
    private EncoderRenderer renderer;
    private EncoderRenderer preview;
    private EncoderProperties ep;
    
    
//    Printer PrinterAttributes
//    PrintResolution PrinterJob
//    JobSettings Paper
//    PaperSource PageLayout
//    PageRange
    
   
    void setEncoderProperties(EncoderProperties ep) {
        System.out.println("setEncoderPropertes()");
        this.ep = ep;
        preview = new EncoderRenderer(ep, 500, 500);
        previewUI.getChildren().add(preview);
        preview.drawEncoder();
    }

   
    public void doPrint() {
        System.out.println("doPrint()");
        job = PrinterJob.createPrinterJob();
        Node node;
        
        if (job != null && renderer != null) {
            boolean printed = job.printPage(renderer);
            if (printed) {
                job.endJob();
            } else {
                System.out.println("Error printing");
            }
        } else {
            System.out.println("Error setting up job"); // TODO: error handling
        }
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
        Stage stage = (Stage) cancelUI.getScene().getWindow();
        stage.close();
    }
    
    // TODO: job status
    
    private void updatePrinterList() {
        System.out.println("generatePrinterList()");
        Printer dp = Printer.getDefaultPrinter();

//        destinationUI.getItems().add(dp);
        for (Printer p: Printer.getAllPrinters()) {
            destinationUI.getItems().add(p);
        }
        destinationUI.getSelectionModel().select(dp);
    }

    private void updatePaperList() {
        paperUI.getItems().setAll(printer.getPrinterAttributes().getSupportedPapers());
        paperUI.getSelectionModel().select(printer.getPrinterAttributes().getDefaultPaper());
//        sizeUI.setCellFactory((item) -> {
//            String s;
//            Paper i = (Paper) item;
//            s = i.getName();
//            return s;
//        });
    }
    

    private void updateMaxCopies() {
        
    }
    
    private void updateOrientationList() {
        orientationUI.getItems().setAll(printer.getPrinterAttributes().getSupportedPageOrientations());
        orientationUI.getSelectionModel().select(printer.getPrinterAttributes().getDefaultPageOrientation());
        // there's a limited set of orientations defined by PageOrientation constants
    }
    
    private void updatePaperSource() {
        
    }    
    
    private void updatePrintColor() {
        
    }

    private void updatePrintQuality() {
        
    }
    
    private void updatePrintResolution() {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize()");
        destinationUI.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            System.out.println("Printer changed");
            this.printer = (Printer) obs.getValue();
            updateOrientationList();
            updatePaperList();
        });
        updatePrinterList();
    }

}