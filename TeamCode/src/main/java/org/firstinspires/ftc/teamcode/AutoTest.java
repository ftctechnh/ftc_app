package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Kaden on 2/3/18.
 */
@Autonomous(name = "Testing driving w/distance", group = "test")
public class AutoTest extends LinearOpMode {
    AutoDrive drive;
    public void runOpMode() throws InterruptedException {
        drive = new AutoDrive(hardwareMap, telemetry);
        drive.init();
        telemetry.addLine("ready to start");
        waitForStart();
        drive.driveLeftUntilDistance(1, 36);
    }
}
