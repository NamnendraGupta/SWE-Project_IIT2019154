package com.example.robodoc.firebase.firestore;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.enums.Gender;
import com.example.robodoc.enums.UserKey;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.realtimeDb.DatabaseKeys;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class GetAssignedUsersList {

    public interface GetAssignedUsersListInterface{
        void userListInterface(boolean result, ArrayList<UserInfo> userInfoList);
    }

    private final String TAG="Getting Users List";
    private final ProgressIndicatorFragment progressIndicatorFragment;

    public GetAssignedUsersList(FragmentManager manager, GetAssignedUsersListInterface listInterface, String doctorUId){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing With Server","Getting Assigned Users List");
        progressIndicatorFragment.show(manager,TAG);

        ArrayList<String> uidList=new ArrayList<>();

        Globals
                .getFirebaseDatabase()
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
                            progressIndicatorFragment.dismiss();
                            Log.d(TAG,"Fetch Successful");
                            listInterface.userListInterface(true, new ArrayList<>());
                        }
                        else {
                            fetchFromFirestore(uidList,listInterface);
                        }
                    }
                    else {
                        progressIndicatorFragment.dismiss();
                        Log.d(TAG,"Error in Fetching Assigned Users List");
                        listInterface.userListInterface(false,null);
                    }
                });
    }

    private void fetchFromFirestore(ArrayList<String> uidList, GetAssignedUsersListInterface listInterface){

        Globals
                .getFirestore()
                .collection("USERS")
                .get()
                .addOnCompleteListener(task -> {
                    progressIndicatorFragment.dismiss();
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
                        listInterface.userListInterface(true,userInfoList);
                    }
                    else {
                        Log.d(TAG,"Error in Fetching Assigned Users List");
                        listInterface.userListInterface(false,null);
                    }
                });

    }

}
