package com.example.weatherappgui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

public class GetCitiesNames {
    public static List<EgyptCity> getCities() {

        String jsonFile = "src/main/resources/EgyptCities.json";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return (List)objectMapper.readValue(new File(jsonFile), new TypeReference<List<EgyptCity>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
