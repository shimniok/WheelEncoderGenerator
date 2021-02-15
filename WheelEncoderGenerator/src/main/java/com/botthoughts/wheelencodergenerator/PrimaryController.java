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

import java.io.File;
import java.io.IOException;
import static java.lang.Double.NaN;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PrintQuality;
import javafx.print.PrintResolution;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.util.converter.DoubleStringConverter;

public class PrimaryController implements Initializable {

//    BasicEncoder basicEncoder;
//    QuadratureEncoder quadratureEncoder;
//    BinaryEncoder binaryEncoder;
//    GrayEncoder grayEncoder;
//    EncoderModel currentEncoder;
    private EncoderView encoderPreview;
    private File currentFile;
    
//    private Color bg; // background
//    private Color fg; // foreground

    @FXML
    Canvas encoderUI;
    @FXML
    ComboBox typeUI;
    @FXML
    Spinner resolutionUI;
    @FXML
    TextField outerUI;
    @FXML
    TextField innerUI;
    @FXML
    TextField centerUI;
    @FXML
    ComboBox unitsUI;
    @FXML
    ToggleGroup directionUI;
    @FXML
    ToggleButton cwUI;
    @FXML
    ToggleButton ccwUI;
    @FXML
    ToggleButton invertedUI;
    @FXML
    ToggleButton indexUI;
    @FXML
    AnchorPane canvasContainer;

    @FXML
    public void saveFileAs() {
        FileChooser fc = new FileChooser();
        File f = fc.showSaveDialog(App.stage);
        try {
            currentFile = f; // only do this if save succeeds!
            System.out.println("file="+f.getCanonicalPath());
        } catch (IOException ex) {
            System.out.println("IOException"+ex);
            ex.printStackTrace(); // TODO: error handling
        }
    }

    @FXML
    public void saveFile() {
    }

    @FXML
    public void openFile() {
    }

    @FXML
    public void newFile() {
    }

    @FXML
    public void print(Event e) {

        print(encoderUI);
        
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("print.fxml"));
//        Parent parent;
//        Scene scene;
//        Stage stage;
//
//        System.out.println("print()");
//
//        try {
//            parent = fxmlLoader.load();
//            PrintController pc = fxmlLoader.getController();
//            stage = new Stage();
//            scene = new Scene(parent);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setScene(scene);
//            stage.showAndWait();
//        } catch (IOException ex) {
//            System.out.println("IOException in print(): "+ex);
//            //ex.printStackTrace(); // TODO: error handling
//        }

    }
    
    @FXML
    public void print(Node node) {
        PrinterJob job = PrinterJob.createPrinterJob();
        EncoderProperties ep = EncoderProperties.getInstance();
        
        if (job != null && job.showPrintDialog(App.stage)) {
            Printer printer = job.getPrinter();
    
            PageLayout pageLayout = job.getJobSettings().getPageLayout();
            PrintResolution resolution = printer.getPrinterAttributes()
                    .getDefaultPrintResolution();
            double dpi = resolution.getFeedResolution(); // dpi
            System.out.println("print dpi="+dpi);
            double scale = dpi/72;
            double width = scale * pageLayout.getPrintableWidth();
            double height = scale * pageLayout.getPrintableHeight();
            System.out.println("width="+width+", height="+height);
           
//            double screenDPI = Screen.getPrimary().getDpi();
//            System.out.println("screen dpi="+screenDPI);
            Canvas c = new Canvas(width, height);
            c.getTransforms().add(new Scale(1/scale, 1/scale));
            c.setVisible(true); // won't print otherwise
            AnchorPane pane = new AnchorPane();
            pane.getChildren().add(c); // required to print/scale
            pane.setVisible(true);

            GraphicsContext gc = c.getGraphicsContext2D();
            gc.setImageSmoothing(false);
            
            scale = dpi;
            if (ep.getUnits().get().equals(EncoderProperties.MM))
                scale /= 25.4;
            
            EncoderView ev = new EncoderView(c, scale);
            ev.render();

            job.getJobSettings().setPrintQuality(PrintQuality.HIGH);
            
            boolean success = job.printPage(pane);
            if (success) {
                job.endJob();
            }

        }

    }

    @FXML
    public void export() {
    }

    @FXML
    public void onOpenDialog(ActionEvent event) throws IOException {
    }

    private Double parseDouble(String s) {
        if (s == null || s.equals(""))
            return 0.0;
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
            return NaN;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EncoderProperties ep = EncoderProperties.getInstance();

//        basicEncoder = new BasicEncoder();
//        binaryEncoder = new BinaryEncoder();
//        quadratureEncoder = new QuadratureEncoder();
//        grayEncoder = new GrayEncoder();

        encoderPreview = new EncoderView(encoderUI);
        ep.addListener((observable, oldvalue, newvalue) -> {
            encoderPreview.render();
        });

        // TODO - convert type to property on eProperties
        typeUI.getItems().setAll(ep.getTypeOptions());
        typeUI.valueProperty().bindBidirectional(ep.getType());
//        typeUI.getSelectionModel().select(0);

        resolutionUI.setValueFactory(new ResolutionValueFactory(
                ep.getEncoder(), ep.getResolution().get()));
        resolutionUI.getValueFactory().valueProperty().bindBidirectional(
                ep.getResolution());
        resolutionUI.getEditor().setTextFormatter(new IntegerTextFormatter(8).get());
        // TODO: change valuevactory whenever type changes
        ep.getType().addListener((observable, oldvalue, newvalue) -> {
            ResolutionValueFactory vf = 
                    (ResolutionValueFactory) resolutionUI.getValueFactory();
            vf.setEncoder(ep.getEncoder());
        });
        outerUI.textProperty().bindBidirectional(
                (Property) ep.getOuterDiameter(),
                new DoubleStringConverter());
        outerUI.setTextFormatter(new DoubleTextFormatter(100.0).get());
        
        innerUI.textProperty().bindBidirectional(
                (Property) ep.getInnerDiameter(),
                new DoubleStringConverter());
        innerUI.setTextFormatter(new DoubleTextFormatter(10.0).get());

        centerUI.textProperty().bindBidirectional(
                (Property) ep.getCenterDiameter(),
                new DoubleStringConverter());
        centerUI.setTextFormatter(new DoubleTextFormatter(5.0).get());

        unitsUI.getItems().addAll(ep.getUnitOptions());
        unitsUI.valueProperty().bindBidirectional(ep.getUnits());

        invertedUI.selectedProperty().bindBidirectional(ep.getInverted());

        indexUI.selectedProperty().bindBidirectional(ep.getIndexTrack());

        // TODO - direction
        // directionUI
        cwUI.selectedProperty().bindBidirectional(ep.getDirection());
        cwUI.selectedProperty().addListener((observable, oldvalue, newvalue) -> {
            System.out.println("cwUI, newvalue: " + newvalue);
            ep.getDirection().set(cwUI.selectedProperty().get());
            ccwUI.selectedProperty().set(oldvalue); // make sure the other toggle toggles
        });

        encoderPreview.render();
       
        // TODO input verification for resolution
        // TODO file save
        // TODO file save as
        // TODO file open
        // TODO file new
        // TODO file export
    }

//    private Parent loadFXML(String print) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
}
