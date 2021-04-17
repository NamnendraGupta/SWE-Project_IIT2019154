package com.example.robodoc.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.classes.User;
import com.example.robodoc.firebase.Globals;
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tvName=view.findViewById(R.id.tvUserDetailsName);
        ImageView imgUser=view.findViewById(R.id.imgUserDetails);

        User user= Globals.getCurrentUser();

        tvName.setText(user.getName());
        Picasso.get().load(user.getPhotoURL()).into(imgUser);
    }
}