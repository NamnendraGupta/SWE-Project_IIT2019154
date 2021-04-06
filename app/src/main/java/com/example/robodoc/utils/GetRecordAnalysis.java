package com.example.robodoc.utils;

import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.enums.VitalSignsKey;

import java.util.ArrayList;

public class GetRecordAnalysis {
    private Arraylist<VitalInput> recordList;
    public GetRecordAnalysis(ArrayList<VitalInput> recordList) {
        this.recordList = recordList;
    }
        float AvgBT=0;
        int AvgLBP=0;
        int AvgHBP=0;
        int AvgOL=0;
        int AvgHR=0;
        int AvgGL=0;
        float maxBT=0,minBT=200;
        int inNormalBT=0,aboveNormalBT=0,belowNormalBT=0;
        int maxHBP=0,maxLBP=0,minHBP=300,minLBP=300,inNormalBP=0,aboveNormalBP=0,belowNormalBP=0;
        int maxOL=0,minOL=400,inNormalOL=0,aboveNormalOL=0,belowNormalOL=0;
        int maxHR=0,minHR=300,inNormalHR=0,aboveNormalHR=0,belowNormalHR=0;
        int maxGL=0,minGL=2000,inNormalGL=0,aboveNormalGL=0,belowNormalGL=0;

        int n=recordList.size();
        for(int i=0;i<recordList.size();i++)
        {
            VitalInput record = recordList.get(i);
            float BT;
            int LBP,HBP,OL,HR,GL;
            BT=record.getBodyTemperature();
            LBP=record.getLowBP();
            HBP=record.getHighBP();
            OL=record.getOxygenLevel();
            HR=record.getHeartRate();
            GL=record.getGlucoseLevel();
            if(BT>=97 && BT<=99)
                inNormalBT++;
            if(BT<97)
                belowNormalBT++;
            if(BT>99)
                aboveNormalBT++;
            if(OL>=75 && OL<=100)
                inNormalOL++;
            if(OL<75)
                belowNormalOL++;
            if(OL>100)
                aboveNormalOL++;
            if(HR>=60 && HR<=100)
                inNormalHR++;
            if(HR<60)
                belowNormalHR++;
            if(HR>100)
                aboveNormalHR++;
            if(GL>=70 && GL<=100)
                inNormalGL++;
            if(GL<70)
                belowNormalGL++;
            if(GL>100)
                aboveNormalGL++;
            if(HBP>=90 && HBP<=120 && LBP>=60 && LBP<=80)
                inNormalBP++;
            if(LBP<60)
                belowNormalBP++;
            if(HBP>120)
                aboveNormalBP++;
            if(BT>maxBT)
                maxBT=BT;
            if(HBP>maxHBP) {
                maxHBP = HBP;
                maxLBP = LBP;
            }
            if(OL>maxOL)
                maxOL=OL;
            if(HR>maxHR)
                maxHR=HR;
            if(GL>maxGL)
                maxGL=GL;
            if(BT<minBT)
                minBT=BT;
            if(LBP<minLBP) {
                minLBP = LBP;
                minHBP = HBP;
            }
            if(OL<minOL)
                minOL=OL;
            if(HR<minHR)
                minHR=HR;
            if(GL<minGL)
                minGL=GL;
            AvgBT+=BT;
            AvgLBP+=LBP;
            AvgHBP+=HBP;
            AvgOL+=OL;
            AvgHR+=HR;
            AvgGL+=GL;
        }
        AvgBT/=n;
        AvgLBP/=n;
        AvgHBP/=n;
        AvgOL/=n;
        AvgHR/=n;
        AvgGL/=n;
        public String AverageBT()
        {
            return Float.toString(AvgBT);
        }
        public String AverageBP() {
            String s = "(" + AvgLBP + "," + AvgHBP + ")";
            return s;
        }
        public String AverageOL()
        {
            return Integer.toString(AvgOL);
        }
        public String AverageHR()
        {
            return Integer.toString(AvgHR);
        }
        public String AverageGL()
        {
            return Integer.toString(AvgGL);
        }
        public String NormalBT()
        {
            String s="97F-99F";
            return s;
        }
        public String NormalBP()
        {
            String s="60-80,90-120 mmHg";
            return s;
        }
    public String NormalOL()
    {
        String s="75-100 mmHg";
        return s;
    }
    public String NormalHR()
    {
        String s="60-100 bpm";
        return s;
    }
    public String NormalGL()
    {
        String s="70-100 mg/dL";
        return s;
    }
    public String MaxBP()
    {
        String s="("+maxLBP+","+maxHBP+")";
        return s;
    }
    public String MaxBT()
    {
        return Float.toString(maxBT);
    }
    public String MaxOL()
    {
        return Integer.toString(maxOL);
    }
    public String MaxHR()
    {
        return Integer.toString(maxHR);
    }
    public String MaxGL()
    {
        return Integer.toString(maxGL);
    }
    public String MinBP()
    {
        String s="("+minLBP+","+minHBP+")";
        return s;
    }
    public String MinBT()
    {
        return Float.toString(minBT);
    }
    public String MinOL()
    {
        return Integer.toString(minOL);
    }
    public String MinHR()
    {
        return Integer.toString(minHR);
    }
    public String MinGL()
    {
        return Integer.toString(minGL);
    }
    public String InNormalBP()
    {
        return Integer.toString(inNormalBP);
    }
    public String InNormalBT()
    {
        return Integer.toString(inNormalBT);
    }
    public String InNormalOL()
    {
        return Integer.toString(inNormalOL);
    }
    public String InNormalHR()
    {
        return Integer.toString(inNormalHR);
    }
    public String InNormalGL()
    {
        return Integer.toString(inNormalGL);
    }
    public String AboveNormalBP()
    {
        return Integer.toString(aboveNormalBP);
    }
    public String AboveNormalBT()
    {
        return Integer.toString(aboveNormalBT);
    }
    public String AboveNormalOL()
    {
        return Integer.toString(aboveNormalOL);
    }
    public String AboveNormalHR()
    {
        return Integer.toString(aboveNormalHR);
    }
    public String AboveNormalGL()
    {
        return Integer.toString(aboveNormalGL);
    }
    public String BelowNormalBP()
    {
        return Integer.toString(belowNormalBP);
    }
    public String BelowNormalBT()
    {
        return Integer.toString(belowNormalBT);
    }
    public String BelowNormalOL()
    {
        return Integer.toString(belowNormalOL);
    }
    public String BelowNormalHR()
    {
        return Integer.toString(belowNormalHR);
    }
    public String BelowNormalGL()
    {
        return Integer.toString(belowNormalGL);
    }
}
