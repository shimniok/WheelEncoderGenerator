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
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author mes
 */
public class PrintController implements Initializable {

    @FXML
    DialogPane dialogUI;
    @FXML
    VBox previewWindowUI;
    @FXML
    Canvas previewUI;
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
    
    private SimpleObjectProperty<Printer> printerProperty;
    private SimpleObjectProperty<Paper> paperProperty;
    
//    Printer PrinterAttributes
//    PrintResolution PrinterJob
//    JobSettings Paper
//    PageLayout
//    PageRange
    
    public PrintController() {
        System.out.println("PrintController constructor");
        printerProperty = new SimpleObjectProperty<>();
        paperProperty = new SimpleObjectProperty<>();        
    }
    
    
    void setEncoderProperties(EncoderProperties ep) {
        System.out.println("setEncoderPropertes()");
        this.ep = ep;
        //preview = new EncoderRenderer(ep, 100, 100);
        //previewUI.getChildren().add(preview);
        //preview.drawEncoder();
        System.out.println("setEncoderProperties() exit");
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
        Printer.getAllPrinters().forEach(p -> {
            destinationUI.getItems().add(p);
        });
    }

    private void updatePaperList() {
        System.out.println("updatePaperList()");
        Set<Paper> pl = printer.getPrinterAttributes().getSupportedPapers();
        Paper pp = printer.getPrinterAttributes().getDefaultPaper();
        
        paperUI.getItems().setAll(pl);
        paperUI.getSelectionModel().select(pp);
        // TODO: improve display name
    }
    

    private void updateCopies() {
        System.out.println("updateCopies()");
        int max = printer.getPrinterAttributes().getMaxCopies();
        int c = printer.getPrinterAttributes().getDefaultCopies();
        
        copiesUI.setValueFactory(new IntegerSpinnerValueFactory(1, max, c, 1));
    }
    
    private void updateOrientationList() {
        System.out.println("updateOrientationList()");
        Set<PageOrientation> po = printer.getPrinterAttributes().getSupportedPageOrientations();
        PageOrientation o = printer.getPrinterAttributes().getDefaultPageOrientation();

        orientationUI.getItems().setAll(po);
        orientationUI.getSelectionModel().select(o);
        // there's a limited set of orientations defined by PageOrientation constants
    }
    
    private void updatePaperSource() {
        System.out.println("updatePaperSource()");
        Set<PaperSource> ps = printer.getPrinterAttributes().getSupportedPaperSources();
        PaperSource ds = printer.getPrinterAttributes().getDefaultPaperSource();

        sourceUI.getItems().setAll(ps);
        sourceUI.getSelectionModel().select(ds);
        // TODO: improve display name
    }    

    private void updatePrintQuality() {
        System.out.println("updatePrintQuality()");
        Set<PrintQuality> pq = printer.getPrinterAttributes().getSupportedPrintQuality();
        PrintQuality q = printer.getPrinterAttributes().getDefaultPrintQuality();
        
        qualityUI.getItems().setAll(pq);
        qualityUI.getSelectionModel().select(q);
        // TODO: improve display name
    }
    
    private void updatePrintResolution() {
        System.out.println("updatePrintResolution()");
        Set<PrintResolution> pr = printer.getPrinterAttributes().getSupportedPrintResolutions();
        System.out.println(pr);
//        PrintResolution dr = printer.getPrinterAttributes().getDefaultPrintResolution();
//
//        if (pr != null) resolutionUI.getItems().setAll(pr);
//        if (dr != null) resolutionUI.getSelectionModel().select(dr);
    }

        
    private void updatePaper(GraphicsContext gc, Paper p) {
        double padding=10;
        double w;
        double wPage;
        double wMax;
        double wScale;
        double h;
        double hPage;
        double hMax;
        double hScale;
        double scale;
                
        System.out.println("updatePaper()");

        // Maximum dimensions
        wMax = previewWindowUI.getPrefWidth()-padding*2;
        hMax = previewWindowUI.getPrefHeight()-padding*2;
        System.out.println("wmax="+wMax+" hmax="+hMax);
        
        // Paper dimensions in points
        wPage = p.getWidth();
        hPage = p.getHeight();
        System.out.println("pw="+wPage+" ph="+hPage);
        
        // Compute h/w scaling factors
        wScale = wMax / wPage;
        hScale = hMax / hPage;
        scale = Math.min(wScale, hScale);
        System.out.println("scale="+scale+" wScale="+wScale+" hScale="+hScale);
        
        w = scale * wPage;
        h = scale * hPage;
        System.out.println("w="+w+" h="+h);
        
        previewUI.setWidth(w);
        previewUI.setHeight(h);
        
        double x2 = gc.getCanvas().getWidth();
        double y2 = gc.getCanvas().getHeight();

        // redraw
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, x2, y2);
    }

    
    private void updatePreview() {
        System.out.println("updatePreview()");
        Paper paper = (Paper) paperUI.getSelectionModel().getSelectedItem();
        System.out.println(paper.getName());

        PrintResolution res = printer.getPrinterAttributes().getDefaultPrintResolution();
        System.out.println("cross feed res="+res.getCrossFeedResolution());
        System.out.println("feed res="+res.getFeedResolution());
//        res.getCrossFeedResolution()*
//        res.getFeedResolution()*

        // Get width & height of page in points (1/72nds of an inch)
        double widthPoints = paper.getWidth();
        double heightPoints = paper.getHeight();
        // Determine scaling factor to fit page in preview window
        double scaleWidth = widthPoints / previewUI.getWidth();
        double scaleHeight = heightPoints / previewUI.getHeight();
        System.out.println("scaleWidth="+scaleWidth+" scaleHeight="+scaleHeight);
        // Find raw x and y scaling factor
        // Pick the smallest

    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("initialize()");

        GraphicsContext gc = previewUI.getGraphicsContext2D();

        updatePrinterList();
//        destinationUI.selectionModelProperty().bindBidirectional(printerProperty);
 
        destinationUI.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            System.out.println("Printer changed");
            this.printer = (Printer) nv;
//            updateOrientationList();
            updatePaperList();
            
            
//            updateCopies();
//            updateOrientationList();
//            updatePaperSource();
//            updatePrintQuality();
//            updatePrintResolution();
//            updatePreview();
        });
        
        paperUI.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            System.out.println("paper changed");
            updatePaper(gc, (Paper) nv);
        });
        
//        DropShadow dropShadow = new DropShadow();
//        dropShadow.setRadius(5.0);
//        dropShadow.setOffsetX(10.0);
//        dropShadow.setOffsetY(10.0);
//        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
//        gc.setEffect(dropShadow);
    
        destinationUI.getSelectionModel().select(Printer.getDefaultPrinter());

    }

}