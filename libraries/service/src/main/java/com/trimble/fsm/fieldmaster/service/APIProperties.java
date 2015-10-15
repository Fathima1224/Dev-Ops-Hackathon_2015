package com.trimble.fsm.fieldmaster.service;

import java.util.HashMap;
import java.util.Map;

public class APIProperties {

    private static APIProperties apiProperties = new APIProperties();

    private Map<String, Object> propertiesMap = new HashMap<String, Object>();

    private APIProperties() {
    }

    public static APIProperties getInstance() {
        return apiProperties;
    }

    public Object getValue(String key) {
        Object value = null;
        value = propertiesMap.get(key);
        return value;
    }

    public void setValue(String key, Object value) {
        propertiesMap.put(key, value);
    }
}
