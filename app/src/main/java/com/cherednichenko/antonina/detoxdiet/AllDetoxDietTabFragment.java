package com.cherednichenko.antonina.detoxdiet;

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

import com.cherednichenko.antonina.detoxdiet.detox_diet_data.DataProcessor;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;
import com.cherednichenko.antonina.detoxdiet.detox_diet_programs_list.DetoxDietLaunchMode;
import com.cherednichenko.antonina.detoxdiet.detox_diet_programs_list.DetoxDietProgramsListFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonya on 9/20/16.
 */
public class AllDetoxDietTabFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_all_detox_diet_tab, container, false);
        view = rootView;

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        Bundle args = getArguments();
        int mode = args.getInt("mode");
        List<ProgramInfo> programs = new ArrayList<>();
        if (mode == DetoxDietLaunchMode.LIKED.getMode()) {
            programs = DataProcessor.getLikedPrograms(getContext());
        } else if (mode == DetoxDietLaunchMode.STANDARD.getMode()) {
            programs = DataProcessor.getAllPrograms(getContext());
        } else if (mode == DetoxDietLaunchMode.SEARCH.getMode()) {
            //ADD functionality for search here
            String query = args.getString("searchQuery");
            programs = DataProcessor.getSearchPrograms(getContext(), query);
        }

//        Fragment allFragment = new DetoxDietProgramsListFragment();
//        Bundle allBundle = new Bundle();
//        allBundle.putSerializable("receipes", (Serializable) programs);
//        allFragment.setArguments(allBundle);

        Fragment detoxFragment = new DetoxDietProgramsListFragment();
        Bundle detoxBundle = new Bundle();
        detoxBundle.putSerializable("receipes", (Serializable) DataProcessor.getDetoxPrograms(programs));
        detoxBundle.putString("tag", "detox");
        detoxFragment.setArguments(detoxBundle);

        Fragment dietFragment = new DetoxDietProgramsListFragment();
        Bundle dietBundle = new Bundle();
        dietBundle.putSerializable("receipes", (Serializable) DataProcessor.getDietPrograms(programs));
        detoxBundle.putString("tag", "diet");
        dietFragment.setArguments(dietBundle);


        //adapter.addFragment(allFragment, "ALL");
        adapter.addFragment(detoxFragment, "DETOX");
        adapter.addFragment(dietFragment, "DIET");
        viewPager.setAdapter(adapter);
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
