package com.example.robodoc.classes;

import com.example.robodoc.enums.VitalSignsKey;
import com.google.firebase.database.DataSnapshot;

import java.util.Date;
import java.util.HashMap;

public class VitalInput {

    public enum VitalInputType{
        AUTOMATIC, MANUAL
    }

    private String InputID;
    private Long TimeOfInput;
    private int HighBP;
    private int LowBP;
    private float BodyTemperature;
    private int GlucoseLevel;
    private int HeartRate;
    private int OxygenLevel;
    private VitalInputType InputType;

    public VitalInput(VitalInputType type){
        TimeOfInput=new Date().getTime();
        InputID=null;
        InputType=type;
    }

    public VitalInput(DataSnapshot snapshot){
        InputID=snapshot.getKey();
        TimeOfInput=(Long) snapshot.child(VitalSignsKey.INPUT_TIME).getValue();
        HighBP=Integer.parseInt(snapshot.child(VitalSignsKey.BP_HIGH).getValue().toString());
        LowBP=Integer.parseInt(snapshot.child(VitalSignsKey.BP_LOW).getValue().toString());
        BodyTemperature=Float.parseFloat(snapshot.child(VitalSignsKey.BODY_TEMP).getValue().toString());;
        GlucoseLevel=Integer.parseInt(snapshot.child(VitalSignsKey.GLUCOSE_LEVEL).getValue().toString());;
        HeartRate=Integer.parseInt(snapshot.child(VitalSignsKey.HEART_RATE).getValue().toString());;
        OxygenLevel=Integer.parseInt(snapshot.child(VitalSignsKey.OXYGEN_LEVEL).getValue().toString());;
        InputType=VitalInputType.valueOf(snapshot.child(VitalSignsKey.INPUT_TYPE).getValue().toString());
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
        hash.put(VitalSignsKey.INPUT_TYPE,InputType.toString());
        return hash;
    }

    public VitalInputType getInputType() {
        return InputType;
    }

    public void setInputType(VitalInputType inputType) {
        InputType = inputType;
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
