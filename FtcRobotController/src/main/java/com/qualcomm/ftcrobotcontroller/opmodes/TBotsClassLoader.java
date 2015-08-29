package com.qualcomm.ftcrobotcontroller.opmodes;

import android.os.Environment;
import android.util.Log;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

public class TBotsClassLoader {

    private static ClassLoader classLoader;
    private static List<Class<? extends OpMode>> opModeList;

    public static final String FILE_LOCATION = "FIRST";

    public static final String LOG_TAG = "Thunderbots";

    public static List<Class<? extends OpMode>> loadJars(List<File> filelist) {
        URL[] jarurls = getJarURLs(filelist);
        //classLoader = new URLClassLoader(jarurls, TBotsClassLoader.class.getClassLoader());
        classLoader = getClassLoader(jarurls);
        Thread.currentThread().setContextClassLoader(classLoader);
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

    // experimental
    private static ClassLoader getClassLoader(URL[] jarurls) {
        String pathString = getDelimitedPathString(jarurls);
        File cacheFile = new File(FtcRobotControllerActivity.getPrivateFilesDir(), "/thunderbots/");
        cacheFile.mkdirs();
        String cacheDir = cacheFile.toString();
        ClassLoader parentLoader = TBotsClassLoader.class.getClassLoader();
        return new DexClassLoader(pathString, cacheDir, null, parentLoader);
        //return new PathClassLoader(pathString, parentLoader);
    }

    private static <T> String getDelimitedPathString(T[] arr) {
        String result = "";
        for (T obj : arr){
            result += File.pathSeparator;
            result += obj.toString();
        }
        return result.substring(1);
    }

    /*
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
    */

    private static void loadJarFile(File jarfile) throws IOException {
        //Log.d(LOG_TAG, "Now opening " + jarfile);
        //DexFile jarobj = new DexFile(jarfile);
        File cache = new File(FtcRobotControllerActivity.getPrivateFilesDir(), "/thunderbots/temp");
        DexFile jarobj = DexFile.loadDex(jarfile.getAbsolutePath(), cache.getAbsolutePath(), 0);
        Enumeration<String> jarentries = jarobj.entries();
        while (jarentries.hasMoreElements()) {
            String entry = jarentries.nextElement();
            //Log.d(LOG_TAG, "Examining entry: " + entry);
            //if (entry.endsWith(".class")) {
            attemptLoadClass(entry);
            //}
        }
        jarobj.close();
    }

    private static void attemptLoadClass(String classname) {
        //String classname = entryname;//.replace('/', '.');
        //classname = classname.substring(0, classname.length() - 6);
        //Log.d(LOG_TAG, "Checking class " + classname);
        try {
            Class<?> c = classLoader.loadClass(classname);
            Object instance = c.newInstance();
            if (instance instanceof OpMode) {
                Log.i(LOG_TAG, "Found " + classname + " as an op mode!");
                opModeList.add(((OpMode)instance).getClass());
            }
        } catch (Throwable ex) {
            //Log.e(LOG_TAG, "Exception while loading " + entryname + ": ");//, ex)
            //Log.e(LOG_TAG, ex.getMessage());// ;
        }
    }

    private static URL[] getJarURLs(List<File> fileList) {
        List<URL> jarList = new ArrayList<URL>();
        for (File f : fileList) {
            if (f.isFile() && f.getName().endsWith(".jar")) {
                try {
                    jarList.add(f.getAbsoluteFile().toURI().toURL());
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
                //RobotLog.d("[Thunderbots] Found file: " + f);
            } else if (f.isDirectory()) {
                getFilesInDirectory(f, fileList);
            }
        }
    }

    private static File getTargetDirectory() {
        File sdcard = Environment.getExternalStorageDirectory();
        return new File(sdcard, FILE_LOCATION);
    }

}
