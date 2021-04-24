package com.example.robodoc.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.adapters.DoctorListAdapter;
import com.example.robodoc.fragments.utils.ProgressIndicatorFragment;
import com.example.robodoc.viewModels.user.DoctorListViewModel;
import com.google.android.material.card.MaterialCardView;

public class DoctorListFragment extends Fragment {

    public DoctorListFragment() {
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
        return inflater.inflate(R.layout.fragment_doctor_list, container, false);
    }

    private RecyclerView rcvDoctors;
    private TextView tvNoUserDisplay;
    private MaterialCardView mcvDoctors;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvDoctors=view.findViewById(R.id.rcvDoctorsList);
        tvNoUserDisplay=view.findViewById(R.id.tvDoctorNoUserDisplay);
        mcvDoctors=view.findViewById(R.id.mcvDoctorsList);

        rcvDoctors.setLayoutManager(new LinearLayoutManager(getContext()));

        DoctorListViewModel viewModel=new ViewModelProvider(requireActivity()).get(DoctorListViewModel.class);

        ProgressIndicatorFragment progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Loading List of Available Doctors");

        viewModel.CheckDoctorListLoading().observe(getViewLifecycleOwner(),aBoolean -> {
            if(aBoolean)
                progressIndicatorFragment.show(getParentFragmentManager(),"LoadingDoctorsList");
            else {
                if(progressIndicatorFragment.isVisible())
                    progressIndicatorFragment.dismiss();
            }
        });

        viewModel.GetDoctorsList().observe(getViewLifecycleOwner(),userInfoArrayList -> {
            if(userInfoArrayList.size()==0){
                tvNoUserDisplay.setVisibility(View.VISIBLE);
                rcvDoctors.setVisibility(View.GONE);
                mcvDoctors.setVisibility(View.GONE);
            }
            else {
                tvNoUserDisplay.setVisibility(View.GONE);
                rcvDoctors.setVisibility(View.VISIBLE);
                mcvDoctors.setVisibility(View.VISIBLE);
                rcvDoctors.setAdapter(new DoctorListAdapter(userInfoArrayList, Navigation.findNavController(view)));
            }
        });
    }
}