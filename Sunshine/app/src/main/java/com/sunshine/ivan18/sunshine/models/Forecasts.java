package com.sunshine.ivan18.sunshine.models;

import java.util.List;

/**
 * Created by ivan18 on 09/03/15.
 */
public class Forecasts {

    WeatherItem weather;
    TemperatureItem temp;

    String city;

    public Forecasts(WeatherItem weather, TemperatureItem temp, String city) {
        this.weather = weather;
        this.temp = temp;
        this.city = city;
    }

    public WeatherItem getWeather() {

        return weather;
    }

    public void setWeather(WeatherItem weather) {
        this.weather = weather;
    }

    public TemperatureItem getTemp() {
        return temp;
    }

    public void setTemp(TemperatureItem temp) {
        this.temp = temp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
