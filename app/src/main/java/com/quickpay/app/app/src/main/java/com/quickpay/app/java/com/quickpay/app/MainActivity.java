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
import android.view.ViewGroup;
import android.widget.*;

public class MainActivity extends Activity {

    private static final int BLUE = Color.rgb(8, 96, 190);
    private static final int GREEN = Color.rgb(0, 92, 68);
    private static final int YELLOW = Color.rgb(255, 190, 25);
    private static final int DARK = Color.rgb(35, 35, 35);

    private SharedPreferences pref;

    private int dp(float n) {
        return (int) (n * getResources()
                .getDisplayMetrics().density + 0.5f);
    }

    private TextView tv(String text, float size, int color) {
        TextView t = new TextView(this);
        t.setText(text);
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

    private GradientDrawable outline(
            int color,
            int stroke,
            float radius) {

        GradientDrawable d = bg(color, radius);
        d.setStroke(dp(1), stroke);
        return d;
    }

    private void space(LinearLayout parent, int height) {
        parent.addView(
                new Space(this),
                new LinearLayout.LayoutParams(
                        1,
                        dp(height)));
    }

    private TextView button(
            String text,
            int background,
            int textColor) {

        TextView b = tv(text, 18, textColor);
        b.setGravity(Gravity.CENTER);
        b.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);
        b.setBackground(
                bg(background, 12));

        return b;
    }

    private EditText input(
            String hint,
            boolean password) {

        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(17);
        e.setSingleLine(true);

        e.setPadding(
                dp(18),
                0,
                dp(18),
                0);

        e.setTextColor(DARK);
        e.setHintTextColor(Color.GRAY);
        e.setBackground(
                bg(Color.WHITE, 12));

        if (password) {
            e.setInputType(
                    InputType.TYPE_CLASS_NUMBER |
                    InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        } else {
            e.setInputType(
                    InputType.TYPE_CLASS_PHONE);
        }

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
                        15));

        return e;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences(
                "quick_pay",
                Context.MODE_PRIVATE);

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

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL);

        root.setGravity(
                Gravity.CENTER_HORIZONTAL);

        root.setPadding(
                dp(25),
                dp(18),
                dp(25),
                dp(18));

        root.setBackgroundColor(BLUE);

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.addView(root);

        setContentView(scroll);

        TextView language =
                tv(
                        "বাংলা     EN",
                        16,
                        Color.WHITE);

        language.setGravity(Gravity.CENTER);
        language.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        language.setBackground(
                bg(
                        Color.rgb(55, 130, 205),
                        40));

        LinearLayout.LayoutParams langParams =
                new LinearLayout.LayoutParams(
                        dp(175),
                        dp(50));

        langParams.gravity = Gravity.RIGHT;

        root.addView(
                language,
                langParams);

        space(root, 45);

        TextView logo =
                tv(
                        "Quick Pay",
                        32,
                        BLUE);

        logo.setGravity(Gravity.CENTER);

        logo.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        logo.setBackground(
                bg(Color.WHITE, 18));

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(300),
                        dp(95)));

        TextView sub =
                tv(
                        "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম",
                        16,
                        Color.WHITE);

        sub.setGravity(Gravity.CENTER);

        root.addView(
                sub,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)));

        space(root, 15);

        EditText phone =
                input("ফোন", false);

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)));

        space(root, 14);

        LinearLayout passwordBox =
                new LinearLayout(this);

        passwordBox.setGravity(
                Gravity.CENTER_VERTICAL);

        passwordBox.setPadding(
                dp(5),
                0,
                dp(5),
                0);

        passwordBox.setBackground(
                bg(Color.WHITE, 12));

        EditText password =
                input(
                        "৬ ডিজিট পাসওয়ার্ড",
                        true);

        password.setBackgroundColor(
                Color.TRANSPARENT);

        passwordBox.addView(
                password,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1));

        TextView eye =
                tv("◉", 23, BLUE);

        eye.setGravity(Gravity.CENTER);

        passwordBox.addView(
                eye,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(62)));

        root.addView(
                passwordBox,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)));

        eye.setOnClickListener(v -> {

            boolean hidden =
                    (password.getInputType()
                            & InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                            != 0;

            if (hidden) {
                password.setInputType(
                        InputType.TYPE_CLASS_NUMBER);
            } else {
                password.setInputType(
                        InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            }

            password.setSelection(
                    password.length());
        });

        space(root, 20);

        TextView login =
                button(
                        "লগইন",
                        Color.WHITE,
                        BLUE);

        root.addView(
                login,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)));

        login.setOnClickListener(v -> {

            String phoneText =
                    phone.getText()
                            .toString()
                            .trim();

            String passText =
                    password.getText()
                            .toString()
                            .trim();

            if (phoneText.isEmpty()) {
                phone.setError(
                        "ফোন নম্বর দিন");
                return;
            }

            if (passText.isEmpty()) {
                password.setError(
                        "পাসওয়ার্ড দিন");
                return;
            }

            String savedPhone =
                    pref.getString(
                            "phone",
                            "");

            String savedPassword =
                    pref.getString(
                            "password",
                            "");

            if (!savedPhone.isEmpty()) {

                if (!savedPhone.equals(phoneText)
                        ||
                        !savedPassword.equals(passText)) {

                    Toast.makeText(
                            this,
                            "ফোন নম্বর বা পাসওয়ার্ড সঠিক নয়",
                            Toast.LENGTH_SHORT).show();

                    return;
                }
            }

            pref.edit()
                    .putString(
                            "phone",
                            phoneText)
                    .putString(
                            "password",
                            passText)
                    .putBoolean(
                            "logged_in",
                            true)
                    .apply();

            if (pref.getString(
                    "pin",
                    "").length() == 8) {

                showHome();

            } else {
                showPinSetup();
            }
        });

        TextView forgot =
                tv(
                        "পাসওয়ার্ড ভুলে গেছেন?",
                        16,
                        Color.WHITE);

        forgot.setGravity(Gravity.CENTER);

        root.addView(
                forgot,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(48)));

        forgot.setOnClickListener(
                v -> showForgotPassword());

        TextView register =
                tv(
                        "অ্যাকাউন্ট নেই?  রেজিস্টার করুন",
                        17,
                        Color.WHITE);

        register.setGravity(Gravity.CENTER);

        register.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        root.addView(
                register,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)));

        register.setOnClickListener(
                v -> showRegister());
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
                LinearLayout.VERTICAL);

        screen.setGravity(
                Gravity.CENTER);

        screen.setPadding(
                dp(20),
                dp(15),
                dp(20),
                dp(15));

        screen.setBackgroundColor(BLUE);

        setContentView(screen);

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL);

        card.setGravity(
                Gravity.CENTER_HORIZONTAL);

        card.setPadding(
                dp(25),
                dp(28),
                dp(25),
                dp(22));

        card.setBackground(
                bg(
                        Color.rgb(250, 252, 250),
                        28));

        screen.addView(
                card,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(500)));

        TextView lock =
                tv("🔒", 50, BLUE);

        lock.setGravity(Gravity.CENTER);

        lock.setBackground(
                bg(
                        Color.rgb(232, 240, 250),
                        70));

        card.addView(
                lock,
                new LinearLayout.LayoutParams(
                        dp(105),
                        dp(105)));

        space(card, 18);

        TextView title =
                tv(
                        "পিন সেট করুন",
                        26,
                        BLUE);

        title.setGravity(Gravity.CENTER);

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        card.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)));

        TextView sub =
                tv(
                        "অ্যাপে ঢোকার জন্য ৮ ডিজিটের পিন দিন",
                        17,
                        Color.DKGRAY);

        sub.setGravity(Gravity.CENTER);

        card.addView(
                sub,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)));

        space(card, 8);

        EditText pin =
                pinInput("৮ ডিজিট PIN");

        EditText confirm =
                pinInput("PIN আবার দিন");

        card.addView(
                pin,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)));

        space(card, 10);

        card.addView(
                confirm,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)));

        space(card, 16);

        TextView save =
                button(
                        "পিন সেট করুন  ✓",
                        Color.TRANSPARENT,
                        BLUE);

        save.setBackground(
                outline(
                        Color.TRANSPARENT,
                        Color.rgb(80, 145, 205),
                        16));

        card.addView(
                save,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)));

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
                        "৮ ডিজিটের PIN দিন");
                return;
            }

            if (!a.equals(b)) {
                confirm.setError(
                        "দুইটি PIN একই নয়");
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
                LinearLayout.VERTICAL);

        screen.setGravity(
                Gravity.CENTER);

        screen.setPadding(
                dp(20),
                dp(15),
                dp(20),
                dp(15));

        screen.setBackgroundColor(BLUE);

        setContentView(screen);

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL);

        card.setGravity(
                Gravity.CENTER_HORIZONTAL);

        card.setPadding(
                dp(25),
                dp(28),
                dp(25),
                dp(22));

        card.setBackground(
                bg(
                        Color.rgb(250, 252, 250),
                        28));

        screen.addView(
                card,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(460)));

        TextView lock =
                tv("🔒", 50, BLUE);

        lock.setGravity(Gravity.CENTER);

        lock.setBackground(
                bg(
                        Color.rgb(232, 240, 250),
                        70));

        card.addView(
                lock,
                new LinearLayout.LayoutParams(
                        dp(105),
                        dp(105)));

        space(card, 18);

        TextView title =
                tv(
                        "পিন যাচাই করুন",
                        26,
                        BLUE);

        title.setGravity(Gravity.CENTER);

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        card.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)));

        TextView sub =
                tv(
                        "আপনার ৮ ডিজিটের পিন দিন",
                        17,
                        Color.DKGRAY);

        sub.setGravity(Gravity.CENTER);

        card.addView(
                sub,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)));

        space(card, 10);

        EditText pin =
                pinInput("৮ ডিজিটের পিন");

        card.addView(
                pin,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)));

        space(card, 18);

        TextView verify =
                button(
                        "যাচাই করুন ✓",
                        BLUE,
                        Color.WHITE);

        card.addView(
                verify,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)));

        verify.setOnClickListener(v -> {

            String saved =
                    pref.getString(
                            "pin",
                            "");

            String entered =
                    pin.getText()
                            .toString()
                            .trim();

            if (entered.length() != 8) {

                pin.setError(
                        "৮ ডিজিটের পিন দিন");

                return;
            }

            if (entered.equals(saved)) {

                showHome();

            } else {

                Toast.makeText(
                        this,
                        "ভুল পিন",
                        Toast.LENGTH_SHORT).show();
            }
        });

        TextView login =
                tv(
                        "পিন ভুলে গেছেন? লগইন করুন",
                        15,
                        BLUE);

        login.setGravity(Gravity.CENTER);

        card.addView(
                login,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)));

        login.setOnClickListener(v -> {

            pref.edit()
                    .putBoolean(
                            "logged_in",
                            false)
                    .apply();

            showLogin();
        });
    }

    // =========================================================
    // HOME
    // =========================================================

    private void showHome() {

        getWindow().setStatusBarColor(GREEN);
        getWindow().setNavigationBarColor(GREEN);

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL);

        root.setBackgroundColor(
                Color.WHITE);

        scroll.addView(root);

        setContentView(scroll);

        // HEADER
        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.VERTICAL);

        header.setPadding(
                dp(12),
                dp(10),
                dp(12),
                dp(8));

        header.setBackgroundColor(GREEN);

        LinearLayout top =
                new LinearLayout(this);

        top.setGravity(
                Gravity.CENTER_VERTICAL);

        TextView logo =
                tv(
                        " QUICK PAY ",
                        18,
                        GREEN);

        logo.setGravity(Gravity.CENTER);

        logo.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        logo.setBackground(
                bg(Color.WHITE, 15));

        top.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(125),
                        dp(44)));

        Space topSpace =
                new Space(this);

        top.addView(
                topSpace,
                new LinearLayout.LayoutParams(
                        0,
                        1,
                        1));

        TextView language =
                tv(
                        "EN",
                        14,
                        Color.WHITE);

        language.setGravity(Gravity.CENTER);

        top.addView(
                language,
                new LinearLayout.LayoutParams(
                        dp(35),
                        dp(44)));

        TextView notification =
                tv(
                        "🔔",
                        20,
                        Color.WHITE);

        notification.setGravity(
                Gravity.CENTER);

        top.addView(
                notification,
                new LinearLayout.LayoutParams(
                        dp(40),
                        dp(44)));

        TextView logout =
                tv(
                        "↪",
                        23,
                        Color.WHITE);

        logout.setGravity(
                Gravity.CENTER);

        top.addView(
                logout,
                new LinearLayout.LayoutParams(
                        dp(40),
                        dp(44)));

        logout.setOnClickListener(v -> {

            pref.edit()
                    .putBoolean(
                            "logged_in",
                            false)
                    .apply();

            showLogin();
        });

        header.addView(top);

        String name =
                pref.getString(
                        "name",
                        "Rosy");

        TextView greeting =
                tv(
                        "হ্যালো, " + name + " 👋",
                        18,
                        Color.WHITE);

        greeting.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        greeting.setPadding(
                dp(5),
                dp(8),
                dp(5),
                dp(3));

        header.addView(greeting);

        TextView balance =
                tv(
                        "৳ 0.00",
                        24,
                        DARK);

        balance.setGravity(
                Gravity.CENTER);

        balance.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        balance.setBackground(
                bg(YELLOW, 25));

        LinearLayout.LayoutParams balanceParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(48));

        balanceParams.setMargins(
                dp(5),
                dp(5),
                dp(5),
                dp(5));

        header.addView(
                balance,
                balanceParams);

        LinearLayout balanceNames =
                new LinearLayout(this);

        TextView mainBalance =
                tv(
                        "Main Balance",
                        13,
                        Color.WHITE);

        mainBalance.setGravity(
                Gravity.CENTER);

        TextView driveBalance =
                tv(
                        "Drive Balance",
                        13,
                        Color.WHITE);

        driveBalance.setGravity(
                Gravity.CENTER);

        balanceNames.addView(
                mainBalance,
                new LinearLayout.LayoutParams(
                        0,
                        dp(32),
                        1));

        balanceNames.addView(
                driveBalance,
                new LinearLayout.LayoutParams(
                        0,
                        dp(32),
                        1));

        header.addView(balanceNames);

        root.addView(header);

        // NOTICE
        TextView notice =
                tv(
                        "📢 Quick Pay-এ স্বাগতম • নিরাপদে লেনদেন করুন",
                        13,
                        DARK);

        notice.setPadding(
                dp(12),
                dp(10),
                dp(12),
                dp(10));

        root.addView(notice);

        // SERVICE GRID
        LinearLayout grid =
                new LinearLayout(this);

        grid.setOrientation(
                LinearLayout.VERTICAL);

        String[][] services = {

                {"💰", "Wallet\nDeposit"},
                {"📱", "Mobile\nBanking"},
                {"🏦", "Bank\nTransfer"},
                {"📲", "Mobile\nRecharge"},

                {"💬", "Group\nChat"},
                {"🎁", "Invite\nBonus"},
                {"🧾", "Bill\nPay"},
                {"⭐", "Special\nOffer"},

                {"☎️", "Customer\nCare"},
                {"⭐", "Customer\nReview"},
                {"▶️", "Video\nTutorial"},
                {"📞", "Contact\nUs"}
        };

        for (int rowNumber = 0;
             rowNumber < 3;
             rowNumber++) {

            LinearLayout row =
                    new LinearLayout(this);

            row.setGravity(
                    Gravity.CENTER);

            for (int col = 0;
                 col < 4;
                 col++) {

                int index =
                        rowNumber * 4 + col;

                LinearLayout box =
                        new LinearLayout(this);

                box.setOrientation(
                        LinearLayout.VERTICAL);

                box.setGravity(
                        Gravity.CENTER);

                TextView icon =
                        tv(
                                services[index][0],
                                27,
                                DARK);

                icon.setGravity(
                        Gravity.CENTER);

                TextView serviceName =
                        tv(
                                services[index][1],
                                11,
                                DARK);

                serviceName.setGravity(
                        Gravity.CENTER);

                serviceName.setTypeface(
                        Typeface.DEFAULT,
                        Typeface.BOLD);

                box.addView(
                        icon,
                        new LinearLayout.LayoutParams(
                                -1,
                                dp(38)));

                box.addView(
                        serviceName,
                        new LinearLayout.LayoutParams(
                                -1,
                                dp(38)));

                row.addView(
                        box,
                        new LinearLayout.LayoutParams(
                                0,
                                dp(76),
                                1));

                if (index == 0) {

                    box.setOnClickListener(
                            v -> showWalletDeposit());
                }

                if (index == 1) {

                    box.setOnClickListener(
                            v -> showMobileBanking());
                }
            }

            grid.addView(row);
        }

        root.addView(grid);

        // BONUS
        LinearLayout bonus =
                new LinearLayout(this);

        bonus.setOrientation(
                LinearLayout.VERTICAL);

        bonus.setPadding(
                dp(8),
                dp(6),
                dp(8),
                dp(6));

        bonus.setBackgroundColor(GREEN);

        TextView bonusTitle =
                tv(
                        "ডিপোজিট বোনাস",
                        17,
                        Color.WHITE);

        bonusTitle.setGravity(
                Gravity.CENTER);

        bonusTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        bonus.addView(
                bonusTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(30)));

        LinearLayout amounts =
                new LinearLayout(this);

        String[] amountList = {
                "৳500",
                "৳1000",
                "৳2000"
        };

        for (String amount :
                amountList) {

            TextView amountBox =
                    tv(
                            amount,
                            16,
                            DARK);

            amountBox.setGravity(
                    Gravity.CENTER);

            amountBox.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD);

            amountBox.setBackground(
                    bg(Color.WHITE, 8));

            LinearLayout.LayoutParams amountParams =
                    new LinearLayout.LayoutParams(
                            0,
                            dp(40),
                            1);

            amountParams.setMargins(
                    dp(3),
                    dp(3),
                    dp(3),
                    dp(3));

            amounts.addView(
                    amountBox,
                    amountParams);
        }

        bonus.addView(amounts);

        root.addView(
                bonus,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(80)));

        // BOTTOM NAV
        LinearLayout nav =
                new LinearLayout(this);

        nav.setGravity(
                Gravity.CENTER);

        nav.setBackgroundColor(GREEN);

        String[] navItems = {
                "⌂\nHome",
                "৳\nলেনদেন",
                "👤\nপ্রোফাইল"
        };

        for (String item :
                navItems) {

            TextView navItem =
                    tv(
                            item,
                            13,
                            Color.WHITE);

            navItem.setGravity(
                    Gravity.CENTER);

            nav.addView(
                    navItem,
                    new LinearLayout.LayoutParams(
                            0,
                            dp(58),
                            1));
        }

        root.addView(nav);
    }

    // =========================================================
    // WALLET DEPOSIT
    // =========================================================

    private void showWalletDeposit() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL);

        root.setBackgroundColor(
                Color.WHITE);

        setContentView(root);

        // BLUE HEADER
        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL);

        header.setPadding(
                dp(8),
                0,
                dp(8),
                0);

        header.setBackgroundColor(BLUE);

        TextView back =
                tv(
                        "‹",
                        38,
                        Color.WHITE);

        back.setGravity(
                Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(62)));

        back.setOnClickListener(
                v -> showHome());

        TextView title =
                tv(
                        "ওয়ালেট ডিপোজিট",
                        21,
                        Color.WHITE);

        title.setGravity(
                Gravity.CENTER_VERTICAL);

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        header.addView(
                title,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1));

        root.addView(header);

        // TEXT
        TextView text1 =
                tv(
                        "আপনার Quick Pay ওয়ালেটে টাকা জমা করুন।",
                        20,
                        DARK);

        text1.setTypeface(
                Typeface.DEFAULT,
                Typeface.NORMAL);

        text1.setPadding(
                dp(22),
                dp(48),
                dp(22),
                dp(12));

        root.addView(text1);

        TextView text2 =
                tv(
                        "ডিপোজিট করার জন্য মোবাইল ব্যাংকিং ব্যবহার করুন।",
                        17,
                        DARK);

        text2.setPadding(
                dp(22),
                dp(20),
                dp(22),
                dp(30));

        root.addView(text2);

        TextView mobileBanking =
                button(
                        "মোবাইল ব্যাংকিং",
                        BLUE,
                        Color.WHITE);

        LinearLayout.LayoutParams mbParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60));

        mbParams.setMargins(
                dp(20),
                dp(5),
                dp(20),
                dp(10));

        root.addView(
                mobileBanking,
                mbParams);

        mobileBanking.setOnClickListener(
                v -> showMobileBanking());
    }

    // =========================================================
    // MOBILE BANKING
    // =========================================================

    private void showMobileBanking() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL);

        root.setBackgroundColor(
                Color.rgb(248, 249, 251));

        scroll.addView(root);

        setContentView(scroll);

        // HEADER
        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL);

        header.setPadding(
                dp(8),
                0,
                dp(8),
                0);

        header.setBackgroundColor(BLUE);

        TextView back =
                tv(
                        "‹",
                        38,
                        Color.WHITE);

        back.setGravity(
                Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(62)));

        back.setOnClickListener(
                v -> showWalletDeposit());

        TextView title =
                tv(
                        "মোবাইল ব্যাংকিং",
                        21,
                        Color.WHITE);

        title.setGravity(
                Gravity.CENTER_VERTICAL);

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        header.addView(
                title,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1));

        root.addView(header);

        // CLEAR NOTICE
        TextView notice =
                tv(
                        "●  মোবাইল ব্যাংকিং এর মাধ্যমে সহজেই ডিপোজিট করুন",
                        16,
                        DARK);

        notice.setPadding(
                dp(20),
                dp(22),
                dp(20),
                dp(18));

        root.addView(notice);

        // ALL PERSONAL NUMBERS
        addProviderRow(
                root,
                "bKash",
                "বিকাশ পার্সোনাল নাম্বার",
                "01XXXXXXXXX");

        addProviderRow(
                root,
                "Nagad",
                "নগদ পার্সোনাল নাম্বার",
                "01XXXXXXXXX");

        addProviderRow(
                root,
                "Rocket",
                "রকেট পার্সোনাল নাম্বার",
                "01XXXXXXXXX");

        addProviderRow(
                root,
                "Upay",
                "উপায় পার্সোনাল নাম্বার",
                "01XXXXXXXXX");

        // AUTO DEPOSIT
        TextView autoDeposit =
                button(
                        "অটো ডিপোজিট  →",
                        BLUE,
                        Color.WHITE);

        LinearLayout.LayoutParams autoParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58));

        autoParams.setMargins(
                dp(18),
                dp(12),
                dp(18),
                dp(8));

        root.addView(
                autoDeposit,
                autoParams);

        autoDeposit.setOnClickListener(
                v -> showMoneyForm("সেন্ড মানি"));
    }

    // =========================================================
    // PROVIDER ROW
    // =========================================================

    private void addProviderRow(
            LinearLayout parent,
            String name,
            String subtitle,
            String number) {

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.VERTICAL);

        row.setPadding(
                dp(18),
                dp(10),
                dp(18),
                dp(10));

        row.setBackgroundColor(
                Color.WHITE);

        TextView provider =
                tv(
                        name,
                        21,
                        BLUE);

        provider.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        row.addView(
                provider,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(32)));

        TextView numberText =
                tv(
                        subtitle,
                        14,
                        Color.DKGRAY);

        row.addView(
                numberText,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(26)));

        TextView numberView =
                tv(
                        number,
                        17,
                        Color.BLACK);

        numberView.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        row.addView(
                numberView,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(30)));

        LinearLayout actions =
                new LinearLayout(this);

        actions.setGravity(
                Gravity.CENTER_VERTICAL);

        TextView copy =
                tv(
                        "কপি",
                        14,
                        BLUE);

        copy.setGravity(
                Gravity.CENTER);

        copy.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        copy.setBackground(
                outline(
                        Color.TRANSPARENT,
                        BLUE,
                        10));

        actions.addView(
                copy,
                new LinearLayout.LayoutParams(
                        dp(70),
                        dp(38)));

        TextView send =
                tv(
                        "সেন্ড মানি",
                        14,
                        BLUE);

        send.setGravity(
                Gravity.CENTER);

        send.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        actions.addView(
                send,
                new LinearLayout.LayoutParams(
                        0,
                        dp(38),
                        1));

        TextView cash =
                tv(
                        "ক্যাশ আউট",
                        14,
                        GREEN);

        cash.setGravity(
                Gravity.CENTER);

        cash.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        actions.addView(
                cash,
                new LinearLayout.LayoutParams(
                        0,
                        dp(38),
                        1));

        row.addView(
                actions,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(42)));

        copy.setOnClickListener(v -> {

            android.content.ClipboardManager clipboard =
                    (android.content.ClipboardManager)
                            getSystemService(
                                    Context.CLIPBOARD_SERVICE);

            clipboard.setPrimaryClip(
                    android.content.ClipData.newPlainText(
                            "Mobile Banking Number",
                            number));

            Toast.makeText(
                    this,
                    "নম্বর কপি হয়েছে",
                    Toast.LENGTH_SHORT).show();
        });

        send.setOnClickListener(
                v -> showMoneyForm("সেন্ড মানি"));

        cash.setOnClickListener(
                v -> showMoneyForm("ক্যাশ আউট"));

        parent.addView(
                row,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(130)));

        View divider =
                new View(this);

        divider.setBackgroundColor(
                Color.rgb(225, 228, 232));

        parent.addView(
                divider,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(1)));
    }

    // =========================================================
    // SEND MONEY / CASH OUT
    // =========================================================

    private void showMoneyForm(
            String type) {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(
                Color.WHITE);

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL);

        root.setBackgroundColor(
                Color.rgb(248, 249, 251));

        setContentView(root);

        // HEADER
        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL);

        header.setPadding(
                dp(8),
                0,
                dp(8),
                0);

        header.setBackgroundColor(BLUE);

        TextView back =
                tv(
                        "‹",
                        38,
                        Color.WHITE);

        back.setGravity(
                Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(52),
                        dp(62)));

        back.setOnClickListener(
                v -> showMobileBanking());

        TextView title =
                tv(
                        type,
                        21,
                        Color.WHITE);

        title.setGravity(
                Gravity.CENTER_VERTICAL);

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        header.addView(
                title,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1));

        root.addView(header);

        // FORM CARD
        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL);

        card.setPadding(
                dp(18),
                dp(20),
                dp(18),
                dp(20));

        card.setBackground(
                bg(Color.WHITE, 18));

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2);

        cardParams.setMargins(
                dp(12),
                dp(18),
                dp(12),
                0);

        root.addView(
                card,
                cardParams);

        TextView providerInfo =
                tv(
                        "bKash / Nagad / Rocket / Upay",
                        16,
                        BLUE);

        providerInfo.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        card.addView(
                providerInfo,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(34)));

        TextView minimum =
                tv(
                        "সর্বনিম্ন লেনদেন: ৳ ৫০০",
                        15,
                        Color.DKGRAY);

        card.addView(
                minimum,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(30)));

        space(card, 10);

        EditText number =
                input(
                        type.equals("ক্যাশ আউট")
                                ? "আপনার মোবাইল নম্বর"
                                : "যে নম্বরে পাঠাবেন",
                        false);

        card.addView(
                number,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)));

        space(card, 12);

        EditText amount =
                input(
                        "টাকার পরিমাণ (সর্বনিম্ন ৳৫০০)",
                        false);

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL);

        card.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)));

        space(card, 12);

        EditText reference =
                input(
                        "রেফারেন্স (ঐচ্ছিক)",
                        false);

        card.addView(
                reference,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)));

        space(card, 16);

        TextView confirm =
                button(
                        type + "  →",
                        BLUE,
                        Color.WHITE);

        card.addView(
                confirm,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)));

        confirm.setOnClickListener(v -> {

            String phone =
                    number.getText()
                            .toString()
                            .trim();

            String amountText =
                    amount.getText()
                            .toString()
                            .trim();

            if (phone.isEmpty()) {

                number.setError(
                        "মোবাইল নম্বর দিন");

                return;
            }

            if (amountText.isEmpty()) {

                amount.setError(
                        "টাকার পরিমাণ দিন");

                return;
            }

            try {

                double value =
                        Double.parseDouble(
                                amountText);

                // MINIMUM 500
                if (value < 500) {

                    amount.setError(
                            "সর্বনিম্ন ৫০০ টাকা");

                    return;
                }

                new AlertDialog.Builder(this)

                        .setTitle(
                                type +
                                " নিশ্চিত করুন")

                        .setMessage(
                                "নম্বর: " +
                                phone +
                                "\n\n" +
                                "পরিমাণ: ৳ " +
                                amountText +
                                "\n\n" +
                                "লেনদেনটি নিশ্চিত করতে OK চাপুন।")

                        .setNegativeButton(
                                "বাতিল",
                                null)

                        .setPositiveButton(
                                "OK",
                                (dialog, which) -> {

                                    Toast.makeText(
                                            this,
                                            "রিকোয়েস্ট গ্রহণ করা হয়েছে।\nAPI পরে সংযুক্ত করা যাবে।",
                                            Toast.LENGTH_LONG)
                                            .show();
                                })

                        .show();

            } catch (Exception e) {

                amount.setError(
                        "সঠিক টাকার পরিমাণ দিন");
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
                LinearLayout.VERTICAL);

        root.setPadding(
                dp(22),
                dp(18),
                dp(22),
                dp(20));

        root.setGravity(
                Gravity.CENTER_HORIZONTAL);

        root.setBackgroundColor(BLUE);

        ScrollView scroll =
                new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.addView(root);

        setContentView(scroll);

        TextView logo =
                tv(
                        "Quick Pay",
                        29,
                        BLUE);

        logo.setGravity(
                Gravity.CENTER);

        logo.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD);

        logo.setBackground(
                bg(Color.WHITE, 18));

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(260),
                        dp(75)));

        space(root, 12);

        EditText country =
                input(
                        "বাংলাদেশ  🇧🇩",
                        false);

        root.addView(
                country,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)));

        space(root, 9);

        EditText agent =
                input(
                        "রিসেলার এজেন্ট কোড",
                        false);

        root.addView(
                agent,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)));

        space(root, 9);

        EditText name =
                input(
                        "পূর্ণ নাম",
                        false);

        root.addView(
                name,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)));

        space(root, 9);

        EditText phone =
                input(
                        "+880 ফোন নম্বর",
                        false);

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)));

        space(root, 9);

        EditText password =
                input(
                        "৬ ডিজিট পাসওয়ার্ড",
                        true);

        root.addView(
                password,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)));

        space(root, 9);

        EditText confirmPassword =
                input(
                        "৬ ডিজিট পাসওয়ার্ড নিশ্চিত করুন",
                        true);

        root.addView(
                confirmPassword,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)));

        space(root, 16);

        TextView next =
                button(
                        "পরবর্তী",
                        Color.WHITE,
                        BLUE);

        root.addView(
                next,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)));

        next.setOnClickListener(v -> {

            String fullName =
                    name.getText()
                            .toString()
                            .trim();

            String phoneText =
                    phone.getText()
                            .toString()
                            .trim();

            String passText =
                    password.getText()
                            .toString()
                            .trim();

            String confirmText =
                    confirmPassword.getText()
                            .toString()
                            .trim();

            if (fullName.isEmpty()) {

                name.setError(
                        "পূর্ণ নাম দিন");

                return;
            }

            if (phoneText.isEmpty()) {

                phone.setError(
                        "ফোন নম্বর দিন");

                return;
            }

            if (passText.length() != 6) {

                password.setError(
                        "৬ ডিজিটের পাসওয়ার্ড দিন");

                return;
            }

            if (!passText.equals(confirmText)) {

                confirmPassword.setError(
                        "পাসওয়ার্ড একই নয়");

                return;
            }

            pref.edit()
                    .putString(
                            "name",
                            fullName)
                    .putString(
                            "phone",
                            phoneText)
                    .putString(
                            "password",
                            passText)
                    .putBoolean(
                            "logged_in",
                            true)
                    .apply();

            showPinSetup();
        });

        TextView login =
                tv(
                        "অ্যাকাউন্ট আছে?  লগইন",
                        16,
                        Color.WHITE);

        login.setGravity(
                Gravity.CENTER);

        root.addView(
                login,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)));

        login.setOnClickListener(
                v -> showLogin());
    }

    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    private void showForgotPassword() {

        EditText phone =
                new EditText(this);

        phone.setHint(
                "ফোন নম্বর");

        phone.setInputType(
                InputType.TYPE_CLASS_PHONE);

        new AlertDialog.Builder(this)

                .setTitle(
                        "পাসওয়ার্ড পুনরুদ্ধার")

                .setMessage(
                        "আপনার রেজিস্টার করা ফোন নম্বর দিন।")

                .setView(phone)

                .setPositiveButton(
                        "পরবর্তী",
                        (dialog, which) -> {

                            Toast.makeText(
                                    this,
                                    "পাসওয়ার্ড রিসেট ফিচার পরে যুক্ত হবে",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        })

                .setNegativeButton(
                        "বাতিল",
                        null)

                .show();
    }

    // =========================================================
    // BACK BUTTON
    // =========================================================

    @Override
    public void onBackPressed() {

        if (pref.getBoolean(
                "logged_in",
                false)) {

            showHome();

        } else {

            showLogin();
        }
    }
}
