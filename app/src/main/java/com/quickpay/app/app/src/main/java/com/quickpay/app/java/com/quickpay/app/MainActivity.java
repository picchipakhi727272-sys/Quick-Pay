package com.quickpay.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputFilter;
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
    private final int LIGHT_BG = Color.rgb(247, 248, 250);
    private final int YELLOW = Color.rgb(255, 193, 7);

    private SharedPreferences pref;

    private EditText phoneInput;
    private EditText passwordInput;

    private TextView languageText;
    private TextView subtitleText;
    private TextView forgotText;
    private TextView registerText;

    private boolean bangla = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences(
                "quick_pay",
                Context.MODE_PRIVATE
        );

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        if (pref.getBoolean("logged_in", false)
                && !pref.getString("pin", "").isEmpty()) {

            showPinUnlock();

        } else if (pref.getBoolean("logged_in", false)) {

            showPinSetup();

        } else {

            showLogin();
        }
    }

    // =====================================================
    // BASIC HELPERS
    // =====================================================

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

    // =====================================================
    // LOGIN
    // =====================================================

    private void showLogin() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout loginRoot =
                new LinearLayout(this);

        loginRoot.setOrientation(
                LinearLayout.VERTICAL
        );

        loginRoot.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        loginRoot.setPadding(
                dp(28),
                dp(18),
                dp(28),
                dp(20)
        );

        loginRoot.setBackgroundColor(BLUE);

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BLUE);

        scroll.addView(loginRoot);

        setContentView(scroll);

        // LANGUAGE

        LinearLayout languageBox =
                new LinearLayout(this);

        languageBox.setGravity(
                Gravity.CENTER
        );

        languageBox.setBackground(
                bg(
                        Color.rgb(55, 130, 205),
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

        LinearLayout.LayoutParams languageParams =
                new LinearLayout.LayoutParams(
                        dp(175),
                        dp(56)
                );

        languageParams.gravity =
                Gravity.RIGHT;

        loginRoot.addView(
                languageBox,
                languageParams
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
                        "Don't have an account?  Register"
                );
            }
        });

        addSpace(loginRoot, 55);

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
                bg(WHITE, 18)
        );

        loginRoot.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(320),
                        dp(105)
                )
        );

        addSpace(loginRoot, 12);

        subtitleText =
                text(
                        "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম",
                        17,
                        WHITE
                );

        subtitleText.setGravity(
                Gravity.CENTER
        );

        loginRoot.addView(
                subtitleText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        addSpace(loginRoot, 22);

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
                bg(WHITE, 12)
        );

        loginRoot.addView(
                phoneInput,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        addSpace(loginRoot, 16);

        // PASSWORD

        LinearLayout passwordBox =
                new LinearLayout(this);

        passwordBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        passwordBox.setPadding(
                dp(15),
                0,
                dp(5),
                0
        );

        passwordBox.setBackground(
                bg(WHITE, 12)
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
                InputType.TYPE_CLASS_NUMBER
                        |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        passwordInput.setBackgroundColor(
                Color.TRANSPARENT
        );

        passwordBox.addView(
                passwordInput,
                new LinearLayout.LayoutParams(
                        0,
                        dp(60),
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
                        dp(60)
                )
        );

        loginRoot.addView(
                passwordBox,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
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
                        InputType.TYPE_CLASS_NUMBER
                                |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
                );
            }

            passwordInput.setSelection(
                    passwordInput.length()
            );
        });

        addSpace(loginRoot, 20);

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
                bg(WHITE, 12)
        );

        loginRoot.addView(
                loginButton,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
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

        addSpace(loginRoot, 16);

        // FORGOT

        forgotText =
                text(
                        "পাসওয়ার্ড ভুলে গেছেন?",
                        17,
                        WHITE
                );

        forgotText.setGravity(
                Gravity.CENTER
        );

        loginRoot.addView(
                forgotText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(40)
                )
        );

        forgotText.setOnClickListener(
                v -> showForgotPassword()
        );

        // REGISTER

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

        loginRoot.addView(
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

    // =====================================================
    // PIN SETUP
    // =====================================================

    private void showPinSetup() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        ScrollView scroll =
                new ScrollView(this);

        scroll.setBackgroundColor(BLUE);

        LinearLayout screen =
                new LinearLayout(this);

        screen.setOrientation(
                LinearLayout.VERTICAL
        );

        screen.setGravity(
                Gravity.CENTER
        );

        screen.setPadding(
                dp(25),
                dp(25),
                dp(25),
                dp(25)
        );

        scroll.addView(screen);

        setContentView(scroll);

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
                dp(30),
                dp(28),
                dp(30)
        );

        card.setBackground(
                bg(WHITE, 25)
        );

        screen.addView(
                card,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        TextView lock =
                text(
                        "🔒",
                        55,
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
                        60
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

        addSpace(card, 12);

        EditText pin =
                makePinBox();

        card.addView(
                pin,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(65)
                )
        );

        addSpace(card, 14);

        EditText confirmPin =
                makePinBox();

        confirmPin.setHint(
                "PIN আবার দিন"
        );

        card.addView(
                confirmPin,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(65)
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
                        WHITE,
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
                        dp(62)
                )
        );

        save.setOnClickListener(v -> {

            String p =
                    pin.getText()
                            .toString()
                            .trim();

            String cp =
                    confirmPin.getText()
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
        });
    }

    // =====================================================
    // PIN UNLOCK
    // =====================================================

    private void showPinUnlock() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        ScrollView scroll =
                new ScrollView(this);

        scroll.setBackgroundColor(BLUE);

        LinearLayout screen =
                new LinearLayout(this);

        screen.setOrientation(
                LinearLayout.VERTICAL
        );

        screen.setGravity(
                Gravity.CENTER
        );

        screen.setPadding(
                dp(25),
                dp(25),
                dp(25),
                dp(25)
        );

        scroll.addView(screen);

        setContentView(scroll);

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
                dp(30),
                dp(28),
                dp(25)
        );

        card.setBackground(
                bg(WHITE, 25)
        );

        screen.addView(
                card,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                )
        );

        TextView lock =
                text(
                        "🔒",
                        55,
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
                        60
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

        addSpace(card, 12);

        EditText pin =
                makePinBox();

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
                        WHITE,
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
                        dp(62)
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

                pin.requestFocus();
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
                        dp(45)
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

    // =====================================================
    // PIN BOX
    // =====================================================

    private EditText makePinBox() {

        EditText pin =
                new EditText(this);

        pin.setTextSize(22);
        pin.setSingleLine(true);
        pin.setGravity(
                Gravity.CENTER
        );

        pin.setTextColor(DARK);
        pin.setHintTextColor(
                Color.GRAY
        );

        pin.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        pin.setFilters(
                new InputFilter[]{
                        new InputFilter.LengthFilter(8)
                }
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

    // =====================================================
    // HOME
    // =====================================================

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
                LIGHT_BG
        );

        setContentView(main);

        // =================================================
        // HEADER
        // =================================================

        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.VERTICAL
        );

        header.setPadding(
                dp(16),
                dp(8),
                dp(16),
                dp(8)
        );

        header.setBackgroundColor(
                DARK_GREEN
        );

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(205)
                )
        );

        // TOP

        LinearLayout top =
                new LinearLayout(this);

        top.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.addView(
                top,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
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
                        18
                )
        );

        top.addView(
                brand,
                new LinearLayout.LayoutParams(
                        dp(165),
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
                        dp(40),
                        dp(48)
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

        // =================================================
        // USER + BALANCE
        // =================================================

        LinearLayout user =
                new LinearLayout(this);

        user.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.addView(
                user,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(58)
                )
        );

        String savedName =
                pref.getString(
                        "name",
                        "Rosy"
                );

        TextView userName =
                text(
                        savedName,
                        24,
                        WHITE
                );

        userName.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        userName.setGravity(
                Gravity.CENTER_VERTICAL
        );

        user.addView(
                userName,
                new LinearLayout.LayoutParams(
                        0,
                        dp(58),
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
                        32
                )
        );

        user.addView(
                balance,
                new LinearLayout.LayoutParams(
                        dp(225),
                        dp(58)
                )
        );

        // =================================================
        // NOTICE
        // =================================================

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
                dp(12),
                0
        );

        notice.setSingleLine(true);

        notice.setBackground(
                bg(
                        WHITE,
                        12
                )
        );

        LinearLayout.LayoutParams noticeParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(40)
                );

        noticeParams.topMargin = dp(7);

        header.addView(
                notice,
                noticeParams
        );

        // =================================================
        // SERVICES
        // =================================================

        LinearLayout services =
                new LinearLayout(this);

        services.setOrientation(
                LinearLayout.VERTICAL
        );

        services.setPadding(
                dp(8),
                dp(5),
                dp(8),
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

        String[][] serviceNames = {

                {
                        "💰\nওয়ালেট\nডিপোজিট",
                        "📱\nমোবাইল\nব্যাংকিং",
                        "🏦\nব্যাংক\nট্রান্সফার",
                        "📲\nমোবাইল\nরিচার্জ"
                },

                {
                        "💬\nগ্রুপ\nচ্যাট",
                        "🎁\nইনভাইট\nবোনাস",
                        "🧾\nবিল\nপে",
                        "🏷️\nবিশেষ\nঅফার"
                },

                {
                        "🎧\nকাস্টমার\nকেয়ার",
                        "⭐\nকাস্টমার\nরিভিউ",
                        "▶️\nভিডিও\nটিউটোরিয়াল",
                        "👥\nআমাদের\nসম্পর্কে"
                }
        };

        for (int row = 0; row < 3; row++) {

            LinearLayout serviceRow =
                    new LinearLayout(this);

            serviceRow.setGravity(
                    Gravity.CENTER
            );

            services.addView(
                    serviceRow,
                    new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            0,
                            1
                    )
            );

            for (int col = 0; col < 4; col++) {

                TextView service =
                        text(
                                serviceNames[row][col],
                                12,
                                DARK
                        );

                service.setGravity(
                        Gravity.CENTER
                );

                service.setTypeface(
                        Typeface.DEFAULT,
                        Typeface.NORMAL
                );

                service.setBackgroundColor(
                        WHITE
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

                serviceRow.addView(
                        service,
                        params
                );

                final String selectedService =
                        serviceNames[row][col]
                                .replace(
                                        "\n",
                                        " "
                                );

                service.setOnClickListener(v -> {

                    Toast.makeText(
                            this,
                            selectedService,
                            Toast.LENGTH_SHORT
                    ).show();
                });
            }
        }

        // =================================================
        // DEPOSIT BONUS
        // =================================================

        LinearLayout bonus =
                new LinearLayout(this);

        bonus.setOrientation(
                LinearLayout.VERTICAL
        );

        bonus.setPadding(
                dp(10),
                dp(5),
                dp(10),
                dp(5)
        );

        bonus.setBackground(
                bg(
                        DARK_GREEN,
                        15
                )
        );

        main.addView(
                bonus,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(82)
                )
        );

        TextView bonusTitle =
                text(
                        "🎁  ডিপোজিট বোনাস অফার",
                        17,
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
                        dp(30)
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(22)
                )
        );

        // =================================================
        // BOTTOM NAV
        // =================================================

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

    // =====================================================
    // REGISTER
    // =====================================================

    private void showRegister() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout registerRoot =
                new LinearLayout(this);

        registerRoot.setOrientation(
                LinearLayout.VERTICAL
        );

        registerRoot.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        registerRoot.setPadding(
                dp(28),
                dp(18),
                dp(28),
                dp(20)
        );

        registerRoot.setBackgroundColor(
                BLUE
        );

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BLUE);

        scroll.addView(registerRoot);

        setContentView(scroll);

        addSpace(
                registerRoot,
                18
        );

        // LOGO

        TextView logo =
                text(
                        "Quick Pay",
                        31,
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

        registerRoot.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(320),
                        dp(95)
                )
        );

        addSpace(
                registerRoot,
                10
        );

        TextView subtitle =
                text(
                        "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম",
                        16,
                        WHITE
                );

        subtitle.setGravity(
                Gravity.CENTER
        );

        registerRoot.addView(
                subtitle,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(40)
                )
        );

        addSpace(
                registerRoot,
                16
        );

        // COUNTRY

        TextView country =
                text(
                        "🇧🇩   বাংলাদেশ                         ▼",
                        18,
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

        registerRoot.addView(
                country,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        addSpace(
                registerRoot,
                14
        );

        // AGENT CODE

        EditText agentCode =
                registerInput(
                        "🎁   রিসেলার এজেন্ট কোড"
                );

        registerRoot.addView(
                agentCode,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        addSpace(
                registerRoot,
                14
        );

        // NAME

        EditText name =
                registerInput(
                        "👤   পূর্ণ নাম"
                );

        registerRoot.addView(
                name,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        addSpace(
                registerRoot,
                14
        );

        // PHONE

        EditText phone =
                registerInput(
                        "+880   ফোন নম্বর"
                );

        phone.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        registerRoot.addView(
                phone,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        addSpace(
                registerRoot,
                14
        );

        // PASSWORD

        EditText password =
                registerInput(
                        "🔒   ৬ ডিজিট পাসওয়ার্ড"
                );

        password.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        password.setFilters(
                new InputFilter[]{
                        new InputFilter.LengthFilter(6)
                }
        );

        registerRoot.addView(
                password,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        addSpace(
                registerRoot,
                14
        );

        // CONFIRM PASSWORD

        EditText confirmPassword =
                registerInput(
                        "🔒   ৬ ডিজিট পাসওয়ার্ড নিশ্চিত করুন"
                );

        confirmPassword.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        confirmPassword.setFilters(
                new InputFilter[]{
                        new InputFilter.LengthFilter(6)
                }
        );

        registerRoot.addView(
                confirmPassword,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        addSpace(
                registerRoot,
                20
        );

        // NEXT

        TextView next =
                text(
                        "পরবর্তী",
                        21,
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
                        12
                )
        );

        registerRoot.addView(
                next,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
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

            String confirm =
                    confirmPassword
                            .getText()
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

            if (!pass.equals(confirm)) {

                confirmPassword.setError(
                        "পাসওয়ার্ড মিলছে না"
                );

                confirmPassword.requestFocus();
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
                    .putString(
                            "agent_code",
                            agentCode
                                    .getText()
                                    .toString()
                                    .trim()
                    )
                    .putBoolean(
                            "logged_in",
                            true
                    )
                    .apply();

            showPinSetup();
        });

        addSpace(
                registerRoot,
                16
        );

        // LOGIN

        TextView login =
                text(
                        "অ্যাকাউন্ট আছে?  লগইন",
                        18,
                        WHITE
                );

        login.setGravity(
                Gravity.CENTER
        );

        login.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        registerRoot.addView(
                login,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(50)
                )
        );

        login.setOnClickListener(
                v -> showLogin()
        );
    }

    // =====================================================
    // REGISTER INPUT
    // =====================================================

    private EditText registerInput(
            String hint
    ) {

        EditText e =
                new EditText(this);

        e.setHint(hint);
        e.setTextSize(18);

        e.setSingleLine(true);

        e.setTextColor(DARK);

        e.setHintTextColor(
                Color.rgb(
                        145,
                        155,
                        165
                )
        );

        e.setPadding(
                dp(18),
                0,
                dp(15),
                0
        );

        e.setBackground(
                bg(
                        WHITE,
                        12
                )
        );

        return e;
    }

    // =====================================================
    // FORGOT PASSWORD
    // =====================================================

    private void showForgotPassword() {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setPadding(
                dp(20),
                dp(10),
                dp(20),
                dp(5)
        );

        EditText phone =
                new EditText(this);

        phone.setHint(
                "রেজিস্টার করা ফোন"
        );

        phone.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        box.addView(phone);

        EditText newPassword =
                new EditText(this);

        newPassword.setHint(
                "নতুন ৬ ডিজিট পাসওয়ার্ড"
        );

        newPassword.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        newPassword.setFilters(
                new InputFilter[]{
                        new InputFilter.LengthFilter(6)
                }
        );

        box.addView(newPassword);

        new AlertDialog.Builder(this)
                .setTitle(
                        "পাসওয়ার্ড পরিবর্তন"
                )
                .setView(box)
                .setNegativeButton(
                        "বাতিল",
                        null
                )
                .setPositiveButton(
                        "পরিবর্তন",
                        (dialog, which) -> {

                            String savedPhone =
                                    pref.getString(
                                            "phone",
                                            ""
                                    );

                            String enteredPhone =
                                    phone.getText()
                                            .toString()
                                            .trim();

                            String newPass =
                                    newPassword
                                            .getText()
                                            .toString()
                                            .trim();

                            if (savedPhone.equals(
                                    enteredPhone)
                                    && newPass.length() == 6) {

                                pref.edit()
                                        .putString(
                                                "password",
                                                newPass
                                        )
                                        .putBoolean(
                                                "logged_in",
                                                false
                                        )
                                        .apply();

                                Toast.makeText(
                                        this,
                                        "পাসওয়ার্ড পরিবর্তন হয়েছে",
                                        Toast.LENGTH_LONG
                                ).show();

                            } else {

                                Toast.makeText(
                                        this,
                                        "তথ্য সঠিক নয়",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                )
                .show();
    }

    // =====================================================
    // BOTTOM BUTTON
    // =====================================================

    private void addBottomButton(
            LinearLayout parent,
            String title,
            boolean active
    ) {

        TextView b =
                text(
                        title,
                        13,
                        active
                                ? BLUE
                                : Color.GRAY
                );

        b.setGravity(
                Gravity.CENTER
        );

        parent.addView(
                b,
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        1
                )
        );

        b.setOnClickListener(v -> {

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

    // =====================================================
    // BACK BUTTON
    // =====================================================

    @Override
    public void onBackPressed() {

        if (pref != null &&
                pref.getBoolean(
                        "logged_in",
                        false
                )) {

            showHome();

        } else {

            super.onBackPressed();
        }
    }
}
