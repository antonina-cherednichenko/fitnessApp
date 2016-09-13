package com.example.tonya.detox;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tonya on 8/29/16.
 */
public class ReceipeAdapter extends RecyclerView.Adapter<ReceipeAdapter.ReceipeViewHolder> {

    private List<ReceipeInfo> receipeList;
    private Context context;
    private boolean likedMode;
    private List<ReceipeInfo> likedList;

    public ReceipeAdapter(Context context, List<ReceipeInfo> receipeList, boolean likedMode) {
        this.context = context;
        this.receipeList = receipeList;
        this.likedMode = likedMode;
        if (likedMode) {
            likedList = filterLikedReceipes();
        }
    }

    @Override
    public int getItemCount() {

        return likedMode ? likedList.size() : receipeList.size();
    }

    @Override
    public void onBindViewHolder(ReceipeViewHolder receipeViewHolder, int i) {
        ReceipeInfo ri = likedMode ? likedList.get(i) : receipeList.get(i);
        receipeViewHolder.name.setText(ri.name);
        receipeViewHolder.description.setText(ri.description);
        receipeViewHolder.image.setImageResource(ri.photoId);
        receipeViewHolder.liked.setImageResource(ri.liked ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_grey);

    }

    @Override
    public ReceipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.receipe_layout, viewGroup, false);
        final ReceipeViewHolder holder = new ReceipeViewHolder(view);
        //TODO replace this on some cool animation
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                //check if position exists
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, ReceipeActivity.class);
                    intent.putExtra("receipe_info", receipeList.get(position));
                    context.startActivity(intent);
                }
            }
        });

        final Button readMoreButton = (Button) view.findViewById(R.id.read_more);
        readMoreButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                //check if position exists
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, ReceipeActivity.class);
                    intent.putExtra("receipe_info", receipeList.get(position));
                    context.startActivity(intent);
                }
            }
        });

        final ImageButton likeButton = (ImageButton) view.findViewById(R.id.btnLike);
        likeButton.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                //check if position exists
                if (position != RecyclerView.NO_POSITION) {
                    if (likedMode) {
                        ReceipeInfo likedElement = likedList.get(position);
                        ReceipeInfo origElement = receipeList.get(receipeList.indexOf(likedElement));
                        likeButton.setImageResource(R.drawable.ic_heart_outline_grey);
                        origElement.liked = false;
                    } else {
                        ReceipeInfo element = receipeList.get(position);
                        if (element.liked) {
                            likeButton.setImageResource(R.drawable.ic_heart_outline_grey);
                            element.liked = false;

                        } else {
                            likeButton.setImageResource(R.drawable.ic_heart_red);
                            element.liked = true;
                        }
                    }
                }
            }
        });

        return holder;
    }

    private List<ReceipeInfo> filterLikedReceipes() {
        List<ReceipeInfo> liked = new ArrayList<>();
        for (ReceipeInfo receipe : receipeList) {
            if (receipe.liked) {
                liked.add(receipe);
            }
        }
        return liked;
    }

    public class ReceipeViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView description;
        public ImageView image;
        public ImageButton liked;


        public ReceipeViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.receipe_name);
            description = (TextView) v.findViewById(R.id.receipe_description);
            image = (ImageView) v.findViewById(R.id.receipe_image);
            liked = (ImageButton) v.findViewById(R.id.btnLike);
        }
    }
}

