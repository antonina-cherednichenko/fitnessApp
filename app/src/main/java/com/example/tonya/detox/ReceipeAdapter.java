package com.example.tonya.detox;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tonya on 8/29/16.
 */
public class ReceipeAdapter extends RecyclerView.Adapter<ReceipeAdapter.ReceipeViewHolder> {

    private List<ReceipeInfo> receipeList;
    private Context context;

    public ReceipeAdapter(Context context, List<ReceipeInfo> receipeList) {
        this.context = context;
        this.receipeList = receipeList;
    }


    @Override
    public int getItemCount() {
        return receipeList.size();
    }

    @Override
    public void onBindViewHolder(ReceipeViewHolder receipeViewHolder, int i) {
        ReceipeInfo ri = receipeList.get(i);
        receipeViewHolder.name.setText(ri.name);
        receipeViewHolder.description.setText(ri.description);
        receipeViewHolder.image.setImageResource(ri.photoId);

    }

    @Override
    public ReceipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.receipe_layout, viewGroup, false);
        final ReceipeViewHolder holder = new ReceipeViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                //check if position exists
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, ReceipeActivity.class);
                    intent.putExtra("receipe_position", position);
                    context.startActivity(intent);
                }
            }
        });

        return holder;
    }



    public class ReceipeViewHolder extends RecyclerView.ViewHolder {

        protected TextView name;
        protected TextView description;
        protected ImageView image;


        public ReceipeViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.receipe_name);
            description = (TextView) v.findViewById(R.id.receipe_description);
            image = (ImageView) v.findViewById(R.id.receipe_image);
        }
    }
}

