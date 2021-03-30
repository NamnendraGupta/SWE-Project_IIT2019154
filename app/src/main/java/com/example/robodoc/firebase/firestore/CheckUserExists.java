package com.example.robodoc.firebase.firestore;

import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
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
                                userExistsInterface.onTaskResult(true,true);
                                Globals.updateUserWithSnapshot(snapshot);
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

}
