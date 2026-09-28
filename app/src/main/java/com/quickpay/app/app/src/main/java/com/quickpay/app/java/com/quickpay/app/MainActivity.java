package com.quickpay.app;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class MainActivity extends Activity {

    private final int BLUE = Color.rgb(8, 96, 190);
    private final int DARK_GREEN = Color.rgb(0, 92, 68);
    private final int WHITE = Color.WHITE;
    private final int DARK = Color.rgb(35, 35, 35);
    private final int YELLOW = Color.rgb(255, 190, 25);

    private SharedPreferences pref;

    private EditText phoneInput, passwordInput;
    private TextView subtitleText, forgotText, registerText, languageText;
    private boolean bangla = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences("quick_pay", Context.MODE_PRIVATE);

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        if (pref.getBoolean("logged_in", false)) {

            if (!pref.getString("pin", "").isEmpty()) {
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
                getResources().getDisplayMetrics().density
                + 0.5f
        );
    }

    private TextView text(
            String value,
            float size,
            int color
    ) {
        TextView t = new TextView(this);

        t.setText(value);
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

    private GradientDrawable strokeBg(
            int color,
            int strokeColor,
            float radius
    ) {
        GradientDrawable d =
                bg(color, radius);

        d.setStroke(
                dp(1),
                strokeColor
        );

        return d;
    }

    private void addSpace(
            LinearLayout parent,
            int height
    ) {
        Space s = new Space(this);

        parent.addView(
                s,
                new LinearLayout.LayoutParams(
                        1,
                        dp(height)
                )
        );
    }

    private EditText makeInput(
            String hint,
            boolean number,
            boolean password
    ) {
        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(17);
        e.setSingleLine(true);

        e.setTextColor(DARK);
        e.setHintTextColor(Color.GRAY);

        e.setPadding(
                dp(18),
                0,
                dp(18),
                0
        );

        int type;

        if (number) {
            type = InputType.TYPE_CLASS_NUMBER;
        } else {
            type = InputType.TYPE_CLASS_TEXT;
        }

        if (password) {
            type |=
                    InputType.TYPE_NUMBER_VARIATION_PASSWORD;
        }

        e.setInputType(type);
        e.setBackground(bg(WHITE, 12));

        return e;
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
                dp(28),
                dp(20),
                dp(28),
                dp(20)
        );

        root.setBackgroundColor(BLUE);

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BLUE);
        scroll.addView(root);

        setContentView(scroll);

        // LANGUAGE

        LinearLayout languageBox =
                new LinearLayout(this);

        languageBox.setGravity(
                Gravity.CENTER
        );

        languageBox.setBackground(
                bg(
                        Color.rgb(
                                55,
                                130,
                                205
                        ),
                        40
                )
        );

        languageText =
                text(
                        "বাংলা     EN",
                        16,
                        WHITE
                );

        languageText.setGravity(
                Gravity.CENTER
        );

        languageText.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        languageBox.addView(
                languageText,
                new LinearLayout.LayoutParams(
                        dp(170),
                        dp(48)
                )
        );

        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        dp(175),
                        dp(56)
                );

        lp.gravity = Gravity.RIGHT;

        root.addView(
                languageBox,
                lp
        );

        languageText.setOnClickListener(v -> {

            bangla = !bangla;

            if (bangla) {

                languageText.setText(
                        "বাংলা     EN"
                );

                subtitleText.setText(
                        "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম"
                );

                phoneInput.setHint(
                        "ফোন"
                );

                passwordInput.setHint(
                        "৬ ডিজিট পাসওয়ার্ড"
                );

                forgotText.setText(
                        "পাসওয়ার্ড ভুলে গেছেন?"
                );

                registerText.setText(
                        "অ্যাকাউন্ট নেই?  রেজিস্টার করুন"
                );

            } else {

                languageText.setText(
                        "BN     EN"
                );

                subtitleText.setText(
                        "Bangladesh's best recharge business platform"
                );

                phoneInput.setHint(
                        "Phone"
                );

                passwordInput.setHint(
                        "6 Digit Password"
                );

                forgotText.setText(
                        "Forgot Password?"
                );

                registerText.setText(
                        "Don't have an account? Register"
                );
            }
        });

        addSpace(root, 55);

        // LOGO

        TextView logo =
                text(
                        "Quick Pay",
                        32,
                        BLUE
                );

        logo.setGravity(
                Gravity.CENTER
        );

        logo.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        logo.setBackground(
                bg(
                        WHITE,
                        18
                )
        );

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(320),
                        dp(105)
                )
        );

        addSpace(root, 10);

        subtitleText =
                text(
                        "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম",
                        17,
                        WHITE
                );

        subtitleText.setGravity(
                Gravity.CENTER
        );

        root.addView(
                subtitleText,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        addSpace(root, 18);

        // PHONE

        phoneInput =
                makeInput(
                        "ফোন",
                        false,
                        false
                );

        phoneInput.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        root.addView(
                phoneInput,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        addSpace(root, 16);

        // PASSWORD

        LinearLayout passwordBox =
                new LinearLayout(this);

        passwordBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        passwordBox.setPadding(
                dp(10),
                0,
                dp(4),
                0
        );

        passwordBox.setBackground(
                bg(
                        WHITE,
                        12
                )
        );

        passwordInput =
                makeInput(
                        "৬ ডিজিট পাসওয়ার্ড",
                        true,
                        true
                );

        passwordInput.setBackgroundColor(
                Color.TRANSPARENT
        );

        passwordBox.addView(
                passwordInput,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1
                )
        );

        TextView eye =
                text(
                        "◉",
                        24,
                        BLUE
                );

        eye.setGravity(
                Gravity.CENTER
        );

        passwordBox.addView(
                eye,
                new LinearLayout.LayoutParams(
                        dp(55),
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
                    (
                            passwordInput.getInputType()
                            &
                            InputType.TYPE_NUMBER_VARIATION_PASSWORD
                    ) != 0;

            if (hidden) {

                passwordInput.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                );

            } else {

                passwordInput.setInputType(
                        InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
                );
            }

            passwordInput.setSelection(
                    passwordInput.length()
            );
        });

        addSpace(root, 22);

        // LOGIN BUTTON

        TextView login =
                text(
                        "লগইন",
                        21,
                        BLUE
                );

        login.setGravity(
                Gravity.CENTER
        );

        login.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        login.setBackground(
                bg(
                        WHITE,
                        12
                )
        );

        root.addView(
                login,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        login.setOnClickListener(
                v -> doLogin()
        );

        addSpace(root, 12);

        forgotText =
                text(
                        "পাসওয়ার্ড ভুলে গেছেন?",
                        17,
                        WHITE
                );

        forgotText.setGravity(
                Gravity.CENTER
        );

        root.addView(
                forgotText,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)
                )
        );

        forgotText.setOnClickListener(
                v -> showForgotPassword()
        );

        registerText =
                text(
                        "অ্যাকাউন্ট নেই?  রেজিস্টার করুন",
                        18,
                        WHITE
                );

        registerText.setGravity(
                Gravity.CENTER
        );

        registerText.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        root.addView(
                registerText,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        registerText.setOnClickListener(
                v -> showRegister()
        );
    }

    // =========================================================
    // LOGIN ACTION
    // =========================================================

    private void doLogin() {

        String phone =
                phoneInput.getText()
                        .toString()
                        .trim();

        String password =
                passwordInput.getText()
                        .toString()
                        .trim();

        if (phone.isEmpty()) {

            phoneInput.setError(
                    "ফোন নম্বর দিন"
            );

            phoneInput.requestFocus();

            return;
        }

        if (password.isEmpty()) {

            passwordInput.setError(
                    "পাসওয়ার্ড দিন"
            );

            passwordInput.requestFocus();

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

        if (!savedPhone.isEmpty()) {

            if (!savedPhone.equals(phone)) {

                Toast.makeText(
                        this,
                        "ফোন নম্বর সঠিক নয়",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (!savedPassword.equals(password)) {

                Toast.makeText(
                        this,
                        "পাসওয়ার্ড সঠিক নয়",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }
        }

        pref.edit()
                .putString(
                        "phone",
                        phone
                )
                .putString(
                        "password",
                        password
                )
                .putBoolean(
                        "logged_in",
                        true
                )
                .apply();

        if (pref.getString(
                "pin",
                ""
        ).isEmpty()) {

            showPinSetup();

        } else {

            showHome();
        }
    }

    // =========================================================
    // PIN SETUP
    // =========================================================

    private void showPinSetup() {
        showPinScreen(false);
    }

    private void showPinUnlock() {
        showPinScreen(true);
    }

    private void showPinScreen(
            boolean unlock
    ) {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout screen =
                new LinearLayout(this);

        screen.setOrientation(
                LinearLayout.VERTICAL
        );

        screen.setGravity(
                Gravity.CENTER
        );

        screen.setPadding(
                dp(22),
                dp(20),
                dp(22),
                dp(20)
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
                dp(28),
                dp(32),
                dp(28),
                dp(22)
        );

        card.setBackground(
                bg(
                        Color.rgb(
                                250,
                                252,
                                250
                        ),
                        28
                )
        );

        screen.addView(
                card,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(
                                unlock
                                        ? 500
                                        : 520
                        )
                )
        );

        TextView lock =
                text(
                        "🔒",
                        52,
                        BLUE
                );

        lock.setGravity(
                Gravity.CENTER
        );

        lock.setBackground(
                bg(
                        Color.rgb(
                                232,
                                240,
                                250
                        ),
                        70
                )
        );

        card.addView(
                lock,
                new LinearLayout.LayoutParams(
                        dp(110),
                        dp(110)
                )
        );

        addSpace(card, 20);

        TextView title =
                text(
                        unlock
                                ? "পিন যাচাই করুন"
                                : "পিন সেট করুন",
                        27,
                        BLUE
                );

        title.setGravity(
                Gravity.CENTER
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

        TextView subtitle =
                text(
                        unlock
                                ? "আপনার ৮ ডিজিটের পিন দিন"
                                : "অ্যাপে ঢোকার জন্য ৮ ডিজিটের পিন দিন",
                        17,
                        Color.DKGRAY
                );

        subtitle.setGravity(
                Gravity.CENTER
        );

        card.addView(
                subtitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        addSpace(card, 8);

        EditText pin =
                makePinBox();

        pin.setHint(
                "৮ ডিজিট PIN"
        );

        card.addView(
                pin,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(64)
                )
        );

        EditText confirm = null;

        if (!unlock) {

            addSpace(card, 12);

            confirm =
                    makePinBox();

            confirm.setHint(
                    "PIN আবার দিন"
            );

            card.addView(
                    confirm,
                    new LinearLayout.LayoutParams(
                            -1,
                            dp(64)
                    )
            );
        }

        addSpace(card, 18);

        TextView action =
                text(
                        unlock
                                ? "যাচাই করুন  ✓"
                                : "পিন সেট করুন  ✓",
                        20,
                        BLUE
                );

        action.setGravity(
                Gravity.CENTER
        );

        action.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        action.setBackground(
                strokeBg(
                        Color.TRANSPARENT,
                        Color.rgb(
                                80,
                                145,
                                205
                        ),
                        16
                )
        );

        card.addView(
                action,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        final EditText confirmPin =
                confirm;

        action.setOnClickListener(v -> {

            String p =
                    pin.getText()
                            .toString()
                            .trim();

            if (p.length() != 8) {

                Toast.makeText(
                        this,
                        "৮ ডিজিটের PIN দিন",
                        Toast.LENGTH_SHORT
                ).show();

                pin.requestFocus();

                return;
            }

            if (unlock) {

                String saved =
                        pref.getString(
                                "pin",
                                ""
                        );

                if (p.equals(saved)) {

                    showHome();

                } else {

                    pin.setError(
                            "ভুল PIN"
                    );

                    Toast.makeText(
                            this,
                            "PIN সঠিক নয়",
                            Toast.LENGTH_SHORT
                    ).show();
                }

            } else {

                String cp =
                        confirmPin.getText()
                                .toString()
                                .trim();

                if (!p.equals(cp)) {

                    Toast.makeText(
                            this,
                            "দুইটি PIN একই নয়",
                            Toast.LENGTH_SHORT
                    ).show();

                    confirmPin.requestFocus();

                    return;
                }

                pref.edit()
                        .putString(
                                "pin",
                                p
                        )
                        .putBoolean(
                                "logged_in",
                                true
                        )
                        .apply();

                showHome();
            }
        });

        if (unlock) {

            addSpace(card, 10);

            TextView forgot =
                    text(
                            "PIN ভুলে গেছেন?  লগইন করুন",
                            16,
                            BLUE
                    );

            forgot.setGravity(
                    Gravity.CENTER
            );

            card.addView(
                    forgot,
                    new LinearLayout.LayoutParams(
                            -1,
                            dp(40)
                    )
            );

            forgot.setOnClickListener(v -> {

                pref.edit()
                        .putBoolean(
                                "logged_in",
                                false
                        )
                        .apply();

                showLogin();
            });
        }
    }

    // =========================================================
    // PIN INPUT
    // =========================================================

    private EditText makePinBox() {

        EditText pin =
                new EditText(this);

        pin.setTextSize(20);

        pin.setSingleLine(true);

        pin.setGravity(
                Gravity.CENTER
        );

        pin.setTextColor(DARK);

        pin.setHintTextColor(
                Color.rgb(
                        150,
                        150,
                        150
                )
        );

        pin.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        pin.setBackground(
                strokeBg(
                        Color.rgb(
                                248,
                                250,
                                253
                        ),
                        Color.rgb(
                                215,
                                225,
                                235
                        ),
                        15
                )
        );

        return pin;
    }

    // =========================================================
    // HOME
    // =========================================================

    private void showHome() {

        getWindow().setStatusBarColor(
                DARK_GREEN
        );

        getWindow().setNavigationBarColor(
                WHITE
        );

        LinearLayout main =
                new LinearLayout(this);

        main.setOrientation(
                LinearLayout.VERTICAL
        );

        main.setBackgroundColor(
                WHITE
        );

        setContentView(main);

        // =====================================================
        // HEADER
        // =====================================================

        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.VERTICAL
        );

        header.setPadding(
                dp(12),
                dp(7),
                dp(12),
                dp(7)
        );

        header.setBackgroundColor(
                DARK_GREEN
        );

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(185)
                )
        );

        // TOP BAR

        LinearLayout top =
                new LinearLayout(this);

        top.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.addView(
                top,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                )
        );

        TextView brand =
                text(
                        "Quick Pay",
                        21,
                        DARK
                );

        brand.setGravity(
                Gravity.CENTER
        );

        brand.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        brand.setBackground(
                bg(
                        WHITE,
                        16
                )
        );

        top.addView(
                brand,
                new LinearLayout.LayoutParams(
                        dp(145),
                        dp(48)
                )
        );

        Space topSpace =
                new Space(this);

        top.addView(
                topSpace,
                new LinearLayout.LayoutParams(
                        0,
                        1,
                        1
                )
        );

        TextView en =
                text(
                        "EN",
                        15,
                        WHITE
                );

        en.setGravity(
                Gravity.CENTER
        );

        top.addView(
                en,
                new LinearLayout.LayoutParams(
                        dp(38),
                        dp(48)
                )
        );

        TextView bell =
                text(
                        "♧",
                        27,
                        WHITE
                );

        bell.setGravity(
                Gravity.CENTER
        );

        top.addView(
                bell,
                new LinearLayout.LayoutParams(
                        dp(42),
                        dp(48)
                )
        );

        TextView logout =
                text(
                        "⇥",
                        27,
                        WHITE
                );

        logout.setGravity(
                Gravity.CENTER
        );

        top.addView(
                logout,
                new LinearLayout.LayoutParams(
                        dp(42),
                        dp(48)
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

        // USER + BALANCE

        LinearLayout userRow =
                new LinearLayout(this);

        userRow.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.addView(
                userRow,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        String name =
                pref.getString(
                        "name",
                        "Rosy"
                );

        TextView userName =
                text(
                        name,
                        26,
                        WHITE
                );

        userName.setGravity(
                Gravity.CENTER_VERTICAL
        );

        userName.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        userRow.addView(
                userName,
                new LinearLayout.LayoutParams(
                        0,
                        dp(60),
                        1
                )
        );

        TextView balance =
                text(
                        "মেইন ব্যালেন্স: ৳ ১২,৫০০\n" +
                        "ড্রাইভ ব্যালেন্স: ৳ ১৮০",
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
                bg(
                        YELLOW,
                        35
                )
        );

        userRow.addView(
                balance,
                new LinearLayout.LayoutParams(
                        dp(205),
                        dp(57)
                )
        );

        // NOTICE

        TextView notice =
                text(
                        "●  সর্বশেষ আপডেট: Quick Pay-এ স্বাগতম",
                        13,
                        Color.DKGRAY
                );

        notice.setGravity(
                Gravity.CENTER_VERTICAL
        );

        notice.setPadding(
                dp(12),
                0,
                dp(8),
                0
        );

        notice.setSingleLine(
                true
        );

        notice.setBackground(
                bg(
                        WHITE,
                        13
                )
        );

        LinearLayout.LayoutParams np =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(40)
                );

        np.topMargin =
                dp(5);

        header.addView(
                notice,
                np
        );

        // =====================================================
        // SERVICES
        // =====================================================

        LinearLayout services =
                new LinearLayout(this);

        services.setOrientation(
                LinearLayout.VERTICAL
        );

        services.setGravity(
                Gravity.CENTER
        );

        services.setPadding(
                dp(10),
                dp(5),
                dp(10),
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

        // ROW 1

        LinearLayout row1 =
                createServiceRow(
                        services
                );

        addService(
                row1,
                "👛",
                "ওয়ালেট\nডিপোজিট"
        );

        addService(
                row1,
                "💵",
                "মোবাইল\nব্যাংকিং"
        );

        addService(
                row1,
                "🏦",
                "ব্যাংক\nট্রান্সফার"
        );

        addService(
                row1,
                "📱",
                "মোবাইল\nরিচার্জ"
        );

        // ROW 2

        LinearLayout row2 =
                createServiceRow(
                        services
                );

        addService(
                row2,
                "💬",
                "গ্রুপ\nচ্যাট"
        );

        addService(
                row2,
                "🎁",
                "ইনভাইট\nবোনাস"
        );

        addService(
                row2,
                "🧾",
                "বিল\nপে"
        );

        addService(
                row2,
                "🏷",
                "বিশেষ\nঅফার"
        );

        // ROW 3

        LinearLayout row3 =
                createServiceRow(
                        services
                );

        addService(
                row3,
                "🎧",
                "কাস্টমার\nকেয়ার"
        );

        addService(
                row3,
                "⭐",
                "কাস্টমার\nরিভিউ"
        );

        addService(
                row3,
                "▶",
                "ভিডিও\nটিউটোরিয়াল"
        );

        addService(
                row3,
                "👥",
                "কন্টাক্ট\nআস"
        );

        // =====================================================
        // BONUS
        // =====================================================

        LinearLayout bonus =
                new LinearLayout(this);

        bonus.setOrientation(
                LinearLayout.VERTICAL
        );

        bonus.setPadding(
                dp(12),
                dp(3),
                dp(12),
                dp(3)
        );

        bonus.setBackground(
                bg(
                        DARK_GREEN,
                        18
                )
        );

        main.addView(
                bonus,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(103)
                )
        );

        TextView bonusTitle =
                text(
                        "🎁  ডিপোজিট বোনাস অফার",
                        18,
                        WHITE
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
                        dp(34)
                )
        );

        TextView bonusSub =
                text(
                        "এখনই করুন, বোনাস নিয়ে নিন!",
                        11,
                        WHITE
                );

        bonusSub.setGravity(
                Gravity.CENTER_VERTICAL
        );

        bonus.addView(
                bonusSub,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(20)
                )
        );

        LinearLayout bonusBoxes =
                new LinearLayout(this);

        bonusBoxes.setGravity(
                Gravity.CENTER
        );

        bonus.addView(
                bonusBoxes,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)
                )
        );

        addBonusBox(
                bonusBoxes,
                "৳ ৫০০",
                "বোনাস ৳ ৫০"
        );

        addBonusBox(
                bonusBoxes,
                "৳ ১০০০",
                "বোনাস ৳ ১০০"
        );

        addBonusBox(
                bonusBoxes,
                "৳ ২০০০",
                "বোনাস ৳ ২০০"
        );

        // =====================================================
        // BOTTOM NAVIGATION
        // =====================================================

        LinearLayout bottom =
                new LinearLayout(this);

        bottom.setGravity(
                Gravity.CENTER
        );

        bottom.setBackgroundColor(
                WHITE
        );

        main.addView(
                bottom,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        addBottomButton(
                bottom,
                "⌂\nহোম",
                true
        );

        addBottomButton(
                bottom,
                "◷\nলেনদেন",
                false
        );

        addBottomButton(
                bottom,
                "♙\nপ্রোফাইল",
                false
        );
    }

    // =========================================================
    // SERVICE ROW
    // =========================================================

    private LinearLayout createServiceRow(
            LinearLayout parent
    ) {

        LinearLayout row =
                new LinearLayout(this);

        row.setGravity(
                Gravity.CENTER
        );

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

    // =========================================================
    // SERVICE ITEM
    // =========================================================

    private void addService(
            LinearLayout row,
            String icon,
            String title
    ) {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(
                Gravity.CENTER
        );

        TextView iconText =
                text(
                        icon,
                        27,
                        DARK
                );

        iconText.setGravity(
                Gravity.CENTER
        );

        box.addView(
                iconText,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(37)
                )
        );

        TextView titleText =
                text(
                        title,
                        11,
                        DARK
                );

        titleText.setGravity(
                Gravity.CENTER
        );

        box.addView(
                titleText,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(37)
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

        row.addView(
                box,
                p
        );

        box.setOnClickListener(v -> {

    String item = title.replace("\n", " ");

    if (item.contains("মোবাইল ব্যাংকিং")) {
        showMobileBanking();
    } else {
        Toast.makeText(
                this,
                item,
                Toast.LENGTH_SHORT
        ).show();
    }
});

    // =========================================================
    // BONUS BOX
    // =========================================================

    private void addBonusBox(
            LinearLayout parent,
            String amount,
            String bonus
    ) {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(
                Gravity.CENTER
        );

        box.setBackground(
                bg(
                        WHITE,
                        9
                )
        );

        TextView a =
                text(
                        amount,
                        13,
                        DARK
                );

        a.setGravity(
                Gravity.CENTER
        );

        a.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        box.addView(
                a,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(20)
                )
        );

        TextView b =
                text(
                        bonus,
                        10,
                        Color.rgb(
                                170,
                                40,
                                40
                        )
                );

        b.setGravity(
                Gravity.CENTER
        );

        box.addView(
                b,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(18)
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        dp(40),
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
    }

    // =========================================================
    // BOTTOM BUTTON
    // =========================================================

    private void addBottomButton(
            LinearLayout parent,
            String label,
            boolean selected
    ) {

        TextView b =
                text(
                        label,
                        12,
                        selected
                                ? DARK_GREEN
                                : Color.DKGRAY
                );

        b.setGravity(
                Gravity.CENTER
        );

        if (selected) {
            b.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );
        }

        parent.addView(
                b,
                new LinearLayout.LayoutParams(
                        0,
                        dp(58),
                        1
                )
        );
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

        root.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        root.setPadding(
                dp(24),
                dp(18),
                dp(24),
                dp(18)
        );

        root.setBackgroundColor(BLUE);

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BLUE);
        scroll.addView(root);

        setContentView(scroll);

        // LOGO

        TextView logo =
                text(
                        "Quick Pay",
                        30,
                        BLUE
                );

        logo.setGravity(
                Gravity.CENTER
        );

        logo.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        logo.setBackground(
                bg(
                        WHITE,
                        18
                )
        );

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(250),
                        dp(82)
                )
        );

        addSpace(root, 14);

        TextView title =
                text(
                        "নতুন অ্যাকাউন্ট তৈরি করুন",
                        22,
                        WHITE
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        root.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)
                )
        );

        addSpace(root, 10);

        // COUNTRY

        TextView country =
                text(
                        "🇧🇩   বাংলাদেশ                         ▾",
                        17,
                        DARK
                );

        country.setGravity(
                Gravity.CENTER_VERTICAL
        );

        country.setPadding(
                dp(18),
                0,
                dp(12),
                0
        );

        country.setBackground(
                bg(
                        WHITE,
                        12
                )
        );

        root.addView(
                country,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        addSpace(root, 10);

        // AGENT CODE

        EditText agent =
                makeInput(
                        "রিসেলার এজেন্ট কোড",
                        false,
                        false
                );

        root.addView(
                agent,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        addSpace(root, 10);

        // NAME

        EditText name =
                makeInput(
                        "পূর্ণ নাম",
                        false,
                        false
                );

        root.addView(
                name,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        addSpace(root, 10);

        // PHONE

        EditText phone =
                makeInput(
                        "+880 ফোন নম্বর",
                        false,
                        false
                );

        phone.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        addSpace(root, 10);

        // PASSWORD

        EditText password =
                makeInput(
                        "৬ ডিজিট পাসওয়ার্ড",
                        true,
                        true
                );

        root.addView(
                password,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        addSpace(root, 10);

        // CONFIRM PASSWORD

        EditText confirm =
                makeInput(
                        "৬ ডিজিট পাসওয়ার্ড নিশ্চিত করুন",
                        true,
                        true
                );

        root.addView(
                confirm,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        addSpace(root, 18);

        // NEXT

        TextView next =
                text(
                        "পরবর্তী",
                        20,
                        BLUE
                );

        next.setGravity(
                Gravity.CENTER
        );

        next.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        next.setBackground(
                bg(
                        WHITE,
                        14
                )
        );

        root.addView(
                next,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
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

            String pass =
                    password.getText()
                            .toString()
                            .trim();

            String cp =
                    confirm.getText()
                            .toString()
                            .trim();

            if (n.isEmpty()) {

                name.setError(
                        "পূর্ণ নাম দিন"
                );

                name.requestFocus();

                return;
            }

            if (p.isEmpty()) {

                phone.setError(
                        "ফোন নম্বর দিন"
                );

                phone.requestFocus();

                return;
            }

            if (pass.length() != 6) {

                password.setError(
                        "৬ ডিজিটের পাসওয়ার্ড দিন"
                );

                password.requestFocus();

                return;
            }

            if (!pass.equals(cp)) {

                confirm.setError(
                        "পাসওয়ার্ড মিলছে না"
                );

                confirm.requestFocus();

                return;
            }

            pref.edit()
                    .putString(
                            "name",
                            n
                    )
                    .putString(
                            "phone",
                            p
                    )
                    .putString(
                            "password",
                            pass
                    )
                    .putBoolean(
                            "logged_in",
                            true
                    )
                    .apply();

            showPinSetup();
        });

        addSpace(root, 10);

        TextView login =
                text(
                        "অ্যাকাউন্ট আছে?  লগইন",
                        17,
                        WHITE
                );

        login.setGravity(
                Gravity.CENTER
        );

        root.addView(
                login,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(48)
                )
        );

        login.setOnClickListener(
                v -> showLogin()
        );
    }

    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    private void showForgotPassword() {

        final EditText input =
                makeInput(
                        "ফোন নম্বর",
                        false,
                        false
                );

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setPadding(
                dp(25),
                dp(20),
                dp(25),
                dp(10)
        );

        box.addView(
                input,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        new android.app.AlertDialog.Builder(this)
                .setTitle(
                        "পাসওয়ার্ড পুনরুদ্ধার"
                )
                .setMessage(
                        "আপনার রেজিস্টার করা ফোন নম্বর দিন।"
                )
                .setView(box)
                .setPositiveButton(
                        "ঠিক আছে",
                        (dialog, which) ->

                                Toast.makeText(
                                        this,
                                        "পাসওয়ার্ড রিসেটের জন্য সাপোর্টে যোগাযোগ করুন",
                                        Toast.LENGTH_LONG
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

        if (
                pref != null &&
                pref.getBoolean(
                        "logged_in",
                        false
                )
        ) {

            showHome();

        } else {

            super.onBackPressed();
        }
    }
}

// =========================================================
// MOBILE BANKING
// =========================================================

private void showMobileBanking() {

    getWindow().setStatusBarColor(BLUE);
    getWindow().setNavigationBarColor(Color.WHITE);

    LinearLayout main = new LinearLayout(this);
    main.setOrientation(LinearLayout.VERTICAL);
    main.setBackgroundColor(Color.rgb(248, 249, 251));

    setContentView(main);

    // HEADER
    LinearLayout header = new LinearLayout(this);
    header.setGravity(Gravity.CENTER_VERTICAL);
    header.setPadding(dp(10), 0, dp(10), 0);
    header.setBackgroundColor(BLUE);

    main.addView(
            header,
            new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dp(62)
            )
    );

    TextView back = text("‹", 38, WHITE);
    back.setGravity(Gravity.CENTER);

    header.addView(
            back,
            new LinearLayout.LayoutParams(
                    dp(55),
                    dp(62)
            )
    );

    back.setOnClickListener(v -> showHome());

    TextView title = text(
            "মোবাইল ব্যাংকিং",
            21,
            WHITE
    );

    title.setGravity(Gravity.CENTER_VERTICAL);
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

    TextView bell = text("♧", 27, WHITE);
    bell.setGravity(Gravity.CENTER);

    header.addView(
            bell,
            new LinearLayout.LayoutParams(
                    dp(55),
                    dp(62)
            )
    );

    // NOTICE
    TextView notice = text(
            "●  মোবাইল ব্যাংকিং এর মাধ্যমে সহজেই ডিপোজিট করুন",
            14,
            Color.DKGRAY
    );

    notice.setGravity(Gravity.CENTER_VERTICAL);
    notice.setPadding(dp(15), 0, dp(10), 0);
    notice.setBackground(
            bg(WHITE, 12)
    );

    LinearLayout.LayoutParams noticeParams =
            new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dp(48)
            );

    noticeParams.setMargins(
            dp(12),
            dp(12),
            dp(12),
            dp(10)
    );

    main.addView(notice, noticeParams);

    // PAYMENT METHODS
    ScrollView scroll = new ScrollView(this);

    LinearLayout content = new LinearLayout(this);
    content.setOrientation(LinearLayout.VERTICAL);
    content.setPadding(
            dp(12),
            dp(2),
            dp(12),
            dp(15)
    );

    scroll.addView(content);

    main.addView(
            scroll,
            new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    0,
                    1
            )
    );

    addBankMethod(
            content,
            "bKash",
            "বিকাশ এজেন্ট নম্বর",
            "01XXXXXXXXX"
    );

    addBankMethod(
            content,
            "Nagad",
            "নগদ এজেন্ট নম্বর",
            "01XXXXXXXXX"
    );

    addBankMethod(
            content,
            "Rocket",
            "রকেট এজেন্ট নম্বর",
            "01XXXXXXXXX"
    );

    addBankMethod(
            content,
            "Upay",
            "উপায় এজেন্ট নম্বর",
            "01XXXXXXXXX"
    );

    // AUTO DEPOSIT BUTTON
    TextView autoDeposit = text(
            "অটো ডিপোজিট  →",
            19,
            WHITE
    );

    autoDeposit.setGravity(Gravity.CENTER);
    autoDeposit.setTypeface(
            Typeface.DEFAULT,
            Typeface.BOLD
    );

    autoDeposit.setBackground(
            bg(BLUE, 14)
    );

    LinearLayout.LayoutParams autoParams =
            new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dp(60)
            );

    autoParams.setMargins(
            dp(12),
            dp(5),
            dp(12),
            dp(15)
    );

    main.addView(
            autoDeposit,
            autoParams
    );

    autoDeposit.setOnClickListener(v -> {

        Toast.makeText(
                this,
                "অটো ডিপোজিট শীঘ্রই চালু হবে",
                Toast.LENGTH_SHORT
        ).show();

    });
}


// =========================================================
// MOBILE BANKING METHOD CARD
// =========================================================

private void addBankMethod(
        LinearLayout parent,
        String name,
        String subtitle,
        String number
) {

    LinearLayout card =
            new LinearLayout(this);

    card.setOrientation(
            LinearLayout.VERTICAL
    );

    card.setPadding(
            dp(15),
            dp(12),
            dp(15),
            dp(12)
    );

    card.setBackground(
            bg(WHITE, 16)
    );

    TextView nameText =
            text(
                    name,
                    21,
                    BLUE
            );

    nameText.setTypeface(
            Typeface.DEFAULT,
            Typeface.BOLD
    );

    card.addView(
            nameText,
            new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dp(32)
            )
    );

    TextView subText =
            text(
                    subtitle,
                    14,
                    Color.DKGRAY
            );

    card.addView(
            subText,
            new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dp(25)
            )
    );

    LinearLayout numberRow =
            new LinearLayout(this);

    numberRow.setGravity(
            Gravity.CENTER_VERTICAL
    );

    TextView numberText =
            text(
                    number,
                    18,
                    Color.BLACK
            );

    numberText.setTypeface(
            Typeface.DEFAULT,
            Typeface.BOLD
    );

    numberRow.addView(
            numberText,
            new LinearLayout.LayoutParams(
                    0,
                    dp(45),
                    1
            )
    );

    TextView copy =
            text(
                    "কপি",
                    15,
                    BLUE
            );

    copy.setGravity(
            Gravity.CENTER
    );

    copy.setTypeface(
            Typeface.DEFAULT,
            Typeface.BOLD
    );

    copy.setBackground(
            strokeBg(
                    Color.TRANSPARENT,
                    BLUE,
                    10
            )
    );

    numberRow.addView(
            copy,
            new LinearLayout.LayoutParams(
                    dp(65),
                    dp(40)
            )
    );

    copy.setOnClickListener(v -> {

        android.content.ClipboardManager clipboard =
                (android.content.ClipboardManager)
                        getSystemService(
                                Context.CLIPBOARD_SERVICE
                        );

        android.content.ClipData clip =
                android.content.ClipData.newPlainText(
                        "Mobile Banking Number",
                        number
                );

        clipboard.setPrimaryClip(clip);

        Toast.makeText(
                this,
                "নম্বর কপি হয়েছে",
                Toast.LENGTH_SHORT
        ).show();
    });

    card.addView(
            numberRow,
            new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dp(50)
            )
    );

    LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dp(130)
            );

    params.setMargins(
            0,
            0,
            0,
            dp(10)
    );

    parent.addView(card, params); {
}
