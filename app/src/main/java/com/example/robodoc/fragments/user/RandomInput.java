package com.example.robodoc.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.firebase.realtimeDb.UploadVitalInput;
import com.example.robodoc.utils.GetRandomBloodPressure;
import com.example.robodoc.utils.GetRandomBodyTemperature;
import com.example.robodoc.utils.GetRandomGlucoseLevel;
import com.example.robodoc.utils.GetRandomHeartRate;
import com.example.robodoc.utils.GetRandomOxygenLevel;

public class RandomInput extends Fragment implements UploadVitalInput.UploadVitalInputInterface {

    public RandomInput() {
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
        return inflater.inflate(R.layout.fragment_random_input, container, false);
    }

    Button btnSubmit, btnCancel, btnReGenerate;
    TextView tvSysBloodPressure, tvDiasBloodPressure, tvBodyTemperature, tvGlucoseLevel, tvOxygenLevel, tvHeartRate;
    VitalInput vitalInput;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnSubmit=view.findViewById(R.id.btnSubmitInput);
        btnCancel=view.findViewById(R.id.btnCancel);
        btnReGenerate=view.findViewById(R.id.btnRegenerate);

        tvSysBloodPressure=view.findViewById(R.id.tvBpSys);
        tvDiasBloodPressure=view.findViewById(R.id.tvBpDias);
        tvBodyTemperature=view.findViewById(R.id.tvBodyTemp);
        tvGlucoseLevel=view.findViewById(R.id.tvGlucoseLevel);
        tvOxygenLevel=view.findViewById(R.id.tvOxygenLevel);
        tvHeartRate=view.findViewById(R.id.tvHeartRate);

        generateRandomInput();

        btnReGenerate.setOnClickListener(v -> generateRandomInput());

        btnSubmit.setOnClickListener(v -> new UploadVitalInput(getParentFragmentManager(),RandomInput.this,vitalInput.getInitialHashMap()));
    }

    private void generateRandomInput(){
        vitalInput=new VitalInput(VitalInput.VitalInputType.AUTOMATIC);
        Pair<Integer, Integer> bp= GetRandomBloodPressure.get();
        vitalInput.setHighBP(bp.first);
        vitalInput.setLowBP(bp.second);
        vitalInput.setBodyTemperature(GetRandomBodyTemperature.get());
        vitalInput.setHeartRate(GetRandomHeartRate.get());
        vitalInput.setGlucoseLevel(GetRandomGlucoseLevel.get());
        vitalInput.setOxygenLevel(GetRandomOxygenLevel.get());
        updateViewWithData();
    }

    private void updateViewWithData(){
        tvSysBloodPressure.setText(Integer.toString(vitalInput.getHighBP()));
        tvDiasBloodPressure.setText(Integer.toString(vitalInput.getLowBP()));
        tvBodyTemperature.setText(String.format("%.02f",vitalInput.getBodyTemperature()));
        tvGlucoseLevel.setText(Integer.toString(vitalInput.getGlucoseLevel()));
        tvHeartRate.setText(Integer.toString(vitalInput.getHeartRate()));
        tvOxygenLevel.setText(Integer.toString(vitalInput.getOxygenLevel()));
    }

    @Override
    public void OnUpload(boolean result) {
        if(result){
            Toast.makeText(getContext(),"Data Uploaded Successfully",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getContext(),"Error in Uploading Data! Please Try Again",Toast.LENGTH_LONG).show();
        }
    }
}