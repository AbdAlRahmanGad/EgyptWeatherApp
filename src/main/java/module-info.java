module com.example.weatherappgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires javafx.media;

//    requires eu.hansolo.medusa;
//    requires eu.hansolo.toolbox;
//    requires eu.hansolo.tilesfx;
    requires javafx.swing;
    requires eu.hansolo.tilesfx;
//    requires eu.hansolo.medusa;
//    requires eu.hansolo.toolboxfx;
//    import javafx.scene.layout.TilePane;




    opens com.example.weatherappgui;
//    to javafx.fxml;
//    opens com.example.weatherappgui to com.fasterxml.jackson.databind;
    exports com.example.weatherappgui;
}