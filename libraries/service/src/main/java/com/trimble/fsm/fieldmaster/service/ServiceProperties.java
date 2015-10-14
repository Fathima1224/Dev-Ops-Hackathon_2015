package com.trimble.fsm.fieldmaster.service;

import java.util.HashMap;
import java.util.Map;

public class ServiceProperties {

    private static ServiceProperties serviceProperties = new ServiceProperties();

    private Map<String, Object> propertiesMap = new HashMap<String, Object>();

    private ServiceProperties() {
    }

    public static ServiceProperties getInstance() {
        return serviceProperties;
    }

    public Object getValue(String key) throws Exception {
        Object value = null;
        value = propertiesMap.get(key);
        if (value == null) {
            throw new Exception(key + "is not set. Initialise " + key
                    + "before calling the instance creation");
        }
        return value;
    }

    public void setValue(String key, Object value) {
        propertiesMap.put(key, value);
    }
}
