package com.example.robodoc.fragments.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.robodoc.R;
import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChooseInputMethod#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseInputMethod extends Fragment {

    public ChooseInputMethod() {
        // Required empty public constructor
    }

    public static ChooseInputMethod newInstance() {
        ChooseInputMethod fragment = new ChooseInputMethod();
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
        return inflater.inflate(R.layout.fragment_choose_input_method, container, false);
    }

    Button btnManualInput, btnRandomInput;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnManualInput=view.findViewById(R.id.btnManualInput);
        btnRandomInput=view.findViewById(R.id.btnAutomaticGenerate);

        btnManualInput.setOnClickListener(v -> ManualInput.newInstance().show(getFragmentManager(),"MANUAL INPUT"));

        btnRandomInput.setOnClickListener(v -> RandomInput.newInstance().show(getFragmentManager(),"RANDOM INPUT"));

    }
}