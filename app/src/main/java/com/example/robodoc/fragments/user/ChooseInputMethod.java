package com.example.robodoc.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.robodoc.R;

public class ChooseInputMethod extends Fragment {

    public ChooseInputMethod() {
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
        return inflater.inflate(R.layout.fragment_choose_input_method, container, false);
    }

    Button btnManualInput, btnRandomInput;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnManualInput=view.findViewById(R.id.btnManualInput);
        btnRandomInput=view.findViewById(R.id.btnAutomaticGenerate);

        NavController navController= Navigation.findNavController(requireActivity(),R.id.navHostMain);

        btnManualInput.setOnClickListener(v -> {
            NavDirections action=ChooseInputMethodDirections.ActionInputManul();
            navController.navigate(action);
        });

        btnRandomInput.setOnClickListener(v -> {
            NavDirections action=ChooseInputMethodDirections.ActionInputRandom();
            navController.navigate(action);
        });

    }
}