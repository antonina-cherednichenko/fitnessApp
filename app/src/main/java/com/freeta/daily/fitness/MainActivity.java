package com.freeta.daily.fitness;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface robotoBold = Typeface.createFromAsset(this.getAssets(),
                "font/Roboto-Bold.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(this.getAssets(),
                "font/Roboto-Regular.ttf");

        TextView helloText = (TextView) findViewById(R.id.hello_text);
        helloText.setAlpha(0);
        final ObjectAnimator helloAnim = ObjectAnimator.ofFloat(helloText, View.ALPHA, 0, 1);
        helloAnim.setDuration(1750);


//        TextView followText = (TextView) findViewById(R.id.follow_text);
//        followText.setAlpha(0);
//        final ObjectAnimator followAnim = ObjectAnimator.ofFloat(followText, View.ALPHA, 0, 1);
//        followAnim.setDuration(1400);
//
//        TextView findText = (TextView) findViewById(R.id.find_text);
//        findText.setAlpha(0);
//        final ObjectAnimator findAnim = ObjectAnimator.ofFloat(findText, View.ALPHA, 0, 1);
//        findAnim.setDuration(1400);


//        TextView stayTunedText = (TextView) findViewById(R.id.stay_tuned_text);
//        stayTunedText.setAlpha(0);
//        final ObjectAnimator stayAnim = ObjectAnimator.ofFloat(stayTunedText, View.ALPHA, 0, 1);
//        stayAnim.setDuration(1400);


        helloAnim.start();
//        helloAnim.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                stayAnim.start();
//            }
//        });

//        findAnim.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                followAnim.start();
//            }
//        });
//
//        followAnim.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                stayAnim.start();
//            }
//        });

//        stayAnim.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//
//            }
//        });


        helloText.setTypeface(robotoBold);
//        findText.setTypeface(robotoRegular);
//        followText.setTypeface(robotoRegular);
//        stayTunedText.setTypeface(robotoRegular);
    }

    public void onInfoClicked(View view) {
        Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void onLoginClicked(View view) {
        Intent intent = new Intent(MainActivity.this, NavigationDrawerWithFragmentsActivity.class);
        startActivity(intent);
    }
}
