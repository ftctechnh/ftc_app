package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android.dx.cf.iface.ParseException;
import com.android.dx.command.Main;

/**
 * The {@code DalvikCompiler} class contains static methods for compiling standard java
 * jar files to Dalvik-compatible jar files. This allows the VM to search and instantiate
 * classes in the jar later.
 *
 * @author Zach Ohara
 */
public class DalvikCompiler {

    /**
     * The sub-directory of the application's private data folder that is used to cache
     * the Dalvik-compiled jar files. The actual directory doesn't matter at all, as long
     * as the directory is not used for anything else.
     */
    private static final String OUTPUT_TO = "/thunderbots/compiled";

    /**
     * Compiles the given list of jar files to Dalvik-compatible jar files, and stores the compiled
     * jar files in a cache folder. As every jar file is compiled, its {@code File} in the list is
     * changed to reflect its new location. Files that cannot be opened are logged to Logcat.
     * <br>
     * The files in {@code jarList} must all be .jar files as a prerequisite.
     *
     * @param jarList the list of jar files to compile.
     */
    public static void convertJars(List<File> jarList) {
        for (int i = 0; i < jarList.size(); i++) {
            File f = jarList.get(i);
            //System.out.println("in: " + f);
            //System.out.println("out: " + outputFile(f));
            List<String> args = new ArrayList<String>();
            args.add("--dex");
            args.add("--output=" + outputFile(f).getAbsolutePath() + "");
            args.add("" + f.getAbsolutePath() + "");
            try {
                Main.main(args.toArray(new String[args.size()]));
                jarList.set(i, outputFile(f));
            } catch (ParseException ex) {
                System.out.println(f.getName() + " was probably generated using the wrong compiler!");
                System.out.println("Make sure to use Java 1.6");
            }
        }
    }

    /**
     * Generates a file path for the compiled and cached jar from the given jar file.
     *
     * @param inputFile the jar file to generate a cache file path for.
     * @return the cache file path for the given jar.
     */
    private static File outputFile(File inputFile) {
        String inputName = inputFile.getName();
        inputName = inputName.replace(" ", "-");
        File output = new File(FtcRobotControllerActivity.getPrivateFilesDir(), OUTPUT_TO);
        if (output.exists()) {
            output.delete();
        }
        output.mkdirs();
        return new File(output, "compiled-" + inputFile.getName());
    }

    /**
     * Removes all the non-jar files from the given list of {@code File} objects.
     *
     * @param fileList a list of file objects.
     * @return the list of file objects, but with all non-jar files removed.
     */
    public static List<File> getJarList(List<File> fileList) {
        for (int i = fileList.size() - 1; i >= 0; i--) {
            if (!fileList.get(i).getName().endsWith(".jar")) {
                fileList.remove(i);
            }
        }
        return fileList;
    }

}
