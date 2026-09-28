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
         * প্রথমবার Login না করলে Login Screen
         *
         * আগে Login করা থাকলে Password Unlock Screen
         */
        if (pref.getBoolean("logged_in", false)
                && !pref.getString("password_hash", "").isEmpty()) {

            showUnlockScreen();

        } else {

            showLogin();
        }
    }

    // ----------------------------------------------------
    // BASIC HELPERS
    // ----------------------------------------------------

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

            /*
             * যদি আগে Registration করা থাকে,
             * তাহলে সেই password check হবে।
             *
             * প্রথমবার account না থাকলে
             * এই login-কে demo account হিসেবে save করা হবে।
             */

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

            showHome();
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
    // PASSWORD UNLOCK SCREEN
    // ====================================================

    private void showUnlockScreen() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout unlock =
                new LinearLayout(this);

        unlock.setOrientation(
                LinearLayout.VERTICAL
        );

        unlock.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        unlock.setPadding(
                dp(30),
                dp(80),
                dp(30),
                dp(30)
        );

        unlock.setBackgroundColor(BLUE);

        setContentView(unlock);

        TextView logo = text(
                "Quick Pay",
                34,
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

        unlock.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(300),
                        dp(115)
                )
        );

        addSpace(unlock, 35);

        TextView welcome = text(
                "স্বাগতম আবার",
                25,
                WHITE
        );

        welcome.setGravity(Gravity.CENTER);

        welcome.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        unlock.addView(
                welcome,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        addSpace(unlock, 10);

        String phone =
                pref.getString("phone", "");

        TextView phoneText = text(
                maskPhone(phone),
                17,
                Color.WHITE
        );

        phoneText.setGravity(Gravity.CENTER);

        unlock.addView(
                phoneText,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(35)
                )
        );

        addSpace(unlock, 25);

        EditText unlockPassword =
                new EditText(this);

        unlockPassword.setHint(
                "পাসওয়ার্ড লিখুন"
        );

        unlockPassword.setTextSize(19);

        unlockPassword.setSingleLine(true);

        unlockPassword.setGravity(
                Gravity.CENTER
        );

        unlockPassword.setTextColor(DARK);
        unlockPassword.setHintTextColor(
                Color.GRAY
        );

        unlockPassword.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        unlockPassword.setBackground(
                bg(WHITE, 12)
        );

        unlock.addView(
                unlockPassword,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        addSpace(unlock, 20);

        TextView enter = text(
                "প্রবেশ করুন",
                20,
                BLUE
        );

        enter.setGravity(Gravity.CENTER);

        enter.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        enter.setBackground(
                bg(WHITE, 12)
        );

        unlock.addView(
                enter,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(62)
                )
        );

        enter.setOnClickListener(v -> {

            String entered =
                    unlockPassword.getText()
                            .toString()
                            .trim();

            String saved =
                    pref.getString("password", "");

            if (entered.isEmpty()) {

                unlockPassword.setError(
                        "পাসওয়ার্ড দিন"
                );

                return;
            }

            if (entered.equals(saved)) {

                pref.edit()
                        .putBoolean("logged_in", true)
                        .apply();

                showHome();

            } else {

                unlockPassword.setError(
                        "ভুল পাসওয়ার্ড"
                );

                Toast.makeText(
                        this,
                        "পাসওয়ার্ড সঠিক নয়",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        addSpace(unlock, 22);

        TextView different =
                text(
                        "অন্য অ্যাকাউন্ট দিয়ে লগইন",
                        17,
                        WHITE
                );

        different.setGravity(Gravity.CENTER);

        unlock.addView(
                different,
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        dp(45)
                )
        );

        different.setOnClickListener(v -> {

            pref.edit()
                    .putBoolean("logged_in", false)
                    .apply();

            showLogin();
        });
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

        TextView userName = text(
                "Rosy",
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
    // REGISTER
    // ====================================================

    private void showRegister() {

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

        phone.setHint("ফোন নম্বর");
        phone.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        box.addView(phone);

        EditText password =
                new EditText(this);

        password.setHint(
                "৬ ডিজিট পাসওয়ার্ড"
        );

        password.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        box.addView(password);

        new android.app.AlertDialog.Builder(this)
                .setTitle("Quick Pay Registration")
                .setView(box)
                .setNegativeButton(
                        "বাতিল",
                        null
                )
                .setPositiveButton(
                        "রেজিস্টার",
                        (dialog, which) -> {

                            String p =
                                    phone.getText()
                                            .toString()
                                            .trim();

                            String pass =
                                    password.getText()
                                            .toString()
                                            .trim();

                            if (p.isEmpty()
                                    || pass.isEmpty()) {

                                Toast.makeText(
                                        this,
                                        "সব তথ্য দিন",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            pref.edit()
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
                                            false
                                    )
                                    .apply();

                            Toast.makeText(
                                    this,
                                    "Registration সফল। এখন Login করুন।",
                                    Toast.LENGTH_LONG
                            ).show();
                        })
                .show();
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
    // PHONE MASK
    // ====================================================

    private String maskPhone(String phone) {

        if (phone == null || phone.length() < 5) {
            return phone;
        }

        int visible = Math.min(
                4,
                phone.length()
        );

        String last =
                phone.substring(
                        phone.length() - visible
                );

        return "******" + last;
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
