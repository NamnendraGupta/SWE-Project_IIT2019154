package com.example.robodoc.fragments.user;

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
import com.example.robodoc.adapters.DoctorListAdapter;
import com.example.robodoc.adapters.UserListAdapter;
import com.example.robodoc.classes.UserInfo;
import com.example.robodoc.firebase.firestore.GetDoctorsList;

import java.util.ArrayList;

public class DoctorListFragment extends Fragment implements GetDoctorsList.GetDoctorsListInterface {

    public DoctorListFragment() {
        // Required empty public constructor
    }

    public static DoctorListFragment newInstance() {
        DoctorListFragment fragment = new DoctorListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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

    RecyclerView rcvDoctors;
    TextView tvNoUserDisplay;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvDoctors=view.findViewById(R.id.rcvDoctorsList);
        tvNoUserDisplay=view.findViewById(R.id.tvDoctorNoUserDisplay);
        rcvDoctors.setLayoutManager(new LinearLayoutManager(getContext()));

        new GetDoctorsList(getFragmentManager(),this);
    }

    @Override
    public void getDoctorsList(boolean result, ArrayList<UserInfo> userList) {
        if(result){
            if(userList.size()>0){
                rcvDoctors.setVisibility(View.VISIBLE);
                tvNoUserDisplay.setVisibility(View.GONE);
                rcvDoctors.setAdapter(new DoctorListAdapter(userList,getFragmentManager()));
            }
            else {
                rcvDoctors.setVisibility(View.GONE);
                tvNoUserDisplay.setVisibility(View.VISIBLE);
            }
        }
        else {
            Toast.makeText(getContext(),"Error in Fetching Doctor's List! Please Try Again",Toast.LENGTH_SHORT).show();
        }
    }
}