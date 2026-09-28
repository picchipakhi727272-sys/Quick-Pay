package com.quickpay.app;

import android.app.Activity;
import android.app.AlertDialog;
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

    private static final int BLUE = Color.rgb(8,96,190);
    private static final int GREEN = Color.rgb(0,92,68);
    private static final int YELLOW = Color.rgb(255,190,25);
    private static final int DARK = Color.rgb(35,35,35);

    private SharedPreferences pref;

    private String selectedMobileProvider = "বিকাশ";
    private String selectedAccountType = "পার্সোনাল";

    private int dp(float n) {
        return (int)(n * getResources().getDisplayMetrics().density + .5f);
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
        p.addView(
                new Space(this),
                new LinearLayout.LayoutParams(
                        1,
                        dp(h)
                )
        );
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        pref = getSharedPreferences(
                "quick_pay",
                Context.MODE_PRIVATE
        );

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

    /* =========================================================
       INPUT
       ========================================================= */

    private EditText input(String hint, boolean password) {

        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(17);
        e.setSingleLine(true);

        e.setPadding(
                dp(18),
                0,
                dp(18),
                0
        );

        e.setTextColor(DARK);
        e.setHintTextColor(Color.GRAY);
        e.setBackground(bg(Color.WHITE, 12));

        String h = hint == null ? "" : hint;

        boolean numeric =
                password
                || h.contains("ফোন")
                || h.contains("নম্বর")
                || h.contains("টাকার পরিমাণ")
                || h.contains("PIN")
                || h.contains("পিন");

        if (numeric) {

            if (password) {

                e.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                                | InputType.TYPE_NUMBER_VARIATION_PASSWORD
                );

            } else {

                e.setInputType(
                        InputType.TYPE_CLASS_PHONE
                );
            }

        } else {

            e.setInputType(
                    InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            );
        }

        return e;
    }

    private EditText pinInput(String hint) {

        EditText e = input(hint, true);

        e.setGravity(Gravity.CENTER);
        e.setTextSize(20);

        e.setBackground(
                outline(
                        Color.rgb(248,250,253),
                        Color.rgb(215,225,235),
                        15
                )
        );

        return e;
    }

    private TextView button(
            String s,
            int color,
            int textColor) {

        TextView b = tv(
                s,
                20,
                textColor
        );

        b.setGravity(Gravity.CENTER);

        b.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        b.setBackground(
                bg(color, 12)
        );

        return b;
    }

    /* =========================================================
       LOGIN
       ========================================================= */

    private void showLogin() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        root.setPadding(
                dp(25),
                dp(18),
                dp(25),
                dp(18)
        );

        root.setBackgroundColor(BLUE);

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.addView(root);
        setContentView(scroll);

        TextView lang =
                tv(
                        "বাংলা     EN",
                        16,
                        Color.WHITE
                );

        lang.setGravity(Gravity.CENTER);

        lang.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        lang.setBackground(
                bg(
                        Color.rgb(55,130,205),
                        40
                )
        );

        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        dp(175),
                        dp(50)
                );

        lp.gravity = Gravity.RIGHT;

        root.addView(lang, lp);

        space(root,45);

        TextView logo =
                tv(
                        "Quick Pay",
                        32,
                        BLUE
                );

        logo.setGravity(Gravity.CENTER);

        logo.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        logo.setBackground(
                bg(Color.WHITE,18)
        );

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(300),
                        dp(95)
                )
        );

        TextView sub =
                tv(
                        "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম",
                        16,
                        Color.WHITE
                );

        sub.setGravity(Gravity.CENTER);

        root.addView(
                sub,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        space(root,15);

        EditText phone =
                input(
                        "ফোন",
                        false
                );

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        space(root,14);

        LinearLayout passBox =
                new LinearLayout(this);

        passBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        passBox.setPadding(
                dp(5),
                0,
                dp(5),
                0
        );

        passBox.setBackground(
                bg(Color.WHITE,12)
        );

        EditText pass =
                input(
                        "৬ ডিজিট পাসওয়ার্ড",
                        true
                );

        pass.setBackgroundColor(
                Color.TRANSPARENT
        );

        passBox.addView(
                pass,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1
                )
        );

        TextView eye =
                tv(
                        "◉",
                        23,
                        BLUE
                );

        eye.setGravity(Gravity.CENTER);

        passBox.addView(
                eye,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(62)
                )
        );

        root.addView(
                passBox,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        eye.setOnClickListener(v -> {

            boolean hidden =
                    (pass.getInputType()
                            & InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                            != 0;

            if (hidden) {

                pass.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                );

            } else {

                pass.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                                | InputType.TYPE_NUMBER_VARIATION_PASSWORD
                );
            }

            pass.setSelection(
                    pass.length()
            );
        });

        space(root,20);

        TextView login =
                button(
                        "লগইন",
                        Color.WHITE,
                        BLUE
                );

        root.addView(
                login,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        login.setOnClickListener(v -> {

            String p =
                    phone.getText()
                            .toString()
                            .trim();

            String pw =
                    pass.getText()
                            .toString()
                            .trim();

            if (p.isEmpty()) {

                phone.setError(
                        "ফোন নম্বর দিন"
                );

                return;
            }

            if (pw.isEmpty()) {

                pass.setError(
                        "পাসওয়ার্ড দিন"
                );

                return;
            }

            String sp =
                    pref.getString(
                            "phone",
                            ""
                    );

            String sw =
                    pref.getString(
                            "password",
                            ""
                    );

            if (!sp.isEmpty()
                    && (!sp.equals(p)
                    || !sw.equals(pw))) {

                Toast.makeText(
                        this,
                        "ফোন নম্বর বা পাসওয়ার্ড সঠিক নয়",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            pref.edit()
                    .putString("phone",p)
                    .putString("password",pw)
                    .putBoolean("logged_in",true)
                    .apply();

            if (pref.getString("pin","").length() == 8) {

                showHome();

            } else {

                showPinSetup();
            }
        });

        TextView forgot =
                tv(
                        "পাসওয়ার্ড ভুলে গেছেন?",
                        16,
                        Color.WHITE
                );

        forgot.setGravity(Gravity.CENTER);

        root.addView(
                forgot,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(48)
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
                        -1,
                        dp(52)
                )
        );

        reg.setOnClickListener(
                v -> showRegister()
        );
    }

    /* =========================================================
       PIN SETUP
       ========================================================= */

    private void showPinSetup() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout screen =
                new LinearLayout(this);

        screen.setOrientation(
                LinearLayout.VERTICAL
        );

        screen.setGravity(Gravity.CENTER);

        screen.setPadding(
                dp(20),
                dp(15),
                dp(20),
                dp(15)
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
                dp(25),
                dp(28),
                dp(25),
                dp(22)
        );

        card.setBackground(
                bg(
                        Color.rgb(250,252,250),
                        28
                )
        );

        screen.addView(
                card,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(500)
                )
        );

        TextView lock =
                tv(
                        "🔒",
                        50,
                        BLUE
                );

        lock.setGravity(Gravity.CENTER);

        lock.setBackground(
                bg(
                        Color.rgb(232,240,250),
                        70
                )
        );

        card.addView(
                lock,
                new LinearLayout.LayoutParams(
                        dp(105),
                        dp(105)
                )
        );

        space(card,18);

        TextView title =
                tv(
                        "পিন সেট করুন",
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
                        -1,
                        dp(42)
                )
        );

        TextView sub =
                tv(
                        "অ্যাপে ঢোকার জন্য ৮ ডিজিটের পিন দিন",
                        17,
                        Color.DKGRAY
                );

        sub.setGravity(Gravity.CENTER);

        card.addView(
                sub,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)
                )
        );

        space(card,8);

        EditText p =
                pinInput("৮ ডিজিট PIN");

        EditText c =
                pinInput("PIN আবার দিন");

        card.addView(
                p,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        space(card,10);

        card.addView(
                c,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        space(card,16);

        TextView save =
                button(
                        "পিন সেট করুন  ✓",
                        Color.TRANSPARENT,
                        BLUE
                );

        save.setBackground(
                outline(
                        Color.TRANSPARENT,
                        Color.rgb(80,145,205),
                        16
                )
        );

        card.addView(
                save,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        save.setOnClickListener(v -> {

            String a =
                    p.getText()
                            .toString()
                            .trim();

            String b =
                    c.getText()
                            .toString()
                            .trim();

            if (a.length() != 8) {

                p.setError(
                        "৮ ডিজিটের PIN দিন"
                );

                return;
            }

            if (!a.equals(b)) {

                c.setError(
                        "দুইটি PIN একই নয়"
                );

                return;
            }

            pref.edit()
                    .putString("pin",a)
                    .putBoolean("logged_in",true)
                    .apply();

            showHome();
        });
    }

    /* =========================================================
       PIN UNLOCK
       ========================================================= */

    private void showPinUnlock() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout screen =
                new LinearLayout(this);

        screen.setOrientation(
                LinearLayout.VERTICAL
        );

        screen.setGravity(Gravity.CENTER);

        screen.setPadding(
                dp(20),
                dp(15),
                dp(20),
                dp(15)
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
                dp(25),
                dp(28),
                dp(25),
                dp(20)
        );

        card.setBackground(
                bg(
                        Color.rgb(250,252,250),
                        28
                )
        );

        screen.addView(
                card,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(480)
                )
        );

        TextView lock =
                tv(
                        "🔒",
                        50,
                        BLUE
                );

        lock.setGravity(Gravity.CENTER);

        lock.setBackground(
                bg(
                        Color.rgb(232,240,250),
                        70
                )
        );

        card.addView(
                lock,
                new LinearLayout.LayoutParams(
                        dp(105),
                        dp(105)
                )
        );

        space(card,18);

        TextView title =
                tv(
                        "পিন যাচাই করুন",
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
                        -1,
                        dp(42)
                )
        );

        TextView sub =
                tv(
                        "আপনার ৮ ডিজিটের পিন দিন",
                        17,
                        Color.DKGRAY
                );

        sub.setGravity(Gravity.CENTER);

        card.addView(
                sub,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)
                )
        );

        space(card,8);

        EditText p =
                pinInput("PIN");

        card.addView(
                p,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        space(card,18);

        TextView verify =
                button(
                        "যাচাই করুন  ✓",
                        Color.TRANSPARENT,
                        BLUE
                );

        verify.setBackground(
                outline(
                        Color.TRANSPARENT,
                        Color.rgb(80,145,205),
                        16
                )
        );

        card.addView(
                verify,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        verify.setOnClickListener(v -> {

            String saved =
                    pref.getString(
                            "pin",
                            ""
                    );

            if (p.getText()
                    .toString()
                    .trim()
                    .equals(saved)) {

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
                        -1,
                        dp(48)
                )
        );

        forgot.setOnClickListener(v -> {

            pref.edit()
                    .putBoolean("logged_in",false)
                    .apply();

            showLogin();
        });
    }

    /* =========================================================
       HOME SERVICE
       ========================================================= */

    private LinearLayout serviceRow(
            LinearLayout parent) {

        LinearLayout r =
                new LinearLayout(this);

        r.setGravity(
                Gravity.CENTER
        );

        parent.addView(
                r,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        return r;
    }

    private void service(
            LinearLayout row,
            String icon,
            String title) {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(
                Gravity.CENTER
        );

        TextView i =
                tv(
                        icon,
                        27,
                        DARK
                );

        i.setGravity(
                Gravity.CENTER
        );

        box.addView(
                i,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(38)
                )
        );

        TextView t =
                tv(
                        title,
                        11,
                        DARK
                );

        t.setGravity(
                Gravity.CENTER
        );

        box.addView(
                t,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(38)
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                );

        p.setMargins(
                dp(1),
                dp(1),
                dp(1),
                dp(1)
        );

        row.addView(box,p);

        if (title.contains("অ্যাড\nব্যালেন্স")) {

            box.setOnClickListener(
                    v -> showAddBalance()
            );

        } else if (
                title.contains("মোবাইল\nব্যাংকিং")
        ) {

            box.setOnClickListener(
                    v -> showMobileBanking()
            );
        }
    }

    private void bonusBox(
            LinearLayout parent,
            String amount,
            String bonus) {

        LinearLayout b =
                new LinearLayout(this);

        b.setOrientation(
                LinearLayout.VERTICAL
        );

        b.setGravity(
                Gravity.CENTER
        );

        b.setBackground(
                bg(Color.WHITE,10)
        );

        TextView a =
                tv(
                        amount,
                        13,
                        DARK
                );

        a.setGravity(Gravity.CENTER);

        TextView x =
                tv(
                        bonus,
                        10,
                        Color.rgb(170,40,40)
                );

        x.setGravity(Gravity.CENTER);

        b.addView(
                a,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(22)
                )
        );

        b.addView(
                x,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(20)
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        dp(44),
                        1
                );

        p.setMargins(
                dp(3),
                0,
                dp(3),
                0
        );

        parent.addView(b,p);
    }

    private void nav(
            LinearLayout parent,
            String s) {

        TextView n =
                tv(
                        s,
                        14,
                        DARK
                );

        n.setGravity(Gravity.CENTER);

        parent.addView(
                n,
                new LinearLayout.LayoutParams(
                        0,
                        dp(58),
                        1
                )
        );
    }

    /* =========================================================
       HOME
       ========================================================= */

    private void showHome() {

        getWindow().setStatusBarColor(GREEN);
        getWindow().setNavigationBarColor(Color.WHITE);

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
                dp(10),
                dp(6),
                dp(10),
                dp(6)
        );

        header.setBackgroundColor(
                GREEN
        );

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(178)
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
                        -1,
                        dp(50)
                )
        );

        TextView brand =
                tv(
                        "Quick Pay",
                        21,
                        DARK
                );

        brand.setGravity(Gravity.CENTER);

        brand.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        brand.setBackground(
                bg(Color.WHITE,15)
        );

        top.addView(
                brand,
                new LinearLayout.LayoutParams(
                        dp(145),
                        dp(45)
                )
        );

        top.addView(
                new Space(this),
                new LinearLayout.LayoutParams(
                        0,
                        1,
                        1
                )
        );

        TextView en =
                tv(
                        "EN",
                        15,
                        Color.WHITE
                );

        en.setGravity(Gravity.CENTER);

        top.addView(
                en,
                new LinearLayout.LayoutParams(
                        dp(38),
                        dp(45)
                )
        );

        TextView bell =
                tv(
                        "🔔",
                        19,
                        Color.WHITE
                );

        bell.setGravity(Gravity.CENTER);

        top.addView(
                bell,
                new LinearLayout.LayoutParams(
                        dp(42),
                        dp(45)
                )
        );

        TextView out =
                tv(
                        "⇥",
                        25,
                        Color.WHITE
                );

        out.setGravity(Gravity.CENTER);

        top.addView(
                out,
                new LinearLayout.LayoutParams(
                        dp(42),
                        dp(45)
                )
        );

        out.setOnClickListener(v -> {

            pref.edit()
                    .putBoolean("logged_in",false)
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
                        -1,
                        dp(62)
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
                        0,
                        dp(62),
                        1
                )
        );

        TextView bal =
                tv(
                        "Main Balance  ৳ ১২,৫০০\nDrive Balance  ৳ ১৮০",
                        12,
                        DARK
                );

        bal.setGravity(Gravity.CENTER);

        bal.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        bal.setBackground(
                bg(YELLOW,30)
        );

        user.addView(
                bal,
                new LinearLayout.LayoutParams(
                        dp(190),
                        dp(56)
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
                dp(10),
                0,
                dp(5),
                0
        );

        notice.setSingleLine(true);

        notice.setBackground(
                bg(Color.WHITE,12)
        );

        header.addView(
                notice,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(38)
                )
        );

        LinearLayout services =
                new LinearLayout(this);

        services.setOrientation(
                LinearLayout.VERTICAL
        );

        services.setPadding(
                dp(12),
                dp(4),
                dp(12),
                dp(2)
        );

        main.addView(
                services,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        LinearLayout r =
                serviceRow(services);

        service(
                r,
                "👛",
                "অ্যাড\nব্যালেন্স"
        );

        service(
                r,
                "💵",
                "মোবাইল\nব্যাংকিং"
        );

        service(
                r,
                "🏦",
                "ব্যাংক\nট্রান্সফার"
        );

        service(
                r,
                "📱",
                "মোবাইল\nরিচার্জ"
        );

        r = serviceRow(services);

        service(
                r,
                "💬",
                "গ্রুপ\nচ্যাট"
        );

        service(
                r,
                "🎁",
                "ইনভাইট\nবোনাস"
        );

        service(
                r,
                "🧾",
                "বিল\nপে"
        );

        service(
                r,
                "🏷",
                "বিশেষ\nঅফার"
        );

        r = serviceRow(services);

        service(
                r,
                "🎧",
                "কাস্টমার\nকেয়ার"
        );

        service(
                r,
                "⭐",
                "কাস্টমার\nরিভিউ"
        );

        service(
                r,
                "▶",
                "ভিডিও\nটিউটোরিয়াল"
        );

        service(
                r,
                "👥",
                "কন্টাক্ট\nআস"
        );

        LinearLayout bonus =
                new LinearLayout(this);

        bonus.setOrientation(
                LinearLayout.VERTICAL
        );

        bonus.setPadding(
                dp(10),
                dp(2),
                dp(10),
                dp(3)
        );

        bonus.setBackground(
                bg(GREEN,16)
        );

        main.addView(
                bonus,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(98)
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
                        -1,
                        dp(32)
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
                        -1,
                        dp(20)
                )
        );

        LinearLayout bb =
                new LinearLayout(this);

        bb.setGravity(Gravity.CENTER);

        bonus.addView(
                bb,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)
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
                        -1,
                        dp(58)
                )
        );

        nav(
                bottom,
                "⌂\nহোম"
        );

        nav(
                bottom,
                "◷\nলেনদেন"
        );

        nav(
                bottom,
                "♙\nপ্রোফাইল"
        );
    }

    /* =========================================================
       ADD BALANCE
       ========================================================= */

    private void showAddBalance() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

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

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(8),
                0,
                dp(8),
                0
        );

        header.setBackgroundColor(
                BLUE
        );

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        TextView back =
                tv(
                        "‹",
                        38,
                        Color.WHITE
                );

        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(62)
                )
        );

        back.setOnClickListener(
                v -> showHome()
        );

        TextView title =
                tv(
                        "অ্যাড ব্যালেন্স",
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
                        0,
                        dp(62),
                        1
                )
        );

        TextView info =
                tv(
                        "আপনার Quick Pay ওয়ালেটে টাকা যোগ করুন।\n\n"
                                + "টাকা যোগ করার জন্য নিচের অটো ডিপোজিট অপশন ব্যবহার করুন।",
                        17,
                        DARK
                );

        info.setPadding(
                dp(28),
                dp(35),
                dp(28),
                dp(20)
        );

        main.addView(
                info,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(150)
                )
        );

        TextView auto =
                button(
                        "অটো ডিপোজিট",
                        BLUE,
                        Color.WHITE
                );

        LinearLayout.LayoutParams ap =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                );

        ap.setMargins(
                dp(20),
                dp(10),
                dp(20),
                0
        );

        main.addView(
                auto,
                ap
        );

        auto.setOnClickListener(
                v -> showAutoDeposit()
        );
    }

    /* =========================================================
       AUTO DEPOSIT
       ========================================================= */

    private void showAutoDeposit() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.rgb(248,249,251)
        );

        setContentView(root);

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(8),
                0,
                dp(8),
                0
        );

        header.setBackgroundColor(
                BLUE
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        TextView back =
                tv(
                        "‹",
                        38,
                        Color.WHITE
                );

        back.setGravity(
                Gravity.CENTER
        );

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(62)
                )
        );

        back.setOnClickListener(
                v -> showAddBalance()
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
                        0,
                        dp(62),
                        1
                )
        );

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(14),
                dp(15),
                dp(14),
                dp(20)
        );

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        TextView notice =
                tv(
                        "নিচের যেকোনো নম্বরে টাকা পাঠান।\n"
                                + "টাকা পাঠানোর পর সঠিক টাকার পরিমাণ ও Transaction ID দিয়ে সাবমিট করুন।",
                        16,
                        DARK
                );

        notice.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(18)
        );

        notice.setBackground(
                bg(Color.WHITE,18)
        );

        content.addView(
                notice,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(105)
                )
        );

        space(content,12);

        addDepositProvider(
                content,
                "bKash",
                "বিকাশ পার্সোনাল",
                ""
        );

        addDepositProvider(
                content,
                "Nagad",
                "নগদ পার্সোনাল",
                ""
        );

        addDepositProvider(
                content,
                "Rocket",
                "রকেট পার্সোনাল",
                ""
        );

        addDepositProvider(
                content,
                "Upay",
                "উপায় পার্সোনাল",
                ""
        );

        space(content,12);

        TextView amountTitle =
                tv(
                        "টাকার পরিমাণ",
                        16,
                        BLUE
                );

        amountTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        content.addView(
                amountTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(32)
                )
        );

        EditText amount =
                input(
                        "টাকার পরিমাণ",
                        false
                );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        content.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(content,12);

        TextView trxTitle =
                tv(
                        "Transaction ID",
                        16,
                        BLUE
                );

        trxTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        content.addView(
                trxTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(32)
                )
        );

        EditText trx =
                input(
                        "Transaction ID লিখুন",
                        false
                );

        trx.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        );

        content.addView(
                trx,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(content,18);

        TextView submit =
                button(
                        "ডিপোজিট সাবমিট করুন",
                        BLUE,
                        Color.WHITE
                );

        content.addView(
                submit,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        submit.setOnClickListener(v -> {

            String money =
                    amount.getText()
                            .toString()
                            .trim();

            String transaction =
                    trx.getText()
                            .toString()
                            .trim();

            if (money.isEmpty()) {

                amount.setError(
                        "টাকার পরিমাণ দিন"
                );

                return;
            }

            if (transaction.isEmpty()) {

                trx.setError(
                        "Transaction ID দিন"
                );

                return;
            }

            try {

                double value =
                        Double.parseDouble(money);

                if (value <= 0) {

                    amount.setError(
                            "সঠিক টাকার পরিমাণ দিন"
                    );

                    return;
                }

            } catch (Exception e) {

                amount.setError(
                        "সঠিক টাকার পরিমাণ দিন"
                );

                return;
            }

            new AlertDialog.Builder(this)

                    .setTitle(
                            "ডিপোজিট সাবমিট"
                    )

                    .setMessage(
                            "টাকার পরিমাণ: ৳ "
                                    + money
                                    + "\nTransaction ID: "
                                    + transaction
                                    + "\n\nআপনার ডিপোজিট রিকোয়েস্ট সাবমিট করা হবে।"
                    )

                    .setNegativeButton(
                            "বাতিল",
                            null
                    )

                    .setPositiveButton(
                            "সাবমিট",
                            (dialog, which) -> {

                                Toast.makeText(
                                        this,
                                        "ডিপোজিট রিকোয়েস্ট সাবমিট হয়েছে। অ্যাডমিন ভেরিফাই করার পর ব্যালেন্স যোগ হবে।",
                                        Toast.LENGTH_LONG
                                ).show();

                                amount.setText("");
                                trx.setText("");
                            }
                    )

                    .show();
        });
    }

    /* =========================================================
       DEPOSIT PROVIDER
       ========================================================= */

    private void addDepositProvider(
            LinearLayout parent,
            String name,
            String subtitle,
            String number) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(12),
                dp(7),
                dp(12),
                dp(7)
        );

        card.setBackground(
                bg(Color.WHITE,14)
        );

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(86)
                );

        cardParams.setMargins(
                0,
                0,
                0,
                dp(7)
        );

        parent.addView(
                card,
                cardParams
        );

        TextView title =
                tv(
                        name + "  •  " + subtitle,
                        17,
                        BLUE
                );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(28)
                )
        );

        LinearLayout row =
                new LinearLayout(this);

        row.setGravity(
                Gravity.CENTER_VERTICAL
        );

        card.addView(
                row,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)
                )
        );

        TextView phone =
                tv(
                        number.isEmpty()
                                ? "নম্বর এখনো সেট করা হয়নি"
                                : number,
                        15,
                        number.isEmpty()
                                ? Color.GRAY
                                : DARK
                );

        phone.setGravity(
                Gravity.CENTER_VERTICAL
        );

        row.addView(
                phone,
                new LinearLayout.LayoutParams(
                        0,
                        dp(40),
                        1
                )
        );

        TextView copy =
                button(
                        "কপি",
                        BLUE,
                        Color.WHITE
                );

        row.addView(
                copy,
                new LinearLayout.LayoutParams(
                        dp(68),
                        dp(38)
                )
        );

        copy.setOnClickListener(v -> {

            if (number.isEmpty()) {

                Toast.makeText(
                        this,
                        "নম্বর এখনো সেট করা হয়নি",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            android.content.ClipboardManager clipboard =
                    (android.content.ClipboardManager)
                            getSystemService(
                                    Context.CLIPBOARD_SERVICE
                            );

            clipboard.setPrimaryClip(
                    android.content.ClipData.newPlainText(
                            "Company Number",
                            number
                    )
            );

            Toast.makeText(
                    this,
                    name + " নম্বর কপি হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    /* =========================================================
       MOBILE BANKING
       ========================================================= */

    private void showMobileBanking() {
        showMobileBanking(selectedMobileProvider);
    }

    private void showMobileBanking(String provider) {

        selectedMobileProvider = provider;

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout main =
                new LinearLayout(this);

        main.setOrientation(
                LinearLayout.VERTICAL
        );

        main.setBackgroundColor(
                Color.rgb(248,249,251)
        );

        setContentView(main);

        /* HEADER */

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(8),
                0,
                dp(8),
                0
        );

        header.setBackgroundColor(
                BLUE
        );

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        TextView back =
                tv(
                        "‹",
                        38,
                        Color.WHITE
                );

        back.setGravity(
                Gravity.CENTER
        );

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(62)
                )
        );

        back.setOnClickListener(
                v -> showHome()
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
                        0,
                        dp(62),
                        1
                )
        );

        TextView bell =
                tv(
                        "🔔",
                        19,
                        Color.WHITE
                );

        bell.setGravity(Gravity.CENTER);

        header.addView(
                bell,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(62)
                )
        );

        /* SCROLL */

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(12),
                dp(12),
                dp(12),
                dp(20)
        );

        scroll.addView(content);

        main.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        /* PROVIDER TITLE */

        TextView providerTitle =
                tv(
                        "মোবাইল ব্যাংকিং নির্বাচন করুন",
                        16,
                        DARK
                );

        providerTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        providerTitle.setPadding(
                dp(4),
                dp(2),
                dp(4),
                dp(7)
        );

        content.addView(
                providerTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(34)
                )
        );

        /* PROVIDER ROW */

        LinearLayout providers =
                new LinearLayout(this);

        providers.setGravity(
                Gravity.CENTER
        );

        content.addView(
                providers,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(78)
                )
        );

        addMobileProviderTab(
                providers,
                "বিকাশ",
                "💗",
                "বিকাশ"
        );

        addMobileProviderTab(
                providers,
                "নগদ",
                "🟠",
                "নগদ"
        );

        addMobileProviderTab(
                providers,
                "রকেট",
                "🟣",
                "রকেট"
        );

        addMobileProviderTab(
                providers,
                "উপায়",
                "🟡",
                "উপায়"
        );

        space(content,10);

        /* FORM CARD */

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(16),
                dp(15),
                dp(16),
                dp(18)
        );

        card.setBackground(
                bg(Color.WHITE,18)
        );

        content.addView(
                card,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        TextView selected =
                tv(
                        "✓ " + selectedMobileProvider,
                        20,
                        BLUE
                );

        selected.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        selected.setGravity(
                Gravity.CENTER_VERTICAL
        );

        card.addView(
                selected,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)
                )
        );

        TextView accountTitle =
                tv(
                        "একাউন্ট ধরন",
                        15,
                        DARK
                );

        accountTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                accountTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(30)
                )
        );

        LinearLayout accountRow =
                new LinearLayout(this);

        accountRow.setGravity(
                Gravity.CENTER
        );

        card.addView(
                accountRow,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                )
        );

        TextView personal =
                mobileChoiceButton(
                        "পার্সোনাল",
                        selectedAccountType.equals("পার্সোনাল")
                );

        TextView agent =
                mobileChoiceButton(
                        "এজেন্ট",
                        selectedAccountType.equals("এজেন্ট")
                );

        accountRow.addView(
                personal,
                new LinearLayout.LayoutParams(
                        0,
                        dp(48),
                        1
                )
        );

        LinearLayout.LayoutParams agentParams =
                new LinearLayout.LayoutParams(
                        0,
                        dp(48),
                        1
                );

        agentParams.leftMargin = dp(8);

        accountRow.addView(
                agent,
                agentParams
        );

        personal.setOnClickListener(v -> {

            selectedAccountType = "পার্সোনাল";

            showMobileBanking(
                    selectedMobileProvider
            );
        });

        agent.setOnClickListener(v -> {

            selectedAccountType = "এজেন্ট";

            showMobileBanking(
                    selectedMobileProvider
            );
        });

        space(card,12);

        TextView numberTitle =
                tv(
                        "মোবাইল নম্বর",
                        15,
                        DARK
                );

        numberTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                numberTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(30)
                )
        );

        LinearLayout phoneBox =
                new LinearLayout(this);

        phoneBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        phoneBox.setBackground(
                outline(
                        Color.WHITE,
                        Color.rgb(220,225,232),
                        12
                )
        );

        card.addView(
                phoneBox,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        TextView country =
                tv(
                        "+88",
                        17,
                        BLUE
                );

        country.setGravity(
                Gravity.CENTER
        );

        country.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        phoneBox.addView(
                country,
                new LinearLayout.LayoutParams(
                        dp(55),
                        dp(58)
                )
        );

        EditText number =
                new EditText(this);

        number.setHint(
                "মোবাইল নম্বর লিখুন"
        );

        number.setTextSize(17);
        number.setSingleLine(true);
        number.setTextColor(DARK);
        number.setHintTextColor(Color.GRAY);
        number.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        number.setPadding(
                dp(8),
                0,
                dp(12),
                0
        );

        number.setBackgroundColor(
                Color.TRANSPARENT
        );

        phoneBox.addView(
                number,
                new LinearLayout.LayoutParams(
                        0,
                        dp(58),
                        1
                )
        );

        space(card,12);

        TextView amountTitle =
                tv(
                        "পরিমাণ লিখুন",
                        15,
                        DARK
                );

        amountTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                amountTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(30)
                )
        );

        EditText amount =
                new EditText(this);

        amount.setHint(
                "টাকার পরিমাণ লিখুন"
        );

        amount.setTextSize(17);
        amount.setSingleLine(true);
        amount.setTextColor(DARK);
        amount.setHintTextColor(Color.GRAY);

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        amount.setPadding(
                dp(18),
                0,
                dp(18),
                0
        );

        amount.setBackground(
                outline(
                        Color.WHITE,
                        Color.rgb(220,225,232),
                        12
                )
        );

        card.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(card,10);

        TextView quickTitle =
                tv(
                        "দ্রুত পরিমাণ নির্বাচন করুন",
                        14,
                        Color.DKGRAY
                );

        card.addView(
                quickTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(28)
                )
        );

        LinearLayout quickRow =
                new LinearLayout(this);

        quickRow.setGravity(
                Gravity.CENTER
        );

        card.addView(
                quickRow,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(48)
                )
        );

        addQuickAmount(
                quickRow,
                amount,
                "৳ 1,000"
        );

        addQuickAmount(
                quickRow,
                amount,
                "৳ 10,000"
        );

        addQuickAmount(
                quickRow,
                amount,
                "৳ 20,000"
        );

        addQuickAmount(
                quickRow,
                amount,
                "৳ 50,000"
        );

        space(card,16);

        TextView send =
                button(
                        "টাকা পাঠান  →",
                        BLUE,
                        Color.WHITE
                );

        card.addView(
                send,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        send.setOnClickListener(v -> {

            String num =
                    number.getText()
                            .toString()
                            .trim()
                            .replace(" ", "")
                            .replace("-", "");

            String money =
                    amount.getText()
                            .toString()
                            .trim();

            if (num.isEmpty()) {

                number.setError(
                        "মোবাইল নম্বর দিন"
                );

                return;
            }

            if (num.startsWith("+88")) {
                num = num.substring(3);
            }

            if (!num.matches("01[0-9]{9}")) {

                number.setError(
                        "সঠিক ১১ ডিজিটের মোবাইল নম্বর দিন"
                );

                return;
            }

            if (money.isEmpty()) {

                amount.setError(
                        "টাকার পরিমাণ দিন"
                );

                return;
            }

            try {

                double value =
                        Double.parseDouble(money);

                if (value < 500) {

                    amount.setError(
                            "সর্বনিম্ন ৫০০ টাকা"
                    );

                    return;
                }

                new AlertDialog.Builder(this)

                        .setTitle(
                                "টাকা পাঠানো নিশ্চিত করুন"
                        )

                        .setMessage(
                                "মাধ্যম: "
                                        + selectedMobileProvider
                                        + "\n"
                                        + "একাউন্ট: "
                                        + selectedAccountType
                                        + "\n"
                                        + "নম্বর: +88 "
                                        + num
                                        + "\n"
                                        + "পরিমাণ: ৳ "
                                        + money
                                        + "\n\n"
                                        + "রিকোয়েস্টটি গ্রহণ করা হবে। আসল টাকা পাঠানোর জন্য Provider API সংযুক্ত করতে হবে।"
                        )

                        .setNegativeButton(
                                "বাতিল",
                                null
                        )

                        .setPositiveButton(
                                "নিশ্চিত",
                                (dialog, which) -> {

                                    Toast.makeText(
                                            this,
                                            selectedMobileProvider
                                                    + " টাকা পাঠানোর রিকোয়েস্ট গ্রহণ করা হয়েছে।",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    number.setText("");
                                    amount.setText("");
                                }
                        )

                        .show();

            } catch (Exception e) {

                amount.setError(
                        "সঠিক টাকার পরিমাণ দিন"
                );
            }
        });
    }

    /* =========================================================
       MOBILE PROVIDER TAB
       ========================================================= */

    private void addMobileProviderTab(
            LinearLayout parent,
            String name,
            String icon,
            String provider) {

        boolean selected =
                selectedMobileProvider.equals(provider);

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(
                Gravity.CENTER
        );

        if (selected) {

            box.setBackground(
                    outline(
                            Color.rgb(238,246,255),
                            BLUE,
                            13
                    )
            );

        } else {

            box.setBackground(
                    outline(
                            Color.WHITE,
                            Color.rgb(220,225,232),
                            13
                    )
            );
        }

        TextView i =
                tv(
                        icon,
                        22,
                        DARK
                );

        i.setGravity(
                Gravity.CENTER
        );

        box.addView(
                i,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(30)
                )
        );

        TextView n =
                tv(
                        name,
                        13,
                        selected ? BLUE : DARK
                );

        n.setGravity(
                Gravity.CENTER
        );

        n.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        box.addView(
                n,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(27)
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        dp(72),
                        1
                );

        p.setMargins(
                dp(3),
                0,
                dp(3),
                0
        );

        parent.addView(
                box,
                p
        );

        box.setOnClickListener(v -> {

            selectedMobileProvider = provider;

            showMobileBanking(
                    provider
            );
        });
    }

    /* =========================================================
       ACCOUNT TYPE BUTTON
       ========================================================= */

    private TextView mobileChoiceButton(
            String text,
            boolean selected) {

        TextView b =
                tv(
                        text,
                        15,
                        selected ? BLUE : Color.DKGRAY
                );

        b.setGravity(
                Gravity.CENTER
        );

        b.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        if (selected) {

            b.setBackground(
                    outline(
                            Color.rgb(238,246,255),
                            BLUE,
                            12
                    )
            );

        } else {

            b.setBackground(
                    outline(
                            Color.WHITE,
                            Color.rgb(215,220,228),
                            12
                    )
            );
        }

        return b;
    }

    /* =========================================================
       QUICK AMOUNT
       ========================================================= */

    private void addQuickAmount(
            LinearLayout parent,
            EditText amount,
            String value) {

        TextView b =
                tv(
                        value,
                        12,
                        BLUE
                );

        b.setGravity(
                Gravity.CENTER
        );

        b.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        b.setBackground(
                outline(
                        Color.WHITE,
                        BLUE,
                        9
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        dp(42),
                        1
                );

        p.setMargins(
                dp(2),
                0,
                dp(2),
                0
        );

        parent.addView(
                b,
                p
        );

        b.setOnClickListener(v -> {

            String clean =
                    value
                            .replace("৳", "")
                            .replace(",", "")
                            .trim();

            amount.setText(clean);

            amount.setSelection(
                    amount.length()
            );
        });
    }

    /* =========================================================
       OLD MONEY FORM
       ========================================================= */

    private void showMoneyForm(
            String type) {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.rgb(248,249,251)
        );

        setContentView(root);

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(8),
                0,
                dp(8),
                0
        );

        header.setBackgroundColor(
                BLUE
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        TextView back =
                tv(
                        "‹",
                        38,
                        Color.WHITE
                );

        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(62)
                )
        );

        back.setOnClickListener(
                v -> showMobileBanking()
        );

        TextView title =
                tv(
                        type,
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
                        0,
                        dp(62),
                        1
                )
        );

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(18),
                dp(20),
                dp(18),
                dp(20)
        );

        card.setBackground(
                bg(Color.WHITE,18)
        );

        LinearLayout.LayoutParams cardp =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        cardp.setMargins(
                dp(12),
                dp(18),
                dp(12),
                0
        );

        root.addView(
                card,
                cardp
        );

        TextView info =
                tv(
                        "বিকাশ / নগদ / রকেট / উপায়",
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
                        -1,
                        dp(34)
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
                        -1,
                        dp(30)
                )
        );

        space(card,10);

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
                        -1,
                        dp(58)
                )
        );

        space(card,12);

        EditText amount =
                input(
                        "টাকার পরিমাণ (সর্বনিম্ন ৳৫০০)",
                        false
                );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        card.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(card,12);

        EditText reference =
                input(
                        "রেফারেন্স (ঐচ্ছিক)",
                        false
                );

        card.addView(
                reference,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(card,16);

        TextView confirm =
                button(
                        type + "  →",
                        BLUE,
                        Color.WHITE
                );

        card.addView(
                confirm,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
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
                                (d,w) -> {

                                    Toast.makeText(
                                            this,
                                            "রিকোয়েস্ট গ্রহণ করা হয়েছে। Provider API সংযুক্ত হলে আসল লেনদেন সম্পন্ন হবে।",
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                        )

                        .show();

            } catch(Exception e) {

                amount.setError(
                        "সঠিক টাকার পরিমাণ দিন"
                );
            }
        });
    }

    /* =========================================================
       REGISTRATION
       ========================================================= */

    private void showRegister() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setPadding(
                dp(22),
                dp(18),
                dp(22),
                dp(20)
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
                tv(
                        "Quick Pay",
                        29,
                        BLUE
                );

        logo.setGravity(Gravity.CENTER);

        logo.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        logo.setBackground(
                bg(Color.WHITE,18)
        );

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(260),
                        dp(75)
                )
        );

        space(root,12);

        TextView country =
                tv(
                        "বাংলাদেশ  🇧🇩",
                        18,
                        Color.GRAY
                );

        country.setGravity(
                Gravity.CENTER_VERTICAL
        );

        country.setPadding(
                dp(18),
                0,
                dp(18),
                0
        );

        country.setBackground(
                bg(Color.WHITE,12)
        );

        country.setFocusable(false);
        country.setClickable(false);

        root.addView(
                country,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        space(root,9);

        EditText agent =
                input(
                        "রিসেলার এজেন্ট কোড",
                        false
                );

        root.addView(
                agent,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        space(root,9);

        EditText name =
                input(
                        "পূর্ণ নাম",
                        false
                );

        root.addView(
                name,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        space(root,9);

        EditText phone =
                input(
                        "+880 ফোন নম্বর",
                        false
                );

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        space(root,9);

        EditText pw =
                input(
                        "৬ ডিজিট পাসওয়ার্ড",
                        true
                );

        root.addView(
                pw,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        space(root,9);

        EditText cpw =
                input(
                        "৬ ডিজিট পাসওয়ার্ড নিশ্চিত করুন",
                        true
                );

        root.addView(
                cpw,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        space(root,16);

        TextView next =
                button(
                        "পরবর্তী",
                        Color.WHITE,
                        BLUE
                );

        root.addView(
                next,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
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
                        -1,
                        dp(52)
                )
        );

        back.setOnClickListener(
                v -> showLogin()
        );
    }

    /* =========================================================
       FORGOT PASSWORD
       ========================================================= */

    private void showForgotPassword() {

        final EditText p =
                new EditText(this);

        p.setHint(
                "ফোন নম্বর"
        );

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
                        (d,w) ->
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

    /* =========================================================
       BACK BUTTON
       ========================================================= */

    @Override
    public void onBackPressed() {

        if (
                pref.getBoolean(
                        "logged_in",
                        false
                )
        ) {

            showHome();

        } else {

            showLogin();
        }
    }
}
