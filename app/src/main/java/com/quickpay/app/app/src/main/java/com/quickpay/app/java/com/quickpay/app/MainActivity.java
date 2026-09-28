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

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getSharedPreferences(
                "quick_pay",
                Context.MODE_PRIVATE
        );

        if (pref.getBoolean("logged_in", false)) {

            String pin = pref.getString("pin", "");

            if (pin.length() == 8) {
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
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
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
        GradientDrawable d = new GradientDrawable();

        d.setColor(color);
        d.setCornerRadius(dp((int) radius));

        return d;
    }

    private GradientDrawable outline(
            int strokeColor,
            int fillColor,
            float radius
    ) {
        GradientDrawable d = new GradientDrawable();

        d.setColor(fillColor);
        d.setCornerRadius(dp((int) radius));
        d.setStroke(dp(1), strokeColor);

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

    private EditText textInput(
            String hint
    ) {
        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(15);
        e.setSingleLine(true);
        e.setPadding(
                dp(14),
                0,
                dp(14),
                0
        );

        e.setBackground(
                outline(
                        Color.LTGRAY,
                        Color.WHITE,
                        10
                )
        );

        e.setLayoutParams(
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        return e;
    }

    private EditText phoneInput() {

        EditText e = textInput(
                "মোবাইল নম্বর"
        );

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
                15,
                textColor
        );

        b.setGravity(Gravity.CENTER);

        b.setTypeface(null, 1);

        b.setBackground(
                bg(
                        background,
                        10
                )
        );

        b.setLayoutParams(
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        return b;
    }

    // =========================
    // LOGIN
    // =========================

    private void showLogin() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setPadding(
                dp(20),
                dp(35),
                dp(20),
                dp(20)
        );

        root.setBackgroundColor(
                Color.WHITE
        );

        TextView title =
                tv(
                        "Quick Pay",
                        28,
                        BLUE
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        root.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        root.addView(
                space(20)
        );

        TextView welcome =
                tv(
                        "লগইন করুন",
                        22,
                        DARK
                );

        welcome.setGravity(
                Gravity.CENTER
        );

        welcome.setTypeface(null, 1);

        root.addView(
                welcome,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        root.addView(
                space(15)
        );

        EditText phone =
                phoneInput();

        root.addView(phone);

        root.addView(
                space(10)
        );

        EditText password =
                passwordInput(
                        "৬ সংখ্যার পাসওয়ার্ড"
                );

        root.addView(password);

        root.addView(
                space(15)
        );

        TextView login =
                button(
                        "লগইন",
                        BLUE,
                        Color.WHITE
                );

        root.addView(login);

        login.setOnClickListener(
                v -> {

                    if (phone.getText()
                            .toString()
                            .trim()
                            .isEmpty()) {

                        phone.setError(
                                "মোবাইল নম্বর দিন"
                        );

                        return;
                    }

                    if (password.getText()
                            .toString()
                            .length() != 6) {

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
                            .apply();

                    showPinSetup();
                }
        );

        root.addView(
                space(12)
        );

        TextView register =
                tv(
                        "নতুন একাউন্ট খুলুন",
                        15,
                        BLUE
                );

        register.setGravity(
                Gravity.CENTER
        );

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

        root.addView(
                space(5)
        );

        TextView forgot =
                tv(
                        "পাসওয়ার্ড ভুলে গেছেন?",
                        14,
                        DARK
                );

        forgot.setGravity(
                Gravity.CENTER
        );

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

        setContentView(root);
    }

    // =========================
    // PIN SETUP
    // =========================

    private void showPinSetup() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        root.setPadding(
                dp(20),
                dp(50),
                dp(20),
                dp(20)
        );

        root.setBackgroundColor(
                Color.WHITE
        );

        TextView title =
                tv(
                        "PIN সেট করুন",
                        24,
                        BLUE
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        root.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        root.addView(
                space(20)
        );

        TextView info =
                tv(
                        "আপনার ৮ সংখ্যার PIN সেট করুন",
                        16,
                        DARK
                );

        info.setGravity(
                Gravity.CENTER
        );

        root.addView(
                info,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        root.addView(
                space(15)
        );

        EditText pin =
                pinInput(
                        "৮ সংখ্যার PIN"
                );

        root.addView(pin);

        root.addView(
                space(10)
        );

        EditText confirm =
                pinInput(
                        "PIN আবার দিন"
                );

        root.addView(confirm);

        root.addView(
                space(15)
        );

        TextView save =
                button(
                        "PIN সংরক্ষণ করুন",
                        BLUE,
                        Color.WHITE
                );

        root.addView(save);

        save.setOnClickListener(
                v -> {

                    String p =
                            pin.getText()
                                    .toString();

                    String c =
                            confirm.getText()
                                    .toString();

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
                }
        );

        setContentView(root);
    }

    // =========================
    // PIN UNLOCK
    // =========================

    private void showPinUnlock() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        root.setPadding(
                dp(20),
                dp(50),
                dp(20),
                dp(20)
        );

        root.setBackgroundColor(
                Color.WHITE
        );

        TextView title =
                tv(
                        "Quick Pay",
                        28,
                        BLUE
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        root.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        root.addView(
                space(20)
        );

        TextView info =
                tv(
                        "আপনার PIN দিন",
                        18,
                        DARK
                );

        info.setGravity(
                Gravity.CENTER
        );

        root.addView(
                info,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        root.addView(
                space(15)
        );

        EditText pin =
                pinInput(
                        "৮ সংখ্যার PIN"
                );

        root.addView(pin);

        root.addView(
                space(15)
        );

        TextView unlock =
                button(
                        "প্রবেশ করুন",
                        BLUE,
                        Color.WHITE
                );

        root.addView(unlock);

        unlock.setOnClickListener(
                v -> {

                    String saved =
                            pref.getString(
                                    "pin",
                                    ""
                            );

                    if (pin.getText()
                            .toString()
                            .equals(saved)) {

                        showHome();

                    } else {

                        pin.setError(
                                "ভুল PIN"
                        );
                    }
                }
        );

        setContentView(root);
    }

    // =========================
    // HOME
    // =========================

    private void showHome() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.WHITE
        );

        // Header
        TextView header =
                tv(
                        "Quick Pay",
                        22,
                        Color.WHITE
                );

        header.setGravity(
                Gravity.CENTER
        );

        header.setTypeface(null, 1);

        header.setBackgroundColor(
                BLUE
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(15),
                dp(15),
                dp(15),
                dp(15)
        );

        root.addView(
                content,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        TextView balance =
                tv(
                        "বর্তমান ব্যালেন্স\n৳ 0.00",
                        20,
                        Color.WHITE
                );

        balance.setGravity(
                Gravity.CENTER
        );

        balance.setTypeface(null, 1);

        balance.setBackground(
                bg(
                        GREEN,
                        12
                )
        );

        content.addView(
                balance,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(100)
                )
        );

        content.addView(
                space(15)
        );

        // Service grid
        GridLayout grid =
                new GridLayout(this);

        grid.setColumnCount(2);

        String[] services = {
                "অ্যাড ব্যালেন্স",
                "মোবাইল ব্যাংকিং",
                "মোবাইল রিচার্জ",
                "ইন্টারনেট প্যাক",
                "লেনদেনের ইতিহাস",
                "বোনাস / কমিশন",
                "কাস্টমার কেয়ার",
                "অন্যান্য"
        };

        for (String service : services) {

            TextView item =
                    tv(
                            service,
                            15,
                            DARK
                    );

            item.setGravity(
                    Gravity.CENTER
            );

            item.setBackground(
                    outline(
                            Color.LTGRAY,
                            Color.WHITE,
                            10
                    )
            );

            GridLayout.LayoutParams lp =
                    new GridLayout.LayoutParams();

            lp.width = 0;
            lp.height = dp(75);

            lp.columnSpec =
                    GridLayout.spec(
                            GridLayout.UNDEFINED,
                            1f
                    );

            lp.setMargins(
                    dp(5),
                    dp(5),
                    dp(5),
                    dp(5)
            );

            grid.addView(
                    item,
                    lp
            );

            if (service.equals(
                    "অ্যাড ব্যালেন্স"
            )) {

                item.setOnClickListener(
                        v -> showWalletDeposit()
                );

            } else if (service.equals(
                    "মোবাইল ব্যাংকিং"
            )) {

                item.setOnClickListener(
                        v -> showMobileBanking()
                );
            }
        }

        content.addView(
                grid,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        TextView logout =
                button(
                        "লগআউট",
                        Color.LTGRAY,
                        DARK
                );

        root.addView(
                logout,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        logout.setOnClickListener(
                v -> {

                    pref.edit()
                            .clear()
                            .apply();

                    showLogin();
                }
        );

        setContentView(root);
    }

    // =========================
    // WALLET DEPOSIT
    // =========================

    private void showWalletDeposit() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.WHITE
        );

        TextView header =
                tv(
                        "অ্যাড ব্যালেন্স",
                        20,
                        Color.WHITE
                );

        header.setGravity(
                Gravity.CENTER
        );

        header.setTypeface(null, 1);

        header.setBackgroundColor(
                BLUE
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
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

        root.addView(
                content,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        TextView intro =
                tv(
                        "আপনার Quick Pay ওয়ালেটে টাকা জমা করুন।",
                        16,
                        DARK
                );

        intro.setGravity(
                Gravity.CENTER
        );

        content.addView(
                intro,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        content.addView(
                space(10)
        );

        TextView info =
                tv(
                        "ব্যালেন্স যোগ করার জন্য অটো ডিপোজিট ব্যবহার করুন।",
                        15,
                        DARK
                );

        info.setGravity(
                Gravity.CENTER
        );

        content.addView(
                info,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        content.addView(
                space(15)
        );

        // =========================
        // এখানে শুধু পরিবর্তন করা হয়েছে
        // Mobile Banking -> Auto Deposit
        // =========================

        TextView autoDeposit =
                button(
                        "অটো ডিপোজিট",
                        BLUE,
                        Color.WHITE
                );

        content.addView(
                autoDeposit
        );

        autoDeposit.setOnClickListener(
                v -> showAutoDeposit()
        );

        content.addView(
                space(10)
        );

        TextView back =
                button(
                        "ফিরে যান",
                        Color.LTGRAY,
                        DARK
                );

        content.addView(
                back
        );

        back.setOnClickListener(
                v -> showHome()
        );

        setContentView(root);
    }

    // =========================
    // AUTO DEPOSIT
    // =========================

    private void showAutoDeposit() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.WHITE
        );

        TextView header =
                tv(
                        "অটো ডিপোজিট",
                        20,
                        Color.WHITE
                );

        header.setGravity(
                Gravity.CENTER
        );

        header.setTypeface(null, 1);

        header.setBackgroundColor(
                BLUE
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
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

        root.addView(
                content,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        TextView notice =
                tv(
                        "অটো ডিপোজিট সেটআপ করুন",
                        18,
                        DARK
                );

        notice.setGravity(
                Gravity.CENTER
        );

        notice.setTypeface(null, 1);

        content.addView(
                notice,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        content.addView(
                space(10)
        );

        TextView description =
                tv(
                        "পেমেন্ট API সংযুক্ত হলে স্বয়ংক্রিয়ভাবে ব্যালেন্স যোগ হবে।",
                        14,
                        DARK
                );

        description.setGravity(
                Gravity.CENTER
        );

        content.addView(
                description,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        content.addView(
                space(15)
        );

        TextView providerTitle =
                tv(
                        "পেমেন্ট মাধ্যম নির্বাচন করুন",
                        15,
                        DARK
                );

        content.addView(
                providerTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(40)
                )
        );

        Spinner provider =
                new Spinner(this);

        String[] providers = {
                "bKash",
                "Nagad",
                "Rocket",
                "Upay"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        providers
                );

        provider.setAdapter(adapter);

        provider.setBackground(
                outline(
                        Color.LTGRAY,
                        Color.WHITE,
                        10
                )
        );

        content.addView(
                provider,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(50)
                )
        );

        content.addView(
                space(10)
        );

        EditText number =
                phoneInput();

        content.addView(number);

        content.addView(
                space(10)
        );

        EditText amount =
                numberInput(
                        "অটো ডিপোজিটের পরিমাণ"
                );

        content.addView(amount);

        content.addView(
                space(15)
        );

        LinearLayout switchRow =
                new LinearLayout(this);

        switchRow.setOrientation(
                LinearLayout.HORIZONTAL
        );

        switchRow.setGravity(
                Gravity.CENTER_VERTICAL
        );

        TextView switchText =
                tv(
                        "অটো ডিপোজিট চালু করুন",
                        15,
                        DARK
                );

        switchRow.addView(
                switchText,
                new LinearLayout.LayoutParams(
                        0,
                        dp(50),
                        1
                )
        );

        Switch autoSwitch =
                new Switch(this);

        switchRow.addView(
                autoSwitch,
                new LinearLayout.LayoutParams(
                        dp(60),
                        dp(50)
                )
        );

        content.addView(
                switchRow
        );

        content.addView(
                space(15)
        );

        TextView save =
                button(
                        "সেভ করুন",
                        BLUE,
                        Color.WHITE
                );

        content.addView(save);

        save.setOnClickListener(
                v -> {

                    String mobile =
                            number.getText()
                                    .toString()
                                    .trim();

                    String money =
                            amount.getText()
                                    .toString()
                                    .trim();

                    if (mobile.isEmpty()) {

                        number.setError(
                                "মোবাইল নম্বর দিন"
                        );

                        return;
                    }

                    if (money.isEmpty()) {

                        amount.setError(
                                "পরিমাণ দিন"
                        );

                        return;
                    }

                    if (!autoSwitch.isChecked()) {

                        new AlertDialog.Builder(this)
                                .setTitle(
                                        "অটো ডিপোজিট"
                                )
                                .setMessage(
                                        "অটো ডিপোজিট চালু করার জন্য সুইচটি অন করুন।"
                                )
                                .setPositiveButton(
                                        "ঠিক আছে",
                                        null
                                )
                                .show();

                        return;
                    }

                    pref.edit()
                            .putBoolean(
                                    "auto_deposit",
                                    true
                            )
                            .putString(
                                    "auto_provider",
                                    provider
                                            .getSelectedItem()
                                            .toString()
                            )
                            .putString(
                                    "auto_number",
                                    mobile
                            )
                            .putString(
                                    "auto_amount",
                                    money
                            )
                            .apply();

                    new AlertDialog.Builder(this)
                            .setTitle(
                                    "সফল"
                            )
                            .setMessage(
                                    "অটো ডিপোজিট সেটিংস সংরক্ষণ হয়েছে।\n\nAPI সংযুক্ত করার পর এটি বাস্তবে স্বয়ংক্রিয়ভাবে কাজ করবে।"
                            )
                            .setPositiveButton(
                                    "ঠিক আছে",
                                    (dialog, which) ->
                                            showWalletDeposit()
                            )
                            .show();
                }
        );

        content.addView(
                space(10)
        );

        TextView back =
                button(
                        "ফিরে যান",
                        Color.LTGRAY,
                        DARK
                );

        content.addView(back);

        back.setOnClickListener(
                v -> showWalletDeposit()
        );

        // আগের সংরক্ষিত তথ্য থাকলে দেখাবে
        boolean enabled =
                pref.getBoolean(
                        "auto_deposit",
                        false
                );

        if (enabled) {

            autoSwitch.setChecked(true);

            String savedNumber =
                    pref.getString(
                            "auto_number",
                            ""
                    );

            String savedAmount =
                    pref.getString(
                            "auto_amount",
                            ""
                    );

            number.setText(
                    savedNumber
            );

            amount.setText(
                    savedAmount
            );

            String savedProvider =
                    pref.getString(
                            "auto_provider",
                            ""
                    );

            for (int i = 0;
                 i < providers.length;
                 i++) {

                if (providers[i].equals(
                        savedProvider
                )) {

                    provider.setSelection(i);
                    break;
                }
            }
        }

        setContentView(root);
    }

    // =========================
    // MOBILE BANKING
    // =========================

    private void showMobileBanking() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.WHITE
        );

        TextView header =
                tv(
                        "মোবাইল ব্যাংকিং",
                        20,
                        Color.WHITE
                );

        header.setGravity(
                Gravity.CENTER
        );

        header.setTypeface(null, 1);

        header.setBackgroundColor(
                BLUE
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(60)
                )
        );

        LinearLayout content =
                new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(15),
                dp(15),
                dp(15),
                dp(15)
        );

        ScrollView scroll =
                new ScrollView(this);

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        TextView notice =
                tv(
                        "মোবাইল ব্যাংকিংয়ের মাধ্যমে লেনদেন করুন।",
                        15,
                        DARK
                );

        notice.setGravity(
                Gravity.CENTER
        );

        content.addView(
                notice,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        content.addView(
                space(10)
        );

        addProviderRow(
                content,
                "bKash"
        );

        addProviderRow(
                content,
                "Nagad"
        );

        addProviderRow(
                content,
                "Rocket"
        );

        addProviderRow(
                content,
                "Upay"
        );

        content.addView(
                space(15)
        );

        TextView back =
                button(
                        "ফিরে যান",
                        Color.LTGRAY,
                        DARK
                );

        content.addView(back);

        back.setOnClickListener(
                v -> showHome()
        );

        setContentView(root);
    }

    // =========================
    // PROVIDER ROW
    // =========================

    private void addProviderRow(
            LinearLayout parent,
            String provider
    ) {

        LinearLayout card =
                new LinearLayout(this);

        card.setOrientation(
                LinearLayout.VERTICAL
        );

        card.setPadding(
                dp(12),
                dp(12),
                dp(12),
                dp(12)
        );

        card.setBackground(
                outline(
                        Color.LTGRAY,
                        Color.WHITE,
                        10
                )
        );

        TextView name =
                tv(
                        provider,
                        18,
                        DARK
                );

        name.setTypeface(null, 1);

        card.addView(
                name,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(35)
                )
        );

        TextView subtitle =
                tv(
                        "মোবাইল নম্বর",
                        13,
                        Color.GRAY
                );

        card.addView(
                subtitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(25)
                )
        );

        TextView number =
                tv(
                        "01XXXXXXXXX",
                        15,
                        BLUE
                );

        number.setTypeface(null, 1);

        card.addView(
                number,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(35)
                )
        );

        LinearLayout actions =
                new LinearLayout(this);

        actions.setOrientation(
                LinearLayout.HORIZONTAL
        );

        TextView copy =
                button(
                        "কপি",
                        Color.LTGRAY,
                        DARK
                );

        TextView send =
                button(
                        "সেন্ড মানি",
                        BLUE,
                        Color.WHITE
                );

        TextView cashout =
                button(
                        "ক্যাশ আউট",
                        GREEN,
                        Color.WHITE
                );

        LinearLayout.LayoutParams a =
                new LinearLayout.LayoutParams(
                        0,
                        dp(45),
                        1
                );

        a.setMargins(
                dp(3),
                0,
                dp(3),
                0
        );

        actions.addView(
                copy,
                a
        );

        actions.addView(
                send,
                a
        );

        actions.addView(
                cashout,
                a
        );

        card.addView(actions);

        copy.setOnClickListener(
                v -> {

                    android.content.ClipboardManager cm =
                            (android.content.ClipboardManager)
                                    getSystemService(
                                            CLIPBOARD_SERVICE
                                    );

                    android.content.ClipData data =
                            android.content.ClipData
                                    .newPlainText(
                                            "number",
                                            "01XXXXXXXXX"
                                    );

                    cm.setPrimaryClip(data);

                    Toast.makeText(
                            this,
                            "নম্বর কপি হয়েছে",
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );

        send.setOnClickListener(
                v -> showMoneyForm(
                        "সেন্ড মানি - " + provider
                )
        );

        cashout.setOnClickListener(
                v -> showMoneyForm(
                        "ক্যাশ আউট - " + provider
                )
        );

        parent.addView(
                card,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        parent.addView(
                space(10)
        );
    }

    // =========================
    // MONEY FORM
    // =========================

    private void showMoneyForm(
            String type
    ) {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );

        root.setBackgroundColor(
                Color.WHITE
        );

        TextView title =
                tv(
                        type,
                        22,
                        BLUE
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        root.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        root.addView(
                space(15)
        );

        EditText phone =
                phoneInput();

        root.addView(phone);

        root.addView(
                space(10)
        );

        EditText amount =
                numberInput(
                        "টাকার পরিমাণ"
                );

        root.addView(amount);

        root.addView(
                space(10)
        );

        EditText reference =
                textInput(
                        "রেফারেন্স (ঐচ্ছিক)"
                );

        root.addView(reference);

        root.addView(
                space(15)
        );

        TextView confirm =
                button(
                        "কনফার্ম",
                        BLUE,
                        Color.WHITE
                );

        root.addView(confirm);

        confirm.setOnClickListener(
                v -> {

                    if (phone.getText()
                            .toString()
                            .trim()
                            .isEmpty()) {

                        phone.setError(
                                "মোবাইল নম্বর দিন"
                        );

                        return;
                    }

                    if (amount.getText()
                            .toString()
                            .trim()
                            .isEmpty()) {

                        amount.setError(
                                "টাকার পরিমাণ দিন"
                        );

                        return;
                    }

                    new AlertDialog.Builder(this)
                            .setTitle(
                                    "লেনদেন নিশ্চিত করুন"
                            )
                            .setMessage(
                                    type +
                                    "\n\nনম্বর: " +
                                    phone.getText()
                                            .toString() +
                                    "\nপরিমাণ: ৳" +
                                    amount.getText()
                                            .toString()
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
                                                Toast.LENGTH_SHORT
                                        ).show();

                                        showMobileBanking();
                                    }
                            )
                            .show();
                }
        );

        root.addView(
                space(10)
        );

        TextView back =
                button(
                        "ফিরে যান",
                        Color.LTGRAY,
                        DARK
                );

        root.addView(back);

        back.setOnClickListener(
                v -> showMobileBanking()
        );

        setContentView(root);
    }

    // =========================
    // REGISTER
    // =========================

    private void showRegister() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setPadding(
                dp(20),
                dp(30),
                dp(20),
                dp(20)
        );

        root.setBackgroundColor(
                Color.WHITE
        );

        TextView title =
                tv(
                        "রেজিস্টার",
                        24,
                        BLUE
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        root.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        root.addView(
                space(15)
        );

        EditText name =
                textInput(
                        "আপনার নাম"
                );

        root.addView(name);

        root.addView(
                space(10)
        );

        EditText phone =
                phoneInput();

        root.addView(phone);

        root.addView(
                space(10)
        );

        EditText password =
                passwordInput(
                        "৬ সংখ্যার পাসওয়ার্ড"
                );

        root.addView(password);

        root.addView(
                space(15)
        );

        TextView register =
                button(
                        "একাউন্ট তৈরি করুন",
                        BLUE,
                        Color.WHITE
                );

        root.addView(register);

        register.setOnClickListener(
                v -> {

                    if (name.getText()
                            .toString()
                            .trim()
                            .isEmpty()) {

                        name.setError(
                                "নাম দিন"
                        );

                        return;
                    }

                    if (phone.getText()
                            .toString()
                            .trim()
                            .isEmpty()) {

                        phone.setError(
                                "মোবাইল নম্বর দিন"
                        );

                        return;
                    }

                    if (password.getText()
                            .toString()
                            .length() != 6) {

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
                                    name.getText()
                                            .toString()
                            )
                            .putString(
                                    "phone",
                                    phone.getText()
                                            .toString()
                            )
                            .putString(
                                    "password",
                                    password.getText()
                                            .toString()
                            )
                            .apply();

                    showPinSetup();
                }
        );

        root.addView(
                space(10)
        );

        TextView back =
                button(
                        "ফিরে যান",
                        Color.LTGRAY,
                        DARK
                );

        root.addView(back);

        back.setOnClickListener(
                v -> showLogin()
        );

        setContentView(root);
    }

    // =========================
    // FORGOT PASSWORD
    // =========================

    private void showForgotPassword() {

        LinearLayout root =
                new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setPadding(
                dp(20),
                dp(40),
                dp(20),
                dp(20)
        );

        root.setBackgroundColor(
                Color.WHITE
        );

        TextView title =
                tv(
                        "পাসওয়ার্ড ভুলে গেছেন?",
                        22,
                        BLUE
                );

        title.setGravity(
                Gravity.CENTER
        );

        title.setTypeface(null, 1);

        root.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        root.addView(
                space(15)
        );

        EditText phone =
                phoneInput();

        root.addView(phone);

        root.addView(
                space(15)
        );

        TextView submit =
                button(
                        "রিকোয়েস্ট পাঠান",
                        BLUE,
                        Color.WHITE
                );

        root.addView(submit);

        submit.setOnClickListener(
                v -> {

                    if (phone.getText()
                            .toString()
                            .trim()
                            .isEmpty()) {

                        phone.setError(
                                "মোবাইল নম্বর দিন"
                        );

                        return;
                    }

                    new AlertDialog.Builder(this)
                            .setTitle(
                                    "রিকোয়েস্ট"
                            )
                            .setMessage(
                                    "পাসওয়ার্ড রিসেটের রিকোয়েস্ট গ্রহণ করা হয়েছে।"
                            )
                            .setPositiveButton(
                                    "ঠিক আছে",
                                    null
                            )
                            .show();
                }
        );

        root.addView(
                space(10)
        );

        TextView back =
                button(
                        "ফিরে যান",
                        Color.LTGRAY,
                        DARK
                );

        root.addView(back);

        back.setOnClickListener(
                v -> showLogin()
        );

        setContentView(root);
    }

    // =========================
    // BACK BUTTON
    // =========================

    @Override
    public void onBackPressed() {

        showHome();
    }
}
