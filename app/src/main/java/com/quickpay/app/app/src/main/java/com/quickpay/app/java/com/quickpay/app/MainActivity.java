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
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class MainActivity extends Activity {

    // =========================================================
    // COLORS
    // =========================================================

    private final int BLUE = Color.rgb(8, 96, 190);
    private final int DARK_GREEN = Color.rgb(0, 78, 58);
    private final int LIGHT_GREEN = Color.rgb(0, 105, 76);
    private final int WHITE = Color.WHITE;
    private final int DARK = Color.rgb(35, 35, 35);
    private final int LIGHT_BG = Color.rgb(248, 249, 251);
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
        d.setStroke(dp(1), strokeColor);

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
                dp(25),
                dp(18),
                dp(25),
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

        languageBox.setGravity(Gravity.CENTER);

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

        languageText.setGravity(Gravity.CENTER);

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
                        dp(55)
                );

        langParams.gravity = Gravity.RIGHT;

        root.addView(
                languageBox,
                langParams
        );

        languageText.setOnClickListener(v -> {

            bangla = !bangla;

            if (bangla) {

                languageText.setText("বাংলা     EN");

                subtitleText.setText(
                        "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম"
                );

                phoneInput.setHint("ফোন");

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

                languageText.setText("BN     EN");

                subtitleText.setText(
                        "Bangladesh's best recharge business platform"
                );

                phoneInput.setHint("Phone");

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

        addSpace(root, 45);

        // LOGO

        TextView logo =
                text(
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
                bg(WHITE, 18)
        );

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(320),
                        dp(105)
                )
        );

        addSpace(root, 12);

        subtitleText =
                text(
                        "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম",
                        17,
                        WHITE
                );

        subtitleText.setGravity(Gravity.CENTER);

        root.addView(
                subtitleText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        addSpace(root, 20);

        // PHONE

        phoneInput =
                new EditText(this);

        phoneInput.setHint("ফোন");
        phoneInput.setTextSize(18);
        phoneInput.setSingleLine(true);

        phoneInput.setTextColor(DARK);
        phoneInput.setHintTextColor(Color.GRAY);

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

        root.addView(
                phoneInput,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
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
                dp(12),
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
        passwordInput.setHintTextColor(Color.GRAY);

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

        eye.setGravity(Gravity.CENTER);

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

        addSpace(root, 23);

        // LOGIN BUTTON

        TextView loginButton =
                text(
                        "লগইন",
                        21,
                        BLUE
                );

        loginButton.setGravity(Gravity.CENTER);

        loginButton.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        loginButton.setBackground(
                bg(WHITE, 12)
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

            if (password.length() != 6) {

                passwordInput.setError(
                        "৬ ডিজিটের পাসওয়ার্ড দিন"
                );

                passwordInput.requestFocus();

                return;
            }

            String savedPhone =
                    pref.getString("phone", "");

            String savedPassword =
                    pref.getString("password", "");

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
                    .putString("phone", phone)
                    .putString("password", password)
                    .putBoolean("logged_in", true)
                    .apply();

            if (pref.getString("pin", "").isEmpty()) {

                showPinSetup();

            } else {

                showPinUnlock();
            }
        });

        addSpace(root, 15);

        forgotText =
                text(
                        "পাসওয়ার্ড ভুলে গেছেন?",
                        17,
                        WHITE
                );

        forgotText.setGravity(Gravity.CENTER);

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

        registerText.setGravity(Gravity.CENTER);

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
                dp(20),
                dp(20),
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(520)
                )
        );

        TextView lock =
                text(
                        "🔒",
                        50,
                        BLUE
                );

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

        addSpace(card, 18);

        TextView title =
                text(
                        "পিন সেট করুন",
                        27,
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

        subtitle.setGravity(Gravity.CENTER);

        card.addView(
                subtitle,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(42)
                )
        );

        addSpace(card, 8);

        EditText pin = makePinBox();

        pin.setHint("৮ ডিজিট PIN");

        card.addView(
                pin,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        addSpace(card, 10);

        EditText confirm = makePinBox();

        confirm.setHint("PIN আবার দিন");

        card.addView(
                confirm,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        addSpace(card, 16);

        TextView save =
                text(
                        "পিন সেট করুন  ✓",
                        20,
                        BLUE
                );

        save.setGravity(Gravity.CENTER);

        save.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        save.setBackground(
                strokeBg(
                        Color.TRANSPARENT,
                        Color.rgb(80, 145, 205),
                        16
                )
        );

        card.addView(
                save,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(58)
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
                    .putString("pin", p)
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
                dp(20),
                dp(20),
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
                dp(25),
                dp(30),
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(500)
                )
        );

        TextView lock =
                text(
                        "🔒",
                        50,
                        BLUE
                );

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

        addSpace(card, 18);

        TextView title =
                text(
                        "পিন যাচাই করুন",
                        27,
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

        subtitle.setGravity(Gravity.CENTER);

        card.addView(
                subtitle,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        addSpace(card, 8);

        EditText pin = makePinBox();

        pin.setHint("PIN");

        card.addView(
                pin,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        addSpace(card, 18);

        TextView verify =
                text(
                        "যাচাই করুন  ✓",
                        20,
                        BLUE
                );

        verify.setGravity(Gravity.CENTER);

        verify.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        verify.setBackground(
                strokeBg(
                        Color.TRANSPARENT,
                        Color.rgb(80, 145, 205),
                        16
                )
        );

        card.addView(
                verify,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(58)
                )
        );

        verify.setOnClickListener(v -> {

            String entered =
                    pin.getText()
                            .toString()
                            .trim();

            String saved =
                    pref.getString("pin", "");

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

                pin.setError("ভুল PIN");

                Toast.makeText(
                        this,
                        "PIN সঠিক নয়",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        addSpace(card, 10);

        TextView forgotPin =
                text(
                        "PIN ভুলে গেছেন?  লগইন করুন",
                        16,
                        BLUE
                );

        forgotPin.setGravity(Gravity.CENTER);

        card.addView(
                forgotPin,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(40)
                )
        );

        forgotPin.setOnClickListener(v -> {

            pref.edit()
                    .putBoolean("logged_in", false)
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

        pin.setGravity(Gravity.CENTER);

        pin.setTextColor(DARK);

        pin.setHintTextColor(
                Color.rgb(150, 150, 150)
        );

        pin.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        pin.setBackground(
                strokeBg(
                        Color.rgb(248, 250, 253),
                        Color.rgb(215, 225, 235),
                        15
                )
        );

        return pin;
    }

    // =========================================================
    // HOME
    // =========================================================

    private void showHome() {

        getWindow().setStatusBarColor(DARK_GREEN);
        getWindow().setNavigationBarColor(WHITE);

        LinearLayout main =
                new LinearLayout(this);

        main.setOrientation(
                LinearLayout.VERTICAL
        );

        main.setBackgroundColor(WHITE);

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
                dp(6),
                dp(12),
                dp(6)
        );

        header.setBackgroundColor(DARK_GREEN);

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(190)
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
                        22,
                        DARK
                );

        brand.setGravity(Gravity.CENTER);

        brand.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        brand.setBackground(
                bg(WHITE, 17)
        );

        top.addView(
                brand,
                new LinearLayout.LayoutParams(
                        dp(150),
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

        en.setGravity(Gravity.CENTER);

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

        bell.setGravity(Gravity.CENTER);

        top.addView(
                bell,
                new LinearLayout.LayoutParams(
                        dp(45),
                        dp(48)
                )
        );

        TextView logout =
                text(
                        "⇥",
                        27,
                        WHITE
                );

        logout.setGravity(Gravity.CENTER);

        top.addView(
                logout,
                new LinearLayout.LayoutParams(
                        dp(45),
                        dp(48)
                )
        );

        logout.setOnClickListener(v -> {

            pref.edit()
                    .putBoolean("logged_in", false)
                    .apply();

            showLogin();
        });

        // USER ROW

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
                        dp(60),
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

        balance.setGravity(Gravity.CENTER);

        balance.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        balance.setBackground(
                bg(YELLOW, 35)
        );

        userRow.addView(
                balance,
                new LinearLayout.LayoutParams(
                        dp(215),
                        dp(60)
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

        notice.setSingleLine(true);

        notice.setBackground(
                bg(WHITE, 13)
        );

        LinearLayout.LayoutParams noticeParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(40)
                );

        noticeParams.topMargin = dp(6);

        header.addView(
                notice,
                noticeParams
        );

        // =====================================================
        // SERVICES
        // =====================================================

        LinearLayout services =
                new LinearLayout(this);

        services.setOrientation(
                LinearLayout.VERTICAL
        );

        services.setGravity(Gravity.CENTER);

        services.setPadding(
                dp(12),
                dp(5),
                dp(12),
                dp(2)
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
                createServiceRow(services);

        addService(
                row1,
                "👛",
                "ওয়ালেট\nডিপোজিট",
                1
        );

        addService(
                row1,
                "💵",
                "মোবাইল\nব্যাংকিং",
                2
        );

        addService(
                row1,
                "🏦",
                "ব্যাংক\nট্রান্সফার",
                3
        );

        addService(
                row1,
                "📱",
                "মোবাইল\nরিচার্জ",
                4
        );

        // ROW 2

        LinearLayout row2 =
                createServiceRow(services);

        addService(
                row2,
                "💬",
                "গ্রুপ\nচ্যাট",
                5
        );

        addService(
                row2,
                "🎁",
                "ইনভাইট\nবোনাস",
                6
        );

        addService(
                row2,
                "🧾",
                "বিল\nপে",
                7
        );

        addService(
                row2,
                "🏷",
                "বিশেষ\nঅফার",
                8
        );

        // ROW 3

        LinearLayout row3 =
                createServiceRow(services);

        addService(
                row3,
                "🎧",
                "কাস্টমার\nকেয়ার",
                9
        );

        addService(
                row3,
                "⭐",
                "কাস্টমার\nরিভিউ",
                10
        );

        addService(
                row3,
                "▶",
                "ভিডিও\nটিউটোরিয়াল",
                11
        );

        addService(
                row3,
                "👥",
                "আমাদের\nসম্পর্কে",
                12
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
                bg(DARK_GREEN, 18)
        );

        main.addView(
                bonus,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(105)
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(35)
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
                        dp(22)
                )
        );

        LinearLayout bonusBoxes =
                new LinearLayout(this);

        bonusBoxes.setGravity(Gravity.CENTER);

        bonus.addView(
                bonusBoxes,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
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
        // BOTTOM NAV
        // =====================================================

        LinearLayout bottom =
                new LinearLayout(this);

        bottom.setGravity(Gravity.CENTER);

        bottom.setBackgroundColor(WHITE);

        main.addView(
                bottom,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
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

        row.setGravity(Gravity.CENTER);

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
            String title,
            int type
    ) {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(Gravity.CENTER);

        TextView iconText =
                text(
                        icon,
                        29,
                        DARK
                );

        iconText.setGravity(Gravity.CENTER);

        box.addView(
                iconText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(42)
                )
        );

        TextView titleText =
                text(
                        title,
                        12,
                        DARK
                );

        titleText.setGravity(Gravity.CENTER);

        box.addView(
                titleText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(38)
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
                dp(1),
                dp(2),
                dp(1)
        );

        row.addView(box, params);

        box.setOnClickListener(v -> {

            switch (type) {

                case 1:
                    showWalletDeposit();
                    break;

                case 2:
                    showMobileBanking();
                    break;

                case 3:
                    showBankTransfer();
                    break;

                default:
                    Toast.makeText(
                            this,
                            title.replace("\n", " "),
                            Toast.LENGTH_SHORT
                    ).show();
                    break;
            }
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

        box.setGravity(Gravity.CENTER);

        box.setBackground(
                bg(WHITE, 10)
        );

        TextView amountText =
                text(
                        amount,
                        14,
                        DARK
                );

        amountText.setGravity(Gravity.CENTER);

        amountText.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        box.addView(
                amountText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(21)
                )
        );

        TextView bonusText =
                text(
                        bonus,
                        11,
                        Color.rgb(170, 40, 40)
                );

        bonusText.setGravity(Gravity.CENTER);

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
                        dp(42),
                        1
                );

        params.setMargins(
                dp(3),
                0,
                dp(3),
                0
        );

        parent.addView(box, params);
    }

    // =========================================================
    // BOTTOM BUTTON
    // =========================================================

    private void addBottomButton(
            LinearLayout parent,
            String value,
            boolean active
    ) {

        TextView button =
                text(
                        value,
                        13,
                        active ? DARK_GREEN : Color.GRAY
                );

        button.setGravity(Gravity.CENTER);

        button.setTypeface(
                Typeface.DEFAULT,
                active
                        ? Typeface.BOLD
                        : Typeface.NORMAL
        );

        parent.addView(
                button,
                new LinearLayout.LayoutParams(
                        0,
                        dp(60),
                        1
                )
        );
    }

    // =========================================================
    // MOBILE BANKING
    // =========================================================

    private void showMobileBanking() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(WHITE);

        LinearLayout main =
                new LinearLayout(this);

        main.setOrientation(
                LinearLayout.VERTICAL
        );

        main.setBackgroundColor(LIGHT_BG);

        setContentView(main);

        // HEADER

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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        TextView back =
                text(
                        "‹",
                        38,
                        WHITE
                );

        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(55),
                        dp(62)
                )
        );

        back.setOnClickListener(
                v -> showHome()
        );

        TextView title =
                text(
                        "মোবাইল ব্যাংকিং",
                        21,
                        WHITE
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
                text(
                        "♧",
                        27,
                        WHITE
                );

        bell.setGravity(Gravity.CENTER);

        header.addView(
                bell,
                new LinearLayout.LayoutParams(
                        dp(55),
                        dp(62)
                )
        );

        // NOTICE

        TextView notice =
                text(
                        "●  মোবাইল ব্যাংকিং এর মাধ্যমে সহজেই ডিপোজিট করুন",
                        14,
                        Color.DKGRAY
                );

        notice.setGravity(
                Gravity.CENTER_VERTICAL
        );

        notice.setPadding(
                dp(15),
                0,
                dp(10),
                0
        );

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

        main.addView(
                notice,
                noticeParams
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

        // BKASH

        addBankMethod(
                content,
                "bKash",
                "বিকাশ এজেন্ট নম্বর",
                "01XXXXXXXXX"
        );

        // NAGAD

        addBankMethod(
                content,
                "Nagad",
                "নগদ এজেন্ট নম্বর",
                "01XXXXXXXXX"
        );

        // ROCKET

        addBankMethod(
                content,
                "Rocket",
                "রকেট এজেন্ট নম্বর",
                "01XXXXXXXXX"
        );

        // UPAY

        addBankMethod(
                content,
                "Upay",
                "উপায় এজেন্ট নম্বর",
                "01XXXXXXXXX"
        );

        // AUTO DEPOSIT

        TextView autoDeposit =
                text(
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
    // MOBILE BANKING CARD
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

        copy.setGravity(Gravity.CENTER);

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

            ClipboardManager clipboard =
                    (ClipboardManager)
                            getSystemService(
                                    Context.CLIPBOARD_SERVICE
                            );

            ClipData clip =
                    ClipData.newPlainText(
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

        parent.addView(card, params);
    }

    // =========================================================
    // WALLET DEPOSIT
    // =========================================================

    private void showWalletDeposit() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(WHITE);

        LinearLayout main =
                new LinearLayout(this);

        main.setOrientation(
                LinearLayout.VERTICAL
        );

        main.setBackgroundColor(LIGHT_BG);

        setContentView(main);

        addSimpleHeader(
                main,
                "ওয়ালেট ডিপোজিট"
        );

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );

        main.addView(
                content,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        TextView info =
                text(
                        "আপনার Quick Pay ওয়ালেটে টাকা জমা করুন।\n\n" +
                        "ডিপোজিট করার জন্য মোবাইল ব্যাংকিং অথবা ব্যাংক ট্রান্সফার ব্যবহার করুন।",
                        17,
                        DARK
                );

        info.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );

        info.setBackground(
                bg(WHITE, 16)
        );

        content.addView(
                info,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(150)
                )
        );

        addSpace(content, 20);

        TextView mobile =
                text(
                        "মোবাইল ব্যাংকিং",
                        19,
                        WHITE
                );

        mobile.setGravity(Gravity.CENTER);

        mobile.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        mobile.setBackground(
                bg(BLUE, 14)
        );

        content.addView(
                mobile,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(58)
                )
        );

        mobile.setOnClickListener(
                v -> showMobileBanking()
        );
    }

    // =========================================================
    // BANK TRANSFER
    // =========================================================

    private void showBankTransfer() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(WHITE);

        LinearLayout main =
                new LinearLayout(this);

        main.setOrientation(
                LinearLayout.VERTICAL
        );

        main.setBackgroundColor(LIGHT_BG);

        setContentView(main);

        addSimpleHeader(
                main,
                "ব্যাংক ট্রান্সফার"
        );

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(18)
        );

        main.addView(
                content,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        0,
                        1
                )
        );

        addBankInfo(
                content,
                "ব্যাংকের নাম",
                "Quick Pay Bank"
        );

        addBankInfo(
                content,
                "অ্যাকাউন্ট নাম",
                "Quick Pay"
        );

        addBankInfo(
                content,
                "অ্যাকাউন্ট নম্বর",
                "XXXXXXXXXXXX"
        );

        addBankInfo(
                content,
                "রাউটিং নম্বর",
                "XXXXXXXXX"
        );
    }

    // =========================================================
    // SIMPLE HEADER
    // =========================================================

    private void addSimpleHeader(
            LinearLayout main,
            String title
    ) {

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        TextView back =
                text(
                        "‹",
                        38,
                        WHITE
                );

        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(55),
                        dp(62)
                )
        );

        back.setOnClickListener(
                v -> showHome()
        );

        TextView titleText =
                text(
                        title,
                        21,
                        WHITE
                );

        titleText.setGravity(
                Gravity.CENTER_VERTICAL
        );

        titleText.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        header.addView(
                titleText,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1
                )
        );

        Space space =
                new Space(this);

        header.addView(
                space,
                new LinearLayout.LayoutParams(
                        dp(55),
                        dp(62)
                )
        );
    }

    // =========================================================
    // BANK INFO
    // =========================================================

    private void addBankInfo(
            LinearLayout parent,
            String title,
            String value
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(16),
                dp(12),
                dp(16),
                dp(12)
        );

        card.setBackground(
                bg(WHITE, 14)
        );

        TextView t =
                text(
                        title,
                        14,
                        Color.GRAY
                );

        card.addView(
                t,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(25)
                )
        );

        TextView v =
                text(
                        value,
                        18,
                        DARK
                );

        v.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                v,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(32)
                )
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(75)
                );

        params.setMargins(
                0,
                0,
                0,
                dp(10)
        );

        parent.addView(card, params);
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
                dp(15),
                dp(25),
                dp(20)
        );

        root.setBackgroundColor(BLUE);

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.addView(root);

        setContentView(scroll);

        addSpace(root, 10);

        TextView logo =
                text(
                        "Quick Pay",
                        30,
                        BLUE
                );

        logo.setGravity(Gravity.CENTER);

        logo.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        logo.setBackground(
                bg(WHITE, 18)
        );

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(260),
                        dp(75)
                )
        );

        addSpace(root, 12);

        // COUNTRY

        TextView country =
                makeRegisterBox(
                        "🇧🇩   বাংলাদেশ                         ▼"
                );

        root.addView(
                country,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(58)
                )
        );

        addSpace(root, 10);

        // AGENT CODE

        EditText agentCode =
                makeRegisterInput(
                        "রিসেলার এজেন্ট কোড"
                );

        root.addView(
                agentCode,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(58)
                )
        );

        addSpace(root, 10);

        // NAME

        EditText nameInput =
                makeRegisterInput(
                        "পূর্ণ নাম"
                );

        root.addView(
                nameInput,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(58)
                )
        );

        addSpace(root, 10);

        // PHONE

        EditText phone =
                makeRegisterInput(
                        "+880 ফোন নম্বর"
                );

        phone.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(58)
                )
        );

        addSpace(root, 10);

        // PASSWORD

        LinearLayout passBox =
                createPasswordBox(
                        "৬ ডিজিট পাসওয়ার্ড"
                );

        root.addView(
                passBox,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(58)
                )
        );

        addSpace(root, 10);

        // CONFIRM

        LinearLayout confirmBox =
                createPasswordBox(
                        "৬ ডিজিট পাসওয়ার্ড নিশ্চিত করুন"
                );

        root.addView(
                confirmBox,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(58)
                )
        );

        addSpace(root, 18);

        TextView next =
                text(
                        "পরবর্তী",
                        20,
                        BLUE
                );

        next.setGravity(Gravity.CENTER);

        next.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        next.setBackground(
                bg(WHITE, 14)
        );

        root.addView(
                next,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        next.setOnClickListener(v -> {

            String n =
                    nameInput.getText()
                            .toString()
                            .trim();

            String p =
                    phone.getText()
                            .toString()
                            .trim();

            EditText pass =
                    (EditText) passBox.getChildAt(0);

            EditText confirm =
                    (EditText) confirmBox.getChildAt(0);

            String password =
                    pass.getText()
                            .toString()
                            .trim();

            String confirmPassword =
                    confirm.getText()
                            .toString()
                            .trim();

            if (n.isEmpty()) {

                nameInput.setError(
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

            if (password.length() != 6) {

                pass.setError(
                        "৬ ডিজিটের পাসওয়ার্ড দিন"
                );

                return;
            }

            if (!password.equals(confirmPassword)) {

                confirm.setError(
                        "পাসওয়ার্ড মিলছে না"
                );

                return;
            }

            pref.edit()
                    .putString("name", n)
                    .putString("phone", p)
                    .putString("password", password)
                    .putBoolean("logged_in", true)
                    .apply();

            showPinSetup();
        });

        addSpace(root, 12);

        TextView login =
                text(
                        "অ্যাকাউন্ট আছে?  লগইন",
                        17,
                        WHITE
                );

        login.setGravity(Gravity.CENTER);

        root.addView(
                login,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        login.setOnClickListener(
                v -> showLogin()
        );
    }

    // =========================================================
    // REGISTER INPUT
    // =========================================================

    private EditText makeRegisterInput(
            String hint
    ) {

        EditText e =
                new EditText(this);

        e.setHint(hint);
        e.setTextSize(16);
        e.setSingleLine(true);

        e.setTextColor(DARK);
        e.setHintTextColor(Color.GRAY);

        e.setPadding(
                dp(18),
                0,
                dp(18),
                0
        );

        e.setBackground(
                bg(WHITE, 12)
        );

        return e;
    }

    // =========================================================
    // REGISTER COUNTRY
    // =========================================================

    private TextView makeRegisterBox(
            String value
    ) {

        TextView t =
                text(
                        value,
                        16,
                        DARK
                );

        t.setGravity(
                Gravity.CENTER_VERTICAL
        );

        t.setPadding(
                dp(18),
                0,
                dp(18),
                0
        );

        t.setBackground(
                bg(WHITE, 12)
        );

        return t;
    }

    // =========================================================
    // REGISTER PASSWORD BOX
    // =========================================================

    private LinearLayout createPasswordBox(
            String hint
    ) {

        LinearLayout box =
                new LinearLayout(this);

        box.setGravity(
                Gravity.CENTER_VERTICAL
        );

        box.setPadding(
                dp(8),
                0,
                dp(5),
                0
        );

        box.setBackground(
                bg(WHITE, 12)
        );

        EditText input =
                new EditText(this);

        input.setHint(hint);
        input.setTextSize(16);
        input.setSingleLine(true);

        input.setTextColor(DARK);
        input.setHintTextColor(Color.GRAY);

        input.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        input.setBackgroundColor(
                Color.TRANSPARENT
        );

        box.addView(
                input,
                new LinearLayout.LayoutParams(
                        0,
                        dp(58),
                        1
                )
        );

        TextView eye =
                text(
                        "◉",
                        22,
                        BLUE
                );

        eye.setGravity(Gravity.CENTER);

        box.addView(
                eye,
                new LinearLayout.LayoutParams(
                        dp(50),
                        dp(58)
                )
        );

        eye.setOnClickListener(v -> {

            int type =
                    input.getInputType();

            if ((type &
                    InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                    != 0) {

                input.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                );

            } else {

                input.setInputType(
                        InputType.TYPE_CLASS_NUMBER |
                                InputType.TYPE_NUMBER_VARIATION_PASSWORD
                );
            }

            input.setSelection(
                    input.length()
            );
        });

        return box;
    }

    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    private void showForgotPassword() {

        final EditText input =
                new EditText(this);

        input.setHint("নতুন ৬ ডিজিটের পাসওয়ার্ড");
        input.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        input.setSingleLine(true);

        int pad = dp(20);

        FrameLayout container =
                new FrameLayout(this);

        container.setPadding(
                pad,
                0,
                pad,
                0
        );

        container.addView(
                input,
                new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(60)
                )
        );

        new AlertDialog.Builder(this)
                .setTitle("পাসওয়ার্ড পরিবর্তন")
                .setMessage(
                        "আপনার নতুন ৬ ডিজিটের পাসওয়ার্ড দিন"
                )
                .setView(container)
                .setNegativeButton(
                        "বাতিল",
                        null
                )
                .setPositiveButton(
                        "সংরক্ষণ",
                        (dialog, which) -> {

                            String newPassword =
                                    input.getText()
                                            .toString()
                                            .trim();

                            if (newPassword.length() != 6) {

                                Toast.makeText(
                                        this,
                                        "৬ ডিজিটের পাসওয়ার্ড দিন",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            pref.edit()
                                    .putString(
                                            "password",
                                            newPassword
                                    )
                                    .apply();

                            Toast.makeText(
                                    this,
                                    "পাসওয়ার্ড পরিবর্তন হয়েছে",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                )
                .show();
    }

    // =========================================================
    // BACK BUTTON
    // =========================================================

    @Override
    public void onBackPressed() {

        if (pref.getBoolean("logged_in", false)) {

            showHome();

        } else {

            showLogin();
        }
    }
}
