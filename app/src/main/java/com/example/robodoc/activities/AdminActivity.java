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
import com.example.robodoc.viewModels.admin.UserListViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity implements AlertDialogFragment.AlertDialogInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        MaterialToolbar toolbar=findViewById(R.id.adminToolbar);

        NavController navController = Navigation.findNavController(this, R.id.navHostAdmin);
        AppBarConfiguration appBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar, navController,appBarConfiguration);

        Intent intent=getIntent();
        if(intent.getBooleanExtra("IsDoctor",false))
            toolbar.getMenu().findItem(R.id.aMenuDoctor).setVisible(true);

        UserListViewModel viewModel=new ViewModelProvider(this).get(UserListViewModel.class);
        viewModel.setUserUId(intent.getStringExtra("UID"));

        toolbar.setOnMenuItemClickListener(item -> {
            Menu menu=toolbar.getMenu();
            if(item==menu.findItem(R.id.aMenuDoctor)){
                Intent intent1=new Intent(this,DoctorActivity.class);
                intent1.putExtra("IsAdmin",true);
                intent1.putExtra("IsDoctor",true);
                intent1.putExtra("UID",intent.getStringExtra("UID"));
                intent1.putExtra("UserName",intent.getStringExtra("UserName"));
                startActivity(intent1);
                this.finish();
            }
            else if(item==menu.findItem(R.id.aMenuUser)){
                startActivity(new Intent(AdminActivity.this,MainActivity.class));
                this.finish();
            }
            else if(item==menu.findItem(R.id.aMenuLogout)){
                AlertDialogFragment dialogFragment=AlertDialogFragment.newInstance("RoboDoc Warning","Are you sure you want to logout?");
                dialogFragment.SetInterface(this,"Logout");
                dialogFragment.show(getSupportFragmentManager(),"LogoutDialog");
            }
            return false;
        });
    }

    @Override
    public void onPositiveButtonClicked(String type) {
        if(type.equals("Logout")){
            FirebaseAuth.getInstance().signOut();
            Intent intent1=new Intent(AdminActivity.this,StartupActivity.class);
            intent1.putExtra("AfterLogout",true);
            startActivity(intent1);
            AdminActivity.this.finish();
        }
    }
}