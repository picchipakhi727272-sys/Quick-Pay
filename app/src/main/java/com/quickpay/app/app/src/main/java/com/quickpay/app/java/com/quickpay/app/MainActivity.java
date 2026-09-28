package com.quickpay.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class MainActivity extends Activity {

    int blue = Color.rgb(8, 96, 190);

    LinearLayout root;
    boolean bangla = true;

    TextView title;
    TextView subtitle;
    TextView forgot;
    TextView register;
    TextView language;

    EditText phone;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(blue);
        getWindow().setNavigationBarColor(blue);

        showLogin();
    }

    int dp(float value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    TextView text(String value, float size, int color) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    GradientDrawableBox box(int color, float radius) {
        GradientDrawableBox bg = new GradientDrawableBox();
        bg.setColor(color);
        bg.setCornerRadius(dp(radius));
        bg.setStroke(dp(1), Color.rgb(210, 210, 210));
        return bg;
    }

    void addSpace(int height) {
        Space s = new Space(this);
        root.addView(s, new LinearLayout.LayoutParams(
                1, dp(height)
        ));
    }

    TextView makeButton(String value) {
        TextView b = text(value, 22, blue);
        b.setGravity(Gravity.CENTER);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setBackground(box(Color.WHITE, 12));

        root.addView(b, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(62)
        ));

        return b;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void showLogin() {

        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(dp(34), dp(20), dp(34), dp(20));
        root.setBackgroundColor(blue);

        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(blue);
        scroll.addView(root);

        setContentView(scroll);

        // Language switch
        LinearLayout languageBox = new LinearLayout(this);
        languageBox.setGravity(Gravity.CENTER);
        languageBox.setPadding(dp(5), dp(4), dp(5), dp(4));
        languageBox.setBackground(box(Color.rgb(65, 135, 210), 40));

        language = text("বাংলা     EN", 16, Color.WHITE);
        language.setGravity(Gravity.CENTER);
        language.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        languageBox.addView(language,
                new LinearLayout.LayoutParams(dp(170), dp(48)));

        LinearLayout.LayoutParams langParams =
                new LinearLayout.LayoutParams(
                        dp(175), dp(56));

        langParams.gravity = Gravity.RIGHT;

        root.addView(languageBox, langParams);

        language.setOnClickListener(v -> {
            bangla = !bangla;
            updateLanguage();
        });

        addSpace(110);

        // Logo box
        TextView logo = text("Quick Pay", 32, blue);
        logo.setGravity(Gravity.CENTER);
        logo.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        logo.setBackground(box(Color.WHITE, 18));

        root.addView(logo, new LinearLayout.LayoutParams(
                dp(320), dp(125)
        ));

        addSpace(10);

        subtitle = text(
                "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম",
                18,
                Color.WHITE
        );
        subtitle.setGravity(Gravity.CENTER);

        root.addView(subtitle, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(45)
        ));

        addSpace(25);

        // Phone
        phone = new EditText(this);
        phone.setHint("ফোন");
        phone.setTextSize(20);
        phone.setSingleLine(true);
        phone.setPadding(dp(25), 0, dp(20), 0);
        phone.setTextColor(Color.DKGRAY);
        phone.setHintTextColor(Color.GRAY);
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        phone.setBackground(box(Color.WHITE, 12));

        root.addView(phone, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(62)
        ));

        addSpace(28);

        // Password row
        LinearLayout passwordBox = new LinearLayout(this);
        passwordBox.setGravity(Gravity.CENTER_VERTICAL);
        passwordBox.setPadding(dp(20), 0, dp(10), 0);
        passwordBox.setBackground(box(Color.WHITE, 12));

        password = new EditText(this);
        password.setHint("৬ ডিজিট পাসওয়ার্ড");
        password.setTextSize(20);
        password.setSingleLine(true);
        password.setTextColor(Color.DKGRAY);
        password.setHintTextColor(Color.GRAY);
        password.setInputType(
                InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_NUMBER_VARIATION_PASSWORD
        );
        password.setBackgroundColor(Color.TRANSPARENT);

        LinearLayout.LayoutParams passParams =
                new LinearLayout.LayoutParams(0, dp(62), 1);

        passwordBox.addView(password, passParams);

        TextView eye = text("◉", 27, blue);
        eye.setGravity(Gravity.CENTER);
        passwordBox.addView(eye, new LinearLayout.LayoutParams(
                dp(55), dp(62)
        ));

        root.addView(passwordBox, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(62)
        ));

        eye.setOnClickListener(v -> {
            if ((password.getInputType() &
                    InputType.TYPE_TEXT_VARIATION_PASSWORD) != 0) {

                password.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                );

            } else {

                password.setInputType(
                        InputType.TYPE_CLASS_NUMBER |
                        InputType.TYPE_NUMBER_VARIATION_PASSWORD
                );
            }

            password.setSelection(password.length());
        });

        addSpace(32);

        // Login button
        TextView login = makeButton("লগইন");

        login.setOnClickListener(v -> {

            if (phone.getText().toString().trim().isEmpty()) {
                phone.setError("ফোন নম্বর দিন");
                phone.requestFocus();
                return;
            }

            if (password.getText().toString().trim().isEmpty()) {
                password.setError("পাসওয়ার্ড দিন");
                password.requestFocus();
                return;
            }

            Toast.makeText(
                    MainActivity.this,
                    "লগইন সফল",
                    Toast.LENGTH_SHORT
            ).show();

            // পরে এখানে Home Screen খুলবে
        });

        addSpace(25);

        forgot = text(
                "পাসওয়ার্ড ভুলে গেছেন?",
                17,
                Color.WHITE
        );
        forgot.setGravity(Gravity.CENTER);

        root.addView(forgot, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(40)
        ));

        forgot.setOnClickListener(v ->
                Toast.makeText(
                        MainActivity.this,
                        "পাসওয়ার্ড রিসেট",
                        Toast.LENGTH_SHORT
                ).show()
        );

        register = text(
                "অ্যাকাউন্ট নেই?  রেজিস্টার করুন",
                18,
                Color.WHITE
        );
        register.setGravity(Gravity.CENTER);
        register.setTypeface(Typeface.DEFAULT, Typeface.BOLD);

        root.addView(register, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(55)
        ));

        register.setOnClickListener(v ->
                Toast.makeText(
                        MainActivity.this,
                        "রেজিস্ট্রেশন পেজ",
                        Toast.LENGTH_SHORT
                ).show()
        );
    }

    void updateLanguage() {

        if (bangla) {
            language.setText("বাংলা     EN");
            subtitle.setText(
                    "বাংলাদেশের সেরা রিচার্জ ব্যবসা প্ল্যাটফর্ম"
            );
            phone.setHint("ফোন");
            password.setHint("৬ ডিজিট পাসওয়ার্ড");
            forgot.setText("পাসওয়ার্ড ভুলে গেছেন?");
            register.setText("অ্যাকাউন্ট নেই?  রেজিস্টার করুন");
        } else {
            language.setText("BN     EN");
            subtitle.setText(
                    "Bangladesh's best recharge business platform"
            );
            phone.setHint("Phone");
            password.setHint("6 Digit Password");
            forgot.setText("Forgot Password?");
            register.setText("Don't have an account?  Register");
        }
    }

    static class GradientDrawableBox
            extends android.graphics.drawable.GradientDrawable {
    }
}
