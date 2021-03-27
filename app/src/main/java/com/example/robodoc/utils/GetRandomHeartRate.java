package com.example.robodoc.utils;
import java.util.Random;

public class GetRandomHeartRate {
    public static Integer get() {
        Random rand = new Random();
        int test=rand.nextInt(100);
        if(test>=0 && test<=69)
        {
            return rand.nextInt(40)+60;
        }
        if(test>=70 && test<=74)
        {

            return rand.nextInt(20)+40;
        }
        if(test>=75 && test<=84)
        {

            return rand.nextInt(10)+100;
        }
        if(test>=85 && test<=94)
        {

            return rand.nextInt(10)+110;
        }
        if(test==95 || test==96)
        {

            return rand.nextInt(20)+120;
        }
        if(test==97 || test==98)
        {

            return rand.nextInt(20)+140;
        }
        if(test==99)
        {

            return rand.nextInt(41) + 160;
        }
        return rand.nextInt(5);
    }
}
