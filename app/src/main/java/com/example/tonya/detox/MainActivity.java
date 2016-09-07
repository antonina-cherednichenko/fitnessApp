package com.example.tonya.detox;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        TextView followText = (TextView) findViewById(R.id.follow_text);
        TextView findText = (TextView) findViewById(R.id.find_text);
        TextView stayTunedText = (TextView) findViewById(R.id.stay_tuned_text);

        helloText.setTypeface(robotoBold);

        findText.setTypeface(robotoRegular);
        followText.setTypeface(robotoRegular);
        stayTunedText.setTypeface(robotoRegular);
    }

    public void onInfoClicked(View view) {
        Intent intent = new Intent(MainActivity.this, IntroductionActivity.class);
        startActivity(intent);
    }

    public void onLoginClicked(View view) {
        Intent intent = new Intent(MainActivity.this, ReceipesActivity.class);
        startActivity(intent);
    }
}
