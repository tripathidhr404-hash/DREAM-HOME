DREAM HOME launcher icon resources

1. In your GitHub repository, open app/src/main/.
2. Upload the entire 'res' folder from this ZIP so the final paths are:
   app/src/main/res/mipmap-mdpi/ic_launcher.png
   app/src/main/res/mipmap-hdpi/ic_launcher.png
   app/src/main/res/mipmap-xhdpi/ic_launcher.png
   app/src/main/res/mipmap-xxhdpi/ic_launcher.png
   app/src/main/res/mipmap-xxxhdpi/ic_launcher.png

3. Open app/src/main/AndroidManifest.xml and find the <application ...> tag.
   Add this attribute inside that tag:
   android:icon="@mipmap/ic_launcher"

   If android:icon is already present, replace its value instead of adding a duplicate.
   Keep android:label / app name unchanged (DREAM HOME).

4. Commit the changes, then run the existing GitHub Actions workflow to build a new APK.

Note: This ZIP contains launcher icon PNGs only; it does not replace your app source or build files.
