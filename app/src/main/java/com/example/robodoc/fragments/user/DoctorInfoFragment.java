package com.example.robodoc.fragments.user;

import android.content.DialogInterface;
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
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.realtimeDb.AssignDoctorToUser;
import com.example.robodoc.firebase.realtimeDb.CancelCurrentAppointment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

public class DoctorInfoFragment extends DialogFragment implements AssignDoctorToUser.AssignDoctorToUserInterface, CancelCurrentAppointment.CancelCurrentAppointmentInterface {

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
    Button btnRequestForAppointment, btnClose, btnViewInteraction, btnCancelAppointment;

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
        btnViewInteraction=view.findViewById(R.id.btnViewInteraction);
        btnCancelAppointment=view.findViewById(R.id.btnCancelAppointment);

        tvName.setText(doctorInfo.getName());
        tvEmail.setText(doctorInfo.getEmail());
        Picasso.get().load(doctorInfo.getPhotoUrl()).into(imgPhoto);

        btnClose.setOnClickListener(v -> {
            getDialog().dismiss();
        });

        if(doctorInfo.getUID().equals(Globals.getCurrentUser().getAssignedDoctorUID())){
            btnRequestForAppointment.setVisibility(View.GONE);
            btnViewInteraction.setVisibility(View.VISIBLE);
            btnCancelAppointment.setVisibility(View.VISIBLE);
        }
        else {
            btnRequestForAppointment.setVisibility(View.VISIBLE);
            btnViewInteraction.setVisibility(View.GONE);
            btnCancelAppointment.setVisibility(View.GONE);
        }

        btnRequestForAppointment.setOnClickListener(v -> {
            MaterialAlertDialogBuilder alertDialogBuilder=new MaterialAlertDialogBuilder(getActivity())
                    .setTitle("Message")
                    .setMessage("Are You Sure you want to Request for Appointment with "+doctorInfo.getName()+"?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new AssignDoctorToUser(Globals.getCurrentUserUid(),doctorInfo.getUID(),getFragmentManager(),DoctorInfoFragment.this);
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialogBuilder.create().show();
        });

        btnCancelAppointment.setOnClickListener(v -> {
            MaterialAlertDialogBuilder alertDialogBuilder=new MaterialAlertDialogBuilder(getActivity())
                    .setTitle("Warning")
                    .setMessage("Are You Sure you want to Cancel your Appointment with "+doctorInfo.getName()+"?\nYour Entire Interaction will be lost!")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new CancelCurrentAppointment(getFragmentManager(),DoctorInfoFragment.this,Globals.getCurrentUserUid(),doctorInfo.getUID());

                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialogBuilder.create().show();
        });

        btnViewInteraction.setOnClickListener(v -> {

        });
    }

    @Override
    public void onDoctorAssigned(boolean result) {
        if(result){
            Globals.getCurrentUser().setDoctorAssigned(true);
            Globals.getCurrentUser().setAssignedDoctorUID(doctorInfo.getUID());
            getDialog().dismiss();
            Toast.makeText(getActivity(),"Doctor Assigned Successfully",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(),"Error in Assigning Doctor! Please Try Again",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAppointmentCanceled(boolean result) {
        if(result){
            Globals.getCurrentUser().setDoctorAssigned(false);
            Globals.getCurrentUser().setAssignedDoctorUID(null);
            getDialog().dismiss();
            Toast.makeText(getActivity(),"Appointment Canceled Successfully",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(),"Error in Canceling Appointment",Toast.LENGTH_SHORT).show();
        }
    }
}