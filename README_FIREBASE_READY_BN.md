# Quick Pay — Firebase Backend Ready

এই সংস্করণে বিদ্যমান UI রেখে Firebase ভিত্তিক backend foundation যোগ করা হয়েছে।

- Firebase Auth SDK + Firestore
- Authenticated user session (anonymous Firebase session, existing phone/password UI unchanged)
- User profile sync
- Transaction records sync
- Admin-controlled bKash/Nagad/Rocket payment numbers from `settings/general`
- Payment number visibility controls
- Firestore security rules

Mobile Recharge এখনো real provider API ছাড়া request/demo হিসেবেই থাকে।
