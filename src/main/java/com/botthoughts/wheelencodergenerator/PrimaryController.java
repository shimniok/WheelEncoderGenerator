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

import com.botthoughts.util.BoundedIntegerTextFilter;
import com.botthoughts.util.DoubleFormatter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.botthoughts.util.AppInfo;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import javax.imageio.ImageIO;

/**
 * Primary controller for WheelEncoderGenerator app handles the main window and all related actions.
 * @author mes
 */
public class PrimaryController implements Initializable {

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // Private fields
  
  private File currentFile;
  private static final String WEG_EXT = ".we2";
  private static final ExtensionFilter wegExtFilter
      = new ExtensionFilter("Wheel Encoder Generator v2", "*" + WEG_EXT);
  private static final ExtensionFilter pngExtFilter
      = new ExtensionFilter("Portable Network Graphics", "*.png");
  private SimpleStringProperty filename;
  private SimpleBooleanProperty saved;
  private final SimpleIntegerProperty decimals = new SimpleIntegerProperty();
  private Alert alertDialog;
  private Alert confirmDialog;
  private EncoderProperties ep;

  //////////////////////////////////////////////////////////////////////////////////////////////////
  // FXML UI Widgets
  
  // Main Window
  EncoderView encoderCanvas;
  @FXML VBox app;
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
  @FXML AnchorPane encoderContainer;
  @FXML Button newButton;
  @FXML Button saveButton;
  @FXML Button saveAsButton;
  @FXML Button printButton;
  @FXML MenuButton helpButton;
  @FXML GridPane updatePane;
  @FXML Label gitUrlUI;
  @FXML Label updateMessageUI;
  
  private Stage helpStage;
  private Stage aboutStage;

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // UTILITIES
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////
  
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
      latest = gts.getNames().get(0);
    } catch (MalformedURLException ex) {
      System.out.println("PrimaryControler.initialize(): MalformedURLException: "+ex);
    } catch (IOException ex) {
      System.out.println("PrimaryControler.initialize(): IOException: "+ex);
    }

    try {
      String current = "v" + AppInfo.get().getVersion(); // Prefix with 'v' to match github tags
      // If the latest tag isn't equal to the current version, then either an update is available
      // (unless you're the developer working on a *newer* version.
      updatePane.setVisible(!current.equals(latest)); // Show the update message
      updateMessageUI.setText("Update available: "+latest);
      
    } catch (IOException e) {
      System.out.println("AppInfo: "+e);
    }

  }
  
  private void removeAllDimensionErrors() {
    outerUI.getStyleClass().remove("error");
    innerUI.getStyleClass().remove("error");
    centerUI.getStyleClass().remove("error");
  }
  
 
  private void addDimensionValidator(TextField tf) {
    tf.textProperty().addListener((obs, ov, nv) -> {
//      System.out.println(tf.getId()+" changed");
      if (ep.isValid()) {
        removeAllDimensionErrors();
      } else {
//        System.out.println("error");
        if (!tf.getStyleClass().contains("error")) tf.getStyleClass().add("error");
      }
//      System.out.println("outer: "+outerUI.getStyleClass());
//      System.out.println("inner: "+innerUI.getStyleClass());
//      System.out.println("center: "+centerUI.getStyleClass());
    });
  }

  private void invalidWarning() {
    showErrorDialog("Invalid Encoder", "Please fix invalid encoder settings.");
  }
  

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // DIALOGS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////
  
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
   * Checks if current file needs saving and if so, prompts user to save with Yes/No/Cancel,
   * returning a boolean representing whether the calling method should continue or abort.
   * @return true to continue, false to abort
   */
  private boolean saveAndContinue() {
    SimpleBooleanProperty okToContinue = new SimpleBooleanProperty(false);
    
    if (saved.get()) {
      okToContinue.set(true);
    } else {
      Optional<ButtonType> optional = 
          this.showConfirmDialog("Save?", "Save changes before continuing?");
      optional.filter(response -> response == ButtonType.YES)
          .ifPresent(response -> okToContinue.set(saveFile()) );
      optional.filter(response -> response == ButtonType.CANCEL)
          .ifPresent(response -> okToContinue.set(false) );
      optional.filter(response -> response == ButtonType.NO)
          .ifPresent(response -> okToContinue.set(true));
    }

    return okToContinue.get();
  }
  
  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // EVENT HANDLERS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////
  
  /**
   * Save the current encoder to the specified file.
   * @param f is the File to which the encoder will be saved.
   * @return true if file save is successful, false if unsuccessful or file is null
   */
  public boolean saveFile(File f) {
    if (f == null) return false;

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
      return false;
    }
    return true;
  }
  
  /**
   * Handler for File Save calls saveFile() if the file has been saved previously and calls 
   * saveFileAs() if the file has never been saved before.
   * @return result of SaveFile() or saveFileAs(); false if ep.isValid() is false
   */
  @FXML
  public boolean saveFile() {
    if (ep.isValid()) {
      if (currentFile == null) {
        return saveFileAs();
      } else {
        return saveFile(currentFile);
      }
    } else {
      this.invalidWarning();
      return false;
    }
  }
  
  /**
   * Save file into new file/location selected by user from dialog.
   * @return result of SaveFile(); false if ep.isValid() is false
   */
  @FXML
  public boolean saveFileAs() {
    if (ep.isValid()) {
      FileChooser fc = new FileChooser();
      fc.setInitialFileName(filename.get());
      fc.getExtensionFilters().add(wegExtFilter);
      File f = fc.showSaveDialog(App.stage);
      System.out.println("filename: "+f);
      return saveFile(f);
    } else {
      this.invalidWarning();
      return false;
    }
  }
  
  /**
   * Opens file selected by user from file open dialog. Prompts user to save current encoder if it
   * has not been saved yet.
   */
  @FXML
  public void openFile() {
    FileChooser fc = new FileChooser();
    fc.getExtensionFilters().add(wegExtFilter);
    
    if (!this.saveAndContinue()) return;
    
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

    if (!saveAndContinue()) return;
    
    currentFile = null;
    filename.set("untitled" + WEG_EXT);
    ep.initialize();
    saved.set(false);
  }

  /**
   * Handles print event by calling method to print encoder node.
   * @param e 
   */
  @FXML
  public void print(Event e) {
    if (ep.isValid()) {
      print();
    } else {
      this.invalidWarning();
    }
  }

  /**
   * Prints the encoder.
   */
  @FXML
  public void print() {
    PrinterJob job;
    double scale;
    double width;
    double height;
    double dpi;
    PageLayout layout;
    PrintResolution resolution;

    job = PrinterJob.createPrinterJob();
    
    if (job != null && job.showPrintDialog(App.stage)) {
      Printer printer = job.getPrinter();

      layout = job.getJobSettings().getPageLayout();
      resolution = printer.getPrinterAttributes()
          .getDefaultPrintResolution();
      dpi = resolution.getFeedResolution(); // dpi
      System.out.println("print dpi=" + dpi);
      scale = dpi / 72;
      width = scale * layout.getPrintableWidth();
      height = scale * layout.getPrintableHeight();
      System.out.println("width=" + width + ", height=" + height);

      scale = dpi;
      if (ep.unitsProperty().get().equals(EncoderProperties.Units.MM)) {
        scale /= 25.4;
      }

      EncoderView ev = new EncoderView(scale, ep, width, height);
      ev.draw();
      ev.getTransforms().add(new Scale(1 / scale, 1 / scale));
      ev.setVisible(true); // won't print otherwise
      AnchorPane pane = new AnchorPane();
      pane.getChildren().add(ev); // required to print/scale
      pane.setVisible(true);

      job.getJobSettings().setPrintQuality(PrintQuality.HIGH);

      if (job.printPage(pane)) {
        job.endJob();
      } else {
        showErrorDialog("Printing Problem", "Status: " + job.getJobStatus().toString());
      }

    }

  }

  /**
   * Export encoder as image.
   */
  @FXML
  public void export() {
    WritableImage image = encoderCanvas.snapshot(new SnapshotParameters(), null);

    FileChooser fc = new FileChooser();
    fc.getExtensionFilters().add(pngExtFilter); // need png extension filter
    File file = fc.showSaveDialog(App.stage);
    
    if (file == null) return;
    
    try {
      BufferedImage x = SwingFXUtils.fromFXImage(image, null);
      ImageIO.write(x, "png", file);
    } catch (IOException e) {
      System.out.println("IOException: "+e.getMessage());
    }
  }
  
  /**
   * Open Help window to display online help.
   */
  @FXML
  public void help() { 
    helpStage.show();
  }

  /**
   * Open About window to display app information.
   */
  @FXML
  public void about() {
    aboutStage.show();
  }
  
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

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // LISTENERS
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////
  
  /**
   * Update the application title bar based on the current filename and saved state.
   */
  public void updateTitle() {
    String title = "WheelEncoderGenerator - " + this.filename.get();
    if (!saved.get()) title += "*";
    App.stage.setTitle(title);
  }

  /**
   * Returns a ChangeListener for use with toggle buttons to change the button text based on
   * selected and not-selected status.
   * 
   * @param yes is the String to display when selected
   * @param no is the String to display when not selected
   * @param b is the button in question
   * @return a ChangeListener lambda incorporating the other parameters
   */
  private ChangeListener<Boolean> toggleListener(String yes, String no, ToggleButton b) {
    return (obs, ov, nv) -> { b.setText((nv)?yes:no); };
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////
  //
  // INITIALIZATION
  //
  //////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * Initialize the PrimaryController
   * @param url
   * @param rb 
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
//    System.out.println("PrimaryController: initialize()");

    ep = new EncoderProperties();

    // Update encoder preview and saved status on any settings change
    encoderCanvas = new EncoderView(ep, 500, 500);
    
    ObservableList<Node> children = encoderContainer.getChildren();
    children.add(encoderCanvas);
    
    // Set up canvas resizing
    encoderCanvas.widthProperty().bind(encoderContainer.widthProperty());
    encoderCanvas.heightProperty().bind(encoderContainer.heightProperty());

    saved = new SimpleBooleanProperty();
    ep.addListener((observable, oldvalue, newvalue) -> {
      encoderCanvas.draw();
      saved.set(false);
    });
    
    // Handle exiting/quitting
    App.stage.setOnCloseRequest((event) -> {
      if (!saveAndContinue()) {
        event.consume();
      } else {
        Platform.exit();
      }
    });
    
    typeUI.getItems().setAll(ep.getTypeOptions());
    typeUI.valueProperty().bindBidirectional(ep.typeProperty());
//    System.out.println("PrimaryController: type=" + ep.typeProperty());

    resolutionUI.setValueFactory(
        new ResolutionValueFactory(new IntegerStringConverter(), 
            ep.resolutionIncrementProperty(), ep.resolutionDecrementProperty()));
    resolutionUI.getValueFactory().valueProperty().bindBidirectional(ep.resolutionProperty());
    TextFormatter<Integer> tf = new TextFormatter(
        new BoundedIntegerTextFilter(ep.minResolutionProperty(), ep.maxResolutionProperty()));
    resolutionUI.getEditor().setTextFormatter(tf);

    decimals.set(1); // units default to mm, so manually set decimal format 
    
    DoubleFormatter outerFmt = new DoubleFormatter();
    outerFmt.decimalsProperty().bind(decimals);
    outerUI.textProperty().bindBidirectional((Property) ep.outerDiameterProperty(), 
        outerFmt.getConverter());
    outerUI.setTextFormatter(outerFmt);
    addDimensionValidator(outerUI);
    
    DoubleFormatter innerFmt = new DoubleFormatter();
    innerFmt.decimalsProperty().bind(decimals);
    innerUI.textProperty().bindBidirectional((Property) ep.innerDiameterProperty(), 
        innerFmt.getConverter());
    innerUI.setTextFormatter(innerFmt);
    addDimensionValidator(innerUI);

    DoubleFormatter centerFmt = new DoubleFormatter();
    centerFmt.decimalsProperty().bind(decimals);
    centerUI.textProperty().bindBidirectional((Property) ep.centerDiameterProperty(), 
        centerFmt.getConverter());
    centerUI.setTextFormatter(centerFmt);
    addDimensionValidator(centerUI);
    
    unitsUI.getItems().addAll(ep.getUnitOptions());
    unitsUI.valueProperty().bindBidirectional(ep.unitsProperty());
    unitsUI.valueProperty().addListener((obs, ov, nv) -> {
      if (nv.equals(EncoderProperties.Units.MM)) {
        decimals.set(1);
        // Automatically convert current value
        ep.outerDiameterProperty().set(UnitConverter.toMillimeter(ep.outerDiameterProperty().get()));
        ep.innerDiameterProperty().set(UnitConverter.toMillimeter(ep.innerDiameterProperty().get()));
        ep.centerDiameterProperty().set(UnitConverter.toMillimeter(ep.centerDiameterProperty().get()));        
      } else if (nv.equals(EncoderProperties.Units.INCH)) {
        decimals.set(3);
        // Automatically convert current value
        ep.outerDiameterProperty().set(UnitConverter.toInch(ep.outerDiameterProperty().get()));
        ep.innerDiameterProperty().set(UnitConverter.toInch(ep.innerDiameterProperty().get()));
        ep.centerDiameterProperty().set(UnitConverter.toInch(ep.centerDiameterProperty().get()));        
      }
      // Force conversion to new format
      outerUI.textProperty().set(outerUI.textProperty().get());
      innerUI.textProperty().set(innerUI.textProperty().get());
      centerUI.textProperty().set(centerUI.textProperty().get());
    });

    invertedUI.selectedProperty().bindBidirectional(ep.invertedProperty());
    invertedUI.selectedProperty().addListener(this.toggleListener("Yes", "No", invertedUI));

    indexUI.selectedProperty().bindBidirectional(ep.indexedProperty());
    indexUI.disableProperty().bind(ep.indexableProperty().not());
    indexUI.selectedProperty().addListener(this.toggleListener("Yes", "No", indexUI));

    // Clockwise/Counter-clockwise buttons part of ToggleGroup
    ToggleGroup direction = new ToggleGroup();
    cwUI.toggleGroupProperty().set(direction);
    ccwUI.toggleGroupProperty().set(direction);
    cwUI.selectedProperty().bindBidirectional(ep.directionProperty());
    // Disable if the current encoder type is non-directional
    cwUI.disableProperty().bind(ep.directionalProperty().not());
    ccwUI.disableProperty().bind(ep.directionalProperty().not());

    // Dialogs
    alertDialog = new Alert(Alert.AlertType.ERROR);
    confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
    confirmDialog.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
    
    // Current filename
    filename = new SimpleStringProperty();
    // Update App title on filename change
    filename.addListener((obs, ov, nv) -> {
      this.updateTitle();
    });

    // Update App title on saved change
    saved.addListener((obs, ov, nv) -> {
      this.updateTitle();
    });
    // Only enable save button if unsaved changes
    saveButton.disableProperty().bind(saved);
    
    // Initialize a new encoder
    saved.set(true); // so we don't get prompted on launch
    newFile();

    // Force rendering since this doesn't seem to happen via listener for some reason
    encoderCanvas.draw();

    checkForUpdates();
    
    // Initialize Help display to minimize load time later
    try {
      FXMLLoader loader = new FXMLLoader();
      Parent root = loader.load(getClass().getResource("help.fxml"));
      helpStage = new Stage();
      helpStage.setTitle("WEG - Online Help");
      helpStage.setScene(new Scene(root));
    } catch (IOException e) {
//      this.showErrorDialog("Error", "Error loading help window");
      System.out.println("Error loading help window: "+e.getMessage());
    }

    // Initialize About display to minimize load time later
    aboutStage = new Stage();
    try {
      FXMLLoader loader = new FXMLLoader();
      Parent root = loader.load(getClass().getResource("about.fxml"));
      aboutStage.setScene(new Scene(root));
      aboutStage.setTitle("About " + AppInfo.get().getAbbr());
    } catch (IOException e) {
//        this.showErrorDialog("Error", "Error loading about window\n");
      System.out.println("IOException: " + e.getMessage());
    }
   
  }

}


//TODO: more padding around encoder on canvas