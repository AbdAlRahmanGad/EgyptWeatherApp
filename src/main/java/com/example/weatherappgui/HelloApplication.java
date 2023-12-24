package com.example.weatherappgui;

import com.fasterxml.jackson.databind.JsonNode;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** add thread for miny cities and save their data
 *
 *     and prevent to be opened twice until closed
 *
 *     if tiny city opened make thread to refresh every 3 minutes
 *
 *
 *
 *     last updated ->
 *
 *     click on city to be updated every [1, 60] minute
 *
 *     make the main Stage update every 10 minuted default
 *
 *     make the main Stage update every 10 minuted default
 *
 *     make add element by element
 *
 *     design pattern
 *
 *
 */

///// lazy uploud

    ///// to be added
     /// lazy upload main frame;
    ///


public class HelloApplication extends Application {
    int colIndex = 0;
    int rowIndex = 0;
    int limit = 5;
    List<EgyptCity> cities = new ArrayList<>();
    List<Tile> imageTiles = new ArrayList<>();
    List<Tile> dataTiles = new ArrayList<>();
    private  Map<String, ScrollPane> rootMap = new HashMap<>();
    Stage primaryStage = new Stage();
    ScrollPane mainRoot = new ScrollPane();

    GridPane gridPane = new GridPane();
    int SECONDS = 60;
    public int setRowIndex(){
        rowIndex++;
        if(rowIndex == limit){
            rowIndex = 1;
            colIndex++;
        }
       return rowIndex;
    }
    Scene primaryscene;
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage = this.primaryStage;
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        cities = GetCitiesNames.getCities();
        initializeScene(cities, primaryStage);

        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(() -> {
            updateDataCity(cities);
            System.out.println(new Date());
        }, 5, 60, TimeUnit.SECONDS);

        mainRoot = new ScrollPane(gridPane);
        primaryscene = new Scene(mainRoot, 620, 700);
        primaryStage.setFullScreen(true);
        primaryStage.setScene(primaryscene);
        primaryStage.show();
    }

    private void updateDataCity(List<EgyptCity> cities) {

        for (int i=0; i < cities.size(); i++) {

            JsonNode cityJson = GetData.getJson(cities.get(i).getLatitude(), cities.get(i).getLongitude());


            Image image = new Image("https:" + GetData.getConditionIcon(cityJson), 300, 150, true, false);

            final int finalI = i;
            new Thread(() -> {
                Platform.runLater(() ->{
                    imageTiles.get(finalI).setText(GetData.getConditionName(cityJson));
                    imageTiles.get(finalI).setImage(image);
                    dataTiles.get(finalI).setTitle("Humidity: " + GetData.getHumidity(cityJson) + "%");
                    dataTiles.get(finalI).setText("Wind " + GetData.getWindKph(cityJson) + " km/h");
                    dataTiles.get(finalI).setValue(GetData.getTemp_c(cityJson));
                }); // Update on JavaFX Application Thread
            }).start();
//            imageTiles.get(i).setText(GetData.getConditionName(cityJson));
//            imageTiles.get(i).setImage(image);
//            dataTiles.get(i).setTitle("Humidity: " + GetData.getHumidity(cityJson) + "%");
//            dataTiles.get(i).setText("Wind " + GetData.getWindKph(cityJson) + " km/h");
//            dataTiles.get(i).setValue(GetData.getTemp_c(cityJson));
//            dataTiles.get(i).trig
//            dataTiles.get(i).fireTileEvt(new TileEvt.DATA(){
//            });
        }
    }

        private void initializeScene(List<EgyptCity> cities, Stage primaryStage) {
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
                    .prefSize(300, 150)
                    .textSize(Tile.TextSize.BIGGER)
                    .borderColor(Color.BLACK)
                    .unit("\u00b0C")
                    .decimals(2)
                    .textAlignment(TextAlignment.CENTER)
                    .titleAlignment(TextAlignment.CENTER)
                    .build();

            dataTile.setTitle("Humidity: "+GetData.getHumidity(cityJson)+"%");
            dataTile.setText("Wind "+GetData.getWindKph(cityJson)+" km/h");
            dataTile.setValue(GetData.getTemp_c(cityJson));
            Button nameButton  = new Button(city.getNameInEnglish());
            nameButton.setOnAction(actionEvent -> {
                        primaryscene.setRoot(makeNewScene(city.getCitiesAndVillages(), city.getNameInEnglish()));
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
    public void addElementByElement(List<CityData> citiesAndVillages, int newRowIndex, int newColIndex, GridPane gridPane2){
        for (CityData minyCity : citiesAndVillages) {
            Text t = new Text();
            JsonNode cityJson = GetData.getJson(minyCity.getLatitude(), minyCity.getLongitude());
            t.setVisible(true);
            Tile imageTile2 = TileBuilder.create().skinType(Tile.SkinType.IMAGE)
                    .prefSize(300, 150)
                    .title(minyCity.getNameInArabic())
                    .textSize(Tile.TextSize.BIGGER)
                    .textAlignment(TextAlignment.CENTER)
                    .titleAlignment(TextAlignment.CENTER)
                    .text(GetData.getConditionName(cityJson))
                    .animated(false)
                    .build();

            Image image = new Image("https:" + GetData.getConditionIcon(cityJson), 300, 150, true, false);
            imageTile2.setImage(image);

            Tile dataTile2 = TileBuilder.create().skinType(Tile.SkinType.FLUID)
                    .prefSize(200, 150)
                    .textSize(Tile.TextSize.BIGGER)
                    .borderColor(Color.BLACK)
                    .title("Humidity: " + GetData.getHumidity(cityJson) + "%")
                    .text("Wind " + GetData.getWindKph(cityJson) + " km/h")
                    .unit("\u00b0C")
                    .animated(false)
                    .decimals(2)
                    .build();
            dataTile2.setValue(GetData.getTemp_c(cityJson));
            VBox vb = new VBox();
            vb.getChildren().add(imageTile2);
            vb.getChildren().add(dataTile2);

            newRowIndex++;
            if(newRowIndex == limit){
                newRowIndex = 1;
                newColIndex++;
            }
            imageTiles.add(imageTile2);
            dataTiles.add(dataTile2);
            int finalNewRowIndex = newRowIndex;
            int finalNewColIndex = newColIndex;

            new Thread(() -> {
                Platform.runLater(() ->{
                    gridPane2.add(vb, finalNewRowIndex, finalNewColIndex, 1, 1);
                }); // Update on JavaFX Application Thread
            }).start();

            ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
            exec.scheduleAtFixedRate(() -> {
                updateOneCity(minyCity, imageTile2,dataTile2);
                System.out.println(new Date());
            }, 0, 60, TimeUnit.SECONDS);
        }
    }
    public ScrollPane makeNewScene(List<CityData> citiesAndVillages, String cityName){
        if (!rootMap.containsKey(cityName)) {

            int newColIndex = 1;
            int newRowIndex = 0;

            GridPane gridPane2 = new GridPane();
            gridPane2.setHgap(10);
            gridPane2.setVgap(10);
            Button returnButton = new Button("Return");
            returnButton.setMinHeight(50);
            returnButton.setMinWidth(100);

            gridPane2.add(returnButton, newRowIndex, 0, 3, 1);
            cities = GetCitiesNames.getCities();
            ScrollPane scrollPane2 = new ScrollPane(gridPane2);
            rootMap.put(cityName, scrollPane2);

               Thread t1 = new Thread(() -> {
                    addElementByElement(citiesAndVillages, newRowIndex, newColIndex, gridPane2);
                });
               t1.start();

//                when thread finish
//                    begin
//                    t1.en
            //// JOIN
            ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
            exec.scheduleAtFixedRate(() -> {
                updateDataMinyCity(citiesAndVillages,imageTiles,dataTiles);
                System.out.println(new Date());
            }, 60, 180, TimeUnit.SECONDS);
            returnButton.setOnAction(actionEvent -> {
//                updateDataCity(cities);
                new Thread(() -> {
//                    Platform.runLater(() ->{
                        updateDataCity(cities);
//                    }); // Update on JavaFX Application Thread
                }).start();
                primaryscene.setRoot(mainRoot);
                exec.shutdown();
            });

//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
//                    childScene = new Scene(scrollPane2, 620, 700);
//                    Stage newStage = new Stage();
//                    newStage.setTitle(cityName);
//                }
//            });


//            return sceneMap.
//            newStage.setOnCloseRequest(event -> stageMap.remove(cityName));
//            Platform.runLater(() -> {
//                stageMap.get(cityName).setScene(childScene);
//                stageMap.get(cityName).show();

//            });

//            stageMap.get(cityName).setOnCloseRequest(windowEvent -> {
//                exec.shutdown();

//                stageMap.get(cityName).close();

//                stageMap.get(cityName).re;
//                try {
//                    if (!exec.awaitTermination(5, TimeUnit.SECONDS)) {
//                        exec.shutdownNow();
//                    }
//                } catch (InterruptedException e) {
//                    exec.shutdownNow();
//                    Thread.currentThread().interrupt();
//                }
//            });
        }else {
//            for (CityData minyCity : citiesAndVillages) {
//                new Thread(() -> {
//                    Platform.runLater(() ->{
//                        updateOneCity(minyCity, imageTile2,dataTile2);
//                    }); // Update on JavaFX Application Thread
//                }).start();
//            }
            }

        return  rootMap.get(cityName);
    }
    private void updateOneCity(CityData minyCity,
                                    Tile imageTiles2,
                                    Tile dataTiles2) {

            JsonNode cityJson = GetData.getJson(minyCity.getLatitude(), minyCity.getLongitude());

            imageTiles2.setText(GetData.getConditionName(cityJson));

            Image image = new Image("https:" + GetData.getConditionIcon(cityJson), 300, 150, true, false);
            imageTiles2.setImage(image);

            dataTiles2.setTitle("Humidity: " + GetData.getHumidity(cityJson) + "%");
            dataTiles2.setText("Wind " + GetData.getWindKph(cityJson) + " km/h");
            dataTiles2.setValue(GetData.getTemp_c(cityJson));

    }
    private void updateDataMinyCity(List<CityData> minyCities,
    List<Tile> imageTiles2,
    List<Tile> dataTiles2) {

        int i =0;
        for (CityData minyCity : minyCities) {

            JsonNode cityJson = GetData.getJson(minyCity.getLatitude(), minyCity.getLongitude());

            imageTiles2.get(i).setText(GetData.getConditionName(cityJson));

            Image image = new Image("https:" + GetData.getConditionIcon(cityJson), 300, 150, true, false);
            imageTiles2.get(i).setImage(image);

            dataTiles2.get(i).setTitle("Humidity: " + GetData.getHumidity(cityJson) + "%");
            dataTiles2.get(i).setText("Wind " + GetData.getWindKph(cityJson) + " km/h");
            dataTiles2.get(i).setValue(GetData.getTemp_c(cityJson));
            i++;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}