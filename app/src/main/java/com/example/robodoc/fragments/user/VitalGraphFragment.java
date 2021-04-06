package com.example.robodoc.fragments.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.robodoc.R;
import com.example.robodoc.activities.MainActivity;
import com.example.robodoc.activities.UserStatsActivity;
import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.enums.VitalInputType;
import com.example.robodoc.utils.CustomXAxisRenderer;
import com.example.robodoc.utils.GetRandomHeartRate;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VitalGraphFragment extends Fragment {

    public VitalGraphFragment() {
        // Required empty public constructor
    }

    public VitalGraphFragment(VitalInputType inputType){
        Bundle args = new Bundle();
        args.putString("TYPE",inputType.toString());
        setArguments(args);
    }

    public static VitalGraphFragment newInstance(VitalInputType inputType) {
        VitalGraphFragment fragment = new VitalGraphFragment();
        Bundle args = new Bundle();
        args.putString("TYPE",inputType.toString());
        fragment.setArguments(args);
        return fragment;
    }


    VitalInputType inputType;
    ArrayList<VitalInput> arrayList;
    LineChart chart;

    TextView tvAverage, tvMaximum, tvMinimum, tvInRange, tvAboveRange, tvBelowRange, tvNormalRange;
    Button btnClose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            inputType=VitalInputType.valueOf(getArguments().getString("TYPE"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vital_graph, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrayList = ((UserStatsActivity)getActivity()).getVitalInputList();
        chart =  view.findViewById(R.id.chart);

        tvNormalRange=view.findViewById(R.id.tvGraphNormalRange);
        tvAverage=view.findViewById(R.id.tvGraphAverage);
        tvMaximum=view.findViewById(R.id.tvGraphMaximum);
        tvMinimum=view.findViewById(R.id.tvGraphMinimum);
        tvInRange=view.findViewById(R.id.tvGraphNormal);
        tvAboveRange=view.findViewById(R.id.tvGraphAbove);
        tvBelowRange=view.findViewById(R.id.tvGraphBelow);
        btnClose=view.findViewById(R.id.btnCloseGraphActivity);

        int a=5,b=6;
        String dataStr="("+a+","+b+")";
        tvNormalRange.setText(dataStr);

        btnClose.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        chart.setXAxisRenderer(new CustomXAxisRenderer(chart.getViewPortHandler(),chart.getXAxis(),chart.getTransformer(YAxis.AxisDependency.LEFT)));
        chart.getXAxis().setYOffset(10);
        chart.getDescription().setEnabled(false);
        chart.setExtraTopOffset(5);

        List<Entry> entries = new ArrayList<Entry>();
        final String[] quarters;
        quarters = new String[arrayList.size()];
        for(int i=0;i<arrayList.size();i++)
        {
            long t=arrayList.get(i).getTimeOfInput();
            DateFormat simple = new SimpleDateFormat("dd/MMM\nHH:mm");
            Date result = new Date(t);
            quarters[i]=simple.format(result);
        }

        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return quarters[(int) value];
            }
        };

        switch (inputType)
        {
            case HEART_RATE:
                for(int i=0;i<arrayList.size();i++) {
                    int h=arrayList.get(i).getHeartRate();
                    entries.add(new Entry(i, h));
                }
                XAxis xAxis = chart.getXAxis();
                xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
                xAxis.setValueFormatter(formatter);

                LineDataSet dataSet1 = new LineDataSet(entries, "HEART RATE");
                LineData lineData = new LineData(dataSet1);
                chart.setData(lineData);
                chart.invalidate();

                dataSet1.setColor(Color.RED);
                dataSet1.setCircleColor(Color.YELLOW);
                dataSet1.setLineWidth(3f);

                break;

            case OXYGEN_LEVEL:
                for (int i = 0; i < arrayList.size(); i++) {
                    int O = arrayList.get(i).getOxygenLevel();
                    entries.add(new Entry(i, O));
                }
                XAxis xAxis1 = chart.getXAxis();
                xAxis1.setGranularity(1f); // minimum axis-step (interval) is 1
                xAxis1.setValueFormatter(formatter);

                LineDataSet dataSet2 = new LineDataSet(entries, "OXYGEN LEVEL");
                LineData lineData1 = new LineData(dataSet2);
                chart.setData(lineData1);
                chart.invalidate();

                dataSet2.setColor(Color.RED);
                dataSet2.setCircleColor(Color.YELLOW);
                dataSet2.setLineWidth(3f);

                break;



            case GLUCOSE_LEVEL:
                for(int i=0;i<arrayList.size();i++) {
                    int O=arrayList.get(i).getOxygenLevel();
                    entries.add(new Entry(i, O));
                }

                XAxis xAxis2 = chart.getXAxis();
                xAxis2.setGranularity(1f); // minimum axis-step (interval) is 1
                xAxis2.setValueFormatter(formatter);

                LineDataSet dataSet3 = new LineDataSet(entries, "GLUCOSE LEVEL");
                LineData lineData2 = new LineData(dataSet3);
                chart.setData(lineData2);
                chart.invalidate();

                dataSet3.setColor(Color.RED);
                dataSet3.setCircleColor(Color.YELLOW);
                dataSet3.setLineWidth(3f);

                break;


            case BLOOD_PRESSURE:
                List<Entry> entries1 = new ArrayList<Entry>();
                for(int i=0;i<arrayList.size();i++) {
                    int H=arrayList.get(i).getHighBP();
                    int L=arrayList.get(i).getLowBP();
                    entries.add(new Entry(i, H));
                    entries1.add(new Entry(i,L));
                }

                XAxis xAxis3 = chart.getXAxis();
                xAxis3.setGranularity(1f); // minimum axis-step (interval) is 1
                xAxis3.setValueFormatter(formatter);

                LineDataSet dataSet6 = new LineDataSet(entries, "High BP");
                LineDataSet dataSet7 = new LineDataSet(entries1,"Low BP");
                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(dataSet6);
                dataSets.add(dataSet7);
                LineData data = new LineData(dataSets);
                chart.setData(data);
                chart.invalidate();

                dataSet6.setColor(Color.RED);
                dataSet6.setCircleColor(Color.YELLOW);
                dataSet6.setLineWidth(3f);

                dataSet7.setColor(Color.BLUE);
                dataSet7.setCircleColor(Color.RED);
                dataSet7.setLineWidth(3f);

                break;


            case BODY_TEMPERATURE:
                for(int i=0;i<arrayList.size();i++) {
                    float bt=arrayList.get(i).getBodyTemperature();
                    entries.add(new Entry(i, bt));
                }

                XAxis xAxis4 = chart.getXAxis();
                xAxis4.setGranularity(1f); // minimum axis-step (interval) is 1
                xAxis4.setValueFormatter(formatter);

                LineDataSet dataSet = new LineDataSet(entries, "BODY TEMPERATURE");
                LineData lineData4 = new LineData(dataSet);
                chart.setData(lineData4);
                chart.invalidate();

                dataSet.setColor(Color.RED);
                dataSet.setCircleColor(Color.YELLOW);
                dataSet.setLineWidth(3f);

                break;

        }

    }

}