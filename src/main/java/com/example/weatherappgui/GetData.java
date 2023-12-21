package com.example.weatherappgui;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.module.FindException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetData {

    public static String getTemp(double latitude, double longitude) {

        String apiUrl = "https://api.open-meteo.com/v1/forecast?current=temperature_2m&latitude=" + latitude + "&longitude=" + longitude;
        try {
            String jsonResponse = makeHttpRequest(apiUrl);

            JsonNode jsonNode = parseJsonResponse(jsonResponse);

            return handleWeatherData(jsonNode);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String makeHttpRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        } finally {
            connection.disconnect();
        }
    }

    private static JsonNode parseJsonResponse(String jsonResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonResponse);
    }

    private static String handleWeatherData(JsonNode jsonNode) {

        return jsonNode.at("/current/temperature_2m").asText();
//        System.out.println("Current Temperature: " + temperature + "°C");
    }
}

