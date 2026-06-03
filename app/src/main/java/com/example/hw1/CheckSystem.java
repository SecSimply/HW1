package com.example.hw1; // Use your app's package name

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import androidx.core.content.ContextCompat;

/**
 * A utility class to check various system settings and permissions.
 * The results are logged to Logcat. i need to make a change for commiting
 */
public class CheckSystem {

    private static final String TAG = "CheckSystem";

    /**
     * Checks all specified system settings and logs the results.
     *
     * @param context The application context.
     */
    public static void performSystemChecks(Context context) {
        Log.d(TAG, "--- Performing System Checks ---");
        isUnknownSourcesEnabled(context);
        isAccessibilityServiceEnabled(context);
        hasSendSmsPermission(context);
        hasProcessOutgoingCallsPermission(context);
        Log.d(TAG, "--- System Checks Complete ---");
    }

    /**
     * Checks if the "Install from unknown sources" setting is enabled for the app.
     * For Android Oreo (API 26) and above, this is a per-app permission.
     * For older versions, it's a global system setting.
     *
     * @param context The application context.
     * @return true if installation from unknown sources is allowed, false otherwise.
     */
    public static boolean isUnknownSourcesEnabled(Context context) {
        boolean isEnabled;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For Android 8.0 (Oreo) and above, check canRequestPackageInstalls()
            isEnabled = context.getPackageManager().canRequestPackageInstalls();
        } else {
            // For versions below Oreo, check the global setting [4]
            try {
                isEnabled = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.INSTALL_NON_MARKET_APPS) == 1;
            } catch (Settings.SettingNotFoundException e) {
                Log.e(TAG, "INSTALL_NON_MARKET_APPS setting not found.", e);
                isEnabled = false;
            }
        }
        Log.d(TAG, "Install from unknown sources enabled: " + isEnabled);
        return isEnabled;
    }

    /**
     * Checks if any accessibility services are enabled on the device.
     * This method checks the general accessibility setting, not for a specific service.
     *
     * @param context The application context.
     * @return true if any accessibility service is enabled, false otherwise.
     */
    public static boolean isAccessibilityServiceEnabled(Context context) {
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding ACCESSIBILITY_ENABLED setting", e);
        }

        boolean isEnabled = accessibilityEnabled == 1;
        Log.d(TAG, "Accessibility settings enabled: " + isEnabled);

        // Optional: Log which services are enabled if any
        if (isEnabled) {
            String enabledServices = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (enabledServices != null) {
                Log.d(TAG, "Enabled accessibility services: " + enabledServices);
            }
        }
        return isEnabled;
    }


    /**
     * Checks if the app has been granted the SEND_SMS permission.
     *
     * @param context The application context.
     * @return true if the permission is granted, false otherwise.
     */
    public static boolean hasSendSmsPermission(Context context) {
        boolean hasPermission = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED;
        Log.d(TAG, "SEND_SMS permission granted: " + hasPermission);
        return hasPermission;
    }

    /**
     * Checks if the app has been granted the PROCESS_OUTGOING_CALLS permission. [10]
     *
     * @param context The application context.
     * @return true if the permission is granted, false otherwise.
     */
    public static boolean hasProcessOutgoingCallsPermission(Context context) {
        boolean hasPermission = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.PROCESS_OUTGOING_CALLS
        ) == PackageManager.PERMISSION_GRANTED;
        Log.d(TAG, "PROCESS_OUTGOING_CALLS permission granted: " + hasPermission);
        return hasPermission;
    }
}
