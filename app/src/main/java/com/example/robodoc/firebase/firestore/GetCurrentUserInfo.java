package com.example.robodoc.firebase.firestore;

import androidx.annotation.NonNull;

import com.example.robodoc.enums.UserKey;
import com.example.robodoc.firebase.realtimeDb.DatabaseKeys;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class GetCurrentUserInfo {

    public interface GetUserInfoInterface{
        void onResultCallback(boolean result, DocumentSnapshot snapshot, boolean isDoctorAssigned, String doctorUId, String DoctorName);
    }

    public GetCurrentUserInfo(String UId, GetUserInfoInterface infoInterface){
        FirebaseFirestore
                .getInstance()
                .collection("USERS")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(DocumentSnapshot snapshot:task.getResult()){
                            if(snapshot.getId().equals(UId)){
                                getAssignedDoctorInfo(UId, infoInterface, snapshot);
                            }
                        }
                    }
                    else {
                        infoInterface.onResultCallback(false,null,false,null,null);
                    }
                });
    }

    private void getAssignedDoctorInfo(String UId, GetUserInfoInterface infoInterface, DocumentSnapshot snapshot){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(DatabaseKeys.KEY_USERS)
                .child(UId)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot dataSnapshot=task.getResult();
                        if(dataSnapshot.child(DatabaseKeys.KEY_USER_DOCTOR_ASSIGNED).exists()){
                            String doctorUID=dataSnapshot.child(DatabaseKeys.KEY_USER_DOCTOR_ASSIGNED).getValue().toString();
                            FirebaseFirestore
                                    .getInstance()
                                    .collection("USERS")
                                    .document(doctorUID)
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if(task1.isSuccessful()){
                                            String name=task1.getResult().getString(UserKey.NAME.toString());
                                            infoInterface.onResultCallback(true,snapshot,true,doctorUID,name);
                                        }
                                        else {
                                            infoInterface.onResultCallback(false,null,false,null,null);
                                        }
                                    });
                        }
                        else {
                            infoInterface.onResultCallback(true,snapshot,false,null,null);
                        }
                    }
                    else {
                        infoInterface.onResultCallback(false,null,false,null,null);
                    }
                });
    }

}
