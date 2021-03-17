package com.example.robodoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    intent=new Intent(SplashScreenActivity.this,MainActivity.class);
                    intent.putExtra("User", (Parcelable) new User(SplashScreenActivity.this,FirebaseAuth.getInstance().getCurrentUser().getUid()));
                }
                else {
                    intent=new Intent(SplashScreenActivity.this,LoginActivity.class);
                }
                startActivity(intent);
                SplashScreenActivity.this.finish();
            }
        },1500);
    }
}