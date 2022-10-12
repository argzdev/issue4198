# issue4198
### What product is affected?
- Firebase Dynamic Links
### Summary
- Updating build.gradle `targetSdk` to 33 will causes Firebase Dynamic links to not work on Android 13 devices.
### Steps to reproduce
1. Run project on an Android 13 emulator or physical device.
2. Go to the device's browser and type `issue3842.page.link/deeplinks` in the URL.
### Expected Result
- The deeplink will redirect to the installed APK.
### Actual Result
- An infinite loading is shown, and does not redirect.
- Logcat shows the following error:
```
2022-10-12 23:26:09.805 7960-7960/com.google.android.gms E/AcceptInvitation: Activity not found to handle Intent action [CONTEXT service_id=77 ]
    android.content.ActivityNotFoundException: Unable to find explicit activity class {com.argz.issue4198/com.argz.issue4198.MainActivity}; have you declared this activity in your AndroidManifest.xml, or does your intent not match its declared <intent-filter>?
        at android.app.Instrumentation.checkStartActivityResult(Instrumentation.java:2158)
        at android.app.Instrumentation.execStartActivity(Instrumentation.java:1805)
        at android.app.Activity.startActivityForResult(Activity.java:5470)
        at ezq.platform_startActivityForResult(:com.google.android.gms@221821047@22.18.21 (190800-453244992):2)
        at ezp.startActivityForResult(:com.google.android.gms@221821047@22.18.21 (190800-453244992):2)
```
