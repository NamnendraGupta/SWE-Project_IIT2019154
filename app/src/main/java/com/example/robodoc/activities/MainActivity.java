package com.example.robodoc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.classes.User;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.auth.SignOut;
import com.example.robodoc.fragments.user.ChooseInputMethod;
import com.example.robodoc.utils.GetRandomBloodPressure;
import com.example.robodoc.utils.GetRandomBodyTemperature;
import com.example.robodoc.utils.GetRandomGlucoseLevel;
import com.example.robodoc.utils.GetRandomHeartRate;
import com.example.robodoc.utils.GetRandomOxygenLevel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements SignOut.SignOutInterface {

    MaterialToolbar toolbar;
    User currentUser;
    TextView tvName;
    ImageView imgUser;
    Button btnTakeInput;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentById(R.id.fragmentUserDashboard)).commit();

        toolbar=findViewById(R.id.toolbar);
        tvName=findViewById(R.id.tvUserName);
        imgUser=findViewById(R.id.imgUser);
        btnTakeInput=findViewById(R.id.btnTakeInput);

        updateInterface();

        toolbar.setOnMenuItemClickListener(item -> {
            Menu menu=toolbar.getMenu();
            if(item==menu.findItem(R.id.menuLogout))
                signOut();
            else if(item==menu.findItem(R.id.menuAdmin))
                startAdminActivity();
            else if(item==menu.findItem(R.id.menuDoctor))
                startDoctorActivity();
            return false;
        });

        btnTakeInput.setOnClickListener(v -> {
            FragmentManager manager=getSupportFragmentManager();
            ChooseInputMethod chooseInputMethod=ChooseInputMethod.newInstance();
            manager
                    .beginTransaction()
                    .replace(R.id.fragmentUserDashboard,chooseInputMethod,"CHOOSE_INPUT_METHOD")
                    .show(manager.findFragmentById(R.id.fragmentUserDashboard))
                    .commit();
        });
    }

    private void updateInterface(){
        currentUser=Globals.getCurrentUser();
        if(currentUser.isAdmin())
            toolbar.getMenu().findItem(R.id.menuAdmin).setVisible(true);
        if(currentUser.isDoctor())
            toolbar.getMenu().findItem(R.id.menuDoctor).setVisible(true);

        String name="Hello "+currentUser.getName();
        tvName.setText(name);
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