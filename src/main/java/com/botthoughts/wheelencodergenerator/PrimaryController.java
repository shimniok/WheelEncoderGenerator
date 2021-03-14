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
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.converter.DoubleStringConverter;
import com.botthoughts.util.GitTagService;
import java.net.MalformedURLException;
import java.util.ArrayList;

public class PrimaryController implements Initializable {

  private EncoderView encoderPreview;
  private File currentFile;
  private static final String EXT = ".we2";
  private static final ExtensionFilter extensionFilter
      = new ExtensionFilter("Wheel Encoder Generator v2", "*" + EXT);
  private SimpleStringProperty filename;
  private SimpleBooleanProperty saved;
  private Alert alertDialog;
  private Alert confirmDialog;
  private EncoderProperties ep;

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
  Button newButton;
  @FXML
  Button saveButton;
  @FXML
  Button saveAsButton;
  @FXML
  Button PrintButton;
  
 
  private void showErrorDialog(String title, String text) {
    alertDialog.setTitle(title);
    alertDialog.setContentText(text);
    alertDialog.setAlertType(Alert.AlertType.ERROR);
    alertDialog.showAndWait();
  }

  private Optional<ButtonType> showConfirmDialog(String title, String text) {
    confirmDialog.setContentText(text);
    confirmDialog.setTitle(title);
    Optional<ButtonType> res = confirmDialog.showAndWait();
    return res;
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
        saved.set(true);
      } catch (IOException ex) {
        showErrorDialog("File Save Error", "Error saving " + f.getName() + "\n" + ex.getMessage());
      }
    }
  }
  
  public void updateTitle() {
    String title = "WheelEncoderGenerator - " + this.filename.get();
    if (!saved.get()) title += "*";
    App.stage.setTitle(title);
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
    SimpleBooleanProperty cancel = new SimpleBooleanProperty(false);
    
    if (!saved.get()) {
      Optional<ButtonType> optional = 
          this.showConfirmDialog("Save?", "Save changes before opening a new file?");
      optional.filter(response -> response == ButtonType.YES)
          .ifPresent(response -> saveFile());
      optional.filter(response -> response == ButtonType.CANCEL)
          .ifPresent(response -> { cancel.set(true); });
    }
    if (cancel.get()) return;
    
    File f = fc.showOpenDialog(App.stage);

    if (f == null) return;

    try {
      FileInputStream in;
      in = new FileInputStream(f);
      Properties p = new Properties();
      p.load(in);
      currentFile = f;
      filename.set(f.getName());
      saved.set(true);
    } catch (IOException ex) {
      showErrorDialog("File Open Error", "Error opening " + f.getName() + "\n" + ex.getMessage());
    }
  }

  @FXML
  public void newFile() {
    SimpleBooleanProperty cancel = new SimpleBooleanProperty(false);

    if (!saved.get()) {
      Optional<ButtonType> optional = 
          this.showConfirmDialog("Save?", "Save changes before creating a new encoder?");
      optional.filter(response -> response == ButtonType.YES)
          .ifPresent(response -> saveFile());
      optional.filter(response -> response == ButtonType.CANCEL)
          .ifPresent(response -> { cancel.set(true); });
    }
    if (cancel.get()) return;
    
    currentFile = null;
    filename.set("untitled" + EXT);
    ep.initialize();
    saved.set(false);
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
      //gc.setImageSmoothing(false);

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
        showErrorDialog("Printing Problem", "Status: " + job.getJobStatus().toString());
      }

    }

  }

  @FXML
  public void export() {
    // TODO file export Issue #7
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
  
//  @FXML
//  public void checkForUpdates() {
//    GitHubTagService service = new GitHubTagService("shimniok", "WheelEncoderGenerator");
//    try {
//      String s = service.getLatestTagName();
//      //System.out.println("Latest version: "+s);
//    } catch (IOException ex) {
//      System.out.println("checkForUpdates(): IOException " + ex);
//    }
//  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    ep = new EncoderProperties();

    GitTagService gts;
    try {
      gts = new GitTagService("shimniok", "WheelEncoderGenerator");
      ArrayList<String> names = gts.getNames();
      System.out.println(names);
    } catch (MalformedURLException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
   
    App.stage.setOnCloseRequest((event) -> {
      if (!saved.get()) {
        Optional<ButtonType> optional = this.showConfirmDialog("Save?", 
            "Save changes before continuing?");
        optional.filter(response -> response == ButtonType.CANCEL)
            .ifPresent( response -> event.consume() ); // consume close event
        optional.filter(response -> response == ButtonType.YES)
            .ifPresent( response -> this.saveFile() );
          // If ButtonType.NO, do nothing and continue closing
      }
    });
    
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

// TODO: implement Double formatting Issue #10
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
    invertedUI.selectedProperty().addListener((obs, ov, nv) -> {
      if (nv) {
        invertedUI.setText("Yes"); // if selected, "Yes"
      } else {
        invertedUI.setText("No"); // if not selected, "no"
      }
    });
   
    indexUI.selectedProperty().bindBidirectional(ep.getIndexed());
    indexUI.disableProperty().bind(ep.getIndexable().not());
    indexUI.selectedProperty().addListener((obs, ov, nv) -> {
      if (nv) {
        indexUI.setText("Yes"); // if selected, "Yes"
      } else {
        indexUI.setText("No"); // if not selected, "no"
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

    alertDialog = new Alert(Alert.AlertType.ERROR);

    confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
    confirmDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
    
    // Update encoder preview and saved status on any settings change
    encoderPreview = new EncoderView(encoderUI, ep);
    ep.addListener((observable, oldvalue, newvalue) -> {
      encoderPreview.render();
      saved.set(false);
    });

    filename = new SimpleStringProperty();
    // Update App title on filename change
    filename.addListener((obs, ov, nv) -> {
      this.updateTitle();
    });

    // set saved true so newFile() doesn't prompt
    saved = new SimpleBooleanProperty(true); 
    // Update App title on saved change
    saved.addListener((obs, ov, nv) -> {
      this.updateTitle();
    });
    saveButton.disableProperty().bind(saved);
    
    newFile();
    
    encoderPreview.render();
  }

}
