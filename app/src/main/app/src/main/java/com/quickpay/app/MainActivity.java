package com.quickpay.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setGravity(Gravity.CENTER);
        main.setPadding(30, 30, 30, 30);
        main.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("Quick Pay");
        title.setTextSize(32);
        title.setTextColor(Color.rgb(20, 90, 200));
        title.setGravity(Gravity.CENTER);

        TextView subtitle = new TextView(this);
        subtitle.setText("দ্রুত ও সহজ মোবাইল সেবা");
        subtitle.setTextSize(18);
        subtitle.setTextColor(Color.DKGRAY);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 20, 0, 0);

        main.addView(title);
        main.addView(subtitle);

        setContentView(main);
    }
}
