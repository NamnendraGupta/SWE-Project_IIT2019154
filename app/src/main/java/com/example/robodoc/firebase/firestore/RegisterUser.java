package com.example.robodoc.firebase.firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.realtimeDb.DatabaseKeys;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;

public class RegisterUser {

    public interface RegisterUserInterface{
        public void onUserRegister(boolean result);
    }

    private final String TAG="User Registration";
    private final ProgressIndicatorFragment progressIndicatorFragment;

    public RegisterUser(FragmentManager fragmentManager, RegisterUserInterface registerUserInterface, HashMap<String,Object> userData){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Registering User on Server");
        progressIndicatorFragment.show(fragmentManager,TAG);

        Globals
                .getFirestore()
                .collection("USERS")
                .document(Globals.getCurrentUserUid())
                .set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Globals.updateUserWithHashMap(userData);
                            updateRealtimeDB(registerUserInterface);
                        }
                        else {
                            progressIndicatorFragment.dismiss();
                            Log.d(TAG,"User Registration Failed");
                            registerUserInterface.onUserRegister(false);
                        }
                    }
                });

    }

    private void updateRealtimeDB(RegisterUserInterface registerUserInterface){
        Globals
                .getFirebaseDatabase()
                .getReference()
                .child(DatabaseKeys.KEY_USERS)
                .child(Globals.getCurrentUserUid())
                .child(DatabaseKeys.KEY_USER_VITAL_SIGNS_NUM)
                .setValue(false)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressIndicatorFragment.dismiss();
                        if(task.isSuccessful()){
                            Log.d(TAG,"User Registration Successful");
                            registerUserInterface.onUserRegister(true);
                        }
                        else {
                            Log.d(TAG,"User Registration Failed");
                            registerUserInterface.onUserRegister(false);
                        }
                    }
                });
    }

}
