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

public class DoctorActivity extends AppCompatActivity implements SignOut.SignOutInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        MaterialToolbar toolbar=findViewById(R.id.doctorToolbar);

        NavController navController= Navigation.findNavController(this,R.id.navHostDoctor);
        AppBarConfiguration appBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration);

        if(Globals.isUserAdmin())
            toolbar.getMenu().findItem(R.id.dMenuAdmin).setVisible(true);

        toolbar.setOnMenuItemClickListener(item -> {
            Menu menu=toolbar.getMenu();
            if(item==menu.findItem(R.id.dMenuAdmin)){
                startActivity(new Intent(this,AdminActivity.class));
                this.finish();
            }
            else if(item==menu.findItem(R.id.dMenuUser)){
                startActivity(new Intent(this, MainActivity.class));
                this.finish();
            }
            else if(item==menu.findItem(R.id.dMenuLogout)){
                new SignOut(this,this,getSupportFragmentManager());
            }
            return false;
        });
    }

    @Override
    public void onSignOut(boolean result) {
        if(result){
            Toast.makeText(DoctorActivity.this,"Sign Out Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DoctorActivity.this,LoginActivity.class));
            DoctorActivity.this.finish();
        }
        else {
            Snackbar.make(getWindow().getDecorView().getRootView(),"Sign Out Failed, Please try Again",2000).show();
        }
    }
}