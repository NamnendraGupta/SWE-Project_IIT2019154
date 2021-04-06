package com.example.robodoc.fragments.doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robodoc.R;
import com.example.robodoc.adapters.AssignedUserListAdapter;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.firestore.GetAssignedUsersList;

import java.util.ArrayList;

public class AssignedUserListFragment extends Fragment implements GetAssignedUsersList.GetAssignedUsersListInterface {

    public AssignedUserListFragment() {
        // Required empty public constructor
    }

    public static AssignedUserListFragment newInstance() {
        AssignedUserListFragment fragment = new AssignedUserListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    TextView tvNoUserDisplay;
    RecyclerView rcvUsersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assigned_user_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvNoUserDisplay=view.findViewById(R.id.tvAssignedUserListNoDisplay);
        rcvUsersList=view.findViewById(R.id.rcvAssignedUsersList);
        rcvUsersList.setLayoutManager(new LinearLayoutManager(getActivity()));

        new GetAssignedUsersList(getChildFragmentManager(),this, Globals.getCurrentUserUid());
    }

    @Override
    public void userListInterface(boolean result, ArrayList<UserInfo> userInfoList) {
        if(result){
            if(userInfoList.isEmpty()){
                rcvUsersList.setVisibility(View.GONE);
                tvNoUserDisplay.setVisibility(View.VISIBLE);
            }
            else {
                rcvUsersList.setVisibility(View.VISIBLE);
                tvNoUserDisplay.setVisibility(View.GONE);
                rcvUsersList.setAdapter(new AssignedUserListAdapter(userInfoList,getFragmentManager()));
            }
        }
        else {
            rcvUsersList.setVisibility(View.GONE);
            tvNoUserDisplay.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(),"Error in Fetching List!",Toast.LENGTH_SHORT).show();
        }
    }
}