package com.example.robodoc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements RegisterUser.RegisterUserInterface {

    EditText etName, etDOB;
    Spinner spinnerGender;
    Button btnSubmit;

    boolean isRegistering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName=findViewById(R.id.etName);
        etDOB=findViewById(R.id.editTextDate);
        spinnerGender=findViewById(R.id.spinnerGender);
        btnSubmit=findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                String dob;
                User.Gender gender;

                name=etName.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Please Enter Your Name",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(etDOB.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Please Enter Your DOB",Toast.LENGTH_SHORT).show();
                    return;
                }
                dob=etDOB.getText().toString();
                if(spinnerGender.getSelectedItemPosition()==0)
                    gender= User.Gender.MALE;
                else
                    gender= User.Gender.FEMALE;
                new RegisterUser(name,dob,gender,getSupportFragmentManager(),RegisterActivity.this);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(!isRegistering){
            onRegisterFailure();
        }
    }

    private void onRegisterFailure(){
        FirebaseAuth.getInstance().signOut();
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("866049601770-5gc1camgktsnhgfdbtimj5hvgepdovco.apps.googleusercontent.com")
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient= GoogleSignIn.getClient(RegisterActivity.this,gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                RegisterActivity.this.finish();
            }
        });
    }

    @Override
    public void onUserRegistration(boolean task) {
        if(task){
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            RegisterActivity.this.finish();
        }
        else
            onRegisterFailure();
    }
}