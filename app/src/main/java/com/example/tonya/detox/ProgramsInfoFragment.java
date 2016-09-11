package com.example.tonya.detox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by tonya on 9/8/16.
 */
public class ProgramsInfoFragment extends Fragment {
    private int[] images = {R.drawable.green_detox_program, R.drawable.citrus_detox_program, R.drawable.apple_detox_program,
            R.drawable.juice_detox_program, R.drawable.rice_detox_program};

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

        ReceipeAdapter adapter = new ReceipeAdapter(getActivity(), createReceipeList());
        recList.setAdapter(adapter);

        return rootView;
    }

    private List<ReceipeInfo> createReceipeList() {
        List<ReceipeInfo> receipes = new ArrayList<>();
        try {
            String chatFileData = loadProgramsData();
            JSONObject jsonData = new JSONObject(chatFileData);
            JSONArray allPrograms = jsonData.getJSONArray("data");
            for (int i = 0; i < allPrograms.length(); i++) {
                JSONObject program = allPrograms.getJSONObject(i);
                receipes.add(new ReceipeInfo(program.getString("name"), program.getString("description"), images[i]));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return receipes;
    }

    private String loadProgramsData() throws IOException {
        InputStream inputStream = getResources().openRawResource(R.raw.programs_data);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String receiveString;
        StringBuilder stringBuilder = new StringBuilder();

        while ((receiveString = bufferedReader.readLine()) != null) {
            stringBuilder.append(receiveString);
            stringBuilder.append("\n");
        }

        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();


        return stringBuilder.toString();
    }
}
