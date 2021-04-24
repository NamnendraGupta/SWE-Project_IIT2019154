package com.example.robodoc.firebase.realtimeDb;

import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.example.robodoc.fragments.utils.ProgressIndicatorFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class AssignDoctorToUser {

    public interface AssignDoctorToUserInterface{
        void onDoctorAssigned(boolean result);
    }

    private final String TAG="ASSIGN DOCTOR TO USER";
    private final ProgressIndicatorFragment progressIndicatorFragment;

    public AssignDoctorToUser(String userUid, String doctorUid, FragmentManager manager, AssignDoctorToUserInterface assignInterface){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing Data with Server","Assigning Doctor to the User");
        progressIndicatorFragment.show(manager,TAG);

        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference();

        dbRef
                .child(DatabaseKeys.KEY_USERS)
                .child(userUid)
                .child(DatabaseKeys.KEY_USER_DOCTOR_ASSIGNED)
                .setValue(doctorUid)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        dbRef
                                .child(DatabaseKeys.KEY_DOCTORS)
                                .child(doctorUid)
                                .child(DatabaseKeys.KEY_DOCTORS_ASSIGNED_LIST)
                                .child(userUid)
                                .setValue(new Date().getTime())
                                .addOnCompleteListener(task1 -> {
                                    progressIndicatorFragment.dismiss();
                                    if(task1.isSuccessful()){
                                        Log.d(TAG,"User Assigned Successfully");
                                        assignInterface.onDoctorAssigned(true);
                                    }
                                    else {
                                        Log.d(TAG,"Error in Assigning User");
                                        assignInterface.onDoctorAssigned(false);
                                    }
                                });
                    }
                    else {
                        Log.d(TAG,"Error in Assigning User");
                        progressIndicatorFragment.dismiss();
                        assignInterface.onDoctorAssigned(false);
                    }
                });
    }

}
