package com.example.robodoc.adapters;

import androidx.navigation.NavController;

import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.fragments.user.DoctorListFragmentDirections;

import java.util.ArrayList;

public class DoctorListAdapter extends UserListAdapter {

    public DoctorListAdapter(ArrayList<UserInfo> userList, NavController navController) {
        super(userList, navController);
    }

    @Override
    protected void onButtonClicked(int position) {
        navController.navigate(DoctorListFragmentDirections.ActionDoctorInfo(position));
    }
}
