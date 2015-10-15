package com.trimble.oculus.core.mobile.auth;

import android.os.Bundle;
import android.util.Log;

import com.trimble.fsm.fieldmaster.service.IServiceProvider;


public class AuthBGService implements IServiceProvider {

    ServerAuthAPI serverAuthorisationAPI = null;

    @Override
    public String RunTask(int methodId, Bundle extras) {
        Log.d("library", "RunTask insider AuthBGService" + methodId);
        switch (methodId) {
            case DelegateAuthAPI.BackgroundMethods.AUTHENTICATE:
                return authenticate(extras);
            case DelegateAuthAPI.BackgroundMethods.GET_USER_INFO:
                return getPersonInfo(extras);
        }
        return null;
    }

    private String authenticate(Bundle extras) {
        System.out.println("auth inside bgservice authenticate");
        String userName = extras.getString(DelegateAuthAPI.BackgroundMethods.USERNAME_PARAM);
        String password = extras.getString(DelegateAuthAPI.BackgroundMethods.PASSWORD_PARAM);
        System.out.println("Auth BG Service authenticate userName - " + userName);
        String result=getServerAPI().authenticate1(userName,password);
        extras.putString(DelegateAuthAPI.BackgroundMethods.AUTHENTICATE_RESULT,result);
        return null;
    }

    private String getPersonInfo(Bundle extras){
        String userName = extras.getString(DelegateAuthAPI.BackgroundMethods.USERNAME_PARAM);
        String response=getServerAPI().getPersonInformation(userName);
        extras.putString(DelegateAuthAPI.BackgroundMethods.PERSON_INFO_LIST,response);
        return null;
    }

    public ServerAuthAPI getServerAPI() {
        if (serverAuthorisationAPI == null) {
            try {
                serverAuthorisationAPI = new ServerAuthAPI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return serverAuthorisationAPI;
    }
}
