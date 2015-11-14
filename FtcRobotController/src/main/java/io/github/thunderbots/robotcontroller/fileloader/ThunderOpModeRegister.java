package io.github.thunderbots.robotcontroller.fileloader;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;

import java.io.File;
import java.util.List;

import io.github.thunderbots.robotcontroller.logging.ThunderLog;

public class ThunderOpModeRegister {

    /**
     * The Op Mode Manager will call this method when it wants a list of all
     * available op modes. Add your op mode to the list to enable it.
     *
     * @param manager op mode manager
     */
    public static void register(OpModeManager manager) {

        ThunderLog.i("About to register op modes...");
        try {
            List<File> fileList = OpModeClassLoader.getFileSet();
            ThunderLog.d("Preliminary fileList: " + fileList);
            DalvikConverter.getJarList(fileList);
            ThunderLog.d("Jar-only fileList: " + fileList);
            DalvikConverter.convertJars(fileList);
            ThunderLog.d("Converted fileList: " + fileList);
            List<Class<? extends OpMode>> opmodeList = OpModeClassLoader.loadJars(fileList);
            ThunderLog.d("Final opmodeList: " + opmodeList);
            ThunderLog.i("Now registering OpModes...");
            for (Class<? extends OpMode> opmode : opmodeList) {
                if (AnnotationReader.isActive(opmode)) {
                    try {
                        manager.register(AnnotationReader.getOpModeName(opmode), opmode);
                        ThunderLog.i("Registered " + opmode.getSimpleName());
                    } catch (Throwable ex) {
                        ThunderLog.e("Error registering op mode: " + opmode.getSimpleName());
                        ThunderLog.e(ex.getMessage());
                    }
                }
            }
        } catch (Throwable ex) {
            ThunderLog.e("Error reading external files:");
            if (ex instanceof Exception) {
                ex.printStackTrace();
            }
        }
    }

}
