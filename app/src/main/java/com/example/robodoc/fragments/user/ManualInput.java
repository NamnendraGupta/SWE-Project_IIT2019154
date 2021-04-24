package com.example.robodoc.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.firebase.realtimeDb.UploadVitalInput;
import com.example.robodoc.fragments.utils.ProgressIndicatorFragment;
import com.example.robodoc.viewModels.user.UserInfoViewModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

public class ManualInput extends Fragment implements UploadVitalInput.UploadVitalInputInterface {

    public ManualInput() {
        // Required empty public constructor
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

    private TextInputLayout inputBpSys, inputBpDias, inputBodyTemp, inputHeartRate, inputOxygenLevel, inputGlucoseLevel;
    private ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnSubmit = view.findViewById(R.id.btnManualSubmitInput);

        inputBpSys=view.findViewById(R.id.inputBpSys);
        inputBpDias=view.findViewById(R.id.inputBpDias);
        inputBodyTemp=view.findViewById(R.id.inputBodyTemp);
        inputHeartRate=view.findViewById(R.id.inputHeartRate);
        inputOxygenLevel=view.findViewById(R.id.inputOxygenLevel);
        inputGlucoseLevel=view.findViewById(R.id.inputSugarLevel);

        inputBpSys.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(inputBpSys.getEditText().getText().toString().isEmpty())
                    inputBpSys.setError("Please Enter your Systolic Blood Pressure");
                else
                    inputBpSys.setError("");
            }
            else
                inputBpSys.setError("");
        });

        inputBpDias.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(inputBpDias.getEditText().getText().toString().isEmpty())
                    inputBpDias.setError("Please Enter your Diastolic Blood Pressure");
                else
                    inputBpDias.setError("");
            }
            else
                inputBpDias.setError("");
        });

        inputBodyTemp.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(inputBodyTemp.getEditText().getText().toString().isEmpty())
                    inputBodyTemp.setError("Please Enter your Body Temperature");
                else
                    inputBodyTemp.setError("");
            }
            else
                inputBodyTemp.setError("");
        });

        inputHeartRate.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(inputHeartRate.getEditText().getText().toString().isEmpty())
                    inputHeartRate.setError("Please Enter your Heart Rate");
                else
                    inputHeartRate.setError("");
            }
            else
                inputHeartRate.setError("");
        });

        inputOxygenLevel.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(inputOxygenLevel.getEditText().getText().toString().isEmpty())
                    inputOxygenLevel.setError("Please Enter your Oxygen Level");
                else
                    inputOxygenLevel.setError("");
            }
            else
                inputOxygenLevel.setError("");
        });

        inputGlucoseLevel.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(inputGlucoseLevel.getEditText().getText().toString().isEmpty())
                    inputGlucoseLevel.setError("Please Enter your Glucose Level");
                else
                    inputGlucoseLevel.setError("");
            }
            else
                inputGlucoseLevel.setError("");
        });

        btnSubmit.setOnClickListener(v -> {
            if(isInputValid()){
                UserInfoViewModel viewModel=new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
                VitalInput vitalInput=new VitalInput(VitalInput.VitalInputType.MANUAL);
                vitalInput.setHighBP(Integer.parseInt(inputBpSys.getEditText().getText().toString()));
                vitalInput.setLowBP(Integer.parseInt(inputBpDias.getEditText().getText().toString()));
                vitalInput.setBodyTemperature(Float.parseFloat(inputBodyTemp.getEditText().getText().toString()));
                vitalInput.setHeartRate(Integer.parseInt(inputHeartRate.getEditText().getText().toString()));
                vitalInput.setGlucoseLevel(Integer.parseInt(inputGlucoseLevel.getEditText().getText().toString()));
                vitalInput.setOxygenLevel(Integer.parseInt(inputOxygenLevel.getEditText().getText().toString()));

                progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing With Server","Uploading Vital Data");
                progressIndicatorFragment.show(getParentFragmentManager(),"UploadData");
                new UploadVitalInput(ManualInput.this,viewModel.getUId(),vitalInput.getInitialHashMap());
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
        progressIndicatorFragment.dismiss();
        if(result){
            NavController navController=Navigation.findNavController(requireActivity(),R.id.navHostMain);
            navController.popBackStack(R.id.placeholderMain,false);
            ShowSnackbar();
        }
        else {
            Toast.makeText(getContext(),"Error in Uploading Data! Please Try Again",Toast.LENGTH_LONG).show();
        }
    }

    private void ShowSnackbar(){
        Snackbar snackbar=Snackbar.make(requireActivity().getWindow().getDecorView().getRootView(),"New Record Added",4000);
        snackbar.setActionTextColor(requireContext().getResources().getColor(R.color.colorPrimaryDark));
        snackbar.setBackgroundTint(requireContext().getResources().getColor(R.color.colorAccentDark));
        snackbar.show();
    }
}