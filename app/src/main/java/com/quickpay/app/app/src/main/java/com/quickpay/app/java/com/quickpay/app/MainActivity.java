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
import android.view.inputmethod.InputMethodManager;
import android.widget.*;

public class MainActivity extends Activity {

    private final int BLUE = Color.rgb(8, 96, 190);
    private final int DARK_GREEN = Color.rgb(0, 92, 68);
    private final int WHITE = Color.WHITE;
    private final int DARK = Color.rgb(35, 35, 35);

    private SharedPreferences pref;

    private LinearLayout root;

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

        pref = getSharedPreferences("quick_pay", Context.MODE_PRIVATE);

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        /*
         * Login করা থাকলে এবং PIN সেট করা থাকলে
         * সরাসরি PIN Lock Screen
         */
        if (pref.getBoolean("logged_in", false)
                && !pref.getString("pin", "").isEmpty()) {

            showPinUnlock();

        } else if (pref.getBoolean("logged_in", false)) {

            showPinSetup();

        } else {

            showLogin();
        }
    }

    // ====================================================
    // BASIC HELPERS
    // ====================================================

    private int dp(float value) {
        return (int) (value *
                getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView text(String value, float size, int color) {

        TextView t = new TextView(this);

        t.setText(value);
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

    private GradientDrawable strokeBg(
            int color,
            int strokeColor,
            float radius) {

        GradientDrawable d = new GradientDrawable();

        d.setColor(color);
        d.setCornerRadius(dp(radius));
        d.setStroke(dp(1), strokeColor);

        return d;
    }

    private void clearScreen() {

        root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(
                dp(24),
                dp(20),
                dp(24),
                dp(20)
        );

        setContentView(root);
    }

    // ====================================================
    // LOGIN SCREEN
    // ====================================================

    private void showLogin() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout loginRoot = new LinearLayout(this);

        loginRoot.setOrientation(LinearLayout.VERTICAL);
        loginRoot.setGravity(Gravity.CENTER_HORIZONTAL);

        loginRoot.setPadding(
                dp(30),
                dp(20),
                dp(30),
                dp(20)
        );

        loginRoot.setBackgroundColor(BLUE);

        ScrollView scroll = new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BLUE);
        scroll.addView(loginRoot);

        setContentView(scroll);

        // ---------------- LANGUAGE ----------------

        LinearLayout languageBox = new LinearLayout(this);

        languageBox.setGravity(Gravity.CENTER);

        languageBox.setBackground(
                bg(Color.rgb(55, 130, 205), 40)
        );

        languageText = text(
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

        LinearLayout.LayoutParams languageParams =
                new LinearLayout.LayoutParams(
                        dp(175),
                        dp(56)
                );

        languageParams.gravity = Gravity.RIGHT;

        loginRoot.addView(
                languageBox,
                languageParams
        );

        languageText.setOnClickListener(v -> {

            bangla = !bangla;

            if (bangla) {
                languageText.setText("বাংলা     EN");
                subtitleText.setText(
                        "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম"
                );
                phoneInput.setHint("ফোন");
                passwordInput.setHint("৬ ডিজিট পাসওয়ার্ড");
                forgotText.setText("পাসওয়ার্ড ভুলে গেছেন?");
                registerText.setText(
                        "অ্যাকাউন্ট নেই?  রেজিস্টার করুন"
                );
            } else {
                languageText.setText("BN     EN");
                subtitleText.setText(
                        "Bangladesh's best recharge business platform"
                );
                phoneInput.setHint("Phone");
                passwordInput.setHint("6 Digit Password");
                forgotText.setText("Forgot Password?");
                registerText.setText(
                        "Don't have an account?  Register"
                );
            }
        });

        addSpace(loginRoot, 90);

        // ---------------- LOGO ----------------

        TextView logo = text(
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

        loginRoot.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(320),
                        dp(120)
                )
        );

        addSpace(loginRoot, 12);

        subtitleText = text(
                "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম",
                18,
                WHITE
        );

        subtitleText.setGravity(Gravity.CENTER);

        loginRoot.addView(
                subtitleText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        addSpace(loginRoot, 25);

        // ---------------- PHONE ----------------

        phoneInput = new EditText(this);

        phoneInput.setHint("ফোন");
        phoneInput.setTextSize(19);
        phoneInput.setSingleLine(true);

        phoneInput.setTextColor(DARK);
        phoneInput.setHintTextColor(Color.GRAY);

        phoneInput.setPadding(
                dp(22),
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
                        dp(62)
                )
        );

        addSpace(loginRoot, 22);

        // ---------------- PASSWORD ----------------

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

        passwordInput = new EditText(this);

        passwordInput.setHint("৬ ডিজিট পাসওয়ার্ড");
        passwordInput.setTextSize(19);

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

        LinearLayout.LayoutParams passParams =
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1
                );

        passwordBox.addView(
                passwordInput,
                passParams
        );

        TextView eye = text(
                "◉",
                26,
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

        loginRoot.addView(
                passwordBox,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        eye.setOnClickListener(v -> {

            int type = passwordInput.getInputType();

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

        addSpace(loginRoot, 28);

        // ---------------- LOGIN BUTTON ----------------

        TextView loginButton = text(
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

        loginRoot.addView(
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

            Toast.makeText(
                    this,
                    "লগইন সফল",
                    Toast.LENGTH_SHORT
            ).show();

            if (pref.getString("pin", "").isEmpty()) {
                showPinSetup();
            } else {
                showHome();
            }
        });

        addSpace(loginRoot, 22);

        // ---------------- FORGOT ----------------

        forgotText = text(
                "পাসওয়ার্ড ভুলে গেছেন?",
                17,
                WHITE
        );

        forgotText.setGravity(Gravity.CENTER);

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

        // ---------------- REGISTER ----------------

        registerText = text(
                "অ্যাকাউন্ট নেই?  রেজিস্টার করুন",
                18,
                WHITE
        );

        registerText.setGravity(Gravity.CENTER);

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

    // ====================================================
    // PIN SETUP
    // ====================================================

    private void showPinSetup() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout screen = new LinearLayout(this);

        screen.setOrientation(LinearLayout.VERTICAL);
        screen.setGravity(Gravity.CENTER);
        screen.setPadding(
                dp(35),
                dp(30),
                dp(35),
                dp(30)
        );

        screen.setBackgroundColor(BLUE);

        setContentView(screen);

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_HORIZONTAL);

        card.setPadding(
                dp(30),
                dp(35),
                dp(30),
                dp(35)
        );

        card.setBackground(
                bg(WHITE, 25)
        );

        screen.addView(
                card,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(500)
                )
        );

        TextView lock = text(
                "🔒",
                55,
                BLUE
        );

        lock.setGravity(Gravity.CENTER);

        lock.setBackground(
                bg(Color.rgb(232, 240, 250), 60)
        );

        card.addView(
                lock,
                new LinearLayout.LayoutParams(
                        dp(110),
                        dp(110)
                )
        );

        addSpace(card, 25);

        TextView title = text(
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

        TextView subtitle = text(
                "অ্যাপে ঢোকার জন্য ৪ ডিজিটের পিন দিন",
                17,
                Color.DKGRAY
        );

        subtitle.setGravity(Gravity.CENTER);

        card.addView(
                subtitle,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(40)
                )
        );

        addSpace(card, 15);

        EditText pin = makePinBox();

        card.addView(
                pin,
                new LinearLayout.LayoutParams(
                        dp(230),
                        dp(65)
                )
        );

        addSpace(card, 15);

        EditText confirmPin = makePinBox();

        confirmPin.setHint("PIN আবার দিন");

        card.addView(
                confirmPin,
                new LinearLayout.LayoutParams(
                        dp(230),
                        dp(65)
                )
        );

        addSpace(card, 20);

        TextView save = text(
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
                        WHITE,
                        Color.rgb(80, 145, 205),
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

            String p = pin.getText()
                    .toString()
                    .trim();

            String cp = confirmPin.getText()
                    .toString()
                    .trim();

            if (p.length() != 4) {

                Toast.makeText(
                        this,
                        "৪ ডিজিটের PIN দিন",
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
                    .putString("pin", p)
                    .putBoolean("logged_in", true)
                    .apply();

            Toast.makeText(
                    this,
                    "PIN সেট হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();

            showHome();
        });
    }

    // ====================================================
    // PIN UNLOCK
    // ====================================================

    private void showPinUnlock() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout screen = new LinearLayout(this);

        screen.setOrientation(LinearLayout.VERTICAL);
        screen.setGravity(Gravity.CENTER);
        screen.setPadding(
                dp(35),
                dp(30),
                dp(35),
                dp(30)
        );

        screen.setBackgroundColor(BLUE);

        setContentView(screen);

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_HORIZONTAL);

        card.setPadding(
                dp(30),
                dp(40),
                dp(30),
                dp(35)
        );

        card.setBackground(
                bg(WHITE, 25)
        );

        screen.addView(
                card,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(500)
                )
        );

        TextView lock = text(
                "🔒",
                55,
                BLUE
        );

        lock.setGravity(Gravity.CENTER);

        lock.setBackground(
                bg(Color.rgb(232, 240, 250), 60)
        );

        card.addView(
                lock,
                new LinearLayout.LayoutParams(
                        dp(110),
                        dp(110)
                )
        );

        addSpace(card, 25);

        TextView title = text(
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

        TextView subtitle = text(
                "আপনার ৪ ডিজিটের পিন দিন",
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

        addSpace(card, 15);

        EditText pin = makePinBox();

        pin.setGravity(Gravity.CENTER);

        card.addView(
                pin,
                new LinearLayout.LayoutParams(
                        dp(230),
                        dp(65)
                )
        );

        addSpace(card, 25);

        TextView verify = text(
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
                        WHITE,
                        Color.rgb(80, 145, 205),
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

            String entered = pin.getText()
                    .toString()
                    .trim();

            String saved = pref.getString(
                    "pin",
                    ""
            );

            if (entered.length() != 4) {

                Toast.makeText(
                        this,
                        "৪ ডিজিটের PIN দিন",
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

        addSpace(card, 15);

        TextView forgotPin = text(
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

    // ====================================================
    // PIN BOX
    // ====================================================

    private EditText makePinBox() {

        EditText pin = new EditText(this);

        pin.setTextSize(22);
        pin.setSingleLine(true);

        pin.setGravity(Gravity.CENTER);

        pin.setTextColor(DARK);
        pin.setHintTextColor(Color.GRAY);

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

    // ====================================================
    // HOME SCREEN
    // ====================================================

    private void showHome() {

        getWindow().setStatusBarColor(DARK_GREEN);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout main =
                new LinearLayout(this);

        main.setOrientation(
                LinearLayout.VERTICAL
        );

        main.setBackgroundColor(
                Color.rgb(245, 247, 249)
        );

        setContentView(main);

        // ---------------- HEADER ----------------

        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.VERTICAL
        );

        header.setPadding(
                dp(16),
                dp(10),
                dp(16),
                dp(10)
        );

        header.setBackgroundColor(
                DARK_GREEN
        );

        main.addView(
                header,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(210)
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
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(48)
                )
        );

        TextView brand = text(
                "Quick Pay",
                22,
                WHITE
        );

        brand.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        top.addView(
                brand,
                new LinearLayout.LayoutParams(
                        0,
                        dp(48),
                        1
                )
        );

        TextView en = text(
                "EN",
                15,
                WHITE
        );

        en.setGravity(Gravity.CENTER);

        top.addView(
                en,
                new LinearLayout.LayoutParams(
                        dp(45),
                        dp(48)
                )
        );

        TextView bell = text(
                "🔔",
                20,
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

        TextView logout = text(
                "⇥",
                26,
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

        // ---------------- USER ----------------

        LinearLayout user =
                new LinearLayout(this);

        user.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.addView(
                user,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(55)
                )
        );

        String savedName = pref.getString(
                "name",
                "Rosy"
        );

        TextView userName = text(
                savedName,
                23,
                WHITE
        );

        userName.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        user.addView(
                userName,
                new LinearLayout.LayoutParams(
                        0,
                        dp(55),
                        1
                )
        );

        TextView balance =
                text(
                        "মেইন ব্যালেন্স: ৳ ১২,৫০০\n" +
                        "ড্রাইভ ব্যালেন্স: ৳ ১৮০",
                        14,
                        Color.rgb(40, 40, 40)
                );

        balance.setGravity(
                Gravity.CENTER
        );

        balance.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        balance.setBackground(
                bg(Color.rgb(255, 213, 70), 16)
        );

        user.addView(
                balance,
                new LinearLayout.LayoutParams(
                        dp(215),
                        dp(55)
                )
        );

        // ---------------- NOTICE ----------------

        TextView notice = text(
                "📢 সর্বশেষ আপডেট: Quick Pay-এ স্বাগতম",
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
                bg(WHITE, 12)
        );

        LinearLayout.LayoutParams noticeParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(38)
                );

        noticeParams.topMargin = dp(8);

        header.addView(
                notice,
                noticeParams
        );

        // ---------------- SERVICES ----------------

        LinearLayout services =
                new LinearLayout(this);

        services.setOrientation(
                LinearLayout.VERTICAL
        );

        services.setPadding(
                dp(10),
                dp(8),
                dp(10),
                dp(5)
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
                        "💰\nWallet Deposit",
                        "📱\nMobile Banking",
                        "🏦\nBank Transfer",
                        "📲\nMobile Recharge"
                },

                {
                        "💬\nGroup Chat",
                        "🎁\nInvite Bonus",
                        "🧾\nBill Pay",
                        "⭐\nSpecial Offer"
                },

                {
                        "🎧\nCustomer Care",
                        "⭐\nCustomer Review",
                        "▶\nVideo Tutorial",
                        "☎\nContact Us"
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

                TextView card =
                        text(
                                serviceNames[row][col],
                                12,
                                DARK
                        );

                card.setGravity(
                        Gravity.CENTER
                );

                card.setTypeface(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                );

                card.setBackground(
                        strokeBg(
                                WHITE,
                                Color.rgb(225, 225, 225),
                                14
                        )
                );

                LinearLayout.LayoutParams cardParams =
                        new LinearLayout.LayoutParams(
                                0,
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                1
                        );

                cardParams.setMargins(
                        dp(4),
                        dp(4),
                        dp(4),
                        dp(4)
                );

                serviceRow.addView(
                        card,
                        cardParams
                );

                final String service =
                        serviceNames[row][col];

                card.setOnClickListener(v -> {

                    Toast.makeText(
                            this,
                            service.replace("\n", " "),
                            Toast.LENGTH_SHORT
                    ).show();
                });
            }
        }

        // ---------------- BANNER ----------------

        TextView banner = text(
                "🎁  রিচার্জ করলেই পাবেন\n" +
                        "বিশেষ বোনাস",
                19,
                WHITE
        );

        banner.setGravity(
                Gravity.CENTER
        );

        banner.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        banner.setBackground(
                bg(BLUE, 16)
        );

        LinearLayout.LayoutParams bannerParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(75)
                );

        bannerParams.setMargins(
                dp(12),
                dp(4),
                dp(12),
                dp(4)
        );

        main.addView(
                banner,
                bannerParams
        );

        // ---------------- BOTTOM NAV ----------------

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
                "↔\nলেনদেন",
                false
        );

        addBottomButton(
                bottom,
                "●\nপ্রোফাইল",
                false
        );
    }

    // ====================================================
    // REGISTER SCREEN
    // ====================================================

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
                dp(30),
                dp(20),
                dp(30),
                dp(20)
        );

        registerRoot.setBackgroundColor(BLUE);

        ScrollView scroll = new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.setBackgroundColor(BLUE);
        scroll.addView(registerRoot);

        setContentView(scroll);

        addSpace(registerRoot, 30);

        // ---------------- LOGO ----------------

        TextView logo = text(
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

        registerRoot.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(320),
                        dp(120)
                )
        );

        addSpace(registerRoot, 10);

        TextView subtitle = text(
                "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম",
                18,
                WHITE
        );

        subtitle.setGravity(Gravity.CENTER);

        registerRoot.addView(
                subtitle,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        addSpace(registerRoot, 20);

        // ---------------- COUNTRY ----------------

        TextView country = text(
                "🇧🇩   বাংলাদেশ                         ▼",
                19,
                DARK
        );

        country.setGravity(
                Gravity.CENTER_VERTICAL
        );

        country.setPadding(
                dp(20),
                0,
                dp(15),
                0
        );

        country.setBackground(
                bg(WHITE, 12)
        );

        registerRoot.addView(
                country,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        addSpace(registerRoot, 18);

        // ---------------- AGENT CODE ----------------

        EditText agentCode = registerInput(
                "🎁   রিসেলার এজেন্ট কোড"
        );

        registerRoot.addView(
                agentCode,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        addSpace(registerRoot, 18);

        // ---------------- NAME ----------------

        EditText name = registerInput(
                "👤   পূর্ণ নাম"
        );

        registerRoot.addView(
                name,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        addSpace(registerRoot, 18);

        // ---------------- PHONE ----------------

        EditText phone = registerInput(
                "+880   ফোন নম্বর"
        );

        phone.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        registerRoot.addView(
                phone,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        addSpace(registerRoot, 18);

        // ---------------- PASSWORD ----------------

        EditText password = registerInput(
                "🔒   ৬ ডিজিট পাসওয়ার্ড"
        );

        password.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        registerRoot.addView(
                password,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        addSpace(registerRoot, 18);

        // ---------------- CONFIRM PASSWORD ----------------

        EditText confirmPassword = registerInput(
                "🔒   ৬ ডিজিট পাসওয়ার্ড নিশ্চিত করুন"
        );

        confirmPassword.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        registerRoot.addView(
                confirmPassword,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        addSpace(registerRoot, 25);

        // ---------------- NEXT ----------------

        TextView next = text(
                "পরবর্তী",
                22,
                BLUE
        );

        next.setGravity(Gravity.CENTER);

        next.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        next.setBackground(
                bg(WHITE, 12)
        );

        registerRoot.addView(
                next,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        next.setOnClickListener(v -> {

            String n = name.getText()
                    .toString()
                    .trim();

            String p = phone.getText()
                    .toString()
                    .trim();

            String pass = password.getText()
                    .toString()
                    .trim();

            String confirm = confirmPassword
                    .getText()
                    .toString()
                    .trim();

            if (n.isEmpty()) {
                name.setError("পূর্ণ নাম দিন");
                name.requestFocus();
                return;
            }

            if (p.isEmpty()) {
                phone.setError("ফোন নম্বর দিন");
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
                    .putString("name", n)
                    .putString("phone", p)
                    .putString("password", pass)
                    .putString(
                            "agent_code",
                            agentCode.getText()
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

        addSpace(registerRoot, 20);

        // ---------------- LOGIN ----------------

        TextView login = text(
                "অ্যাকাউন্ট আছে?  লগইন",
                18,
                WHITE
        );

        login.setGravity(Gravity.CENTER);

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

    // ====================================================
    // REGISTER INPUT
    // ====================================================

    private EditText registerInput(String hint) {

        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(19);

        e.setSingleLine(true);

        e.setTextColor(DARK);
        e.setHintTextColor(
                Color.rgb(145, 155, 165)
        );

        e.setPadding(
                dp(20),
                0,
                dp(15),
                0
        );

        e.setBackground(
                bg(WHITE, 12)
        );

        return e;
    }

    // ====================================================
    // FORGOT PASSWORD
    // ====================================================

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

        phone.setHint("রেজিস্টার করা ফোন");
        phone.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        box.addView(phone);

        EditText newPassword =
                new EditText(this);

        newPassword.setHint(
                "নতুন পাসওয়ার্ড"
        );

        newPassword.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        box.addView(newPassword);

        new android.app.AlertDialog.Builder(this)
                .setTitle("পাসওয়ার্ড পরিবর্তন")
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
                                    newPassword.getText()
                                            .toString()
                                            .trim();

                            if (savedPhone.equals(
                                    enteredPhone)
                                    && !newPass.isEmpty()) {

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
                                        "ফোন নম্বর সঠিক নয়",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        })
                .show();
    }

    // ====================================================
    // BOTTOM BUTTON
    // ====================================================

    private void addBottomButton(
            LinearLayout parent,
            String title,
            boolean active) {

        TextView b = text(
                title,
                13,
                active ? BLUE : Color.GRAY
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
                    title.replace("\n", " "),
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    // ====================================================
    // SPACE
    // ====================================================

    private void addSpace(
            LinearLayout parent,
            int height) {

        Space s = new Space(this);

        parent.addView(
                s,
                new LinearLayout.LayoutParams(
                        1,
                        dp(height)
                )
        );
    }

    // ====================================================
    // BACK BUTTON
    // ====================================================

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
