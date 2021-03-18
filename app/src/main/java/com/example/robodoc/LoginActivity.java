package com.example.robodoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements CheckUserExists.checkInterface, FetchUserInfo.FetchedUserInfoCallback {

    Button btnLogin;
    FirebaseAuth auth;
    private final int RC_SIGN_IN=1001;
    GoogleSignInClient mGoogleSignInClient;
    ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin=findViewById(R.id.btnLogin);
        auth=FirebaseAuth.getInstance();

        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("866049601770-5gc1camgktsnhgfdbtimj5hvgepdovco.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn(){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Authenticating","Loading Google Accounts");
        progressIndicatorFragment.show(getSupportFragmentManager(),"Authenticating");
        Intent signInIntent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            progressIndicatorFragment.dismiss();
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            }
            catch (ApiException e){
                Log.w("AUTH","Google Sign In Failed");
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken){
        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Authenticating","Signing in with Google");
        progressIndicatorFragment.show(getSupportFragmentManager(),"Authenticating");
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressIndicatorFragment.dismiss();
                        if(task.isSuccessful()){
                            FirebaseUser user=auth.getCurrentUser();
                            Toast.makeText(LoginActivity.this,"Sign In Successful for "+user.getDisplayName(),Toast.LENGTH_LONG).show();
                            new CheckUserExists(user.getUid(),getSupportFragmentManager(),LoginActivity.this);
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"Sign In Failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onEventCallback(boolean result) {
        if(result){
            new FetchUserInfo(getSupportFragmentManager(),FirebaseAuth.getInstance().getCurrentUser().getUid(),LoginActivity.this);
        }
        else {
            startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            LoginActivity.this.finish();
        }
    }

    @Override
    public void onUserDataFetched(HashMap<String, Object> UserData) {
        startActivity(new Intent(LoginActivity.this,MainActivity.class));
        LoginActivity.this.finish();
    }
}