package com.walnuthillseagles.walnutlibrary;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;

import com.qualcomm.robotcore.hardware.GyroSensor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Yan Vologzhanin on 1/4/2016.
 */
@Deprecated
public class GyroDrive extends DistanceDrive{
    public static final long WAITRESOLUTION = 300;
    //Hare
    private GyroSensor gyro;

    //Constatns that allow you to modify behaviors
    //Value used when motors need to be realigned
    public static final double MOTORADJUSTMENTPOW = 0.8;
    //Gyro only
    public GyroDrive(DistanceMotor myLeft, DistanceMotor myRight, double myWidth,
                     GyroSensor myGyro){
        super(myLeft, myRight,myWidth);
        gyro = myGyro;
        gyro.calibrate();
    }
    //Stuff I worry about
}
