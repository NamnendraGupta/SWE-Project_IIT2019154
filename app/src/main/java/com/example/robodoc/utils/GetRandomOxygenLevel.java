package com.example.robodoc.utils;
import java.util.Random;
public class GetRandomOxygenLevel {
    public static Integer get() {
        Random rand = new Random();
        int test=rand.nextInt(100);
        if(test>=0 && test<=59)
        {
            return rand.nextInt(25)+75;
        }
        if(test>=60 && test<=79)
        {

            return rand.nextInt(5)+70;
        }
        if(test>=80 && test<=89)
        {

            return rand.nextInt(5)+65;
        }
        if(test>=90 && test<=93)
        {

            return rand.nextInt(5)+60;
        }
        if(test==94)
        {

            return rand.nextInt(10)+50;
        }
        if(test==95 || test==96)
        {

            return rand.nextInt(10)+100;
        }
        if(test==97 || test==98)
        {

            return rand.nextInt(5) + 110;
        }
        if(test==99)
        {

            return rand.nextInt(6)+115;
        }
        return rand.nextInt(5);
    }
}
