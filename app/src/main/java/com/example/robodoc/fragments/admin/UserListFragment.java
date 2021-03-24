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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserListFragment extends Fragment implements GetUsersList.GetUsersListInterface {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView rcvUsers;
    RecyclerView.Adapter adapter;
    TextView tvNoUserDisplay;
    ArrayList<UserInfo> userList;

    public UserListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserListFragment.
     */
    public static UserListFragment newInstance(String param1, String param2) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        adapter=new UserListAdapter(getActivity(),userList);
        rcvUsers.setAdapter(adapter);

        new GetUsersList(getFragmentManager(),UserListFragment.this);
    }

    @Override
    public void getUsersList(boolean result, ArrayList<UserInfo> UserList) {
        if(result){
            Log.d("USERS LIST FRAGMENT","List Size is "+UserList.size());
            if(UserList.size()>0){
                userList=UserList;
                adapter=new UserListAdapter(getActivity(),userList);
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