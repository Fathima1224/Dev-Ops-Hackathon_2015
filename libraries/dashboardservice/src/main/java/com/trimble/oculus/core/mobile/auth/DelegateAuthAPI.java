package com.trimble.oculus.core.mobile.auth;

import android.os.Bundle;
import android.util.Log;

import com.trimble.fsm.fieldmaster.service.ApplicationContextProvider;
import com.trimble.fsm.fieldmaster.service.ServiceHelperBase;


public class DelegateAuthAPI extends ServiceHelperBase {
    ServerAuthAPI authorisationAPI = null;

    long staleTime = -1;

    private DelegateAuthAPI(String resultAction) {
        super(ApplicationContextProvider.getContext(), AuthBGService.class.getName(), resultAction);
        try {
            authorisationAPI = new ServerAuthAPI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DelegateAuthAPI() {
        this(null);
    }

    public static class BackgroundMethods {
        public static final int AUTHENTICATE = 61;
        public static final int GET_USER_INFO = 62;
        public static final String USERNAME_PARAM = "USERNAME";
        public static final String PASSWORD_PARAM = "PASSWORD";
        public static final String AUTHENTICATE_RESULT="AUTHENTICATE_RESULT";
        public static final String PERSON_INFO_LIST="AUTHENTICATE_RESULT";

    }

    public static DelegateAuthAPI getInstance() {
        return new DelegateAuthAPI();
    }

    public static DelegateAuthAPI getInstance(String returnAction) {
        return new DelegateAuthAPI(returnAction);
    }



    public String authenticateUser(String userName, String password) {
        System.out.println("auth inside delegate authenticate");
        Log.d("library", "DelegateAuthAPI authenticate user" + userName + " ," + userName);
        Bundle extras = new Bundle();
        extras.putString(DelegateAuthAPI.BackgroundMethods.USERNAME_PARAM, userName);
        extras.putString(DelegateAuthAPI.BackgroundMethods.PASSWORD_PARAM, password);
        RunMethod(DelegateAuthAPI.BackgroundMethods.AUTHENTICATE, extras);
        return null;
    }

    public String getPersonInfo(String userName){
        Bundle extras = new Bundle();
        extras.putString(DelegateAuthAPI.BackgroundMethods.USERNAME_PARAM, userName);
        RunMethod(DelegateAuthAPI.BackgroundMethods.GET_USER_INFO, extras);
        return null;
    }



}
