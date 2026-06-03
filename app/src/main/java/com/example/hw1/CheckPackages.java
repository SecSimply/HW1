package com.example.hw1;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.List;/**
 * A utility class to list all installed packages and their requested permissions.
 * The results are logged to Logcat for debugging and analysis.
 */
public class CheckPackages {

    private static final String TAG = "CheckPackages";

    /**
     * Retrieves and logs all installed applications and their permissions.
     *
     * @param context The application context, used to get the PackageManager.
     */
    public static void logInstalledPackagesAndPermissions(Context context) {
        // Get the PackageManager instance
        final PackageManager pm = context.getPackageManager();

        // Get a list of all installed applications.
        // The '0' flag means we get basic information without any special flags.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        Log.d(TAG, "--- Listing All Installed Packages and Permissions ---");

        // Loop through each installed application
        for (ApplicationInfo applicationInfo : packages) {
            try {
                // Get PackageInfo for the current application to access its permissions.
                // We use PackageManager.GET_PERMISSIONS to ensure the permissions array is populated.
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);

                // Get the user-friendly application name
                String appName = (String) pm.getApplicationLabel(applicationInfo);

                // Start logging the info for this specific application
                Log.d(TAG, "Application: " + appName + " | Package: " + applicationInfo.packageName);

                // The requestedPermissions array can be null if the app requests no permissions.
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null && requestedPermissions.length > 0) {
                    // Loop through the permissions and log each one
                    for (String permission : requestedPermissions) {
                        Log.d(TAG, "  > Permission: " + permission);
                    }
                } else {
                    // Log if the application has not requested any permissions
                    Log.d(TAG, "  > No permissions requested for this package.");
                }

            } catch (PackageManager.NameNotFoundException e) {
                // This exception should not happen in this loop, but it's good practice to handle it.
                Log.e(TAG, "Error: Could not find package info for " + applicationInfo.packageName, e);
            }
        }

        Log.d(TAG, "--- Finished Listing Packages ---");
    }
}
