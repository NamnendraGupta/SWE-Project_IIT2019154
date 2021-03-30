package com.example.robodoc.firebase.realtimeDb;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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

        Globals
                .getFirebaseDatabase()
                .getReference()
                .child("VITAL_INPUTS")
                .child(Globals.getCurrentUserUid())
                .push()
                .setValue(inputData)
                .addOnCompleteListener(task -> {
                    progressIndicatorFragment.dismiss();
                    if(task.isSuccessful()){
                        Log.d(TAG,"Upload Successful");
                        inputInterface.OnUpload(true);
                    }
                    else {
                        Log.e(TAG,"Error in Uploading");
                        inputInterface.OnUpload(false);
                    }
                });
    }
}
