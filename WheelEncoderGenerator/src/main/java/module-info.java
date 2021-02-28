module com.botthoughts.wheelencodergenerator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.botthoughts.wheelencodergenerator to javafx.fxml;
    exports com.botthoughts.wheelencodergenerator;
}
