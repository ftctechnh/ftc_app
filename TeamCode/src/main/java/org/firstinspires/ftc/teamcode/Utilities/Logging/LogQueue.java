package org.firstinspires.ftc.teamcode.Utilities.Logging;

import android.os.Environment;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Hardware.BaseHardware;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ArrayBlockingQueue;

public class LogQueue {

    final String FOLDER = "FIRST/LOGS/";
    final int CAPACITY = 128;

    ArrayBlockingQueue<Object[]> queue;
    BaseHardware hardware;
    AsyncLogWriter logManager;
    FileWriter fileWriter;

    final String[] GAMEPAD_PROPERTIES = {"timestamp", "id", "left_stick_x", "left_stick_y",
            "right_stick_x", "right_stick_y", "left_trigger", "right_trigger", "dpad_up",
            "dpad_down", "dpad_left", "dpad_right", "a", "b", "x", "y", "start", "back", "guide"};

    final Field[] fields;

    int propLength;

    public LogQueue(BaseHardware hardware) throws NoSuchFieldException, IOException {
        this.hardware = hardware;
        queue = new ArrayBlockingQueue<>(CAPACITY * 2);

        fields = new Field[GAMEPAD_PROPERTIES.length];
        for (int i = 0; i < GAMEPAD_PROPERTIES.length; i++) {
            fields[i] = Gamepad.class.getField(GAMEPAD_PROPERTIES[i]);
        }

        propLength = GAMEPAD_PROPERTIES.length*2;

        fileWriter = new FileWriter(getFile(hardware));
        logManager = new AsyncLogWriter(fileWriter, queue);
        logManager.start();
    }

    public File getFile(BaseHardware hardware) {
        final File path = Environment.getExternalStorageDirectory();
        if (!path.exists()) {path.mkdirs();}

        long time = System.currentTimeMillis();
        String hardwareType = hardware.getClass().getSimpleName();
        String opMode = hardware.opMode.getClass().getSimpleName();
        String name = FOLDER + time + "-" + hardwareType + "-" + opMode + ".csv";
        return new File(path, name);

    }

    public void update() throws IllegalAccessException {
        // First, we'll write controller information, then chassis information, then sensor
        // information, then other info

        Object[] readings = new Object[propLength];
        int gamepadNum = 0;

        for (Gamepad g : new Gamepad[] {hardware.opMode.gamepad1, hardware.opMode.gamepad2}) {
            for (int i = 0; i < fields.length; i++) {
                readings[gamepadNum + i] = fields[i].get(g);
            }
            gamepadNum += 1;
        }

        queue.add(readings);
        // Will automatically update AsyncLogWriter
    }

    public void shutdown() {
        logManager.terminate();
    }
}
