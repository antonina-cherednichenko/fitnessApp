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

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface robotoBold = Typeface.createFromAsset(this.getAssets(),
                "font/Roboto-Bold.ttf");

        TextView helloText = (TextView) findViewById(R.id.hello_text);
        TextView orText = (TextView) findViewById(R.id.or_text);
        helloText.setAlpha(0);
        final ObjectAnimator helloAnim = ObjectAnimator.ofFloat(helloText, View.ALPHA, 0, 1);
        helloAnim.setDuration(1750);

        helloAnim.start();

        helloText.setTypeface(robotoBold);
        orText.setTypeface(robotoBold);
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
