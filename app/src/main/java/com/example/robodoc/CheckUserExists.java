package com.example.robodoc;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CheckUserExists {

    public interface checkInterface{
        public void onEventCallback(boolean result);
    }

    private String uid;
    private FragmentManager fragmentManager;
    private ProgressIndicatorFragment progressIndicatorFragment;
    private checkInterface anInterface;

    public CheckUserExists(String uid, FragmentManager fragmentManager, checkInterface anInterface){
        this.uid=uid;
        this.fragmentManager=fragmentManager;
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Retrieving Information","Checking whether User already Exists or Not");
        this.anInterface=anInterface;
        checkUser();
    }

    private void checkUser(){
        progressIndicatorFragment.show(fragmentManager,"Checking User");
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference=firestore.collection("USERS");
        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressIndicatorFragment.dismiss();
                        if(task.isSuccessful()){
                            Log.d("SUCCESS","Database loaded successfully");
                            for(QueryDocumentSnapshot snapshot:task.getResult())
                                if(snapshot.getId().equals(uid)){
                                    Log.d("USER","User Already Exists");
                                    anInterface.onEventCallback(true);
                                    return;
                                }
                            Log.d("USER","User does not exists");
                            anInterface.onEventCallback(false);
                        }
                        else
                            Log.d("ERROR","Error in Checking from database");
                    }
                });
    }

}
