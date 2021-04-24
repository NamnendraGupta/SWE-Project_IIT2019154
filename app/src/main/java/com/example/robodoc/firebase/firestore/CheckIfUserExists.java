package com.example.robodoc.firebase.firestore;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CheckIfUserExists {

    public interface UserExistsInterface{
        void onResultCallback(boolean task, boolean result);
    }

    public CheckIfUserExists(String UId, UserExistsInterface userExistsInterface){

        FirebaseFirestore
                .getInstance()
                .collection("USERS")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot snapshot:task.getResult()){
                            if(snapshot.getId().equals(UId)){
                                userExistsInterface.onResultCallback(true,true);
                                return;
                            }
                        }
                        userExistsInterface.onResultCallback(true,false);
                    }
                    else {
                        userExistsInterface.onResultCallback(false,false);
                    }
                });
    }

}
