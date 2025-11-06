# GeoPhotoMap (osmdroid) - Android project (with GitHub Actions)

This project demonstrates:
- Take photo with camera
- Add text onto photo
- Read EXIF geotag and show location on an offline-friendly map (osmdroid) — no Google Maps API key needed.

This archive includes a GitHub Actions workflow which is triggered manually (workflow_dispatch) to build a debug APK.

## How to use
1. Create a new GitHub repository and upload the contents of this ZIP (Upload files in the repository page).
2. Go to the repository's Actions tab -> choose "Android Build (manual)" -> Run workflow -> choose branch 'main' and click Run.
3. After the run completes, open the workflow run and download the artifact `app-debug-apk`.

If the build fails due to Gradle or SDK versions, tell me the error and I will adjust the workflow accordingly.
