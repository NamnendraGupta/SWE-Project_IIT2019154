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

import com.example.robodoc.R;
import com.example.robodoc.adapters.RecordListAdapter;
import com.example.robodoc.classes.VitalInput;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordsFragment extends Fragment {

    public RecordsFragment() {
        // Required empty public constructor
    }

    public static RecordsFragment newInstance() {
        RecordsFragment fragment = new RecordsFragment();
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
        return inflater.inflate(R.layout.fragment_records, container, false);
    }

    private RecyclerView rcvRecords;
    private RecordListAdapter recordListAdapter;
    private TextView tvNoRecordsDisplay;
    ArrayList<VitalInput> vitalInputsList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vitalInputsList=new ArrayList<>();
        rcvRecords=view.findViewById(R.id.rcvRecords);
        tvNoRecordsDisplay=view.findViewById(R.id.tvNoRecordDisplay);
        recordListAdapter=new RecordListAdapter(getFragmentManager(),vitalInputsList);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rcvRecords.setLayoutManager(layoutManager);
        rcvRecords.setAdapter(recordListAdapter);
    }

    public void ShowList(ArrayList<VitalInput> list){
        vitalInputsList=list;
        if(vitalInputsList.size()==0){
            rcvRecords.setVisibility(View.GONE);
            tvNoRecordsDisplay.setVisibility(View.VISIBLE);
        }
        else {
            rcvRecords.setVisibility(View.VISIBLE);
            tvNoRecordsDisplay.setVisibility(View.GONE);
            recordListAdapter=new RecordListAdapter(getFragmentManager(),vitalInputsList);
            rcvRecords.setAdapter(recordListAdapter);
            recordListAdapter.notifyDataSetChanged();
        }
    }

    public void addRecord(VitalInput newRecord){
        vitalInputsList.add(newRecord);
        recordListAdapter=new RecordListAdapter(getFragmentManager(),vitalInputsList);
        rcvRecords.setAdapter(recordListAdapter);
        recordListAdapter.notifyDataSetChanged();
    }

    public void HideList(){
        vitalInputsList=new ArrayList<>();
        recordListAdapter.notifyDataSetChanged();
    }
}