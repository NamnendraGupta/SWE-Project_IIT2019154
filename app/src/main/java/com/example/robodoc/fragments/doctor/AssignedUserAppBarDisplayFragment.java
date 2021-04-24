package com.example.robodoc.fragments.doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.viewModels.doctor.UserListViewModel;
import com.squareup.picasso.Picasso;

public class AssignedUserAppBarDisplayFragment extends Fragment {

    public AssignedUserAppBarDisplayFragment() {
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
        return inflater.inflate(R.layout.fragment_assigned_user_app_bar_display, container, false);
    }

    TextView tvName, tvEmail;
    ImageView imgUser;

    private UserInfo userInfo;
    private boolean IsDetailsOpened;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName=view.findViewById(R.id.tvAssignedUserAppBarName);
        tvEmail=view.findViewById(R.id.tvAssignedUserAppBarEmail);
        imgUser=view.findViewById(R.id.imgAssignedUserAppBar);

        IsDetailsOpened=false;
        UpdateInterface();
    }

    public void SetUserInfo(UserInfo userInfo){
        this.userInfo=userInfo;
        IsDetailsOpened=true;
        UpdateInterface();
    }

    private void UpdateInterface(){
        if(IsDetailsOpened){
            tvName.setText(userInfo.getName());
            tvEmail.setText(userInfo.getEmail());
            Picasso.get().load(userInfo.getPhotoUrl()).into(imgUser);
        }
    }
}