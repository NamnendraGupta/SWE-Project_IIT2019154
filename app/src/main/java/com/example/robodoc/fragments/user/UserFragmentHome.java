package com.example.robodoc.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.robodoc.R;
import com.example.robodoc.viewModels.user.RecordListViewModel;
import com.google.android.material.card.MaterialCardView;

public class UserFragmentHome extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    private Button btnViewStats, btnViewRecords;
    private MaterialCardView cvViewStats, cvViewRecords;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnChooseInput = view.findViewById(R.id.btnChooseInput);
        Button btnViewDoctors = view.findViewById(R.id.btnViewDoctors);
        btnViewStats=view.findViewById(R.id.btnViewStats);
        btnViewRecords=view.findViewById(R.id.btnViewRecords);
        Button btnSetReminder = view.findViewById(R.id.btnSetAlarm);

        cvViewStats=view.findViewById(R.id.cvbViewStats);
        cvViewRecords=view.findViewById(R.id.cvbViewRecords);

        NavController navController= Navigation.findNavController(view);

        btnSetReminder.setOnClickListener(v -> {
            NavDirections action=UserFragmentHomeDirections.ActionMainToAlarm();
            navController.navigate(action);
        });

        btnChooseInput.setOnClickListener(v -> {
            NavDirections action=UserFragmentHomeDirections.actionMainToInput();
            navController.navigate(action);
        });

        btnViewRecords.setOnClickListener(v -> {
            NavDirections action=UserFragmentHomeDirections.ActionMainToRecords();
            navController.navigate(action);
        });

        btnViewStats.setOnClickListener(v -> {
            NavDirections action=UserFragmentHomeDirections.ActionMainToStats();
            navController.navigate(action);
        });

        btnViewDoctors.setOnClickListener(v -> {
            NavDirections action=UserFragmentHomeDirections.ActionMainToDoctorList();
            navController.navigate(action);
        });

        RecordListViewModel viewModel=new ViewModelProvider(requireActivity()).get(RecordListViewModel.class);

        viewModel.GetListSize().observe(getViewLifecycleOwner(), integer -> {
            if(integer==0){
                btnViewRecords.setVisibility(View.GONE);
                cvViewRecords.setVisibility(View.GONE);
                btnViewStats.setVisibility(View.GONE);
                cvViewStats.setVisibility(View.GONE);
            }
            else if(integer==1){
                btnViewRecords.setVisibility(View.VISIBLE);
                cvViewRecords.setVisibility(View.VISIBLE);
                btnViewStats.setVisibility(View.GONE);
                cvViewStats.setVisibility(View.GONE);
            }
            else {
                btnViewRecords.setVisibility(View.VISIBLE);
                cvViewRecords.setVisibility(View.VISIBLE);
                btnViewStats.setVisibility(View.VISIBLE);
                cvViewStats.setVisibility(View.VISIBLE);
            }
        });

    }
}