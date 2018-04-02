package org.firstinspires.ftc.teamcode.TestOpModes;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.GMR.Robot.Robot;
import org.firstinspires.ftc.teamcode.GMR.Robot.SubSystems.DriveTrain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static java.security.AccessController.getContext;

/**
 * Created by pston on 4/2/2018
 */

public class TestGyro extends OpMode {

    private Robot robot;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry, false);
    }

    @Override
    public void loop() {

        if (gamepad1.a) {
            while (robot.driveTrain.gyroTurn(DriveTrain.Direction.TURNRIGHT, 0.5, 90)) {
            }
        }

    }

    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Notes");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
