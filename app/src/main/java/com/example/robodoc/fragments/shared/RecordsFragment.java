package com.example.robodoc.fragments.shared;

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
import com.example.robodoc.adapters.RecordListAdapter;
import com.example.robodoc.viewModels.user.RecordListViewModel;

public class RecordsFragment extends Fragment {

    public RecordsFragment() {
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
        return inflater.inflate(R.layout.fragment_records, container, false);
    }

    private RecyclerView rcvRecords;
    private RecordListAdapter recordListAdapter;
    private TextView tvNoRecordsDisplay;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvRecords=view.findViewById(R.id.rcvRecords);
        tvNoRecordsDisplay=view.findViewById(R.id.tvNoRecordDisplay);

        rcvRecords.setLayoutManager(new LinearLayoutManager(requireActivity()));

        RecordListViewModel viewModel=new ViewModelProvider(requireActivity()).get(RecordListViewModel.class);

        viewModel.GetRecordList().observe(getViewLifecycleOwner(), vitalInputs -> {
            recordListAdapter=new RecordListAdapter(vitalInputs, Navigation.findNavController(view));
            rcvRecords.setAdapter(recordListAdapter);
            recordListAdapter.notifyDataSetChanged();
        });

        viewModel.GetListSize().observe(getViewLifecycleOwner(), integer -> {
            if(integer==0){
                rcvRecords.setVisibility(View.GONE);
                tvNoRecordsDisplay.setVisibility(View.VISIBLE);
            }
            else {
                rcvRecords.setVisibility(View.VISIBLE);
                tvNoRecordsDisplay.setVisibility(View.GONE);
            }
        });
    }
}