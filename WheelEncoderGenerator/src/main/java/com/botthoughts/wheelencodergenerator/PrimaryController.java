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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Double.NaN;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PrintQuality;
import javafx.print.PrintResolution;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.converter.DoubleStringConverter;

public class PrimaryController implements Initializable {

//    BasicEncoder basicEncoder;
//    QuadratureEncoder quadratureEncoder;
//    BinaryEncoder binaryEncoder;
//    GrayEncoder grayEncoder;
//    EncoderModel currentEncoder;
  private EncoderView encoderPreview;
  private File currentFile;
  private static final String extension = ".we2";
  private static final ExtensionFilter extensionFilter
      = new ExtensionFilter("Wheel Encoder Generator v2", "*" + extension);
  private SimpleStringProperty filename;
  private SimpleStringProperty alertTitle;
  private SimpleStringProperty alertText;
  private Alert alertDialog;
  private EncoderProperties ep;

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

  private void showAlert(String title, String text) {
    alertText.set(text);
    alertTitle.set(title);
    alertDialog.showAndWait();
  }

  public void saveFile(File f) {
    if (f != null) {
      try {
        FileOutputStream out = new FileOutputStream(f);
        Properties p = ep.toProperties();
        p.store(out, "Wheel Encoder Generator");
        System.out.println("file=" + f.getCanonicalPath());
        currentFile = f; // only do this if save succeeds!
        filename.set(f.getName());
      } catch (IOException ex) {
        showAlert("File Save Error", "Error saving " + f.getName() + "\n" + ex.getMessage());
      }
    }
  }

  @FXML
  public void saveFile() {
    if (currentFile == null) {
      saveFileAs();
    } else {
      saveFile(currentFile);
    }
  }

  @FXML
  public void saveFileAs() {
    FileChooser fc = new FileChooser();
    fc.setInitialFileName(filename.get());
    fc.getExtensionFilters().add(extensionFilter);
    saveFile(fc.showSaveDialog(App.stage));
  }

  @FXML
  public void openFile() {
    FileChooser fc = new FileChooser();
    fc.getExtensionFilters().add(extensionFilter);
    File f = fc.showOpenDialog(App.stage);
    try {
      FileInputStream in;
      in = new FileInputStream(f);
      Properties p = new Properties();
      p.load(in);
      currentFile = f;
      filename.set(f.getName());
    } catch (IOException ex) {
      showAlert("File Open Error", "Error opening " + f.getName() + "\n" + ex.getMessage());
    }
  }

  @FXML
  public void newFile() {
    System.out.println("File new");
    currentFile = null;
    filename.set("untitled" + extension);
    ep.initialize();
  }

  @FXML
  public void print(Event e) {
    print(encoderUI);
  }

  @FXML
  public void print(Node node) {
    PrinterJob job = PrinterJob.createPrinterJob();

    if (job != null && job.showPrintDialog(App.stage)) {
      Printer printer = job.getPrinter();

      PageLayout pageLayout = job.getJobSettings().getPageLayout();
      PrintResolution resolution = printer.getPrinterAttributes()
          .getDefaultPrintResolution();
      double dpi = resolution.getFeedResolution(); // dpi
      System.out.println("print dpi=" + dpi);
      double scale = dpi / 72;
      double width = scale * pageLayout.getPrintableWidth();
      double height = scale * pageLayout.getPrintableHeight();
      System.out.println("width=" + width + ", height=" + height);

      Canvas c = new Canvas(width, height);
      c.getTransforms().add(new Scale(1 / scale, 1 / scale));
      c.setVisible(true); // won't print otherwise
      AnchorPane pane = new AnchorPane();
      pane.getChildren().add(c); // required to print/scale
      pane.setVisible(true);

      GraphicsContext gc = c.getGraphicsContext2D();
      gc.setImageSmoothing(false);

      scale = dpi;
      if (ep.getUnits().get().equals(EncoderProperties.UNITS_MM)) {
        scale /= 25.4;
      }

      EncoderView ev = new EncoderView(c, scale, ep);
      ev.render();

      job.getJobSettings().setPrintQuality(PrintQuality.HIGH);

      boolean success = job.printPage(pane);
      if (success) {
        job.endJob();
      } else {
        showAlert("Printing Problem", "Status: " + job.getJobStatus().toString());
      }

    }

  }

  @FXML
  public void export() {
    // TODO file export
  }

  private Double parseDouble(String s) {
    if (s == null || s.equals("")) {
      return 0.0;
    }
    try {
      return Double.parseDouble(s);
    } catch (NumberFormatException nfe) {
      return NaN;
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    ep = new EncoderProperties();

    typeUI.getItems().setAll(ep.getTypeOptions());
    typeUI.valueProperty().bindBidirectional(ep.getType());
    System.out.println("PrimaryController: type=" + ep.getType());

    resolutionUI.setValueFactory(new ResolutionValueFactory(ep));
    resolutionUI.getValueFactory().valueProperty().bindBidirectional(ep.getResolution());
    resolutionUI.getEditor().setTextFormatter(new IntegerTextFormatter().get());
//        ep.getType().addListener((observable, oldvalue, newvalue) -> {
//            ResolutionValueFactory vf = (ResolutionValueFactory) resolutionUI.getValueFactory();
//            vf.setEncoder(ep.getEncoder());
//        });

// TODO: implement Double formatting
//  private final StringBuilder sb;
//  private final Formatter formatter;
//  change..setText(String.format("%.1f", Double.parseDouble(newText)));


    outerUI.textProperty().bindBidirectional(
        (Property) ep.getOuterDiameter(),
        new DoubleStringConverter());
    outerUI.setTextFormatter(new DoubleTextFormatter().get());

    innerUI.textProperty().bindBidirectional(
        (Property) ep.getInnerDiameter(),
        new DoubleStringConverter());
    innerUI.setTextFormatter(new DoubleTextFormatter().get());

    centerUI.textProperty().bindBidirectional(
        (Property) ep.getCenterDiameter(),
        new DoubleStringConverter());
    centerUI.setTextFormatter(new DoubleTextFormatter().get());

    unitsUI.getItems().addAll(ep.getUnitOptions());
    unitsUI.valueProperty().bindBidirectional(ep.getUnits());

    invertedUI.selectedProperty().bindBidirectional(ep.getInverted());

    indexUI.selectedProperty().bindBidirectional(ep.getIndexed());
    indexUI.disableProperty().bind(ep.getIndexable().not());
    indexUI.selectedProperty().addListener((obs, ov, nv) -> {
      if (nv) {
        indexUI.setText("Yes");
      } else {
        indexUI.setText("No");
      }
    });

    // directionUI, two buttons, only one selected at once.
    cwUI.selectedProperty().bindBidirectional(ep.getDirection());
    cwUI.selectedProperty().addListener((obs, ov, nv) -> {
      ep.getDirection().set(nv);
      ccwUI.selectedProperty().set(ov); // make sure the other toggle toggles
    });
    cwUI.disableProperty().bind(ep.getDirectional().not());
    ccwUI.disableProperty().bind(ep.getDirectional().not());

    alertTitle = new SimpleStringProperty("");
    alertText = new SimpleStringProperty("");

    alertDialog = new Alert(Alert.AlertType.ERROR);
    alertDialog.contentTextProperty().bind(alertText);
    alertDialog.titleProperty().bind(alertTitle);

    // App title set to filename
    filename = new SimpleStringProperty();
    App.stage.titleProperty().bindBidirectional(filename);
    newFile();

    // Update encoder preview anytime there's a change
    encoderPreview = new EncoderView(encoderUI, ep);
    ep.addListener((observable, oldvalue, newvalue) -> {
      encoderPreview.render();
    });

    encoderPreview.render();
  }

}
