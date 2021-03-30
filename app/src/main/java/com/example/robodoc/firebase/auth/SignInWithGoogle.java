package com.example.robodoc.firebase.auth;

import android.util.Log;

import androidx.fragment.app.FragmentManager;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.firestore.CheckUserExists;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInWithGoogle implements CheckUserExists.checkUserExistsInterface {

    public interface SignInInterface{
        void signInResult(boolean result, boolean userExists);
    }

    private final String TAG="SignIn";

    private final ProgressIndicatorFragment progressIndicatorFragment;
    private final SignInInterface signInInterface;

    public SignInWithGoogle(String idToken, FragmentManager fragmentManager, SignInInterface signInInterface){
        this.signInInterface=signInInterface;
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Authenticating","Signing In");
        progressIndicatorFragment.show(fragmentManager,"Signing In");

        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        Globals.getAuth().signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    progressIndicatorFragment.dismiss();
                    if(task.isSuccessful()){
                        Globals.updateCurrentUser();
                        Log.d(TAG,"Sign in with Google Successful");
                        new CheckUserExists(Globals.getCurrentUserUid(),fragmentManager,SignInWithGoogle.this);
                    }
                    else {
                        Log.d(TAG,"Sign-In Failed");
                        Globals.SignOut();
                        signInInterface.signInResult(false,false);
                    }
                });
    }

    @Override
    public void onTaskResult(boolean result, boolean task) {
        if(task){
            signInInterface.signInResult(true,result);
        }
        else {
            Globals.SignOut();
            signInInterface.signInResult(false,false);
        }
    }

}
