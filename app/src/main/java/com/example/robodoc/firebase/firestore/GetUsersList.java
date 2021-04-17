package com.example.robodoc.firebase.firestore;

import android.net.Uri;
import android.util.Log;

import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.enums.Gender;
import com.example.robodoc.enums.UserKey;
import com.example.robodoc.firebase.Globals;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;

public class GetUsersList {
    
    public interface GetUsersListInterface{
        void getUsersList(boolean result, ArrayList<UserInfo> userList);
    }
    
    private final String TAG="Get Users List";
    
    public GetUsersList(GetUsersListInterface listInterface){

        Globals
                .getFirestore()
                .collection("USERS")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d(TAG,"Users List fetched successfully");
                        ArrayList<UserInfo> userList=new ArrayList<>();
                        for(DocumentSnapshot snapshot:task.getResult()){
                            String  uid=snapshot.getId();
                            if(!uid.equals(Globals.getCurrentUserUid())){
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
                                userList.add(info);
                            }
                        }
                        listInterface.getUsersList(true,userList);
                    }
                    else {
                        Log.e(TAG,"Error in Fetching Users List");
                        listInterface.getUsersList(false,null);
                    }
                });
        
    }
    
}
