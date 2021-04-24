package com.example.robodoc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.example.robodoc.R;
import com.example.robodoc.fragments.utils.AlertDialogFragment;
import com.example.robodoc.fragments.utils.ProgressIndicatorFragment;
import com.example.robodoc.viewModels.user.RecordListViewModel;
import com.example.robodoc.viewModels.user.UserInfoViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements AlertDialogFragment.AlertDialogInterface {

    private MaterialToolbar toolbar;
    private boolean IsInfoLoading, IsListLoading, IsDialogDisplayed;
    private ProgressIndicatorFragment progressIndicatorFragment;
    private UserInfoViewModel InfoViewModel;
    RecordListViewModel RecordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);

        InfoViewModel=new ViewModelProvider(this).get(UserInfoViewModel.class);
        RecordViewModel=new ViewModelProvider(this).get(RecordListViewModel.class);

        RecordViewModel.setUserUId(InfoViewModel.getUId());

        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing With Server","Loading User Data");

        IsDialogDisplayed=false;

        InfoViewModel.CheckIsUserInfoLoading().observe(this, aBoolean -> {
            IsInfoLoading=aBoolean;
            UpdateProgress();
        });

        RecordViewModel.CheckIsListLoading().observe(this,aBoolean -> {
            IsListLoading=aBoolean;
            UpdateProgress();
        });

        NavController navController= Navigation.findNavController(this,R.id.navHostMain);
        AppBarConfiguration appBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar,navController,appBarConfiguration);

        InfoViewModel.GetCurrentUser().observe(this, user -> {
            if(user!=null){
                if(user.isAdmin())
                    toolbar.getMenu().findItem(R.id.menuAdmin).setVisible(true);
                if(user.isDoctor())
                    toolbar.getMenu().findItem(R.id.menuDoctor).setVisible(true);
            }
        });

        toolbar.setOnMenuItemClickListener(item -> {
            Menu menu=toolbar.getMenu();
            if(item==menu.findItem(R.id.menuLogout)){
                AlertDialogFragment dialogFragment=AlertDialogFragment.newInstance("RoboDoc Warning","Are you sure you want to logout?");
                dialogFragment.SetInterface(this,"Logout");
                dialogFragment.show(getSupportFragmentManager(),"LogoutDialog");
            }
            else if(item==menu.findItem(R.id.menuAdmin)){
                Intent intent=new Intent(this,AdminActivity.class);
                startRobodocActivity(intent);
            }
            else if(item==menu.findItem(R.id.menuDoctor)){
                Intent intent=new Intent(this,DoctorActivity.class);
                startRobodocActivity(intent);
            }
            return false;
        });
    }

    private void startRobodocActivity(Intent intent){
        intent.putExtra("IsAdmin",InfoViewModel.IsUserAdmin());
        intent.putExtra("IsDoctor",InfoViewModel.IsUserDoctor());
        intent.putExtra("UID",InfoViewModel.getUId());
        intent.putExtra("UserName",InfoViewModel.getDisplayName());
        startActivity(intent);
        MainActivity.this.finish();
    }

    private void UpdateProgress(){
        if(IsListLoading || IsInfoLoading){
            if(!IsDialogDisplayed){
                progressIndicatorFragment.show(getSupportFragmentManager(),"LoadingData");
                IsDialogDisplayed=true;
            }
        }
        else {
            if(IsDialogDisplayed){
                progressIndicatorFragment.dismiss();
                IsDialogDisplayed=false;
            }
        }
    }

    @Override
    public void onPositiveButtonClicked(String type) {
        if(type.equals("Logout")){
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(MainActivity.this,StartupActivity.class);
            intent.putExtra("AfterLogout",true);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }
}