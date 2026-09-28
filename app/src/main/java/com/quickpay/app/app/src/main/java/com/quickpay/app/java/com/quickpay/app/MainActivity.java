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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    // =========================================================
    // COLORS
    // =========================================================

    private static final int BLUE = Color.rgb(8, 96, 190);
    private static final int GREEN = Color.rgb(0, 92, 68);
    private static final int YELLOW = Color.rgb(255, 190, 25);
    private static final int DARK = Color.rgb(35, 35, 35);
    private static final int LIGHT_BG = Color.rgb(246, 247, 251);
    private static final int BORDER = Color.rgb(225, 226, 231);

    private SharedPreferences pref;

    // =========================================================
    // MOBILE BANKING
    // =========================================================

    private String selectedMobileBanking = "বিকাশ";
    private String selectedAccountType = "পার্সোনাল";

    private TextView bkashTab;
    private TextView nagadTab;
    private TextView rocketTab;
    private TextView upayTab;

    private TextView personalTab;
    private TextView agentTab;

    private EditText mobileNumberInput;
    private EditText amountInput;

    // =========================================================
    // ACTIVITY START
    // =========================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences(
                "quick_pay",
                Context.MODE_PRIVATE
        );

        boolean loggedIn = pref.getBoolean(
                "logged_in",
                false
        );

        String savedPin = pref.getString(
                "pin",
                ""
        );

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
    // DP
    // =========================================================

    private int dp(int value) {

        return (int) (
                value *
                        getResources()
                                .getDisplayMetrics()
                                .density
        );
    }

    // =========================================================
    // TEXTVIEW
    // =========================================================

    private TextView tv(
            String text,
            float size,
            int color
    ) {

        TextView t = new TextView(this);

        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);

        return t;
    }

    // =========================================================
    // BACKGROUND
    // =========================================================

    private GradientDrawable bg(
            int color,
            int radius
    ) {

        GradientDrawable g =
                new GradientDrawable();

        g.setColor(color);
        g.setCornerRadius(dp(radius));

        return g;
    }

    // =========================================================
    // OUTLINE
    // =========================================================

    private GradientDrawable outline(
            int fill,
            int stroke,
            int radius
    ) {

        GradientDrawable g =
                new GradientDrawable();

        g.setColor(fill);
        g.setCornerRadius(dp(radius));
        g.setStroke(dp(1), stroke);

        return g;
    }

    // =========================================================
    // SPACE
    // =========================================================

    private View space(int height) {

        View v = new View(this);

        v.setLayoutParams(
                new LinearLayout.LayoutParams(
                        1,
                        dp(height)
                )
        );

        return v;
    }

    // =========================================================
    // INPUT
    // =========================================================

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

        boolean numeric =
                password ||
                hint.contains("ফোন") ||
                hint.contains("নম্বর") ||
                hint.contains("টাকার পরিমাণ") ||
                hint.contains("পরিমাণ") ||
                hint.contains("PIN") ||
                hint.contains("পিন");

        if (numeric) {

            if (password) {

                e.setInputType(
                        InputType.TYPE_CLASS_NUMBER |
                                InputType.TYPE_NUMBER_VARIATION_PASSWORD
                );

            } else {

                e.setInputType(
                        InputType.TYPE_CLASS_NUMBER |
                                InputType.TYPE_NUMBER_FLAG_DECIMAL
                );
            }

        } else {

            e.setInputType(
                    InputType.TYPE_CLASS_TEXT
            );
        }

        e.setBackground(
                outline(
                        Color.WHITE,
                        BORDER,
                        16
                )
        );

        return e;
    }

    // =========================================================
    // PIN INPUT
    // =========================================================

    private EditText pinInput(
            String hint
    ) {

        EditText e =
                new EditText(this);

        e.setHint(hint);
        e.setTextSize(22);
        e.setGravity(Gravity.CENTER);
        e.setSingleLine(true);

        e.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        e.setBackground(
                outline(
                        Color.WHITE,
                        BORDER,
                        16
                )
        );

        return e;
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private Button button(
            String text,
            int color
    ) {

        Button b =
                new Button(this);

        b.setText(text);
        b.setTextSize(17);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER);

        b.setBackground(
                bg(
                        color,
                        16
                )
        );

        return b;
    }

    // =========================================================
    // LOGIN
    // =========================================================

    private void showLogin() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setGravity(
                Gravity.CENTER
        );

        root.setPadding(
                dp(28),
                dp(30),
                dp(28),
                dp(30)
        );

        root.setBackgroundColor(
                LIGHT_BG
        );

        TextView logo =
                tv(
                        "Quick Pay",
                        34,
                        BLUE
                );

        logo.setGravity(
                Gravity.CENTER
        );

        logo.setTypeface(
                null,
                Typeface.BOLD
        );

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(70)
                )
        );

        TextView welcome =
                tv(
                        "স্বাগতম",
                        26,
                        DARK
                );

        welcome.setGravity(
                Gravity.CENTER
        );

        welcome.setTypeface(
                null,
                Typeface.BOLD
        );

        root.addView(welcome);

        root.addView(
                space(25)
        );

        EditText phone =
                input(
                        "মোবাইল নম্বর",
                        false
                );

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        root.addView(
                space(14)
        );

        EditText password =
                input(
                        "পাসওয়ার্ড",
                        true
                );

        root.addView(
                password,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        root.addView(
                space(20)
        );

        Button login =
                button(
                        "লগইন",
                        BLUE
                );

        root.addView(
                login,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        login.setOnClickListener(v -> {

            String p =
                    phone.getText()
                            .toString()
                            .trim();

            String pass =
                    password.getText()
                            .toString()
                            .trim();

            if (p.isEmpty()) {

                phone.setError(
                        "মোবাইল নম্বর দিন"
                );

                return;
            }

            if (pass.isEmpty()) {

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
                    && p.equals(savedPhone)
                    && pass.equals(savedPassword)) {

                pref.edit()
                        .putBoolean(
                                "logged_in",
                                true
                        )
                        .apply();

                String savedPin =
                        pref.getString(
                                "pin",
                                ""
                        );

                if (savedPin.length() == 8) {
                    showPinUnlock();
                } else {
                    showPinSetup();
                }

            } else {

                Toast.makeText(
                        this,
                        "মোবাইল নম্বর অথবা পাসওয়ার্ড সঠিক নয়",
                        Toast.LENGTH_LONG
                ).show();
            }
        });

        root.addView(
                space(10)
        );

        TextView forgot =
                tv(
                        "পাসওয়ার্ড ভুলে গেছেন?",
                        16,
                        BLUE
                );

        forgot.setGravity(
                Gravity.CENTER
        );

        root.addView(
                forgot,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        forgot.setOnClickListener(
                v -> showForgotPassword()
        );

        TextView register =
                tv(
                        "নতুন একাউন্ট খুলুন",
                        17,
                        GREEN
                );

        register.setGravity(
                Gravity.CENTER
        );

        root.addView(
                register,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
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
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setGravity(
                Gravity.CENTER
        );

        root.setPadding(
                dp(30),
                dp(30),
                dp(30),
                dp(30)
        );

        root.setBackgroundColor(
                LIGHT_BG
        );

        TextView title =
                tv(
                        "PIN সেট করুন",
                        28,
                        DARK
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        root.addView(title);

        root.addView(
                space(12)
        );

        TextView sub =
                tv(
                        "আপনার ৮ সংখ্যার PIN সেট করুন",
                        17,
                        Color.GRAY
                );

        sub.setGravity(
                Gravity.CENTER
        );

        root.addView(sub);

        root.addView(
                space(25)
        );

        EditText pin =
                pinInput(
                        "৮ সংখ্যার PIN"
                );

        root.addView(
                pin,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        root.addView(
                space(15)
        );

        EditText confirm =
                pinInput(
                        "PIN আবার লিখুন"
                );

        root.addView(
                confirm,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        root.addView(
                space(22)
        );

        Button save =
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

            String c =
                    confirm.getText()
                            .toString()
                            .trim();

            if (p.length() != 8) {

                pin.setError(
                        "৮ সংখ্যার PIN দিন"
                );

                return;
            }

            if (!p.equals(c)) {

                confirm.setError(
                        "PIN মিলছে না"
                );

                return;
            }

            pref.edit()
                    .putString(
                            "pin",
                            p
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
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setGravity(
                Gravity.CENTER
        );

        root.setPadding(
                dp(30),
                dp(30),
                dp(30),
                dp(30)
        );

        root.setBackgroundColor(
                LIGHT_BG
        );

        TextView title =
                tv(
                        "Quick Pay",
                        34,
                        BLUE
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        root.addView(title);

        root.addView(
                space(15)
        );

        TextView sub =
                tv(
                        "আপনার PIN দিন",
                        22,
                        DARK
                );

        sub.setGravity(
                Gravity.CENTER
        );

        root.addView(sub);

        root.addView(
                space(25)
        );

        EditText pin =
                pinInput(
                        "৮ সংখ্যার PIN"
                );

        root.addView(
                pin,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        root.addView(
                space(20)
        );

        Button unlock =
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

                pin.setError(
                        "PIN সঠিক নয়"
                );
            }
        });

        setContentView(root);
    }

    // =========================================================
    // HOME
    // =========================================================

    private void showHome() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                LIGHT_BG
        );

        // HEADER

        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.VERTICAL
        );

        header.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(18)
        );

        header.setBackgroundColor(
                GREEN
        );

        TextView title =
                tv(
                        "Quick Pay",
                        28,
                        Color.WHITE
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        header.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        TextView balance =
                tv(
                        "Main Balance  ৳ ১২,৫০০",
                        20,
                        Color.WHITE
                );

        balance.setGravity(
                Gravity.CENTER
        );

        header.addView(balance);

        TextView drive =
                tv(
                        "Drive Balance  ৳ ১৮০",
                        17,
                        Color.WHITE
                );

        drive.setGravity(
                Gravity.CENTER
        );

        header.addView(drive);

        root.addView(header);

        // CONTENT

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(15),
                dp(18),
                dp(15),
                dp(30)
        );

        // ROW 1

        LinearLayout row1 =
                new LinearLayout(this);

        row1.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addHomeService(
                row1,
                "💰",
                "অ্যাড\nব্যালেন্স",
                v -> showAddBalance()
        );

        addHomeService(
                row1,
                "🏦",
                "মোবাইল\nব্যাংকিং",
                v -> showMobileBanking()
        );

        content.addView(row1);

        content.addView(
                space(12)
        );

        // ROW 2

        LinearLayout row2 =
                new LinearLayout(this);

        row2.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addHomeService(
                row2,
                "📱",
                "মোবাইল\nরিচার্জ",
                v -> showComingSoon(
                        "মোবাইল রিচার্জ"
                )
        );

        addHomeService(
                row2,
                "🌐",
                "ইন্টারনেট\nপ্যাক",
                v -> showComingSoon(
                        "ইন্টারনেট প্যাক"
                )
        );

        content.addView(row2);

        content.addView(
                space(12)
        );

        // ROW 3

        LinearLayout row3 =
                new LinearLayout(this);

        row3.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addHomeService(
                row3,
                "📜",
                "লেনদেন\nইতিহাস",
                v -> showComingSoon(
                        "লেনদেন ইতিহাস"
                )
        );

        addHomeService(
                row3,
                "🎁",
                "বোনাস/\nকমিশন",
                v -> showComingSoon(
                        "বোনাস / কমিশন"
                )
        );

        content.addView(row3);

        content.addView(
                space(12)
        );

        // ROW 4

        LinearLayout row4 =
                new LinearLayout(this);

        row4.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addHomeService(
                row4,
                "🎧",
                "কাস্টমার\nকেয়ার",
                v -> showComingSoon(
                        "কাস্টমার কেয়ার"
                )
        );

        addHomeService(
                row4,
                "⚡",
                "বিদ্যুৎ\nবিল",
                v -> showComingSoon(
                        "বিদ্যুৎ বিল"
                )
        );

        content.addView(row4);

        content.addView(
                space(20)
        );

        // BONUS

        LinearLayout bonus =
                new LinearLayout(this);

        bonus.setOrientation(
                LinearLayout.VERTICAL
        );

        bonus.setPadding(
                dp(20),
                dp(18),
                dp(20),
                dp(18)
        );

        bonus.setBackground(
                outline(
                        Color.WHITE,
                        BORDER,
                        18
                )
        );

        TextView bonusTitle =
                tv(
                        "🔥 বেশি অ্যাড মানি = বেশি বোনাস = বেশি লাভ",
                        19,
                        DARK
                );

        bonusTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        bonus.addView(
                bonusTitle
        );

        bonus.addView(
                space(8)
        );

        TextView bonusText =
                tv(
                        "মোবাইল রিচার্জ • ইন্টারনেট প্যাক • ডি বিল • সেন্ড মানি",
                        15,
                        Color.GRAY
                );

        bonus.addView(
                bonusText
        );

        content.addView(
                bonus
        );

        scroll.addView(
                content
        );

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        // BOTTOM NAV

        LinearLayout bottom =
                new LinearLayout(this);

        bottom.setGravity(
                Gravity.CENTER
        );

        bottom.setPadding(
                dp(10),
                dp(8),
                dp(10),
                dp(8)
        );

        bottom.setBackgroundColor(
                Color.WHITE
        );

        TextView home =
                tv(
                        "⌂\nহোম",
                        15,
                        BLUE
                );

        home.setGravity(
                Gravity.CENTER
        );

        bottom.addView(
                home,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1
                )
        );

        TextView history =
                tv(
                        "▤\nইতিহাস",
                        15,
                        Color.GRAY
                );

        history.setGravity(
                Gravity.CENTER
        );

        bottom.addView(
                history,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1
                )
        );

        TextView account =
                tv(
                        "♙\nপ্রোফাইল",
                        15,
                        Color.GRAY
                );

        account.setGravity(
                Gravity.CENTER
        );

        bottom.addView(
                account,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1
                )
        );

        root.addView(bottom);

        setContentView(root);
    }

    // =========================================================
    // HOME SERVICE
    // =========================================================

    private void addHomeService(
            LinearLayout row,
            String icon,
            String text,
            View.OnClickListener listener
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setGravity(
                Gravity.CENTER
        );

        card.setPadding(
                dp(5),
                dp(10),
                dp(5),
                dp(10)
        );

        card.setBackground(
                outline(
                        Color.WHITE,
                        BORDER,
                        18
                )
        );

        TextView iconText =
                tv(
                        icon,
                        27,
                        DARK
                );

        iconText.setGravity(
                Gravity.CENTER
        );

        card.addView(iconText);

        TextView label =
                tv(
                        text,
                        15,
                        DARK
                );

        label.setGravity(
                Gravity.CENTER
        );

        card.addView(label);

        card.setOnClickListener(
                listener
        );

        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        0,
                        dp(105),
                        1
                );

        lp.setMargins(
                dp(5),
                0,
                dp(5),
                0
        );

        row.addView(
                card,
                lp
        );
    }

    // =========================================================
    // ADD BALANCE
    // =========================================================

    private void showAddBalance() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                LIGHT_BG
        );

        root.addView(
                createHeader(
                        "অ্যাড ব্যালেন্স"
                )
        );

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(25),
                dp(30),
                dp(25),
                dp(30)
        );

        TextView info =
                tv(
                        "আপনার Quick Pay ব্যালেন্স যোগ করতে নিচের অপশনটি নির্বাচন করুন।",
                        18,
                        Color.GRAY
                );

        info.setGravity(
                Gravity.CENTER
        );

        content.addView(info);

        content.addView(
                space(25)
        );

        Button auto =
                button(
                        "অটো ডিপোজিট",
                        BLUE
                );

        content.addView(
                auto,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        auto.setOnClickListener(
                v -> showAutoDeposit()
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
    // HEADER
    // =========================================================

    private LinearLayout createHeader(
            String titleText
    ) {

        LinearLayout header =
                new LinearLayout(this);

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(12),
                dp(10),
                dp(12),
                dp(10)
        );

        header.setBackgroundColor(
                BLUE
        );

        TextView back =
                tv(
                        "‹",
                        42,
                        Color.WHITE
                );

        back.setGravity(
                Gravity.CENTER
        );

        back.setBackground(
                bg(
                        Color.rgb(
                                60,
                                135,
                                215
                        ),
                        50
                )
        );

        LinearLayout.LayoutParams backLp =
                new LinearLayout.LayoutParams(
                        dp(62),
                        dp(62)
                );

        backLp.setMargins(
                dp(10),
                0,
                dp(12),
                0
        );

        header.addView(
                back,
                backLp
        );

        TextView title =
                tv(
                        titleText,
                        25,
                        Color.WHITE
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(
                null,
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

        back.setOnClickListener(
                v -> showHome()
        );

        return header;
    }

    // =========================================================
    // AUTO DEPOSIT / MANUAL DEPOSIT
    // =========================================================

    private void showAutoDeposit() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                LIGHT_BG
        );

        root.addView(
                createHeader(
                        "অটো ডিপোজিট"
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
                dp(18),
                dp(18),
                dp(18),
                dp(30)
        );

        TextView notice =
                tv(
                        "নিচের নম্বরে টাকা পাঠিয়ে পরিমাণ ও ট্রানজেকশন আইডি দিয়ে ডিপোজিট রিকোয়েস্ট করুন।",
                        16,
                        Color.GRAY
                );

        notice.setGravity(
                Gravity.CENTER
        );

        content.addView(notice);

        content.addView(
                space(18)
        );

        // =====================================================
        // PROVIDER NUMBERS
        // বর্তমানে সব ফাঁকা
        // পরে এখানে নম্বর বসানো যাবে
        // =====================================================

        addDepositProvider(
                content,
                "bKash Personal",
                "",
                "বিকাশ"
        );

        addDepositProvider(
                content,
                "Nagad Personal",
                "",
                "নগদ"
        );

        addDepositProvider(
                content,
                "Rocket Personal",
                "",
                "রকেট"
        );

        addDepositProvider(
                content,
                "Upay Personal",
                "",
                "উপায়"
        );

        content.addView(
                space(18)
        );

        TextView targetTitle =
                tv(
                        "কোন ব্যালেন্সে যোগ করবেন?",
                        20,
                        DARK
                );

        targetTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                targetTitle
        );

        content.addView(
                space(10)
        );

        LinearLayout targetRow =
                new LinearLayout(this);

        targetRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        Button mainBalance =
                button(
                        "Main Balance\nসর্বনিম্ন ৳৫০",
                        BLUE
                );

        Button directBalance =
                button(
                        "Direct Balance\nসর্বনিম্ন ৳৫০০",
                        Color.GRAY
                );

        targetRow.addView(
                mainBalance,
                new LinearLayout.LayoutParams(
                        0,
                        dp(70),
                        1
                )
        );

        LinearLayout.LayoutParams directLp =
                new LinearLayout.LayoutParams(
                        0,
                        dp(70),
                        1
                );

        directLp.setMargins(
                dp(8),
                0,
                0,
                0
        );

        targetRow.addView(
                directBalance,
                directLp
        );

        content.addView(
                targetRow
        );

        final String[] target =
                {"Main Balance"};

        mainBalance.setOnClickListener(v -> {

            target[0] =
                    "Main Balance";

            mainBalance.setBackground(
                    bg(
                            BLUE,
                            16
                    )
            );

            directBalance.setBackground(
                    bg(
                            Color.GRAY,
                            16
                    )
            );
        });

        directBalance.setOnClickListener(v -> {

            target[0] =
                    "Direct Balance";

            directBalance.setBackground(
                    bg(
                            BLUE,
                            16
                    )
            );

            mainBalance.setBackground(
                    bg(
                            Color.GRAY,
                            16
                    )
            );
        });

        content.addView(
                space(18)
        );

        EditText amount =
                input(
                        "টাকার পরিমাণ",
                        false
                );

        content.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        content.addView(
                space(12)
        );

        EditText transaction =
                input(
                        "Transaction ID",
                        false
                );

        content.addView(
                transaction,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        content.addView(
                space(18)
        );

        Button submit =
                button(
                        "ডিপোজিট রিকোয়েস্ট",
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

            String t =
                    transaction.getText()
                            .toString()
                            .trim();

            if (a.isEmpty()) {

                amount.setError(
                        "টাকার পরিমাণ দিন"
                );

                return;
            }

            double value;

            try {

                value =
                        Double.parseDouble(a);

            } catch (Exception e) {

                amount.setError(
                        "সঠিক পরিমাণ দিন"
                );

                return;
            }

            double minimum =
                    target[0].equals(
                            "Direct Balance"
                    )
                            ? 500
                            : 50;

            if (value < minimum) {

                amount.setError(
                        target[0].equals(
                                "Direct Balance"
                        )
                                ? "Direct Balance-এর জন্য সর্বনিম্ন ৳৫০০"
                                : "Main Balance-এর জন্য সর্বনিম্ন ৳৫০"
                );

                return;
            }

            if (t.isEmpty()) {

                transaction.setError(
                        "Transaction ID দিন"
                );

                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle(
                            "ডিপোজিট রিকোয়েস্ট"
                    )
                    .setMessage(
                            "ব্যালেন্স: " +
                                    target[0] +
                                    "\n\n" +
                                    "পরিমাণ: ৳ " +
                                    a +
                                    "\n\n" +
                                    "Transaction ID: " +
                                    t +
                                    "\n\n" +
                                    "অ্যাডমিন ভেরিফিকেশনের পর ব্যালেন্স যোগ হবে।"
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
                                        "ডিপোজিট রিকোয়েস্ট সাবমিট হয়েছে।",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                    )
                    .show();
        });

        scroll.addView(
                content
        );

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
    // DEPOSIT PROVIDER CARD
    // =========================================================

    private void addDepositProvider(
            LinearLayout parent,
            String name,
            String number,
            String provider
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.HORIZONTAL
        );

        card.setGravity(
                Gravity.CENTER_VERTICAL
        );

        card.setPadding(
                dp(14),
                dp(8),
                dp(10),
                dp(8)
        );

        card.setBackground(
                outline(
                        Color.WHITE,
                        BORDER,
                        16
                )
        );

        TextView title =
                tv(
                        name,
                        17,
                        DARK
                );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                title,
                new LinearLayout.LayoutParams(
                        0,
                        dp(55),
                        1
                )
        );

        TextView numberText =
                tv(
                        number,
                        16,
                        Color.GRAY
                );

        numberText.setGravity(
                Gravity.CENTER
        );

        card.addView(
                numberText,
                new LinearLayout.LayoutParams(
                        dp(90),
                        dp(55)
                )
        );

        Button copy =
                new Button(this);

        copy.setText(
                "কপি"
        );

        copy.setTextSize(
                13
        );

        copy.setAllCaps(
                false
        );

        copy.setTextColor(
                Color.WHITE
        );

        copy.setBackground(
                bg(
                        BLUE,
                        12
                )
        );

        card.addView(
                copy,
                new LinearLayout.LayoutParams(
                        dp(62),
                        dp(45)
                )
        );

        copy.setOnClickListener(v -> {

            if (number == null
                    || number.trim().isEmpty()) {

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

            android.content.ClipData data =
                    android.content.ClipData
                            .newPlainText(
                                    "Number",
                                    number
                            );

            clipboard.setPrimaryClip(
                    data
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
                        dp(72)
                );

        lp.setMargins(
                0,
                0,
                0,
                dp(8)
        );

        parent.addView(
                card,
                lp
        );
    }

    // =========================================================
    // MOBILE BANKING SCREEN
    // =========================================================

    private void showMobileBanking() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                LIGHT_BG
        );

        root.addView(
                createHeader(
                        "মোবাইল ব্যাংকিং"
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
                dp(35),
                dp(18),
                dp(35),
                dp(30)
        );

        // =====================================================
        // BIKASH / NAGAD / ROCKET / UPAY
        // =====================================================

        LinearLayout providerRow =
                new LinearLayout(this);

        providerRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        providerRow.setGravity(
                Gravity.CENTER_VERTICAL
        );

        bkashTab =
                createMobileProvider(
                        "bKash",
                        "বিকাশ",
                        Color.rgb(
                                250,
                                235,
                                242
                        )
                );

        nagadTab =
                createMobileProvider(
                        "নগদ",
                        "নগদ",
                        Color.rgb(
                                250,
                                238,
                                235
                        )
                );

        rocketTab =
                createMobileProvider(
                        "ROCKET",
                        "রকেট",
                        Color.rgb(
                                240,
                                235,
                                247
                        )
                );

        upayTab =
                createMobileProvider(
                        "উপায়",
                        "উপায়",
                        Color.rgb(
                                233,
                                244,
                                238
                        )
                );

        providerRow.addView(
                makeProviderBox(
                        bkashTab
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(145),
                        1
                )
        );

        providerRow.addView(
                makeProviderBox(
                        nagadTab
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(145),
                        1
                )
        );

        providerRow.addView(
                makeProviderBox(
                        rocketTab
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(145),
                        1
                )
        );

        providerRow.addView(
                makeProviderBox(
                        upayTab
                ),
                new LinearLayout.LayoutParams(
                        0,
                        dp(145),
                        1
                )
        );

        content.addView(
                providerRow
        );

        content.addView(
                space(25)
        );

        // =====================================================
        // ACCOUNT TYPE
        // =====================================================

        TextView accountTitle =
                tv(
                        "একাউন্ট ধরন",
                        26,
                        DARK
                );

        accountTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                accountTitle
        );

        TextView accountSub =
                tv(
                        "একাউন্টের ধরন নির্বাচন করুন",
                        19,
                        Color.GRAY
                );

        content.addView(
                accountSub
        );

        content.addView(
                space(15)
        );

        LinearLayout accountRow =
                new LinearLayout(this);

        accountRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        personalTab =
                createAccountButton(
                        "♙",
                        "পার্সোনাল"
                );

        agentTab =
                createAccountButton(
                        "▦",
                        "এজেন্ট"
                );

        accountRow.addView(
                personalTab,
                new LinearLayout.LayoutParams(
                        0,
                        dp(105),
                        1
                )
        );

        LinearLayout.LayoutParams agentLp =
                new LinearLayout.LayoutParams(
                        0,
                        dp(105),
                        1
                );

        agentLp.setMargins(
                dp(22),
                0,
                0,
                0
        );

        accountRow.addView(
                agentTab,
                agentLp
        );

        content.addView(
                accountRow
        );

        content.addView(
                space(32)
        );

        // =====================================================
        // MOBILE NUMBER
        // =====================================================

        TextView numberTitle =
                tv(
                        "মোবাইল নম্বর",
                        26,
                        DARK
                );

        numberTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                numberTitle
        );

        TextView numberSub =
                tv(
                        "রিসিভারের মোবাইল নম্বর দিন",
                        19,
                        Color.GRAY
                );

        content.addView(
                numberSub
        );

        content.addView(
                space(15)
        );

        LinearLayout numberBox =
                new LinearLayout(this);

        numberBox.setOrientation(
                LinearLayout.HORIZONTAL
        );

        numberBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        numberBox.setBackground(
                outline(
                        Color.WHITE,
                        BORDER,
                        18
                )
        );

        TextView country =
                tv(
                        "+88",
                        19,
                        Color.GRAY
                );

        country.setGravity(
                Gravity.CENTER
        );

        numberBox.addView(
                country,
                new LinearLayout.LayoutParams(
                        dp(100),
                        dp(92)
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
                        dp(92)
                )
        );

        mobileNumberInput =
                new EditText(this);

        mobileNumberInput.setHint(
                "01XXXXXXXXX"
        );

        mobileNumberInput.setTextSize(
                19
        );

        mobileNumberInput.setSingleLine(
                true
        );

        mobileNumberInput.setPadding(
                dp(25),
                0,
                dp(15),
                0
        );

        mobileNumberInput.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        mobileNumberInput.setBackgroundColor(
                Color.TRANSPARENT
        );

        numberBox.addView(
                mobileNumberInput,
                new LinearLayout.LayoutParams(
                        0,
                        dp(92),
                        1
                )
        );

        content.addView(
                numberBox,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(92)
                )
        );

        content.addView(
                space(32)
        );

        // =====================================================
        // AMOUNT
        // =====================================================

        TextView amountTitle =
                tv(
                        "পরিমাণ লিখুন",
                        26,
                        DARK
                );

        amountTitle.setTypeface(
                null,
                Typeface.BOLD
        );

        content.addView(
                amountTitle
        );

        TextView amountSub =
                tv(
                        "কত টাকা পাঠাবেন?",
                        19,
                        Color.GRAY
                );

        content.addView(
                amountSub
        );

        content.addView(
                space(15)
        );

        LinearLayout amountBox =
                new LinearLayout(this);

        amountBox.setOrientation(
                LinearLayout.HORIZONTAL
        );

        amountBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        amountBox.setPadding(
                dp(25),
                0,
                dp(20),
                0
        );

        amountBox.setBackground(
                outline(
                        Color.WHITE,
                        BORDER,
                        18
                )
        );

        TextView moneyIcon =
                tv(
                        "৳",
                        32,
                        DARK
                );

        moneyIcon.setGravity(
                Gravity.CENTER
        );

        amountBox.addView(
                moneyIcon,
                new LinearLayout.LayoutParams(
                        dp(70),
                        dp(92)
                )
        );

        amountInput =
                new EditText(this);

        amountInput.setHint(
                "পরিমাণ লিখুন"
        );

        amountInput.setTextSize(
                20
        );

        amountInput.setSingleLine(
                true
        );

        amountInput.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        amountInput.setBackgroundColor(
                Color.TRANSPARENT
        );

        amountBox.addView(
                amountInput,
                new LinearLayout.LayoutParams(
                        0,
                        dp(92),
                        1
                )
        );

        content.addView(
                amountBox,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(92)
                )
        );

        content.addView(
                space(20)
        );

        // =====================================================
        // QUICK AMOUNT BUTTONS
        // =====================================================

        LinearLayout quickRow =
                new LinearLayout(this);

        quickRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addQuickAmount(
                quickRow,
                "৳ 1,000",
                "1000"
        );

        addQuickAmount(
                quickRow,
                "৳ 10,000",
                "10000"
        );

        addQuickAmount(
                quickRow,
                "৳ 20,000",
                "20000"
        );

        addQuickAmount(
                quickRow,
                "৳ 50,000",
                "50000"
        );

        content.addView(
                quickRow
        );

        content.addView(
                space(45)
        );

        // =====================================================
        // SEND MONEY
        // =====================================================

        Button sendButton =
                new Button(this);

        sendButton.setText(
                "টাকা পাঠান   →"
        );

        sendButton.setTextSize(
                23
        );

        sendButton.setTextColor(
                Color.WHITE
        );

        sendButton.setGravity(
                Gravity.CENTER
        );

        sendButton.setAllCaps(
                false
        );

        sendButton.setBackground(
                bg(
                        Color.rgb(
                                165,
                                172,
                                185
                        ),
                        28
                )
        );

        content.addView(
                sendButton,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(90)
                )
        );

        sendButton.setOnClickListener(v -> {

            String number =
                    mobileNumberInput
                            .getText()
                            .toString()
                            .trim();

            String amount =
                    amountInput
                            .getText()
                            .toString()
                            .trim();

            // NUMBER CHECK

            if (number.length() < 11) {

                mobileNumberInput.setError(
                        "সঠিক মোবাইল নম্বর দিন"
                );

                mobileNumberInput.requestFocus();

                return;
            }

            // AMOUNT CHECK

            if (amount.isEmpty()) {

                amountInput.setError(
                        "পরিমাণ লিখুন"
                );

                amountInput.requestFocus();

                return;
            }

            double value;

            try {

                value =
                        Double.parseDouble(
                                amount
                        );

            } catch (Exception e) {

                amountInput.setError(
                        "সঠিক পরিমাণ লিখুন"
                );

                return;
            }

            if (value <= 0) {

                amountInput.setError(
                        "সঠিক পরিমাণ লিখুন"
                );

                return;
            }

            // CONFIRM

            new AlertDialog.Builder(this)
                    .setTitle(
                            "টাকা পাঠান"
                    )
                    .setMessage(
                            "মাধ্যম: " +
                                    selectedMobileBanking +
                                    "\n\n" +
                                    "একাউন্ট: " +
                                    selectedAccountType +
                                    "\n\n" +
                                    "মোবাইল নম্বর: " +
                                    number +
                                    "\n\n" +
                                    "পরিমাণ: ৳ " +
                                    amount
                    )
                    .setNegativeButton(
                            "বাতিল",
                            null
                    )
                    .setPositiveButton(
                            "নিশ্চিত করুন",
                            (dialog, which) -> {

                                Toast.makeText(
                                        this,
                                        selectedMobileBanking +
                                                " এর টাকা পাঠানোর রিকোয়েস্ট গ্রহণ করা হয়েছে।",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                    )
                    .show();
        });

        scroll.addView(
                content
        );

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(root);

        // DEFAULT
        selectMobileProvider(
                "বিকাশ"
        );

        selectAccountType(
                "পার্সোনাল"
        );
    }

    // =========================================================
    // MOBILE PROVIDER BOX
    // =========================================================

    private LinearLayout makeProviderBox(
            TextView tab
    ) {

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(
                Gravity.CENTER
        );

        box.setPadding(
                dp(3),
                dp(4),
                dp(3),
                dp(3)
        );

        box.addView(
                tab,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(75)
                )
        );

        TextView name =
                tv(
                        getProviderName(
                                tab.getText()
                                        .toString()
                        ),
                        20,
                        DARK
                );

        name.setGravity(
                Gravity.CENTER
        );

        name.setTypeface(
                null,
                Typeface.BOLD
        );

        box.addView(
                name,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        box.setOnClickListener(v -> {

            String provider =
                    getProviderName(
                            tab.getText()
                                    .toString()
                    );

            selectMobileProvider(
                    provider
            );
        });

        return box;
    }

    // =========================================================
    // PROVIDER BUTTON
    // =========================================================

    private TextView createMobileProvider(
            String logoText,
            String name,
            int bgColor
    ) {

        TextView t =
                tv(
                        logoText,
                        15,
                        DARK
                );

        t.setGravity(
                Gravity.CENTER
        );

        t.setTypeface(
                null,
                Typeface.BOLD
        );

        t.setTag(name);

        GradientDrawable normal =
                new GradientDrawable();

        normal.setColor(
                bgColor
        );

        normal.setCornerRadius(
                dp(28)
        );

        t.setBackground(
                normal
        );

        return t;
    }

    // =========================================================
    // PROVIDER SELECT
    // =========================================================

    private void selectMobileProvider(
            String provider
    ) {

        selectedMobileBanking =
                provider;

        resetProviderStyle(
                bkashTab,
                Color.rgb(
                        250,
                        235,
                        242
                )
        );

        resetProviderStyle(
                nagadTab,
                Color.rgb(
                        250,
                        238,
                        235
                )
        );

        resetProviderStyle(
                rocketTab,
                Color.rgb(
                        240,
                        235,
                        247
                )
        );

        resetProviderStyle(
                upayTab,
                Color.rgb(
                        233,
                        244,
                        238
                )
        );

        TextView selected =
                null;

        if (provider.equals("বিকাশ")) {

            selected =
                    bkashTab;

        } else if (provider.equals("নগদ")) {

            selected =
                    nagadTab;

        } else if (provider.equals("রকেট")) {

            selected =
                    rocketTab;

        } else if (provider.equals("উপায়")) {

            selected =
                    upayTab;
        }

        if (selected != null) {

            int color =
                    Color.WHITE;

            if (provider.equals("বিকাশ")) {

                color =
                        Color.rgb(
                                250,
                                235,
                                242
                        );

            } else if (provider.equals("নগদ")) {

                color =
                        Color.rgb(
                                250,
                                238,
                                235
                        );

            } else if (provider.equals("রকেট")) {

                color =
                        Color.rgb(
                                240,
                                235,
                                247
                        );

            } else if (provider.equals("উপায়")) {

                color =
                        Color.rgb(
                                233,
                                244,
                                238
                        );
            }

            GradientDrawable selectedBg =
                    new GradientDrawable();

            selectedBg.setColor(
                    color
            );

            selectedBg.setCornerRadius(
                    dp(28)
            );

            selectedBg.setStroke(
                    dp(3),
                    Color.rgb(
                            48,
                            69,
                            92
                    )
            );

            selected.setBackground(
                    selectedBg
            );
        }
    }

    // =========================================================
    // RESET PROVIDER
    // =========================================================

    private void resetProviderStyle(
            TextView view,
            int color
    ) {

        if (view == null) {
            return;
        }

        GradientDrawable g =
                new GradientDrawable();

        g.setColor(
                color
        );

        g.setCornerRadius(
                dp(28)
        );

        view.setBackground(g);
    }

    // =========================================================
    // PROVIDER NAME
    // =========================================================

    private String getProviderName(
            String value
    ) {

        if (value.equals("bKash")) {
            return "বিকাশ";
        }

        if (value.equals("নগদ")) {
            return "নগদ";
        }

        if (value.equals("ROCKET")) {
            return "রকেট";
        }

        if (value.equals("উপায়")) {
            return "উপায়";
        }

        return value;
    }

    // =========================================================
    // ACCOUNT BUTTON
    // =========================================================

    private TextView createAccountButton(
            String icon,
            String name
    ) {

        TextView t =
                tv(
                        icon + "\n" + name,
                        19,
                        DARK
                );

        t.setGravity(
                Gravity.CENTER
        );

        t.setTypeface(
                null,
                Typeface.BOLD
        );

        t.setTag(name);

        t.setBackground(
                outline(
                        Color.WHITE,
                        BORDER,
                        18
                )
        );

        t.setOnClickListener(
                v -> selectAccountType(
                        name
                )
        );

        return t;
    }

    // =========================================================
    // ACCOUNT SELECT
    // =========================================================

    private void selectAccountType(
            String type
    ) {

        selectedAccountType =
                type;

        if (personalTab == null
                || agentTab == null) {

            return;
        }

        if (type.equals("পার্সোনাল")) {

            personalTab.setTextColor(
                    DARK
            );

            personalTab.setBackground(
                    outline(
                            Color.rgb(
                                    242,
                                    248,
                                    255
                            ),
                            Color.rgb(
                                    48,
                                    69,
                                    92
                            ),
                            18
                    )
            );

            agentTab.setTextColor(
                    DARK
            );

            agentTab.setBackground(
                    outline(
                            Color.WHITE,
                            BORDER,
                            18
                    )
            );

        } else {

            agentTab.setTextColor(
                    DARK
            );

            agentTab.setBackground(
                    outline(
                            Color.rgb(
                                    242,
                                    248,
                                    255
                            ),
                            Color.rgb(
                                    48,
                                    69,
                                    92
                            ),
                            18
                    )
            );

            personalTab.setTextColor(
                    DARK
            );

            personalTab.setBackground(
                    outline(
                            Color.WHITE,
                            BORDER,
                            18
                    )
            );
        }
    }

    // =========================================================
    // QUICK AMOUNT
    // =========================================================

    private void addQuickAmount(
            LinearLayout row,
            String text,
            String value
    ) {

        Button b =
                new Button(this);

        b.setText(text);
        b.setTextSize(15);
        b.setTextColor(DARK);
        b.setAllCaps(false);

        b.setBackground(
                outline(
                        Color.WHITE,
                        BORDER,
                        18
                )
        );

        b.setOnClickListener(v -> {

            if (amountInput != null) {

                amountInput.setText(
                        value
                );

                amountInput.setSelection(
                        amountInput.length()
                );
            }
        });

        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        0,
                        dp(75),
                        1
                );

        lp.setMargins(
                dp(2),
                0,
                dp(2),
                0
        );

        row.addView(
                b,
                lp
        );
    }

    // =========================================================
    // OLD PROVIDER ROW
    // =========================================================

    private void addProviderRow(
            LinearLayout parent,
            String name,
            String number
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.HORIZONTAL
        );

        card.setGravity(
                Gravity.CENTER_VERTICAL
        );

        card.setPadding(
                dp(15),
                dp(8),
                dp(10),
                dp(8)
        );

        card.setBackground(
                outline(
                        Color.WHITE,
                        BORDER,
                        16
                )
        );

        TextView nameText =
                tv(
                        name,
                        17,
                        DARK
                );

        nameText.setTypeface(
                null,
                Typeface.BOLD
        );

        card.addView(
                nameText,
                new LinearLayout.LayoutParams(
                        0,
                        dp(60),
                        1
                )
        );

        TextView numberText =
                tv(
                        number == null
                                ? ""
                                : number,
                        15,
                        Color.GRAY
                );

        numberText.setGravity(
                Gravity.CENTER
        );

        card.addView(
                numberText,
                new LinearLayout.LayoutParams(
                        dp(110),
                        dp(60)
                )
        );

        Button copy =
                new Button(this);

        copy.setText(
                "কপি"
        );

        copy.setTextSize(
                13
        );

        copy.setAllCaps(
                false
        );

        copy.setTextColor(
                Color.WHITE
        );

        copy.setBackground(
                bg(
                        BLUE,
                        12
                )
        );

        card.addView(
                copy,
                new LinearLayout.LayoutParams(
                        dp(62),
                        dp(45)
                )
        );

        copy.setOnClickListener(v -> {

            if (number == null
                    || number.trim().isEmpty()) {

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

            android.content.ClipData clip =
                    android.content.ClipData
                            .newPlainText(
                                    "Number",
                                    number
                            );

            clipboard.setPrimaryClip(
                    clip
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
                        dp(78)
                );

        lp.setMargins(
                0,
                0,
                0,
                dp(8)
        );

        parent.addView(
                card,
                lp
        );
    }

    // =========================================================
    // MONEY FORM
    // =========================================================

    private void showMoneyForm(
            String titleText,
            String buttonText
    ) {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                LIGHT_BG
        );

        root.addView(
                createHeader(
                        titleText
                )
        );

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(25),
                dp(25),
                dp(25),
                dp(25)
        );

        EditText phone =
                input(
                        "মোবাইল নম্বর",
                        false
                );

        content.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        content.addView(
                space(15)
        );

        EditText amount =
                input(
                        "টাকার পরিমাণ",
                        false
                );

        content.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        content.addView(
                space(20)
        );

        Button submit =
                button(
                        buttonText,
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

            String p =
                    phone.getText()
                            .toString()
                            .trim();

            String a =
                    amount.getText()
                            .toString()
                            .trim();

            if (p.length() < 11) {

                phone.setError(
                        "সঠিক নম্বর দিন"
                );

                return;
            }

            if (a.isEmpty()) {

                amount.setError(
                        "টাকার পরিমাণ দিন"
                );

                return;
            }

            double value;

            try {

                value =
                        Double.parseDouble(a);

            } catch (Exception e) {

                amount.setError(
                        "সঠিক পরিমাণ দিন"
                );

                return;
            }

            if (value < 500) {

                amount.setError(
                        "সর্বনিম্ন ৳৫০০"
                );

                return;
            }

            Toast.makeText(
                    this,
                    "রিকোয়েস্ট গ্রহণ করা হয়েছে।",
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
    // REGISTER
    // =========================================================

    private void showRegister() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                LIGHT_BG
        );

        root.addView(
                createHeader(
                        "রেজিস্টার"
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
                dp(25),
                dp(25),
                dp(25),
                dp(30)
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

        content.addView(
                space(15)
        );

        EditText phone =
                input(
                        "মোবাইল নম্বর",
                        false
                );

        content.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        content.addView(
                space(15)
        );

        EditText password =
                input(
                        "পাসওয়ার্ড",
                        true
                );

        content.addView(
                password,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        content.addView(
                space(20)
        );

        Button register =
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

            if (n.isEmpty()) {

                name.setError(
                        "নাম দিন"
                );

                return;
            }

            if (p.length() < 11) {

                phone.setError(
                        "সঠিক মোবাইল নম্বর দিন"
                );

                return;
            }

            if (pass.length() < 4) {

                password.setError(
                        "পাসওয়ার্ড দিন"
                );

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

            Toast.makeText(
                    this,
                    "একাউন্ট তৈরি হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();

            showPinSetup();
        });

        scroll.addView(
                content
        );

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
    // FORGOT PASSWORD
    // =========================================================

    private void showForgotPassword() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setGravity(
                Gravity.CENTER
        );

        root.setPadding(
                dp(25),
                dp(25),
                dp(25),
                dp(25)
        );

        root.setBackgroundColor(
                LIGHT_BG
        );

        TextView title =
                tv(
                        "পাসওয়ার্ড পরিবর্তন",
                        25,
                        DARK
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(
                null,
                Typeface.BOLD
        );

        root.addView(title);

        root.addView(
                space(25)
        );

        EditText phone =
                input(
                        "মোবাইল নম্বর",
                        false
                );

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        root.addView(
                space(15)
        );

        EditText newPassword =
                input(
                        "নতুন পাসওয়ার্ড",
                        true
                );

        root.addView(
                newPassword,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        root.addView(
                space(20)
        );

        Button save =
                button(
                        "পরিবর্তন করুন",
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
                    phone.getText()
                            .toString()
                            .trim();

            String pass =
                    newPassword.getText()
                            .toString()
                            .trim();

            String savedPhone =
                    pref.getString(
                            "phone",
                            ""
                    );

            if (!p.equals(savedPhone)) {

                phone.setError(
                        "নম্বর মিলছে না"
                );

                return;
            }

            if (pass.length() < 4) {

                newPassword.setError(
                        "নতুন পাসওয়ার্ড দিন"
                );

                return;
            }

            pref.edit()
                    .putString(
                            "password",
                            pass
                    )
                    .apply();

            Toast.makeText(
                    this,
                    "পাসওয়ার্ড পরিবর্তন হয়েছে",
                    Toast.LENGTH_LONG
            ).show();

            showLogin();
        });

        setContentView(root);
    }

    // =========================================================
    // COMING SOON
    // =========================================================

    private void showComingSoon(
            String title
    ) {

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(
                        "এই সেবাটি পরবর্তী ধাপে যুক্ত করা হবে।"
                )
                .setPositiveButton(
                        "ঠিক আছে",
                        null
                )
                .show();
    }

    // =========================================================
    // BACK
    // =========================================================

    @Override
    public void onBackPressed() {

        boolean loggedIn =
                pref.getBoolean(
                        "logged_in",
                        false
                );

        if (loggedIn) {

            showHome();

        } else {

            super.onBackPressed();
        }
    }
}
