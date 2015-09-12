/* Copyright (C) 2015 Thunderbots-5604
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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

/**
 * The {@code OpModeClassLoader} class is responsible for loading class files into instantiated
 * objects.
 *
 * @author Zach Ohara
 */
public class OpModeClassLoader {

    private static ClassLoader classLoader;
    private static List<Class<? extends OpMode>> opModeList;

    public static final String FILE_LOCATION = "FIRST";

    public static final String LOG_TAG = "Thunderbots";

    public static List<Class<? extends OpMode>> loadJars(List<File> filelist) {
        URL[] jarurls = getJarURLs(filelist);
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

    private static ClassLoader getClassLoader(URL[] jarurls) {
        String pathString = getDelimitedPathString(jarurls);
        File cacheFile = new File(FtcRobotControllerActivity.getPrivateFilesDir(), "/thunderbots/");
        cacheFile.mkdirs();
        String cacheDir = cacheFile.toString();
        ClassLoader parentLoader = OpModeClassLoader.class.getClassLoader();
        return new DexClassLoader(pathString, cacheDir, null, parentLoader);
    }

    /**
     * Returns a single string that contains the value of toString() for every object given
     * in the array, delimited by {@code java.io.File#pathSeperator}
     *
     * @param arr the array of objects.
     * @param <T> the type of the array. This doesn't really matter at all.
     * @return the concatenation of o.toString() for every object o in arr.
     */
    private static <T> String getDelimitedPathString(T[] arr) {
        String result = "";
        for (T obj : arr){
            result += File.pathSeparator;
            result += obj.toString();
        }
        return result.substring(1);
    }

    /**
     * Loads the classes contained in a given jar file. The jar file must have already been converted
     * to a dalvik-compatible form.
     *
     * @param jarfile the file to load classes from.
     * @throws IOException if an IOException is thrown by the underlying class loading system.
     */
    private static void loadJarFile(File jarfile) throws IOException {
        File cache = new File(FtcRobotControllerActivity.getPrivateFilesDir(), "/thunderbots/temp");
        DexFile jarobj = DexFile.loadDex(jarfile.getAbsolutePath(), cache.getAbsolutePath(), 0);
        Enumeration<String> jarentries = jarobj.entries();
        while (jarentries.hasMoreElements()) {
            String entry = jarentries.nextElement();
            attemptLoadClass(entry);
        }
        jarobj.close();
    }

    /**
     * Attempts to load a class with the given fully-qualified name. Any exception thrown from
     * within this method will be caught and handled (either ignored or logged).
     * <br>
     * The class will be loaded by the {@code classLoader} class variable. If the loaded class is
     * a valid, instantiable {@code OpMode}, it will be added to the {@code opModeList} class
     * variable.
     *
     * @param classname the fully-qualified name of the class to be resolved.
     */
    private static void attemptLoadClass(String classname) {
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

    /**
     * Gets a list of all the files contained within the app's directory, usually "sdcard/FIRST/"
     *
     * @return a list of all files in the apps directory.
     */
    public static List<File> getFileSet() {
        List<File> fileList = new ArrayList<File>();
        getFilesInDirectory(getTargetDirectory(), fileList);
        return fileList;
    }

    /**
     * Recursively browses the given directory and builds a list that contains every file
     * in the given directory.
     *
     * @param current the directory to look for files in.
     * @param fileList the list of all files in the given directory. This list should be
     * empty when 
     */
    private static void getFilesInDirectory(File current, List<File> fileList) {
        for (File f : current.listFiles()) {
            if (f.isFile()) {
                fileList.add(f);
            } else if (f.isDirectory()) {
                getFilesInDirectory(f, fileList);
            }
        }
    }

    /**
     * Gets the {@code File} object for the directory that should be searched for jar files.
     *
     * @return the {@code File} to search.
     */
    private static File getTargetDirectory() {
        File sdcard = Environment.getExternalStorageDirectory();
        return new File(sdcard, FILE_LOCATION);
    }

}
