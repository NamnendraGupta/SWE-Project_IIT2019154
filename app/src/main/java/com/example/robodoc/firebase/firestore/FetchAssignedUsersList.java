package com.example.robodoc.firebase.firestore;

import android.net.Uri;
import android.util.Log;

import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.enums.Gender;
import com.example.robodoc.enums.UserKey;
import com.example.robodoc.firebase.realtimeDb.DatabaseKeys;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class FetchAssignedUsersList {

    public interface FetchInterface{
        void onUserListFetchCallback(boolean result, ArrayList<UserInfo> userInfoList);
    }

    private final String TAG="Fetching Users List";

    public FetchAssignedUsersList(FetchInterface fetchInterface, String doctorUId){
        ArrayList<String> uidList=new ArrayList<>();

        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(DatabaseKeys.KEY_DOCTORS)
                .child(doctorUId)
                .child(DatabaseKeys.KEY_DOCTORS_ASSIGNED_LIST)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(DataSnapshot dataSnapshot:task.getResult().getChildren()){
                            uidList.add(dataSnapshot.getKey());
                        }
                        if(uidList.size()==0){
                            Log.d(TAG,"Fetch Successful for "+doctorUId);
                            fetchInterface.onUserListFetchCallback(true, new ArrayList<>());
                        }
                        else {
                            fetchFromFirestore(uidList,fetchInterface);
                        }
                    }
                    else {
                        Log.d(TAG,"Error in Fetching Assigned Users List");
                        fetchInterface.onUserListFetchCallback(false,null);
                    }
                });
    }

    private void fetchFromFirestore(ArrayList<String> uidList, FetchInterface listInterface){
        FirebaseFirestore
                .getInstance()
                .collection("USERS")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG,"Fetch Successful");
                        ArrayList<UserInfo> userInfoList=new ArrayList<>();
                        for(DocumentSnapshot snapshot:task.getResult()){
                            String uid=snapshot.getId();
                            if(uidList.contains(uid)){
                                UserInfo info=new UserInfo(uid);
                                info.setName(snapshot.getString(UserKey.NAME.toString()));
                                info.setEmail(snapshot.getString(UserKey.EMAIL.toString()));
                                info.setPhotoUrl(Uri.parse(snapshot.getString(UserKey.PHOTO_URL.toString())));
                                if(snapshot.getString(UserKey.GENDER.toString()).equals(Gender.MALE.toString()))
                                    info.setGender(Gender.MALE);
                                else
                                    info.setGender(Gender.FEMALE);
                                info.setAdmin(snapshot.getBoolean(UserKey.IS_ADMIN.toString()));
                                info.setDoctor(snapshot.getBoolean(UserKey.IS_DOCTOR.toString()));
                                info.setDateRegistered(new Date(snapshot.getLong(UserKey.DATE_REGISTERED.toString())));
                                userInfoList.add(info);
                                uidList.remove(uid);
                            }
                        }
                        listInterface.onUserListFetchCallback(true,userInfoList);
                    }
                    else {
                        Log.d(TAG,"Error in Fetching Assigned Users List");
                        listInterface.onUserListFetchCallback(false,null);
                    }
                });
    }
}
