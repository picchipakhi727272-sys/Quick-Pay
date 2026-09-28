package com.quickpay.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    int green = Color.rgb(0, 92, 65);
    int lightGreen = Color.rgb(232, 247, 241);
    int dark = Color.rgb(30, 40, 38);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(246, 248, 247));

        // ===== TOP HEADER =====
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(28, 30, 28, 24);
        header.setBackgroundColor(green);

        TextView logo = new TextView(this);
        logo.setText("QUICK PAY");
        logo.setTextColor(Color.WHITE);
        logo.setTextSize(24);
        logo.setTypeface(Typeface.DEFAULT_BOLD);

        TextView hello = new TextView(this);
        hello.setText("Welcome back, Rosy 👋");
        hello.setTextColor(Color.WHITE);
        hello.setTextSize(16);
        hello.setPadding(0, 18, 0, 8);

        TextView balance = new TextView(this);
        balance.setText("৳ 0.00   •   Available Balance");
        balance.setTextColor(dark);
        balance.setTextSize(18);
        balance.setTypeface(Typeface.DEFAULT_BOLD);
        balance.setGravity(Gravity.CENTER);
        balance.setPadding(15, 20, 15, 20);
        balance.setBackgroundColor(Color.rgb(255, 211, 62));

        header.addView(logo);
        header.addView(hello);
        header.addView(balance);

        root.addView(header);

        // ===== SCROLL AREA =====
        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(18, 18, 18, 18);

        TextView notice = new TextView(this);
        notice.setText("🔔  Welcome to Quick Pay • Fast & Secure Payments");
        notice.setTextColor(dark);
        notice.setTextSize(14);
        notice.setPadding(20, 20, 20, 20);
        notice.setBackgroundColor(Color.WHITE);

        content.addView(notice);

        TextView title = new TextView(this);
        title.setText("Quick Services");
        title.setTextSize(21);
        title.setTextColor(dark);
        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setPadding(5, 25, 5, 15);

        content.addView(title);

        // ===== SERVICE GRID =====
        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(2);
        grid.setPadding(0, 0, 0, 10);

        String[] services = {
                "💰 Wallet Deposit",
                "📱 Mobile Banking",
                "🏦 Bank Transfer",
                "📲 Mobile Recharge",
                "👥 Group Chat",
                "🎁 Invite Bonus",
                "💳 Bill Pay",
                "⭐ Special Offer",
                "🎧 Customer Care",
                "⭐ Customer Review",
                "🎥 Video Tutorial",
                "☎ Contact Us"
        };

        for (String service : services) {
            Button button = new Button(this);
            button.setText(service);
            button.setTextSize(14);
            button.setTextColor(dark);
            button.setAllCaps(false);
            button.setBackgroundColor(Color.WHITE);
            button.setPadding(8, 20, 8, 20);

            GridLayout.LayoutParams params =
                    new GridLayout.LayoutParams();

            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec =
                    GridLayout.spec(GridLayout.UNDEFINED, 1f);

            params.setMargins(7, 7, 7, 7);

            button.setLayoutParams(params);
            grid.addView(button);
        }

        content.addView(grid);

        // ===== PROMO =====
        TextView promo = new TextView(this);
        promo.setText("🎉  SPECIAL OFFER\nGet exciting rewards with Quick Pay!");
        promo.setTextSize(17);
        promo.setTextColor(Color.WHITE);
        promo.setTypeface(Typeface.DEFAULT_BOLD);
        promo.setGravity(Gravity.CENTER);
        promo.setPadding(20, 28, 20, 28);
        promo.setBackgroundColor(green);

        content.addView(promo);

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        // ===== BOTTOM NAVIGATION =====
        LinearLayout bottom = new LinearLayout(this);
        bottom.setOrientation(LinearLayout.HORIZONTAL);
        bottom.setGravity(Gravity.CENTER);
        bottom.setPadding(5, 8, 5, 8);
        bottom.setBackgroundColor(Color.WHITE);

        String[] nav = {"🏠\nHome", "📋\nTransactions", "👤\nProfile"};

        for (String item : nav) {
            TextView navItem = new TextView(this);
            navItem.setText(item);
            navItem.setTextSize(13);
            navItem.setTextColor(green);
            navItem.setGravity(Gravity.CENTER);
            navItem.setPadding(5, 10, 5, 10);

            bottom.addView(
                    navItem,
                    new LinearLayout.LayoutParams(
                            0,
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            1
                    )
            );
        }

        root.addView(bottom);

        setContentView(root);
    }
}
