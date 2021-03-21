package com.example.robodoc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.firebase.auth.SignOut;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements SignOut.SignOutInterface {

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.btnLogOut);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SignOut(MainActivity.this,MainActivity.this,getSupportFragmentManager());
            }
        });
    }

    @Override
    public void onSignOut(boolean result) {
        if(result){
            Toast.makeText(MainActivity.this,"Sign Out Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            MainActivity.this.finish();
        }
        else {
            Snackbar.make(getWindow().getDecorView().getRootView(),"Sign Out Failed, Please try Again",2000).show();
        }
    }
}