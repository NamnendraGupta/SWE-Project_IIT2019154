package com.example.robodoc.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.fragments.user.DoctorInfoFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DoctorListAdapter extends UserListAdapter {

    public DoctorListAdapter(ArrayList<UserInfo> userList, FragmentManager manager) {
        super(userList, manager);
    }

    @Override
    protected void onButtonClicked(UserInfo userInfo, int position) {
        DoctorInfoFragment doctorInfoFragment=new DoctorInfoFragment(userInfo);
        doctorInfoFragment.show(manager,"Doctor Info");
    }
}
