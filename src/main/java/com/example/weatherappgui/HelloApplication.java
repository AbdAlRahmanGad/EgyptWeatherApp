package com.example.weatherappgui;

//import  .eu.hansolo.medusa.*;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.media.*;

import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.SpringLayout.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;

public class HelloApplication extends Application {
    int colIndex = 0;
    int rowIndex = -1;
    int limit = 4;
    List<EgyptCity> cities = new ArrayList<>();

    public int setRowIndex(){
        rowIndex++;
        if(rowIndex == limit){
            rowIndex = 0;
            colIndex++;
        }
       return rowIndex;
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        TilePane tilePane = new TilePane();
//        Tile tile = TileBuilder.create().skinType(Tile.SkinType.FLUID)
//                .prefSize(WIDTH, HEIGHT)
//                .title("Fire Smoke")
//                .text("CPU temp")
//                .unit("\u00b0C")
//                .threshold(70) // triggers the fire and smoke effect
//                .decimals(0)
//                .animated(true)
//                .build();
//        tilePane.getChildren.add(tile);
//        gridPane.add(tile, setRowIndex(), colIndex, 1, 1);

//        Tile
        cities = GetCitiesNames.getCities();
        for (EgyptCity city : cities) {
            Text t = new Text();
//            t.setText(GetData.getTemp(city.getLatitude(), city.getLongitude()));
            t.setVisible(true);
            Tile tile = TileBuilder.create().skinType(Tile.SkinType.COLOR)
                  .prefSize(300, 150)
                  .title("Fire Smoke")
                  .text("CPU temp")
                  .unit("\u00b0C")
                  .threshold(20) // triggers the fire and smoke effect
                  .decimals(2)
                    .activeColor(Color.RED)
                  .animated(true)
                  .build();
            tile.setImage(new Image("https://cdn.weatherapi.com/weather/64x64/day/116.png"));
//            tile.setValue(Double.parseDouble(GetData.getTemp(city.getLatitude(), city.getLongitude())));
            tile.setValue(22.02);
            Tile tile1 = TileBuilder.create().skinType(Tile.SkinType.COLOR)
                    .prefSize(200, 150)
                    .title("Color (Sections)")
                    .description("CPU temp")
                    .text("Text")
                    .unit("\u00b0C")
                    .animated(true)
                    .leftValue(3.5)
                    .decimals(2)
                    .build();
//            tile1.setValue(Double.parseDouble(GetData.getTemp(city.getLatitude(), city.getLongitude())));
            Button b  = new Button(city.getNameInEnglish());
            VBox vb = new VBox();
            vb.getChildren().add(tile1);
            vb.getChildren().add(tile);
            vb.getChildren().add(b);

            gridPane.add(vb, setRowIndex(), colIndex, 1, 1);
        }
//            System.out.println(city.getNameInArabic()+": ");
//        for (int i = 1; i <= 150; i++) {
////            flowPane.getChildren().add(new javafx.scene.control.Button("Button " + i));
//            gridPane.add(new Button("text"), setRowIndex(), colIndex, 1, 1);
//
//        }

        // Create a ScrollPane and set the content to the FlowPane
        ScrollPane scrollPane = new ScrollPane(gridPane);

        // Create the Scene
        Scene scene = new Scene(scrollPane, 500, 500);

        // Set up the Stage
        primaryStage.setTitle("Scrollable FlowPane Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}