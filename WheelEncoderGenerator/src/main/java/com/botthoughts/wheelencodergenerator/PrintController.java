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
import java.util.Set;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PaperSource;
import javafx.print.PrintQuality;
import javafx.print.PrintResolution;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
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
//    PageLayout
//    PageRange
    
   
    void setEncoderProperties(EncoderProperties ep) {
        System.out.println("setEncoderPropertes()");
        this.ep = ep;
        preview = new EncoderRenderer(ep, 100, 100);
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
    }
    
    public void doCancel() {
        System.out.println("doCancel()");
        Stage stage = (Stage) cancelUI.getScene().getWindow();
        stage.close();
    }
    
    // TODO: job status
    
    private void updatePrinterList() {
        System.out.println("generatePrinterList()");
        for (Printer p: Printer.getAllPrinters()) {
            destinationUI.getItems().add(p);
        }
    }

    private void updatePaperList() {
        Set<Paper> pl = printer.getPrinterAttributes().getSupportedPapers();
        Paper pp = printer.getPrinterAttributes().getDefaultPaper();
        
        paperUI.getItems().setAll(pl);
        paperUI.getSelectionModel().select(pp);
        // TODO: improve display name
    }
    

    private void updateCopies() {
        int max = printer.getPrinterAttributes().getMaxCopies();
        int c = printer.getPrinterAttributes().getDefaultCopies();
        
        copiesUI.setValueFactory(new IntegerSpinnerValueFactory(1, max, c, 1));
    }
    
    private void updateOrientationList() {
        Set<PageOrientation> po = printer.getPrinterAttributes().getSupportedPageOrientations();
        PageOrientation o = printer.getPrinterAttributes().getDefaultPageOrientation();

        orientationUI.getItems().setAll(po);
        orientationUI.getSelectionModel().select(o);
        // there's a limited set of orientations defined by PageOrientation constants
    }
    
    private void updatePaperSource() {
        Set<PaperSource> ps = printer.getPrinterAttributes().getSupportedPaperSources();
        PaperSource ds = printer.getPrinterAttributes().getDefaultPaperSource();

        sourceUI.getItems().setAll(ps);
        sourceUI.getSelectionModel().select(ds);
        // TODO: improve display name
    }    

    private void updatePrintQuality() {
        Set<PrintQuality> pq = printer.getPrinterAttributes().getSupportedPrintQuality();
        PrintQuality q = printer.getPrinterAttributes().getDefaultPrintQuality();
        
        qualityUI.getItems().setAll(pq);
        qualityUI.getSelectionModel().select(q);
        // TODO: improve display name
    }
    
    private void updatePrintResolution() {
        Set<PrintResolution> pr = printer.getPrinterAttributes().getSupportedPrintResolutions();
        System.out.println(pr);
//        PrintResolution dr = printer.getPrinterAttributes().getDefaultPrintResolution();
//
//        if (pr != null) resolutionUI.getItems().setAll(pr);
//        if (dr != null) resolutionUI.getSelectionModel().select(dr);
    }

    private void updatePreview() {
        Paper paper = (Paper) paperUI.getSelectionModel().getSelectedItem();
        System.out.println(paper.getName());
        PrintResolution res = printer.getPrinterAttributes().getDefaultPrintResolution();
        System.out.println("cross feed res="+res.getCrossFeedResolution());
        System.out.println("feed res="+res.getFeedResolution());
//        res.getCrossFeedResolution()*
//        res.getFeedResolution()*
        double w = paper.getWidth();
        double h = paper.getHeight();
        System.out.println("w="+w);
        System.out.println("h="+h);
        if (preview != null && ep != null) {
            preview.setWidth(w);
            preview.setHeight(h);
//            preview.setScaleX(4);
//            preview.setScaleY(4);
            preview.drawEncoder();
        }
    }

    
    private void updatePrinter(Printer p) {
        System.out.println("Printer changed");
        this.printer = p;
        updateOrientationList();
        updatePaperList();
        updateCopies();
        updateOrientationList();
        updatePaperSource();
        updatePrintQuality();
        updatePrintResolution();
        updatePreview();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize()");
        updatePrinterList();
        ChangeListener cl = (obs, ov, nv) -> {
          updatePrinter((Printer) obs.getValue());
        };
        destinationUI.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            updatePrinter((Printer) obs.getValue());
        });
        destinationUI.getSelectionModel().select(Printer.getDefaultPrinter());
        paperUI.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            updatePreview();
        });
    }

}