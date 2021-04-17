package com.example.robodoc.fragments.doctor;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.fragments.shared.ProgressIndicatorFragment;
import com.example.robodoc.viewModels.doctor.UserListViewModel;
import com.example.robodoc.viewModels.user.RecordListViewModel;
import com.squareup.picasso.Picasso;

public class AssignedUserListInfoFragment extends Fragment {

    public AssignedUserListInfoFragment() {
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
        return inflater.inflate(R.layout.fragment_assigned_user_list_info, container, false);
    }

    private UserInfo userInfo;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserListViewModel viewModel=new ViewModelProvider(requireActivity()).get(UserListViewModel.class);
        userInfo=viewModel.GetUserInfo(AssignedUserListInfoFragmentArgs.fromBundle(getArguments()).getListPosition());

        TextView tvName = view.findViewById(R.id.tvAssignedUserName);
        TextView tvEmail = view.findViewById(R.id.tvAssignedUserEmail);
        TextView tvGender = view.findViewById(R.id.tvAssignedUserGender);
        Button btnViewStats = view.findViewById(R.id.btnViewAssignStats);
        Button btnViewInteraction = view.findViewById(R.id.btnViewUserInteraction);
        Button btnViewRecords = view.findViewById(R.id.btnViewAssignRecords);
        ImageView imgUser = view.findViewById(R.id.imgAssignedUser);

        tvName.setText(userInfo.getName());
        tvEmail.setText(userInfo.getEmail());
        tvGender.setText(userInfo.getGender().toString());
        Picasso.get().load(userInfo.getPhotoUrl()).into(imgUser);

        NavController navController= Navigation.findNavController(requireActivity(),R.id.navHostDoctor);

        btnViewStats.setOnClickListener(v -> navController.navigate(R.id.userStatsFragment));

        btnViewInteraction.setOnClickListener(v -> {
            NavDirections action=AssignedUserListInfoFragmentDirections.ActionDoctorToInteraction(
                    true,
                    userInfo.getUID(),
                    userInfo.getName()
            );
            navController.navigate(action);
        });

        btnViewRecords.setOnClickListener(v -> {
            NavDirections action=AssignedUserListInfoFragmentDirections.actionAssignedUserListInfoFragmentToRecordsFragment();
            navController.navigate(action);
        });

        RecordListViewModel viewModel1=new ViewModelProvider(requireActivity()).get(RecordListViewModel.class);
        viewModel1.setUserUId(userInfo.getUID());

        ProgressIndicatorFragment progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Fetching Records of "+userInfo.getName());

        viewModel1.CheckIsListLoading().observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean){
                progressIndicatorFragment.show(getParentFragmentManager(),"FetchingUserRecords");
            }
            else {
                if(progressIndicatorFragment.isVisible())
                    progressIndicatorFragment.dismiss();
            }
        });

        viewModel1.GetListSize().observe(getViewLifecycleOwner(), integer -> {
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