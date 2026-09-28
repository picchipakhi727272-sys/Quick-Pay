package com.quickpay.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(30, 30, 30, 30);
        root.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("Quick Pay");
        title.setTextSize(32);
        title.setTextColor(Color.rgb(0, 92, 65));
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);

        TextView subtitle = new TextView(this);
        subtitle.setText("আপনার পেমেন্ট সেবা এক জায়গায়");
        subtitle.setTextSize(17);
        subtitle.setTextColor(Color.DKGRAY);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 15, 0, 0);

        root.addView(title);
        root.addView(subtitle);

        setContentView(root);
    }
}
