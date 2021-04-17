package com.example.robodoc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.auth.SignOut;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

public class AdminActivity extends AppCompatActivity implements SignOut.SignOutInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        MaterialToolbar toolbar=findViewById(R.id.adminToolbar);

        NavController navController= Navigation.findNavController(this,R.id.navHostAdmin);
        AppBarConfiguration appBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration);

        if(Globals.isUserDoctor())
            toolbar.getMenu().findItem(R.id.aMenuDoctor).setVisible(true);

        toolbar.setOnMenuItemClickListener(item -> {
            Menu menu=toolbar.getMenu();
            if(item==menu.findItem(R.id.aMenuDoctor)){
                startActivity(new Intent(this,DoctorActivity.class));
                this.finish();
            }
            else if(item==menu.findItem(R.id.aMenuUser)){
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
            }
            else if(item==menu.findItem(R.id.aMenuLogout)){
                new SignOut(this,this,getSupportFragmentManager());
            }
            return false;
        });
    }

    @Override
    public void onSignOut(boolean result) {
        if(result){
            Toast.makeText(AdminActivity.this,"Sign Out Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminActivity.this,LoginActivity.class));
            AdminActivity.this.finish();
        }
        else {
            Snackbar.make(getWindow().getDecorView().getRootView(),"Sign Out Failed, Please try Again",2000).show();
        }
    }
}