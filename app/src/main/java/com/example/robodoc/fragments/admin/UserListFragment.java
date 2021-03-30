package com.example.robodoc.fragments.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.adapters.UserListAdapter;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.firebase.Globals;
import com.example.robodoc.firebase.firestore.GetUsersList;

import java.util.ArrayList;

public class UserListFragment extends Fragment implements GetUsersList.GetUsersListInterface {

    RecyclerView rcvUsers;
    RecyclerView.Adapter adapter;
    TextView tvNoUserDisplay;
    ArrayList<UserInfo> userList;

    public UserListFragment() {
        // Required empty public constructor
    }

    public static UserListFragment newInstance(String param1, String param2) {
        return new UserListFragment();
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
        rcvUsers=view.findViewById(R.id.rcvUsers);
        tvNoUserDisplay=view.findViewById(R.id.tvNoUserDisplay);
        tvNoUserDisplay.setVisibility(View.GONE);

        rcvUsers.setLayoutManager(new LinearLayoutManager(getActivity()));

        userList=new ArrayList<>();
        adapter=new UserListAdapter(userList,getFragmentManager());
        rcvUsers.setAdapter(adapter);

        new GetUsersList(getFragmentManager(),UserListFragment.this);
    }

    @Override
    public void getUsersList(boolean result, ArrayList<UserInfo> UserList) {
        if(result){
            Log.d("USERS LIST FRAGMENT","List Size is "+UserList.size());
            if(UserList.size()>0){
                userList=UserList;
                adapter=new UserListAdapter(userList,getFragmentManager());
                rcvUsers.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            else {
                tvNoUserDisplay.setVisibility(View.VISIBLE);
                rcvUsers.setVisibility(View.GONE);
            }
        }
    }
}