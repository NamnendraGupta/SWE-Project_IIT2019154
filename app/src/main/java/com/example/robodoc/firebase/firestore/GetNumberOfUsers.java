package com.example.robodoc.firebase.firestore;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GetNumberOfUsers {

    public interface CallbackInterface{
        void OnResultCallback(boolean result, int num);
    }

    public GetNumberOfUsers(CallbackInterface callbackInterface){

        String TAG="Fetching User Number";

        FirebaseFirestore
                .getInstance()
                .collection("USERS")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG,"Number Fetched Successfully!");
                        int num=0;
                        for(DocumentSnapshot ignored :task.getResult())
                            num++;
                        callbackInterface.OnResultCallback(true,num);
                    }
                    else {
                        Log.e(TAG,"Error in Fetching Number "+task.getException().getLocalizedMessage());
                        callbackInterface.OnResultCallback(false,0);
                    }
                });

    }

}
