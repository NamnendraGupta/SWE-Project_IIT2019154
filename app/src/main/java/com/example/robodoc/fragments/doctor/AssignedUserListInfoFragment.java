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
import com.example.robodoc.fragments.utils.ProgressIndicatorFragment;
import com.example.robodoc.utils.DateTimeUtils;
import com.example.robodoc.viewModels.doctor.UserListViewModel;
import com.example.robodoc.viewModels.user.RecordListViewModel;
import com.google.android.material.card.MaterialCardView;
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
        int Position=AssignedUserListInfoFragmentArgs.fromBundle(getArguments()).getListPosition();
        userInfo=viewModel.GetUserInfo(Position);

        AssignedUserAppBarDisplayFragment displayFragment = (AssignedUserAppBarDisplayFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentAppBarDisplay);
        displayFragment.SetUserInfo(userInfo);

        TextView tvName = view.findViewById(R.id.tvAssignedUserName);
        TextView tvEmail = view.findViewById(R.id.tvAssignedUserEmail);
        TextView tvGender = view.findViewById(R.id.tvAssignedUserGender);
        TextView tvNumOfRecords = view.findViewById(R.id.tvAssignedUserNumOfRecords);
        TextView tvLastRecordedTime = view.findViewById(R.id.tvAssignedUserLastRecordedTime);
        Button btnViewStats = view.findViewById(R.id.btnViewAssignStats);
        Button btnViewInteraction = view.findViewById(R.id.btnViewUserInteraction);
        Button btnViewRecords = view.findViewById(R.id.btnViewAssignRecords);
        MaterialCardView mcvViewStats = view.findViewById(R.id.mcvBtnAssignedUserViewStatistics);
        MaterialCardView mcvViewRecords = view.findViewById(R.id.mcvBtnAssignedUserViewRecords);
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
            tvNumOfRecords.setText(Integer.toString(integer));
            if(integer==0){
                btnViewRecords.setVisibility(View.GONE);
                mcvViewRecords.setVisibility(View.GONE);
                btnViewStats.setVisibility(View.GONE);
                mcvViewStats.setVisibility(View.GONE);
            }
            else if(integer==1){
                btnViewRecords.setVisibility(View.VISIBLE);
                mcvViewRecords.setVisibility(View.VISIBLE);
                btnViewStats.setVisibility(View.GONE);
                mcvViewStats.setVisibility(View.GONE);
            }
            else {
                btnViewRecords.setVisibility(View.VISIBLE);
                mcvViewRecords.setVisibility(View.VISIBLE);
                btnViewStats.setVisibility(View.VISIBLE);
                mcvViewStats.setVisibility(View.VISIBLE);
            }
        });

        viewModel1.GetRecordList().observe(getViewLifecycleOwner(),vitalInputs -> {
            if(vitalInputs.size()>0){
                long timeOfInput=vitalInputs.get(vitalInputs.size()-1).getTimeOfInput();
                String displayTime= DateTimeUtils.getDisplayTime(timeOfInput)+", "+DateTimeUtils.getDisplayDate(timeOfInput);
                tvLastRecordedTime.setText(displayTime);
            }
        });
    }
}