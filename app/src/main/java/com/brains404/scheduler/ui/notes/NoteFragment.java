package com.brains404.scheduler.ui.notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.brains404.scheduler.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment {
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setRetainInstance(true);
    }


    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_note, container, false);
        ViewPager viewPager =view.findViewById(R.id.note_viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs =  view.findViewById(R.id.result_note_tabs);

        // get current day (selected tab)
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position= tab.getPosition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {


            }
        });

        tabs.setupWithViewPager(viewPager);
        // Float Action Button For Time Table
        FloatingActionButton fab = view.findViewById(R.id.note_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addNoteActivity = new Intent(getActivity(), addNote.class);
                //pass the current selected tab which represent the day position
                addNoteActivity.putExtra("CURRENT_NOTE_DAY_ID",position);
                startActivity(addNoteActivity);
                //disable transition animation
                /*( getActivity()).overridePendingTransition(0, 0);*/
            }
        });
        // to prevent NPE we need to make sure that our bundle exist
        Intent i =getActivity().getIntent();
        Bundle extras = i.getExtras();
        //Log.d("Test", "Hello World");
        if (extras!= null) {
            // Bundle exist
            String LAST_VISITED_NOTE_DAY_ID = "LAST_VISITED_NOTE_DAY_ID";
            boolean isVisited = extras.containsKey(LAST_VISITED_NOTE_DAY_ID);
            //Log.d("Test", "is"+isVisited);

            if (isVisited) {
                int idLabel = extras.getInt(LAST_VISITED_NOTE_DAY_ID);
                // Log.d("Test", "is"+idDay);
                // Restore last visited TabLayout
                TabLayout.Tab selectedTab = tabs.getTabAt(idLabel);
                assert selectedTab != null;
                selectedTab.select();
            }
        }
        return view;
    }
    // Add Fragments to Tabs with idDay
    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new NoteTabsFragment(0), getString(R.string.tab_title_monday));
        adapter.addFragment(new NoteTabsFragment(1), getString(R.string.tab_title_tuesday));
        adapter.addFragment(new NoteTabsFragment(2), getString(R.string.tab_title_wednesday));
        adapter.addFragment(new NoteTabsFragment(3), getString(R.string.tab_title_thursday));
        adapter.addFragment(new NoteTabsFragment(4), getString(R.string.tab_title_friday));
        adapter.addFragment(new NoteTabsFragment(5), getString(R.string.tab_title_saturday));
        adapter.addFragment(new NoteTabsFragment(6), getString(R.string.tab_title_sunday));
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

