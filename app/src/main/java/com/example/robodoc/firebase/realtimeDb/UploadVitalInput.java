package com.example.robodoc.firebase.realtimeDb;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.fragments.shared.ProgressIndicatorFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class UploadVitalInput {

    public interface UploadVitalInputInterface{
        void OnUpload(boolean result);
    }

    private final String TAG="Upload Vital Input";
    private final ProgressIndicatorFragment progressIndicatorFragment;

    public UploadVitalInput(FragmentManager fragmentManager, UploadVitalInputInterface inputInterface, HashMap<String,Object> inputData){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Synchronizing with Server","Uploading Input of Vital-Signs of User");
        progressIndicatorFragment.show(fragmentManager,TAG);

        DatabaseReference dbRef=Globals
                .getFirebaseDatabase()
                .getReference()
                .child(DatabaseKeys.KEY_USERS)
                .child(Globals.getCurrentUserUid());

        dbRef.child(DatabaseKeys.KEY_USER_VITAL_SIGNS_NUM).setValue(true)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        dbRef.child(DatabaseKeys.KEY_USER_VITAL_SIGNS_LIST).push().setValue(inputData)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressIndicatorFragment.dismiss();
                                        if(task.isSuccessful()){
                                            Log.d(TAG,"Upload Successful");
                                            inputInterface.OnUpload(true);
                                        }
                                        else {
                                            Log.e(TAG,"Error in Uploading");
                                            inputInterface.OnUpload(false);
                                        }
                                    }
                                });
                    }
                    else {
                        progressIndicatorFragment.dismiss();
                        Log.e(TAG,"Error in Uploading");
                        inputInterface.OnUpload(false);
                    }
                });
    }
}
