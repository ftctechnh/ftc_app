package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Kaden on 2/3/18.
 */
@Autonomous(name = "Testing ramping", group = "test")
@Disabled
public class AutoTest extends LinearOpMode {
    AutoDrive drive;
    public void runOpMode() throws InterruptedException {
        drive = new AutoDrive(hardwareMap, telemetry);
        drive.init();
        waitForStart();
        drive.forward(1, 100);
    }
}
