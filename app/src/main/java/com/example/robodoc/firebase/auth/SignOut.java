package com.example.robodoc.firebase.auth;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SignOut {

    public interface SignOutInterface{
        public void onSignOut(boolean result);
    }

    private final String TAG="Logout";
    private final ProgressIndicatorFragment progressIndicatorFragment;

    public SignOut(Context context, SignOutInterface signOutInterface, FragmentManager fragmentManager){

        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Signing Out","Logging out the Current User");
        progressIndicatorFragment.show(fragmentManager,"Signing Out");

        Globals.SignOut();

        GoogleSignInClient googleSignInClient=Globals.getGoogleSignInClient(context);
        googleSignInClient
                .signOut()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressIndicatorFragment.dismiss();
                        if(task.isSuccessful()){
                            Log.d(TAG,"Sign Out Successful!");
                            signOutInterface.onSignOut(true);
                        }
                        else {
                            Log.d(TAG,"Sign Out Failed");
                            signOutInterface.onSignOut(false);
                        }
                    }
                });

    }

}
