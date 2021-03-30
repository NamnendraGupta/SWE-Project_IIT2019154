package com.example.robodoc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.robodoc.R;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.firestore.FetchCurrentUserInfo;

public class SplashScreenActivity extends AppCompatActivity implements FetchCurrentUserInfo.FetchInfoInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            if(Globals.isUserLoggedIn()){
                new FetchCurrentUserInfo(getSupportFragmentManager(),SplashScreenActivity.this);
            }
            else{
                startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                SplashScreenActivity.this.finish();
            }
        },2000);
    }

    @Override
    public void onUserDataFetched(boolean result) {
        if(result)
            startActivity(new Intent(SplashScreenActivity.this,MainActivity.class));
        SplashScreenActivity.this.finish();
    }
}