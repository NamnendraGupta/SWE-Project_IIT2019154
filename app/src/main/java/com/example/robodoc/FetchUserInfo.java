package com.example.robodoc;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.Key;
import java.util.HashMap;

public class FetchUserInfo {

    public interface FetchedUserInfoCallback{
        public void onUserDataFetched(HashMap<String ,Object> UserData);
    }

    private String UID;
    private FetchedUserInfoCallback callback;
    private ProgressIndicatorFragment progressIndicatorFragment;
    private FragmentManager fragmentManager;

    public FetchUserInfo(FragmentManager fragmentManager, String UID, FetchedUserInfoCallback callback){
        this.fragmentManager=fragmentManager;
        this.UID=UID;
        this.callback=callback;
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing","Fetching User Information from Server");
        fetchData();
    }

    private void fetchData(){
        progressIndicatorFragment.show(fragmentManager,"FETCH DATA");
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("USERS")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                progressIndicatorFragment.dismiss();
                if(task.isSuccessful()){
                    Log.d("DATABASE","User Fetch Successful");
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        if(snapshot.getId().equals(UID)){
                            HashMap<String , Object> hashMap=new HashMap<>();
                            hashMap.put(User.Keys.UID.toString(),snapshot.get(User.Keys.UID.toString()));
                            hashMap.put(User.Keys.EMAIL.toString(),snapshot.get(User.Keys.EMAIL.toString()));
                            hashMap.put(User.Keys.NAME.toString(),snapshot.get(User.Keys.NAME.toString()));
                            hashMap.put(User.Keys.PHOTO_URL.toString(),snapshot.get(User.Keys.PHOTO_URL.toString()));
                            hashMap.put(User.Keys.DOB.toString(),snapshot.get(User.Keys.DOB.toString()));
                            hashMap.put(User.Keys.DATE_REGISTERED.toString(),snapshot.get(User.Keys.DATE_REGISTERED.toString()));
                            hashMap.put(User.Keys.GENDER.toString(),snapshot.get(User.Keys.GENDER.toString()));
                            hashMap.put(User.Keys.IS_ADMIN.toString(), snapshot.get(User.Keys.IS_ADMIN.toString()));
                            hashMap.put(User.Keys.IS_DOCTOR.toString(),snapshot.get(User.Keys.IS_DOCTOR.toString()));
                            callback.onUserDataFetched(hashMap);
                            return;
                        }
                    }
                }
                else {
                    Log.d("Error","Error in Fetching Information");
                }
            }
        });

    }

}
