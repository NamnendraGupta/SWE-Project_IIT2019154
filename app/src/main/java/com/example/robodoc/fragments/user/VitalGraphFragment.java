package com.example.robodoc.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.enums.VitalInputType;

public class VitalGraphFragment extends Fragment {

    public VitalGraphFragment() {
        // Required empty public constructor
    }

    public VitalGraphFragment(VitalInputType inputType){
        Bundle args = new Bundle();
        args.putString("TYPE",inputType.toString());
        setArguments(args);
    }

    public static VitalGraphFragment newInstance(VitalInputType inputType) {
        VitalGraphFragment fragment = new VitalGraphFragment();
        Bundle args = new Bundle();
        args.putString("TYPE",inputType.toString());
        fragment.setArguments(args);
        return fragment;
    }


    VitalInputType inputType;
    TextView tvInputType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inputType=VitalInputType.valueOf(getArguments().getString("TYPE"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vital_graph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvInputType=view.findViewById(R.id.tvGraphFragmentLabel);
        tvInputType.setText(inputType.toString());
    }
}