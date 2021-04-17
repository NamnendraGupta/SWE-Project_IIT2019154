package com.example.robodoc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.classes.User;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.auth.SignOut;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements SignOut.SignOutInterface {

    private MaterialToolbar toolbar;
    private Fragment fragmentUserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);
        fragmentUserDetails=getSupportFragmentManager().findFragmentById(R.id.fragmentUserDetails);

        NavController navController= Navigation.findNavController(this,R.id.navHostMain);
        AppBarConfiguration appBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId()!=R.id.frgUserStats){
                    getSupportFragmentManager()
                        .beginTransaction()
                        .show(fragmentUserDetails)
                        .commit();
            }
            else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(fragmentUserDetails)
                        .commit();
            }
        });

        User currentUser = Globals.getCurrentUser();
        if(currentUser.isAdmin())
            toolbar.getMenu().findItem(R.id.menuAdmin).setVisible(true);
        if(currentUser.isDoctor())
            toolbar.getMenu().findItem(R.id.menuDoctor).setVisible(true);

        toolbar.setOnMenuItemClickListener(item -> {
            Menu menu=toolbar.getMenu();
            if(item==menu.findItem(R.id.menuLogout)){
                new SignOut(this,this,getSupportFragmentManager());
            }
            else if(item==menu.findItem(R.id.menuAdmin)){
                startActivity(new Intent(this,AdminActivity.class));
                this.finish();
            }
            else if(item==menu.findItem(R.id.menuDoctor)){
                startActivity(new Intent(this, DoctorActivity.class));
                this.finish();
            }
            return false;
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