package com.example.robodoc.fragments.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.robodoc.R;

public class AlertDialogFragment extends DialogFragment {

    private AlertDialogFragment(){

    }

    public static AlertDialogFragment newInstance(String title, String message){
        Bundle args=new Bundle();
        args.putString("TITLE",title);
        args.putString("MESSAGE",message);
        AlertDialogFragment alertDialogFragment=new AlertDialogFragment();
        alertDialogFragment.setArguments(args);
        return alertDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alert_dialog_fragment,container);
    }

    public interface AlertDialogInterface{
        void onPositiveButtonClicked(String type);
    }

    private AlertDialogInterface dialogInterface;
    private String ActionType;

    public void SetInterface(AlertDialogInterface dialogInterface, String type){
        this.dialogInterface=dialogInterface;
        this.ActionType=type;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvTitle = view.findViewById(R.id.tvAlertDialogTitle);
        TextView tvMessage = view.findViewById(R.id.tvAlertDialogMessage);
        Button btnPositive = view.findViewById(R.id.btnAlertDialogPositive);
        Button btnNegative = view.findViewById(R.id.btnAlertDialogNegative);

        tvTitle.setText(getArguments().getString("TITLE"));
        tvMessage.setText(getArguments().getString("MESSAGE"));

        btnNegative.setOnClickListener(v -> getDialog().dismiss());

        btnPositive.setOnClickListener(v -> {
            dialogInterface.onPositiveButtonClicked(ActionType);
            getDialog().dismiss();
        });
    }
}
