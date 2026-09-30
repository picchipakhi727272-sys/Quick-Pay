package com.quickpay.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.view.View;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.security.MessageDigest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final int BLUE = Color.rgb(8,96,190);
    private static final int GREEN = Color.rgb(0,92,68);
    private static final int YELLOW = Color.rgb(255,190,25);
    private static final int DARK = Color.rgb(35,35,35);

    private SharedPreferences pref;

    /* =========================================================
       FIREBASE BACKEND
       ========================================================= */
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private ListenerRegistration chatListener;
    private ListenerRegistration profileListener;
    private ListenerRegistration liveListener;
    private final Handler liveHandler = new Handler(Looper.getMainLooper());
    private boolean firebaseReady = false;
    private String verificationId = "";
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private boolean pendingRegistration = false;
    private String pendingName = "";
    private String pendingPhone = "";
    private String pendingPassword = "";

    private String firebaseUid() {
        FirebaseUser u = firebaseAuth == null ? null : firebaseAuth.getCurrentUser();
        return u == null ? "" : u.getUid();
    }

    private String cleanPhone(String phone) {
        if (phone == null) return "";
        String p = phone.trim().replace(" ", "").replace("-", "");
        if (p.startsWith("+880")) return p;
        if (p.startsWith("880")) return "+" + p;
        if (p.startsWith("0") && p.length() == 11) return "+88" + p;
        return p;
    }

    private String firebaseError(Exception e) {
        if (e == null || e.getMessage() == null) return "Firebase error";
        return e.getMessage();
    }

    private String selectedMobileProvider = "à¦¬à¦¿à¦•à¦¾à¦¶";
    private String selectedAccountType = "à¦ªà¦¾à¦°à§à¦¸à§‹à¦¨à¦¾à¦²";
    private String selectedRechargeOperator = "GP";

    /* BILL PAY */
    private String selectedBillType = "";
    private String selectedBillCode = "";
    private int selectedBillColor = BLUE;

    /* =========================================================
       BASIC HELPERS
       ========================================================= */

    private int dp(float n) {
        return (int)(n * getResources().getDisplayMetrics().density + .5f);
    }

    private TextView tv(String s, float size, int color) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        return t;
    }

    private GradientDrawable bg(int color, float radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    private GradientDrawable outline(int color, int stroke, float radius) {
        GradientDrawable d = bg(color, radius);
        d.setStroke(dp(1), stroke);
        return d;
    }

    private void space(LinearLayout p, int h) {
        p.addView(
                new Space(this),
                new LinearLayout.LayoutParams(1, dp(h))
        );
    }

    private TextView button(String s, int color, int textColor) {
        TextView b = tv(s, 20, textColor);
        b.setGravity(Gravity.CENTER);
        b.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        b.setBackground(bg(color, 12));
        return b;
    }

    /* =========================================================
       CREATE
       ========================================================= */

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        pref = getSharedPreferences(
                "quick_pay",
                Context.MODE_PRIVATE
        );

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        firebaseReady = true;

        // Ensure a Firebase-authenticated session is available for Firestore requests.
        if (firebaseAuth.getCurrentUser() == null) {
            firebaseAuth.signInAnonymously();
        }

        if (pref.getBoolean("logged_in", false)) {

            if (pref.getString("pin", "").length() == 8) {
                showPinUnlock();
            } else {
                showPinSetup();
            }

        } else {
            showLogin();
        }
    }

    /* =========================================================
       FIREBASE SESSION / PROFILE
       ========================================================= */

    private void ensureFirebaseSession() {
        if (!firebaseReady || firebaseAuth == null) return;
        if (firebaseAuth.getCurrentUser() != null) syncCurrentProfile();
    }

    private String sha256(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format(Locale.US, "%02x", b));
            return sb.toString();
        } catch (Exception e) { return value; }
    }

    private void startPhoneVerification(String rawPhone, boolean registration, String name, String password) {
        String phone = cleanPhone(rawPhone);
        if (!phone.matches("\\+8801[3-9][0-9]{8}")) {
            Toast.makeText(this, "à¦¸à¦ à¦¿à¦• à¦¬à¦¾à¦‚à¦²à¦¾à¦¦à§‡à¦¶à¦¿ à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦¨à¦®à§à¦¬à¦° à¦¦à¦¿à¦¨", Toast.LENGTH_SHORT).show();
            return;
        }
        pendingRegistration = registration;
        pendingName = name == null ? "" : name;
        pendingPhone = phone;
        pendingPassword = password == null ? "" : password;

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override public void onVerificationCompleted(PhoneAuthCredential credential) {
                        signInWithPhoneCredential(credential);
                    }
                    @Override public void onVerificationFailed(com.google.firebase.FirebaseException e) {
                        Toast.makeText(MainActivity.this,
                                "OTP à¦ªà¦¾à¦ à¦¾à¦¨à§‹ à¦¯à¦¾à¦¯à¦¼à¦¨à¦¿: " + firebaseError(e), Toast.LENGTH_LONG).show();
                    }
                    @Override public void onCodeSent(String id, PhoneAuthProvider.ForceResendingToken token) {
                        verificationId = id;
                        resendToken = token;
                        showOtpDialog();
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void showOtpDialog() {
        final EditText otp = input("à§¬ à¦¡à¦¿à¦œà¦¿à¦Ÿ OTP", false);
        otp.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("OTP à¦¯à¦¾à¦šà¦¾à¦‡")
                .setMessage("" + pendingPhone + " à¦¨à¦®à§à¦¬à¦°à§‡ à¦ªà¦¾à¦ à¦¾à¦¨à§‹ OTP à¦²à¦¿à¦–à§à¦¨à¥¤")
                .setView(otp)
                .setNegativeButton("à¦¬à¦¾à¦¤à¦¿à¦²", null)
                .setPositiveButton("à¦¯à¦¾à¦šà¦¾à¦‡", null)
                .create();
        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String code = otp.getText().toString().trim();
            if (code.length() != 6) { otp.setError("à§¬ à¦¡à¦¿à¦œà¦¿à¦Ÿà§‡à¦° OTP à¦¦à¦¿à¦¨"); return; }
            if (verificationId.isEmpty()) { Toast.makeText(this,"OTP session à¦ªà¦¾à¦“à¦¯à¦¼à¦¾ à¦¯à¦¾à¦¯à¦¼à¦¨à¦¿à¥¤ à¦†à¦¬à¦¾à¦° à¦šà§‡à¦·à§à¦Ÿà¦¾ à¦•à¦°à§à¦¨à¥¤",Toast.LENGTH_SHORT).show(); return; }
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithPhoneCredential(credential);
            dialog.dismiss();
        }));
        dialog.show();
    }

    private void signInWithPhoneCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(result -> {
                    FirebaseUser user = result.getUser();
                    if (user == null) return;
                    firebaseReady = true;
                    if (pendingRegistration) {
                        createRegisteredProfile(user.getUid());
                    } else {
                        verifyExistingProfileAfterPhoneLogin(user.getUid());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this,
                        "OTP à¦¯à¦¾à¦šà¦¾à¦‡ à¦¬à§à¦¯à¦°à§à¦¥ à¦¹à¦¯à¦¼à§‡à¦›à§‡à¥¤", Toast.LENGTH_LONG).show());
    }

    private void createRegisteredProfile(String uid) {
        Map<String,Object> data = new HashMap<>();
        data.put("uid", uid);
        data.put("name", pendingName);
        data.put("phone", pendingPhone);
        data.put("passwordHash", sha256(pendingPassword));
        data.put("userId", uid.substring(0, Math.min(8, uid.length())).toUpperCase(Locale.US));
        data.put("mainBalance", 0.0);
        data.put("driveBalance", 0.0);
        data.put("mainBalanceLocked", false);
        data.put("driveBalanceLocked", false);
        data.put("accountLocked", false);
        data.put("status", "ACTIVE");
        data.put("createdAt", com.google.firebase.firestore.FieldValue.serverTimestamp());
        data.put("updatedAt", com.google.firebase.firestore.FieldValue.serverTimestamp());
        firestore.collection("users").document(uid).set(data)
                .addOnSuccessListener(v -> {
                    pref.edit().putString("name", pendingName).putString("phone", pendingPhone)
                            .putString("password", pendingPassword).putBoolean("logged_in", true).apply();
                    cacheProfile(0,0,false,false,false);
                    Toast.makeText(this,"à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦¤à§ˆà¦°à¦¿ à¦¹à¦¯à¦¼à§‡à¦›à§‡à¥¤",Toast.LENGTH_SHORT).show();
                    showPinSetup();
                    listenToProfile();
                })
                .addOnFailureListener(e -> Toast.makeText(this,
                        "à¦ªà§à¦°à§‹à¦«à¦¾à¦‡à¦² à¦¤à§ˆà¦°à¦¿ à¦•à¦°à¦¾ à¦¯à¦¾à¦¯à¦¼à¦¨à¦¿: " + firebaseError(e), Toast.LENGTH_LONG).show());
    }

    private void verifyExistingProfileAfterPhoneLogin(String uid) {
        firestore.collection("users").document(uid).get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) {
                        firebaseAuth.signOut();
                        Toast.makeText(this,"à¦à¦‡ à¦¨à¦®à§à¦¬à¦°à§‡à¦° Quick Pay à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦ªà¦¾à¦“à¦¯à¦¼à¦¾ à¦¯à¦¾à¦¯à¦¼à¦¨à¦¿à¥¤ à¦°à§‡à¦œà¦¿à¦¸à§à¦Ÿà¦¾à¦° à¦•à¦°à§à¦¨à¥¤",Toast.LENGTH_LONG).show();
                        return;
                    }
                    String savedHash = snapshot.getString("passwordHash");
                    if (savedHash != null && !savedHash.equals(sha256(pendingPassword))) {
                        firebaseAuth.signOut();
                        Toast.makeText(this,"à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡ à¦¸à¦ à¦¿à¦• à¦¨à¦¯à¦¼à¥¤",Toast.LENGTH_LONG).show();
                        return;
                    }
                    pref.edit().putString("name", snapshot.getString("name") == null ? "" : snapshot.getString("name"))
                            .putString("phone", pendingPhone)
                            .putString("password", pendingPassword)
                            .putBoolean("logged_in", true).apply();
                    cacheProfile(
                            snapshot.getDouble("mainBalance") == null ? 0 : snapshot.getDouble("mainBalance"),
                            snapshot.getDouble("driveBalance") == null ? 0 : snapshot.getDouble("driveBalance"),
                            Boolean.TRUE.equals(snapshot.getBoolean("mainBalanceLocked")),
                            Boolean.TRUE.equals(snapshot.getBoolean("driveBalanceLocked")),
                            Boolean.TRUE.equals(snapshot.getBoolean("accountLocked")));
                    listenToProfile();
                    if (pref.getString("pin", "").length() == 8) showHome(); else showPinSetup();
                })
                .addOnFailureListener(e -> Toast.makeText(this,"à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦¤à¦¥à§à¦¯ à¦ªà¦¡à¦¼à¦¾ à¦¯à¦¾à¦¯à¦¼à¦¨à¦¿à¥¤",Toast.LENGTH_LONG).show());
    }

    private void syncCurrentProfile() {
        if (!firebaseReady || firestore == null || firebaseUid().isEmpty()) return;
        String uid = firebaseUid();
        Map<String,Object> data = new HashMap<>();
        data.put("uid", uid);
        data.put("name", pref.getString("name", ""));
        data.put("phone", pref.getString("phone", ""));
        data.put("userId", uid.substring(0, Math.min(8, uid.length())).toUpperCase(Locale.US));
        data.put("updatedAt", com.google.firebase.firestore.FieldValue.serverTimestamp());

        firestore.collection("users").document(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (!snapshot.exists()) {
                        data.put("mainBalance", 0.0);
                        data.put("driveBalance", 0.0);
                        data.put("mainBalanceLocked", false);
                        data.put("driveBalanceLocked", false);
                        data.put("accountLocked", false);
                        data.put("status", "ACTIVE");
                        data.put("createdAt", com.google.firebase.firestore.FieldValue.serverTimestamp());
                        firestore.collection("users").document(uid).set(data)
                                .addOnSuccessListener(v -> cacheProfile(0,0,false,false,false));
                    } else {
                        cacheProfile(
                                snapshot.getDouble("mainBalance") == null ? 0 : snapshot.getDouble("mainBalance"),
                                snapshot.getDouble("driveBalance") == null ? 0 : snapshot.getDouble("driveBalance"),
                                Boolean.TRUE.equals(snapshot.getBoolean("mainBalanceLocked")),
                                Boolean.TRUE.equals(snapshot.getBoolean("driveBalanceLocked")),
                                Boolean.TRUE.equals(snapshot.getBoolean("accountLocked"))
                        );
                    }
                    listenToProfile();
                });
    }

    private void cacheProfile(double main, double drive, boolean mainLocked, boolean driveLocked, boolean accountLocked) {
        pref.edit()
                .putString("main_balance", String.valueOf(Math.max(0, main)))
                .putString("drive_balance", String.valueOf(Math.max(0, drive)))
                .putBoolean("main_balance_locked", mainLocked)
                .putBoolean("drive_balance_locked", driveLocked)
                .putBoolean("account_locked", accountLocked)
                .apply();
    }

    private void listenToProfile() {
        if (!firebaseReady || firestore == null || firebaseUid().isEmpty()) return;
        if (profileListener != null) profileListener.remove();
        profileListener = firestore.collection("users").document(firebaseUid())
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null || snapshot == null || !snapshot.exists()) return;
                    double main = snapshot.getDouble("mainBalance") == null ? 0 : snapshot.getDouble("mainBalance");
                    double drive = snapshot.getDouble("driveBalance") == null ? 0 : snapshot.getDouble("driveBalance");
                    cacheProfile(main, drive,
                            Boolean.TRUE.equals(snapshot.getBoolean("mainBalanceLocked")),
                            Boolean.TRUE.equals(snapshot.getBoolean("driveBalanceLocked")),
                            Boolean.TRUE.equals(snapshot.getBoolean("accountLocked")));
                });
    }

    private boolean transactionBlocked() {
        if (pref.getBoolean("account_locked", false) || pref.getBoolean("main_balance_locked", false)) {
            Toast.makeText(this, "à¦†à¦ªà¦¨à¦¾à¦° à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿà§‡ à¦ªà¦°à§à¦¯à¦¾à¦ªà§à¦¤ à¦¬à§à¦¯à¦¾à¦²à§‡à¦¨à§à¦¸ à¦¨à¦¾à¦‡", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private boolean transactionBlockedFor(double amount) {
        if (transactionBlocked()) return true;
        double balance = getMainBalance();
        if (amount > 0 && balance < amount) {
            Toast.makeText(this, "à¦†à¦ªà¦¨à¦¾à¦° à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿà§‡ à¦ªà¦°à§à¦¯à¦¾à¦ªà§à¦¤ à¦¬à§à¦¯à¦¾à¦²à§‡à¦¨à§à¦¸ à¦¨à¦¾à¦‡", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private void writeFirestoreTransaction(String type, String detail, double amount, boolean credit, String status) {
        if (!firebaseReady || firestore == null || firebaseUid().isEmpty()) return;
        Map<String,Object> m = new HashMap<>();
        m.put("uid", firebaseUid());
        m.put("userId", pref.getString("phone", ""));
        m.put("name", pref.getString("name", ""));
        m.put("phone", pref.getString("phone", ""));
        m.put("type", type);
        m.put("detail", detail);
        m.put("amount", Math.max(0, amount));
        m.put("credit", credit);
        m.put("status", status);
        m.put("createdAt", com.google.firebase.firestore.FieldValue.serverTimestamp());
        firestore.collection("transactions").add(m);
    }

    private void submitDepositRequestToFirebase(double amount, String transactionId) {
        if (!firebaseReady || firestore == null || firebaseAuth == null) {
            Toast.makeText(this, "Firebase à¦¸à¦‚à¦¯à§‹à¦— à¦¨à§‡à¦‡à¥¤ à¦ªà¦°à§‡ à¦†à¦¬à¦¾à¦° à¦šà§‡à¦·à§à¦Ÿà¦¾ à¦•à¦°à§à¦¨à¥¤", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseUser current = firebaseAuth.getCurrentUser();
        if (current == null) {
            firebaseAuth.signInAnonymously()
                    .addOnSuccessListener(result -> writeDepositRequest(amount, transactionId))
                    .addOnFailureListener(e -> Toast.makeText(this,
                            "Firebase à¦²à¦—à¦‡à¦¨ à¦•à¦°à¦¾ à¦¯à¦¾à¦¯à¦¼à¦¨à¦¿: " + firebaseError(e), Toast.LENGTH_LONG).show());
            return;
        }
        writeDepositRequest(amount, transactionId);
    }

    private void writeDepositRequest(double amount, String transactionId) {
        String uid = firebaseUid();
        if (uid.isEmpty()) {
            Toast.makeText(this, "Firebase à¦‡à¦‰à¦œà¦¾à¦° à¦¸à§‡à¦¶à¦¨ à¦ªà¦¾à¦“à¦¯à¦¼à¦¾ à¦¯à¦¾à¦¯à¦¼à¦¨à¦¿à¥¤", Toast.LENGTH_LONG).show();
            return;
        }
        Map<String,Object> m = new HashMap<>();
        m.put("uid", uid);
        m.put("name", pref.getString("name", ""));
        m.put("phone", pref.getString("phone", ""));
        m.put("amount", amount);
        m.put("method", "Manual Deposit");
        m.put("transactionId", transactionId);
        m.put("status", "PENDING");
        m.put("createdAt", com.google.firebase.firestore.FieldValue.serverTimestamp());
        firestore.collection("depositRequests").add(m)
                .addOnSuccessListener(v -> {
                    recordTransaction("à¦…à§à¦¯à¦¾à¦¡ à¦®à¦¾à¦¨à¦¿", "à¦¡à¦¿à¦ªà§‹à¦œà¦¿à¦Ÿ / " + transactionId, amount, true, "PENDING");
                    Toast.makeText(this,
                            "à¦¡à¦¿à¦ªà§‹à¦œà¦¿à¦Ÿ à¦°à¦¿à¦•à§‹à¦¯à¦¼à§‡à¦¸à§à¦Ÿ à¦…à§à¦¯à¦¾à¦¡à¦®à¦¿à¦¨à§‡à¦° à¦•à¦¾à¦›à§‡ à¦ªà¦¾à¦ à¦¾à¦¨à§‹ à¦¹à¦¯à¦¼à§‡à¦›à§‡à¥¤",
                            Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> Toast.makeText(this,
                        "à¦¡à¦¿à¦ªà§‹à¦œà¦¿à¦Ÿ à¦°à¦¿à¦•à§‹à¦¯à¦¼à§‡à¦¸à§à¦Ÿ à¦ªà¦¾à¦ à¦¾à¦¨à§‹ à¦¯à¦¾à¦¯à¦¼à¦¨à¦¿: " + firebaseError(e),
                        Toast.LENGTH_LONG).show());
    }

    private void submitFirestoreRequest(String collection, Map<String,Object> data) {
        if (!firebaseReady || firestore == null || firebaseUid().isEmpty()) {
            Toast.makeText(this, "Firebase à¦¸à¦‚à¦¯à§‹à¦— à¦¨à§‡à¦‡à¥¤ à¦ªà¦°à§‡ à¦†à¦¬à¦¾à¦° à¦šà§‡à¦·à§à¦Ÿà¦¾ à¦•à¦°à§à¦¨à¥¤", Toast.LENGTH_LONG).show();
            return;
        }
        data.put("userId", firebaseUid());
        data.put("uid", firebaseUid());
        data.put("name", pref.getString("name", ""));
        data.put("phone", pref.getString("phone", ""));
        if (!data.containsKey("status")) data.put("status", "PENDING");
        if (!data.containsKey("createdAt")) data.put("createdAt", com.google.firebase.firestore.FieldValue.serverTimestamp());
        firestore.collection(collection).add(data);
    }

    private void refreshPaymentNumbers() {
        if (!firebaseReady || firestore == null) return;
        firestore.collection("settings").document("paymentNumbers").get().addOnSuccessListener(doc -> {
            if (doc.exists()) {
                cachePaymentNumber("bkash", doc.getString("bkash"), doc.getBoolean("bkashActive"));
                cachePaymentNumber("nagad", doc.getString("nagad"), doc.getBoolean("nagadActive"));
                cachePaymentNumber("rocket", doc.getString("rocket"), doc.getBoolean("rocketActive"));
            }
            firestore.collection("paymentNumbers").document("bkash").get().addOnSuccessListener(d -> { if (d.exists()) cachePaymentNumber("bkash", d.getString("number"), d.getBoolean("active")); });
            firestore.collection("paymentNumbers").document("nagad").get().addOnSuccessListener(d -> { if (d.exists()) cachePaymentNumber("nagad", d.getString("number"), d.getBoolean("active")); });
            firestore.collection("paymentNumbers").document("rocket").get().addOnSuccessListener(d -> { if (d.exists()) cachePaymentNumber("rocket", d.getString("number"), d.getBoolean("active")); });
        });
    }

    private void cachePaymentNumber(String key, String number, Boolean active) {
        if (number != null) pref.edit().putString("payment_" + key, number).apply();
        if (active != null) pref.edit().putBoolean("payment_" + key + "_active", active).apply();
    }

    private String paymentNumberFor(String name) {
        String key = name.toLowerCase(Locale.US);
        if (key.contains("bkash") || key.contains("à¦¬à¦¿à¦•à¦¾à¦¶")) return pref.getBoolean("payment_bkash_active", true) ? pref.getString("payment_bkash", "") : "";
        if (key.contains("nagad") || key.contains("à¦¨à¦—à¦¦")) return pref.getBoolean("payment_nagad_active", true) ? pref.getString("payment_nagad", "") : "";
        if (key.contains("rocket") || key.contains("à¦°à¦•à§‡à¦Ÿ")) return pref.getBoolean("payment_rocket_active", true) ? pref.getString("payment_rocket", "") : "";
        return "";
    }

    /* =========================================================
       INPUT
       ========================================================= */

    private EditText input(String hint, boolean password) {

        EditText e = new EditText(this);

        e.setHint(hint);
        e.setTextSize(17);
        e.setSingleLine(true);

        e.setPadding(
                dp(18),
                0,
                dp(18),
                0
        );

        e.setTextColor(DARK);
        e.setHintTextColor(Color.GRAY);
        e.setBackground(bg(Color.WHITE, 12));

        String h = hint == null ? "" : hint;

        boolean numeric =
                password
                || h.contains("à¦«à§‹à¦¨")
                || h.contains("à¦¨à¦®à§à¦¬à¦°")
                || h.contains("à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£")
                || h.contains("PIN")
                || h.contains("à¦ªà¦¿à¦¨");

        if (numeric) {

            if (password) {

                e.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                                | InputType.TYPE_NUMBER_VARIATION_PASSWORD
                );

            } else {

                e.setInputType(
                        InputType.TYPE_CLASS_PHONE
                );
            }

        } else {

            e.setInputType(
                    InputType.TYPE_CLASS_TEXT
                            | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            );
        }

        return e;
    }

    private EditText pinInput(String hint) {

        EditText e = input(hint, true);

        e.setGravity(Gravity.CENTER);
        e.setTextSize(20);

        e.setBackground(
                outline(
                        Color.rgb(248,250,253),
                        Color.rgb(215,225,235),
                        15
                )
        );

        return e;
    }

    /* =========================================================
       USER REQUESTED FEATURES
       ========================================================= */
    private boolean isEnglish(){return pref.getBoolean("language_english",false);}
    private String tx(String bn,String en){return isEnglish()?en:bn;}
    private void toggleLanguage(){pref.edit().putBoolean("language_english",!isEnglish()).apply();if(pref.getBoolean("logged_in",false))showHome();else showLogin();}
    private String now(){return new SimpleDateFormat("dd/MM/yyyy hh:mm a",Locale.getDefault()).format(new Date());}
    private void recordTransaction(String type,String detail,double amount,boolean credit){
        recordTransaction(type,detail,amount,credit,"SUCCESS");
    }

    private void recordTransaction(String type,String detail,double amount,boolean credit,String status){
        try {
            JSONArray a=new JSONArray(pref.getString("transaction_history","[]"));
            JSONObject o=new JSONObject();
            o.put("type",type);
            o.put("detail",detail);
            o.put("amount",Math.max(0,amount));
            o.put("credit",credit);
            o.put("status",status);
            o.put("time",now());
            a.put(0,o);
            pref.edit().putString("transaction_history",a.toString()).apply();
        } catch(Exception ignored) {}
        writeFirestoreTransaction(type,detail,amount,credit,status);
    }
    private String panelText(String key,String fallback){String v=pref.getString(key,"");return v.trim().isEmpty()?fallback:v;}
    private JSONArray panelArray(String key){try{return new JSONArray(pref.getString(key,"[]"));}catch(Exception e){return new JSONArray();}}
    private void openExternal(String url){try{startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));}catch(Exception e){Toast.makeText(this,tx("à¦²à¦¿à¦‚à¦•à¦Ÿà¦¿ à¦–à§‹à¦²à¦¾ à¦¯à¦¾à¦šà§à¦›à§‡ à¦¨à¦¾","Unable to open link"),Toast.LENGTH_SHORT).show();}}
    private void addLiveActivityTicker(LinearLayout header){
        LinearLayout live=new LinearLayout(this); live.setGravity(Gravity.CENTER_VERTICAL); live.setBackground(bg(Color.WHITE,14)); live.setPadding(dp(8),0,dp(8),0);
        TextView badge=tv("â— LIVE",12,Color.RED); badge.setTypeface(Typeface.DEFAULT,Typeface.BOLD); live.addView(badge,new LinearLayout.LayoutParams(dp(62),dp(38)));
        TextView a=tv("Quick Pay Live Activity",12,Color.DKGRAY); a.setSingleLine(true); a.setEllipsize(android.text.TextUtils.TruncateAt.MARQUEE); a.setMarqueeRepeatLimit(-1); a.setSelected(true); live.addView(a,new LinearLayout.LayoutParams(0,dp(38),1));
        header.addView(live,new LinearLayout.LayoutParams(-1,dp(38)));
        if (!firebaseReady || firestore == null || firebaseUid().isEmpty()) return;
        if (liveListener != null) liveListener.remove();
        liveListener=firestore.collection("liveActivities").whereEqualTo("active",true).limit(500).addSnapshotListener((snap,error)->{
            if(error!=null || snap==null || snap.isEmpty()){ a.setText("Quick Pay Live Activity"); return; }
            java.util.ArrayList<String> items=new java.util.ArrayList<>();
            for(DocumentSnapshot d:snap.getDocuments()){
                String text=d.getString("text");
                if(text==null || text.trim().isEmpty()){ String name=d.getString("name"); String phone=d.getString("phone"); String type=d.getString("type"); Double amount=d.getDouble("amount"); String when=d.getString("displayTime"); StringBuilder b=new StringBuilder(); if(name!=null)b.append(name); if(phone!=null&&!phone.isEmpty())b.append(" (").append(phone).append(")"); if(amount!=null)b.append(" ").append(String.format(Locale.getDefault(),"à§³ %.0f",amount)); if(type!=null&&!type.isEmpty())b.append(" ").append(type); if(when!=null&&!when.isEmpty())b.append(" â€¢ ").append(when); text=b.toString().trim(); }
                if(!text.isEmpty()) items.add(text);
            }
            if(items.isEmpty()){a.setText("Quick Pay Live Activity");return;}
            liveHandler.removeCallbacksAndMessages(null); int[] ix={0};
            Runnable rotate=new Runnable(){public void run(){ if(items.isEmpty())return; a.setText(items.get(ix[0]%items.size())); ix[0]++; int sec=Math.max(3,Math.min(60,pref.getInt("live_rotate_seconds",5))); liveHandler.postDelayed(this,sec*1000L); }}; rotate.run();
        });
        firestore.collection("settings").document("general").get().addOnSuccessListener(d->{Long sec=d.getLong("liveRotateSeconds"); if(sec!=null)pref.edit().putInt("live_rotate_seconds",Math.max(3,Math.min(60,sec.intValue()))).apply();});
    }
    private void addProfileItem(LinearLayout parent,String icon,String text,View.OnClickListener click){LinearLayout r=new LinearLayout(this);r.setGravity(Gravity.CENTER_VERTICAL);r.setPadding(dp(14),0,dp(10),0);r.setBackground(bg(Color.WHITE,14));TextView i=tv(icon,22,DARK);i.setGravity(Gravity.CENTER);r.addView(i,new LinearLayout.LayoutParams(dp(55),dp(62)));TextView t=tv(text,18,DARK);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);r.addView(t,new LinearLayout.LayoutParams(0,dp(62),1));TextView a=tv("â€º",30,Color.GRAY);a.setGravity(Gravity.CENTER);r.addView(a,new LinearLayout.LayoutParams(dp(35),dp(62)));parent.addView(r,new LinearLayout.LayoutParams(-1,dp(62)));space(parent,1);r.setOnClickListener(click);}
    private void showTransactionHistory(){getWindow().setStatusBarColor(BLUE);getWindow().setNavigationBarColor(Color.WHITE);LinearLayout main=new LinearLayout(this);main.setOrientation(LinearLayout.VERTICAL);main.setBackgroundColor(Color.rgb(247,248,250));setContentView(main);LinearLayout h=new LinearLayout(this);h.setGravity(Gravity.CENTER_VERTICAL);h.setBackgroundColor(BLUE);main.addView(h,new LinearLayout.LayoutParams(-1,dp(62)));TextView b=tv("â€¹",38,Color.WHITE);b.setGravity(Gravity.CENTER);h.addView(b,new LinearLayout.LayoutParams(dp(52),dp(62)));b.setOnClickListener(v->showHome());TextView t=tv(tx("à¦²à§‡à¦¨à¦¦à§‡à¦¨","Transactions"),21,Color.WHITE);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);h.addView(t,new LinearLayout.LayoutParams(0,dp(62),1));ScrollView sc=new ScrollView(this);LinearLayout list=new LinearLayout(this);list.setOrientation(LinearLayout.VERTICAL);list.setPadding(dp(12),dp(12),dp(12),dp(20));sc.addView(list);main.addView(sc,new LinearLayout.LayoutParams(-1,0,1));JSONArray a=panelArray("transaction_history");if(a.length()==0){TextView e=tv(tx("à¦à¦–à¦¨à¦“ à¦•à§‹à¦¨à§‹ à¦²à§‡à¦¨à¦¦à§‡à¦¨ à¦¨à§‡à¦‡","No transactions yet"),17,Color.GRAY);e.setGravity(Gravity.CENTER);list.addView(e,new LinearLayout.LayoutParams(-1,dp(100)));return;}for(int i=0;i<a.length();i++)try{JSONObject o=a.getJSONObject(i);String type=o.optString("type","Transaction"),detail=o.optString("detail",""),time=o.optString("time","");double amount=o.optDouble("amount",0);boolean credit=o.optBoolean("credit",false);LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(16),dp(12),dp(16),dp(12));c.setBackground(bg(Color.WHITE,16));LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,-2);cp.setMargins(0,0,0,dp(10));list.addView(c,cp);LinearLayout r=new LinearLayout(this);r.setGravity(Gravity.CENTER_VERTICAL);TextView tt=tv(type,16,DARK);tt.setTypeface(Typeface.DEFAULT,Typeface.BOLD);r.addView(tt,new LinearLayout.LayoutParams(0,dp(28),1));TextView am=tv((credit?"+ ":"- ")+String.format(Locale.getDefault(),"à§³ %.2f",amount),16,credit?Color.rgb(30,150,80):Color.rgb(190,45,45));am.setTypeface(Typeface.DEFAULT,Typeface.BOLD);r.addView(am,new LinearLayout.LayoutParams(-2,dp(28)));c.addView(r);c.addView(tv(detail,13,Color.DKGRAY),new LinearLayout.LayoutParams(-1,dp(26)));c.addView(tv(time+"   â€¢   SUCCESS",11,Color.GRAY),new LinearLayout.LayoutParams(-1,dp(22)));}catch(Exception ignored){}}
    private void showProfile(){getWindow().setStatusBarColor(BLUE);getWindow().setNavigationBarColor(Color.WHITE);LinearLayout main=new LinearLayout(this);main.setOrientation(LinearLayout.VERTICAL);main.setBackgroundColor(Color.rgb(247,248,250));setContentView(main);LinearLayout h=new LinearLayout(this);h.setGravity(Gravity.CENTER_VERTICAL);h.setBackgroundColor(BLUE);main.addView(h,new LinearLayout.LayoutParams(-1,dp(62)));TextView title=tv(tx("à¦ªà§à¦°à§‹à¦«à¦¾à¦‡à¦²","Profile"),24,Color.WHITE);title.setGravity(Gravity.CENTER);title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);h.addView(title,new LinearLayout.LayoutParams(0,dp(62),1));TextView out=tv("â‡¥",28,Color.WHITE);out.setGravity(Gravity.CENTER);h.addView(out,new LinearLayout.LayoutParams(dp(60),dp(62)));out.setOnClickListener(v->{pref.edit().putBoolean("logged_in",false).apply();showLogin();});ScrollView sc=new ScrollView(this);LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(16),dp(16),dp(16),dp(20));sc.addView(c);main.addView(sc,new LinearLayout.LayoutParams(-1,0,1));LinearLayout card=new LinearLayout(this);card.setOrientation(LinearLayout.VERTICAL);card.setPadding(dp(20),dp(18),dp(20),dp(18));card.setBackground(bg(Color.WHITE,18));c.addView(card,new LinearLayout.LayoutParams(-1,-2));TextView n=tv(pref.getString("name","Rosy"),25,DARK);n.setTypeface(Typeface.DEFAULT,Typeface.BOLD);card.addView(n,new LinearLayout.LayoutParams(-1,dp(40)));card.addView(tv(pref.getString("phone",""),16,Color.GRAY),new LinearLayout.LayoutParams(-1,dp(30)));card.addView(tv("à¦°à§‡à¦«à¦¾à¦° à¦•à§‹à¦¡: "+getInviteCode(),15,Color.DKGRAY),new LinearLayout.LayoutParams(-1,dp(30)));space(c,12);addProfileItem(c,"ðŸ›¡",tx("à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡ à¦ªà¦°à¦¿à¦¬à¦°à§à¦¤à¦¨","Change Password"),v->showChangePassword());addProfileItem(c,"ðŸ›¡",tx("à¦ªà¦¿à¦¨ à¦ªà¦°à¦¿à¦¬à¦°à§à¦¤à¦¨","Change PIN"),v->showChangePin());addProfileItem(c,"ðŸŽ§",tx("à¦•à¦¾à¦¸à§à¦Ÿà¦®à¦¾à¦° à¦•à§‡à§Ÿà¦¾à¦°","Customer Care"),v->showCustomerCare());addProfileItem(c,"ðŸŒ",tx("à¦†à¦®à¦¾à¦¦à§‡à¦° à¦¸à¦®à§à¦ªà¦°à§à¦•à§‡","About Us"),v->showAboutUs());TextView ver=tv("à¦­à¦¾à¦°à§à¦¸à¦¨ 1.0.0",15,Color.GRAY);ver.setGravity(Gravity.CENTER);c.addView(ver,new LinearLayout.LayoutParams(-1,dp(60)));}
    private void showAboutUs(){getWindow().setStatusBarColor(BLUE);getWindow().setNavigationBarColor(Color.WHITE);LinearLayout main=new LinearLayout(this);main.setOrientation(LinearLayout.VERTICAL);main.setBackgroundColor(Color.rgb(247,248,250));setContentView(main);LinearLayout h=new LinearLayout(this);h.setGravity(Gravity.CENTER_VERTICAL);h.setBackgroundColor(BLUE);main.addView(h,new LinearLayout.LayoutParams(-1,dp(62)));TextView b=tv("â€¹",38,Color.WHITE);b.setGravity(Gravity.CENTER);h.addView(b,new LinearLayout.LayoutParams(dp(52),dp(62)));b.setOnClickListener(v->showProfile());TextView t=tv(tx("à¦†à¦®à¦¾à¦¦à§‡à¦° à¦¸à¦®à§à¦ªà¦°à§à¦•à§‡","About Us"),21,Color.WHITE);t.setTypeface(Typeface.DEFAULT,Typeface.BOLD);h.addView(t,new LinearLayout.LayoutParams(0,dp(62),1));ScrollView sc=new ScrollView(this);LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(16),dp(16),dp(16),dp(30));sc.addView(c);main.addView(sc,new LinearLayout.LayoutParams(-1,0,1));String[] keys={"about_company_name","about_intro","about_status","about_license","about_registration","about_validity","about_address","about_phone","about_extra"};String[] labels={"à¦ªà§à¦°à¦¤à¦¿à¦·à§à¦ à¦¾à¦¨à§‡à¦° à¦¨à¦¾à¦®","à¦ªà¦°à¦¿à¦šà¦¿à¦¤à¦¿","à¦ªà§à¦°à¦¤à¦¿à¦·à§à¦ à¦¾à¦¨à§‡à¦° à¦§à¦°à¦¨/à¦¸à§à¦Ÿà§à¦¯à¦¾à¦Ÿà¦¾à¦¸","à¦²à¦¾à¦‡à¦¸à§‡à¦¨à§à¦¸ à¦¤à¦¥à§à¦¯","à¦°à§‡à¦œà¦¿à¦¸à§à¦Ÿà§à¦°à§‡à¦¶à¦¨ à¦¤à¦¥à§à¦¯","à¦²à¦¾à¦‡à¦¸à§‡à¦¨à§à¦¸/à¦°à§‡à¦œà¦¿à¦¸à§à¦Ÿà§à¦°à§‡à¦¶à¦¨à§‡à¦° à¦®à§‡à§Ÿà¦¾à¦¦","à¦ à¦¿à¦•à¦¾à¦¨à¦¾","à¦¯à§‹à¦—à¦¾à¦¯à§‹à¦—","à¦…à¦¤à¦¿à¦°à¦¿à¦•à§à¦¤ à¦¤à¦¥à§à¦¯"};int added=0;for(int i=0;i<keys.length;i++){String v=pref.getString(keys[i],"");if(v.trim().isEmpty())continue;added++;TextView l=tv(labels[i],13,BLUE);l.setTypeface(Typeface.DEFAULT,Typeface.BOLD);c.addView(l,new LinearLayout.LayoutParams(-1,dp(26)));TextView val=tv(v,16,DARK);val.setPadding(dp(14),dp(12),dp(14),dp(12));val.setBackground(bg(Color.WHITE,14));c.addView(val,new LinearLayout.LayoutParams(-1,-2));space(c,10);}if(added==0){TextView v=tv(tx("à¦†à¦®à¦¾à¦¦à§‡à¦° à¦¸à¦®à§à¦ªà¦°à§à¦•à§‡ à¦¤à¦¥à§à¦¯ Panel à¦¥à§‡à¦•à§‡ à¦¯à§‹à¦— à¦•à¦°à¦¾ à¦¹à¦¬à§‡à¥¤","About information will be provided from the panel."),16,Color.GRAY);c.addView(v,new LinearLayout.LayoutParams(-1,dp(100)));}}

    /* =========================================================
       LOGIN
       ========================================================= */

    private void showLogin() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        root.setPadding(
                dp(25),
                dp(18),
                dp(25),
                dp(18)
        );

        root.setBackgroundColor(BLUE);

        ScrollView scroll = new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.addView(root);

        setContentView(scroll);

        TextView lang = tv(isEnglish()?"BN     English":"à¦¬à¦¾à¦‚à¦²à¦¾     EN",16,Color.WHITE);

        lang.setGravity(Gravity.CENTER);
        lang.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        lang.setBackground(bg(Color.rgb(55,130,205),40));
        lang.setOnClickListener(v -> toggleLanguage());

        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(
                        dp(175),
                        dp(50)
                );

        lp.gravity = Gravity.RIGHT;
        root.addView(lang,lp);

        space(root,45);

        TextView logo = tv("Quick Pay",32,BLUE);

        logo.setGravity(Gravity.CENTER);
        logo.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        logo.setBackground(bg(Color.WHITE,18));

        root.addView(
                logo,
                new LinearLayout.LayoutParams(
                        dp(300),
                        dp(95)
                )
        );

        TextView sub =
                tv(
                        "à¦¬à¦¾à¦‚à¦²à¦¾à¦¦à§‡à¦¶à§‡à¦° à¦¸à§‡à¦°à¦¾ à¦°à¦¿à¦šà¦¾à¦°à§à¦œ à¦¬à§à¦¯à¦¬à¦¸à¦¾ à¦ªà§à¦²à§à¦¯à¦¾à¦Ÿà¦«à¦°à§à¦®",
                        16,
                        Color.WHITE
                );

        sub.setGravity(Gravity.CENTER);

        root.addView(
                sub,
                new LinearLayout.LayoutParams(-1,dp(45))
        );

        space(root,15);

        EditText phone = input("à¦«à§‹à¦¨",false);

        root.addView(
                phone,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        space(root,14);

        LinearLayout passBox = new LinearLayout(this);

        passBox.setGravity(Gravity.CENTER_VERTICAL);
        passBox.setPadding(dp(5),0,dp(5),0);
        passBox.setBackground(bg(Color.WHITE,12));

        EditText pass = input("à§¬ à¦¡à¦¿à¦œà¦¿à¦Ÿ à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡",true);
        pass.setBackgroundColor(Color.TRANSPARENT);

        passBox.addView(
                pass,
                new LinearLayout.LayoutParams(0,dp(62),1)
        );

        TextView eye = tv("â—‰",23,BLUE);
        eye.setGravity(Gravity.CENTER);

        passBox.addView(
                eye,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        root.addView(
                passBox,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        eye.setOnClickListener(v -> {

            boolean hidden =
                    (pass.getInputType()
                            & InputType.TYPE_NUMBER_VARIATION_PASSWORD)
                            != 0;

            if (hidden) {
                pass.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                pass.setInputType(
                        InputType.TYPE_CLASS_NUMBER
                                | InputType.TYPE_NUMBER_VARIATION_PASSWORD
                );
            }

            pass.setSelection(pass.length());
        });

        space(root,20);

        TextView login = button("à¦²à¦—à¦‡à¦¨",Color.WHITE,BLUE);

        root.addView(
                login,
                new LinearLayout.LayoutParams(-1,dp(60))
        );

        login.setOnClickListener(v -> {

            String p = phone.getText().toString().trim();
            String pw = pass.getText().toString().trim();

            if (p.isEmpty()) {
                phone.setError("à¦«à§‹à¦¨ à¦¨à¦®à§à¦¬à¦° à¦¦à¦¿à¦¨");
                return;
            }

            if (pw.isEmpty()) {
                pass.setError("à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡ à¦¦à¦¿à¦¨");
                return;
            }

            startPhoneVerification(p, false, pref.getString("name", ""), pw);
        });

        TextView forgot =
                tv(
                        "à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡ à¦­à§à¦²à§‡ à¦—à§‡à¦›à§‡à¦¨?",
                        16,
                        Color.WHITE
                );

        forgot.setGravity(Gravity.CENTER);

        root.addView(
                forgot,
                new LinearLayout.LayoutParams(-1,dp(48))
        );

        forgot.setOnClickListener(v -> showForgotPassword());

        TextView reg =
                tv(
                        "à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦¨à§‡à¦‡?  à¦°à§‡à¦œà¦¿à¦¸à§à¦Ÿà¦¾à¦° à¦•à¦°à§à¦¨",
                        17,
                        Color.WHITE
                );

        reg.setGravity(Gravity.CENTER);
        reg.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        root.addView(
                reg,
                new LinearLayout.LayoutParams(-1,dp(52))
        );

        reg.setOnClickListener(v -> showRegister());
    }

    /* =========================================================
       PIN SETUP
       ========================================================= */

    private void showPinSetup() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout screen = new LinearLayout(this);

        screen.setOrientation(LinearLayout.VERTICAL);
        screen.setGravity(Gravity.CENTER);

        screen.setPadding(
                dp(20),dp(15),dp(20),dp(15)
        );

        screen.setBackgroundColor(BLUE);

        setContentView(screen);

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_HORIZONTAL);

        card.setPadding(
                dp(25),dp(28),dp(25),dp(22)
        );

        card.setBackground(bg(Color.rgb(250,252,250),28));

        screen.addView(
                card,
                new LinearLayout.LayoutParams(-1,dp(500))
        );

        TextView lock = tv("ðŸ”’",50,BLUE);
        lock.setGravity(Gravity.CENTER);
        lock.setBackground(bg(Color.rgb(232,240,250),70));

        card.addView(
                lock,
                new LinearLayout.LayoutParams(dp(105),dp(105))
        );

        space(card,18);

        TextView title = tv("à¦ªà¦¿à¦¨ à¦¸à§‡à¦Ÿ à¦•à¦°à§à¦¨",26,BLUE);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                title,
                new LinearLayout.LayoutParams(-1,dp(42))
        );

        TextView sub =
                tv(
                        "à¦…à§à¦¯à¦¾à¦ªà§‡ à¦¢à§‹à¦•à¦¾à¦° à¦œà¦¨à§à¦¯ à§® à¦¡à¦¿à¦œà¦¿à¦Ÿà§‡à¦° à¦ªà¦¿à¦¨ à¦¦à¦¿à¦¨",
                        17,
                        Color.DKGRAY
                );

        sub.setGravity(Gravity.CENTER);

        card.addView(
                sub,
                new LinearLayout.LayoutParams(-1,dp(42))
        );

        space(card,8);

        EditText p = pinInput("à§® à¦¡à¦¿à¦œà¦¿à¦Ÿ PIN");
        EditText c = pinInput("PIN à¦†à¦¬à¦¾à¦° à¦¦à¦¿à¦¨");

        card.addView(
                p,
                new LinearLayout.LayoutParams(-1,dp(60))
        );

        space(card,10);

        card.addView(
                c,
                new LinearLayout.LayoutParams(-1,dp(60))
        );

        space(card,16);

        TextView save =
                button(
                        "à¦ªà¦¿à¦¨ à¦¸à§‡à¦Ÿ à¦•à¦°à§à¦¨  âœ“",
                        Color.TRANSPARENT,
                        BLUE
                );

        save.setBackground(
                outline(
                        Color.TRANSPARENT,
                        Color.rgb(80,145,205),
                        16
                )
        );

        card.addView(
                save,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        save.setOnClickListener(v -> {

            String a = p.getText().toString().trim();
            String b = c.getText().toString().trim();

            if (a.length() != 8) {
                p.setError("à§® à¦¡à¦¿à¦œà¦¿à¦Ÿà§‡à¦° PIN à¦¦à¦¿à¦¨");
                return;
            }

            if (!a.equals(b)) {
                c.setError("à¦¦à§à¦‡à¦Ÿà¦¿ PIN à¦à¦•à¦‡ à¦¨à¦¯à¦¼");
                return;
            }

            pref.edit()
                    .putString("pin",a)
                    .putBoolean("logged_in",true)
                    .apply();

            showHome();
        });
    }

    /* =========================================================
       PIN UNLOCK
       ========================================================= */

    private void showPinUnlock() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout screen = new LinearLayout(this);

        screen.setOrientation(LinearLayout.VERTICAL);
        screen.setGravity(Gravity.CENTER);

        screen.setPadding(
                dp(20),dp(15),dp(20),dp(15)
        );

        screen.setBackgroundColor(BLUE);

        setContentView(screen);

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER_HORIZONTAL);

        card.setPadding(
                dp(25),dp(28),dp(25),dp(20)
        );

        card.setBackground(bg(Color.rgb(250,252,250),28));

        screen.addView(
                card,
                new LinearLayout.LayoutParams(-1,dp(480))
        );

        TextView lock = tv("ðŸ”’",50,BLUE);
        lock.setGravity(Gravity.CENTER);
        lock.setBackground(bg(Color.rgb(232,240,250),70));

        card.addView(
                lock,
                new LinearLayout.LayoutParams(dp(105),dp(105))
        );

        space(card,18);

        TextView title = tv("à¦ªà¦¿à¦¨ à¦¯à¦¾à¦šà¦¾à¦‡ à¦•à¦°à§à¦¨",26,BLUE);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                title,
                new LinearLayout.LayoutParams(-1,dp(42))
        );

        TextView sub =
                tv(
                        "à¦†à¦ªà¦¨à¦¾à¦° à§® à¦¡à¦¿à¦œà¦¿à¦Ÿà§‡à¦° à¦ªà¦¿à¦¨ à¦¦à¦¿à¦¨",
                        17,
                        Color.DKGRAY
                );

        sub.setGravity(Gravity.CENTER);

        card.addView(
                sub,
                new LinearLayout.LayoutParams(-1,dp(42))
        );

        space(card,8);

        EditText p = pinInput("PIN");

        card.addView(
                p,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        space(card,18);

        TextView verify =
                button(
                        "à¦¯à¦¾à¦šà¦¾à¦‡ à¦•à¦°à§à¦¨  âœ“",
                        Color.TRANSPARENT,
                        BLUE
                );

        verify.setBackground(
                outline(
                        Color.TRANSPARENT,
                        Color.rgb(80,145,205),
                        16
                )
        );

        card.addView(
                verify,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        verify.setOnClickListener(v -> {

            String saved = pref.getString("pin","");

            if (p.getText().toString().trim().equals(saved)) {
                showHome();
            } else {
                p.setError("à¦­à§à¦² PIN");
            }
        });

        TextView forgot =
                tv(
                        "PIN à¦­à§à¦²à§‡ à¦—à§‡à¦›à§‡à¦¨?  à¦²à¦—à¦‡à¦¨ à¦•à¦°à§à¦¨",
                        16,
                        BLUE
                );

        forgot.setGravity(Gravity.CENTER);

        card.addView(
                forgot,
                new LinearLayout.LayoutParams(-1,dp(48))
        );

        forgot.setOnClickListener(v -> {

            pref.edit()
                    .putBoolean("logged_in",false)
                    .apply();

            showLogin();
        });
    }

    /* =========================================================
       HOME SERVICE
       ========================================================= */

    private LinearLayout serviceRow(LinearLayout parent) {

        LinearLayout r = new LinearLayout(this);

        r.setGravity(Gravity.CENTER);

        parent.addView(
                r,
                new LinearLayout.LayoutParams(-1,0,1)
        );

        return r;
    }

    private void service(
            LinearLayout row,
            String icon,
            String title) {

        LinearLayout box = new LinearLayout(this);

        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);

        TextView i = tv(icon,27,DARK);
        i.setGravity(Gravity.CENTER);

        box.addView(
                i,
                new LinearLayout.LayoutParams(-1,dp(38))
        );

        TextView t = tv(title,11,DARK);
        t.setGravity(Gravity.CENTER);

        box.addView(
                t,
                new LinearLayout.LayoutParams(-1,dp(38))
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(0,-1,1);

        p.setMargins(dp(1),dp(1),dp(1),dp(1));

        row.addView(box,p);

        if (title.contains("à¦…à§à¦¯à¦¾à¦¡\nà¦¬à§à¦¯à¦¾à¦²à§‡à¦¨à§à¦¸")) {

            box.setOnClickListener(v -> showAddBalance());

        } else if (title.contains("à¦®à§‹à¦¬à¦¾à¦‡à¦²\nà¦¬à§à¦¯à¦¾à¦‚à¦•à¦¿à¦‚")) {

            box.setOnClickListener(v -> showMobileBanking());

        } else if (title.contains("à¦¬à§à¦¯à¦¾à¦‚à¦•\nà¦Ÿà§à¦°à¦¾à¦¨à§à¦¸à¦«à¦¾à¦°")) {

            box.setOnClickListener(v -> showBankTransfer());

        } else if (title.contains("à¦®à§‹à¦¬à¦¾à¦‡à¦²\nà¦°à¦¿à¦šà¦¾à¦°à§à¦œ")) {

            box.setOnClickListener(v -> showMobileRecharge());

        } else if (title.contains("à¦—à§à¦°à§à¦ª\nà¦šà§à¦¯à¦¾à¦Ÿ")) {

            box.setOnClickListener(v -> showGroupChat());

        } else if (title.contains("à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ\nà¦¬à§‹à¦¨à¦¾à¦¸")) {

            box.setOnClickListener(v -> showInviteBonus());

        } else if (title.contains("à¦¬à¦¿à¦¶à§‡à¦·\nà¦…à¦«à¦¾à¦°")) {

            box.setOnClickListener(v -> showSpecialOffers());

        } else if (title.contains("à¦•à¦¾à¦¸à§à¦Ÿà¦®à¦¾à¦°\nà¦•à§‡à¦¯à¦¼à¦¾à¦°")) {

            box.setOnClickListener(v -> showCustomerCare());

        } else if (title.contains("à¦•à¦¾à¦¸à§à¦Ÿà¦®à¦¾à¦°\nà¦°à¦¿à¦­à¦¿à¦‰")) {

            box.setOnClickListener(v -> showCustomerReviews());

        } else if (title.contains("à¦­à¦¿à¦¡à¦¿à¦“\nà¦Ÿà¦¿à¦‰à¦Ÿà§‹à¦°à¦¿à¦¯à¦¼à¦¾à¦²")) {

            box.setOnClickListener(v -> showVideoTutorials());

        } else if (title.contains("à¦¬à¦¿à¦²\nà¦ªà§‡")) {

            box.setOnClickListener(v -> showBillPay());
        }
    }

    private void bonusBox(
            LinearLayout parent,
            String amount,
            String bonus) {

        LinearLayout b = new LinearLayout(this);

        b.setOrientation(LinearLayout.VERTICAL);
        b.setGravity(Gravity.CENTER);
        b.setBackground(bg(Color.WHITE,10));

        TextView a = tv(amount,13,DARK);
        a.setGravity(Gravity.CENTER);

        TextView x = tv(bonus,10,Color.rgb(170,40,40));
        x.setGravity(Gravity.CENTER);

        b.addView(
                a,
                new LinearLayout.LayoutParams(-1,dp(22))
        );

        b.addView(
                x,
                new LinearLayout.LayoutParams(-1,dp(20))
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(0,dp(44),1);

        p.setMargins(dp(3),0,dp(3),0);

        parent.addView(b,p);
    }

    private void nav(LinearLayout parent,String s,View.OnClickListener click){TextView n=tv(s,14,DARK);n.setGravity(Gravity.CENTER);parent.addView(n,new LinearLayout.LayoutParams(0,dp(58),1));n.setOnClickListener(click);}

    /* =========================================================
       HOME
       ========================================================= */

    private void showHome() {

        getWindow().setStatusBarColor(GREEN);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout main = new LinearLayout(this);

        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.WHITE);

        setContentView(main);

        LinearLayout header = new LinearLayout(this);

        header.setOrientation(LinearLayout.VERTICAL);
        header.setPadding(dp(10),dp(6),dp(10),dp(6));
        header.setBackgroundColor(GREEN);

        main.addView(
                header,
                new LinearLayout.LayoutParams(-1,dp(178))
        );

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);

        header.addView(
                top,
                new LinearLayout.LayoutParams(-1,dp(50))
        );

        TextView brand = tv("Quick Pay",21,DARK);

        brand.setGravity(Gravity.CENTER);
        brand.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        brand.setBackground(bg(Color.WHITE,15));

        top.addView(
                brand,
                new LinearLayout.LayoutParams(dp(145),dp(45))
        );

        top.addView(
                new Space(this),
                new LinearLayout.LayoutParams(0,1,1)
        );

        TextView en = tv(isEnglish()?"BN":"EN",15,Color.WHITE);
        en.setGravity(Gravity.CENTER);

        top.addView(
                en,
                new LinearLayout.LayoutParams(dp(38),dp(45))
        );
        en.setOnClickListener(v -> toggleLanguage());

        TextView bell = tv("ðŸ””",19,Color.WHITE);
        bell.setGravity(Gravity.CENTER);

        top.addView(
                bell,
                new LinearLayout.LayoutParams(dp(42),dp(45))
        );


        LinearLayout user = new LinearLayout(this);
        user.setGravity(Gravity.CENTER_VERTICAL);

        header.addView(
                user,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        TextView name =
                tv(
                        pref.getString("name","Rosy"),
                        25,
                        Color.WHITE
                );

        name.setGravity(Gravity.CENTER_VERTICAL);
        name.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        user.addView(
                name,
                new LinearLayout.LayoutParams(0,dp(62),1)
        );

        final TextView bal =
                tv(
                        "",
                        12,
                        DARK
                );

        bal.setGravity(Gravity.CENTER);
        bal.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        bal.setBackground(bg(YELLOW,30));

        final boolean[] balanceVisible = {pref.getBoolean("balance_visible", false)};

        Runnable refreshBalance = () -> {
            if (balanceVisible[0]) {
                bal.setText(
                        "Main Balance  à§³ " + formatMoney(getMainBalance())
                                + "\nDrive Balance  à§³ " + formatMoney(getDriveBalance())
                                + "  ðŸ‘"
                );
            } else {
                bal.setText("Main Balance  â€¢ â€¢ â€¢ â€¢  /  Drive Balance  â€¢ â€¢ â€¢ â€¢  ðŸ‘");
            }
        };

        refreshBalance.run();

        bal.setOnClickListener(v -> {
            balanceVisible[0] = !balanceVisible[0];
            pref.edit().putBoolean("balance_visible", balanceVisible[0]).apply();
            refreshBalance.run();
        });

        user.addView(
                bal,
                new LinearLayout.LayoutParams(dp(190),dp(56))
        );

        addLiveActivityTicker(header);

        LinearLayout services = new LinearLayout(this);

        services.setOrientation(LinearLayout.VERTICAL);
        services.setPadding(dp(12),dp(4),dp(12),dp(2));

        main.addView(
                services,
                new LinearLayout.LayoutParams(-1,0,1)
        );

        LinearLayout r = serviceRow(services);

        service(r,"ðŸ‘›","à¦…à§à¦¯à¦¾à¦¡\nà¦¬à§à¦¯à¦¾à¦²à§‡à¦¨à§à¦¸");
        service(r,"ðŸ’µ","à¦®à§‹à¦¬à¦¾à¦‡à¦²\nà¦¬à§à¦¯à¦¾à¦‚à¦•à¦¿à¦‚");
        service(r,"ðŸ¦","à¦¬à§à¦¯à¦¾à¦‚à¦•\nà¦Ÿà§à¦°à¦¾à¦¨à§à¦¸à¦«à¦¾à¦°");
        service(r,"ðŸ“±","à¦®à§‹à¦¬à¦¾à¦‡à¦²\nà¦°à¦¿à¦šà¦¾à¦°à§à¦œ");

        r = serviceRow(services);

        service(r,"ðŸ’¬","à¦—à§à¦°à§à¦ª\nà¦šà§à¦¯à¦¾à¦Ÿ");
        service(r,"ðŸŽ","à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ\nà¦¬à§‹à¦¨à¦¾à¦¸");
        service(r,"ðŸ§¾","à¦¬à¦¿à¦²\nà¦ªà§‡");
        service(r,"ðŸ·","à¦¬à¦¿à¦¶à§‡à¦·\nà¦…à¦«à¦¾à¦°");

        r = serviceRow(services);

        service(r,"ðŸŽ§","à¦•à¦¾à¦¸à§à¦Ÿà¦®à¦¾à¦°\nà¦•à§‡à¦¯à¦¼à¦¾à¦°");
        service(r,"â­","à¦•à¦¾à¦¸à§à¦Ÿà¦®à¦¾à¦°\nà¦°à¦¿à¦­à¦¿à¦‰");
        service(r,"â–¶","à¦­à¦¿à¦¡à¦¿à¦“\nà¦Ÿà¦¿à¦‰à¦Ÿà§‹à¦°à¦¿à¦¯à¦¼à¦¾à¦²");
        service(r,"ðŸ‘¥","à¦•à¦¨à§à¦Ÿà¦¾à¦•à§à¦Ÿ\nà¦†à¦¸");

        String bannerUri=pref.getString("deposit_banner_uri","");
        if(!bannerUri.isEmpty()){
            ImageView iv=new ImageView(this);iv.setScaleType(ImageView.ScaleType.CENTER_CROP);try{iv.setImageURI(Uri.parse(bannerUri));}catch(Exception ignored){}main.addView(iv,new LinearLayout.LayoutParams(-1,dp(118)));iv.setOnClickListener(v->{String action=pref.getString("deposit_banner_action","");if(!action.isEmpty())openExternal(action);});
        }else{
            LinearLayout bonus=new LinearLayout(this);bonus.setOrientation(LinearLayout.VERTICAL);bonus.setPadding(dp(10),dp(2),dp(10),dp(3));bonus.setBackground(bg(GREEN,16));main.addView(bonus,new LinearLayout.LayoutParams(-1,dp(98)));TextView bt=tv(tx("ðŸŽ  à¦¡à¦¿à¦ªà§‹à¦œà¦¿à¦Ÿ à¦¬à§‹à¦¨à¦¾à¦¸ à¦…à¦«à¦¾à¦°","ðŸŽ  Deposit Bonus Offer"),18,Color.WHITE);bt.setGravity(Gravity.CENTER_VERTICAL);bt.setTypeface(Typeface.DEFAULT,Typeface.BOLD);bonus.addView(bt,new LinearLayout.LayoutParams(-1,dp(32)));TextView bs=tv(tx("à¦à¦–à¦¨à¦‡ à¦•à¦°à§à¦¨, à¦¬à§‹à¦¨à¦¾à¦¸ à¦¨à¦¿à¦¯à¦¼à§‡ à¦¨à¦¿à¦¨!","Deposit now and get bonus!"),11,Color.WHITE);bonus.addView(bs,new LinearLayout.LayoutParams(-1,dp(20)));LinearLayout bb=new LinearLayout(this);bb.setGravity(Gravity.CENTER);bonus.addView(bb,new LinearLayout.LayoutParams(-1,dp(42)));bonusBox(bb,panelText("deposit_bonus_1_amount","à§³ à§«à§¦à§¦"),panelText("deposit_bonus_1_bonus","à¦¬à§‹à¦¨à¦¾à¦¸ à§³ à§«à§¦"));bonusBox(bb,panelText("deposit_bonus_2_amount","à§³ à§§à§¦à§¦à§¦"),panelText("deposit_bonus_2_bonus","à¦¬à§‹à¦¨à¦¾à¦¸ à§³ à§§à§¦à§¦"));bonusBox(bb,panelText("deposit_bonus_3_amount","à§³ à§¨à§¦à§¦à§¦"),panelText("deposit_bonus_3_bonus","à¦¬à§‹à¦¨à¦¾à¦¸ à§³ à§¨à§¦à§¦"));
        }

        LinearLayout bottom = new LinearLayout(this);

        bottom.setGravity(Gravity.CENTER);
        bottom.setBackgroundColor(Color.WHITE);

        main.addView(
                bottom,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        nav(bottom,"âŒ‚\n"+tx("à¦¹à§‹à¦®","Home"),v->showHome());
        nav(bottom,"â—·\n"+tx("à¦²à§‡à¦¨à¦¦à§‡à¦¨","Transactions"),v->showTransactionHistory());
        nav(bottom,"â™™\n"+tx("à¦ªà§à¦°à§‹à¦«à¦¾à¦‡à¦²","Profile"),v->showProfile());
    }

    /* =========================================================
       BILL PAY
       ========================================================= */

    private void showBillPay() {

        selectedBillType = "";
        selectedBillCode = "";
        selectedBillColor = BLUE;

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout main = new LinearLayout(this);

        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.rgb(247,248,250));

        setContentView(main);

        LinearLayout header = new LinearLayout(this);

        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(8),0,dp(8),0);
        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        TextView back = tv("â€¹",38,Color.WHITE);
        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        back.setOnClickListener(v -> showHome());

        TextView title = tv("à¦¬à¦¿à¦² à¦ªà§‡à¦®à§‡à¦¨à§à¦Ÿ",21,Color.WHITE);

        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        header.addView(
                title,
                new LinearLayout.LayoutParams(0,dp(62),1)
        );

        header.addView(
                new Space(this),
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);

        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(12),dp(10),dp(12),dp(25));

        scroll.addView(content);

        main.addView(
                scroll,
                new LinearLayout.LayoutParams(-1,0,1)
        );

        LinearLayout heading = new LinearLayout(this);

        heading.setGravity(Gravity.CENTER_VERTICAL);

        content.addView(
                heading,
                new LinearLayout.LayoutParams(-1,dp(45))
        );

        TextView doc = tv("â–¤",23,Color.rgb(70,80,95));
        doc.setGravity(Gravity.CENTER);

        heading.addView(
                doc,
                new LinearLayout.LayoutParams(dp(38),dp(42))
        );

        TextView choose =
                tv(
                        "à¦¬à¦¿à¦²à§‡à¦° à¦§à¦°à¦¨ à¦¨à¦¿à¦°à§à¦¬à¦¾à¦šà¦¨ à¦•à¦°à§à¦¨",
                        16,
                        DARK
                );

        choose.setGravity(Gravity.CENTER_VERTICAL);
        choose.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        heading.addView(
                choose,
                new LinearLayout.LayoutParams(0,dp(42),1)
        );

        LinearLayout row = null;

        String[][] types = {
                {"à¦¡à§‡à¦¸à¦•à§‹","âš¡","DES","0"},
                {"à¦¡à¦¿à¦ªà¦¿à¦¡à¦¿à¦¸à¦¿","âš¡","DPDC","1"},
                {"à¦¨à§‡à¦¸à¦•à§‹","ÏŸ","NESCO","2"},
                {"à¦“à§Ÿà¦¾à¦¸à¦¾","ðŸ’§","WASA","3"},
                {"à¦—à§à¦¯à¦¾à¦¸","â™¨","GAS","4"},
                {"à¦Ÿà§‡à¦²à¦¿à¦«à§‹à¦¨","â˜Ž","TEL","5"},
                {"à¦‡à¦¨à§à¦Ÿà¦¾à¦°à¦¨à§‡à¦Ÿ","âŒ","INT","6"},
                {"à¦®à§‹à¦¬à¦¾à¦‡à¦²","â–¯","MOB","7"},
                {"à¦•à§‡à¦¬à¦²","â–­","CAB","8"},
                {"à¦¡à¦¿à¦¶","â—‰","DISH","9"},
                {"à¦¸à¦¿à¦Ÿà¦¿ à¦•à¦°à§à¦ª","â–¥","CITY","10"}
        };

        for (int i = 0; i < types.length; i++) {

            if (i % 2 == 0) {

                row = new LinearLayout(this);
                row.setGravity(Gravity.CENTER);

                content.addView(
                        row,
                        new LinearLayout.LayoutParams(-1,dp(128))
                );
            }

            addBillType(
                    row,
                    types[i][0],
                    types[i][1],
                    types[i][2],
                    Integer.parseInt(types[i][3])
            );
        }
    }

    /* =========================================================
       BILL TYPE CARD
       ========================================================= */

    private void addBillType(
            LinearLayout parent,
            String name,
            String icon,
            String code,
            int index) {

        int[] colors = {
                Color.rgb(255,170,20),
                Color.rgb(255,82,55),
                Color.rgb(72,155,75),
                Color.rgb(35,150,240),
                Color.rgb(255,80,75),
                Color.rgb(75,180,90),
                Color.rgb(125,75,205),
                Color.rgb(30,175,170),
                Color.rgb(135,110,100),
                Color.rgb(85,105,205),
                Color.rgb(85,125,145)
        };

        int color = colors[
                Math.min(index,colors.length - 1)
        ];

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setGravity(Gravity.CENTER);

        card.setPadding(
                dp(5),
                dp(7),
                dp(5),
                dp(7)
        );

        card.setBackground(
                outline(
                        Color.WHITE,
                        Color.rgb(225,225,225),
                        13
                )
        );

        LinearLayout.LayoutParams cp =
                new LinearLayout.LayoutParams(
                        0,
                        dp(116),
                        1
                );

        cp.setMargins(
                dp(3),
                dp(5),
                dp(3),
                dp(5)
        );

        parent.addView(card,cp);

        TextView iconView =
                tv(
                        icon,
                        28,
                        color
                );

        iconView.setGravity(Gravity.CENTER);
        iconView.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        GradientDrawable circle =
                new GradientDrawable();

        circle.setShape(
                GradientDrawable.OVAL
        );

        circle.setColor(
                Color.argb(
                        35,
                        Color.red(color),
                        Color.green(color),
                        Color.blue(color)
                )
        );

        iconView.setBackground(circle);

        LinearLayout.LayoutParams ip =
                new LinearLayout.LayoutParams(
                        dp(58),
                        dp(58)
                );

        card.addView(iconView,ip);

        TextView nameView =
                tv(
                        name,
                        14,
                        DARK
                );

        nameView.setGravity(Gravity.CENTER);
        nameView.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        card.addView(
                nameView,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(35)
                )
        );

        card.setOnClickListener(v -> {

            selectedBillType = name;
            selectedBillCode = code;
            selectedBillColor = color;

            showBillPayForm();
        });
    }

    /* =========================================================
       BILL PAY FORM
       ========================================================= */

    private void showBillPayForm() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout main = new LinearLayout(this);

        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.rgb(247,248,250));

        setContentView(main);

        LinearLayout header = new LinearLayout(this);

        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(8),0,dp(8),0);
        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        TextView back = tv("â€¹",38,Color.WHITE);
        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        back.setOnClickListener(v -> showHome());

        TextView title = tv("à¦¬à¦¿à¦² à¦ªà§‡à¦®à§‡à¦¨à§à¦Ÿ",21,Color.WHITE);

        title.setGravity(Gravity.CENTER);
        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        header.addView(
                title,
                new LinearLayout.LayoutParams(0,dp(62),1)
        );

        header.addView(
                new Space(this),
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);

        content.setOrientation(
                LinearLayout.VERTICAL
        );

        content.setPadding(
                dp(12),
                dp(10),
                dp(12),
                dp(25)
        );

        scroll.addView(content);

        main.addView(
                scroll,
                new LinearLayout.LayoutParams(-1,0,1)
        );

        LinearLayout heading = new LinearLayout(this);

        heading.setGravity(Gravity.CENTER_VERTICAL);

        content.addView(
                heading,
                new LinearLayout.LayoutParams(-1,dp(45))
        );

        TextView doc = tv("â–¤",23,Color.rgb(70,80,95));
        doc.setGravity(Gravity.CENTER);

        heading.addView(
                doc,
                new LinearLayout.LayoutParams(dp(38),dp(42))
        );

        TextView headingText =
                tv(
                        "à¦¬à¦¿à¦²à§‡à¦° à¦§à¦°à¦¨ à¦¨à¦¿à¦°à§à¦¬à¦¾à¦šà¦¨ à¦•à¦°à§à¦¨",
                        16,
                        DARK
                );

        headingText.setGravity(
                Gravity.CENTER_VERTICAL
        );

        headingText.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        heading.addView(
                headingText,
                new LinearLayout.LayoutParams(0,dp(42),1)
        );

        TextView change =
                tv(
                        "à¦ªà¦°à¦¿à¦¬à¦°à§à¦¤à¦¨",
                        14,
                        BLUE
                );

        change.setGravity(Gravity.CENTER);

        heading.addView(
                change,
                new LinearLayout.LayoutParams(dp(75),dp(42))
        );

        change.setOnClickListener(
                v -> showBillPay()
        );

        /* SELECTED BILL CARD */

        LinearLayout selectedCard =
                new LinearLayout(this);

        selectedCard.setOrientation(
                LinearLayout.VERTICAL
        );

        selectedCard.setGravity(
                Gravity.CENTER
        );

        selectedCard.setBackground(
                outline(
                        Color.WHITE,
                        selectedBillColor,
                        13
                )
        );

        content.addView(
                selectedCard,
                new LinearLayout.LayoutParams(-1,dp(126))
        );

        TextView selectedIcon =
                tv(
                        getBillIcon(selectedBillType),
                        28,
                        selectedBillColor
                );

        selectedIcon.setGravity(Gravity.CENTER);

        GradientDrawable selectedCircle =
                new GradientDrawable();

        selectedCircle.setShape(
                GradientDrawable.OVAL
        );

        selectedCircle.setColor(
                Color.argb(
                        35,
                        Color.red(selectedBillColor),
                        Color.green(selectedBillColor),
                        Color.blue(selectedBillColor)
                )
        );

        selectedIcon.setBackground(
                selectedCircle
        );

        selectedCard.addView(
                selectedIcon,
                new LinearLayout.LayoutParams(
                        dp(58),
                        dp(58)
                )
        );

        TextView selectedName =
                tv(
                        selectedBillType,
                        14,
                        DARK
                );

        selectedName.setGravity(Gravity.CENTER);
        selectedName.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        selectedCard.addView(
                selectedName,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(35)
                )
        );

        space(content,10);

        LinearLayout infoTitle =
                new LinearLayout(this);

        infoTitle.setGravity(
                Gravity.CENTER_VERTICAL
        );

        content.addView(
                infoTitle,
                new LinearLayout.LayoutParams(-1,dp(40))
        );

        TextView infoIcon =
                tv("â“˜",21,Color.rgb(70,80,95));

        infoIcon.setGravity(Gravity.CENTER);

        infoTitle.addView(
                infoIcon,
                new LinearLayout.LayoutParams(dp(35),dp(38))
        );

        TextView info =
                tv(
                        "à¦¬à¦¿à¦²à§‡à¦° à¦¤à¦¥à§à¦¯ à¦¦à¦¿à¦¨",
                        17,
                        DARK
                );

        info.setGravity(
                Gravity.CENTER_VERTICAL
        );

        info.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        infoTitle.addView(
                info,
                new LinearLayout.LayoutParams(0,dp(38),1)
        );

        space(content,3);

        TextView numberTitle =
                tv(
                        "à¦¬à¦¿à¦² à¦¨à¦®à§à¦¬à¦°",
                        14,
                        Color.DKGRAY
                );

        numberTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        content.addView(
                numberTitle,
                new LinearLayout.LayoutParams(-1,dp(28))
        );

        EditText billNumber =
                new EditText(this);

        billNumber.setHint(
                "à¦¬à¦¿à¦² à¦¨à¦®à§à¦¬à¦° (" +
                        selectedBillCode +
                        "...)"
        );

        billNumber.setTextSize(16);
        billNumber.setSingleLine(true);
        billNumber.setTextColor(DARK);
        billNumber.setHintTextColor(Color.GRAY);
        billNumber.setPadding(
                dp(15),
                0,
                dp(15),
                0
        );

        billNumber.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        );

        billNumber.setBackground(
                outline(
                        Color.WHITE,
                        Color.rgb(220,225,232),
                        11
                )
        );

        content.addView(
                billNumber,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(content,8);

        TextView amountTitle =
                tv(
                        "à¦¬à¦¿à¦²à§‡à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£",
                        14,
                        Color.DKGRAY
                );

        amountTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        content.addView(
                amountTitle,
                new LinearLayout.LayoutParams(-1,dp(28))
        );

        EditText billAmount =
                new EditText(this);

        billAmount.setHint(
                "à¦¬à¦¿à¦²à§‡à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ (à¦¸à¦°à§à¦¬à¦¨à¦¿à¦®à§à¦¨ à§³500)"
        );

        billAmount.setTextSize(16);
        billAmount.setSingleLine(true);
        billAmount.setTextColor(DARK);
        billAmount.setHintTextColor(Color.GRAY);

        billAmount.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        billAmount.setPadding(
                dp(15),
                0,
                dp(15),
                0
        );

        billAmount.setBackground(
                outline(
                        Color.WHITE,
                        Color.rgb(220,225,232),
                        11
                )
        );

        content.addView(
                billAmount,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(content,18);

        TextView pay =
                button(
                        "à¦¬à¦¿à¦² à¦ªà§‡à¦®à§‡à¦¨à§à¦Ÿ à¦•à¦°à§à¦¨",
                        Color.rgb(165,165,165),
                        Color.WHITE
                );

        pay.setEnabled(false);

        content.addView(
                pay,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(content,18);

        double balance =
                getMainBalance();

        TextView balanceView =
                tv(
                        "â–£  à¦¬à¦°à§à¦¤à¦®à¦¾à¦¨ à¦¬à§à¦¯à¦¾à¦²à§‡à¦¨à§à¦¸:  à§³ "
                                + formatMoney(balance),
                        17,
                        Color.rgb(55,75,100)
                );

        balanceView.setGravity(
                Gravity.CENTER
        );

        balanceView.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        balanceView.setBackground(
                bg(Color.WHITE,12)
        );

        content.addView(
                balanceView,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        TextWatcher watcher =
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        updateBillPayButton(
                                billNumber,
                                billAmount,
                                pay
                        );
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s) {
                    }
                };

        billNumber.addTextChangedListener(watcher);
        billAmount.addTextChangedListener(watcher);

        pay.setOnClickListener(v -> {

            String number =
                    billNumber.getText()
                            .toString()
                            .trim();

            String money =
                    billAmount.getText()
                            .toString()
                            .trim();

            if (number.isEmpty()) {
                billNumber.setError(
                        "à¦¬à¦¿à¦² à¦¨à¦®à§à¦¬à¦° à¦¦à¦¿à¦¨"
                );
                return;
            }

            if (money.isEmpty()) {
                billAmount.setError(
                        "à¦¬à¦¿à¦²à§‡à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¦à¦¿à¦¨"
                );
                return;
            }

            double value;

            try {

                value =
                        Double.parseDouble(
                                money
                        );

            } catch (Exception e) {

                billAmount.setError(
                        "à¦¸à¦ à¦¿à¦• à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¦à¦¿à¦¨"
                );

                return;
            }

            if (value < 500) {

                billAmount.setError(
                        "à¦¸à¦°à§à¦¬à¦¨à¦¿à¦®à§à¦¨ à¦¬à¦¿à¦² à¦ªà§‡à¦®à§‡à¦¨à§à¦Ÿ à§³500"
                );

                return;
            }

            double currentBalance =
                    getMainBalance();

            if (currentBalance > 0 &&
                    value > currentBalance) {

                new AlertDialog.Builder(this)
                        .setTitle(
                                "à¦ªà¦°à§à¦¯à¦¾à¦ªà§à¦¤ à¦¬à§à¦¯à¦¾à¦²à§‡à¦¨à§à¦¸ à¦¨à§‡à¦‡"
                        )
                        .setMessage(
                                "à¦†à¦ªà¦¨à¦¾à¦° à¦¬à¦°à§à¦¤à¦®à¦¾à¦¨ à¦¬à§à¦¯à¦¾à¦²à§‡à¦¨à§à¦¸: à§³ "
                                        + formatMoney(
                                        currentBalance
                                )
                                        + "\n"
                                        + "à¦¬à¦¿à¦²à§‡à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£: à§³ "
                                        + formatMoney(
                                        value
                                )
                        )
                        .setPositiveButton(
                                "à¦ à¦¿à¦• à¦†à¦›à§‡",
                                null
                        )
                        .show();

                return;
            }

            final String finalBillNumber = number;
            final String finalBillMoney = money;

            new AlertDialog.Builder(this)
                    .setTitle(
                            "à¦¬à¦¿à¦² à¦ªà§‡à¦®à§‡à¦¨à§à¦Ÿ à¦¨à¦¿à¦¶à§à¦šà¦¿à¦¤ à¦•à¦°à§à¦¨"
                    )
                    .setMessage(
                            "à¦¬à¦¿à¦²à§‡à¦° à¦§à¦°à¦¨: "
                                    + selectedBillType
                                    + "\n\n"
                                    + "à¦¬à¦¿à¦² à¦¨à¦®à§à¦¬à¦°: "
                                    + number
                                    + "\n\n"
                                    + "à¦¬à¦¿à¦²à§‡à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£: à§³ "
                                    + money
                                    + "\n\n"
                                    + "à¦à¦‡ à¦­à¦¾à¦°à§à¦¸à¦¨à§‡ à¦à¦Ÿà¦¿ Demo Bill Payment Requestà¥¤ "
                                    + "à¦†à¦¸à¦² à¦¬à¦¿à¦² à¦ªà¦°à¦¿à¦¶à§‹à¦§à§‡à¦° à¦œà¦¨à§à¦¯ à¦¸à¦‚à¦¶à§à¦²à¦¿à¦·à§à¦Ÿ Provider API/Backend à¦¸à¦‚à¦¯à§à¦•à§à¦¤ à¦•à¦°à¦¤à§‡ à¦¹à¦¬à§‡à¥¤"
                    )
                    .setNegativeButton(
                            "à¦¬à¦¾à¦¤à¦¿à¦²",
                            null
                    )
                    .setPositiveButton(
                            "à¦¨à¦¿à¦¶à§à¦šà¦¿à¦¤ à¦•à¦°à§à¦¨",
                            (dialog,which) -> {

                                if (transactionBlockedFor(Double.parseDouble(finalBillMoney))) return;
                                Map<String,Object> req = new HashMap<>(); req.put("billType", selectedBillType); req.put("billNumber", finalBillNumber); req.put("amount", Double.parseDouble(finalBillMoney)); submitFirestoreRequest("billPayRequests", req); recordTransaction("à¦¬à¦¿à¦² à¦ªà§‡",selectedBillType+" / "+finalBillNumber,Double.parseDouble(finalBillMoney),false,"PENDING");
                                Toast.makeText(
                                        this,
                                        selectedBillType
                                                + " à¦¬à¦¿à¦² à¦ªà§‡à¦®à§‡à¦¨à§à¦Ÿ à¦°à¦¿à¦•à§‹à§Ÿà§‡à¦¸à§à¦Ÿ à¦—à§à¦°à¦¹à¦£ à¦•à¦°à¦¾ à¦¹à§Ÿà§‡à¦›à§‡à¥¤",
                                        Toast.LENGTH_LONG
                                ).show();

                                billNumber.setText("");
                                billAmount.setText("");
                            }
                    )
                    .show();
        });
    }

    /* =========================================================
       BILL PAY BUTTON
       ========================================================= */

    private void updateBillPayButton(
            EditText number,
            EditText amount,
            TextView pay) {

        String n =
                number.getText()
                        .toString()
                        .trim();

        String a =
                amount.getText()
                        .toString()
                        .trim();

        boolean valid = false;

        if (!n.isEmpty() && !a.isEmpty()) {

            try {

                double value =
                        Double.parseDouble(a);

                valid = value >= 500;

            } catch (Exception ignored) {
                valid = false;
            }
        }

        if (valid) {

            pay.setEnabled(true);
            pay.setTextColor(Color.WHITE);
            pay.setBackground(
                    bg(BLUE,12)
            );

        } else {

            pay.setEnabled(false);
            pay.setTextColor(Color.WHITE);
            pay.setBackground(
                    bg(Color.rgb(165,165,165),12)
            );
        }
    }

    /* =========================================================
       BILL ICON
       ========================================================= */

    private String getBillIcon(String type) {

        if (type.equals("à¦¡à§‡à¦¸à¦•à§‹")) return "âš¡";
        if (type.equals("à¦¡à¦¿à¦ªà¦¿à¦¡à¦¿à¦¸à¦¿")) return "âš¡";
        if (type.equals("à¦¨à§‡à¦¸à¦•à§‹")) return "ÏŸ";
        if (type.equals("à¦“à§Ÿà¦¾à¦¸à¦¾")) return "ðŸ’§";
        if (type.equals("à¦—à§à¦¯à¦¾à¦¸")) return "â™¨";
        if (type.equals("à¦Ÿà§‡à¦²à¦¿à¦«à§‹à¦¨")) return "â˜Ž";
        if (type.equals("à¦‡à¦¨à§à¦Ÿà¦¾à¦°à¦¨à§‡à¦Ÿ")) return "âŒ";
        if (type.equals("à¦®à§‹à¦¬à¦¾à¦‡à¦²")) return "â–¯";
        if (type.equals("à¦•à§‡à¦¬à¦²")) return "â–­";
        if (type.equals("à¦¡à¦¿à¦¶")) return "â—‰";
        if (type.equals("à¦¸à¦¿à¦Ÿà¦¿ à¦•à¦°à§à¦ª")) return "â–¥";

        return "â–¤";
    }

    /* =========================================================
       BILL BALANCE
       ========================================================= */

    private double getDriveBalance() {
        String saved = pref.getString("drive_balance", "0");
        try {
            return Double.parseDouble(saved);
        } catch (Exception e) {
            return 0;
        }
    }

    private double getMainBalance() {

        String saved =
                pref.getString(
                        "main_balance",
                        "0"
                );

        try {

            return Double.parseDouble(saved);

        } catch (Exception e) {

            return 0;
        }
    }

    private String formatMoney(double value) {

        if (value == (long)value) {

            return String.format(
                    Locale.getDefault(),
                    "%d.00",
                    (long)value
            );
        }

        return String.format(
                Locale.getDefault(),
                "%.2f",
                value
        );
    }

    /* =========================================================
       INVITE BONUS
       ========================================================= */

    private String getInviteCode() {

        String code = pref.getString("invite_code", "");

        if (code.isEmpty()) {

            String phone =
                    pref.getString("phone", "")
                            .replaceAll("[^0-9]", "");

            String tail =
                    phone.length() >= 4
                            ? phone.substring(phone.length() - 4)
                            : "0000";

            code = "QP" + tail;

            pref.edit()
                    .putString("invite_code", code)
                    .apply();
        }

        return code;
    }

    private int getInviteCount() {
        return pref.getInt("invite_count", 0);
    }

    // Demo panel à¦¥à§‡à¦•à§‡ Invite Count à¦ªà¦°à¦¿à¦¬à¦°à§à¦¤à¦¨ à¦•à¦°à¦¾à¦° à¦œà¦¨à§à¦¯
    private void setInviteCount(int count) {
        if (count < 0) count = 0;
        pref.edit()
                .putInt("invite_count", count)
                .apply();
    }

    private double getInviteEarned() {
        return Double.longBitsToDouble(
                pref.getLong(
                        "invite_earned_bits",
                        Double.doubleToLongBits(0.0)
                )
        );
    }

    private void saveInviteEarned(double value) {
        pref.edit()
                .putLong(
                        "invite_earned_bits",
                        Double.doubleToLongBits(value)
                )
                .apply();
    }

    private void copyInviteCode() {

        String code = getInviteCode();

        android.content.ClipboardManager clipboard =
                (android.content.ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);

        android.content.ClipData clip =
                android.content.ClipData.newPlainText(
                        "Quick Pay Invite Code",
                        code
                );

        clipboard.setPrimaryClip(clip);

        Toast.makeText(
                this,
                "à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ à¦•à§‹à¦¡ à¦•à¦ªà¦¿ à¦¹à¦¯à¦¼à§‡à¦›à§‡: " + code,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void shareInvite() {

        String code = getInviteCode();

        String link =
                "quickpay://invite/" + code;

        String message =
                "ðŸŽ Quick Pay-à¦ à¦†à¦®à¦¾à¦•à§‡ à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ à¦•à¦°à¦¾ à¦¹à¦¯à¦¼à§‡à¦›à§‡!\n\n"
                        + "Invite Code: " + code + "\n"
                        + "Invite Link: " + link + "\n\n"
                        + "à¦…à§à¦¯à¦¾à¦ªà§‡ à¦°à§‡à¦œà¦¿à¦¸à§à¦Ÿà§à¦°à§‡à¦¶à¦¨à§‡à¦° à¦¸à¦®à¦¯à¦¼ à¦à¦‡ à¦•à§‹à¦¡ à¦¬à§à¦¯à¦¬à¦¹à¦¾à¦° à¦•à¦°à§à¦¨à¥¤";

        android.content.Intent share =
                new android.content.Intent(
                        android.content.Intent.ACTION_SEND
                );

        share.setType("text/plain");
        share.putExtra(
                android.content.Intent.EXTRA_TEXT,
                message
        );

        startActivity(
                android.content.Intent.createChooser(
                        share,
                        "à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ à¦¶à§‡à¦¯à¦¼à¦¾à¦° à¦•à¦°à§à¦¨"
                )
        );
    }

    private void showInviteBonus() {
        syncInviteBonusTransaction();

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout main = new LinearLayout(this);

        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.rgb(247,248,250));

        setContentView(main);

        LinearLayout header = new LinearLayout(this);

        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(8),0,dp(8),0);
        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        TextView back = tv("â€¹",38,Color.WHITE);
        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        back.setOnClickListener(v -> showHome());

        TextView title =
                tv("à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ à¦¬à§‹à¦¨à¦¾à¦¸",21,Color.WHITE);

        title.setGravity(Gravity.CENTER);
        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        header.addView(
                title,
                new LinearLayout.LayoutParams(0,dp(62),1)
        );

        header.addView(
                new Space(this),
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);

        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(
                dp(12),dp(12),dp(12),dp(28)
        );

        scroll.addView(content);

        main.addView(
                scroll,
                new LinearLayout.LayoutParams(-1,0,1)
        );

        LinearLayout hero = new LinearLayout(this);

        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setGravity(Gravity.CENTER_HORIZONTAL);
        hero.setPadding(
                dp(18),dp(20),dp(18),dp(20)
        );
        hero.setBackground(bg(GREEN,18));

        content.addView(
                hero,
                new LinearLayout.LayoutParams(-1,dp(190))
        );

        TextView gift = tv("ðŸŽ",44,Color.WHITE);
        gift.setGravity(Gravity.CENTER);

        hero.addView(
                gift,
                new LinearLayout.LayoutParams(-1,dp(52))
        );

        TextView ht =
                tv(
                        "à¦¬à¦¨à§à¦§à§à¦•à§‡ à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ à¦•à¦°à§à¦¨",
                        22,
                        Color.WHITE
                );

        ht.setGravity(Gravity.CENTER);
        ht.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        hero.addView(
                ht,
                new LinearLayout.LayoutParams(-1,dp(40))
        );

        TextView hs =
                tv(
                        "à¦†à¦ªà¦¨à¦¾à¦° à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ à¦•à§‹à¦¡ à¦¶à§‡à¦¯à¦¼à¦¾à¦° à¦•à¦°à§‡ à¦¨à¦¤à§à¦¨ à¦‡à¦‰à¦œà¦¾à¦° à¦†à¦¨à§à¦¨",
                        13,
                        Color.WHITE
                );

        hs.setGravity(Gravity.CENTER);

        hero.addView(
                hs,
                new LinearLayout.LayoutParams(-1,dp(35))
        );

        space(content,10);

        LinearLayout codeCard = new LinearLayout(this);

        codeCard.setOrientation(LinearLayout.VERTICAL);
        codeCard.setPadding(
                dp(16),dp(14),dp(16),dp(14)
        );
        codeCard.setBackground(bg(Color.WHITE,16));

        content.addView(
                codeCard,
                new LinearLayout.LayoutParams(-1,dp(142))
        );

        TextView cl =
                tv(
                        "à¦†à¦ªà¦¨à¦¾à¦° Invite Code",
                        14,
                        Color.DKGRAY
                );

        cl.setGravity(Gravity.CENTER);

        codeCard.addView(
                cl,
                new LinearLayout.LayoutParams(-1,dp(28))
        );

        TextView code =
                tv(
                        getInviteCode(),
                        28,
                        BLUE
                );

        code.setGravity(Gravity.CENTER);
        code.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        codeCard.addView(
                code,
                new LinearLayout.LayoutParams(-1,dp(48))
        );

        TextView copy =
                button(
                        "à¦•à§‹à¦¡ à¦•à¦ªà¦¿ à¦•à¦°à§à¦¨",
                        BLUE,
                        Color.WHITE
                );

        codeCard.addView(
                copy,
                new LinearLayout.LayoutParams(-1,dp(48))
        );

        copy.setOnClickListener(v -> copyInviteCode());

        space(content,10);

        LinearLayout shareCard = new LinearLayout(this);

        shareCard.setOrientation(LinearLayout.VERTICAL);
        shareCard.setPadding(
                dp(16),dp(14),dp(16),dp(14)
        );
        shareCard.setBackground(bg(Color.WHITE,16));

        content.addView(
                shareCard,
                new LinearLayout.LayoutParams(-1,dp(128))
        );

        TextView st =
                tv(
                        "Invite Link",
                        15,
                        DARK
                );

        st.setGravity(Gravity.CENTER);

        shareCard.addView(
                st,
                new LinearLayout.LayoutParams(-1,dp(28))
        );

        TextView link =
                tv(
                        "quickpay://invite/"
                                + getInviteCode(),
                        13,
                        Color.DKGRAY
                );

        link.setGravity(Gravity.CENTER);

        shareCard.addView(
                link,
                new LinearLayout.LayoutParams(-1,dp(32))
        );

        TextView share =
                button(
                        "à¦¶à§‡à¦¯à¦¼à¦¾à¦° à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ",
                        GREEN,
                        Color.WHITE
                );

        shareCard.addView(
                share,
                new LinearLayout.LayoutParams(-1,dp(48))
        );

        share.setOnClickListener(v -> shareInvite());

        space(content,10);

        LinearLayout stats = new LinearLayout(this);

        stats.setGravity(Gravity.CENTER);

        content.addView(
                stats,
                new LinearLayout.LayoutParams(-1,dp(92))
        );

        TextView invited =
                tv(
                        "ðŸ‘¤\n"
                                + getInviteCount()
                                + "\nà¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿà§‡à¦¡",
                        15,
                        DARK
                );

        invited.setGravity(Gravity.CENTER);
        invited.setBackground(bg(Color.WHITE,14));

        stats.addView(
                invited,
                new LinearLayout.LayoutParams(0,dp(82),1)
        );

        TextView earned =
                tv(
                        "ðŸ’°\nà§³ "
                                + formatMoney(getInviteEarned())
                                + "\nà¦¬à§‹à¦¨à¦¾à¦¸",
                        15,
                        DARK
                );

        earned.setGravity(Gravity.CENTER);
        earned.setBackground(bg(Color.WHITE,14));

        LinearLayout.LayoutParams ep =
                new LinearLayout.LayoutParams(0,dp(82),1);

        ep.setMargins(dp(8),0,0,0);

        stats.addView(earned,ep);

        space(content,10);

        TextView rulesTitle =
                tv(
                        "ðŸ“‹ à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ à¦¬à§‹à¦¨à¦¾à¦¸ à¦¨à¦¿à¦¯à¦¼à¦®",
                        18,
                        DARK
                );

        rulesTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        content.addView(
                rulesTitle,
                new LinearLayout.LayoutParams(-1,dp(38))
        );

        TextView rules =
                tv(
                        "â€¢ à¦†à¦ªà¦¨à¦¾à¦° Invite Code à¦•à¦ªà¦¿ à¦¬à¦¾ à¦¶à§‡à¦¯à¦¼à¦¾à¦° à¦•à¦°à§à¦¨à¥¤\n"
                                + "â€¢ à¦¨à¦¤à§à¦¨ à¦‡à¦‰à¦œà¦¾à¦° à¦°à§‡à¦œà¦¿à¦¸à§à¦Ÿà§à¦°à§‡à¦¶à¦¨à§‡à¦° à¦¸à¦®à¦¯à¦¼ à¦•à§‹à¦¡ à¦¬à§à¦¯à¦¬à¦¹à¦¾à¦° à¦•à¦°à¦¤à§‡ à¦ªà¦¾à¦°à¦¬à§‡à¥¤\n"
                                + "â€¢ Invite Count à¦“ Bonus Amount à¦¡à§‡à¦®à§‹ à¦ªà§à¦¯à¦¾à¦¨à§‡à¦² à¦¥à§‡à¦•à§‡ à¦‡à¦šà§à¦›à¦¾à¦®à¦¤à§‹ à¦ªà¦°à¦¿à¦¬à¦°à§à¦¤à¦¨ à¦•à¦°à¦¾ à¦¯à¦¾à¦¬à§‡à¥¤\n"
                                + "â€¢ à¦¨à¦¤à§à¦¨ Invite à¦¹à¦²à§‡ à¦¸à¦‚à¦–à§à¦¯à¦¾ à§§, à§¨, à§©, à§ª, à§«, à§¬... à¦à¦­à¦¾à¦¬à§‡ à¦¦à§‡à¦–à¦¾à¦¬à§‡à¥¤",
                        14,
                        Color.DKGRAY
                );

        rules.setPadding(
                dp(15),dp(12),dp(15),dp(12)
        );
        rules.setBackground(bg(Color.WHITE,14));

        content.addView(
                rules,
                new LinearLayout.LayoutParams(-1,dp(150))
        );

        space(content,10);

        TextView historyButton =
                button(
                        "ðŸŽ à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ à¦Ÿà§à¦°à¦¾à¦¨à§à¦¸à§‡à¦•à¦¶à¦¨ / à¦¹à¦¿à¦¸à§à¦Ÿà§‹à¦°à¦¿",
                        Color.WHITE,
                        BLUE
                );

        content.addView(
                historyButton,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        historyButton.setOnClickListener(
                v -> showInviteHistory()
        );
    }

    private void syncInviteBonusTransaction(){try{double earned=getInviteEarned();double recorded=Double.parseDouble(pref.getString("invite_earned_recorded","0"));if(earned>recorded){recordTransaction("à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ à¦¬à§‹à¦¨à¦¾à¦¸","Invite Bonus",earned-recorded,true);pref.edit().putString("invite_earned_recorded",String.valueOf(earned)).apply();}}catch(Exception ignored){}}

    private void showInviteHistory() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout main = new LinearLayout(this);

        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.rgb(247,248,250));

        setContentView(main);

        LinearLayout header = new LinearLayout(this);

        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(8),0,dp(8),0);
        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        TextView back = tv("â€¹",38,Color.WHITE);
        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        back.setOnClickListener(v -> showInviteBonus());

        TextView title =
                tv("à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ à¦Ÿà§à¦°à¦¾à¦¨à§à¦¸à§‡à¦•à¦¶à¦¨",20,Color.WHITE);

        title.setGravity(Gravity.CENTER);
        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        header.addView(
                title,
                new LinearLayout.LayoutParams(0,dp(62),1)
        );

        header.addView(
                new Space(this),
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        ScrollView scroll = new ScrollView(this);

        LinearLayout list = new LinearLayout(this);

        list.setOrientation(LinearLayout.VERTICAL);
        list.setPadding(
                dp(12),dp(12),dp(12),dp(25)
        );

        scroll.addView(list);

        main.addView(
                scroll,
                new LinearLayout.LayoutParams(-1,0,1)
        );

        int count = getInviteCount();

        if (count == 0) {

            LinearLayout empty = new LinearLayout(this);

            empty.setOrientation(LinearLayout.VERTICAL);
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(
                    dp(15),dp(25),dp(15),dp(25)
            );
            empty.setBackground(bg(Color.WHITE,16));

            list.addView(
                    empty,
                    new LinearLayout.LayoutParams(-1,dp(190))
            );

            TextView icon = tv("ðŸŽ",42,DARK);
            icon.setGravity(Gravity.CENTER);

            empty.addView(
                    icon,
                    new LinearLayout.LayoutParams(-1,dp(55))
            );

            TextView msg =
                    tv(
                            "à¦à¦–à¦¨à¦“ à¦•à§‹à¦¨à§‹ à¦‡à¦¨à¦­à¦¾à¦‡à¦Ÿ à¦Ÿà§à¦°à¦¾à¦¨à§à¦¸à§‡à¦•à¦¶à¦¨ à¦¨à§‡à¦‡",
                            16,
                            DARK
                    );

            msg.setGravity(Gravity.CENTER);

            empty.addView(
                    msg,
                    new LinearLayout.LayoutParams(-1,dp(45))
            );

            TextView sub =
                    tv(
                            "Invite Code à¦¶à§‡à¦¯à¦¼à¦¾à¦° à¦•à¦°à¦²à§‡ à¦à¦–à¦¾à¦¨à§‡ à¦¹à¦¿à¦¸à¦¾à¦¬ à¦¦à§‡à¦–à¦¾à¦¨à§‹ à¦¯à¦¾à¦¬à§‡à¥¤",
                            13,
                            Color.GRAY
                    );

            sub.setGravity(Gravity.CENTER);

            empty.addView(
                    sub,
                    new LinearLayout.LayoutParams(-1,dp(45))
            );

            return;
        }

        for (int i = count; i >= 1; i--) {

            LinearLayout row = new LinearLayout(this);

            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(
                    dp(12),dp(8),dp(12),dp(8)
            );
            row.setBackground(bg(Color.WHITE,14));

            LinearLayout.LayoutParams rp =
                    new LinearLayout.LayoutParams(
                            -1,
                            dp(82)
                    );

            rp.setMargins(0,0,0,dp(8));

            list.addView(row,rp);

            TextView icon =
                    tv("ðŸ‘¤",30,BLUE);

            icon.setGravity(Gravity.CENTER);

            row.addView(
                    icon,
                    new LinearLayout.LayoutParams(
                            dp(48),dp(62)
                    )
            );

            LinearLayout info = new LinearLayout(this);

            info.setOrientation(LinearLayout.VERTICAL);
            info.setGravity(Gravity.CENTER_VERTICAL);

            row.addView(
                    info,
                    new LinearLayout.LayoutParams(0,dp(62),1)
            );

            TextView name =
                    tv(
                            "Invited User #" + i,
                            15,
                            DARK
                    );

            name.setTypeface(
                    Typeface.DEFAULT,
                    Typeface.BOLD
            );

            info.addView(
                    name,
                    new LinearLayout.LayoutParams(-1,dp(30))
            );

            TextView status =
                    tv(
                            "Referral code: " + getInviteCode(),
                            12,
                            Color.GRAY
                    );

            info.addView(
                    status,
                    new LinearLayout.LayoutParams(-1,dp(25))
            );

            TextView amount =
                    tv(
                            "Pending",
                            13,
                            Color.rgb(190,120,0)
                    );

            amount.setGravity(Gravity.CENTER);

            row.addView(
                    amount,
                    new LinearLayout.LayoutParams(dp(78),dp(55))
            );
        }
    }


    /* =========================================================
       SPECIAL OFFERS
       ========================================================= */

    private JSONArray getSpecialOfferData() {
        String saved = pref.getString("special_offers", "");
        try {
            if (!saved.trim().isEmpty()) return new JSONArray(saved);
        } catch (Exception ignored) {}

        JSONArray defaults = new JSONArray();
        defaults.put(makeOffer("à¦—à§à¦°à¦¾à¦®à§€à¦£à¦«à§‹à¦¨","","10 GB","100 à¦®à¦¿à¦¨à¦¿à¦Ÿ","","299","50","30 à¦¦à¦¿à¦¨","à¦¬à¦¿à¦¶à§‡à¦· à¦‡à¦¨à§à¦Ÿà¦¾à¦°à¦¨à§‡à¦Ÿ à¦“ à¦®à¦¿à¦¨à¦¿à¦Ÿ à¦…à¦«à¦¾à¦°"));
        defaults.put(makeOffer("à¦—à§à¦°à¦¾à¦®à§€à¦£à¦«à§‹à¦¨","","5 GB","200 à¦®à¦¿à¦¨à¦¿à¦Ÿ","50 SMS","199","30","15 à¦¦à¦¿à¦¨","à¦¦à§ˆà¦¨à¦¨à§à¦¦à¦¿à¦¨ à¦¬à§à¦¯à¦¬à¦¹à¦¾à¦°à§‡à¦° à¦…à¦«à¦¾à¦°"));
        defaults.put(makeOffer("à¦¬à¦¾à¦‚à¦²à¦¾à¦²à¦¿à¦‚à¦•","","12 GB","100 à¦®à¦¿à¦¨à¦¿à¦Ÿ","","299","50","30 à¦¦à¦¿à¦¨","à¦¡à¦¾à¦Ÿà¦¾ + à¦®à¦¿à¦¨à¦¿à¦Ÿ à¦ªà§à¦¯à¦¾à¦•"));
        defaults.put(makeOffer("Airtel","","10 GB","150 à¦®à¦¿à¦¨à¦¿à¦Ÿ","","279","50","30 à¦¦à¦¿à¦¨","à¦¡à¦¾à¦Ÿà¦¾ à¦“ à¦®à¦¿à¦¨à¦¿à¦Ÿ à¦…à¦«à¦¾à¦°"));
        defaults.put(makeOffer("Robi","","8 GB","200 à¦®à¦¿à¦¨à¦¿à¦Ÿ","","299","100","30 à¦¦à¦¿à¦¨","à¦¬à§‹à¦¨à¦¾à¦¸à¦¸à¦¹ à¦…à¦«à¦¾à¦°"));
        defaults.put(makeOffer("Teletalk","","5 GB","100 à¦®à¦¿à¦¨à¦¿à¦Ÿ","","199","50","15 à¦¦à¦¿à¦¨","à¦•à¦® à¦¦à¦¾à¦®à§‡à¦° à¦ªà§à¦¯à¦¾à¦•"));

        pref.edit().putString("special_offers", defaults.toString()).apply();
        return defaults;
    }

    private JSONObject makeOffer(String operator, String logo, String data, String minutes,
                                 String sms, String price, String bonus, String validity,
                                 String description) {
        JSONObject o = new JSONObject();
        try {
            o.put("operator", operator);
            o.put("logo", logo);
            o.put("data", data);
            o.put("minutes", minutes);
            o.put("sms", sms);
            o.put("price", price);
            o.put("bonus", bonus);
            o.put("validity", validity);
            o.put("description", description);
            o.put("visible", true);
        } catch (Exception ignored) {}
        return o;
    }

    private String offerText(JSONObject o, String key) {
        try { return o.optString(key, "").trim(); }
        catch (Exception e) { return ""; }
    }

    private boolean offerVisible(JSONObject o) {
        return o.optBoolean("visible", true);
    }

    private void showSpecialOffers() {
        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout main = new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.rgb(247,248,250));
        setContentView(main);

        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(8),0,dp(8),0);
        header.setBackgroundColor(BLUE);
        main.addView(header,new LinearLayout.LayoutParams(-1,dp(62)));

        TextView back = tv("â€¹",38,Color.WHITE);
        back.setGravity(Gravity.CENTER);
        header.addView(back,new LinearLayout.LayoutParams(dp(52),dp(62)));
        back.setOnClickListener(v -> showHome());

        TextView title = tv(tx("à¦¬à¦¿à¦¶à§‡à¦· à¦…à¦«à¦¾à¦°","Special Offers"),21,Color.WHITE);
        title.setGravity(Gravity.CENTER);
        title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        header.addView(title,new LinearLayout.LayoutParams(0,dp(62),1));
        header.addView(new Space(this),new LinearLayout.LayoutParams(dp(52),dp(62)));

        ScrollView scroll = new ScrollView(this);
        LinearLayout content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(12),dp(12),dp(12),dp(25));
        scroll.addView(content);
        main.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));

        TextView intro = tv(tx("à¦…à¦«à¦¾à¦° à¦¨à¦¿à¦°à§à¦¬à¦¾à¦šà¦¨ à¦•à¦°à§à¦¨","Select Offer"),18,DARK);
        intro.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        intro.setPadding(dp(4),0,dp(4),dp(10));
        content.addView(intro,new LinearLayout.LayoutParams(-1,dp(42)));

        final String[] operators = {"à¦—à§à¦°à¦¾à¦®à§€à¦£à¦«à§‹à¦¨","à¦¬à¦¾à¦‚à¦²à¦¾à¦²à¦¿à¦‚à¦•","Robi","Airtel","Teletalk"};
        final JSONArray offers = getSpecialOfferData();

        HorizontalScrollView opScroll = new HorizontalScrollView(this);
        opScroll.setHorizontalScrollBarEnabled(false);
        LinearLayout opRow = new LinearLayout(this);
        opRow.setPadding(dp(2),0,dp(2),dp(8));
        opScroll.addView(opRow);
        content.addView(opScroll,new LinearLayout.LayoutParams(-1,dp(66)));

        LinearLayout offerList = new LinearLayout(this);
        offerList.setOrientation(LinearLayout.VERTICAL);
        content.addView(offerList,new LinearLayout.LayoutParams(-1,-2));

        final TextView[] tabs = new TextView[operators.length];

        Runnable[] render = new Runnable[1];
        final String[] selected = {operators[0]};

        render[0] = () -> {
            offerList.removeAllViews();

            TextView selectedLabel = tv(
                    tx("à¦…à¦«à¦¾à¦° à¦¨à¦¿à¦°à§à¦¬à¦¾à¦šà¦¨ à¦•à¦°à§à¦¨","Choose an offer"),
                    16,
                    DARK
            );
            selectedLabel.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
            selectedLabel.setPadding(dp(4),dp(2),dp(4),dp(10));
            offerList.addView(selectedLabel,new LinearLayout.LayoutParams(-1,dp(38)));

            int count = 0;

            for (int i=0;i<offers.length();i++) {
                JSONObject o = offers.optJSONObject(i);
                if (o == null || !offerVisible(o)) continue;

                String operator = offerText(o,"operator");
                if (!selected[0].equalsIgnoreCase(operator)) continue;

                count++;

                LinearLayout card = new LinearLayout(this);
                card.setOrientation(LinearLayout.VERTICAL);
                card.setPadding(dp(16),dp(14),dp(16),dp(14));
                card.setBackground(outline(Color.WHITE,Color.rgb(225,228,232),16));

                LinearLayout.LayoutParams cp =
                        new LinearLayout.LayoutParams(-1,-2);
                cp.setMargins(0,0,0,dp(10));
                offerList.addView(card,cp);

                String logo = offerText(o,"logo");
                String offerName = offerText(o,"offer_name");
                TextView name = tv(
                        (logo.isEmpty() ? "ðŸ“± " : logo+"  ")
                                + (offerName.isEmpty() ? selected[0] : offerName),
                        17,
                        DARK
                );
                name.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                card.addView(name,new LinearLayout.LayoutParams(-1,dp(32)));

                String data = offerText(o,"data");
                String minutes = offerText(o,"minutes");
                String sms = offerText(o,"sms");
                String price = offerText(o,"price");
                String bonus = offerText(o,"bonus");
                String validity = offerText(o,"validity");
                String description = offerText(o,"description");

                StringBuilder mainLine = new StringBuilder();
                if (!data.isEmpty()) mainLine.append(data);
                if (!minutes.isEmpty()) {
                    if (mainLine.length() > 0) mainLine.append("  ");
                    mainLine.append(minutes);
                }
                if (!sms.isEmpty()) {
                    if (mainLine.length() > 0) mainLine.append("  ");
                    mainLine.append(sms);
                }

                if (mainLine.length() > 0) {
                    TextView x = tv(mainLine.toString(),16,DARK);
                    x.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                    card.addView(x,new LinearLayout.LayoutParams(-1,dp(34)));
                }

                if (!validity.isEmpty()) {
                    TextView x = tv(
                            tx("à¦®à§‡à¦¯à¦¼à¦¾à¦¦: ","Validity: ") + validity,
                            14,
                            Color.DKGRAY
                    );
                    card.addView(x,new LinearLayout.LayoutParams(-1,dp(26)));
                }

                LinearLayout priceRow = new LinearLayout(this);
                priceRow.setGravity(Gravity.CENTER_VERTICAL);

                if (!price.isEmpty()) {
                    TextView x = tv("à§³ "+price,19,BLUE);
                    x.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                    priceRow.addView(x,new LinearLayout.LayoutParams(0,dp(34),1));
                } else {
                    priceRow.addView(new Space(this),new LinearLayout.LayoutParams(0,dp(34),1));
                }

                if (!bonus.isEmpty() && !bonus.equals("0")) {
                    TextView x = tv(
                            tx("à¦¬à§‹à¦¨à¦¾à¦¸ à§³ "+bonus,"Bonus à§³ "+bonus),
                            14,
                            GREEN
                    );
                    x.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                    priceRow.addView(x,new LinearLayout.LayoutParams(-2,dp(34)));
                }

                card.addView(priceRow);

                if (!description.isEmpty()) {
                    TextView x = tv(description,13,Color.GRAY);
                    x.setPadding(0,dp(4),0,dp(7));
                    card.addView(x,new LinearLayout.LayoutParams(-1,-2));
                }

                TextView take = button(
                        tx("à¦…à¦«à¦¾à¦° à¦¨à¦¿à¦¨","Get Offer"),
                        GREEN,
                        Color.WHITE
                );
                card.addView(take,new LinearLayout.LayoutParams(-1,dp(48)));

                final String fOperator = selected[0];
                final String fData = data;
                final String fMinutes = minutes;
                final String fPrice = price;
                final String fBonus = bonus;
                final String fValidity = validity;

                take.setOnClickListener(v -> {
                    double amount = 0;
                    try { amount = Double.parseDouble(fPrice); }
                    catch (Exception ignored) {}

                    if (transactionBlockedFor(amount)) return;
                    Map<String,Object> offerReq = new HashMap<>();
                    offerReq.put("offer", fOperator); offerReq.put("data", fData); offerReq.put("minutes", fMinutes); offerReq.put("price", amount); offerReq.put("bonus", fBonus); offerReq.put("validity", fValidity);
                    submitFirestoreRequest("specialOfferRequests", offerReq);
                    recordTransaction(
                            "à¦¬à¦¿à¦¶à§‡à¦· à¦…à¦«à¦¾à¦°",
                            fOperator+" / "+fData+" / "+fMinutes,
                            amount,
                            false,
                            "PENDING"
                    );

                    String msg = fOperator
                            + (fData.isEmpty() ? "" : "\n"+fData)
                            + (fMinutes.isEmpty() ? "" : "\n"+fMinutes)
                            + (fPrice.isEmpty() ? "" : "\nà¦®à§‚à¦²à§à¦¯: à§³ "+fPrice)
                            + (fBonus.isEmpty() || fBonus.equals("0") ? "" : "\nà¦¬à§‹à¦¨à¦¾à¦¸: à§³ "+fBonus)
                            + (fValidity.isEmpty() ? "" : "\nà¦®à§‡à¦¯à¦¼à¦¾à¦¦: "+fValidity);

                    new AlertDialog.Builder(this)
                            .setTitle(tx("à¦…à¦«à¦¾à¦°","Offer"))
                            .setMessage(msg)
                            .setNegativeButton(tx("à¦¬à¦¨à§à¦§","Close"),null)
                            .setPositiveButton(tx("à¦ à¦¿à¦• à¦†à¦›à§‡","OK"),null)
                            .show();
                });
            }

            if (count == 0) {
                TextView empty = tv(
                        tx("à¦à¦‡ à¦…à¦ªà¦¾à¦°à§‡à¦Ÿà¦°à§‡à¦° à¦•à§‹à¦¨à§‹ à¦…à¦«à¦¾à¦° à¦¨à§‡à¦‡","No offers available for this operator"),
                        15,
                        Color.GRAY
                );
                empty.setGravity(Gravity.CENTER);
                offerList.addView(empty,new LinearLayout.LayoutParams(-1,dp(90)));
            }
        };

        for (int i=0;i<operators.length;i++) {
            final int index = i;
            TextView tab = tv(operators[i],14,DARK);
            tab.setGravity(Gravity.CENTER);
            tab.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
            tab.setPadding(dp(14),0,dp(14),0);
            opRow.addView(
                    tab,
                    new LinearLayout.LayoutParams(dp(118),dp(48))
            );
            tabs[i] = tab;

            tab.setOnClickListener(v -> {
                selected[0] = operators[index];
                for (int j=0;j<tabs.length;j++) {
                    if (j == index) {
                        tabs[j].setTextColor(BLUE);
                        tabs[j].setBackground(outline(Color.WHITE,BLUE,22));
                    } else {
                        tabs[j].setTextColor(DARK);
                        tabs[j].setBackground(bg(Color.WHITE,22));
                    }
                }
                render[0].run();
            });
        }

        tabs[0].setTextColor(BLUE);
        tabs[0].setBackground(outline(Color.WHITE,BLUE,22));
        for (int i=1;i<tabs.length;i++) {
            tabs[i].setBackground(bg(Color.WHITE,22));
        }

        render[0].run();
    }

    /* =========================================================
       CUSTOMER CARE
       ========================================================= */

    private String getCustomerCareWhatsApp() {
        return pref.getString("customer_care_whatsapp", "").trim();
    }

    private void showCustomerCare() {
        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);
        LinearLayout main=new LinearLayout(this);
        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.rgb(247,248,250));
        setContentView(main);

        LinearLayout header=new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL); header.setPadding(dp(8),0,dp(8),0); header.setBackgroundColor(BLUE);
        main.addView(header,new LinearLayout.LayoutParams(-1,dp(62)));
        TextView back=tv("â€¹",38,Color.WHITE); back.setGravity(Gravity.CENTER);
        header.addView(back,new LinearLayout.LayoutParams(dp(52),dp(62))); back.setOnClickListener(v->showHome());
        TextView title=tv("à¦•à¦¾à¦¸à§à¦Ÿà¦®à¦¾à¦° à¦•à§‡à¦¯à¦¼à¦¾à¦°",21,Color.WHITE); title.setGravity(Gravity.CENTER); title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        header.addView(title,new LinearLayout.LayoutParams(0,dp(62),1)); header.addView(new Space(this),new LinearLayout.LayoutParams(dp(52),dp(62)));

        LinearLayout content=new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); content.setGravity(Gravity.TOP); content.setPadding(dp(16),dp(20),dp(16),dp(25));
        main.addView(content,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout card=new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setGravity(Gravity.CENTER_HORIZONTAL); card.setPadding(dp(18),dp(25),dp(18),dp(25)); card.setBackground(bg(Color.WHITE,18));
        content.addView(card,new LinearLayout.LayoutParams(-1,-2));
        TextView icon=tv("ðŸ’¬",48,GREEN); icon.setGravity(Gravity.CENTER); card.addView(icon,new LinearLayout.LayoutParams(-1,dp(65)));
        TextView heading=tv("à¦•à¦¾à¦¸à§à¦Ÿà¦®à¦¾à¦° à¦•à§‡à¦¯à¦¼à¦¾à¦°",22,DARK); heading.setTypeface(Typeface.DEFAULT,Typeface.BOLD); heading.setGravity(Gravity.CENTER); card.addView(heading,new LinearLayout.LayoutParams(-1,dp(40)));

        String number=getCustomerCareWhatsApp();
        TextView numberText=tv(number.isEmpty()?"WhatsApp à¦¨à¦®à§à¦¬à¦° à¦à¦–à¦¨à§‹ à¦¸à§‡à¦Ÿ à¦•à¦°à¦¾ à¦¹à¦¯à¦¼à¦¨à¦¿":"WhatsApp: "+number,15,Color.DKGRAY);
        numberText.setGravity(Gravity.CENTER); numberText.setPadding(0,dp(8),0,dp(15)); card.addView(numberText,new LinearLayout.LayoutParams(-1,dp(55)));
        TextView contact=button("WhatsApp-à¦ à¦¯à§‹à¦—à¦¾à¦¯à§‹à¦— à¦•à¦°à§à¦¨",GREEN,Color.WHITE);
        card.addView(contact,new LinearLayout.LayoutParams(-1,dp(55)));
        contact.setEnabled(!number.isEmpty());
        if(number.isEmpty()) contact.setBackground(bg(Color.rgb(170,170,170),12));
        contact.setOnClickListener(v->openCustomerCareWhatsApp(number));
    }

    private void openCustomerCareWhatsApp(String rawNumber) {
        String number=rawNumber.replaceAll("[^0-9]","");
        if(number.startsWith("00")) number=number.substring(2);
        if(number.startsWith("0")) number="88"+number;
        else if(!number.startsWith("88")) number="88"+number;
        try {
            android.content.Intent app=new android.content.Intent(android.content.Intent.ACTION_VIEW,android.net.Uri.parse("whatsapp://send?phone="+number));
            startActivity(app);
        } catch(Exception e) {
            try {
                android.content.Intent web=new android.content.Intent(android.content.Intent.ACTION_VIEW,android.net.Uri.parse("https://wa.me/"+number));
                startActivity(web);
            } catch(Exception ignored) {
                Toast.makeText(this,"WhatsApp à¦–à§‹à¦²à¦¾ à¦¯à¦¾à¦šà§à¦›à§‡ à¦¨à¦¾",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* =========================================================
       CUSTOMER REVIEWS
       ========================================================= */

    private JSONArray getCustomerReviewData() {
        String saved=pref.getString("customer_reviews","");
        try { if(!saved.trim().isEmpty()) return new JSONArray(saved); }
        catch(Exception ignored) {}
        return new JSONArray();
    }

    private void showCustomerReviews() {
        getWindow().setStatusBarColor(BLUE); getWindow().setNavigationBarColor(Color.WHITE);
        LinearLayout main=new LinearLayout(this); main.setOrientation(LinearLayout.VERTICAL); main.setBackgroundColor(Color.rgb(247,248,250)); setContentView(main);
        LinearLayout header=new LinearLayout(this); header.setGravity(Gravity.CENTER_VERTICAL); header.setPadding(dp(8),0,dp(8),0); header.setBackgroundColor(BLUE); main.addView(header,new LinearLayout.LayoutParams(-1,dp(62)));
        TextView back=tv("â€¹",38,Color.WHITE); back.setGravity(Gravity.CENTER); header.addView(back,new LinearLayout.LayoutParams(dp(52),dp(62))); back.setOnClickListener(v->showHome());
        TextView title=tv("à¦•à¦¾à¦¸à§à¦Ÿà¦®à¦¾à¦° à¦°à¦¿à¦­à¦¿à¦‰",21,Color.WHITE); title.setGravity(Gravity.CENTER); title.setTypeface(Typeface.DEFAULT,Typeface.BOLD); header.addView(title,new LinearLayout.LayoutParams(0,dp(62),1)); header.addView(new Space(this),new LinearLayout.LayoutParams(dp(52),dp(62)));
        ScrollView scroll=new ScrollView(this); LinearLayout list=new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); list.setPadding(dp(12),dp(12),dp(12),dp(25)); scroll.addView(list); main.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        JSONArray reviews=getCustomerReviewData();
        if(reviews.length()==0) { addVideoEmptyState(list,"â­","à¦•à¦¾à¦¸à§à¦Ÿà¦®à¦¾à¦° à¦°à¦¿à¦­à¦¿à¦‰","à¦•à¦¾à¦¸à§à¦Ÿà¦®à¦¾à¦°à¦¦à§‡à¦° à¦­à¦¿à¦¡à¦¿à¦“ à¦°à¦¿à¦­à¦¿à¦‰ à¦à¦–à¦¾à¦¨à§‡ à¦¦à§‡à¦–à¦¾ à¦¯à¦¾à¦¬à§‡à¥¤"); return; }
        for(int i=0;i<reviews.length();i++) { JSONObject item=reviews.optJSONObject(i); if(item==null||!item.optBoolean("visible",true)) continue; addVideoCard(list,item.optString("title","à¦•à¦¾à¦¸à§à¦Ÿà¦®à¦¾à¦° à¦°à¦¿à¦­à¦¿à¦‰"),item.optString("description",""),item.optString("url","")); }
    }

    /* =========================================================
       VIDEO TUTORIALS
       ========================================================= */

    private JSONArray getTutorialData() {
        String saved=pref.getString("video_tutorials","");
        try { if(!saved.trim().isEmpty()) return new JSONArray(saved); }
        catch(Exception ignored) {}
        return new JSONArray();
    }

    private void showVideoTutorials() {
        getWindow().setStatusBarColor(BLUE); getWindow().setNavigationBarColor(Color.WHITE);
        LinearLayout main=new LinearLayout(this); main.setOrientation(LinearLayout.VERTICAL); main.setBackgroundColor(Color.rgb(247,248,250)); setContentView(main);
        LinearLayout header=new LinearLayout(this); header.setGravity(Gravity.CENTER_VERTICAL); header.setPadding(dp(8),0,dp(8),0); header.setBackgroundColor(BLUE); main.addView(header,new LinearLayout.LayoutParams(-1,dp(62)));
        TextView back=tv("â€¹",38,Color.WHITE); back.setGravity(Gravity.CENTER); header.addView(back,new LinearLayout.LayoutParams(dp(52),dp(62))); back.setOnClickListener(v->showHome());
        TextView title=tv("à¦­à¦¿à¦¡à¦¿à¦“ à¦Ÿà¦¿à¦‰à¦Ÿà§‹à¦°à¦¿à¦¯à¦¼à¦¾à¦²",21,Color.WHITE); title.setGravity(Gravity.CENTER); title.setTypeface(Typeface.DEFAULT,Typeface.BOLD); header.addView(title,new LinearLayout.LayoutParams(0,dp(62),1)); header.addView(new Space(this),new LinearLayout.LayoutParams(dp(52),dp(62)));
        ScrollView scroll=new ScrollView(this); LinearLayout list=new LinearLayout(this); list.setOrientation(LinearLayout.VERTICAL); list.setPadding(dp(12),dp(12),dp(12),dp(25)); scroll.addView(list); main.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        JSONArray tutorials=getTutorialData();
        if(tutorials.length()==0) { addVideoEmptyState(list,"â–¶","à¦­à¦¿à¦¡à¦¿à¦“ à¦Ÿà¦¿à¦‰à¦Ÿà§‹à¦°à¦¿à¦¯à¦¼à¦¾à¦²","à¦…à§à¦¯à¦¾à¦ª à¦¬à§à¦¯à¦¬à¦¹à¦¾à¦° à¦¶à§‡à¦–à¦¾à¦¨à§‹à¦° à¦­à¦¿à¦¡à¦¿à¦“ à¦à¦–à¦¾à¦¨à§‡ à¦¦à§‡à¦–à¦¾ à¦¯à¦¾à¦¬à§‡à¥¤"); return; }
        for(int i=0;i<tutorials.length();i++) { JSONObject item=tutorials.optJSONObject(i); if(item==null||!item.optBoolean("visible",true)) continue; addVideoCard(list,item.optString("title","à¦­à¦¿à¦¡à¦¿à¦“ à¦Ÿà¦¿à¦‰à¦Ÿà§‹à¦°à¦¿à¦¯à¦¼à¦¾à¦²"),item.optString("description",""),item.optString("url","")); }
    }

    private void addVideoEmptyState(LinearLayout parent,String iconText,String titleText,String messageText) {
        LinearLayout card=new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setGravity(Gravity.CENTER); card.setPadding(dp(20),dp(25),dp(20),dp(25)); card.setBackground(bg(Color.WHITE,16)); parent.addView(card,new LinearLayout.LayoutParams(-1,dp(210)));
        TextView icon=tv(iconText,44,DARK); icon.setGravity(Gravity.CENTER); card.addView(icon,new LinearLayout.LayoutParams(-1,dp(58)));
        TextView title=tv(titleText,18,DARK); title.setGravity(Gravity.CENTER); title.setTypeface(Typeface.DEFAULT,Typeface.BOLD); card.addView(title,new LinearLayout.LayoutParams(-1,dp(38)));
        TextView message=tv(messageText,14,Color.GRAY); message.setGravity(Gravity.CENTER); card.addView(message,new LinearLayout.LayoutParams(-1,dp(55)));
    }

    private void addVideoCard(LinearLayout parent,String titleText,String descriptionText,String url) {
        LinearLayout card=new LinearLayout(this); card.setOrientation(LinearLayout.VERTICAL); card.setPadding(dp(15),dp(14),dp(15),dp(14)); card.setBackground(bg(Color.WHITE,16));
        LinearLayout.LayoutParams cp=new LinearLayout.LayoutParams(-1,-2); cp.setMargins(0,0,0,dp(10)); parent.addView(card,cp);
        TextView play=tv("â–¶",34,BLUE); play.setGravity(Gravity.CENTER); card.addView(play,new LinearLayout.LayoutParams(-1,dp(62)));
        TextView title=tv(titleText,17,DARK); title.setTypeface(Typeface.DEFAULT,Typeface.BOLD); card.addView(title,new LinearLayout.LayoutParams(-1,dp(32)));
        if(!descriptionText.trim().isEmpty()) { TextView description=tv(descriptionText,13,Color.GRAY); description.setPadding(0,dp(4),0,dp(9)); card.addView(description,new LinearLayout.LayoutParams(-1,-2)); }
        TextView watch=button("à¦­à¦¿à¦¡à¦¿à¦“ à¦¦à§‡à¦–à§à¦¨",BLUE,Color.WHITE); card.addView(watch,new LinearLayout.LayoutParams(-1,dp(50))); watch.setEnabled(!url.trim().isEmpty()); if(url.trim().isEmpty()) watch.setBackground(bg(Color.rgb(170,170,170),12)); watch.setOnClickListener(v->openVideoUrl(url));
    }

    private void openVideoUrl(String url) {
        if(url==null||url.trim().isEmpty()) { Toast.makeText(this,"à¦­à¦¿à¦¡à¦¿à¦“ à¦²à¦¿à¦‚à¦• à¦à¦–à¦¨à§‹ à¦¸à§‡à¦Ÿ à¦•à¦°à¦¾ à¦¹à¦¯à¦¼à¦¨à¦¿",Toast.LENGTH_SHORT).show(); return; }
        try { startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW,android.net.Uri.parse(url.trim()))); }
        catch(Exception e) { Toast.makeText(this,"à¦­à¦¿à¦¡à¦¿à¦“ à¦–à§‹à¦²à¦¾ à¦¯à¦¾à¦šà§à¦›à§‡ à¦¨à¦¾",Toast.LENGTH_SHORT).show(); }
    }

    /* =========================================================
       GROUP CHAT
       ========================================================= */

    private void showGroupChat() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout main = new LinearLayout(this);

        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.rgb(248,249,251));

        setContentView(main);

        LinearLayout header = new LinearLayout(this);

        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(5),0,dp(5),0);
        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        TextView back = tv("â€¹",38,Color.WHITE);
        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        back.setOnClickListener(v -> showHome());

        LinearLayout titleBox = new LinearLayout(this);

        titleBox.setOrientation(LinearLayout.VERTICAL);
        titleBox.setGravity(Gravity.CENTER_VERTICAL);

        header.addView(
                titleBox,
                new LinearLayout.LayoutParams(0,dp(62),1)
        );

        TextView title = tv("à¦—à§à¦°à§à¦ª à¦šà§à¦¯à¦¾à¦Ÿ",20,Color.WHITE);
        title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        titleBox.addView(
                title,
                new LinearLayout.LayoutParams(-1,dp(34))
        );

        TextView online = tv("Quick Pay Community",11,Color.WHITE);

        titleBox.addView(
                online,
                new LinearLayout.LayoutParams(-1,dp(22))
        );

        TextView clear = tv("â‹®",30,Color.WHITE);
        clear.setGravity(Gravity.CENTER);

        header.addView(
                clear,
                new LinearLayout.LayoutParams(dp(48),dp(62))
        );

        clear.setOnClickListener(v -> showChatMenu());

        ScrollView scroll = new ScrollView(this);

        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(248,249,251));

        LinearLayout chatBox = new LinearLayout(this);

        chatBox.setOrientation(LinearLayout.VERTICAL);
        chatBox.setPadding(
                dp(10),dp(12),dp(10),dp(12)
        );

        scroll.addView(chatBox);

        main.addView(
                scroll,
                new LinearLayout.LayoutParams(-1,0,1)
        );

        loadChatMessages(chatBox,scroll);

        LinearLayout inputArea = new LinearLayout(this);

        inputArea.setGravity(Gravity.CENTER_VERTICAL);
        inputArea.setPadding(dp(8),dp(7),dp(8),dp(7));
        inputArea.setBackgroundColor(Color.WHITE);

        main.addView(
                inputArea,
                new LinearLayout.LayoutParams(-1,dp(70))
        );

        EditText message = new EditText(this);

        message.setHint("à¦®à§‡à¦¸à§‡à¦œ à¦²à¦¿à¦–à§à¦¨...");
        message.setTextSize(16);
        message.setSingleLine(false);
        message.setMaxLines(3);
        message.setTextColor(DARK);
        message.setHintTextColor(Color.GRAY);
        message.setPadding(dp(15),0,dp(15),0);

        message.setBackground(
                outline(
                        Color.rgb(247,249,252),
                        Color.rgb(215,222,232),
                        25
                )
        );

        inputArea.addView(
                message,
                new LinearLayout.LayoutParams(0,dp(54),1)
        );

        TextView send = tv("âž¤",24,Color.WHITE);

        send.setGravity(Gravity.CENTER);
        send.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        send.setBackground(bg(BLUE,50));

        LinearLayout.LayoutParams sendParams =
                new LinearLayout.LayoutParams(dp(54),dp(54));

        sendParams.leftMargin = dp(7);

        inputArea.addView(send,sendParams);

        send.setOnClickListener(v -> {

            String text =
                    message.getText()
                            .toString()
                            .trim();

            if (text.isEmpty()) {
                return;
            }

            saveChatMessage(text);

            message.setText("");

            loadChatMessages(chatBox,scroll);
        });

        message.setOnEditorActionListener((v,actionId,event) -> {

            String text =
                    message.getText()
                            .toString()
                            .trim();

            if (!text.isEmpty()) {

                saveChatMessage(text);
                message.setText("");
                loadChatMessages(chatBox,scroll);

                return true;
            }

            return false;
        });
    }

    /* =========================================================
       CHAT SAVE
       ========================================================= */

    private void saveChatMessage(String text) {
        String name = pref.getString("name","Rosy");
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

        // Keep the sender's own pending message visible locally.
        try {
            JSONArray array = new JSONArray(pref.getString("group_chat_messages","[]"));
            JSONObject object = new JSONObject();
            object.put("name", name);
            object.put("message", text);
            object.put("time", time);
            object.put("status", "PENDING");
            array.put(object);
            pref.edit().putString("group_chat_messages", array.toString()).apply();
        } catch (Exception ignored) {}

        if (!firebaseReady || firestore == null || firebaseUid().isEmpty()) {
            Toast.makeText(this,"Firebase à¦¸à¦‚à¦¯à§‹à¦— à¦¨à§‡à¦‡à¥¤ à¦®à§‡à¦¸à§‡à¦œà¦Ÿà¦¿ à¦¶à§à¦§à§ à¦à¦‡ à¦«à§‹à¦¨à§‡ à¦†à¦›à§‡à¥¤",Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String,Object> object = new HashMap<>();
        object.put("uid", firebaseUid());
        object.put("name", name);
        object.put("phone", pref.getString("phone", ""));
        object.put("message", text);
        object.put("status", "PENDING");
        object.put("createdAt", com.google.firebase.firestore.FieldValue.serverTimestamp());

        firestore.collection("groupChatMessages").add(object)
                .addOnSuccessListener(v -> Toast.makeText(this,
                        "à¦®à§‡à¦¸à§‡à¦œ à¦ªà¦¾à¦ à¦¾à¦¨à§‹ à¦¹à¦¯à¦¼à§‡à¦›à§‡à¥¤ à¦…à§à¦¯à¦¾à¦¡à¦®à¦¿à¦¨ à¦…à¦¨à§à¦®à§‹à¦¦à¦¨à§‡à¦° à¦ªà¦° à¦¸à¦¬à¦¾à¦‡ à¦¦à§‡à¦–à¦¤à§‡ à¦ªà¦¾à¦¬à§‡à¥¤",
                        Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(this,
                        "à¦®à§‡à¦¸à§‡à¦œ à¦¸à¦¾à¦°à§à¦­à¦¾à¦°à§‡ à¦ªà¦¾à¦ à¦¾à¦¨à§‹ à¦¯à¦¾à¦¯à¦¼à¦¨à¦¿à¥¤",
                        Toast.LENGTH_SHORT).show());
    }

    /* =========================================================
       LOAD CHAT
       ========================================================= */

    private void loadChatMessages(
            LinearLayout chatBox,
            ScrollView scroll) {

        if (!firebaseReady || firestore == null || firebaseUid().isEmpty()) {
            loadLocalChatMessages(chatBox, scroll);
            return;
        }

        if (chatListener != null) chatListener.remove();

        chatListener = firestore.collection("groupChatMessages")
                .whereEqualTo("status", "APPROVED")
                .addSnapshotListener((snap, error) -> {
                    if (error != null || snap == null) {
                        loadLocalChatMessages(chatBox, scroll);
                        return;
                    }
                    chatBox.removeAllViews();
                    if (snap.isEmpty()) {
                        addChatEmptyState(chatBox);
                    } else {
                        for (DocumentSnapshot doc : snap.getDocuments()) {
                            addChatMessage(chatBox,
                                    doc.getString("name") == null ? "User" : doc.getString("name"),
                                    doc.getString("message") == null ? "" : doc.getString("message"),
                                    formatFirestoreTime(doc));
                        }
                    }
                    chatBox.postDelayed(() -> scroll.fullScroll(ScrollView.FOCUS_DOWN), 100);
                });
    }

    private String formatFirestoreTime(DocumentSnapshot doc) {
        try {
            com.google.firebase.Timestamp ts = doc.getTimestamp("createdAt");
            if (ts != null) return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(ts.toDate());
        } catch (Exception ignored) {}
        return "";
    }

    private void addChatEmptyState(LinearLayout chatBox) {
        LinearLayout empty = new LinearLayout(this);
        empty.setOrientation(LinearLayout.VERTICAL);
        empty.setGravity(Gravity.CENTER);
        empty.setPadding(dp(20),dp(60),dp(20),dp(60));
        TextView icon = tv("ðŸ’¬",48,BLUE); icon.setGravity(Gravity.CENTER);
        empty.addView(icon,new LinearLayout.LayoutParams(-1,dp(70)));
        TextView title = tv("à¦—à§à¦°à§à¦ª à¦šà§à¦¯à¦¾à¦Ÿà§‡ à¦¸à§à¦¬à¦¾à¦—à¦¤à¦®",21,BLUE);
        title.setGravity(Gravity.CENTER); title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        empty.addView(title,new LinearLayout.LayoutParams(-1,dp(40)));
        TextView sub = tv("à¦…à§à¦¯à¦¾à¦¡à¦®à¦¿à¦¨ à¦…à¦¨à§à¦®à§‹à¦¦à¦¿à¦¤ à¦®à§‡à¦¸à§‡à¦œ à¦à¦–à¦¾à¦¨à§‡ à¦¦à§‡à¦–à¦¾ à¦¯à¦¾à¦¬à§‡à¥¤",15,Color.DKGRAY);
        sub.setGravity(Gravity.CENTER);
        empty.addView(sub,new LinearLayout.LayoutParams(-1,dp(45)));
        chatBox.addView(empty,new LinearLayout.LayoutParams(-1,-2));
    }

    private void loadLocalChatMessages(LinearLayout chatBox, ScrollView scroll) {
        chatBox.removeAllViews();
        try {
            JSONArray array = new JSONArray(pref.getString("group_chat_messages","[]"));
            if (array.length() == 0) {
                addChatEmptyState(chatBox);
            } else {
                for (int i=0;i<array.length();i++) {
                    JSONObject object=array.getJSONObject(i);
                    addChatMessage(chatBox,object.optString("name","User"),object.optString("message",""),object.optString("time",""));
                }
            }
        } catch(Exception e) { addChatEmptyState(chatBox); }
        chatBox.postDelayed(() -> scroll.fullScroll(ScrollView.FOCUS_DOWN),100);
    }

    /* =========================================================
       CHAT MESSAGE DESIGN
       ========================================================= */

    private void addChatMessage(
            LinearLayout parent,
            String name,
            String message,
            String time) {

        String currentName =
                pref.getString("name","Rosy");

        boolean mine =
                name.equals(currentName);

        LinearLayout row =
                new LinearLayout(this);

        row.setOrientation(
                LinearLayout.HORIZONTAL
        );

        row.setGravity(
                mine
                        ? Gravity.RIGHT
                        : Gravity.LEFT
        );

        LinearLayout.LayoutParams rowParams =
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                );

        rowParams.setMargins(
                dp(2),dp(4),dp(2),dp(4)
        );

        parent.addView(row,rowParams);

        LinearLayout bubble =
                new LinearLayout(this);

        bubble.setOrientation(
                LinearLayout.VERTICAL
        );

        bubble.setPadding(
                dp(13),
                dp(8),
                dp(13),
                dp(8)
        );

        if (mine) {

            bubble.setBackground(
                    bg(
                            Color.rgb(225,240,255),
                            16
                    )
            );

        } else {

            bubble.setBackground(
                    bg(
                            Color.WHITE,
                            16
                    )
            );
        }

        LinearLayout.LayoutParams bubbleParams =
                new LinearLayout.LayoutParams(
                        dp(285),
                        -2
                );

        row.addView(bubble,bubbleParams);

        TextView user =
                tv(
                        mine
                                ? "à¦†à¦ªà¦¨à¦¿"
                                : name,
                        13,
                        mine ? BLUE : GREEN
                );

        user.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        bubble.addView(
                user,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(24)
                )
        );

        TextView text =
                tv(
                        message,
                        16,
                        DARK
                );

        text.setPadding(0,dp(2),0,dp(2));
        text.setGravity(Gravity.LEFT);

        bubble.addView(
                text,
                new LinearLayout.LayoutParams(
                        -1,
                        -2
                )
        );

        TextView clock =
                tv(
                        time,
                        10,
                        Color.GRAY
                );

        clock.setGravity(
                mine
                        ? Gravity.RIGHT
                        : Gravity.LEFT
        );

        bubble.addView(
                clock,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(20)
                )
        );
    }

    /* =========================================================
       CHAT MENU
       ========================================================= */

    private void showChatMenu() {

        String[] options = {
                "à¦šà§à¦¯à¦¾à¦Ÿ à¦ªà¦°à¦¿à¦·à§à¦•à¦¾à¦° à¦•à¦°à§à¦¨",
                "à¦šà§à¦¯à¦¾à¦Ÿ à¦¸à¦®à§à¦ªà¦°à§à¦•à§‡"
        };

        new AlertDialog.Builder(this)
                .setTitle("à¦—à§à¦°à§à¦ª à¦šà§à¦¯à¦¾à¦Ÿ")
                .setItems(
                        options,
                        (dialog,which) -> {

                            if (which == 0) {

                                new AlertDialog.Builder(this)
                                        .setTitle(
                                                "à¦šà§à¦¯à¦¾à¦Ÿ à¦ªà¦°à¦¿à¦·à§à¦•à¦¾à¦° à¦•à¦°à¦¬à§‡à¦¨?"
                                        )
                                        .setMessage(
                                                "à¦à¦‡ à¦«à§‹à¦¨à§‡ à¦¸à¦‚à¦°à¦•à§à¦·à¦¿à¦¤ à¦¸à¦¬ à¦—à§à¦°à§à¦ª à¦šà§à¦¯à¦¾à¦Ÿ à¦®à§à¦›à§‡ à¦¯à¦¾à¦¬à§‡à¥¤"
                                        )
                                        .setNegativeButton(
                                                "à¦¬à¦¾à¦¤à¦¿à¦²",
                                                null
                                        )
                                        .setPositiveButton(
                                                "à¦®à§à¦›à§‡ à¦«à§‡à¦²à§à¦¨",
                                                (d,w) -> {

                                                    pref.edit()
                                                            .remove(
                                                                    "group_chat_messages"
                                                            )
                                                            .apply();

                                                    showGroupChat();

                                                    Toast.makeText(
                                                            this,
                                                            "à¦šà§à¦¯à¦¾à¦Ÿ à¦ªà¦°à¦¿à¦·à§à¦•à¦¾à¦° à¦•à¦°à¦¾ à¦¹à¦¯à¦¼à§‡à¦›à§‡",
                                                            Toast.LENGTH_SHORT
                                                    ).show();
                                                }
                                        )
                                        .show();

                            } else {

                                new AlertDialog.Builder(this)
                                        .setTitle(
                                                "à¦—à§à¦°à§à¦ª à¦šà§à¦¯à¦¾à¦Ÿ"
                                        )
                                        .setMessage(
                                                "à¦à¦–à¦¾à¦¨à§‡ Quick Pay à¦¬à§à¦¯à¦¬à¦¹à¦¾à¦°à¦•à¦¾à¦°à§€à¦°à¦¾ à¦—à§à¦°à§à¦ªà§‡ à¦•à¦¥à¦¾ à¦¬à¦²à¦¤à§‡ à¦ªà¦¾à¦°à¦¬à§‡à¥¤\n\n"
                                                        + "à¦¬à¦°à§à¦¤à¦®à¦¾à¦¨ à¦­à¦¾à¦°à§à¦¸à¦¨à§‡ à¦®à§‡à¦¸à§‡à¦œ à¦à¦‡ à¦¡à¦¿à¦­à¦¾à¦‡à¦¸à§‡ à¦²à§‹à¦•à¦¾à¦²à¦¿ à¦¸à¦‚à¦°à¦•à§à¦·à¦£ à¦•à¦°à¦¾ à¦¹à¦šà§à¦›à§‡à¥¤\n\n"
                                                        + "à¦ªà¦°à¦¬à¦°à§à¦¤à§€à¦¤à§‡ Backend / API / Socket à¦¯à§à¦•à§à¦¤ à¦•à¦°à¦²à§‡ à¦à¦•à¦¾à¦§à¦¿à¦• à¦¬à§à¦¯à¦¬à¦¹à¦¾à¦°à¦•à¦¾à¦°à§€à¦° à¦®à¦§à§à¦¯à§‡ à¦²à¦¾à¦‡à¦­ à¦šà§à¦¯à¦¾à¦Ÿ à¦•à¦°à¦¾ à¦¯à¦¾à¦¬à§‡à¥¤"
                                        )
                                        .setPositiveButton(
                                                "à¦ à¦¿à¦• à¦†à¦›à§‡",
                                                null
                                        )
                                        .show();
                            }
                        }
                )
                .show();
    }

    /* =========================================================
       ADD BALANCE
       ========================================================= */

    private void showAddBalance() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout main = new LinearLayout(this);

        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.WHITE);

        setContentView(main);

        LinearLayout header = new LinearLayout(this);

        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(8),0,dp(8),0);
        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        TextView back = tv("â€¹",38,Color.WHITE);
        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        back.setOnClickListener(v -> showHome());

        TextView title = tv("à¦…à§à¦¯à¦¾à¦¡ à¦¬à§à¦¯à¦¾à¦²à§‡à¦¨à§à¦¸",21,Color.WHITE);

        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        header.addView(
                title,
                new LinearLayout.LayoutParams(0,dp(62),1)
        );

        TextView info =
                tv(
                        "à¦†à¦ªà¦¨à¦¾à¦° Quick Pay à¦“à§Ÿà¦¾à¦²à§‡à¦Ÿà§‡ à¦Ÿà¦¾à¦•à¦¾ à¦¯à§‹à¦— à¦•à¦°à§à¦¨à¥¤\n\n"
                                + "à¦Ÿà¦¾à¦•à¦¾ à¦¯à§‹à¦— à¦•à¦°à¦¾à¦° à¦œà¦¨à§à¦¯ à¦¨à¦¿à¦šà§‡à¦° à¦…à¦Ÿà§‹ à¦¡à¦¿à¦ªà§‹à¦œà¦¿à¦Ÿ à¦…à¦ªà¦¶à¦¨ à¦¬à§à¦¯à¦¬à¦¹à¦¾à¦° à¦•à¦°à§à¦¨à¥¤",
                        17,
                        DARK
                );

        info.setPadding(dp(28),dp(35),dp(28),dp(20));

        main.addView(
                info,
                new LinearLayout.LayoutParams(-1,dp(150))
        );

        TextView auto =
                button(
                        "à¦…à¦Ÿà§‹ à¦¡à¦¿à¦ªà§‹à¦œà¦¿à¦Ÿ",
                        BLUE,
                        Color.WHITE
                );

        LinearLayout.LayoutParams ap =
                new LinearLayout.LayoutParams(-1,dp(60));

        ap.setMargins(dp(20),dp(10),dp(20),0);

        main.addView(auto,ap);

        auto.setOnClickListener(v -> showAutoDeposit());
    }

    /* =========================================================
       AUTO DEPOSIT
       ========================================================= */

    private void showAutoDeposit() {

        refreshPaymentNumbers();

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(248,249,251));

        setContentView(root);

        LinearLayout header = new LinearLayout(this);

        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(8),0,dp(8),0);
        header.setBackgroundColor(BLUE);

        root.addView(
                header,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        TextView back = tv("â€¹",38,Color.WHITE);
        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        back.setOnClickListener(v -> showAddBalance());

        TextView title = tv("à¦…à¦Ÿà§‹ à¦¡à¦¿à¦ªà§‹à¦œà¦¿à¦Ÿ",21,Color.WHITE);

        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        header.addView(
                title,
                new LinearLayout.LayoutParams(0,dp(62),1)
        );

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);

        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(14),dp(15),dp(14),dp(20));

        scroll.addView(content);

        root.addView(
                scroll,
                new LinearLayout.LayoutParams(-1,0,1)
        );

        TextView notice =
                tv(
                        "à¦¨à¦¿à¦šà§‡à¦° à¦¯à§‡à¦•à§‹à¦¨à§‹ à¦¨à¦®à§à¦¬à¦°à§‡ à¦Ÿà¦¾à¦•à¦¾ à¦ªà¦¾à¦ à¦¾à¦¨à¥¤\n"
                                + "à¦Ÿà¦¾à¦•à¦¾ à¦ªà¦¾à¦ à¦¾à¦¨à§‹à¦° à¦ªà¦° à¦¸à¦ à¦¿à¦• à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦“ Transaction ID à¦¦à¦¿à¦¯à¦¼à§‡ à¦¸à¦¾à¦¬à¦®à¦¿à¦Ÿ à¦•à¦°à§à¦¨à¥¤",
                        16,
                        DARK
                );

        notice.setPadding(dp(18),dp(18),dp(18),dp(18));
        notice.setBackground(bg(Color.WHITE,18));

        content.addView(
                notice,
                new LinearLayout.LayoutParams(-1,dp(105))
        );

        space(content,12);

        addDepositProvider(
                content,
                "bKash",
                "à¦¬à¦¿à¦•à¦¾à¦¶ à¦ªà¦¾à¦°à§à¦¸à§‹à¦¨à¦¾à¦²",
                ""
        );

        addDepositProvider(
                content,
                "Nagad",
                "à¦¨à¦—à¦¦ à¦ªà¦¾à¦°à§à¦¸à§‹à¦¨à¦¾à¦²",
                ""
        );

        addDepositProvider(
                content,
                "Rocket",
                "à¦°à¦•à§‡à¦Ÿ à¦ªà¦¾à¦°à§à¦¸à§‹à¦¨à¦¾à¦²",
                ""
        );

        addDepositProvider(
                content,
                "Upay",
                "à¦‰à¦ªà¦¾à§Ÿ à¦ªà¦¾à¦°à§à¦¸à§‹à¦¨à¦¾à¦²",
                ""
        );

        space(content,12);

        TextView amountTitle = tv("à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£",16,BLUE);

        amountTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        content.addView(
                amountTitle,
                new LinearLayout.LayoutParams(-1,dp(32))
        );

        EditText amount = input("à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£",false);

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        content.addView(
                amount,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(content,12);

        TextView trxTitle = tv("Transaction ID",16,BLUE);

        trxTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        content.addView(
                trxTitle,
                new LinearLayout.LayoutParams(-1,dp(32))
        );

        EditText trx = input("Transaction ID à¦²à¦¿à¦–à§à¦¨",false);

        trx.setInputType(
                InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        );

        content.addView(
                trx,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(content,18);

        TextView submit =
                button(
                        "à¦¡à¦¿à¦ªà§‹à¦œà¦¿à¦Ÿ à¦¸à¦¾à¦¬à¦®à¦¿à¦Ÿ à¦•à¦°à§à¦¨",
                        BLUE,
                        Color.WHITE
                );

        content.addView(
                submit,
                new LinearLayout.LayoutParams(-1,dp(60))
        );

        submit.setOnClickListener(v -> {

            String money =
                    amount.getText().toString().trim();

            String transaction =
                    trx.getText().toString().trim();

            if (money.isEmpty()) {
                amount.setError("à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¦à¦¿à¦¨");
                return;
            }

            if (transaction.isEmpty()) {
                trx.setError("Transaction ID à¦¦à¦¿à¦¨");
                return;
            }

            try {

                double value =
                        Double.parseDouble(money);

                if (value < 50) {
                    amount.setError("à¦¸à¦°à§à¦¬à¦¨à¦¿à¦®à§à¦¨ à§«à§¦ à¦Ÿà¦¾à¦•à¦¾");
                    return;
                }

            } catch (Exception e) {

                amount.setError("à¦¸à¦ à¦¿à¦• à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¦à¦¿à¦¨");
                return;
            }

            final String finalDepositMoney = money;
            final String finalDepositTransaction = transaction;

            new AlertDialog.Builder(this)
                    .setTitle("à¦¡à¦¿à¦ªà§‹à¦œà¦¿à¦Ÿ à¦¸à¦¾à¦¬à¦®à¦¿à¦Ÿ")
                    .setMessage(
                            "à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£: à§³ "
                                    + money
                                    + "\nTransaction ID: "
                                    + transaction
                                    + "\n\nà¦†à¦ªà¦¨à¦¾à¦° à¦¡à¦¿à¦ªà§‹à¦œà¦¿à¦Ÿ à¦°à¦¿à¦•à§‹à¦¯à¦¼à§‡à¦¸à§à¦Ÿ à¦¸à¦¾à¦¬à¦®à¦¿à¦Ÿ à¦•à¦°à¦¾ à¦¹à¦¬à§‡à¥¤"
                    )
                    .setNegativeButton("à¦¬à¦¾à¦¤à¦¿à¦²",null)
                    .setPositiveButton(
                            "à¦¸à¦¾à¦¬à¦®à¦¿à¦Ÿ",
                            (dialog,which) -> {

                                submitDepositRequestToFirebase(Double.parseDouble(finalDepositMoney), finalDepositTransaction);

                                amount.setText("");
                                trx.setText("");
                            }
                    )
                    .show();
        });
    }

    /* =========================================================
       DEPOSIT PROVIDER
       ========================================================= */

    private void addDepositProvider(
            LinearLayout parent,
            String name,
            String subtitle,
            String number) {

        String adminNumber = paymentNumberFor(name);
        if (!adminNumber.isEmpty()) number = adminNumber;

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(12),dp(7),dp(12),dp(7));
        card.setBackground(bg(Color.WHITE,14));

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(-1,dp(86));

        cardParams.setMargins(0,0,0,dp(7));

        parent.addView(card,cardParams);

        TextView title =
                tv(
                        name + "  â€¢  " + subtitle,
                        17,
                        BLUE
                );

        title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                title,
                new LinearLayout.LayoutParams(-1,dp(28))
        );

        LinearLayout row = new LinearLayout(this);

        row.setGravity(Gravity.CENTER_VERTICAL);

        card.addView(
                row,
                new LinearLayout.LayoutParams(-1,dp(42))
        );

        TextView phone =
                tv(
                        number.isEmpty()
                                ? "à¦¨à¦®à§à¦¬à¦° à¦à¦–à¦¨à§‹ à¦¸à§‡à¦Ÿ à¦•à¦°à¦¾ à¦¹à§Ÿà¦¨à¦¿"
                                : number,
                        15,
                        number.isEmpty()
                                ? Color.GRAY
                                : DARK
                );

        phone.setGravity(Gravity.CENTER_VERTICAL);

        row.addView(
                phone,
                new LinearLayout.LayoutParams(0,dp(40),1)
        );

        TextView copy =
                button(
                        "à¦•à¦ªà¦¿",
                        BLUE,
                        Color.WHITE
                );

        row.addView(
                copy,
                new LinearLayout.LayoutParams(dp(68),dp(38))
        );

        copy.setOnClickListener(v -> {

            if (number.isEmpty()) {

                Toast.makeText(
                        this,
                        "à¦¨à¦®à§à¦¬à¦° à¦à¦–à¦¨à§‹ à¦¸à§‡à¦Ÿ à¦•à¦°à¦¾ à¦¹à§Ÿà¦¨à¦¿",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            android.content.ClipboardManager clipboard =
                    (android.content.ClipboardManager)
                            getSystemService(
                                    Context.CLIPBOARD_SERVICE
                            );

            clipboard.setPrimaryClip(
                    android.content.ClipData.newPlainText(
                            "Company Number",
                            number
                    )
            );

            Toast.makeText(
                    this,
                    name + " à¦¨à¦®à§à¦¬à¦° à¦•à¦ªà¦¿ à¦¹à¦¯à¦¼à§‡à¦›à§‡",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    /* =========================================================
       BANK TRANSFER
       ========================================================= */

    private void showBankTransfer() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout main = new LinearLayout(this);

        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.rgb(248,249,251));

        setContentView(main);

        LinearLayout header = new LinearLayout(this);

        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(8),0,dp(8),0);
        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        TextView back = tv("â€¹",38,Color.WHITE);
        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        back.setOnClickListener(v -> showHome());

        TextView title = tv("à¦¬à§à¦¯à¦¾à¦‚à¦• à¦Ÿà§à¦°à¦¾à¦¨à§à¦¸à¦«à¦¾à¦°",21,Color.WHITE);

        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        header.addView(
                title,
                new LinearLayout.LayoutParams(0,dp(62),1)
        );

        TextView bankIcon = tv("ðŸ¦",21,Color.WHITE);
        bankIcon.setGravity(Gravity.CENTER);

        header.addView(
                bankIcon,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);

        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(12),dp(12),dp(12),dp(25));

        scroll.addView(content);

        main.addView(
                scroll,
                new LinearLayout.LayoutParams(-1,0,1)
        );

        TextView info =
                tv(
                        "ðŸ¦ à¦¬à§à¦¯à¦¾à¦‚à¦• à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿà§‡ à¦Ÿà¦¾à¦•à¦¾ à¦ªà¦¾à¦ à¦¾à¦¨\n\n"
                                + "à¦¨à¦¿à¦šà§‡à¦° à¦¤à¦¥à§à¦¯à¦—à§à¦²à§‹ à¦¸à¦ à¦¿à¦•à¦­à¦¾à¦¬à§‡ à¦ªà§‚à¦°à¦£ à¦•à¦°à§‡ à¦Ÿà§à¦°à¦¾à¦¨à§à¦¸à¦«à¦¾à¦° à¦°à¦¿à¦•à§‹à§Ÿà§‡à¦¸à§à¦Ÿ à¦ªà¦¾à¦ à¦¾à¦¨à¥¤",
                        16,
                        DARK
                );

        info.setPadding(dp(18),dp(18),dp(18),dp(18));
        info.setBackground(bg(Color.WHITE,18));

        content.addView(
                info,
                new LinearLayout.LayoutParams(-1,dp(125))
        );

        space(content,12);

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16),dp(16),dp(16),dp(20));
        card.setBackground(bg(Color.WHITE,18));

        content.addView(
                card,
                new LinearLayout.LayoutParams(-1,-2)
        );

        TextView bankTitle = tv("à¦¬à§à¦¯à¦¾à¦‚à¦•à§‡à¦° à¦¨à¦¾à¦®",15,DARK);
        bankTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                bankTitle,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        EditText bank = input("à¦¬à§à¦¯à¦¾à¦‚à¦•à§‡à¦° à¦¨à¦¾à¦® à¦²à¦¿à¦–à§à¦¨",false);

        card.addView(
                bank,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(card,12);

        TextView holderTitle =
                tv("à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦¹à§‹à¦²à§à¦¡à¦¾à¦°à§‡à¦° à¦¨à¦¾à¦®",15,DARK);

        holderTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                holderTitle,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        EditText holder =
                input("à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦¹à§‹à¦²à§à¦¡à¦¾à¦°à§‡à¦° à¦¨à¦¾à¦®",false);

        card.addView(
                holder,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(card,12);

        TextView accountTitle =
                tv("à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦¨à¦®à§à¦¬à¦°",15,DARK);

        accountTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                accountTitle,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        EditText account =
                input("à¦¬à§à¦¯à¦¾à¦‚à¦• à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦¨à¦®à§à¦¬à¦°",false);

        account.setInputType(InputType.TYPE_CLASS_NUMBER);

        card.addView(
                account,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(card,12);

        TextView branchTitle =
                tv("à¦¶à¦¾à¦–à¦¾à¦° à¦¨à¦¾à¦®",15,DARK);

        branchTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                branchTitle,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        EditText branch =
                input("à¦¶à¦¾à¦–à¦¾à¦° à¦¨à¦¾à¦® (à¦à¦šà§à¦›à¦¿à¦•)",false);

        card.addView(
                branch,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(card,12);

        TextView amountTitle =
                tv("à¦Ÿà§à¦°à¦¾à¦¨à§à¦¸à¦«à¦¾à¦°à§‡à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£",15,DARK);

        amountTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                amountTitle,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        EditText amount =
                input("à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£",false);

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        card.addView(
                amount,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(card,12);

        TextView referenceTitle =
                tv("à¦°à§‡à¦«à¦¾à¦°à§‡à¦¨à§à¦¸",15,DARK);

        referenceTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                referenceTitle,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        EditText reference =
                input("à¦°à§‡à¦«à¦¾à¦°à§‡à¦¨à§à¦¸ (à¦à¦šà§à¦›à¦¿à¦•)",false);

        card.addView(
                reference,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(card,18);

        TextView transfer =
                button(
                        "à¦¬à§à¦¯à¦¾à¦‚à¦• à¦Ÿà§à¦°à¦¾à¦¨à§à¦¸à¦«à¦¾à¦° à¦•à¦°à§à¦¨  â†’",
                        BLUE,
                        Color.WHITE
                );

        card.addView(
                transfer,
                new LinearLayout.LayoutParams(-1,dp(60))
        );

        transfer.setOnClickListener(v -> {

            String bankName =
                    bank.getText().toString().trim();

            String holderName =
                    holder.getText().toString().trim();

            String accountNumber =
                    account.getText().toString().trim();

            String amountValue =
                    amount.getText().toString().trim();

            String branchName =
                    branch.getText().toString().trim();

            String referenceValue =
                    reference.getText().toString().trim();

            if (bankName.isEmpty()) {
                bank.setError("à¦¬à§à¦¯à¦¾à¦‚à¦•à§‡à¦° à¦¨à¦¾à¦® à¦¦à¦¿à¦¨");
                return;
            }

            if (holderName.isEmpty()) {
                holder.setError("à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦¹à§‹à¦²à§à¦¡à¦¾à¦°à§‡à¦° à¦¨à¦¾à¦® à¦¦à¦¿à¦¨");
                return;
            }

            if (accountNumber.isEmpty()) {
                account.setError("à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦¨à¦®à§à¦¬à¦° à¦¦à¦¿à¦¨");
                return;
            }

            if (accountNumber.length() < 8) {
                account.setError("à¦¸à¦ à¦¿à¦• à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦¨à¦®à§à¦¬à¦° à¦¦à¦¿à¦¨");
                return;
            }

            if (amountValue.isEmpty()) {
                amount.setError("à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¦à¦¿à¦¨");
                return;
            }

            try {

                double value =
                        Double.parseDouble(amountValue);

                if (value < 500) {
                    amount.setError("à¦¸à¦°à§à¦¬à¦¨à¦¿à¦®à§à¦¨ à§«à§¦à§¦ à¦Ÿà¦¾à¦•à¦¾");
                    return;
                }

                String message =
                        "à¦¬à§à¦¯à¦¾à¦‚à¦•: "
                                + bankName
                                + "\n\nà¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦¹à§‹à¦²à§à¦¡à¦¾à¦°: "
                                + holderName
                                + "\n\nà¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦¨à¦®à§à¦¬à¦°: "
                                + accountNumber
                                + "\n\nà¦¶à¦¾à¦–à¦¾: "
                                + (
                                branchName.isEmpty()
                                        ? "à¦¦à§‡à¦“à§Ÿà¦¾ à¦¹à§Ÿà¦¨à¦¿"
                                        : branchName
                        )
                                + "\n\nà¦ªà¦°à¦¿à¦®à¦¾à¦£: à§³ "
                                + amountValue
                                + "\n\n"
                                + (
                                referenceValue.isEmpty()
                                        ? ""
                                        : "à¦°à§‡à¦«à¦¾à¦°à§‡à¦¨à§à¦¸: "
                                        + referenceValue
                                        + "\n\n"
                        )
                                + "à¦Ÿà§à¦°à¦¾à¦¨à§à¦¸à¦«à¦¾à¦° à¦¤à¦¥à§à¦¯à¦—à§à¦²à§‹ à¦¸à¦ à¦¿à¦• à¦•à¦¿à¦¨à¦¾ à¦¯à¦¾à¦šà¦¾à¦‡ à¦•à¦°à§à¦¨à¥¤";

                final String finalBankName = bankName;
                final String finalAccountNumber = accountNumber;
                final String finalAmountValue = amountValue;

                new AlertDialog.Builder(this)
                        .setTitle("à¦¬à§à¦¯à¦¾à¦‚à¦• à¦Ÿà§à¦°à¦¾à¦¨à§à¦¸à¦«à¦¾à¦° à¦¨à¦¿à¦¶à§à¦šà¦¿à¦¤ à¦•à¦°à§à¦¨")
                        .setMessage(message)
                        .setNegativeButton("à¦¬à¦¾à¦¤à¦¿à¦²",null)
                        .setPositiveButton(
                                "à¦¨à¦¿à¦¶à§à¦šà¦¿à¦¤",
                                (dialog,which) -> {

                                    if (transactionBlockedFor(Double.parseDouble(finalAmountValue))) return;
                                    Map<String,Object> req = new HashMap<>(); req.put("bank", finalBankName); req.put("accountNumber", finalAccountNumber); req.put("amount", Double.parseDouble(finalAmountValue)); req.put("holderName", holder.getText().toString().trim()); req.put("branch", branch.getText().toString().trim()); req.put("reference", reference.getText().toString().trim()); submitFirestoreRequest("bankTransferRequests", req); recordTransaction("à¦¬à§à¦¯à¦¾à¦‚à¦• à¦Ÿà§à¦°à¦¾à¦¨à§à¦¸à¦«à¦¾à¦°",finalBankName+" / "+finalAccountNumber,Double.parseDouble(finalAmountValue),false,"PENDING");
                                    Toast.makeText(
                                            this,
                                            "à¦¬à§à¦¯à¦¾à¦‚à¦• à¦Ÿà§à¦°à¦¾à¦¨à§à¦¸à¦«à¦¾à¦° à¦°à¦¿à¦•à§‹à§Ÿà§‡à¦¸à§à¦Ÿ à¦—à§à¦°à¦¹à¦£ à¦•à¦°à¦¾ à¦¹à§Ÿà§‡à¦›à§‡à¥¤",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    bank.setText("");
                                    holder.setText("");
                                    account.setText("");
                                    branch.setText("");
                                    amount.setText("");
                                    reference.setText("");
                                }
                        )
                        .show();

            } catch (Exception e) {

                amount.setError("à¦¸à¦ à¦¿à¦• à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¦à¦¿à¦¨");
            }
        });
    }

    /* =========================================================
       MOBILE BANKING
       ========================================================= */

    private void showMobileBanking() {
        showMobileBanking(selectedMobileProvider);
    }

    private void showMobileBanking(String provider) {

        selectedMobileProvider = provider;

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout main = new LinearLayout(this);

        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.rgb(248,249,251));

        setContentView(main);

        LinearLayout header = new LinearLayout(this);

        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(8),0,dp(8),0);
        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        TextView back = tv("â€¹",38,Color.WHITE);
        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        back.setOnClickListener(v -> showHome());

        TextView title = tv("à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦¬à§à¦¯à¦¾à¦‚à¦•à¦¿à¦‚",21,Color.WHITE);

        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        header.addView(
                title,
                new LinearLayout.LayoutParams(0,dp(62),1)
        );

        TextView bell = tv("ðŸ””",19,Color.WHITE);
        bell.setGravity(Gravity.CENTER);

        header.addView(
                bell,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);

        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(12),dp(12),dp(12),dp(20));

        scroll.addView(content);

        main.addView(
                scroll,
                new LinearLayout.LayoutParams(-1,0,1)
        );

        TextView providerTitle =
                tv(
                        "à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦¬à§à¦¯à¦¾à¦‚à¦•à¦¿à¦‚ à¦¨à¦¿à¦°à§à¦¬à¦¾à¦šà¦¨ à¦•à¦°à§à¦¨",
                        16,
                        DARK
                );

        providerTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        providerTitle.setPadding(dp(4),dp(2),dp(4),dp(7));

        content.addView(
                providerTitle,
                new LinearLayout.LayoutParams(-1,dp(34))
        );

        LinearLayout providers = new LinearLayout(this);
        providers.setGravity(Gravity.CENTER);

        content.addView(
                providers,
                new LinearLayout.LayoutParams(-1,dp(78))
        );

        addMobileProviderTab(providers,"à¦¬à¦¿à¦•à¦¾à¦¶","ðŸ’—","à¦¬à¦¿à¦•à¦¾à¦¶");
        addMobileProviderTab(providers,"à¦¨à¦—à¦¦","ðŸŸ ","à¦¨à¦—à¦¦");
        addMobileProviderTab(providers,"à¦°à¦•à§‡à¦Ÿ","ðŸŸ£","à¦°à¦•à§‡à¦Ÿ");
        addMobileProviderTab(providers,"à¦‰à¦ªà¦¾à§Ÿ","ðŸŸ¡","à¦‰à¦ªà¦¾à§Ÿ");

        space(content,10);

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16),dp(15),dp(16),dp(18));
        card.setBackground(bg(Color.WHITE,18));

        content.addView(
                card,
                new LinearLayout.LayoutParams(-1,-2)
        );

        TextView selected =
                tv(
                        "âœ“ " + selectedMobileProvider,
                        20,
                        BLUE
                );

        selected.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        selected.setGravity(Gravity.CENTER_VERTICAL);

        card.addView(
                selected,
                new LinearLayout.LayoutParams(-1,dp(42))
        );

        TextView accountTitle =
                tv("à¦à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦§à¦°à¦¨",15,DARK);

        accountTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                accountTitle,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        LinearLayout accountRow = new LinearLayout(this);
        accountRow.setGravity(Gravity.CENTER);

        card.addView(
                accountRow,
                new LinearLayout.LayoutParams(-1,dp(52))
        );

        TextView personal =
                mobileChoiceButton(
                        "à¦ªà¦¾à¦°à§à¦¸à§‹à¦¨à¦¾à¦²",
                        selectedAccountType.equals("à¦ªà¦¾à¦°à§à¦¸à§‹à¦¨à¦¾à¦²")
                );

        TextView agent =
                mobileChoiceButton(
                        "à¦à¦œà§‡à¦¨à§à¦Ÿ",
                        selectedAccountType.equals("à¦à¦œà§‡à¦¨à§à¦Ÿ")
                );

        accountRow.addView(
                personal,
                new LinearLayout.LayoutParams(0,dp(48),1)
        );

        LinearLayout.LayoutParams agentParams =
                new LinearLayout.LayoutParams(0,dp(48),1);

        agentParams.leftMargin = dp(8);

        accountRow.addView(agent,agentParams);

        personal.setOnClickListener(v -> {

            selectedAccountType = "à¦ªà¦¾à¦°à§à¦¸à§‹à¦¨à¦¾à¦²";
            showMobileBanking(selectedMobileProvider);
        });

        agent.setOnClickListener(v -> {

            selectedAccountType = "à¦à¦œà§‡à¦¨à§à¦Ÿ";
            showMobileBanking(selectedMobileProvider);
        });

        space(card,12);

        TextView numberTitle =
                tv("à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦¨à¦®à§à¦¬à¦°",15,DARK);

        numberTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                numberTitle,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        LinearLayout phoneBox = new LinearLayout(this);

        phoneBox.setGravity(Gravity.CENTER_VERTICAL);

        phoneBox.setBackground(
                outline(
                        Color.WHITE,
                        Color.rgb(220,225,232),
                        12
                )
        );

        card.addView(
                phoneBox,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        TextView country = tv("+88",17,BLUE);

        country.setGravity(Gravity.CENTER);
        country.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        phoneBox.addView(
                country,
                new LinearLayout.LayoutParams(dp(55),dp(58))
        );

        EditText number = new EditText(this);

        number.setHint("à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦¨à¦®à§à¦¬à¦° à¦²à¦¿à¦–à§à¦¨");
        number.setTextSize(17);
        number.setSingleLine(true);
        number.setTextColor(DARK);
        number.setHintTextColor(Color.GRAY);
        number.setInputType(InputType.TYPE_CLASS_PHONE);
        number.setPadding(dp(8),0,dp(12),0);
        number.setBackgroundColor(Color.TRANSPARENT);

        phoneBox.addView(
                number,
                new LinearLayout.LayoutParams(0,dp(58),1)
        );

        space(card,12);

        TextView amountTitle =
                tv("à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦²à¦¿à¦–à§à¦¨",15,DARK);

        amountTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                amountTitle,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        EditText amount = new EditText(this);

        amount.setHint("à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦²à¦¿à¦–à§à¦¨");
        amount.setTextSize(17);
        amount.setSingleLine(true);
        amount.setTextColor(DARK);
        amount.setHintTextColor(Color.GRAY);

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        amount.setPadding(dp(18),0,dp(18),0);

        amount.setBackground(
                outline(
                        Color.WHITE,
                        Color.rgb(220,225,232),
                        12
                )
        );

        card.addView(
                amount,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(card,10);

        TextView quickTitle =
                tv(
                        "à¦¦à§à¦°à§à¦¤ à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¨à¦¿à¦°à§à¦¬à¦¾à¦šà¦¨ à¦•à¦°à§à¦¨",
                        14,
                        Color.DKGRAY
                );

        card.addView(
                quickTitle,
                new LinearLayout.LayoutParams(-1,dp(28))
        );

        LinearLayout quickRow = new LinearLayout(this);
        quickRow.setGravity(Gravity.CENTER);

        card.addView(
                quickRow,
                new LinearLayout.LayoutParams(-1,dp(48))
        );

        addQuickAmount(quickRow,amount,"à§³ 1,000");
        addQuickAmount(quickRow,amount,"à§³ 10,000");
        addQuickAmount(quickRow,amount,"à§³ 20,000");
        addQuickAmount(quickRow,amount,"à§³ 50,000");

        space(card,16);

        TextView send =
                button(
                        "à¦Ÿà¦¾à¦•à¦¾ à¦ªà¦¾à¦ à¦¾à¦¨  â†’",
                        BLUE,
                        Color.WHITE
                );

        card.addView(
                send,
                new LinearLayout.LayoutParams(-1,dp(60))
        );

        send.setOnClickListener(v -> {

            String num =
                    number.getText()
                            .toString()
                            .trim()
                            .replace(" ","")
                            .replace("-","");

            String money =
                    amount.getText()
                            .toString()
                            .trim();

            if (num.isEmpty()) {
                number.setError("à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦¨à¦®à§à¦¬à¦° à¦¦à¦¿à¦¨");
                return;
            }

            if (num.startsWith("+88")) {
                num = num.substring(3);
            }

            if (!num.matches("01[0-9]{9}")) {
                number.setError(
                        "à¦¸à¦ à¦¿à¦• à§§à§§ à¦¡à¦¿à¦œà¦¿à¦Ÿà§‡à¦° à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦¨à¦®à§à¦¬à¦° à¦¦à¦¿à¦¨"
                );
                return;
            }

            if (money.isEmpty()) {
                amount.setError("à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¦à¦¿à¦¨");
                return;
            }

            try {

                double value =
                        Double.parseDouble(money);

                if (value < 500) {
                    amount.setError("à¦¸à¦°à§à¦¬à¦¨à¦¿à¦®à§à¦¨ à§«à§¦à§¦ à¦Ÿà¦¾à¦•à¦¾");
                    return;
                }

                final String finalSendNumber = num;
                final String finalSendMoney = money;

                new AlertDialog.Builder(this)
                        .setTitle("à¦Ÿà¦¾à¦•à¦¾ à¦ªà¦¾à¦ à¦¾à¦¨à§‹ à¦¨à¦¿à¦¶à§à¦šà¦¿à¦¤ à¦•à¦°à§à¦¨")
                        .setMessage(
                                "à¦®à¦¾à¦§à§à¦¯à¦®: "
                                        + selectedMobileProvider
                                        + "\nà¦à¦•à¦¾à¦‰à¦¨à§à¦Ÿ: "
                                        + selectedAccountType
                                        + "\nà¦¨à¦®à§à¦¬à¦°: +88 "
                                        + num
                                        + "\nà¦ªà¦°à¦¿à¦®à¦¾à¦£: à§³ "
                                        + money
                                        + "\n\n"
                                        + "à¦à¦Ÿà¦¿ à¦à¦•à¦Ÿà¦¿ à¦¡à§‡à¦®à§‹ à¦°à¦¿à¦•à§‹à§Ÿà§‡à¦¸à§à¦Ÿà¥¤ à¦†à¦¸à¦² à¦Ÿà¦¾à¦•à¦¾ à¦ªà¦¾à¦ à¦¾à¦¨à§‹à¦° à¦œà¦¨à§à¦¯ Provider API à¦¸à¦‚à¦¯à§à¦•à§à¦¤ à¦•à¦°à¦¤à§‡ à¦¹à¦¬à§‡à¥¤"
                        )
                        .setNegativeButton("à¦¬à¦¾à¦¤à¦¿à¦²",null)
                        .setPositiveButton(
                                "à¦¨à¦¿à¦¶à§à¦šà¦¿à¦¤",
                                (dialog,which) -> {

                                    if (transactionBlockedFor(Double.parseDouble(finalSendMoney))) return;
                                    Map<String,Object> req = new HashMap<>(); req.put("method", selectedMobileProvider); req.put("accountType", selectedAccountType); req.put("destinationNumber", finalSendNumber); req.put("amount", Double.parseDouble(finalSendMoney)); submitFirestoreRequest("mobileBankingRequests", req); recordTransaction("à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦¬à§à¦¯à¦¾à¦‚à¦•à¦¿à¦‚",selectedMobileProvider+" / "+finalSendNumber,Double.parseDouble(finalSendMoney),false,"PENDING");
                                    Toast.makeText(
                                            this,
                                            selectedMobileProvider
                                                    + " à¦Ÿà¦¾à¦•à¦¾ à¦ªà¦¾à¦ à¦¾à¦¨à§‹à¦° à¦°à¦¿à¦•à§‹à§Ÿà§‡à¦¸à§à¦Ÿ à¦—à§à¦°à¦¹à¦£ à¦•à¦°à¦¾ à¦¹à§Ÿà§‡à¦›à§‡à¥¤",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    number.setText("");
                                    amount.setText("");
                                }
                        )
                        .show();

            } catch(Exception e) {

                amount.setError("à¦¸à¦ à¦¿à¦• à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¦à¦¿à¦¨");
            }
        });
    }

    /* =========================================================
       MOBILE PROVIDER TAB
       ========================================================= */

    private void addMobileProviderTab(
            LinearLayout parent,
            String name,
            String icon,
            String provider) {

        boolean selected =
                selectedMobileProvider.equals(provider);

        LinearLayout box = new LinearLayout(this);

        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);

        if (selected) {

            box.setBackground(
                    outline(
                            Color.rgb(238,246,255),
                            BLUE,
                            13
                    )
            );

        } else {

            box.setBackground(
                    outline(
                            Color.WHITE,
                            Color.rgb(220,225,232),
                            13
                    )
            );
        }

        TextView i = tv(icon,22,DARK);
        i.setGravity(Gravity.CENTER);

        box.addView(
                i,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        TextView n =
                tv(
                        name,
                        13,
                        selected ? BLUE : DARK
                );

        n.setGravity(Gravity.CENTER);
        n.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        box.addView(
                n,
                new LinearLayout.LayoutParams(-1,dp(27))
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(0,dp(72),1);

        p.setMargins(dp(3),0,dp(3),0);

        parent.addView(box,p);

        box.setOnClickListener(v -> {

            selectedMobileProvider = provider;
            showMobileBanking(provider);
        });
    }

    /* =========================================================
       ACCOUNT TYPE
       ========================================================= */

    private TextView mobileChoiceButton(
            String text,
            boolean selected) {

        TextView b =
                tv(
                        text,
                        15,
                        selected ? BLUE : Color.DKGRAY
                );

        b.setGravity(Gravity.CENTER);
        b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        if (selected) {

            b.setBackground(
                    outline(
                            Color.rgb(238,246,255),
                            BLUE,
                            12
                    )
            );

        } else {

            b.setBackground(
                    outline(
                            Color.WHITE,
                            Color.rgb(215,220,228),
                            12
                    )
            );
        }

        return b;
    }

    /* =========================================================
       QUICK AMOUNT
       ========================================================= */

    private void addQuickAmount(
            LinearLayout parent,
            EditText amount,
            String value) {

        TextView b = tv(value,12,BLUE);

        b.setGravity(Gravity.CENTER);
        b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        b.setBackground(
                outline(
                        Color.WHITE,
                        BLUE,
                        9
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(0,dp(42),1);

        p.setMargins(dp(2),0,dp(2),0);

        parent.addView(b,p);

        b.setOnClickListener(v -> {

            String clean =
                    value
                            .replace("à§³","")
                            .replace(",","")
                            .trim();

            amount.setText(clean);
            amount.setSelection(amount.length());
        });
    }

    /* =========================================================
       MOBILE RECHARGE
       ========================================================= */

    private void showMobileRecharge() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout main = new LinearLayout(this);

        main.setOrientation(LinearLayout.VERTICAL);
        main.setBackgroundColor(Color.rgb(248,249,251));

        setContentView(main);

        LinearLayout header = new LinearLayout(this);

        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(8),0,dp(8),0);
        header.setBackgroundColor(BLUE);

        main.addView(
                header,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        TextView back = tv("â€¹",38,Color.WHITE);
        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        back.setOnClickListener(v -> showHome());

        TextView title =
                tv(
                        "à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦°à¦¿à¦šà¦¾à¦°à§à¦œ",
                        21,
                        Color.WHITE
                );

        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        header.addView(
                title,
                new LinearLayout.LayoutParams(0,dp(62),1)
        );

        TextView bell = tv("ðŸ””",19,Color.WHITE);
        bell.setGravity(Gravity.CENTER);

        header.addView(
                bell,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        ScrollView scroll = new ScrollView(this);

        LinearLayout content = new LinearLayout(this);

        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(12),dp(12),dp(12),dp(20));

        scroll.addView(content);

        main.addView(
                scroll,
                new LinearLayout.LayoutParams(-1,0,1)
        );

        TextView operatorTitle =
                tv(
                        "à¦…à¦ªà¦¾à¦°à§‡à¦Ÿà¦° à¦¨à¦¿à¦°à§à¦¬à¦¾à¦šà¦¨ à¦•à¦°à§à¦¨",
                        16,
                        DARK
                );

        operatorTitle.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        content.addView(
                operatorTitle,
                new LinearLayout.LayoutParams(-1,dp(36))
        );

        LinearLayout opRow1 = new LinearLayout(this);
        opRow1.setGravity(Gravity.CENTER);

        content.addView(
                opRow1,
                new LinearLayout.LayoutParams(-1,dp(72))
        );

        addRechargeOperator(opRow1,"GP","ðŸŸ¢","GP");
        addRechargeOperator(opRow1,"Robi","ðŸ”´","Robi");
        addRechargeOperator(opRow1,"Airtel","ðŸ”µ","Airtel");

        LinearLayout opRow2 = new LinearLayout(this);
        opRow2.setGravity(Gravity.CENTER);

        content.addView(
                opRow2,
                new LinearLayout.LayoutParams(-1,dp(72))
        );

        addRechargeOperator(
                opRow2,
                "Banglalink",
                "ðŸŸ ",
                "Banglalink"
        );

        addRechargeOperator(
                opRow2,
                "Teletalk",
                "ðŸŸ¢",
                "Teletalk"
        );

        space(content,10);

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16),dp(16),dp(16),dp(18));
        card.setBackground(bg(Color.WHITE,18));

        content.addView(
                card,
                new LinearLayout.LayoutParams(-1,-2)
        );

        TextView selected =
                tv(
                        "âœ“ " + selectedRechargeOperator,
                        20,
                        BLUE
                );

        selected.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        selected.setGravity(Gravity.CENTER_VERTICAL);

        card.addView(
                selected,
                new LinearLayout.LayoutParams(-1,dp(42))
        );

        TextView numberTitle =
                tv("à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦¨à¦®à§à¦¬à¦°",15,DARK);

        numberTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                numberTitle,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        EditText number = new EditText(this);

        number.setHint("à¦¯à§‡ à¦¨à¦®à§à¦¬à¦°à§‡ à¦°à¦¿à¦šà¦¾à¦°à§à¦œ à¦•à¦°à¦¬à§‡à¦¨");
        number.setTextSize(17);
        number.setSingleLine(true);
        number.setTextColor(DARK);
        number.setHintTextColor(Color.GRAY);
        number.setInputType(InputType.TYPE_CLASS_PHONE);
        number.setPadding(dp(18),0,dp(18),0);

        number.setBackground(
                outline(
                        Color.WHITE,
                        Color.rgb(220,225,232),
                        12
                )
        );

        card.addView(
                number,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(card,12);

        TextView typeTitle =
                tv("à¦°à¦¿à¦šà¦¾à¦°à§à¦œà§‡à¦° à¦§à¦°à¦¨",15,DARK);

        typeTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                typeTitle,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        LinearLayout typeRow = new LinearLayout(this);
        typeRow.setGravity(Gravity.CENTER);

        card.addView(
                typeRow,
                new LinearLayout.LayoutParams(-1,dp(52))
        );

        TextView prepaid =
                rechargeTypeButton("à¦ªà§à¦°à¦¿à¦ªà§‡à¦‡à¦¡",true);

        TextView postpaid =
                rechargeTypeButton("à¦ªà§‹à¦¸à§à¦Ÿà¦ªà§‡à¦‡à¦¡",false);

        typeRow.addView(
                prepaid,
                new LinearLayout.LayoutParams(0,dp(48),1)
        );

        LinearLayout.LayoutParams postParams =
                new LinearLayout.LayoutParams(0,dp(48),1);

        postParams.leftMargin = dp(8);

        typeRow.addView(postpaid,postParams);

        final boolean[] isPrepaid =
                new boolean[]{true};

        prepaid.setOnClickListener(v -> {

            isPrepaid[0] = true;

            prepaid.setBackground(
                    outline(
                            Color.rgb(238,246,255),
                            BLUE,
                            12
                    )
            );

            prepaid.setTextColor(BLUE);

            postpaid.setBackground(
                    outline(
                            Color.WHITE,
                            Color.rgb(215,220,228),
                            12
                    )
            );

            postpaid.setTextColor(Color.DKGRAY);
        });

        postpaid.setOnClickListener(v -> {

            isPrepaid[0] = false;

            postpaid.setBackground(
                    outline(
                            Color.rgb(238,246,255),
                            BLUE,
                            12
                    )
            );

            postpaid.setTextColor(BLUE);

            prepaid.setBackground(
                    outline(
                            Color.WHITE,
                            Color.rgb(215,220,228),
                            12
                    )
            );

            prepaid.setTextColor(Color.DKGRAY);
        });

        space(card,12);

        TextView amountTitle =
                tv("à¦°à¦¿à¦šà¦¾à¦°à§à¦œà§‡à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£",15,DARK);

        amountTitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                amountTitle,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        EditText amount = new EditText(this);

        amount.setHint("à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦²à¦¿à¦–à§à¦¨");
        amount.setTextSize(17);
        amount.setSingleLine(true);
        amount.setTextColor(DARK);
        amount.setHintTextColor(Color.GRAY);

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        amount.setPadding(dp(18),0,dp(18),0);

        amount.setBackground(
                outline(
                        Color.WHITE,
                        Color.rgb(220,225,232),
                        12
                )
        );

        card.addView(
                amount,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(card,10);

        TextView quick =
                tv(
                        "à¦¦à§à¦°à§à¦¤ à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¨à¦¿à¦°à§à¦¬à¦¾à¦šà¦¨ à¦•à¦°à§à¦¨",
                        14,
                        Color.DKGRAY
                );

        card.addView(
                quick,
                new LinearLayout.LayoutParams(-1,dp(28))
        );

        LinearLayout quickRow = new LinearLayout(this);
        quickRow.setGravity(Gravity.CENTER);

        card.addView(
                quickRow,
                new LinearLayout.LayoutParams(-1,dp(48))
        );

        addQuickRechargeAmount(quickRow,amount,"à§³ à§«à§¦");
        addQuickRechargeAmount(quickRow,amount,"à§³ à§§à§¦à§¦");
        addQuickRechargeAmount(quickRow,amount,"à§³ à§¨à§¦à§¦");
        addQuickRechargeAmount(quickRow,amount,"à§³ à§«à§¦à§¦");

        space(card,18);

        TextView recharge =
                button(
                        "à¦°à¦¿à¦šà¦¾à¦°à§à¦œ à¦•à¦°à§à¦¨  â†’",
                        BLUE,
                        Color.WHITE
                );

        card.addView(
                recharge,
                new LinearLayout.LayoutParams(-1,dp(60))
        );

        recharge.setOnClickListener(v -> {

            String num =
                    number.getText()
                            .toString()
                            .trim()
                            .replace(" ","")
                            .replace("-","");

            String money =
                    amount.getText()
                            .toString()
                            .trim();

            if (num.isEmpty()) {
                number.setError("à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦¨à¦®à§à¦¬à¦° à¦¦à¦¿à¦¨");
                return;
            }

            if (num.startsWith("+88")) {
                num = num.substring(3);
            }

            if (!num.matches("01[0-9]{9}")) {

                number.setError(
                        "à¦¸à¦ à¦¿à¦• à§§à§§ à¦¡à¦¿à¦œà¦¿à¦Ÿà§‡à¦° à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦¨à¦®à§à¦¬à¦° à¦¦à¦¿à¦¨"
                );

                return;
            }

            if (money.isEmpty()) {

                amount.setError(
                        "à¦°à¦¿à¦šà¦¾à¦°à§à¦œà§‡à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¦à¦¿à¦¨"
                );

                return;
            }

            try {

                double value =
                        Double.parseDouble(money);

                if (value < 20) {

                    amount.setError(
                            "à¦¸à¦°à§à¦¬à¦¨à¦¿à¦®à§à¦¨ à§¨à§¦ à¦Ÿà¦¾à¦•à¦¾"
                    );

                    return;
                }

                String rechargeType =
                        isPrepaid[0]
                                ? "à¦ªà§à¦°à¦¿à¦ªà§‡à¦‡à¦¡"
                                : "à¦ªà§‹à¦¸à§à¦Ÿà¦ªà§‡à¦‡à¦¡";

                final String finalRechargeNumber = num;
                final String finalRechargeMoney = money;

                new AlertDialog.Builder(this)
                        .setTitle("à¦°à¦¿à¦šà¦¾à¦°à§à¦œ à¦¨à¦¿à¦¶à§à¦šà¦¿à¦¤ à¦•à¦°à§à¦¨")
                        .setMessage(
                                "à¦…à¦ªà¦¾à¦°à§‡à¦Ÿà¦°: "
                                        + selectedRechargeOperator
                                        + "\nà¦§à¦°à¦¨: "
                                        + rechargeType
                                        + "\nà¦¨à¦®à§à¦¬à¦°: "
                                        + num
                                        + "\nà¦ªà¦°à¦¿à¦®à¦¾à¦£: à§³ "
                                        + money
                                        + "\n\n"
                                        + "à¦à¦Ÿà¦¿ à¦à¦•à¦Ÿà¦¿ à¦¡à§‡à¦®à§‹ à¦°à¦¿à¦šà¦¾à¦°à§à¦œ à¦°à¦¿à¦•à§‹à§Ÿà§‡à¦¸à§à¦Ÿà¥¤ à¦†à¦¸à¦² à¦°à¦¿à¦šà¦¾à¦°à§à¦œà§‡à¦° à¦œà¦¨à§à¦¯ Recharge API à¦¸à¦‚à¦¯à§à¦•à§à¦¤ à¦•à¦°à¦¤à§‡ à¦¹à¦¬à§‡à¥¤"
                        )
                        .setNegativeButton("à¦¬à¦¾à¦¤à¦¿à¦²",null)
                        .setPositiveButton(
                                "à¦¨à¦¿à¦¶à§à¦šà¦¿à¦¤",
                                (dialog,which) -> {

                                    if (transactionBlockedFor(Double.parseDouble(finalRechargeMoney))) return;
                                    Map<String,Object> req = new HashMap<>(); req.put("operator", selectedRechargeOperator); req.put("destinationNumber", finalRechargeNumber); req.put("amount", Double.parseDouble(finalRechargeMoney)); req.put("rechargeType", rechargeType); submitFirestoreRequest("rechargeRequests", req); recordTransaction("à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦°à¦¿à¦šà¦¾à¦°à§à¦œ",selectedRechargeOperator+" / "+finalRechargeNumber,Double.parseDouble(finalRechargeMoney),false,"PENDING");
                                    Toast.makeText(
                                            this,
                                            selectedRechargeOperator
                                                    + " à¦°à¦¿à¦šà¦¾à¦°à§à¦œ à¦°à¦¿à¦•à§‹à§Ÿà§‡à¦¸à§à¦Ÿ à¦—à§à¦°à¦¹à¦£ à¦•à¦°à¦¾ à¦¹à§Ÿà§‡à¦›à§‡à¥¤",
                                            Toast.LENGTH_LONG
                                    ).show();

                                    number.setText("");
                                    amount.setText("");
                                }
                        )
                        .show();

            } catch(Exception e) {

                amount.setError(
                        "à¦¸à¦ à¦¿à¦• à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¦à¦¿à¦¨"
                );
            }
        });
    }

    /* =========================================================
       RECHARGE OPERATOR
       ========================================================= */

    private void addRechargeOperator(
            LinearLayout parent,
            String name,
            String icon,
            String operator) {

        boolean selected =
                selectedRechargeOperator.equals(operator);

        LinearLayout box = new LinearLayout(this);

        box.setOrientation(LinearLayout.VERTICAL);
        box.setGravity(Gravity.CENTER);

        if (selected) {

            box.setBackground(
                    outline(
                            Color.rgb(238,246,255),
                            BLUE,
                            13
                    )
            );

        } else {

            box.setBackground(
                    outline(
                            Color.WHITE,
                            Color.rgb(220,225,232),
                            13
                    )
            );
        }

        TextView i = tv(icon,22,DARK);
        i.setGravity(Gravity.CENTER);

        box.addView(
                i,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        TextView n =
                tv(
                        name,
                        13,
                        selected ? BLUE : DARK
                );

        n.setGravity(Gravity.CENTER);
        n.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        box.addView(
                n,
                new LinearLayout.LayoutParams(-1,dp(27))
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(0,dp(68),1);

        p.setMargins(dp(3),0,dp(3),0);

        parent.addView(box,p);

        box.setOnClickListener(v -> {

            selectedRechargeOperator = operator;
            showMobileRecharge();
        });
    }

    /* =========================================================
       RECHARGE TYPE
       ========================================================= */

    private TextView rechargeTypeButton(
            String text,
            boolean selected) {

        TextView b =
                tv(
                        text,
                        15,
                        selected ? BLUE : Color.DKGRAY
                );

        b.setGravity(Gravity.CENTER);
        b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        if (selected) {

            b.setBackground(
                    outline(
                            Color.rgb(238,246,255),
                            BLUE,
                            12
                    )
            );

        } else {

            b.setBackground(
                    outline(
                            Color.WHITE,
                            Color.rgb(215,220,228),
                            12
                    )
            );
        }

        return b;
    }

    /* =========================================================
       QUICK RECHARGE AMOUNT
       ========================================================= */

    private void addQuickRechargeAmount(
            LinearLayout parent,
            EditText amount,
            String value) {

        TextView b = tv(value,12,BLUE);

        b.setGravity(Gravity.CENTER);
        b.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        b.setBackground(
                outline(
                        Color.WHITE,
                        BLUE,
                        9
                )
        );

        LinearLayout.LayoutParams p =
                new LinearLayout.LayoutParams(0,dp(42),1);

        p.setMargins(dp(2),0,dp(2),0);

        parent.addView(b,p);

        b.setOnClickListener(v -> {

            String clean =
                    value
                            .replace("à§³","")
                            .replace(",","")
                            .replace(" ","")
                            .replace("à§¦","0")
                            .replace("à§§","1")
                            .replace("à§¨","2")
                            .replace("à§©","3")
                            .replace("à§ª","4")
                            .replace("à§«","5")
                            .replace("à§¬","6")
                            .replace("à§­","7")
                            .replace("à§®","8")
                            .replace("à§¯","9");

            amount.setText(clean);
            amount.setSelection(amount.length());
        });
    }

    /* =========================================================
       OLD MONEY FORM
       ========================================================= */

    private void showMoneyForm(String type) {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(Color.WHITE);

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(248,249,251));

        setContentView(root);

        LinearLayout header = new LinearLayout(this);

        header.setGravity(Gravity.CENTER_VERTICAL);
        header.setPadding(dp(8),0,dp(8),0);
        header.setBackgroundColor(BLUE);

        root.addView(
                header,
                new LinearLayout.LayoutParams(-1,dp(62))
        );

        TextView back = tv("â€¹",38,Color.WHITE);
        back.setGravity(Gravity.CENTER);

        header.addView(
                back,
                new LinearLayout.LayoutParams(dp(52),dp(62))
        );

        back.setOnClickListener(v -> showMobileBanking());

        TextView title = tv(type,21,Color.WHITE);

        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        header.addView(
                title,
                new LinearLayout.LayoutParams(0,dp(62),1)
        );

        LinearLayout card = new LinearLayout(this);

        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(18),dp(20),dp(18),dp(20));
        card.setBackground(bg(Color.WHITE,18));

        LinearLayout.LayoutParams cardp =
                new LinearLayout.LayoutParams(-1,-2);

        cardp.setMargins(dp(12),dp(18),dp(12),0);

        root.addView(card,cardp);

        TextView info =
                tv(
                        "à¦¬à¦¿à¦•à¦¾à¦¶ / à¦¨à¦—à¦¦ / à¦°à¦•à§‡à¦Ÿ / à¦‰à¦ªà¦¾à§Ÿ",
                        16,
                        BLUE
                );

        info.setTypeface(Typeface.DEFAULT,Typeface.BOLD);

        card.addView(
                info,
                new LinearLayout.LayoutParams(-1,dp(34))
        );

        TextView min =
                tv(
                        "à¦¸à¦°à§à¦¬à¦¨à¦¿à¦®à§à¦¨ à¦²à§‡à¦¨à¦¦à§‡à¦¨: à§³ à§«à§¦à§¦",
                        15,
                        Color.DKGRAY
                );

        card.addView(
                min,
                new LinearLayout.LayoutParams(-1,dp(30))
        );

        space(card,10);

        EditText number =
                input(
                        type.equals("à¦•à§à¦¯à¦¾à¦¶ à¦†à¦‰à¦Ÿ")
                                ? "à¦†à¦ªà¦¨à¦¾à¦° à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦¨à¦®à§à¦¬à¦°"
                                : "à¦¯à§‡ à¦¨à¦®à§à¦¬à¦°à§‡ à¦ªà¦¾à¦ à¦¾à¦¬à§‡à¦¨",
                        false
                );

        card.addView(
                number,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(card,12);

        EditText amount =
                input(
                        "à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ (à¦¸à¦°à§à¦¬à¦¨à¦¿à¦®à§à¦¨ à§³à§«à§¦à§¦)",
                        false
                );

        amount.setInputType(
                InputType.TYPE_CLASS_NUMBER
                        | InputType.TYPE_NUMBER_FLAG_DECIMAL
        );

        card.addView(
                amount,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(card,12);

        EditText reference =
                input(
                        "à¦°à§‡à¦«à¦¾à¦°à§‡à¦¨à§à¦¸ (à¦à¦šà§à¦›à¦¿à¦•)",
                        false
                );

        card.addView(
                reference,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        space(card,16);

        TextView confirm =
                button(
                        type + "  â†’",
                        BLUE,
                        Color.WHITE
                );

        card.addView(
                confirm,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        confirm.setOnClickListener(v -> {

            String num =
                    number.getText().toString().trim();

            String raw =
                    amount.getText().toString().trim();

            if (num.isEmpty()) {
                number.setError("à¦®à§‹à¦¬à¦¾à¦‡à¦² à¦¨à¦®à§à¦¬à¦° à¦¦à¦¿à¦¨");
                return;
            }

            if (raw.isEmpty()) {
                amount.setError("à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¦à¦¿à¦¨");
                return;
            }

            try {

                double value =
                        Double.parseDouble(raw);

                if (value < 500) {
                    amount.setError("à¦¸à¦°à§à¦¬à¦¨à¦¿à¦®à§à¦¨ à§«à§¦à§¦ à¦Ÿà¦¾à¦•à¦¾");
                    return;
                }

                final String finalProviderNumber = num;
                final String finalProviderRaw = raw;

                new AlertDialog.Builder(this)
                        .setTitle(type + " à¦¨à¦¿à¦¶à§à¦šà¦¿à¦¤ à¦•à¦°à§à¦¨")
                        .setMessage(
                                "à¦¨à¦®à§à¦¬à¦°: "
                                        + num
                                        + "\nà¦ªà¦°à¦¿à¦®à¦¾à¦£: à§³ "
                                        + raw
                                        + "\n\n"
                                        + "à¦à¦Ÿà¦¿ à¦à¦•à¦Ÿà¦¿ à¦¡à§‡à¦®à§‹ à¦°à¦¿à¦•à§‹à§Ÿà§‡à¦¸à§à¦Ÿà¥¤ Provider API à¦¸à¦‚à¦¯à§à¦•à§à¦¤ à¦¹à¦²à§‡ à¦†à¦¸à¦² à¦²à§‡à¦¨à¦¦à§‡à¦¨ à¦¸à¦®à§à¦ªà¦¨à§à¦¨ à¦¹à¦¬à§‡à¥¤"
                        )
                        .setNegativeButton("à¦¬à¦¾à¦¤à¦¿à¦²",null)
                        .setPositiveButton(
                                "OK",
                                (d,w) -> {

                                    if (transactionBlockedFor(Double.parseDouble(finalProviderRaw))) return;
                                    recordTransaction(type,type+" / "+finalProviderNumber,Double.parseDouble(finalProviderRaw),false,"PENDING");
                                    Toast.makeText(
                                            this,
                                            "à¦°à¦¿à¦•à§‹à§Ÿà§‡à¦¸à§à¦Ÿ à¦—à§à¦°à¦¹à¦£ à¦•à¦°à¦¾ à¦¹à§Ÿà§‡à¦›à§‡à¥¤",
                                            Toast.LENGTH_LONG
                                    ).show();
                                }
                        )
                        .show();

            } catch(Exception e) {

                amount.setError(
                        "à¦¸à¦ à¦¿à¦• à¦Ÿà¦¾à¦•à¦¾à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¦à¦¿à¦¨"
                );
            }
        });
    }

    /* =========================================================
       REGISTER
       ========================================================= */

    private void showRegister() {

        getWindow().setStatusBarColor(BLUE);
        getWindow().setNavigationBarColor(BLUE);

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(22),dp(18),dp(22),dp(20));
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setBackgroundColor(BLUE);

        ScrollView sc = new ScrollView(this);

        sc.setFillViewport(true);
        sc.addView(root);

        setContentView(sc);

        TextView logo = tv("Quick Pay",29,BLUE);

        logo.setGravity(Gravity.CENTER);
        logo.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        logo.setBackground(bg(Color.WHITE,18));

        root.addView(
                logo,
                new LinearLayout.LayoutParams(dp(260),dp(75))
        );

        space(root,12);

        TextView country =
                tv(
                        "à¦¬à¦¾à¦‚à¦²à¦¾à¦¦à§‡à¦¶  ðŸ‡§ðŸ‡©",
                        18,
                        Color.GRAY
                );

        country.setGravity(Gravity.CENTER_VERTICAL);
        country.setPadding(dp(18),0,dp(18),0);
        country.setBackground(bg(Color.WHITE,12));

        root.addView(
                country,
                new LinearLayout.LayoutParams(-1,dp(55))
        );

        space(root,9);

        EditText agent =
                input("à¦°à¦¿à¦¸à§‡à¦²à¦¾à¦° à¦à¦œà§‡à¦¨à§à¦Ÿ à¦•à§‹à¦¡",false);

        root.addView(
                agent,
                new LinearLayout.LayoutParams(-1,dp(55))
        );

        space(root,9);

        EditText name = input("à¦ªà§‚à¦°à§à¦£ à¦¨à¦¾à¦®",false);

        root.addView(
                name,
                new LinearLayout.LayoutParams(-1,dp(55))
        );

        space(root,9);

        EditText phone =
                input("+880 à¦«à§‹à¦¨ à¦¨à¦®à§à¦¬à¦°",false);

        root.addView(
                phone,
                new LinearLayout.LayoutParams(-1,dp(55))
        );

        space(root,9);

        EditText pw =
                input("à§¬ à¦¡à¦¿à¦œà¦¿à¦Ÿ à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡",true);

        root.addView(
                pw,
                new LinearLayout.LayoutParams(-1,dp(55))
        );

        space(root,9);

        EditText cpw =
                input(
                        "à§¬ à¦¡à¦¿à¦œà¦¿à¦Ÿ à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡ à¦¨à¦¿à¦¶à§à¦šà¦¿à¦¤ à¦•à¦°à§à¦¨",
                        true
                );

        root.addView(
                cpw,
                new LinearLayout.LayoutParams(-1,dp(55))
        );

        space(root,16);

        TextView next =
                button(
                        "à¦ªà¦°à¦¬à¦°à§à¦¤à§€",
                        Color.WHITE,
                        BLUE
                );

        root.addView(
                next,
                new LinearLayout.LayoutParams(-1,dp(58))
        );

        next.setOnClickListener(v -> {

            String p =
                    phone.getText().toString().trim();

            String a =
                    pw.getText().toString().trim();

            String c =
                    cpw.getText().toString().trim();

            if (name.getText().toString().trim().isEmpty()) {
                name.setError("à¦ªà§‚à¦°à§à¦£ à¦¨à¦¾à¦® à¦¦à¦¿à¦¨");
                return;
            }

            if (p.isEmpty()) {
                phone.setError("à¦«à§‹à¦¨ à¦¨à¦®à§à¦¬à¦° à¦¦à¦¿à¦¨");
                return;
            }

            if (a.length() != 6) {
                pw.setError("à§¬ à¦¡à¦¿à¦œà¦¿à¦Ÿà§‡à¦° à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡ à¦¦à¦¿à¦¨");
                return;
            }

            if (!a.equals(c)) {
                cpw.setError("à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡ à¦à¦•à¦‡ à¦¨à¦¯à¦¼");
                return;
            }

            startPhoneVerification(
                    p,
                    true,
                    name.getText().toString().trim(),
                    a
            );
        });

        TextView back =
                tv(
                        "à¦…à§à¦¯à¦¾à¦•à¦¾à¦‰à¦¨à§à¦Ÿ à¦†à¦›à§‡?  à¦²à¦—à¦‡à¦¨",
                        16,
                        Color.WHITE
                );

        back.setGravity(Gravity.CENTER);

        root.addView(
                back,
                new LinearLayout.LayoutParams(-1,dp(52))
        );

        back.setOnClickListener(v -> showLogin());
    }

    /* =========================================================
       FORGOT PASSWORD
       ========================================================= */

    private void showChangePassword() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(8),dp(4),dp(8),0);

        EditText current = input(tx("à¦¬à¦°à§à¦¤à¦®à¦¾à¦¨ à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡","Current Password"),true);
        EditText next = input(tx("à¦¨à¦¤à§à¦¨ à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡","New Password"),true);
        EditText confirm = input(tx("à¦¨à¦¤à§à¦¨ à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡ à¦†à¦¬à¦¾à¦° à¦¦à¦¿à¦¨","Confirm New Password"),true);

        box.addView(current,new LinearLayout.LayoutParams(-1,dp(58)));
        space(box,10);
        box.addView(next,new LinearLayout.LayoutParams(-1,dp(58)));
        space(box,10);
        box.addView(confirm,new LinearLayout.LayoutParams(-1,dp(58)));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(tx("à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡ à¦ªà¦°à¦¿à¦¬à¦°à§à¦¤à¦¨","Change Password"))
                .setView(box)
                .setNegativeButton(tx("à¦¬à¦¾à¦¤à¦¿à¦²","Cancel"),null)
                .setPositiveButton(tx("à¦¸à¦¾à¦¬à¦®à¦¿à¦Ÿ","Submit"),null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String oldPass = current.getText().toString();
            String newPass = next.getText().toString();
            String confirmPass = confirm.getText().toString();
            String saved = pref.getString("password","");

            if (oldPass.isEmpty()) {
                current.setError(tx("à¦¬à¦°à§à¦¤à¦®à¦¾à¦¨ à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡ à¦¦à¦¿à¦¨","Enter current password"));
                return;
            }
            if (!saved.isEmpty() && !saved.equals(oldPass)) {
                current.setError(tx("à¦¬à¦°à§à¦¤à¦®à¦¾à¦¨ à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡ à¦¸à¦ à¦¿à¦• à¦¨à¦¯à¦¼","Current password is incorrect"));
                return;
            }
            if (newPass.length() < 4) {
                next.setError(tx("à¦•à¦®à¦ªà¦•à§à¦·à§‡ à§ª à¦…à¦•à§à¦·à¦° à¦¦à¦¿à¦¨","Use at least 4 characters"));
                return;
            }
            if (!newPass.equals(confirmPass)) {
                confirm.setError(tx("à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡ à¦®à¦¿à¦²à¦›à§‡ à¦¨à¦¾","Passwords do not match"));
                return;
            }

            pref.edit().putString("password",newPass).apply();
            if (firebaseReady && firestore != null && !firebaseUid().isEmpty()) {
                firestore.collection("users").document(firebaseUid())
                        .update("passwordHash", sha256(newPass), "updatedAt", com.google.firebase.firestore.FieldValue.serverTimestamp());
            }
            dialog.dismiss();
            Toast.makeText(this,tx("à¦ªà¦¾à¦¸à¦“à¦¯à¦¼à¦¾à¦°à§à¦¡ à¦ªà¦°à¦¿à¦¬à¦°à§à¦¤à¦¨ à¦¹à¦¯à¦¼à§‡à¦›à§‡","Password changed successfully"),Toast.LENGTH_SHORT).show();
        }));

        dialog.show();
    }

    private void showChangePin() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(dp(8),dp(4),dp(8),0);

        EditText current = pinInput(tx("à¦¬à¦°à§à¦¤à¦®à¦¾à¦¨ à§® à¦¡à¦¿à¦œà¦¿à¦Ÿ PIN","Current 8-digit PIN"));
        EditText next = pinInput(tx("à¦¨à¦¤à§à¦¨ à§® à¦¡à¦¿à¦œà¦¿à¦Ÿ PIN","New 8-digit PIN"));
        EditText confirm = pinInput(tx("à¦¨à¦¤à§à¦¨ PIN à¦†à¦¬à¦¾à¦° à¦¦à¦¿à¦¨","Confirm New PIN"));

        box.addView(current,new LinearLayout.LayoutParams(-1,dp(58)));
        space(box,10);
        box.addView(next,new LinearLayout.LayoutParams(-1,dp(58)));
        space(box,10);
        box.addView(confirm,new LinearLayout.LayoutParams(-1,dp(58)));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(tx("PIN à¦ªà¦°à¦¿à¦¬à¦°à§à¦¤à¦¨","Change PIN"))
                .setView(box)
                .setNegativeButton(tx("à¦¬à¦¾à¦¤à¦¿à¦²","Cancel"),null)
                .setPositiveButton(tx("à¦¸à¦¾à¦¬à¦®à¦¿à¦Ÿ","Submit"),null)
                .create();

        dialog.setOnShowListener(d -> dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String oldPin = current.getText().toString().trim();
            String newPin = next.getText().toString().trim();
            String confirmPin = confirm.getText().toString().trim();
            String saved = pref.getString("pin","");

            if (!saved.isEmpty() && !saved.equals(oldPin)) {
                current.setError(tx("à¦¬à¦°à§à¦¤à¦®à¦¾à¦¨ PIN à¦¸à¦ à¦¿à¦• à¦¨à¦¯à¦¼","Current PIN is incorrect"));
                return;
            }
            if (newPin.length() != 8) {
                next.setError(tx("à§® à¦¡à¦¿à¦œà¦¿à¦Ÿà§‡à¦° PIN à¦¦à¦¿à¦¨","Enter an 8-digit PIN"));
                return;
            }
            if (!newPin.equals(confirmPin)) {
                confirm.setError(tx("PIN à¦®à¦¿à¦²à¦›à§‡ à¦¨à¦¾","PINs do not match"));
                return;
            }

            pref.edit().putString("pin",newPin).apply();
            dialog.dismiss();
            Toast.makeText(this,tx("PIN à¦ªà¦°à¦¿à¦¬à¦°à§à¦¤à¦¨ à¦¹à¦¯à¦¼à§‡à¦›à§‡","PIN changed successfully"),Toast.LENGTH_SHORT).show();
        }));

        dialog.show();
    }

    private void showForgotPassword() {
        showChangePassword();
    }

    @Override
    protected void onDestroy() {
        if (chatListener != null) chatListener.remove();
        if (profileListener != null) profileListener.remove();
        super.onDestroy();
    }

    /* =========================================================
       BACK
       ========================================================= */

    @Override
    public void onBackPressed() {

        if (pref.getBoolean("logged_in",false)) {
            showHome();
        } else {
            showLogin();
        }
    }
}
