package com.example.robodoc.fragments.user;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.classes.UserInfo;
import com.squareup.picasso.Picasso;

public class DoctorInfoFragment extends DialogFragment {

    public DoctorInfoFragment() {
        // Required empty public constructor
    }

    private UserInfo doctorInfo;

    public DoctorInfoFragment(UserInfo doctorInfo){
        this.doctorInfo=doctorInfo;
    }

    public static DoctorInfoFragment newInstance() {
        DoctorInfoFragment fragment = new DoctorInfoFragment();
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
        return inflater.inflate(R.layout.fragment_doctor_info, container, false);
    }

    TextView tvName, tvEmail;
    ImageView imgPhoto;
    Button btnRequestForAppointment, btnClose;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvName=view.findViewById(R.id.tvDoctorInfoName);
        tvEmail=view.findViewById(R.id.tvDoctorInfoEmail);
        imgPhoto=view.findViewById(R.id.imgDoctorInfo);
        btnRequestForAppointment=view.findViewById(R.id.btnDoctorInfoRequest);
        btnClose=view.findViewById(R.id.btnDoctorInfoCancel);

        tvName.setText(doctorInfo.getName());
        tvEmail.setText(doctorInfo.getEmail());
        Picasso.get().load(doctorInfo.getPhotoUrl()).into(imgPhoto);

        btnClose.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        btnRequestForAppointment.setOnClickListener(v -> {

        });
    }
}