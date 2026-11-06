DREAM HOME - Android Studio Project

यह Android ऐप प्रोजेक्ट आपकी INDEX.html फाइल को WebView में चलाता है। मूल HTML पेज को बदला नहीं गया है। ऐप का नाम DREAM HOME रखा गया है।

जरूरी बातें:
1. पहली बार ऐप खोलते समय इंटरनेट चालू रखें, ताकि Tailwind CSS, Font Awesome और Google Fonts लोड हो सकें।
2. WebView cache mode सेट है; इंटरनेट से सफलतापूर्वक लोड हुई बाहरी फाइलें दोबारा उपलब्ध रहने की कोशिश करेंगी। Android/WebView cache साफ होने या CDN cache न मिलने पर ऑफलाइन डिजाइन अधूरा दिख सकता है। इसलिए यह अभी 100% offline APK नहीं है।
3. ऐप का डेटा WebView localStorage में रहता है। ऐप का data/cache clear या ऐप uninstall करने पर डेटा मिट सकता है। समय-समय पर JSON Backup लें।
4. Restore विकल्प के लिए Android file picker जोड़ा गया है।

APK बनाने के चरण:
1. कंप्यूटर में Android Studio इंस्टॉल करें।
2. Android Studio > Open चुनें और इस ZIP से निकला DREAM_HOME_Android_Project फोल्डर खोलें।
3. Gradle Sync पूरा होने दें (इसके लिए इंटरनेट चाहिए और Android SDK Platform 35 स्थापित होना चाहिए)।
4. Build > Build Bundle(s) / APK(s) > Build APK(s) चुनें।
5. APK app/build/outputs/apk/debug/app-debug.apk में बनेगी।

नोट: इस वातावरण में Android SDK/Gradle उपलब्ध नहीं था, इसलिए यहाँ सीधे APK compile नहीं की जा सकी।
