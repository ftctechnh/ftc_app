package org.lasarobotics.vision.test.android;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Implements the Android camera
 */
public enum Cameras {
    PRIMARY(0),
    SECONDARY(1),
    OTHER_1(2),
    OTHER_2(3);

    final int id;

    Cameras(int id) {
        this.id = id;
    }

    public static boolean isHardwareAvailable(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public int getID() {
        return id;
    }

    public Camera createCamera() {
        return new Camera(this);
    }
}
