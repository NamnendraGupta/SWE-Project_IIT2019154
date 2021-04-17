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
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.firebase.firestore.UpdateUserRole;
import com.example.robodoc.utils.DateTimeUtils;
import com.example.robodoc.viewModels.admin.UserListViewModel;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.squareup.picasso.Picasso;

public class UserInfoFragment extends Fragment implements UpdateUserRole.UpdateUserRoleInterface {

    public interface UserInfoFragmentInterface{
        void onUserUpdated(int position, boolean isAdmin, boolean isDoctor);
    }

    private UserInfo userInfo;
    private UserInfoFragmentInterface userInterface;
    private int position;
    private Button btnUpdate;
    private boolean isAdmin, isDoctor;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setUserInterface(int position, UserInfoFragmentInterface userInterface){
        this.position=position;
        this.userInterface=userInterface;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        UserListViewModel viewModel=new ViewModelProvider(requireActivity()).get(UserListViewModel.class);
        userInfo=viewModel.GetUserInfo(UserInfoFragmentArgs.fromBundle(getArguments()).getListPosition());

        TextView tvName = view.findViewById(R.id.tvUserInfoName);
        TextView tvEmail = view.findViewById(R.id.tvUserInfoEmail);
        TextView tvUID = view.findViewById(R.id.tvUserInfoUID);
        TextView tvDateRegistered = view.findViewById(R.id.tvUserInfoDateRegistered);
        TextView tvGender = view.findViewById(R.id.tvUserInfoGender);
        ImageView imgUser = view.findViewById(R.id.imgUserInfo);
        SwitchMaterial switchAdmin = view.findViewById(R.id.switchUserInfoAdmin);
        SwitchMaterial switchDoctor = view.findViewById(R.id.switchUserInfoDoctor);
        btnUpdate=view.findViewById(R.id.btnUserInfoSubmit);

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

        switchAdmin.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isAdmin=isChecked;
            btnUpdate.setEnabled(isStateChanged());
        });

        switchDoctor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDoctor=isChecked;
            btnUpdate.setEnabled(isStateChanged());
        });

        btnUpdate.setOnClickListener(v -> new UpdateUserRole(getParentFragmentManager(),UserInfoFragment.this,userInfo.getUID(),isAdmin,isDoctor));

        super.onViewCreated(view, savedInstanceState);
    }

    private boolean isStateChanged(){
        return (isAdmin!=userInfo.isAdmin() || isDoctor!=userInfo.isDoctor());
    }

    @Override
    public void UpdateResult(boolean result) {
        if(result){
            Toast.makeText(getContext(),"User Roles Updated",Toast.LENGTH_LONG).show();
            userInterface.onUserUpdated(position,isAdmin,isDoctor);
        }
        else {
            Toast.makeText(getContext(),"Error in Updating User Role",Toast.LENGTH_LONG).show();
        }
    }
}