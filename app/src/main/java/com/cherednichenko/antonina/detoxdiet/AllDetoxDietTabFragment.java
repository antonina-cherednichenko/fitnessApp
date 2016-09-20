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

        List<ProgramInfo> receipes = DataProcessor.createReceipeList(getResources().openRawResource(R.raw.programs_data));

        Bundle args = getArguments();
        boolean mode = args.getBoolean("mode");


        //setup default fragment with program cards
        Fragment allFragment = new DetoxDietProgramsListFragment();
        Bundle allBundle = new Bundle();
        allBundle.putSerializable("receipes", (Serializable) receipes);

        allBundle.putBoolean("mode", mode);
        allFragment.setArguments(allBundle);

        Fragment detoxFragment = new DetoxDietProgramsListFragment();
        Bundle detoxBundle = new Bundle();
        detoxBundle.putSerializable("receipes", (Serializable) receipes);
        detoxBundle.putBoolean("mode", mode);
        detoxFragment.setArguments(detoxBundle);

        Fragment dietFragment = new DetoxDietProgramsListFragment();
        Bundle dietBundle = new Bundle();
        dietBundle.putSerializable("receipes", (Serializable) receipes);
        dietBundle.putBoolean("mode", mode);
        dietFragment.setArguments(dietBundle);


        adapter.addFragment(allFragment, "ALL");
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
