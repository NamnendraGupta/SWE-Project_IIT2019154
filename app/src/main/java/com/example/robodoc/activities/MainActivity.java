package com.example.robodoc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.classes.User;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.auth.SignOut;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements SignOut.SignOutInterface {

    MaterialToolbar toolbar;
    User currentUser;
    TextView tvName;
    ImageView imgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        tvName=findViewById(R.id.tvUserName);
        imgUser=findViewById(R.id.imgUser);

        updateInterface();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menuLogout:{
                        signOut();
                        break;
                    }
                    case R.id.menuAdmin:{
                        startAdminActivity();
                        break;
                    }
                    case R.id.menuDoctor:{
                        startDoctorActivity();
                        break;
                    }
                }

                return false;
            }
        });
    }

    private void updateInterface(){
        currentUser=Globals.getCurrentUser();
        if(currentUser.isAdmin())
            toolbar.getMenu().findItem(R.id.menuAdmin).setVisible(true);
        if(currentUser.isDoctor())
            toolbar.getMenu().findItem(R.id.menuDoctor).setVisible(true);

        tvName.setText("Hello "+currentUser.getName());
        Picasso.get().load(currentUser.getPhotoURL()).into(imgUser);
    }

    private void startAdminActivity(){
        startActivity(new Intent(this,AdminActivity.class));
        this.finish();
    }

    private void startDoctorActivity(){
        startActivity(new Intent(this, DoctorActivity.class));
        this.finish();
    }

    private void ShowSnackbar(String data){
        Snackbar.make(getWindow().getDecorView().getRootView(),data,3000).show();
    }

    private void signOut(){
        new SignOut(this,this,getSupportFragmentManager());
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