module com.botthoughts.wheelencodergenerator {
  opens com.botthoughts.wheelencodergenerator to javafx.fxml;
  exports com.botthoughts.wheelencodergenerator;

  requires javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  requires org.json;
  requires javafx.web;
  requires java.base;
}
