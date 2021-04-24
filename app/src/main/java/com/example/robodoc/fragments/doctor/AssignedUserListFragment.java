package com.example.robodoc.fragments.doctor;

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
import com.example.robodoc.adapters.AssignedUserListAdapter;
import com.example.robodoc.fragments.utils.ProgressIndicatorFragment;
import com.example.robodoc.viewModels.doctor.UserListViewModel;
import com.google.android.material.card.MaterialCardView;

public class AssignedUserListFragment extends Fragment {

    public AssignedUserListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private TextView tvNoUserDisplay;
    private RecyclerView rcvUsersList;
    private MaterialCardView mcvUsersList;
    private ProgressIndicatorFragment progressIndicatorFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assigned_user_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Fetching User's List");

        tvNoUserDisplay=view.findViewById(R.id.tvAssignedUserListNoDisplay);
        rcvUsersList=view.findViewById(R.id.rcvAssignedUsersList);
        mcvUsersList=view.findViewById(R.id.mcvAssignedUsersList);
        rcvUsersList.setLayoutManager(new LinearLayoutManager(getActivity()));

        UserListViewModel viewModel=new ViewModelProvider(requireActivity()).get(UserListViewModel.class);

        viewModel.getUsersList().observe(getViewLifecycleOwner(), userInfoArrayList -> {
            if(userInfoArrayList.isEmpty()){
                rcvUsersList.setVisibility(View.GONE);
                mcvUsersList.setVisibility(View.GONE);
                tvNoUserDisplay.setVisibility(View.VISIBLE);
            }
            else {
                rcvUsersList.setVisibility(View.VISIBLE);
                mcvUsersList.setVisibility(View.VISIBLE);
                tvNoUserDisplay.setVisibility(View.GONE);
                rcvUsersList.setAdapter(new AssignedUserListAdapter(userInfoArrayList, Navigation.findNavController(view)));
            }
        });

        viewModel.CheckUserListLoading().observe(getViewLifecycleOwner(), aBoolean -> {
            if(aBoolean)
                progressIndicatorFragment.show(getParentFragmentManager(),"Fetching Users List");
            else{
                if(progressIndicatorFragment.isVisible())
                    progressIndicatorFragment.dismiss();
            }
        });
    }
}