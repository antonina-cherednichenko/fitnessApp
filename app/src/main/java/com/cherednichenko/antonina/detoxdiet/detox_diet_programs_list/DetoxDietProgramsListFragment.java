package com.cherednichenko.antonina.detoxdiet.detox_diet_programs_list;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cherednichenko.antonina.detoxdiet.R;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.DataProcessor;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by tonya on 9/8/16.
 */
public class DetoxDietProgramsListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_detox_diet_programs_list, container, false);

        final RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.card_list);

        final RelativeLayout emptyState = (RelativeLayout) rootView.findViewById(R.id.empty_state);
        //recList.setHasFixedSize(true);
        recList.setItemAnimator(new SlideInUpAnimator());

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);


        final List<ProgramInfo> receipes;
        final String tag;
        Bundle args = getArguments();
        if (args != null) {
            receipes = (List<ProgramInfo>) args.getSerializable("receipes");
            tag = args.getString("tag");
        } else {
            receipes = new ArrayList<>();
            tag = "all";
        }

        final DetoxDietProgramsListAdapter adapter = new DetoxDietProgramsListAdapter(getActivity(), receipes);
        if (receipes.size() > 0) {
            recList.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        } else {
            recList.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
            ((TextView) emptyState.findViewById(R.id.empty_state_text)).setText(String.format("No %s were found", tag));
        }
        recList.setAdapter(adapter);

        BottomBar bottomBar = (BottomBar) rootView.findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.bottombar_favorite) {
                    List<ProgramInfo> likedPrograms = DataProcessor.getLikedPrograms(receipes);
                    if (likedPrograms.size() > 0) {
                        recList.setAdapter(new DetoxDietProgramsListAdapter(getActivity(), likedPrograms));
                        recList.setVisibility(View.VISIBLE);
                        emptyState.setVisibility(View.GONE);
                    } else {
                        emptyState.setVisibility(View.VISIBLE);
                        recList.setVisibility(View.GONE);
                        ((TextView) emptyState.findViewById(R.id.empty_state_text)).setText(String.format("No Favourite %s were found", tag));
                    }
                    //adapter.notifyDataSetChanged();
                } else if (tabId == R.id.bottombar_new) {
                    List<ProgramInfo> newPrograms = DataProcessor.getNewPrograms(receipes);
                    if (newPrograms.size() > 0) {
                        recList.setAdapter(new DetoxDietProgramsListAdapter(getActivity(), newPrograms));
                        recList.setVisibility(View.VISIBLE);
                        emptyState.setVisibility(View.GONE);
                    } else {
                        emptyState.setVisibility(View.VISIBLE);
                        recList.setVisibility(View.GONE);
                        ((TextView) emptyState.findViewById(R.id.empty_state_text)).setText(String.format("No New %s were found", tag));
                    }
                } else if (tabId == R.id.bottombar_all) {
                    if (receipes.size() > 0) {
                        recList.setAdapter(new DetoxDietProgramsListAdapter(getActivity(), receipes));
                        recList.setVisibility(View.VISIBLE);
                        emptyState.setVisibility(View.GONE);
                    } else {
                        emptyState.setVisibility(View.VISIBLE);
                        recList.setVisibility(View.GONE);
                        emptyState.setVisibility(View.VISIBLE);
                        recList.setVisibility(View.GONE);
                        ((TextView) emptyState.findViewById(R.id.empty_state_text)).setText(String.format("No %s were found", tag));
                    }
                }
            }
        });

        return rootView;
    }

}
