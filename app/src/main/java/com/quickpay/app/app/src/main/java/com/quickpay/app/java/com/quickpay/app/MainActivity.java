package com.quickpay.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    // ============================================================
    // COLORS
    // ============================================================

    private static final int BLUE = Color.rgb(8, 96, 190);
    private static final int GREEN = Color.rgb(0, 92, 68);
    private static final int YELLOW = Color.rgb(255, 190, 25);
    private static final int DARK = Color.rgb(35, 35, 35);
    private static final int LIGHT_BG = Color.rgb(247, 248, 250);

    // ============================================================
    // COMPANY NUMBERS
    // এখানেই তোমাদের আসল কোম্পানির নম্বর বসাবে
    // ============================================================

    private static final String COMPANY_BKASH = "01799999000";
    private static final String COMPANY_NAGAD = "01XXXXXXXXX";
    private static final String COMPANY_ROCKET = "01XXXXXXXXX";
    private static final String COMPANY_UPAY = "01XXXXXXXXX";

    // ============================================================
    // VARIABLES
    // ============================================================

    private SharedPreferences pref;

    private static final int PICK_IMAGE = 1001;

    private TextView selectedFileText;
    private String selectedFileUri = "";

    // ============================================================
    // ON CREATE
    // ============================================================

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

        if (loggedIn) {

            String pin = pref.getString(
                    "pin",
                    ""
            );

            if (pin.length() == 8) {
                showPinUnlock();
            } else {
                showPinSetup();
            }

        } else {
            showLogin();
        }
    }

    // ============================================================
    // BASIC HELPERS
    // ============================================================

    private int dp(int value) {
        return (int) (
                value * getResources()
                        .getDisplayMetrics()
                        .density
        );
    }

    private TextView tv(
            String text,
            int size,
            int color
    ) {

        TextView t = new TextView(this);

        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);

        return t;
    }

    private GradientDrawable bg(
            int color,
            float radius
    ) {

        GradientDrawable g =
                new GradientDrawable();

        g.setColor(color);
        g.setCornerRadius(dp((int) radius));

        return g;
    }

    private GradientDrawable outline(
            int fill,
            int stroke,
            int strokeWidth,
            float radius
    ) {

        GradientDrawable g =
                new GradientDrawable();

        g.setColor(fill);

        g.setStroke(
                dp(strokeWidth),
                stroke
        );

        g.setCornerRadius(
                dp((int) radius)
        );

        return g;
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

    private EditText textInput(
            String hint
    ) {

        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(16);
        e.setSingleLine(true);
        e.setPadding(
                dp(18),
                dp(10),
                dp(18),
                dp(10)
        );

        e.setBackground(
                outline(
                        Color.WHITE,
                        Color.rgb(225, 225, 225),
                        1,
                        18
                )
        );

        return e;
    }

    private EditText phoneInput(
            String hint
    ) {

        EditText e = textInput(hint);

        e.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        return e;
    }

    private EditText numberInput(
            String hint
    ) {

        EditText e = textInput(hint);

        e.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        return e;
    }

    private EditText passwordInput(
            String hint
    ) {

        EditText e = textInput(hint);

        e.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        return e;
    }

    private EditText pinInput(
            String hint
    ) {

        EditText e = passwordInput(hint);

        e.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        return e;
    }

    private TextView button(
            String text,
            int background,
            int textColor
    ) {

        TextView b = tv(
                text,
                17,
                textColor
        );

        b.setGravity(Gravity.CENTER);

        b.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        b.setPadding(
                dp(12),
                dp(12),
                dp(12),
                dp(12)
        );

        b.setBackground(
                bg(
                        background,
                        18
                )
        );

        return b;
    }

    // ============================================================
    // ROOT LAYOUT
    // ============================================================

    private LinearLayout root() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                LIGHT_BG
        );

        return root;
    }

    // ============================================================
    // HEADER
    // ============================================================

    private LinearLayout header(
            String title,
            final Runnable backAction
    ) {

        LinearLayout h =
                new LinearLayout(this);

        h.setOrientation(
                LinearLayout.HORIZONTAL
        );

        h.setGravity(
                Gravity.CENTER_VERTICAL
        );

        h.setPadding(
                dp(18),
                0,
                dp(18),
                0
        );

        h.setBackgroundColor(BLUE);

        TextView back =
                tv(
                        "‹",
                        42,
                        Color.WHITE
                );

        back.setGravity(Gravity.CENTER);

        back.setLayoutParams(
                new LinearLayout.LayoutParams(
                        dp(55),
                        dp(64)
                )
        );

        back.setOnClickListener(
                v -> backAction.run()
        );

        h.addView(back);

        TextView titleView =
                tv(
                        title,
                        25,
                        Color.WHITE
                );

        titleView.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        h.addView(
                titleView,
                new LinearLayout.LayoutParams(
                        0,
                        dp(64),
                        1
                )
        );

        return h;
    }

    // ============================================================
    // LOGIN
    // ============================================================

    private void showLogin() {

        LinearLayout root = root();

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setPadding(
                dp(25),
                dp(35),
                dp(25),
                dp(35)
        );

        TextView title =
                tv(
                        "Quick Pay",
                        30,
                        BLUE
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        box.addView(title);

        box.addView(space(30));

        TextView welcome =
                tv(
                        "লগইন করুন",
                        22,
                        DARK
                );

        welcome.setGravity(
                Gravity.CENTER
        );

        welcome.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        box.addView(welcome);

        box.addView(space(20));

        EditText phone =
                phoneInput(
                        "মোবাইল নম্বর"
                );

        box.addView(phone);

        box.addView(space(12));

        EditText password =
                passwordInput(
                        "৬ সংখ্যার পাসওয়ার্ড"
                );

        box.addView(password);

        box.addView(space(18));

        TextView login =
                button(
                        "লগইন",
                        BLUE,
                        Color.WHITE
                );

        box.addView(login);

        login.setOnClickListener(
                v -> {

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

                    if (pass.length() != 6) {

                        password.setError(
                                "৬ সংখ্যার পাসওয়ার্ড দিন"
                        );

                        return;
                    }

                    pref.edit()
                            .putBoolean(
                                    "logged_in",
                                    true
                            )
                            .putString(
                                    "phone",
                                    p
                            )
                            .apply();

                    showPinSetup();
                }
        );

        box.addView(space(12));

        TextView register =
                button(
                        "নতুন একাউন্ট খুলুন",
                        Color.WHITE,
                        BLUE
                );

        register.setBackground(
                outline(
                        Color.WHITE,
                        BLUE,
                        2,
                        18
                )
        );

        box.addView(register);

        register.setOnClickListener(
                v -> showRegister()
        );

        box.addView(space(10));

        TextView forgot =
                tv(
                        "পাসওয়ার্ড ভুলে গেছেন?",
                        15,
                        BLUE
                );

        forgot.setGravity(
                Gravity.CENTER
        );

        box.addView(forgot);

        forgot.setOnClickListener(
                v -> showForgotPassword()
        );

        scroll.addView(box);

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

    // ============================================================
    // PIN SETUP
    // ============================================================

    private void showPinSetup() {

        LinearLayout root = root();

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setPadding(
                dp(25),
                dp(60),
                dp(25),
                dp(25)
        );

        TextView title =
                tv(
                        "PIN সেট করুন",
                        25,
                        BLUE
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        box.addView(title);

        box.addView(space(25));

        TextView info =
                tv(
                        "অ্যাপ নিরাপদ রাখতে ৮ সংখ্যার PIN সেট করুন।",
                        16,
                        DARK
                );

        info.setGravity(
                Gravity.CENTER
        );

        box.addView(info);

        box.addView(space(18));

        EditText pin =
                pinInput(
                        "৮ সংখ্যার PIN"
                );

        box.addView(pin);

        box.addView(space(15));

        TextView confirm =
                button(
                        "PIN সংরক্ষণ করুন",
                        BLUE,
                        Color.WHITE
                );

        box.addView(confirm);

        confirm.setOnClickListener(
                v -> {

                    String p =
                            pin.getText()
                                    .toString()
                                    .trim();

                    if (p.length() != 8) {

                        pin.setError(
                                "৮ সংখ্যার PIN দিন"
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
                }
        );

        root.addView(
                box,
                new LinearLayout.LayoutParams(
                        -1,
                        -1
                )
        );

        setContentView(root);
    }

    // ============================================================
    // PIN UNLOCK
    // ============================================================

    private void showPinUnlock() {

        LinearLayout root = root();

        LinearLayout box =
                new LinearLayout(this);

        box.setOrientation(
                LinearLayout.VERTICAL
        );

        box.setGravity(
                Gravity.CENTER
        );

        box.setPadding(
                dp(25),
                dp(25),
                dp(25),
                dp(25)
        );

        TextView title =
                tv(
                        "Quick Pay",
                        30,
                        BLUE
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        box.addView(title);

        box.addView(space(20));

        TextView info =
                tv(
                        "আপনার PIN দিন",
                        19,
                        DARK
                );

        info.setGravity(
                Gravity.CENTER
        );

        box.addView(info);

        box.addView(space(15));

        EditText pin =
                pinInput(
                        "৮ সংখ্যার PIN"
                );

        box.addView(
                pin,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        box.addView(space(15));

        TextView unlock =
                button(
                        "প্রবেশ করুন",
                        BLUE,
                        Color.WHITE
                );

        box.addView(unlock);

        unlock.setOnClickListener(
                v -> {

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
                                "PIN সঠিক নয়"
                        );
                    }
                }
        );

        root.addView(box);

        setContentView(root);
    }

    // ============================================================
    // HOME
    // ============================================================

    private void showHome() {

        LinearLayout root = root();

        LinearLayout head =
                header(
                        "Quick Pay",
                        () -> {
                            // Home
                        }
                );

        root.addView(head);

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

        TextView hello =
                tv(
                        "স্বাগতম 👋",
                        22,
                        DARK
                );

        hello.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        content.addView(hello);

        content.addView(space(12));

        LinearLayout balanceCard =
                new LinearLayout(this);

        balanceCard.setOrientation(
                LinearLayout.VERTICAL
        );

        balanceCard.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );

        balanceCard.setBackground(
                bg(
                        Color.WHITE,
                        22
                )
        );

        TextView balanceTitle =
                tv(
                        "CURRENT BALANCE",
                        14,
                        Color.GRAY
                );

        balanceCard.addView(balanceTitle);

        TextView balance =
                tv(
                        "৳ 0.00",
                        28,
                        BLUE
                );

        balance.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        balanceCard.addView(balance);

        content.addView(balanceCard);

        content.addView(space(18));

        TextView serviceTitle =
                tv(
                        "সেবাসমূহ",
                        21,
                        DARK
                );

        serviceTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        content.addView(serviceTitle);

        content.addView(space(12));

        LinearLayout row1 =
                new LinearLayout(this);

        row1.setOrientation(
                LinearLayout.HORIZONTAL
        );

        TextView addBalance =
                button(
                        "অ্যাড ব্যালেন্স",
                        BLUE,
                        Color.WHITE
                );

        TextView mobileBanking =
                button(
                        "মোবাইল ব্যাংকিং",
                        GREEN,
                        Color.WHITE
                );

        row1.addView(
                addBalance,
                new LinearLayout.LayoutParams(
                        0,
                        dp(65),
                        1
                )
        );

        row1.addView(
                spaceHorizontal(8)
        );

        row1.addView(
                mobileBanking,
                new LinearLayout.LayoutParams(
                        0,
                        dp(65),
                        1
                )
        );

        content.addView(row1);

        addBalance.setOnClickListener(
                v -> showWalletDeposit()
        );

        mobileBanking.setOnClickListener(
                v -> showMobileBanking()
        );

        content.addView(space(10));

        TextView recharge =
                button(
                        "মোবাইল রিচার্জ",
                        BLUE,
                        Color.WHITE
                );

        content.addView(recharge);

        recharge.setOnClickListener(
                v -> showMoneyForm(
                        "মোবাইল রিচার্জ"
                )
        );

        content.addView(space(10));

        TextView internet =
                button(
                        "ইন্টারনেট প্যাক",
                        GREEN,
                        Color.WHITE
                );

        content.addView(internet);

        internet.setOnClickListener(
                v -> showMoneyForm(
                        "ইন্টারনেট প্যাক"
                )
        );

        content.addView(space(10));

        TextView history =
                button(
                        "ট্রানজেকশন হিস্টোরি",
                        Color.DKGRAY,
                        Color.WHITE
                );

        content.addView(history);

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

    private Space spaceHorizontal(int width) {

        Space s = new Space(this);

        s.setLayoutParams(
                new LinearLayout.LayoutParams(
                        dp(width),
                        1
                )
        );

        return s;
    }

    // ============================================================
    // WALLET DEPOSIT
    // ============================================================

    private void showWalletDeposit() {

        LinearLayout root = root();

        root.addView(
                header(
                        "অ্যাড ব্যালেন্স",
                        () -> showHome()
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

        TextView intro =
                tv(
                        "আপনার Quick Pay ওয়ালেটে টাকা জমা করুন।",
                        19,
                        DARK
                );

        content.addView(intro);

        content.addView(space(15));

        TextView info =
                tv(
                        "ব্যালেন্স যোগ করার জন্য অটো ডিপোজিট ব্যবহার করুন।",
                        16,
                        DARK
                );

        content.addView(info);

        content.addView(space(20));

        // আগের মোবাইল ব্যাংকিং বাটনের জায়গায়
        // এখন শুধু অটো ডিপোজিট

        TextView autoDeposit =
                button(
                        "অটো ডিপোজিট",
                        BLUE,
                        Color.WHITE
                );

        content.addView(autoDeposit);

        autoDeposit.setOnClickListener(
                v -> showAutoDeposit()
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

        setContentView(root);
    }

    // ============================================================
    // AUTO DEPOSIT
    // ============================================================

    private void showAutoDeposit() {

        LinearLayout root = root();

        root.addView(
                header(
                        "অটো ডিপোজিট",
                        () -> showWalletDeposit()
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
                dp(20),
                dp(25),
                dp(25)
        );

        // --------------------------------------------------------
        // INTRO
        // --------------------------------------------------------

        TextView intro =
                tv(
                        "অটো ডিপোজিটের মাধ্যমে Quick Pay ব্যালেন্সে টাকা যোগ করুন।",
                        19,
                        DARK
                );

        intro.setPadding(
                dp(8),
                dp(5),
                dp(8),
                dp(5)
        );

        content.addView(intro);

        content.addView(space(12));

        TextView demo =
                tv(
                        "এটি বর্তমানে ডেমো মোডে আছে। পরে API সংযুক্ত করা যাবে।",
                        16,
                        DARK
                );

        content.addView(demo);

        content.addView(space(15));

        // --------------------------------------------------------
        // PAYMENT METHOD TITLE
        // --------------------------------------------------------

        TextView methodTitle =
                tv(
                        "পেমেন্ট মাধ্যম",
                        18,
                        BLUE
                );

        methodTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        content.addView(methodTitle);

        content.addView(space(8));

        // --------------------------------------------------------
        // PROVIDER CARDS
        // --------------------------------------------------------

        addCompactProvider(
                content,
                "bKash • পার্সোনাল",
                COMPANY_BKASH
        );

        addCompactProvider(
                content,
                "Nagad • পার্সোনাল",
                COMPANY_NAGAD
        );

        addCompactProvider(
                content,
                "Rocket • পার্সোনাল",
                COMPANY_ROCKET
        );

        addCompactProvider(
                content,
                "Upay • পার্সোনাল",
                COMPANY_UPAY
        );

        content.addView(space(8));

        // --------------------------------------------------------
        // BALANCE TYPE
        // --------------------------------------------------------

        TextView balanceTitle =
                tv(
                        "ব্যালেন্সের ধরন সিলেক্ট করুন",
                        17,
                        BLUE
                );

        balanceTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        content.addView(balanceTitle);

        content.addView(space(8));

        LinearLayout balanceRow =
                new LinearLayout(this);

        balanceRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        TextView mainBalance =
                balanceTypeButton(
                        "Main Balance",
                        "সর্বনিম্ন ৫০ টাকা"
                );

        TextView driveBalance =
                balanceTypeButton(
                        "Drive Balance",
                        "সর্বনিম্ন ৫০০ টাকা"
                );

        balanceRow.addView(
                mainBalance,
                new LinearLayout.LayoutParams(
                        0,
                        dp(90),
                        1
                )
        );

        balanceRow.addView(
                spaceHorizontal(8)
        );

        balanceRow.addView(
                driveBalance,
                new LinearLayout.LayoutParams(
                        0,
                        dp(90),
                        1
                )
        );

        content.addView(balanceRow);

        final String[] selectedBalance =
                {"Main Balance"};

        mainBalance.setBackground(
                outline(
                        Color.rgb(230, 242, 252),
                        BLUE,
                        2,
                        18
                )
        );

        mainBalance.setOnClickListener(
                v -> {

                    selectedBalance[0] =
                            "Main Balance";

                    mainBalance.setBackground(
                            outline(
                                    Color.rgb(230, 242, 252),
                                    BLUE,
                                    2,
                                    18
                            )
                    );

                    driveBalance.setBackground(
                            bg(
                                    Color.rgb(245, 246, 248),
                                    18
                            )
                    );
                }
        );

        driveBalance.setOnClickListener(
                v -> {

                    selectedBalance[0] =
                            "Drive Balance";

                    driveBalance.setBackground(
                            outline(
                                    Color.rgb(230, 242, 252),
                                    BLUE,
                                    2,
                                    18
                            )
                    );

                    mainBalance.setBackground(
                            bg(
                                    Color.rgb(245, 246, 248),
                                    18
                            )
                    );
                }
        );

        content.addView(space(12));

        // --------------------------------------------------------
        // SENT FROM NUMBER
        // --------------------------------------------------------

        TextView sentFromTitle =
                tv(
                        "যে নম্বর থেকে টাকা পাঠিয়েছেন",
                        17,
                        BLUE
                );

        sentFromTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        content.addView(sentFromTitle);

        content.addView(space(6));

        EditText sentFrom =
                phoneInput(
                        "যে নম্বর থেকে টাকা পাঠিয়েছেন"
                );

        content.addView(
                sentFrom,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        content.addView(space(10));

        // --------------------------------------------------------
        // TRANSACTION ID
        // --------------------------------------------------------

        TextView trxTitle =
                tv(
                        "Transaction ID",
                        17,
                        BLUE
                );

        trxTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        content.addView(trxTitle);

        content.addView(space(6));

        EditText trx =
                textInput(
                        "TrxID (ট্রানজেকশন আইডি)"
                );

        content.addView(
                trx,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        content.addView(space(10));

        // --------------------------------------------------------
        // AMOUNT
        // --------------------------------------------------------

        TextView amountTitle =
                tv(
                        "টাকার পরিমাণ",
                        17,
                        BLUE
                );

        amountTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        content.addView(amountTitle);

        content.addView(space(6));

        EditText amount =
                numberInput(
                        "টাকার পরিমাণ (BDT)"
                );

        content.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        content.addView(space(10));

        // --------------------------------------------------------
        // SCREENSHOT / PROOF
        // --------------------------------------------------------

        TextView proofTitle =
                tv(
                        "পেমেন্টের প্রমাণ",
                        17,
                        BLUE
                );

        proofTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        content.addView(proofTitle);

        content.addView(space(6));

        LinearLayout fileBox =
                new LinearLayout(this);

        fileBox.setOrientation(
                LinearLayout.HORIZONTAL
        );

        fileBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        fileBox.setPadding(
                dp(15),
                dp(5),
                dp(5),
                dp(5)
        );

        fileBox.setBackground(
                outline(
                        Color.WHITE,
                        Color.rgb(225, 225, 225),
                        1,
                        18
                )
        );

        selectedFileText =
                tv(
                        "কোনো ফাইল নির্বাচন করা হয়নি",
                        14,
                        Color.GRAY
                );

        fileBox.addView(
                selectedFileText,
                new LinearLayout.LayoutParams(
                        0,
                        dp(50),
                        1
                )
        );

        TextView chooseFile =
                button(
                        "ফাইল নির্বাচন",
                        BLUE,
                        Color.WHITE
                );

        chooseFile.setTextSize(14);

        fileBox.addView(
                chooseFile,
                new LinearLayout.LayoutParams(
                        dp(135),
                        dp(48)
                )
        );

        chooseFile.setOnClickListener(
                v -> pickImage()
        );

        content.addView(fileBox);

        content.addView(space(15));

        // --------------------------------------------------------
        // SUBMIT
        // --------------------------------------------------------

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
                        dp(62)
                )
        );

        submit.setOnClickListener(
                v -> {

                    String from =
                            sentFrom.getText()
                                    .toString()
                                    .trim();

                    String transaction =
                            trx.getText()
                                    .toString()
                                    .trim();

                    String amountText =
                            amount.getText()
                                    .toString()
                                    .trim();

                    if (from.isEmpty()) {

                        sentFrom.setError(
                                "যে নম্বর থেকে টাকা পাঠিয়েছেন সেটি দিন"
                        );

                        return;
                    }

                    if (transaction.isEmpty()) {

                        trx.setError(
                                "Transaction ID দিন"
                        );

                        return;
                    }

                    if (amountText.isEmpty()) {

                        amount.setError(
                                "টাকার পরিমাণ দিন"
                        );

                        return;
                    }

                    double amountValue;

                    try {

                        amountValue =
                                Double.parseDouble(
                                        amountText
                                );

                    } catch (Exception e) {

                        amount.setError(
                                "সঠিক টাকার পরিমাণ দিন"
                        );

                        return;
                    }

                    double minimum =
                            selectedBalance[0]
                                    .equals("Drive Balance")
                                    ? 500
                                    : 50;

                    if (amountValue < minimum) {

                        amount.setError(
                                selectedBalance[0]
                                        + " এর জন্য সর্বনিম্ন "
                                        + minimum
                                        + " টাকা"
                        );

                        return;
                    }

                    showDepositConfirmation(
                            selectedBalance[0],
                            from,
                            transaction,
                            amountText
                    );
                }
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

        setContentView(root);
    }

    // ============================================================
    // COMPACT PROVIDER CARD
    // ============================================================

    private void addCompactProvider(
            LinearLayout parent,
            String name,
            String number
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(15),
                dp(10),
                dp(10),
                dp(10)
        );

        card.setBackground(
                bg(
                        Color.WHITE,
                        18
                )
        );

        LinearLayout top =
                new LinearLayout(this);

        top.setOrientation(
                LinearLayout.HORIZONTAL
        );

        top.setGravity(
                Gravity.CENTER_VERTICAL
        );

        TextView title =
                tv(
                        name,
                        17,
                        BLUE
                );

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        top.addView(
                title,
                new LinearLayout.LayoutParams(
                        0,
                        dp(40),
                        1
                )
        );

        TextView copy =
                button(
                        "কপি",
                        BLUE,
                        Color.WHITE
                );

        copy.setTextSize(14);

        top.addView(
                copy,
                new LinearLayout.LayoutParams(
                        dp(80),
                        dp(42)
                )
        );

        card.addView(top);

        LinearLayout numberRow =
                new LinearLayout(this);

        numberRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        numberRow.setGravity(
                Gravity.CENTER_VERTICAL
        );

        TextView numberText =
                tv(
                        number,
                        17,
                        DARK
                );

        numberRow.addView(
                numberText,
                new LinearLayout.LayoutParams(
                        0,
                        dp(38),
                        1
                )
        );

        card.addView(numberRow);

        copy.setOnClickListener(
                v -> {

                    ClipboardManager clipboard =
                            (ClipboardManager)
                                    getSystemService(
                                            CLIPBOARD_SERVICE
                                    );

                    ClipData clip =
                            ClipData.newPlainText(
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
                }
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(95)
                );

        params.setMargins(
                0,
                dp(5),
                0,
                dp(5)
        );

        parent.addView(
                card,
                params
        );
    }

    // ============================================================
    // BALANCE TYPE BUTTON
    // ============================================================

    private TextView balanceTypeButton(
            String title,
            String subtitle
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
                dp(5),
                dp(5),
                dp(5),
                dp(5)
        );

        box.setBackground(
                bg(
                        Color.rgb(245, 246, 248),
                        18
                )
        );

        TextView t =
                tv(
                        title,
                        15,
                        DARK
                );

        t.setGravity(
                Gravity.CENTER
        );

        t.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        box.addView(t);

        TextView s =
                tv(
                        subtitle,
                        12,
                        Color.GRAY
                );

        s.setGravity(
                Gravity.CENTER
        );

        box.addView(s);

        return box;
    }

    // ============================================================
    // PICK IMAGE
    // ============================================================

    private void pickImage() {

        Intent intent =
                new Intent(
                        Intent.ACTION_GET_CONTENT
                );

        intent.setType(
                "image/*"
        );

        intent.addCategory(
                Intent.CATEGORY_OPENABLE
        );

        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "ছবি নির্বাচন করুন"
                ),
                PICK_IMAGE
        );
    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            Intent data
    ) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        if (
                requestCode == PICK_IMAGE &&
                resultCode == RESULT_OK &&
                data != null
        ) {

            Uri uri =
                    data.getData();

            if (uri != null) {

                selectedFileUri =
                        uri.toString();

                if (selectedFileText != null) {

                    selectedFileText.setText(
                            "ফাইল নির্বাচন করা হয়েছে"
                    );

                    selectedFileText.setTextColor(
                            BLUE
                    );
                }
            }
        }
    }

    // ============================================================
    // DEPOSIT CONFIRMATION
    // ============================================================

    private void showDepositConfirmation(
            String balanceType,
            String from,
            String transaction,
            String amount
    ) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle(
                "ডিপোজিট রিকোয়েস্ট"
        );

        builder.setMessage(
                "ব্যালেন্স: "
                        + balanceType
                        + "\n\n"
                        + "যে নম্বর থেকে: "
                        + from
                        + "\n\n"
                        + "Transaction ID: "
                        + transaction
                        + "\n\n"
                        + "টাকার পরিমাণ: ৳"
                        + amount
                        + "\n\n"
                        + "রিকোয়েস্টটি সাবমিট করা হবে।"
        );

        builder.setNegativeButton(
                "বাতিল",
                null
        );

        builder.setPositiveButton(
                "সাবমিট",
                (dialog, which) -> {

                    Toast.makeText(
                            this,
                            "ডিপোজিট রিকোয়েস্ট সাবমিট হয়েছে।",
                            Toast.LENGTH_LONG
                    ).show();

                    showHome();
                }
        );

        builder.show();
    }

    // ============================================================
    // MOBILE BANKING
    // ============================================================

    private void showMobileBanking() {

        LinearLayout root = root();

        root.addView(
                header(
                        "মোবাইল ব্যাংকিং",
                        () -> showHome()
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
                dp(25)
        );

        TextView notice =
                tv(
                        "মোবাইল ব্যাংকিংয়ের মাধ্যমে লেনদেন করুন।",
                        17,
                        DARK
                );

        content.addView(notice);

        content.addView(space(15));

        addProviderRow(
                content,
                "bKash • বিকাশ",
                COMPANY_BKASH
        );

        addProviderRow(
                content,
                "Nagad • নগদ",
                COMPANY_NAGAD
        );

        addProviderRow(
                content,
                "Rocket • রকেট",
                COMPANY_ROCKET
        );

        addProviderRow(
                content,
                "Upay • উপায়",
                COMPANY_UPAY
        );

        content.addView(space(15));

        TextView send =
                button(
                        "সেন্ড মানি",
                        BLUE,
                        Color.WHITE
                );

        content.addView(send);

        send.setOnClickListener(
                v -> showMoneyForm(
                        "সেন্ড মানি"
                )
        );

        content.addView(space(10));

        TextView cashout =
                button(
                        "ক্যাশ আউট",
                        GREEN,
                        Color.WHITE
                );

        content.addView(cashout);

        cashout.setOnClickListener(
                v -> showMoneyForm(
                        "ক্যাশ আউট"
                )
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

        setContentView(root);
    }

    // ============================================================
    // OLD PROVIDER ROW
    // ============================================================

    private void addProviderRow(
            LinearLayout parent,
            String name,
            String number
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(18),
                dp(14),
                dp(18),
                dp(14)
        );

        card.setBackground(
                bg(
                        Color.WHITE,
                        20
                )
        );

        TextView title =
                tv(
                        name,
                        18,
                        BLUE
                );

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        card.addView(title);

        card.addView(space(5));

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row.setGravity(
                Gravity.CENTER_VERTICAL
        );

        TextView num =
                tv(
                        number,
                        17,
                        DARK
                );

        row.addView(
                num,
                new LinearLayout.LayoutParams(
                        0,
                        dp(48),
                        1
                )
        );

        TextView copy =
                button(
                        "কপি",
                        BLUE,
                        Color.WHITE
                );

        row.addView(
                copy,
                new LinearLayout.LayoutParams(
                        dp(80),
                        dp(48)
                )
        );

        card.addView(row);

        copy.setOnClickListener(
                v -> {

                    ClipboardManager clipboard =
                            (ClipboardManager)
                                    getSystemService(
                                            CLIPBOARD_SERVICE
                                    );

                    clipboard.setPrimaryClip(
                            ClipData.newPlainText(
                                    "number",
                                    number
                            )
                    );

                    Toast.makeText(
                            this,
                            "নম্বর কপি হয়েছে",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        params.setMargins(
                0,
                dp(5),
                0,
                dp(5)
        );

        parent.addView(
                card,
                params
        );
    }

    // ============================================================
    // MONEY FORM
    // ============================================================

    private void showMoneyForm(
            String type
    ) {

        LinearLayout root = root();

        root.addView(
                header(
                        type,
                        () -> showMobileBanking()
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
                dp(20),
                dp(20),
                dp(20),
                dp(25)
        );

        TextView title =
                tv(
                        type,
                        23,
                        BLUE
                );

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        content.addView(title);

        content.addView(space(18));

        EditText phone =
                phoneInput(
                        "মোবাইল নম্বর"
                );

        content.addView(phone);

        content.addView(space(10));

        EditText amount =
                numberInput(
                        "টাকার পরিমাণ"
                );

        content.addView(amount);

        content.addView(space(10));

        EditText reference =
                textInput(
                        "Reference (ঐচ্ছিক)"
                );

        content.addView(reference);

        content.addView(space(18));

        TextView submit =
                button(
                        "সাবমিট করুন",
                        BLUE,
                        Color.WHITE
                );

        content.addView(submit);

        submit.setOnClickListener(
                v -> {

                    String p =
                            phone.getText()
                                    .toString()
                                    .trim();

                    String a =
                            amount.getText()
                                    .toString()
                                    .trim();

                    if (p.isEmpty()) {

                        phone.setError(
                                "মোবাইল নম্বর দিন"
                        );

                        return;
                    }

                    if (a.isEmpty()) {

                        amount.setError(
                                "টাকার পরিমাণ দিন"
                        );

                        return;
                    }

                    new AlertDialog.Builder(this)
                            .setTitle(
                                    "নিশ্চিত করুন"
                            )
                            .setMessage(
                                    type
                                            + "\n\nনম্বর: "
                                            + p
                                            + "\n\nটাকা: ৳"
                                            + a
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
                }
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

        setContentView(root);
    }

    // ============================================================
    // REGISTER
    // ============================================================

    private void showRegister() {

        LinearLayout root = root();

        root.addView(
                header(
                        "রেজিস্টার",
                        () -> showLogin()
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
                dp(25)
        );

        EditText name =
                textInput(
                        "আপনার নাম"
                );

        content.addView(name);

        content.addView(space(12));

        EditText phone =
                phoneInput(
                        "মোবাইল নম্বর"
                );

        content.addView(phone);

        content.addView(space(12));

        EditText password =
                passwordInput(
                        "৬ সংখ্যার পাসওয়ার্ড"
                );

        content.addView(password);

        content.addView(space(18));

        TextView register =
                button(
                        "রেজিস্টার করুন",
                        BLUE,
                        Color.WHITE
                );

        content.addView(register);

        register.setOnClickListener(
                v -> {

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

                    if (p.isEmpty()) {

                        phone.setError(
                                "মোবাইল নম্বর দিন"
                        );

                        return;
                    }

                    if (pass.length() != 6) {

                        password.setError(
                                "৬ সংখ্যার পাসওয়ার্ড দিন"
                        );

                        return;
                    }

                    pref.edit()
                            .putBoolean(
                                    "logged_in",
                                    true
                            )
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
                            .apply();

                    showPinSetup();
                }
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

        setContentView(root);
    }

    // ============================================================
    // FORGOT PASSWORD
    // ============================================================

    private void showForgotPassword() {

        LinearLayout root = root();

        root.addView(
                header(
                        "পাসওয়ার্ড পরিবর্তন",
                        () -> showLogin()
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
                dp(25)
        );

        EditText phone =
                phoneInput(
                        "মোবাইল নম্বর"
                );

        content.addView(phone);

        content.addView(space(12));

        EditText newPass =
                passwordInput(
                        "নতুন ৬ সংখ্যার পাসওয়ার্ড"
                );

        content.addView(newPass);

        content.addView(space(18));

        TextView save =
                button(
                        "পাসওয়ার্ড পরিবর্তন করুন",
                        BLUE,
                        Color.WHITE
                );

        content.addView(save);

        save.setOnClickListener(
                v -> {

                    String p =
                            phone.getText()
                                    .toString()
                                    .trim();

                    String pass =
                            newPass.getText()
                                    .toString()
                                    .trim();

                    if (p.isEmpty()) {

                        phone.setError(
                                "মোবাইল নম্বর দিন"
                        );

                        return;
                    }

                    if (pass.length() != 6) {

                        newPass.setError(
                                "৬ সংখ্যার পাসওয়ার্ড দিন"
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
                            "পাসওয়ার্ড পরিবর্তন হয়েছে",
                            Toast.LENGTH_LONG
                    ).show();

                    showLogin();
                }
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

    private static final String COMPANY_BKASH = "01799999000";
private static final String COMPANY_NAGAD = "01XXXXXXXXX";
private static final String COMPANY_ROCKET = "01XXXXXXXXX";
private static final String COMPANY_UPAY = "01XXXXXXXXX";
    @Override
    public void onBackPressed() {

        private static final String COMPANY_BKASH = "017XXXXXXXX";
private static final String COMPANY_NAGAD = "018XXXXXXXX";
private static final String COMPANY_ROCKET = "019XXXXXXXX";
private static final String COMPANY_UPAY = "016XXXXXXXX";
        showHome();
    }
}
