package com.example.robodoc.firebase.firestore;

import android.util.Log;

import com.example.robodoc.firebase.realtimeDb.DatabaseKeys;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterNewUser {

    public interface NewUserInterface{
        void OnUserRegister(boolean result);
    }

    private final String TAG="User Registration";

    public RegisterNewUser(NewUserInterface userInterface, String UID, HashMap<String,Object> userData){

        FirebaseFirestore
                .getInstance()
                .collection("USERS")
                .document(UID)
                .set(userData)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        FirebaseDatabase
                                .getInstance()
                                .getReference()
                                .child(DatabaseKeys.KEY_USERS)
                                .child(UID)
                                .child(DatabaseKeys.KEY_USER_VITAL_SIGNS_NUM)
                                .setValue(false)
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        Log.d(TAG,"User Registration Successful");
                                        userInterface.OnUserRegister(true);
                                    }
                                    else{
                                        Log.d(TAG,"User Registration Failed");
                                        userInterface.OnUserRegister(false);
                                    }
                                });
                    }
                    else {
                        Log.d(TAG,"User Registration Failed");
                        userInterface.OnUserRegister(false);
                    }
                });

    }
}
