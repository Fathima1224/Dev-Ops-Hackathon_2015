package com.trimble.fsm.fieldmaster.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.AndroidRuntimeException;
import android.util.Base64;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.trimble.fsm.fieldmaster.service.common.ObscuredSharedPreferences;
import com.trimble.fsm.fieldmaster.service.common.ServiceUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class ServiceHelper {

    public static final String AUTH_TKT_STR = "auth_tkt";
    public static final String BEARER_TOKEN = "bearer";
    public static final String REFRESH_TOKEN = "refresh_token";
    public static final String EXP_DT = "exprDt";
    public static final String EXP_TIME = "exprTime";
    public static final String LOGIN_TIMESTAMP_STR = "LOGIN_TIMESTAMP";
    public static final String LOGIN_TIME_LONG = "LOGIN_TIME_LONG";
    public static final int EXPIRY_IN_MINUTE = 43200;
    public static final String USERNAME_STR = "userName";
    public static final String PASSWORD_STR = "password";
    public static final String RESOURCE_ID_STR = "resourceId";
    public static final String  PASSWORD_PARAM= "PASSWORD";
    public static final String ERR_INVALID_USER_CREDENTIALS = "ERR_INVALID_USER_CREDENTIALS";
    public static final String ERR_NO_EMPLOYEE_ASSIGNED = "ERR_NO_EMPLOYEE_ASSIGNED";
    public static final String ERR_FINDING_EMPLOYEE = "ERR_FINDING_EMPLOYEE";
    public static final String ERR_NO_DEVICE_ASSOCIATION = "ERR_NO_DEVICE_ASSOCIATION";
    public static final String ERR_SYSTEM_RETRY = "ERR_SYSTEM_RETRY";
    public static final String SYSTEM_ERROR = "SYSTEM_ERROR";
    public static final String TIMEOUT_ERROR = "TIMEOUT_ERROR";
    public static final String NO_NETWORK_CONNECTION = "NO_NETWORK_CONNECTION";
    public static final String USER_MODIFIED_OR_DELETED = "USER_MODIFIED_OR_DELETED";
    public static final String ERR_NO_MOBILE_APP_ACCESS = "ERR_NO_MOBILE_APP_ACCESS";
    public static final String AUTH_TOKEN_URL = "/wsapi/v3/tokens";   
    public static final int ACCESS_METHOD_GET = 1;
    public static final int ACCESS_METHOD_PUT = 2;
    public static final int ACCESS_METHOD_POST = 3;
    public static final int ACCESS_METHOD_DELETE = 4;
    public static final int JSON_RPC_METHOD = 5;
    public static final int SOAP_METHOD = 6;
    static final int CONNECTION_TIME_OUT = 60000;
    private static final String URL_LOGIN = "/application/signon/secured/login.cgi";
    private static final String URL_COMPOSITE_LOGIN =  "/wsapi/v3/fieldmaster/login";
    static HttpContext localContext = new BasicHttpContext();
    static CookieStore cookieStore = new BasicCookieStore();
    static ArrayList<String> authorizationKeys;
    static SimpleDateFormat inFormatter = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z' z");

    static SharedPreferences preference = new ObscuredSharedPreferences(ApplicationContextProvider.getContext(), PreferenceManager.getDefaultSharedPreferences(ApplicationContextProvider.getContext()));
    static SharedPreferences.Editor editor = preference.edit();

    private static final String CONSUMER_KEY="NZy0X9bEYKZcIxCwnt6V8A4Kzoca";
    private static final String CONSUMER_SECRET="HImhZOSgcubWYsrxqiVUdwwt0Dka";

//    private static final String CONSUMER_KEY="NkY0wQwH6Nnfwx3MXFRDVV7RBvAa";
//    private static final String CONSUMER_SECRET="YUNQzfDJ424ULqsk2PO0OgOjiAIa";





    public static String getHostName() {
        SharedPreferences pref = new ObscuredSharedPreferences(ApplicationContextProvider.getContext(), PreferenceManager.getDefaultSharedPreferences(ApplicationContextProvider.getContext()));
        return pref.getString("HOSTNAME", null);
    }

    public static String getAuthCookie() {
        return null;
    }

    public static String authenticateUser(String userName,String password){
        String url=getHostName()+"/accessToken/v4/";
        String responseStr = null;
        HttpPost httpPost = null;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try{
            JSONObject requestObject=new JSONObject();
            JSONObject oauthObject=new JSONObject();

            oauthObject.put("grantType","password");
            oauthObject.put("clientId",CONSUMER_KEY);
            oauthObject.put("clientSecret",CONSUMER_SECRET);
            oauthObject.put("username",userName);
            oauthObject.put("password", password);

            requestObject.put("oauth2", oauthObject);

            httpPost=new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            StringEntity entity = new StringEntity(requestObject.toString());
            httpPost.setEntity(entity);
            System.out.println("authenticateUser() calling :"+url+" requestBody: "+requestObject.toString());
            responseStr=httpClient.execute(httpPost,
                    new HttpResponseHandler());
            System.out.println("authenticateUser() response: "+responseStr);

            JSONObject responseObject=new JSONObject(responseStr);
            String expTime=responseObject.getJSONObject("oauth2").getString("expiresIn");
            String refreshToken=responseObject.getJSONObject("oauth2").getString("refreshToken");
            String bearerToken=responseObject.getJSONObject("oauth2").getString("accessToken");
            System.out.println("Service Helper: access token:" + bearerToken);
            Calendar expCalendar=Calendar.getInstance();
           // expCalendar.add(Calendar.SECOND, Integer.parseInt(expTime));
            expCalendar.add(Calendar.SECOND, 60);

            editor.putLong(EXP_TIME, expCalendar.getTimeInMillis());
            editor.putString(REFRESH_TOKEN,refreshToken);
            editor.putString(BEARER_TOKEN,bearerToken);
            if(userName!=null && userName.length()>0){
                editor.putString(USERNAME_STR,userName);
            }
            if(password!=null && password.length()>0){
                editor.putString(PASSWORD_STR,password);
            }
            editor.commit();
            return bearerToken;

        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }


    public static String getAuthToken(){

        String bearerToken;
        if((bearerToken=preference.getString(BEARER_TOKEN,null))!=null){
            return bearerToken;
        }else{
            bearerToken=authenticateUser(preference.getString(USERNAME_STR,""),preference.getString(PASSWORD_STR,""));
            return  bearerToken;
        }
    }

    public static String getRefreshToken(){
        System.out.println("*********getting refresh Token***********");
        String url=ServiceHelper.getHostName()+"/accessToken/v4/refresh";
        JSONObject requestObject=new JSONObject();
        JSONObject responseObject=new JSONObject();
        String responseStr;
        HttpPost httpPost;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        try{
            JSONObject oauthObject=new JSONObject();
            oauthObject.put("refreshToken",preference.getString(REFRESH_TOKEN,""));
            oauthObject.put("clientId", CONSUMER_KEY);
            oauthObject.put("clientSecret", CONSUMER_SECRET);

            requestObject.put("oauth2", oauthObject);

            httpPost=new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "application/json");
            StringEntity entity = new StringEntity(requestObject.toString());
            httpPost.setEntity(entity);
            responseStr=httpClient.execute(httpPost,
                    new HttpResponseHandler());

            responseObject=new JSONObject(responseStr);
            String expTime=responseObject.getJSONObject("oauth2").getString("expiresIn");
            String refreshToken=responseObject.getJSONObject("oauth2").getString("refreshToken");
            String bearerToken=responseObject.getJSONObject("oauth2").getString("accessToken");
            Calendar expCalendar=Calendar.getInstance();
            expCalendar.add(Calendar.SECOND, Integer.parseInt(expTime));

            editor.putLong(EXP_TIME, expCalendar.getTimeInMillis());
            editor.putString(REFRESH_TOKEN,refreshToken);
            editor.putString(BEARER_TOKEN, bearerToken);
            editor.commit();

            return bearerToken;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getAuthTokenOld() {
        Date currentDate = Calendar.getInstance().getTime();
        inFormatter.setTimeZone(TimeZone.getDefault());
        SharedPreferences prefs = new ObscuredSharedPreferences(ApplicationContextProvider.getContext(), PreferenceManager
                .getDefaultSharedPreferences(ApplicationContextProvider
                        .getContext()));
        String bearerToken = prefs.getString(BEARER_TOKEN, null);
        Date exprDt = new Date(prefs.getLong(EXP_DT, 0));
        if (bearerToken == null || exprDt == null || exprDt.before(currentDate)) {
            String url = getHostName() + AUTH_TOKEN_URL;
            JSONObject reqObject = new JSONObject();
            JSONObject identityObject = new JSONObject();
            String responseStr = null;
            HttpPost httpPost = null;
            DefaultHttpClient httpClient = HttpsClientManager.getHttpsClient();

            try {
                identityObject.put("username",
                        prefs.getString(USERNAME_STR, null));
                identityObject.put("password",
                        new String(prefs.getString(PASSWORD_STR, null)));
                reqObject.put("identity", identityObject);

                httpPost = new HttpPost(url);
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Accept", "application/json");

                StringEntity entity = new StringEntity(reqObject.toString());
                httpPost.setEntity(entity);
                responseStr = httpClient.execute(httpPost,
                        new HttpResponseHandler());
                Log.d("ServiceHelper", "getAuthToken - responseStr - " + responseStr);
                JSONObject tokens = (new JSONObject(responseStr))
                        .getJSONObject("token");
                bearerToken = tokens.getString(BEARER_TOKEN);
                String exprDtStr = tokens.getString("exprDt");
                Log.d("library", "exprDtStr - " + exprDtStr);
                exprDt = inFormatter.parse(exprDtStr + " GMT");
                Log.d("library", "exprDt - " + exprDt);
                Editor editor = prefs.edit();
                editor.putString(BEARER_TOKEN, bearerToken);
                editor.putLong(EXP_DT, exprDt.getTime());
                editor.commit();
            } catch (Exception e) {
                e.printStackTrace();
                throw new AndroidRuntimeException(ERR_NO_MOBILE_APP_ACCESS);
            }
        }
        Log.d("ServiceHelper", "getAuthToken - bearerToken - " + bearerToken);
        return bearerToken;
    }

    public static String makeServerCall(String url, String requestBody,
                                        int accessMethod) throws JSONException, IOException, ParseException {
        return makeServerCall(url, requestBody, accessMethod, null);
    }

    public static String makeServerCall(String url, String requestBody,
                                        int accessMethod, String methodAction) throws JSONException, ParseException, IOException {
        return makeServerCall(url, requestBody, accessMethod, methodAction, null, null);
    }

    public static String makeServerCall(String url, String requestBody,
                                        int accessMethod, String methodAction, File file, String extension) throws JSONException, IOException, ParseException, ConnectTimeoutException {
        StringBuilder sb = null;
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        Log.d("ServiceHelper", "url - " + url + " ,requestBody - "
                + requestBody + " ,accessMethod - " + accessMethod + ",methodAction - " + methodAction + ",file - " + file);
      //  DefaultHttpClient httpClient = HttpsClientManager.getHttpsClient();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpParams params = new BasicHttpParams();//setting timeout for request and response
        HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIME_OUT);
        HttpConnectionParams.setSoTimeout(params, CONNECTION_TIME_OUT);
        httpClient.setParams(params);

        String authParam = "";
      System.out.println("serviceHelper makeservercall - " + url + ","
            + requestBody + "," + accessMethod + "," + methodAction);
        if (isRESTCall(accessMethod)) {
            Calendar expCalendar=Calendar.getInstance();
            Calendar currentCalendar=Calendar.getInstance();
            long expTime=preference.getLong(EXP_TIME,0);
            expCalendar.setTimeInMillis(expTime);
            if(currentCalendar.before(expCalendar)){
                authParam = getAuthToken();

            }else{
                authParam=getRefreshToken();
            }
            System.out.println("Service Helper Auth Token for url: " + url + "token: " + authParam);
           // authParam = "e2468b34eacbfa304075b62510ea4afa";//TODO Remove
            //authParam = "c4e042cc7ede6d77db46118e1effb65";// Angeerasa local
           // authParam="beab44f36c4cea53d07ee872deda641";
        } else {
            authParam = getAuthCookie();
        }

        if (authParam == null) {
            sendBroadcastIfUserModifiedOrDeleted();
        }

        HttpResponse response = null;

        String responseStr = null;
        switch (accessMethod) {
            case ACCESS_METHOD_GET:
                response = makeGetCall(httpClient, url, authParam);
                break;
            case ACCESS_METHOD_PUT:
                response = makePutCall(httpClient, url, requestBody, authParam);
                break;
            case ACCESS_METHOD_POST:
                response = makePostCall(httpClient, url, requestBody, authParam, file, extension);
                break;
            case ACCESS_METHOD_DELETE:
                response = makeDeleteCall(httpClient, url, authParam);
                break;
        }

        // response status code check
        StatusLine status = response.getStatusLine();
        Log.d("library", "serviceHelper - status - " + status);
        if (status.getStatusCode() == HttpStatus.SC_OK || status.getStatusCode()==HttpStatus.SC_CREATED) {
            if (file != null && accessMethod == ACCESS_METHOD_GET) {
               FileOutputStream fos = null;
               HttpEntity entity = response.getEntity();
               fos = new FileOutputStream(file);
               entity.writeTo(fos);
               entity.consumeContent();
               fos.flush();
            }
            else {
            sb = responseReader(response);
            responseStr = sb.toString();
                Log.d("ServiceHelper"," Response content - " + responseStr + " for the url - " + url + " ,requestBody - "
                        + requestBody + " ,accessMethod - " + accessMethod + ",methodAction - " + methodAction + ",file - " + file);
            if (responseStr != null && responseStr.contains("CTXMGR_EXCEPTION")) {
            sendBroadcastIfUserModifiedOrDeleted();
            }
            }
        } //else if(status.getStatusCode() == HttpStatus.SC_UNAUTHORIZED){
//            //refreshToken
//            //makeServerCall
//            StringBuilder s=responseReader(response);
//            System.out.println("Service Helper 401 response: "+s.toString());
//            getRefreshToken();
//            makeServerCall(url,requestBody,accessMethod,methodAction,file,extension);
//        }
        else {
            sb = responseReader(response);
            Log.d("ServiceHelper"," Error Response - " + sb + " for the url - " + url + " ,requestBody - "
                    + requestBody + " ,accessMethod - " + accessMethod + ",methodAction - " + methodAction + ",file - " + file);
            throw new AndroidRuntimeException(SYSTEM_ERROR);
        }
        return responseStr;
    }

    private static boolean isRESTCall(int accessMethod) {
        return accessMethod == ACCESS_METHOD_GET || accessMethod == ACCESS_METHOD_PUT || accessMethod == ACCESS_METHOD_POST || accessMethod == ACCESS_METHOD_DELETE;
    }



    private static HttpResponse makePostCall(DefaultHttpClient httpClient, String serviceUrl, String requestBody, String authParam, File file, String extension) throws ClientProtocolException, IOException {

         String contentType = "application/json;charset=UTF-8";
         StringEntity entity = new StringEntity(requestBody);

         HttpPost httpPost = new HttpPost(serviceUrl);
         httpPost.setEntity(entity);

         if (file != null && file.exists()) {
             contentType = "multipart/form-data";
             MultipartEntity multiPartEntity = new MultipartEntity();
             FileBody fileBody = null;

             MimeTypeMap mime = MimeTypeMap.getSingleton();
             String type = mime.getMimeTypeFromExtension(extension);
             fileBody = new FileBody(file, System.currentTimeMillis()+"."+extension,type,null);
             System.out.println("fileBody -- " + fileBody);
             //Prepare payload
             multiPartEntity.addPart("attachment", fileBody);
             //Set to request body
             httpPost.setEntity(multiPartEntity);
         }

         httpPost.setHeader("Content-Type", contentType);
         httpPost.setHeader("Accept", "application/json");
         httpPost.setHeader("Authorization", "Bearer " + authParam);
         System.out.println("contentType - " + contentType + " , extension " + extension + ", authParam - " + authParam);

         return httpClient.execute(httpPost,
                 localContext);
    }

   private static HttpResponse makeGetCall(DefaultHttpClient httpClient,
                                            String url, String authParam) throws IOException {
        HttpGet httpget = new HttpGet(url);
        httpget.setHeader("Accept", "application/json");
        httpget.setHeader("Content-Type", "application/json");
        httpget.setHeader("Authorization", "Bearer " + authParam);
        return httpClient.execute(httpget, localContext);
    }

   private static HttpResponse makeDeleteCall(DefaultHttpClient httpClient,
                                               String url, String authParam) throws IOException {
        HttpDelete httpdelete = new HttpDelete(url);
        httpdelete.setHeader("Accept", "application/json");
        httpdelete.setHeader("Content-Type", "application/json");
        httpdelete.setHeader("Authorization", "Bearer " + authParam);
        return httpClient.execute(httpdelete, localContext);
    }

   private static HttpResponse makePutCall(DefaultHttpClient httpClient,
         String url, String requestBody, String authParam)
            throws IOException {
        HttpPut httpPut = new HttpPut(url);
        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-Type", "application/json");
        httpPut.setHeader("Authorization", "Bearer " + authParam);
        if (requestBody != null) {
            StringEntity params = new StringEntity(requestBody);
            httpPut.setEntity(params);
        }
        return httpClient.execute(httpPut, localContext);
    }

    /**
     * @param response
     * @return
     * @throws IllegalStateException
     */
    public static StringBuilder responseReader(HttpResponse response)
            throws IllegalStateException {
        StringBuilder sb = null;
        try {
            // writing response to log
            InputStream in = response.getEntity().getContent();
            InputStreamReader is = new InputStreamReader(in);
            sb = new StringBuilder();
            BufferedReader br = new BufferedReader(is);
            String read = br.readLine();
            while (read != null) {
                // Log.d(read);
                sb.append(read);
                read = br.readLine();
            }
        } catch (ClientProtocolException e) {
            // writing exception to log
            e.printStackTrace();
        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }
        return sb;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            Log.w("LOG", e.getMessage());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.w("LOG", e.getMessage());
            }
        }
        return sb.toString();
    }

    public static boolean isInternetConnectionAvailable() {
        boolean isWifiConnected = false;
        boolean isMobileConnected = false;
        boolean isInternetConnected = false;
        Context context = ApplicationContextProvider.getContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {

                    if (info[i].getTypeName().toLowerCase().indexOf("wifi") != -1) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            // Toast.makeText(this, info[i].getState(),
                            // Toast.LENGTH_LONG).show();
                            isWifiConnected = true;
                        }
                    } else if (info[i].getTypeName().toLowerCase().indexOf("mobile") != -1) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            // Toast.makeText(this, info[i].getState(),
                            // Toast.LENGTH_LONG).show();
                            isMobileConnected = true;
                        }
                    }

                }
            }
        }
        /*
         * if (!(isWifiConnected || isMobileConnected)) { throw new
		 * AndroidRuntimeException(NO_NETWORK_CONNECTION); }
		 */

        if (isWifiConnected || isMobileConnected)
            isInternetConnected = true;

        Log.d("Internet connection", "isWifiConnected:" + isWifiConnected
                + ",isMobileConnected:" + isMobileConnected
                + ",isInternetConnected:" + isInternetConnected);

        return isInternetConnected;

    }

    public static String convertURL(String str) {

        String url = "";
        if (null != str) {
            try {
                url = new String(str.trim().replace(" ", "%20")
                        .replace("&", "%26").replace(",", "%2c")
                        .replace("(", "%28").replace(")", "%29")
                        .replace("!", "%21").replace("=", "%3D")
                        .replace("<", "%3C").replace(">", "%3E")
                        .replace("#", "%23").replace("$", "%24")
                        .replace("'", "%27").replace("*", "%2A")
                        .replace("-", "%2D").replace(".", "%2E")
                        .replace("/", "%2F").replace(":", "%3A")
                        .replace(";", "%3B").replace("?", "%3F")
                        .replace("@", "%40").replace("[", "%5B")
                        .replace("\\", "%5C").replace("]", "%5D")
                        .replace("_", "%5F").replace("`", "%60")
                        .replace("{", "%7B").replace("|", "%7C")
                        .replace("}", "%7D"));
            } catch (Exception e) {
                e.printStackTrace();
                url = "";
            }
        }
        return url;
    }

    public static String capitalizeFirstLetters(String line) {
        if (line == null || line.length() == 0) {
            return line;
        }
        StringTokenizer st = new StringTokenizer(line);
        StringBuffer returnLineBuffer = new StringBuffer();
        while (st.hasMoreTokens()) {
            String a = st.nextToken();
            if (a.length() > 1) {
                returnLineBuffer.append((a.charAt(0) + "").toUpperCase()
                        + a.substring(1).toLowerCase());
            } else {
                returnLineBuffer.append((a.charAt(0) + "").toUpperCase());
            }
            returnLineBuffer.append(" ");
        }
        return returnLineBuffer.toString();
    }

   private static void sendBroadcastIfUserModifiedOrDeleted() {
      Context context = ApplicationContextProvider.getContext();
      Bundle mExtras = new Bundle();
      Intent resultIntent = new Intent(ServiceUtils.SERVICE_ACTION_RESULT);
      mExtras.putInt(ProcessorService.Extras.METHOD_EXTRA,
            ServiceUtils.USER_LOGOUT);
      resultIntent.putExtras(mExtras);
      resultIntent.setPackage(context.getPackageName());
      context.sendBroadcast(resultIntent);
   }
}
