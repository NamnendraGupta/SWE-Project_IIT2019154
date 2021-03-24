package com.example.robodoc.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.classes.User;
import com.example.robodoc.enums.Gender;
import com.example.robodoc.enums.UserKey;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.firestore.RegisterUser;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements RegisterUser.RegisterUserInterface {

    private EditText etName;
    private TextView tvDOB;
    private RadioButton rbMale;
    private Date dob;
    private MaterialDatePicker datePicker;

    private boolean isRegistering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName=findViewById(R.id.etName);
        tvDOB=findViewById(R.id.tvDisplayDOB);
        Button btnSelectDOB = findViewById(R.id.btnSelectDOB);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        rbMale=findViewById(R.id.rbMale);

        isRegistering=false;

        dob=new Date();
        tvDOB.setText(getDisplayDate(dob));

        datePicker=MaterialDatePicker
                .Builder.datePicker()
                .setTitleText("Select Your DOB")
                .setSelection(dob.getTime())
                .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            dob=new Date((Long)selection);
            tvDOB.setText(getDisplayDate(dob));
        });

        btnSelectDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getSupportFragmentManager(),"DOB");
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                long dateOfBirth,dateRegistered;
                Gender gender;

                name=etName.getText().toString();
                if(name.isEmpty()){
                    Snackbar.make(getWindow().getDecorView().getRootView(),"Please Enter Your Name",2000).show();
                    return;
                }

                dateOfBirth=dob.getTime();
                dateRegistered=new Date().getTime();

                if(rbMale.isChecked())
                    gender=Gender.MALE;
                else
                    gender=Gender.FEMALE;

                isRegistering=true;
                HashMap<String,Object> userData=Globals.getFirebaseUserInfo();

                userData.put(UserKey.NAME.toString(),name);
                userData.put(UserKey.DOB.toString(),dateOfBirth);
                userData.put(UserKey.DATE_REGISTERED.toString(),dateRegistered);
                userData.put(UserKey.GENDER.toString(),gender);
                userData.put(UserKey.IS_ADMIN.toString(),false);
                userData.put(UserKey.IS_DOCTOR.toString(),false);

                new RegisterUser(getSupportFragmentManager(),RegisterActivity.this,userData);
            }
        });
    }

    private String getDisplayDate(Date date){
        String result;
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        result=calendar.get(Calendar.DAY_OF_MONTH)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.YEAR);
        return result;
    }

    @Override
    public void onBackPressed() {
        if(!isRegistering)
            onRegistrationFailed();
    }

    @Override
    public void onUserRegister(boolean result) {
        isRegistering=false;
        if(result){
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
            RegisterActivity.this.finish();
        }
        else
            onRegistrationFailed();
    }

    private void onRegistrationFailed(){
        Globals.SignOut();
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        RegisterActivity.this.finish();
    }
}