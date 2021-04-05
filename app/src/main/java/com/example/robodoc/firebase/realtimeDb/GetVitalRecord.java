package com.example.robodoc.firebase.realtimeDb;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.firebase.Globals;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class GetVitalRecord {

    public interface GetVitalRecordInterface{
        void onNewRecordObtained(VitalInput newRecord);
    }

    private final String TAG="Getting Vital Record";

    public  GetVitalRecord(GetVitalRecordInterface vitalRecordInterface){

        Globals
                .getFirebaseDatabase()
                .getReference()
                .child("VITAL_INPUTS")
                .child(Globals.getCurrentUserUid())
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        vitalRecordInterface.onNewRecordObtained(new VitalInput(snapshot));
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
