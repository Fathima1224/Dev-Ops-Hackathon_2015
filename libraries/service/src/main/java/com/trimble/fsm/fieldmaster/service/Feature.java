package com.trimble.fsm.fieldmaster.service;

import java.util.Map;

/**
 * Created by EAlages on 26-03-2015.
 */
public class Feature {
    String status;

    Map<String, Feature> subfeatures;

    public Map<String, Feature> getSubfeatures() {
        return subfeatures;
    }

    public void setSubfeatures(Map<String, Feature> subfeaturesMap) {
        this.subfeatures = subfeaturesMap;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

	@Override
	public String toString() {
		return "Feature [status=" + status + ", subfeaturesMap="
				+ subfeatures + "]";
	}

    
}
