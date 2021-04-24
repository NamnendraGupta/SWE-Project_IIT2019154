package com.example.robodoc.firebase.realtimeDb;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UploadVitalInput {

    public interface UploadVitalInputInterface{
        void OnUpload(boolean result);
    }

    private final String TAG="Upload Vital Input";

    public UploadVitalInput(UploadVitalInputInterface inputInterface, String UID, HashMap<String,Object> inputData){
        DatabaseReference dbRef= FirebaseDatabase
                .getInstance()
                .getReference()
                .child(DatabaseKeys.KEY_USERS)
                .child(UID);

        dbRef.child(DatabaseKeys.KEY_USER_VITAL_SIGNS_NUM).setValue(true)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        dbRef.child(DatabaseKeys.KEY_USER_VITAL_SIGNS_LIST).push().setValue(inputData)
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        Log.d(TAG,"Upload Successful");
                                        inputInterface.OnUpload(true);
                                    }
                                    else {
                                        Log.e(TAG,"Error in Uploading");
                                        inputInterface.OnUpload(false);
                                    }
                                });
                    }
                    else {
                        Log.e(TAG,"Error in Uploading");
                        inputInterface.OnUpload(false);
                    }
                });
    }

}
