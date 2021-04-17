package com.example.robodoc.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

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
import com.example.robodoc.viewModels.user.DoctorListViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

public class DoctorInfoFragment extends Fragment implements AssignDoctorToUser.AssignDoctorToUserInterface, CancelCurrentAppointment.CancelCurrentAppointmentInterface {

    public DoctorInfoFragment() {
        // Required empty public constructor
    }

    private UserInfo doctorInfo;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DoctorListViewModel viewModel=new ViewModelProvider(requireActivity()).get(DoctorListViewModel.class);
        doctorInfo=viewModel.getDoctorInfo(DoctorInfoFragmentArgs.fromBundle(getArguments()).getListPosition());

        TextView tvName = view.findViewById(R.id.tvDoctorInfoName);
        TextView tvEmail = view.findViewById(R.id.tvDoctorInfoEmail);
        ImageView imgPhoto = view.findViewById(R.id.imgDoctorInfo);
        Button btnRequestForAppointment = view.findViewById(R.id.btnDoctorInfoRequest);
        Button btnViewInteraction = view.findViewById(R.id.btnViewInteraction);
        Button btnCancelAppointment = view.findViewById(R.id.btnCancelAppointment);

        tvName.setText(doctorInfo.getName());
        tvEmail.setText(doctorInfo.getEmail());
        Picasso.get().load(doctorInfo.getPhotoUrl()).into(imgPhoto);

        if(Globals.getCurrentUser().isDoctorAssigned()){
            btnRequestForAppointment.setVisibility(View.GONE);
            if(doctorInfo.getUID().equals(Globals.getCurrentUser().getAssignedDoctorUID())){
                btnViewInteraction.setVisibility(View.VISIBLE);
                btnCancelAppointment.setVisibility(View.VISIBLE);
            }
            else {
                btnViewInteraction.setVisibility(View.GONE);
                btnCancelAppointment.setVisibility(View.GONE);
            }
        }
        else {
            btnRequestForAppointment.setVisibility(View.VISIBLE);
            btnViewInteraction.setVisibility(View.GONE);
            btnCancelAppointment.setVisibility(View.GONE);
        }

        btnRequestForAppointment.setOnClickListener(v -> {
            MaterialAlertDialogBuilder alertDialogBuilder=new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Message")
                    .setMessage("Are You Sure you want to Request for Appointment with "+doctorInfo.getName()+"?")
                    .setPositiveButton("YES", (dialog, which) -> new AssignDoctorToUser(Globals.getCurrentUserUid(),doctorInfo.getUID(),getParentFragmentManager(),DoctorInfoFragment.this))
                    .setNegativeButton("NO", (dialog, which) -> dialog.cancel());
            alertDialogBuilder.create().show();
        });

        btnCancelAppointment.setOnClickListener(v -> {
            MaterialAlertDialogBuilder alertDialogBuilder=new MaterialAlertDialogBuilder(requireActivity())
                    .setTitle("Warning")
                    .setMessage("Are You Sure you want to Cancel your Appointment with "+doctorInfo.getName()+"?\nYour Entire Interaction will be lost!")
                    .setPositiveButton("YES", (dialog, which) -> new CancelCurrentAppointment(getParentFragmentManager(),DoctorInfoFragment.this,Globals.getCurrentUserUid(),doctorInfo.getUID()))
                    .setNegativeButton("NO", (dialog, which) -> dialog.cancel());
            alertDialogBuilder.create().show();
        });

        btnViewInteraction.setOnClickListener(v -> {
            NavDirections action=DoctorInfoFragmentDirections.ActionUserToInteraction(
                    false,
                    doctorInfo.getUID(),
                    doctorInfo.getName()
            );
            Navigation.findNavController(view).navigate(action);
        });
    }

    @Override
    public void onDoctorAssigned(boolean result) {
        if(result){
            Globals.getCurrentUser().setDoctorAssigned(true);
            Globals.getCurrentUser().setAssignedDoctorUID(doctorInfo.getUID());
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
            Toast.makeText(getActivity(),"Appointment Canceled Successfully",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(),"Error in Canceling Appointment",Toast.LENGTH_SHORT).show();
        }
    }
}