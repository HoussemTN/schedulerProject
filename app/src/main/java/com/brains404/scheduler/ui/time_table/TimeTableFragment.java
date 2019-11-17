package com.brains404.scheduler.ui.time_table;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.brains404.scheduler.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class TimeTableFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }



    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_time_table, container, false);
        ViewPager viewPager =view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs =  view.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        // Float Action Button For Time Table
       FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addSessionActivity = new Intent(getActivity(), addSession.class);
                startActivity(addSessionActivity);
                //disable transition animation
                /*( getActivity()).overridePendingTransition(0, 0);*/


            }
        });
        return view;
    }
    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new TimeTableTabsFragment("MONDAY"), getString(R.string.tab_title_monday));
        adapter.addFragment(new TimeTableTabsFragment("TUESDAY"), getString(R.string.tab_title_tuesday));
        adapter.addFragment(new TimeTableTabsFragment("WEDNESDAY"), getString(R.string.tab_title_wednesday));
        adapter.addFragment(new TimeTableTabsFragment("THURSDAY"), getString(R.string.tab_title_thursday));
        adapter.addFragment(new TimeTableTabsFragment("FRIDAY"), getString(R.string.tab_title_friday));
        adapter.addFragment(new TimeTableTabsFragment("SATURDAY"), getString(R.string.tab_title_saturday));
        adapter.addFragment(new TimeTableTabsFragment("SUNDAY"), getString(R.string.tab_title_sunday));
        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);

        }

    }

}

