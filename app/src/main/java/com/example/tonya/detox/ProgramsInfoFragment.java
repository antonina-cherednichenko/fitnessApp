package com.example.tonya.detox;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by tonya on 9/8/16.
 */
public class ProgramsInfoFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_receipes, container, false);
        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.cardList);
        //recList.setHasFixedSize(true);
        recList.setItemAnimator(new SlideInUpAnimator());

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        Bundle args = getArguments();
        List<ReceipeInfo> receipes = (List<ReceipeInfo>) args.getSerializable("receipes");
        boolean likedOnlyMode = (boolean) args.getSerializable("mode");

        ReceipeAdapter adapter = new ReceipeAdapter(getActivity(), receipes, likedOnlyMode);
        recList.setAdapter(adapter);

        return rootView;
    }

}
