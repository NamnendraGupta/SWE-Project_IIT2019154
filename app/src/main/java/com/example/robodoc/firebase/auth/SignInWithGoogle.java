package com.example.robodoc.firebase.auth;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.firestore.CheckUserExists;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInWithGoogle implements CheckUserExists.checkUserExistsInterface {

    public interface SignInInterface{
        public void signInResult(boolean result, boolean userExists);
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
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressIndicatorFragment.dismiss();
                        if(task.isSuccessful()){
                            Log.d(TAG,"Sign in with Google Successful");
                            new CheckUserExists(Globals.getCurrentUserUid(),fragmentManager,SignInWithGoogle.this);
                        }
                        else {
                            Log.d(TAG,"Sign-In Failed");
                            Globals.SignOut();
                            signInInterface.signInResult(false,false);
                        }
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
