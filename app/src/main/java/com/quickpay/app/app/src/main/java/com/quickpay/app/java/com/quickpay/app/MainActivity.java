package com.quickpay.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class MainActivity extends Activity {

    int green = Color.rgb(7, 103, 68);
    int greenDark = Color.rgb(5, 82, 54);
    int yellow = Color.rgb(255, 201, 0);
    int bg = Color.rgb(247, 247, 247);
    int text = Color.rgb(35, 35, 35);

    int dp(float v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }

    GradientDrawable roundBg(int color, float radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(radius));
        return g;
    }

    TextView tv(String s, float size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER);
        t.setTypeface(Typeface.create("sans", bold ? Typeface.BOLD : Typeface.NORMAL));
        return t;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(green);
        getWindow().setNavigationBarColor(Color.WHITE);

        buildHome();
    }

    void buildHome() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(bg);

        // ================= HEADER =================

        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(dp(20), dp(18), dp(20), dp(18));
        header.setBackground(roundBg(green, 28));

        // TOP ROW
        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);

        TextView logo = tv("loadbazar", 20, Color.rgb(15, 70, 140), true);
        logo.setBackground(roundBg(Color.WHITE, 16));
        logo.setPadding(dp(20), dp(10), dp(20), dp(10));

        top.addView(logo,
                new LinearLayout.LayoutParams(dp(190), dp(70)));

        Space space = new Space(this);
        top.addView(space,
                new LinearLayout.LayoutParams(0, 1, 1));

        TextView en = tv("EN", 16, Color.WHITE, true);
        top.addView(en,
                new LinearLayout.LayoutParams(dp(45), dp(55)));

        TextView bell = tv("♧", 32, Color.WHITE, false);
        bell.setText("♧");
        top.addView(bell,
                new LinearLayout.LayoutParams(dp(48), dp(55)));

        TextView logout = tv("⇥", 34, Color.WHITE, false);
        top.addView(logout,
                new LinearLayout.LayoutParams(dp(48), dp(55)));

        header.addView(top);

        // USER + BALANCE
        LinearLayout userBalance = new LinearLayout(this);
        userBalance.setGravity(Gravity.CENTER_VERTICAL);
        userBalance.setPadding(0, dp(10), 0, dp(10));

        TextView user = tv("Rosy", 28, Color.WHITE, true);
        user.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

        userBalance.addView(user,
                new LinearLayout.LayoutParams(dp(170), dp(65)));

        LinearLayout balance = new LinearLayout(this);
        balance.setGravity(Gravity.CENTER_VERTICAL);
        balance.setPadding(dp(18), 0, dp(12), 0);
        balance.setBackground(roundBg(yellow, 40));

        TextView balanceText =
                tv("মেইন ব্যালেন্স: • • • • / উইথড্র ব্যালেন্স: • • • •",
                        14, Color.DKGRAY, true);

        balance.addView(balanceText,
                new LinearLayout.LayoutParams(0, dp(60), 1));

        TextView eye = tv("◉", 22, Color.DKGRAY, true);
        balance.addView(eye,
                new LinearLayout.LayoutParams(dp(40), dp(60)));

        userBalance.addView(balance,
                new LinearLayout.LayoutParams(0, dp(65), 1));

        header.addView(userBalance);

        // NOTICE
        TextView notice =
                tv("●   রাকিব রহমান (016********) ৭৯৯ টাকা এয়ারটেল রিচার্জ করলেন",
                        14, Color.DKGRAY, false);

        notice.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        notice.setPadding(dp(15), 0, dp(10), 0);
        notice.setBackground(roundBg(Color.WHITE, 18));

        header.addView(notice,
                new LinearLayout.LayoutParams(
                        -1, dp(58)));

        root.addView(header,
                new LinearLayout.LayoutParams(-1, dp(300)));

        // ================= SCROLL CONTENT =================

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(bg);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(20), dp(20), dp(20), dp(10));

        // ================= SERVICE CARD 1 =================

        LinearLayout card1 = serviceCard();

        addService(card1, "👛", "ওয়ালেট\nডিপোজিট");
        addService(card1, "💵", "মোবাইল\nব্যাংকিং");
        addService(card1, "🏦", "ব্যাংক\nট্রান্সফার");
        addService(card1, "📱", "মোবাইল\nরিচার্জ");

        content.addView(card1);

        // ================= SERVICE CARD 2 =================

        LinearLayout card2 = serviceCard();

        addService(card2, "💬", "গ্রুপ\nচ্যাট");
        addService(card2, "🎁", "ইনভাইট\nবোনাস");
        addService(card2, "🧾", "বিল\nপে");
        addService(card2, "🏷️", "বিশেষ\nঅফার");

        content.addView(card2);

        // ================= SERVICE CARD 3 =================

        LinearLayout card3 = serviceCard();

        addService(card3, "🎧", "কাস্টমার\nকেয়ার");
        addService(card3, "⭐", "কাস্টমার\nরিভিউ");
        addService(card3, "▶️", "ভিডিও\nটিউটোরিয়াল");
        addService(card3, "👥", "আমাদের\nসম্পর্কে");

        content.addView(card3);

        // ================= BONUS BANNER =================

        LinearLayout banner = new LinearLayout(this);
        banner.setOrientation(LinearLayout.VERTICAL);
        banner.setPadding(dp(12), dp(12), dp(12), dp(12));
        banner.setBackground(roundBg(greenDark, 20));

        TextView bannerTitle =
                tv("🎁  ডিপোজিট বোনাস অফার", 23, Color.WHITE, true);
        bannerTitle.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        TextView bannerSub =
                tv("এখনই করুন, বোনাস নিয়ে নিন! 🎉",
                        14, Color.WHITE, false);
        bannerSub.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        banner.addView(bannerTitle,
                new LinearLayout.LayoutParams(-1, dp(42)));

        banner.addView(bannerSub,
                new LinearLayout.LayoutParams(-1, dp(32)));

        LinearLayout offers = new LinearLayout(this);
        offers.setGravity(Gravity.CENTER);
        offers.setWeightSum(3);

        addOffer(offers, "১২৯৯", "৩৯৯");
        addOffer(offers, "২৫৯৯", "৭৯৯");
        addOffer(offers, "৪৯৯৯", "১৪৯৯");

        banner.addView(offers,
                new LinearLayout.LayoutParams(-1, dp(115)));

        content.addView(banner,
                new LinearLayout.LayoutParams(
                        -1, dp(210)));

        scroll.addView(content);

        root.addView(scroll,
                new LinearLayout.LayoutParams(
                        -1, 0, 1));

        // ================= BOTTOM NAV =================

        LinearLayout bottom = new LinearLayout(this);
        bottom.setGravity(Gravity.CENTER);
        bottom.setBackgroundColor(Color.WHITE);
        bottom.setPadding(0, dp(8), 0, dp(8));

        addBottom(bottom, "⌂", "হোম", true);
        addBottom(bottom, "◷", "লেনদেন", false);
        addBottom(bottom, "♙", "প্রোফাইল", false);

        root.addView(bottom,
                new LinearLayout.LayoutParams(-1, dp(82)));

        setContentView(root);
    }

    LinearLayout serviceCard() {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER);
        card.setPadding(dp(5), dp(8), dp(5), dp(8));
        card.setBackground(roundBg(Color.WHITE, 22));

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, dp(195));

        p.setMargins(0, 0, 0, dp(18));

        card.setLayoutParams(p);

        return card;
    }

    void addService(LinearLayout row, String icon, String name) {

        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);

        TextView ic = tv(icon, 38, Color.DKGRAY, false);

        TextView label = tv(name, 14, text, false);
        label.setGravity(Gravity.CENTER);
        label.setTypeface(Typeface.DEFAULT);

        item.addView(ic,
                new LinearLayout.LayoutParams(-1, dp(65)));

        item.addView(label,
                new LinearLayout.LayoutParams(-1, dp(55)));

        row.addView(item,
                new LinearLayout.LayoutParams(0, -1, 1));
    }

    void addOffer(LinearLayout parent, String deposit, String bonus) {

        LinearLayout offer = new LinearLayout(this);
        offer.setOrientation(LinearLayout.VERTICAL);
        offer.setGravity(Gravity.CENTER);
        offer.setPadding(dp(4), dp(5), dp(4), dp(5));
        offer.setBackground(roundBg(Color.WHITE, 14));

        TextView d =
                tv("৳ " + deposit, 18, greenDark, true);

        TextView d2 =
                tv("ডিপোজিট করুন", 11, Color.DKGRAY, false);

        TextView b =
                tv("পেয়ে যান ৳ " + bonus, 12, Color.rgb(190, 20, 20), true);

        offer.addView(d,
                new LinearLayout.LayoutParams(-1, dp(32)));

        offer.addView(d2,
                new LinearLayout.LayoutParams(-1, dp(25)));

        offer.addView(b,
                new LinearLayout.LayoutParams(-1, dp(30)));

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(0, dp(100), 1);

        p.setMargins(dp(4), 0, dp(4), 0);

        parent.addView(offer, p);
    }

    void addBottom(LinearLayout parent,
                   String icon,
                   String name,
                   boolean active) {

        LinearLayout item = new LinearLayout(this);
        item.setOrientation(LinearLayout.VERTICAL);
        item.setGravity(Gravity.CENTER);

        int c = active
                ? Color.rgb(220, 55, 55)
                : Color.GRAY;

        TextView i = tv(icon, 34, c, false);

        TextView n = tv(name, 14, c, active);

        item.addView(i,
                new LinearLayout.LayoutParams(-1, dp(40)));

        item.addView(n,
                new LinearLayout.LayoutParams(-1, dp(25)));

        parent.addView(item,
                new LinearLayout.LayoutParams(0, -1, 1));
    }
}
