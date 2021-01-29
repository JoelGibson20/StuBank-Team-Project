package com.back4app.java.StuBank.ui.pound;

import android.os.Bundle;

import com.back4app.java.StuBank.R;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

public class NewRecipientTabbed extends AppCompatActivity {

    //Create fragmented view to allow movement between transferring to other Stubank
    //users and transferring to anyone else
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_recipient_tabbed);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new StubankFragment(), "Stubank User");
        sectionsPagerAdapter.addFragment(new OtherFragment(), "Anyone Else");
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);

        tabs.setupWithViewPager(viewPager);


    }
}