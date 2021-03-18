package com.example.robodoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity  implements FetchUserInfo.FetchedUserInfoCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    new FetchUserInfo(getSupportFragmentManager(),FirebaseAuth.getInstance().getCurrentUser().getUid(),SplashScreenActivity.this);
                }
                else {
                    startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                    SplashScreenActivity.this.finish();
                }
            }
        },1500);
    }

    @Override
    public void onUserDataFetched(HashMap<String, Object> UserData) {

    }
}