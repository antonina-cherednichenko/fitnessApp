package com.cherednichenko.antonina.detoxdiet.detox_diet_programs_list;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cherednichenko.antonina.detoxdiet.NotificationService;
import com.cherednichenko.antonina.detoxdiet.R;
import com.cherednichenko.antonina.detoxdiet.db.ProgramsDatabaseHelper;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.DataProcessor;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;
import com.cherednichenko.antonina.detoxdiet.detox_diet_program_info.ProgramInfoActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tonya on 8/29/16.
 */
public class DetoxDietProgramsListAdapter extends RecyclerView.Adapter<DetoxDietProgramsListAdapter.ReceipeViewHolder> {

    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

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
                Calendar now = Calendar.getInstance();


                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        holder,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(((Activity) context).getFragmentManager(), "Datepickerdialog");
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
                        updateHeartButton(holder, true);
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

    public class ReceipeViewHolder extends RecyclerView.ViewHolder implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
        public TextView name;
        public TextView description;
        public ImageView image;
        public ImageButton liked;

        private int year;
        private int monthOfYear;
        private int dayOfMonth;

        public ReceipeViewHolder(View v) {
            super(v);

            name = (TextView) v.findViewById(R.id.receipe_name);
            description = (TextView) v.findViewById(R.id.receipe_description);
            image = (ImageView) v.findViewById(R.id.receipe_image);
            liked = (ImageButton) v.findViewById(R.id.btnLike);

        }

        @Override
        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent serviceIntent = new Intent(context, NotificationService.class);

            final int position = this.getAdapterPosition();
            ProgramInfo receipe = receipeList.get(position);
            serviceIntent.putExtra("receipe_info", receipe);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                    serviceIntent, PendingIntent.FLAG_ONE_SHOT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(this.year, this.monthOfYear, this.dayOfMonth, hourOfDay, minute, second);

            long scheduleTime = calendar.getTimeInMillis();
            alarm.set(
                    // This alarm will wake up the device when System.currentTimeMillis()
                    // equals the second argument value
                    alarm.RTC_WAKEUP,
                    scheduleTime,
                    pendingIntent
            );

            ProgramsDatabaseHelper databaseHelper = ProgramsDatabaseHelper.getInstance(context);
            databaseHelper.addEvent(receipe, scheduleTime);
        }

        @Override
        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            Calendar now = Calendar.getInstance();
            this.year = year;
            this.monthOfYear = monthOfYear;
            this.dayOfMonth = dayOfMonth;

            TimePickerDialog tpd = TimePickerDialog.newInstance(
                    this,
                    now.get(Calendar.HOUR),
                    0,
                    false
            );
            tpd.show(((Activity) context).getFragmentManager(), "Timepickerdialog");
        }
    }

    public void showLikedSnackbar(View view, String info) {
        Snackbar.make(((Activity) context).findViewById(R.id.receipes_fragment), info, Snackbar.LENGTH_SHORT).show();
    }

    private void updateHeartButton(final ReceipeViewHolder holder, boolean animated) {
        if (animated) {

            AnimatorSet animatorSet = new AnimatorSet();


            ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.liked, "rotation", 0f, 360f);
            rotationAnim.setDuration(300);
            rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

            ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.liked, "scaleX", 0.2f, 1f);
            bounceAnimX.setDuration(300);
            bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

            ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.liked, "scaleY", 0.2f, 1f);
            bounceAnimY.setDuration(300);
            bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
            bounceAnimY.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    holder.liked.setImageResource(R.drawable.ic_heart_red);
                }
            });

            animatorSet.play(rotationAnim);
            animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    //resetLikeAnimationState(holder);
                }
            });

            animatorSet.start();

        }
    }


}

