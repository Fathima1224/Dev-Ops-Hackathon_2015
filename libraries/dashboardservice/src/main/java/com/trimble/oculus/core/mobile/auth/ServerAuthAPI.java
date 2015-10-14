package com.trimble.oculus.core.mobile.auth;


import android.util.Log;

import com.trimble.fsm.fieldmaster.service.ServiceHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ServerAuthAPI {

    private static final String URL_AUTHENTICATION = "http://54.69.61.57:8080/Hackathon/api/v1/index";
  //  private static final String URL_AUTHENTICATION = "/persons/v4";
    public static final String AUTHENTICATION_RESULT_SUCCESS="SUCCESS";
    public static final String AUTHENTICATION_RESULT_FAILURE="FAILURE";


    public String authenticate(String userName,String password){

        try{
            String bearerToken=ServiceHelper.authenticateUser(userName,password);
            if(bearerToken!=null){
                return  AUTHENTICATION_RESULT_SUCCESS;
            }else{
                return AUTHENTICATION_RESULT_FAILURE;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return AUTHENTICATION_RESULT_FAILURE;

    }

    public String authenticate1(String userName,String password){
//         System.out.println("auth inside serverapi authenticate username: "+userName);
//         String[] usernameSplitArray=userName.split("@");
//         String url=ServiceHelper.getHostName()+URL_AUTHENTICATION+"?emailAdrs="+userName;
//        // String url=ServiceHelper.getHostName()+URL_AUTHENTICATION;
//         String response;
//         JSONObject responseObject;
//         try{
//             response=ServiceHelper.makeServerCall(url,null,ServiceHelper.ACCESS_METHOD_GET);
//             System.out.println("auth response:"+response);
//             responseObject=new JSONObject(response);
//             JSONArray personsArray=responseObject.getJSONArray("persons");
//             for(int i=0;i<personsArray.length();i++){
//                 JSONObject person=personsArray.getJSONObject(i);
//                 String emailId=person.getJSONObject("uId").getString("emailAdrs");
//                 if(emailId.equals(userName)){
//                     return AUTHENTICATION_RESULT_SUCCESS;
//                 }
//             }
//             return AUTHENTICATION_RESULT_FAILURE;
//         }catch(Exception e){
//             e.printStackTrace();
//         }
//
//        return AUTHENTICATION_RESULT_FAILURE;


        StringBuilder stringBuilder = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL_AUTHENTICATION);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
                System.out.println("Webservice call Resonse: "+stringBuilder.toString());
            } else {
                Log.d("JSON", "Failed to download file");
            }
        } catch (Exception e) {
            Log.d("readJSONFeed", e.getLocalizedMessage());
        }
        return stringBuilder.toString();




    }

    public String getPersonInformation(String userName){
        System.out.println("auth inside serverapi authenticate");
        String[] usernameSplitArray=userName.split("@");
        //  String url=ServiceHelper.getHostName()+URL_AUTHENTICATION+"?emailAdrs="+usernameSplitArray[0];
        String url=ServiceHelper.getHostName()+URL_AUTHENTICATION+"?emailAdrs="+userName;
        String response;
        JSONObject responseObject;
        String responseString;
        try{
            response=ServiceHelper.makeServerCall(url,null,ServiceHelper.ACCESS_METHOD_GET);
            System.out.println("auth response:"+response);
            responseObject=new JSONObject(response);
            JSONArray personsArray=responseObject.getJSONArray("persons");
            for(int i=0;i<personsArray.length();i++){
                JSONObject person=personsArray.getJSONObject(i);
                String emailId=person.getJSONObject("uId").getString("emailAdrs");
                if(emailId.equals(userName)){
                    responseString=person.toString();
                    return responseString;
                }
            }
            return null;
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;

    }




}
