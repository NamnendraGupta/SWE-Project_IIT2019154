package com.example.robodoc.firebase.firestore;

import android.util.Log;

import com.example.robodoc.enums.UserKey;
import com.example.robodoc.firebase.realtimeDb.DatabaseKeys;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateUserRole {

    public interface UpdateUserRoleInterface{
        void UpdateResult(boolean result);
    }

    private final String TAG="Updating User Role";

    private final String UID;
    private final boolean hasAdminChanged;
    private final boolean hasDoctorChanged;
    private final boolean adminValue;
    private final boolean doctorValue;
    private final UpdateUserRoleInterface userRoleInterface;

    public UpdateUserRole(UpdateUserRoleInterface userRoleInterface, String uid, boolean hasAdminChanged, boolean isAdmin, boolean hasDoctorChanged, boolean isDoctor){

        this.userRoleInterface=userRoleInterface;
        this.UID=uid;
        this.hasAdminChanged=hasAdminChanged;
        this.hasDoctorChanged=hasDoctorChanged;
        this.adminValue=isAdmin;
        this.doctorValue=isDoctor;

        UpdateRoles();
    }

    private void UpdateRoles(){
        HashMap<String,Object> hashMap=new HashMap<>();

        if(hasAdminChanged)
            hashMap.put(UserKey.IS_ADMIN.toString(),adminValue);

        if(hasDoctorChanged)
            hashMap.put(UserKey.IS_DOCTOR.toString(),doctorValue);

        FirebaseFirestore
                .getInstance()
                .collection("USERS")
                .document(UID)
                .update(hashMap)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        if(hasDoctorChanged)
                            UpdateRealtimeDB();
                        else
                            userRoleInterface.UpdateResult(true);
                    }
                    else {
                        Log.e(TAG,"Error in Updating User Role");
                        userRoleInterface.UpdateResult(false);
                    }
                });
    }

    private void UpdateRealtimeDB(){

        DatabaseReference doctorReference= FirebaseDatabase
                .getInstance()
                .getReference()
                .child(DatabaseKeys.KEY_DOCTORS)
                .child(UID);

        if(doctorValue){
            doctorReference.child(DatabaseKeys.KEY_DOCTORS_IS_ACTIVE).setValue(true).addOnCompleteListener(task -> {
                Log.d(TAG,"Change of User Role Successful");
                userRoleInterface.UpdateResult(true);
            });
        }
        else {
            doctorReference.child(DatabaseKeys.KEY_DOCTORS_IS_ACTIVE).setValue(false).addOnCompleteListener(task -> {
                Log.d(TAG,"Change of User Role Successful");
                ArrayList<String> usersList=new ArrayList<>();
                doctorReference.child(DatabaseKeys.KEY_DOCTORS_ASSIGNED_LIST)
                        .get()
                        .addOnCompleteListener(task1 -> {
                            for(DataSnapshot dataSnapshot:task1.getResult().getChildren()){
                                usersList.add(dataSnapshot.getKey());
                            }
                            doctorReference.child(DatabaseKeys.KEY_DOCTORS_ASSIGNED_LIST).removeValue().addOnCompleteListener(task2 -> {
                                if(usersList.size()>0)
                                    UpdateAssignedUsersDB(usersList,0);
                                else {
                                    userRoleInterface.UpdateResult(true);
                                }
                            });
                        });
            });
        }
    }

    private void UpdateAssignedUsersDB(ArrayList<String> UsersList, int CurrentPosition){
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(DatabaseKeys.KEY_USERS)
                .child(UsersList.get(CurrentPosition))
                .child(DatabaseKeys.KEY_USER_DOCTOR_ASSIGNED)
                .removeValue()
                .addOnCompleteListener(task -> {
                    if(CurrentPosition==UsersList.size()-1){
                        userRoleInterface.UpdateResult(true);
                    }
                    else
                        UpdateAssignedUsersDB(UsersList,CurrentPosition+1);
                });
    }
}
