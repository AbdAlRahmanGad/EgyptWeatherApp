package com.example.weatherappgui;

import com.fasterxml.jackson.databind.JsonNode;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.events.TileEvt;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
 *     make
 *
 */




public class HelloApplication extends Application {
    int colIndex = 0;
    int rowIndex = 0;
    int limit = 5;
    List<EgyptCity> cities = new ArrayList<>();
    List<Tile> imageTiles = new ArrayList<>();
    List<Tile> dataTiles = new ArrayList<>();
    private  Map<String, Stage> stageMap = new HashMap<>();


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
    @Override
    public void start(Stage primaryStage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        cities = GetCitiesNames.getCities();
        initializeScene(cities);

        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
        exec.scheduleAtFixedRate(() -> {
            updateDataCity(cities);
            System.out.println(new Date());
        }, 30, 60, TimeUnit.SECONDS);

        ScrollPane scrollPane = new ScrollPane(gridPane);
        Scene scene = new Scene(scrollPane, 620, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateDataCity(List<EgyptCity> cities) {

        for (int i=0; i < cities.size(); i++) {

            JsonNode cityJson = GetData.getJson(cities.get(i).getLatitude(), cities.get(i).getLongitude());

            imageTiles.get(i).setText(GetData.getConditionName(cityJson));

            Image image = new Image("https:" + GetData.getConditionIcon(cityJson), 300, 150, true, false);
            imageTiles.get(i).setImage(image);


            dataTiles.get(i).setTitle("Humidity: " + GetData.getHumidity(cityJson) + "%");
            dataTiles.get(i).setText("Wind " + GetData.getWindKph(cityJson) + " km/h");
            dataTiles.get(i).setValue(GetData.getTemp_c(cityJson));
//            dataTiles.get(i).trig
//            dataTiles.get(i).fireTileEvt(new TileEvt.DATA(){
//
//            });
        }
    }

        private void initializeScene(List<EgyptCity> cities) {
        for (EgyptCity city : cities) {
//            stages.add(new Stage());
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
//            Platform.setImplicitExit(false);
            nameButton.setOnAction(actionEvent -> {
//                new Thread(new Task<Void>() {
//                    @Override public Void call() {
//                        System.out.println("fdsfsfds    ");
//                       makeNewStage(city.getCitiesAndVillages(), city.getNameInEnglish());
//                        return null;
//                    }
//                }).start();
                Runnable task = () -> {
                    Platform.runLater(() -> {
                        makeNewStage(city.getCitiesAndVillages(), city.getNameInEnglish());
                    });
                };
                Thread thread = new Thread(task);
                thread.setDaemon(true);
                thread.start();
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



//    public int setRowIndex2(int newRowIndex){
//        newRowIndex++;
//        if(newRowIndex == limit){
//            newRowIndex = 1;
//        }
//        return newRowIndex;
//    }
    public void makeNewStage(List<CityData> citiesAndVillages, String cityName){
        if (!stageMap.containsKey(cityName)) {

//            List<Tile> imageTiles = new ArrayList<>();
//            List<Tile> dataTiles = new ArrayList<>();

            int newColIndex = 0;
            int newRowIndex = 0;

            GridPane gridPane2 = new GridPane();
            gridPane2.setHgap(10);
            gridPane2.setVgap(10);

            cities = GetCitiesNames.getCities();
            for (CityData minyCity : citiesAndVillages) {
                Text t = new Text();
                JsonNode cityJson = GetData.getJson(minyCity.getLatitude(), minyCity.getLongitude());
                t.setVisible(true);
                Tile imageTile2 = TileBuilder.create().skinType(Tile.SkinType.IMAGE)
                        .prefSize(300, 150)
                        .title(minyCity.getNameInArabic())
                        .textSize(Tile.TextSize.BIGGER)
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
                gridPane2.add(vb, newRowIndex, newColIndex, 1, 1);
            }
            Scene childScene;
            ScrollPane scrollPane2 = new ScrollPane(gridPane2);

//            Platform.runLater(new Runnable() {
//                @Override
//                public void run() {
                    childScene = new Scene(scrollPane2, 620, 700);
                    Stage newStage = new Stage();
                    newStage.setTitle(cityName);
//                }
//            });

            stageMap.put(cityName, newStage);
//            newStage.setOnCloseRequest(event -> stageMap.remove(cityName));
//            Platform.runLater(() -> {
                stageMap.get(cityName).setScene(childScene);
                stageMap.get(cityName).show();

//            });
//            ScheduledExecutorService exec = Executors.newScheduledThreadPool(20);
//            exec.scheduleAtFixedRate(() -> {
//                updateDataMinyCity(citiesAndVillages,imageTiles,dataTiles);
//                System.out.println(new Date());
//            }, 30, 180, TimeUnit.SECONDS);
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
        }else{
            System.out.println("lolo");
            stageMap.get(cityName).show();
        }
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