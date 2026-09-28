package com.quickpay.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {

    private static final int BLUE = Color.rgb(8, 96, 190);
    private static final int GREEN = Color.rgb(0, 92, 68);
    private static final int YELLOW = Color.rgb(255, 190, 25);
    private static final int DARK = Color.rgb(35, 35, 35);

    private SharedPreferences pref;

    private int dp(float n) {
        return (int) (n * getResources().getDisplayMetrics().density + .5f);
    }

    private TextView tv(String s, float size, int color) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    private GradientDrawable bg(int color, float radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    private GradientDrawable outline(int color, int stroke, float radius) {
        GradientDrawable d = bg(color, radius);
        d.setStroke(dp(1), stroke);
        return d;
    }

    private void space(LinearLayout p, int h) {
        p.addView(new Space(this),
                new LinearLayout.LayoutParams(1, dp(h)));
    }

    private TextView button(String s, int color, int textColor) {
        TextView b = tv(s, 19, textColor);
        b.setGravity(Gravity.CENTER);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setBackground(bg(color, 12));
        return b;
    }

    private EditText input(String hint, boolean password) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextSize(17);
        e.setSingleLine(true);
        e.setPadding(dp(18), 0, dp(18), 0);
        e.setTextColor(DARK);
        e.setHintTextColor(Color.GRAY);
        e.setBackground(bg(Color.WHITE, 12));

        e.setInputType(
                password
                        ? InputType.TYPE_CLASS_NUMBER |
                          InputType.TYPE_NUMBER_VARIATION_PASSWORD
                        : InputType.TYPE_CLASS_PHONE
        );

        return e;
    }

    private EditText pinInput(String hint) {
        EditText e = input(hint, true);
        e.setGravity(Gravity.CENTER);
        e.setTextSize(20);
        e.setBackground(
                outline(
                        Color.rgb(248, 250, 253),
                        Color.rgb(215, 225, 235),
                        15
                )
        );
        return e;
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        pref = getSharedPreferences("quick_pay", Context.MODE_PRIVATE);

        if (pref.getBoolean("logged_in", false)) {

            if (pref.getString("pin", "").length() == 8) {
                showPinUnlock();
            } else {
                showPinSetup();
            }

        } else {
            showLogin();
        }
    }

    // =========================================================
    // LOGIN
    // =========================================================

    private void showLogin() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(
                dp(25), dp(18), dp(25), dp(18)
        );
        root.setBackgroundColor(BLUE);

        ScrollView sc = new ScrollView(this);
        sc.setFillViewport(true);
        sc.addView(root);
        setContentView(sc);

        TextView lang = tv("বাংলা     EN", 16, Color.WHITE);
        lang.setGravity(Gravity.CENTER);
        lang.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        lang.setBackground(
                bg(Color.rgb(55, 130, 205), 40)
        );

        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        dp(175), dp(50)
                );
        lp.gravity = Gravity.RIGHT;

        root.addView(lang, lp);

        space(root, 45);

        TextView logo = tv("Quick Pay", 32, BLUE);
        logo.setGravity(Gravity.CENTER);
        logo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        logo.setBackground(bg(Color.WHITE, 18));

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(300), dp(95)
                )
        );

        TextView sub = tv(
                "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম",
                16,
                Color.WHITE
        );

        sub.setGravity(Gravity.CENTER);

        root.addView(
                sub,
                new LinearLayout.LayoutParams(
                        -1, dp(45)
                )
        );

        space(root, 15);

        EditText phone = input("ফোন", false);

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1, dp(62)
                )
        );

        space(root, 14);

        LinearLayout passBox = new LinearLayout(this);
        passBox.setGravity(Gravity.CENTER_VERTICAL);
        passBox.setPadding(dp(5), 0, dp(5), 0);
        passBox.setBackground(bg(Color.WHITE, 12));

        EditText pass = input(
                "৬ ডিজিট পাসওয়ার্ড",
                true
        );

        pass.setBackgroundColor(Color.TRANSPARENT);

        passBox.addView(
                pass,
                new LinearLayout.LayoutParams(
                        0, dp(62), 1
                )
        );

        TextView eye = tv("◉", 23, BLUE);
        eye.setGravity(Gravity.CENTER);

        passBox.addView(
                eye,
                new LinearLayout.LayoutParams(
                        dp(52), dp(62)
                )
        );

        root.addView(
                passBox,
                new LinearLayout.LayoutParams(
                        -1, dp(62)
                )
        );

        eye.setOnClickListener(v -> {

            boolean hidden =
                    (pass.getInputType()
                            & InputType.TYPE_NUMBER_VARIATION_PASSWORD) != 0;

            pass.setInputType(
                    hidden
                            ? InputType.TYPE_CLASS_NUMBER
                            : InputType.TYPE_CLASS_NUMBER |
                              InputType.TYPE_NUMBER_VARIATION_PASSWORD
            );

            pass.setSelection(pass.length());
        });

        space(root, 20);

        TextView login =
                button("লগইন", Color.WHITE, BLUE);

        root.addView(
                login,
                new LinearLayout.LayoutParams(
                        -1, dp(60)
                )
        );

        login.setOnClickListener(v -> {

            String p =
                    phone.getText().toString().trim();

            String pw =
                    pass.getText().toString().trim();

            if (p.isEmpty()) {
                phone.setError("ফোন নম্বর দিন");
                return;
            }

            if (pw.isEmpty()) {
                pass.setError("পাসওয়ার্ড দিন");
                return;
            }

            String sp = pref.getString("phone", "");
            String sw = pref.getString("password", "");

            if (!sp.isEmpty()
                    && (!sp.equals(p) || !sw.equals(pw))) {

                Toast.makeText(
                        this,
                        "ফোন নম্বর বা পাসওয়ার্ড সঠিক নয়",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            pref.edit()
                    .putString("phone", p)
                    .putString("password", pw)
                    .putBoolean("logged_in", true)
                    .apply();

            if (pref.getString("pin", "").length() == 8) {
                showHome();
            } else {
                showPinSetup();
            }
        });

        TextView forgot =
                tv("পাসওয়ার্ড ভুলে গেছেন?", 16, Color.WHITE);

        forgot.setGravity(Gravity.CENTER);

        root.addView(
                forgot,
                new LinearLayout.LayoutParams(
                        -1, dp(48)
                )
        );

        forgot.setOnClickListener(
                v -> showForgotPassword()
        );

        TextView reg =
                tv(
                        "অ্যাকাউন্ট নেই?  রেজিস্টার করুন",
                        17,
                        Color.WHITE
                );

        reg.setGravity(Gravity.CENTER);
        reg.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        root.addView(
                reg,
                new LinearLayout.LayoutParams(
                        -1, dp(52)
                )
        );

        reg.setOnClickListener(
                v -> showRegister()
        );
    }

    // =========================================================
    // PIN
    // =========================================================

    private void showPinSetup() {
        showPinScreen(false);
    }

    private void showPinUnlock() {
        showPinScreen(true);
    }

    private void showPinScreen(boolean unlock) {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout screen =
                new LinearLayout(this);

        screen.setOrientation(
                LinearLayout.VERTICAL
        );

        screen.setGravity(Gravity.CENTER);

        screen.setPadding(
                dp(20), dp(15),
                dp(20), dp(15)
        );

        screen.setBackgroundColor(BLUE);

        setContentView(screen);

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        card.setPadding(
                dp(25), dp(28),
                dp(25), dp(20)
        );

        card.setBackground(
                bg(Color.rgb(250, 252, 250), 28)
        );

        screen.addView(
                card,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(unlock ? 480 : 500)
                )
        );

        TextView lock =
                tv("🔒", 50, BLUE);

        lock.setGravity(Gravity.CENTER);

        lock.setBackground(
                bg(Color.rgb(232, 240, 250), 70)
        );

        card.addView(
                lock,
                new LinearLayout.LayoutParams(
                        dp(105), dp(105)
                )
        );

        space(card, 18);

        TextView title =
                tv(
                        unlock
                                ? "পিন যাচাই করুন"
                                : "পিন সেট করুন",
                        26,
                        BLUE
                );

        title.setGravity(Gravity.CENTER);
        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1, dp(42)
                )
        );

        TextView sub =
                tv(
                        unlock
                                ? "আপনার ৮ ডিজিটের পিন দিন"
                                : "অ্যাপে ঢোকার জন্য ৮ ডিজিটের পিন দিন",
                        17,
                        Color.DKGRAY
                );

        sub.setGravity(Gravity.CENTER);

        card.addView(
                sub,
                new LinearLayout.LayoutParams(
                        -1, dp(42)
                )
        );

        space(card, 8);

        EditText p =
                pinInput(
                        unlock
                                ? "PIN"
                                : "৮ ডিজিট PIN"
                );

        card.addView(
                p,
                new LinearLayout.LayoutParams(
                        -1, dp(62)
                )
        );

        space(card, unlock ? 18 : 10);

        if (unlock) {

            TextView verify =
                    button(
                            "যাচাই করুন  ✓",
                            Color.TRANSPARENT,
                            BLUE
                    );

            verify.setBackground(
                    outline(
                            Color.TRANSPARENT,
                            Color.rgb(80, 145, 205),
                            16
                    )
            );

            card.addView(
                    verify,
                    new LinearLayout.LayoutParams(
                            -1, dp(58)
                    )
            );

            verify.setOnClickListener(v -> {

                if (p.getText().toString().trim()
                        .equals(pref.getString("pin", ""))) {

                    showHome();

                } else {

                    p.setError("ভুল PIN");
                }
            });

            TextView forgot =
                    tv(
                            "PIN ভুলে গেছেন?  লগইন করুন",
                            16,
                            BLUE
                    );

            forgot.setGravity(Gravity.CENTER);

            card.addView(
                    forgot,
                    new LinearLayout.LayoutParams(
                            -1, dp(48)
                    )
            );

            forgot.setOnClickListener(v -> {

                pref.edit()
                        .putBoolean("logged_in", false)
                        .apply();

                showLogin();
            });

        } else {

            EditText c =
                    pinInput("PIN আবার দিন");

            card.addView(
                    c,
                    new LinearLayout.LayoutParams(
                            -1, dp(60)
                    )
            );

            space(card, 16);

            TextView save =
                    button(
                            "পিন সেট করুন  ✓",
                            Color.TRANSPARENT,
                            BLUE
                    );

            save.setBackground(
                    outline(
                            Color.TRANSPARENT,
                            Color.rgb(80, 145, 205),
                            16
                    )
            );

            card.addView(
                    save,
                    new LinearLayout.LayoutParams(
                            -1, dp(58)
                    )
            );

            save.setOnClickListener(v -> {

                String a =
                        p.getText().toString().trim();

                String bb =
                        c.getText().toString().trim();

                if (a.length() != 8) {
                    p.setError(
                            "৮ ডিজিটের PIN দিন"
                    );
                    return;
                }

                if (!a.equals(bb)) {
                    c.setError(
                            "দুইটি PIN একই নয়"
                    );
                    return;
                }

                pref.edit()
                        .putString("pin", a)
                        .putBoolean("logged_in", true)
                        .apply();

                showHome();
            });
        }
    }

    // =========================================================
    // HOME
    // =========================================================

    private LinearLayout serviceRow(
            LinearLayout parent
    ) {

        LinearLayout r =
                new LinearLayout(this);

        r.setGravity(Gravity.CENTER);

        parent.addView(
                r,
                new LinearLayout.LayoutParams(
                        -1, 0, 1
                )
        );

        return r;
    }

    private void service(
            LinearLayout row,
            String icon,
            String title
    ) {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(Gravity.CENTER);

        TextView i =
                tv(icon, 27, DARK);

        i.setGravity(Gravity.CENTER);

        box.addView(
                i,
                new LinearLayout.LayoutParams(
                        -1, dp(38)
                )
        );

        TextView t =
                tv(title, 11, DARK);

        t.setGravity(Gravity.CENTER);

        box.addView(
                t,
                new LinearLayout.LayoutParams(
                        -1, dp(38)
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0, -1, 1
                );

        p.setMargins(
                dp(1), dp(1),
                dp(1), dp(1)
        );

        row.addView(box, p);

        if (title.equals("ওয়ালেট\nডিপোজিট")) {
            box.setOnClickListener(
                    v -> showWalletDeposit()
            );
        }

        if (title.equals("মোবাইল\nব্যাংকিং")) {
            box.setOnClickListener(
                    v -> showMobileBanking()
            );
        }
    }

    private void bonusBox(
            LinearLayout parent,
            String amount,
            String bonus
    ) {

        LinearLayout b =
                new LinearLayout(this);

        b.setOrientation(
                LinearLayout.VERTICAL
        );

        b.setGravity(Gravity.CENTER);

        b.setBackground(
                bg(Color.WHITE, 10)
        );

        TextView a =
                tv(amount, 13, DARK);

        a.setGravity(Gravity.CENTER);

        TextView x =
                tv(
                        bonus,
                        10,
                        Color.rgb(170, 40, 40)
                );

        x.setGravity(Gravity.CENTER);

        b.addView(
                a,
                new LinearLayout.LayoutParams(
                        -1, dp(22)
                )
        );

        b.addView(
                x,
                new LinearLayout.LayoutParams(
                        -1, dp(20)
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0, dp(44), 1
                );

        p.setMargins(
                dp(3), 0,
                dp(3), 0
        );

        parent.addView(b, p);
    }

    private void nav(
            LinearLayout parent,
            String s
    ) {

        TextView n =
                tv(s, 14, DARK);

        n.setGravity(Gravity.CENTER);

        parent.addView(
                n,
                new LinearLayout.LayoutParams(
                        0, dp(58), 1
                )
        );
    }

    private void showHome() {

        getWindow().setStatusBarColor(GREEN);
        getWindow().setNavigationBarColor(
                Color.WHITE
        );

        LinearLayout main =
                new LinearLayout(this);

        main.setOrientation(
                LinearLayout.VERTICAL
        );

        main.setBackgroundColor(
                Color.WHITE
        );

        setContentView(main);

        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.VERTICAL
        );

        header.setPadding(
                dp(10), dp(6),
                dp(10), dp(6)
        );

        header.setBackgroundColor(GREEN);

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1, dp(178)
                )
        );

        LinearLayout top =
                new LinearLayout(this);

        top.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.addView(
                top,
                new LinearLayout.LayoutParams(
                        -1, dp(50)
                )
        );

        TextView brand =
                tv("Quick Pay", 21, DARK);

        brand.setGravity(Gravity.CENTER);

        brand.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        brand.setBackground(
                bg(Color.WHITE, 15)
        );

        top.addView(
                brand,
                new LinearLayout.LayoutParams(
                        dp(145), dp(45)
                )
        );

        top.addView(
                new Space(this),
                new LinearLayout.LayoutParams(
                        0, 1, 1
                )
        );

        TextView en =
                tv("EN", 15, Color.WHITE);

        en.setGravity(Gravity.CENTER);

        top.addView(
                en,
                new LinearLayout.LayoutParams(
                        dp(38), dp(45)
                )
        );

        TextView bell =
                tv("🔔", 19, Color.WHITE);

        bell.setGravity(Gravity.CENTER);

        top.addView(
                bell,
                new LinearLayout.LayoutParams(
                        dp(42), dp(45)
                )
        );

        TextView out =
                tv("⇥", 25, Color.WHITE);

        out.setGravity(Gravity.CENTER);

        top.addView(
                out,
                new LinearLayout.LayoutParams(
                        dp(42), dp(45)
                )
        );

        out.setOnClickListener(v -> {

            pref.edit()
                    .putBoolean("logged_in", false)
                    .apply();

            showLogin();
        });

        LinearLayout user =
                new LinearLayout(this);

        user.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.addView(
                user,
                new LinearLayout.LayoutParams(
                        -1, dp(62)
                )
        );

        TextView name =
                tv(
                        pref.getString(
                                "name",
                                "Rosy"
                        ),
                        25,
                        Color.WHITE
                );

        name.setGravity(
                Gravity.CENTER_VERTICAL
        );

        name.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        user.addView(
                name,
                new LinearLayout.LayoutParams(
                        0, dp(62), 1
                )
        );

        TextView bal =
                tv(
                        "Main Balance  ৳ ১২,৫০০\n" +
                        "Drive Balance  ৳ ১৮০",
                        12,
                        DARK
                );

        bal.setGravity(Gravity.CENTER);

        bal.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        bal.setBackground(
                bg(YELLOW, 30)
        );

        user.addView(
                bal,
                new LinearLayout.LayoutParams(
                        dp(190), dp(56)
                )
        );

        TextView notice =
                tv(
                        "●  সর্বশেষ আপডেট: Quick Pay-এ স্বাগতম",
                        12,
                        Color.DKGRAY
                );

        notice.setGravity(
                Gravity.CENTER_VERTICAL
        );

        notice.setPadding(
                dp(10), 0, dp(5), 0
        );

        notice.setSingleLine(true);

        notice.setBackground(
                bg(Color.WHITE, 12)
        );

        header.addView(
                notice,
                new LinearLayout.LayoutParams(
                        -1, dp(38)
                )
        );

        LinearLayout services =
                new LinearLayout(this);

        services.setOrientation(
                LinearLayout.VERTICAL
        );

        services.setPadding(
                dp(12), dp(4),
                dp(12), dp(2)
        );

        main.addView(
                services,
                new LinearLayout.LayoutParams(
                        -1, 0, 1
                )
        );

        LinearLayout r =
                serviceRow(services);

        service(r, "👛", "ওয়ালেট\nডিপোজিট");
        service(r, "💵", "মোবাইল\nব্যাংকিং");
        service(r, "🏦", "ব্যাংক\nট্রান্সফার");
        service(r, "📱", "মোবাইল\nরিচার্জ");

        r = serviceRow(services);

        service(r, "💬", "গ্রুপ\nচ্যাট");
        service(r, "🎁", "ইনভাইট\nবোনাস");
        service(r, "🧾", "বিল\nপে");
        service(r, "🏷", "বিশেষ\nঅফার");

        r = serviceRow(services);

        service(r, "🎧", "কাস্টমার\nকেয়ার");
        service(r, "⭐", "কাস্টমার\nরিভিউ");
        service(r, "▶", "ভিডিও\nটিউটোরিয়াল");
        service(r, "👥", "কন্টাক্ট\nআস");

        LinearLayout bonus =
                new LinearLayout(this);

        bonus.setOrientation(
                LinearLayout.VERTICAL
        );

        bonus.setPadding(
                dp(10), dp(2),
                dp(10), dp(3)
        );

        bonus.setBackground(
                bg(GREEN, 16)
        );

        main.addView(
                bonus,
                new LinearLayout.LayoutParams(
                        -1, dp(98)
                )
        );

        TextView bt =
                tv(
                        "🎁  ডিপোজিট বোনাস অফার",
                        18,
                        Color.WHITE
                );

        bt.setGravity(
                Gravity.CENTER_VERTICAL
        );

        bt.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        bonus.addView(
                bt,
                new LinearLayout.LayoutParams(
                        -1, dp(32)
                )
        );

        TextView bs =
                tv(
                        "এখনই করুন, বোনাস নিয়ে নিন!",
                        11,
                        Color.WHITE
                );

        bonus.addView(
                bs,
                new LinearLayout.LayoutParams(
                        -1, dp(20)
                )
        );

        LinearLayout bb =
                new LinearLayout(this);

        bb.setGravity(Gravity.CENTER);

        bonus.addView(
                bb,
                new LinearLayout.LayoutParams(
                        -1, dp(42)
                )
        );

        bonusBox(
                bb,
                "৳ ৫০০",
                "বোনাস ৳ ৫০"
        );

        bonusBox(
                bb,
                "৳ ১০০০",
                "বোনাস ৳ ১০০"
        );

        bonusBox(
                bb,
                "৳ ২০০০",
                "বোনাস ৳ ২০০"
        );

        LinearLayout bottom =
                new LinearLayout(this);

        bottom.setGravity(Gravity.CENTER);

        bottom.setBackgroundColor(
                Color.WHITE
        );

        main.addView(
                bottom,
                new LinearLayout.LayoutParams(
                        -1, dp(58)
                )
        );

        nav(bottom, "⌂\nহোম");
        nav(bottom, "◷\nলেনদেন");
        nav(bottom, "♙\nপ্রোফাইল");
    }

    // =========================================================
    // WALLET DEPOSIT
    // =========================================================

    private void showWalletDeposit() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(
                Color.WHITE
        );

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.WHITE
        );

        setContentView(root);

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(8), 0,
                dp(8), 0
        );

        header.setBackgroundColor(BLUE);

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1, dp(62)
                )
        );

        TextView back =
                tv("‹", 38, Color.WHITE);

        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(52), dp(62)
                )
        );

        back.setOnClickListener(
                v -> showHome()
        );

        TextView title =
                tv(
                        "ওয়ালেট ডিপোজিট",
                        21,
                        Color.WHITE
                );

        title.setGravity(
                Gravity.CENTER_VERTICAL
        );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        header.addView(
                title,
                new LinearLayout.LayoutParams(
                        0, dp(62), 1
                )
        );

        ScrollView sc =
                new ScrollView(this);

        root.addView(
                sc,
                new LinearLayout.LayoutParams(
                        -1, 0, 1
                )
        );

        LinearLayout body =
                new LinearLayout(this);

        body.setOrientation(
                LinearLayout.VERTICAL
        );

        body.setPadding(
                dp(22), dp(32),
                dp(22), dp(25)
        );

        sc.addView(body);

        TextView t1 =
                tv(
                        "আপনার Quick Pay ওয়ালেটে টাকা জমা করুন।",
                        20,
                        DARK
                );

        t1.setGravity(Gravity.LEFT);

        body.addView(
                t1,
                new LinearLayout.LayoutParams(
                        -1, dp(80)
                )
        );

        TextView t2 =
                tv(
                        "ডিপোজিট করার জন্য মোবাইল ব্যাংকিং-এর মাধ্যমে নিচের নির্দেশনা অনুসরণ করুন।",
                        17,
                        DARK
                );

        t2.setGravity(Gravity.LEFT);

        body.addView(
                t2,
                new LinearLayout.LayoutParams(
                        -1, dp(105)
                )
        );

        TextView mb =
                button(
                        "মোবাইল ব্যাংকিং",
                        BLUE,
                        Color.WHITE
                );

        body.addView(
                mb,
                new LinearLayout.LayoutParams(
                        -1, dp(62)
                )
        );

        mb.setOnClickListener(
                v -> showMobileBanking()
        );
    }

    // =========================================================
    // MOBILE BANKING
    // =========================================================

    private void showMobileBanking() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(
                Color.WHITE
        );

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.WHITE
        );

        setContentView(root);

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(8), 0,
                dp(8), 0
        );

        header.setBackgroundColor(BLUE);

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1, dp(62)
                )
        );

        TextView back =
                tv("‹", 38, Color.WHITE);

        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(52), dp(62)
                )
        );

        back.setOnClickListener(
                v -> showWalletDeposit()
        );

        TextView title =
                tv(
                        "মোবাইল ব্যাংকিং",
                        21,
                        Color.WHITE
                );

        title.setGravity(
                Gravity.CENTER_VERTICAL
        );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        header.addView(
                title,
                new LinearLayout.LayoutParams(
                        0, dp(62), 1
                )
        );

        TextView more =
                tv("⌘", 24, Color.WHITE);

        more.setGravity(Gravity.CENTER);

        header.addView(
                more,
                new LinearLayout.LayoutParams(
                        dp(45), dp(62)
                )
        );

        ScrollView sc =
                new ScrollView(this);

        root.addView(
                sc,
                new LinearLayout.LayoutParams(
                        -1, 0, 1
                )
        );

        LinearLayout body =
                new LinearLayout(this);

        body.setOrientation(
                LinearLayout.VERTICAL
        );

        body.setPadding(
                dp(12), dp(12),
                dp(12), dp(10)
        );

        sc.addView(body);

        TextView notice =
                tv(
                        "●  মোবাইল ব্যাংকিং এর মাধ্যমে সহজেই ডিপোজিট করুন",
                        15,
                        DARK
                );

        notice.setPadding(
                dp(8), dp(5),
                dp(8), dp(5)
        );

        body.addView(
                notice,
                new LinearLayout.LayoutParams(
                        -1, dp(48)
                )
        );

        addProviderRow(
                body,
                "bKash",
                "বিকাশ পার্সোনাল নাম্বার",
                "01XXXXXXXXX"
        );

        addProviderRow(
                body,
                "Nagad",
                "নগদ পার্সোনাল নাম্বার",
                "01XXXXXXXXX"
        );

        addProviderRow(
                body,
                "Rocket",
                "রকেট পার্সোনাল নাম্বার",
                "01XXXXXXXXX"
        );

        addProviderRow(
                body,
                "Upay",
                "উপায় পার্সোনাল নাম্বার",
                "01XXXXXXXXX"
        );

        TextView auto =
                button(
                        "অটো ডিপোজিট  →",
                        BLUE,
                        Color.WHITE
                );

        root.addView(
                auto,
                new LinearLayout.LayoutParams(
                        -1, dp(60)
                )
        );

        LinearLayout.LayoutParams ap =
                (LinearLayout.LayoutParams)
                        auto.getLayoutParams();

        ap.setMargins(
                dp(12), dp(6),
                dp(12), dp(8)
        );

        auto.setOnClickListener(
                v -> showDepositForm()
        );
    }

    // =========================================================
    // PERSONAL NUMBER ROW
    // =========================================================

    private void addProviderRow(
            LinearLayout parent,
            String name,
            String subtitle,
            String number
    ) {

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.VERTICAL
        );

        row.setPadding(
                dp(12), dp(8),
                dp(12), dp(5)
        );

        row.setBackgroundColor(
                Color.WHITE
        );

        TextView n =
                tv(name, 20, BLUE);

        n.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        row.addView(
                n,
                new LinearLayout.LayoutParams(
                        -1, dp(30)
                )
        );

        TextView sub =
                tv(
                        subtitle,
                        14,
                        Color.DKGRAY
                );

        row.addView(
                sub,
                new LinearLayout.LayoutParams(
                        -1, dp(24)
                )
        );

        TextView num =
                tv(
                        number,
                        18,
                        DARK
                );

        num.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        row.addView(
                num,
                new LinearLayout.LayoutParams(
                        -1, dp(30)
                )
        );

        LinearLayout actions =
                new LinearLayout(this);

        actions.setGravity(
                Gravity.CENTER_VERTICAL
        );

        TextView copy =
                tv("কপি", 14, BLUE);

        copy.setGravity(Gravity.CENTER);

        copy.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        copy.setBackground(
                outline(
                        Color.TRANSPARENT,
                        BLUE,
                        10
                )
        );

        actions.addView(
                copy,
                new LinearLayout.LayoutParams(
                        dp(70), dp(36)
                )
        );

        TextView sm =
                tv(
                        "সেন্ড মানি",
                        14,
                        BLUE
                );

        sm.setGravity(Gravity.CENTER);

        sm.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        actions.addView(
                sm,
                new LinearLayout.LayoutParams(
                        0, dp(36), 1
                )
        );

        TextView co =
                tv(
                        "ক্যাশ আউট",
                        14,
                        GREEN
                );

        co.setGravity(Gravity.CENTER);

        co.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        actions.addView(
                co,
                new LinearLayout.LayoutParams(
                        0, dp(36), 1
                )
        );

        row.addView(
                actions,
                new LinearLayout.LayoutParams(
                        -1, dp(42)
                )
        );

        copy.setOnClickListener(v -> {

            ClipboardManager cb =
                    (ClipboardManager)
                            getSystemService(
                                    Context.CLIPBOARD_SERVICE
                            );

            cb.setPrimaryClip(
                    ClipData.newPlainText(
                            "Mobile Banking Number",
                            number
                    )
            );

            Toast.makeText(
                    this,
                    "পার্সোনাল নম্বর কপি হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();
        });

        sm.setOnClickListener(
                v -> showMoneyForm("সেন্ড মানি")
        );

        co.setOnClickListener(
                v -> showMoneyForm("ক্যাশ আউট")
        );

        parent.addView(
                row,
                new LinearLayout.LayoutParams(
                        -1, dp(125)
                )
        );

        Space d = new Space(this);

        d.setBackgroundColor(
                Color.rgb(225, 228, 232)
        );

        parent.addView(
                d,
                new LinearLayout.LayoutParams(
                        -1, dp(1)
                )
        );
    }

    // =========================================================
    // AUTO DEPOSIT
    // =========================================================

    private void showDepositForm() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(
                Color.WHITE
        );

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.rgb(248, 249, 251)
        );

        setContentView(root);

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(8), 0,
                dp(8), 0
        );

        header.setBackgroundColor(BLUE);

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1, dp(62)
                )
        );

        TextView back =
                tv("‹", 38, Color.WHITE);

        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(52), dp(62)
                )
        );

        back.setOnClickListener(
                v -> showMobileBanking()
        );

        TextView title =
                tv(
                        "অটো ডিপোজিট",
                        21,
                        Color.WHITE
                );

        title.setGravity(
                Gravity.CENTER_VERTICAL
        );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        header.addView(
                title,
                new LinearLayout.LayoutParams(
                        0, dp(62), 1
                )
        );

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(18), dp(18),
                dp(18), dp(20)
        );

        card.setBackground(
                bg(Color.WHITE, 18)
        );

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                        -1, -2
                );

        cp.setMargins(
                dp(12), dp(18),
                dp(12), 0
        );

        root.addView(card, cp);

        TextView info =
                tv(
                        "ডিপোজিট তথ্য দিন",
                        20,
                        BLUE
                );

        info.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                info,
                new LinearLayout.LayoutParams(
                        -1, dp(40)
                )
        );

        TextView min =
                tv(
                        "সর্বনিম্ন ডিপোজিট: ৳ ৫০০",
                        15,
                        Color.DKGRAY
                );

        card.addView(
                min,
                new LinearLayout.LayoutParams(
                        -1, dp(32)
                )
        );

        space(card, 8);

        Spinner provider =
                new Spinner(this);

        String[] ps = {
                "bKash",
                "Nagad",
                "Rocket",
                "Upay"
        };

        provider.setAdapter(
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        ps
                )
        );

        card.addView(
                provider,
                new LinearLayout.LayoutParams(
                        -1, dp(55)
                )
        );

        space(card, 10);

        TextView balTitle =
                tv(
                        "কোন ব্যালেন্সে জমা হবে?",
                        15,
                        DARK
                );

        card.addView(
                balTitle,
                new LinearLayout.LayoutParams(
                        -1, dp(30)
                )
        );

        RadioGroup rg =
                new RadioGroup(this);

        rg.setOrientation(
                RadioGroup.HORIZONTAL
        );

        RadioButton main =
                new RadioButton(this);

        main.setText("Main Balance");
        main.setChecked(true);

        RadioButton drive =
                new RadioButton(this);

        drive.setText("Drive Balance");

        rg.addView(
                main,
                new RadioGroup.LayoutParams(
                        0, dp(50), 1
                )
        );

        rg.addView(
                drive,
                new RadioGroup.LayoutParams(
                        0, dp(50), 1
                )
        );

        card.addView(rg);

        space(card, 8);

        EditText amount =
                input(
                        "টাকার পরিমাণ (সর্বনিম্ন ৳৫০০)",
                        false
                );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        card.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1, dp(58)
                )
        );

        space(card, 10);

        EditText trx =
                input(
                        "Transaction ID / TrxID",
                        false
                );

        card.addView(
                trx,
                new LinearLayout.LayoutParams(
                        -1, dp(58)
                )
        );

        space(card, 16);

        TextView submit =
                button(
                        "ডিপোজিট রিকোয়েস্ট  →",
                        BLUE,
                        Color.WHITE
                );

        card.addView(
                submit,
                new LinearLayout.LayoutParams(
                        -1, dp(58)
                )
        );

        submit.setOnClickListener(v -> {

            String raw =
                    amount.getText()
                            .toString()
                            .trim();

            String id =
                    trx.getText()
                            .toString()
                            .trim();

            if (raw.isEmpty()) {
                amount.setError(
                        "টাকার পরিমাণ দিন"
                );
                return;
            }

            if (id.isEmpty()) {
                trx.setError(
                        "Transaction ID দিন"
                );
                return;
            }

            try {

                double val =
                        Double.parseDouble(raw);

                if (val < 500) {
                    amount.setError(
                            "সর্বনিম্ন ৫০০ টাকা"
                    );
                    return;
                }

                String balance =
                        main.isChecked()
                                ? "Main Balance"
                                : "Drive Balance";

                new AlertDialog.Builder(this)
                        .setTitle(
                                "ডিপোজিট নিশ্চিত করুন"
                        )
                        .setMessage(
                                "Provider: "
                                        + provider.getSelectedItem()
                                        + "\nBalance: "
                                        + balance
                                        + "\nপরিমাণ: ৳ "
                                        + raw
                                        + "\nTrxID: "
                                        + id
                        )
                        .setNegativeButton(
                                "বাতিল",
                                null
                        )
                        .setPositiveButton(
                                "জমা দিন",
                                (d, w) ->
                                        Toast.makeText(
                                                this,
                                                "ডিপোজিট রিকোয়েস্ট গ্রহণ করা হয়েছে। API যুক্ত হলে আসল ব্যালেন্স আপডেট হবে।",
                                                Toast.LENGTH_LONG
                                        ).show()
                        )
                        .show();

            } catch (Exception e) {

                amount.setError(
                        "সঠিক টাকার পরিমাণ দিন"
                );
            }
        });
    }

    // =========================================================
    // SEND MONEY / CASH OUT
    // =========================================================

    private void showMoneyForm(String type) {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(
                Color.WHITE
        );

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.rgb(248, 249, 251)
        );

        setContentView(root);

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(8), 0,
                dp(8), 0
        );

        header.setBackgroundColor(BLUE);

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1, dp(62)
                )
        );

        TextView back =
                tv("‹", 38, Color.WHITE);

        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(52), dp(62)
                )
        );

        back.setOnClickListener(
                v -> showMobileBanking()
        );

        TextView title =
                tv(type, 21, Color.WHITE);

        title.setGravity(
                Gravity.CENTER_VERTICAL
        );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        header.addView(
                title,
                new LinearLayout.LayoutParams(
                        0, dp(62), 1
                )
        );

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(18), dp(20),
                dp(18), dp(20)
        );

        card.setBackground(
                bg(Color.WHITE, 18)
        );

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                        -1, -2
                );

        cp.setMargins(
                dp(12), dp(18),
                dp(12), 0
        );

        root.addView(card, cp);

        TextView info =
                tv(
                        "bKash / Nagad / Rocket / Upay",
                        16,
                        BLUE
                );

        info.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                info,
                new LinearLayout.LayoutParams(
                        -1, dp(34)
                )
        );

        TextView min =
                tv(
                        "সর্বনিম্ন লেনদেন: ৳ ৫০০",
                        15,
                        Color.DKGRAY
                );

        card.addView(
                min,
                new LinearLayout.LayoutParams(
                        -1, dp(30)
                )
        );

        space(card, 10);

        EditText number =
                input(
                        type.equals("ক্যাশ আউট")
                                ? "আপনার মোবাইল নম্বর"
                                : "যে নম্বরে পাঠাবেন",
                        false
                );

        card.addView(
                number,
                new LinearLayout.LayoutParams(
                        -1, dp(58)
                )
        );

        space(card, 12);

        EditText amount =
                input(
                        "টাকার পরিমাণ (সর্বনিম্ন ৳৫০০)",
                        false
                );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        card.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1, dp(58)
                )
        );

        space(card, 12);

        EditText reference =
                input(
                        "রেফারেন্স (ঐচ্ছিক)",
                        false
                );

        card.addView(
                reference,
                new LinearLayout.LayoutParams(
                        -1, dp(58)
                )
        );

        space(card, 16);

        TextView confirm =
                button(
                        type + "  →",
                        BLUE,
                        Color.WHITE
                );

        card.addView(
                confirm,
                new LinearLayout.LayoutParams(
                        -1, dp(58)
                )
        );

        confirm.setOnClickListener(v -> {

            String num =
                    number.getText()
                            .toString()
                            .trim();

            String raw =
                    amount.getText()
                            .toString()
                            .trim();

            if (num.isEmpty()) {
                number.setError(
                        "মোবাইল নম্বর দিন"
                );
                return;
            }

            if (raw.isEmpty()) {
                amount.setError(
                        "টাকার পরিমাণ দিন"
                );
                return;
            }

            try {

                double value =
                        Double.parseDouble(raw);

                if (value < 500) {
                    amount.setError(
                            "সর্বনিম্ন ৫০০ টাকা"
                    );
                    return;
                }

                new AlertDialog.Builder(this)
                        .setTitle(
                                type + " নিশ্চিত করুন"
                        )
                        .setMessage(
                                "নম্বর: "
                                        + num
                                        + "\nপরিমাণ: ৳ "
                                        + raw
                                        + "\n\nলেনদেনটি নিশ্চিত করতে OK চাপুন।"
                        )
                        .setNegativeButton(
                                "বাতিল",
                                null
                        )
                        .setPositiveButton(
                                "OK",
                                (d, w) ->
                                        Toast.makeText(
                                                this,
                                                "রিকোয়েস্ট গ্রহণ করা হয়েছে। Provider API পরে যুক্ত করা যাবে।",
                                                Toast.LENGTH_LONG
                                        ).show()
                        )
                        .show();

            } catch (Exception e) {

                amount.setError(
                        "সঠিক টাকার পরিমাণ দিন"
                );
            }
        });
    }

    // =========================================================
    // REGISTER
    // =========================================================

    private void showRegister() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setPadding(
                dp(22), dp(18),
                dp(22), dp(20)
        );

        root.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        root.setBackgroundColor(BLUE);

        ScrollView sc =
                new ScrollView(this);

        sc.setFillViewport(true);
        sc.addView(root);

        setContentView(sc);

        TextView logo =
                tv("Quick Pay", 29, BLUE);

        logo.setGravity(Gravity.CENTER);

        logo.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        logo.setBackground(
                bg(Color.WHITE, 18)
        );

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(260), dp(75)
                )
        );

        space(root, 12);

        EditText country =
                input(
                        "বাংলাদেশ  🇧🇩",
                        false
                );

        root.addView(
                country,
                new LinearLayout.LayoutParams(
                        -1, dp(55)
                )
        );

        space(root, 9);

        EditText agent =
                input(
                        "রিসেলার এজেন্ট কোড",
                        false
                );

        root.addView(
                agent,
                new LinearLayout.LayoutParams(
                        -1, dp(55)
                )
        );

        space(root, 9);

        EditText name =
                input(
                        "পূর্ণ নাম",
                        false
                );

        root.addView(
                name,
                new LinearLayout.LayoutParams(
                        -1, dp(55)
                )
        );

        space(root, 9);

        EditText phone =
                input(
                        "+880 ফোন নম্বর",
                        false
                );

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1, dp(55)
                )
        );

        space(root, 9);

        EditText pw =
                input(
                        "৬ ডিজিট পাসওয়ার্ড",
                        true
                );

        root.addView(
                pw,
                new LinearLayout.LayoutParams(
                        -1, dp(55)
                )
        );

        space(root, 9);

        EditText cpw =
                input(
                        "৬ ডিজিট পাসওয়ার্ড নিশ্চিত করুন",
                        true
                );

        root.addView(
                cpw,
                new LinearLayout.LayoutParams(
                        -1, dp(55)
                )
        );

        space(root, 16);

        TextView next =
                button(
                        "পরবর্তী",
                        Color.WHITE,
                        BLUE
                );

        root.addView(
                next,
                new LinearLayout.LayoutParams(
                        -1, dp(58)
                )
        );

        next.setOnClickListener(v -> {

            String p =
                    phone.getText()
                            .toString()
                            .trim();

            String a =
                    pw.getText()
                            .toString()
                            .trim();

            String c =
                    cpw.getText()
                            .toString()
                            .trim();

            if (name.getText()
                    .toString()
                    .trim()
                    .isEmpty()) {

                name.setError(
                        "পূর্ণ নাম দিন"
                );

                return;
            }

            if (p.isEmpty()) {

                phone.setError(
                        "ফোন নম্বর দিন"
                );

                return;
            }

            if (a.length() != 6) {

                pw.setError(
                        "৬ ডিজিটের পাসওয়ার্ড দিন"
                );

                return;
            }

            if (!a.equals(c)) {

                cpw.setError(
                        "পাসওয়ার্ড একই নয়"
                );

                return;
            }

            pref.edit()
                    .putString(
                            "name",
                            name.getText()
                                    .toString()
                                    .trim()
                    )
                    .putString(
                            "phone",
                            p
                    )
                    .putString(
                            "password",
                            a
                    )
                    .putBoolean(
                            "logged_in",
                            true
                    )
                    .apply();

            showPinSetup();
        });

        TextView back =
                tv(
                        "অ্যাকাউন্ট আছে?  লগইন",
                        16,
                        Color.WHITE
                );

        back.setGravity(Gravity.CENTER);

        root.addView(
                back,
                new LinearLayout.LayoutParams(
                        -1, dp(52)
                )
        );

        back.setOnClickListener(
                v -> showLogin()
        );
    }

    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    private void showForgotPassword() {

        final EditText p =
                new EditText(this);

        p.setHint("ফোন নম্বর");

        p.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        new AlertDialog.Builder(this)
                .setTitle(
                        "পাসওয়ার্ড পুনরুদ্ধার"
                )
                .setMessage(
                        "আপনার রেজিস্টার করা ফোন নম্বর দিন।"
                )
                .setView(p)
                .setPositiveButton(
                        "পরবর্তী",
                        (d, w) ->
                                Toast.makeText(
                                        this,
                                        "পাসওয়ার্ড রিসেট ফিচার পরে যুক্ত হবে",
                                        Toast.LENGTH_SHORT
                                ).show()
                )
                .setNegativeButton(
                        "বাতিল",
                        null
                )
                .show();
    }

    // =========================================================
    // BACK BUTTON
    // =========================================================

    @Override
    public void onBackPressed() {

        if (pref.getBoolean(
                "logged_in",
                false
        )) {

            showHome();

        } else {

            showLogin();
        }
    }
}
