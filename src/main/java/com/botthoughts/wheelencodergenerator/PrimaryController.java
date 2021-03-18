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
import com.botthoughts.util.GitTagService;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PrimaryController implements Initializable {

  private EncoderView encoderPreview;
  private File currentFile;
  private static final String EXT = ".we2";
  private static final ExtensionFilter extensionFilter
      = new ExtensionFilter("Wheel Encoder Generator v2", "*" + EXT);
  private SimpleStringProperty filename;
  private SimpleBooleanProperty saved;
  private final SimpleIntegerProperty decimals = new SimpleIntegerProperty();
  private Alert alertDialog;
  private Alert confirmDialog;
  private EncoderProperties ep;
  private WebHelpController helpController;

  @FXML Canvas encoderUI;
  @FXML ComboBox typeUI;
  @FXML Spinner resolutionUI;
  @FXML TextField outerUI;
  @FXML TextField innerUI;
  @FXML TextField centerUI;
  @FXML ComboBox unitsUI;
  @FXML ToggleGroup directionUI;
  @FXML ToggleButton cwUI;
  @FXML ToggleButton ccwUI;
  @FXML ToggleButton invertedUI;
  @FXML ToggleButton indexUI;
  @FXML AnchorPane canvasContainer;
  @FXML Button newButton;
  @FXML Button saveButton;
  @FXML Button saveAsButton;
  @FXML Button printButton;
  @FXML Button helpButton;
  @FXML GridPane updatePane;
  @FXML Label gitUrlUI;
  private Stage helpStage;

  /**
   * Copies the URL for GitHub Releases into the clipboard.
   */
  @FXML
  public void copyGithubUrlToClipboard() {
    Clipboard clipboard = Clipboard.getSystemClipboard();
    ClipboardContent content = new ClipboardContent();
    String url = gitUrlUI.getText();
    gitUrlUI.setText("Copied to clipboard!");
    content.putString(url);
    clipboard.setContent(content);

    Timeline timeline = new Timeline();
    timeline.autoReverseProperty().set(false);
    timeline.getKeyFrames().add(new KeyFrame(Duration.millis(3000),
            new KeyValue(gitUrlUI.textProperty(), url)
    ));
    timeline.play();
  }

  /**
   * Use GitTagService to read Tags API and compare to current version specified in 
   * version.properties to determine if application has update available and display update message
   * with button to copy URL.
   */
  private void checkForUpdates() {

    // Get the latest tag from the application's GitHub repo.
    GitTagService gts;
    String latest = "";
    try {
      gts = new GitTagService("shimniok", "WheelEncoderGenerator");
      ArrayList<String> names = gts.getNames();
      System.out.println(names.get(0));
    } catch (MalformedURLException ex) {
      System.out.println("PrimaryControler.initialize(): MalformedURLException: "+ex);
    } catch (IOException ex) {
      System.out.println("PrimaryControler.initialize(): IOException: "+ex);
    }

    // Get version of this app from version.properties
    Properties properties = new Properties();
    try {
      InputStream stream = App.class.getResourceAsStream("/version.properties");
      properties.load(stream);
      String version = "v" + properties.getProperty("version"); // prefix with 'v'
      System.out.println(version);

      // If the latest tag isn't equal to the current version, then either an update is available
      // (unless you're the developer working on a *newer* version.
      if (!version.equals(latest)) {
        updatePane.setVisible(true); // Show the update message
      } else {
        updatePane.setVisible(false); // Hide the update message
      }
    } catch (IOException e) {
      // We don't *really* need to bug the user about this do we?
      System.out.println("Problem loading version properties");
    }
    
  }
  
  /**
   * Show a dialog.
   * @param title is the title for the title bar
   * @param text is the text to display in the dialog
   * @param type is the alert type to use (see Alert.AlertType
   */
  private void showDialog(String title, String text, Alert.AlertType type) {
    alertDialog.setTitle(title);
    alertDialog.setContentText(text);
    alertDialog.setAlertType(type);
    alertDialog.showAndWait();
  }
      
  /**
   * Show an error dialog.
   * @param title is the title for the title bar
   * @param text is the text to display in the dialog
   */
  private void showErrorDialog(String title, String text) {
    showDialog(title, text, Alert.AlertType.ERROR);
  }

  /**
   * Show a confirmation dialog
   * @param title is the title for the title bar
   * @param text is the text to display in the dialog
   * @return result as an Optional<ButtonType>
   */
  private Optional<ButtonType> showConfirmDialog(String title, String text) {
    confirmDialog.setContentText(text);
    confirmDialog.setTitle(title);
    Optional<ButtonType> res = confirmDialog.showAndWait();
    return res;
  }
  
  /**
   * Save the current encoder to the specified file.
   * @param f is the File to which the encoder will be saved.
   */
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
      } catch (IOException ex) { // TODO should really handle errors externally so we can e.g. cancel new/open/quit operation
        showErrorDialog("File Save Error", "Error saving " + f.getName() + "\n" + ex.getMessage());
      }
    }
  }
  
  /**
   * Update the application title bar based on the current filename and saved state.
   */
  public void updateTitle() {
    String title = "WheelEncoderGenerator - " + this.filename.get();
    if (!saved.get()) title += "*";
    App.stage.setTitle(title);
  }

  /**
   * Handler for File Save calls saveFile() if the file has been saved previously and calls 
   * saveFileAs() if the file has never been saved before.
   */
  @FXML
  public void saveFile() {
    if (currentFile == null) {
      saveFileAs();
    } else {
      saveFile(currentFile);
    }
  }
  
  /**
   * Save file into new file/location selected by user from dialog.
   */
  @FXML
  public void saveFileAs() {
    FileChooser fc = new FileChooser();
    fc.setInitialFileName(filename.get());
    fc.getExtensionFilters().add(extensionFilter);
    saveFile(fc.showSaveDialog(App.stage));
  }
  
  /**
   * Opens file selected by user from file open dialog. Prompts user to save current encoder if it
   * has not been saved yet.
   */
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

  /**
   * Erase current encoder and reset to default. Prompts user to save current encoder if it has
   * not been saved yet.
   */
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

  /**
   * Handles print event by calling method to print encoder node.
   * @param e 
   */
  @FXML
  public void print(Event e) {
    print(encoderUI);
  }

  /**
   * Prints the specified encoder node.
   * @param node is the node containing the encoder to be printed.
   */
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

  /**
   * Export encoder as image. Not yet implemented.
   */
  @FXML
  public void export() {
    // TODO file export Issue #7
  }
  
  /**
   * Open Help window to display online help.
   */
  @FXML
  public void help() {
    Parent root;
    try {
      FXMLLoader loader = new FXMLLoader();
      root = loader.load(getClass().getResource("help.fxml"));
      helpStage = new Stage();
      helpStage.setTitle("WEG - Online Help");
      helpStage.setScene(new Scene(root));
      helpStage.show();
    } catch (IOException e) {
      this.showErrorDialog("Error", "Error loading help window\n"+e);
    }
  }

  /**
   * Parses a double from a string and returns the number (or NaN).
   * @param s is the string to parse. A null or "" results in 0.0. 
   * @return the Double resulting from parsing the string; NaN if format is erroneous.
   */
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
  
  /**
   * Temporary for handling events
   */
  @FXML
  public void eventHandler(Event e) {
    System.out.println("Event: "+e.getEventType().getName());
  }
  
  /**
   * Initialize the PrimaryController
   * @param url
   * @param rb 
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    ep = new EncoderProperties();

    System.out.println("PrimaryController: initialize()");
    
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
      // TODO: also close help window, if applicable
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

    decimals.set(1); // set to 1 since Units set to MM by default; would be nice to bind/automate

    DoubleFormatter outerFmt = new DoubleFormatter();
    outerFmt.decimalsProperty().bind(decimals);
    outerUI.textProperty().bindBidirectional((Property) ep.getOuterDiameter(), 
        outerFmt.getConverter());
    outerUI.setTextFormatter(outerFmt);
    
    DoubleFormatter innerFmt = new DoubleFormatter();
    innerFmt.decimalsProperty().bind(decimals);
    innerUI.textProperty().bindBidirectional((Property) ep.getInnerDiameter(), 
        innerFmt.getConverter());
    innerUI.setTextFormatter(innerFmt);
    
    DoubleFormatter centerFmt = new DoubleFormatter();
    centerFmt.decimalsProperty().bind(decimals);
    centerUI.textProperty().bindBidirectional((Property) ep.getCenterDiameter(), 
        centerFmt.getConverter());
    centerUI.setTextFormatter(centerFmt);

    unitsUI.getItems().addAll(ep.getUnitOptions());
    unitsUI.valueProperty().bindBidirectional(ep.getUnits());
    unitsUI.valueProperty().addListener((obs, ov, nv) -> {
      if (nv.equals(EncoderProperties.UNITS_MM)) {
        decimals.set(1);
        // Automatically convert current value
        ep.getOuterDiameter().set(UnitConverter.toMillimeter(ep.getOuterDiameter().get()));
        ep.getInnerDiameter().set(UnitConverter.toMillimeter(ep.getInnerDiameter().get()));
        ep.getCenterDiameter().set(UnitConverter.toMillimeter(ep.getCenterDiameter().get()));        
      } else if (nv.equals(EncoderProperties.UNITS_INCH)) {
        decimals.set(3);
        // Automatically convert current value
        ep.getOuterDiameter().set(UnitConverter.toInch(ep.getOuterDiameter().get()));
        ep.getInnerDiameter().set(UnitConverter.toInch(ep.getInnerDiameter().get()));
        ep.getCenterDiameter().set(UnitConverter.toInch(ep.getCenterDiameter().get()));        
      }
      // Force conversion to new format
      outerUI.textProperty().set(outerUI.textProperty().get());
      innerUI.textProperty().set(innerUI.textProperty().get());
      centerUI.textProperty().set(centerUI.textProperty().get());
    });

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

    checkForUpdates();
  }

}
