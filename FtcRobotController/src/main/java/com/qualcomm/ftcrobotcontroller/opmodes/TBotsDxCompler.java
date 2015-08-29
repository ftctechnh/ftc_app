package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.FtcRobotControllerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.android.dx.cf.iface.ParseException;
import com.android.dx.command.Main;

public class TBotsDxCompler {

    private static final String OUTPUT_TO = "/thunderbots/compiled";

    public static void convertJars(List<File> jarList) {
        for (int i = 0; i < jarList.size(); i++) {
            File f = jarList.get(i);
            System.out.println("in: " + f);
            System.out.println("out: " + outputFile(f));
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

    public static List<File> getJarList(List<File> fileList) {
        for (int i = fileList.size() - 1; i >= 0; i--) {
            if (!fileList.get(i).getName().endsWith(".jar")) {
                fileList.remove(i);
            }
        }
        return fileList;
    }

}
