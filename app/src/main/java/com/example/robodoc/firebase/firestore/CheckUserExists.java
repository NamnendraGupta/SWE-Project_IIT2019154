package com.example.robodoc.firebase.firestore;

import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.realtimeDb.DatabaseKeys;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

public class CheckUserExists {

    public interface checkUserExistsInterface{
        void onTaskResult(boolean result, boolean task);
    }

    private final String TAG="Checking User Existence";
    private final ProgressIndicatorFragment progressIndicatorFragment;

    public CheckUserExists(String uid, FragmentManager fragmentManager, checkUserExistsInterface userExistsInterface){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Checking whether User already exists or not");
        progressIndicatorFragment.show(fragmentManager,"Checking in Database");

        Globals
                .getFirestore()
                .collection("USERS")
                .get()
                .addOnCompleteListener(task -> {
                    progressIndicatorFragment.dismiss();
                    if(task.isSuccessful()){
                        Log.d(TAG,"Database Access Successful");
                        for(DocumentSnapshot snapshot:task.getResult()){
                            if(snapshot.getId().equals(uid)){
                                Log.d(TAG,"User Already Exists");
                                Globals.updateUserWithSnapshot(snapshot);
                                updateAssignedDoctorInfo(userExistsInterface);
                                return;
                            }
                        }
                        Log.d(TAG,"User does not exists");
                        userExistsInterface.onTaskResult(false,true);
                    }
                    else {
                        Log.d(TAG,"Error in Accessing Database");
                        userExistsInterface.onTaskResult(false,false);
                    }
                });
    }

    private void updateAssignedDoctorInfo(checkUserExistsInterface userExistsInterface){
        Globals
                .getFirebaseDatabase()
                .getReference()
                .child(DatabaseKeys.KEY_USERS)
                .child(Globals.getCurrentUserUid())
                .get()
                .addOnCompleteListener(task -> {
                    progressIndicatorFragment.dismiss();
                    if(task.isSuccessful()){
                        Log.d(TAG,"User Data Fetched Successfully");
                        DataSnapshot snapshot=task.getResult();
                        if(snapshot.child(DatabaseKeys.KEY_USER_DOCTOR_ASSIGNED).exists()){
                            Globals.getCurrentUser().setDoctorAssigned(true);
                            Globals.getCurrentUser().setAssignedDoctorUID(snapshot.child(DatabaseKeys.KEY_USER_DOCTOR_ASSIGNED).getValue().toString());
                        }
                        else {
                            Globals.getCurrentUser().setDoctorAssigned(false);
                        }
                        userExistsInterface.onTaskResult(true,true);
                    }
                    else {
                        Log.d(TAG,"Error in Fetching User Data");
                        userExistsInterface.onTaskResult(false,false);
                    }
                });
    }

}
