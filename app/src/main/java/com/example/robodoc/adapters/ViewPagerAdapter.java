package com.example.robodoc.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.robodoc.classes.VitalInput;
import com.example.robodoc.enums.VitalInputType;
import com.example.robodoc.fragments.user.VitalGraphFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:{
                return new VitalGraphFragment(VitalInputType.BODY_TEMPERATURE);
            }
            case 2:{
                return new VitalGraphFragment(VitalInputType.HEART_RATE);
            }
            case 3:{
                return new VitalGraphFragment(VitalInputType.GLUCOSE_LEVEL);
            }
            case 4:{
                return new VitalGraphFragment(VitalInputType.OXYGEN_LEVEL);
            }
            default:{
                return new VitalGraphFragment(VitalInputType.BLOOD_PRESSURE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
