package com.example.robodoc.utils;

import android.util.Pair;

import java.util.Random;

public class GetRandomBloodPressure {
    public static Pair<Integer,Integer> get() {
        Random rand = new Random();
        int sy = rand.nextInt(130);
        int di = rand.nextInt(100);
        sy = sy + 70;
        di = di + 40;
        if (sy <= 90 && di <= 60)
            return new Pair<Integer, Integer>(sy, di);
        if (sy >= 90 && sy <= 120 && di >= 60 && di <= 80)
            return new Pair<Integer, Integer>(sy, di);
        if (sy > 120 && di > 70)
            return new Pair<Integer, Integer>(sy, di);
        if (sy - di < 20)
            return get();
        else
            return get();
    }
}
