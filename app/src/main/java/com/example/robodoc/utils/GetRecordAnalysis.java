package com.example.robodoc.utils;

import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.enums.VitalSignsKey;

import java.util.ArrayList;

public class GetRecordAnalysis {
    private Arraylist<VitalInput> recordList;
    public GetRecordAnalysis(ArrayList<VitalInput> recordList)
    {
        this.recordList=recordList;
        float AvgBT=0;
        int AvgLBP=0;
        int AvgHBP=0;
        int AvgOL=0;
        int AvgHR=0;
        int AvgGL=0;
        int n=recordList.size();
        for(int i=0;i<recordList.size();i++)
        {
            VitalInput record = recordList.get(i);
            AvgBT+=record.getBodyTemperature();
            AvgLBP+=record.getLowBP();
            AvgHBP+=record.getHighBP();
            AvgOL+=record.getOxygenLevel();
            AvgHR+=record.getHeartRate();
            AvgGL+=record.getGlucoseLevel();
        }
        AvgBT/=n;
        AvgLBP/=n;
        AvgHBP/=n;
        AvgOL/=n;
        AvgHR/=n;
        AvgGL/=n;
        public static float AverageBT()
        {
            return AvgBT;
        }
        public static int AverageLBP()
        {
            return AvgLBP;
        }
        public static int AverageHBP()
        {
            return AvgHBP;
        }
        public static int AverageOL()
        {
            return AvgOL;
        }
        public static int AverageHR()
        {
            return AvgHR;
        }
        public static int AverageGL()
        {
            return AvgGL;
        }
    }
}
