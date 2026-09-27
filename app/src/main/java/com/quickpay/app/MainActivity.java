package com.quickpay.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    LinearLayout main;

    int blue = Color.rgb(10, 96, 190);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showLogin();
    }

    TextView text(String s, int size) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(Color.WHITE);
        t.setGravity(Gravity.CENTER);
        return t;
    }

    EditText box(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextSize(20);
        e.setSingleLine(true);
        e.setPadding(25, 0, 25, 0);
        e.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, 80);
        p.setMargins(35, 15, 35, 15);
        e.setLayoutParams(p);

        return e;
    }

    Button button(String name) {
        Button b = new Button(this);
        b.setText(name);
        b.setTextSize(22);
        b.setTextColor(blue);
        b.setTypeface(null, Typeface.BOLD);
        b.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, 80);
        p.setMargins(35, 20, 35, 10);
        b.setLayoutParams(p);

        return b;
    }

    void showLogin() {

        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setGravity(Gravity.CENTER_HORIZONTAL);
        main.setPadding(0, 40, 0, 20);
        main.setBackgroundColor(blue);

        TextView logo = text("loadbazar", 38);
        logo.setTypeface(null, Typeface.BOLD);

        TextView subtitle =
                text("বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম", 17);

        main.addView(logo);
        main.addView(subtitle);

        EditText phone = box("📞  ফোন");
        EditText password = box("🔒  ৬ ডিজিট পাসওয়ার্ড");

        main.addView(phone);
        main.addView(password);

        Button login = button("লগইন");
        main.addView(login);

        TextView forgot =
                text("পাসওয়ার্ড ভুলে গেছেন?", 17);
        main.addView(forgot);

        TextView register =
                text("অ্যাকাউন্ট নেই?  রেজিস্টার করুন", 18);
        main.addView(register);

        login.setOnClickListener(v ->
                Toast.makeText(this,
                        "লগইন সিস্টেম পরে যুক্ত করা হবে",
                        Toast.LENGTH_SHORT).show());

        register.setOnClickListener(v ->
                showRegister());

        setContentView(main);
    }

    void showRegister() {

        main.removeAllViews();

        TextView title = text("loadbazar", 36);
        title.setTypeface(null, Typeface.BOLD);
        main.addView(title);

        main.addView(text(
                "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম", 17));

        main.addView(box("🇧🇩  বাংলাদেশ"));
        main.addView(box("🎁  রিসেলার এজেন্ট কোড"));
        main.addView(box("👤  পূর্ণ নাম"));
        main.addView(box("+880   ফোন নম্বর"));
        main.addView(box("🔒  ৬ ডিজিট পাসওয়ার্ড"));
        main.addView(box("🔒  ৬ ডিজিট পাসওয়ার্ড নিশ্চিত করুন"));

        Button next = button("পরবর্তী");
        main.addView(next);

        TextView login =
                text("অ্যাকাউন্ট আছে?  লগইন", 18);
        main.addView(login);

        login.setOnClickListener(v -> showLogin());
    }
  }package com.quickpay.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {

    LinearLayout main;

    int blue = Color.rgb(10, 96, 190);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showLogin();
    }

    TextView text(String s, int size) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(Color.WHITE);
        t.setGravity(Gravity.CENTER);
        return t;
    }

    EditText box(String hint) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setTextSize(20);
        e.setSingleLine(true);
        e.setPadding(25, 0, 25, 0);
        e.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, 80);
        p.setMargins(35, 15, 35, 15);
        e.setLayoutParams(p);

        return e;
    }

    Button button(String name) {
        Button b = new Button(this);
        b.setText(name);
        b.setTextSize(22);
        b.setTextColor(blue);
        b.setTypeface(null, Typeface.BOLD);
        b.setBackgroundColor(Color.WHITE);

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(-1, 80);
        p.setMargins(35, 20, 35, 10);
        b.setLayoutParams(p);

        return b;
    }

    void showLogin() {

        main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setGravity(Gravity.CENTER_HORIZONTAL);
        main.setPadding(0, 40, 0, 20);
        main.setBackgroundColor(blue);

        TextView logo = text("loadbazar", 38);
        logo.setTypeface(null, Typeface.BOLD);

        TextView subtitle =
                text("বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম", 17);

        main.addView(logo);
        main.addView(subtitle);

        EditText phone = box("📞  ফোন");
        EditText password = box("🔒  ৬ ডিজিট পাসওয়ার্ড");

        main.addView(phone);
        main.addView(password);

        Button login = button("লগইন");
        main.addView(login);

        TextView forgot =
                text("পাসওয়ার্ড ভুলে গেছেন?", 17);
        main.addView(forgot);

        TextView register =
                text("অ্যাকাউন্ট নেই?  রেজিস্টার করুন", 18);
        main.addView(register);

        login.setOnClickListener(v ->
                Toast.makeText(this,
                        "লগইন সিস্টেম পরে যুক্ত করা হবে",
                        Toast.LENGTH_SHORT).show());

        register.setOnClickListener(v ->
                showRegister());

        setContentView(main);
    }

    void showRegister() {

        main.removeAllViews();

        TextView title = text("loadbazar", 36);
        title.setTypeface(null, Typeface.BOLD);
        main.addView(title);

        main.addView(text(
                "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম", 17));

        main.addView(box("🇧🇩  বাংলাদেশ"));
        main.addView(box("🎁  রিসেলার এজেন্ট কোড"));
        main.addView(box("👤  পূর্ণ নাম"));
        main.addView(box("+880   ফোন নম্বর"));
        main.addView(box("🔒  ৬ ডিজিট পাসওয়ার্ড"));
        main.addView(box("🔒  ৬ ডিজিট পাসওয়ার্ড নিশ্চিত করুন"));

        Button next = button("পরবর্তী");
        main.addView(next);

        TextView login =
                text("অ্যাকাউন্ট আছে?  লগইন", 18);
        main.addView(login);

        login.setOnClickListener(v -> showLogin());
    }
}
