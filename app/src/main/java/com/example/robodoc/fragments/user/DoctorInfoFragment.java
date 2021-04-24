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
import com.example.robodoc.firebase.realtimeDb.AssignDoctorToUser;
import com.example.robodoc.firebase.realtimeDb.CancelCurrentAppointment;
import com.example.robodoc.fragments.utils.AlertDialogFragment;
import com.example.robodoc.viewModels.user.DoctorListViewModel;
import com.example.robodoc.viewModels.user.UserInfoViewModel;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

public class DoctorInfoFragment extends Fragment implements
        AssignDoctorToUser.AssignDoctorToUserInterface,
        CancelCurrentAppointment.CancelCurrentAppointmentInterface,
        AlertDialogFragment.AlertDialogInterface {

    public DoctorInfoFragment() {
        // Required empty public constructor
    }

    private UserInfo doctorInfo;
    private UserInfoViewModel infoViewModel;

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

    private Button btnRequestForAppointment,btnViewInteraction,btnCancelAppointment;
    private MaterialCardView mcvRequest,mcvInteract,mcvCancel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DoctorListViewModel viewModel=new ViewModelProvider(requireActivity()).get(DoctorListViewModel.class);
        infoViewModel=new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        doctorInfo=viewModel.getDoctorInfo(DoctorInfoFragmentArgs.fromBundle(getArguments()).getListPosition());

        TextView tvName = view.findViewById(R.id.tvDoctorInfoName);
        TextView tvEmail = view.findViewById(R.id.tvDoctorInfoEmail);
        ImageView imgPhoto = view.findViewById(R.id.imgDoctorInfo);
        btnRequestForAppointment = view.findViewById(R.id.btnDoctorInfoRequest);
        btnViewInteraction = view.findViewById(R.id.btnViewInteraction);
        btnCancelAppointment = view.findViewById(R.id.btnCancelAppointment);
        mcvRequest=view.findViewById(R.id.mcvBtnRequestAppointment);
        mcvInteract=view.findViewById(R.id.mcvBtnViewInteraction);
        mcvCancel=view.findViewById(R.id.mcvBtnCancelAppointment);

        tvName.setText(doctorInfo.getName());
        tvEmail.setText(doctorInfo.getEmail());
        Picasso.get().load(doctorInfo.getPhotoUrl()).into(imgPhoto);

        UpdateInterface();

        btnRequestForAppointment.setOnClickListener(v -> {
            AlertDialogFragment alertDialogFragment=AlertDialogFragment.newInstance("RoboDoc Message","Are You Sure you want to Request for Appointment with "+doctorInfo.getName()+"?");
            alertDialogFragment.SetInterface(this,"RequestAppointment");
            alertDialogFragment.show(getParentFragmentManager(),"RequestAppointmentDialog");
        });

        btnCancelAppointment.setOnClickListener(v -> {
            AlertDialogFragment alertDialogFragment=AlertDialogFragment.newInstance("RoboDoc Message","Are You Sure you want to Cancel your Appointment with "+doctorInfo.getName()+"?");
            alertDialogFragment.SetInterface(this,"CancelAppointment");
            alertDialogFragment.show(getParentFragmentManager(),"CancelAppointmentDialog");
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

    private void UpdateInterface(){
        if(infoViewModel.IsDoctorAssigned()){
            btnRequestForAppointment.setVisibility(View.GONE);
            mcvRequest.setVisibility(View.GONE);
            if(doctorInfo.getUID().equals(infoViewModel.GetAssignedDoctorUID())){
                btnViewInteraction.setVisibility(View.VISIBLE);
                mcvInteract.setVisibility(View.VISIBLE);
                btnCancelAppointment.setVisibility(View.VISIBLE);
                mcvCancel.setVisibility(View.VISIBLE);
            }
            else {
                btnViewInteraction.setVisibility(View.GONE);
                mcvInteract.setVisibility(View.GONE);
                btnCancelAppointment.setVisibility(View.GONE);
                mcvCancel.setVisibility(View.GONE);
            }
        }
        else {
            btnRequestForAppointment.setVisibility(View.VISIBLE);
            mcvRequest.setVisibility(View.VISIBLE);
            btnViewInteraction.setVisibility(View.GONE);
            mcvInteract.setVisibility(View.GONE);
            btnCancelAppointment.setVisibility(View.GONE);
            mcvCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDoctorAssigned(boolean result) {
        if(result){
            infoViewModel.setDoctorAssigned(true,doctorInfo.getUID(),doctorInfo.getName());
            Toast.makeText(getActivity(),"Doctor Assigned Successfully",Toast.LENGTH_SHORT).show();
            UpdateInterface();
        }
        else {
            Toast.makeText(getActivity(),"Error in Assigning Doctor! Please Try Again",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAppointmentCanceled(boolean result) {
        if(result){
            infoViewModel.setDoctorAssigned(false,null,null);
            Toast.makeText(getActivity(),"Appointment Canceled Successfully",Toast.LENGTH_SHORT).show();
            UpdateInterface();
        }
        else {
            Toast.makeText(getActivity(),"Error in Canceling Appointment",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPositiveButtonClicked(String type) {
        if(type.equals("RequestAppointment")){
            new AssignDoctorToUser(infoViewModel.getUId(),doctorInfo.getUID(),getParentFragmentManager(),DoctorInfoFragment.this);
        }
        else if(type.equals("CancelAppointment")){
            new CancelCurrentAppointment(getParentFragmentManager(),DoctorInfoFragment.this,infoViewModel.getUId(),doctorInfo.getUID());
        }
    }
}