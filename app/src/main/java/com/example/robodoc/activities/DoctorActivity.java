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
import com.example.robodoc.fragments.doctor.AssignedUserAppBarDisplayFragment;
import com.example.robodoc.fragments.utils.AlertDialogFragment;
import com.example.robodoc.viewModels.doctor.UserListViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorActivity extends AppCompatActivity implements AlertDialogFragment.AlertDialogInterface {

    private AssignedUserAppBarDisplayFragment displayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        Intent intent=getIntent();

        UserListViewModel viewModel=new ViewModelProvider(this).get(UserListViewModel.class);
        viewModel.setDoctorDetails(intent.getStringExtra("UID"),intent.getStringExtra("UserName"));

        MaterialToolbar toolbar=findViewById(R.id.doctorToolbar);

        NavController navController = Navigation.findNavController(this, R.id.navHostDoctor);
        AppBarConfiguration appBarConfiguration=new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(toolbar, navController,appBarConfiguration);

        if(intent.getBooleanExtra("IsAdmin",false))
            toolbar.getMenu().findItem(R.id.dMenuAdmin).setVisible(true);

        displayFragment=(AssignedUserAppBarDisplayFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentAppBarDisplay);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(destination.getId()==R.id.assignedUserListFragment || destination.getId()==R.id.assignedUserListInfoFragment){
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(displayFragment)
                        .commit();
            }
            else {
                getSupportFragmentManager()
                        .beginTransaction()
                        .show(displayFragment)
                        .commit();
            }
        });

        toolbar.setOnMenuItemClickListener(item -> {
            Menu menu=toolbar.getMenu();
            if(item==menu.findItem(R.id.dMenuAdmin)){
                Intent intent1=new Intent(this,AdminActivity.class);
                intent1.putExtra("IsAdmin",true);
                intent1.putExtra("IsDoctor",true);
                intent1.putExtra("UID",intent.getStringExtra("UID"));
                intent1.putExtra("UserName",intent.getStringExtra("UserName"));
                startActivity(intent1);
                this.finish();
            }
            else if(item==menu.findItem(R.id.dMenuUser)){
                startActivity(new Intent(DoctorActivity.this,MainActivity.class));
                this.finish();
            }
            else if(item==menu.findItem(R.id.dMenuLogout)){
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
            Intent intent1=new Intent(DoctorActivity.this,StartupActivity.class);
            intent1.putExtra("AfterLogout",true);
            startActivity(intent1);
            DoctorActivity.this.finish();
        }
    }
}