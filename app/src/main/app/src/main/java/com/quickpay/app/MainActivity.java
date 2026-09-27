package com.quickpay.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    int blue = Color.rgb(10, 98, 190);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLogin();
    }

    private TextView text(String value, int size) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(Color.WHITE);
        t.setGravity(Gravity.CENTER);
        return t;
    }

    private EditText input(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextSize(18);
        e.setSingleLine(true);
        e.setPadding(30, 5, 30, 5);
        e.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, 65);
        p.setMargins(30, 15, 30, 15);
        e.setLayoutParams(p);

        return e;
    }

    private Button button(String value) {
        Button b = new Button(this);
        b.setText(value);
        b.setTextSize(20);
        b.setTextColor(blue);
        b.setTypeface(null, Typeface.BOLD);
        b.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, 65);
        p.setMargins(30, 20, 30, 10);
        b.setLayoutParams(p);

        return b;
    }

    private LinearLayout baseLayout() {
        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setGravity(Gravity.CENTER_HORIZONTAL);
        main.setPadding(0, 35, 0, 20);
        main.setBackgroundColor(blue);
        return main;
    }

    private TextView logo() {
        TextView logo = new TextView(this);
        logo.setText("Quick Pay");
        logo.setTextSize(34);
        logo.setTypeface(null, Typeface.BOLD);
        logo.setTextColor(Color.rgb(20, 70, 150));
        logo.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(320, 110);
        p.setMargins(0, 20, 0, 5);
        logo.setLayoutParams(p);
        logo.setBackgroundColor(Color.WHITE);

        return logo;
    }

    private void showLogin() {

        LinearLayout main = baseLayout();

        main.addView(logo());

        main.addView(text(
                "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম", 18));

        EditText phone = input("ফোন");
        main.addView(phone);

        EditText password = input("৬ ডিজিট পাসওয়ার্ড");
        password.setInputType(2 | 0x00000080);
        main.addView(password);

        Button login = button("লগইন");
        main.addView(login);

        TextView forgot = text("পাসওয়ার্ড ভুলে গেছেন?", 17);
        forgot.setPadding(0, 15, 0, 10);
        main.addView(forgot);

        TextView register = text("অ্যাকাউন্ট নেই?  রেজিস্টার করুন", 18);
        register.setPadding(0, 15, 0, 10);
        main.addView(register);

        register.setOnClickListener(v -> showRegister());

        setContentView(main);
    }

    private void showRegister() {

        LinearLayout main = baseLayout();

        main.addView(logo());

        main.addView(text(
                "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম", 18));

        EditText country = input("🇧🇩  বাংলাদেশ");
        main.addView(country);

        EditText agent = input("রিসেলার এজেন্ট কোড");
        main.addView(agent);

        EditText name = input("পূর্ণ নাম");
        main.addView(name);

        EditText phone = input("+880   ফোন নম্বর");
        main.addView(phone);

        EditText pass = input("৬ ডিজিট পাসওয়ার্ড");
        pass.setInputType(2 | 0x00000080);
        main.addView(pass);

        EditText confirm = input("৬ ডিজিট পাসওয়ার্ড নিশ্চিত করুন");
        confirm.setInputType(2 | 0x00000080);
        main.addView(confirm);

        Button next = button("পরবর্তী");
        main.addView(next);

        TextView login = text("অ্যাকাউন্ট আছে?  লগইন", 18);
        login.setPadding(0, 15, 0, 10);
        main.addView(login);

        login.setOnClickListener(v -> showLogin());

        setContentView(main);
    }
}
