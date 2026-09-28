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

    // =========================================================
    // COLORS
    // =========================================================

    private final int BLUE = Color.rgb(8, 96, 190);
    private final int DARK_GREEN = Color.rgb(0, 92, 68);
    private final int LIGHT_GREEN = Color.rgb(0, 105, 76);
    private final int WHITE = Color.WHITE;
    private final int DARK = Color.rgb(35, 35, 35);
    private final int LIGHT_BG = Color.rgb(250, 250, 250);
    private final int YELLOW = Color.rgb(255, 190, 25);

    private SharedPreferences pref;

    private EditText phoneInput;
    private EditText passwordInput;

    private TextView subtitleText;
    private TextView forgotText;
    private TextView registerText;
    private TextView languageText;

    private boolean bangla = true;

    // =========================================================
    // ON CREATE
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences(
                "quick_pay",
                Context.MODE_PRIVATE
        );

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
                        getResources()
                                .getDisplayMetrics()
                                .density
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
                new GradientDrawable();

        d.setColor(color);
        d.setCornerRadius(dp(radius));
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

        languageText = text(
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

        LinearLayout.LayoutParams langParams =
                new LinearLayout.LayoutParams(
                        dp(175),
                        dp(56)
                );

        langParams.gravity =
                Gravity.RIGHT;

        root.addView(
                languageBox,
                langParams
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

        addSpace(root, 70);

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
                        dp(110)
                )
        );

        addSpace(root, 12);

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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        addSpace(root, 22);

        // PHONE

        phoneInput =
                new EditText(this);

        phoneInput.setHint("ফোন");
        phoneInput.setTextSize(18);
        phoneInput.setSingleLine(true);

        phoneInput.setTextColor(DARK);
        phoneInput.setHintTextColor(
                Color.GRAY
        );

        phoneInput.setPadding(
                dp(20),
                0,
                dp(20),
                0
        );

        phoneInput.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        phoneInput.setBackground(
                bg(
                        WHITE,
                        12
                )
        );

        root.addView(
                phoneInput,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        addSpace(root, 18);

        // PASSWORD

        LinearLayout passwordBox =
                new LinearLayout(this);

        passwordBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        passwordBox.setPadding(
                dp(12),
                0,
                dp(5),
                0
        );

        passwordBox.setBackground(
                bg(
                        WHITE,
                        12
                )
        );

        passwordInput =
                new EditText(this);

        passwordInput.setHint(
                "৬ ডিজিট পাসওয়ার্ড"
        );

        passwordInput.setTextSize(18);
        passwordInput.setSingleLine(true);

        passwordInput.setTextColor(DARK);
        passwordInput.setHintTextColor(
                Color.GRAY
        );

        passwordInput.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        eye.setOnClickListener(v -> {

            int type =
                    passwordInput.getInputType();

            if ((type &
                    InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                    != 0) {

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

        addSpace(root, 25);

        // LOGIN BUTTON

        TextView loginButton =
                text(
                        "লগইন",
                        21,
                        BLUE
                );

        loginButton.setGravity(
                Gravity.CENTER
        );

        loginButton.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        loginButton.setBackground(
                bg(
                        WHITE,
                        12
                )
        );

        root.addView(
                loginButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        loginButton.setOnClickListener(v -> {

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
        });

        addSpace(root, 18);

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
                        ViewGroup.LayoutParams.MATCH_PARENT,
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(55)
                )
        );

        registerText.setOnClickListener(
                v -> showRegister()
        );
    }

    // =========================================================
    // PIN SETUP - 8 DIGIT
    // =========================================================

    private void showPinSetup() {

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
                dp(35),
                dp(28),
                dp(25)
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(510)
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

        addSpace(card, 22);

        TextView title =
                text(
                        "পিন সেট করুন",
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        TextView subtitle =
                text(
                        "অ্যাপে ঢোকার জন্য ৮ ডিজিটের পিন দিন",
                        17,
                        Color.DKGRAY
                );

        subtitle.setGravity(
                Gravity.CENTER
        );

        card.addView(
                subtitle,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(42)
                )
        );

        addSpace(card, 10);

        EditText pin =
                makePinBox();

        pin.setHint(
                "৮ ডিজিট PIN"
        );

        card.addView(
                pin,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        addSpace(card, 12);

        EditText confirm =
                makePinBox();

        confirm.setHint(
                "PIN আবার দিন"
        );

        card.addView(
                confirm,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        addSpace(card, 18);

        TextView save =
                text(
                        "পিন সেট করুন  ✓",
                        20,
                        BLUE
                );

        save.setGravity(
                Gravity.CENTER
        );

        save.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        save.setBackground(
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
                save,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        save.setOnClickListener(v -> {

            String p =
                    pin.getText()
                            .toString()
                            .trim();

            String cp =
                    confirm.getText()
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

            if (!p.equals(cp)) {

                Toast.makeText(
                        this,
                        "দুইটি PIN একই নয়",
                        Toast.LENGTH_SHORT
                ).show();

                confirm.requestFocus();

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
        });
    }

    // =========================================================
    // PIN UNLOCK - EXACT STYLE
    // =========================================================

    private void showPinUnlock() {

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
                dp(35),
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(500)
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

        addSpace(card, 22);

        TextView title =
                text(
                        "পিন যাচাই করুন",
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        TextView subtitle =
                text(
                        "আপনার ৮ ডিজিটের পিন দিন",
                        18,
                        Color.DKGRAY
                );

        subtitle.setGravity(
                Gravity.CENTER
        );

        card.addView(
                subtitle,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        addSpace(card, 8);

        EditText pin =
                makePinBox();

        pin.setHint(
                "PIN"
        );

        card.addView(
                pin,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(65)
                )
        );

        addSpace(card, 20);

        TextView verify =
                text(
                        "যাচাই করুন  ✓",
                        20,
                        BLUE
                );

        verify.setGravity(
                Gravity.CENTER
        );

        verify.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        verify.setBackground(
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
                verify,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        verify.setOnClickListener(v -> {

            String entered =
                    pin.getText()
                            .toString()
                            .trim();

            String saved =
                    pref.getString(
                            "pin",
                            ""
                    );

            if (entered.length() != 8) {

                Toast.makeText(
                        this,
                        "৮ ডিজিটের PIN দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (entered.equals(saved)) {

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
        });

        addSpace(card, 12);

        TextView forgotPin =
                text(
                        "PIN ভুলে গেছেন?  লগইন করুন",
                        16,
                        BLUE
                );

        forgotPin.setGravity(
                Gravity.CENTER
        );

        card.addView(
                forgotPin,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(40)
                )
        );

        forgotPin.setOnClickListener(v -> {

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
    // HOME SCREEN
    // =========================================================

    private void showHome() {

        getWindow().setStatusBarColor(
                DARK_GREEN
        );

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

        // =====================================================
        // GREEN HEADER
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

        header.setBackground(
                bg(
                        DARK_GREEN,
                        0
                )
        );

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(195)
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(55)
                )
        );

        TextView brand =
                text(
                        "Quick Pay",
                        22,
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
                        dp(150),
                        dp(50)
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
                        16,
                        WHITE
                );

        en.setGravity(
                Gravity.CENTER
        );

        top.addView(
                en,
                new LinearLayout.LayoutParams(
                        dp(42),
                        dp(50)
                )
        );

        TextView bell =
                text(
                        "♧",
                        28,
                        WHITE
                );

        bell.setGravity(
                Gravity.CENTER
        );

        top.addView(
                bell,
                new LinearLayout.LayoutParams(
                        dp(48),
                        dp(50)
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
                        dp(48),
                        dp(50)
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(65)
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
                        27,
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
                        dp(65),
                        1
                )
        );

        TextView balance =
                text(
                        "মেইন ব্যালেন্স: ৳ ১২,৫০০\n" +
                        "ড্রাইভ ব্যালেন্স: ৳ ১৮০",
                        13,
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
                        dp(220),
                        dp(62)
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

        LinearLayout.LayoutParams noticeParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(40)
                );

        noticeParams.topMargin =
                dp(7);

        header.addView(
                notice,
                noticeParams
        );

        // =====================================================
        // SERVICES AREA
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
                dp(15),
                dp(8),
                dp(15),
                dp(3)
        );

        main.addView(
                services,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
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
                "আমাদের\nসম্পর্কে"
        );

        // =====================================================
        // BONUS BANNER
        // =====================================================

        LinearLayout bonus =
                new LinearLayout(this);

        bonus.setOrientation(
                LinearLayout.VERTICAL
        );

        bonus.setPadding(
                dp(12),
                dp(4),
                dp(12),
                dp(4)
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(110)
                )
        );

        TextView bonusTitle =
                text(
                        "🎁  ডিপোজিট বোনাস অফার",
                        19,
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(40)
                )
        );

        TextView bonusSub =
                text(
                        "এখনই করুন, বোনাস নিয়ে নিন!",
                        12,
                        WHITE
                );

        bonusSub.setGravity(
                Gravity.CENTER_VERTICAL
        );

        bonus.addView(
                bonusSub,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(25)
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
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
        // BOTTOM NAV
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
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
                        30,
                        DARK
                );

        iconText.setGravity(
                Gravity.CENTER
        );

        box.addView(
                iconText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(40)
                )
        );

        TextView titleText =
                text(
                        title,
                        12,
                        DARK
                );

        titleText.setGravity(
                Gravity.CENTER
        );

        titleText.setTypeface(
                Typeface.DEFAULT,
                Typeface.NORMAL
        );

        box.addView(
                titleText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(40)
                )
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1
                );

        params.setMargins(
                dp(2),
                dp(2),
                dp(2),
                dp(2)
        );

        row.addView(
                box,
                params
        );

        box.setOnClickListener(v -> {

            Toast.makeText(
                    this,
                    title.replace(
                            "\n",
                            " "
                    ),
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

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
                        10
                )
        );

        TextView amountText =
                text(
                        amount,
                        14,
                        DARK
                );

        amountText.setGravity(
                Gravity.CENTER
        );

        amountText.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        box.addView(
                amountText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(22)
                )
        );

        TextView bonusText =
                text(
                        bonus,
                        11,
                        Color.rgb(
                                170,
                                40,
                                40
                        )
                );

        bonusText.setGravity(
                Gravity.CENTER
        );

        box.addView(
                bonusText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(20)
                )
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        0,
                        dp(45),
                        1
                );

        params.setMargins(
                dp(3),
                0,
                dp(3),
                0
        );

        parent.addView(
                box,
                params
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
                dp(25),
                dp(20),
                dp(25),
                dp(20)
        );

        root.setBackgroundColor(BLUE);

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.addView(root);

        setContentView(scroll);

        addSpace(root, 15);

        TextView logo =
                text(
                        "Quick Pay",
                        30,
                        BLUE
                );

        logo.setGravity(
