module com.botthoughts.wheelencodergenerator {
  opens com.botthoughts.wheelencodergenerator to javafx.fxml;
  opens com.botthoughts.util to javafx.fxml;
  exports com.botthoughts.wheelencodergenerator;
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;
  requires java.desktop;
  requires org.json;
  requires java.base;
}
