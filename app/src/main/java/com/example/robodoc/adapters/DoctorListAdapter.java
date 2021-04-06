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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserInfo doctorInfo=userList.get(position);
        holder.tvName.setText(doctorInfo.getName());
        holder.tvEmail.setText(doctorInfo.getEmail());
        Picasso.get().load(doctorInfo.getPhotoUrl()).into(holder.imgUser);
        holder.btnShowDetails.setOnClickListener(v -> {
            DoctorInfoFragment doctorInfoFragment=new DoctorInfoFragment(doctorInfo);
            doctorInfoFragment.show(manager,"Doctor Info");
        });
    }
}
