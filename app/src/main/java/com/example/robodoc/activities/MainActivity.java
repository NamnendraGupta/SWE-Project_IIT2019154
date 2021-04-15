package com.example.robodoc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.auth.SignOut;
import com.example.robodoc.firebase.realtimeDb.GetVitalRecord;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
import com.example.robodoc.fragments.user.ChooseInputMethod;
import com.example.robodoc.fragments.user.DoctorListFragment;
import com.example.robodoc.fragments.user.RecordsFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SignOut.SignOutInterface, GetVitalRecord.GetVitalRecordInterface {

    MaterialToolbar toolbar;
    User currentUser;
    TextView tvName,tvNoRecordsDisplay;
    ImageView imgUser;
    Button btnTakeInput, btnShowRecords, btnViewStats, btnViewDoctorList;
    ChooseInputMethod chooseInputMethod;
    RecordsFragment recordsFragment;
    DoctorListFragment doctorListFragment;

    RecyclerView rcvRecords;

    public static ArrayList<VitalInput> vitalInputsList;
    ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseInputMethod=ChooseInputMethod.newInstance();
        recordsFragment=RecordsFragment.newInstance();
        doctorListFragment=DoctorListFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainerDashboard,chooseInputMethod,"ChooseInputFragment")
                .add(R.id.fragmentContainerDashboard,recordsFragment,"RecordsFragment")
                .add(R.id.fragmentContainerDashboard,doctorListFragment,"DoctorsFragment")
                .hide(chooseInputMethod)
                .hide(recordsFragment)
                .hide(doctorListFragment)
                .commit();

        toolbar=findViewById(R.id.toolbar);
        tvName=findViewById(R.id.tvUserName);
        imgUser=findViewById(R.id.imgUser);
        btnTakeInput=findViewById(R.id.btnTakeInput);
        btnShowRecords=findViewById(R.id.btnShowRecords);
        btnViewStats=findViewById(R.id.btnViewStats);
        btnViewDoctorList=findViewById(R.id.btnViewDoctorList);

        rcvRecords=findViewById(R.id.rcvRecords);
        tvNoRecordsDisplay=findViewById(R.id.tvNoRecordDisplay);

        vitalInputsList=new ArrayList<>();

        updateInterface();

        new GetVitalRecord(MainActivity.this,Globals.getCurrentUserUid());
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Loading Records");
        progressIndicatorFragment.show(getSupportFragmentManager(),"Syncing Data");

        toolbar.setOnMenuItemClickListener(item -> {
            Menu menu=toolbar.getMenu();
            if(item==menu.findItem(R.id.menuLogout))
                signOut();
            else if(item==menu.findItem(R.id.menuAdmin))
                startAdminActivity();
            else if(item==menu.findItem(R.id.menuDoctor))
                startDoctorActivity();
            else if(item==menu.findItem(R.id.menuSetAlarm))
                startAlarmActivity();
            return false;
        });

        updateButtonsAccordingToList();

        btnTakeInput.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            if(chooseInputMethod.isHidden()){
                fragmentTransaction.show(chooseInputMethod);
                btnTakeInput.setText("Hide");
                btnShowRecords.setVisibility(View.GONE);
                btnViewStats.setVisibility(View.GONE);
                btnViewDoctorList.setVisibility(View.GONE);
            }
            else {
                fragmentTransaction.hide(chooseInputMethod);
                btnTakeInput.setText("Generate Input");
                btnViewDoctorList.setVisibility(View.VISIBLE);
                updateButtonsAccordingToList();
            }
            fragmentTransaction.commit();
        });

        btnShowRecords.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            if(recordsFragment.isHidden()){
                fragmentTransaction.show(recordsFragment);
                recordsFragment.ShowList(vitalInputsList);
                Log.d("SIZE",vitalInputsList.size()+"");
                btnShowRecords.setText("Hide");
                btnTakeInput.setVisibility(View.GONE);
                btnViewStats.setVisibility(View.GONE);
                btnViewDoctorList.setVisibility(View.GONE);
            }
            else {
                fragmentTransaction.hide(recordsFragment);
                recordsFragment.HideList();
                btnShowRecords.setText("Show Records");
                btnTakeInput.setVisibility(View.VISIBLE);
                btnViewDoctorList.setVisibility(View.VISIBLE);
                if(vitalInputsList.size()>1)
                    btnViewStats.setVisibility(View.VISIBLE);
            }
            fragmentTransaction.commit();
        });

        btnViewStats.setOnClickListener(v -> {
            Intent intent=new Intent(this,UserStatsActivity.class);
            intent.putExtra("SOURCE","USER");
            startActivity(intent);
        });

        btnViewDoctorList.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            if(doctorListFragment.isHidden()){
                fragmentTransaction.show(doctorListFragment);
                btnViewDoctorList.setText("Hide");
                btnShowRecords.setVisibility(View.GONE);
                btnViewStats.setVisibility(View.GONE);
                btnTakeInput.setVisibility(View.GONE);
            }
            else {
                fragmentTransaction.hide(doctorListFragment);
                btnViewDoctorList.setText("View List of Doctors");
                btnTakeInput.setVisibility(View.VISIBLE);
                updateButtonsAccordingToList();
            }
            fragmentTransaction.commit();
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

    private void startAlarmActivity(){
        startActivity(new Intent(this,AlarmActivity.class));
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

    private void updateButtonsAccordingToList(){
        if(vitalInputsList.size()==0){
            btnViewStats.setVisibility(View.GONE);
            btnShowRecords.setVisibility(View.GONE);
        }
        else if(vitalInputsList.size()==1) {
            btnShowRecords.setVisibility(View.VISIBLE);
            btnViewStats.setVisibility(View.GONE);
        }
        else {
            btnShowRecords.setVisibility(View.VISIBLE);
            btnViewStats.setVisibility(View.VISIBLE);
        }
    }

    boolean isStart=false;

    @Override
    public void onNewRecordObtained(boolean hasRecords, VitalInput newRecord) {
        if(hasRecords){
            for(int i=0;i<vitalInputsList.size();i++){
                if(vitalInputsList.get(i).getInputID().equals(newRecord.getInputID()))
                    return;
            }
            vitalInputsList.add(newRecord);
            if(!isStart){
                isStart=true;
                progressIndicatorFragment.dismiss();
                updateButtonsAccordingToList();
            }
            if(chooseInputMethod.isHidden()){
                if(vitalInputsList.size()==1 || vitalInputsList.size()==2)
                    updateButtonsAccordingToList();
            }
        }
        else {
            isStart=true;
            progressIndicatorFragment.dismiss();
        }
    }
}