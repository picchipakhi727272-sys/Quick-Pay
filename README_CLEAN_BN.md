# Quick Pay — Clean Firebase Ready Project

এই ZIP-টি মূল Quick Pay project-এর পরিষ্কার করা সংস্করণ।

রাখা হয়েছে:
- Firebase Auth + Firestore যুক্ত MainActivity.java
- app/build.gradle
- google-services.json
- AndroidManifest.xml
- firestore.rules
- GitHub Actions APK build workflow

বাদ দেওয়া হয়েছে:
- ভুল nested app/src/app/src... structure
- duplicate/ভুল জায়গার MainActivity.java
- ভেতরে ঢোকানো duplicate Firebase ZIP
- ভাঙা import-ready workflow

নোট: APK build করার জন্য GitHub Actions workflow Gradle 8.9 ব্যবহার করে।
