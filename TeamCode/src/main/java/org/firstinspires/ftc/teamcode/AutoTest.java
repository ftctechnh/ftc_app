package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Kaden on 2/3/18.
 */
@Autonomous(name = "Testing cv", group = "test")
public class AutoTest extends LinearOpMode {
    Robot robot;
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("DO NOT PRESS PLAY YET"); telemetry.update();
        robot = new Robot(this);
        robot.mapRobot();
        robot.calibrateGyro();
        telemetry.addLine("NOW YOU CAN PRESS PLAY"); telemetry.update();
        waitForStart();
        robot.drive.forward(1, 5);
    }
}
