package com.example.robodoc.fragments.startup;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.robodoc.R;
import com.example.robodoc.activities.MainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SplashScreenFragment extends Fragment {

    public SplashScreenFragment() {
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
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Handler().postDelayed(this::Navigate,1000);
    }

    private void Navigate(){
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(requireActivity(), MainActivity.class));
            requireActivity().finish();
        }
        else {
            Navigation.findNavController(requireActivity(),R.id.navHostStartup).navigate(R.id.loginFragment);
        }
    }

}