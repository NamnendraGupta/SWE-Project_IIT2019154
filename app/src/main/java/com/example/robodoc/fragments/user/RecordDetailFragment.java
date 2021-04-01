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
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.enums.VitalSignsKey;

import java.util.Calendar;
import java.util.Date;

public class RecordDetailFragment extends DialogFragment {

    public RecordDetailFragment() {
        // Required empty public constructor
    }

    public static RecordDetailFragment newInstance(VitalInput vitalInput) {
        RecordDetailFragment fragment = new RecordDetailFragment();
        Bundle args = vitalInput.getRecordBundle();
        fragment.setArguments(args);
        return fragment;
    }

    private String inputID;
    private Long TimeOfInput;
    private int HighBP, LowBP, GlucoseLevel, HeartRate, OxygenLevel;
    private float BodyTemp;
    private VitalInput.VitalInputType InputType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle=getArguments();
            inputID=bundle.getString(VitalSignsKey.INPUT_ID);
            TimeOfInput=bundle.getLong(VitalSignsKey.INPUT_TIME);
            HighBP=bundle.getInt(VitalSignsKey.BP_HIGH);
            LowBP=bundle.getInt(VitalSignsKey.BP_LOW);
            GlucoseLevel=bundle.getInt(VitalSignsKey.GLUCOSE_LEVEL);
            HeartRate=bundle.getInt(VitalSignsKey.HEART_RATE);
            OxygenLevel=bundle.getInt(VitalSignsKey.OXYGEN_LEVEL);
            BodyTemp=bundle.getFloat(VitalSignsKey.BODY_TEMP);
            InputType= VitalInput.VitalInputType.valueOf(bundle.getString(VitalSignsKey.INPUT_TYPE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_record_detail, container, false);
    }

    Button btnClose;

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
        btnClose=view.findViewById(R.id.btnRecordDetailClose);

        getDialog().setCanceledOnTouchOutside(false);

        tvID.setText(inputID);
        tvDate.setText(getRecordDate(TimeOfInput));
        tvTime.setText(getRecordTime(TimeOfInput));
        tvHighBP.setText(Integer.toString(HighBP));
        tvLowBP.setText(Integer.toString(LowBP));
        tvGlucose.setText(Integer.toString(GlucoseLevel));
        tvHeartRate.setText(Integer.toString(HeartRate));
        tvOxygen.setText(Integer.toString(OxygenLevel));
        tvBodyTemp.setText(String.format("%.02f",BodyTemp));
        tvInputType.setText(InputType.toString());

        btnClose.setOnClickListener(v -> {
            getDialog().dismiss();
        });
    }

    private String getRecordTime(Long time){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(time));
        String stringTime="";

        int hour=calendar.get(Calendar.HOUR);
        int minute=calendar.get(Calendar.MINUTE);

        if(hour==0)
            stringTime+="12:";
        else{
            if(hour<10)
                stringTime+="0";
            stringTime+=hour+":";
        }

        if(minute<10)
            stringTime+="0";
        stringTime+=minute+" ";

        if(calendar.get(Calendar.AM_PM)==1)
            stringTime+="PM";
        else
            stringTime+="AM";

        return stringTime;
    }

    private String getRecordDate(Long time){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date(time));
        String stringDate="";

        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH)+1;
        int year=calendar.get(Calendar.YEAR);

        if(day<10)
            stringDate+="0";
        stringDate+=day+"-";

        if(month<10)
            stringDate+="0";
        stringDate+=month+"-";

        stringDate+=year;

        return stringDate;
    }
}