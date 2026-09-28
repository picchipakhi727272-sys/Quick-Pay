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
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    private static final int BLUE = Color.rgb(8, 96, 190);
    private static final int GREEN = Color.rgb(0, 92, 68);
    private static final int YELLOW = Color.rgb(255, 190, 25);
    private static final int DARK = Color.rgb(35, 35, 35);
    private static final int LIGHT = Color.rgb(247, 248, 250);

    private SharedPreferences pref;

    private String selectedDepositMethod = "bKash";
    private String selectedBalanceType = "Main Balance";

    private final String BKASH_NUMBER = "";
    private final String NAGAD_NUMBER = "";
    private final String ROCKET_NUMBER = "";
    private final String UPAY_NUMBER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences("quick_pay", Context.MODE_PRIVATE);

        if (pref.getBoolean("logged_in", false)) {
            showHome();
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

    private GradientDrawable outline(
            int fill,
            int strokeColor,
            int strokeWidth,
            float radius) {

        GradientDrawable g = new GradientDrawable();
        g.setColor(fill);
        g.setCornerRadius(dp((int) radius));
        g.setStroke(dp(strokeWidth), strokeColor);
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

    private EditText input(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextSize(17);
        e.setSingleLine(true);
        e.setTextColor(DARK);
        e.setHintTextColor(Color.rgb(155, 160, 170));
        e.setPadding(dp(20), 0, dp(20), 0);
        e.setBackground(bg(Color.rgb(246, 247, 249), 16));
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

    private Button button(String text, int color) {
        Button b = new Button(this);
        b.setText(text);
        b.setTextSize(18);
        b.setTextColor(Color.WHITE);
        b.setAllCaps(false);
        b.setGravity(Gravity.CENTER);
        b.setPadding(dp(10), 0, dp(10), 0);
        b.setBackground(bg(color, 18));
        return b;
    }

    private TextView headerTitle(String title) {
        TextView t = tv(title, 24, Color.WHITE);
        t.setTypeface(null, android.graphics.Typeface.BOLD);
        t.setGravity(Gravity.CENTER_VERTICAL);
        return t;
    }

    private LinearLayout header(String title, boolean back) {

        LinearLayout h = new LinearLayout(this);
        h.setOrientation(LinearLayout.HORIZONTAL);
        h.setGravity(Gravity.CENTER_VERTICAL);
        h.setPadding(dp(14), 0, dp(14), 0);
        h.setBackground(bg(BLUE, 0));

        if (back) {
            TextView b = tv("‹", 42, Color.WHITE);
            b.setGravity(Gravity.CENTER);
            b.setTypeface(null, android.graphics.Typeface.BOLD);

            LinearLayout.LayoutParams bp =
                    new LinearLayout.LayoutParams(dp(55), dp(70));

            h.addView(b, bp);

            b.setOnClickListener(v -> showHome());
        }

        TextView titleView = headerTitle(title);

        LinearLayout.LayoutParams tp =
                new LinearLayout.LayoutParams(
                        0,
                        dp(70),
                        1
                );

        h.addView(titleView, tp);

        return h;
    }

    private ScrollView baseScroll(LinearLayout content) {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(LIGHT);
        scroll.addView(content);
        return scroll;
    }

    // =========================
    // LOGIN
    // =========================

    private void showLogin() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(24), dp(30), dp(24), dp(24));
        root.setBackgroundColor(LIGHT);

        TextView logo = tv("🦅", 60, BLUE);
        logo.setGravity(Gravity.CENTER);

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(80)
                )
        );

        TextView title = tv(
                "Quick Pay",
                30,
                BLUE
        );
        title.setGravity(Gravity.CENTER);
        title.setTypeface(null, android.graphics.Typeface.BOLD);

        root.addView(title);

        root.addView(space(30));

        EditText mobile = input("মোবাইল নম্বর");
        mobile.setInputType(InputType.TYPE_CLASS_PHONE);

        root.addView(
                mobile,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        root.addView(space(14));

        EditText password = input("পাসওয়ার্ড");
        password.setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        root.addView(
                password,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        root.addView(space(20));

        Button login = button("লগইন", BLUE);

        root.addView(
                login,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        root.addView(space(10));

        Button register = button(
                "নতুন অ্যাকাউন্ট খুলুন",
                GREEN
        );

        root.addView(
                register,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        login.setOnClickListener(v -> {

            if (mobile.getText().toString().trim().isEmpty() ||
                    password.getText().toString().trim().isEmpty()) {

                Toast.makeText(
                        this,
                        "মোবাইল নম্বর ও পাসওয়ার্ড দিন",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            pref.edit()
                    .putBoolean("logged_in", true)
                    .apply();

            showHome();
        });

        register.setOnClickListener(v -> showRegister());

        setContentView(root);
    }

    // =========================
    // REGISTER
    // =========================

    private void showRegister() {

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(30)
        );
        content.setBackgroundColor(LIGHT);

        content.addView(header("রেজিস্টার", true));

        TextView title = tv(
                "নতুন অ্যাকাউন্ট তৈরি করুন",
                22,
                DARK
        );
        title.setTypeface(null, android.graphics.Typeface.BOLD);

        content.addView(title);

        content.addView(space(20));

        EditText name = input("আপনার নাম");
        content.addView(
                name,
                new LinearLayout.LayoutParams(-1, dp(55))
        );

        content.addView(space(12));

        EditText mobile = input("মোবাইল নম্বর");
        mobile.setInputType(InputType.TYPE_CLASS_PHONE);

        content.addView(
                mobile,
                new LinearLayout.LayoutParams(-1, dp(55))
        );

        content.addView(space(12));

        EditText password = input("পাসওয়ার্ড");
        password.setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD
        );

        content.addView(
                password,
                new LinearLayout.LayoutParams(-1, dp(55))
        );

        content.addView(space(20));

        Button create = button(
                "অ্যাকাউন্ট তৈরি করুন",
                BLUE
        );

        content.addView(
                create,
                new LinearLayout.LayoutParams(-1, dp(58))
        );

        create.setOnClickListener(v -> {

            if (name.getText().toString().trim().isEmpty() ||
                    mobile.getText().toString().trim().isEmpty() ||
                    password.getText().toString().trim().isEmpty()) {

                Toast.makeText(
                        this,
                        "সব তথ্য পূরণ করুন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            pref.edit()
                    .putBoolean("logged_in", true)
                    .apply();

            Toast.makeText(
                    this,
                    "অ্যাকাউন্ট তৈরি হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();

            showHome();
        });

        setContentView(baseScroll(content));
    }

    // =========================
    // HOME
    // =========================

    private void showHome() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(LIGHT);

        LinearLayout top = new LinearLayout(this);
        top.setOrientation(LinearLayout.VERTICAL);
        top.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );
        top.setBackground(bg(GREEN, 0));

        TextView title = tv(
                "🦅 Quick Pay",
                25,
                Color.WHITE
        );
        title.setTypeface(null, android.graphics.Typeface.BOLD);

        top.addView(title);

        TextView welcome = tv(
                "আপনার বিশ্বস্ত ডিজিটাল সেবা প্ল্যাটফর্ম",
                15,
                Color.WHITE
        );

        top.addView(welcome);

        root.addView(top);

        LinearLayout balance = new LinearLayout(this);
        balance.setOrientation(LinearLayout.HORIZONTAL);
        balance.setPadding(
                dp(12),
                dp(12),
                dp(12),
                dp(12)
        );

        TextView mainBalance = tv(
                "Main Balance\n৳ ১২,৫০০",
                17,
                DARK
        );
        mainBalance.setGravity(Gravity.CENTER);

        mainBalance.setBackground(bg(Color.WHITE, 18));

        TextView driveBalance = tv(
                "Drive Balance\n৳ ১৮০",
                17,
                DARK
        );
        driveBalance.setGravity(Gravity.CENTER);
        driveBalance.setBackground(bg(Color.WHITE, 18));

        LinearLayout.LayoutParams half =
                new LinearLayout.LayoutParams(
                        0,
                        dp(90),
                        1
                );

        half.setMargins(dp(5), 0, dp(5), 0);

        balance.addView(mainBalance, half);
        balance.addView(driveBalance, half);

        root.addView(balance);

        ScrollView scroll = new ScrollView(this);

        LinearLayout services = new LinearLayout(this);
        services.setOrientation(LinearLayout.VERTICAL);
        services.setPadding(
                dp(15),
                dp(5),
                dp(15),
                dp(30)
        );

        TextView serviceTitle = tv(
                "সেবাসমূহ",
                22,
                DARK
        );
        serviceTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        services.addView(serviceTitle);

        services.addView(space(10));

        addHomeService(
                services,
                "💰",
                "অ্যাড ব্যালেন্স",
                "ব্যালেন্স যোগ করুন",
                v -> showAddBalance()
        );

        addHomeService(
                services,
                "📱",
                "মোবাইল ব্যাংকিং",
                "টাকা পাঠান",
                v -> showMobileBanking()
        );

        addHomeService(
                services,
                "📲",
                "মোবাইল রিচার্জ",
                "সকল সিমে রিচার্জ",
                v -> showMoneyForm("মোবাইল রিচার্জ")
        );

        addHomeService(
                services,
                "🌐",
                "ইন্টারনেট প্যাক",
                "ইন্টারনেট অফার",
                v -> showMoneyForm("ইন্টারনেট প্যাক")
        );

        addHomeService(
                services,
                "📜",
                "Transaction History",
                "লেনদেনের হিস্টোরি",
                v -> showHistory()
        );

        addHomeService(
                services,
                "🎁",
                "Bonus / Commission",
                "বোনাস ও কমিশন",
                v -> showBonus()
        );

        addHomeService(
                services,
                "🎧",
                "Customer Care",
                "সাহায্যের জন্য যোগাযোগ করুন",
                v -> showCustomerCare()
        );

        scroll.addView(services);

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
            LinearLayout parent,
            String icon,
            String title,
            String sub,
            View.OnClickListener listener) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(Gravity.CENTER_VERTICAL);
        card.setPadding(
                dp(15),
                dp(12),
                dp(15),
                dp(12)
        );

        card.setBackground(bg(Color.WHITE, 18));

        TextView i = tv(icon, 30, DARK);
        i.setGravity(Gravity.CENTER);

        card.addView(
                i,
                new LinearLayout.LayoutParams(
                        dp(55),
                        dp(65)
                )
        );

        LinearLayout texts = new LinearLayout(this);
        texts.setOrientation(LinearLayout.VERTICAL);

        TextView t = tv(title, 18, DARK);
        t.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView s = tv(sub, 14, Color.GRAY);

        texts.addView(t);
        texts.addView(s);

        card.addView(
                texts,
                new LinearLayout.LayoutParams(
                        0,
                        -2,
                        1
                )
        );

        TextView arrow = tv("›", 32, BLUE);
        arrow.setGravity(Gravity.CENTER);

        card.addView(
                arrow,
                new LinearLayout.LayoutParams(
                        dp(35),
                        dp(60)
                )
        );

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(82)
                );

        cp.setMargins(0, dp(7), 0, dp(7));

        parent.addView(card, cp);

        card.setOnClickListener(listener);
    }

    // =========================
    // ADD BALANCE
    // =========================

    private void showAddBalance() {

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setBackgroundColor(LIGHT);

        content.addView(
                header("অ্যাড ব্যালেন্স", true)
        );

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(
                dp(20),
                dp(22),
                dp(20),
                dp(22)
        );
        card.setBackground(bg(Color.WHITE, 24));

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        cp.setMargins(
                dp(18),
                dp(20),
                dp(18),
                dp(20)
        );

        TextView title = tv(
                "আপনার ব্যালেন্স যোগ করুন",
                23,
                DARK
        );

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        card.addView(title);

        card.addView(space(10));

        TextView info = tv(
                "নিচের অপশন ব্যবহার করে Quick Pay ব্যালেন্সে টাকা যোগ করতে পারবেন।",
                17,
                Color.DKGRAY
        );

        info.setPadding(0, 0, 0, dp(10));

        card.addView(info);

        Button auto = button(
                "অটো ডিপোজিট",
                BLUE
        );

        card.addView(
                auto,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        auto.setOnClickListener(
                v -> showAutoDeposit()
        );

        content.addView(card, cp);

        setContentView(
                baseScroll(content)
        );
    }

    // =========================
    // AUTO DEPOSIT
    // =========================

    private void showAutoDeposit() {

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setBackgroundColor(LIGHT);

        content.addView(
                header("অটো ডিপোজিট", true)
        );

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(
                dp(22),
                dp(25),
                dp(22),
                dp(25)
        );
        card.setBackground(
                bg(Color.WHITE, 26)
        );

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        cp.setMargins(
                dp(24),
                dp(30),
                dp(24),
                dp(20)
        );

        TextView intro = tv(
                "অটো ডিপোজিটের মাধ্যমে Quick Pay ব্যালেন্সে টাকা যোগ করুন।",
                19,
                DARK
        );

        intro.setLineSpacing(0, 1.15f);

        card.addView(intro);

        card.addView(space(22));

        TextView demo = tv(
                "এটি এখন ডেমো মোডে আছে। পরে API সংযুক্ত করা যাবে।",
                17,
                Color.DKGRAY
        );

        demo.setLineSpacing(0, 1.15f);

        card.addView(demo);

        card.addView(space(22));

        TextView methodTitle = tv(
                "পেমেন্ট মাধ্যম",
                19,
                BLUE
        );

        methodTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        card.addView(methodTitle);

        card.addView(space(7));

        Spinner spinner = new Spinner(this);

        String[] methods = {
                "bKash",
                "Nagad",
                "Rocket",
                "Upay"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        methods
                );

        spinner.setAdapter(adapter);

        if (selectedDepositMethod.equals("Nagad"))
            spinner.setSelection(1);
        else if (selectedDepositMethod.equals("Rocket"))
            spinner.setSelection(2);
        else if (selectedDepositMethod.equals("Upay"))
            spinner.setSelection(3);
        else
            spinner.setSelection(0);

        card.addView(
                spinner,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        spinner.setOnItemSelectedListener(
                new android.widget.AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            android.widget.AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        selectedDepositMethod =
                                methods[position];
                    }

                    @Override
                    public void onNothingSelected(
                            android.widget.AdapterView<?> parent) {
                    }
                }
        );

        card.addView(space(15));

        EditText amount = input(
                "টাকার পরিমাণ"
        );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        card.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        card.addView(space(20));

        Button start = button(
                "অটো ডিপোজিট চালু করুন",
                BLUE
        );

        card.addView(
                start,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        start.setOnClickListener(v -> {

            String value =
                    amount.getText()
                            .toString()
                            .trim();

            if (value.isEmpty()) {

                Toast.makeText(
                        this,
                        "টাকার পরিমাণ লিখুন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            double money;

            try {
                money = Double.parseDouble(value);
            } catch (Exception e) {
                Toast.makeText(
                        this,
                        "সঠিক টাকার পরিমাণ দিন",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            if (money < 50) {

                Toast.makeText(
                        this,
                        "সর্বনিম্ন ৫০ টাকা",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            showDepositRequest(
                    selectedDepositMethod,
                    money
            );
        });

        content.addView(card, cp);

        setContentView(
                baseScroll(content)
        );
    }

    // =========================
    // DEPOSIT REQUEST SCREEN
    // =========================

    private void showDepositRequest(
            String method,
            double amount) {

        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setBackgroundColor(LIGHT);

        content.addView(
                header("অটো ডিপোজিট", true)
        );

        ScrollView scroll =
                new ScrollView(this);

        LinearLayout body =
                new LinearLayout(this);

        body.setOrientation(
                LinearLayout.VERTICAL
        );

        body.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(30)
        );

        // =========================
        // NUMBER CARD
        // =========================

        LinearLayout numberCard =
                new LinearLayout(this);

        numberCard.setOrientation(
                LinearLayout.VERTICAL
        );

        numberCard.setPadding(
                dp(22),
                dp(20),
                dp(22),
                dp(20)
        );

        numberCard.setBackground(
                bg(Color.WHITE, 24)
        );

        TextView numberTitle = tv(
                "💳  আমাদের নাম্বার সমূহ (Personal)",
                20,
                DARK
        );

        numberTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        numberCard.addView(numberTitle);

        numberCard.addView(space(18));

        LinearLayout numberRow =
                new LinearLayout(this);

        numberRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        TextView numberText =
                tv(
                        getProviderNumber(method),
                        17,
                        DARK
                );

        numberRow.addView(
                numberText,
                new LinearLayout.LayoutParams(
                        0,
                        dp(50),
                        1
                )
        );

        Button copy =
                button("Copy", BLUE);

        LinearLayout.LayoutParams copyParams =
                new LinearLayout.LayoutParams(
                        dp(105),
                        dp(50)
                );

        numberRow.addView(
                copy,
                copyParams
        );

        numberCard.addView(numberRow);

        copy.setOnClickListener(v -> {

            String number =
                    getProviderNumber(method);

            if (number.isEmpty()) {

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
                    android.content.ClipData.newPlainText(
                            "Payment Number",
                            number
                    );

            clipboard.setPrimaryClip(clip);

            Toast.makeText(
                    this,
                    "নম্বর কপি হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();
        });

        body.addView(
                numberCard,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        body.addView(space(15));

        // =========================
        // BALANCE CARD
        // =========================

        LinearLayout formCard =
                new LinearLayout(this);

        formCard.setOrientation(
                LinearLayout.VERTICAL
        );

        formCard.setPadding(
                dp(22),
                dp(22),
                dp(22),
                dp(25)
        );

        formCard.setBackground(
                bg(Color.WHITE, 24)
        );

        TextView balanceTitle = tv(
                "ব্যালেন্সের ধরন সিলেক্ট করুন",
                20,
                DARK
        );

        balanceTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        formCard.addView(balanceTitle);

        formCard.addView(space(14));

        LinearLayout typeRow =
                new LinearLayout(this);

        typeRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        TextView main =
                tv(
                        "💳\nMain Balance\nসর্বনিম্ন ৫০ টাকা",
                        16,
                        DARK
                );

        main.setGravity(Gravity.CENTER);
        main.setPadding(
                dp(5),
                dp(10),
                dp(5),
                dp(10)
        );

        TextView drive =
                tv(
                        "💳\nDrive Balance\nসর্বনিম্ন ৫০ টাকা",
                        16,
                        DARK
                );

        drive.setGravity(Gravity.CENTER);
        drive.setPadding(
                dp(5),
                dp(10),
                dp(5),
                dp(10)
        );

        LinearLayout.LayoutParams typeParams =
                new LinearLayout.LayoutParams(
                        0,
                        dp(145),
                        1
                );

        typeParams.setMargins(
                dp(3),
                0,
                dp(3),
                0
        );

        typeRow.addView(main, typeParams);
        typeRow.addView(drive, typeParams);

        formCard.addView(typeRow);

        selectBalance(main, drive);

        main.setOnClickListener(v -> {
            selectedBalanceType =
                    "Main Balance";
            selectBalance(main, drive);
        });

        drive.setOnClickListener(v -> {
            selectedBalanceType =
                    "Drive Balance";
            selectBalance(main, drive);
        });

        formCard.addView(space(14));

        EditText sender =
                input(
                        "যে নাম্বার থেকে টাকা পাঠিয়েছেন"
                );

        sender.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        formCard.addView(
                sender,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        formCard.addView(space(12));

        EditText trx =
                input(
                        "TrxID (ট্রানজাকশন আইডি)"
                );

        formCard.addView(
                trx,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        formCard.addView(space(12));

        EditText amountInput =
                input(
                        "টাকার পরিমাণ (BDT)"
                );

        amountInput.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        if (amount > 0) {
            amountInput.setText(
                    String.valueOf(
                            (long) amount
                    )
            );
        }

        formCard.addView(
                amountInput,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        formCard.addView(space(14));

        TextView fileText =
                tv(
                        "Choose File     No file chosen",
                        16,
                        Color.DKGRAY
                );

        fileText.setGravity(
                Gravity.CENTER_VERTICAL
        );

        fileText.setPadding(
                dp(20),
                0,
                dp(10),
                0
        );

        fileText.setBackground(
                outline(
                        Color.WHITE,
                        Color.rgb(220, 220, 220),
                        1,
                        15
                )
        );

        formCard.addView(
                fileText,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        formCard.addView(space(18));

        Button submit =
                button(
                        "Submit Request",
                        Color.rgb(28, 160, 235)
                );

        formCard.addView(
                submit,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        submit.setOnClickListener(v -> {

            String senderNumber =
                    sender.getText()
                            .toString()
                            .trim();

            String trxId =
                    trx.getText()
                            .toString()
                            .trim();

            String moneyText =
                    amountInput.getText()
                            .toString()
                            .trim();

            if (senderNumber.isEmpty()) {

                Toast.makeText(
                        this,
                        "যে নাম্বার থেকে টাকা পাঠিয়েছেন সেটি দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (trxId.isEmpty()) {

                Toast.makeText(
                        this,
                        "TrxID দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (moneyText.isEmpty()) {

                Toast.makeText(
                        this,
                        "টাকার পরিমাণ দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            double money;

            try {
                money =
                        Double.parseDouble(
                                moneyText
                        );
            } catch (Exception e) {

                Toast.makeText(
                        this,
                        "সঠিক টাকার পরিমাণ দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (money < 50) {

                Toast.makeText(
                        this,
                        "সর্বনিম্ন ৫০ টাকা জমা দিতে পারবেন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("Request Submitted")
                    .setMessage(
                            "Payment Method: " + method +
                            "\nBalance: " +
                            selectedBalanceType +
                            "\nAmount: ৳ " +
                            money +
                            "\n\nআপনার ডিপোজিট রিকোয়েস্ট গ্রহণ করা হয়েছে।\nঅ্যাডমিন যাচাই করার পর ব্যালেন্স যোগ হবে।"
                    )
                    .setPositiveButton(
                            "ঠিক আছে",
                            null
                    )
                    .show();
        });

        body.addView(formCard);

        scroll.addView(body);

        content.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(content);
    }

    private void selectBalance(
            TextView main,
            TextView drive) {

        if (selectedBalanceType.equals(
                "Main Balance")) {

            main.setBackground(
                    outline(
                            Color.rgb(235, 244, 250),
                            BLUE,
                            2,
                            18
                    )
            );

            drive.setBackground(
                    bg(
                            Color.rgb(246, 247, 249),
                            18
                    )
            );

        } else {

            drive.setBackground(
                    outline(
                            Color.rgb(235, 244, 250),
                            BLUE,
                            2,
                            18
                    )
            );

            main.setBackground(
                    bg(
                            Color.rgb(246, 247, 249),
                            18
                    )
            );
        }
    }

    private String getProviderNumber(
            String method) {

        if (method.equals("bKash"))
            return BKASH_NUMBER;

        if (method.equals("Nagad"))
            return NAGAD_NUMBER;

        if (method.equals("Rocket"))
            return ROCKET_NUMBER;

        if (method.equals("Upay"))
            return UPAY_NUMBER;

        return "";
    }

    // =========================
    // MOBILE BANKING
    // =========================

    private String selectedMobileProvider =
            "বিকাশ";

    private String selectedAccountType =
            "পার্সোনাল";

    private void showMobileBanking() {

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setBackgroundColor(LIGHT);

        content.addView(
                header("মোবাইল ব্যাংকিং", true)
        );

        LinearLayout body =
                new LinearLayout(this);

        body.setOrientation(
                LinearLayout.VERTICAL
        );

        body.setPadding(
                dp(15),
                dp(15),
                dp(15),
                dp(25)
        );

        HorizontalScrollView providerScroll =
                new HorizontalScrollView(this);

        providerScroll.setHorizontalScrollBarEnabled(false);

        LinearLayout providers =
                new LinearLayout(this);

        providers.setOrientation(
                        LinearLayout.HORIZONTAL
        );

        addMobileProvider(
                providers,
                "বিকাশ"
        );

        addMobileProvider(
                providers,
                "নগদ"
        );

        addMobileProvider(
                providers,
                "রকেট"
        );

        addMobileProvider(
                providers,
                "উপায়"
        );

        providerScroll.addView(providers);

        body.addView(providerScroll);

        body.addView(space(12));

        LinearLayout accountCard =
                new LinearLayout(this);

        accountCard.setOrientation(
                LinearLayout.VERTICAL
        );

        accountCard.setPadding(
                dp(18),
                dp(18),
                dp(18),
                dp(22)
        );

        accountCard.setBackground(
                bg(Color.WHITE, 24)
        );

        TextView accountTitle =
                tv(
                        "একাউন্ট ধরন",
                        20,
                        DARK
                );

        accountTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        accountCard.addView(accountTitle);

        accountCard.addView(space(12));

        LinearLayout accountRow =
                new LinearLayout(this);

        accountRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        TextView personal =
                tv(
                        "পার্সোনাল",
                        17,
                        DARK
                );

        personal.setGravity(Gravity.CENTER);

        TextView agent =
                tv(
                        "এজেন্ট",
                        17,
                        DARK
                );

        agent.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams accountParams =
                new LinearLayout.LayoutParams(
                        0,
                        dp(55),
                        1
                );

        accountParams.setMargins(
                dp(4),
                0,
                dp(4),
                0
        );

        accountRow.addView(
                personal,
                accountParams
        );

        accountRow.addView(
                agent,
                accountParams
        );

        accountCard.addView(accountRow);

        accountSelect(
                personal,
                agent
        );

        personal.setOnClickListener(v -> {

            selectedAccountType =
                    "পার্সোনাল";

            accountSelect(
                    personal,
                    agent
            );
        });

        agent.setOnClickListener(v -> {

            selectedAccountType =
                    "এজেন্ট";

            accountSelect(
                    personal,
                    agent
            );
        });

        accountCard.addView(space(20));

        TextView mobileTitle =
                tv(
                        "মোবাইল নম্বর",
                        19,
                        DARK
                );

        mobileTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        accountCard.addView(mobileTitle);

        accountCard.addView(space(8));

        LinearLayout mobileRow =
                new LinearLayout(this);

        mobileRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        TextView prefix =
                tv("+88", 18, DARK);

        prefix.setGravity(Gravity.CENTER);

        prefix.setBackground(
                bg(
                        Color.rgb(242, 243, 245),
                        15
                )
        );

        EditText number =
                input(
                        "মোবাইল নম্বর"
                );

        number.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        mobileRow.addView(
                prefix,
                new LinearLayout.LayoutParams(
                        dp(65),
                        dp(58)
                )
        );

        LinearLayout.LayoutParams np =
                new LinearLayout.LayoutParams(
                        0,
                        dp(58),
                        1
                );

        np.setMargins(
                dp(8),
                0,
                0,
                0
        );

        mobileRow.addView(number, np);

        accountCard.addView(mobileRow);

        accountCard.addView(space(18));

        TextView amountTitle =
                tv(
                        "পরিমাণ লিখুন",
                        19,
                        DARK
                );

        amountTitle.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        accountCard.addView(amountTitle);

        accountCard.addView(space(8));

        EditText amount =
                input(
                        "টাকার পরিমাণ"
                );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        accountCard.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        accountCard.addView(space(12));

        LinearLayout quick =
                new LinearLayout(this);

        quick.setOrientation(
                LinearLayout.HORIZONTAL
        );

        addQuickAmount(
                quick,
                amount,
                "৳ 1,000"
        );

        addQuickAmount(
                quick,
                amount,
                "৳ 10,000"
        );

        addQuickAmount(
                quick,
                amount,
                "৳ 20,000"
        );

        addQuickAmount(
                quick,
                amount,
                "৳ 50,000"
        );

        accountCard.addView(quick);

        accountCard.addView(space(20));

        Button send =
                button(
                        "টাকা পাঠান →",
                        BLUE
                );

        accountCard.addView(
                send,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        send.setOnClickListener(v -> {

            String n =
                    number.getText()
                            .toString()
                            .replaceAll(
                                    "[^0-9]",
                                    ""
                            );

            String a =
                    amount.getText()
                            .toString()
                            .trim();

            if (n.length() != 11) {

                Toast.makeText(
                        this,
                        "সঠিক ১১ সংখ্যার মোবাইল নম্বর দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (a.isEmpty()) {

                Toast.makeText(
                        this,
                        "টাকার পরিমাণ লিখুন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            double money;

            try {
                money =
                        Double.parseDouble(a);
            } catch (Exception e) {

                Toast.makeText(
                        this,
                        "সঠিক টাকার পরিমাণ দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (money < 500) {

                Toast.makeText(
                        this,
                        "সর্বনিম্ন ৫০০ টাকা পাঠাতে পারবেন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            new AlertDialog.Builder(this)
                    .setTitle("টাকা পাঠানোর তথ্য")
                    .setMessage(
                            "মাধ্যম: " +
                            selectedMobileProvider +
                            "\nএকাউন্ট: " +
                            selectedAccountType +
                            "\nনম্বর: +88" +
                            n +
                            "\nপরিমাণ: ৳ " +
                            money +
                            "\n\nএটি বর্তমানে ডেমো মোডে আছে। API সংযুক্ত হলে এখান থেকে প্রকৃত লেনদেন করা যাবে।"
                    )
                    .setPositiveButton(
                            "ঠিক আছে",
                            null
                    )
                    .show();
        });

        body.addView(accountCard);

        ScrollView scroll =
                new ScrollView(this);

        scroll.addView(body);

        content.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(content);
    }

    private void addMobileProvider(
            LinearLayout parent,
            String name) {

        TextView p =
                tv(
                        name,
                        17,
                        DARK
                );

        p.setGravity(Gravity.CENTER);
        p.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        LinearLayout.LayoutParams pp =
                new LinearLayout.LayoutParams(
                        dp(88),
                        dp(62)
                );

        pp.setMargins(
                dp(4),
                0,
                dp(4),
                0
        );

        parent.addView(p, pp);

        if (name.equals(
                selectedMobileProvider)) {

            p.setBackground(
                    outline(
                            Color.rgb(230, 242, 252),
                            BLUE,
                            2,
                            16
                    )
            );
        } else {

            p.setBackground(
                    bg(
                            Color.WHITE,
                            16
                    )
            );
        }

        p.setOnClickListener(v -> {

            selectedMobileProvider =
                    name;

            showMobileBanking();
        });
    }

    private void accountSelect(
            TextView personal,
            TextView agent) {

        if (selectedAccountType.equals(
                "পার্সোনাল")) {

            personal.setBackground(
                    outline(
                            Color.rgb(230, 242, 252),
                            BLUE,
                            2,
                            15
                    )
            );

            agent.setBackground(
                    bg(
                            Color.rgb(246, 247, 249),
                            15
                    )
            );

        } else {

            agent.setBackground(
                    outline(
                            Color.rgb(230, 242, 252),
                            BLUE,
                            2,
                            15
                    )
            );

            personal.setBackground(
                    bg(
                            Color.rgb(246, 247, 249),
                            15
                    )
            );
        }
    }

    private void addQuickAmount(
            LinearLayout parent,
            EditText amount,
            String value) {

        TextView q =
                tv(
                        value,
                        13,
                        DARK
                );

        q.setGravity(Gravity.CENTER);

        q.setBackground(
                bg(
                        Color.rgb(246, 247, 249),
                        12
                )
        );

        LinearLayout.LayoutParams qp =
                new LinearLayout.LayoutParams(
                        0,
                        dp(45),
                        1
                );

        qp.setMargins(
                dp(3),
                0,
                dp(3),
                0
        );

        parent.addView(q, qp);

        q.setOnClickListener(v -> {

            String clean =
                    value.replaceAll(
                            "[^0-9]",
                            ""
                    );

            amount.setText(clean);
        });
    }

    // =========================
    // GENERIC MONEY FORM
    // =========================

    private void showMoneyForm(
            String titleText) {

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setBackgroundColor(LIGHT);

        content.addView(
                header(titleText, true)
        );

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(20),
                dp(22),
                dp(20),
                dp(25)
        );

        card.setBackground(
                bg(Color.WHITE, 24)
        );

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        cp.setMargins(
                dp(18),
                dp(20),
                dp(18),
                dp(20)
        );

        EditText number =
                input("মোবাইল নম্বর");

        number.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        card.addView(
                number,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        card.addView(space(14));

        EditText amount =
                input("টাকার পরিমাণ");

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
        );

        card.addView(
                amount,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(58)
                )
        );

        card.addView(space(20));

        Button submit =
                button(
                        "পরবর্তী",
                        BLUE
                );

        card.addView(
                submit,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        submit.setOnClickListener(v -> {

            if (number.getText()
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

            if (amount.getText()
                    .toString()
                    .trim()
                    .isEmpty()) {

                Toast.makeText(
                        this,
                        "টাকার পরিমাণ দিন",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Toast.makeText(
                    this,
                    "রিকোয়েস্ট প্রস্তুত হয়েছে",
                    Toast.LENGTH_SHORT
            ).show();
        });

        content.addView(card, cp);

        setContentView(
                baseScroll(content)
        );
    }

    // =========================
    // HISTORY
    // =========================

    private void showHistory() {

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setBackgroundColor(LIGHT);

        content.addView(
                header(
                        "Transaction History",
                        true
                )
        );

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );

        card.setBackground(
                bg(Color.WHITE, 22)
        );

        TextView empty =
                tv(
                        "এখনো কোনো লেনদেন নেই।",
                        18,
                        Color.GRAY
                );

        empty.setGravity(Gravity.CENTER);

        card.addView(
                empty,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(120)
                )
        );

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        cp.setMargins(
                dp(18),
                dp(20),
                dp(18),
                0
        );

        content.addView(card, cp);

        setContentView(
                baseScroll(content)
        );
    }

    // =========================
    // BONUS
    // =========================

    private void showBonus() {

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setBackgroundColor(LIGHT);

        content.addView(
                header(
                        "Bonus / Commission",
                        true
                )
        );

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(20),
                dp(22),
                dp(20),
                dp(22)
        );

        card.setBackground(
                bg(Color.WHITE, 22)
        );

        TextView title =
                tv(
                        "🎁 বোনাস ও কমিশন",
                        22,
                        BLUE
                );

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        card.addView(title);

        card.addView(space(15));

        TextView text =
                tv(
                        "আপনার রিচার্জ ও অন্যান্য সেবার কমিশন এখানে দেখা যাবে।",
                        17,
                        DARK
                );

        card.addView(text);

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        cp.setMargins(
                dp(18),
                dp(20),
                dp(18),
                0
        );

        content.addView(card, cp);

        setContentView(
                baseScroll(content)
        );
    }

    // =========================
    // CUSTOMER CARE
    // =========================

    private void showCustomerCare() {

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setBackgroundColor(LIGHT);

        content.addView(
                header(
                        "Customer Care",
                        true
                )
        );

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setGravity(Gravity.CENTER);

        card.setPadding(
                dp(25),
                dp(30),
                dp(25),
                dp(30)
        );

        card.setBackground(
                bg(Color.WHITE, 24)
        );

        TextView icon =
                tv("🎧", 55, BLUE);

        icon.setGravity(Gravity.CENTER);

        card.addView(
                icon,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(75)
                )
        );

        TextView title =
                tv(
                        "Customer Care",
                        24,
                        DARK
                );

        title.setGravity(Gravity.CENTER);

        title.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        card.addView(title);

        card.addView(space(10));

        TextView info =
                tv(
                        "যেকোনো সমস্যায় আমাদের সাথে যোগাযোগ করুন।",
                        16,
                        Color.GRAY
                );

        info.setGravity(Gravity.CENTER);

        card.addView(info);

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        cp.setMargins(
                dp(18),
                dp(20),
                dp(18),
                0
        );

        content.addView(card, cp);

        setContentView(
                baseScroll(content)
        );
    }

    // =========================
    // BACK BUTTON
    // =========================

    @Override
    public void onBackPressed() {
        showHome();
    }
}
