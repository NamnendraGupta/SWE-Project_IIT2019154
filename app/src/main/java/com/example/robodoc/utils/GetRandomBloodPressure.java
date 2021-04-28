package com.example.robodoc.utils;

import android.util.Pair;

import java.util.Random;

public class GetRandomBloodPressure {
    public static Pair<Integer,Integer> get() {
        Random rand = new Random();
        int test=rand.nextInt(100);
        int sy,di;
        if(test>=0 && test<=49)
        {
            sy=rand.nextInt(31)+90;
            di=rand.nextInt(21)+60;
            return new Pair<>(sy, di);
        }
        if(test>=50 && test<=59)
        {
            sy=rand.nextInt(20)+70;
            di=rand.nextInt(20)+40;
            return new Pair<>(sy, di);
        }
        if(test>=60 && test<=69)
        {
            sy=rand.nextInt(10)+120;
            di=rand.nextInt(10)+75;
            return new Pair<>(sy, di);
        }
        if(test>=70 && test<=79)
        {
            sy=rand.nextInt(10)+130;
            di=rand.nextInt(10)+80;
            return new Pair<>(sy, di);
        }
        if(test>=80 && test<=87)
        {
            sy=rand.nextInt(10)+140;
            di=rand.nextInt(10)+90;
            return new Pair<>(sy, di);
        }
        if(test>=88 && test<=92)
        {
            sy=rand.nextInt(10)+150;
            di=rand.nextInt(10)+100;
            return new Pair<>(sy, di);
        }
        if(test>=93 && test<=95)
        {
            sy=rand.nextInt(10)+160;
            di=rand.nextInt(5)+110;
            return new Pair<>(sy, di);
        }
        if(test>=96 && test<=97)
        {
            sy=rand.nextInt(10)+170;
            di=rand.nextInt(5)+115;
            return new Pair<>(sy, di);
        }
        if(test>98 && test<=99)
        {
            sy=rand.nextInt(20)+180;
            di=rand.nextInt(20)+120;
            return new Pair<>(sy, di);
        }
        return get();
    }
}
