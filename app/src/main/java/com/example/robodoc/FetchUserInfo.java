package com.example.robodoc;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class FetchUserInfo {

    public interface FetchedUserInfoCallback{
        public void onUserDataFetched(HashMap<String ,Object> UserData);
    }

    private String UID;
    private FetchedUserInfoCallback callback;
    private Context context;

    public FetchUserInfo(Context context, String UID, FetchedUserInfoCallback callback){
        this.context=context;
        this.UID=UID;
        this.callback=callback;
        fetchData();
    }

    private void fetchData(){
        ProgressDialog dialog=new ProgressDialog(context);
        dialog.setTitle("Syncing");
        dialog.setMessage("Fetching Data from Server");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                HashMap<String ,Object> hashMap=new HashMap<>();
            }
        });

    }

}
