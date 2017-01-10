package com.freeta.daily.fitness;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.freeta.daily.fitness.detox_diet_data.DataProcessor;
import com.freeta.daily.fitness.detox_diet_data.ProgramInfo;
import com.freeta.daily.fitness.detox_diet_programs_list.DetoxDietLaunchMode;
import com.freeta.daily.fitness.detox_diet_programs_list.DetoxDietProgramsListFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonya on 9/20/16.
 */

public class AllDetoxDietTabFragment extends Fragment implements TabSelected {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_all_detox_diet_tab, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //done to save selected tab when search was done
        Bundle args = getArguments();
        int tab = args.getInt("tab");
        tabLayout.setScrollPosition(tab, 0f, true);
        viewPager.setCurrentItem(tab);

        return rootView;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        Bundle args = getArguments();
        int mode = args.getInt("mode");
        String tag = "";
        List<ProgramInfo> programs = new ArrayList<>();
        if (mode == DetoxDietLaunchMode.LIKED.getMode()) {
            programs = DataProcessor.getLikedPrograms(getContext());
            tag += "Favourite ";
        } else if (mode == DetoxDietLaunchMode.STANDARD.getMode()) {
            programs = DataProcessor.getAllPrograms(getContext());
        } else if (mode == DetoxDietLaunchMode.SEARCH.getMode()) {
            //ADD functionality for search here
            String query = args.getString("searchQuery");
            programs = DataProcessor.getSearchPrograms(getContext(), query);
        } else if (mode == DetoxDietLaunchMode.NEW.getMode()) {
            programs = DataProcessor.getNewPrograms(getContext());
            tag += "New ";
        }

//        Fragment allFragment = new DetoxDietProgramsListFragment();
//        Bundle allBundle = new Bundle();
//        allBundle.putSerializable("receipes", (Serializable) programs);
//        allFragment.setArguments(allBundle);

        Fragment detoxFragment = new DetoxDietProgramsListFragment();
        Bundle detoxBundle = new Bundle();
        detoxBundle.putSerializable("receipes", (Serializable) DataProcessor.getDetoxPrograms(programs));
        detoxBundle.putString("tag", tag + "Fitness Programs");
        detoxBundle.putString("mode", "detox");
        detoxFragment.setArguments(detoxBundle);

        Fragment scheduleFragment = new ScheduleFragment();

        adapter.addFragment(detoxFragment, getResources().getString(R.string.tab_detox));

        adapter.addFragment(scheduleFragment, getResources().getString(R.string.tab_schedule));
        viewPager.setAdapter(adapter);
    }

    @Override
    public int search() {
        return viewPager.getCurrentItem();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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
