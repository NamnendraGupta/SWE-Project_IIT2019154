package com.example.robodoc.utils;
import java.util.Random;

public class GetRandomBodyTemperature {
    public static Float get() {
        Random rand = new Random();
        int test=rand.nextInt(100);
        if(test>=0 && test<=49)
        {
            return rand.nextFloat() * (100 - 97) + 97;
        }
        if(test==50)
        {

            return rand.nextFloat() * (80 - 75) + 75;
        }
        if(test==51)
        {

            return rand.nextFloat() * (85 - 80) + 80;
        }
        if(test==52)
        {

            return rand.nextFloat() * (89 - 85) + 85;
        }
        if(test==53)
        {

            return rand.nextFloat() * (92 - 89) + 89;
        }
        if(test==54 || test==55)
        {

            return rand.nextFloat() * (95 - 92) + 92;
        }
        if(test>=56 && test<=70)
        {

            return rand.nextFloat() * (97 - 95) + 95;
        }
        if(test>=71 && test<=85)
        {

            return rand.nextFloat() * (103 - 100) + 100;
        }
        if(test>=86 && test<=95)
        {

            return rand.nextFloat() * (105 - 103) + 103;
        }
        if(test==96 || test==97)
        {

            return rand.nextFloat() * (107 - 105) + 105;
        }
        if(test==98)
        {

            return rand.nextFloat() * (109 - 107) + 107;
        }
        if(test==99)
        {

            return rand.nextFloat() * (111 - 109) + 109;
        }
        return rand.nextFloat() * (1) + 0;
    }
}
