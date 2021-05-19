package com.example.robodoc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.robodoc.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class StartupActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        toolbar=findViewById(R.id.toolbarStartupActivity);

        Intent intent=getIntent();
        boolean AfterLogout=intent.getBooleanExtra("AfterLogout",false);

        navController=Navigation.findNavController(this,R.id.navHostStartup);
        AppBarConfiguration appBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration);

        if(AfterLogout){
            navController.navigate(R.id.ActionStartLogin);
            Snackbar.make(getWindow().getDecorView().getRootView(),"Sign Out Successful",2500).show();
        }
        else {
            new Handler().postDelayed(() -> {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                    startActivity(new Intent(this,MainActivity.class));
                    this.finish();
                }
                else {
                    navController.navigate(R.id.ActionStartLogin);
                }
            },1500);
        }

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId()==R.id.registerFragment)
                toolbar.setVisibility(View.VISIBLE);
            else
                toolbar.setVisibility(View.GONE);
        });
    }
}