package com.example.robodoc.utils;

import java.util.Random;

public class GetRandomGlucoseLevel {
    public static Integer get() {
        Random rand = new Random();
        int test=rand.nextInt(100);
        if(test>=0 && test<=24)
        {
            return rand.nextInt(31)+70;
        }
        if(test>=25 && test<=49)
        {

            return rand.nextInt(25)+101;
        }
        if(test>=50 && test<=64)
        {

            return rand.nextInt(55)+126;
        }
        if(test>=65 && test<=74)
        {

            return rand.nextInt(70)+181;
        }
        if(test>=75 && test<=79)
        {

            return rand.nextInt(50)+251;
        }
        if(test==80 || test==81)
        {

            return rand.nextInt(50)+301;
        }
        if(test==82 || test==83)
        {

            return rand.nextInt(50) + 351;
        }
        if(test==84)
        {

            return rand.nextInt(100)+401;
        }
        if(test>=85 && test<=94)
        {

            return rand.nextInt(20)+50;
        }
        if(test>=95 && test<=99)
        {

            return rand.nextInt(20)+30;
        }
        return rand.nextInt(5);
    }
}
