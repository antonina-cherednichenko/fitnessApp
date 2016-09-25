package com.cherednichenko.antonina.detoxdiet;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cherednichenko.antonina.detoxdiet.db.ProgramsDatabaseHelper;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;
import com.cherednichenko.antonina.detoxdiet.detox_diet_program_info.ProgramInfoActivity;

import java.util.Calendar;
import java.util.List;

/**
 * Created by tonya on 8/29/16.
 */
public class RecommendedListAdapter extends RecyclerView.Adapter<RecommendedListAdapter.ReceipeViewHolder> {

    private List<ProgramInfo> receipeList;
    private Context context;


    public RecommendedListAdapter(Context context, List<ProgramInfo> receipeList) {
        this.context = context;
        this.receipeList = receipeList;
    }

    @Override
    public int getItemCount() {
        return receipeList.size();
    }

    @Override
    public void onBindViewHolder(ReceipeViewHolder receipeViewHolder, int i) {
        ProgramInfo ri = receipeList.get(i);
        receipeViewHolder.name.setText(ri.getName());
        receipeViewHolder.image.setImageResource(R.drawable.ic_heart_red);
    }

    @Override
    public ReceipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.recommended_item_layout, viewGroup, false);
        final ReceipeViewHolder holder = new ReceipeViewHolder(view);
        return holder;
    }

    public class ReceipeViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;


        public ReceipeViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.recommended_item_text);
            image = (ImageView) v.findViewById(R.id.recommended_item_image);
        }
    }

}

