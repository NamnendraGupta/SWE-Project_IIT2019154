package com.example.robodoc.fragments.shared;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.utils.DateTimeUtils;

public class RecordDetailFragment extends Fragment {

    public RecordDetailFragment() {
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
        return inflater.inflate(R.layout.fragment_record_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvID = view.findViewById(R.id.tvRecordDetailID);
        TextView tvDate = view.findViewById(R.id.tvRecordDetailDate);
        TextView tvTime = view.findViewById(R.id.tvRecordDetailTime);
        TextView tvHighBP = view.findViewById(R.id.tvRecordDetailHighBP);
        TextView tvLowBP = view.findViewById(R.id.tvRecordDetailLowBP);
        TextView tvGlucose = view.findViewById(R.id.tvRecordDetailGlucose);
        TextView tvHeartRate = view.findViewById(R.id.tvRecordDetailHeartRate);
        TextView tvOxygen = view.findViewById(R.id.tvRecordDetailOxygen);
        TextView tvBodyTemp = view.findViewById(R.id.tvRecordDetailTemp);
        TextView tvInputType = view.findViewById(R.id.tvRecordDetailType);

        RecordDetailFragmentArgs args=RecordDetailFragmentArgs.fromBundle(getArguments());
        String inputID = args.getInputID();
        Long timeOfInput = args.getTimeOfInput();
        int highBP = args.getHighBP();
        int lowBP = args.getLowBP();
        int glucoseLevel = args.getGlucoseLevel();
        int heartRate = args.getHeartRate();
        int oxygenLevel = args.getOxygenLevel();
        float bodyTemp = args.getBodyTemperature();
        VitalInput.VitalInputType inputType = VitalInput.VitalInputType.valueOf(args.getInputType());

        tvID.setText(inputID);
        tvDate.setText(DateTimeUtils.getDisplayDate(timeOfInput));
        tvTime.setText(DateTimeUtils.getDisplayTime(timeOfInput));
        tvHighBP.setText(Integer.toString(highBP));
        tvLowBP.setText(Integer.toString(lowBP));
        tvGlucose.setText(Integer.toString(glucoseLevel));
        tvHeartRate.setText(Integer.toString(heartRate));
        tvOxygen.setText(Integer.toString(oxygenLevel));
        tvBodyTemp.setText(String.format("%.02f", bodyTemp));
        tvInputType.setText(inputType.toString());
    }
}