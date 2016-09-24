package com.cherednichenko.antonina.detoxdiet.detox_diet_programs_list;

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

import com.cherednichenko.antonina.detoxdiet.R;
import com.cherednichenko.antonina.detoxdiet.db.ProgramsDatabaseHelper;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;
import com.cherednichenko.antonina.detoxdiet.detox_diet_program_info.ProgramInfoActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by tonya on 8/29/16.
 */
public class DetoxDietProgramsListAdapter extends RecyclerView.Adapter<DetoxDietProgramsListAdapter.ReceipeViewHolder> {

    private List<ProgramInfo> receipeList;
    private Context context;


    public DetoxDietProgramsListAdapter(Context context, List<ProgramInfo> receipeList) {
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
        receipeViewHolder.description.setText(ri.getDescription());
        receipeViewHolder.image.setImageResource(ri.getPhotoId());
        receipeViewHolder.liked.setImageResource(ri.getLiked() == 1 ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_grey);

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
                    Intent intent = new Intent(context, ProgramInfoActivity.class);
                    intent.putExtra("receipe_info", receipeList.get(position));
                    context.startActivity(intent);
                }
            }
        });

        final Button scheduleButton = (Button) view.findViewById(R.id.schedule);
        scheduleButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                ProgramInfo receipe = receipeList.get(position);
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.item/event");

                Calendar cal = Calendar.getInstance();
                long startTime = cal.getTimeInMillis();
                long endTime = cal.getTimeInMillis() + receipe.getDuration() * 24 * 60 * 60 * 1000;

                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);

                intent.putExtra(CalendarContract.Events.TITLE, receipe.getName());
                intent.putExtra(CalendarContract.Events.DESCRIPTION, receipe.getShortDescription());

                context.startActivity(intent);
            }
        });

        final Button readMoreButton = (Button) view.findViewById(R.id.read_more);
        readMoreButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = holder.getAdapterPosition();
                //check if position exists
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, ProgramInfoActivity.class);
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
                    ProgramInfo element = receipeList.get(position);
                    ProgramsDatabaseHelper databaseHelper = ProgramsDatabaseHelper.getInstance(context);
                    if (element.getLiked() == 1) {
                        likeButton.setImageResource(R.drawable.ic_heart_outline_grey);
                        //update Db here
                        element.setLiked(0);
                        databaseHelper.updateLikedStatusOfProgram(element, 0);
                        showLikedSnackbar(v, element.getName() + " unliked!");

                    } else {
                        likeButton.setImageResource(R.drawable.ic_heart_red);
                        element.setLiked(1);
                        databaseHelper.updateLikedStatusOfProgram(element, 1);
                        showLikedSnackbar(v, element.getName() + " liked!");
                    }
                }
            }
        });

        return holder;
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

    public void showLikedSnackbar(View view, String info) {
        Snackbar.make(view, info, Snackbar.LENGTH_SHORT).show();
    }
}

