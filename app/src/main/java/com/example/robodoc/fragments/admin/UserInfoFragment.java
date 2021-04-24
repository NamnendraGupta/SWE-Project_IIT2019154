package com.example.robodoc.fragments.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.fragments.utils.ProgressIndicatorFragment;
import com.example.robodoc.utils.DateTimeUtils;
import com.example.robodoc.viewModels.admin.UserListViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.squareup.picasso.Picasso;

public class UserInfoFragment extends Fragment {

    private UserInfo userInfo;
    private int position;

    private boolean isAdmin, isDoctor;
    private TextView tvName,tvEmail,tvUID,tvDateRegistered,tvGender;
    private ImageView imgUser;
    private SwitchMaterial switchAdmin,switchDoctor;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserListViewModel viewModel=new ViewModelProvider(requireActivity()).get(UserListViewModel.class);
        position=UserInfoFragmentArgs.fromBundle(getArguments()).getListPosition();

        viewModel.getUsersList().observe(getViewLifecycleOwner(),userInfoArrayList -> {
            if(userInfoArrayList.size()>0){
                userInfo=userInfoArrayList.get(position);
                UpdateInterface();
            }
        });

        tvName = view.findViewById(R.id.tvUserInfoName);
        tvEmail = view.findViewById(R.id.tvUserInfoEmail);
        tvUID = view.findViewById(R.id.tvUserInfoUID);
        tvDateRegistered = view.findViewById(R.id.tvUserInfoDateRegistered);
        tvGender = view.findViewById(R.id.tvUserInfoGender);
        imgUser = view.findViewById(R.id.imgUserInfo);
        switchAdmin = view.findViewById(R.id.switchUserInfoAdmin);
        switchDoctor = view.findViewById(R.id.switchUserInfoDoctor);
        Button btnUpdate = view.findViewById(R.id.btnUserInfoSubmit);

        switchAdmin.setOnCheckedChangeListener((buttonView, isChecked) -> isAdmin=isChecked);

        switchDoctor.setOnCheckedChangeListener((buttonView, isChecked) -> isDoctor=isChecked);

        btnUpdate.setOnClickListener(v -> {
            if(isStateChanged())
                viewModel.UpdateUserRoles(position,isDoctor,isAdmin);
            else
                Snackbar.make(requireActivity().getWindow().getDecorView().getRootView(),"Please Change the User Role",3000).show();
        });

        ProgressIndicatorFragment progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing With Server","Updating User Role");

        viewModel.CheckIsUserRoleUpdating().observe(getViewLifecycleOwner(),aBoolean -> {
            if(aBoolean){
                progressIndicatorFragment.show(getParentFragmentManager(),"UpdateUserRole");
            }
            else {
                if(progressIndicatorFragment.isVisible()){
                    progressIndicatorFragment.dismiss();
                    boolean result=viewModel.GetUpdateResult();
                    String displayResult;
                    if(result)
                        displayResult="User Role Updated Successfully";
                    else
                        displayResult="Error in Updating Role";
                    Snackbar.make(requireActivity().getWindow().getDecorView().getRootView(),displayResult,3000).show();
                }
            }
        });
    }

    private boolean isStateChanged(){
        return (isAdmin!=userInfo.isAdmin() || isDoctor!=userInfo.isDoctor());
    }

    private void UpdateInterface(){
        isAdmin=userInfo.isAdmin();
        isDoctor=userInfo.isDoctor();

        tvName.setText(userInfo.getName());
        tvEmail.setText(userInfo.getEmail());
        tvUID.setText(userInfo.getUID());
        tvDateRegistered.setText(DateTimeUtils.getDisplayDate(userInfo.getDateRegistered().getTime()));
        tvGender.setText(userInfo.getGender().toString());
        Picasso.get().load(userInfo.getPhotoUrl()).into(imgUser);
        switchAdmin.setUseMaterialThemeColors(true);
        switchAdmin.setChecked(isAdmin);
        switchDoctor.setUseMaterialThemeColors(true);
        switchDoctor.setChecked(isDoctor);
    }
}