package com.example.robodoc.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.example.robodoc.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class ProgressIndicatorFragment extends DialogFragment {

    private TextView tvTitle, tvMessage;
    private LinearProgressIndicator progressIndicator;

    public static ProgressIndicatorFragment newInstance(String title, String message){
        Bundle args=new Bundle();
        args.putString("TITLE",title);
        args.putString("MESSAGE",message);
        ProgressIndicatorFragment fragment=new ProgressIndicatorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.progress_indicator_fragment,container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle=view.findViewById(R.id.tvTitle);
        tvMessage=view.findViewById(R.id.tvMessage);
        progressIndicator=view.findViewById(R.id.progress_indicator);

        tvTitle.setText(getArguments().getString("TITLE"));
        tvMessage.setText(getArguments().getString("MESSAGE"));

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public void show(){
        getDialog().show();
    }

    public void updateData(String title, String message){
        tvTitle.setText(title);
        tvMessage.setText(message);
    }

}
