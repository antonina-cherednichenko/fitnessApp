package com.freeta.daily.fitness.detox_diet_program_info;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.freeta.daily.fitness.R;
import com.freeta.daily.fitness.detox_diet_data.ProgramInfo;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ProgramInfoActivity extends AppCompatActivity {

    private ProgramInfo receipe;

    private ImageView photoInstructionsView;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipe);

        Intent intent = getIntent();
        receipe = (ProgramInfo) intent.getSerializableExtra("receipe_info");

        photoInstructionsView = (ImageView) findViewById(R.id.instructions_image);
        Picasso.with(this).load(receipe.getPhotoInstructionsURL()).into(photoInstructionsView);

        mAttacher = new PhotoViewAttacher(photoInstructionsView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(receipe.getName());

        showInfoSnackbar(photoInstructionsView, getResources().getString(R.string.zoom_scroll_hint));

    }

    public void showInfoSnackbar(View view, String info) {
        Snackbar.make(view, info, Snackbar.LENGTH_LONG).show();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }


}
