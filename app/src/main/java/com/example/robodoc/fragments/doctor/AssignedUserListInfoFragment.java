package com.example.robodoc.fragments.doctor;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.activities.ChatActivity;
import com.example.robodoc.activities.UserStatsActivity;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.firebase.realtimeDb.GetVitalRecord;
import com.example.robodoc.fragments.user.RecordsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AssignedUserListInfoFragment extends Fragment implements GetVitalRecord.GetVitalRecordInterface {

    public AssignedUserListInfoFragment() {
        // Required empty public constructor
    }

    public static AssignedUserListInfoFragment newInstance(UserInfo userInfo) {
        AssignedUserListInfoFragment fragment = new AssignedUserListInfoFragment();
        fragment.userInfo=userInfo;
        return fragment;
    }

    private UserInfo userInfo;
    public static ArrayList<VitalInput> vitalInputList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordsFragment=RecordsFragment.newInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assigned_user_list_info, container, false);
    }

    TextView tvName, tvEmail, tvGender;
    Button btnClose, btnViewStats, btnViewInteraction;
    ImageView imgUser;

    RecordsFragment recordsFragment;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vitalInputList=new ArrayList<>();

        tvName=view.findViewById(R.id.tvAssignedUserName);
        tvEmail=view.findViewById(R.id.tvAssignedUserEmail);
        tvGender=view.findViewById(R.id.tvAssignedUserGender);
        btnClose=view.findViewById(R.id.btnCloseAssignedUser);
        btnViewStats=view.findViewById(R.id.btnViewAssignStats);
        btnViewInteraction=view.findViewById(R.id.btnViewUserInteraction);
        imgUser=view.findViewById(R.id.imgAssignedUser);

        tvName.setText(userInfo.getName());
        tvEmail.setText(userInfo.getEmail());
        tvGender.setText(userInfo.getGender().toString());
        Picasso.get().load(userInfo.getPhotoUrl()).into(imgUser);

        btnClose.setOnClickListener(v -> {
            getFragmentManager()
                    .beginTransaction()
                    .hide(getFragmentManager().findFragmentByTag("Assigned User Fragment "+userInfo.getUID()))
                    .remove(getFragmentManager().findFragmentByTag("Assigned User Fragment "+userInfo.getUID()))
                    .show(getFragmentManager().findFragmentByTag("AssignedUserList"))
                    .commit();

        });

        btnViewStats.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), UserStatsActivity.class);
            intent.putExtra("SOURCE","DOCTOR");
            intent.putExtra("NAME",userInfo.getName());
            startActivity(intent);
        });

        btnViewInteraction.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), ChatActivity.class);
            intent.putExtra("IsDoctor",true);
            intent.putExtra("DestinationUID",userInfo.getUID());
            intent.putExtra("DestinationUserName",userInfo.getName());
            startActivity(intent);
        });

        new GetVitalRecord(this,userInfo.getUID());

        getChildFragmentManager().beginTransaction()
                .add(R.id.frameLayoutAssignedUser,recordsFragment,"Records Fragment")
                .show(recordsFragment)
                .commit();
    }

    @Override
    public void onNewRecordObtained(boolean hasRecords, VitalInput newRecord) {
        if(hasRecords){
            for(int i=0;i<vitalInputList.size();i++){
                if(vitalInputList.get(i).getInputID().equals(newRecord.getInputID()))
                    return;
            }
            vitalInputList.add(newRecord);
            recordsFragment.ShowList(vitalInputList);
            if(vitalInputList.size()<2){
                btnViewStats.setVisibility(View.GONE);
            }
            else {
                btnViewStats.setVisibility(View.VISIBLE);
            }
        }
        else
            recordsFragment.HideList();
    }
}