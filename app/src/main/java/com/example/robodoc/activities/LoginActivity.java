package com.example.robodoc.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.auth.SignInWithGoogle;
import com.example.robodoc.fragments.ProgressIndicatorFragment;
import com.example.robodoc.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements SignInWithGoogle.SignInInterface {

    private final int RC_SIGN_IN=1001;
    private ProgressIndicatorFragment progressIndicatorFragment;
    private boolean isAuthenticating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);

        isAuthenticating=false;

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Authenticating","Loading Google Accounts");
                progressIndicatorFragment.show(getSupportFragmentManager(),"Authenticating");
                GoogleSignInClient googleSignInClient=Globals.getGoogleSignInClient(LoginActivity.this);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent signInIntent=googleSignInClient.getSignInIntent();
                        startActivityForResult(signInIntent,RC_SIGN_IN);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN){
            progressIndicatorFragment.dismiss();
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                isAuthenticating=true;
                GoogleSignInAccount account=task.getResult(ApiException.class);
                new SignInWithGoogle(account.getIdToken(),getSupportFragmentManager(),LoginActivity.this);
            }
            catch (ApiException e){
                Log.w("AUTH","Google Sign In Failed, Error:"+e.getLocalizedMessage());
            }
        }
    }

    @Override
    public void signInResult(boolean result, boolean userExists) {
        isAuthenticating=false;
        if(result){
            Toast.makeText(LoginActivity.this,"Sign-In Successful",Toast.LENGTH_LONG).show();
            if(userExists)
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            else
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            LoginActivity.this.finish();
        }
        else {
            Snackbar.make(getWindow().getDecorView().getRootView(),"Sign In Failed",2500);
        }
    }

    @Override
    public void onBackPressed() {
        if(!isAuthenticating)
            super.onBackPressed();
    }
}