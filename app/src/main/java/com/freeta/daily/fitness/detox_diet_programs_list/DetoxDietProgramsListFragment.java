package com.freeta.daily.fitness.detox_diet_programs_list;


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

import com.freeta.daily.fitness.R;
import com.freeta.daily.fitness.detox_diet_data.DataProcessor;
import com.freeta.daily.fitness.detox_diet_data.ProgramInfo;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by tonya on 9/8/16.
 */
public class DetoxDietProgramsListFragment extends Fragment {

    List<ProgramInfo> receipes;
    List<ProgramInfo> allPrograms;
    RecyclerView recList;
    RelativeLayout emptyState;
    String tag;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_detox_diet_programs_list, container, false);

        recList = (RecyclerView) rootView.findViewById(R.id.card_list);
        emptyState = (RelativeLayout) rootView.findViewById(R.id.empty_state);
        //recList.setHasFixedSize(true);
        recList.setItemAnimator(new SlideInUpAnimator());

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        Bundle args = getArguments();

        if (args != null) {
            receipes = (List<ProgramInfo>) args.getSerializable("receipes");
            tag = args.getString("tag");
            if (args.getString("mode").equals("detox")) {
                allPrograms = DataProcessor.getDetoxPrograms(DataProcessor.getAllPrograms(getActivity()));
            } else {
                allPrograms = DataProcessor.getDietPrograms(DataProcessor.getAllPrograms(getActivity()));
            }
        } else {
            receipes = new ArrayList<>();
            allPrograms = new ArrayList<>();
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
        BottomBarSelecter selector = new BottomBarSelecter();

        bottomBar.setOnTabReselectListener(selector);
        bottomBar.setOnTabSelectListener(selector);

        return rootView;
    }


    private class BottomBarSelecter implements OnTabSelectListener, OnTabReselectListener {

        @Override
        public void onTabReSelected(@IdRes int tabId) {
            performSelectOp(tabId, allPrograms);
        }

        @Override
        public void onTabSelected(@IdRes int tabId) {
            performSelectOp(tabId, receipes);
        }

        private void performSelectOp(int tabId, List<ProgramInfo> programs) {
            if (tabId == R.id.bottombar_favorite) {
                List<ProgramInfo> likedPrograms = DataProcessor.getLikedPrograms(programs);

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
                List<ProgramInfo> newPrograms = DataProcessor.getNewPrograms(programs);
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
                if (programs.size() > 0) {
                    recList.setAdapter(new DetoxDietProgramsListAdapter(getActivity(), programs));

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
    }

}
