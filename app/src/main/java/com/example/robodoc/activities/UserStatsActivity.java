package com.example.robodoc.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.robodoc.R;
import com.example.robodoc.adapters.ViewPagerAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class UserStatsActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    TabLayout tabLayout;
    ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_stats);

        toolbar=findViewById(R.id.toolbarUserStats);
        tabLayout=findViewById(R.id.tabLayoutUserStats);
        viewPager2=findViewById(R.id.viewPagerUserStats);

        viewPager2.setAdapter(new ViewPagerAdapter(this));
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