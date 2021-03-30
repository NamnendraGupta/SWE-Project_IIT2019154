package com.example.robodoc.classes;

import com.example.robodoc.enums.VitalSignsKey;

import java.util.Date;
import java.util.HashMap;

public class VitalInput {

    private String InputID;
    private Long TimeOfInput;
    private int HighBP;
    private int LowBP;
    private float BodyTemperature;
    private int GlucoseLevel;
    private int HeartRate;
    private int OxygenLevel;

    public VitalInput(){
        TimeOfInput=new Date().getTime();
        InputID=null;
    }

    public VitalInput(String inputID){
        InputID=inputID;
    }

    public HashMap<String,Object> getInitialHashMap(){
        HashMap<String,Object> hash=new HashMap<>();
        hash.put(VitalSignsKey.INPUT_ID,InputID);
        hash.put(VitalSignsKey.INPUT_TIME,TimeOfInput);
        hash.put(VitalSignsKey.BP_HIGH,HighBP);
        hash.put(VitalSignsKey.BP_LOW,LowBP);
        hash.put(VitalSignsKey.BODY_TEMP,BodyTemperature);
        hash.put(VitalSignsKey.GLUCOSE_LEVEL,GlucoseLevel);
        hash.put(VitalSignsKey.HEART_RATE,HeartRate);
        hash.put(VitalSignsKey.OXYGEN_LEVEL,OxygenLevel);
        return hash;
    }

    public String getInputID() {
        return InputID;
    }

    public void setInputID(String inputID) {
        InputID = inputID;
    }

    public Long getTimeOfInput() {
        return TimeOfInput;
    }

    public void setTimeOfInput(Long timeOfInput) {
        TimeOfInput = timeOfInput;
    }

    public int getHighBP() {
        return HighBP;
    }

    public void setHighBP(int highBP) {
        HighBP = highBP;
    }

    public int getLowBP() {
        return LowBP;
    }

    public void setLowBP(int lowBP) {
        LowBP = lowBP;
    }

    public float getBodyTemperature() {
        return BodyTemperature;
    }

    public void setBodyTemperature(float bodyTemperature) {
        BodyTemperature = bodyTemperature;
    }

    public int getGlucoseLevel() {
        return GlucoseLevel;
    }

    public void setGlucoseLevel(int glucoseLevel) {
        GlucoseLevel = glucoseLevel;
    }

    public int getHeartRate() {
        return HeartRate;
    }

    public void setHeartRate(int heartRate) {
        HeartRate = heartRate;
    }

    public int getOxygenLevel() {
        return OxygenLevel;
    }

    public void setOxygenLevel(int oxygenLevel) {
        OxygenLevel = oxygenLevel;
    }
}
