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
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.fragments.shared.ProgressIndicatorFragment;
import com.example.robodoc.viewModels.user.RecordListViewModel;

public class UserFragmentHome extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_home, container, false);
    }

    private Button btnViewStats;
    private Button btnViewRecords;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnChooseInput = view.findViewById(R.id.btnChooseInput);
        Button btnViewDoctors = view.findViewById(R.id.btnViewDoctors);
        btnViewStats=view.findViewById(R.id.btnViewStats);
        btnViewRecords=view.findViewById(R.id.btnViewRecords);
        Button btnSetReminder = view.findViewById(R.id.btnSetAlarm);

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

        ProgressIndicatorFragment progressIndicatorFragment= ProgressIndicatorFragment.newInstance("Syncing with Server","Loading Records");

        RecordListViewModel viewModel=new ViewModelProvider(requireActivity()).get(RecordListViewModel.class);
        viewModel.setUserUId(Globals.getCurrentUserUid());

        viewModel.CheckIsListLoading().observe(getViewLifecycleOwner(),aBoolean -> {
            if(aBoolean){
                progressIndicatorFragment.show(getParentFragmentManager(),"LoadingRecords");
            }
            else {
                if(progressIndicatorFragment.isVisible())
                    progressIndicatorFragment.dismiss();
            }
        });

        viewModel.GetListSize().observe(getViewLifecycleOwner(), integer -> {
            if(integer==0){
                btnViewRecords.setVisibility(View.GONE);
                btnViewStats.setVisibility(View.GONE);
            }
            else if(integer==1){
                btnViewRecords.setVisibility(View.VISIBLE);
                btnViewStats.setVisibility(View.GONE);
            }
            else {
                btnViewRecords.setVisibility(View.VISIBLE);
                btnViewStats.setVisibility(View.VISIBLE);
            }
        });

    }
}