package com.trimble.fsm.fieldmaster.service;

import java.util.Map;

/**
 * Created by EAlages on 26-03-2015.
 */
public class Settings {
    Map<String, Feature> features;

    Map<String,  String[]> appSettings;

    public Map<String, Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Feature> features) {
        this.features = features;
    }

    public Map<String, String[]> getAppSettings() {
        return appSettings;
    }

    public void setAppSettings(Map<String,  String[]> appSettings) {
        this.appSettings = appSettings;
    }

    @Override
    public String toString() {
        return "Settings [featuresMap=" + features + ", appSettingsMap="
                + appSettings + "]";
    }
}


