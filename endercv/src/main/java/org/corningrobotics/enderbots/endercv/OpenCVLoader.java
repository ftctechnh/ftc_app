package org.corningrobotics.enderbots.endercv;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by guinea on 2/21/18.
 * -------------------------------------------------------------------------------------
 * Copyright (c) 2018 FTC Team 5484 Enderbots
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * 
 * By downloading, copying, installing or using the software you agree to this license.
 * If you do not agree to this license, do not download, install,
 * copy or use the software.
 * -------------------------------------------------------------------------------------
 * Credit to the OpenRC team for coming up with this general solution.
 */

public class OpenCVLoader {
    // This path may need to be changed for Samsung S5 phones; 
    // its hardcoded because idk how else to get the path without a Context.
    // Feel free to change the value to fit the phone you are using.
    private static String filesDir = "/data/user/0/com.qualcomm.ftcrobotcontroller/files";
    public static void setFilesDir(String path) {
        filesDir = path;
    }
    public static void loadOpenCV() {

        File protectedStorageLib = new File(filesDir + "/extra/libopencv_java3.so");
        File protectedExtraFolder = new File(filesDir + "/extra/");
        File internalStorageLib = new File(Environment.getExternalStorageDirectory() + "/EnderCV/libopencv_java3.so");
        if (!protectedStorageLib.exists() && internalStorageLib.exists()) {
            if (!protectedExtraFolder.exists())
                protectedExtraFolder.mkdir();

        }

        try {
            /*
             * Copy the file with a 1MiB buffer
             */
            InputStream is = new FileInputStream(internalStorageLib);
            OutputStream os = new FileOutputStream(protectedStorageLib);
            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) > 0) {
                os.write(buff, 0, len);
            }
            is.close();
            os.close();

            System.load(protectedStorageLib.getAbsolutePath());
        } catch (Exception e) {
            Log.e("EnderCV", "OpenCV Load Error: ", e);
        }

    }
}
