package com.quickpay.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {

    int blue = Color.rgb(10, 96, 190);
    int darkBlue = Color.rgb(0, 70, 150);
    int white = Color.WHITE;
    int gray = Color.rgb(130, 135, 140);

    LinearLayout main;
    boolean bangla = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLogin();
    }

    TextView text(String value, int size) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(white);
        t.setGravity(Gravity.CENTER);
        return t;
    }

    EditText input(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextSize(20);
        e.setSingleLine(true);
        e.setPadding(25, 5, 25, 5);
        e.setTextColor(darkBlue);
        e.setHintTextColor(gray);

        GradientHelper.setBackground(e, white, 18);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 64);
        p.setMargins(0, 12, 0, 12);
        e.setLayoutParams(p);

        return e;
    }

    Button button(String value) {
        Button b = new Button(this);
        b.setText(value);
        b.setTextSize(21);
        b.setTextColor(darkBlue);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        GradientHelper.setBackground(b, white, 18);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 65);
        p.setMargins(0, 18, 0, 10);
        b.setLayoutParams(p);

        return b;
    }

    void baseLayout() {
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(blue);

        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setGravity(Gravity.CENTER_HORIZONTAL);
        main.setPadding(35, 35, 35, 35);

        scroll.addView(main);
        setContentView(scroll);
    }

    void header() {
        TextView title = text("Quick Pay", 34);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(
                        -1, 80);
        p.setMargins(0, 20, 0, 5);
        title.setLayoutParams(p);

        main.addView(title);

        TextView subtitle = text(
                bangla ? "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম"
                       : "Bangladesh's smart recharge platform", 18);

        main.addView(subtitle);

        // বাংলা / English
        Button language = button(bangla ? "বাংলা        EN" : "বাংলা        EN");
        language.setTextSize(16);
        language.setBackgroundColor(Color.TRANSPARENT);

        language.setOnClickListener(v -> {
            bangla = !bangla;
            showLogin();
        });

        main.addView(language);
    }

    void showLogin() {
        baseLayout();
        header();

        EditText phone = input(bangla ? "ফোন" : "Phone");
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        main.addView(phone);

        EditText password = input(
                bangla ? "৬ ডিজিট পাসওয়ার্ড" : "6 digit password");
        password.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        main.addView(password);

        Button login = button(bangla ? "লগইন" : "LOGIN");

        login.setOnClickListener(v -> {
            // পরে এখানে Login system যোগ করা যাবে
        });

        main.addView(login);

        TextView forgot = text(
                bangla ? "পাসওয়ার্ড ভুলে গেছেন?"
                       : "Forgot password?", 17);

        forgot.setPadding(0, 20, 0, 20);
        main.addView(forgot);

        TextView register = text(
                bangla ? "অ্যাকাউন্ট নেই? রেজিস্টার করুন"
                       : "Don't have an account? Register", 18);

        register.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        register.setOnClickListener(v -> showRegister());

        main.addView(register);
    }

    void showRegister() {
        baseLayout();
        header();

        TextView country = text(
                bangla ? "🇧🇩   বাংলাদেশ        ▼"
                       : "🇧🇩   Bangladesh        ▼", 20);

        GradientHelper.setBackground(country, white, 18);

        country.setTextColor(darkBlue);
        country.setGravity(Gravity.CENTER_VERTICAL);
        country.setPadding(25, 0, 25, 0);

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(-1, 65);
        cp.setMargins(0, 15, 0, 10);
        country.setLayoutParams(cp);

        main.addView(country);

        EditText agent = input(
                bangla ? "রিসেলার এজেন্ট কোড"
                       : "Reseller Agent Code");
        main.addView(agent);

        EditText name = input(
                bangla ? "পূর্ণ নাম" : "Full Name");
        main.addView(name);

        EditText phone = input(
                bangla ? "+880    ফোন নম্বর"
                       : "+880    Phone Number");
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        main.addView(phone);

        EditText password = input(
                bangla ? "৬ ডিজিট পাসওয়ার্ড"
                       : "6 digit password");
        password.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        main.addView(password);

        EditText confirm = input(
                bangla ? "৬ ডিজিট পাসওয়ার্ড নিশ্চিত করুন"
                       : "Confirm 6 digit password");
        confirm.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        main.addView(confirm);

        Button next = button(bangla ? "পরবর্তী" : "NEXT");

        next.setOnClickListener(v -> {
            // পরে Registration system যোগ করা যাবে
        });

        main.addView(next);

        TextView login = text(
                bangla ? "অ্যাকাউন্ট আছে? লগইন"
                       : "Already have an account? Login", 18);

        login.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        login.setOnClickListener(v -> showLogin());

        main.addView(login);
    }

    public static class GradientHelper {

        public static void setBackground(
                View view, int color, int radius) {

            android.graphics.drawable.GradientDrawable bg =
                    new android.graphics.drawable.GradientDrawable();

            bg.setColor(color);
            bg.setCornerRadius(radius);

            view.setBackground(bg);
        }
    }
}
