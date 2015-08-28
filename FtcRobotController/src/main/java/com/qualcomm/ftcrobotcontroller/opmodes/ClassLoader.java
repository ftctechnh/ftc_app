package com.qualcomm.ftcrobotcontroller.opmodes;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.RobotLog;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Zach on 8/27/2015.
 */
public class ClassLoader {

    private static URLClassLoader classLoader;
    private static List<Class<? extends OpMode>> opModeList;

    public static final String FILE_LOCATION = "FIRST";

    public static List<Class<? extends OpMode>> loadJars(List<File> filelist) {
        URL[] jarurls = getJarURLs(filelist);
        classLoader = new URLClassLoader(jarurls, ClassLoader.class.getClassLoader());
        opModeList = new ArrayList<Class<? extends OpMode>>();
        for (File jarfile : filelist) {
            if (jarfile.getName().endsWith(".jar")) {
                try {
                    loadJarFile(jarfile);
                } catch (Exception ignore) {}
            }
        }
        return opModeList;
    }

    private static void loadJarFile(File jarfile) throws IOException {
        RobotLog.i("[Thunderbots] Now opening " + jarfile);
        JarFile jarobj = new JarFile(jarfile);
        Enumeration<JarEntry> jarentries = jarobj.entries();
        while (jarentries.hasMoreElements()) {
            JarEntry entry = jarentries.nextElement();
            if (entry.getName().endsWith(".class")) {
                attemptLoadClass(entry.getName());
            }
        }
        jarobj.close();
    }

    private static void attemptLoadClass(String entryname) {
        String classname = entryname.replace('/', '.');
        classname = classname.substring(0, classname.length() - 6);
        RobotLog.i("[Thunderbots] Checking class " + classname);
        try {
            Class<?> c = classLoader.loadClass(classname);
            Object instance = c.newInstance();
            if (instance instanceof OpMode) {
                RobotLog.i("[Thunderbots] Found " + classname + " as an op mode!");
                opModeList.add(((OpMode)instance).getClass());
            }
        } catch (Throwable ex) {
            if (ex instanceof Exception) {
                RobotLog.logStacktrace((Exception) ex);
            }
        }
    }

    private static URL[] getJarURLs(List<File> fileList) {
        List<URL> jarList = new ArrayList<URL>();
        for (File f : fileList) {
            if (f.isFile() && f.getName().endsWith(".jar")) {
                try {
                    jarList.add(f.toURI().toURL());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
        return jarList.toArray(new URL[jarList.size()]);
    }

    public static List<File> getFileSet() {
        List<File> fileList = new ArrayList<File>();
        getFilesInDirectory(getTargetDirectory(), fileList);
        return fileList;
    }

    private static void getFilesInDirectory(File current, List<File> fileList) {
        for (File f : current.listFiles()) {
            if (f.isFile()) {
                fileList.add(f);
                RobotLog.i("[Thunderbots] Found file: " + f);
            } else if (f.isDirectory()) {
                getFilesInDirectory(f, fileList);
            }
        }
    }

    private static final File getTargetDirectory() {
        File sdcard = Environment.getExternalStorageDirectory();
        return new File(sdcard, FILE_LOCATION);
    }

}
