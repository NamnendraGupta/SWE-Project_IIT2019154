package com.example.robodoc.application;

import com.example.robodoc.firebase.Globals;

public class ApplicationClass extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Globals.initializeGlobals();
    }
}
