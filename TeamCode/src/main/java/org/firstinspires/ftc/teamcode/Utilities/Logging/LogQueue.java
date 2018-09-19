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

        propLength = GAMEPAD_PROPERTIES.length;

        fileWriter = new FileWriter(getFile(hardware));
        logManager = new AsyncLogWriter(fileWriter, queue);
        logManager.start();
    }

    public File getFile(BaseHardware hardware) {
        final File folder = new File(Environment.getExternalStorageDirectory().toString(), FOLDER);
        if (!folder.exists()) {folder.mkdirs();}

        long time = System.currentTimeMillis();
        String hardwareType = hardware.getClass().getSimpleName();
        String opMode = hardware.opMode.getClass().getSimpleName();
        String name = time + "-" + hardwareType + "-" + opMode + ".csv";
        return new File(folder, name);
    }

    public void update() throws IllegalAccessException {
        // First, we'll write controller information, then chassis information, then sensor
        // information, then other info

        // This is a really space inefficient way of writing data
        // If this becomes a problem, we'll need to fix it later
        Object[] readings = new Object[propLength + 8];
        int gamepadNum = 0;

        for (Gamepad g : new Gamepad[] {hardware.opMode.gamepad1}) {
            for (int i = 0; i < fields.length; i++) {
                readings[gamepadNum + i] = fields[i].get(g);
            }
        }

        for (int i = 0; i < 4; i++) {
            readings[propLength + 2*i] = hardware.bulkDataResponse.getVelocity(i);
            readings[propLength + 2*i + 1] = hardware.bulkDataResponse.getEncoder(i);
        }
        //readings[propLength+8] = bytesToHex(hardware.bulkDataResponse.toPayloadByteArray());

        queue.add(readings);
        // Will automatically update AsyncLogWriter
    }

    public void shutdown() {
        logManager.terminate();
    }
}
