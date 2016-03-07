package org.lasarobotics.vision.test.android;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Android utilities
 */
public final class Util {
    /**
     * Gets the contexts of an activity without calling from an Activity class
     *
     * @return the main Application (as a Context)
     */
    public static Context getContext() {
        try {
            final Class<?> activityThreadClass =
                    Class.forName("android.app.ActivityThread");
            //find and load the main activity method
            final Method method = activityThreadClass.getMethod("currentApplication");
            return (Application) method.invoke(null, (Object[]) null);
        } catch (final java.lang.Throwable e) {
            // handle exception
            throw new IllegalArgumentException("No context could be retrieved!");
        }
    }

    /**
     * Get the Android application's data directory
     *
     * @param ctx The Application Context - use "this" if inside context; getContext() otherwise
     * @return The data directory, as a string
     */
    public static String getDataDirectory(Context ctx) {
        try {
            return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).applicationInfo.dataDir;
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException("Data directory not found!");
        }
    }

    /**
     * Gets the user working directory
     *
     * @return The user working directory, as a string
     */
    public static String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }

    /**
     * Gets the directory of the DCIM folder, preferably on the external storage
     *
     * @return The directory of the DCIM folder, as a string
     */
    public static String getDCIMDirectory() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    }

    public static File createFileOnDevice(String fileDirectory, String fileName, boolean overwrite) throws IOException {
        fileDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + fileDirectory;
        File dir = new File(fileDirectory);

        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(fileDirectory, fileName);
        // if file doesn't exists, then create it
        if (!file.exists() || overwrite) {
            if (file.exists() && overwrite)
                file.delete();
            file.createNewFile();
        } else {
            int i = 0;
            while (file.exists()) {
                file = new File(fileDirectory, fileName + "." + i);
                i++;
            }
            file.createNewFile();
        }
        return file;
    }
}
