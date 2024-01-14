package com.example.weatherappgui;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetData {
    public static JsonNode getJson(double latitude, double longitude) {
        StringBuilder strB = new StringBuilder("http://api.weatherapi.com/v1/current.json"+"?key=638a603c359d4dda80d25445241401&q="+latitude+"," +longitude +"&aqi=no");
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
    public static int getHumidity(JsonNode jsonNode) {
        return jsonNode.at("/current/humidity").asInt();
    }
    public static String getConditionName(JsonNode jsonNode) {
        return jsonNode.at("/current/condition/text").asText();
    }
    public static String getConditionIcon(JsonNode jsonNode) {
         return jsonNode.at("/current/condition/icon").asText();
    }
    public static Double getWindKph(JsonNode jsonNode) {
        return jsonNode.at("/current/wind_kph").asDouble();
    }
}

