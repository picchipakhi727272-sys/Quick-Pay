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
import android.view.View;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    // =========================================================
    // COLORS
    // =========================================================

    private static final int BLUE   = Color.rgb(8, 96, 190);
    private static final int GREEN  = Color.rgb(0, 92, 68);
    private static final int YELLOW = Color.rgb(255, 190, 25);
    private static final int DARK   = Color.rgb(35, 35, 35);
    private static final int BG     = Color.rgb(248, 249, 251);

    // =========================================================
    // APP DATA
    // =========================================================

    private SharedPreferences pref;

    private String selectedMobileProvider = "বিকাশ";
    private String selectedAccountType = "পার্সোনাল";
    private String selectedRechargeOperator = "GP";

    private String selectedOfferOperator = "Grameenphone";

    private final String[] OFFER_OPERATORS = {
            "Grameenphone",
            "Robi",
            "Banglalink",
            "Teletalk"
    };

    // =========================================================
    // CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    // =========================================================
    // BASIC HELPERS
    // =========================================================

    private int dp(float value) {
        return (int) (
                value *
                getResources()
                        .getDisplayMetrics()
                        .density + 0.5f
        );
    }

    private TextView tv(
            String text,
            float size,
            int color
    ) {

        TextView t = new TextView(this);

        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(color);

        return t;
    }

    private GradientDrawable bg(
            int color,
            float radius
    ) {

        GradientDrawable d =
                new GradientDrawable();

        d.setColor(color);
        d.setCornerRadius(dp(radius));

        return d;
    }

    private GradientDrawable outline(
            int color,
            int stroke,
            float radius
    ) {

        GradientDrawable d =
                bg(color, radius);

        d.setStroke(dp(1), stroke);

        return d;
    }

    private void space(
            LinearLayout parent,
            int height
    ) {

        parent.addView(
                new Space(this),
                new LinearLayout.LayoutParams(
                        1,
                        dp(height)
                )
        );
    }

    private TextView button(
            String text,
            int color,
            int textColor
    ) {

        TextView b =
                tv(text, 18, textColor);

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

    private EditText input(
            String hint,
            boolean password
    ) {

        EditText e =
                new EditText(this);

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

        e.setBackground(
                bg(Color.WHITE, 12)
        );

        String h =
                hint == null ? "" : hint;

        boolean numeric =
                password
                        || h.contains("ফোন")
                        || h.contains("নম্বর")
                        || h.contains("টাকার")
                        || h.contains("PIN")
                        || h.contains("পিন");

        if (numeric) {

            if (password) {

                e.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                                |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
                );

            } else {

                e.setInputType(
                        InputType.TYPE_CLASS_PHONE
                );
            }

        } else {

            e.setInputType(
                    InputType.TYPE_CLASS_TEXT
                            |
                    InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            );
        }

        return e;
    }

    private EditText pinInput(
            String hint
    ) {

        EditText e =
                input(hint, true);

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

    // =========================================================
    // COMMON PAGE HEADER
    // =========================================================

    private void pageBack(
            LinearLayout header,
            String title
    ) {

        TextView back =
                tv("‹", 38, Color.WHITE);

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

        TextView t =
                tv(title, 21, Color.WHITE);

        t.setGravity(
                Gravity.CENTER_VERTICAL
        );

        t.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        header.addView(
                t,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1
                )
        );
    }

    // =========================================================
    // LOGIN
    // =========================================================

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

        TextView language =
                tv("বাংলা     EN", 16, Color.WHITE);

        language.setGravity(Gravity.CENTER);

        language.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        language.setBackground(
                bg(
                        Color.rgb(55, 130, 205),
                        40
                )
        );

        LinearLayout.LayoutParams langParam =
                new LinearLayout.LayoutParams(
                        dp(175),
                        dp(50)
                );

        langParam.gravity = Gravity.RIGHT;

        root.addView(
                language,
                langParam
        );

        space(root, 45);

        TextView logo =
                tv("Quick Pay", 32, BLUE);

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

        space(root, 15);

        EditText phone =
                input("ফোন", false);

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        space(root, 14);

        LinearLayout passwordBox =
                new LinearLayout(this);

        passwordBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        passwordBox.setPadding(
                dp(5),
                0,
                dp(5),
                0
        );

        passwordBox.setBackground(
                bg(Color.WHITE, 12)
        );

        EditText password =
                input("৬ ডিজিট পাসওয়ার্ড", true);

        password.setBackgroundColor(
                Color.TRANSPARENT
        );

        passwordBox.addView(
                password,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1
                )
        );

        TextView eye =
                tv("◉", 23, BLUE);

        eye.setGravity(Gravity.CENTER);

        passwordBox.addView(
                eye,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(62)
                )
        );

        root.addView(
                passwordBox,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        eye.setOnClickListener(v -> {

            boolean hidden =
                    (password.getInputType()
                            &
                            InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                            != 0;

            if (hidden) {

                password.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                );

            } else {

                password.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                                |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
                );
            }

            password.setSelection(
                    password.length()
            );
        });

        space(root, 20);

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
                    password.getText()
                            .toString()
                            .trim();

            if (p.isEmpty()) {
                phone.setError(
                        "ফোন নম্বর দিন"
                );
                return;
            }

            if (pw.isEmpty()) {
                password.setError(
                        "পাসওয়ার্ড দিন"
                );
                return;
            }

            String savedPhone =
                    pref.getString(
                            "phone",
                            ""
                    );

            String savedPassword =
                    pref.getString(
                            "password",
                            ""
                    );

            if (!savedPhone.isEmpty()
                    &&
                    (!savedPhone.equals(p)
                            ||
                     !savedPassword.equals(pw))) {

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

        TextView register =
                tv(
                        "অ্যাকাউন্ট নেই?  রেজিস্টার করুন",
                        17,
                        Color.WHITE
                );

        register.setGravity(Gravity.CENTER);

        register.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        root.addView(
                register,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                )
        );

        register.setOnClickListener(
                v -> showRegister()
        );
    }

    // =========================================================
    // PIN SETUP
    // =========================================================

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
                        Color.rgb(250, 252, 250),
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
                tv("🔒", 50, BLUE);

        lock.setGravity(Gravity.CENTER);

        lock.setBackground(
                bg(
                        Color.rgb(232, 240, 250),
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

        space(card, 18);

        TextView title =
                tv("পিন সেট করুন", 26, BLUE);

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

        space(card, 8);

        EditText pin =
                pinInput("৮ ডিজিট PIN");

        EditText confirm =
                pinInput("PIN আবার দিন");

        card.addView(
                pin,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        space(card, 10);

        card.addView(
                confirm,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
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
                        -1,
                        dp(58)
                )
        );

        save.setOnClickListener(v -> {

            String a =
                    pin.getText()
                            .toString()
                            .trim();

            String b =
                    confirm.getText()
                            .toString()
                            .trim();

            if (a.length() != 8) {
                pin.setError(
                        "৮ ডিজিটের PIN দিন"
                );
                return;
            }

            if (!a.equals(b)) {
                confirm.setError(
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

    // =========================================================
    // PIN UNLOCK
    // =========================================================

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
                        Color.rgb(250, 252, 250),
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
                tv("🔒", 50, BLUE);

        lock.setGravity(Gravity.CENTER);

        lock.setBackground(
                bg(
                        Color.rgb(232, 240, 250),
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

        space(card, 18);

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

        space(card, 8);

        EditText pin =
                pinInput("PIN");

        card.addView(
                pin,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        space(card, 18);

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
                        -1,
                        dp(58)
                )
        );

        verify.setOnClickListener(v -> {

            if (pin.getText()
                    .toString()
                    .trim()
                    .equals(
                            pref.getString(
                                    "pin",
                                    ""
                            )
                    )) {

                showHome();

            } else {

                pin.setError("ভুল PIN");
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
                    .putBoolean("logged_in", false)
                    .apply();

            showLogin();
        });
    }

    // =========================================================
    // HOME
    // =========================================================

    private LinearLayout serviceRow(
            LinearLayout parent
    ) {

        LinearLayout row =
                new LinearLayout(this);

        row.setGravity(Gravity.CENTER);

        parent.addView(
                row,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        return row;
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
                        -1,
                        dp(38)
                )
        );

        TextView t =
                tv(title, 11, DARK);

        t.setGravity(Gravity.CENTER);

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

        row.addView(box, p);

        if (title.contains("অ্যাড\nব্যালেন্স")) {

            box.setOnClickListener(
                    v -> showAddBalance()
            );

        } else if (title.contains("মোবাইল\nব্যাংকিং")) {

            box.setOnClickListener(
                    v -> showMobileBanking()
            );

        } else if (title.contains("ব্যাংক\nট্রান্সফার")) {

            box.setOnClickListener(
                    v -> showBankTransfer()
            );

        } else if (title.contains("মোবাইল\nরিচার্জ")) {

            box.setOnClickListener(
                    v -> showMobileRecharge()
            );

        } else if (title.contains("গ্রুপ\nচ্যাট")) {

            box.setOnClickListener(
                    v -> showGroupChat()
            );

        } else if (title.equals("অফার")) {

            box.setOnClickListener(
                    v -> showOfferPage()
            );
        }
    }

    private void bonusBox(
            LinearLayout parent,
            String amount,
            String bonus
    ) {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(Gravity.CENTER);

        box.setBackground(
                bg(Color.WHITE, 10)
        );

        TextView a =
                tv(amount, 13, DARK);

        a.setGravity(Gravity.CENTER);

        TextView b =
                tv(
                        bonus,
                        10,
                        Color.rgb(170, 40, 40)
                );

        b.setGravity(Gravity.CENTER);

        box.addView(
                a,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(22)
                )
        );

        box.addView(
                b,
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

        parent.addView(box, p);
    }

    private void nav(
            LinearLayout parent,
            String text,
            int which
    ) {

        TextView n =
                tv(text, 14, DARK);

        n.setGravity(Gravity.CENTER);

        parent.addView(
                n,
                new LinearLayout.LayoutParams(
                        0,
                        dp(58),
                        1
                )
        );

        n.setOnClickListener(v -> {

            if (which == 0) {

                showHome();

            } else if (which == 2) {

                showProfile();

            } else {

                Toast.makeText(
                        this,
                        "লেনদেন হিস্টোরি শিগগির যুক্ত হবে",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
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

        // HEADER
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

        header.setBackgroundColor(GREEN);

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
                tv("EN", 15, Color.WHITE);

        en.setGravity(Gravity.CENTER);

        top.addView(
                en,
                new LinearLayout.LayoutParams(
                        dp(38),
                        dp(45)
                )
        );

        TextView bell =
                tv("🔔", 19, Color.WHITE);

        bell.setGravity(Gravity.CENTER);

        top.addView(
                bell,
                new LinearLayout.LayoutParams(
                        dp(42),
                        dp(45)
                )
        );

        TextView logout =
                tv("⇥", 25, Color.WHITE);

        logout.setGravity(Gravity.CENTER);

        top.addView(
                logout,
                new LinearLayout.LayoutParams(
                        dp(42),
                        dp(45)
                )
        );

        logout.setOnClickListener(v -> {

            pref.edit()
                    .putBoolean("logged_in", false)
                    .apply();

            showLogin();
        });

        // USER AREA
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

        TextView balance =
                tv(
                        "Main Balance  ৳ ১২,৫০০\nDrive Balance  ৳ ১৮০",
                        12,
                        DARK
                );

        balance.setGravity(
                Gravity.CENTER
        );

        balance.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        balance.setBackground(
                bg(YELLOW, 30)
        );

        user.addView(
                balance,
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
                bg(Color.WHITE, 12)
        );

        header.addView(
                notice,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(38)
                )
        );

        // SERVICES
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

        LinearLayout row =
                serviceRow(services);

        service(
                row,
                "👛",
                "অ্যাড\nব্যালেন্স"
        );

        service(
                row,
                "💵",
                "মোবাইল\nব্যাংকিং"
        );

        service(
                row,
                "🏦",
                "ব্যাংক\nট্রান্সফার"
        );

        service(
                row,
                "📱",
                "মোবাইল\nরিচার্জ"
        );

        row = serviceRow(services);

        service(
                row,
                "💬",
                "গ্রুপ\nচ্যাট"
        );

        // IMPORTANT:
        // আগের Invite Bonus-এর জায়গায় Offer
        service(
                row,
                "🏷",
                "অফার"
        );

        service(
                row,
                "🧾",
                "বিল\nপে"
        );

        service(
                row,
                "🎧",
                "কাস্টমার\nকেয়ার"
        );

        row = serviceRow(services);

        service(
                row,
                "⭐",
                "কাস্টমার\nরিভিউ"
        );

        service(
                row,
                "▶",
                "ভিডিও\nটিউটোরিয়াল"
        );

        service(
                row,
                "👥",
                "কন্টাক্ট\nআস"
        );

        service(
                row,
                "ℹ",
                "আরও\nসেবা"
        );

        // BONUS AREA
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
                bg(GREEN, 16)
        );

        main.addView(
                bonus,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(98)
                )
        );

        TextView bonusTitle =
                tv(
                        "🎁  ডিপোজিট বোনাস অফার",
                        18,
                        Color.WHITE
                );

        bonusTitle.setGravity(
                Gravity.CENTER_VERTICAL
        );

        bonusTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        bonus.addView(
                bonusTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(32)
                )
        );

        TextView bonusSub =
                tv(
                        "এখনই করুন, বোনাস নিয়ে নিন!",
                        11,
                        Color.WHITE
                );

        bonus.addView(
                bonusSub,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(20)
                )
        );

        LinearLayout bonusRow =
                new LinearLayout(this);

        bonusRow.setGravity(Gravity.CENTER);

        bonus.addView(
                bonusRow,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)
                )
        );

        bonusBox(
                bonusRow,
                "৳ ৫০০",
                "বোনাস ৳ ৫০"
        );

        bonusBox(
                bonusRow,
                "৳ ১০০০",
                "বোনাস ৳ ১০০"
        );

        bonusBox(
                bonusRow,
                "৳ ২০০০",
                "বোনাস ৳ ২০০"
        );

        // BOTTOM NAV
        LinearLayout bottom =
                new LinearLayout(this);

        bottom.setGravity(
                Gravity.CENTER
        );

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

        nav(bottom, "⌂\nহোম", 0);
        nav(bottom, "◷\nলেনদেন", 1);
        nav(bottom, "♙\nপ্রোফাইল", 2);
    }

    // =========================================================
    // OFFER PAGE
    // =========================================================

    private void showOfferPage() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(
                Color.WHITE
        );

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(BG);

        setContentView(root);

        // HEADER
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

        header.setBackgroundColor(BLUE);

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        TextView back =
                tv("‹", 38, Color.WHITE);

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
                        "🏷 অফার",
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

        // DESIGN PRESERVED:
        // এখানে Admin button রাখা হয়নি।
        TextView spacer =
                tv("", 20, Color.WHITE);

        header.addView(
                spacer,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(62)
                )
        );

        // CONTENT
        ScrollView scroll =
                new ScrollView(this);

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(10),
                dp(10),
                dp(10),
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

        TextView intro =
                tv(
                        "🏷 অপারেটর বেছে নিয়ে আপনার পছন্দের অফার দেখুন",
                        17,
                        BLUE
                );

        intro.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        intro.setGravity(Gravity.CENTER_VERTICAL);

        intro.setPadding(
                dp(14),
                dp(12),
                dp(14),
                dp(12)
        );

        intro.setBackground(
                bg(Color.WHITE, 16)
        );

        content.addView(
                intro,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        space(content, 10);

        LinearLayout tabs =
                new LinearLayout(this);

        tabs.setGravity(Gravity.CENTER);

        content.addView(
                tabs,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        addOfferTab(
                tabs,
                "Grameenphone"
        );

        addOfferTab(
                tabs,
                "Robi"
        );

        addOfferTab(
                tabs,
                "Banglalink"
        );

        addOfferTab(
                tabs,
                "Teletalk"
        );

        space(content, 8);

        addOfferPlaceholder(
                content,
                "Grameenphone"
        );
    }

    private void addOfferTab(
            LinearLayout parent,
            String operator
    ) {

        boolean selected =
                selectedOfferOperator
                        .equals(operator);

        TextView tab =
                tv(
                        operator,
                        12,
                        selected ? BLUE : DARK
                );

        tab.setGravity(
                Gravity.CENTER
        );

        tab.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        tab.setBackground(
                outline(
                        selected
                                ? Color.rgb(238, 246, 255)
                                : Color.WHITE,
                        selected
                                ? BLUE
                                : Color.rgb(220, 225, 232),
                        12
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        dp(52),
                        1
                );

        p.setMargins(
                dp(3),
                0,
                dp(3),
                0
        );

        parent.addView(tab, p);

        tab.setOnClickListener(v -> {

            selectedOfferOperator =
                    operator;

            showOfferPage();
        });
    }

    private void addOfferPlaceholder(
            LinearLayout parent,
            String operator
    ) {

        int lightColor;

        if (operator.equals("Grameenphone")) {

            lightColor =
                    Color.rgb(220, 235, 255);

        } else if (operator.equals("Robi")) {

            lightColor =
                    Color.rgb(255, 235, 238);

        } else if (operator.equals("Banglalink")) {

            lightColor =
                    Color.rgb(255, 245, 220);

        } else {

            lightColor =
                    Color.rgb(230, 248, 232);
        }

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setGravity(
                Gravity.CENTER
        );

        card.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(16)
        );

        card.setBackground(
                bg(Color.WHITE, 18)
        );

        parent.addView(
                card,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(190)
                )
        );

        TextView operatorTitle =
                tv(
                        operator,
                        22,
                        BLUE
                );

        operatorTitle.setGravity(
                Gravity.CENTER
        );

        operatorTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        operatorTitle.setBackground(
                bg(lightColor, 16)
        );

        card.addView(
                operatorTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        TextView message =
                tv(
                        "এই অপারেটরের অফার এখানে দেখানো হবে",
                        15,
                        DARK
                );

        message.setGravity(
                Gravity.CENTER
        );

        message.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                message,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        TextView note =
                tv(
                        "অফারগুলো এখানে পরে সাজানো হবে",
                        12,
                        Color.GRAY
                );

        note.setGravity(Gravity.CENTER);

        card.addView(
                note,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(30)
                )
        );

        space(parent, 10);
    }

    // =========================================================
    // GROUP CHAT
    // =========================================================

    private void showGroupChat() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(
                Color.WHITE
        );

        LinearLayout main =
                new LinearLayout(this);

        main.setOrientation(
                LinearLayout.VERTICAL
        );

        main.setBackgroundColor(BG);

        setContentView(main);

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(5),
                0,
                dp(5),
                0
        );

        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        TextView back =
                tv("‹", 38, Color.WHITE);

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

        LinearLayout titleBox =
                new LinearLayout(this);

        titleBox.setOrientation(
                LinearLayout.VERTICAL
        );

        titleBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.addView(
                titleBox,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1
                )
        );

        TextView title =
                tv(
                        "গ্রুপ চ্যাট",
                        20,
                        Color.WHITE
                );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        titleBox.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(34)
                )
        );

        titleBox.addView(
                tv(
                        "Quick Pay Community",
                        11,
                        Color.WHITE
                ),
                new LinearLayout.LayoutParams(
                        -1,
                        dp(22)
                )
        );

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BG);

        LinearLayout chat =
                new LinearLayout(this);

        chat.setOrientation(
                LinearLayout.VERTICAL
        );

        chat.setPadding(
                dp(10),
                dp(12),
                dp(10),
                dp(12)
        );

        scroll.addView(chat);

        main.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        loadMessages(
                chat,
                scroll
        );

        LinearLayout bottom =
                new LinearLayout(this);

        bottom.setGravity(
                Gravity.CENTER_VERTICAL
        );

        bottom.setPadding(
                dp(8),
                dp(7),
                dp(8),
                dp(7)
        );

        bottom.setBackgroundColor(
                Color.WHITE
        );

        main.addView(
                bottom,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(70)
                )
        );

        EditText message =
                new EditText(this);

        message.setHint(
                "মেসেজ লিখুন..."
        );

        message.setTextSize(16);

        message.setSingleLine(false);

        message.setMaxLines(3);

        message.setPadding(
                dp(15),
                0,
                dp(15),
                0
        );

        message.setBackground(
                outline(
                        Color.rgb(247, 249, 252),
                        Color.rgb(215, 222, 232),
                        25
                )
        );

        bottom.addView(
                message,
                new LinearLayout.LayoutParams(
                        0,
                        dp(54),
                        1
                )
        );

        TextView send =
                tv("➤", 24, Color.WHITE);

        send.setGravity(Gravity.CENTER);

        send.setBackground(
                bg(BLUE, 50)
        );

        LinearLayout.LayoutParams sendParam =
                new LinearLayout.LayoutParams(
                        dp(54),
                        dp(54)
                );

        sendParam.leftMargin =
                dp(7);

        bottom.addView(
                send,
                sendParam
        );

        send.setOnClickListener(v -> {

            String text =
                    message.getText()
                            .toString()
                            .trim();

            if (text.isEmpty()) return;

            saveMessage(text);

            message.setText("");

            loadMessages(
                    chat,
                    scroll
            );
        });
    }

    private void saveMessage(
            String text
    ) {

        try {

            JSONArray array =
                    new JSONArray(
                            pref.getString(
                                    "group_chat_messages",
                                    "[]"
                            )
                    );

            JSONObject object =
                    new JSONObject();

            object.put(
                    "name",
                    pref.getString(
                            "name",
                            "Rosy"
                    )
            );

            object.put(
                    "message",
                    text
            );

            object.put(
                    "time",
                    new SimpleDateFormat(
                            "hh:mm a",
                            Locale.getDefault()
                    ).format(new Date())
            );

            array.put(object);

            pref.edit()
                    .putString(
                            "group_chat_messages",
                            array.toString()
                    )
                    .apply();

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "মেসেজ সেভ করা যায়নি",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void loadMessages(
            LinearLayout chat,
            ScrollView scroll
    ) {

        chat.removeAllViews();

        try {

            JSONArray array =
                    new JSONArray(
                            pref.getString(
                                    "group_chat_messages",
                                    "[]"
                            )
                    );

            if (array.length() == 0) {

                TextView empty =
                        tv(
                                "💬\nগ্রুপ চ্যাটে স্বাগতম\nপ্রথম মেসেজটি আপনিই পাঠান।",
                                18,
                                BLUE
                        );

                empty.setGravity(
                        Gravity.CENTER
                );

                chat.addView(
                        empty,
                        new LinearLayout.LayoutParams(
                                -1,
                                dp(220)
                        )
                );

            } else {

                for (int i = 0;
                     i < array.length();
                     i++) {

                    JSONObject o =
                            array.getJSONObject(i);

                    addMessage(
                            chat,
                            o.optString(
                                    "name",
                                    "User"
                            ),
                            o.optString(
                                    "message",
                                    ""
                            ),
                            o.optString(
                                    "time",
                                    ""
                            )
                    );
                }
            }

        } catch (Exception e) {

            TextView error =
                    tv(
                            "চ্যাট লোড করা যায়নি",
                            16,
                            Color.RED
                    );

            error.setGravity(
                    Gravity.CENTER
            );

            chat.addView(
                    error,
                    new LinearLayout.LayoutParams(
                            -1,
                            dp(60)
                    )
            );
        }

        chat.postDelayed(
                () -> scroll.fullScroll(
                        ScrollView.FOCUS_DOWN
                ),
                100
        );
    }

    private void addMessage(
            LinearLayout parent,
            String name,
            String message,
            String time
    ) {

        boolean mine =
                name.equals(
                        pref.getString(
                                "name",
                                "Rosy"
                        )
                );

        LinearLayout row =
                new LinearLayout(this);

        row.setGravity(
                mine
                        ? Gravity.RIGHT
                        : Gravity.LEFT
        );

        parent.addView(
                row,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        LinearLayout bubble =
                new LinearLayout(this);

        bubble.setOrientation(
                LinearLayout.VERTICAL
        );

        bubble.setPadding(
                dp(13),
                dp(8),
                dp(13),
                dp(8)
        );

        bubble.setBackground(
                bg(
                        mine
                                ? Color.rgb(225,240,255)
                                : Color.WHITE,
                        16
                )
        );

        row.addView(
                bubble,
                new LinearLayout.LayoutParams(
                        dp(285),
                        -2
                )
        );

        TextView user =
                tv(
                        mine ? "আপনি" : name,
                        13,
                        mine ? BLUE : GREEN
                );

        user.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        bubble.addView(
                user,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(24)
                )
        );

        bubble.addView(
                tv(message, 16, DARK)
        );

        TextView clock =
                tv(time, 10, Color.GRAY);

        clock.setGravity(
                mine
                        ? Gravity.RIGHT
                        : Gravity.LEFT
        );

        bubble.addView(
                clock,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(20)
                )
        );
    }

    // =========================================================
    // ADD BALANCE
    // =========================================================

    private void showAddBalance() {

        getWindow().setStatusBarColor(BLUE);

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

        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        pageBack(
                header,
                "অ্যাড ব্যালেন্স"
        );

        TextView info =
                tv(
                        "আপনার Quick Pay ওয়ালেটে টাকা যোগ করুন।\n\nটাকা যোগ করার জন্য নিচের অটো ডিপোজিট অপশন ব্যবহার করুন।",
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

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                );

        p.setMargins(
                dp(20),
                dp(10),
                dp(20),
                0
        );

        main.addView(auto, p);

        auto.setOnClickListener(
                v -> showAutoDeposit()
        );
    }

    // =========================================================
    // AUTO DEPOSIT
    // =========================================================

    private void showAutoDeposit() {

        getWindow().setStatusBarColor(BLUE);

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(BG);

        setContentView(root);

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setBackgroundColor(BLUE);

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        pageBack(
                header,
                "অটো ডিপোজিট"
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
                        "নিচের যেকোনো নম্বরে টাকা পাঠান।\nটাকা পাঠানোর পর সঠিক টাকার পরিমাণ ও Transaction ID দিয়ে সাবমিট করুন।",
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
                bg(Color.WHITE, 18)
        );

        content.addView(
                notice,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(105)
                )
        );

        space(content, 12);

        addDepositProvider(
                content,
                "bKash",
                "বিকাশ পার্সোনাল"
        );

        addDepositProvider(
                content,
                "Nagad",
                "নগদ পার্সোনাল"
        );

        addDepositProvider(
                content,
                "Rocket",
                "রকেট পার্সোনাল"
        );

        addDepositProvider(
                content,
                "Upay",
                "উপায় পার্সোনাল"
        );

        space(content, 12);

        content.addView(
                tv(
                        "টাকার পরিমাণ",
                        16,
                        BLUE
                )
        );

        EditText amount =
                input(
                        "টাকার পরিমাণ",
                        false
                );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        |
                InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        content.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(content, 12);

        content.addView(
                tv(
                        "Transaction ID",
                        16,
                        BLUE
                )
        );

        EditText trx =
                input(
                        "Transaction ID লিখুন",
                        false
                );

        content.addView(
                trx,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(content, 18);

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

            new AlertDialog.Builder(this)
                    .setTitle(
                            "ডিপোজিট সাবমিট"
                    )
                    .setMessage(
                            "টাকার পরিমাণ: ৳ "
                                    + money
                                    + "\nTransaction ID: "
                                    + transaction
                    )
                    .setNegativeButton(
                            "বাতিল",
                            null
                    )
                    .setPositiveButton(
                            "সাবমিট",
                            (d, w) -> {

                                Toast.makeText(
                                        this,
                                        "ডিপোজিট রিকোয়েস্ট সাবমিট হয়েছে।",
                                        Toast.LENGTH_LONG
                                ).show();

                                amount.setText("");
                                trx.setText("");
                            }
                    )
                    .show();
        });
    }

    private void addDepositProvider(
            LinearLayout parent,
            String name,
            String subtitle
    ) {

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
                bg(Color.WHITE, 14)
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(78)
                );

        p.setMargins(
                0,
                0,
                0,
                dp(7)
        );

        parent.addView(card, p);

        TextView title =
                tv(
                        name
                                + "  •  "
                                + subtitle,
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
                        dp(45)
                )
        );
    }

    // =========================================================
    // BANK TRANSFER
    // =========================================================

    private void showBankTransfer() {

        getWindow().setStatusBarColor(BLUE);

        LinearLayout main =
                new LinearLayout(this);

        main.setOrientation(
                LinearLayout.VERTICAL
        );

        main.setBackgroundColor(BG);

        setContentView(main);

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        pageBack(
                header,
                "ব্যাংক ট্রান্সফার"
        );

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
                dp(25)
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

        TextView info =
                tv(
                        "🏦 ব্যাংক অ্যাকাউন্টে টাকা পাঠান\n\nনিচের তথ্যগুলো সঠিকভাবে পূরণ করে ট্রান্সফার রিকোয়েস্ট পাঠান।",
                        16,
                        DARK
                );

        info.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(18)
        );

        info.setBackground(
                bg(Color.WHITE, 18)
        );

        content.addView(
                info,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(125)
                )
        );

        space(content, 12);

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(20)
        );

        card.setBackground(
                bg(Color.WHITE, 18)
        );

        content.addView(
                card,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        EditText bank =
                input("ব্যাংকের নাম লিখুন", false);

        EditText holder =
                input(
                        "অ্যাকাউন্ট হোল্ডারের নাম",
                        false
                );

        EditText account =
                input(
                        "ব্যাংক অ্যাকাউন্ট নম্বর",
                        false
                );

        EditText amount =
                input(
                        "টাকার পরিমাণ",
                        false
                );

        account.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        |
                InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        card.addView(
                bank,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(card, 10);

        card.addView(
                holder,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(card, 10);

        card.addView(
                account,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(card, 10);

        card.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(card, 18);

        TextView transfer =
                button(
                        "ব্যাংক ট্রান্সফার করুন  →",
                        BLUE,
                        Color.WHITE
                );

        card.addView(
                transfer,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        transfer.setOnClickListener(v -> {

            if (bank.getText()
                    .toString()
                    .trim()
                    .isEmpty()) {

                bank.setError(
                        "ব্যাংকের নাম দিন"
                );

                return;
            }

            if (holder.getText()
                    .toString()
                    .trim()
                    .isEmpty()) {

                holder.setError(
                        "হোল্ডারের নাম দিন"
                );

                return;
            }

            if (account.getText()
                    .toString()
                    .trim()
                    .length() < 8) {

                account.setError(
                        "সঠিক অ্যাকাউন্ট নম্বর দিন"
                );

                return;
            }

            if (amount.getText()
                    .toString()
                    .trim()
                    .isEmpty()) {

                amount.setError(
                        "টাকার পরিমাণ দিন"
                );

                return;
            }

            Toast.makeText(
                    this,
                    "ব্যাংক ট্রান্সফার রিকোয়েস্ট গ্রহণ করা হয়েছে।",
                    Toast.LENGTH_LONG
            ).show();
        });
    }

    // =========================================================
    // MOBILE BANKING
    // =========================================================

    private void showMobileBanking() {

        getWindow().setStatusBarColor(BLUE);

        LinearLayout main =
                new LinearLayout(this);

        main.setOrientation(
                LinearLayout.VERTICAL
        );

        main.setBackgroundColor(BG);

        setContentView(main);

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        pageBack(
                header,
                "মোবাইল ব্যাংকিং"
        );

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

        content.addView(
                tv(
                        "মোবাইল ব্যাংকিং নির্বাচন করুন",
                        16,
                        DARK
                )
        );

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

        addMobileProvider(
                providers,
                "বিকাশ",
                "💗"
        );

        addMobileProvider(
                providers,
                "নগদ",
                "🟠"
        );

        addMobileProvider(
                providers,
                "রকেট",
                "🟣"
        );

        addMobileProvider(
                providers,
                "উপায়",
                "🟡"
        );

        space(content, 10);

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
                bg(Color.WHITE, 18)
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

        card.addView(
                selected,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)
                )
        );

        EditText number =
                input(
                        "মোবাইল নম্বর লিখুন",
                        false
                );

        number.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        card.addView(
                number,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(card, 12);

        EditText amount =
                input(
                        "টাকার পরিমাণ লিখুন",
                        false
                );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        |
                InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        card.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(card, 18);

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

            String phone =
                    number.getText()
                            .toString()
                            .trim()
                            .replace(" ", "")
                            .replace("-", "");

            if (phone.startsWith("+88")) {
                phone = phone.substring(3);
            }

            String money =
                    amount.getText()
                            .toString()
                            .trim();

            if (!phone.matches(
                    "01[0-9]{9}"
            )) {

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

            new AlertDialog.Builder(this)
                    .setTitle(
                            "টাকা পাঠানো নিশ্চিত করুন"
                    )
                    .setMessage(
                            "মাধ্যম: "
                                    + selectedMobileProvider
                                    + "\nনম্বর: "
                                    + phone
                                    + "\nপরিমাণ: ৳ "
                                    + money
                    )
                    .setNegativeButton(
                            "বাতিল",
                            null
                    )
                    .setPositiveButton(
                            "নিশ্চিত",
                            (d, w) -> {

                                Toast.makeText(
                                        this,
                                        "রিকোয়েস্ট গ্রহণ করা হয়েছে।",
                                        Toast.LENGTH_LONG
                                ).show();

                                number.setText("");
                                amount.setText("");
                            }
                    )
                    .show();
        });
    }

    private void addMobileProvider(
            LinearLayout parent,
            String name,
            String icon
    ) {

        boolean selected =
                selectedMobileProvider
                        .equals(name);

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(
                Gravity.CENTER
        );

        box.setBackground(
                outline(
                        selected
                                ? Color.rgb(238,246,255)
                                : Color.WHITE,
                        selected
                                ? BLUE
                                : Color.rgb(220,225,232),
                        13
                )
        );

        TextView i =
                tv(icon, 22, DARK);

        i.setGravity(Gravity.CENTER);

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

        n.setGravity(Gravity.CENTER);

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

        parent.addView(box, p);

        box.setOnClickListener(v -> {

            selectedMobileProvider =
                    name;

            showMobileBanking();
        });
    }

    // =========================================================
    // MOBILE RECHARGE
    // =========================================================

    private void showMobileRecharge() {

        getWindow().setStatusBarColor(BLUE);

        LinearLayout main =
                new LinearLayout(this);

        main.setOrientation(
                LinearLayout.VERTICAL
        );

        main.setBackgroundColor(BG);

        setContentView(main);

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        pageBack(
                header,
                "মোবাইল রিচার্জ"
        );

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

        content.addView(
                tv(
                        "অপারেটর নির্বাচন করুন",
                        16,
                        DARK
                )
        );

        LinearLayout op1 =
                new LinearLayout(this);

        op1.setGravity(
                Gravity.CENTER
        );

        content.addView(
                op1,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(72)
                )
        );

        addRechargeOperator(
                op1,
                "GP",
                "🟢",
                "GP"
        );

        addRechargeOperator(
                op1,
                "Robi",
                "🔴",
                "Robi"
        );

        addRechargeOperator(
                op1,
                "Airtel",
                "🔵",
                "Airtel"
        );

        LinearLayout op2 =
                new LinearLayout(this);

        op2.setGravity(
                Gravity.CENTER
        );

        content.addView(
                op2,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(72)
                )
        );

        addRechargeOperator(
                op2,
                "Banglalink",
                "🟠",
                "Banglalink"
        );

        addRechargeOperator(
                op2,
                "Teletalk",
                "🟢",
                "Teletalk"
        );

        space(content, 10);

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(16),
                dp(16),
                dp(16),
                dp(18)
        );

        card.setBackground(
                bg(Color.WHITE, 18)
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
                        "✓ "
                                + selectedRechargeOperator,
                        20,
                        BLUE
                );

        selected.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                selected,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)
                )
        );

        EditText number =
                input(
                        "যে নম্বরে রিচার্জ করবেন",
                        false
                );

        card.addView(
                number,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(card, 12);

        EditText amount =
                input(
                        "টাকার পরিমাণ লিখুন",
                        false
                );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        |
                InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        card.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        space(card, 18);

        TextView recharge =
                button(
                        "রিচার্জ করুন  →",
                        BLUE,
                        Color.WHITE
                );

        card.addView(
                recharge,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        recharge.setOnClickListener(v -> {

            String num =
                    number.getText()
                            .toString()
                            .trim()
                            .replace(" ", "")
                            .replace("-", "");

            if (num.startsWith("+88")) {
                num = num.substring(3);
            }

            String money =
                    amount.getText()
                            .toString()
                            .trim();

            if (!num.matches(
                    "01[0-9]{9}"
            )) {

                number.setError(
                        "সঠিক ১১ ডিজিটের মোবাইল নম্বর দিন"
                );

                return;
            }

            if (money.isEmpty()) {

                amount.setError(
                        "রিচার্জের পরিমাণ দিন"
                );

                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle(
                            "রিচার্জ নিশ্চিত করুন"
                    )
                    .setMessage(
                            "অপারেটর: "
                                    + selectedRechargeOperator
                                    + "\nনম্বর: "
                                    + num
                                    + "\nপরিমাণ: ৳ "
                                    + money
                    )
                    .setNegativeButton(
                            "বাতিল",
                            null
                    )
                    .setPositiveButton(
                            "নিশ্চিত",
                            (d, w) -> {

                                Toast.makeText(
                                        this,
                                        "রিচার্জ রিকোয়েস্ট গ্রহণ করা হয়েছে।",
                                        Toast.LENGTH_LONG
                                ).show();

                                number.setText("");
                                amount.setText("");
                            }
                    )
                    .show();
        });
    }

    private void addRechargeOperator(
            LinearLayout parent,
            String name,
            String icon,
            String operator
    ) {

        boolean selected =
                selectedRechargeOperator
                        .equals(operator);

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(
                Gravity.CENTER
        );

        box.setBackground(
                outline(
                        selected
                                ? Color.rgb(238,246,255)
                                : Color.WHITE,
                        selected
                                ? BLUE
                                : Color.rgb(220,225,232),
                        13
                )
        );

        TextView iconView =
                tv(icon, 22, DARK);

        iconView.setGravity(
                Gravity.CENTER
        );

        box.addView(
                iconView,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(30)
                )
        );

        TextView nameView =
                tv(
                        name,
                        13,
                        selected ? BLUE : DARK
                );

        nameView.setGravity(
                Gravity.CENTER
        );

        nameView.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        box.addView(
                nameView,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(27)
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        dp(68),
                        1
                );

        p.setMargins(
                dp(3),
                0,
                dp(3),
                0
        );

        parent.addView(box, p);

        box.setOnClickListener(v -> {

            selectedRechargeOperator =
                    operator;

            showMobileRecharge();
        });
    }

    // =========================================================
    // PROFILE
    // =========================================================

    private void showProfile() {

        getWindow().setStatusBarColor(BLUE);

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(BG);

        setContentView(root);

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setBackgroundColor(BLUE);

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        pageBack(
                header,
                "প্রোফাইল"
        );

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(18)
        );

        card.setBackground(
                bg(Color.WHITE, 18)
        );

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        cp.setMargins(
                dp(12),
                dp(15),
                dp(12),
                0
        );

        root.addView(card, cp);

        card.addView(
                tv(
                        "নাম: "
                                + pref.getString(
                                "name",
                                "Rosy"
                        ),
                        18,
                        DARK
                ),
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        card.addView(
                tv(
                        "ফোন: "
                                + pref.getString(
                                "phone",
                                ""
                        ),
                        16,
                        DARK
                ),
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        space(card, 20);

        TextView logout =
                button(
                        "Logout",
                        Color.WHITE,
                        Color.rgb(170,45,45)
                );

        logout.setBackground(
                outline(
                        Color.WHITE,
                        Color.rgb(170,45,45),
                        12
                )
        );

        card.addView(
                logout,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        logout.setOnClickListener(v -> {

            pref.edit()
                    .putBoolean(
                            "logged_in",
                            false
                    )
                    .apply();

            showLogin();
        });
    }

    // =========================================================
    // REGISTER
    // =========================================================

    private void showRegister() {

        getWindow().setStatusBarColor(BLUE);

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

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.addView(root);

        setContentView(scroll);

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
                        dp(260),
                        dp(75)
                )
        );

        space(root, 12);

        EditText name =
                input(
                        "পূর্ণ নাম",
                        false
                );

        EditText phone =
                input(
                        "+880 ফোন নম্বর",
                        false
                );

        EditText password =
                input(
                        "৬ ডিজিট পাসওয়ার্ড",
                        true
                );

        EditText confirm =
                input(
                        "৬ ডিজিট পাসওয়ার্ড নিশ্চিত করুন",
                        true
                );

        root.addView(
                name,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        space(root, 9);

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        space(root, 9);

        root.addView(
                password,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        space(root, 9);

        root.addView(
                confirm,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
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
                        -1,
                        dp(58)
                )
        );

        next.setOnClickListener(v -> {

            String n =
                    name.getText()
                            .toString()
                            .trim();

            String p =
                    phone.getText()
                            .toString()
                            .trim();

            String a =
                    password.getText()
                            .toString()
                            .trim();

            String c =
                    confirm.getText()
                            .toString()
                            .trim();

            if (n.isEmpty()) {
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
                password.setError(
                        "৬ ডিজিটের পাসওয়ার্ড দিন"
                );
                return;
            }

            if (!a.equals(c)) {
                confirm.setError(
                        "পাসওয়ার্ড একই নয়"
                );
                return;
            }

            pref.edit()
                    .putString("name", n)
                    .putString("phone", p)
                    .putString("password", a)
                    .putBoolean("logged_in", true)
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

    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    private void showForgotPassword() {

        final EditText phone =
                new EditText(this);

        phone.setHint(
                "ফোন নম্বর"
        );

        phone.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        new AlertDialog.Builder(this)
                .setTitle(
                        "পাসওয়ার্ড পুনরুদ্ধার"
                )
                .setMessage(
                        "আপনার রেজিস্টার করা ফোন নম্বর দিন।"
                )
                .setView(phone)
                .setNegativeButton(
                        "বাতিল",
                        null
                )
                .setPositiveButton(
                        "পরবর্তী",
                        (d, w) ->
                                Toast.makeText(
                                        this,
                                        "পাসওয়ার্ড রিসেট ফিচার পরে যুক্ত হবে",
                                        Toast.LENGTH_SHORT
                                ).show()
                )
                .show();
    }

    // =========================================================
    // BACK
    // =========================================================

    @Override
    public void onBackPressed() {

        if (pref != null
                &&
                pref.getBoolean(
                        "logged_in",
                        false
                )) {

            showHome();

        } else {

            showLogin();
        }
    }
}
