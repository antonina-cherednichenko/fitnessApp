package com.freeta.daily.fitness;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by tonya on 9/8/16.
 */
public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);
        String sourceLink = String.format("<font color=#00BFFF><u> %s</u></font>", "dietsanddetox@gmail.com");

        TextView about = (TextView) rootView.findViewById(R.id.email_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            about.setText(Html.fromHtml(sourceLink, Html.FROM_HTML_MODE_LEGACY));
        } else {
            about.setText(Html.fromHtml(sourceLink));
        }

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:dietsanddetox@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hello Detox & Diets app");

                intent.putExtra(Intent.EXTRA_TEXT, "Hi!");
                startActivity(Intent.createChooser(intent, "Email:"));
                startActivity(intent);
            }
        });


        return rootView;
    }

}
