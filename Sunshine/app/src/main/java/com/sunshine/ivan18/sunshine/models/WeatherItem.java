package com.sunshine.ivan18.sunshine.models;

import java.util.List;

/**
 * Created by ivan18 on 09/03/15.
 */
public class WeatherItem {

    String main;
    String description;
    String icon;
    String min;
    String max;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {

        String description= getMain()+", "+getDescription()+", "+getMin()+", "+getMax()+", "+getIcon();

        return description;
    }
}
