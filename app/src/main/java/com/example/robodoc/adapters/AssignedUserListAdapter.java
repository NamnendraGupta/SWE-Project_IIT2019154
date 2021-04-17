package com.example.robodoc.adapters;

import androidx.navigation.NavController;

import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.fragments.doctor.AssignedUserListFragmentDirections;

import java.util.ArrayList;

public class AssignedUserListAdapter extends UserListAdapter {

    public AssignedUserListAdapter(ArrayList<UserInfo> userList, NavController navController) {
        super(userList, navController);
    }

    @Override
    protected void onButtonClicked(int position) {
        navController.navigate(AssignedUserListFragmentDirections.actionAssignedUserListFragmentToAssignedUserListInfoFragment(position));
    }
}
