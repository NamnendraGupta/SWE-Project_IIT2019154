package com.example.robodoc.fragments.shared;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.utils.DateTimeUtils;
import com.example.robodoc.viewModels.user.RecordListViewModel;

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

        RecordListViewModel viewModel=new ViewModelProvider(requireActivity()).get(RecordListViewModel.class);
        VitalInput vitalInput=viewModel.GetRecordInfo(RecordDetailFragmentArgs.fromBundle(getArguments()).getListPosition());

        tvID.setText(vitalInput.getInputID());
        tvDate.setText(DateTimeUtils.getDisplayDate(vitalInput.getTimeOfInput()));
        tvTime.setText(DateTimeUtils.getDisplayTime(vitalInput.getTimeOfInput()));
        tvHighBP.setText(Integer.toString(vitalInput.getHighBP()));
        tvLowBP.setText(Integer.toString(vitalInput.getLowBP()));
        tvGlucose.setText(Integer.toString(vitalInput.getGlucoseLevel()));
        tvHeartRate.setText(Integer.toString(vitalInput.getHeartRate()));
        tvOxygen.setText(Integer.toString(vitalInput.getOxygenLevel()));
        tvBodyTemp.setText(String.format("%.02f", vitalInput.getBodyTemperature()));
        tvInputType.setText(vitalInput.getInputType().toString());
    }
}