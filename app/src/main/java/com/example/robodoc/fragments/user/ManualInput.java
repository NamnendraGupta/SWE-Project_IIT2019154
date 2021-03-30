package com.example.robodoc.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.firebase.realtimeDb.UploadVitalInput;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManualInput#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManualInput extends DialogFragment implements UploadVitalInput.UploadVitalInputInterface {

    public ManualInput() {
        // Required empty public constructor
    }

    public static ManualInput newInstance() {
        ManualInput fragment = new ManualInput();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manual_input, container, false);
    }

    Button btnCancel, btnSubmit;
    TextInputLayout inputBpSys, inputBpDias, inputBodyTemp, inputHeartRate, inputOxygenLevel, inputGlucoseLevel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCancel=view.findViewById(R.id.btnManualCancel);
        btnSubmit=view.findViewById(R.id.btnManualSubmitInput);

        inputBpSys=view.findViewById(R.id.inputBpSys);
        inputBpDias=view.findViewById(R.id.inputBpDias);
        inputBodyTemp=view.findViewById(R.id.inputBodyTemp);
        inputHeartRate=view.findViewById(R.id.inputHeartRate);
        inputOxygenLevel=view.findViewById(R.id.inputOxygenLevel);
        inputGlucoseLevel=view.findViewById(R.id.inputSugarLevel);

        inputBpSys.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(inputBpSys.getEditText().getText().toString().isEmpty())
                        inputBpSys.setError("Please Enter your Systolic Blood Pressure");
                    else
                        inputBpSys.setError("");
                }
                else
                    inputBpSys.setError("");
            }
        });

        inputBpDias.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(inputBpDias.getEditText().getText().toString().isEmpty())
                        inputBpDias.setError("Please Enter your Diastolic Blood Pressure");
                    else
                        inputBpDias.setError("");
                }
                else
                    inputBpDias.setError("");
            }
        });

        inputBodyTemp.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(inputBodyTemp.getEditText().getText().toString().isEmpty())
                        inputBodyTemp.setError("Please Enter your Body Temperature");
                    else
                        inputBodyTemp.setError("");
                }
                else
                    inputBodyTemp.setError("");
            }
        });

        inputHeartRate.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(inputHeartRate.getEditText().getText().toString().isEmpty())
                        inputHeartRate.setError("Please Enter your Heart Rate");
                    else
                        inputHeartRate.setError("");
                }
                else
                    inputHeartRate.setError("");
            }
        });

        inputOxygenLevel.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(inputOxygenLevel.getEditText().getText().toString().isEmpty())
                        inputOxygenLevel.setError("Please Enter your Oxygen Level");
                    else
                        inputOxygenLevel.setError("");
                }
                else
                    inputOxygenLevel.setError("");
            }
        });

        inputGlucoseLevel.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(inputGlucoseLevel.getEditText().getText().toString().isEmpty())
                        inputGlucoseLevel.setError("Please Enter your Glucose Level");
                    else
                        inputGlucoseLevel.setError("");
                }
                else
                    inputGlucoseLevel.setError("");
            }
        });

        getDialog().setCanceledOnTouchOutside(false);

        btnCancel.setOnClickListener(v -> getDialog().dismiss());

        btnSubmit.setOnClickListener(v -> {
            if(isInputValid()){
                VitalInput vitalInput=new VitalInput();
                vitalInput.setHighBP(Integer.parseInt(inputBpSys.getEditText().getText().toString()));
                vitalInput.setLowBP(Integer.parseInt(inputBpDias.getEditText().getText().toString()));
                vitalInput.setBodyTemperature(Float.parseFloat(inputBodyTemp.getEditText().getText().toString()));
                vitalInput.setHeartRate(Integer.parseInt(inputHeartRate.getEditText().getText().toString()));
                vitalInput.setGlucoseLevel(Integer.parseInt(inputGlucoseLevel.getEditText().getText().toString()));
                vitalInput.setOxygenLevel(Integer.parseInt(inputOxygenLevel.getEditText().getText().toString()));
                new UploadVitalInput(getFragmentManager(),ManualInput.this,vitalInput.getInitialHashMap());
            }
        });
    }

    private boolean isInputValid(){

        boolean result=true;

        if(inputBpSys.getEditText().getText().toString().isEmpty()){
            inputBpSys.setError("Please Enter your Systolic Blood Pressure");
            result=false;
        }

        if(inputBpDias.getEditText().getText().toString().isEmpty()){
            inputBpDias.setError("Please Enter your Diastolic Blood Pressure");
            result=false;
        }

        if(inputBodyTemp.getEditText().getText().toString().isEmpty()){
            inputBodyTemp.setError("Please Enter your Body Temperature");
            result=false;
        }

        if(inputHeartRate.getEditText().getText().toString().isEmpty()){
            inputHeartRate.setError("Please Enter your Heart Rate");
            result=false;
        }

        if(inputGlucoseLevel.getEditText().getText().toString().isEmpty()){
            inputOxygenLevel.setError("Please Enter your Oxygen Level");
            result=false;
        }

        if(inputOxygenLevel.getEditText().getText().toString().isEmpty()){
            inputGlucoseLevel.setError("Please Enter your Glucose Level");
            result=false;
        }

        return result;
    }

    @Override
    public void OnUpload(boolean result) {
        if(result){
            Toast.makeText(getContext(),"Data Upload Successful",Toast.LENGTH_LONG).show();
            getDialog().dismiss();
        }
        else {
            Toast.makeText(getContext(),"Error in Uploading Data! Please Try Again",Toast.LENGTH_LONG).show();
        }
    }
}