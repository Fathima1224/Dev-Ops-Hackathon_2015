package com.trimble.fsm.fieldmaster.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpResponseHandler implements ResponseHandler<String> {

    @Override
    public String handleResponse(HttpResponse response)
            throws ClientProtocolException, IOException {

        HttpEntity entity = response.getEntity();
        if (entity != null) {
            return EntityUtils.toString(entity);
        } else {
            return null;
        }

    }

}
