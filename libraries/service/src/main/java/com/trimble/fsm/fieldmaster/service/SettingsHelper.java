package com.trimble.fsm.fieldmaster.service;

import com.trimble.fsm.fieldmaster.service.common.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by EAlages on 08-04-2015.
 */
public class SettingsHelper {
    private static Settings settings = null;

    public SettingsHelper(Settings settings){
        this.settings = settings;
    }

    public String getFeatureStatus(String feature){
        System.out.println("settings - " + settings);
        if(settings == null){
            return "hidden";
        }
        Feature featureObj = settings.getFeatures().get(feature);
        if(featureObj != null){
            return featureObj.getStatus();
        }
        else{
            return "hidden";
        }
    }

    public String getSubFeatureStatus(String feature, String subfeature){
        System.out.println("settings - " + settings);
        if(settings == null){
            return "hidden";
        }

        Feature featureObj = settings.getFeatures().get(feature);
        if(featureObj != null){
            Map<String, Feature> subfeaturesMap = featureObj.getSubfeatures();

            if(subfeaturesMap != null && subfeaturesMap.containsKey(subfeature)){
                Feature subfeatureObj = subfeaturesMap.get(subfeature);
                if(subfeatureObj != null){
                    return subfeatureObj.getStatus();
                }
            }
        }
        return "hidden";
    }

    public String[] getValueForFeature(String setting) {
        Map<String, String[]> value = settings.getAppSettings();
        if(value!= null){
          return value.get(setting);
        }
        return null;
    }
}
