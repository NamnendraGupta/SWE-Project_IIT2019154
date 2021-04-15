package com.example.robodoc.firebase.firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.realtimeDb.DatabaseKeys;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class FetchCurrentUserInfo {

    public interface FetchInfoInterface{
        void onUserDataFetched(boolean result);
    }

    private final String TAG="Fetching User Info";
    private final ProgressIndicatorFragment progressIndicatorFragment;

    public FetchCurrentUserInfo(FragmentManager manager, FetchInfoInterface infoInterface){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing With Server","Fetching User's Data");
        progressIndicatorFragment.show(manager,"Fetching User Data");

        Globals
                .getFirestore()
                .collection("USERS")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot documentSnapshot:task.getResult()){
                            if(documentSnapshot.getId().equals(Globals.getCurrentUserUid())){
                                Globals.updateUserWithSnapshot(documentSnapshot);
                                updateAssignedDoctorInfo(infoInterface);
                                return;
                            }
                        }
                    }
                    Log.d(TAG,"Error in Fetching User Data");
                    infoInterface.onUserDataFetched(false);
                    progressIndicatorFragment.dismiss();
                });
    }

    private void updateAssignedDoctorInfo(FetchInfoInterface infoInterface){
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
                        infoInterface.onUserDataFetched(true);
                    }
                    else {
                        Log.d(TAG,"Error in Fetching User Data");
                        infoInterface.onUserDataFetched(false);
                    }
                });
    }
}
