package com.example.robodoc.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.utils.DateTimeUtils;
import com.example.robodoc.viewModels.user.RecordListViewModel;
import com.example.robodoc.viewModels.user.UserInfoViewModel;
import com.squareup.picasso.Picasso;

public class UserDetailsFragment extends Fragment {

    public UserDetailsFragment() {
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
        return inflater.inflate(R.layout.fragment_user_details, container, false);
    }

    private TextView tvName, tvEmail, tvUID, tvAssignedDoctorLabel, tvAssignedDoctorName, tvNumRecords, tvLastRecordTime;
    private ImageView imgUser,imgDoctorAssignedCheck;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName=view.findViewById(R.id.tvUserDetailsName);
        tvEmail=view.findViewById(R.id.tvUserDetailsEmail);
        tvUID=view.findViewById(R.id.tvUserDetailsUID);
        tvAssignedDoctorLabel=view.findViewById(R.id.tvUserDetailsAssignedDoctorLabel);
        tvAssignedDoctorName=view.findViewById(R.id.tvUserDetailsAssignedDoctorName);
        tvNumRecords=view.findViewById(R.id.tvUserDetailsNumRecords);
        tvLastRecordTime=view.findViewById(R.id.tvUserDetailsLastRecordTime);

        imgUser=view.findViewById(R.id.imgUserDetails);
        imgDoctorAssignedCheck=view.findViewById(R.id.imgUserDetailsAssignedDoctorCheck);

        UserInfoViewModel infoViewModel=new ViewModelProvider(requireActivity()).get(UserInfoViewModel.class);
        RecordListViewModel recordListViewModel=new ViewModelProvider(requireActivity()).get(RecordListViewModel.class);

        infoViewModel.GetCurrentUser().observe(getViewLifecycleOwner(),user -> {
            if(user!=null){
                String name="Welcome "+user.getName();

                tvName.setText(name);
                tvEmail.setText(user.getEmailID());
                tvUID.setText(user.getUID());
                Picasso.get().load(user.getPhotoURL()).into(imgUser);

                if(user.isDoctorAssigned()){
                    imgDoctorAssignedCheck.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_success));
                    tvAssignedDoctorLabel.setVisibility(View.VISIBLE);
                    tvAssignedDoctorName.setVisibility(View.VISIBLE);
                    tvAssignedDoctorName.setText(user.getAssignedDoctorName());
                }
                else {
                    imgDoctorAssignedCheck.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.ic_error));
                    tvAssignedDoctorLabel.setVisibility(View.GONE);
                    tvAssignedDoctorName.setVisibility(View.GONE);
                }
            }
        });

        recordListViewModel.GetRecordList().observe(getViewLifecycleOwner(), vitalInputs -> {
            tvNumRecords.setText(Integer.toString(vitalInputs.size()));
            if(vitalInputs.size()==0){
                tvLastRecordTime.setText("No Records Yet");
            }
            else {
                Long lastRecordedTime=vitalInputs.get(vitalInputs.size()-1).getTimeOfInput();
                String displayTime= DateTimeUtils.getDisplayTime(lastRecordedTime)+", "+DateTimeUtils.getDisplayDate(lastRecordedTime);
                tvLastRecordTime.setText(displayTime);
            }
        });

    }
}