package com.cherednichenko.antonina.detoxdiet.detox_diet_programs_list;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cherednichenko.antonina.detoxdiet.R;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by tonya on 9/8/16.
 */
public class DetoxDietProgramsListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detox_diet_programs_list, container, false);
        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.cardList);
        //recList.setHasFixedSize(true);
        recList.setItemAnimator(new SlideInUpAnimator());

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        Bundle args = getArguments();
        if (args != null) {
            List<ProgramInfo> receipes = (List<ProgramInfo>) args.getSerializable("receipes");
            boolean likedOnlyMode = args.getBoolean("mode");

            DetoxDietProgramsListAdapter adapter = new DetoxDietProgramsListAdapter(getActivity(), receipes, likedOnlyMode);
            recList.setAdapter(adapter);
        }

        return rootView;
    }

}
