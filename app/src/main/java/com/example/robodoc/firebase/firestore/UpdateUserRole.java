package com.example.robodoc.firebase.firestore;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.robodoc.enums.UserKey;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;

public class UpdateUserRole {

    public interface UpdateUserRoleInterface{
        public void UpdateResult(boolean result);
    }

    private final String TAG="Updating User Role";
    private final ProgressIndicatorFragment progressIndicatorFragment;

    public UpdateUserRole(FragmentManager manager, UpdateUserRoleInterface userRoleInterface, String uid, boolean isAdmin, boolean isDoctor){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Updating User Roles");
        progressIndicatorFragment.show(manager,TAG);

        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put(UserKey.IS_DOCTOR.toString(),isDoctor);
        hashMap.put(UserKey.IS_ADMIN.toString(),isAdmin);
        Globals
                .getFirestore()
                .collection("USERS")
                .document(uid)
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressIndicatorFragment.dismiss();
                        if(task.isSuccessful()){
                            Log.d(TAG,"Change of User Role Successful");
                            userRoleInterface.UpdateResult(true);
                        }
                        else {
                            Log.e(TAG,"Error in Updating User Role");
                            userRoleInterface.UpdateResult(false);
                        }
                    }
                });
    }

}
