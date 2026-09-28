package com.quickpay.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class MainActivity extends Activity {

    int blue = Color.rgb(8, 96, 190);
    int green = Color.rgb(0, 91, 55);
    int yellow = Color.rgb(255, 190, 0);

    LinearLayout root;

    EditText phone;
    EditText password;

    TextView language;
    TextView subtitle;
    TextView forgot;
    TextView register;

    boolean bangla = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(blue);
        getWindow().setNavigationBarColor(blue);

        showLogin();
    }

    int dp(float value) {
        return (int) (value *
                getResources().getDisplayMetrics().density + 0.5f);
    }

    GradientDrawable bg(int color, float radius) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(radius));
        return g;
    }

    GradientDrawable borderBg(
            int color,
            float radius,
            int strokeColor
    ) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(radius));
        g.setStroke(dp(1), strokeColor);
        return g;
    }

    TextView tv(
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

    void space(
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

    void showLogin() {

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(blue);

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(
                dp(34),
                dp(20),
                dp(34),
                dp(20)
        );

        scroll.addView(root);
        setContentView(scroll);

        // Language
        LinearLayout langBox = new LinearLayout(this);
        langBox.setGravity(Gravity.CENTER);
        langBox.setPadding(
                dp(5),
                dp(4),
                dp(5),
                dp(4)
        );
        langBox.setBackground(
                bg(Color.rgb(65, 135, 210), 40)
        );

        language = tv(
                "বাংলা     EN",
                16,
                Color.WHITE
        );

        language.setGravity(Gravity.CENTER);
        language.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        langBox.addView(
                language,
                new LinearLayout.LayoutParams(
                        dp(170),
                        dp(48)
                )
        );

        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        dp(175),
                        dp(56)
                );

        lp.gravity = Gravity.RIGHT;

        root.addView(langBox, lp);

        language.setOnClickListener(v -> {

            bangla = !bangla;
            updateLanguage();
        });

        space(root, 80);

        // Logo
        TextView logo = tv(
                "Quick Pay",
                32,
                blue
        );

        logo.setGravity(Gravity.CENTER);
        logo.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        logo.setBackground(
                bg(Color.WHITE, 18)
        );

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(320),
                        dp(125)
                )
        );

        space(root, 8);

        subtitle = tv(
                "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম",
                18,
                Color.WHITE
        );

        subtitle.setGravity(Gravity.CENTER);

        root.addView(
                subtitle,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(45)
                )
        );

        space(root, 20);

        // Phone
        phone = new EditText(this);

        phone.setHint("ফোন");
        phone.setTextSize(20);
        phone.setSingleLine(true);
        phone.setPadding(
                dp(25),
                0,
                dp(20),
                0
        );

        phone.setTextColor(Color.DKGRAY);
        phone.setHintTextColor(Color.GRAY);

        phone.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        phone.setBackground(
                borderBg(
                        Color.WHITE,
                        12,
                        Color.LTGRAY
                )
        );

        root.addView(
                phone,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        space(root, 25);

        // Password
        LinearLayout passwordBox =
                new LinearLayout(this);

        passwordBox.setGravity(
                Gravity.CENTER_VERTICAL
        );

        passwordBox.setPadding(
                dp(20),
                0,
                dp(5),
                0
        );

        passwordBox.setBackground(
                borderBg(
                        Color.WHITE,
                        12,
                        Color.LTGRAY
                )
        );

        password = new EditText(this);

        password.setHint(
                "৬ ডিজিট পাসওয়ার্ড"
        );

        password.setTextSize(20);

        password.setSingleLine(true);

        password.setTextColor(Color.DKGRAY);

        password.setHintTextColor(Color.GRAY);

        password.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        password.setBackgroundColor(
                Color.TRANSPARENT
        );

        passwordBox.addView(
                password,
                new LinearLayout.LayoutParams(
                        0,
                        dp(62),
                        1
                )
        );

        TextView eye =
                tv("◉", 27, blue);

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
                        -1,
                        dp(62)
                )
        );

        eye.setOnClickListener(v -> {

            int type =
                    password.getInputType();

            if ((type &
                    InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                    != 0) {

                password.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                );

            } else {

                password.setInputType(
                        InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
                );
            }

            password.setSelection(
                    password.length()
            );
        });

        space(root, 30);

        // Login
        TextView login =
                button("লগইন");

        root.addView(
                login,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
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
                        "ফোন নম্বর দিন"
                );

                phone.requestFocus();

                return;
            }

            if (pass.isEmpty()) {

                password.setError(
                        "পাসওয়ার্ড দিন"
                );

                password.requestFocus();

                return;
            }

            if (pass.length() < 6) {

                password.setError(
                        "৬ ডিজিট পাসওয়ার্ড দিন"
                );

                password.requestFocus();

                return;
            }

            // LOGIN SUCCESS
            showHome();
        });

        space(root, 20);

        forgot = tv(
                "পাসওয়ার্ড ভুলে গেছেন?",
                17,
                Color.WHITE
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
                v -> showForgot()
        );

        register = tv(
                "অ্যাকাউন্ট নেই?  রেজিস্টার করুন",
                18,
                Color.WHITE
        );

        register.setGravity(Gravity.CENTER);

        register.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        root.addView(
                register,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        register.setOnClickListener(
                v -> showRegister()
        );
    }

    TextView button(String value) {

        TextView b =
                tv(value, 22, blue);

        b.setGravity(Gravity.CENTER);

        b.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        b.setBackground(
                bg(Color.WHITE, 12)
        );

        return b;
    }

    // =========================================================
    // HOME SCREEN
    // =========================================================

    void showHome() {

        getWindow().setStatusBarColor(green);
        getWindow().setNavigationBarColor(Color.WHITE);

        root = new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setBackgroundColor(
                Color.rgb(248, 248, 248)
        );

        setContentView(root);

        // ---------------- HEADER ----------------

        LinearLayout header =
                new LinearLayout(this);

        header.setOrientation(
                LinearLayout.VERTICAL
        );

        header.setPadding(
                dp(20),
                dp(15),
                dp(20),
                dp(12)
        );

        header.setBackground(
                bg(green, 28)
        );

        root.addView(
                header,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        3.4f
                )
        );

        // Top row
        LinearLayout top =
                new LinearLayout(this);

        top.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.addView(
                top,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        TextView logo =
                tv("Quick Pay", 24, blue);

        logo.setGravity(Gravity.CENTER);

        logo.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        logo.setBackground(
                bg(Color.WHITE, 15)
        );

        top.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(145),
                        dp(55)
                )
        );

        Space topSpace = new Space(this);

        top.addView(
                topSpace,
                new LinearLayout.LayoutParams(
                        0,
                        1,
                        1
                )
        );

        TextView en =
                tv("EN", 16, Color.WHITE);

        en.setGravity(Gravity.CENTER);

        top.addView(
                en,
                new LinearLayout.LayoutParams(
                        dp(40),
                        dp(50)
                )
        );

        TextView bell =
                tv("♧", 30, Color.WHITE);

        bell.setGravity(Gravity.CENTER);

        top.addView(
                bell,
                new LinearLayout.LayoutParams(
                        dp(45),
                        dp(50)
                )
        );

        TextView logout =
                tv("➜", 30, Color.WHITE);

        logout.setGravity(Gravity.CENTER);

        top.addView(
                logout,
                new LinearLayout.LayoutParams(
                        dp(45),
                        dp(50)
                )
        );

        logout.setOnClickListener(
                v -> showLogin()
        );

        // Name + balance
        LinearLayout info =
                new LinearLayout(this);

        info.setGravity(
                Gravity.CENTER_VERTICAL
        );

        header.addView(
                info,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1.1f
                )
        );

        TextView name =
                tv("Rosy", 25, Color.WHITE);

        name.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        info.addView(
                name,
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );

        LinearLayout balance =
                new LinearLayout(this);

        balance.setGravity(Gravity.CENTER);

        balance.setPadding(
                dp(12),
                0,
                dp(8),
                0
        );

        balance.setBackground(
                bg(yellow, 40)
        );

        TextView balText =
                tv(
                        "মেইন ব্যালেন্স: • • • •\n" +
                        "ড্রাইভ ব্যালেন্স: • • • •",
                        13,
                        Color.DKGRAY
                );

        balText.setGravity(
                Gravity.CENTER_VERTICAL
        );

        balance.addView(
                balText,
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );

        TextView eye =
                tv("◉", 22, Color.DKGRAY);

        eye.setGravity(Gravity.CENTER);

        balance.addView(
                eye,
                new LinearLayout.LayoutParams(
                        dp(35),
                        -1
                )
        );

        info.addView(
                balance,
                new LinearLayout.LayoutParams(
                        dp(245),
                        dp(65)
                )
        );

        // Notice
        TextView notice =
                tv(
                        "●   রাকিব রহমান (016*******) ৭৯৯ টাকা " +
                        "এয়ারটেল রিচার্জ করলেন",
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

        notice.setBackground(
                bg(Color.WHITE, 14)
        );

        header.addView(
                notice,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        0.8f
                )
        );

        // ---------------- SERVICES ----------------

        LinearLayout services =
                new LinearLayout(this);

        services.setOrientation(
                LinearLayout.VERTICAL
        );

        services.setPadding(
                dp(12),
                dp(5),
                dp(12),
                dp(2)
        );

        root.addView(
                services,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        5.3f
                )
        );

        String[] icons = {
                "👛", "💵", "🏦", "📱",
                "💬", "🎁", "🧾", "🏷",
                "🎧", "⭐", "▶", "👥"
        };

        String[] names = {
                "ওয়ালেট\nডিপোজিট",
                "মোবাইল\nব্যাংকিং",
                "ব্যাংক\nট্রান্সফার",
                "মোবাইল\nরিচার্জ",
                "গ্রুপ\nচ্যাট",
                "ইনভাইট\nবোনাস",
                "বিল\nপে",
                "বিশেষ\nঅফার",
                "কাস্টমার\nকেয়ার",
                "কাস্টমার\nরিভিউ",
                "ভিডিও\nটিউটোরিয়াল",
                "আমাদের\nসম্পর্কে"
        };

        for (int r = 0; r < 3; r++) {

            LinearLayout row =
                    new LinearLayout(this);

            row.setGravity(Gravity.CENTER);

            services.addView(
                    row,
                    new LinearLayout.LayoutParams(
                            -1,
                            0,
                            1
                    )
            );

            for (int c = 0; c < 4; c++) {

                int index =
                        r * 4 + c;

                LinearLayout item =
                        new LinearLayout(this);

                item.setOrientation(
                        LinearLayout.VERTICAL
                );

                item.setGravity(
                        Gravity.CENTER
                );

                TextView icon =
                        tv(
                                icons[index],
                                25,
                                Color.DKGRAY
                        );

                icon.setGravity(
                        Gravity.CENTER
                );

                item.addView(
                        icon,
                        new LinearLayout.LayoutParams(
                                -1,
                                0,
                                1
                        )
                );

                TextView label =
                        tv(
                                names[index],
                                12,
                                Color.DKGRAY
                        );

                label.setGravity(
                        Gravity.CENTER
                );

                item.addView(
                        label,
                        new LinearLayout.LayoutParams(
                                -1,
                                0,
                                1
                        )
                );

                row.addView(
                        item,
                        new LinearLayout.LayoutParams(
                                0,
                                -1,
                                1
                        )
                );
            }
        }

        // ---------------- OFFER ----------------

        LinearLayout offer =
                new LinearLayout(this);

        offer.setOrientation(
                LinearLayout.VERTICAL
        );

        offer.setPadding(
                dp(15),
                dp(8),
                dp(15),
                dp(8)
        );

        offer.setBackground(
                bg(green, 18)
        );

        root.addView(
                offer,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1.35f
                )
        );

        TextView offerTitle =
                tv(
                        "🎁  ডিপোজিট বোনাস অফার",
                        17,
                        Color.WHITE
                );

        offerTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        offer.addView(
                offerTitle,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        TextView offerText =
                tv(
                        "এখনই করুন, বোনাস নিয়ে নিন!",
                        11,
                        Color.WHITE
                );

        offer.addView(
                offerText,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        0.8f
                )
        );

        LinearLayout amounts =
                new LinearLayout(this);

        amounts.setGravity(
                Gravity.CENTER
        );

        offer.addView(
                amounts,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1.4f
                )
        );

        String[] amount = {
                "৳ ১২৯৯\nবোনাস ৬৯৯",
                "৳ ২৫৯৯\nবোনাস ৮৯৯",
                "৳ ৪৫৯৯\nবোনাস ১৪৯৯"
        };

        for (String a : amount) {

            TextView x =
                    tv(a, 11, Color.DKGRAY);

            x.setGravity(
                    Gravity.CENTER
            );

            x.setBackground(
                    bg(Color.WHITE, 10)
            );

            LinearLayout.LayoutParams xlp =
                    new LinearLayout.LayoutParams(
                            0,
                            -1,
                            1
                    );

            xlp.setMargins(
                    dp(3),
                    0,
                    dp(3),
                    0
            );

            amounts.addView(x, xlp);
        }

        // ---------------- BOTTOM NAV ----------------

        LinearLayout bottom =
                new LinearLayout(this);

        bottom.setGravity(
                Gravity.CENTER
        );

        bottom.setBackgroundColor(
                Color.WHITE
        );

        root.addView(
                bottom,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1.15f
                )
        );

        addNav(
                bottom,
                "⌂",
                "হোম",
                true
        );

        addNav(
                bottom,
                "◷",
                "লেনদেন",
                false
        );

        addNav(
                bottom,
                "♙",
                "প্রোফাইল",
                false
        );
    }

    void addNav(
            LinearLayout parent,
            String icon,
            String title,
            boolean active
    ) {

        LinearLayout item =
                new LinearLayout(this);

        item.setOrientation(
                LinearLayout.VERTICAL
        );

        item.setGravity(
                Gravity.CENTER
        );

        TextView i =
                tv(
                        icon,
                        27,
                        active ? Color.rgb(210, 60, 60)
                               : Color.GRAY
                );

        i.setGravity(Gravity.CENTER);

        item.addView(
                i,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        TextView t =
                tv(
                        title,
                        11,
                        active ? Color.rgb(210, 60, 60)
                               : Color.GRAY
                );

        t.setGravity(Gravity.CENTER);

        item.addView(
                t,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        0.8f
                )
        );

        parent.addView(
                item,
                new LinearLayout.LayoutParams(
                        0,
                        -1,
                        1
                )
        );
    }

    // =========================================================
    // FORGOT PASSWORD
    // =========================================================

    void showForgot() {

        getWindow().setStatusBarColor(blue);
        getWindow().setNavigationBarColor(blue);

        LinearLayout page =
                new LinearLayout(this);

        page.setOrientation(
                LinearLayout.VERTICAL
        );

        page.setGravity(
                Gravity.CENTER_HORIZONTAL
        );

        page.setPadding(
                dp(30),
                dp(40),
                dp(30),
                dp(20)
        );

        page.setBackgroundColor(blue);

        setContentView(page);

        TextView title =
                tv(
                        "পাসওয়ার্ড পরিবর্তন",
                        28,
                        Color.WHITE
                );

        title.setGravity(Gravity.CENTER);

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        page.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(70)
                )
        );

        space(page, 30);

        EditText p =
                new EditText(this);

        p.setHint(
                "ফোন নম্বর"
        );

        p.setTextSize(19);

        p.setSingleLine(true);

        p.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        p.setPadding(
                dp(20),
                0,
                dp(20),
                0
        );

        p.setBackground(
                bg(Color.WHITE, 12)
        );

        page.addView(
                p,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        space(page, 25);

        TextView reset =
                button("OTP পাঠান");

        page.addView(
                reset,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        reset.setOnClickListener(
                v -> Toast.makeText(
                        this,
                        "OTP পাঠানোর ব্যবস্থা এখানে যুক্ত হবে",
                        Toast.LENGTH_SHORT
                ).show()
        );

        space(page, 20);

        TextView back =
                tv(
                        "← লগইনে ফিরে যান",
                        18,
                        Color.WHITE
                );

        back.setGravity(Gravity.CENTER);

        page.addView(
                back,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        back.setOnClickListener(
                v -> showLogin()
        );
    }

    // =========================================================
    // REGISTER
    // =========================================================

    void showRegister() {

        getWindow().setStatusBarColor(blue);
        getWindow().setNavigationBarColor(blue);

        ScrollView scroll =
                new ScrollView(this);

        scroll.setBackgroundColor(blue);

        LinearLayout page =
                new LinearLayout(this);

        page.setOrientation(
                LinearLayout.VERTICAL
        );

        page.setPadding(
                dp(30),
                dp(25),
                dp(30),
                dp(25)
        );

        scroll.addView(page);

        setContentView(scroll);

        TextView title =
                tv(
                        "নতুন অ্যাকাউন্ট",
                        28,
                        Color.WHITE
                );

        title.setGravity(Gravity.CENTER);

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        page.addView(
                title,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(70)
                )
        );

        EditText name =
                field("পূর্ণ নাম");

        page.addView(
                name,
                fieldParams()
        );

        space(page, 18);

        EditText regPhone =
                field("ফোন নম্বর");

        regPhone.setInputType(
                InputType.TYPE_CLASS_PHONE
        );

        page.addView(
                regPhone,
                fieldParams()
        );

        space(page, 18);

        EditText pass =
                field("৬ ডিজিট পাসওয়ার্ড");

        pass.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );

        page.addView(
                pass,
                fieldParams()
        );

        space(page, 25);

        TextView create =
                button("রেজিস্টার");

        page.addView(
                create,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(62)
                )
        );

        create.setOnClickListener(
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

                    if (regPhone.getText()
                            .toString()
                            .trim()
                            .isEmpty()) {

                        regPhone.setError(
                                "ফোন নম্বর দিন"
                        );

                        return;
                    }

                    if (pass.getText()
                            .toString()
                            .trim()
                            .length() < 6) {

                        pass.setError(
                                "৬ ডিজিট পাসওয়ার্ড দিন"
                        );

                        return;
                    }

                    Toast.makeText(
                            this,
                            "রেজিস্ট্রেশন সম্পন্ন",
                            Toast.LENGTH_SHORT
                    ).show();

                    showLogin();
                }
        );

        space(page, 20);

        TextView back =
                tv(
                        "← লগইনে ফিরে যান",
                        18,
                        Color.WHITE
                );

        back.setGravity(Gravity.CENTER);

        page.addView(
                back,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );

        back.setOnClickListener(
                v -> showLogin()
        );
    }

    EditText field(String hint) {

        EditText e =
                new EditText(this);

        e.setHint(hint);
        e.setTextSize(19);
        e.setSingleLine(true);

        e.setTextColor(Color.DKGRAY);
        e.setHintTextColor(Color.GRAY);

        e.setPadding(
                dp(20),
                0,
                dp(20),
                0
        );

        e.setBackground(
                bg(Color.WHITE, 12)
        );

        return e;
    }

    LinearLayout.LayoutParams fieldParams() {

        return new LinearLayout.LayoutParams(
                -1,
                dp(62)
        );
    }

    // =========================================================
    // LANGUAGE
    // =========================================================

    void updateLanguage() {

        if (bangla) {

            language.setText(
                    "বাংলা     EN"
            );

            subtitle.setText(
                    "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম"
            );

            phone.setHint("ফোন");

            password.setHint(
                    "৬ ডিজিট পাসওয়ার্ড"
            );

            forgot.setText(
                    "পাসওয়ার্ড ভুলে গেছেন?"
            );

            register.setText(
                    "অ্যাকাউন্ট নেই?  রেজিস্টার করুন"
            );

        } else {

            language.setText(
                    "BN     EN"
            );

            subtitle.setText(
                    "Bangladesh's best recharge business platform"
            );

            phone.setHint("Phone");

            password.setHint(
                    "6 Digit Password"
            );

            forgot.setText(
                    "Forgot Password?"
            );

            register.setText(
                    "Don't have an account?  Register"
            );
        }
    }

    // =========================================================
    // BACK BUTTON
    // =========================================================

    @Override
    public void onBackPressed() {

        // Back করলে Login-এ ফিরে যাবে
        showLogin();
    }
}
