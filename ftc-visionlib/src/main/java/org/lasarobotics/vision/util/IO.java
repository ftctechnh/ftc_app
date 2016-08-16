/*
 * Copyright (c) 2016 Arthur Pachachura, LASA Robotics, and contributors
 * MIT licensed
 *
 * Thank you to Ehsan Asdar (LASA).
 */

package org.lasarobotics.vision.util;

import android.os.Environment;

import org.lasarobotics.vision.android.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * File Input/Output operations
 */
class IO {
    /**
     * Write a text file on the device
     *
     * @param directory Directory to write the file into
     * @param filename  The filename, including extension
     * @param data      Data to write to file
     * @param overwrite True to overwrite file if exists, false to create new file with appended "." and integer
     */
    public static void writeTextFile(String directory, String filename, String data, boolean overwrite) {
        try {
            File f;
            f = Util.createFileOnDevice(directory, filename, overwrite);
            FileWriter fw = new FileWriter(f.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read an entire text file into a string
     *
     * @param directory Directory from which to read the file
     * @param filename  Filename with extension(s)
     * @return String containing the file - use getLines() to get the lines of the file
     */
    public static String readTextFile(String directory, String filename) {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + directory, filename);
        try {
            String str = "";
            Scanner s = new Scanner(f);
            while (s.hasNextLine())
                str += s.nextLine() + "\n";
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Seperate a string into lines split by line endings
     *
     * @param data Single string
     * @return Lines
     */
    public static String[] getLines(String data) {
        return data.split("\r?\n");
    }
}
