# AndroidUtils

A comprehensive utility library for Android developers that provides a wide range of helper methods to simplify common Android development tasks.

## Overview

AndroidUtils is a collection of static utility methods that cover various aspects of Android development, including UI operations, network handling, date & time formatting, permission management, file operations, validation, security, image processing, device information, and intent handling.

## Features

- **UI Utilities**: Methods for keyboard management, toast messages, and screen metrics conversion
- **Network Utilities**: Check network connectivity and open system settings
- **Date & Time Utilities**: Format and parse dates, get time differences
- **Permission Utilities**: Check, request, and handle runtime permissions
- **File Utilities**: External storage operations and bitmap saving
- **Validation Utilities**: Validate emails, phone numbers, URLs, and passwords
- **Security Utilities**: Generate MD5/SHA-256 hashes, Base64 encoding/decoding, and UUID generation
- **Image Utilities**: Convert between bitmaps and Base64, resize images
- **Device Utilities**: Get device information and check Android version
- **Intent Utilities**: Open URLs, send emails, dial numbers, share text, and open maps

## Installation

### Step 1. Add JitPack repository

Add it in your root settings.gradle at the end of repositories:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### Step 2. Add the dependency

Add the following to your app's build.gradle file:

```gradle
dependencies {
    implementation 'com.github.RedAlpha1:Android-Extension-Lib:Tag'
}
```

> Note: Replace 'Tag' with the actual release version (e.g., 'v1.0.0').

## Usage

### UI Utilities

```java
// Hide keyboard
AndroidUtils.hideKeyboard(activity);

// Show toast
AndroidUtils.showToast(context, "Message", Toast.LENGTH_SHORT);

// Convert dp to pixels
int pixels = AndroidUtils.dpToPx(context, 16);

// Get screen dimensions
int width = AndroidUtils.getScreenWidth(context);
int height = AndroidUtils.getScreenHeight(context);
```

### Network Utilities

```java
// Check if network is available
if (AndroidUtils.isNetworkAvailable(context)) {
    // Perform network operation
} else {
    // Show offline message
}

// Open network settings
AndroidUtils.openNetworkSettings(context);
```

### Date & Time Utilities

```java
// Format date
String formattedDate = AndroidUtils.formatDate(new Date(), "yyyy-MM-dd");

// Parse date
Date date = AndroidUtils.parseDate("2023-01-01", "yyyy-MM-dd");

// Get current date/time
String now = AndroidUtils.getCurrentDateTime("HH:mm:ss");

// Get time difference
long daysDiff = AndroidUtils.getDateDiff(startDate, endDate, TimeUnit.DAYS);
```

### Permission Utilities

```java
// Check permission
if (AndroidUtils.isPermissionGranted(context, Manifest.permission.CAMERA)) {
    // Permission already granted
} else {
    // Request permission
    AndroidUtils.requestPermissions(activity, 
                                   new String[]{Manifest.permission.CAMERA}, 
                                   REQUEST_CAMERA);
}

// Check if should show rationale
if (AndroidUtils.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
    // Show rationale
}

// Open app settings
AndroidUtils.openAppSettings(context);
```

### File Utilities

```java
// Check if storage is available
if (AndroidUtils.isExternalStorageWritable()) {
    // Save file
    File directory = AndroidUtils.getAppExternalFilesDir(context, null);
    File file = new File(directory, "image.jpg");
    AndroidUtils.saveBitmapToFile(bitmap, file, Bitmap.CompressFormat.JPEG, 90);
}
```

### Validation Utilities

```java
// Validate email
if (AndroidUtils.isValidEmail("user@example.com")) {
    // Valid email
}

// Validate phone
if (AndroidUtils.isValidPhone("+1234567890")) {
    // Valid phone
}

// Validate URL
if (AndroidUtils.isValidUrl("https://example.com")) {
    // Valid URL
}

// Validate password
if (AndroidUtils.isValidPassword("Password123!", 8, true, true, true, true)) {
    // Valid password
}
```

### Security Utilities

```java
// Generate hash
String md5Hash = AndroidUtils.md5("input string");
String sha256Hash = AndroidUtils.sha256("input string");

// Base64 encoding/decoding
String encoded = AndroidUtils.encodeBase64("text");
String decoded = AndroidUtils.decodeBase64(encoded);

// Generate UUID
String uuid = AndroidUtils.generateUuid();
```

### Image Utilities

```java
// Convert bitmap to Base64
String base64 = AndroidUtils.bitmapToBase64(bitmap, Bitmap.CompressFormat.JPEG, 90);

// Convert Base64 to bitmap
Bitmap bitmap = AndroidUtils.base64ToBitmap(base64);

// Resize bitmap
Bitmap resized = AndroidUtils.resizeBitmap(originalBitmap, 300, 200);
```

### Device Utilities

```java
// Get device information
String model = AndroidUtils.getDeviceModel();
String androidVersion = AndroidUtils.getAndroidVersion();
int sdkVersion = AndroidUtils.getAndroidSdkVersion();

// Check Android version
if (AndroidUtils.isAndroid10OrHigher()) {
    // Use Android 10+ specific API
}
```

### Intent Utilities

```java
// Open URL
AndroidUtils.openUrl(context, "https://example.com");

// Send email
AndroidUtils.sendEmail(context, "recipient@example.com", "Subject", "Body");

// Dial phone number
AndroidUtils.dialPhoneNumber(context, "+1234567890");

// Share text
AndroidUtils.shareText(context, "Share with", "Text to share");

// Open maps
AndroidUtils.openGoogleMaps(context, 37.7749, -122.4194, "San Francisco");
```

## Requirements

- Android API level 24 or higher
- AndroidX

## License

```
Copyright (c) 2025 Prakhar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request
