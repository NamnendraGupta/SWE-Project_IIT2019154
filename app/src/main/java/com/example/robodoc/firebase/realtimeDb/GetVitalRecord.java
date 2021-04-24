package com.example.robodoc.firebase.realtimeDb;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.robodoc.classes.VitalInput;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GetVitalRecord {

    public interface GetVitalRecordInterface{
        void onNewRecordObtained(boolean hasRecords, VitalInput newRecord);
    }

    private final String TAG="Getting Vital Record";

    public  GetVitalRecord(GetVitalRecordInterface vitalRecordInterface, String uid){

        DatabaseReference dbRef= FirebaseDatabase
                .getInstance()
                .getReference()
                .child(DatabaseKeys.KEY_USERS)
                .child(uid);

        dbRef.get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DataSnapshot snapshot=task.getResult();
                        boolean hasRecords=(Boolean) snapshot.child(DatabaseKeys.KEY_USER_VITAL_SIGNS_NUM).getValue();
                        if(!hasRecords){
                            vitalRecordInterface.onNewRecordObtained(false,null);
                        }
                        else {
                            for(DataSnapshot snapshot1:snapshot.child(DatabaseKeys.KEY_USER_VITAL_SIGNS_LIST).getChildren()){
                                vitalRecordInterface.onNewRecordObtained(true,new VitalInput(snapshot1));
                            }
                        }
                    }
                    else {
                        vitalRecordInterface.onNewRecordObtained(false,null);
                    }
                });


        dbRef.child(DatabaseKeys.KEY_USER_VITAL_SIGNS_LIST)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        vitalRecordInterface.onNewRecordObtained(true, new VitalInput(snapshot));
                        Log.w(TAG,"Added - "+snapshot.toString());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Log.w(TAG,"Changed - "+snapshot.toString());
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Log.w(TAG,"Removed - "+snapshot.toString());
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Log.w(TAG,"Moved - "+snapshot.toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e(TAG, error.getMessage());
                    }
                });
    }
}
