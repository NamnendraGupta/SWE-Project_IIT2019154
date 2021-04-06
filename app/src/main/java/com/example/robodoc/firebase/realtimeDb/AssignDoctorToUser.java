package com.example.robodoc.firebase.realtimeDb;

import com.example.robodoc.fragments.ProgressIndicatorFragment;

public class AssignDoctorToUser {

    public interface AssignDoctorToUserInterface{
        void onDoctorAssigned(boolean result);
    }

    private final String TAG="ASSIGN DOCTOR TO USER";
    private final ProgressIndicatorFragment progressIndicatorFragment;

    public AssignDoctorToUser(String userUid, String doctorUid){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing Data with Server","Assigning Doctor to the User");

    }

}
