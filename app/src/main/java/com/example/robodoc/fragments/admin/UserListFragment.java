package com.example.robodoc.fragments.admin;

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
import com.example.robodoc.adapters.AdminUserListAdapter;
import com.example.robodoc.fragments.utils.ProgressIndicatorFragment;
import com.example.robodoc.viewModels.admin.UserListViewModel;

public class UserListFragment extends Fragment {

    private RecyclerView rcvUsers;
    private TextView tvNoUserDisplay;

    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvNoUserDisplay=view.findViewById(R.id.tvNoUserDisplay);
        rcvUsers=view.findViewById(R.id.rcvUsers);
        rcvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

        UserListViewModel viewModel=new ViewModelProvider(requireActivity()).get(UserListViewModel.class);

        ProgressIndicatorFragment progressIndicatorFragment=ProgressIndicatorFragment.newInstance("Syncing with Server","Loading List of Users");

        viewModel.CheckUserListLoading().observe(getViewLifecycleOwner(),aBoolean -> {
            if(aBoolean)
                progressIndicatorFragment.show(getParentFragmentManager(),"LoadingUsersList");
            else {
                if(progressIndicatorFragment.isVisible())
                    progressIndicatorFragment.dismiss();
            }
        });

        viewModel.getUsersList().observe(getViewLifecycleOwner(),userInfoArrayList -> {
            if(userInfoArrayList.size()==0){
                tvNoUserDisplay.setVisibility(View.VISIBLE);
                rcvUsers.setVisibility(View.GONE);
            }
            else {
                tvNoUserDisplay.setVisibility(View.GONE);
                rcvUsers.setVisibility(View.VISIBLE);
                rcvUsers.setAdapter(new AdminUserListAdapter(userInfoArrayList, Navigation.findNavController(view)));
            }
        });
    }
}