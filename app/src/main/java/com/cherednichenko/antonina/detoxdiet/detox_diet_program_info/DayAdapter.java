package com.cherednichenko.antonina.detoxdiet.detox_diet_program_info;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cherednichenko.antonina.detoxdiet.R;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.DayInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by tonya on 8/29/16.
 */
public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {

    private List<DayInfo> dayList;
    private Context context;

    private static final int ONLY_IMAGE_TYPE = 1;
    private static final int IMAGE_AND_DESCRIPTION_TYPE = 2;
    private static final int ONLY_DESCRIPTION_TYPE = 3;

    public DayAdapter(Context context, List<DayInfo> dayList) {
        this.context = context;
        this.dayList = dayList;
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        DayInfo di = dayList.get(i);

        if (viewHolder.getItemViewType() == ONLY_IMAGE_TYPE) {
            ViewHolderOnlyImage holder = (ViewHolderOnlyImage) viewHolder;
            try {
                Picasso
                        .with(context)
                        .load(di.getPhoto())
                        .transform(new FitToTargetViewTransformation(holder.image))
                        //.fit()
                        .into(holder.image);

            } catch (Exception e) {
                System.out.println("Error while loading image " + di.getPhoto());
            }

            holder.name.setText(di.getName());
        } else if (viewHolder.getItemViewType() == ONLY_DESCRIPTION_TYPE) {
            ViewHolderOnlyDescription holder = (ViewHolderOnlyDescription) viewHolder;
            holder.name.setText(di.getName());
            holder.description.setText(di.getDescription());

        } else if (viewHolder.getItemViewType() == IMAGE_AND_DESCRIPTION_TYPE) {
            ViewHolderImageAndDescription holder = (ViewHolderImageAndDescription) viewHolder;

            try {
                Picasso
                        .with(context)
                        .load(di.getPhoto())
                        //.transform(new FitToTargetViewTransformation(holder.image))
                        .fit()
                        .into(holder.image);

            } catch (Exception e) {
                System.out.println("Error while loading image " + di.getPhoto());
            }
            holder.name.setText(di.getName());
            holder.description.setText(di.getDescription());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view;
        if (viewType == ONLY_IMAGE_TYPE) {
            view = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.day_layout_only_image, viewGroup, false);
            return new ViewHolderOnlyImage(view);
        } else if (viewType == IMAGE_AND_DESCRIPTION_TYPE) {
            view = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.day_layout_image_and_description, viewGroup, false);
            return new ViewHolderImageAndDescription(view);
            //ONLY_DESCRIPTION_TYPE
        } else {
            view = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.day_layout_only_description, viewGroup, false);
            return new ViewHolderOnlyDescription(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        DayInfo di = dayList.get(position);
        return di.getOnlyPhoto();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }


    //Define ViewHolders for different types of cards
    public class ViewHolderOnlyImage extends ViewHolder {
        TextView name;
        ImageView image;

        public ViewHolderOnlyImage(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.day_name);
            image = (ImageView) v.findViewById(R.id.day_image);
        }
    }

    public class ViewHolderOnlyDescription extends ViewHolder {
        TextView name;
        TextView description;

        public ViewHolderOnlyDescription(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.day_name);
            description = (TextView) v.findViewById(R.id.day_description);
        }
    }

    public class ViewHolderImageAndDescription extends ViewHolder {
        TextView name;
        TextView description;
        ImageView image;

        public ViewHolderImageAndDescription(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.day_name);
            description = (TextView) v.findViewById(R.id.day_description);
            image = (ImageView) v.findViewById(R.id.day_image);
        }
    }

    public class FitToTargetViewTransformation implements Transformation {
        private View view;

        public FitToTargetViewTransformation(View view) {
            this.view = view;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            int targetWidth = view.getWidth();

            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            if (source.getHeight() >= source.getWidth()) {
                return source;
            }


            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                // Same bitmap is returned if sizes are the same
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }
    }
}

