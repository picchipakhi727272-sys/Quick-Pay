package com.quickpay.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    // =========================================================
    // COLORS
    // =========================================================

    private static final int BLUE = Color.rgb(8, 96, 190);
    private static final int GREEN = Color.rgb(0, 92, 68);
    private static final int YELLOW = Color.rgb(255, 190, 25);
    private static final int DARK = Color.rgb(35, 35, 35);

    private static final int BG = Color.rgb(246, 247, 250);
    private static final int TEXT_GRAY = Color.rgb(105, 110, 120);
    private static final int BORDER = Color.rgb(225, 227, 232);
    private static final int SELECTED = Color.rgb(45, 65, 85);

    private SharedPreferences pref;

    // =========================================================
    // MOBILE BANKING STATE
    // =========================================================

    private String selectedMobileProvider = "বিকাশ";
    private String selectedAccountType = "পার্সোনাল";

    // =========================================================
    // MOBILE RECHARGE STATE
    // =========================================================

    private String selectedRechargeOperator = "বাংলালিংক";
    private String selectedRechargeType = "প্রিপেইড";

    // =========================================================
    // ACTIVITY
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences(
                "quick_pay",
                Context.MODE_PRIVATE
        );

        boolean loggedIn =
                pref.getBoolean("logged_in", false);

        String savedPin =
                pref.getString("pin", "");

        if (loggedIn) {

            if (savedPin.length() == 8) {
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

    private int dp(int value) {
        return (int) (
                value *
                        getResources()
                                .getDisplayMetrics()
                                .density
        );
    }

    private TextView tv(
            String text,
            float size,
            int color,
            boolean bold
    ) {

        TextView t = new TextView(this);

        t.setText(text);
        t.setTextSize(
                TypedValue.COMPLEX_UNIT_SP,
                size
        );
        t.setTextColor(color);

        if (bold) {
            t.setTypeface(
                    null,
                    android.graphics.Typeface.BOLD
            );
        }

        t.setGravity(Gravity.CENTER_VERTICAL);

        return t;
    }

    private GradientDrawable bg(
            int color,
            int radius
    ) {

        GradientDrawable d =
                new GradientDrawable();

        d.setColor(color);
        d.setCornerRadius(dp(radius));

        return d;
    }

    private GradientDrawable outline(
            int strokeColor,
            int fillColor,
            int radius
    ) {

        GradientDrawable d =
                new GradientDrawable();

        d.setColor(fillColor);
        d.setStroke(
                dp(2),
                strokeColor
        );
        d.setCornerRadius(dp(radius));

        return d;
    }

    private Space space(int height) {

        Space s = new Space(this);

        s.setLayoutParams(
                new LinearLayout.LayoutParams(
                        1,
                        dp(height)
                )
        );

        return s;
    }

    private EditText input(
            String hint,
            boolean number
    ) {

        EditText e =
                new EditText(this);

        e.setHint(hint);
        e.setTextSize(17);
        e.setSingleLine(true);
        e.setPadding(
                dp(15),
                0,
                dp(15),
                0
        );

        if (number) {
            e.setInputType(
                    InputType.TYPE_CLASS_PHONE
            );
        } else {
            e.setInputType(
                    InputType.TYPE_CLASS_TEXT
            );
        }

        e.setBackground(
                outline(
                        BORDER,
                        Color.WHITE,
                        14
                )
        );

        return e;
    }

    private EditText pinInput() {

        EditText e =
                new EditText(this);

        e.setHint("৮ সংখ্যার PIN");
        e.setTextSize(18);
        e.setSingleLine(true);
        e.setGravity(Gravity.CENTER);
        e.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        e.setBackground(
                outline(
                        BORDER,
                        Color.WHITE,
                        14
                )
        );

        return e;
    }

    private TextView button(
            String text,
            int color
    ) {

        TextView b =
                tv(
                        text,
                        17,
                        Color.WHITE,
                        true
                );

        b.setGravity(Gravity.CENTER);

        b.setBackground(
                bg(
                        color,
                        14
                )
        );

        return b;
    }

    private LinearLayout vertical() {

        LinearLayout l =
                new LinearLayout(this);

        l.setOrientation(
                LinearLayout.VERTICAL
        );

        return l;
    }

    private LinearLayout horizontal() {

        LinearLayout l =
                new LinearLayout(this);

        l.setOrientation(
                LinearLayout.HORIZONTAL
        );

        return l;
    }

    // =========================================================
    // LOGIN
    // =========================================================

    private void showLogin() {

        LinearLayout root =
                vertical();

        root.setGravity(Gravity.CENTER);
        root.setPadding(
                dp(25),
                dp(25),
                dp(25),
                dp(25)
        );

        root.setBackgroundColor(BLUE);

        TextView logo =
                tv(
                        "⚡",
                        55,
                        Color.WHITE,
                        true
                );

        logo.setGravity(Gravity.CENTER);

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(80)
                )
        );

        TextView title =
                tv(
                        "Quick Pay",
                        30,
                        Color.WHITE,
                        true
                );

        title.setGravity(Gravity.CENTER);

        root.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        TextView subtitle =
                tv(
                        "আপনার বিশ্বস্ত ডিজিটাল সেবা",
                        16,
                        Color.WHITE,
                        false
                );

        subtitle.setGravity(Gravity.CENTER);

        root.addView(
                subtitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(40)
                )
        );

        root.addView(space(25));

        EditText phone =
                input(
                        "মোবাইল নম্বর",
                        true
                );

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        root.addView(space(12));

        EditText password =
                input(
                        "পাসওয়ার্ড",
                        false
                );

        password.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        root.addView(
                password,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        root.addView(space(18));

        TextView login =
                button(
                        "লগইন",
                        GREEN
                );

        root.addView(
                login,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        login.setOnClickListener(v -> {

            if (phone.getText()
                    .toString()
                    .trim()
                    .isEmpty()) {

                Toast.makeText(
                        this,
                        "মোবাইল নম্বর দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (password.getText()
                    .toString()
                    .trim()
                    .isEmpty()) {

                Toast.makeText(
                        this,
                        "পাসওয়ার্ড দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            pref.edit()
                    .putBoolean(
                            "logged_in",
                            true
                    )
                    .putString(
                            "user_name",
                            "User"
                    )
                    .apply();

            showPinSetup();
        });

        root.addView(space(10));

        TextView forgot =
                tv(
                        "পাসওয়ার্ড ভুলে গেছেন?",
                        15,
                        Color.WHITE,
                        false
                );

        forgot.setGravity(Gravity.CENTER);

        root.addView(
                forgot,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(40)
                )
        );

        forgot.setOnClickListener(
                v -> showForgotPassword()
        );

        TextView register =
                tv(
                        "নতুন একাউন্ট খুলুন",
                        16,
                        Color.WHITE,
                        true
                );

        register.setGravity(Gravity.CENTER);

        root.addView(
                register,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        register.setOnClickListener(
                v -> showRegister()
        );

        setContentView(root);
    }

    // =========================================================
    // PIN SETUP
    // =========================================================

    private void showPinSetup() {

        LinearLayout root =
                vertical();

        root.setGravity(Gravity.CENTER);
        root.setPadding(
                dp(25),
                dp(25),
                dp(25),
                dp(25)
        );

        root.setBackgroundColor(BG);

        TextView icon =
                tv(
                        "🔐",
                        55,
                        BLUE,
                        true
                );

        icon.setGravity(Gravity.CENTER);

        root.addView(
                icon,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(80)
                )
        );

        TextView title =
                tv(
                        "PIN সেট করুন",
                        27,
                        DARK,
                        true
                );

        title.setGravity(Gravity.CENTER);

        root.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        TextView sub =
                tv(
                        "আপনার ৮ সংখ্যার PIN দিন",
                        16,
                        TEXT_GRAY,
                        false
                );

        sub.setGravity(Gravity.CENTER);

        root.addView(
                sub,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        root.addView(space(20));

        EditText pin =
                pinInput();

        root.addView(
                pin,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        root.addView(space(18));

        TextView save =
                button(
                        "PIN সংরক্ষণ করুন",
                        BLUE
                );

        root.addView(
                save,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        save.setOnClickListener(v -> {

            String p =
                    pin.getText()
                            .toString()
                            .trim();

            if (p.length() != 8) {

                Toast.makeText(
                        this,
                        "৮ সংখ্যার PIN দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            pref.edit()
                    .putString("pin", p)
                    .putBoolean(
                            "logged_in",
                            true
                    )
                    .apply();

            showHome();
        });

        setContentView(root);
    }

    // =========================================================
    // PIN UNLOCK
    // =========================================================

    private void showPinUnlock() {

        LinearLayout root =
                vertical();

        root.setGravity(Gravity.CENTER);
        root.setPadding(
                dp(25),
                dp(25),
                dp(25),
                dp(25)
        );

        root.setBackgroundColor(BG);

        TextView icon =
                tv(
                        "🔐",
                        55,
                        BLUE,
                        true
                );

        icon.setGravity(Gravity.CENTER);

        root.addView(
                icon,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(80)
                )
        );

        TextView title =
                tv(
                        "Quick Pay",
                        28,
                        DARK,
                        true
                );

        title.setGravity(Gravity.CENTER);

        root.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        TextView sub =
                tv(
                        "PIN দিয়ে প্রবেশ করুন",
                        16,
                        TEXT_GRAY,
                        false
                );

        sub.setGravity(Gravity.CENTER);

        root.addView(
                sub,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(40)
                )
        );

        root.addView(space(20));

        EditText pin =
                pinInput();

        root.addView(
                pin,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        root.addView(space(18));

        TextView unlock =
                button(
                        "প্রবেশ করুন",
                        BLUE
                );

        root.addView(
                unlock,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        unlock.setOnClickListener(v -> {

            String entered =
                    pin.getText()
                            .toString()
                            .trim();

            String saved =
                    pref.getString(
                            "pin",
                            ""
                    );

            if (entered.equals(saved)) {

                showHome();

            } else {

                Toast.makeText(
                        this,
                        "PIN সঠিক নয়",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        setContentView(root);
    }

    // =========================================================
    // HOME
    // =========================================================

    private void showHome() {

        LinearLayout root =
                vertical();

        root.setBackgroundColor(BG);

        // HEADER
        LinearLayout header =
                vertical();

        header.setPadding(
                dp(20),
                dp(15),
                dp(20),
                dp(15)
        );

        header.setBackgroundColor(GREEN);

        TextView brand =
                tv(
                        "⚡ Quick Pay",
                        25,
                        Color.WHITE,
                        true
                );

        header.addView(
                brand,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        String userName =
                pref.getString(
                        "user_name",
                        "User"
                );

        TextView welcome =
                tv(
                        "স্বাগতম, " + userName,
                        16,
                        Color.WHITE,
                        false
                );

        header.addView(
                welcome,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(35)
                )
        );

        root.addView(header);

        // BALANCE
        LinearLayout balance =
                vertical();

        balance.setPadding(
                dp(18),
                dp(15),
                dp(18),
                dp(15)
        );

        balance.setBackground(
                bg(
                        Color.WHITE,
                        16
                )
        );

        TextView mainBalance =
                tv(
                        "Main Balance  ৳ ১২,৫০০",
                        21,
                        DARK,
                        true
                );

        balance.addView(
                mainBalance,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        TextView driveBalance =
                tv(
                        "Drive Balance  ৳ ১৮০",
                        17,
                        TEXT_GRAY,
                        false
                );

        balance.addView(
                driveBalance,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(35)
                )
        );

        LinearLayout.LayoutParams balLp =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(100)
                );

        balLp.setMargins(
                dp(15),
                dp(15),
                dp(15),
                dp(10)
        );

        root.addView(balance, balLp);

        // SCROLL
        ScrollView scroll =
                new ScrollView(this);

        LinearLayout content =
                vertical();

        content.setPadding(
                dp(15),
                dp(5),
                dp(15),
                dp(25)
        );

        scroll.addView(content);

        // SECTION
        TextView serviceTitle =
                tv(
                        "Quick Services",
                        22,
                        DARK,
                        true
                );

        content.addView(
                serviceTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        // ROW 1
        LinearLayout row1 =
                horizontal();

        addHomeService(
                row1,
                "📱",
                "মোবাইল রিচার্জ",
                v -> showMobileRecharge()
        );

        addHomeService(
                row1,
                "💰",
                "অ্যাড ব্যালেন্স",
                v -> showAddBalance()
        );

        content.addView(row1);

        // ROW 2
        LinearLayout row2 =
                horizontal();

        addHomeService(
                row2,
                "🏦",
                "মোবাইল ব্যাংকিং",
                v -> showMobileBanking()
        );

        addHomeService(
                row2,
                "🌐",
                "ইন্টারনেট প্যাক",
                v -> Toast.makeText(
                        this,
                        "ইন্টারনেট প্যাক শিগগির আসছে",
                        Toast.LENGTH_SHORT
                ).show()
        );

        content.addView(row2);

        // ROW 3
        LinearLayout row3 =
                horizontal();

        addHomeService(
                row3,
                "📜",
                "লেনদেন ইতিহাস",
                v -> Toast.makeText(
                        this,
                        "লেনদেন ইতিহাস শিগগির আসছে",
                        Toast.LENGTH_SHORT
                ).show()
        );

        addHomeService(
                row3,
                "🎁",
                "বোনাস / কমিশন",
                v -> Toast.makeText(
                        this,
                        "বোনাস / কমিশন শিগগির আসছে",
                        Toast.LENGTH_SHORT
                ).show()
        );

        content.addView(row3);

        // ROW 4
        LinearLayout row4 =
                horizontal();

        addHomeService(
                row4,
                "🎧",
                "কাস্টমার কেয়ার",
                v -> Toast.makeText(
                        this,
                        "কাস্টমার কেয়ার",
                        Toast.LENGTH_SHORT
                ).show()
        );

        addHomeService(
                row4,
                "⚙️",
                "আরও সেবা",
                v -> Toast.makeText(
                        this,
                        "আরও সেবা শিগগির আসছে",
                        Toast.LENGTH_SHORT
                ).show()
        );

        content.addView(row4);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(root);
    }

    private void addHomeService(
            LinearLayout row,
            String icon,
            String text,
            View.OnClickListener click
    ) {

        LinearLayout box =
                vertical();

        box.setGravity(Gravity.CENTER);

        box.setBackground(
                outline(
                        BORDER,
                        Color.WHITE,
                        16
                )
        );

        TextView i =
                tv(
                        icon,
                        30,
                        DARK,
                        false
                );

        i.setGravity(Gravity.CENTER);

        box.addView(
                i,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        TextView t =
                tv(
                        text,
                        14,
                        DARK,
                        true
                );

        t.setGravity(Gravity.CENTER);

        box.addView(
                t,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        0,
                        dp(110),
                        1
                );

        lp.setMargins(
                dp(5),
                dp(5),
                dp(5),
                dp(5)
        );

        row.addView(box, lp);

        box.setOnClickListener(click);
    }

    // =========================================================
    // ADD BALANCE
    // =========================================================

    private void showAddBalance() {

        LinearLayout root =
                vertical();

        root.setBackgroundColor(BG);

        addBlueHeader(
                root,
                "অ্যাড ব্যালেন্স",
                v -> showHome()
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );

        TextView info =
                tv(
                        "আপনার Quick Pay একাউন্টে ব্যালেন্স যোগ করুন",
                        18,
                        DARK,
                        true
                );

        content.addView(
                info,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(70)
                )
        );

        TextView auto =
                button(
                        "অটো ডিপোজিট",
                        BLUE
                );

        content.addView(
                auto,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(65)
                )
        );

        auto.setOnClickListener(
                v -> showAutoDeposit()
        );

        TextView note =
                tv(
                        "\nMain Balance সর্বনিম্ন ৳50\nDirect Balance সর্বনিম্ন ৳500",
                        16,
                        TEXT_GRAY,
                        false
                );

        content.addView(
                note,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(100)
                )
        );

        root.addView(
                content,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(root);
    }

    // =========================================================
    // AUTO DEPOSIT
    // =========================================================

    private void showAutoDeposit() {

        LinearLayout root =
                vertical();

        root.setBackgroundColor(BG);

        addBlueHeader(
                root,
                "অটো ডিপোজিট",
                v -> showAddBalance()
        );

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout content =
                vertical();

        content.setPadding(
                dp(15),
                dp(15),
                dp(15),
                dp(25)
        );

        TextView title =
                tv(
                        "টাকা জমা দেওয়ার মাধ্যম",
                        21,
                        DARK,
                        true
                );

        content.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        String[] providers = {
                "বিকাশ Personal",
                "নগদ Personal",
                "রকেট Personal",
                "উপায় Personal"
        };

        String[] numbers = {
                "",
                "",
                "",
                ""
        };

        for (int i = 0; i < providers.length; i++) {

            addDepositProvider(
                    content,
                    providers[i],
                    numbers[i]
            );
        }

        content.addView(space(15));

        TextView amountTitle =
                tv(
                        "পরিমাণ লিখুন",
                        19,
                        DARK,
                        true
                );

        content.addView(amountTitle);

        EditText amount =
                input(
                        "টাকার পরিমাণ",
                        true
                );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        content.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        content.addView(space(10));

        TextView transactionTitle =
                tv(
                        "Transaction ID",
                        19,
                        DARK,
                        true
                );

        content.addView(
                transactionTitle
        );

        EditText transactionId =
                input(
                        "Transaction ID লিখুন",
                        false
                );

        content.addView(
                transactionId,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        content.addView(space(18));

        TextView submit =
                button(
                        "রিকোয়েস্ট পাঠান",
                        BLUE
                );

        content.addView(
                submit,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        submit.setOnClickListener(v -> {

            String a =
                    amount.getText()
                            .toString()
                            .trim();

            String trx =
                    transactionId.getText()
                            .toString()
                            .trim();

            if (a.isEmpty()) {

                Toast.makeText(
                        this,
                        "টাকার পরিমাণ লিখুন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            int value;

            try {

                value =
                        Integer.parseInt(a);

            } catch (Exception e) {

                Toast.makeText(
                        this,
                        "সঠিক পরিমাণ লিখুন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (value < 50) {

                Toast.makeText(
                        this,
                        "সর্বনিম্ন ৳50 জমা দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (trx.isEmpty()) {

                Toast.makeText(
                        this,
                        "Transaction ID দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("রিকোয়েস্ট পাঠানো হয়েছে")
                    .setMessage(
                            "পরিমাণ: ৳ " + value
                                    + "\n"
                                    + "Transaction ID: "
                                    + trx
                                    + "\n\n"
                                    + "আপনার ডিপোজিট রিকোয়েস্ট "
                                    + "অ্যাডমিন ভেরিফাই করার পর "
                                    + "ব্যালেন্স যোগ করা হবে।"
                    )
                    .setPositiveButton(
                            "ঠিক আছে",
                            null
                    )
                    .show();
        });

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(root);
    }

    private void addDepositProvider(
            LinearLayout parent,
            String name,
            String number
    ) {

        LinearLayout box =
                horizontal();

        box.setGravity(
                Gravity.CENTER_VERTICAL
        );

        box.setPadding(
                dp(15),
                dp(5),
                dp(8),
                dp(5)
        );

        box.setBackground(
                outline(
                        BORDER,
                        Color.WHITE,
                        14
                )
        );

        TextView nameView =
                tv(
                        name,
                        16,
                        DARK,
                        true
                );

        box.addView(
                nameView,
                new LinearLayout.LayoutParams(
                        0,
                        dp(65),
                        1
                )
        );

        TextView copy =
                tv(
                        "কপি",
                        15,
                        BLUE,
                        true
                );

        copy.setGravity(Gravity.CENTER);

        copy.setBackground(
                bg(
                        Color.rgb(232, 241, 252),
                        10
                )
        );

        box.addView(
                copy,
                new LinearLayout.LayoutParams(
                        dp(65),
                        dp(45)
                )
        );

        copy.setOnClickListener(v -> {

            if (number == null ||
                    number.trim().isEmpty()) {

                Toast.makeText(
                        this,
                        "নম্বর এখনো সেট করা হয়নি",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            android.content.ClipboardManager cm =
                    (android.content.ClipboardManager)
                            getSystemService(
                                    CLIPBOARD_SERVICE
                            );

            cm.setPrimaryClip(
                    android.content.ClipData.newPlainText(
                            "Number",
                            number
                    )
            );

            Toast.makeText(
                    this,
                    "নম্বর কপি হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();
        });

        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(75)
                );

        lp.setMargins(
                0,
                0,
                0,
                dp(8)
        );

        parent.addView(box, lp);
    }

    // =========================================================
    // MOBILE BANKING
    // =========================================================

    private void showMobileBanking() {

        LinearLayout root =
                vertical();

        root.setBackgroundColor(BG);

        addBlueHeader(
                root,
                "মোবাইল ব্যাংকিং",
                v -> showHome()
        );

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout content =
                vertical();

        content.setPadding(
                dp(20),
                dp(15),
                dp(20),
                dp(30)
        );

        TextView providerTitle =
                tv(
                        "একটি মাধ্যম নির্বাচন করুন",
                        18,
                        TEXT_GRAY,
                        false
                );

        content.addView(
                providerTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        LinearLayout providerRow =
                horizontal();

        String[] providers = {
                "বিকাশ",
                "নগদ",
                "রকেট",
                "উপায়"
        };

        String[] icons = {
                "b",
                "ন",
                "R",
                "u"
        };

        for (int i = 0; i < providers.length; i++) {

            final String provider =
                    providers[i];

            LinearLayout box =
                    vertical();

            box.setGravity(Gravity.CENTER);

            boolean selected =
                    selectedMobileProvider
                            .equals(provider);

            box.setBackground(
                    selected
                            ? outline(
                                    SELECTED,
                                    Color.rgb(
                                            239,
                                            246,
                                            253
                                    ),
                                    12
                            )
                            : bg(
                                    Color.WHITE,
                                    12
                            )
            );

            TextView icon =
                    tv(
                            icons[i],
                            22,
                            selected
                                    ? BLUE
                                    : DARK,
                            true
                    );

            icon.setGravity(Gravity.CENTER);

            box.addView(
                    icon,
                    new LinearLayout.LayoutParams(
                            dp(65),
                            dp(55)
                    )
            );

            TextView name =
                    tv(
                            provider,
                            14,
                            DARK,
                            selected
                    );

            name.setGravity(Gravity.CENTER);

            box.addView(
                    name,
                    new LinearLayout.LayoutParams(
                            dp(75),
                            dp(35)
                    )
            );

            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(
                            0,
                            dp(100),
                            1
                    );

            lp.setMargins(
                    dp(3),
                    0,
                    dp(3),
                    0
            );

            providerRow.addView(
                    box,
                    lp
            );

            box.setOnClickListener(v -> {

                selectedMobileProvider =
                        provider;

                showMobileBanking();
            });
        }

        content.addView(providerRow);

        content.addView(space(22));

        TextView accountTitle =
                tv(
                        "একাউন্ট ধরন",
                        22,
                        DARK,
                        true
                );

        content.addView(accountTitle);

        LinearLayout accountRow =
                horizontal();

        String[] types = {
                "পার্সোনাল",
                "এজেন্ট"
        };

        for (String type : types) {

            final String selectedType =
                    type;

            TextView typeBox =
                    tv(
                            type,
                            17,
                            DARK,
                            selectedAccountType
                                    .equals(type)
                    );

            typeBox.setGravity(
                    Gravity.CENTER
            );

            typeBox.setBackground(
                    selectedAccountType
                            .equals(type)
                            ? outline(
                                    SELECTED,
                                    Color.rgb(
                                            239,
                                            246,
                                            253
                                    ),
                                    14
                            )
                            : outline(
                                    BORDER,
                                    Color.WHITE,
                                    14
                            )
            );

            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(
                            0,
                            dp(65),
                            1
                    );

            lp.setMargins(
                    dp(4),
                    dp(10),
                    dp(4),
                    0
            );

            accountRow.addView(
                    typeBox,
                    lp
            );

            typeBox.setOnClickListener(v -> {

                selectedAccountType =
                        selectedType;

                showMobileBanking();
            });
        }

        content.addView(accountRow);

        content.addView(space(25));

        TextView numberTitle =
                tv(
                        "মোবাইল নম্বর",
                        22,
                        DARK,
                        true
                );

        content.addView(numberTitle);

        LinearLayout numberBox =
                horizontal();

        numberBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        numberBox.setBackground(
                outline(
                        BORDER,
                        Color.WHITE,
                        14
                )
        );

        TextView country =
                tv(
                        "+88",
                        18,
                        TEXT_GRAY,
                        true
                );

        country.setGravity(
                Gravity.CENTER
        );

        numberBox.addView(
                country,
                new LinearLayout.LayoutParams(
                        dp(70),
                        dp(65)
                )
        );

        View line =
                new View(this);

        line.setBackgroundColor(BORDER);

        numberBox.addView(
                line,
                new LinearLayout.LayoutParams(
                        dp(1),
                        dp(65)
                )
        );

        EditText mobile =
                new EditText(this);

        mobile.setHint(
                "মোবাইল নম্বর"
        );

        mobile.setTextSize(17);

        mobile.setSingleLine(true);

        mobile.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        mobile.setPadding(
                dp(15),
                0,
                dp(15),
                0
        );

        mobile.setBackgroundColor(
                Color.TRANSPARENT
        );

        numberBox.addView(
                mobile,
                new LinearLayout.LayoutParams(
                        0,
                        dp(65),
                        1
                )
        );

        content.addView(
                numberBox,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(65)
                )
        );

        content.addView(space(25));

        TextView amountTitle =
                tv(
                        "পরিমাণ লিখুন",
                        22,
                        DARK,
                        true
                );

        content.addView(amountTitle);

        EditText amount =
                new EditText(this);

        amount.setHint(
                "টাকার পরিমাণ"
        );

        amount.setTextSize(18);

        amount.setSingleLine(true);

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        amount.setPadding(
                dp(15),
                0,
                dp(15),
                0
        );

        amount.setBackground(
                outline(
                        BORDER,
                        Color.WHITE,
                        14
                )
        );

        content.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(65)
                )
        );

        content.addView(space(12));

        LinearLayout quick =
                horizontal();

        String[] quickMoney = {
                "1000",
                "10000",
                "20000",
                "50000"
        };

        String[] quickText = {
                "৳ ১,০০০",
                "৳ ১০,০০০",
                "৳ ২০,০০০",
                "৳ ৫০,০০০"
        };

        for (int i = 0;
             i < quickMoney.length;
             i++) {

            TextView q =
                    tv(
                            quickText[i],
                            14,
                            DARK,
                            true
                    );

            q.setGravity(Gravity.CENTER);

            q.setBackground(
                    outline(
                            BORDER,
                            Color.WHITE,
                            12
                    )
            );

            final String value =
                    quickMoney[i];

            q.setOnClickListener(v -> {

                amount.setText(value);

                amount.setSelection(
                        amount.length()
                );
            });

            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(
                            0,
                            dp(55),
                            1
                    );

            lp.setMargins(
                    dp(3),
                    0,
                    dp(3),
                    0
            );

            quick.addView(q, lp);
        }

        content.addView(quick);

        content.addView(space(25));

        TextView send =
                button(
                        "টাকা পাঠান   →",
                        GREEN
                );

        content.addView(
                send,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(65)
                )
        );

        send.setOnClickListener(v -> {

            String number =
                    mobile.getText()
                            .toString()
                            .replaceAll(
                                    "[^0-9]",
                                    ""
                            );

            String money =
                    amount.getText()
                            .toString()
                            .trim();

            if (number.length() != 11) {

                Toast.makeText(
                        this,
                        "সঠিক ১১ সংখ্যার মোবাইল নম্বর দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (money.isEmpty()) {

                Toast.makeText(
                        this,
                        "টাকার পরিমাণ লিখুন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            int value;

            try {

                value =
                        Integer.parseInt(money);

            } catch (Exception e) {

                Toast.makeText(
                        this,
                        "সঠিক পরিমাণ লিখুন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (value < 500) {

                Toast.makeText(
                        this,
                        "সর্বনিম্ন ৳500 পাঠানো যাবে",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle(
                            "লেনদেন নিশ্চিত করুন"
                    )
                    .setMessage(
                            "মাধ্যম: "
                                    + selectedMobileProvider
                                    + "\n\n"
                                    + "একাউন্ট: "
                                    + selectedAccountType
                                    + "\n\n"
                                    + "নম্বর: +88 "
                                    + number
                                    + "\n\n"
                                    + "পরিমাণ: ৳ "
                                    + value
                                    + "\n\n"
                                    + "এটি বর্তমানে ডেমো রিকোয়েস্ট। "
                                    + "কোনো আসল টাকা পাঠানো হয়নি।"
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
                                        "রিকোয়েস্ট গ্রহণ করা হয়েছে",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                    )
                    .show();
        });

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(root);
    }

    // =========================================================
    // MOBILE RECHARGE
    // =========================================================

    private void showMobileRecharge() {

        LinearLayout root =
                vertical();

        root.setBackgroundColor(BG);

        addBlueHeader(
                root,
                "মোবাইল রিচার্জ",
                v -> showHome()
        );

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout content =
                vertical();

        content.setPadding(
                dp(20),
                dp(10),
                dp(20),
                dp(30)
        );

        TextView operatorTitle =
                tv(
                        "একটি অপারেটর নির্বাচন করুন",
                        18,
                        TEXT_GRAY,
                        false
                );

        content.addView(
                operatorTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        // OPERATOR SCROLL
        HorizontalScrollView operatorScroll =
                new HorizontalScrollView(this);

        operatorScroll.setHorizontalScrollBarEnabled(
                false
        );

        LinearLayout operatorRow =
                horizontal();

        operatorScroll.addView(
                operatorRow
        );

        String[] operators = {
                "গ্রামীণফোন",
                "বাংলালিংক",
                "রবি",
                "এয়ারটেল",
                "টেলিটক"
        };

        String[] logo = {
                "G",
                "B",
                "R",
                "A",
                "T"
        };

        for (int i = 0;
             i < operators.length;
             i++) {

            final String op =
                    operators[i];

            boolean selected =
                    selectedRechargeOperator
                            .equals(op);

            LinearLayout box =
                    vertical();

            box.setGravity(Gravity.CENTER);

            box.setPadding(
                    dp(5),
                    dp(5),
                    dp(5),
                    dp(3)
            );

            box.setBackground(
                    selected
                            ? outline(
                                    SELECTED,
                                    Color.rgb(
                                            239,
                                            246,
                                            253
                                    ),
                                    14
                            )
                            : bg(
                                    Color.rgb(
                                            238,
                                            241,
                                            246
                                    ),
                                    14
                            )
            );

            TextView logoView =
                    tv(
                            logo[i],
                            25,
                            BLUE,
                            true
                    );

            logoView.setGravity(
                    Gravity.CENTER
            );

            logoView.setBackground(
                    bg(
                            selected
                                    ? Color.rgb(
                                            225,
                                            239,
                                            252
                                    )
                                    : Color.rgb(
                                            238,
                                            241,
                                            246
                                    ),
                            15
                    )
            );

            box.addView(
                    logoView,
                    new LinearLayout.LayoutParams(
                            dp(65),
                            dp(65)
                    )
            );

            TextView name =
                    tv(
                            op,
                            14,
                            DARK,
                            selected
                    );

            name.setGravity(
                    Gravity.CENTER
            );

            box.addView(
                    name,
                    new LinearLayout.LayoutParams(
                            dp(90),
                            dp(35)
                    )
            );

            TextView check =
                    tv(
                            selected ? "✓" : "",
                            17,
                            Color.WHITE,
                            true
                    );

            check.setGravity(
                    Gravity.CENTER
            );

            if (selected) {

                check.setBackground(
                        bg(
                                SELECTED,
                                50
                        )
                );
            }

            box.addView(
                    check,
                    new LinearLayout.LayoutParams(
                            dp(27),
                            dp(27)
                    )
            );

            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(
                            dp(105),
                            dp(140)
                    );

            lp.setMargins(
                    0,
                    0,
                    dp(8),
                    0
            );

            operatorRow.addView(
                    box,
                    lp
            );

            box.setOnClickListener(v -> {

                selectedRechargeOperator =
                        op;

                showMobileRecharge();
            });
        }

        content.addView(
                operatorScroll
        );

        content.addView(space(20));

        // RECHARGE TYPE
        TextView typeTitle =
                tv(
                        "রিচার্জ ধরন",
                        22,
                        DARK,
                        true
                );

        content.addView(typeTitle);

        TextView typeSub =
                tv(
                        "রিচার্জের ধরন নির্বাচন করুন",
                        17,
                        TEXT_GRAY,
                        false
                );

        content.addView(typeSub);

        content.addView(space(10));

        LinearLayout typeRow =
                horizontal();

        String[] rechargeTypes = {
                "প্রিপেইড",
                "পোস্টপেইড"
        };

        for (String type :
                rechargeTypes) {

            final String selectedType =
                    type;

            boolean selected =
                    selectedRechargeType
                            .equals(type);

            TextView typeBox =
                    tv(
                            type,
                            17,
                            DARK,
                            selected
                    );

            typeBox.setGravity(
                    Gravity.CENTER
            );

            typeBox.setBackground(
                    selected
                            ? outline(
                                    SELECTED,
                                    Color.rgb(
                                            239,
                                            246,
                                            253
                                    ),
                                    14
                            )
                            : outline(
                                    BORDER,
                                    Color.WHITE,
                                    14
                            )
            );

            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(
                            0,
                            dp(75),
                            1
                    );

            lp.setMargins(
                    dp(4),
                    0,
                    dp(4),
                    0
            );

            typeRow.addView(
                    typeBox,
                    lp
            );

            typeBox.setOnClickListener(v -> {

                selectedRechargeType =
                        selectedType;

                showMobileRecharge();
            });
        }

        content.addView(typeRow);

        content.addView(space(28));

        // MOBILE NUMBER
        TextView numberTitle =
                tv(
                        "মোবাইল নম্বর",
                        22,
                        DARK,
                        true
                );

        content.addView(numberTitle);

        TextView numberSub =
                tv(
                        "রিচার্জ করতে চান এমন নম্বর দিন",
                        17,
                        TEXT_GRAY,
                        false
                );

        content.addView(numberSub);

        content.addView(space(10));

        LinearLayout numberBox =
                horizontal();

        numberBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        numberBox.setBackground(
                outline(
                        BORDER,
                        Color.WHITE,
                        15
                )
        );

        TextView prefix =
                tv(
                        "+88",
                        18,
                        TEXT_GRAY,
                        true
                );

        prefix.setGravity(
                Gravity.CENTER
        );

        numberBox.addView(
                prefix,
                new LinearLayout.LayoutParams(
                        dp(80),
                        dp(70)
                )
        );

        View divider =
                new View(this);

        divider.setBackgroundColor(
                BORDER
        );

        numberBox.addView(
                divider,
                new LinearLayout.LayoutParams(
                        dp(1),
                        dp(70)
                )
        );

        EditText mobile =
                new EditText(this);

        mobile.setHint(
                "ফোন নম্বর"
        );

        mobile.setTextSize(18);

        mobile.setSingleLine(true);

        mobile.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        mobile.setPadding(
                dp(15),
                0,
                dp(15),
                0
        );

        mobile.setBackgroundColor(
                Color.TRANSPARENT
        );

        numberBox.addView(
                mobile,
                new LinearLayout.LayoutParams(
                        0,
                        dp(70),
                        1
                )
        );

        content.addView(
                numberBox,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(70)
                )
        );

        content.addView(space(28));

        // AMOUNT
        TextView amountTitle =
                tv(
                        "পরিমাণ",
                        22,
                        DARK,
                        true
                );

        content.addView(amountTitle);

        TextView amountSub =
                tv(
                        "কত টাকা রিচার্জ করতে চান?",
                        17,
                        TEXT_GRAY,
                        false
                );

        content.addView(amountSub);

        content.addView(space(10));

        EditText amount =
                new EditText(this);

        amount.setHint(
                "পরিমাণ লিখুন"
        );

        amount.setTextSize(18);

        amount.setSingleLine(true);

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        amount.setPadding(
                dp(20),
                0,
                dp(20),
                0
        );

        amount.setBackground(
                outline(
                        BORDER,
                        Color.WHITE,
                        15
                )
        );

        content.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(70)
                )
        );

        content.addView(space(15));

        // QUICK MONEY
        LinearLayout quickRow =
                horizontal();

        String[] values = {
                "20",
                "100",
                "200",
                "500"
        };

        for (String value :
                values) {

            TextView quick =
                    tv(
                            "৳ " + value,
                            16,
                            DARK,
                            true
                    );

            quick.setGravity(
                    Gravity.CENTER
            );

            quick.setBackground(
                    outline(
                            BORDER,
                            Color.WHITE,
                            15
                    )
            );

            final String v =
                    value;

            quick.setOnClickListener(
                    view -> {

                        amount.setText(v);

                        amount.setSelection(
                                amount.length()
                        );
                    }
            );

            LinearLayout.LayoutParams lp =
                    new LinearLayout.LayoutParams(
                            0,
                            dp(65),
                            1
                    );

            lp.setMargins(
                    0,
                    0,
                    dp(7),
                    0
            );

            quickRow.addView(
                    quick,
                    lp
            );
        }

        content.addView(quickRow);

        content.addView(space(28));

        // RECHARGE BUTTON
        TextView recharge =
                button(
                        "রিচার্জ করুন   →",
                        GREEN
                );

        content.addView(
                recharge,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(65)
                )
        );

        recharge.setOnClickListener(v -> {

            String number =
                    mobile.getText()
                            .toString()
                            .replaceAll(
                                    "[^0-9]",
                                    ""
                            );

            String amountText =
                    amount.getText()
                            .toString()
                            .trim();

            if (number.length() != 11) {

                Toast.makeText(
                        this,
                        "সঠিক ১১ সংখ্যার মোবাইল নম্বর দিন",
                        Toast.LENGTH_SHORT
                ).show();

                mobile.requestFocus();

                return;
            }

            if (amountText.isEmpty()) {

                Toast.makeText(
                        this,
                        "রিচার্জের পরিমাণ লিখুন",
                        Toast.LENGTH_SHORT
                ).show();

                amount.requestFocus();

                return;
            }

            int rechargeAmount;

            try {

                rechargeAmount =
                        Integer.parseInt(
                                amountText
                        );

            } catch (Exception e) {

                Toast.makeText(
                        this,
                        "সঠিক পরিমাণ লিখুন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (rechargeAmount < 20) {

                Toast.makeText(
                        this,
                        "সর্বনিম্ন রিচার্জ ৳20",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle(
                            "রিচার্জ নিশ্চিত করুন"
                    )
                    .setMessage(
                            "অপারেটর: "
                                    + selectedRechargeOperator
                                    + "\n\n"
                                    + "রিচার্জ ধরন: "
                                    + selectedRechargeType
                                    + "\n\n"
                                    + "মোবাইল নম্বর: +88 "
                                    + number
                                    + "\n\n"
                                    + "পরিমাণ: ৳ "
                                    + rechargeAmount
                                    + "\n\n"
                                    + "এটি বর্তমানে ডেমো রিকোয়েস্ট। "
                                    + "কোনো আসল রিচার্জ করা হয়নি।"
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
                                        selectedRechargeOperator
                                                + " রিচার্জ রিকোয়েস্ট গ্রহণ করা হয়েছে",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                    )
                    .show();
        });

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(root);
    }

    // =========================================================
    // BLUE HEADER
    // =========================================================

    private void addBlueHeader(
            LinearLayout root,
            String title,
            View.OnClickListener backClick
    ) {

        LinearLayout header =
                horizontal();

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(10),
                dp(10),
                dp(10),
                dp(10)
        );

        header.setBackgroundColor(
                BLUE
        );

        TextView back =
                tv(
                        "‹",
                        42,
                        Color.WHITE,
                        false
                );

        back.setGravity(
                Gravity.CENTER
        );

        back.setBackground(
                bg(
                        Color.argb(
                                45,
                                255,
                                255,
                                255
                        ),
                        50
                )
        );

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(58),
                        dp(58)
                )
        );

        TextView titleView =
                tv(
                        title,
                        24,
                        Color.WHITE,
                        true
                );

        titleView.setGravity(
                Gravity.CENTER
        );

        header.addView(
                titleView,
                new LinearLayout.LayoutParams(
                        0,
                        dp(58),
                        1
                )
        );

        Space right =
                new Space(this);

        header.addView(
                right,
                new LinearLayout.LayoutParams(
                        dp(58),
                        dp(58)
                )
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(78)
                )
        );

        back.setOnClickListener(
                backClick
        );
    }

    // =========================================================
    // REGISTER
    // =========================================================

    private void showRegister() {

        LinearLayout root =
                vertical();

        root.setBackgroundColor(BG);

        addBlueHeader(
                root,
                "রেজিস্টার",
                v -> showLogin()
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );

        EditText name =
                input(
                        "আপনার নাম",
                        false
                );

        content.addView(
                name,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        content.addView(space(12));

        EditText phone =
                input(
                        "মোবাইল নম্বর",
                        true
                );

        content.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        content.addView(space(12));

        EditText password =
                input(
                        "পাসওয়ার্ড",
                        false
                );

        password.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        content.addView(
                password,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        content.addView(space(20));

        TextView register =
                button(
                        "একাউন্ট তৈরি করুন",
                        BLUE
                );

        content.addView(
                register,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        register.setOnClickListener(v -> {

            if (name.getText()
                    .toString()
                    .trim()
                    .isEmpty()) {

                Toast.makeText(
                        this,
                        "নাম লিখুন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (phone.getText()
                    .toString()
                    .trim()
                    .isEmpty()) {

                Toast.makeText(
                        this,
                        "মোবাইল নম্বর দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (password.getText()
                    .toString()
                    .trim()
                    .isEmpty()) {

                Toast.makeText(
                        this,
                        "পাসওয়ার্ড দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            pref.edit()
                    .putBoolean(
                            "logged_in",
                            true
                    )
                    .putString(
                            "user_name",
                            name.getText()
                                    .toString()
                    )
                    .apply();

            Toast.makeText(
                    this,
                    "একাউন্ট তৈরি হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();

            showPinSetup();
        });

        root.addView(
                content,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(root);
    }

    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    private void showForgotPassword() {

        LinearLayout root =
                vertical();

        root.setBackgroundColor(BG);

        addBlueHeader(
                root,
                "পাসওয়ার্ড ভুলে গেছেন",
                v -> showLogin()
        );

        LinearLayout content =
                vertical();

        content.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );

        TextView info =
                tv(
                        "আপনার মোবাইল নম্বর দিন।",
                        18,
                        DARK,
                        true
                );

        content.addView(
                info,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        EditText phone =
                input(
                        "মোবাইল নম্বর",
                        true
                );

        content.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        content.addView(space(18));

        TextView send =
                button(
                        "রিকভারি রিকোয়েস্ট",
                        BLUE
                );

        content.addView(
                send,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        send.setOnClickListener(v -> {

            if (phone.getText()
                    .toString()
                    .trim()
                    .isEmpty()) {

                Toast.makeText(
                        this,
                        "মোবাইল নম্বর দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Toast.makeText(
                    this,
                    "রিকভারি রিকোয়েস্ট গ্রহণ করা হয়েছে",
                    Toast.LENGTH_LONG
            ).show();
        });

        root.addView(
                content,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(root);
    }

    // =========================================================
    // BACK BUTTON
    // =========================================================

    @Override
    public void onBackPressed() {

        if (pref != null &&
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
