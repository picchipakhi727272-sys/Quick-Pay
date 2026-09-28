package com.quickpay.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.*;

public class MainActivity extends Activity {

    private static final int BLUE = Color.rgb(8, 96, 190);
    private static final int GREEN = Color.rgb(0, 92, 68);
    private static final int YELLOW = Color.rgb(255, 190, 25);
    private static final int DARK = Color.rgb(35, 35, 35);

    private SharedPreferences pref;

    private String selectedMobileProvider = "বিকাশ";
    private String selectedAccountType = "পার্সোনাল";
    private String selectedRechargeOperator = "GP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences("quick_pay", MODE_PRIVATE);

        if (pref.getBoolean("logged_in", false)) {
            if (pref.getBoolean("pin_set", false)) {
                showPinUnlock();
            } else {
                showPinSetup();
            }
        } else {
            showLogin();
        }
    }

    // =========================
    // BASIC HELPERS
    // =========================

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    private TextView tv(String text, float size, int color) {
        TextView t = new TextView(this);
        t.setText(text);
        t.setTextSize(size);
        t.setTextColor(color);
        t.setGravity(Gravity.CENTER_VERTICAL);
        return t;
    }

    private GradientDrawable bg(int color, float radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp((int) radius));
        return g;
    }

    private GradientDrawable outline(int color, int strokeColor, float radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp((int) radius));
        g.setStroke(dp(1), strokeColor);
        return g;
    }

    private Space space(int h) {
        Space s = new Space(this);
        s.setLayoutParams(new LinearLayout.LayoutParams(
                1, dp(h)
        ));
        return s;
    }

    private Button button(String text, int color) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(15);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER);
        b.setBackground(bg(color, 12));
        b.setPadding(dp(10), dp(8), dp(10), dp(8));

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                );
        p.setMargins(dp(8), dp(5), dp(8), dp(5));
        b.setLayoutParams(p);

        return b;
    }

    private EditText input(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextSize(15);
        e.setTextColor(DARK);
        e.setHintTextColor(Color.GRAY);
        e.setSingleLine(true);
        e.setPadding(dp(15), 0, dp(15), 0);
        e.setBackground(outline(Color.WHITE, Color.LTGRAY, 10));

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(52)
                );
        p.setMargins(dp(8), dp(5), dp(8), dp(5));
        e.setLayoutParams(p);

        return e;
    }

    private EditText pinInput(String hint) {
        EditText e = input(hint);
        e.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );
        return e;
    }

    private TextView titleText(String text) {
        TextView t = tv(text, 20, DARK);
        t.setTypeface(null, android.graphics.Typeface.BOLD);
        t.setGravity(Gravity.CENTER);
        return t;
    }

    // =========================
    // LOGIN
    // =========================

    private void showLogin() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(18), dp(20), dp(18), dp(20));
        root.setBackgroundColor(Color.rgb(245, 247, 250));

        TextView logo = tv("💳", 55, BLUE);
        logo.setGravity(Gravity.CENTER);

        TextView title = tv("Quick Pay", 30, BLUE);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView sub = tv("নিরাপদ ডিজিটাল সেবা", 15, Color.GRAY);
        sub.setGravity(Gravity.CENTER);

        EditText mobile = input("মোবাইল নম্বর");
        mobile.setInputType(InputType.TYPE_CLASS_PHONE);

        EditText password = input("পাসওয়ার্ড");
        password.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        Button login = button("লগইন করুন", BLUE);

        TextView register = tv(
                "নতুন অ্যাকাউন্ট খুলুন",
                15,
                BLUE
        );
        register.setGravity(Gravity.CENTER);
        register.setPadding(0, dp(12), 0, dp(12));

        TextView forgot = tv(
                "পাসওয়ার্ড ভুলে গেছেন?",
                14,
                Color.GRAY
        );
        forgot.setGravity(Gravity.CENTER);

        root.addView(logo,
                new LinearLayout.LayoutParams(-1, dp(75)));
        root.addView(title,
                new LinearLayout.LayoutParams(-1, dp(45)));
        root.addView(sub,
                new LinearLayout.LayoutParams(-1, dp(35)));

        root.addView(space(15));
        root.addView(mobile);
        root.addView(password);
        root.addView(login);
        root.addView(register);
        root.addView(forgot);

        setContentView(root);

        login.setOnClickListener(v -> {

            String m = mobile.getText().toString().trim();
            String p = password.getText().toString().trim();

            if (m.isEmpty()) {
                mobile.setError("মোবাইল নম্বর দিন");
                return;
            }

            if (p.isEmpty()) {
                password.setError("পাসওয়ার্ড দিন");
                return;
            }

            pref.edit()
                    .putBoolean("logged_in", true)
                    .apply();

            if (!pref.getBoolean("pin_set", false)) {
                showPinSetup();
            } else {
                showPinUnlock();
            }
        });

        register.setOnClickListener(v -> showRegister());
        forgot.setOnClickListener(v -> showForgotPassword());
    }

    // =========================
    // PIN SETUP
    // =========================

    private void showPinSetup() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(18), dp(20), dp(18), dp(20));
        root.setBackgroundColor(Color.rgb(245, 247, 250));

        TextView title = titleText("PIN সেট করুন");
        TextView sub = tv(
                "অ্যাপটি নিরাপদ রাখতে ৪ সংখ্যার PIN দিন",
                14,
                Color.GRAY
        );
        sub.setGravity(Gravity.CENTER);

        EditText pin = pinInput("৪ সংখ্যার PIN");
        EditText confirm = pinInput("PIN আবার দিন");

        Button save = button("PIN সংরক্ষণ করুন", BLUE);

        root.addView(title,
                new LinearLayout.LayoutParams(-1, dp(55)));
        root.addView(sub,
                new LinearLayout.LayoutParams(-1, dp(45)));

        root.addView(pin);
        root.addView(confirm);
        root.addView(save);

        setContentView(root);

        save.setOnClickListener(v -> {

            String p1 = pin.getText().toString();
            String p2 = confirm.getText().toString();

            if (p1.length() != 4) {
                pin.setError("৪ সংখ্যার PIN দিন");
                return;
            }

            if (!p1.equals(p2)) {
                confirm.setError("PIN মিলছে না");
                return;
            }

            pref.edit()
                    .putString("pin", p1)
                    .putBoolean("pin_set", true)
                    .apply();

            showHome();
        });
    }

    // =========================
    // PIN UNLOCK
    // =========================

    private void showPinUnlock() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(dp(18), dp(20), dp(18), dp(20));
        root.setBackgroundColor(Color.rgb(245, 247, 250));

        TextView logo = tv("🔐", 50, BLUE);
        logo.setGravity(Gravity.CENTER);

        TextView title = titleText("Quick Pay");
        TextView sub = tv(
                "আপনার PIN দিন",
                15,
                Color.GRAY
        );
        sub.setGravity(Gravity.CENTER);

        EditText pin = pinInput("৪ সংখ্যার PIN");
        Button unlock = button("আনলক করুন", BLUE);

        root.addView(logo,
                new LinearLayout.LayoutParams(-1, dp(75)));
        root.addView(title,
                new LinearLayout.LayoutParams(-1, dp(45)));
        root.addView(sub,
                new LinearLayout.LayoutParams(-1, dp(40)));
        root.addView(pin);
        root.addView(unlock);

        setContentView(root);

        unlock.setOnClickListener(v -> {

            String saved = pref.getString("pin", "");
            String entered = pin.getText().toString();

            if (saved.equals(entered)) {
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
    }

    // =========================
    // HOME
    // =========================

    private void showHome() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(245, 247, 250));

        // GREEN HEADER
        LinearLayout header = new LinearLayout(this);
        header.setOrientation(LinearLayout.VERTICAL);
        header.setGravity(Gravity.CENTER);
        header.setPadding(dp(15), dp(18), dp(15), dp(18));
        header.setBackground(bg(GREEN, 0));

        TextView brand = tv("💳 Quick Pay", 25, Color.WHITE);
        brand.setGravity(Gravity.CENTER);
        brand.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView name = tv(
                "স্বাগতম, Quick Pay User",
                15,
                Color.WHITE
        );
        name.setGravity(Gravity.CENTER);

        TextView mainBalance = tv(
                "Main Balance ৳ ১২,৫০০",
                17,
                Color.WHITE
        );
        mainBalance.setGravity(Gravity.CENTER);

        TextView driveBalance = tv(
                "Drive Balance ৳ ১৮০",
                15,
                Color.WHITE
        );
        driveBalance.setGravity(Gravity.CENTER);

        header.addView(brand,
                new LinearLayout.LayoutParams(-1, dp(42)));
        header.addView(name,
                new LinearLayout.LayoutParams(-1, dp(30)));
        header.addView(mainBalance,
                new LinearLayout.LayoutParams(-1, dp(30)));
        header.addView(driveBalance,
                new LinearLayout.LayoutParams(-1, dp(28)));

        root.addView(header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(180)
                ));

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(10), dp(12), dp(10), dp(20));

        TextView services = tv(
                "সেবাসমূহ",
                20,
                DARK
        );
        services.setTypeface(null,
                android.graphics.Typeface.BOLD);
        services.setPadding(dp(8), dp(5), dp(8), dp(8));

        content.addView(services);

        LinearLayout grid = new LinearLayout(this);
        grid.setOrientation(LinearLayout.VERTICAL);

        LinearLayout row1 = new LinearLayout(this);
        row1.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout row2 = new LinearLayout(this);
        row2.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout row3 = new LinearLayout(this);
        row3.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout addBalance =
                serviceBox("👛\nঅ্যাড\nব্যালেন্স");

        LinearLayout mobileBanking =
                serviceBox("💵\nমোবাইল\nব্যাংকিং");

        LinearLayout bankTransfer =
                serviceBox("🏦\nব্যাংক\nট্রান্সফার");

        LinearLayout recharge =
                serviceBox("📱\nমোবাইল\nরিচার্জ");

        LinearLayout internet =
                serviceBox("🌐\nইন্টারনেট\nপ্যাক");

        LinearLayout history =
                serviceBox("📜\nলেনদেন\nইতিহাস");

        LinearLayout bonus =
                serviceBox("🎁\nবোনাস/\nকমিশন");

        LinearLayout customer =
                serviceBox("☎️\nকাস্টমার\nকেয়ার");

        addToRow(row1, addBalance);
        addToRow(row1, mobileBanking);
        addToRow(row1, bankTransfer);

        addToRow(row2, recharge);
        addToRow(row2, internet);
        addToRow(row2, history);

        addToRow(row3, bonus);
        addToRow(row3, customer);

        grid.addView(row1);
        grid.addView(row2);
        grid.addView(row3);

        content.addView(grid);

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

        addBalance.setOnClickListener(v ->
                showAddBalance());

        mobileBanking.setOnClickListener(v ->
                showMobileBanking());

        bankTransfer.setOnClickListener(v ->
                showBankTransfer());

        recharge.setOnClickListener(v ->
                showMobileRecharge());

        internet.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "ইন্টারনেট প্যাক সেবা শীঘ্রই যুক্ত হবে",
                        Toast.LENGTH_SHORT
                ).show());

        history.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "লেনদেন ইতিহাস শীঘ্রই যুক্ত হবে",
                        Toast.LENGTH_SHORT
                ).show());

        bonus.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "বোনাস/কমিশন সেবা শীঘ্রই যুক্ত হবে",
                        Toast.LENGTH_SHORT
                ).show());

        customer.setOnClickListener(v ->
                showCustomerCare());
    }

    private LinearLayout serviceBox(String text) {

        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);
        box.setPadding(dp(5), dp(10), dp(5), dp(10));
        box.setBackground(
                outline(Color.WHITE, Color.LTGRAY, 12)
        );

        TextView t = tv(text, 15, DARK);
        t.setGravity(Gravity.CENTER);
        t.setTypeface(null,
                android.graphics.Typeface.BOLD);

        box.addView(t,
                new LinearLayout.LayoutParams(
                        -1,
                        -1
                ));

        return box;
    }

    private void addToRow(
            LinearLayout row,
            LinearLayout box
    ) {
        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        dp(105),
                        1
                );

        p.setMargins(
                dp(4),
                dp(4),
                dp(4),
                dp(4)
        );

        row.addView(box, p);
    }

    // =========================
    // ADD BALANCE
    // =========================

    private void showAddBalance() {

        LinearLayout root = pageRoot(
                "অ্যাড ব্যালেন্স"
        );

        LinearLayout card = cardLayout();

        TextView title = tv(
                "💰 ব্যালেন্স যোগ করুন",
                20,
                DARK
        );
        title.setTypeface(null,
                android.graphics.Typeface.BOLD);

        TextView info = tv(
                "নিচের অপশন থেকে অটো ডিপোজিট নির্বাচন করুন",
                14,
                Color.GRAY
        );

        Button autoDeposit =
                button("⚡ অটো ডিপোজিট", GREEN);

        card.addView(title);
        card.addView(info);
        card.addView(space(10));
        card.addView(autoDeposit);

        root.addView(card);

        setContentView(root);

        autoDeposit.setOnClickListener(v ->
                showAutoDeposit());
    }

    // =========================
    // AUTO DEPOSIT
    // =========================

    private void showAutoDeposit() {

        LinearLayout root = pageRoot(
                "অটো ডিপোজিট"
        );

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(
                LinearLayout.VERTICAL
        );
        content.setPadding(
                dp(8),
                dp(8),
                dp(8),
                dp(20)
        );

        TextView title = tv(
                "অটো ডিপোজিট",
                21,
                DARK
        );
        title.setTypeface(null,
                android.graphics.Typeface.BOLD);

        TextView min = tv(
                "Main Balance-এর জন্য সর্বনিম্ন ৳ ৫০",
                14,
                Color.GRAY
        );

        content.addView(title);
        content.addView(min);
        content.addView(space(8));

        LinearLayout providerRow =
                new LinearLayout(this);

        providerRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        Button bkash = smallProvider(
                "bKash",
                "বিকাশ"
        );

        Button nagad = smallProvider(
                "Nagad",
                "নগদ"
        );

        Button rocket = smallProvider(
                "Rocket",
                "রকেট"
        );

        Button upay = smallProvider(
                "Upay",
                "উপায়"
        );

        providerRow.addView(bkash);
        providerRow.addView(nagad);
        providerRow.addView(rocket);
        providerRow.addView(upay);

        content.addView(providerRow);

        TextView selected =
                tv("নির্বাচিত: বিকাশ", 15, BLUE);
        selected.setPadding(
                dp(8),
                dp(12),
                dp(8),
                dp(5)
        );

        content.addView(selected);

        TextView numberTitle = tv(
                "Personal নম্বর",
                14,
                DARK
        );
        numberTitle.setPadding(
                dp(8), dp(8), dp(8), dp(3)
        );

        content.addView(numberTitle);

        TextView number = tv(
                "",
                17,
                DARK
        );
        number.setGravity(Gravity.CENTER);
        number.setPadding(
                dp(5), dp(8), dp(5), dp(8)
        );
        number.setBackground(
                outline(Color.WHITE, Color.LTGRAY, 10)
        );

        content.addView(number);

        Button copy = button(
                "📋 নম্বর কপি করুন",
                BLUE
        );

        content.addView(copy);

        EditText amount = input(
                "অ্যাড মানির পরিমাণ"
        );
        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        EditText trx = input(
                "Transaction ID"
        );

        content.addView(amount);
        content.addView(trx);

        Button submit = button(
                "রিকোয়েস্ট পাঠান",
                GREEN
        );

        content.addView(submit);

        TextView note = tv(
                "নোট: নম্বর অ্যাডমিন প্যানেল থেকে সেট না করা পর্যন্ত এখানে নম্বর দেখাবে না।",
                13,
                Color.GRAY
        );

        note.setPadding(
                dp(10),
                dp(12),
                dp(10),
                dp(5)
        );

        content.addView(note);

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

        final String[] selectedProvider =
                {"বিকাশ"};

        bkash.setOnClickListener(v -> {
            selectedProvider[0] = "বিকাশ";
            selected.setText("নির্বাচিত: বিকাশ");
            number.setText("");
        });

        nagad.setOnClickListener(v -> {
            selectedProvider[0] = "নগদ";
            selected.setText("নির্বাচিত: নগদ");
            number.setText("");
        });

        rocket.setOnClickListener(v -> {
            selectedProvider[0] = "রকেট";
            selected.setText("নির্বাচিত: রকেট");
            number.setText("");
        });

        upay.setOnClickListener(v -> {
            selectedProvider[0] = "উপায়";
            selected.setText("নির্বাচিত: উপায়");
            number.setText("");
        });

        copy.setOnClickListener(v -> {

            if (number.getText().toString().trim().isEmpty()) {

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
                                    Context.CLIPBOARD_SERVICE
                            );

            cm.setPrimaryClip(
                    android.content.ClipData.newPlainText(
                            "Number",
                            number.getText().toString()
                    )
            );

            Toast.makeText(
                    this,
                    "নম্বর কপি হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();
        });

        submit.setOnClickListener(v -> {

            String a = amount.getText()
                    .toString()
                    .trim();

            String t = trx.getText()
                    .toString()
                    .trim();

            if (a.isEmpty()) {
                amount.setError("পরিমাণ দিন");
                return;
            }

            int value;

            try {
                value = Integer.parseInt(a);
            } catch (Exception e) {
                amount.setError("সঠিক পরিমাণ দিন");
                return;
            }

            if (value < 50) {
                amount.setError(
                        "সর্বনিম্ন ৳ ৫০"
                );
                return;
            }

            if (t.isEmpty()) {
                trx.setError(
                        "Transaction ID দিন"
                );
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("রিকোয়েস্ট নিশ্চিত করুন")
                    .setMessage(
                            "Provider: " +
                                    selectedProvider[0] +
                                    "\nAmount: ৳ " +
                                    value +
                                    "\n\n" +
                                    "এটি একটি ডেমো ডিপোজিট রিকোয়েস্ট। " +
                                    "অ্যাডমিন যাচাই করার পর ব্যালেন্স যোগ হবে।"
                    )
                    .setNegativeButton(
                            "বাতিল",
                            null
                    )
                    .setPositiveButton(
                            "পাঠান",
                            (d, w) -> Toast.makeText(
                                    this,
                                    "ডিপোজিট রিকোয়েস্ট পাঠানো হয়েছে",
                                    Toast.LENGTH_LONG
                            ).show()
                    )
                    .show();
        });
    }

    private Button smallProvider(
            String text,
            String provider
    ) {

        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(12);
        b.setTextColor(DARK);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER);
        b.setBackground(
                outline(
                        Color.WHITE,
                        Color.LTGRAY,
                        10
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        dp(50),
                        1
                );

        p.setMargins(
                dp(2),
                dp(2),
                dp(2),
                dp(2)
        );

        b.setLayoutParams(p);

        return b;
    }

    // =========================
    // MOBILE BANKING
    // =========================

    private void showMobileBanking() {

        LinearLayout root = pageRoot(
                "মোবাইল ব্যাংকিং"
        );

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(
                LinearLayout.VERTICAL
        );
        content.setPadding(
                dp(8),
                dp(8),
                dp(8),
                dp(20)
        );

        TextView heading = tv(
                "মোবাইল ব্যাংকিং",
                21,
                DARK
        );
        heading.setTypeface(null,
                android.graphics.Typeface.BOLD);

        content.addView(heading);

        LinearLayout providers =
                new LinearLayout(this);

        providers.setOrientation(
                LinearLayout.HORIZONTAL
        );

        Button bkash = smallProvider(
                "bKash",
                "বিকাশ"
        );

        Button nagad = smallProvider(
                "Nagad",
                "নগদ"
        );

        Button rocket = smallProvider(
                "Rocket",
                "রকেট"
        );

        Button upay = smallProvider(
                "Upay",
                "উপায়"
        );

        providers.addView(bkash);
        providers.addView(nagad);
        providers.addView(rocket);
        providers.addView(upay);

        content.addView(providers);

        TextView providerName =
                tv(
                        "নির্বাচিত: বিকাশ",
                        15,
                        BLUE
                );

        providerName.setPadding(
                dp(8),
                dp(12),
                dp(8),
                dp(5)
        );

        content.addView(providerName);

        TextView accountLabel = tv(
                "অ্যাকাউন্ট টাইপ",
                14,
                DARK
        );

        accountLabel.setPadding(
                dp(8),
                dp(10),
                dp(8),
                dp(3)
        );

        content.addView(accountLabel);

        LinearLayout accountRow =
                new LinearLayout(this);

        accountRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        Button personal =
                smallProvider(
                        "পার্সোনাল",
                        "পার্সোনাল"
                );

        Button agent =
                smallProvider(
                        "এজেন্ট",
                        "এজেন্ট"
                );

        accountRow.addView(personal);
        accountRow.addView(agent);

        content.addView(accountRow);

        EditText mobile =
                input("+88 মোবাইল নম্বর");

        mobile.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        content.addView(mobile);

        EditText amount =
                input("টাকার পরিমাণ");

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        content.addView(amount);

        TextView quickTitle = tv(
                "দ্রুত Amount নির্বাচন করুন",
                14,
                DARK
        );

        quickTitle.setPadding(
                dp(8),
                dp(8),
                dp(8),
                dp(3)
        );

        content.addView(quickTitle);

        LinearLayout quick1 =
                new LinearLayout(this);

        quick1.setOrientation(
                LinearLayout.HORIZONTAL
        );

        Button q1000 =
                quickAmount("৳ ১,০০০");

        Button q10000 =
                quickAmount("৳ ১০,০০০");

        Button q20000 =
                quickAmount("৳ ২০,০০০");

        Button q50000 =
                quickAmount("৳ ৫০,০০০");

        quick1.addView(q1000);
        quick1.addView(q10000);
        quick1.addView(q20000);
        quick1.addView(q50000);

        content.addView(quick1);

        Button send =
                button(
                        "টাকা পাঠান →",
                        GREEN
                );

        content.addView(send);

        TextView note = tv(
                "সর্বনিম্ন ৳ ৫০০। API সংযুক্ত না থাকলে এটি Demo Request হিসেবে কাজ করবে।",
                13,
                Color.GRAY
        );

        note.setPadding(
                dp(10),
                dp(10),
                dp(10),
                dp(5)
        );

        content.addView(note);

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

        final String[] provider =
                {"বিকাশ"};

        final String[] account =
                {"পার্সোনাল"};

        bkash.setOnClickListener(v -> {
            provider[0] = "বিকাশ";
            providerName.setText(
                    "নির্বাচিত: বিকাশ"
            );
        });

        nagad.setOnClickListener(v -> {
            provider[0] = "নগদ";
            providerName.setText(
                    "নির্বাচিত: নগদ"
            );
        });

        rocket.setOnClickListener(v -> {
            provider[0] = "রকেট";
            providerName.setText(
                    "নির্বাচিত: রকেট"
            );
        });

        upay.setOnClickListener(v -> {
            provider[0] = "উপায়";
            providerName.setText(
                    "নির্বাচিত: উপায়"
            );
        });

        personal.setOnClickListener(v -> {
            account[0] = "পার্সোনাল";
            Toast.makeText(
                    this,
                    "পার্সোনাল নির্বাচন করা হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();
        });

        agent.setOnClickListener(v -> {
            account[0] = "এজেন্ট";
            Toast.makeText(
                    this,
                    "এজেন্ট নির্বাচন করা হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();
        });

        q1000.setOnClickListener(v ->
                amount.setText("1000"));

        q10000.setOnClickListener(v ->
                amount.setText("10000"));

        q20000.setOnClickListener(v ->
                amount.setText("20000"));

        q50000.setOnClickListener(v ->
                amount.setText("50000"));

        send.setOnClickListener(v -> {

            String number =
                    mobile.getText()
                            .toString()
                            .trim();

            String money =
                    amount.getText()
                            .toString()
                            .trim();

            if (!number.matches("01[0-9]{9}")) {
                mobile.setError(
                        "১১ সংখ্যার সঠিক মোবাইল নম্বর দিন"
                );
                return;
            }

            if (money.isEmpty()) {
                amount.setError(
                        "টাকার পরিমাণ দিন"
                );
                return;
            }

            int value;

            try {
                value = Integer.parseInt(money);
            } catch (Exception e) {
                amount.setError(
                        "সঠিক পরিমাণ দিন"
                );
                return;
            }

            if (value < 500) {
                amount.setError(
                        "সর্বনিম্ন ৳ ৫০০"
                );
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("পেমেন্ট নিশ্চিত করুন")
                    .setMessage(
                            "Provider: " +
                                    provider[0] +
                                    "\nAccount: " +
                                    account[0] +
                                    "\nNumber: " +
                                    number +
                                    "\nAmount: ৳ " +
                                    value +
                                    "\n\n" +
                                    "এটি একটি Demo Request। " +
                                    "আসল টাকা পাঠানোর জন্য সংশ্লিষ্ট Payment API প্রয়োজন।"
                    )
                    .setNegativeButton(
                            "বাতিল",
                            null
                    )
                    .setPositiveButton(
                            "নিশ্চিত করুন",
                            (d, w) -> Toast.makeText(
                                    this,
                                    "মোবাইল ব্যাংকিং রিকোয়েস্ট গ্রহণ করা হয়েছে",
                                    Toast.LENGTH_LONG
                            ).show()
                    )
                    .show();
        });
    }

    // =========================
    // BANK TRANSFER
    // =========================

    private void showBankTransfer() {

        LinearLayout root = pageRoot(
                "ব্যাংক ট্রান্সফার"
        );

        ScrollView scroll = new ScrollView(this);

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(8),
                dp(8),
                dp(8),
                dp(20)
        );

        TextView title = tv(
                "🏦 ব্যাংক ট্রান্সফার",
                21,
                DARK
        );

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        content.addView(title);

        TextView info = tv(
                "ব্যাংক অ্যাকাউন্টে টাকা পাঠানোর তথ্য দিন",
                14,
                Color.GRAY
        );

        content.addView(info);

        EditText bankName =
                input("ব্যাংকের নাম");

        EditText accountName =
                input("অ্যাকাউন্টের নাম");

        EditText accountNumber =
                input("অ্যাকাউন্ট নম্বর");

        accountNumber.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        EditText amount =
                input("টাকার পরিমাণ");

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        content.addView(bankName);
        content.addView(accountName);
        content.addView(accountNumber);
        content.addView(amount);

        TextView quickTitle =
                tv(
                        "দ্রুত Amount",
                        14,
                        DARK
                );

        quickTitle.setPadding(
                dp(8),
                dp(8),
                dp(8),
                dp(3)
        );

        content.addView(quickTitle);

        LinearLayout quick =
                new LinearLayout(this);

        quick.setOrientation(
                LinearLayout.HORIZONTAL
        );

        Button q500 =
                quickAmount("৳ ৫০০");

        Button q1000 =
                quickAmount("৳ ১,০০০");

        Button q5000 =
                quickAmount("৳ ৫,০০০");

        Button q10000 =
                quickAmount("৳ ১০,০০০");

        quick.addView(q500);
        quick.addView(q1000);
        quick.addView(q5000);
        quick.addView(q10000);

        content.addView(quick);

        Button transfer =
                button(
                        "টাকা পাঠান →",
                        GREEN
                );

        content.addView(transfer);

        TextView note =
                tv(
                        "সর্বনিম্ন ব্যাংক ট্রান্সফার ৳ ৫০০। API ছাড়া এটি Demo Request হিসেবে কাজ করবে।",
                        13,
                        Color.GRAY
                );

        note.setPadding(
                dp(10),
                dp(10),
                dp(10),
                dp(5)
        );

        content.addView(note);

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

        q500.setOnClickListener(v ->
                amount.setText("500"));

        q1000.setOnClickListener(v ->
                amount.setText("1000"));

        q5000.setOnClickListener(v ->
                amount.setText("5000"));

        q10000.setOnClickListener(v ->
                amount.setText("10000"));

        transfer.setOnClickListener(v -> {

            String bank =
                    bankName.getText()
                            .toString()
                            .trim();

            String name =
                    accountName.getText()
                            .toString()
                            .trim();

            String acc =
                    accountNumber.getText()
                            .toString()
                            .trim();

            String money =
                    amount.getText()
                            .toString()
                            .trim();

            if (bank.isEmpty()) {
                bankName.setError(
                        "ব্যাংকের নাম দিন"
                );
                return;
            }

            if (name.isEmpty()) {
                accountName.setError(
                        "অ্যাকাউন্টের নাম দিন"
                );
                return;
            }

            if (acc.isEmpty()) {
                accountNumber.setError(
                        "অ্যাকাউন্ট নম্বর দিন"
                );
                return;
            }

            if (money.isEmpty()) {
                amount.setError(
                        "টাকার পরিমাণ দিন"
                );
                return;
            }

            int value;

            try {
                value = Integer.parseInt(money);
            } catch (Exception e) {
                amount.setError(
                        "সঠিক পরিমাণ দিন"
                );
                return;
            }

            if (value < 500) {
                amount.setError(
                        "সর্বনিম্ন ৳ ৫০০"
                );
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle(
                            "ব্যাংক ট্রান্সফার নিশ্চিত করুন"
                    )
                    .setMessage(
                            "Bank: " + bank +
                                    "\nAccount Name: " + name +
                                    "\nAccount No: " + acc +
                                    "\nAmount: ৳ " + value +
                                    "\n\n" +
                                    "এটি একটি Demo Request। " +
                                    "আসল ব্যাংক ট্রান্সফারের জন্য Bank API সংযুক্ত করতে হবে।"
                    )
                    .setNegativeButton(
                            "বাতিল",
                            null
                    )
                    .setPositiveButton(
                            "নিশ্চিত করুন",
                            (d, w) -> Toast.makeText(
                                    this,
                                    "ব্যাংক ট্রান্সফার রিকোয়েস্ট গ্রহণ করা হয়েছে",
                                    Toast.LENGTH_LONG
                            ).show()
                    )
                    .show();
        });
    }

    // =========================
    // MOBILE RECHARGE
    // =========================

    private void showMobileRecharge() {

        LinearLayout root = pageRoot(
                "মোবাইল রিচার্জ"
        );

        ScrollView scroll = new ScrollView(this);

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(8),
                dp(8),
                dp(8),
                dp(20)
        );

        TextView title =
                tv(
                        "📱 মোবাইল রিচার্জ",
                        21,
                        DARK
                );

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        content.addView(title);

        LinearLayout operators =
                new LinearLayout(this);

        operators.setOrientation(
                LinearLayout.HORIZONTAL
        );

        Button gp =
                smallProvider(
                        "GP",
                        "GP"
                );

        Button robi =
                smallProvider(
                        "Robi",
                        "Robi"
                );

        Button airtel =
                smallProvider(
                        "Airtel",
                        "Airtel"
                );

        Button bl =
                smallProvider(
                        "Banglalink",
                        "Banglalink"
                );

        Button teletalk =
                smallProvider(
                        "Teletalk",
                        "Teletalk"
                );

        operators.addView(gp);
        operators.addView(robi);
        operators.addView(airtel);
        operators.addView(bl);
        operators.addView(teletalk);

        content.addView(operators);

        TextView selected =
                tv(
                        "নির্বাচিত: GP",
                        15,
                        BLUE
                );

        selected.setPadding(
                dp(8),
                dp(12),
                dp(8),
                dp(5)
        );

        content.addView(selected);

        TextView typeTitle =
                tv(
                        "সিমের ধরন",
                        14,
                        DARK
                );

        typeTitle.setPadding(
                dp(8),
                dp(8),
                dp(8),
                dp(3)
        );

        content.addView(typeTitle);

        LinearLayout simType =
                new LinearLayout(this);

        simType.setOrientation(
                LinearLayout.HORIZONTAL
        );

        Button prepaid =
                smallProvider(
                        "Prepaid",
                        "Prepaid"
                );

        Button postpaid =
                smallProvider(
                        "Postpaid",
                        "Postpaid"
                );

        simType.addView(prepaid);
        simType.addView(postpaid);

        content.addView(simType);

        EditText number =
                input(
                        "+88 মোবাইল নম্বর"
                );

        number.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        content.addView(number);

        EditText amount =
                input(
                        "রিচার্জের পরিমাণ"
                );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        content.addView(amount);

        TextView quickTitle =
                tv(
                        "দ্রুত Amount নির্বাচন করুন",
                        14,
                        DARK
                );

        quickTitle.setPadding(
                dp(8),
                dp(8),
                dp(8),
                dp(3)
        );

        content.addView(quickTitle);

        LinearLayout quick1 =
                new LinearLayout(this);

        quick1.setOrientation(
                LinearLayout.HORIZONTAL
        );

        Button q20 =
                quickAmount("৳ ২০");

        Button q50 =
                quickAmount("৳ ৫০");

        Button q100 =
                quickAmount("৳ ১০০");

        Button q200 =
                quickAmount("৳ ২০০");

        quick1.addView(q20);
        quick1.addView(q50);
        quick1.addView(q100);
        quick1.addView(q200);

        content.addView(quick1);

        LinearLayout quick2 =
                new LinearLayout(this);

        quick2.setOrientation(
                LinearLayout.HORIZONTAL
        );

        Button q500 =
                quickAmount("৳ ৫০০");

        Button q1000 =
                quickAmount("৳ ১,০০০");

        quick2.addView(q500);
        quick2.addView(q1000);

        content.addView(quick2);

        Button recharge =
                button(
                        "রিচার্জ করুন →",
                        GREEN
                );

        content.addView(recharge);

        TextView note =
                tv(
                        "সর্বনিম্ন ৳ ২০। API সংযুক্ত না থাকলে এটি Demo Recharge Request হিসেবে কাজ করবে।",
                        13,
                        Color.GRAY
                );

        note.setPadding(
                dp(10),
                dp(10),
                dp(10),
                dp(5)
        );

        content.addView(note);

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

        final String[] operator =
                {"GP"};

        final String[] sim =
                {"Prepaid"};

        gp.setOnClickListener(v -> {
            operator[0] = "GP";
            selected.setText(
                    "নির্বাচিত: GP"
            );
        });

        robi.setOnClickListener(v -> {
            operator[0] = "Robi";
            selected.setText(
                    "নির্বাচিত: Robi"
            );
        });

        airtel.setOnClickListener(v -> {
            operator[0] = "Airtel";
            selected.setText(
                    "নির্বাচিত: Airtel"
            );
        });

        bl.setOnClickListener(v -> {
            operator[0] = "Banglalink";
            selected.setText(
                    "নির্বাচিত: Banglalink"
            );
        });

        teletalk.setOnClickListener(v -> {
            operator[0] = "Teletalk";
            selected.setText(
                    "নির্বাচিত: Teletalk"
            );
        });

        prepaid.setOnClickListener(v -> {
            sim[0] = "Prepaid";
            Toast.makeText(
                    this,
                    "Prepaid নির্বাচন করা হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();
        });

        postpaid.setOnClickListener(v -> {
            sim[0] = "Postpaid";
            Toast.makeText(
                    this,
                    "Postpaid নির্বাচন করা হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();
        });

        q20.setOnClickListener(v ->
                amount.setText("20"));

        q50.setOnClickListener(v ->
                amount.setText("50"));

        q100.setOnClickListener(v ->
                amount.setText("100"));

        q200.setOnClickListener(v ->
                amount.setText("200"));

        q500.setOnClickListener(v ->
                amount.setText("500"));

        q1000.setOnClickListener(v ->
                amount.setText("1000"));

        recharge.setOnClickListener(v -> {

            String phone =
                    number.getText()
                            .toString()
                            .trim();

            String money =
                    amount.getText()
                            .toString()
                            .trim();

            if (!phone.matches(
                    "01[0-9]{9}"
            )) {
                number.setError(
                        "১১ সংখ্যার সঠিক মোবাইল নম্বর দিন"
                );
                return;
            }

            if (money.isEmpty()) {
                amount.setError(
                        "রিচার্জের পরিমাণ দিন"
                );
                return;
            }

            int value;

            try {
                value = Integer.parseInt(money);
            } catch (Exception e) {
                amount.setError(
                        "সঠিক পরিমাণ দিন"
                );
                return;
            }

            if (value < 20) {
                amount.setError(
                        "সর্বনিম্ন ৳ ২০"
                );
                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle(
                            "রিচার্জ নিশ্চিত করুন"
                    )
                    .setMessage(
                            "Operator: " +
                                    operator[0] +
                                    "\nType: " +
                                    sim[0] +
                                    "\nNumber: " +
                                    phone +
                                    "\nAmount: ৳ " +
                                    value +
                                    "\n\n" +
                                    "এটি একটি Demo Recharge Request। " +
                                    "আসল রিচার্জের জন্য Recharge API সংযুক্ত করতে হবে।"
                    )
                    .setNegativeButton(
                            "বাতিল",
                            null
                    )
                    .setPositiveButton(
                            "রিচার্জ করুন",
                            (d, w) -> Toast.makeText(
                                    this,
                                    "রিচার্জ রিকোয়েস্ট গ্রহণ করা হয়েছে",
                                    Toast.LENGTH_LONG
                            ).show()
                    )
                    .show();
        });
    }

    private Button quickAmount(String text) {

        Button b = new Button(this);

        b.setText(text);
        b.setTextSize(12);
        b.setTextColor(DARK);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER);
        b.setBackground(
                outline(
                        Color.WHITE,
                        Color.LTGRAY,
                        10
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        0,
                        dp(50),
                        1
                );

        p.setMargins(
                dp(2),
                dp(3),
                dp(2),
                dp(3)
        );

        b.setLayoutParams(p);

        return b;
    }

    // =========================
    // OLD MONEY FORM
    // =========================

    private void showMoneyForm(
            String title,
            String hint
    ) {

        LinearLayout root =
                pageRoot(title);

        LinearLayout card =
                cardLayout();

        EditText amount =
                input(hint);

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        Button submit =
                button(
                        "সাবমিট করুন",
                        GREEN
                );

        card.addView(amount);
        card.addView(submit);

        root.addView(card);

        setContentView(root);

        submit.setOnClickListener(v -> {

            if (amount.getText()
                    .toString()
                    .trim()
                    .isEmpty()) {

                amount.setError(
                        "পরিমাণ দিন"
                );

                return;
            }

            Toast.makeText(
                    this,
                    "রিকোয়েস্ট গ্রহণ করা হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    // =========================
    // REGISTER
    // =========================

    private void showRegister() {

        LinearLayout root =
                pageRoot("রেজিস্টার");

        LinearLayout card =
                cardLayout();

        EditText name =
                input("আপনার নাম");

        EditText mobile =
                input("মোবাইল নম্বর");

        mobile.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        EditText password =
                input("পাসওয়ার্ড");

        password.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        EditText confirm =
                input("পাসওয়ার্ড আবার দিন");

        confirm.setInputType(
                InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        Button register =
                button(
                        "রেজিস্টার করুন",
                        BLUE
                );

        card.addView(name);
        card.addView(mobile);
        card.addView(password);
        card.addView(confirm);
        card.addView(register);

        root.addView(card);

        setContentView(root);

        register.setOnClickListener(v -> {

            String n =
                    name.getText()
                            .toString()
                            .trim();

            String m =
                    mobile.getText()
                            .toString()
                            .trim();

            String p =
                    password.getText()
                            .toString();

            String c =
                    confirm.getText()
                            .toString();

            if (n.isEmpty()) {
                name.setError("নাম দিন");
                return;
            }

            if (!m.matches(
                    "01[0-9]{9}"
            )) {
                mobile.setError(
                        "সঠিক মোবাইল নম্বর দিন"
                );
                return;
            }

            if (p.length() < 4) {
                password.setError(
                        "কমপক্ষে ৪ অক্ষর দিন"
                );
                return;
            }

            if (!p.equals(c)) {
                confirm.setError(
                        "পাসওয়ার্ড মিলছে না"
                );
                return;
            }

            pref.edit()
                    .putBoolean(
                            "logged_in",
                            true
                    )
                    .apply();

            Toast.makeText(
                    this,
                    "রেজিস্ট্রেশন সফল",
                    Toast.LENGTH_SHORT
            ).show();

            showPinSetup();
        });
    }

    // =========================
    // FORGOT PASSWORD
    // =========================

    private void showForgotPassword() {

        LinearLayout root =
                pageRoot(
                        "পাসওয়ার্ড পুনরুদ্ধার"
                );

        LinearLayout card =
                cardLayout();

        EditText mobile =
                input(
                        "মোবাইল নম্বর"
                );

        mobile.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        Button submit =
                button(
                        "OTP পাঠান",
                        BLUE
                );

        card.addView(mobile);
        card.addView(submit);

        root.addView(card);

        setContentView(root);

        submit.setOnClickListener(v -> {

            String m =
                    mobile.getText()
                            .toString()
                            .trim();

            if (!m.matches(
                    "01[0-9]{9}"
            )) {

                mobile.setError(
                        "সঠিক মোবাইল নম্বর দিন"
                );

                return;
            }

            Toast.makeText(
                    this,
                    "OTP রিকোয়েস্ট পাঠানো হয়েছে",
                    Toast.LENGTH_LONG
            ).show();
        });
    }

    // =========================
    // CUSTOMER CARE
    // =========================

    private void showCustomerCare() {

        LinearLayout root =
                pageRoot("কাস্টমার কেয়ার");

        LinearLayout card =
                cardLayout();

        TextView icon =
                tv("☎️", 45, BLUE);

        icon.setGravity(Gravity.CENTER);

        TextView title =
                titleText("Quick Pay Customer Care");

        TextView info =
                tv(
                        "আপনার যেকোনো সমস্যা বা সহায়তার জন্য কাস্টমার কেয়ারে যোগাযোগ করুন।",
                        15,
                        DARK
                );

        info.setGravity(Gravity.CENTER);

        Button call =
                button(
                        "কাস্টমার কেয়ার",
                        GREEN
                );

        card.addView(icon);
        card.addView(title);
        card.addView(info);
        card.addView(call);

        root.addView(card);

        setContentView(root);

        call.setOnClickListener(v ->
                Toast.makeText(
                        this,
                        "কাস্টমার কেয়ার নম্বর অ্যাডমিন প্যানেল থেকে সেট করুন",
                        Toast.LENGTH_LONG
                ).show()
        );
    }

    // =========================
    // PAGE ROOT
    // =========================

    private LinearLayout pageRoot(
            String title
    ) {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.rgb(245, 247, 250)
        );

        // BLUE HEADER
        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.HORIZONTAL
        );

        header.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.setPadding(
                dp(5),
                0,
                dp(10),
                0
        );

        header.setBackground(
                bg(BLUE, 0)
        );

        Button back =
                new Button(this);

        back.setText("‹");
        back.setTextSize(32);
        back.setTextColor(Color.WHITE);
        back.setAllCaps(false);
        back.setGravity(Gravity.CENTER);
        back.setBackgroundColor(
                Color.TRANSPARENT
        );

        header.addView(
                back,
                new LinearLayout.LayoutParams(
                        dp(55),
                        dp(65)
                )
        );

        TextView titleView =
                tv(
                        title,
                        20,
                        Color.WHITE
                );

        titleView.setGravity(
                Gravity.CENTER_VERTICAL
        );

        titleView.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        header.addView(
                titleView,
                new LinearLayout.LayoutParams(
                        0,
                        dp(65),
                        1
                )
        );

        TextView bell =
                tv("🔔", 21, Color.WHITE);

        bell.setGravity(
                Gravity.CENTER
        );

        header.addView(
                bell,
                new LinearLayout.LayoutParams(
                        dp(45),
                        dp(65)
                )
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(65)
                )
        );

        back.setOnClickListener(v ->
                showHome());

        return root;
    }

    // =========================
    // CARD
    // =========================

    private LinearLayout cardLayout() {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(10),
                dp(12),
                dp(10),
                dp(12)
        );

        card.setBackground(
                outline(
                        Color.WHITE,
                        Color.LTGRAY,
                        14
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        p.setMargins(
                dp(10),
                dp(10),
                dp(10),
                dp(10)
        );

        card.setLayoutParams(p);

        return card;
    }

    // =========================
    // BACK
    // =========================

    @Override
    public void onBackPressed() {
        showHome();
    }
}
