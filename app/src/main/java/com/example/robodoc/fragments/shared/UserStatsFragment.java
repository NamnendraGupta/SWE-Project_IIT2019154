package com.example.robodoc.fragments.shared;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.robodoc.R;
import com.example.robodoc.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class UserStatsFragment extends Fragment {

    public UserStatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TabLayout tabLayout = view.findViewById(R.id.tabLayoutFragmentUserStats);
        ViewPager2 viewPager2 = view.findViewById(R.id.viewPagerFragmentUserStats);

        viewPager2.setAdapter(new ViewPagerAdapter(requireActivity()));
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(tabLayout, viewPager2, true, (tab, position) -> {
            switch (position){
                case 0:{
                    tab.setText("Blood Pressure");
                    break;
                }
                case 1:{
                    tab.setText("Body Temperature");
                    break;
                }
                case 2:{
                    tab.setText("Heart Rate");
                    break;
                }
                case 3:{
                    tab.setText("Glucose Level");
                    break;
                }
                case 4:{
                    tab.setText("Oxygen Level");
                    break;
                }
            }
        });
        tabLayoutMediator.attach();
    }
}