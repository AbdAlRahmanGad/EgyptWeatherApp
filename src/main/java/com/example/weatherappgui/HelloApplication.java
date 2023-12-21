package com.example.weatherappgui;

//import  .eu.hansolo.medusa.*;
import com.fasterxml.jackson.databind.JsonNode;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.List;

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

        cities = GetCitiesNames.getCities();
        for (EgyptCity city : cities) {
            Text t = new Text();
            JsonNode cityJson = GetData.getJson(city.getLatitude(), city.getLongitude());
            t.setVisible(true);
            Tile tile = TileBuilder.create().skinType(Tile.SkinType.IMAGE)
                  .prefSize(300, 150)
                    .title(city.getNameInArabic())
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
            Button b  = new Button(city.getNameInEnglish());
            b.setOnAction(actionEvent -> {
                makeNewStage(city.getCitiesAndVillages());
            });
            VBox vb = new VBox();
            vb.getChildren().add(tile);
            vb.getChildren().add(tile1);
            vb.getChildren().add(b);

            gridPane.add(vb, setRowIndex(), colIndex, 1, 1);
        }

        ScrollPane scrollPane = new ScrollPane(gridPane);
        Scene scene = new Scene(scrollPane, 620, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
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