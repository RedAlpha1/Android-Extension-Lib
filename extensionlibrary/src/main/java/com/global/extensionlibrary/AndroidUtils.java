package com.global.extensionlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A comprehensive utilities library for Android developers
 */
public class AndroidUtils {

    private static final String TAG = "AndroidUtils";
    
    // Prevent instantiation
    private AndroidUtils() {}
    
    /**
     * UI UTILITIES
     */
    
    /**
     * Hides the keyboard from the current focus
     * @param activity The current activity
     */
    public static void hideKeyboard(Activity activity) {
        if (activity == null) return;
        
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    
    /**
     * Shows a toast message
     * @param context Application context
     * @param message Message to display
     * @param duration Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     */
    public static void showToast(Context context, String message, int duration) {
        if (context == null || TextUtils.isEmpty(message)) return;
        
        Toast.makeText(context, message, duration).show();
    }
    
    /**
     * Convert dp to pixel
     * @param context Application context
     * @param dp Value in dp
     * @return Value in pixels
     */
    public static int dpToPx(Context context, float dp) {
        if (context == null) return 0;
        
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
    
    /**
     * Convert pixels to dp
     * @param context Application context
     * @param px Value in pixels
     * @return Value in dp
     */
    public static int pxToDp(Context context, float px) {
        if (context == null) return 0;
        
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }
    
    /**
     * Get screen width in pixels
     * @param context Application context
     * @return Screen width in pixels
     */
    public static int getScreenWidth(Context context) {
        if (context == null) return 0;
        
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }
    
    /**
     * Get screen height in pixels
     * @param context Application context
     * @return Screen height in pixels
     */
    public static int getScreenHeight(Context context) {
        if (context == null) return 0;
        
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }
    
    /**
     * NETWORK UTILITIES
     */
    
    /**
     * Check if the device is connected to the internet
     * @param context Application context
     * @return true if connected, false otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return capabilities != null && (
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        } else {
            // For older devices
            return connectivityManager.getActiveNetworkInfo() != null && 
                   connectivityManager.getActiveNetworkInfo().isConnected();
        }
    }
    
    /**
     * Open system settings
     * @param context Application context
     */
    public static void openNetworkSettings(Context context) {
        if (context == null) return;
        
        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
    }
    
    /**
     * DATE & TIME UTILITIES
     */
    
    /**
     * Format date to string
     * @param date Date to format
     * @param format Format pattern (e.g., "yyyy-MM-dd HH:mm:ss")
     * @return Formatted date string
     */
    public static String formatDate(Date date, String format) {
        if (date == null || TextUtils.isEmpty(format)) return "";
        
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(date);
    }
    
    /**
     * Parse string to date
     * @param dateString String date
     * @param format Format pattern
     * @return Date object or null if parsing fails
     */
    public static Date parseDate(String dateString, String format) {
        if (TextUtils.isEmpty(dateString) || TextUtils.isEmpty(format)) return null;
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
            return sdf.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get current date and time as a string
     * @param format Format pattern
     * @return Current date/time as string
     */
    public static String getCurrentDateTime(String format) {
        if (TextUtils.isEmpty(format)) format = "yyyy-MM-dd HH:mm:ss";
        
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date());
    }
    
    /**
     * Get time difference between two dates
     * @param startDate Start date
     * @param endDate End date
     * @param timeUnit TimeUnit for result (e.g., TimeUnit.DAYS)
     * @return Time difference in the specified unit
     */
    public static long getDateDiff(Date startDate, Date endDate, TimeUnit timeUnit) {
        if (startDate == null || endDate == null || timeUnit == null) return 0;
        
        long diffInMillis = endDate.getTime() - startDate.getTime();
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }
    
    /**
     * PERMISSION UTILITIES
     */
    
    /**
     * Check if a permission is granted
     * @param context Application context
     * @param permission Permission to check
     * @return true if granted, false otherwise
     */
    public static boolean isPermissionGranted(Context context, String permission) {
        if (context == null || TextUtils.isEmpty(permission)) return false;
        
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }
    
    /**
     * Request permissions
     * @param activity Current activity
     * @param permissions Permissions to request
     * @param requestCode Request code for onRequestPermissionsResult
     */
    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {
        if (activity == null || permissions == null || permissions.length == 0) return;
        
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }
    
    /**
     * Check if should show permission rationale
     * @param activity Current activity
     * @param permission Permission to check
     * @return true if rationale should be shown, false otherwise
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permission) {
        if (activity == null || TextUtils.isEmpty(permission)) return false;
        
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }
    
    /**
     * Open app settings
     * @param context Application context
     */
    public static void openAppSettings(Context context) {
        if (context == null) return;
        
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
    
    /**
     * FILE UTILITIES
     */
    
    /**
     * Check if external storage is available for read and write
     * @return true if available, false otherwise
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    
    /**
     * Check if external storage is available for read
     * @return true if available, false otherwise
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || 
               Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
    
    /**
     * Get app external files directory
     * @param context Application context
     * @param type Directory type (null for root)
     * @return File directory
     */
    public static File getAppExternalFilesDir(Context context, String type) {
        if (context == null) return null;
        
        return context.getExternalFilesDir(type);
    }
    
    /**
     * Save bitmap to file
     * @param bitmap Bitmap to save
     * @param file Target file
     * @param format Bitmap.CompressFormat (JPEG, PNG, etc.)
     * @param quality Compression quality (0-100)
     * @return true if saved successfully, false otherwise
     */
    public static boolean saveBitmapToFile(Bitmap bitmap, File file, Bitmap.CompressFormat format, int quality) {
        if (bitmap == null || file == null) return false;
        
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(format, quality, fos);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error saving bitmap: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * VALIDATION UTILITIES
     */
    
    /**
     * Validate email address
     * @param email Email to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) return false;
        
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    
    /**
     * Validate phone number (basic validation)
     * @param phone Phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (TextUtils.isEmpty(phone)) return false;
        
        // Basic validation, can be customized for specific formats
        return Patterns.PHONE.matcher(phone).matches();
    }
    
    /**
     * Validate URL
     * @param url URL to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUrl(String url) {
        if (TextUtils.isEmpty(url)) return false;
        
        return Patterns.WEB_URL.matcher(url).matches();
    }
    
    /**
     * Validate password strength
     * @param password Password to validate
     * @param minLength Minimum length
     * @param requireUppercase Require at least one uppercase letter
     * @param requireLowercase Require at least one lowercase letter
     * @param requireDigit Require at least one digit
     * @param requireSpecial Require at least one special character
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password, int minLength, boolean requireUppercase,
                                         boolean requireLowercase, boolean requireDigit, boolean requireSpecial) {
        if (TextUtils.isEmpty(password)) return false;
        
        if (password.length() < minLength) return false;
        
        if (requireUppercase && !Pattern.compile("[A-Z]").matcher(password).find()) return false;
        
        if (requireLowercase && !Pattern.compile("[a-z]").matcher(password).find()) return false;
        
        if (requireDigit && !Pattern.compile("[0-9]").matcher(password).find()) return false;
        
        if (requireSpecial && !Pattern.compile("[^A-Za-z0-9]").matcher(password).find()) return false;
        
        return true;
    }
    
    /**
     * SECURITY UTILITIES
     */
    
    /**
     * Generate MD5 hash
     * @param input String to hash
     * @return MD5 hash or null if error
     */
    public static String md5(String input) {
        if (TextUtils.isEmpty(input)) return "";
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error generating MD5: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Generate SHA-256 hash
     * @param input String to hash
     * @return SHA-256 hash or null if error
     */
    public static String sha256(String input) {
        if (TextUtils.isEmpty(input)) return "";
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Error generating SHA-256: " + e.getMessage());
            return "";
        }
    }
    
    /**
     * Encode string to Base64
     * @param input String to encode
     * @return Base64 encoded string
     */
    public static String encodeBase64(String input) {
        if (TextUtils.isEmpty(input)) return "";
        
        byte[] bytes = input.getBytes();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    
    /**
     * Decode Base64 to string
     * @param input Base64 string to decode
     * @return Decoded string
     */
    public static String decodeBase64(String input) {
        if (TextUtils.isEmpty(input)) return "";
        
        byte[] bytes = Base64.decode(input, Base64.DEFAULT);
        return new String(bytes);
    }
    
    /**
     * Generate a UUID
     * @return Random UUID as string
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * IMAGE UTILITIES
     */
    
    /**
     * Convert bitmap to Base64 string
     * @param bitmap Bitmap to convert
     * @param format Compression format
     * @param quality Compression quality (0-100)
     * @return Base64 string representation
     */
    public static String bitmapToBase64(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        if (bitmap == null) return "";
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(format, quality, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    
    /**
     * Convert Base64 string to bitmap
     * @param base64 Base64 string
     * @return Bitmap or null if error
     */
    public static Bitmap base64ToBitmap(String base64) {
        if (TextUtils.isEmpty(base64)) return null;
        
        try {
            byte[] decodedBytes = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            Log.e(TAG, "Error converting Base64 to bitmap: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Resize bitmap
     * @param bitmap Original bitmap
     * @param targetWidth Target width
     * @param targetHeight Target height
     * @return Resized bitmap
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, int targetWidth, int targetHeight) {
        if (bitmap == null || targetWidth <= 0 || targetHeight <= 0) return null;
        
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);
    }
    
    /**
     * DEVICE UTILITIES
     */
    
    /**
     * Get device model
     * @return Device model
     */
    public static String getDeviceModel() {
        return Build.MODEL;
    }
    
    /**
     * Get Android version
     * @return Android version
     */
    public static String getAndroidVersion() {
        return Build.VERSION.RELEASE;
    }
    
    /**
     * Get Android SDK version
     * @return Android SDK version
     */
    public static int getAndroidSdkVersion() {
        return Build.VERSION.SDK_INT;
    }
    
    /**
     * Check if device is running on Android Oreo (API 26) or higher
     * @return true if API 26+, false otherwise
     */
    public static boolean isOreoOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
    
    /**
     * Check if device is running on Android 10 (API 29) or higher
     * @return true if API 29+, false otherwise
     */
    public static boolean isAndroid10OrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    }
    
    /**
     * Check if device is running on Android 11 (API 30) or higher
     * @return true if API 30+, false otherwise
     */
    public static boolean isAndroid11OrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }
    
    /**
     * INTENT UTILITIES
     */
    
    /**
     * Open URL in browser
     * @param context Application context
     * @param url URL to open
     * @return true if intent was started, false otherwise
     */
    public static boolean openUrl(Context context, String url) {
        if (context == null || TextUtils.isEmpty(url)) return false;
        
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error opening URL: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Open email app
     * @param context Application context
     * @param email Recipient email
     * @param subject Email subject
     * @param body Email body
     * @return true if intent was started, false otherwise
     */
    public static boolean sendEmail(Context context, String email, String subject, String body) {
        if (context == null || TextUtils.isEmpty(email)) return false;
        
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            
            if (!TextUtils.isEmpty(subject)) {
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            }
            
            if (!TextUtils.isEmpty(body)) {
                intent.putExtra(Intent.EXTRA_TEXT, body);
            }
            
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error sending email: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Open phone dialer
     * @param context Application context
     * @param phoneNumber Phone number to dial
     * @return true if intent was started, false otherwise
     */
    public static boolean dialPhoneNumber(Context context, String phoneNumber) {
        if (context == null || TextUtils.isEmpty(phoneNumber)) return false;
        
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error dialing number: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Share text
     * @param context Application context
     * @param title Share chooser title
     * @param text Text to share
     * @return true if intent was started, false otherwise
     */
    public static boolean shareText(Context context, String title, String text) {
        if (context == null || TextUtils.isEmpty(text)) return false;
        
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(intent, title));
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error sharing text: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Open Google Maps with a location
     * @param context Application context
     * @param latitude Latitude
     * @param longitude Longitude
     * @param label Location label
     * @return true if intent was started, false otherwise
     */
    public static boolean openGoogleMaps(Context context, double latitude, double longitude, String label) {
        if (context == null) return false;
        
        try {
            String query = TextUtils.isEmpty(label) ? latitude + "," + longitude : Uri.encode(label);
            Uri uri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + query);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error opening maps: " + e.getMessage());
            return false;
        }
    }
}