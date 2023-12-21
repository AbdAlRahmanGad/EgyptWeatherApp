package com.example.weatherappgui;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class EgyptCity {
    @JsonProperty("Latitude")
    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNameInArabic() {
        return nameInArabic;
    }

    public void setNameInArabic(String nameInArabic) {
        this.nameInArabic = nameInArabic;
    }

    public String getNameInEnglish() {
        return nameInEnglish;
    }

    public void setNameInEnglish(String nameInEnglish) {
        this.nameInEnglish = nameInEnglish;
    }

    public List<CityData> getCitiesAndVillages() {
        return citiesAndVillages;
    }

    public void setCitiesAndVillages(List<CityData> citiesAndVillages) {
        this.citiesAndVillages = citiesAndVillages;
    }

    @JsonProperty("Longitude")
    private double longitude;

    @JsonProperty("NameInArabic")
    private String nameInArabic;

    @JsonProperty("NameInEnglish")
    private String nameInEnglish;

    @JsonProperty("CitiesAndVillages")
    private List<CityData> citiesAndVillages;

    // Getter and setter methods
}

class CityData {
    @JsonProperty("Latitude")
    private double latitude;

    @JsonProperty("Longitude")
    private double longitude;

    @JsonProperty("NameInArabic")
    private String nameInArabic;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getNameInArabic() {
        return nameInArabic;
    }

    public void setNameInArabic(String nameInArabic) {
        this.nameInArabic = nameInArabic;
    }

    public String getNameInEnglish() {
        return nameInEnglish;
    }

    public void setNameInEnglish(String nameInEnglish) {
        this.nameInEnglish = nameInEnglish;
    }

    @JsonProperty("NameInEnglish")
    private String nameInEnglish;

    // Getter and setter methods
}
