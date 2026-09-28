package com.quickpay.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    int green = Color.rgb(0, 92, 65);
    int lightGreen = Color.rgb(232, 247, 241);
    int dark = Color.rgb(35, 45, 42);
    int gray = Color.rgb(110, 120, 116);
    int white = Color.WHITE;
    int yellow = Color.rgb(255, 193, 7);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(green);

        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(Color.rgb(246, 248, 247));

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        scroll.addView(root);

        // ================= HEADER =================

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(22, 22, 22, 22);
        header.setBackgroundColor(green);

        LinearLayout topRow = new LinearLayout(this);
        topRow.setGravity(Gravity.CENTER_VERTICAL);

        TextView logo = text("QUICK PAY", 23, white);
        logo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        topRow.addView(
                logo,
                new LinearLayout.LayoutParams(0, 60, 1)
        );

        TextView notification = text("🔔", 24, white);
        notification.setGravity(Gravity.CENTER);

        topRow.addView(
                notification,
                new LinearLayout.LayoutParams(60, 60)
        );

        TextView menu = text("⋮", 28, white);
        menu.setGravity(Gravity.CENTER);

        topRow.addView(
                menu,
                new LinearLayout.LayoutParams(50, 60)
        );

        header.addView(topRow);

        TextView hello = text("Hello, Rosy 👋", 20, white);
        hello.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        hello.setPadding(0, 14, 0, 3);

        header.addView(hello);

        TextView welcome = text("Welcome back to Quick Pay", 14,
                Color.rgb(220, 240, 232));

        header.addView(welcome);

        // ================= BALANCE =================

        LinearLayout balance = new LinearLayout(this);
        balance.setOrientation(LinearLayout.VERTICAL);
        balance.setPadding(22, 18, 22, 18);
        balance.setBackground(roundDrawable(Color.WHITE, 24));

        LinearLayout balanceTop = new LinearLayout(this);
        balanceTop.setGravity(Gravity.CENTER_VERTICAL);

        TextView balanceTitle = text("Available Balance", 14, gray);

        balanceTop.addView(
                balanceTitle,
                new LinearLayout.LayoutParams(0, 45, 1)
        );

        TextView eye = text("👁", 21, green);
        eye.setGravity(Gravity.CENTER);

        balanceTop.addView(
                eye,
                new LinearLayout.LayoutParams(55, 45)
        );

        balance.addView(balanceTop);

        TextView amount = text("৳ 10,000.00", 30, green);
        amount.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        balance.addView(amount);

        TextView withdraw = text("Withdrawable: ৳ 8,500.00", 13, gray);
        withdraw.setPadding(0, 5, 0, 0);

        balance.addView(withdraw);

        LinearLayout.LayoutParams balanceParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        balanceParams.setMargins(18, 18, 18, 0);

        root.addView(balance, balanceParams);

        // ================= NOTICE =================

        LinearLayout notice = new LinearLayout(this);
        notice.setGravity(Gravity.CENTER_VERTICAL);
        notice.setPadding(16, 12, 16, 12);
        notice.setBackground(roundDrawable(
                Color.rgb(255, 249, 220), 18));

        TextView bell = text("🔔", 20, dark);
        notice.addView(bell,
                new LinearLayout.LayoutParams(45, 45));

        TextView noticeText = text(
                "Important notice • Your account is active",
                14,
                dark);

        notice.addView(
                noticeText,
                new LinearLayout.LayoutParams(0, 45, 1)
        );

        LinearLayout.LayoutParams noticeParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        noticeParams.setMargins(18, 15, 18, 5);

        root.addView(notice, noticeParams);

        // ================= SERVICES TITLE =================

        TextView serviceTitle = text("Quick Services", 21, dark);
        serviceTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        LinearLayout.LayoutParams titleParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        titleParams.setMargins(20, 15, 20, 8);

        root.addView(serviceTitle, titleParams);

        // ================= GRID =================

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(3);
        grid.setPadding(12, 4, 12, 4);

        addService(grid, "💰", "Wallet\nDeposit");
        addService(grid, "📱", "Mobile\nBanking");
        addService(grid, "🏦", "Bank\nTransfer");

        addService(grid, "📲", "Mobile\nRecharge");
        addService(grid, "👥", "Group\nChat");
        addService(grid, "🎁", "Invite\nBonus");

        addService(grid, "🧾", "Bill\nPay");
        addService(grid, "🔥", "Special\nOffer");
        addService(grid, "🎧", "Customer\nCare");

        addService(grid, "⭐", "Customer\nReview");
        addService(grid, "🎥", "Video\nTutorial");
        addService(grid, "📞", "Contact\nUs");

        LinearLayout.LayoutParams gridParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        gridParams.setMargins(8, 0, 8, 0);

        root.addView(grid, gridParams);

        // ================= PROMO =================

        LinearLayout promo = new LinearLayout(this);
        promo.setOrientation(LinearLayout.VERTICAL);
        promo.setPadding(22, 20, 22, 20);
        promo.setBackground(roundDrawable(green, 22));

        TextView promoTitle =
                text("Special Offer 🎉", 21, white);

        promoTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        promo.addView(promoTitle);

        TextView promoText =
                text("Invite friends and earn exciting bonus rewards!",
                        14,
                        Color.rgb(225, 245, 236));

        promoText.setPadding(0, 8, 0, 12);

        promo.addView(promoText);

        Button invite = new Button(this);
        invite.setText("INVITE NOW");
        invite.setTextColor(green);
        invite.setTextSize(13);
        invite.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        invite.setBackground(roundDrawable(white, 30));

        invite.setOnClickListener(v ->
                Toast.makeText(
                        MainActivity.this,
                        "Invite feature coming soon",
                        Toast.LENGTH_SHORT
                ).show()
        );

        promo.addView(invite,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        50
                ));

        LinearLayout.LayoutParams promoParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        promoParams.setMargins(18, 15, 18, 15);

        root.addView(promo, promoParams);

        // ================= BOTTOM NAV =================

        LinearLayout bottom = new LinearLayout(this);
        bottom.setGravity(Gravity.CENTER);
        bottom.setPadding(5, 8, 5, 8);
        bottom.setBackgroundColor(Color.WHITE);

        addBottomItem(bottom, "⌂", "Home", true);
        addBottomItem(bottom, "↕", "Transactions", false);
        addBottomItem(bottom, "♙", "Profile", false);

        LinearLayout.LayoutParams bottomParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        75
                );

        root.addView(bottom, bottomParams);

        setContentView(scroll);
    }

    // ================= TEXT =================

    private TextView text(String value, float size, int color) {

        TextView t = new TextView(this);

        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);

        return t;
    }

    // ================= SERVICE CARD =================

    private void addService(GridLayout grid,
                            String icon,
                            String name) {

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(5, 12, 5, 10);
        card.setBackground(roundDrawable(Color.WHITE, 20));

        TextView iconText = text(icon, 28, green);
        iconText.setGravity(Gravity.CENTER);

        card.addView(iconText,
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        48
                ));

        TextView title = text(name, 12, dark);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        card.addView(title);

        card.setOnClickListener(v ->
                Toast.makeText(
                        MainActivity.this,
                        name.replace("\n", " ") +
                                " selected",
                        Toast.LENGTH_SHORT
                ).show()
        );

        GridLayout.LayoutParams params =
                new GridLayout.LayoutParams();

        params.width = 0;
        params.height = 125;

        params.columnSpec =
                GridLayout.spec(
                        GridLayout.UNDEFINED,
                        1f
                );

        params.setMargins(6, 6, 6, 6);

        grid.addView(card, params);
    }

    // ================= BOTTOM ITEM =================

    private void addBottomItem(LinearLayout parent,
                               String icon,
                               String name,
                               boolean active) {

        LinearLayout item = new LinearLayout(this);

        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);

        TextView iconText =
                text(icon, 25, active ? green : gray);

        iconText.setGravity(Gravity.CENTER);

        item.addView(iconText);

        TextView title =
                text(name, 12, active ? green : gray);

        title.setGravity(Gravity.CENTER);

        item.addView(title);

        parent.addView(
                item,
                new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1
                )
        );
    }

    // ================= ROUNDED BACKGROUND =================

    private GradientDrawable roundDrawable(int color,
                                           float radius) {

        GradientDrawable drawable =
                new GradientDrawable();

        drawable.setColor(color);
        drawable.setCornerRadius(radius);

        return drawable;
    }
}
