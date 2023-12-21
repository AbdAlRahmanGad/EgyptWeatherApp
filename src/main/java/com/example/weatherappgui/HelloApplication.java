package com.example.weatherappgui;

import com.fasterxml.jackson.databind.JsonNode;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** add thread for miny cities and save their data
 *
 *     and prevent to be opened twice until closed
 *
 *     if tiny city opened make thread to refresh every 3 minutes
 */

public class HelloApplication extends Application {
    int colIndex = 0;
    int rowIndex = -1;
    int limit = 4;
    List<EgyptCity> cities = new ArrayList<>();
    List<Tile> imageTiles = new ArrayList<>();
    List<Tile> dataTiles = new ArrayList<>();
    GridPane gridPane = new GridPane();
    int SECONDS = 60;
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
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        cities = GetCitiesNames.getCities();
        initializeScene(cities);

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> {
            updateData(cities);
            System.out.println(new Date());
        }, 0, 60, TimeUnit.SECONDS);

        ScrollPane scrollPane = new ScrollPane(gridPane);
        Scene scene = new Scene(scrollPane, 620, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateData(List<EgyptCity> cities) {

        int i =0;
        for (EgyptCity city : cities) {

            JsonNode cityJson = GetData.getJson(city.getLatitude(), city.getLongitude());

            imageTiles.get(i).setText(GetData.getConditionName(cityJson));
            Image image = new Image("https:" + GetData.getConditionIcon(cityJson), 300, 150, true, false);
            imageTiles.get(i).setImage(image);

            dataTiles.get(i).setTitle("Humidity: " + GetData.getHumidity(cityJson) + "%");
            dataTiles.get(i).setText("Wind " + GetData.getWindKph(cityJson) + " km/h");
            dataTiles.get(i).setValue(GetData.getTemp_c(cityJson));

            i++;
        }
    }



        private void initializeScene(List<EgyptCity> cities) {
        for (EgyptCity city : cities) {
            Text t = new Text();
            JsonNode cityJson = GetData.getJson(city.getLatitude(), city.getLongitude());
            t.setVisible(true);
            Tile imageTile = TileBuilder.create().skinType(Tile.SkinType.IMAGE)
                    .prefSize(300, 150)
                    .title(city.getNameInArabic())
                    .textSize(Tile.TextSize.BIGGER)
                    .animated(false)
                    .build();
            imageTile.setText(GetData.getConditionName(cityJson));
            Image image = new Image("https:"+GetData.getConditionIcon(cityJson), 300, 150, true, false);
            imageTile.setImage(image);

            Tile dataTile = TileBuilder.create().skinType(Tile.SkinType.FLUID)
                    .prefSize(200, 150)
                    .textSize(Tile.TextSize.BIGGER)
                    .borderColor(Color.BLACK)
                    .unit("\u00b0C")
                    .decimals(2)
                    .build();

            dataTile.setTitle("Humidity: "+GetData.getHumidity(cityJson)+"%");
            dataTile.setText("Wind "+GetData.getWindKph(cityJson)+" km/h");
            dataTile.setValue(GetData.getTemp_c(cityJson));
            Button nameButton  = new Button(city.getNameInEnglish());
            nameButton.setOnAction(actionEvent -> {
                makeNewStage(city.getCitiesAndVillages());
            });
            VBox vb = new VBox();
            imageTiles.add(imageTile);
            dataTiles.add(dataTile);
            vb.getChildren().add(imageTile);
            vb.getChildren().add(dataTile);
            vb.getChildren().add(nameButton);

            gridPane.add(vb, setRowIndex(), colIndex, 1, 1);
        }
    }

    public int setRowIndex2(int newRowIndex){
        newRowIndex++;
        if(newRowIndex == limit){
            newRowIndex = 0;
        }
        return newRowIndex;
    }
    public void makeNewStage(List<CityData> citiesAndVillages){
        int newColIndex = 0;
        int newRowIndex = -1;

            GridPane gridPane2 = new GridPane();
            gridPane2.setHgap(10);
            gridPane2.setVgap(10);

            cities = GetCitiesNames.getCities();
            for (CityData minyCity : citiesAndVillages) {
                Text t = new Text();
                JsonNode cityJson = GetData.getJson(minyCity.getLatitude(), minyCity.getLongitude());
                t.setVisible(true);
                Tile tile = TileBuilder.create().skinType(Tile.SkinType.IMAGE)
                        .prefSize(300, 150)
                        .title(minyCity.getNameInArabic())
                        .textSize(Tile.TextSize.BIGGER)
                        .text(GetData.getConditionName(cityJson))
                        .animated(false)
                        .build();

                Image image = new Image("https:"+GetData.getConditionIcon(cityJson), 300, 150, true, false);
                tile.setImage(image);

                Tile tile1 = TileBuilder.create().skinType(Tile.SkinType.FLUID)
                        .prefSize(200, 150)
                        .textSize(Tile.TextSize.BIGGER)
                        .borderColor(Color.BLACK)
                        .title("Humidity: "+GetData.getHumidity(cityJson)+"%")
                        .text("Wind "+GetData.getWindKph(cityJson)+" km/h")
                        .unit("\u00b0C")
                        .decimals(2)
                        .build();
                tile1.setValue(GetData.getTemp_c(cityJson));
                VBox vb = new VBox();
                vb.getChildren().add(tile);
                vb.getChildren().add(tile1);

                newRowIndex = setRowIndex2(newRowIndex);
                newColIndex = newColIndex(newRowIndex, newColIndex);

                gridPane2.add(vb, newRowIndex, newColIndex, 1, 1);
            }
            ScrollPane scrollPane2 = new ScrollPane(gridPane2);
            Scene scene2 = new Scene(scrollPane2, 620, 700);
            Stage secondaryStage = new Stage();
            secondaryStage.setScene(scene2);
            secondaryStage.show();
    }

    public static int newColIndex(int row, int col) {
        if(row == 0 ){
            return col+1;
        }
        return col;
    }

    public static void main(String[] args) {
        launch();
    }
}