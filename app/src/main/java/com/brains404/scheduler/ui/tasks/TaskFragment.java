package com.brains404.scheduler.ui.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.brains404.scheduler.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tasks,container, false);
        ViewPager viewPager =  view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs =  view.findViewById(R.id.result_tabs);
        tabs.setupWithViewPager(viewPager);
        // Float Action Button For Time Table
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent addSessionActivity = new Intent(getActivity(), addTask.class);
                startActivity(addSessionActivity);
                //disable transition animation
                //( getActivity()).overridePendingTransition(0, 0);


            }
        });
        return view;
    }
    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {

        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new TaskTabs("Monday"), getString(R.string.tab_title_monday));
        adapter.addFragment(new TaskTabs("Tuesday"), getString(R.string.tab_title_tuesday));
        adapter.addFragment(new TaskTabs("Wednesday"), getString(R.string.tab_title_wednesday));
        adapter.addFragment(new TaskTabs("Thursday"), getString(R.string.tab_title_thursday));
        adapter.addFragment(new TaskTabs("Friday"), getString(R.string.tab_title_friday));
        adapter.addFragment(new TaskTabs("Saturday"), getString(R.string.tab_title_saturday));
        adapter.addFragment(new TaskTabs("Sunday"), getString(R.string.tab_title_sunday));
        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

         Adapter(FragmentManager manager) {
            super(manager);
        }


        @NotNull
        @Override
           public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}

