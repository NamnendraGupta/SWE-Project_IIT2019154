package com.example.robodoc.adapters;

import androidx.fragment.app.FragmentManager;

import com.example.robodoc.R;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.fragments.doctor.AssignedUserListInfoFragment;

import java.util.ArrayList;

public class AssignedUserListAdapter extends UserListAdapter {

    public AssignedUserListAdapter(ArrayList<UserInfo> userList, FragmentManager manager) {
        super(userList, manager);
    }

    @Override
    protected void onButtonClicked(UserInfo userInfo, int position) {
        AssignedUserListInfoFragment fragment=AssignedUserListInfoFragment.newInstance(userInfo);
        manager.beginTransaction()
                .add(R.id.frameLayoutDoctor,fragment,"Assigned User Fragment "+userInfo.getUID())
                .show(fragment)
                .hide(manager.findFragmentByTag("AssignedUserList"))
                .commit();
    }
}
