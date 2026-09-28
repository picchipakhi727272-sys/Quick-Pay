package com.quickpay.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {

    int green = Color.rgb(6, 103, 68);
    int yellow = Color.rgb(255, 199, 0);
    int bg = Color.rgb(247, 247, 247);
    int dark = Color.rgb(35, 35, 35);

    int dp(float n) {
        return (int)(n * getResources().getDisplayMetrics().density + 0.5f);
    }

    GradientDrawable box(int color, float radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(radius));
        return g;
    }

    TextView text(String s, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER);
        t.setTypeface(Typeface.create(
                "sans",
                bold ? Typeface.BOLD : Typeface.NORMAL
        ));
        return t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(green);
        getWindow().setNavigationBarColor(Color.WHITE);

        home();
    }

    void home() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(bg);

        // ================= HEADER =================

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(dp(18), dp(8), dp(18), dp(10));
        header.setBackground(box(green, 25));

        // TOP
        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);

        TextView logo = text(
                "Quick Pay",
                21,
                Color.rgb(20, 65, 125),
                true
        );

        logo.setBackground(box(Color.WHITE, 15));

        top.addView(
                logo,
                new LinearLayout.LayoutParams(dp(175), dp(55))
        );

        Space sp = new Space(this);

        top.addView(
                sp,
                new LinearLayout.LayoutParams(0, 1, 1)
        );

        top.addView(
                text("EN", 15, Color.WHITE, true),
                new LinearLayout.LayoutParams(dp(40), dp(50))
        );

        top.addView(
                text("♧", 30, Color.WHITE, false),
                new LinearLayout.LayoutParams(dp(45), dp(50))
        );

        top.addView(
                text("⇥", 31, Color.WHITE, false),
                new LinearLayout.LayoutParams(dp(45), dp(50))
        );

        header.addView(top);

        // USER + BALANCE
        LinearLayout userRow = new LinearLayout(this);
        userRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView user = text(
                "Rosy",
                26,
                Color.WHITE,
                true
        );

        user.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

        userRow.addView(
                user,
                new LinearLayout.LayoutParams(dp(155), dp(65))
        );

        LinearLayout balance = new LinearLayout(this);
        balance.setGravity(Gravity.CENTER_VERTICAL);
        balance.setPadding(dp(12), 0, dp(5), 0);
        balance.setBackground(box(yellow, 35));

        TextView balanceText = text(
                "মেইন ব্যালেন্স: • • • •\nড্রাইভ ব্যালেন্স: • • • •",
                12,
                Color.DKGRAY,
                true
        );

        balance.addView(
                balanceText,
                new LinearLayout.LayoutParams(0, dp(58), 1)
        );

        balance.addView(
                text("◉", 21, Color.DKGRAY, true),
                new LinearLayout.LayoutParams(dp(35), dp(58))
        );

        userRow.addView(
                balance,
                new LinearLayout.LayoutParams(0, dp(58), 1)
        );

        header.addView(userRow);

        // NOTICE
        TextView notice = text(
                "●   রাকিব রহমান (016********) ৭৯৯ টাকা এয়ারটেল রিচার্জ করলেন",
                12,
                Color.DKGRAY,
                false
        );

        notice.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        notice.setPadding(dp(10), 0, dp(8), 0);
        notice.setBackground(box(Color.WHITE, 15));

        header.addView(
                notice,
                new LinearLayout.LayoutParams(-1, dp(48))
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(-1, dp(215))
        );

        // ================= SERVICES =================

        LinearLayout services = new LinearLayout(this);
        services.setOrientation(LinearLayout.VERTICAL);
        services.setPadding(dp(12), dp(6), dp(12), dp(2));

        serviceRow(
                services,
                "👛", "ওয়ালেট\nডিপোজিট",
                "💵", "মোবাইল\nব্যাংকিং",
                "🏦", "ব্যাংক\nট্রান্সফার",
                "📱", "মোবাইল\nরিচার্জ"
        );

        serviceRow(
                services,
                "💬", "গ্রুপ\nচ্যাট",
                "🎁", "ইনভাইট\nবোনাস",
                "🧾", "বিল\nপে",
                "🏷", "বিশেষ\nঅফার"
        );

        serviceRow(
                services,
                "🎧", "কাস্টমার\nকেয়ার",
                "⭐", "কাস্টমার\nরিভিউ",
                "▶", "ভিডিও\nটিউটোরিয়াল",
                "👥", "আমাদের\nসম্পর্কে"
        );

        root.addView(
                services,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        // ================= OFFER =================

        LinearLayout offer = new LinearLayout(this);
        offer.setOrientation(LinearLayout.VERTICAL);
        offer.setPadding(dp(10), dp(4), dp(10), dp(4));
        offer.setBackground(box(green, 16));

        TextView offerTitle = text(
                "🎁  ডিপোজিট বোনাস অফার",
                17,
                Color.WHITE,
                true
        );

        offerTitle.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        offer.addView(
                offerTitle,
                new LinearLayout.LayoutParams(-1, dp(28))
        );

        TextView offerSub = text(
                "এখনই করুন, বোনাস নিয়ে নিন!",
                11,
                Color.WHITE,
                false
        );

        offerSub.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        offer.addView(
                offerSub,
                new LinearLayout.LayoutParams(-1, dp(22))
        );

        LinearLayout offerBoxes = new LinearLayout(this);
        offerBoxes.setGravity(Gravity.CENTER);
        offerBoxes.setWeightSum(3);

        smallOffer(offerBoxes, "৳১২৯৯", "৳৩৯৯");
        smallOffer(offerBoxes, "৳২৫৯৯", "৳৭৯৯");
        smallOffer(offerBoxes, "৳৪৯৯৯", "৳১৪৯৯");

        offer.addView(
                offerBoxes,
                new LinearLayout.LayoutParams(-1, dp(50))
        );

        root.addView(
                offer,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(105)
                )
        );

        // ================= BOTTOM =================

        LinearLayout bottom = new LinearLayout(this);
        bottom.setGravity(Gravity.CENTER);
        bottom.setBackgroundColor(Color.WHITE);

        bottomItem(bottom, "⌂", "হোম", true);
        bottomItem(bottom, "◷", "লেনদেন", false);
        bottomItem(bottom, "♙", "প্রোফাইল", false);

        root.addView(
                bottom,
                new LinearLayout.LayoutParams(-1, dp(65))
        );

        setContentView(root);
    }

    void serviceRow(
            LinearLayout parent,
            String i1, String n1,
            String i2, String n2,
            String i3, String n3,
            String i4, String n4) {

        LinearLayout row = new LinearLayout(this);
        row.setGravity(Gravity.CENTER);

        addService(row, i1, n1);
        addService(row, i2, n2);
        addService(row, i3, n3);
        addService(row, i4, n4);

        parent.addView(
                row,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );
    }

    void addService(
            LinearLayout row,
            String icon,
            String name) {

        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);

        TextView ic = text(
                icon,
                29,
                dark,
                false
        );

        TextView nm = text(
                name,
                12,
                dark,
                false
        );

        item.addView(
                ic,
                new LinearLayout.LayoutParams(-1, dp(40))
        );

        item.addView(
                nm,
                new LinearLayout.LayoutParams(-1, dp(38))
        );

        row.addView(
                item,
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );
    }

    void smallOffer(
            LinearLayout parent,
            String deposit,
            String bonus) {

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        box.setBackground(box(Color.WHITE, 10));

        TextView d = text(
                deposit,
                12,
                green,
                true
        );

        TextView b = text(
                "বোনাস " + bonus,
                10,
                Color.RED,
                true
        );

        box.addView(
                d,
                new LinearLayout.LayoutParams(-1, dp(24))
        );

        box.addView(
                b,
                new LinearLayout.LayoutParams(-1, dp(20))
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        dp(45),
                        1
                );

        p.setMargins(dp(3), 0, dp(3), 0);

        parent.addView(box, p);
    }

    void bottomItem(
            LinearLayout parent,
            String icon,
            String name,
            boolean active) {

        int color = active
                ? Color.rgb(210, 55, 55)
                : Color.GRAY;

        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);

        item.addView(
                text(icon, 27, color, false),
                new LinearLayout.LayoutParams(-1, dp(32))
        );

        item.addView(
                text(name, 12, color, active),
                new LinearLayout.LayoutParams(-1, dp(22))
        );

        parent.addView(
                item,
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );
    }
}
