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

    public static JsonNode getJson(double latitude, double longitude) {
//         String str= "http://api.weatherapi.com/v1/current.json";
//        String apiUrl = "https://api.open-meteo.com/v1/forecast?current=temperature_2m&latitude=" + latitude + "&longitude=" + longitude;
        StringBuilder strB = new StringBuilder("http://api.weatherapi.com/v1/current.json"+"?key=6b71275997ab41c3bc0122144232112&q="+latitude+"," +longitude +"&aqi=no");
                String apiUrl = strB.toString();
        try {
            String jsonResponse = makeHttpRequest(apiUrl);

            JsonNode jsonNode = parseJsonResponse(jsonResponse);

            return jsonNode;

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

    public static Double getTemp_c(JsonNode jsonNode) {
        return jsonNode.at("/current/temp_c").asDouble();
    }
    public static Double getHumidity(JsonNode jsonNode) {
        return jsonNode.at("/current/humidity").asDouble();
    }
    public static String getConditionName(JsonNode jsonNode) {
        return jsonNode.at("/current/condition/text").asText();
    }
    public static String getConditionIcon(JsonNode jsonNode) {
         return jsonNode.at("/current/condition/icon").asText();
    }
}

