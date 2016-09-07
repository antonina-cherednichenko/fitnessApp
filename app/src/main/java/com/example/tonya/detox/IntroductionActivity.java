package com.example.tonya.detox;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by tonya on 8/21/16.
 */
public class IntroductionActivity extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add your slide's fragments here.ß
        // AppIntro will automatically generate the dots indicator and buttons.
        AppIntroFragment infoSlide1 = (AppIntroFragment.newInstance("Find", "Find latest detox receipts ", R.drawable.info_page1, Color.parseColor("#80CBC4")));
        AppIntroFragment infoSlide2 = (AppIntroFragment.newInstance("Form", "Form your individual program", R.drawable.info_page7, Color.parseColor("#80CBC4")));
        AppIntroFragment infoSlide3 = (AppIntroFragment.newInstance("Follow", "Follow your detoxication schedule", R.drawable.calendar, Color.parseColor("#80CBC4")));

        addSlide(infoSlide1);
        addSlide(infoSlide2);
        addSlide(infoSlide3);


        setZoomAnimation();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
