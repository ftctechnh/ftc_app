package org.upacreekrobotics.dashboard;

import android.content.Context;
import android.util.Log;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dalvik.system.DexFile;

/**
 * Classpath scanning utility designed to find annotations at runtime.
 */
public class ClasspathScanner {
    public static final String TAG = "ClasspathScanner";

    private DexFile dexFile;
    private ClassFilter filter;

    public ClasspathScanner(ClassFilter filter) {
        Context context = AppUtil.getInstance().getApplication();
        try {
            this.dexFile = new DexFile(context.getPackageCodePath());
        } catch (IOException e) {
            Log.w(TAG, e);
        }
        this.filter = filter;
    }

    /**
     * Scans the dex file for classes and process them.
     */
    public void scanClasspath() {
        List<String> classNames = new ArrayList<>(Collections.list(dexFile.entries()));

        ClassLoader classLoader = org.upacreekrobotics.dashboard.ClasspathScanner.class.getClassLoader();

        for (String className : classNames) {
            if (filter.shouldProcessClass(className)) {
                try {
                    Class klass = Class.forName(className, false, classLoader);

                    filter.processClass(klass);
                } catch (ClassNotFoundException e) {
                    Log.w(TAG, e);
                }
            }
        }
    }
}