package com.trimble.fsm.fieldmaster.service;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class APIFactory {

    private Map<String, Object> apiMap;
    private Set<String> apiSet;

    private final String logTag = "APIFactory";

    private static APIFactory factory = null;

    private APIFactory() {

    }

    public static synchronized APIFactory getInstance() throws Exception {
        if (factory == null) {
            factory = new APIFactory();
            factory.init();
        }
        return factory;
    }

    public Object getAPI(String apiName) {
        if (apiSet.contains(apiName)) {
            return apiMap.get(apiName);
        }
        return null;
    }

    private void init() {

        apiMap = new HashMap<String, Object>();

        Log.d(logTag, "Initialising");

        JSONObject serviceConfig = null;
        try {
            String jsonConfig = (String) APIProperties.getInstance().getValue(
                    "API_CONFIG");
            serviceConfig = new JSONObject(jsonConfig)
                    .getJSONObject("apidefinition");
        } catch (JSONException e1) {
            e1.printStackTrace();
            return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

		Log.d(logTag, "After init: APIMap");

        apiSet = apiMap.keySet();
    }

}
